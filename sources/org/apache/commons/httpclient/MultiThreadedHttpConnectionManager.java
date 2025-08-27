package org.apache.commons.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.WeakHashMap;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.util.IdleConnectionHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/MultiThreadedHttpConnectionManager.class */
public class MultiThreadedHttpConnectionManager implements HttpConnectionManager {
    private static final Log LOG;
    public static final int DEFAULT_MAX_HOST_CONNECTIONS = 2;
    public static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 20;
    private static final Map REFERENCE_TO_CONNECTION_SOURCE;
    private static final ReferenceQueue REFERENCE_QUEUE;
    private static ReferenceQueueThread REFERENCE_QUEUE_THREAD;
    private static WeakHashMap ALL_CONNECTION_MANAGERS;
    static Class class$org$apache$commons$httpclient$MultiThreadedHttpConnectionManager;
    private HttpConnectionManagerParams params = new HttpConnectionManagerParams();
    private volatile boolean shutdown = false;
    private ConnectionPool connectionPool = new ConnectionPool(this, null);

    /* renamed from: org.apache.commons.httpclient.MultiThreadedHttpConnectionManager$1, reason: invalid class name */
    /* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/MultiThreadedHttpConnectionManager$1.class */
    static class AnonymousClass1 {
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$MultiThreadedHttpConnectionManager == null) {
            clsClass$ = class$("org.apache.commons.httpclient.MultiThreadedHttpConnectionManager");
            class$org$apache$commons$httpclient$MultiThreadedHttpConnectionManager = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$MultiThreadedHttpConnectionManager;
        }
        LOG = LogFactory.getLog(clsClass$);
        REFERENCE_TO_CONNECTION_SOURCE = new HashMap();
        REFERENCE_QUEUE = new ReferenceQueue();
        ALL_CONNECTION_MANAGERS = new WeakHashMap();
    }

    public static void shutdownAll() {
        synchronized (REFERENCE_TO_CONNECTION_SOURCE) {
            synchronized (ALL_CONNECTION_MANAGERS) {
                MultiThreadedHttpConnectionManager[] connManagers = (MultiThreadedHttpConnectionManager[]) ALL_CONNECTION_MANAGERS.keySet().toArray(new MultiThreadedHttpConnectionManager[ALL_CONNECTION_MANAGERS.size()]);
                for (int i = 0; i < connManagers.length; i++) {
                    if (connManagers[i] != null) {
                        connManagers[i].shutdown();
                    }
                }
            }
            if (REFERENCE_QUEUE_THREAD != null) {
                REFERENCE_QUEUE_THREAD.shutdown();
                REFERENCE_QUEUE_THREAD = null;
            }
            REFERENCE_TO_CONNECTION_SOURCE.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void storeReferenceToConnection(HttpConnectionWithReference connection, HostConfiguration hostConfiguration, ConnectionPool connectionPool) {
        ConnectionSource source = new ConnectionSource(null);
        source.connectionPool = connectionPool;
        source.hostConfiguration = hostConfiguration;
        synchronized (REFERENCE_TO_CONNECTION_SOURCE) {
            if (REFERENCE_QUEUE_THREAD == null) {
                REFERENCE_QUEUE_THREAD = new ReferenceQueueThread();
                REFERENCE_QUEUE_THREAD.start();
            }
            REFERENCE_TO_CONNECTION_SOURCE.put(connection.reference, source);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void shutdownCheckedOutConnections(ConnectionPool connectionPool) throws IOException {
        ArrayList connectionsToClose = new ArrayList();
        synchronized (REFERENCE_TO_CONNECTION_SOURCE) {
            Iterator referenceIter = REFERENCE_TO_CONNECTION_SOURCE.keySet().iterator();
            while (referenceIter.hasNext()) {
                Reference ref = (Reference) referenceIter.next();
                ConnectionSource source = (ConnectionSource) REFERENCE_TO_CONNECTION_SOURCE.get(ref);
                if (source.connectionPool == connectionPool) {
                    referenceIter.remove();
                    HttpConnection connection = (HttpConnection) ref.get();
                    if (connection != null) {
                        connectionsToClose.add(connection);
                    }
                }
            }
        }
        Iterator i = connectionsToClose.iterator();
        while (i.hasNext()) {
            HttpConnection connection2 = (HttpConnection) i.next();
            connection2.close();
            connection2.setHttpConnectionManager(null);
            connection2.releaseConnection();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void removeReferenceToConnection(HttpConnectionWithReference connection) {
        synchronized (REFERENCE_TO_CONNECTION_SOURCE) {
            REFERENCE_TO_CONNECTION_SOURCE.remove(connection.reference);
        }
    }

    public MultiThreadedHttpConnectionManager() {
        synchronized (ALL_CONNECTION_MANAGERS) {
            ALL_CONNECTION_MANAGERS.put(this, null);
        }
    }

    public synchronized void shutdown() {
        synchronized (this.connectionPool) {
            if (!this.shutdown) {
                this.shutdown = true;
                this.connectionPool.shutdown();
            }
        }
    }

    public boolean isConnectionStaleCheckingEnabled() {
        return this.params.isStaleCheckingEnabled();
    }

    public void setConnectionStaleCheckingEnabled(boolean connectionStaleCheckingEnabled) {
        this.params.setStaleCheckingEnabled(connectionStaleCheckingEnabled);
    }

    public void setMaxConnectionsPerHost(int maxHostConnections) {
        this.params.setDefaultMaxConnectionsPerHost(maxHostConnections);
    }

    public int getMaxConnectionsPerHost() {
        return this.params.getDefaultMaxConnectionsPerHost();
    }

    public void setMaxTotalConnections(int maxTotalConnections) {
        this.params.setMaxTotalConnections(maxTotalConnections);
    }

    public int getMaxTotalConnections() {
        return this.params.getMaxTotalConnections();
    }

    @Override // org.apache.commons.httpclient.HttpConnectionManager
    public HttpConnection getConnection(HostConfiguration hostConfiguration) {
        while (true) {
            try {
                return getConnectionWithTimeout(hostConfiguration, 0L);
            } catch (ConnectionPoolTimeoutException e) {
                LOG.debug("Unexpected exception while waiting for connection", e);
            }
        }
    }

    @Override // org.apache.commons.httpclient.HttpConnectionManager
    public HttpConnection getConnectionWithTimeout(HostConfiguration hostConfiguration, long timeout) throws ConnectionPoolTimeoutException {
        LOG.trace("enter HttpConnectionManager.getConnectionWithTimeout(HostConfiguration, long)");
        if (hostConfiguration == null) {
            throw new IllegalArgumentException("hostConfiguration is null");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(new StringBuffer().append("HttpConnectionManager.getConnection:  config = ").append(hostConfiguration).append(", timeout = ").append(timeout).toString());
        }
        HttpConnection conn = doGetConnection(hostConfiguration, timeout);
        return new HttpConnectionAdapter(conn);
    }

    @Override // org.apache.commons.httpclient.HttpConnectionManager
    public HttpConnection getConnection(HostConfiguration hostConfiguration, long timeout) throws HttpException {
        LOG.trace("enter HttpConnectionManager.getConnection(HostConfiguration, long)");
        try {
            return getConnectionWithTimeout(hostConfiguration, timeout);
        } catch (ConnectionPoolTimeoutException e) {
            throw new HttpException(e.getMessage());
        }
    }

    /* JADX WARN: Finally extract failed */
    private HttpConnection doGetConnection(HostConfiguration hostConfiguration, long timeout) throws ConnectionPoolTimeoutException {
        HttpConnection connection = null;
        int maxHostConnections = this.params.getMaxConnectionsPerHost(hostConfiguration);
        int maxTotalConnections = this.params.getMaxTotalConnections();
        synchronized (this.connectionPool) {
            HostConfiguration hostConfiguration2 = new HostConfiguration(hostConfiguration);
            HostConnectionPool hostPool = this.connectionPool.getHostPool(hostConfiguration2, true);
            WaitingThread waitingThread = null;
            boolean useTimeout = timeout > 0;
            long timeToWait = timeout;
            long startWait = 0;
            while (connection == null) {
                if (this.shutdown) {
                    throw new IllegalStateException("Connection factory has been shutdown.");
                }
                if (hostPool.freeConnections.size() > 0) {
                    connection = this.connectionPool.getFreeConnection(hostConfiguration2);
                } else if (hostPool.numConnections < maxHostConnections && this.connectionPool.numConnections < maxTotalConnections) {
                    connection = this.connectionPool.createConnection(hostConfiguration2);
                } else if (hostPool.numConnections < maxHostConnections && this.connectionPool.freeConnections.size() > 0) {
                    this.connectionPool.deleteLeastUsedConnection();
                    connection = this.connectionPool.createConnection(hostConfiguration2);
                } else {
                    if (useTimeout && timeToWait <= 0) {
                        throw new ConnectionPoolTimeoutException("Timeout waiting for connection");
                    }
                    try {
                        try {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug(new StringBuffer().append("Unable to get a connection, waiting..., hostConfig=").append(hostConfiguration2).toString());
                            }
                            if (waitingThread == null) {
                                waitingThread = new WaitingThread(null);
                                waitingThread.hostConnectionPool = hostPool;
                                waitingThread.thread = Thread.currentThread();
                            } else {
                                waitingThread.interruptedByConnectionPool = false;
                            }
                            if (useTimeout) {
                                startWait = System.currentTimeMillis();
                            }
                            hostPool.waitingThreads.addLast(waitingThread);
                            this.connectionPool.waitingThreads.addLast(waitingThread);
                            this.connectionPool.wait(timeToWait);
                            if (!waitingThread.interruptedByConnectionPool) {
                                hostPool.waitingThreads.remove(waitingThread);
                                this.connectionPool.waitingThreads.remove(waitingThread);
                            }
                            if (useTimeout) {
                                long endWait = System.currentTimeMillis();
                                timeToWait -= endWait - startWait;
                            }
                        } catch (InterruptedException e) {
                            if (!waitingThread.interruptedByConnectionPool) {
                                LOG.debug("Interrupted while waiting for connection", e);
                                throw new IllegalThreadStateException("Interrupted while waiting in MultiThreadedHttpConnectionManager");
                            }
                            if (!waitingThread.interruptedByConnectionPool) {
                                hostPool.waitingThreads.remove(waitingThread);
                                this.connectionPool.waitingThreads.remove(waitingThread);
                            }
                            if (useTimeout) {
                                long endWait2 = System.currentTimeMillis();
                                timeToWait -= endWait2 - startWait;
                            }
                        }
                    } catch (Throwable th) {
                        if (!waitingThread.interruptedByConnectionPool) {
                            hostPool.waitingThreads.remove(waitingThread);
                            this.connectionPool.waitingThreads.remove(waitingThread);
                        }
                        if (useTimeout) {
                            long endWait3 = System.currentTimeMillis();
                            long j = timeToWait - (endWait3 - startWait);
                        }
                        throw th;
                    }
                }
            }
        }
        return connection;
    }

    public int getConnectionsInPool(HostConfiguration hostConfiguration) {
        int i;
        synchronized (this.connectionPool) {
            HostConnectionPool hostPool = this.connectionPool.getHostPool(hostConfiguration, false);
            i = hostPool != null ? hostPool.numConnections : 0;
        }
        return i;
    }

    public int getConnectionsInPool() {
        int i;
        synchronized (this.connectionPool) {
            i = this.connectionPool.numConnections;
        }
        return i;
    }

    public int getConnectionsInUse(HostConfiguration hostConfiguration) {
        return getConnectionsInPool(hostConfiguration);
    }

    public int getConnectionsInUse() {
        return getConnectionsInPool();
    }

    public void deleteClosedConnections() throws IOException {
        this.connectionPool.deleteClosedConnections();
    }

    @Override // org.apache.commons.httpclient.HttpConnectionManager
    public void closeIdleConnections(long idleTimeout) throws IOException {
        this.connectionPool.closeIdleConnections(idleTimeout);
        deleteClosedConnections();
    }

    @Override // org.apache.commons.httpclient.HttpConnectionManager
    public void releaseConnection(HttpConnection conn) throws IOException {
        LOG.trace("enter HttpConnectionManager.releaseConnection(HttpConnection)");
        if (conn instanceof HttpConnectionAdapter) {
            conn = ((HttpConnectionAdapter) conn).getWrappedConnection();
        }
        SimpleHttpConnectionManager.finishLastResponse(conn);
        this.connectionPool.freeConnection(conn);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public HostConfiguration configurationForConnection(HttpConnection conn) {
        HostConfiguration connectionConfiguration = new HostConfiguration();
        connectionConfiguration.setHost(conn.getHost(), conn.getPort(), conn.getProtocol());
        if (conn.getLocalAddress() != null) {
            connectionConfiguration.setLocalAddress(conn.getLocalAddress());
        }
        if (conn.getProxyHost() != null) {
            connectionConfiguration.setProxy(conn.getProxyHost(), conn.getProxyPort());
        }
        return connectionConfiguration;
    }

    @Override // org.apache.commons.httpclient.HttpConnectionManager
    public HttpConnectionManagerParams getParams() {
        return this.params;
    }

    @Override // org.apache.commons.httpclient.HttpConnectionManager
    public void setParams(HttpConnectionManagerParams params) {
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }
        this.params = params;
    }

    /* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/MultiThreadedHttpConnectionManager$ConnectionPool.class */
    private class ConnectionPool {
        private LinkedList freeConnections;
        private LinkedList waitingThreads;
        private final Map mapHosts;
        private IdleConnectionHandler idleConnectionHandler;
        private int numConnections;
        private final MultiThreadedHttpConnectionManager this$0;

        private ConnectionPool(MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager) {
            this.this$0 = multiThreadedHttpConnectionManager;
            this.freeConnections = new LinkedList();
            this.waitingThreads = new LinkedList();
            this.mapHosts = new HashMap();
            this.idleConnectionHandler = new IdleConnectionHandler();
            this.numConnections = 0;
        }

        ConnectionPool(MultiThreadedHttpConnectionManager x0, AnonymousClass1 x1) {
            this(x0);
        }

        public synchronized void shutdown() throws IOException {
            Iterator iter = this.freeConnections.iterator();
            while (iter.hasNext()) {
                HttpConnection conn = (HttpConnection) iter.next();
                iter.remove();
                conn.close();
            }
            MultiThreadedHttpConnectionManager.shutdownCheckedOutConnections(this);
            Iterator iter2 = this.waitingThreads.iterator();
            while (iter2.hasNext()) {
                WaitingThread waiter = (WaitingThread) iter2.next();
                iter2.remove();
                waiter.interruptedByConnectionPool = true;
                waiter.thread.interrupt();
            }
            this.mapHosts.clear();
            this.idleConnectionHandler.removeAll();
        }

        public synchronized HttpConnection createConnection(HostConfiguration hostConfiguration) {
            HostConnectionPool hostPool = getHostPool(hostConfiguration, true);
            if (MultiThreadedHttpConnectionManager.LOG.isDebugEnabled()) {
                MultiThreadedHttpConnectionManager.LOG.debug(new StringBuffer().append("Allocating new connection, hostConfig=").append(hostConfiguration).toString());
            }
            HttpConnectionWithReference connection = new HttpConnectionWithReference(hostConfiguration);
            connection.getParams().setDefaults(this.this$0.params);
            connection.setHttpConnectionManager(this.this$0);
            this.numConnections++;
            hostPool.numConnections++;
            MultiThreadedHttpConnectionManager.storeReferenceToConnection(connection, hostConfiguration, this);
            return connection;
        }

        public synchronized void handleLostConnection(HostConfiguration config) {
            HostConnectionPool hostPool = getHostPool(config, true);
            hostPool.numConnections--;
            if (hostPool.numConnections == 0 && hostPool.waitingThreads.isEmpty()) {
                this.mapHosts.remove(config);
            }
            this.numConnections--;
            notifyWaitingThread(config);
        }

        public synchronized HostConnectionPool getHostPool(HostConfiguration hostConfiguration, boolean create) {
            MultiThreadedHttpConnectionManager.LOG.trace("enter HttpConnectionManager.ConnectionPool.getHostPool(HostConfiguration)");
            HostConnectionPool listConnections = (HostConnectionPool) this.mapHosts.get(hostConfiguration);
            if (listConnections == null && create) {
                listConnections = new HostConnectionPool(null);
                listConnections.hostConfiguration = hostConfiguration;
                this.mapHosts.put(hostConfiguration, listConnections);
            }
            return listConnections;
        }

        public synchronized HttpConnection getFreeConnection(HostConfiguration hostConfiguration) {
            HttpConnectionWithReference connection = null;
            HostConnectionPool hostPool = getHostPool(hostConfiguration, false);
            if (hostPool == null || hostPool.freeConnections.size() <= 0) {
                if (MultiThreadedHttpConnectionManager.LOG.isDebugEnabled()) {
                    MultiThreadedHttpConnectionManager.LOG.debug(new StringBuffer().append("There were no free connections to get, hostConfig=").append(hostConfiguration).toString());
                }
            } else {
                connection = (HttpConnectionWithReference) hostPool.freeConnections.removeLast();
                this.freeConnections.remove(connection);
                MultiThreadedHttpConnectionManager.storeReferenceToConnection(connection, hostConfiguration, this);
                if (MultiThreadedHttpConnectionManager.LOG.isDebugEnabled()) {
                    MultiThreadedHttpConnectionManager.LOG.debug(new StringBuffer().append("Getting free connection, hostConfig=").append(hostConfiguration).toString());
                }
                this.idleConnectionHandler.remove(connection);
            }
            return connection;
        }

        public synchronized void deleteClosedConnections() throws IOException {
            Iterator iter = this.freeConnections.iterator();
            while (iter.hasNext()) {
                HttpConnection conn = (HttpConnection) iter.next();
                if (!conn.isOpen()) {
                    iter.remove();
                    deleteConnection(conn);
                }
            }
        }

        public synchronized void closeIdleConnections(long idleTimeout) throws IOException {
            this.idleConnectionHandler.closeIdleConnections(idleTimeout);
        }

        private synchronized void deleteConnection(HttpConnection connection) throws IOException {
            HostConfiguration connectionConfiguration = this.this$0.configurationForConnection(connection);
            if (MultiThreadedHttpConnectionManager.LOG.isDebugEnabled()) {
                MultiThreadedHttpConnectionManager.LOG.debug(new StringBuffer().append("Reclaiming connection, hostConfig=").append(connectionConfiguration).toString());
            }
            connection.close();
            HostConnectionPool hostPool = getHostPool(connectionConfiguration, true);
            hostPool.freeConnections.remove(connection);
            hostPool.numConnections--;
            this.numConnections--;
            if (hostPool.numConnections == 0 && hostPool.waitingThreads.isEmpty()) {
                this.mapHosts.remove(connectionConfiguration);
            }
            this.idleConnectionHandler.remove(connection);
        }

        public synchronized void deleteLeastUsedConnection() throws IOException {
            HttpConnection connection = (HttpConnection) this.freeConnections.removeFirst();
            if (connection == null) {
                if (MultiThreadedHttpConnectionManager.LOG.isDebugEnabled()) {
                    MultiThreadedHttpConnectionManager.LOG.debug("Attempted to reclaim an unused connection but there were none.");
                    return;
                }
                return;
            }
            deleteConnection(connection);
        }

        public synchronized void notifyWaitingThread(HostConfiguration configuration) {
            notifyWaitingThread(getHostPool(configuration, true));
        }

        public synchronized void notifyWaitingThread(HostConnectionPool hostPool) {
            WaitingThread waitingThread = null;
            if (hostPool.waitingThreads.size() > 0) {
                if (MultiThreadedHttpConnectionManager.LOG.isDebugEnabled()) {
                    MultiThreadedHttpConnectionManager.LOG.debug(new StringBuffer().append("Notifying thread waiting on host pool, hostConfig=").append(hostPool.hostConfiguration).toString());
                }
                waitingThread = (WaitingThread) hostPool.waitingThreads.removeFirst();
                this.waitingThreads.remove(waitingThread);
            } else if (this.waitingThreads.size() > 0) {
                if (MultiThreadedHttpConnectionManager.LOG.isDebugEnabled()) {
                    MultiThreadedHttpConnectionManager.LOG.debug("No-one waiting on host pool, notifying next waiting thread.");
                }
                waitingThread = (WaitingThread) this.waitingThreads.removeFirst();
                waitingThread.hostConnectionPool.waitingThreads.remove(waitingThread);
            } else if (MultiThreadedHttpConnectionManager.LOG.isDebugEnabled()) {
                MultiThreadedHttpConnectionManager.LOG.debug("Notifying no-one, there are no waiting threads");
            }
            if (waitingThread != null) {
                waitingThread.interruptedByConnectionPool = true;
                waitingThread.thread.interrupt();
            }
        }

        public void freeConnection(HttpConnection conn) {
            HostConfiguration connectionConfiguration = this.this$0.configurationForConnection(conn);
            if (MultiThreadedHttpConnectionManager.LOG.isDebugEnabled()) {
                MultiThreadedHttpConnectionManager.LOG.debug(new StringBuffer().append("Freeing connection, hostConfig=").append(connectionConfiguration).toString());
            }
            synchronized (this) {
                if (this.this$0.shutdown) {
                    conn.close();
                    return;
                }
                HostConnectionPool hostPool = getHostPool(connectionConfiguration, true);
                hostPool.freeConnections.add(conn);
                if (hostPool.numConnections == 0) {
                    MultiThreadedHttpConnectionManager.LOG.error(new StringBuffer().append("Host connection pool not found, hostConfig=").append(connectionConfiguration).toString());
                    hostPool.numConnections = 1;
                }
                this.freeConnections.add(conn);
                MultiThreadedHttpConnectionManager.removeReferenceToConnection((HttpConnectionWithReference) conn);
                if (this.numConnections == 0) {
                    MultiThreadedHttpConnectionManager.LOG.error(new StringBuffer().append("Host connection pool not found, hostConfig=").append(connectionConfiguration).toString());
                    this.numConnections = 1;
                }
                this.idleConnectionHandler.add(conn);
                notifyWaitingThread(hostPool);
            }
        }
    }

    /* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/MultiThreadedHttpConnectionManager$ConnectionSource.class */
    private static class ConnectionSource {
        public ConnectionPool connectionPool;
        public HostConfiguration hostConfiguration;

        private ConnectionSource() {
        }

        ConnectionSource(AnonymousClass1 x0) {
            this();
        }
    }

    /* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/MultiThreadedHttpConnectionManager$HostConnectionPool.class */
    private static class HostConnectionPool {
        public HostConfiguration hostConfiguration;
        public LinkedList freeConnections;
        public LinkedList waitingThreads;
        public int numConnections;

        private HostConnectionPool() {
            this.freeConnections = new LinkedList();
            this.waitingThreads = new LinkedList();
            this.numConnections = 0;
        }

        HostConnectionPool(AnonymousClass1 x0) {
            this();
        }
    }

    /* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/MultiThreadedHttpConnectionManager$WaitingThread.class */
    private static class WaitingThread {
        public Thread thread;
        public HostConnectionPool hostConnectionPool;
        public boolean interruptedByConnectionPool;

        private WaitingThread() {
            this.interruptedByConnectionPool = false;
        }

        WaitingThread(AnonymousClass1 x0) {
            this();
        }
    }

    /* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/MultiThreadedHttpConnectionManager$ReferenceQueueThread.class */
    private static class ReferenceQueueThread extends Thread {
        private volatile boolean shutdown = false;

        public ReferenceQueueThread() {
            setDaemon(true);
            setName("MultiThreadedHttpConnectionManager cleanup");
        }

        public void shutdown() {
            this.shutdown = true;
            interrupt();
        }

        private void handleReference(Reference ref) {
            ConnectionSource source;
            synchronized (MultiThreadedHttpConnectionManager.REFERENCE_TO_CONNECTION_SOURCE) {
                source = (ConnectionSource) MultiThreadedHttpConnectionManager.REFERENCE_TO_CONNECTION_SOURCE.remove(ref);
            }
            if (source != null) {
                if (MultiThreadedHttpConnectionManager.LOG.isDebugEnabled()) {
                    MultiThreadedHttpConnectionManager.LOG.debug(new StringBuffer().append("Connection reclaimed by garbage collector, hostConfig=").append(source.hostConfiguration).toString());
                }
                source.connectionPool.handleLostConnection(source.hostConfiguration);
            }
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() throws InterruptedException {
            while (!this.shutdown) {
                try {
                    Reference ref = MultiThreadedHttpConnectionManager.REFERENCE_QUEUE.remove();
                    if (ref != null) {
                        handleReference(ref);
                    }
                } catch (InterruptedException e) {
                    MultiThreadedHttpConnectionManager.LOG.debug("ReferenceQueueThread interrupted", e);
                }
            }
        }
    }

    /* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/MultiThreadedHttpConnectionManager$HttpConnectionWithReference.class */
    private static class HttpConnectionWithReference extends HttpConnection {
        public WeakReference reference;

        public HttpConnectionWithReference(HostConfiguration hostConfiguration) {
            super(hostConfiguration);
            this.reference = new WeakReference(this, MultiThreadedHttpConnectionManager.REFERENCE_QUEUE);
        }
    }

    /* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/MultiThreadedHttpConnectionManager$HttpConnectionAdapter.class */
    private static class HttpConnectionAdapter extends HttpConnection {
        private HttpConnection wrappedConnection;

        public HttpConnectionAdapter(HttpConnection connection) {
            super(connection.getHost(), connection.getPort(), connection.getProtocol());
            this.wrappedConnection = connection;
        }

        protected boolean hasConnection() {
            return this.wrappedConnection != null;
        }

        HttpConnection getWrappedConnection() {
            return this.wrappedConnection;
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void close() throws IOException {
            if (hasConnection()) {
                this.wrappedConnection.close();
            }
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public InetAddress getLocalAddress() {
            if (hasConnection()) {
                return this.wrappedConnection.getLocalAddress();
            }
            return null;
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public boolean isStaleCheckingEnabled() {
            if (hasConnection()) {
                return this.wrappedConnection.isStaleCheckingEnabled();
            }
            return false;
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void setLocalAddress(InetAddress localAddress) throws IllegalStateException {
            if (hasConnection()) {
                this.wrappedConnection.setLocalAddress(localAddress);
                return;
            }
            throw new IllegalStateException("Connection has been released");
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void setStaleCheckingEnabled(boolean staleCheckEnabled) {
            if (hasConnection()) {
                this.wrappedConnection.setStaleCheckingEnabled(staleCheckEnabled);
                return;
            }
            throw new IllegalStateException("Connection has been released");
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public String getHost() {
            if (hasConnection()) {
                return this.wrappedConnection.getHost();
            }
            return null;
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public HttpConnectionManager getHttpConnectionManager() {
            if (hasConnection()) {
                return this.wrappedConnection.getHttpConnectionManager();
            }
            return null;
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public InputStream getLastResponseInputStream() {
            if (hasConnection()) {
                return this.wrappedConnection.getLastResponseInputStream();
            }
            return null;
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public int getPort() {
            if (hasConnection()) {
                return this.wrappedConnection.getPort();
            }
            return -1;
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public Protocol getProtocol() {
            if (hasConnection()) {
                return this.wrappedConnection.getProtocol();
            }
            return null;
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public String getProxyHost() {
            if (hasConnection()) {
                return this.wrappedConnection.getProxyHost();
            }
            return null;
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public int getProxyPort() {
            if (hasConnection()) {
                return this.wrappedConnection.getProxyPort();
            }
            return -1;
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public OutputStream getRequestOutputStream() throws IllegalStateException, IOException {
            if (hasConnection()) {
                return this.wrappedConnection.getRequestOutputStream();
            }
            return null;
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public InputStream getResponseInputStream() throws IllegalStateException, IOException {
            if (hasConnection()) {
                return this.wrappedConnection.getResponseInputStream();
            }
            return null;
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public boolean isOpen() {
            if (hasConnection()) {
                return this.wrappedConnection.isOpen();
            }
            return false;
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public boolean closeIfStale() throws IOException {
            if (hasConnection()) {
                return this.wrappedConnection.closeIfStale();
            }
            return false;
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public boolean isProxied() {
            if (hasConnection()) {
                return this.wrappedConnection.isProxied();
            }
            return false;
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public boolean isResponseAvailable() throws IOException {
            if (hasConnection()) {
                return this.wrappedConnection.isResponseAvailable();
            }
            return false;
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public boolean isResponseAvailable(int timeout) throws IOException {
            if (hasConnection()) {
                return this.wrappedConnection.isResponseAvailable(timeout);
            }
            return false;
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public boolean isSecure() {
            if (hasConnection()) {
                return this.wrappedConnection.isSecure();
            }
            return false;
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public boolean isTransparent() {
            if (hasConnection()) {
                return this.wrappedConnection.isTransparent();
            }
            return false;
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void open() throws IllegalStateException, IOException {
            if (hasConnection()) {
                this.wrappedConnection.open();
                return;
            }
            throw new IllegalStateException("Connection has been released");
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void print(String data) throws IllegalStateException, IOException {
            if (hasConnection()) {
                this.wrappedConnection.print(data);
                return;
            }
            throw new IllegalStateException("Connection has been released");
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void printLine() throws IllegalStateException, IOException {
            if (hasConnection()) {
                this.wrappedConnection.printLine();
                return;
            }
            throw new IllegalStateException("Connection has been released");
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void printLine(String data) throws IllegalStateException, IOException {
            if (hasConnection()) {
                this.wrappedConnection.printLine(data);
                return;
            }
            throw new IllegalStateException("Connection has been released");
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public String readLine() throws IllegalStateException, IOException {
            if (hasConnection()) {
                return this.wrappedConnection.readLine();
            }
            throw new IllegalStateException("Connection has been released");
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public String readLine(String charset) throws IllegalStateException, IOException {
            if (hasConnection()) {
                return this.wrappedConnection.readLine(charset);
            }
            throw new IllegalStateException("Connection has been released");
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void releaseConnection() {
            if (!isLocked() && hasConnection()) {
                HttpConnection wrappedConnection = this.wrappedConnection;
                this.wrappedConnection = null;
                wrappedConnection.releaseConnection();
            }
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void setConnectionTimeout(int timeout) {
            if (hasConnection()) {
                this.wrappedConnection.setConnectionTimeout(timeout);
            }
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void setHost(String host) throws IllegalStateException {
            if (hasConnection()) {
                this.wrappedConnection.setHost(host);
            }
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void setHttpConnectionManager(HttpConnectionManager httpConnectionManager) {
            if (hasConnection()) {
                this.wrappedConnection.setHttpConnectionManager(httpConnectionManager);
            }
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void setLastResponseInputStream(InputStream inStream) {
            if (hasConnection()) {
                this.wrappedConnection.setLastResponseInputStream(inStream);
            }
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void setPort(int port) throws IllegalStateException {
            if (hasConnection()) {
                this.wrappedConnection.setPort(port);
            }
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void setProtocol(Protocol protocol) throws IllegalStateException {
            if (hasConnection()) {
                this.wrappedConnection.setProtocol(protocol);
            }
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void setProxyHost(String host) throws IllegalStateException {
            if (hasConnection()) {
                this.wrappedConnection.setProxyHost(host);
            }
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void setProxyPort(int port) throws IllegalStateException {
            if (hasConnection()) {
                this.wrappedConnection.setProxyPort(port);
            }
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void setSoTimeout(int timeout) throws IllegalStateException, SocketException {
            if (hasConnection()) {
                this.wrappedConnection.setSoTimeout(timeout);
            }
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void shutdownOutput() throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
            if (hasConnection()) {
                this.wrappedConnection.shutdownOutput();
            }
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void tunnelCreated() throws IllegalStateException, IOException {
            if (hasConnection()) {
                this.wrappedConnection.tunnelCreated();
            }
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void write(byte[] data, int offset, int length) throws IllegalStateException, IOException {
            if (hasConnection()) {
                this.wrappedConnection.write(data, offset, length);
                return;
            }
            throw new IllegalStateException("Connection has been released");
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void write(byte[] data) throws IllegalStateException, IOException {
            if (hasConnection()) {
                this.wrappedConnection.write(data);
                return;
            }
            throw new IllegalStateException("Connection has been released");
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void writeLine() throws IllegalStateException, IOException {
            if (hasConnection()) {
                this.wrappedConnection.writeLine();
                return;
            }
            throw new IllegalStateException("Connection has been released");
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void writeLine(byte[] data) throws IllegalStateException, IOException {
            if (hasConnection()) {
                this.wrappedConnection.writeLine(data);
                return;
            }
            throw new IllegalStateException("Connection has been released");
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void flushRequestOutputStream() throws IllegalStateException, IOException {
            if (hasConnection()) {
                this.wrappedConnection.flushRequestOutputStream();
                return;
            }
            throw new IllegalStateException("Connection has been released");
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public int getSoTimeout() throws SocketException {
            if (hasConnection()) {
                return this.wrappedConnection.getSoTimeout();
            }
            throw new IllegalStateException("Connection has been released");
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public String getVirtualHost() {
            if (hasConnection()) {
                return this.wrappedConnection.getVirtualHost();
            }
            throw new IllegalStateException("Connection has been released");
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void setVirtualHost(String host) throws IllegalStateException {
            if (hasConnection()) {
                this.wrappedConnection.setVirtualHost(host);
                return;
            }
            throw new IllegalStateException("Connection has been released");
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public int getSendBufferSize() throws SocketException {
            if (hasConnection()) {
                return this.wrappedConnection.getSendBufferSize();
            }
            throw new IllegalStateException("Connection has been released");
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void setSendBufferSize(int sendBufferSize) throws SocketException {
            if (hasConnection()) {
                this.wrappedConnection.setSendBufferSize(sendBufferSize);
                return;
            }
            throw new IllegalStateException("Connection has been released");
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public HttpConnectionParams getParams() {
            if (hasConnection()) {
                return this.wrappedConnection.getParams();
            }
            throw new IllegalStateException("Connection has been released");
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void setParams(HttpConnectionParams params) {
            if (hasConnection()) {
                this.wrappedConnection.setParams(params);
                return;
            }
            throw new IllegalStateException("Connection has been released");
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void print(String data, String charset) throws IllegalStateException, IOException {
            if (hasConnection()) {
                this.wrappedConnection.print(data, charset);
                return;
            }
            throw new IllegalStateException("Connection has been released");
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void printLine(String data, String charset) throws IllegalStateException, IOException {
            if (hasConnection()) {
                this.wrappedConnection.printLine(data, charset);
                return;
            }
            throw new IllegalStateException("Connection has been released");
        }

        @Override // org.apache.commons.httpclient.HttpConnection
        public void setSocketTimeout(int timeout) throws IllegalStateException, SocketException {
            if (hasConnection()) {
                this.wrappedConnection.setSocketTimeout(timeout);
                return;
            }
            throw new IllegalStateException("Connection has been released");
        }
    }
}
