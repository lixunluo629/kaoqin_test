package io.netty.util.concurrent;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/concurrent/EventExecutorChooserFactory.class */
public interface EventExecutorChooserFactory {

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/concurrent/EventExecutorChooserFactory$EventExecutorChooser.class */
    public interface EventExecutorChooser {
        EventExecutor next();
    }

    EventExecutorChooser newChooser(EventExecutor[] eventExecutorArr);
}
