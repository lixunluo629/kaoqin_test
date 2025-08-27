package org.apache.coyote.http2;

import io.netty.handler.codec.http.HttpHeaders;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Iterator;
import java.util.Locale;
import org.apache.coyote.ActionCode;
import org.apache.coyote.CloseNowException;
import org.apache.coyote.InputBuffer;
import org.apache.coyote.Request;
import org.apache.coyote.Response;
import org.apache.coyote.http11.HttpOutputBuffer;
import org.apache.coyote.http11.OutputFilter;
import org.apache.coyote.http2.HpackDecoder;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.buf.ByteChunk;
import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.http.MimeHeaders;
import org.apache.tomcat.util.http.parser.Host;
import org.apache.tomcat.util.net.ApplicationBufferHandler;
import org.apache.tomcat.util.net.WriteBuffer;
import org.apache.tomcat.util.res.StringManager;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http2/Stream.class */
public class Stream extends AbstractStream implements HpackDecoder.HeaderEmitter {
    private static final int HEADER_STATE_START = 0;
    private static final int HEADER_STATE_PSEUDO = 1;
    private static final int HEADER_STATE_REGULAR = 2;
    private static final int HEADER_STATE_TRAILER = 3;
    private static final MimeHeaders ACK_HEADERS;
    private volatile int weight;
    private volatile long contentLengthReceived;
    private final Http2UpgradeHandler handler;
    private final StreamStateMachine state;
    private final WindowAllocationManager allocationManager;
    private int headerState;
    private StreamException headerException;
    private final Request coyoteRequest;
    private StringBuilder cookieHeader;
    private final Response coyoteResponse;
    private final StreamInputBuffer inputBuffer;
    private final StreamOutputBuffer streamOutputBuffer;
    private final Http2OutputBuffer http2OutputBuffer;
    private static final Log log = LogFactory.getLog((Class<?>) Stream.class);
    private static final StringManager sm = StringManager.getManager((Class<?>) Stream.class);
    private static final Integer HTTP_UPGRADE_STREAM = 1;

    @Override // org.apache.coyote.http2.AbstractStream
    public /* bridge */ /* synthetic */ int getIdAsInt() {
        return super.getIdAsInt();
    }

    @Override // org.apache.coyote.http2.AbstractStream
    public /* bridge */ /* synthetic */ Integer getIdentifier() {
        return super.getIdentifier();
    }

    static {
        Response response = new Response();
        response.setStatus(100);
        StreamProcessor.prepareHeaders(null, response, null, null);
        ACK_HEADERS = response.getMimeHeaders();
    }

    public Stream(Integer identifier, Http2UpgradeHandler handler) {
        this(identifier, handler, null);
    }

    public Stream(Integer identifier, Http2UpgradeHandler handler, Request coyoteRequest) {
        super(identifier);
        this.weight = 16;
        this.contentLengthReceived = 0L;
        this.allocationManager = new WindowAllocationManager(this);
        this.headerState = 0;
        this.headerException = null;
        this.cookieHeader = null;
        this.coyoteResponse = new Response();
        this.streamOutputBuffer = new StreamOutputBuffer();
        this.http2OutputBuffer = new Http2OutputBuffer(this.coyoteResponse, this.streamOutputBuffer);
        this.handler = handler;
        handler.addChild(this);
        setWindowSize(handler.getRemoteSettings().getInitialWindowSize());
        this.state = new StreamStateMachine(this);
        if (coyoteRequest == null) {
            this.coyoteRequest = new Request();
            this.inputBuffer = new StreamInputBuffer();
            this.coyoteRequest.setInputBuffer(this.inputBuffer);
        } else {
            this.coyoteRequest = coyoteRequest;
            this.inputBuffer = null;
            this.state.receivedStartOfHeaders();
            if (HTTP_UPGRADE_STREAM.equals(identifier)) {
                try {
                    prepareRequest();
                } catch (IllegalArgumentException e) {
                    this.coyoteResponse.setStatus(400);
                    this.coyoteResponse.setError();
                }
            }
            this.state.receivedEndOfStream();
        }
        this.coyoteRequest.setSendfile(false);
        this.coyoteResponse.setOutputBuffer(this.http2OutputBuffer);
        this.coyoteRequest.setResponse(this.coyoteResponse);
        this.coyoteRequest.protocol().setString("HTTP/2.0");
        if (this.coyoteRequest.getStartTime() < 0) {
            this.coyoteRequest.setStartTime(System.currentTimeMillis());
        }
    }

