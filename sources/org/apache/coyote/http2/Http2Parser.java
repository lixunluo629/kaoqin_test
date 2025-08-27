package org.apache.coyote.http2;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import org.apache.coyote.ProtocolException;
import org.apache.coyote.http2.HpackDecoder;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.buf.ByteBufferUtils;
import org.apache.tomcat.util.res.StringManager;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http2/Http2Parser.class */
class Http2Parser {
    private static final Log log = LogFactory.getLog((Class<?>) Http2Parser.class);
    private static final StringManager sm = StringManager.getManager((Class<?>) Http2Parser.class);
    static final byte[] CLIENT_PREFACE_START = "PRI * HTTP/2.0\r\n\r\nSM\r\n\r\n".getBytes(StandardCharsets.ISO_8859_1);
    private final String connectionId;
    private final Input input;
    private final Output output;
    private volatile HpackDecoder hpackDecoder;
    private final byte[] frameHeaderBuffer = new byte[9];
    private volatile ByteBuffer headerReadBuffer = ByteBuffer.allocate(1024);
    private volatile int headersCurrentStream = -1;
    private volatile boolean headersEndStream = false;

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http2/Http2Parser$Input.class */
    interface Input {
        boolean fill(boolean z, byte[] bArr, int i, int i2) throws IOException;

        boolean fill(boolean z, byte[] bArr) throws IOException;

        boolean fill(boolean z, ByteBuffer byteBuffer, int i) throws IOException;

        int getMaxFrameSize();
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http2/Http2Parser$Output.class */
    interface Output {
        HpackDecoder getHpackDecoder();

        ByteBuffer startRequestBodyFrame(int i, int i2) throws Http2Exception;

        void endRequestBodyFrame(int i) throws Http2Exception;

        void receivedEndOfStream(int i) throws ConnectionException;

        void swallowedPadding(int i, int i2) throws IOException, ConnectionException;

        HpackDecoder.HeaderEmitter headersStart(int i, boolean z) throws IOException, Http2Exception;

        void headersEnd(int i) throws ConnectionException;

        void reprioritise(int i, int i2, boolean z, int i3) throws Http2Exception;

        void reset(int i, long j) throws Http2Exception;

        void setting(Setting setting, long j) throws ConnectionException;

        void settingsEnd(boolean z) throws IOException;

        void pingReceive(byte[] bArr, boolean z) throws IOException;

        void goaway(int i, long j, String str);

        void incrementWindowSize(int i, int i2) throws Http2Exception;

        void swallowed(int i, FrameType frameType, int i2, int i3) throws IOException;
    }

    Http2Parser(String connectionId, Input input, Output output) {
        this.connectionId = connectionId;
        this.input = input;
        this.output = output;
    }

    boolean readFrame(boolean block) throws IOException, Http2Exception {
        return readFrame(block, null);
    }

    private boolean readFrame(boolean block, FrameType expected) throws IOException, Http2Exception {
        if (!this.input.fill(block, this.frameHeaderBuffer)) {
            return false;
        }
        int payloadSize = ByteUtil.getThreeBytes(this.frameHeaderBuffer, 0);
        FrameType frameType = FrameType.valueOf(ByteUtil.getOneByte(this.frameHeaderBuffer, 3));
        int flags = ByteUtil.getOneByte(this.frameHeaderBuffer, 4);
        int streamId = ByteUtil.get31Bits(this.frameHeaderBuffer, 5);
        try {
            validateFrame(expected, frameType, streamId, flags, payloadSize);
            switch (frameType) {
                case DATA:
                    readDataFrame(streamId, flags, payloadSize);
                    return true;
                case HEADERS:
                    readHeadersFrame(streamId, flags, payloadSize);
                    return true;
                case PRIORITY:
                    readPriorityFrame(streamId);
                    return true;
                case RST:
                    readRstFrame(streamId);
                    return true;
                case SETTINGS:
                    readSettingsFrame(flags, payloadSize);
                    return true;
                case PUSH_PROMISE:
                    readPushPromiseFrame(streamId);
                    return true;
                case PING:
                    readPingFrame(flags);
                    return true;
                case GOAWAY:
                    readGoawayFrame(payloadSize);
                    return true;
                case WINDOW_UPDATE:
                    readWindowUpdateFrame(streamId);
                    return true;
                case CONTINUATION:
                    readContinuationFrame(streamId, flags, payloadSize);
                    return true;
                case UNKNOWN:
                    readUnknownFrame(streamId, frameType, flags, payloadSize);
                    return true;
                default:
                    return true;
            }
        } catch (StreamException se) {
            swallow(streamId, payloadSize, false);
            throw se;
        }
    }

