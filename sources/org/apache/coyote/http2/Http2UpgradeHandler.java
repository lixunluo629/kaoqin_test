package org.apache.coyote.http2;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import javax.servlet.http.WebConnection;
import org.apache.coyote.Adapter;
import org.apache.coyote.CloseNowException;
import org.apache.coyote.ProtocolException;
import org.apache.coyote.Request;
import org.apache.coyote.http11.upgrade.InternalHttpUpgradeHandler;
import org.apache.coyote.http2.HpackDecoder;
import org.apache.coyote.http2.HpackEncoder;
import org.apache.coyote.http2.Http2Parser;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.MimeHeaders;
import org.apache.tomcat.util.net.SSLSupport;
import org.apache.tomcat.util.net.SocketEvent;
import org.apache.tomcat.util.net.SocketWrapperBase;
import org.apache.tomcat.util.res.StringManager;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http2/Http2UpgradeHandler.class */
public class Http2UpgradeHandler extends AbstractStream implements InternalHttpUpgradeHandler, Http2Parser.Input, Http2Parser.Output {
    private static final int FLAG_END_OF_STREAM = 1;
    private static final int FLAG_END_OF_HEADERS = 4;
    private static final String HTTP2_SETTINGS_HEADER = "HTTP2-Settings";
    private final String connectionId;
    private final Http2Protocol protocol;
    private final Adapter adapter;
    private volatile SocketWrapperBase<?> socketWrapper;
    private volatile SSLSupport sslSupport;
    private volatile Http2Parser parser;
    private AtomicReference<ConnectionState> connectionState;
    private volatile long pausedNanoTime;
    private final ConnectionSettingsRemote remoteSettings;
    private final ConnectionSettingsLocal localSettings;
    private HpackDecoder hpackDecoder;
    private HpackEncoder hpackEncoder;
    private long readTimeout;
    private long keepAliveTimeout;
    private long writeTimeout;
    private final Map<Integer, Stream> streams;
    private final AtomicInteger activeRemoteStreamCount;
    private volatile int maxActiveRemoteStreamId;
    private volatile int maxProcessedStreamId;
    private final AtomicInteger nextLocalStreamId;
    private final PingManager pingManager;
    private volatile int newStreamsSinceLastPrune;
    private final ConcurrentMap<AbstractStream, BacklogTracker> backLogStreams;
    private long backLogSize;
    private int maxConcurrentStreamExecution;
    private AtomicInteger streamConcurrency;
    private Queue<StreamRunnable> queuedRunnable;
    private Set<String> allowedTrailerHeaders;
    private int maxHeaderCount;
    private int maxHeaderSize;
    private int maxTrailerCount;
    private int maxTrailerSize;
    private final AtomicLong overheadCount;
    private static final Log log = LogFactory.getLog((Class<?>) Http2UpgradeHandler.class);
    private static final StringManager sm = StringManager.getManager((Class<?>) Http2UpgradeHandler.class);
    private static final AtomicInteger connectionIdGenerator = new AtomicInteger(0);
    private static final Integer STREAM_ID_ZERO = 0;
    private static final byte[] PING = {0, 0, 8, 6, 0, 0, 0, 0, 0};
    private static final byte[] PING_ACK = {0, 0, 8, 6, 1, 0, 0, 0, 0};
    private static final byte[] SETTINGS_ACK = {0, 0, 0, 4, 1, 0, 0, 0, 0};
    private static final byte[] GOAWAY = {7, 0, 0, 0, 0, 0};
    private static final HeaderSink HEADER_SINK = new HeaderSink();

    @Override // org.apache.coyote.http2.AbstractStream
    public /* bridge */ /* synthetic */ int getIdAsInt() {
        return super.getIdAsInt();
    }

    @Override // org.apache.coyote.http2.AbstractStream
    public /* bridge */ /* synthetic */ Integer getIdentifier() {
        return super.getIdentifier();
    }

    @Deprecated
    public Http2UpgradeHandler(Adapter adapter, Request coyoteRequest) {
        this(null, adapter, coyoteRequest);
    }

    public Http2UpgradeHandler(Http2Protocol protocol, Adapter adapter, Request coyoteRequest) {
        super(STREAM_ID_ZERO);
        this.connectionState = new AtomicReference<>(ConnectionState.NEW);
        this.pausedNanoTime = Long.MAX_VALUE;
        this.readTimeout = 5000L;
        this.keepAliveTimeout = org.apache.tomcat.websocket.Constants.DEFAULT_BLOCKING_SEND_TIMEOUT;
        this.writeTimeout = 5000L;
        this.streams = new ConcurrentHashMap();
        this.activeRemoteStreamCount = new AtomicInteger(0);
        this.maxActiveRemoteStreamId = -1;
        this.nextLocalStreamId = new AtomicInteger(2);
        this.pingManager = new PingManager();
        this.newStreamsSinceLastPrune = 0;
        this.backLogStreams = new ConcurrentHashMap();
        this.backLogSize = 0L;
        this.maxConcurrentStreamExecution = 20;
        this.streamConcurrency = null;
        this.queuedRunnable = null;
        this.allowedTrailerHeaders = Collections.emptySet();
        this.maxHeaderCount = 100;
        this.maxHeaderSize = 8192;
        this.maxTrailerCount = 100;
        this.maxTrailerSize = 8192;
        this.overheadCount = new AtomicLong(-10L);
        this.protocol = protocol;
        this.adapter = adapter;
        this.connectionId = Integer.toString(connectionIdGenerator.getAndIncrement());
        this.remoteSettings = new ConnectionSettingsRemote(this.connectionId);
        this.localSettings = new ConnectionSettingsLocal(this.connectionId);
        if (coyoteRequest != null) {
            if (log.isDebugEnabled()) {
                log.debug(sm.getString("upgradeHandler.upgrade", this.connectionId));
            }
            Stream stream = new Stream(1, this, coyoteRequest);
            this.streams.put(1, stream);
            this.maxActiveRemoteStreamId = 1;
            this.activeRemoteStreamCount.set(1);
            this.maxProcessedStreamId = 1;
        }
    }

