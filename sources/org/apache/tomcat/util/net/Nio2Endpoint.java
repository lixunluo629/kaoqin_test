package org.apache.tomcat.util.net;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileChannel;
import java.nio.channels.InterruptedByTimeoutException;
import java.nio.channels.NetworkChannel;
import java.nio.channels.ReadPendingException;
import java.nio.channels.WritePendingException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.ExceptionUtils;
import org.apache.tomcat.util.collections.SynchronizedStack;
import org.apache.tomcat.util.net.AbstractEndpoint;
import org.apache.tomcat.util.net.SocketWrapperBase;
import org.apache.tomcat.util.net.jsse.JSSESupport;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException
    */
/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/net/Nio2Endpoint.class */
public class Nio2Endpoint extends AbstractJsseEndpoint<Nio2Channel> {
    private volatile AsynchronousServerSocketChannel serverSock = null;
    private AsynchronousChannelGroup threadGroup = null;
    private volatile boolean allClosed;
    private SynchronizedStack<Nio2Channel> nioChannels;
    private static final Log log = LogFactory.getLog((Class<?>) Nio2Endpoint.class);
    private static ThreadLocal<Boolean> inlineCompletion = new ThreadLocal<>();

    static {
    }

    public Nio2Endpoint() {
        setMaxConnections(-1);
    }

    public void setSocketProperties(SocketProperties socketProperties) {
        this.socketProperties = socketProperties;
    }

    @Override // org.apache.tomcat.util.net.AbstractEndpoint
    public boolean getDeferAccept() {
        return false;
    }

    public int getKeepAliveCount() {
        return -1;
    }

    @Override // org.apache.tomcat.util.net.AbstractEndpoint
    public void bind() throws Exception {
        if (getExecutor() == null) {
            createExecutor();
        }
        if (getExecutor() instanceof ExecutorService) {
            this.threadGroup = AsynchronousChannelGroup.withThreadPool((ExecutorService) getExecutor());
        }
        if (!this.internalExecutor) {
            log.warn(sm.getString("endpoint.nio2.exclusiveExecutor"));
        }
        this.serverSock = AsynchronousServerSocketChannel.open(this.threadGroup);
        this.socketProperties.setProperties(this.serverSock);
        InetSocketAddress addr = getAddress() != null ? new InetSocketAddress(getAddress(), getPort()) : new InetSocketAddress(getPort());
        this.serverSock.bind(addr, getAcceptCount());
        if (this.acceptorThreadCount != 1) {
            this.acceptorThreadCount = 1;
        }
        initialiseSsl();
    }

    @Override // org.apache.tomcat.util.net.AbstractEndpoint
    public void startInternal() throws Exception {
        if (!this.running) {
            this.allClosed = false;
            this.running = true;
            this.paused = false;
            this.processorCache = new SynchronizedStack<>(128, this.socketProperties.getProcessorCache());
            this.nioChannels = new SynchronizedStack<>(128, this.socketProperties.getBufferPool());
            if (getExecutor() == null) {
                createExecutor();
            }
            initializeConnectionLatch();
            startAcceptorThreads();
        }
    }

