package org.apache.coyote.http2;

import org.apache.coyote.ActionCode;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.res.StringManager;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http2/WindowAllocationManager.class */
class WindowAllocationManager {
    private static final Log log = LogFactory.getLog((Class<?>) WindowAllocationManager.class);
    private static final StringManager sm = StringManager.getManager((Class<?>) WindowAllocationManager.class);
    private static final int NONE = 0;
    private static final int STREAM = 1;
    private static final int CONNECTION = 2;
    private final Stream stream;
    private int waitingFor = 0;

    WindowAllocationManager(Stream stream) {
        this.stream = stream;
    }

    void waitForStream(long timeout) throws InterruptedException {
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("windowAllocationManager.waitFor.stream", this.stream.getConnectionId(), this.stream.getIdentifier(), Long.toString(timeout)));
        }
        waitFor(1, timeout);
    }

    void waitForConnection(long timeout) throws InterruptedException {
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("windowAllocationManager.waitFor.connection", this.stream.getConnectionId(), this.stream.getIdentifier(), Long.toString(timeout)));
        }
        waitFor(2, timeout);
    }

    void waitForStreamNonBlocking() {
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("windowAllocationManager.waitForNonBlocking.stream", this.stream.getConnectionId(), this.stream.getIdentifier()));
        }
        waitForNonBlocking(1);
    }

    void waitForConnectionNonBlocking() {
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("windowAllocationManager.waitForNonBlocking.connection", this.stream.getConnectionId(), this.stream.getIdentifier()));
        }
        waitForNonBlocking(2);
    }

    void notifyStream() {
        notify(1);
    }

    void notifyConnection() {
        notify(2);
    }

    void notifyAny() {
        notify(3);
    }

    private void waitFor(int waitTarget, long timeout) throws InterruptedException {
        synchronized (this.stream) {
            if (this.waitingFor != 0) {
                throw new IllegalStateException(sm.getString("windowAllocationManager.waitFor.ise", this.stream.getConnectionId(), this.stream.getIdentifier()));
            }
            this.waitingFor = waitTarget;
            if (timeout < 0) {
                this.stream.wait();
            } else {
                this.stream.wait(timeout);
            }
        }
    }

    private void waitForNonBlocking(int waitTarget) {
        synchronized (this.stream) {
            if (this.waitingFor == 0) {
                this.waitingFor = waitTarget;
            } else if (this.waitingFor != waitTarget) {
                throw new IllegalStateException(sm.getString("windowAllocationManager.waitFor.ise", this.stream.getConnectionId(), this.stream.getIdentifier()));
            }
        }
    }

    private void notify(int notifyTarget) {
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("windowAllocationManager.notify", this.stream.getConnectionId(), this.stream.getIdentifier(), Integer.toString(this.waitingFor), Integer.toString(notifyTarget)));
        }
        synchronized (this.stream) {
            if ((notifyTarget & this.waitingFor) > 0) {
                this.waitingFor = 0;
                if (this.stream.getCoyoteResponse().getWriteListener() == null) {
                    if (log.isDebugEnabled()) {
                        log.debug(sm.getString("windowAllocationManager.notified", this.stream.getConnectionId(), this.stream.getIdentifier()));
                    }
                    this.stream.notify();
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug(sm.getString("windowAllocationManager.dispatched", this.stream.getConnectionId(), this.stream.getIdentifier()));
                    }
                    this.stream.getCoyoteResponse().action(ActionCode.DISPATCH_WRITE, null);
                    this.stream.getCoyoteResponse().action(ActionCode.DISPATCH_EXECUTE, null);
                }
            }
        }
    }
}