    private void prepareRequest() {
        MessageBytes hostValueMB = this.coyoteRequest.getMimeHeaders().getUniqueValue("host");
        if (hostValueMB == null) {
            throw new IllegalArgumentException();
        }
        hostValueMB.toBytes();
        ByteChunk valueBC = hostValueMB.getByteChunk();
        byte[] valueB = valueBC.getBytes();
        int valueL = valueBC.getLength();
        int valueS = valueBC.getStart();
        int colonPos = Host.parse(hostValueMB);
        if (colonPos != -1) {
            int port = 0;
            for (int i = colonPos + 1; i < valueL; i++) {
                char c = (char) valueB[i + valueS];
                if (c < '0' || c > '9') {
                    throw new IllegalArgumentException();
                }
                port = ((port * 10) + c) - 48;
            }
            this.coyoteRequest.setServerPort(port);
            valueL = colonPos;
        }
        char[] hostNameC = new char[valueL];
        for (int i2 = 0; i2 < valueL; i2++) {
            hostNameC[i2] = (char) valueB[i2 + valueS];
        }
        this.coyoteRequest.serverName().setChars(hostNameC, 0, valueL);
    }

    void rePrioritise(AbstractStream parent, boolean exclusive, int weight) {
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("stream.reprioritisation.debug", getConnectionId(), getIdentifier(), Boolean.toString(exclusive), parent.getIdentifier(), Integer.toString(weight)));
        }
        if (isDescendant(parent)) {
            parent.detachFromParent();
            getParentStream().addChild((Stream) parent);
        }
        if (exclusive) {
            Iterator<Stream> parentsChildren = parent.getChildStreams().iterator();
            while (parentsChildren.hasNext()) {
                Stream parentsChild = parentsChildren.next();
                parentsChildren.remove();
                addChild(parentsChild);
            }
        }
        detachFromParent();
        parent.addChild(this);
        this.weight = weight;
    }

    final void rePrioritise(AbstractStream parent, int weight) {
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("stream.reprioritisation.debug", getConnectionId(), getIdentifier(), Boolean.FALSE, parent.getIdentifier(), Integer.toString(weight)));
        }
        parent.addChild(this);
        this.weight = weight;
    }

    void receiveReset(long errorCode) {
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("stream.reset.debug", getConnectionId(), getIdentifier(), Long.toString(errorCode)));
        }
        this.state.receivedReset();
        if (this.inputBuffer != null) {
            this.inputBuffer.receiveReset();
        }
        cancelAllocationRequests();
    }

    final void cancelAllocationRequests() {
        this.allocationManager.notifyAny();
    }

    void checkState(FrameType frameType) throws Http2Exception {
        this.state.checkFrameType(frameType);
    }

    @Override // org.apache.coyote.http2.AbstractStream
    protected synchronized void incrementWindowSize(int windowSizeIncrement) throws Http2Exception {
        boolean notify = getWindowSize() < 1;
        super.incrementWindowSize(windowSizeIncrement);
        if (notify && getWindowSize() > 0) {
            this.allocationManager.notifyStream();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized int reserveWindowSize(int reservation, boolean block) throws IOException {
        int allocation;
        long windowSize = getWindowSize();
        while (windowSize < 1) {
            if (!canWrite()) {
                throw new CloseNowException(sm.getString("stream.notWritable", getConnectionId(), getIdentifier()));
            }
            if (block) {
                try {
                    long writeTimeout = this.handler.getProtocol().getStreamWriteTimeout();
                    this.allocationManager.waitForStream(writeTimeout);
                    windowSize = getWindowSize();
                    if (windowSize == 0) {
                        doWriteTimeout();
                    }
                } catch (InterruptedException e) {
                    throw new IOException(e);
                }
            } else {
                this.allocationManager.waitForStreamNonBlocking();
                return 0;
            }
        }
        if (windowSize < reservation) {
            allocation = (int) windowSize;
        } else {
            allocation = reservation;
        }
        decrementWindowSize(allocation);
        return allocation;
    }

    void doWriteTimeout() throws CloseNowException {
        String msg = sm.getString("stream.writeTimeout");
        StreamException se = new StreamException(msg, Http2Error.ENHANCE_YOUR_CALM, getIdAsInt());
        this.streamOutputBuffer.closed = true;
        this.coyoteResponse.setError();
        this.coyoteResponse.setErrorReported();
        this.streamOutputBuffer.reset = se;
        throw new CloseNowException(msg, se);
    }

    void waitForConnectionAllocation(long timeout) throws InterruptedException {
        this.allocationManager.waitForConnection(timeout);
    }

    void waitForConnectionAllocationNonBlocking() {
        this.allocationManager.waitForConnectionNonBlocking();
    }

    void notifyConnection() {
        this.allocationManager.notifyConnection();
    }

    @Override // org.apache.coyote.http2.AbstractStream
    @Deprecated
    protected synchronized void doNotifyAll() {
    }

    @Override // org.apache.coyote.http2.HpackDecoder.HeaderEmitter
    public final void emitHeader(String name, String value) throws HpackException {
        String uri;
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("stream.header.debug", getConnectionId(), getIdentifier(), name, value));
        }
        if (!name.toLowerCase(Locale.US).equals(name)) {
            throw new HpackException(sm.getString("stream.header.case", getConnectionId(), getIdentifier(), name));
        }
        if ("connection".equals(name)) {
            throw new HpackException(sm.getString("stream.header.connection", getConnectionId(), getIdentifier()));
        }
        if ("te".equals(name) && !HttpHeaders.Values.TRAILERS.equals(value)) {
            throw new HpackException(sm.getString("stream.header.te", getConnectionId(), getIdentifier(), value));
        }
        if (this.headerException != null) {
            return;
        }
        boolean pseudoHeader = name.charAt(0) == ':';
        if (pseudoHeader && this.headerState != 1) {
            this.headerException = new StreamException(sm.getString("stream.header.unexpectedPseudoHeader", getConnectionId(), getIdentifier(), name), Http2Error.PROTOCOL_ERROR, getIdAsInt());
            return;
        }
        if (this.headerState == 1 && !pseudoHeader) {
            this.headerState = 2;
        }
        switch (name) {
            case ":method":
                if (this.coyoteRequest.method().isNull()) {
                    this.coyoteRequest.method().setString(value);
                    return;
                }
                throw new HpackException(sm.getString("stream.header.duplicate", getConnectionId(), getIdentifier(), ":method"));
            case ":scheme":
                if (this.coyoteRequest.scheme().isNull()) {
                    this.coyoteRequest.scheme().setString(value);
                    return;
                }
                throw new HpackException(sm.getString("stream.header.duplicate", getConnectionId(), getIdentifier(), ":scheme"));
            case ":path":
                if (!this.coyoteRequest.requestURI().isNull()) {
                    throw new HpackException(sm.getString("stream.header.duplicate", getConnectionId(), getIdentifier(), ":path"));
                }
                if (value.length() == 0) {
                    throw new HpackException(sm.getString("stream.header.noPath", getConnectionId(), getIdentifier()));
                }
                int queryStart = value.indexOf(63);
                if (queryStart == -1) {
                    uri = value;
                } else {
                    uri = value.substring(0, queryStart);
                    String query = value.substring(queryStart + 1);
                    this.coyoteRequest.queryString().setString(query);
                }
                byte[] uriBytes = uri.getBytes(StandardCharsets.ISO_8859_1);
                this.coyoteRequest.requestURI().setBytes(uriBytes, 0, uriBytes.length);
                return;
            case ":authority":
                if (this.coyoteRequest.serverName().isNull()) {
                    try {
                        int i = Host.parse(value);
                        if (i > -1) {
                            this.coyoteRequest.serverName().setString(value.substring(0, i));
                            this.coyoteRequest.setServerPort(Integer.parseInt(value.substring(i + 1)));
                            return;
                        } else {
                            this.coyoteRequest.serverName().setString(value);
                            return;
                        }
                    } catch (IllegalArgumentException e) {
                        throw new HpackException(sm.getString("stream.header.invalid", getConnectionId(), getIdentifier(), ":authority", value));
                    }
                }
                throw new HpackException(sm.getString("stream.header.duplicate", getConnectionId(), getIdentifier(), ":authority"));
            case "cookie":
                if (this.cookieHeader == null) {
                    this.cookieHeader = new StringBuilder();
                } else {
                    this.cookieHeader.append("; ");
                }
                this.cookieHeader.append(value);
                return;
            default:
                if (this.headerState != 3 || this.handler.isTrailerHeaderAllowed(name)) {
                    if ("expect".equals(name) && "100-continue".equals(value)) {
                        this.coyoteRequest.setExpectation(true);
                    }
                    if (pseudoHeader) {
                        this.headerException = new StreamException(sm.getString("stream.header.unknownPseudoHeader", getConnectionId(), getIdentifier(), name), Http2Error.PROTOCOL_ERROR, getIdAsInt());
                    }
                    this.coyoteRequest.getMimeHeaders().addValue(name).setString(value);
                    return;
                }
                return;
        }
    }

    @Override // org.apache.coyote.http2.HpackDecoder.HeaderEmitter
    public void setHeaderException(StreamException streamException) {
        if (this.headerException == null) {
            this.headerException = streamException;
        }
    }

    @Override // org.apache.coyote.http2.HpackDecoder.HeaderEmitter
    public void validateHeaders() throws StreamException {
        if (this.headerException == null) {
        } else {
            throw this.headerException;
        }
    }

    final boolean receivedEndOfHeaders() throws ConnectionException {
        if (this.coyoteRequest.method().isNull() || this.coyoteRequest.scheme().isNull() || this.coyoteRequest.requestURI().isNull()) {
            throw new ConnectionException(sm.getString("stream.header.required", getConnectionId(), getIdentifier()), Http2Error.PROTOCOL_ERROR);
        }
        if (this.cookieHeader != null) {
            this.coyoteRequest.getMimeHeaders().addValue("cookie").setString(this.cookieHeader.toString());
        }
        return this.headerState == 2 || this.headerState == 1;
    }

    void writeHeaders() throws IOException {
        boolean endOfStream = this.streamOutputBuffer.hasNoBody();
        this.handler.writeHeaders(this, 0, this.coyoteResponse.getMimeHeaders(), endOfStream, 1024);
    }

    final void addOutputFilter(OutputFilter filter) {
        this.http2OutputBuffer.addFilter(filter);
    }

    void writeAck() throws IOException {
        this.handler.writeHeaders(this, 0, ACK_HEADERS, false, 64);
    }

    @Override // org.apache.coyote.http2.AbstractStream
    protected final String getConnectionId() {
        return this.handler.getConnectionId();
    }

    @Override // org.apache.coyote.http2.AbstractStream
    protected int getWeight() {
        return this.weight;
    }

    Request getCoyoteRequest() {
        return this.coyoteRequest;
    }

    Response getCoyoteResponse() {
        return this.coyoteResponse;
    }

    ByteBuffer getInputByteBuffer() {
        return this.inputBuffer.getInBuffer();
    }

    final void receivedStartOfHeaders(boolean headersEndStream) throws Http2Exception {
        if (this.headerState == 0) {
            this.headerState = 1;
            this.handler.getHpackDecoder().setMaxHeaderCount(this.handler.getMaxHeaderCount());
            this.handler.getHpackDecoder().setMaxHeaderSize(this.handler.getMaxHeaderSize());
        } else if (this.headerState == 1 || this.headerState == 2) {
            if (headersEndStream) {
                this.headerState = 3;
                this.handler.getHpackDecoder().setMaxHeaderCount(this.handler.getMaxTrailerCount());
                this.handler.getHpackDecoder().setMaxHeaderSize(this.handler.getMaxTrailerSize());
            } else {
                throw new ConnectionException(sm.getString("stream.trailerHeader.noEndOfStream", getConnectionId(), getIdentifier()), Http2Error.PROTOCOL_ERROR);
            }
        }
        this.state.receivedStartOfHeaders();
    }

    final void receivedData(int payloadSize) throws ConnectionException {
        this.contentLengthReceived += payloadSize;
        long contentLengthHeader = this.coyoteRequest.getContentLengthLong();
        if (contentLengthHeader > -1 && this.contentLengthReceived > contentLengthHeader) {
            throw new ConnectionException(sm.getString("stream.header.contentLength", getConnectionId(), getIdentifier(), Long.valueOf(contentLengthHeader), Long.valueOf(this.contentLengthReceived)), Http2Error.PROTOCOL_ERROR);
        }
    }

    final void receivedEndOfStream() throws ConnectionException {
        long contentLengthHeader = this.coyoteRequest.getContentLengthLong();
        if (contentLengthHeader > -1 && this.contentLengthReceived != contentLengthHeader) {
            throw new ConnectionException(sm.getString("stream.header.contentLength", getConnectionId(), getIdentifier(), Long.valueOf(contentLengthHeader), Long.valueOf(this.contentLengthReceived)), Http2Error.PROTOCOL_ERROR);
        }
        this.state.receivedEndOfStream();
        if (this.inputBuffer == null) {
            return;
        }
        this.inputBuffer.notifyEof();
    }

    final void sentHeaders() {
        this.state.sentStartOfHeaders();
    }

    final void sentEndOfStream() {
        this.streamOutputBuffer.endOfStreamSent = true;
        this.state.sentEndOfStream();
    }

    final boolean isReadyForWrite() {
        return this.streamOutputBuffer.isReady();
    }

    final boolean flush(boolean block) throws IOException {
        return this.streamOutputBuffer.flush(block);
    }

    StreamInputBuffer getInputBuffer() {
        return this.inputBuffer;
    }

    final HttpOutputBuffer getOutputBuffer() {
        return this.http2OutputBuffer;
    }

    void sentPushPromise() {
        this.state.sentPushPromise();
    }

    boolean isActive() {
        return this.state.isActive();
    }

    boolean canWrite() {
        return this.state.canWrite();
    }

    boolean isClosedFinal() {
        return this.state.isClosedFinal();
    }

    void closeIfIdle() {
        this.state.closeIfIdle();
    }

    boolean isInputFinished() {
        return !this.state.isFrameTypePermitted(FrameType.DATA);
    }

    void close(Http2Exception http2Exception) {
        if (http2Exception instanceof StreamException) {
            try {
                StreamException se = (StreamException) http2Exception;
                if (log.isDebugEnabled()) {
                    log.debug(sm.getString("stream.reset.send", getConnectionId(), getIdentifier(), se.getError()));
                }
                this.state.sendReset();
                this.handler.sendStreamReset(se);
            } catch (IOException ioe) {
                ConnectionException ce = new ConnectionException(sm.getString("stream.reset.fail"), Http2Error.PROTOCOL_ERROR);
                ce.initCause(ioe);
                this.handler.closeConnection(ce);
            }
        } else {
            this.handler.closeConnection(http2Exception);
        }
        if (this.inputBuffer != null) {
            this.inputBuffer.receiveReset();
        }
    }

    boolean isPushSupported() {
        return this.handler.getRemoteSettings().getEnablePush();
    }

    final void push(Request request) throws PrivilegedActionException, IOException {
        if (!isPushSupported() || getIdAsInt() % 2 == 0) {
            return;
        }
        request.getMimeHeaders().addValue(":method").duplicate(request.method());
        request.getMimeHeaders().addValue(":scheme").duplicate(request.scheme());
        StringBuilder path = new StringBuilder(request.requestURI().toString());
        if (!request.queryString().isNull()) {
            path.append('?');
            path.append(request.queryString().toString());
        }
        request.getMimeHeaders().addValue(":path").setString(path.toString());
        if ((!request.scheme().equals("http") || request.getServerPort() != 80) && (!request.scheme().equals("https") || request.getServerPort() != 443)) {
            request.getMimeHeaders().addValue(":authority").setString(request.serverName().getString() + ":" + request.getServerPort());
        } else {
            request.getMimeHeaders().addValue(":authority").duplicate(request.serverName());
        }
        push(this.handler, request, this);
    }

    StreamException getResetException() {
        return this.streamOutputBuffer.reset;
    }

    private static void push(Http2UpgradeHandler handler, Request request, Stream stream) throws PrivilegedActionException, IOException {
        if (org.apache.coyote.Constants.IS_SECURITY_ENABLED) {
            try {
                AccessController.doPrivileged(new PrivilegedPush(handler, request, stream));
                return;
            } catch (PrivilegedActionException ex) {
                Exception e = ex.getException();
                if (e instanceof IOException) {
                    throw ((IOException) e);
                }
                throw new IOException(ex);
            }
        }
        handler.push(request, stream);
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http2/Stream$PrivilegedPush.class */
    private static class PrivilegedPush implements PrivilegedExceptionAction<Void> {
        private final Http2UpgradeHandler handler;
        private final Request request;
        private final Stream stream;

        public PrivilegedPush(Http2UpgradeHandler handler, Request request, Stream stream) {
            this.handler = handler;
            this.request = request;
            this.stream = stream;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedExceptionAction
        public Void run() throws IOException {
            this.handler.push(this.request, this.stream);
            return null;
        }
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http2/Stream$StreamOutputBuffer.class */
    class StreamOutputBuffer implements HttpOutputBuffer, WriteBuffer.Sink {
        private boolean dataLeft;
        private final ByteBuffer buffer = ByteBuffer.allocate(8192);
        private final WriteBuffer writeBuffer = new WriteBuffer(32768);
        private volatile long written = 0;
        private volatile int streamReservation = 0;
        private volatile boolean closed = false;
        private volatile StreamException reset = null;
        private volatile boolean endOfStreamSent = false;
        private volatile boolean writeInterest = false;

        StreamOutputBuffer() {
        }

        @Override // org.apache.coyote.OutputBuffer
        @Deprecated
        public synchronized int doWrite(ByteChunk chunk) throws IOException {
            if (!this.closed) {
                if (!Stream.this.coyoteResponse.isCommitted()) {
                    Stream.this.coyoteResponse.sendHeaders();
                }
                int len = chunk.getLength();
                int offset = 0;
                while (len > 0) {
                    int thisTime = Math.min(this.buffer.remaining(), len);
                    this.buffer.put(chunk.getBytes(), chunk.getOffset() + offset, thisTime);
                    offset += thisTime;
                    len -= thisTime;
                    if (len > 0 && !this.buffer.hasRemaining()) {
                        if (flush(true, Stream.this.coyoteResponse.getWriteListener() == null)) {
                            break;
                        }
                    }
                }
                this.written += offset;
                return offset;
            }
            throw new IllegalStateException(Stream.sm.getString("stream.closed", Stream.this.getConnectionId(), Stream.this.getIdentifier()));
        }

        @Override // org.apache.coyote.OutputBuffer
        public synchronized int doWrite(ByteBuffer chunk) throws IOException {
            if (this.closed) {
                throw new IllegalStateException(Stream.sm.getString("stream.closed", Stream.this.getConnectionId(), Stream.this.getIdentifier()));
            }
            int totalThisTime = 0;
            if (this.writeBuffer.isEmpty()) {
                int chunkLimit = chunk.limit();
                while (true) {
                    if (chunk.remaining() <= 0) {
                        break;
                    }
                    int thisTime = Math.min(this.buffer.remaining(), chunk.remaining());
                    chunk.limit(chunk.position() + thisTime);
                    this.buffer.put(chunk);
                    chunk.limit(chunkLimit);
                    totalThisTime += thisTime;
                    if (chunk.remaining() > 0 && !this.buffer.hasRemaining()) {
                        if (flush(true, Stream.this.coyoteResponse.getWriteListener() == null)) {
                            totalThisTime = chunk.remaining();
                            this.writeBuffer.add(chunk);
                            this.dataLeft = true;
                            break;
                        }
                    }
                }
            } else {
                totalThisTime = chunk.remaining();
                this.writeBuffer.add(chunk);
            }
            this.written += totalThisTime;
            return totalThisTime;
        }

        public synchronized boolean flush(boolean block) throws IOException {
            boolean dataInBuffer = this.buffer.position() > 0;
            boolean flushed = false;
            if (dataInBuffer) {
                dataInBuffer = flush(false, block);
                flushed = true;
            }
            if (dataInBuffer) {
                this.dataLeft = true;
            } else if (this.writeBuffer.isEmpty()) {
                if (flushed) {
                    this.dataLeft = false;
                } else {
                    this.dataLeft = flush(false, block);
                }
            } else {
                this.dataLeft = this.writeBuffer.write(this, block);
            }
            return this.dataLeft;
        }

        private synchronized boolean flush(boolean writeInProgress, boolean block) throws IOException {
            if (Stream.log.isDebugEnabled()) {
                Stream.log.debug(Stream.sm.getString("stream.outputBuffer.flush.debug", Stream.this.getConnectionId(), Stream.this.getIdentifier(), Integer.toString(this.buffer.position()), Boolean.toString(writeInProgress), Boolean.toString(this.closed)));
            }
            if (this.buffer.position() == 0) {
                if (this.closed && !this.endOfStreamSent) {
                    Stream.this.handler.writeBody(Stream.this, this.buffer, 0, true);
                    return false;
                }
                return false;
            }
            this.buffer.flip();
            int left = this.buffer.remaining();
            while (left > 0) {
                if (this.streamReservation == 0) {
                    this.streamReservation = Stream.this.reserveWindowSize(left, block);
                    if (this.streamReservation == 0) {
                        this.buffer.compact();
                        return true;
                    }
                }
                while (this.streamReservation > 0) {
                    int connectionReservation = Stream.this.handler.reserveWindowSize(Stream.this, this.streamReservation, block);
                    if (connectionReservation != 0) {
                        Stream.this.handler.writeBody(Stream.this, this.buffer, connectionReservation, !writeInProgress && this.closed && left == connectionReservation);
                        this.streamReservation -= connectionReservation;
                        left -= connectionReservation;
                    } else {
                        this.buffer.compact();
                        return true;
                    }
                }
            }
            this.buffer.clear();
            return false;
        }

        synchronized boolean isReady() {
            if (Stream.this.getWindowSize() > 0 && Stream.this.handler.getWindowSize() > 0 && !this.dataLeft) {
                return true;
            }
            this.writeInterest = true;
            return false;
        }

        synchronized boolean isRegisteredForWrite() {
            if (this.writeInterest) {
                this.writeInterest = false;
                return true;
            }
            return false;
        }

        @Override // org.apache.coyote.OutputBuffer
        public long getBytesWritten() {
            return this.written;
        }

        @Override // org.apache.coyote.http11.HttpOutputBuffer
        public final void end() throws IOException {
            if (this.reset != null) {
                throw new CloseNowException(this.reset);
            }
            if (!this.closed) {
                this.closed = true;
                flush(true);
            }
        }

        public boolean isClosed() {
            return this.closed;
        }

        public boolean hasNoBody() {
            return this.written == 0 && this.closed;
        }

        @Override // org.apache.coyote.http11.HttpOutputBuffer
        public void flush() throws IOException {
            flush(Stream.this.getCoyoteResponse().getWriteListener() == null);
        }

        @Override // org.apache.tomcat.util.net.WriteBuffer.Sink
        public synchronized boolean writeFromBuffer(ByteBuffer src, boolean blocking) throws IOException {
            int chunkLimit = src.limit();
            while (src.remaining() > 0) {
                int thisTime = Math.min(this.buffer.remaining(), src.remaining());
                src.limit(src.position() + thisTime);
                this.buffer.put(src);
                src.limit(chunkLimit);
                if (flush(false, blocking)) {
                    return true;
                }
            }
            return false;
        }
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http2/Stream$StreamInputBuffer.class */
    class StreamInputBuffer implements InputBuffer {
        private byte[] outBuffer;
        private volatile ByteBuffer inBuffer;
        private volatile boolean readInterest;
        private boolean resetReceived = false;

        StreamInputBuffer() {
        }

        @Override // org.apache.coyote.InputBuffer
        @Deprecated
        public int doRead(ByteChunk chunk) throws IOException {
            ensureBuffersExist();
            synchronized (this.inBuffer) {
                boolean canRead = false;
                while (this.inBuffer.position() == 0) {
                    boolean z = Stream.this.isActive() && !Stream.this.isInputFinished();
                    canRead = z;
                    if (!z) {
                        break;
                    }
                    try {
                        if (Stream.log.isDebugEnabled()) {
                            Stream.log.debug(Stream.sm.getString("stream.inputBuffer.empty"));
                        }
                        long readTimeout = Stream.this.handler.getProtocol().getStreamReadTimeout();
                        if (readTimeout < 0) {
                            this.inBuffer.wait();
                        } else {
                            this.inBuffer.wait(readTimeout);
                        }
                        if (this.resetReceived) {
                            throw new IOException(Stream.sm.getString("stream.inputBuffer.reset"));
                        }
                        if (this.inBuffer.position() == 0) {
                            String msg = Stream.sm.getString("stream.inputBuffer.readTimeout");
                            StreamException se = new StreamException(msg, Http2Error.ENHANCE_YOUR_CALM, Stream.this.getIdAsInt());
                            Stream.this.coyoteResponse.setError();
                            Stream.this.streamOutputBuffer.reset = se;
                            throw new CloseNowException(msg, se);
                        }
                    } catch (InterruptedException e) {
                        throw new IOException(e);
                    }
                }
                if (this.inBuffer.position() > 0) {
                    this.inBuffer.flip();
                    int written = this.inBuffer.remaining();
                    if (Stream.log.isDebugEnabled()) {
                        Stream.log.debug(Stream.sm.getString("stream.inputBuffer.copy", Integer.toString(written)));
                    }
                    this.inBuffer.get(this.outBuffer, 0, written);
                    this.inBuffer.clear();
                    chunk.setBytes(this.outBuffer, 0, written);
                    Stream.this.handler.writeWindowUpdate(Stream.this, written, true);
                    return written;
                }
                if (!canRead) {
                    return -1;
                }
                throw new IllegalStateException();
            }
        }

        @Override // org.apache.coyote.InputBuffer
        public int doRead(ApplicationBufferHandler applicationBufferHandler) throws IOException {
            ensureBuffersExist();
            synchronized (this.inBuffer) {
                boolean canRead = false;
                while (this.inBuffer.position() == 0) {
                    boolean z = Stream.this.isActive() && !Stream.this.isInputFinished();
                    canRead = z;
                    if (!z) {
                        break;
                    }
                    try {
                        if (Stream.log.isDebugEnabled()) {
                            Stream.log.debug(Stream.sm.getString("stream.inputBuffer.empty"));
                        }
                        long readTimeout = Stream.this.handler.getProtocol().getStreamReadTimeout();
                        if (readTimeout < 0) {
                            this.inBuffer.wait();
                        } else {
                            this.inBuffer.wait(readTimeout);
                        }
                        if (this.resetReceived) {
                            throw new IOException(Stream.sm.getString("stream.inputBuffer.reset"));
                        }
                        if (this.inBuffer.position() == 0 && Stream.this.isActive() && !Stream.this.isInputFinished()) {
                            String msg = Stream.sm.getString("stream.inputBuffer.readTimeout");
                            StreamException se = new StreamException(msg, Http2Error.ENHANCE_YOUR_CALM, Stream.this.getIdAsInt());
                            Stream.this.coyoteResponse.setError();
                            Stream.this.streamOutputBuffer.reset = se;
                            throw new CloseNowException(msg, se);
                        }
                    } catch (InterruptedException e) {
                        throw new IOException(e);
                    }
                }
                if (this.inBuffer.position() > 0) {
                    this.inBuffer.flip();
                    int written = this.inBuffer.remaining();
                    if (Stream.log.isDebugEnabled()) {
                        Stream.log.debug(Stream.sm.getString("stream.inputBuffer.copy", Integer.toString(written)));
                    }
                    this.inBuffer.get(this.outBuffer, 0, written);
                    this.inBuffer.clear();
                    applicationBufferHandler.setByteBuffer(ByteBuffer.wrap(this.outBuffer, 0, written));
                    Stream.this.handler.writeWindowUpdate(Stream.this, written, true);
                    return written;
                }
                if (!canRead) {
                    return -1;
                }
                throw new IllegalStateException();
            }
        }

        final boolean isReadyForRead() {
            ensureBuffersExist();
            synchronized (this) {
                if (available() > 0) {
                    return true;
                }
                if (!isRequestBodyFullyRead()) {
                    this.readInterest = true;
                }
                return false;
            }
        }

        synchronized boolean isRequestBodyFullyRead() {
            return (this.inBuffer == null || this.inBuffer.position() == 0) && Stream.this.isInputFinished();
        }

        synchronized int available() {
            if (this.inBuffer == null) {
                return 0;
            }
            return this.inBuffer.position();
        }

        synchronized boolean onDataAvailable() {
            if (this.readInterest) {
                if (Stream.log.isDebugEnabled()) {
                    Stream.log.debug(Stream.sm.getString("stream.inputBuffer.dispatch"));
                }
                this.readInterest = false;
                Stream.this.coyoteRequest.action(ActionCode.DISPATCH_READ, null);
                Stream.this.coyoteRequest.action(ActionCode.DISPATCH_EXECUTE, null);
                return true;
            }
            if (Stream.log.isDebugEnabled()) {
                Stream.log.debug(Stream.sm.getString("stream.inputBuffer.signal"));
            }
            synchronized (this.inBuffer) {
                this.inBuffer.notifyAll();
            }
            return false;
        }

        public ByteBuffer getInBuffer() {
            ensureBuffersExist();
            return this.inBuffer;
        }

        protected synchronized void insertReplayedBody(ByteChunk body) {
            this.inBuffer = ByteBuffer.wrap(body.getBytes(), body.getOffset(), body.getLength());
        }

        private void ensureBuffersExist() {
            if (this.inBuffer == null) {
                int size = Stream.this.handler.getLocalSettings().getInitialWindowSize();
                synchronized (this) {
                    if (this.inBuffer == null) {
                        this.inBuffer = ByteBuffer.allocate(size);
                        this.outBuffer = new byte[size];
                    }
                }
            }
        }

        protected void receiveReset() {
            if (this.inBuffer != null) {
                synchronized (this.inBuffer) {
                    this.resetReceived = true;
                    this.inBuffer.notifyAll();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void notifyEof() {
            if (this.inBuffer != null) {
                synchronized (this.inBuffer) {
                    this.inBuffer.notifyAll();
                }
            }
        }
    }
}
