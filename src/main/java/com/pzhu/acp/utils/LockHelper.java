package com.pzhu.acp.utils;

import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: gali
 * @Date: 2023-04-17 22:04
 * @Description:
 */
@Slf4j
public class LockHelper {

    private static final RedissonClient REDISSON_CLIENT = SpringContextUtil.getBean(RedissonClient.class);

    /**
     * 尝试加分布式锁
     *
     * @param lockKey
     * @param waitTime
     * @param leaseTime
     * @param unit
     * @param runnable
     */
    public static void tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit, Runnable runnable) {
        RLock lock = REDISSON_CLIENT.getLock(lockKey);
        try {
            while (true) {
                if (lock.tryLock(waitTime, leaseTime, unit)) {
                    runnable.run();
                    break;
                }
            }
        } catch (Exception e) {
            log.error("尝试加锁出现异常", e);
            throw new BusinessException(ErrorCode.TRY_REDISSON_LOCK_ERROR);
        } finally {
            //每个线程只能释放自己的锁
            if (lock.isHeldByCurrentThread()) {
                log.info("unLock: " + Thread.currentThread().getId());
                lock.unlock();
            }
        }
    }
}
