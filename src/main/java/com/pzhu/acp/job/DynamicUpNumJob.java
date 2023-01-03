package com.pzhu.acp.job;

import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.constant.OperationConstant;
import com.pzhu.acp.constant.RedisConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.mapper.DiscussMapper;
import com.pzhu.acp.mapper.DynamicMapper;
import com.pzhu.acp.model.entity.Discuss;
import com.pzhu.acp.model.entity.Dynamic;
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
public class DynamicUpNumJob {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private DynamicMapper dynamicMapper;

    private static final String SPLIT_SYMBOL = ":";

    /**
     * 每3分钟，清理一下所有点赞数
     */
    @Scheduled(cron = "0 0/3 * * * ?")
    public void addDynamicUpNum() {
        log.info("动态点赞定时任务开始工作");
        //获取回复下的全部set集合
        Set<Object> members = redisTemplate.opsForSet().members(RedisConstant.DYNAMIC_BASE_UP_KEY);
        if (members != null) {
            if (members.isEmpty()) {
                log.info("该集合为空，redis的Key为：{}", RedisConstant.DYNAMIC_BASE_UP_KEY);
                return;
            }
            //不为空进行解析value并更新数据库
            members.forEach(item -> {
                String[] split = String.valueOf(item).split(SPLIT_SYMBOL);
                Long id = Long.valueOf(split[0]);
                Integer up = Integer.valueOf(split[1]);
                Dynamic discuss = new Dynamic();
                discuss.setId(id);
                discuss.setUp(up);
                int operationNum = dynamicMapper.updateById(discuss);
                if (operationNum == OperationConstant.OPERATION_NUM) {
                    log.warn("更新点赞数失败,该动态参数为:{}", GsonUtil.toJson(discuss));
                    throw new BusinessException(ErrorCode.UPDATE_ERROR);
                }
            });
            redisTemplate.delete(RedisConstant.DYNAMIC_BASE_UP_KEY);
        }
    }
}
