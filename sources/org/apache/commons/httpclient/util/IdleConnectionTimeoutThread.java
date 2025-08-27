package org.apache.commons.httpclient.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.httpclient.HttpConnectionManager;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/util/IdleConnectionTimeoutThread.class */
public class IdleConnectionTimeoutThread extends Thread {
    private List connectionManagers = new ArrayList();
    private boolean shutdown = false;
    private long timeoutInterval = 1000;
    private long connectionTimeout = 3000;

    public IdleConnectionTimeoutThread() {
        setDaemon(true);
    }

    public synchronized void addConnectionManager(HttpConnectionManager connectionManager) {
        if (this.shutdown) {
            throw new IllegalStateException("IdleConnectionTimeoutThread has been shutdown");
        }
        this.connectionManagers.add(connectionManager);
    }

    public synchronized void removeConnectionManager(HttpConnectionManager connectionManager) {
        if (this.shutdown) {
            throw new IllegalStateException("IdleConnectionTimeoutThread has been shutdown");
        }
        this.connectionManagers.remove(connectionManager);
    }

    protected void handleCloseIdleConnections(HttpConnectionManager connectionManager) {
        connectionManager.closeIdleConnections(this.connectionTimeout);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public synchronized void run() throws InterruptedException {
        while (!this.shutdown) {
            for (HttpConnectionManager connectionManager : this.connectionManagers) {
                handleCloseIdleConnections(connectionManager);
            }
            try {
                wait(this.timeoutInterval);
            } catch (InterruptedException e) {
            }
        }
        this.connectionManagers.clear();
    }

    public synchronized void shutdown() {
        this.shutdown = true;
        notifyAll();
    }

    public synchronized void setConnectionTimeout(long connectionTimeout) {
        if (this.shutdown) {
            throw new IllegalStateException("IdleConnectionTimeoutThread has been shutdown");
        }
        this.connectionTimeout = connectionTimeout;
    }

    public synchronized void setTimeoutInterval(long timeoutInterval) {
        if (this.shutdown) {
            throw new IllegalStateException("IdleConnectionTimeoutThread has been shutdown");
        }
        this.timeoutInterval = timeoutInterval;
    }
}
