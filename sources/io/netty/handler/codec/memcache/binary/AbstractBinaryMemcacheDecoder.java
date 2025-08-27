package io.netty.handler.codec.memcache.binary;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.memcache.AbstractMemcacheObjectDecoder;
import io.netty.handler.codec.memcache.DefaultLastMemcacheContent;
import io.netty.handler.codec.memcache.DefaultMemcacheContent;
import io.netty.handler.codec.memcache.LastMemcacheContent;
import io.netty.handler.codec.memcache.MemcacheContent;
import io.netty.handler.codec.memcache.binary.BinaryMemcacheMessage;
import io.netty.util.internal.ObjectUtil;
import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/memcache/binary/AbstractBinaryMemcacheDecoder.class */
public abstract class AbstractBinaryMemcacheDecoder<M extends BinaryMemcacheMessage> extends AbstractMemcacheObjectDecoder {
    public static final int DEFAULT_MAX_CHUNK_SIZE = 8192;
    private final int chunkSize;
    private M currentMessage;
    private int alreadyReadChunkSize;
    private State state;

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/memcache/binary/AbstractBinaryMemcacheDecoder$State.class */
    enum State {
        READ_HEADER,
        READ_EXTRAS,
        READ_KEY,
        READ_CONTENT,
        BAD_MESSAGE
    }

    protected abstract M decodeHeader(ByteBuf byteBuf);

    protected abstract M buildInvalidMessage();

    protected AbstractBinaryMemcacheDecoder() {
        this(8192);
    }

    protected AbstractBinaryMemcacheDecoder(int chunkSize) {
        this.state = State.READ_HEADER;
        ObjectUtil.checkPositiveOrZero(chunkSize, "chunkSize");
        this.chunkSize = chunkSize;
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        DefaultMemcacheContent defaultMemcacheContent;
        switch (this.state) {
            case READ_HEADER:
                try {
                    if (byteBuf.readableBytes() < 24) {
                        return;
                    }
                    resetDecoder();
                    this.currentMessage = (M) decodeHeader(byteBuf);
                    this.state = State.READ_EXTRAS;
                } catch (Exception e) {
                    resetDecoder();
                    list.add(invalidMessage(e));
                    return;
                }
            case READ_EXTRAS:
                try {
                    byte bExtrasLength = this.currentMessage.extrasLength();
                    if (bExtrasLength > 0) {
                        if (byteBuf.readableBytes() < bExtrasLength) {
                            return;
                        } else {
                            this.currentMessage.setExtras(byteBuf.readRetainedSlice(bExtrasLength));
                        }
                    }
                    this.state = State.READ_KEY;
                } catch (Exception e2) {
                    resetDecoder();
                    list.add(invalidMessage(e2));
                    return;
                }
            case READ_KEY:
                try {
                    short sKeyLength = this.currentMessage.keyLength();
                    if (sKeyLength > 0) {
                        if (byteBuf.readableBytes() < sKeyLength) {
                            return;
                        } else {
                            this.currentMessage.setKey(byteBuf.readRetainedSlice(sKeyLength));
                        }
                    }
                    list.add(this.currentMessage.retain());
                    this.state = State.READ_CONTENT;
                } catch (Exception e3) {
                    resetDecoder();
                    list.add(invalidMessage(e3));
                    return;
                }
            case READ_CONTENT:
                try {
                    int iKeyLength = (this.currentMessage.totalBodyLength() - this.currentMessage.keyLength()) - this.currentMessage.extrasLength();
                    int i = byteBuf.readableBytes();
                    if (iKeyLength > 0) {
                        if (i == 0) {
                            return;
                        }
                        if (i > this.chunkSize) {
                            i = this.chunkSize;
                        }
                        int i2 = iKeyLength - this.alreadyReadChunkSize;
                        if (i > i2) {
                            i = i2;
                        }
                        ByteBuf retainedSlice = byteBuf.readRetainedSlice(i);
                        int i3 = this.alreadyReadChunkSize + i;
                        this.alreadyReadChunkSize = i3;
                        if (i3 >= iKeyLength) {
                            defaultMemcacheContent = new DefaultLastMemcacheContent(retainedSlice);
                        } else {
                            defaultMemcacheContent = new DefaultMemcacheContent(retainedSlice);
                        }
                        list.add(defaultMemcacheContent);
                        if (this.alreadyReadChunkSize < iKeyLength) {
                            return;
                        }
                    } else {
                        list.add(LastMemcacheContent.EMPTY_LAST_CONTENT);
                    }
                    resetDecoder();
                    this.state = State.READ_HEADER;
                    return;
                } catch (Exception e4) {
                    resetDecoder();
                    list.add(invalidChunk(e4));
                    return;
                }
            case BAD_MESSAGE:
                byteBuf.skipBytes(actualReadableBytes());
                return;
            default:
                throw new Error("Unknown state reached: " + this.state);
        }
    }

    private M invalidMessage(Exception exc) {
        this.state = State.BAD_MESSAGE;
        M m = (M) buildInvalidMessage();
        m.setDecoderResult(DecoderResult.failure(exc));
        return m;
    }

    private MemcacheContent invalidChunk(Exception cause) {
        this.state = State.BAD_MESSAGE;
        MemcacheContent chunk = new DefaultLastMemcacheContent(Unpooled.EMPTY_BUFFER);
        chunk.setDecoderResult(DecoderResult.failure(cause));
        return chunk;
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder, io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        resetDecoder();
    }

    protected void resetDecoder() {
        if (this.currentMessage != null) {
            this.currentMessage.release();
            this.currentMessage = null;
        }
        this.alreadyReadChunkSize = 0;
    }
}
