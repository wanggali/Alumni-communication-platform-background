package com.pzhu.acp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.constant.CommonConstant;
import com.pzhu.acp.constant.OperationConstant;
import com.pzhu.acp.constant.RedisConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.mapper.CommentMapper;
import com.pzhu.acp.mapper.DiscussMapper;
import com.pzhu.acp.mapper.ReplyMapper;
import com.pzhu.acp.mapper.UserMapper;
import com.pzhu.acp.model.entity.Comment;
import com.pzhu.acp.model.entity.Discuss;
import com.pzhu.acp.model.entity.Reply;
import com.pzhu.acp.model.entity.User;
import com.pzhu.acp.model.query.GetDiscussByPageQuery;
import com.pzhu.acp.model.vo.DiscussVO;
import com.pzhu.acp.service.DiscussService;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【discuss】的数据库操作Service实现
 * @createDate 2022-11-21 21:34:27
 */
@Service
@Slf4j
public class DiscussServiceImpl extends ServiceImpl<DiscussMapper, Discuss>
        implements DiscussService {

    @Resource
    private DiscussMapper discussMapper;

    @Resource
    private ReplyMapper replyMapper;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 分隔符
     */
    private static final String SPLIT_SYMBOL = ":";

    @Override
    public boolean addDiscuss(Discuss discuss) {
        //检查标题是否重名
        checkDiscussTitle(discuss);
        //检查用户是否存在
        checkUserExisted(discuss);
        int operationNum = discussMapper.insert(discuss);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("添加组织失败,该讨论参数为:{}", GsonUtil.toJson(discuss));
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
        return Boolean.TRUE;
    }

    private void checkUserExisted(Discuss discuss) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", discuss.getUid());
        Long count = userMapper.selectCount(userQueryWrapper);
        if (count == OperationConstant.COUNT_NUM) {
            log.warn("该用户不存在，该用户id为：{}", GsonUtil.toJson(discuss.getUid()));
            throw new BusinessException(ErrorCode.NO_EXISTED_USER);
        }
    }

    private void checkDiscussTitle(Discuss discuss) {
        QueryWrapper<Discuss> discussQueryWrapper = new QueryWrapper<>();
        discussQueryWrapper.eq("title", discuss.getTitle());
        Long count = discussMapper.selectCount(discussQueryWrapper);
        if (count > OperationConstant.COUNT_NUM) {
            log.warn("讨论标题已重复，该标题为：{}", GsonUtil.toJson(discuss.getTitle()));
            throw new BusinessException(ErrorCode.EXISTED_NAME);
        }
    }

    @Override
    public boolean updateDiscuss(Discuss discuss) {
        //查询该讨论是否存在
        checkDiscussExisted(discuss);
        //检查用户是否存在
        checkUserExisted(discuss);
        int operationNum = discussMapper.updateById(discuss);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("更新讨论失败,讨论参数为：{}", GsonUtil.toJson(discuss));
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
        }
        return Boolean.TRUE;
    }

    private void checkDiscussExisted(Discuss discuss) {
        QueryWrapper<Discuss> discussQueryWrapper = new QueryWrapper<>();
        discussQueryWrapper.eq("id", discuss.getId());
        Long count = discussMapper.selectCount(discussQueryWrapper);
        if (count == OperationConstant.COUNT_NUM) {
            log.warn("该讨论不存在，该讨论id为：{}", GsonUtil.toJson(discuss.getId()));
            throw new BusinessException(ErrorCode.EXISTED_DISCUSS);
        }
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, BusinessException.class})
    public boolean deleteDiscuss(List<Long> ids) {
        ids.forEach(id -> {
            //查询该讨论是否存在
            QueryWrapper<Discuss> discussQueryWrapper = new QueryWrapper<>();
            discussQueryWrapper.eq("id", id);
            Long count = discussMapper.selectCount(discussQueryWrapper);
            if (count == OperationConstant.COUNT_NUM) {
                log.warn("该讨论不存在，该讨论id为：{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.EXISTED_DISCUSS);
            }
            //删除该条讨论
            int operationNum = discussMapper.deleteById(id);
            if (operationNum == OperationConstant.OPERATION_NUM) {
                log.warn("删除讨论失败，该讨论id为：{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.DELETE_ERROR);
            }
            //删除讨论下的评论和回复
            QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
            commentQueryWrapper.eq("did", id);
            int commentOperationNum = commentMapper.delete(commentQueryWrapper);
            if (commentOperationNum == OperationConstant.OPERATION_NUM) {
                log.warn("删除评论失败，该讨论id为：{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.DELETE_ERROR);
            }
            QueryWrapper<Reply> replyQueryWrapper = new QueryWrapper<>();
            replyQueryWrapper.eq("did", id);
            int replyOperationNum = replyMapper.delete(replyQueryWrapper);
            if (replyOperationNum == OperationConstant.OPERATION_NUM) {
                log.warn("删除回复失败，该讨论id为：{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.DELETE_ERROR);
            }
        });
        return Boolean.TRUE;
    }

    @Override
    public boolean upOrDownAction(Discuss discuss) {
        //查询该讨论是否存在
        checkDiscussExisted(discuss);
        //获取数据库里面的点赞数
        Discuss oldDiscuss = discussMapper.selectById(discuss.getId());
        Integer oldUpNum = oldDiscuss.getUp();
        //获取redis中是否有该回复id对应的点赞数
        String discussKey = RedisConstant.DISCUSS_BASE_UP_KEY;
        Set<Object> set = redisTemplate.opsForSet().members(discussKey);
        if (CollectionUtils.isEmpty(set)) {
            log.info("当前不存在点赞数，直接加入Redis中，回复id为：{}", discuss.getId());
            Integer up = oldUpNum + discuss.getUp();
            redisTemplate.opsForSet().add(discussKey, discuss.getId() + SPLIT_SYMBOL + up);
            return Boolean.TRUE;
        }
        set.forEach(item -> {
            String[] split = item.toString().split(SPLIT_SYMBOL);
            if (Long.valueOf(split[0]).equals(discuss.getId())) {
                redisTemplate.opsForSet().remove(discussKey, item);
                Integer newUpNum = oldUpNum + Integer.parseInt(split[1]) + discuss.getUp();
                item = discuss.getId() + SPLIT_SYMBOL + newUpNum;
                redisTemplate.opsForSet().add(discussKey, item);
            }
        });
        //定时任务每3分钟统计并写入数据库中
        return Boolean.TRUE;
    }

    @Override
    public Map<String, Object> getDiscussByPageOrParam(GetDiscussByPageQuery getDiscussByPageQuery) {
        Page<DiscussVO> discussPage = new Page<>(getDiscussByPageQuery.getPageNum(), getDiscussByPageQuery.getPageSize());
        IPage<DiscussVO> result = discussMapper.selectDiscussByPageOrParam(discussPage, getDiscussByPageQuery);
        List<DiscussVO> records = result.getRecords();
        records.forEach(item -> item.setCreateTime(new Date(item.getCreateTime().getTime())));
        if (getDiscussByPageQuery.getIsAuditType() != null) {
            records = records.stream().filter(item -> item.getIsAudit().equals(getDiscussByPageQuery.getIsAuditType())).collect(Collectors.toList());
        }
        Map<String, Object> map = Maps.newHashMap();
        map.put("items", records);
        map.put("current", result.getCurrent());
        map.put("pages", result.getPages());
        map.put("size", result.getSize());
        map.put("total", result.getTotal());
        return map;
    }
}




