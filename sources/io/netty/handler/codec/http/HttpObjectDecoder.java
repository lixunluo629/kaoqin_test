package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.PrematureChannelClosureException;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.ByteProcessor;
import io.netty.util.internal.AppendableCharSequence;
import io.netty.util.internal.ObjectUtil;
import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/HttpObjectDecoder.class */
public abstract class HttpObjectDecoder extends ByteToMessageDecoder {
    private static final String EMPTY_VALUE = "";
    private final int maxChunkSize;
    private final boolean chunkedSupported;
    protected final boolean validateHeaders;
    private final HeaderParser headerParser;
    private final LineParser lineParser;
    private HttpMessage message;
    private long chunkSize;
    private long contentLength;
    private volatile boolean resetRequested;
    private CharSequence name;
    private CharSequence value;
    private LastHttpContent trailer;
    private State currentState;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/HttpObjectDecoder$State.class */
    private enum State {
        SKIP_CONTROL_CHARS,
        READ_INITIAL,
        READ_HEADER,
        READ_VARIABLE_LENGTH_CONTENT,
        READ_FIXED_LENGTH_CONTENT,
        READ_CHUNK_SIZE,
        READ_CHUNKED_CONTENT,
        READ_CHUNK_DELIMITER,
        READ_CHUNK_FOOTER,
        BAD_MESSAGE,
        UPGRADED
    }

    protected abstract boolean isDecodingRequest();

    protected abstract HttpMessage createMessage(String[] strArr) throws Exception;

    protected abstract HttpMessage createInvalidMessage();

    static {
        $assertionsDisabled = !HttpObjectDecoder.class.desiredAssertionStatus();
    }

    protected HttpObjectDecoder() {
        this(4096, 8192, 8192, true);
    }