    @Override // javax.servlet.http.HttpUpgradeHandler
    public void init(WebConnection webConnection) {
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("upgradeHandler.init", this.connectionId, this.connectionState.get()));
        }
        if (!this.connectionState.compareAndSet(ConnectionState.NEW, ConnectionState.CONNECTED)) {
            return;
        }
        if (this.maxConcurrentStreamExecution < this.localSettings.getMaxConcurrentStreams()) {
            this.streamConcurrency = new AtomicInteger(0);
            this.queuedRunnable = new ConcurrentLinkedQueue();
        }
        this.parser = new Http2Parser(this.connectionId, this, this);
        Stream stream = null;
        this.socketWrapper.setReadTimeout(getReadTimeout());
        this.socketWrapper.setWriteTimeout(getWriteTimeout());
        if (webConnection != null) {
            try {
                stream = getStream(1, true);
                String base64Settings = stream.getCoyoteRequest().getHeader(HTTP2_SETTINGS_HEADER);
                byte[] settings = Base64.decodeBase64(base64Settings);
                FrameType.SETTINGS.check(0, settings.length);
                for (int i = 0; i < settings.length % 6; i++) {
                    int id = ByteUtil.getTwoBytes(settings, i * 6);
                    long value = ByteUtil.getFourBytes(settings, (i * 6) + 2);
                    this.remoteSettings.set(Setting.valueOf(id), value);
                }
            } catch (Http2Exception e) {
                throw new ProtocolException(sm.getString("upgradeHandler.upgrade.fail", this.connectionId));
            }
        }
        writeSettings();
        try {
            this.parser.readConnectionPreface();
            if (log.isDebugEnabled()) {
                log.debug(sm.getString("upgradeHandler.prefaceReceived", this.connectionId));
            }
            try {
                this.pingManager.sendPing(true);
                if (webConnection != null) {
                    processStreamOnContainerThread(stream);
                }
            } catch (IOException ioe) {
                throw new ProtocolException(sm.getString("upgradeHandler.pingFailed"), ioe);
            }
        } catch (Http2Exception e2) {
            String msg = sm.getString("upgradeHandler.invalidPreface", this.connectionId);
            if (log.isDebugEnabled()) {
                log.debug(msg);
            }
            throw new ProtocolException(msg);
        }
    }

    private void processStreamOnContainerThread(Stream stream) {
        StreamProcessor streamProcessor = new StreamProcessor(this, stream, this.adapter, this.socketWrapper);
        streamProcessor.setSslSupport(this.sslSupport);
        processStreamOnContainerThread(streamProcessor, SocketEvent.OPEN_READ);
    }

    void processStreamOnContainerThread(StreamProcessor streamProcessor, SocketEvent event) {
        StreamRunnable streamRunnable = new StreamRunnable(streamProcessor, event);
        if (this.streamConcurrency == null) {
            this.socketWrapper.getEndpoint().getExecutor().execute(streamRunnable);
        } else if (getStreamConcurrency() < this.maxConcurrentStreamExecution) {
            increaseStreamConcurrency();
            this.socketWrapper.getEndpoint().getExecutor().execute(streamRunnable);
        } else {
            this.queuedRunnable.offer(streamRunnable);
        }
    }

    @Override // org.apache.coyote.http11.upgrade.InternalHttpUpgradeHandler
    public void setSocketWrapper(SocketWrapperBase<?> wrapper) {
        this.socketWrapper = wrapper;
    }

    @Override // org.apache.coyote.http11.upgrade.InternalHttpUpgradeHandler
    public void setSslSupport(SSLSupport sslSupport) {
        this.sslSupport = sslSupport;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0177  */
    @Override // org.apache.coyote.http11.upgrade.InternalHttpUpgradeHandler
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public org.apache.tomcat.util.net.AbstractEndpoint.Handler.SocketState upgradeDispatch(org.apache.tomcat.util.net.SocketEvent r10) throws org.apache.coyote.http2.ConnectionException {
        /*
            Method dump skipped, instructions count: 408
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.coyote.http2.Http2UpgradeHandler.upgradeDispatch(org.apache.tomcat.util.net.SocketEvent):org.apache.tomcat.util.net.AbstractEndpoint$Handler$SocketState");
    }

    ConnectionSettingsRemote getRemoteSettings() {
        return this.remoteSettings;
    }

    ConnectionSettingsLocal getLocalSettings() {
        return this.localSettings;
    }

    Http2Protocol getProtocol() {
        return this.protocol;
    }

    @Override // org.apache.coyote.http11.upgrade.InternalHttpUpgradeHandler
    public void pause() {
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("upgradeHandler.pause.entry", this.connectionId));
        }
        if (this.connectionState.compareAndSet(ConnectionState.CONNECTED, ConnectionState.PAUSING)) {
            this.pausedNanoTime = System.nanoTime();
            try {
                writeGoAwayFrame(Integer.MAX_VALUE, Http2Error.NO_ERROR.getCode(), null);
            } catch (IOException e) {
            }
        }
    }

    @Override // javax.servlet.http.HttpUpgradeHandler
    public void destroy() {
    }

    private void checkPauseState() throws IOException {
        if (this.connectionState.get() == ConnectionState.PAUSING && this.pausedNanoTime + this.pingManager.getRoundTripTimeNano() < System.nanoTime()) {
            this.connectionState.compareAndSet(ConnectionState.PAUSING, ConnectionState.PAUSED);
            writeGoAwayFrame(this.maxProcessedStreamId, Http2Error.NO_ERROR.getCode(), null);
        }
    }

    private int increaseStreamConcurrency() {
        return this.streamConcurrency.incrementAndGet();
    }

    private int decreaseStreamConcurrency() {
        return this.streamConcurrency.decrementAndGet();
    }

    private int getStreamConcurrency() {
        return this.streamConcurrency.get();
    }

    void executeQueuedStream() {
        StreamRunnable streamRunnable;
        if (this.streamConcurrency == null) {
            return;
        }
        decreaseStreamConcurrency();
        if (getStreamConcurrency() < this.maxConcurrentStreamExecution && (streamRunnable = this.queuedRunnable.poll()) != null) {
            increaseStreamConcurrency();
            this.socketWrapper.getEndpoint().getExecutor().execute(streamRunnable);
        }
    }

    void sendStreamReset(StreamException se) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("upgradeHandler.rst.debug", this.connectionId, Integer.toString(se.getStreamId()), se.getError(), se.getMessage()));
        }
        byte[] rstFrame = new byte[13];
        ByteUtil.setThreeBytes(rstFrame, 0, 4);
        rstFrame[3] = FrameType.RST.getIdByte();
        ByteUtil.set31Bits(rstFrame, 5, se.getStreamId());
        ByteUtil.setFourBytes(rstFrame, 9, se.getError().getCode());
        synchronized (this.socketWrapper) {
            this.socketWrapper.write(true, rstFrame, 0, rstFrame.length);
            this.socketWrapper.flush(true);
        }
    }

    void closeConnection(Http2Exception ce) {
        try {
            writeGoAwayFrame(this.maxProcessedStreamId, ce.getError().getCode(), ce.getMessage().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
        }
        close();
    }

    private void writeSettings() {
        try {
            byte[] settings = this.localSettings.getSettingsFrameForPending();
            this.socketWrapper.write(true, settings, 0, settings.length);
            this.socketWrapper.flush(true);
        } catch (IOException ioe) {
            String msg = sm.getString("upgradeHandler.sendPrefaceFail", this.connectionId);
            if (log.isDebugEnabled()) {
                log.debug(msg);
            }
            throw new ProtocolException(msg, ioe);
        }
    }

    private void writeGoAwayFrame(int maxStreamId, long errorCode, byte[] debugMsg) throws IOException {
        byte[] fixedPayload = new byte[8];
        ByteUtil.set31Bits(fixedPayload, 0, maxStreamId);
        ByteUtil.setFourBytes(fixedPayload, 4, errorCode);
        int len = 8;
        if (debugMsg != null) {
            len = 8 + debugMsg.length;
        }
        byte[] payloadLength = new byte[3];
        ByteUtil.setThreeBytes(payloadLength, 0, len);
        synchronized (this.socketWrapper) {
            this.socketWrapper.write(true, payloadLength, 0, payloadLength.length);
            this.socketWrapper.write(true, GOAWAY, 0, GOAWAY.length);
            this.socketWrapper.write(true, fixedPayload, 0, 8);
            if (debugMsg != null) {
                this.socketWrapper.write(true, debugMsg, 0, debugMsg.length);
            }
            this.socketWrapper.flush(true);
        }
    }

    void writeHeaders(Stream stream, int pushedStreamId, MimeHeaders mimeHeaders, boolean endOfStream, int payloadSize) throws IOException {
        synchronized (this.socketWrapper) {
            doWriteHeaders(stream, pushedStreamId, mimeHeaders, endOfStream, payloadSize);
        }
        stream.sentHeaders();
        if (endOfStream) {
            stream.sentEndOfStream();
        }
    }

    protected void doWriteHeaders(Stream stream, int pushedStreamId, MimeHeaders mimeHeaders, boolean endOfStream, int payloadSize) throws IOException {
        if (log.isDebugEnabled()) {
            if (pushedStreamId == 0) {
                log.debug(sm.getString("upgradeHandler.writeHeaders", this.connectionId, stream.getIdentifier()));
            } else {
                log.debug(sm.getString("upgradeHandler.writePushHeaders", this.connectionId, stream.getIdentifier(), Integer.valueOf(pushedStreamId), Boolean.valueOf(endOfStream)));
            }
        }
        if (!stream.canWrite()) {
            return;
        }
        byte[] header = new byte[9];
        ByteBuffer payload = ByteBuffer.allocate(payloadSize);
        byte[] pushedStreamIdBytes = null;
        if (pushedStreamId > 0) {
            pushedStreamIdBytes = new byte[4];
            ByteUtil.set31Bits(pushedStreamIdBytes, 0, pushedStreamId);
        }
        boolean first = true;
        HpackEncoder.State state = null;
        while (state != HpackEncoder.State.COMPLETE) {
            if (first && pushedStreamIdBytes != null) {
                payload.put(pushedStreamIdBytes);
            }
            state = getHpackEncoder().encode(mimeHeaders, payload);
            payload.flip();
            if (state == HpackEncoder.State.COMPLETE || payload.limit() > 0) {
                ByteUtil.setThreeBytes(header, 0, payload.limit());
                if (first) {
                    first = false;
                    if (pushedStreamIdBytes == null) {
                        header[3] = FrameType.HEADERS.getIdByte();
                    } else {
                        header[3] = FrameType.PUSH_PROMISE.getIdByte();
                    }
                    if (endOfStream) {
                        header[4] = 1;
                    }
                } else {
                    header[3] = FrameType.CONTINUATION.getIdByte();
                }
                if (state == HpackEncoder.State.COMPLETE) {
                    header[4] = (byte) (header[4] + 4);
                }
                if (log.isDebugEnabled()) {
                    log.debug(payload.limit() + " bytes");
                }
                ByteUtil.set31Bits(header, 5, stream.getIdAsInt());
                try {
                    this.socketWrapper.write(true, header, 0, header.length);
                    this.socketWrapper.write(true, payload);
                    this.socketWrapper.flush(true);
                } catch (IOException ioe) {
                    handleAppInitiatedIOException(ioe);
                }
                payload.clear();
            } else if (state == HpackEncoder.State.UNDERFLOW) {
                payload = ByteBuffer.allocate(payload.capacity() * 2);
            }
        }
    }

    private HpackEncoder getHpackEncoder() {
        if (this.hpackEncoder == null) {
            this.hpackEncoder = new HpackEncoder();
        }
        this.hpackEncoder.setMaxTableSize(this.remoteSettings.getHeaderTableSize());
        return this.hpackEncoder;
    }

    void writeBody(Stream stream, ByteBuffer data, int len, boolean finished) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("upgradeHandler.writeBody", this.connectionId, stream.getIdentifier(), Integer.toString(len)));
        }
        reduceOverheadCount();
        boolean writeable = stream.canWrite();
        byte[] header = new byte[9];
        ByteUtil.setThreeBytes(header, 0, len);
        header[3] = FrameType.DATA.getIdByte();
        if (finished) {
            header[4] = 1;
            stream.sentEndOfStream();
            if (!stream.isActive()) {
                this.activeRemoteStreamCount.decrementAndGet();
            }
        }
        if (writeable) {
            ByteUtil.set31Bits(header, 5, stream.getIdAsInt());
            synchronized (this.socketWrapper) {
                try {
                    this.socketWrapper.write(true, header, 0, header.length);
                    int orgLimit = data.limit();
                    data.limit(data.position() + len);
                    this.socketWrapper.write(true, data);
                    data.limit(orgLimit);
                    this.socketWrapper.flush(true);
                } catch (IOException ioe) {
                    handleAppInitiatedIOException(ioe);
                }
            }
        }
    }

    private void handleAppInitiatedIOException(IOException ioe) throws IOException {
        close();
        throw ioe;
    }

    void writeWindowUpdate(Stream stream, int increment, boolean applicationInitiated) throws IOException {
        if (!stream.canWrite()) {
            return;
        }
        synchronized (this.socketWrapper) {
            byte[] frame = new byte[13];
            ByteUtil.setThreeBytes(frame, 0, 4);
            frame[3] = FrameType.WINDOW_UPDATE.getIdByte();
            ByteUtil.set31Bits(frame, 9, increment);
            this.socketWrapper.write(true, frame, 0, frame.length);
            ByteUtil.set31Bits(frame, 5, stream.getIdAsInt());
            try {
                this.socketWrapper.write(true, frame, 0, frame.length);
                this.socketWrapper.flush(true);
            } catch (IOException ioe) {
                if (applicationInitiated) {
                    handleAppInitiatedIOException(ioe);
                } else {
                    throw ioe;
                }
            }
        }
    }

    private void processWrites() throws IOException {
        synchronized (this.socketWrapper) {
            if (this.socketWrapper.flush(false)) {
                this.socketWrapper.registerWriteInterest();
            }
        }
    }

    int reserveWindowSize(Stream stream, int reservation, boolean block) throws IOException {
        BacklogTracker tracker;
        int allocation = 0;
        synchronized (stream) {
            do {
                synchronized (this) {
                    if (!stream.canWrite()) {
                        throw new CloseNowException(sm.getString("upgradeHandler.stream.notWritable", stream.getConnectionId(), stream.getIdentifier()));
                    }
                    long windowSize = getWindowSize();
                    if (windowSize < 1 || this.backLogSize > 0) {
                        BacklogTracker tracker2 = this.backLogStreams.get(stream);
                        if (tracker2 == null) {
                            this.backLogStreams.put(stream, new BacklogTracker(reservation));
                            this.backLogSize += reservation;
                            for (AbstractStream parent = stream.getParentStream(); parent != null && this.backLogStreams.putIfAbsent(parent, new BacklogTracker()) == null; parent = parent.getParentStream()) {
                            }
                        } else if (tracker2.getUnusedAllocation() > 0) {
                            allocation = tracker2.getUnusedAllocation();
                            decrementWindowSize(allocation);
                            if (tracker2.getRemainingReservation() == 0) {
                                this.backLogStreams.remove(stream);
                            } else {
                                tracker2.useAllocation();
                            }
                        }
                    } else if (windowSize < reservation) {
                        allocation = (int) windowSize;
                        decrementWindowSize(allocation);
                    } else {
                        allocation = reservation;
                        decrementWindowSize(allocation);
                    }
                }
                if (allocation == 0) {
                    if (block) {
                        try {
                            long writeTimeout = this.protocol.getWriteTimeout();
                            stream.waitForConnectionAllocation(writeTimeout);
                            synchronized (this) {
                                tracker = this.backLogStreams.get(stream);
                            }
                            if (tracker != null && tracker.getUnusedAllocation() == 0) {
                                if (log.isDebugEnabled()) {
                                    log.debug(sm.getString("upgradeHandler.noAllocation", this.connectionId, stream.getIdentifier()));
                                }
                                close();
                                stream.doWriteTimeout();
                            }
                        } catch (InterruptedException e) {
                            throw new IOException(sm.getString("upgradeHandler.windowSizeReservationInterrupted", this.connectionId, stream.getIdentifier(), Integer.toString(reservation)), e);
                        }
                    } else {
                        stream.waitForConnectionAllocationNonBlocking();
                        return 0;
                    }
                }
            } while (allocation == 0);
            return allocation;
        }
    }

    @Override // org.apache.coyote.http2.AbstractStream
    protected void incrementWindowSize(int increment) throws Http2Exception {
        Set<AbstractStream> streamsToNotify = null;
        synchronized (this) {
            long windowSize = getWindowSize();
            if (windowSize < 1 && windowSize + increment > 0) {
                streamsToNotify = releaseBackLog((int) (windowSize + increment));
            }
            super.incrementWindowSize(increment);
        }
        if (streamsToNotify != null) {
            for (AbstractStream stream : streamsToNotify) {
                if (log.isDebugEnabled()) {
                    log.debug(sm.getString("upgradeHandler.releaseBacklog", this.connectionId, stream.getIdentifier()));
                }
                if (this != stream) {
                    ((Stream) stream).notifyConnection();
                }
            }
        }
    }

    private synchronized Set<AbstractStream> releaseBackLog(int increment) {
        Set<AbstractStream> result = new HashSet<>();
        if (this.backLogSize < increment) {
            result.addAll(this.backLogStreams.keySet());
            this.backLogStreams.clear();
            this.backLogSize = 0L;
        } else {
            int iAllocate = increment;
            while (true) {
                int leftToAllocate = iAllocate;
                if (leftToAllocate <= 0) {
                    break;
                }
                iAllocate = allocate(this, leftToAllocate);
            }
            for (Map.Entry<AbstractStream, BacklogTracker> entry : this.backLogStreams.entrySet()) {
                int allocation = entry.getValue().getUnusedAllocation();
                if (allocation > 0) {
                    this.backLogSize -= allocation;
                    if (!entry.getValue().isNotifyInProgress()) {
                        result.add(entry.getKey());
                        entry.getValue().startNotify();
                    }
                }
            }
        }
        return result;
    }

    @Override // org.apache.coyote.http2.AbstractStream
    @Deprecated
    protected synchronized void doNotifyAll() {
    }

    private int allocate(AbstractStream stream, int allocation) {
        int allocated;
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("upgradeHandler.allocate.debug", getConnectionId(), stream.getIdentifier(), Integer.toString(allocation)));
        }
        BacklogTracker tracker = this.backLogStreams.get(stream);
        int leftToAllocate = tracker.allocate(allocation);
        if (leftToAllocate == 0) {
            return 0;
        }
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("upgradeHandler.allocate.left", getConnectionId(), stream.getIdentifier(), Integer.toString(leftToAllocate)));
        }
        Set<AbstractStream> recipients = new HashSet<>();
        recipients.addAll(stream.getChildStreams());
        recipients.retainAll(this.backLogStreams.keySet());
        while (leftToAllocate > 0) {
            if (recipients.size() == 0) {
                this.backLogStreams.remove(stream);
                return leftToAllocate;
            }
            int totalWeight = 0;
            for (AbstractStream recipient : recipients) {
                if (log.isDebugEnabled()) {
                    log.debug(sm.getString("upgradeHandler.allocate.recipient", getConnectionId(), stream.getIdentifier(), recipient.getIdentifier(), Integer.toString(recipient.getWeight())));
                }
                totalWeight += recipient.getWeight();
            }
            Iterator<AbstractStream> iter = recipients.iterator();
            int i = 0;
            while (true) {
                allocated = i;
                if (iter.hasNext()) {
                    AbstractStream recipient2 = iter.next();
                    int share = (leftToAllocate * recipient2.getWeight()) / totalWeight;
                    if (share == 0) {
                        share = 1;
                    }
                    int remainder = allocate(recipient2, share);
                    if (remainder > 0) {
                        iter.remove();
                    }
                    i = allocated + (share - remainder);
                }
            }
            leftToAllocate -= allocated;
        }
        return 0;
    }

    private Stream getStream(int streamId, boolean unknownIsError) throws ConnectionException {
        Integer key = Integer.valueOf(streamId);
        Stream result = this.streams.get(key);
        if (result == null && unknownIsError) {
            throw new ConnectionException(sm.getString("upgradeHandler.stream.closed", key), Http2Error.PROTOCOL_ERROR);
        }
        return result;
    }

    private Stream createRemoteStream(int streamId) throws ConnectionException {
        Integer key = Integer.valueOf(streamId);
        if (streamId % 2 != 1) {
            throw new ConnectionException(sm.getString("upgradeHandler.stream.even", key), Http2Error.PROTOCOL_ERROR);
        }
        pruneClosedStreams(streamId);
        Stream result = new Stream(key, this);
        this.streams.put(key, result);
        return result;
    }

    private Stream createLocalStream(Request request) {
        int streamId = this.nextLocalStreamId.getAndAdd(2);
        Integer key = Integer.valueOf(streamId);
        Stream result = new Stream(key, this, request);
        this.streams.put(key, result);
        return result;
    }

    private void close() {
        ConnectionState previous = this.connectionState.getAndSet(ConnectionState.CLOSED);
        if (previous == ConnectionState.CLOSED) {
            return;
        }
        for (Stream stream : this.streams.values()) {
            stream.receiveReset(Http2Error.CANCEL.getCode());
        }
        try {
            this.socketWrapper.close();
        } catch (IOException ioe) {
            log.debug(sm.getString("upgradeHandler.socketCloseFailed"), ioe);
        }
    }

    private void pruneClosedStreams(int streamId) {
        if (this.newStreamsSinceLastPrune < 9) {
            this.newStreamsSinceLastPrune++;
            return;
        }
        this.newStreamsSinceLastPrune = 0;
        long max = this.localSettings.getMaxConcurrentStreams();
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("upgradeHandler.pruneStart", this.connectionId, Long.toString(max), Integer.toString(this.streams.size())));
        }
        long max2 = max + (max / 10);
        if (max2 > 2147483647L) {
            max2 = 2147483647L;
        }
        int toClose = this.streams.size() - ((int) max2);
        if (toClose < 1) {
            return;
        }
        TreeSet<Integer> candidatesStepOne = new TreeSet<>();
        TreeSet<Integer> candidatesStepTwo = new TreeSet<>();
        TreeSet<Integer> candidatesStepThree = new TreeSet<>();
        for (Map.Entry<Integer, Stream> entry : this.streams.entrySet()) {
            Stream stream = entry.getValue();
            if (!stream.isActive()) {
                if (stream.isClosedFinal()) {
                    candidatesStepThree.add(entry.getKey());
                } else if (stream.getChildStreams().size() == 0) {
                    candidatesStepOne.add(entry.getKey());
                } else {
                    candidatesStepTwo.add(entry.getKey());
                }
            }
        }
        Iterator i$ = candidatesStepOne.iterator();
        while (i$.hasNext()) {
            Integer streamIdToRemove = i$.next();
            Stream removedStream = this.streams.remove(streamIdToRemove);
            removedStream.detachFromParent();
            toClose--;
            if (log.isDebugEnabled()) {
                log.debug(sm.getString("upgradeHandler.pruned", this.connectionId, streamIdToRemove));
            }
            AbstractStream parentStream = removedStream.getParentStream();
            while (true) {
                AbstractStream parent = parentStream;
                if (!(parent instanceof Stream) || ((Stream) parent).isActive() || ((Stream) parent).isClosedFinal() || parent.getChildStreams().size() != 0) {
                    break;
                }
                this.streams.remove(parent.getIdentifier());
                parent.detachFromParent();
                toClose--;
                if (log.isDebugEnabled()) {
                    log.debug(sm.getString("upgradeHandler.pruned", this.connectionId, streamIdToRemove));
                }
                candidatesStepTwo.remove(parent.getIdentifier());
                parentStream = parent.getParentStream();
            }
        }
        Iterator i$2 = candidatesStepTwo.iterator();
        while (i$2.hasNext()) {
            Integer streamIdToRemove2 = i$2.next();
            removeStreamFromPriorityTree(streamIdToRemove2);
            toClose--;
            if (log.isDebugEnabled()) {
                log.debug(sm.getString("upgradeHandler.pruned", this.connectionId, streamIdToRemove2));
            }
        }
        while (toClose > 0 && candidatesStepThree.size() > 0) {
            Integer streamIdToRemove3 = candidatesStepThree.pollLast();
            removeStreamFromPriorityTree(streamIdToRemove3);
            toClose--;
            if (log.isDebugEnabled()) {
                log.debug(sm.getString("upgradeHandler.prunedPriority", this.connectionId, streamIdToRemove3));
            }
        }
        if (toClose > 0) {
            log.warn(sm.getString("upgradeHandler.pruneIncomplete", this.connectionId, Integer.toString(streamId), Integer.toString(toClose)));
        }
    }

    private void removeStreamFromPriorityTree(Integer streamIdToRemove) {
        Stream streamToRemove = this.streams.remove(streamIdToRemove);
        Set<Stream> children = streamToRemove.getChildStreams();
        if (streamToRemove.getChildStreams().size() == 1) {
            streamToRemove.getChildStreams().iterator().next().rePrioritise(streamToRemove.getParentStream(), streamToRemove.getWeight());
        } else {
            int totalWeight = 0;
            for (Stream child : children) {
                totalWeight += child.getWeight();
            }
            for (Stream child2 : children) {
                streamToRemove.getChildStreams().iterator().next().rePrioritise(streamToRemove.getParentStream(), (streamToRemove.getWeight() * child2.getWeight()) / totalWeight);
            }
        }
        streamToRemove.detachFromParent();
        streamToRemove.getChildStreams().clear();
    }

    void push(Request request, Stream associatedStream) throws IOException {
        Stream pushStream;
        if (this.localSettings.getMaxConcurrentStreams() < this.activeRemoteStreamCount.incrementAndGet()) {
            this.activeRemoteStreamCount.decrementAndGet();
            return;
        }
        synchronized (this.socketWrapper) {
            pushStream = createLocalStream(request);
            writeHeaders(associatedStream, pushStream.getIdAsInt(), request.getMimeHeaders(), false, 1024);
        }
        pushStream.sentPushPromise();
        processStreamOnContainerThread(pushStream);
    }

    @Override // org.apache.coyote.http2.AbstractStream
    protected final String getConnectionId() {
        return this.connectionId;
    }

    @Override // org.apache.coyote.http2.AbstractStream
    protected final int getWeight() {
        return 0;
    }

    boolean isTrailerHeaderAllowed(String headerName) {
        return this.allowedTrailerHeaders.contains(headerName);
    }

    private void reduceOverheadCount() {
        this.overheadCount.decrementAndGet();
    }

    private void increaseOverheadCount() {
        this.overheadCount.addAndGet(getProtocol().getOverheadCountFactor());
    }

    public long getReadTimeout() {
        return this.readTimeout;
    }

    public void setReadTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
    }

    public long getKeepAliveTimeout() {
        return this.keepAliveTimeout;
    }

    public void setKeepAliveTimeout(long keepAliveTimeout) {
        this.keepAliveTimeout = keepAliveTimeout;
    }

    public long getWriteTimeout() {
        return this.writeTimeout;
    }

    public void setWriteTimeout(long writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public void setMaxConcurrentStreams(long maxConcurrentStreams) {
        this.localSettings.set(Setting.MAX_CONCURRENT_STREAMS, maxConcurrentStreams);
    }

    public void setMaxConcurrentStreamExecution(int maxConcurrentStreamExecution) {
        this.maxConcurrentStreamExecution = maxConcurrentStreamExecution;
    }

    public void setInitialWindowSize(int initialWindowSize) {
        this.localSettings.set(Setting.INITIAL_WINDOW_SIZE, initialWindowSize);
    }

    public void setAllowedTrailerHeaders(Set<String> allowedTrailerHeaders) {
        this.allowedTrailerHeaders = allowedTrailerHeaders;
    }

    public void setMaxHeaderCount(int maxHeaderCount) {
        this.maxHeaderCount = maxHeaderCount;
    }

    public int getMaxHeaderCount() {
        return this.maxHeaderCount;
    }

    public void setMaxHeaderSize(int maxHeaderSize) {
        this.maxHeaderSize = maxHeaderSize;
    }

    public int getMaxHeaderSize() {
        return this.maxHeaderSize;
    }

    public void setMaxTrailerCount(int maxTrailerCount) {
        this.maxTrailerCount = maxTrailerCount;
    }

    public int getMaxTrailerCount() {
        return this.maxTrailerCount;
    }

    public void setMaxTrailerSize(int maxTrailerSize) {
        this.maxTrailerSize = maxTrailerSize;
    }

    public int getMaxTrailerSize() {
        return this.maxTrailerSize;
    }

    public void setInitiatePingDisabled(boolean initiatePingDisabled) {
        this.pingManager.initiateDisabled = initiatePingDisabled;
    }

    @Override // org.apache.coyote.http2.Http2Parser.Input
    public boolean fill(boolean block, byte[] data) throws IOException {
        return fill(block, data, 0, data.length);
    }

    @Override // org.apache.coyote.http2.Http2Parser.Input
    public boolean fill(boolean block, ByteBuffer data, int len) throws IOException {
        boolean result = fill(block, data.array(), data.arrayOffset() + data.position(), len);
        if (result) {
            data.position(data.position() + len);
        }
        return result;
    }

    @Override // org.apache.coyote.http2.Http2Parser.Input
    public boolean fill(boolean block, byte[] data, int offset, int length) throws IOException {
        int len = length;
        int pos = offset;
        boolean nextReadBlock = block;
        while (len > 0) {
            int thisRead = this.socketWrapper.read(nextReadBlock, data, pos, len);
            if (thisRead == 0) {
                if (nextReadBlock) {
                    throw new IllegalStateException();
                }
                return false;
            }
            if (thisRead == -1) {
                if (this.connectionState.get().isNewStreamAllowed()) {
                    throw new EOFException();
                }
                return false;
            }
            pos += thisRead;
            len -= thisRead;
            nextReadBlock = true;
        }
        return true;
    }

    @Override // org.apache.coyote.http2.Http2Parser.Input
    public int getMaxFrameSize() {
        return this.localSettings.getMaxFrameSize();
    }

    @Override // org.apache.coyote.http2.Http2Parser.Output
    public HpackDecoder getHpackDecoder() {
        if (this.hpackDecoder == null) {
            this.hpackDecoder = new HpackDecoder(this.localSettings.getHeaderTableSize());
        }
        return this.hpackDecoder;
    }

    @Override // org.apache.coyote.http2.Http2Parser.Output
    public ByteBuffer startRequestBodyFrame(int streamId, int payloadSize) throws Http2Exception {
        reduceOverheadCount();
        Stream stream = getStream(streamId, true);
        stream.checkState(FrameType.DATA);
        stream.receivedData(payloadSize);
        return stream.getInputByteBuffer();
    }

    @Override // org.apache.coyote.http2.Http2Parser.Output
    public void endRequestBodyFrame(int streamId) throws Http2Exception {
        Stream stream = getStream(streamId, true);
        stream.getInputBuffer().onDataAvailable();
    }

    @Override // org.apache.coyote.http2.Http2Parser.Output
    public void receivedEndOfStream(int streamId) throws ConnectionException {
        Stream stream = getStream(streamId, this.connectionState.get().isNewStreamAllowed());
        if (stream != null) {
            stream.receivedEndOfStream();
            if (!stream.isActive()) {
                this.activeRemoteStreamCount.decrementAndGet();
            }
        }
    }

    @Override // org.apache.coyote.http2.Http2Parser.Output
    public void swallowedPadding(int streamId, int paddingLength) throws IOException, ConnectionException {
        Stream stream = getStream(streamId, true);
        writeWindowUpdate(stream, paddingLength + 1, false);
    }

    @Override // org.apache.coyote.http2.Http2Parser.Output
    public HpackDecoder.HeaderEmitter headersStart(int streamId, boolean headersEndStream) throws IOException, Http2Exception {
        checkPauseState();
        reduceOverheadCount();
        if (this.connectionState.get().isNewStreamAllowed()) {
            Stream stream = getStream(streamId, false);
            if (stream == null) {
                stream = createRemoteStream(streamId);
            }
            if (streamId < this.maxActiveRemoteStreamId) {
                throw new ConnectionException(sm.getString("upgradeHandler.stream.old", Integer.valueOf(streamId), Integer.valueOf(this.maxActiveRemoteStreamId)), Http2Error.PROTOCOL_ERROR);
            }
            stream.checkState(FrameType.HEADERS);
            stream.receivedStartOfHeaders(headersEndStream);
            closeIdleStreams(streamId);
            if (this.localSettings.getMaxConcurrentStreams() < this.activeRemoteStreamCount.incrementAndGet()) {
                this.activeRemoteStreamCount.decrementAndGet();
                throw new StreamException(sm.getString("upgradeHandler.tooManyRemoteStreams", Long.toString(this.localSettings.getMaxConcurrentStreams())), Http2Error.REFUSED_STREAM, streamId);
            }
            return stream;
        }
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("upgradeHandler.noNewStreams", this.connectionId, Integer.toString(streamId)));
        }
        return HEADER_SINK;
    }

    private void closeIdleStreams(int newMaxActiveRemoteStreamId) throws Http2Exception {
        for (int i = this.maxActiveRemoteStreamId + 2; i < newMaxActiveRemoteStreamId; i += 2) {
            Stream stream = getStream(i, false);
            if (stream != null) {
                stream.closeIfIdle();
            }
        }
        this.maxActiveRemoteStreamId = newMaxActiveRemoteStreamId;
    }

    @Override // org.apache.coyote.http2.Http2Parser.Output
    public void reprioritise(int streamId, int parentStreamId, boolean exclusive, int weight) throws Http2Exception {
        if (streamId == parentStreamId) {
            throw new ConnectionException(sm.getString("upgradeHandler.dependency.invalid", getConnectionId(), Integer.valueOf(streamId)), Http2Error.PROTOCOL_ERROR);
        }
        increaseOverheadCount();
        Stream stream = getStream(streamId, false);
        if (stream == null) {
            stream = createRemoteStream(streamId);
        }
        stream.checkState(FrameType.PRIORITY);
        AbstractStream parentStream = getStream(parentStreamId, false);
        if (parentStream == null) {
            parentStream = this;
        }
        stream.rePrioritise(parentStream, exclusive, weight);
    }

    @Override // org.apache.coyote.http2.Http2Parser.Output
    public void headersEnd(int streamId) throws ConnectionException {
        Stream stream = getStream(streamId, this.connectionState.get().isNewStreamAllowed());
        if (stream != null) {
            setMaxProcessedStream(streamId);
            if (stream.isActive() && stream.receivedEndOfHeaders()) {
                processStreamOnContainerThread(stream);
            }
        }
    }

    private void setMaxProcessedStream(int streamId) {
        if (this.maxProcessedStreamId < streamId) {
            this.maxProcessedStreamId = streamId;
        }
    }

    @Override // org.apache.coyote.http2.Http2Parser.Output
    public void reset(int streamId, long errorCode) throws Http2Exception {
        Stream stream = getStream(streamId, true);
        stream.checkState(FrameType.RST);
        stream.receiveReset(errorCode);
    }

    @Override // org.apache.coyote.http2.Http2Parser.Output
    public void setting(Setting setting, long value) throws ConnectionException {
        increaseOverheadCount();
        if (setting == Setting.INITIAL_WINDOW_SIZE) {
            long oldValue = this.remoteSettings.getInitialWindowSize();
            this.remoteSettings.set(setting, value);
            int diff = (int) (value - oldValue);
            for (Stream stream : this.streams.values()) {
                try {
                    stream.incrementWindowSize(diff);
                } catch (Http2Exception h2e) {
                    stream.close(new StreamException(sm.getString("upgradeHandler.windowSizeTooBig", this.connectionId, stream.getIdentifier()), h2e.getError(), stream.getIdAsInt()));
                }
            }
            return;
        }
        this.remoteSettings.set(setting, value);
    }

    @Override // org.apache.coyote.http2.Http2Parser.Output
    public void settingsEnd(boolean ack) throws IOException {
        if (ack) {
            if (!this.localSettings.ack()) {
                log.warn(sm.getString("upgradeHandler.unexpectedAck", this.connectionId, getIdentifier()));
            }
        } else {
            synchronized (this.socketWrapper) {
                this.socketWrapper.write(true, SETTINGS_ACK, 0, SETTINGS_ACK.length);
                this.socketWrapper.flush(true);
            }
        }
    }

    @Override // org.apache.coyote.http2.Http2Parser.Output
    public void pingReceive(byte[] payload, boolean ack) throws IOException {
        if (!ack) {
            increaseOverheadCount();
        }
        this.pingManager.receivePing(payload, ack);
    }

    @Override // org.apache.coyote.http2.Http2Parser.Output
    public void goaway(int lastStreamId, long errorCode, String debugData) {
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("upgradeHandler.goaway.debug", this.connectionId, Integer.toString(lastStreamId), Long.toHexString(errorCode), debugData));
        }
        close();
    }

    @Override // org.apache.coyote.http2.Http2Parser.Output
    public void incrementWindowSize(int streamId, int increment) throws Http2Exception {
        if (streamId == 0) {
            incrementWindowSize(increment);
            return;
        }
        Stream stream = getStream(streamId, true);
        stream.checkState(FrameType.WINDOW_UPDATE);
        stream.incrementWindowSize(increment);
    }

    @Override // org.apache.coyote.http2.Http2Parser.Output
    public void swallowed(int streamId, FrameType frameType, int flags, int size) throws IOException {
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http2/Http2UpgradeHandler$PingManager.class */
    private class PingManager {
        protected boolean initiateDisabled;
        private final long pingIntervalNano = 10000000000L;
        private int sequence;
        private long lastPingNanoTime;
        private Queue<PingRecord> inflightPings;
        private Queue<Long> roundTripTimes;

        private PingManager() {
            this.initiateDisabled = false;
            this.pingIntervalNano = 10000000000L;
            this.sequence = 0;
            this.lastPingNanoTime = Long.MIN_VALUE;
            this.inflightPings = new ConcurrentLinkedQueue();
            this.roundTripTimes = new ConcurrentLinkedQueue();
        }

        public void sendPing(boolean force) throws IOException {
            if (this.initiateDisabled) {
                return;
            }
            long now = System.nanoTime();
            if (force || now - this.lastPingNanoTime > 10000000000L) {
                this.lastPingNanoTime = now;
                byte[] payload = new byte[8];
                synchronized (Http2UpgradeHandler.this.socketWrapper) {
                    int sentSequence = this.sequence + 1;
                    this.sequence = sentSequence;
                    PingRecord pingRecord = new PingRecord(sentSequence, now);
                    this.inflightPings.add(pingRecord);
                    ByteUtil.set31Bits(payload, 4, sentSequence);
                    Http2UpgradeHandler.this.socketWrapper.write(true, Http2UpgradeHandler.PING, 0, Http2UpgradeHandler.PING.length);
                    Http2UpgradeHandler.this.socketWrapper.write(true, payload, 0, payload.length);
                    Http2UpgradeHandler.this.socketWrapper.flush(true);
                }
            }
        }

        public void receivePing(byte[] payload, boolean ack) throws IOException {
            PingRecord pingRecord;
            if (!ack) {
                synchronized (Http2UpgradeHandler.this.socketWrapper) {
                    Http2UpgradeHandler.this.socketWrapper.write(true, Http2UpgradeHandler.PING_ACK, 0, Http2UpgradeHandler.PING_ACK.length);
                    Http2UpgradeHandler.this.socketWrapper.write(true, payload, 0, payload.length);
                    Http2UpgradeHandler.this.socketWrapper.flush(true);
                }
                return;
            }
            int receivedSequence = ByteUtil.get31Bits(payload, 4);
            PingRecord pingRecordPoll = this.inflightPings.poll();
            while (true) {
                pingRecord = pingRecordPoll;
                if (pingRecord == null || pingRecord.getSequence() >= receivedSequence) {
                    break;
                } else {
                    pingRecordPoll = this.inflightPings.poll();
                }
            }
            if (pingRecord != null) {
                long roundTripTime = System.nanoTime() - pingRecord.getSentNanoTime();
                this.roundTripTimes.add(Long.valueOf(roundTripTime));
                while (this.roundTripTimes.size() > 3) {
                    this.roundTripTimes.poll();
                }
                if (Http2UpgradeHandler.log.isDebugEnabled()) {
                    Http2UpgradeHandler.log.debug(Http2UpgradeHandler.sm.getString("pingManager.roundTripTime", Http2UpgradeHandler.this.connectionId, Long.valueOf(roundTripTime)));
                }
            }
        }

        public long getRoundTripTimeNano() {
            long sum = 0;
            long count = 0;
            for (Long roundTripTime : this.roundTripTimes) {
                sum += roundTripTime.longValue();
                count++;
            }
            if (count > 0) {
                return sum / count;
            }
            return 0L;
        }
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http2/Http2UpgradeHandler$PingRecord.class */
    private static class PingRecord {
        private final int sequence;
        private final long sentNanoTime;

        public PingRecord(int sequence, long sentNanoTime) {
            this.sequence = sequence;
            this.sentNanoTime = sentNanoTime;
        }

        public int getSequence() {
            return this.sequence;
        }

        public long getSentNanoTime() {
            return this.sentNanoTime;
        }
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http2/Http2UpgradeHandler$ConnectionState.class */
    private enum ConnectionState {
        NEW(true),
        CONNECTED(true),
        PAUSING(true),
        PAUSED(false),
        CLOSED(false);

        private final boolean newStreamsAllowed;

        ConnectionState(boolean newStreamsAllowed) {
            this.newStreamsAllowed = newStreamsAllowed;
        }

        public boolean isNewStreamAllowed() {
            return this.newStreamsAllowed;
        }
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http2/Http2UpgradeHandler$BacklogTracker.class */
    private static class BacklogTracker {
        private int remainingReservation;
        private int unusedAllocation;
        private boolean notifyInProgress;

        public BacklogTracker() {
        }

        public BacklogTracker(int reservation) {
            this.remainingReservation = reservation;
        }

        public int getRemainingReservation() {
            return this.remainingReservation;
        }

        public int getUnusedAllocation() {
            return this.unusedAllocation;
        }

        public boolean isNotifyInProgress() {
            return this.notifyInProgress;
        }

        public void useAllocation() {
            this.unusedAllocation = 0;
            this.notifyInProgress = false;
        }

        public void startNotify() {
            this.notifyInProgress = true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int allocate(int allocation) {
            if (this.remainingReservation >= allocation) {
                this.remainingReservation -= allocation;
                this.unusedAllocation += allocation;
                return 0;
            }
            int left = allocation - this.remainingReservation;
            this.unusedAllocation += this.remainingReservation;
            this.remainingReservation = 0;
            return left;
        }
    }
}