    private void readDataFrame(int streamId, int flags, int payloadSize) throws IOException, Http2Exception {
        int dataLength;
        String padding;
        int padLength = 0;
        boolean endOfStream = Flags.isEndOfStream(flags);
        if (Flags.hasPadding(flags)) {
            byte[] b = new byte[1];
            this.input.fill(true, b);
            padLength = b[0] & 255;
            if (padLength >= payloadSize) {
                throw new ConnectionException(sm.getString("http2Parser.processFrame.tooMuchPadding", this.connectionId, Integer.toString(streamId), Integer.toString(padLength), Integer.toString(payloadSize)), Http2Error.PROTOCOL_ERROR);
            }
            dataLength = payloadSize - (padLength + 1);
        } else {
            dataLength = payloadSize;
        }
        if (log.isDebugEnabled()) {
            if (Flags.hasPadding(flags)) {
                padding = Integer.toString(padLength);
            } else {
                padding = "none";
            }
            log.debug(sm.getString("http2Parser.processFrameData.lengths", this.connectionId, Integer.toString(streamId), Integer.toString(dataLength), padding));
        }
        ByteBuffer dest = this.output.startRequestBodyFrame(streamId, payloadSize);
        if (dest == null) {
            swallow(streamId, dataLength, false);
            if (padLength > 0) {
                swallow(streamId, padLength, true);
            }
            if (endOfStream) {
                this.output.receivedEndOfStream(streamId);
            }
        } else {
            synchronized (dest) {
                if (dest.remaining() < dataLength) {
                    swallow(streamId, dataLength, false);
                    throw new StreamException(sm.getString("http2Parser.processFrameData.window", this.connectionId), Http2Error.FLOW_CONTROL_ERROR, streamId);
                }
                this.input.fill(true, dest, dataLength);
                if (padLength > 0) {
                    swallow(streamId, padLength, true);
                }
                if (endOfStream) {
                    this.output.receivedEndOfStream(streamId);
                }
                this.output.endRequestBodyFrame(streamId);
            }
        }
        if (padLength > 0) {
            this.output.swallowedPadding(streamId, padLength);
        }
    }

