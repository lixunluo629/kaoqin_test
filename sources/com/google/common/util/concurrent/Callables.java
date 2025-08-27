package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.util.concurrent.Callable;
import javax.annotation.Nullable;

/* loaded from: guava-18.0.jar:com/google/common/util/concurrent/Callables.class */
public final class Callables {
    private Callables() {
    }

    public static <T> Callable<T> returning(@Nullable final T value) {
        return new Callable<T>() { // from class: com.google.common.util.concurrent.Callables.1
            @Override // java.util.concurrent.Callable
            public T call() {
                return (T) value;
            }
        };
    }

    static <T> Callable<T> threadRenaming(final Callable<T> callable, final Supplier<String> nameSupplier) {
        Preconditions.checkNotNull(nameSupplier);
        Preconditions.checkNotNull(callable);
        return new Callable<T>() { // from class: com.google.common.util.concurrent.Callables.2
            @Override // java.util.concurrent.Callable
            public T call() throws Exception {
                Thread threadCurrentThread = Thread.currentThread();
                String name = threadCurrentThread.getName();
                boolean zTrySetName = Callables.trySetName((String) nameSupplier.get(), threadCurrentThread);
                try {
                    T t = (T) callable.call();
                    if (zTrySetName) {
                        Callables.trySetName(name, threadCurrentThread);
                    }
                    return t;
                } catch (Throwable th) {
                    if (zTrySetName) {
                        Callables.trySetName(name, threadCurrentThread);
                    }
                    throw th;
                }
            }
        };
    }

    static Runnable threadRenaming(final Runnable task, final Supplier<String> nameSupplier) {
        Preconditions.checkNotNull(nameSupplier);
        Preconditions.checkNotNull(task);
        return new Runnable() { // from class: com.google.common.util.concurrent.Callables.3
            @Override // java.lang.Runnable
            public void run() {
                Thread currentThread = Thread.currentThread();
                String oldName = currentThread.getName();
                boolean restoreName = Callables.trySetName((String) nameSupplier.get(), currentThread);
                try {
                    task.run();
                    if (restoreName) {
                        Callables.trySetName(oldName, currentThread);
                    }
                } catch (Throwable th) {
                    if (restoreName) {
                        Callables.trySetName(oldName, currentThread);
                    }
                    throw th;
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean trySetName(String threadName, Thread currentThread) {
        try {
            currentThread.setName(threadName);
            return true;
        } catch (SecurityException e) {
            return false;
        }
    }
}
