package org.apache.tomcat.util.net;

import java.util.Objects;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/net/SocketProcessorBase.class */
public abstract class SocketProcessorBase<S> implements Runnable {
    protected SocketWrapperBase<S> socketWrapper;
    protected SocketEvent event;

    protected abstract void doRun();

    public SocketProcessorBase(SocketWrapperBase<S> socketWrapper, SocketEvent event) {
        reset(socketWrapper, event);
    }

    public void reset(SocketWrapperBase<S> socketWrapper, SocketEvent event) {
        Objects.requireNonNull(event);
        this.socketWrapper = socketWrapper;
        this.event = event;
    }

    @Override // java.lang.Runnable
    public final void run() {
        synchronized (this.socketWrapper) {
            if (this.socketWrapper.isClosed()) {
                return;
            }
            doRun();
        }
    }
}
