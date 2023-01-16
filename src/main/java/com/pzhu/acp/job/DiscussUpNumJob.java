package com.pzhu.acp.job;

import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.constant.OperationConstant;
import com.pzhu.acp.constant.RedisConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.mapper.DiscussMapper;
import com.pzhu.acp.mapper.ReplyMapper;
import com.pzhu.acp.model.entity.Discuss;
import com.pzhu.acp.model.entity.Reply;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @Auther: gali
 * @Date: 2022-11-30 23:25
 * @Description:
 */
@Slf4j
@Component
public class DiscussUpNumJob {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private DiscussMapper discussMapper;

    private static final String SPLIT_SYMBOL = ":";

    /**
     * 每一分钟，清理一下所有点赞数
     */
    @Scheduled(cron = "0 0/3 * * * ?")
    public void addDiscussUpNum() {
        log.info("帖子点赞定时任务开始工作");
        //获取帖子下的全部set集合
        Set<Object> members = redisTemplate.opsForSet().members(RedisConstant.DISCUSS_BASE_UP_KEY);
        Set<Object> downMembers = redisTemplate.opsForSet().members(RedisConstant.DISCUSS_DOWN_UP_KEY);
        checkDiscussUp(members);
        checkDiscussDown(downMembers);
    }

    private void checkDiscussDown(Set<Object> downMembers) {
        if (downMembers != null) {
            if (downMembers.isEmpty()) {
                log.info("该集合为空，redis的Key为：{}", RedisConstant.DISCUSS_DOWN_UP_KEY);
                return;
            }
            //不为空进行解析value并更新数据库
            downMembers.forEach(item -> {
                String[] split = String.valueOf(item).split(SPLIT_SYMBOL);
                Long id = Long.valueOf(split[0]);
                Integer down = Integer.valueOf(split[1]);
                Discuss discuss = new Discuss();
                discuss.setId(id);
                discuss.setDown(down);
                int operationNum = discussMapper.updateById(discuss);
                if (operationNum == OperationConstant.OPERATION_NUM) {
                    log.warn("更新点赞数失败,该回复参数为:{}", GsonUtil.toJson(discuss));
                    throw new BusinessException(ErrorCode.UPDATE_ERROR);
                }
            });
            redisTemplate.delete(RedisConstant.DISCUSS_DOWN_UP_KEY);
        }
    }

    private void checkDiscussUp(Set<Object> members) {
        if (members != null) {
            if (members.isEmpty()) {
                log.info("该集合为空，redis的Key为：{}", RedisConstant.DISCUSS_BASE_UP_KEY);
                return;
            }
            //不为空进行解析value并更新数据库
            members.forEach(item -> {
                String[] split = String.valueOf(item).split(SPLIT_SYMBOL);
                Long id = Long.valueOf(split[0]);
                Integer up = Integer.valueOf(split[1]);
                Discuss discuss = new Discuss();
                discuss.setId(id);
                discuss.setUp(up);
                int operationNum = discussMapper.updateById(discuss);
                if (operationNum == OperationConstant.OPERATION_NUM) {
                    log.warn("更新点赞数失败,该回复参数为:{}", GsonUtil.toJson(discuss));
                    throw new BusinessException(ErrorCode.UPDATE_ERROR);
                }
            });
            redisTemplate.delete(RedisConstant.DISCUSS_BASE_UP_KEY);
        }
    }
}
