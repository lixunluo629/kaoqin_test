package org.springframework.core.task;

import java.util.concurrent.Callable;
import org.springframework.util.concurrent.ListenableFuture;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/task/AsyncListenableTaskExecutor.class */
public interface AsyncListenableTaskExecutor extends AsyncTaskExecutor {
    ListenableFuture<?> submitListenable(Runnable runnable);

    <T> ListenableFuture<T> submitListenable(Callable<T> callable);
}