    private void readHeadersFrame(int streamId, int flags, int payloadSize) throws IOException, Http2Exception {
        this.headersEndStream = Flags.isEndOfStream(flags);
        if (this.hpackDecoder == null) {
            this.hpackDecoder = this.output.getHpackDecoder();
        }
        try {
            this.hpackDecoder.setHeaderEmitter(this.output.headersStart(streamId, this.headersEndStream));
            int padLength = 0;
            boolean padding = Flags.hasPadding(flags);
            boolean priority = Flags.hasPriority(flags);
            int optionalLen = 0;
            if (padding) {
                optionalLen = 1;
            }
            if (priority) {
                optionalLen += 5;
            }
            if (optionalLen > 0) {
                byte[] optional = new byte[optionalLen];
                this.input.fill(true, optional);
                int optionalPos = 0;
                if (padding) {
                    optionalPos = 0 + 1;
                    padLength = ByteUtil.getOneByte(optional, 0);
                    if (padLength >= payloadSize) {
                        throw new ConnectionException(sm.getString("http2Parser.processFrame.tooMuchPadding", this.connectionId, Integer.toString(streamId), Integer.toString(padLength), Integer.toString(payloadSize)), Http2Error.PROTOCOL_ERROR);
                    }
                }
                if (priority) {
                    boolean exclusive = ByteUtil.isBit7Set(optional[optionalPos]);
                    int parentStreamId = ByteUtil.get31Bits(optional, optionalPos);
                    int weight = ByteUtil.getOneByte(optional, optionalPos + 4) + 1;
                    this.output.reprioritise(streamId, parentStreamId, exclusive, weight);
                }
                payloadSize = (payloadSize - optionalLen) - padLength;
            }
            readHeaderPayload(streamId, payloadSize);
            swallow(streamId, padLength, true);
            if (Flags.isEndOfHeaders(flags)) {
                onHeadersComplete(streamId);
            } else {
                this.headersCurrentStream = streamId;
            }
        } catch (StreamException se) {
            swallow(streamId, payloadSize, false);
            throw se;
        }
    }

    private void readPriorityFrame(int streamId) throws IOException, Http2Exception {
        byte[] payload = new byte[5];
        this.input.fill(true, payload);
        boolean exclusive = ByteUtil.isBit7Set(payload[0]);
        int parentStreamId = ByteUtil.get31Bits(payload, 0);
        int weight = ByteUtil.getOneByte(payload, 4) + 1;
        if (streamId == parentStreamId) {
            throw new StreamException(sm.getString("http2Parser.processFramePriority.invalidParent", this.connectionId, Integer.valueOf(streamId)), Http2Error.PROTOCOL_ERROR, streamId);
        }
        this.output.reprioritise(streamId, parentStreamId, exclusive, weight);
    }

    private void readRstFrame(int streamId) throws IOException, Http2Exception {
        byte[] payload = new byte[4];
        this.input.fill(true, payload);
        long errorCode = ByteUtil.getFourBytes(payload, 0);
        this.output.reset(streamId, errorCode);
        this.headersCurrentStream = -1;
        this.headersEndStream = false;
    }

    private void readSettingsFrame(int flags, int payloadSize) throws IOException, Http2Exception {
        boolean ack = Flags.isAck(flags);
        if (payloadSize > 0 && ack) {
            throw new ConnectionException(sm.getString("http2Parser.processFrameSettings.ackWithNonZeroPayload"), Http2Error.FRAME_SIZE_ERROR);
        }
        if (payloadSize != 0) {
            byte[] setting = new byte[6];
            for (int i = 0; i < payloadSize / 6; i++) {
                this.input.fill(true, setting);
                int id = ByteUtil.getTwoBytes(setting, 0);
                long value = ByteUtil.getFourBytes(setting, 2);
                this.output.setting(Setting.valueOf(id), value);
            }
        }
        this.output.settingsEnd(ack);
    }

    private void readPushPromiseFrame(int streamId) throws Http2Exception {
        throw new ConnectionException(sm.getString("http2Parser.processFramePushPromise", this.connectionId, Integer.valueOf(streamId)), Http2Error.PROTOCOL_ERROR);
    }

    private void readPingFrame(int flags) throws IOException {
        byte[] payload = new byte[8];
        this.input.fill(true, payload);
        this.output.pingReceive(payload, Flags.isAck(flags));
    }

    private void readGoawayFrame(int payloadSize) throws IOException {
        byte[] payload = new byte[payloadSize];
        this.input.fill(true, payload);
        int lastStreamId = ByteUtil.get31Bits(payload, 0);
        long errorCode = ByteUtil.getFourBytes(payload, 4);
        String debugData = null;
        if (payloadSize > 8) {
            debugData = new String(payload, 8, payloadSize - 8, StandardCharsets.UTF_8);
        }
        this.output.goaway(lastStreamId, errorCode, debugData);
    }

