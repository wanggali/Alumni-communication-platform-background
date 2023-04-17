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
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.mapper.CommentMapper;
import com.pzhu.acp.mapper.DiscussMapper;
import com.pzhu.acp.mapper.UserMapper;
import com.pzhu.acp.model.entity.*;
import com.pzhu.acp.model.query.GetCommentQuery;
import com.pzhu.acp.model.vo.CommentVO;
import com.pzhu.acp.service.CommentService;
import com.pzhu.acp.utils.GsonUtil;
import com.pzhu.acp.utils.LockHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【comment】的数据库操作Service实现
 * @createDate 2022-11-29 21:42:10
 */
@Service
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private DiscussMapper discussMapper;

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
    public boolean addComment(Comment comment) {
        //检查文章、用户是否正常
        checkDiscussAndUser(comment);
        int operationNum = commentMapper.insert(comment);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("添加评论失败,该评论参数为:{}", GsonUtil.toJson(comment));
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
        return Boolean.TRUE;
    }

    private void checkDiscussAndUser(Comment comment) {
        //查询该条文章是否存在
        QueryWrapper<Discuss> discussMapperQueryWrapper = new QueryWrapper<>();
        discussMapperQueryWrapper.eq("id", comment.getDid());
        Long count = discussMapper.selectCount(discussMapperQueryWrapper);
        if (count == OperationConstant.COUNT_NUM) {
            log.warn("该讨论不存在，该讨论id为：{}", GsonUtil.toJson(comment.getDid()));
            throw new BusinessException(ErrorCode.NO_EXISTED_DATA);
        }
        //查询该用户是否存在
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", comment.getUid());
        Long userCount = userMapper.selectCount(userQueryWrapper);
        if (userCount == OperationConstant.COUNT_NUM) {
            log.warn("该用户不存在，该用户id为：{}", GsonUtil.toJson(comment.getUid()));
            throw new BusinessException(ErrorCode.NO_EXISTED_DATA);
        }
    }

    @Override
    public boolean updateComment(Comment comment) {
        lockHelper.tryLock(LockConstant.COMMENT_BASE_ADD_LOCK + comment.getId(),
                1000,
                1000,
                TimeUnit.MILLISECONDS, () -> {
                    //查询该条评论是否存在
                    checkComment(comment);
                    //获取redis中是否有该回复id对应的点赞数
                    String commentUp = (String) redisTemplate.opsForValue().get(RedisConstant.COMMENT_BASE_UP_KEY + SPLIT_SYMBOL + comment.getId());

                    if (StringUtils.isBlank(commentUp)) {
                        log.info("当前不存在点赞数，直接加入Redis中，回复id为：{}", comment.getId());
                        //获取数据库里面的点赞数
                        Comment oldDiscuss = commentMapper.selectById(comment.getId());
                        Integer oldUpNum = oldDiscuss.getUp();
                        Boolean isMember = redisTemplate.opsForSet().isMember(RedisConstant.COMMENT_UP_USER_IDS + SPLIT_SYMBOL + comment.getId(),
                                GsonUtil.toJson(comment.getUid()));
                        if (BooleanUtil.isTrue(isMember)) {
                            int newDiscussUp = oldUpNum - 1;
                            redisTemplate.opsForValue().set(RedisConstant.COMMENT_BASE_UP_KEY + SPLIT_SYMBOL + comment.getId(), GsonUtil.toJson(newDiscussUp));
                            redisTemplate.opsForSet().remove(RedisConstant.COMMENT_UP_USER_IDS + SPLIT_SYMBOL + comment.getId(), GsonUtil.toJson(comment.getUid()));
                        } else {
                            Integer up = oldUpNum + 1;
                            redisTemplate.opsForValue().set(RedisConstant.COMMENT_BASE_UP_KEY + SPLIT_SYMBOL + comment.getId(),
                                    GsonUtil.toJson(up));
                            redisTemplate.opsForSet().add(RedisConstant.COMMENT_UP_USER_IDS + SPLIT_SYMBOL + comment.getId(),
                                    GsonUtil.toJson(comment.getUid()));
                        }
                    } else {
                        Boolean isMember = redisTemplate.opsForSet().isMember(RedisConstant.COMMENT_UP_USER_IDS + SPLIT_SYMBOL + comment.getId(),
                                GsonUtil.toJson(comment.getUid()));
                        if (BooleanUtil.isTrue(isMember)) {
                            int newDiscussUp = Integer.parseInt(commentUp) - 1;
                            redisTemplate.opsForValue().set(RedisConstant.COMMENT_BASE_UP_KEY + SPLIT_SYMBOL + comment.getId(), GsonUtil.toJson(newDiscussUp));
                            redisTemplate.opsForSet().remove(RedisConstant.COMMENT_UP_USER_IDS + SPLIT_SYMBOL + comment.getId(), GsonUtil.toJson(comment.getUid()));
                        } else {
                            int newDiscussUp = Integer.parseInt(commentUp) + 1;
                            redisTemplate.opsForValue().set(RedisConstant.COMMENT_BASE_UP_KEY + SPLIT_SYMBOL + comment.getId(), GsonUtil.toJson(newDiscussUp));
                            redisTemplate.opsForSet().add(RedisConstant.COMMENT_UP_USER_IDS + SPLIT_SYMBOL + comment.getId(), GsonUtil.toJson(comment.getUid()));
                        }
                    }
                });
        //定时任务每一分钟统计并写入数据库中
        return Boolean.TRUE;
    }

    private void checkComment(Comment comment) {
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("id", comment.getId());
        Long count = commentMapper.selectCount(commentQueryWrapper);
        if (count == OperationConstant.COUNT_NUM) {
            log.warn("该评论不存在，该讨论id为：{}", GsonUtil.toJson(comment.getId()));
            throw new BusinessException(ErrorCode.NO_EXISTED_DATA);
        }
    }

    @Override
    public boolean deleteComment(List<Long> ids) {
        ids.forEach(id -> {
            //查询该条评论是否存在
            Comment comment = new Comment();
            comment.setId(id);
            checkComment(comment);
            int operationNum = commentMapper.deleteById(id);
            if (operationNum == OperationConstant.OPERATION_NUM) {
                log.warn("删除评论失败,该评论id为:{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.DELETE_ERROR);
            }
        });
        return Boolean.TRUE;
    }

    @Override
    public Map<String, Object> getCommentByPageOrParam(GetCommentQuery getCommentQuery) {
        //判断当前谈论是否存在
        QueryWrapper<Discuss> discussQueryWrapper = new QueryWrapper<>();
        discussQueryWrapper.eq("id", getCommentQuery.getDid());
        Long count = discussMapper.selectCount(discussQueryWrapper);
        if (count == OperationConstant.COUNT_NUM) {
            log.warn("该评论不存在，该评论id为：{}", GsonUtil.toJson(getCommentQuery.getDid()));
            throw new BusinessException(ErrorCode.NO_EXISTED_DATA);
        }
        Page<CommentVO> commentVOPage = new Page<>(getCommentQuery.getPageNum(), getCommentQuery.getPageSize());
        IPage<CommentVO> result = commentMapper.selectCommentByPageOrParam(commentVOPage, getCommentQuery);
        List<CommentVO> records = result.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            log.info("当前数据为空，该讨论id为：{}", GsonUtil.toJson(getCommentQuery.getDid()));
            return Maps.newHashMap();
        }
        records.forEach(item -> {
            item.setCreateTime(new Date(item.getCreateTime().getTime()));
            item.getUserInfo().setJoinTime(new Date(item.getUserInfo().getJoinTime().getTime()));
            item.getReplyInfo().forEach(param -> {
                param.setCreateTime(new Date(param.getCreateTime().getTime()));
            });
            if (StpUtil.isLogin()) {
                item.setIsUp(checkUserIsUp(StpUtil.getLoginIdAsLong(), RedisConstant.COMMENT_UP_USER_IDS + SPLIT_SYMBOL + item.getId()));
            }
            item.getReplyInfo().forEach(reply -> {
                if (StpUtil.isLogin()) {
                    reply.setIsUp(checkUserIsUp(StpUtil.getLoginIdAsLong(), RedisConstant.REPLY_UP_USER_IDS + SPLIT_SYMBOL + reply.getId()));
                }
            });
        });
        //增加用户名字模糊匹配，学院，地区精准查询
        if (StringUtils.isNotBlank(getCommentQuery.getUserName())) {
            records = records.stream().filter(item -> item.getUserInfo().getUserName().contains(getCommentQuery.getUserName())).collect(Collectors.toList());
        }
        if (getCommentQuery.getCollegeId() != null) {
            records = records.stream().filter(item -> item.getUserInfo().getCollegeId().equals(getCommentQuery.getCollegeId())).collect(Collectors.toList());
        }
        if (getCommentQuery.getRegionId() != null) {
            records = records.stream().filter(item -> item.getUserInfo().getRegionId().equals(getCommentQuery.getRegionId())).collect(Collectors.toList());
        }
        Map<String, Object> map = Maps.newHashMap();
        map.put("items", records);
        map.put("current", result.getCurrent());
        map.put("pages", result.getPages());
        map.put("size", result.getSize());
        map.put("total", result.getTotal());
        return map;
    }

    private Boolean checkUserIsUp(Long userId, String redisKey) {
        if (userId != null && redisTemplate.keys(redisKey) != null) {
            return redisTemplate.opsForSet().isMember(redisKey, GsonUtil.toJson(userId));
        }
        return Boolean.FALSE;
    }
}




