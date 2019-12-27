package com.ldm.media.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @作者 ldm
 * @时间 2019/11/13 16:33
 * @描述 线程池工具类
 */
public class ThreadPoolsUtils {
    //线程池核心线程数
    private static int CORE_POOL_SIZE = 5;
    //线程池最大线程数
    private static int MAX_POOL_SIZE = 10;
    //额外线程空状态生存时间
    private static int KEEP_ALIVE_TIME = 10000;
    //阻塞队列。当核心线程都被占用，且阻塞队列已满的情况下，才会开启额外线程。
    private static BlockingQueue workQueue = new ArrayBlockingQueue(10);
    //线程池
    private static ThreadPoolExecutor threadPool;
    //线程工厂
    private static ThreadFactory threadFactory = new ThreadFactory() {
        private final AtomicInteger integer = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "myThreadPool thread:" + integer.getAndIncrement());
        }
    };

    static {
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, workQueue, threadFactory);
    }

    private ThreadPoolsUtils() {
    }

    public static void execute(Runnable runnable) {
        threadPool.execute(runnable);
    }
}
