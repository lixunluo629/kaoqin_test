package com.moredian.onpremise.core.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/ThreadPoolUtils.class */
public class ThreadPoolUtils {
    public static ExecutorService poolSend;

    static {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("netty-send-%d").build();
        poolSend = new ThreadPoolExecutor(4, 20, 10L, TimeUnit.MINUTES, new LinkedBlockingQueue(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }
}
