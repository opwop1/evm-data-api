package link.vtcm.common.util;

import cn.hutool.core.util.StrUtil;
import link.vtcm.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.function.Supplier;

/**
 * 同步锁工具类
 * @author zhangmj
 * @since 2025/7/1
 */
@Component
@RequiredArgsConstructor
public class SyncUtil {
    private final RedissonClient redissonClient;
    public final int LOCK_TYPE_READ = 1;
    public final int LOCK_TYPE_WRITE = 2;

    /**
     * 读锁
     * @param key 键
     * @param timeout 超时时间，毫秒
     * @param run 执行方法
     */
    public <T> T rLook(String key, Long timeout, Supplier<T> run) {
        return this.rwLook(key, LOCK_TYPE_READ, timeout, run);
    }

    /**
     * 写锁
     * @param key 键
     * @param timeout 超时时间，毫秒
     * @param run 执行方法
     */
    public <T> T wLook(String key, Long timeout, Supplier<T> run) {
        return this.rwLook(key, LOCK_TYPE_WRITE, timeout, run);
    }

    private <T> T rwLook(String key, int lockType, Long timeout, Supplier<T> run) {
        // 获取锁实例
        RReadWriteLock rwLock = redissonClient.getReadWriteLock(key);
        Lock lock = lockType == LOCK_TYPE_READ ? rwLock.readLock() : rwLock.writeLock();

        // 尝试上锁
        boolean lockAcquired = false;
        try {
            lockAcquired = lock.tryLock(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new BaseException(StrUtil.format("获取锁失败 err={}", e.getMessage()));
        }
        if (!lockAcquired) {
            throw new BaseException("获取锁超时，请稍后重试");
        }

        try {
            return run.get();
        } finally {
            // 释放锁
            lock.unlock();
        }
    }

    public <T> T lock(String key, Long timeout, Supplier<T> run) {
        RLock lock = redissonClient.getLock(key);
        boolean lockAcquired;
        try {
            lockAcquired = lock.tryLock(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new BaseException(StrUtil.format("获取锁失败 err={}", e.getMessage()));
        }
        if (!lockAcquired) {
            throw new BaseException("获取锁超时，请稍后重试");
        }
        try {
            return run.get();
        } finally {
            lock.unlock();
        }
    }
}
