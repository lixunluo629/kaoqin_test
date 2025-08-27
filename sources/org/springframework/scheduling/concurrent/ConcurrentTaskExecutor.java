package org.springframework.scheduling.concurrent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.enterprise.concurrent.ManagedExecutors;
import javax.enterprise.concurrent.ManagedTaskListener;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.core.task.TaskDecorator;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.SchedulingAwareRunnable;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.util.ClassUtils;
import org.springframework.util.concurrent.ListenableFuture;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scheduling/concurrent/ConcurrentTaskExecutor.class */
public class ConcurrentTaskExecutor implements AsyncListenableTaskExecutor, SchedulingTaskExecutor {
    private static Class<?> managedExecutorServiceClass;
    private Executor concurrentExecutor;
    private TaskExecutorAdapter adaptedExecutor;

    static {
        try {
            managedExecutorServiceClass = ClassUtils.forName("javax.enterprise.concurrent.ManagedExecutorService", ConcurrentTaskScheduler.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            managedExecutorServiceClass = null;
        }
    }

    public ConcurrentTaskExecutor() {
        setConcurrentExecutor(null);
    }

    public ConcurrentTaskExecutor(Executor concurrentExecutor) {
        setConcurrentExecutor(concurrentExecutor);
    }

    public final void setConcurrentExecutor(Executor concurrentExecutor) {
        if (concurrentExecutor != null) {
            this.concurrentExecutor = concurrentExecutor;
            if (managedExecutorServiceClass != null && managedExecutorServiceClass.isInstance(concurrentExecutor)) {
                this.adaptedExecutor = new ManagedTaskExecutorAdapter(concurrentExecutor);
                return;
            } else {
                this.adaptedExecutor = new TaskExecutorAdapter(concurrentExecutor);
                return;
            }
        }
        this.concurrentExecutor = Executors.newSingleThreadExecutor();
        this.adaptedExecutor = new TaskExecutorAdapter(this.concurrentExecutor);
    }

    public final Executor getConcurrentExecutor() {
        return this.concurrentExecutor;
    }

    public final void setTaskDecorator(TaskDecorator taskDecorator) {
        this.adaptedExecutor.setTaskDecorator(taskDecorator);
    }

    @Override // org.springframework.core.task.TaskExecutor, java.util.concurrent.Executor
    public void execute(Runnable task) {
        this.adaptedExecutor.execute(task);
    }

    @Override // org.springframework.core.task.AsyncTaskExecutor
    public void execute(Runnable task, long startTimeout) {
        this.adaptedExecutor.execute(task, startTimeout);
    }

    @Override // org.springframework.core.task.AsyncTaskExecutor
    public Future<?> submit(Runnable task) {
        return this.adaptedExecutor.submit(task);
    }

    @Override // org.springframework.core.task.AsyncTaskExecutor
    public <T> Future<T> submit(Callable<T> task) {
        return this.adaptedExecutor.submit(task);
    }

    @Override // org.springframework.core.task.AsyncListenableTaskExecutor
    public ListenableFuture<?> submitListenable(Runnable task) {
        return this.adaptedExecutor.submitListenable(task);
    }

    @Override // org.springframework.core.task.AsyncListenableTaskExecutor
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        return this.adaptedExecutor.submitListenable(task);
    }

    @Override // org.springframework.scheduling.SchedulingTaskExecutor
    public boolean prefersShortLivedTasks() {
        return true;
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scheduling/concurrent/ConcurrentTaskExecutor$ManagedTaskExecutorAdapter.class */
    private static class ManagedTaskExecutorAdapter extends TaskExecutorAdapter {
        public ManagedTaskExecutorAdapter(Executor concurrentExecutor) {
            super(concurrentExecutor);
        }

        @Override // org.springframework.core.task.support.TaskExecutorAdapter, org.springframework.core.task.TaskExecutor, java.util.concurrent.Executor
        public void execute(Runnable task) {
            super.execute(ManagedTaskBuilder.buildManagedTask(task, task.toString()));
        }

        @Override // org.springframework.core.task.support.TaskExecutorAdapter, org.springframework.core.task.AsyncTaskExecutor
        public Future<?> submit(Runnable task) {
            return super.submit(ManagedTaskBuilder.buildManagedTask(task, task.toString()));
        }

        @Override // org.springframework.core.task.support.TaskExecutorAdapter, org.springframework.core.task.AsyncTaskExecutor
        public <T> Future<T> submit(Callable<T> task) {
            return super.submit(ManagedTaskBuilder.buildManagedTask(task, task.toString()));
        }

        @Override // org.springframework.core.task.support.TaskExecutorAdapter, org.springframework.core.task.AsyncListenableTaskExecutor
        public ListenableFuture<?> submitListenable(Runnable task) {
            return super.submitListenable(ManagedTaskBuilder.buildManagedTask(task, task.toString()));
        }

        @Override // org.springframework.core.task.support.TaskExecutorAdapter, org.springframework.core.task.AsyncListenableTaskExecutor
        public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
            return super.submitListenable(ManagedTaskBuilder.buildManagedTask(task, task.toString()));
        }
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scheduling/concurrent/ConcurrentTaskExecutor$ManagedTaskBuilder.class */
    protected static class ManagedTaskBuilder {
        protected ManagedTaskBuilder() {
        }

        public static Runnable buildManagedTask(Runnable task, String identityName) {
            Map<String, String> properties = new HashMap<>(2);
            if (task instanceof SchedulingAwareRunnable) {
                properties.put("javax.enterprise.concurrent.LONGRUNNING_HINT", Boolean.toString(((SchedulingAwareRunnable) task).isLongLived()));
            }
            properties.put("javax.enterprise.concurrent.IDENTITY_NAME", identityName);
            return ManagedExecutors.managedTask(task, properties, (ManagedTaskListener) null);
        }

        public static <T> Callable<T> buildManagedTask(Callable<T> task, String identityName) {
            Map<String, String> properties = new HashMap<>(1);
            properties.put("javax.enterprise.concurrent.IDENTITY_NAME", identityName);
            return ManagedExecutors.managedTask(task, properties, (ManagedTaskListener) null);
        }
    }
}
