package org.apache.tomcat.websocket.server;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import javax.websocket.SendHandler;
import javax.websocket.SendResult;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.net.AbstractEndpoint;
import org.apache.tomcat.util.net.SocketWrapperBase;
import org.apache.tomcat.util.res.StringManager;
import org.apache.tomcat.websocket.Transformation;
import org.apache.tomcat.websocket.WsRemoteEndpointImplBase;

/* loaded from: tomcat-embed-websocket-8.5.43.jar:org/apache/tomcat/websocket/server/WsRemoteEndpointImplServer.class */
public class WsRemoteEndpointImplServer extends WsRemoteEndpointImplBase {
    private static final StringManager sm = StringManager.getManager((Class<?>) WsRemoteEndpointImplServer.class);
    private final SocketWrapperBase<?> socketWrapper;
    private final WsWriteTimeout wsWriteTimeout;
    private volatile boolean close;
    private final Log log = LogFactory.getLog((Class<?>) WsRemoteEndpointImplServer.class);
    private volatile SendHandler handler = null;
    private volatile ByteBuffer[] buffers = null;
    private volatile long timeoutExpiry = -1;

    public WsRemoteEndpointImplServer(SocketWrapperBase<?> socketWrapper, WsServerContainer serverContainer) {
        this.socketWrapper = socketWrapper;
        this.wsWriteTimeout = serverContainer.getTimeout();
    }

    @Override // org.apache.tomcat.websocket.WsRemoteEndpointImplBase
    protected final boolean isMasked() {
        return false;
    }

    @Override // org.apache.tomcat.websocket.WsRemoteEndpointImplBase
    protected void doWrite(SendHandler handler, long blockingWriteTimeoutExpiry, ByteBuffer... buffers) {
        if (blockingWriteTimeoutExpiry == -1) {
            this.handler = handler;
            this.buffers = buffers;
            onWritePossible(true);
            return;
        }
        try {
            for (ByteBuffer buffer : buffers) {
                long timeout = blockingWriteTimeoutExpiry - System.currentTimeMillis();
                if (timeout <= 0) {
                    SendResult sr = new SendResult(new SocketTimeoutException());
                    handler.onResult(sr);
                    return;
                } else {
                    this.socketWrapper.setWriteTimeout(timeout);
                    this.socketWrapper.write(true, buffer);
                }
            }
            long timeout2 = blockingWriteTimeoutExpiry - System.currentTimeMillis();
            if (timeout2 <= 0) {
                SendResult sr2 = new SendResult(new SocketTimeoutException());
                handler.onResult(sr2);
            } else {
                this.socketWrapper.setWriteTimeout(timeout2);
                this.socketWrapper.flush(true);
                handler.onResult(SENDRESULT_OK);
            }
        } catch (IOException e) {
            SendResult sr3 = new SendResult(e);
            handler.onResult(sr3);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x005b, code lost:
    
        r6.socketWrapper.flush(false);
        r9 = r6.socketWrapper.isReadyForWrite();
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x006d, code lost:
    
        if (r9 == false) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0070, code lost:
    
        r6.wsWriteTimeout.unregister(r6);
        clearHandler(null, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0082, code lost:
    
        if (r6.close == false) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0085, code lost:
    
        close();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void onWritePossible(boolean r7) {
        /*
            Method dump skipped, instructions count: 200
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.tomcat.websocket.server.WsRemoteEndpointImplServer.onWritePossible(boolean):void");
    }

    @Override // org.apache.tomcat.websocket.WsRemoteEndpointImplBase
    protected void doClose() {
        if (this.handler != null) {
            clearHandler(new EOFException(), true);
        }
        try {
            this.socketWrapper.close();
        } catch (Exception e) {
            if (this.log.isInfoEnabled()) {
                this.log.info(sm.getString("wsRemoteEndpointServer.closeFailed"), e);
            }
        }
        this.wsWriteTimeout.unregister(this);
    }

    protected long getTimeoutExpiry() {
        return this.timeoutExpiry;
    }

    protected void onTimeout(boolean useDispatch) {
        if (this.handler != null) {
            clearHandler(new SocketTimeoutException(), useDispatch);
        }
        close();
    }

    @Override // org.apache.tomcat.websocket.WsRemoteEndpointImplBase
    protected void setTransformation(Transformation transformation) {
        super.setTransformation(transformation);
    }

    private void clearHandler(Throwable t, boolean useDispatch) {
        SendHandler sh = this.handler;
        this.handler = null;
        this.buffers = null;
        if (sh != null) {
            if (useDispatch) {
                OnResultRunnable r = new OnResultRunnable(sh, t);
                AbstractEndpoint<?> endpoint = this.socketWrapper.getEndpoint();
                Executor containerExecutor = endpoint.getExecutor();
                if (endpoint.isRunning() && containerExecutor != null) {
                    containerExecutor.execute(r);
                    return;
                } else {
                    r.run();
                    return;
                }
            }
            if (t == null) {
                sh.onResult(new SendResult());
            } else {
                sh.onResult(new SendResult(t));
            }
        }
    }

    /* loaded from: tomcat-embed-websocket-8.5.43.jar:org/apache/tomcat/websocket/server/WsRemoteEndpointImplServer$OnResultRunnable.class */
    private static class OnResultRunnable implements Runnable {
        private final SendHandler sh;
        private final Throwable t;

        private OnResultRunnable(SendHandler sh, Throwable t) {
            this.sh = sh;
            this.t = t;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.t == null) {
                this.sh.onResult(new SendResult());
            } else {
                this.sh.onResult(new SendResult(this.t));
            }
        }
    }
}