    private void readWindowUpdateFrame(int streamId) throws IOException, Http2Exception {
        byte[] payload = new byte[4];
        this.input.fill(true, payload);
        int windowSizeIncrement = ByteUtil.get31Bits(payload, 0);
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("http2Parser.processFrameWindowUpdate.debug", this.connectionId, Integer.toString(streamId), Integer.toString(windowSizeIncrement)));
        }
        if (windowSizeIncrement == 0) {
            if (streamId == 0) {
                throw new ConnectionException(sm.getString("http2Parser.processFrameWindowUpdate.invalidIncrement"), Http2Error.PROTOCOL_ERROR);
            }
            throw new StreamException(sm.getString("http2Parser.processFrameWindowUpdate.invalidIncrement"), Http2Error.PROTOCOL_ERROR, streamId);
        }
        this.output.incrementWindowSize(streamId, windowSizeIncrement);
    }

    private void readContinuationFrame(int streamId, int flags, int payloadSize) throws IOException, Http2Exception {
        if (this.headersCurrentStream == -1) {
            throw new ConnectionException(sm.getString("http2Parser.processFrameContinuation.notExpected", this.connectionId, Integer.toString(streamId)), Http2Error.PROTOCOL_ERROR);
        }
        readHeaderPayload(streamId, payloadSize);
        if (Flags.isEndOfHeaders(flags)) {
            this.headersCurrentStream = -1;
            onHeadersComplete(streamId);
        }
    }

    private void readHeaderPayload(int streamId, int payloadSize) throws IOException, Http2Exception {
        int newSize;
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("http2Parser.processFrameHeaders.payload", this.connectionId, Integer.valueOf(streamId), Integer.valueOf(payloadSize)));
        }
        int remaining = payloadSize;
        while (remaining > 0) {
            if (this.headerReadBuffer.remaining() == 0) {
                if (this.headerReadBuffer.capacity() < payloadSize) {
                    newSize = payloadSize;
                } else {
                    newSize = this.headerReadBuffer.capacity() * 2;
                }
                this.headerReadBuffer = ByteBufferUtils.expand(this.headerReadBuffer, newSize);
            }
            int toRead = Math.min(this.headerReadBuffer.remaining(), remaining);
            this.input.fill(true, this.headerReadBuffer, toRead);
            this.headerReadBuffer.flip();
            try {
                this.hpackDecoder.decode(this.headerReadBuffer);
                this.headerReadBuffer.compact();
                remaining -= toRead;
                if (this.hpackDecoder.isHeaderCountExceeded()) {
                    StreamException headerException = new StreamException(sm.getString("http2Parser.headerLimitCount", this.connectionId, Integer.valueOf(streamId)), Http2Error.ENHANCE_YOUR_CALM, streamId);
                    this.hpackDecoder.getHeaderEmitter().setHeaderException(headerException);
                }
                if (this.hpackDecoder.isHeaderSizeExceeded(this.headerReadBuffer.position())) {
                    StreamException headerException2 = new StreamException(sm.getString("http2Parser.headerLimitSize", this.connectionId, Integer.valueOf(streamId)), Http2Error.ENHANCE_YOUR_CALM, streamId);
                    this.hpackDecoder.getHeaderEmitter().setHeaderException(headerException2);
                }
                if (this.hpackDecoder.isHeaderSwallowSizeExceeded(this.headerReadBuffer.position())) {
                    throw new ConnectionException(sm.getString("http2Parser.headerLimitSize", this.connectionId, Integer.valueOf(streamId)), Http2Error.ENHANCE_YOUR_CALM);
                }
            } catch (HpackException hpe) {
                throw new ConnectionException(sm.getString("http2Parser.processFrameHeaders.decodingFailed"), Http2Error.COMPRESSION_ERROR, hpe);
            }
        }
    }

    private void readUnknownFrame(int streamId, FrameType frameType, int flags, int payloadSize) throws IOException {
        try {
            swallow(streamId, payloadSize, false);
        } catch (ConnectionException e) {
        }
        this.output.swallowed(streamId, frameType, flags, payloadSize);
    }

    private void swallow(int streamId, int len, boolean mustBeZero) throws IOException, ConnectionException {
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("http2Parser.swallow.debug", this.connectionId, Integer.toString(streamId), Integer.toString(len)));
        }
        if (len == 0) {
            return;
        }
        int read = 0;
        byte[] buffer = new byte[1024];
        while (read < len) {
            int thisTime = Math.min(buffer.length, len - read);
            this.input.fill(true, buffer, 0, thisTime);
            if (mustBeZero) {
                for (int i = 0; i < thisTime; i++) {
                    if (buffer[i] != 0) {
                        throw new ConnectionException(sm.getString("http2Parser.nonZeroPadding", this.connectionId, Integer.toString(streamId)), Http2Error.PROTOCOL_ERROR);
                    }
                }
            }
            read += thisTime;
        }
    }

    private void onHeadersComplete(int streamId) throws Http2Exception {
        if (this.headerReadBuffer.position() > 0) {
            throw new ConnectionException(sm.getString("http2Parser.processFrameHeaders.decodingDataLeft"), Http2Error.COMPRESSION_ERROR);
        }
        this.hpackDecoder.getHeaderEmitter().validateHeaders();
        this.output.headersEnd(streamId);
        if (this.headersEndStream) {
            this.output.receivedEndOfStream(streamId);
            this.headersEndStream = false;
        }
        if (this.headerReadBuffer.capacity() > 1024) {
            this.headerReadBuffer = ByteBuffer.allocate(1024);
        }
    }

    private void validateFrame(FrameType expected, FrameType frameType, int streamId, int flags, int payloadSize) throws Http2Exception {
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("http2Parser.processFrame", this.connectionId, Integer.toString(streamId), frameType, Integer.toString(flags), Integer.toString(payloadSize)));
        }
        if (expected != null && frameType != expected) {
            throw new StreamException(sm.getString("http2Parser.processFrame.unexpectedType", expected, frameType), Http2Error.PROTOCOL_ERROR, streamId);
        }
        int maxFrameSize = this.input.getMaxFrameSize();
        if (payloadSize > maxFrameSize) {
            throw new ConnectionException(sm.getString("http2Parser.payloadTooBig", Integer.toString(payloadSize), Integer.toString(maxFrameSize)), Http2Error.FRAME_SIZE_ERROR);
        }
        if (this.headersCurrentStream != -1) {
            if (this.headersCurrentStream != streamId) {
                throw new ConnectionException(sm.getString("http2Parser.headers.wrongStream", this.connectionId, Integer.toString(this.headersCurrentStream), Integer.toString(streamId)), Http2Error.COMPRESSION_ERROR);
            }
            if (frameType != FrameType.RST && frameType != FrameType.CONTINUATION) {
                throw new ConnectionException(sm.getString("http2Parser.headers.wrongFrameType", this.connectionId, Integer.toString(this.headersCurrentStream), frameType), Http2Error.COMPRESSION_ERROR);
            }
        }
        frameType.check(streamId, payloadSize);
    }

    void readConnectionPreface() throws Http2Exception {
        byte[] data = new byte[CLIENT_PREFACE_START.length];
        try {
            this.input.fill(true, data);
            for (int i = 0; i < CLIENT_PREFACE_START.length; i++) {
                if (CLIENT_PREFACE_START[i] != data[i]) {
                    throw new ProtocolException(sm.getString("http2Parser.preface.invalid"));
                }
            }
            readFrame(true, FrameType.SETTINGS);
        } catch (IOException ioe) {
            throw new ProtocolException(sm.getString("http2Parser.preface.io"), ioe);
        }
    }
}