    protected HttpObjectDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean chunkedSupported) {
        this(maxInitialLineLength, maxHeaderSize, maxChunkSize, chunkedSupported, true);
    }

    protected HttpObjectDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean chunkedSupported, boolean validateHeaders) {
        this(maxInitialLineLength, maxHeaderSize, maxChunkSize, chunkedSupported, validateHeaders, 128);
    }

    protected HttpObjectDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean chunkedSupported, boolean validateHeaders, int initialBufferSize) {
        this.contentLength = Long.MIN_VALUE;
        this.currentState = State.SKIP_CONTROL_CHARS;
        ObjectUtil.checkPositive(maxInitialLineLength, "maxInitialLineLength");
        ObjectUtil.checkPositive(maxHeaderSize, "maxHeaderSize");
        ObjectUtil.checkPositive(maxChunkSize, "maxChunkSize");
        AppendableCharSequence seq = new AppendableCharSequence(initialBufferSize);
        this.lineParser = new LineParser(seq, maxInitialLineLength);
        this.headerParser = new HeaderParser(seq, maxHeaderSize);
        this.maxChunkSize = maxChunkSize;
        this.chunkedSupported = chunkedSupported;
        this.validateHeaders = validateHeaders;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:121:0x030c A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x00a7 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x00a8 A[Catch: Exception -> 0x0176, TRY_ENTER, TryCatch #3 {Exception -> 0x0176, blocks: (B:18:0x009b, B:21:0x00a8, B:22:0x00b7, B:23:0x00d0, B:25:0x00ea, B:27:0x00f1, B:28:0x00fa, B:29:0x00fb, B:31:0x0107, B:35:0x011d, B:39:0x013e, B:41:0x0144, B:43:0x014c, B:45:0x0154, B:46:0x015b, B:47:0x015c, B:49:0x016f, B:37:0x0124), top: B:119:0x009b }] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x02a4 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x02a5  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x02ed  */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void decode(io.netty.channel.ChannelHandlerContext r7, io.netty.buffer.ByteBuf r8, java.util.List<java.lang.Object> r9) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 872
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.http.HttpObjectDecoder.decode(io.netty.channel.ChannelHandlerContext, io.netty.buffer.ByteBuf, java.util.List):void");
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        boolean prematureClosure;
        super.decodeLast(ctx, in, out);
        if (this.resetRequested) {
            resetNow();
        }
        if (this.message != null) {
            boolean chunked = HttpUtil.isTransferEncodingChunked(this.message);
            if (this.currentState == State.READ_VARIABLE_LENGTH_CONTENT && !in.isReadable() && !chunked) {
                out.add(LastHttpContent.EMPTY_LAST_CONTENT);
                resetNow();
                return;
            }
            if (this.currentState == State.READ_HEADER) {
                out.add(invalidMessage(Unpooled.EMPTY_BUFFER, new PrematureChannelClosureException("Connection closed before received headers")));
                resetNow();
                return;
            }
            if (isDecodingRequest() || chunked) {
                prematureClosure = true;
            } else {
                prematureClosure = contentLength() > 0;
            }
            if (!prematureClosure) {
                out.add(LastHttpContent.EMPTY_LAST_CONTENT);
            }
            resetNow();
        }
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder, io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof HttpExpectationFailedEvent) {
            switch (this.currentState) {
                case READ_CHUNK_SIZE:
                case READ_VARIABLE_LENGTH_CONTENT:
                case READ_FIXED_LENGTH_CONTENT:
                    reset();
                    break;
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    protected boolean isContentAlwaysEmpty(HttpMessage msg) {
        if (msg instanceof HttpResponse) {
            HttpResponse res = (HttpResponse) msg;
            int code = res.status().code();
            if (code >= 100 && code < 200) {
                return (code == 101 && !res.headers().contains(HttpHeaderNames.SEC_WEBSOCKET_ACCEPT) && res.headers().contains((CharSequence) HttpHeaderNames.UPGRADE, (CharSequence) HttpHeaderValues.WEBSOCKET, true)) ? false : true;
            }
            switch (code) {
                case 204:
                case 304:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    protected boolean isSwitchingToNonHttp1Protocol(HttpResponse msg) {
        if (msg.status().code() != HttpResponseStatus.SWITCHING_PROTOCOLS.code()) {
            return false;
        }
        String newProtocol = msg.headers().get(HttpHeaderNames.UPGRADE);
        return newProtocol == null || !(newProtocol.contains(HttpVersion.HTTP_1_0.text()) || newProtocol.contains(HttpVersion.HTTP_1_1.text()));
    }

    public void reset() {
        this.resetRequested = true;
    }

    private void resetNow() {
        HttpResponse res;
        HttpMessage message = this.message;
        this.message = null;
        this.name = null;
        this.value = null;
        this.contentLength = Long.MIN_VALUE;
        this.lineParser.reset();
        this.headerParser.reset();
        this.trailer = null;
        if (!isDecodingRequest() && (res = (HttpResponse) message) != null && isSwitchingToNonHttp1Protocol(res)) {
            this.currentState = State.UPGRADED;
        } else {
            this.resetRequested = false;
            this.currentState = State.SKIP_CONTROL_CHARS;
        }
    }

    private HttpMessage invalidMessage(ByteBuf in, Exception cause) {
        this.currentState = State.BAD_MESSAGE;
        in.skipBytes(in.readableBytes());
        if (this.message == null) {
            this.message = createInvalidMessage();
        }
        this.message.setDecoderResult(DecoderResult.failure(cause));
        HttpMessage ret = this.message;
        this.message = null;
        return ret;
    }

    private HttpContent invalidChunk(ByteBuf in, Exception cause) {
        this.currentState = State.BAD_MESSAGE;
        in.skipBytes(in.readableBytes());
        HttpContent chunk = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER);
        chunk.setDecoderResult(DecoderResult.failure(cause));
        this.message = null;
        this.trailer = null;
        return chunk;
    }

    private State readHeaders(ByteBuf buffer) {
        HttpMessage message = this.message;
        HttpHeaders headers = message.headers();
        AppendableCharSequence line = this.headerParser.parse(buffer);
        if (line == null) {
            return null;
        }
        if (line.length() > 0) {
            do {
                char firstChar = line.charAtUnsafe(0);
                if (this.name != null && (firstChar == ' ' || firstChar == '\t')) {
                    String trimmedLine = line.toString().trim();
                    String valueStr = String.valueOf(this.value);
                    this.value = valueStr + ' ' + trimmedLine;
                } else {
                    if (this.name != null) {
                        headers.add(this.name, this.value);
                    }
                    splitHeader(line);
                }
                line = this.headerParser.parse(buffer);
                if (line == null) {
                    return null;
                }
            } while (line.length() > 0);
        }
        if (this.name != null) {
            headers.add(this.name, this.value);
        }
        this.name = null;
        this.value = null;
        List<String> values = headers.getAll(HttpHeaderNames.CONTENT_LENGTH);
        int contentLengthValuesCount = values.size();
        if (contentLengthValuesCount > 0) {
            if (contentLengthValuesCount > 1 && message.protocolVersion() == HttpVersion.HTTP_1_1) {
                throw new IllegalArgumentException("Multiple Content-Length headers found");
            }
            this.contentLength = Long.parseLong(values.get(0));
        }
        if (isContentAlwaysEmpty(message)) {
            HttpUtil.setTransferEncodingChunked(message, false);
            return State.SKIP_CONTROL_CHARS;
        }
        if (!HttpUtil.isTransferEncodingChunked(message)) {
            if (contentLength() >= 0) {
                return State.READ_FIXED_LENGTH_CONTENT;
            }
            return State.READ_VARIABLE_LENGTH_CONTENT;
        }
        if (contentLengthValuesCount > 0 && message.protocolVersion() == HttpVersion.HTTP_1_1) {
            handleTransferEncodingChunkedWithContentLength(message);
        }
        return State.READ_CHUNK_SIZE;
    }

    protected void handleTransferEncodingChunkedWithContentLength(HttpMessage message) {
        message.headers().remove(HttpHeaderNames.CONTENT_LENGTH);
        this.contentLength = Long.MIN_VALUE;
    }

    private long contentLength() {
        if (this.contentLength == Long.MIN_VALUE) {
            this.contentLength = HttpUtil.getContentLength(this.message, -1L);
        }
        return this.contentLength;
    }

    private LastHttpContent readTrailingHeaders(ByteBuf buffer) {
        AppendableCharSequence line = this.headerParser.parse(buffer);
        if (line == null) {
            return null;
        }
        LastHttpContent trailer = this.trailer;
        if (line.length() == 0 && trailer == null) {
            return LastHttpContent.EMPTY_LAST_CONTENT;
        }
        CharSequence lastHeader = null;
        if (trailer == null) {
            DefaultLastHttpContent defaultLastHttpContent = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER, this.validateHeaders);
            this.trailer = defaultLastHttpContent;
            trailer = defaultLastHttpContent;
        }
        while (line.length() > 0) {
            char firstChar = line.charAtUnsafe(0);
            if (lastHeader != null && (firstChar == ' ' || firstChar == '\t')) {
                List<String> current = trailer.trailingHeaders().getAll(lastHeader);
                if (!current.isEmpty()) {
                    int lastPos = current.size() - 1;
                    String lineTrimmed = line.toString().trim();
                    String currentLastPos = current.get(lastPos);
                    current.set(lastPos, currentLastPos + lineTrimmed);
                }
            } else {
                splitHeader(line);
                CharSequence headerName = this.name;
                if (!HttpHeaderNames.CONTENT_LENGTH.contentEqualsIgnoreCase(headerName) && !HttpHeaderNames.TRANSFER_ENCODING.contentEqualsIgnoreCase(headerName) && !HttpHeaderNames.TRAILER.contentEqualsIgnoreCase(headerName)) {
                    trailer.trailingHeaders().add(headerName, this.value);
                }
                lastHeader = this.name;
                this.name = null;
                this.value = null;
            }
            line = this.headerParser.parse(buffer);
            if (line == null) {
                return null;
            }
        }
        this.trailer = null;
        return trailer;
    }

    private static int getChunkSize(String hex) {
        String hex2 = hex.trim();
        for (int i = 0; i < hex2.length(); i++) {
            char c = hex2.charAt(i);
            if (c == ';' || Character.isWhitespace(c) || Character.isISOControl(c)) {
                hex2 = hex2.substring(0, i);
                break;
            }
        }
        return Integer.parseInt(hex2, 16);
    }

    private static String[] splitInitialLine(AppendableCharSequence sb) {
        int aStart = findNonSPLenient(sb, 0);
        int aEnd = findSPLenient(sb, aStart);
        int bStart = findNonSPLenient(sb, aEnd);
        int bEnd = findSPLenient(sb, bStart);
        int cStart = findNonSPLenient(sb, bEnd);
        int cEnd = findEndOfString(sb);
        String[] strArr = new String[3];
        strArr[0] = sb.subStringUnsafe(aStart, aEnd);
        strArr[1] = sb.subStringUnsafe(bStart, bEnd);
        strArr[2] = cStart < cEnd ? sb.subStringUnsafe(cStart, cEnd) : "";
        return strArr;
    }

    private void splitHeader(AppendableCharSequence sb) {
        char ch2;
        int length = sb.length();
        int nameStart = findNonWhitespace(sb, 0, false);
        int nameEnd = nameStart;
        while (nameEnd < length && (ch2 = sb.charAtUnsafe(nameEnd)) != ':' && (isDecodingRequest() || !isOWS(ch2))) {
            nameEnd++;
        }
        if (nameEnd == length) {
            throw new IllegalArgumentException("No colon found");
        }
        int colonEnd = nameEnd;
        while (true) {
            if (colonEnd >= length) {
                break;
            }
            if (sb.charAtUnsafe(colonEnd) != ':') {
                colonEnd++;
            } else {
                colonEnd++;
                break;
            }
        }
        this.name = sb.subStringUnsafe(nameStart, nameEnd);
        int valueStart = findNonWhitespace(sb, colonEnd, true);
        if (valueStart == length) {
            this.value = "";
        } else {
            int valueEnd = findEndOfString(sb);
            this.value = sb.subStringUnsafe(valueStart, valueEnd);
        }
    }

    private static int findNonSPLenient(AppendableCharSequence sb, int offset) {
        for (int result = offset; result < sb.length(); result++) {
            char c = sb.charAtUnsafe(result);
            if (!isSPLenient(c)) {
                if (Character.isWhitespace(c)) {
                    throw new IllegalArgumentException("Invalid separator");
                }
                return result;
            }
        }
        return sb.length();
    }

    private static int findSPLenient(AppendableCharSequence sb, int offset) {
        for (int result = offset; result < sb.length(); result++) {
            if (isSPLenient(sb.charAtUnsafe(result))) {
                return result;
            }
        }
        return sb.length();
    }

    private static boolean isSPLenient(char c) {
        return c == ' ' || c == '\t' || c == 11 || c == '\f' || c == '\r';
    }

    private static int findNonWhitespace(AppendableCharSequence sb, int offset, boolean validateOWS) {
        for (int result = offset; result < sb.length(); result++) {
            char c = sb.charAtUnsafe(result);
            if (!Character.isWhitespace(c)) {
                return result;
            }
            if (validateOWS && !isOWS(c)) {
                throw new IllegalArgumentException("Invalid separator, only a single space or horizontal tab allowed, but received a '" + c + "'");
            }
        }
        return sb.length();
    }

    private static int findEndOfString(AppendableCharSequence sb) {
        for (int result = sb.length() - 1; result > 0; result--) {
            if (!Character.isWhitespace(sb.charAtUnsafe(result))) {
                return result + 1;
            }
        }
        return 0;
    }

    private static boolean isOWS(char ch2) {
        return ch2 == ' ' || ch2 == '\t';
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/HttpObjectDecoder$HeaderParser.class */
    private static class HeaderParser implements ByteProcessor {
        private final AppendableCharSequence seq;
        private final int maxLength;
        private int size;

        HeaderParser(AppendableCharSequence seq, int maxLength) {
            this.seq = seq;
            this.maxLength = maxLength;
        }

        public AppendableCharSequence parse(ByteBuf buffer) {
            int oldSize = this.size;
            this.seq.reset();
            int i = buffer.forEachByte(this);
            if (i == -1) {
                this.size = oldSize;
                return null;
            }
            buffer.readerIndex(i + 1);
            return this.seq;
        }

        public void reset() {
            this.size = 0;
        }

        @Override // io.netty.util.ByteProcessor
        public boolean process(byte value) throws Exception {
            char nextByte = (char) (value & 255);
            if (nextByte == '\n') {
                int len = this.seq.length();
                if (len >= 1 && this.seq.charAtUnsafe(len - 1) == '\r') {
                    this.size--;
                    this.seq.setLength(len - 1);
                    return false;
                }
                return false;
            }
            increaseCount();
            this.seq.append(nextByte);
            return true;
        }

        protected final void increaseCount() {
            int i = this.size + 1;
            this.size = i;
            if (i > this.maxLength) {
                throw newException(this.maxLength);
            }
        }

        protected TooLongFrameException newException(int maxLength) {
            return new TooLongFrameException("HTTP header is larger than " + maxLength + " bytes.");
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/HttpObjectDecoder$LineParser.class */
    private final class LineParser extends HeaderParser {
        LineParser(AppendableCharSequence seq, int maxLength) {
            super(seq, maxLength);
        }

        @Override // io.netty.handler.codec.http.HttpObjectDecoder.HeaderParser
        public AppendableCharSequence parse(ByteBuf buffer) {
            reset();
            return super.parse(buffer);
        }

        @Override // io.netty.handler.codec.http.HttpObjectDecoder.HeaderParser, io.netty.util.ByteProcessor
        public boolean process(byte value) throws Exception {
            if (HttpObjectDecoder.this.currentState == State.SKIP_CONTROL_CHARS) {
                char c = (char) (value & 255);
                if (Character.isISOControl(c) || Character.isWhitespace(c)) {
                    increaseCount();
                    return true;
                }
                HttpObjectDecoder.this.currentState = State.READ_INITIAL;
            }
            return super.process(value);
        }

        @Override // io.netty.handler.codec.http.HttpObjectDecoder.HeaderParser
        protected TooLongFrameException newException(int maxLength) {
            return new TooLongFrameException("An HTTP line is larger than " + maxLength + " bytes.");
        }
    }
}
