package com.zaxxer.hikari.util;

import java.lang.reflect.Constructor;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/util/UtilityElf.class */
public final class UtilityElf {
    public static String getNullIfEmpty(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        return text.trim();
    }

    public static void quietlySleep(long millis) throws InterruptedException {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static boolean safeIsAssignableFrom(Object obj, String className) throws ClassNotFoundException {
        try {
            Class<?> clazz = Class.forName(className);
            return clazz.isAssignableFrom(obj.getClass());
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static <T> T createInstance(String className, Class<T> clazz, Object... args) throws NoSuchMethodException, ClassNotFoundException, SecurityException {
        if (className == null) {
            return null;
        }
        try {
            Class<?> loaded = UtilityElf.class.getClassLoader().loadClass(className);
            if (args.length == 0) {
                return clazz.cast(loaded.newInstance());
            }
            Class<?>[] argClasses = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                argClasses[i] = args[i].getClass();
            }
            Constructor<?> constructor = loaded.getConstructor(argClasses);
            return clazz.cast(constructor.newInstance(args));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ThreadPoolExecutor createThreadPoolExecutor(int queueSize, String threadName, ThreadFactory threadFactory, RejectedExecutionHandler policy) {
        if (threadFactory == null) {
            threadFactory = new DefaultThreadFactory(threadName, true);
        }
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(queueSize);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 5L, TimeUnit.SECONDS, queue, threadFactory, policy);
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

    public static ThreadPoolExecutor createThreadPoolExecutor(BlockingQueue<Runnable> queue, String threadName, ThreadFactory threadFactory, RejectedExecutionHandler policy) {
        if (threadFactory == null) {
            threadFactory = new DefaultThreadFactory(threadName, true);
        }
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 5L, TimeUnit.SECONDS, queue, threadFactory, policy);
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

    public static int getTransactionIsolation(String transactionIsolationName) throws NumberFormatException {
        if (transactionIsolationName != null) {
            try {
                String upperCaseIsolationLevelName = transactionIsolationName.toUpperCase(Locale.ENGLISH);
                return IsolationLevel.valueOf(upperCaseIsolationLevelName).getLevelId();
            } catch (IllegalArgumentException e) {
                try {
                    int level = Integer.parseInt(transactionIsolationName);
                    for (IsolationLevel iso : IsolationLevel.values()) {
                        if (iso.getLevelId() == level) {
                            return iso.getLevelId();
                        }
                    }
                    throw new IllegalArgumentException("Invalid transaction isolation value: " + transactionIsolationName);
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("Invalid transaction isolation value: " + transactionIsolationName, nfe);
                }
            }
        }
        return -1;
    }

    /* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/util/UtilityElf$DefaultThreadFactory.class */
    public static final class DefaultThreadFactory implements ThreadFactory {
        private final String threadName;
        private final boolean daemon;

        public DefaultThreadFactory(String threadName, boolean daemon) {
            this.threadName = threadName;
            this.daemon = daemon;
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, this.threadName);
            thread.setDaemon(this.daemon);
            return thread;
        }
    }
}
