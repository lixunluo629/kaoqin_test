package org.ehcache.core.spi.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import org.ehcache.spi.service.Service;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/service/ExecutionService.class */
public interface ExecutionService extends Service {
    ScheduledExecutorService getScheduledExecutor(String str) throws IllegalArgumentException;

    ExecutorService getOrderedExecutor(String str, BlockingQueue<Runnable> blockingQueue) throws IllegalArgumentException;

    ExecutorService getUnorderedExecutor(String str, BlockingQueue<Runnable> blockingQueue) throws IllegalArgumentException;
}
