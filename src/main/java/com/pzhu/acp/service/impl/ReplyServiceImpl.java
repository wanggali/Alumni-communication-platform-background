package com.pzhu.acp.service.impl;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Sets;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.constant.LockConstant;
import com.pzhu.acp.constant.OperationConstant;
import com.pzhu.acp.constant.RedisConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.mapper.CommentMapper;
import com.pzhu.acp.mapper.DiscussMapper;
import com.pzhu.acp.mapper.UserMapper;
import com.pzhu.acp.model.entity.*;
import com.pzhu.acp.service.ReplyService;
import com.pzhu.acp.mapper.ReplyMapper;
import com.pzhu.acp.utils.GsonUtil;
import com.pzhu.acp.utils.LockHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.opc.PackageRelationshipTypes;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @description 针对表【reply】的数据库操作Service实现
 * @createDate 2022-11-29 21:42:15
 */
@Service
@Slf4j
public class ReplyServiceImpl extends ServiceImpl<ReplyMapper, Reply>
        implements ReplyService {

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
    public boolean addReply(Reply reply) {
        //判断该评论是否存在
        QueryWrapper<Comment> discussQueryWrapper = new QueryWrapper<>();
        discussQueryWrapper.eq("id", reply.getCid());
        Long count = commentMapper.selectCount(discussQueryWrapper);
        if (count == OperationConstant.COUNT_NUM) {
            log.warn("评论不存在，该评论id为：{}", GsonUtil.toJson(reply.getCid()));
            throw new BusinessException(ErrorCode.NO_EXISTED_DATA);
        }
        //判断用户是否存在
        checkUserInfo(reply.getUid());
        checkUserInfo(reply.getReplyId());
        int operationNum = replyMapper.insert(reply);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("添加回复失败,该回复参数为:{}", GsonUtil.toJson(reply));
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
        return Boolean.TRUE;
    }

    private void checkUserInfo(Long id) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", id);
        Long commentUserCount = userMapper.selectCount(userQueryWrapper);
        if (commentUserCount == OperationConstant.COUNT_NUM) {
            log.warn("用户不存在，该用户id为：{}", GsonUtil.toJson(id));
            throw new BusinessException(ErrorCode.NO_EXISTED_DATA);
        }
    }

    @Override
    public boolean updateUpNum(Reply reply) {
        lockHelper.tryLock(LockConstant.REPLY_BASE_ADD_LOCK + reply.getId(),
                1000,
                1000,
                TimeUnit.MILLISECONDS, () -> {
                    //获取redis中是否有该回复id对应的点赞数
                    String replyUp = (String) redisTemplate.opsForValue().get(RedisConstant.REPLY_BASE_UP_KEY + SPLIT_SYMBOL + reply.getId());

                    if (StringUtils.isBlank(replyUp)) {
                        log.info("当前不存在点赞数，直接加入Redis中，回复id为：{}", reply.getId());
                        //获取数据库里面的点赞数
                        Reply oldDiscuss = replyMapper.selectById(reply.getId());
                        Integer oldUpNum = oldDiscuss.getUp();
                        Boolean isMember = redisTemplate.opsForSet().isMember(RedisConstant.REPLY_UP_USER_IDS + SPLIT_SYMBOL + reply.getId(),
                                GsonUtil.toJson(reply.getUid()));
                        if (BooleanUtil.isTrue(isMember)) {
                            int newDiscussUp = oldUpNum - 1;
                            redisTemplate.opsForValue().set(RedisConstant.REPLY_BASE_UP_KEY + SPLIT_SYMBOL + reply.getId(), GsonUtil.toJson(newDiscussUp));
                            redisTemplate.opsForSet().remove(RedisConstant.REPLY_UP_USER_IDS + SPLIT_SYMBOL + reply.getId(), GsonUtil.toJson(reply.getUid()));
                        } else {
                            Integer up = oldUpNum + 1;
                            redisTemplate.opsForValue().set(RedisConstant.REPLY_BASE_UP_KEY + SPLIT_SYMBOL + reply.getId(),
                                    GsonUtil.toJson(up));
                            redisTemplate.opsForSet().add(RedisConstant.REPLY_UP_USER_IDS + SPLIT_SYMBOL + reply.getId(),
                                    GsonUtil.toJson(reply.getUid()));
                        }
                    } else {
                        Boolean isMember = redisTemplate.opsForSet().isMember(RedisConstant.REPLY_UP_USER_IDS + SPLIT_SYMBOL + reply.getId(),
                                GsonUtil.toJson(reply.getUid()));
                        if (BooleanUtil.isTrue(isMember)) {
                            int newDiscussUp = Integer.parseInt(replyUp) - 1;
                            redisTemplate.opsForValue().set(RedisConstant.REPLY_BASE_UP_KEY + SPLIT_SYMBOL + reply.getId(), GsonUtil.toJson(newDiscussUp));
                            redisTemplate.opsForSet().remove(RedisConstant.REPLY_UP_USER_IDS + SPLIT_SYMBOL + reply.getId(), GsonUtil.toJson(reply.getUid()));
                        } else {
                            int newDiscussUp = Integer.parseInt(replyUp) + 1;
                            redisTemplate.opsForValue().set(RedisConstant.REPLY_BASE_UP_KEY + SPLIT_SYMBOL + reply.getId(), GsonUtil.toJson(newDiscussUp));
                            redisTemplate.opsForSet().add(RedisConstant.REPLY_UP_USER_IDS + SPLIT_SYMBOL + reply.getId(), GsonUtil.toJson(reply.getUid()));
                        }
                    }
                });
        //定时任务每3分钟统计并写入数据库中
        return Boolean.TRUE;
    }

    @Override
    public boolean deleteReply(List<Long> ids) {
        ids.forEach(id -> {
            QueryWrapper<Reply> replyQueryWrapper = new QueryWrapper<>();
            replyQueryWrapper.eq("id", id);
            Long count = replyMapper.selectCount(replyQueryWrapper);
            if (count == OperationConstant.COUNT_NUM) {
                log.warn("回复不存在，该回复id为：{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.NO_EXISTED_DATA);
            }
            int operationNum = replyMapper.deleteById(id);
            if (operationNum == OperationConstant.OPERATION_NUM) {
                log.warn("删除回复失败,该回复id为:{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.DELETE_ERROR);
            }
        });
        return Boolean.TRUE;
    }
}