    @Override // org.apache.tomcat.util.net.AbstractEndpoint
    public void stopInternal() {
        releaseConnectionLatch();
        if (!this.paused) {
            pause();
        }
        if (this.running) {
            this.running = false;
            unlockAccept();
            getExecutor().execute(new Runnable() { // from class: org.apache.tomcat.util.net.Nio2Endpoint.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        try {
                            for (Nio2Channel channel : Nio2Endpoint.this.getHandler().getOpenSockets()) {
                                channel.getSocket().close();
                            }
                        } catch (Throwable t) {
                            ExceptionUtils.handleThrowable(t);
                            Nio2Endpoint.this.allClosed = true;
                        }
                    } finally {
                        Nio2Endpoint.this.allClosed = true;
                    }
                }
            });
            this.nioChannels.clear();
            this.processorCache.clear();
        }
    }

    @Override // org.apache.tomcat.util.net.AbstractJsseEndpoint, org.apache.tomcat.util.net.AbstractEndpoint
    public void unbind() throws Exception {
        if (this.running) {
            stop();
        }
        doCloseServerSocket();
        destroySsl();
        super.unbind();
        shutdownExecutor();
        if (getHandler() != null) {
            getHandler().recycle();
        }
    }

    @Override // org.apache.tomcat.util.net.AbstractEndpoint
    protected void doCloseServerSocket() throws IOException {
        if (this.serverSock != null) {
            this.serverSock.close();
            this.serverSock = null;
        }
    }

    @Override // org.apache.tomcat.util.net.AbstractEndpoint
    public void shutdownExecutor() throws InterruptedException, IOException {
        if (this.threadGroup != null && this.internalExecutor) {
            try {
                long timeout = getExecutorTerminationTimeoutMillis();
                while (timeout > 0 && !this.allClosed) {
                    timeout -= 100;
                    Thread.sleep(100L);
                }
                this.threadGroup.shutdownNow();
                if (timeout > 0) {
                    this.threadGroup.awaitTermination(timeout, TimeUnit.MILLISECONDS);
                }
            } catch (IOException e) {
                getLog().warn(sm.getString("endpoint.warn.executorShutdown", getName()), e);
            } catch (InterruptedException e2) {
            }
            if (!this.threadGroup.isTerminated()) {
                getLog().warn(sm.getString("endpoint.warn.executorShutdown", getName()));
            }
            this.threadGroup = null;
        }
        super.shutdownExecutor();
    }

    public int getWriteBufSize() {
        return this.socketProperties.getTxBufSize();
    }

    public int getReadBufSize() {
        return this.socketProperties.getRxBufSize();
    }

    @Override // org.apache.tomcat.util.net.AbstractEndpoint
    protected AbstractEndpoint.Acceptor createAcceptor() {
        return new Acceptor();
    }

    protected boolean setSocketOptions(AsynchronousSocketChannel socket) {
        try {
            this.socketProperties.setProperties(socket);
            Nio2Channel channel = this.nioChannels.pop();
            if (channel == null) {
                SocketBufferHandler bufhandler = new SocketBufferHandler(this.socketProperties.getAppReadBufSize(), this.socketProperties.getAppWriteBufSize(), this.socketProperties.getDirectBuffer());
                if (isSSLEnabled()) {
                    channel = new SecureNio2Channel(bufhandler, this);
                } else {
                    channel = new Nio2Channel(bufhandler);
                }
            }
            Nio2SocketWrapper socketWrapper = new Nio2SocketWrapper(channel, this);
            channel.reset(socket, socketWrapper);
            socketWrapper.setReadTimeout(getSocketProperties().getSoTimeout());
            socketWrapper.setWriteTimeout(getSocketProperties().getSoTimeout());
            socketWrapper.setKeepAliveLeft(getMaxKeepAliveRequests());
            socketWrapper.setSecure(isSSLEnabled());
            socketWrapper.setReadTimeout(getConnectionTimeout());
            socketWrapper.setWriteTimeout(getConnectionTimeout());
            return processSocket(socketWrapper, SocketEvent.OPEN_READ, true);
        } catch (Throwable t) {
            ExceptionUtils.handleThrowable(t);
            log.error("", t);
            return false;
        }
    }

    @Override // org.apache.tomcat.util.net.AbstractEndpoint
    protected SocketProcessorBase<Nio2Channel> createSocketProcessor(SocketWrapperBase<Nio2Channel> socketWrapper, SocketEvent event) {
        return new SocketProcessor(this, socketWrapper, event);
    }

    @Override // org.apache.tomcat.util.net.AbstractEndpoint
    protected Log getLog() {
        return log;
    }

    @Override // org.apache.tomcat.util.net.AbstractJsseEndpoint
    protected NetworkChannel getServerSocket() {
        return this.serverSock;
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/net/Nio2Endpoint$Acceptor.class */
    protected class Acceptor extends AbstractEndpoint.Acceptor {
        protected Acceptor() {
        }

        @Override // java.lang.Runnable
        public void run() throws InterruptedException {
            int errorDelay = 0;
            while (Nio2Endpoint.this.running) {
                while (Nio2Endpoint.this.paused && Nio2Endpoint.this.running) {
                    this.state = AbstractEndpoint.Acceptor.AcceptorState.PAUSED;
                    try {
                        Thread.sleep(50L);
                    } catch (InterruptedException e) {
                    }
                }
                if (!Nio2Endpoint.this.running) {
                    break;
                }
                this.state = AbstractEndpoint.Acceptor.AcceptorState.RUNNING;
                try {
                    Nio2Endpoint.this.countUpOrAwaitConnection();
                    try {
                        AsynchronousSocketChannel socket = Nio2Endpoint.this.serverSock.accept().get();
                        errorDelay = 0;
                        if (!Nio2Endpoint.this.running || Nio2Endpoint.this.paused || !Nio2Endpoint.this.setSocketOptions(socket)) {
                            closeSocket(socket);
                        }
                    } catch (Exception e2) {
                        Nio2Endpoint.this.countDownConnection();
                        if (!Nio2Endpoint.this.running) {
                            break;
                        }
                        errorDelay = Nio2Endpoint.this.handleExceptionWithDelay(errorDelay);
                        throw e2;
                    }
                } catch (Throwable t) {
                    ExceptionUtils.handleThrowable(t);
                    Nio2Endpoint.log.error(AbstractEndpoint.sm.getString("endpoint.accept.fail"), t);
                }
            }
            this.state = AbstractEndpoint.Acceptor.AcceptorState.ENDED;
        }

        private void closeSocket(AsynchronousSocketChannel socket) {
            Nio2Endpoint.this.countDownConnection();
            try {
                socket.close();
            } catch (IOException ioe) {
                if (Nio2Endpoint.log.isDebugEnabled()) {
                    Nio2Endpoint.log.debug(AbstractEndpoint.sm.getString("endpoint.err.close"), ioe);
                }
            }
        }
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/net/Nio2Endpoint$Nio2SocketWrapper.class */
    public static class Nio2SocketWrapper extends SocketWrapperBase<Nio2Channel> {
        private SendfileData sendfileData;
        private final CompletionHandler<Integer, ByteBuffer> readCompletionHandler;
        private final Semaphore readPending;
        private boolean readInterest;
        private boolean readNotify;
        private final CompletionHandler<Integer, ByteBuffer> writeCompletionHandler;
        private final CompletionHandler<Long, ByteBuffer[]> gatheringWriteCompletionHandler;
        private final Semaphore writePending;
        private boolean writeInterest;
        private boolean writeNotify;
        private volatile boolean closed;
        private CompletionHandler<Integer, SendfileData> sendfileHandler;

        public Nio2SocketWrapper(Nio2Channel channel, final Nio2Endpoint endpoint) {
            super(channel, endpoint);
            this.sendfileData = null;
            this.readPending = new Semaphore(1);
            this.readInterest = false;
            this.readNotify = false;
            this.writePending = new Semaphore(1);
            this.writeInterest = false;
            this.writeNotify = false;
            this.closed = false;
            this.sendfileHandler = new CompletionHandler<Integer, SendfileData>() { // from class: org.apache.tomcat.util.net.Nio2Endpoint.Nio2SocketWrapper.1
                @Override // java.nio.channels.CompletionHandler
                public void completed(Integer nWrite, SendfileData attachment) throws IOException {
                    if (nWrite.intValue() < 0) {
                        failed((Throwable) new EOFException(), attachment);
                        return;
                    }
                    attachment.pos += nWrite.intValue();
                    ByteBuffer buffer = Nio2SocketWrapper.this.getSocket().getBufHandler().getWriteBuffer();
                    if (!buffer.hasRemaining()) {
                        if (attachment.length <= 0) {
                            Nio2SocketWrapper.this.setSendfileData(null);
                            try {
                                attachment.fchannel.close();
                            } catch (IOException e) {
                            }
                            if (!Nio2Endpoint.isInline()) {
                                switch (attachment.keepAliveState) {
                                    case NONE:
                                        Nio2SocketWrapper.this.getEndpoint().processSocket(Nio2SocketWrapper.this, SocketEvent.DISCONNECT, false);
                                        break;
                                    case PIPELINED:
                                        Nio2SocketWrapper.this.getEndpoint().processSocket(Nio2SocketWrapper.this, SocketEvent.OPEN_READ, true);
                                        break;
                                    case OPEN:
                                        Nio2SocketWrapper.this.registerReadInterest();
                                        break;
                                }
                                return;
                            }
                            attachment.doneInline = true;
                            return;
                        }
                        Nio2SocketWrapper.this.getSocket().getBufHandler().configureWriteBufferForWrite();
                        try {
                            int nRead = attachment.fchannel.read(buffer);
                            if (nRead > 0) {
                                Nio2SocketWrapper.this.getSocket().getBufHandler().configureWriteBufferForRead();
                                if (attachment.length < buffer.remaining()) {
                                    buffer.limit((buffer.limit() - buffer.remaining()) + ((int) attachment.length));
                                }
                                attachment.length -= nRead;
                            } else {
                                failed((Throwable) new EOFException(), attachment);
                                return;
                            }
                        } catch (IOException e2) {
                            failed((Throwable) e2, attachment);
                            return;
                        }
                    }
                    Nio2SocketWrapper.this.getSocket().write(buffer, AbstractEndpoint.toTimeout(Nio2SocketWrapper.this.getWriteTimeout()), TimeUnit.MILLISECONDS, attachment, this);
                }

                @Override // java.nio.channels.CompletionHandler
                public void failed(Throwable exc, SendfileData attachment) {
                    try {
                        attachment.fchannel.close();
                    } catch (IOException e) {
                    }
                    if (!Nio2Endpoint.isInline()) {
                        Nio2SocketWrapper.this.getEndpoint().processSocket(Nio2SocketWrapper.this, SocketEvent.ERROR, false);
                    } else {
                        attachment.doneInline = true;
                        attachment.error = true;
                    }
                }
            };
            this.socketBufferHandler = channel.getBufHandler();
            this.readCompletionHandler = new CompletionHandler<Integer, ByteBuffer>() { // from class: org.apache.tomcat.util.net.Nio2Endpoint.Nio2SocketWrapper.2
                @Override // java.nio.channels.CompletionHandler
                public void completed(Integer nBytes, ByteBuffer attachment) {
                    if (Nio2Endpoint.log.isDebugEnabled()) {
                        Nio2Endpoint.log.debug("Socket: [" + Nio2SocketWrapper.this + "], Interest: [" + Nio2SocketWrapper.this.readInterest + "]");
                    }
                    Nio2SocketWrapper.this.readNotify = false;
                    synchronized (Nio2SocketWrapper.this.readCompletionHandler) {
                        if (nBytes.intValue() >= 0) {
                            if (!Nio2SocketWrapper.this.readInterest || Nio2Endpoint.isInline()) {
                                Nio2SocketWrapper.this.readPending.release();
                            } else {
                                Nio2SocketWrapper.this.readNotify = true;
                            }
                            Nio2SocketWrapper.this.readInterest = false;
                        } else {
                            failed((Throwable) new EOFException(), attachment);
                        }
                    }
                    if (Nio2SocketWrapper.this.readNotify) {
                        Nio2SocketWrapper.this.getEndpoint().processSocket(Nio2SocketWrapper.this, SocketEvent.OPEN_READ, false);
                    }
                }

                @Override // java.nio.channels.CompletionHandler
                public void failed(Throwable exc, ByteBuffer attachment) {
                    IOException ioe;
                    if (exc instanceof IOException) {
                        ioe = (IOException) exc;
                    } else {
                        ioe = new IOException(exc);
                    }
                    Nio2SocketWrapper.this.setError(ioe);
                    if (exc instanceof AsynchronousCloseException) {
                        Nio2SocketWrapper.this.readPending.release();
                    } else {
                        Nio2SocketWrapper.this.getEndpoint().processSocket(Nio2SocketWrapper.this, SocketEvent.ERROR, true);
                    }
                }
            };
            this.writeCompletionHandler = new CompletionHandler<Integer, ByteBuffer>() { // from class: org.apache.tomcat.util.net.Nio2Endpoint.Nio2SocketWrapper.3
                @Override // java.nio.channels.CompletionHandler
                public void completed(Integer nBytes, ByteBuffer attachment) {
                    Nio2SocketWrapper.this.writeNotify = false;
                    boolean notify = false;
                    synchronized (Nio2SocketWrapper.this.writeCompletionHandler) {
                        if (nBytes.intValue() < 0) {
                            failed((Throwable) new EOFException(SocketWrapperBase.sm.getString("iob.failedwrite")), attachment);
                        } else if (!Nio2SocketWrapper.this.nonBlockingWriteBuffer.isEmpty()) {
                            ByteBuffer[] array = Nio2SocketWrapper.this.nonBlockingWriteBuffer.toArray(attachment);
                            Nio2SocketWrapper.this.getSocket().write(array, 0, array.length, AbstractEndpoint.toTimeout(Nio2SocketWrapper.this.getWriteTimeout()), TimeUnit.MILLISECONDS, array, Nio2SocketWrapper.this.gatheringWriteCompletionHandler);
                        } else if (attachment.hasRemaining()) {
                            Nio2SocketWrapper.this.getSocket().write(attachment, AbstractEndpoint.toTimeout(Nio2SocketWrapper.this.getWriteTimeout()), TimeUnit.MILLISECONDS, attachment, Nio2SocketWrapper.this.writeCompletionHandler);
                        } else {
                            if (!Nio2SocketWrapper.this.writeInterest || Nio2Endpoint.isInline()) {
                                Nio2SocketWrapper.this.writePending.release();
                            } else {
                                Nio2SocketWrapper.this.writeNotify = true;
                                notify = true;
                            }
                            Nio2SocketWrapper.this.writeInterest = false;
                        }
                    }
                    if (notify) {
                        endpoint.processSocket(Nio2SocketWrapper.this, SocketEvent.OPEN_WRITE, true);
                    }
                }

                @Override // java.nio.channels.CompletionHandler
                public void failed(Throwable exc, ByteBuffer attachment) {
                    IOException ioe;
                    if (exc instanceof IOException) {
                        ioe = (IOException) exc;
                    } else {
                        ioe = new IOException(exc);
                    }
                    Nio2SocketWrapper.this.setError(ioe);
                    Nio2SocketWrapper.this.writePending.release();
                    endpoint.processSocket(Nio2SocketWrapper.this, SocketEvent.ERROR, true);
                }
            };
            this.gatheringWriteCompletionHandler = new CompletionHandler<Long, ByteBuffer[]>() { // from class: org.apache.tomcat.util.net.Nio2Endpoint.Nio2SocketWrapper.4
                @Override // java.nio.channels.CompletionHandler
                public void completed(Long nBytes, ByteBuffer[] attachment) {
                    Nio2SocketWrapper.this.writeNotify = false;
                    boolean notify = false;
                    synchronized (Nio2SocketWrapper.this.writeCompletionHandler) {
                        if (nBytes.longValue() < 0) {
                            failed((Throwable) new EOFException(SocketWrapperBase.sm.getString("iob.failedwrite")), attachment);
                        } else if (Nio2SocketWrapper.this.nonBlockingWriteBuffer.isEmpty() && !Nio2SocketWrapper.arrayHasData(attachment)) {
                            if (!Nio2SocketWrapper.this.writeInterest || Nio2Endpoint.isInline()) {
                                Nio2SocketWrapper.this.writePending.release();
                            } else {
                                Nio2SocketWrapper.this.writeNotify = true;
                                notify = true;
                            }
                            Nio2SocketWrapper.this.writeInterest = false;
                        } else {
                            ByteBuffer[] array = Nio2SocketWrapper.this.nonBlockingWriteBuffer.toArray(attachment);
                            Nio2SocketWrapper.this.getSocket().write(array, 0, array.length, AbstractEndpoint.toTimeout(Nio2SocketWrapper.this.getWriteTimeout()), TimeUnit.MILLISECONDS, array, Nio2SocketWrapper.this.gatheringWriteCompletionHandler);
                        }
                    }
                    if (notify) {
                        endpoint.processSocket(Nio2SocketWrapper.this, SocketEvent.OPEN_WRITE, true);
                    }
                }

                @Override // java.nio.channels.CompletionHandler
                public void failed(Throwable exc, ByteBuffer[] attachment) {
                    IOException ioe;
                    if (exc instanceof IOException) {
                        ioe = (IOException) exc;
                    } else {
                        ioe = new IOException(exc);
                    }
                    Nio2SocketWrapper.this.setError(ioe);
                    Nio2SocketWrapper.this.writePending.release();
                    endpoint.processSocket(Nio2SocketWrapper.this, SocketEvent.ERROR, true);
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean arrayHasData(ByteBuffer[] byteBuffers) {
            for (ByteBuffer byteBuffer : byteBuffers) {
                if (byteBuffer.hasRemaining()) {
                    return true;
                }
            }
            return false;
        }

        public void setSendfileData(SendfileData sf) {
            this.sendfileData = sf;
        }

        public SendfileData getSendfileData() {
            return this.sendfileData;
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public boolean isReadyForRead() throws IOException {
            synchronized (this.readCompletionHandler) {
                if (this.readNotify) {
                    return true;
                }
                if (!this.readPending.tryAcquire()) {
                    this.readInterest = true;
                    return false;
                }
                if (!this.socketBufferHandler.isReadBufferEmpty()) {
                    this.readPending.release();
                    return true;
                }
                boolean isReady = fillReadBuffer(false) > 0;
                if (!isReady) {
                    this.readInterest = true;
                }
                return isReady;
            }
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public boolean isReadyForWrite() {
            synchronized (this.writeCompletionHandler) {
                if (this.writeNotify) {
                    return true;
                }
                if (!this.writePending.tryAcquire()) {
                    this.writeInterest = true;
                    return false;
                }
                if (this.socketBufferHandler.isWriteBufferEmpty() && this.nonBlockingWriteBuffer.isEmpty()) {
                    this.writePending.release();
                    return true;
                }
                boolean isReady = !flushNonBlockingInternal(true);
                if (!isReady) {
                    this.writeInterest = true;
                }
                return isReady;
            }
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public int read(boolean block, byte[] b, int off, int len) throws InterruptedException, IOException {
            int i;
            checkError();
            if (Nio2Endpoint.log.isDebugEnabled()) {
                Nio2Endpoint.log.debug("Socket: [" + this + "], block: [" + block + "], length: [" + len + "]");
            }
            if (this.socketBufferHandler == null) {
                throw new IOException(sm.getString("socket.closed"));
            }
            if (!this.readNotify) {
                if (block) {
                    try {
                        this.readPending.acquire();
                    } catch (InterruptedException e) {
                        throw new IOException(e);
                    }
                } else if (!this.readPending.tryAcquire()) {
                    if (Nio2Endpoint.log.isDebugEnabled()) {
                        Nio2Endpoint.log.debug("Socket: [" + this + "], Read in progress. Returning [0]");
                        return 0;
                    }
                    return 0;
                }
            }
            int nRead = populateReadBuffer(b, off, len);
            if (nRead > 0) {
                this.readNotify = false;
                this.readPending.release();
                return nRead;
            }
            synchronized (this.readCompletionHandler) {
                int nRead2 = fillReadBuffer(block);
                if (nRead2 > 0) {
                    this.socketBufferHandler.configureReadBufferForRead();
                    nRead2 = Math.min(nRead2, len);
                    this.socketBufferHandler.getReadBuffer().get(b, off, nRead2);
                } else if (nRead2 == 0 && !block) {
                    this.readInterest = true;
                }
                if (Nio2Endpoint.log.isDebugEnabled()) {
                    Nio2Endpoint.log.debug("Socket: [" + this + "], Read: [" + nRead2 + "]");
                }
                i = nRead2;
            }
            return i;
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public int read(boolean block, ByteBuffer to) throws InterruptedException, IOException {
            int nRead;
            int i;
            checkError();
            if (this.socketBufferHandler == null) {
                throw new IOException(sm.getString("socket.closed"));
            }
            if (!this.readNotify) {
                if (block) {
                    try {
                        this.readPending.acquire();
                    } catch (InterruptedException e) {
                        throw new IOException(e);
                    }
                } else if (!this.readPending.tryAcquire()) {
                    if (Nio2Endpoint.log.isDebugEnabled()) {
                        Nio2Endpoint.log.debug("Socket: [" + this + "], Read in progress. Returning [0]");
                        return 0;
                    }
                    return 0;
                }
            }
            int nRead2 = populateReadBuffer(to);
            if (nRead2 > 0) {
                this.readNotify = false;
                this.readPending.release();
                return nRead2;
            }
            synchronized (this.readCompletionHandler) {
                int limit = this.socketBufferHandler.getReadBuffer().capacity();
                if (block && to.remaining() >= limit) {
                    to.limit(to.position() + limit);
                    nRead = fillReadBuffer(block, to);
                    if (Nio2Endpoint.log.isDebugEnabled()) {
                        Nio2Endpoint.log.debug("Socket: [" + this + "], Read direct from socket: [" + nRead + "]");
                    }
                } else {
                    nRead = fillReadBuffer(block);
                    if (Nio2Endpoint.log.isDebugEnabled()) {
                        Nio2Endpoint.log.debug("Socket: [" + this + "], Read into buffer: [" + nRead + "]");
                    }
                    if (nRead > 0) {
                        nRead = populateReadBuffer(to);
                    } else if (nRead == 0 && !block) {
                        this.readInterest = true;
                    }
                }
                i = nRead;
            }
            return i;
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public void close() {
            if (Nio2Endpoint.log.isDebugEnabled()) {
                Nio2Endpoint.log.debug("Calling [" + getEndpoint() + "].closeSocket([" + this + "])", new Exception());
            }
            try {
                getEndpoint().getHandler().release(this);
            } catch (Throwable e) {
                ExceptionUtils.handleThrowable(e);
                if (Nio2Endpoint.log.isDebugEnabled()) {
                    Nio2Endpoint.log.error("Channel close error", e);
                }
            }
            try {
                synchronized (getSocket()) {
                    if (!this.closed) {
                        this.closed = true;
                        getEndpoint().countDownConnection();
                    }
                    if (getSocket().isOpen()) {
                        getSocket().close(true);
                    }
                }
            } catch (Throwable e2) {
                ExceptionUtils.handleThrowable(e2);
                if (Nio2Endpoint.log.isDebugEnabled()) {
                    Nio2Endpoint.log.error("Channel close error", e2);
                }
            }
            try {
                SendfileData data = getSendfileData();
                if (data != null && data.fchannel != null && data.fchannel.isOpen()) {
                    data.fchannel.close();
                }
            } catch (Throwable e3) {
                ExceptionUtils.handleThrowable(e3);
                if (Nio2Endpoint.log.isDebugEnabled()) {
                    Nio2Endpoint.log.error("Channel close error", e3);
                }
            }
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public boolean isClosed() {
            return this.closed;
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public boolean hasAsyncIO() {
            return getEndpoint().getUseAsyncIO();
        }

        /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/net/Nio2Endpoint$Nio2SocketWrapper$OperationState.class */
        private static class OperationState<A> {
            private final boolean read;
            private final ByteBuffer[] buffers;
            private final int offset;
            private final int length;
            private final A attachment;
            private final long timeout;
            private final TimeUnit unit;
            private final SocketWrapperBase.BlockingMode block;
            private final SocketWrapperBase.CompletionCheck check;
            private final CompletionHandler<Long, ? super A> handler;
            private final Semaphore semaphore;
            private volatile long nBytes;
            private volatile SocketWrapperBase.CompletionState state;

            /*  JADX ERROR: Failed to decode insn: 0x0007: MOVE_MULTI
                java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
                	at java.base/java.lang.System.arraycopy(Native Method)
                	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
                	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
                	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
                	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
                	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
                	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
                	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
                	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
                	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
                	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
                */
            static /* synthetic */ long access$1914(org.apache.tomcat.util.net.Nio2Endpoint.Nio2SocketWrapper.OperationState r6, long r7) {
                /*
                    r0 = r6
                    r1 = r0
                    long r1 = r1.nBytes
                    r2 = r7
                    long r1 = r1 + r2
                    // decode failed: arraycopy: source index -1 out of bounds for object array[6]
                    r0.nBytes = r1
                    return r-1
                */
                throw new UnsupportedOperationException("Method not decompiled: org.apache.tomcat.util.net.Nio2Endpoint.Nio2SocketWrapper.OperationState.access$1914(org.apache.tomcat.util.net.Nio2Endpoint$Nio2SocketWrapper$OperationState, long):long");
            }

            private OperationState(boolean read, ByteBuffer[] buffers, int offset, int length, SocketWrapperBase.BlockingMode block, long timeout, TimeUnit unit, A attachment, SocketWrapperBase.CompletionCheck check, CompletionHandler<Long, ? super A> handler, Semaphore semaphore) {
                this.nBytes = 0L;
                this.state = SocketWrapperBase.CompletionState.PENDING;
                this.read = read;
                this.buffers = buffers;
                this.offset = offset;
                this.length = length;
                this.block = block;
                this.timeout = timeout;
                this.unit = unit;
                this.attachment = attachment;
                this.check = check;
                this.handler = handler;
                this.semaphore = semaphore;
            }
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public <A> SocketWrapperBase.CompletionState read(ByteBuffer[] dsts, int offset, int length, SocketWrapperBase.BlockingMode block, long timeout, TimeUnit unit, A attachment, SocketWrapperBase.CompletionCheck check, CompletionHandler<Long, ? super A> handler) {
            IOException ioe = getError();
            if (ioe != null) {
                handler.failed(ioe, attachment);
                return SocketWrapperBase.CompletionState.ERROR;
            }
            if (timeout == -1) {
                timeout = AbstractEndpoint.toTimeout(getReadTimeout());
            }
            this.readNotify = true;
            if (block == SocketWrapperBase.BlockingMode.BLOCK || block == SocketWrapperBase.BlockingMode.SEMI_BLOCK) {
                try {
                    if (!this.readPending.tryAcquire(timeout, unit)) {
                        handler.failed(new SocketTimeoutException(), attachment);
                        return SocketWrapperBase.CompletionState.ERROR;
                    }
                } catch (InterruptedException e) {
                    handler.failed(e, attachment);
                    return SocketWrapperBase.CompletionState.ERROR;
                }
            } else if (!this.readPending.tryAcquire()) {
                if (block == SocketWrapperBase.BlockingMode.NON_BLOCK) {
                    return SocketWrapperBase.CompletionState.NOT_DONE;
                }
                handler.failed(new ReadPendingException(), attachment);
                return SocketWrapperBase.CompletionState.ERROR;
            }
            OperationState<A> state = new OperationState<>(true, dsts, offset, length, block, timeout, unit, attachment, check, handler, this.readPending);
            VectoredIOCompletionHandler<A> completion = new VectoredIOCompletionHandler<>(this, null);
            Nio2Endpoint.startInline();
            long nBytes = 0;
            if (!this.socketBufferHandler.isReadBufferEmpty()) {
                synchronized (this.readCompletionHandler) {
                    this.socketBufferHandler.configureReadBufferForRead();
                    for (int i = 0; i < length && !this.socketBufferHandler.isReadBufferEmpty(); i++) {
                        nBytes += transfer(this.socketBufferHandler.getReadBuffer(), dsts[offset + i]);
                    }
                }
                if (nBytes > 0) {
                    completion.completed(Long.valueOf(nBytes), (OperationState) state);
                }
            }
            if (nBytes == 0) {
                getSocket().read(dsts, offset, length, timeout, unit, state, completion);
            }
            Nio2Endpoint.endInline();
            if (block == SocketWrapperBase.BlockingMode.BLOCK) {
                synchronized (state) {
                    if (((OperationState) state).state == SocketWrapperBase.CompletionState.PENDING) {
                        try {
                            state.wait(unit.toMillis(timeout));
                            if (((OperationState) state).state == SocketWrapperBase.CompletionState.PENDING) {
                                return SocketWrapperBase.CompletionState.ERROR;
                            }
                        } catch (InterruptedException e2) {
                            handler.failed(new SocketTimeoutException(), attachment);
                            return SocketWrapperBase.CompletionState.ERROR;
                        }
                    }
                }
            }
            return ((OperationState) state).state;
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public <A> SocketWrapperBase.CompletionState write(ByteBuffer[] srcs, int offset, int length, SocketWrapperBase.BlockingMode block, long timeout, TimeUnit unit, A attachment, SocketWrapperBase.CompletionCheck check, CompletionHandler<Long, ? super A> handler) {
            IOException ioe = getError();
            if (ioe != null) {
                handler.failed(ioe, attachment);
                return SocketWrapperBase.CompletionState.ERROR;
            }
            if (timeout == -1) {
                timeout = AbstractEndpoint.toTimeout(getWriteTimeout());
            }
            this.writeNotify = true;
            if (block == SocketWrapperBase.BlockingMode.BLOCK || block == SocketWrapperBase.BlockingMode.SEMI_BLOCK) {
                try {
                    if (!this.writePending.tryAcquire(timeout, unit)) {
                        handler.failed(new SocketTimeoutException(), attachment);
                        return SocketWrapperBase.CompletionState.ERROR;
                    }
                } catch (InterruptedException e) {
                    handler.failed(e, attachment);
                    return SocketWrapperBase.CompletionState.ERROR;
                }
            } else if (!this.writePending.tryAcquire()) {
                if (block == SocketWrapperBase.BlockingMode.NON_BLOCK) {
                    return SocketWrapperBase.CompletionState.NOT_DONE;
                }
                handler.failed(new WritePendingException(), attachment);
                return SocketWrapperBase.CompletionState.ERROR;
            }
            if (!this.socketBufferHandler.isWriteBufferEmpty()) {
                try {
                    doWrite(true);
                } catch (IOException e2) {
                    handler.failed(e2, attachment);
                    return SocketWrapperBase.CompletionState.ERROR;
                }
            }
            OperationState<A> state = new OperationState<>(false, srcs, offset, length, block, timeout, unit, attachment, check, handler, this.writePending);
            VectoredIOCompletionHandler<A> completion = new VectoredIOCompletionHandler<>(this, null);
            Nio2Endpoint.startInline();
            getSocket().write(srcs, offset, length, timeout, unit, state, completion);
            Nio2Endpoint.endInline();
            if (block == SocketWrapperBase.BlockingMode.BLOCK) {
                synchronized (state) {
                    if (((OperationState) state).state == SocketWrapperBase.CompletionState.PENDING) {
                        try {
                            state.wait(unit.toMillis(timeout));
                            if (((OperationState) state).state == SocketWrapperBase.CompletionState.PENDING) {
                                return SocketWrapperBase.CompletionState.ERROR;
                            }
                        } catch (InterruptedException e3) {
                            handler.failed(new SocketTimeoutException(), attachment);
                            return SocketWrapperBase.CompletionState.ERROR;
                        }
                    }
                }
            }
            return ((OperationState) state).state;
        }

        /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/net/Nio2Endpoint$Nio2SocketWrapper$VectoredIOCompletionHandler.class */
        private class VectoredIOCompletionHandler<A> implements CompletionHandler<Long, OperationState<A>> {
            final /* synthetic */ Nio2SocketWrapper this$0;

            private VectoredIOCompletionHandler(Nio2SocketWrapper nio2SocketWrapper) {
                this.this$0 = nio2SocketWrapper;
            }

            @Override // java.nio.channels.CompletionHandler
            public /* bridge */ /* synthetic */ void failed(Throwable th, Object obj) {
                failed(th, (OperationState) obj);
            }

            @Override // java.nio.channels.CompletionHandler
            public /* bridge */ /* synthetic */ void completed(Long l, Object obj) {
                completed(l, (OperationState) obj);
            }

            /* synthetic */ VectoredIOCompletionHandler(Nio2SocketWrapper x0, AnonymousClass1 x1) {
                this(x0);
            }

            /* JADX WARN: Failed to check method for inline after forced processorg.apache.tomcat.util.net.Nio2Endpoint.Nio2SocketWrapper.OperationState.access$1914(org.apache.tomcat.util.net.Nio2Endpoint$Nio2SocketWrapper$OperationState, long):long */
            /* JADX WARN: Multi-variable type inference failed */
            public void completed(Long nBytes, OperationState<A> state) {
                if (nBytes.longValue() < 0) {
                    failed((Throwable) new EOFException(), (OperationState) state);
                    return;
                }
                OperationState.access$1914(state, nBytes.longValue());
                SocketWrapperBase.CompletionState currentState = Nio2Endpoint.isInline() ? SocketWrapperBase.CompletionState.INLINE : SocketWrapperBase.CompletionState.DONE;
                boolean complete = true;
                boolean completion = true;
                if (((OperationState) state).check != null) {
                    SocketWrapperBase.CompletionHandlerCall call = ((OperationState) state).check.callHandler(currentState, ((OperationState) state).buffers, ((OperationState) state).offset, ((OperationState) state).length);
                    if (call == SocketWrapperBase.CompletionHandlerCall.CONTINUE) {
                        complete = false;
                    } else if (call == SocketWrapperBase.CompletionHandlerCall.NONE) {
                        completion = false;
                    }
                }
                if (!complete) {
                    if (((OperationState) state).read) {
                        this.this$0.getSocket().read(((OperationState) state).buffers, ((OperationState) state).offset, ((OperationState) state).length, ((OperationState) state).timeout, ((OperationState) state).unit, state, this);
                        return;
                    } else {
                        this.this$0.getSocket().write(((OperationState) state).buffers, ((OperationState) state).offset, ((OperationState) state).length, ((OperationState) state).timeout, ((OperationState) state).unit, state, this);
                        return;
                    }
                }
                boolean notify = false;
                ((OperationState) state).semaphore.release();
                if (((OperationState) state).block != SocketWrapperBase.BlockingMode.BLOCK || currentState == SocketWrapperBase.CompletionState.INLINE) {
                    ((OperationState) state).state = currentState;
                } else {
                    notify = true;
                }
                if (completion && ((OperationState) state).handler != null) {
                    ((OperationState) state).handler.completed(Long.valueOf(((OperationState) state).nBytes), ((OperationState) state).attachment);
                }
                if (notify) {
                    synchronized (state) {
                        ((OperationState) state).state = currentState;
                        state.notify();
                    }
                }
            }

            /* JADX WARN: Multi-variable type inference failed */
            public void failed(Throwable exc, OperationState<A> state) {
                IOException ioe = null;
                if (exc instanceof InterruptedByTimeoutException) {
                    ioe = new SocketTimeoutException();
                    exc = ioe;
                } else if (exc instanceof IOException) {
                    ioe = (IOException) exc;
                }
                this.this$0.setError(ioe);
                boolean notify = false;
                ((OperationState) state).semaphore.release();
                if (((OperationState) state).block != SocketWrapperBase.BlockingMode.BLOCK) {
                    ((OperationState) state).state = Nio2Endpoint.isInline() ? SocketWrapperBase.CompletionState.ERROR : SocketWrapperBase.CompletionState.DONE;
                } else {
                    notify = true;
                }
                if (((OperationState) state).handler != null) {
                    ((OperationState) state).handler.failed(exc, ((OperationState) state).attachment);
                }
                if (notify) {
                    synchronized (state) {
                        ((OperationState) state).state = Nio2Endpoint.isInline() ? SocketWrapperBase.CompletionState.ERROR : SocketWrapperBase.CompletionState.DONE;
                        state.notify();
                    }
                }
            }
        }

        private int fillReadBuffer(boolean block) throws IOException {
            this.socketBufferHandler.configureReadBufferForWrite();
            return fillReadBuffer(block, this.socketBufferHandler.getReadBuffer());
        }

        private int fillReadBuffer(boolean block, ByteBuffer to) throws IOException {
            int nRead = 0;
            Future<Integer> integer = null;
            try {
                if (block) {
                    try {
                        try {
                            try {
                                Future<Integer> integer2 = getSocket().read(to);
                                long timeout = getReadTimeout();
                                if (timeout > 0) {
                                    nRead = integer2.get(timeout, TimeUnit.MILLISECONDS).intValue();
                                } else {
                                    nRead = integer2.get().intValue();
                                }
                            } catch (TimeoutException e) {
                                integer.cancel(true);
                                throw new SocketTimeoutException();
                            }
                        } catch (ExecutionException e2) {
                            if (e2.getCause() instanceof IOException) {
                                throw ((IOException) e2.getCause());
                            }
                            throw new IOException(e2);
                        }
                    } catch (InterruptedException e3) {
                        throw new IOException(e3);
                    }
                } else {
                    Nio2Endpoint.startInline();
                    getSocket().read(to, AbstractEndpoint.toTimeout(getReadTimeout()), TimeUnit.MILLISECONDS, to, this.readCompletionHandler);
                    Nio2Endpoint.endInline();
                    if (this.readPending.availablePermits() == 1) {
                        nRead = to.position();
                    }
                }
                return nRead;
            } finally {
                this.readPending.release();
            }
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        protected void writeNonBlocking(byte[] buf, int off, int len) throws IOException {
            synchronized (this.writeCompletionHandler) {
                checkError();
                if (this.writeNotify || this.writePending.tryAcquire()) {
                    this.socketBufferHandler.configureWriteBufferForWrite();
                    int thisTime = transfer(buf, off, len, this.socketBufferHandler.getWriteBuffer());
                    int len2 = len - thisTime;
                    int off2 = off + thisTime;
                    if (len2 > 0) {
                        this.nonBlockingWriteBuffer.add(buf, off2, len2);
                    }
                    flushNonBlockingInternal(true);
                } else {
                    this.nonBlockingWriteBuffer.add(buf, off, len);
                }
            }
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        protected void writeNonBlocking(ByteBuffer from) throws IOException {
            writeNonBlockingInternal(from);
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        protected void writeNonBlockingInternal(ByteBuffer from) throws IOException {
            synchronized (this.writeCompletionHandler) {
                checkError();
                if (this.writeNotify || this.writePending.tryAcquire()) {
                    this.socketBufferHandler.configureWriteBufferForWrite();
                    transfer(from, this.socketBufferHandler.getWriteBuffer());
                    if (from.remaining() > 0) {
                        this.nonBlockingWriteBuffer.add(from);
                    }
                    flushNonBlockingInternal(true);
                } else {
                    this.nonBlockingWriteBuffer.add(from);
                }
            }
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        protected void doWrite(boolean block, ByteBuffer from) throws IOException {
            Future<Integer> integer = null;
            do {
                try {
                    integer = getSocket().write(from);
                    long timeout = getWriteTimeout();
                    if (timeout > 0) {
                        if (integer.get(timeout, TimeUnit.MILLISECONDS).intValue() < 0) {
                            throw new EOFException(sm.getString("iob.failedwrite"));
                        }
                    } else if (integer.get().intValue() < 0) {
                        throw new EOFException(sm.getString("iob.failedwrite"));
                    }
                } catch (InterruptedException e) {
                    throw new IOException(e);
                } catch (ExecutionException e2) {
                    if (e2.getCause() instanceof IOException) {
                        throw ((IOException) e2.getCause());
                    }
                    throw new IOException(e2);
                } catch (TimeoutException e3) {
                    integer.cancel(true);
                    throw new SocketTimeoutException();
                }
            } while (from.hasRemaining());
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        protected void flushBlocking() throws IOException {
            checkError();
            if (this.writePending.tryAcquire(AbstractEndpoint.toTimeout(getWriteTimeout()), TimeUnit.MILLISECONDS)) {
                this.writePending.release();
                super.flushBlocking();
                return;
            }
            throw new SocketTimeoutException();
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        protected boolean flushNonBlocking() throws IOException {
            checkError();
            return flushNonBlockingInternal(false);
        }

        private boolean flushNonBlockingInternal(boolean hasPermit) {
            boolean zHasDataToWrite;
            synchronized (this.writeCompletionHandler) {
                if (this.writeNotify || hasPermit || this.writePending.tryAcquire()) {
                    this.writeNotify = false;
                    this.socketBufferHandler.configureWriteBufferForRead();
                    if (!this.nonBlockingWriteBuffer.isEmpty()) {
                        ByteBuffer[] array = this.nonBlockingWriteBuffer.toArray(this.socketBufferHandler.getWriteBuffer());
                        Nio2Endpoint.startInline();
                        getSocket().write(array, 0, array.length, AbstractEndpoint.toTimeout(getWriteTimeout()), TimeUnit.MILLISECONDS, array, this.gatheringWriteCompletionHandler);
                        Nio2Endpoint.endInline();
                    } else if (this.socketBufferHandler.getWriteBuffer().hasRemaining()) {
                        Nio2Endpoint.startInline();
                        getSocket().write(this.socketBufferHandler.getWriteBuffer(), AbstractEndpoint.toTimeout(getWriteTimeout()), TimeUnit.MILLISECONDS, this.socketBufferHandler.getWriteBuffer(), this.writeCompletionHandler);
                        Nio2Endpoint.endInline();
                    } else {
                        if (!hasPermit) {
                            this.writePending.release();
                        }
                        this.writeInterest = false;
                    }
                }
                zHasDataToWrite = hasDataToWrite();
            }
            return zHasDataToWrite;
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public boolean hasDataToRead() {
            boolean z;
            synchronized (this.readCompletionHandler) {
                z = (this.socketBufferHandler.isReadBufferEmpty() && !this.readNotify && getError() == null) ? false : true;
            }
            return z;
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public boolean hasDataToWrite() {
            boolean z;
            synchronized (this.writeCompletionHandler) {
                z = (this.socketBufferHandler.isWriteBufferEmpty() && this.nonBlockingWriteBuffer.isEmpty() && !this.writeNotify && this.writePending.availablePermits() != 0 && getError() == null) ? false : true;
            }
            return z;
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public boolean isReadPending() {
            boolean z;
            synchronized (this.readCompletionHandler) {
                z = this.readPending.availablePermits() == 0;
            }
            return z;
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public boolean isWritePending() {
            boolean z;
            synchronized (this.writeCompletionHandler) {
                z = this.writePending.availablePermits() == 0;
            }
            return z;
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public boolean awaitReadComplete(long timeout, TimeUnit unit) {
            synchronized (this.readCompletionHandler) {
                try {
                    if (this.readNotify) {
                        return true;
                    }
                    if (this.readPending.tryAcquire(timeout, unit)) {
                        this.readPending.release();
                        return true;
                    }
                    return false;
                } catch (InterruptedException e) {
                    return false;
                }
            }
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public boolean awaitWriteComplete(long timeout, TimeUnit unit) {
            synchronized (this.writeCompletionHandler) {
                try {
                    if (this.writeNotify) {
                        return true;
                    }
                    if (this.writePending.tryAcquire(timeout, unit)) {
                        this.writePending.release();
                        return true;
                    }
                    return false;
                } catch (InterruptedException e) {
                    return false;
                }
            }
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public void registerReadInterest() {
            synchronized (this.readCompletionHandler) {
                if (this.readNotify) {
                    return;
                }
                this.readInterest = true;
                if (this.readPending.tryAcquire()) {
                    try {
                        if (fillReadBuffer(false) > 0) {
                            getEndpoint().processSocket(this, SocketEvent.OPEN_READ, true);
                        }
                    } catch (IOException e) {
                        setError(e);
                    }
                }
            }
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public void registerWriteInterest() {
            synchronized (this.writeCompletionHandler) {
                if (this.writeNotify) {
                    return;
                }
                this.writeInterest = true;
                if (this.writePending.availablePermits() == 1) {
                    getEndpoint().processSocket(this, SocketEvent.OPEN_WRITE, true);
                }
            }
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public SendfileDataBase createSendfileData(String filename, long pos, long length) {
            return new SendfileData(filename, pos, length);
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public SendfileState processSendfile(SendfileDataBase sendfileData) throws IOException {
            SendfileData data = (SendfileData) sendfileData;
            setSendfileData(data);
            if (data.fchannel == null || !data.fchannel.isOpen()) {
                Path path = new File(sendfileData.fileName).toPath();
                try {
                    data.fchannel = FileChannel.open(path, StandardOpenOption.READ).position(sendfileData.pos);
                } catch (IOException e) {
                    return SendfileState.ERROR;
                }
            }
            getSocket().getBufHandler().configureWriteBufferForWrite();
            ByteBuffer buffer = getSocket().getBufHandler().getWriteBuffer();
            try {
                int nRead = data.fchannel.read(buffer);
                if (nRead >= 0) {
                    data.length -= nRead;
                    getSocket().getBufHandler().configureWriteBufferForRead();
                    Nio2Endpoint.startInline();
                    getSocket().write(buffer, AbstractEndpoint.toTimeout(getWriteTimeout()), TimeUnit.MILLISECONDS, data, this.sendfileHandler);
                    Nio2Endpoint.endInline();
                    if (!data.doneInline) {
                        return SendfileState.PENDING;
                    }
                    if (data.error) {
                        return SendfileState.ERROR;
                    }
                    return SendfileState.DONE;
                }
                return SendfileState.ERROR;
            } catch (IOException e2) {
                return SendfileState.ERROR;
            }
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        protected void populateRemoteAddr() throws IOException {
            SocketAddress socketAddress = null;
            try {
                socketAddress = getSocket().getIOChannel().getRemoteAddress();
            } catch (IOException e) {
            }
            if (socketAddress instanceof InetSocketAddress) {
                this.remoteAddr = ((InetSocketAddress) socketAddress).getAddress().getHostAddress();
            }
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        protected void populateRemoteHost() throws IOException {
            SocketAddress socketAddress = null;
            try {
                socketAddress = getSocket().getIOChannel().getRemoteAddress();
            } catch (IOException e) {
                Nio2Endpoint.log.warn(sm.getString("endpoint.warn.noRemoteHost", getSocket()), e);
            }
            if (socketAddress instanceof InetSocketAddress) {
                this.remoteHost = ((InetSocketAddress) socketAddress).getAddress().getHostName();
                if (this.remoteAddr == null) {
                    this.remoteAddr = ((InetSocketAddress) socketAddress).getAddress().getHostAddress();
                }
            }
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        protected void populateRemotePort() throws IOException {
            SocketAddress socketAddress = null;
            try {
                socketAddress = getSocket().getIOChannel().getRemoteAddress();
            } catch (IOException e) {
                Nio2Endpoint.log.warn(sm.getString("endpoint.warn.noRemotePort", getSocket()), e);
            }
            if (socketAddress instanceof InetSocketAddress) {
                this.remotePort = ((InetSocketAddress) socketAddress).getPort();
            }
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        protected void populateLocalName() throws IOException {
            SocketAddress socketAddress = null;
            try {
                socketAddress = getSocket().getIOChannel().getLocalAddress();
            } catch (IOException e) {
                Nio2Endpoint.log.warn(sm.getString("endpoint.warn.noLocalName", getSocket()), e);
            }
            if (socketAddress instanceof InetSocketAddress) {
                this.localName = ((InetSocketAddress) socketAddress).getHostName();
            }
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        protected void populateLocalAddr() throws IOException {
            SocketAddress socketAddress = null;
            try {
                socketAddress = getSocket().getIOChannel().getLocalAddress();
            } catch (IOException e) {
                Nio2Endpoint.log.warn(sm.getString("endpoint.warn.noLocalAddr", getSocket()), e);
            }
            if (socketAddress instanceof InetSocketAddress) {
                this.localAddr = ((InetSocketAddress) socketAddress).getAddress().getHostAddress();
            }
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        protected void populateLocalPort() throws IOException {
            SocketAddress socketAddress = null;
            try {
                socketAddress = getSocket().getIOChannel().getLocalAddress();
            } catch (IOException e) {
                Nio2Endpoint.log.warn(sm.getString("endpoint.warn.noLocalPort", getSocket()), e);
            }
            if (socketAddress instanceof InetSocketAddress) {
                this.localPort = ((InetSocketAddress) socketAddress).getPort();
            }
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public SSLSupport getSslSupport(String clientCertProvider) {
            if (getSocket() instanceof SecureNio2Channel) {
                SecureNio2Channel ch2 = (SecureNio2Channel) getSocket();
                SSLSession session = ch2.getSslEngine().getSession();
                return ((Nio2Endpoint) getEndpoint()).getSslImplementation().getSSLSupport(session);
            }
            return null;
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public void doClientAuth(SSLSupport sslSupport) throws IOException {
            SecureNio2Channel sslChannel = (SecureNio2Channel) getSocket();
            SSLEngine engine = sslChannel.getSslEngine();
            if (!engine.getNeedClientAuth()) {
                engine.setNeedClientAuth(true);
                sslChannel.rehandshake();
                ((JSSESupport) sslSupport).setSession(engine.getSession());
            }
        }

        @Override // org.apache.tomcat.util.net.SocketWrapperBase
        public void setAppReadBufHandler(ApplicationBufferHandler handler) {
            getSocket().setAppReadBufHandler(handler);
        }
    }

    public static void startInline() {
        inlineCompletion.set(Boolean.TRUE);
    }

    public static void endInline() {
        inlineCompletion.set(Boolean.FALSE);
    }

    public static boolean isInline() {
        Boolean flag = inlineCompletion.get();
        if (flag == null) {
            return false;
        }
        return flag.booleanValue();
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/net/Nio2Endpoint$SocketProcessor.class */
    protected class SocketProcessor extends SocketProcessorBase<Nio2Channel> {
        final /* synthetic */ Nio2Endpoint this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SocketProcessor(Nio2Endpoint nio2Endpoint, SocketWrapperBase<Nio2Channel> socketWrapper, SocketEvent event) {
            super(socketWrapper, event);
            this.this$0 = nio2Endpoint;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // org.apache.tomcat.util.net.SocketProcessorBase
        protected void doRun() {
            int handshake;
            boolean launch = false;
            try {
                try {
                    try {
                        try {
                            if (((Nio2Channel) this.socketWrapper.getSocket()).isHandshakeComplete()) {
                                handshake = 0;
                            } else if (this.event == SocketEvent.STOP || this.event == SocketEvent.DISCONNECT || this.event == SocketEvent.ERROR) {
                                handshake = -1;
                            } else {
                                handshake = ((Nio2Channel) this.socketWrapper.getSocket()).handshake();
                                this.event = SocketEvent.OPEN_READ;
                            }
                        } catch (VirtualMachineError vme) {
                            ExceptionUtils.handleThrowable(vme);
                            if (0 != 0) {
                                try {
                                    this.this$0.getExecutor().execute(new SocketProcessor(this.this$0, this.socketWrapper, SocketEvent.OPEN_READ));
                                } catch (NullPointerException npe) {
                                    if (this.this$0.running) {
                                        Nio2Endpoint.log.error(AbstractEndpoint.sm.getString("endpoint.launch.fail"), npe);
                                    }
                                }
                            }
                            this.socketWrapper = null;
                            this.event = null;
                            if (!this.this$0.running || this.this$0.paused) {
                                return;
                            }
                            this.this$0.processorCache.push(this);
                            return;
                        }
                    } catch (Throwable t) {
                        Nio2Endpoint.log.error(AbstractEndpoint.sm.getString("endpoint.processing.fail"), t);
                        if (this.socketWrapper != null) {
                            ((Nio2SocketWrapper) this.socketWrapper).close();
                        }
                        if (0 != 0) {
                            try {
                                this.this$0.getExecutor().execute(new SocketProcessor(this.this$0, this.socketWrapper, SocketEvent.OPEN_READ));
                            } catch (NullPointerException npe2) {
                                if (this.this$0.running) {
                                    Nio2Endpoint.log.error(AbstractEndpoint.sm.getString("endpoint.launch.fail"), npe2);
                                }
                            }
                        }
                        this.socketWrapper = null;
                        this.event = null;
                        if (!this.this$0.running || this.this$0.paused) {
                            return;
                        }
                        this.this$0.processorCache.push(this);
                        return;
                    }
                } catch (IOException x) {
                    handshake = -1;
                    if (Nio2Endpoint.log.isDebugEnabled()) {
                        Nio2Endpoint.log.debug(AbstractEndpoint.sm.getString("endpoint.err.handshake"), x);
                    }
                }
                if (handshake == 0) {
                    AbstractEndpoint.Handler.SocketState socketState = AbstractEndpoint.Handler.SocketState.OPEN;
                    AbstractEndpoint.Handler.SocketState state = this.event == null ? this.this$0.getHandler().process(this.socketWrapper, SocketEvent.OPEN_READ) : this.this$0.getHandler().process(this.socketWrapper, this.event);
                    if (state == AbstractEndpoint.Handler.SocketState.CLOSED) {
                        this.socketWrapper.close();
                        if (this.this$0.running && !this.this$0.paused && !this.this$0.nioChannels.push(this.socketWrapper.getSocket())) {
                            ((Nio2Channel) this.socketWrapper.getSocket()).free();
                        }
                    } else if (state == AbstractEndpoint.Handler.SocketState.UPGRADING) {
                        launch = true;
                    }
                } else if (handshake == -1) {
                    this.socketWrapper.close();
                    if (this.this$0.running && !this.this$0.paused && !this.this$0.nioChannels.push(this.socketWrapper.getSocket())) {
                        ((Nio2Channel) this.socketWrapper.getSocket()).free();
                    }
                }
                if (launch) {
                    try {
                        this.this$0.getExecutor().execute(new SocketProcessor(this.this$0, this.socketWrapper, SocketEvent.OPEN_READ));
                    } catch (NullPointerException npe3) {
                        if (this.this$0.running) {
                            Nio2Endpoint.log.error(AbstractEndpoint.sm.getString("endpoint.launch.fail"), npe3);
                        }
                    }
                }
                this.socketWrapper = null;
                this.event = null;
                if (!this.this$0.running || this.this$0.paused) {
                    return;
                }
                this.this$0.processorCache.push(this);
            } catch (Throwable th) {
                if (0 != 0) {
                    try {
                        this.this$0.getExecutor().execute(new SocketProcessor(this.this$0, this.socketWrapper, SocketEvent.OPEN_READ));
                    } catch (NullPointerException npe4) {
                        if (this.this$0.running) {
                            Nio2Endpoint.log.error(AbstractEndpoint.sm.getString("endpoint.launch.fail"), npe4);
                        }
                    }
                }
                this.socketWrapper = null;
                this.event = null;
                if (this.this$0.running && !this.this$0.paused) {
                    this.this$0.processorCache.push(this);
                }
                throw th;
            }
        }
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/net/Nio2Endpoint$SendfileData.class */
    public static class SendfileData extends SendfileDataBase {
        private FileChannel fchannel;
        private boolean doneInline;
        private boolean error;

        public SendfileData(String filename, long pos, long length) {
            super(filename, pos, length);
            this.doneInline = false;
            this.error = false;
        }
    }
}
