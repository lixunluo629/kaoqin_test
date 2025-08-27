package io.netty.handler.codec.stomp;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.ByteProcessor;
import io.netty.util.internal.AppendableCharSequence;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/stomp/StompSubframeDecoder.class */
public class StompSubframeDecoder extends ReplayingDecoder<State> {
    private static final int DEFAULT_CHUNK_SIZE = 8132;
    private static final int DEFAULT_MAX_LINE_LENGTH = 1024;
    private final Utf8LineParser commandParser;
    private final HeaderParser headerParser;
    private final int maxChunkSize;
    private int alreadyReadChunkSize;
    private LastStompContentSubframe lastContent;
    private long contentLength;

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/stomp/StompSubframeDecoder$State.class */
    enum State {
        SKIP_CONTROL_CHARACTERS,
        READ_HEADERS,
        READ_CONTENT,
        FINALIZE_FRAME_READ,
        BAD_FRAME,
        INVALID_CHUNK
    }

    public StompSubframeDecoder() {
        this(1024, DEFAULT_CHUNK_SIZE);
    }

    public StompSubframeDecoder(boolean validateHeaders) {
        this(1024, DEFAULT_CHUNK_SIZE, validateHeaders);
    }

    public StompSubframeDecoder(int maxLineLength, int maxChunkSize) {
        this(maxLineLength, maxChunkSize, false);
    }

