package com.pzhu.acp.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.constant.LockConstant;
import com.pzhu.acp.constant.OperationConstant;
import com.pzhu.acp.constant.RedisConstant;
import com.pzhu.acp.enums.TagsEnum;
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
import com.pzhu.acp.utils.LockHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
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

    @Resource
    private LockHelper lockHelper;

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
    public boolean upOrDownAction(Discuss discuss, String flag) {
        lockHelper.tryLock(LockConstant.DISCUSS_BASE_ADD_LOCK + discuss.getId() + flag,
                1000,
                1000,
                TimeUnit.MILLISECONDS, () -> {
                    //查询该讨论是否存在
                    checkDiscussExisted(discuss);
                    //获取redis中是否有该回复id对应的点赞数
                    String discussUp = (String) redisTemplate.opsForValue().get(RedisConstant.DISCUSS_BASE_UP_KEY + SPLIT_SYMBOL + discuss.getId());
                    String discussDown = (String) redisTemplate.opsForValue().get(RedisConstant.DISCUSS_DOWN_KEY + SPLIT_SYMBOL + discuss.getId());

                    if (TagsEnum.getOrderTypeEnum(flag).equals(TagsEnum.UP)) {
                        if (StringUtils.isBlank(discussUp)) {
                            log.info("当前不存在点赞数，直接加入Redis中，回复id为：{}", discuss.getId());
                            //获取数据库里面的点赞数
                            Discuss oldDiscuss = discussMapper.selectById(discuss.getId());
                            Integer oldUpNum = oldDiscuss.getUp();
                            Boolean isMember = redisTemplate.opsForSet().isMember(RedisConstant.DISCUSS_UP_USER_IDS + SPLIT_SYMBOL + discuss.getId(),
                                    GsonUtil.toJson(discuss.getUid()));
                            if (BooleanUtil.isTrue(isMember)) {
                                int newDiscussUp = oldUpNum - 1;
                                redisTemplate.opsForValue().set(RedisConstant.DISCUSS_BASE_UP_KEY + SPLIT_SYMBOL + discuss.getId(), GsonUtil.toJson(newDiscussUp));
                                redisTemplate.opsForSet().remove(RedisConstant.DISCUSS_UP_USER_IDS + SPLIT_SYMBOL + discuss.getId(), GsonUtil.toJson(discuss.getUid()));
                            } else {
                                Integer up = oldUpNum + 1;
                                redisTemplate.opsForValue().set(RedisConstant.DISCUSS_BASE_UP_KEY + SPLIT_SYMBOL + discuss.getId(),
                                        GsonUtil.toJson(up));
                                redisTemplate.opsForSet().add(RedisConstant.DISCUSS_UP_USER_IDS + SPLIT_SYMBOL + discuss.getId(),
                                        GsonUtil.toJson(discuss.getUid()));
                            }
                        } else {
                            Boolean isMember = redisTemplate.opsForSet().isMember(RedisConstant.DISCUSS_UP_USER_IDS + SPLIT_SYMBOL + discuss.getId(),
                                    GsonUtil.toJson(discuss.getUid()));
                            if (BooleanUtil.isTrue(isMember)) {
                                int newDiscussUp = Integer.parseInt(discussUp) - 1;
                                redisTemplate.opsForValue().set(RedisConstant.DISCUSS_BASE_UP_KEY + SPLIT_SYMBOL + discuss.getId(), GsonUtil.toJson(newDiscussUp));
                                redisTemplate.opsForSet().remove(RedisConstant.DISCUSS_UP_USER_IDS + SPLIT_SYMBOL + discuss.getId(), GsonUtil.toJson(discuss.getUid()));
                            } else {
                                int newDiscussUp = Integer.parseInt(discussUp) + 1;
                                redisTemplate.opsForValue().set(RedisConstant.DISCUSS_BASE_UP_KEY + SPLIT_SYMBOL + discuss.getId(), GsonUtil.toJson(newDiscussUp));
                                redisTemplate.opsForSet().add(RedisConstant.DISCUSS_UP_USER_IDS + SPLIT_SYMBOL + discuss.getId(), GsonUtil.toJson(discuss.getUid()));
                            }
                        }
                        return;
                    }

                    if (StringUtils.isBlank(discussDown)) {
                        log.info("当前不存在点赞数，直接加入Redis中，回复id为：{}", discuss.getId());
                        //获取数据库里面的点赞数
                        Discuss oldDiscuss = discussMapper.selectById(discuss.getId());
                        Integer oldDownNum = oldDiscuss.getDown();
                        Boolean isMember = redisTemplate.opsForSet().isMember(RedisConstant.DISCUSS_DOWN_USER_IDS + SPLIT_SYMBOL + discuss.getId(),
                                GsonUtil.toJson(discuss.getUid()));
                        if (BooleanUtil.isTrue(isMember)) {
                            int newDiscussUp = oldDownNum - 1;
                            redisTemplate.opsForValue().set(RedisConstant.DISCUSS_DOWN_KEY + SPLIT_SYMBOL + discuss.getId(), GsonUtil.toJson(newDiscussUp));
                            redisTemplate.opsForSet().remove(RedisConstant.DISCUSS_DOWN_USER_IDS + SPLIT_SYMBOL + discuss.getId(), GsonUtil.toJson(discuss.getUid()));
                        } else {
                            Integer up = oldDownNum + 1;
                            redisTemplate.opsForValue().set(RedisConstant.DISCUSS_DOWN_KEY + SPLIT_SYMBOL + discuss.getId(), GsonUtil.toJson(up));
                            redisTemplate.opsForSet().add(RedisConstant.DISCUSS_DOWN_USER_IDS + SPLIT_SYMBOL + discuss.getId(), GsonUtil.toJson(discuss.getUid()));
                        }
                    } else {
                        Boolean isMember = redisTemplate.opsForSet().isMember(RedisConstant.DISCUSS_DOWN_USER_IDS + SPLIT_SYMBOL + discuss.getId(),
                                GsonUtil.toJson(discuss.getUid()));
                        if (BooleanUtil.isTrue(isMember)) {
                            int newDiscussUp = Integer.parseInt(discussDown) - 1;
                            redisTemplate.opsForValue().set(RedisConstant.DISCUSS_DOWN_KEY + SPLIT_SYMBOL + discuss.getId(), GsonUtil.toJson(newDiscussUp));
                            redisTemplate.opsForSet().remove(RedisConstant.DISCUSS_DOWN_USER_IDS + SPLIT_SYMBOL + discuss.getId(), GsonUtil.toJson(discuss.getUid()));
                        } else {
                            int newDiscussUp = Integer.parseInt(discussDown) + 1;
                            redisTemplate.opsForValue().set(RedisConstant.DISCUSS_DOWN_KEY + SPLIT_SYMBOL + discuss.getId(), GsonUtil.toJson(newDiscussUp));
                            redisTemplate.opsForSet().add(RedisConstant.DISCUSS_DOWN_USER_IDS + SPLIT_SYMBOL + discuss.getId(), GsonUtil.toJson(discuss.getUid()));
                        }
                    }
                }
        );
        //定时任务每3分钟统计并写入数据库中
        return Boolean.TRUE;
    }

    @Override
    public Map<String, Object> getDiscussByPageOrParam(GetDiscussByPageQuery getDiscussByPageQuery) {
        Page<DiscussVO> discussPage = new Page<>(getDiscussByPageQuery.getPageNum(), getDiscussByPageQuery.getPageSize());
        IPage<DiscussVO> result = discussMapper.selectDiscussByPageOrParam(discussPage, getDiscussByPageQuery);
        List<DiscussVO> records = result.getRecords();

        records.forEach(item -> {
            item.setCreateTime(new Date(item.getCreateTime().getTime()));
            if (StpUtil.isLogin()) {
                item.setIsUp(checkUserIsUp(StpUtil.getLoginIdAsLong(), RedisConstant.DISCUSS_UP_USER_IDS + SPLIT_SYMBOL + item.getId()));
                item.setIsDown(checkUserIsUp(StpUtil.getLoginIdAsLong(), RedisConstant.DISCUSS_DOWN_USER_IDS + SPLIT_SYMBOL + item.getId()));
            }
        });

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

    @Override
    public DiscussVO getDiscussInfoById(Long id) {
        Discuss discuss = new Discuss();
        discuss.setId(id);
        checkDiscussExisted(discuss);
        DiscussVO discussInfoById = discussMapper.getDiscussInfoById(id);
        discussInfoById.setCreateTime(new Date(discussInfoById.getCreateTime().getTime()));
        if (StpUtil.isLogin()) {
            discussInfoById.setIsUp(checkUserIsUp(StpUtil.getLoginIdAsLong(), RedisConstant.DISCUSS_UP_USER_IDS + SPLIT_SYMBOL + discussInfoById.getId()));
            discussInfoById.setIsDown(checkUserIsUp(StpUtil.getLoginIdAsLong(), RedisConstant.DISCUSS_DOWN_USER_IDS + SPLIT_SYMBOL + discussInfoById.getId()));
        }
        return discussInfoById;
    }

    private Boolean checkUserIsUp(Long userId, String redisKey) {
        if (userId != null && redisTemplate.keys(redisKey) != null) {
            return redisTemplate.opsForSet().isMember(redisKey, GsonUtil.toJson(userId));
        }
        return Boolean.FALSE;
    }
}