    public StompSubframeDecoder(int maxLineLength, int maxChunkSize, boolean validateHeaders) {
        super(State.SKIP_CONTROL_CHARACTERS);
        this.contentLength = -1L;
        ObjectUtil.checkPositive(maxLineLength, "maxLineLength");
        ObjectUtil.checkPositive(maxChunkSize, "maxChunkSize");
        this.maxChunkSize = maxChunkSize;
        this.commandParser = new Utf8LineParser(new AppendableCharSequence(16), maxLineLength);
        this.headerParser = new HeaderParser(new AppendableCharSequence(128), maxLineLength, validateHeaders);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:17:0x00cc A[Catch: Exception -> 0x01fe, TryCatch #0 {Exception -> 0x01fe, blocks: (B:15:0x00a2, B:16:0x00b0, B:17:0x00cc, B:20:0x00d8, B:22:0x00e1, B:23:0x00e7, B:25:0x00f0, B:28:0x0108, B:30:0x012b, B:31:0x0142, B:34:0x0156, B:36:0x016e, B:39:0x017d, B:41:0x0194, B:43:0x01b2, B:44:0x01c9, B:40:0x0189, B:46:0x01da, B:48:0x01e5, B:49:0x01ec), top: B:54:0x00a2 }] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x01da A[Catch: Exception -> 0x01fe, FALL_THROUGH, TRY_ENTER, TryCatch #0 {Exception -> 0x01fe, blocks: (B:15:0x00a2, B:16:0x00b0, B:17:0x00cc, B:20:0x00d8, B:22:0x00e1, B:23:0x00e7, B:25:0x00f0, B:28:0x0108, B:30:0x012b, B:31:0x0142, B:34:0x0156, B:36:0x016e, B:39:0x017d, B:41:0x0194, B:43:0x01b2, B:44:0x01c9, B:40:0x0189, B:46:0x01da, B:48:0x01e5, B:49:0x01ec), top: B:54:0x00a2 }] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x01fb A[FALL_THROUGH] */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void decode(io.netty.channel.ChannelHandlerContext r6, io.netty.buffer.ByteBuf r7, java.util.List<java.lang.Object> r8) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 553
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.stomp.StompSubframeDecoder.decode(io.netty.channel.ChannelHandlerContext, io.netty.buffer.ByteBuf, java.util.List):void");
    }

    private StompCommand readCommand(ByteBuf in) {
        CharSequence commandSequence = this.commandParser.parse(in);
        if (commandSequence == null) {
            throw new DecoderException("Failed to read command from channel");
        }
        String commandStr = commandSequence.toString();
        try {
            return StompCommand.valueOf(commandStr);
        } catch (IllegalArgumentException e) {
            throw new DecoderException("Cannot to parse command " + commandStr);
        }
    }

    private State readHeaders(ByteBuf buffer, StompHeaders headers) {
        boolean headerRead;
        do {
            headerRead = this.headerParser.parseHeader(headers, buffer);
        } while (headerRead);
        if (headers.contains(StompHeaders.CONTENT_LENGTH)) {
            this.contentLength = getContentLength(headers);
            if (this.contentLength == 0) {
                return State.FINALIZE_FRAME_READ;
            }
        }
        return State.READ_CONTENT;
    }

    private static long getContentLength(StompHeaders headers) {
        long contentLength = headers.getLong(StompHeaders.CONTENT_LENGTH, 0L);
        if (contentLength < 0) {
            throw new DecoderException(((Object) StompHeaders.CONTENT_LENGTH) + " must be non-negative");
        }
        return contentLength;
    }

    private static void skipNullCharacter(ByteBuf buffer) {
        byte b = buffer.readByte();
        if (b != 0) {
            throw new IllegalStateException("unexpected byte in buffer " + ((int) b) + " while expecting NULL byte");
        }
    }

    private static void skipControlCharacters(ByteBuf buffer) {
        while (true) {
            byte b = buffer.readByte();
            if (b != 13 && b != 10) {
                buffer.readerIndex(buffer.readerIndex() - 1);
                return;
            }
        }
    }

    private void resetDecoder() {
        checkpoint(State.SKIP_CONTROL_CHARACTERS);
        this.contentLength = -1L;
        this.alreadyReadChunkSize = 0;
        this.lastContent = null;
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/stomp/StompSubframeDecoder$Utf8LineParser.class */
    private static class Utf8LineParser implements ByteProcessor {
        private final AppendableCharSequence charSeq;
        private final int maxLineLength;
        private int lineLength;
        private char interim;
        private boolean nextRead;

        Utf8LineParser(AppendableCharSequence charSeq, int maxLineLength) {
            this.charSeq = (AppendableCharSequence) ObjectUtil.checkNotNull(charSeq, "charSeq");
            this.maxLineLength = maxLineLength;
        }

        AppendableCharSequence parse(ByteBuf byteBuf) {
            reset();
            int offset = byteBuf.forEachByte(this);
            if (offset == -1) {
                return null;
            }
            byteBuf.readerIndex(offset + 1);
            return this.charSeq;
        }

        AppendableCharSequence charSequence() {
            return this.charSeq;
        }

        @Override // io.netty.util.ByteProcessor
        public boolean process(byte nextByte) throws Exception {
            if (nextByte == 13) {
                this.lineLength++;
                return true;
            }
            if (nextByte == 10) {
                return false;
            }
            int i = this.lineLength + 1;
            this.lineLength = i;
            if (i > this.maxLineLength) {
                throw new TooLongFrameException("An STOMP line is larger than " + this.maxLineLength + " bytes.");
            }
            if (this.nextRead) {
                this.interim = (char) (this.interim | ((nextByte & 63) << 6));
                this.nextRead = false;
                return true;
            }
            if (this.interim != 0) {
                this.charSeq.append((char) (this.interim | (nextByte & 63)));
                this.interim = (char) 0;
                return true;
            }
            if (nextByte >= 0) {
                this.charSeq.append((char) nextByte);
                return true;
            }
            if ((nextByte & 224) == 192) {
                this.interim = (char) ((nextByte & 31) << 6);
                return true;
            }
            this.interim = (char) ((nextByte & 15) << 12);
            this.nextRead = true;
            return true;
        }

        protected void reset() {
            this.charSeq.reset();
            this.lineLength = 0;
            this.interim = (char) 0;
            this.nextRead = false;
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/stomp/StompSubframeDecoder$HeaderParser.class */
    private static final class HeaderParser extends Utf8LineParser {
        private final boolean validateHeaders;
        private String name;
        private boolean valid;

        HeaderParser(AppendableCharSequence charSeq, int maxLineLength, boolean validateHeaders) {
            super(charSeq, maxLineLength);
            this.validateHeaders = validateHeaders;
        }

        boolean parseHeader(StompHeaders headers, ByteBuf buf) {
            AppendableCharSequence value = super.parse(buf);
            if (value == null) {
                return false;
            }
            if (this.name == null && value.length() == 0) {
                return false;
            }
            if (this.valid) {
                headers.add((StompHeaders) this.name, value.toString());
                return true;
            }
            if (this.validateHeaders) {
                if (StringUtil.isNullOrEmpty(this.name)) {
                    throw new IllegalArgumentException("received an invalid header line '" + value.toString() + '\'');
                }
                String line = this.name + ':' + value.toString();
                throw new IllegalArgumentException("a header value or name contains a prohibited character ':', " + line);
            }
            return true;
        }

        @Override // io.netty.handler.codec.stomp.StompSubframeDecoder.Utf8LineParser, io.netty.util.ByteProcessor
        public boolean process(byte nextByte) throws Exception {
            if (nextByte == 58) {
                if (this.name == null) {
                    AppendableCharSequence charSeq = charSequence();
                    if (charSeq.length() != 0) {
                        this.name = charSeq.substring(0, charSeq.length());
                        charSeq.reset();
                        this.valid = true;
                        return true;
                    }
                    this.name = "";
                } else {
                    this.valid = false;
                }
            }
            return super.process(nextByte);
        }

        @Override // io.netty.handler.codec.stomp.StompSubframeDecoder.Utf8LineParser
        protected void reset() {
            this.name = null;
            this.valid = false;
            super.reset();
        }
    }
}
