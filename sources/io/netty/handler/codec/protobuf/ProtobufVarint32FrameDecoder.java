package io.netty.handler.codec.protobuf;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/protobuf/ProtobufVarint32FrameDecoder.class */
public class ProtobufVarint32FrameDecoder extends ByteToMessageDecoder {
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.markReaderIndex();
        int preIndex = in.readerIndex();
        int length = readRawVarint32(in);
        if (preIndex == in.readerIndex()) {
            return;
        }
        if (length < 0) {
            throw new CorruptedFrameException("negative length: " + length);
        }
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
        } else {
            out.add(in.readRetainedSlice(length));
        }
    }

    private static int readRawVarint32(ByteBuf buffer) {
        int result;
        if (!buffer.isReadable()) {
            return 0;
        }
        buffer.markReaderIndex();
        byte tmp = buffer.readByte();
        if (tmp >= 0) {
            return tmp;
        }
        int result2 = tmp & Byte.MAX_VALUE;
        if (!buffer.isReadable()) {
            buffer.resetReaderIndex();
            return 0;
        }
        byte tmp2 = buffer.readByte();
        if (tmp2 >= 0) {
            result = result2 | (tmp2 << 7);
        } else {
            int result3 = result2 | ((tmp2 & Byte.MAX_VALUE) << 7);
            if (!buffer.isReadable()) {
                buffer.resetReaderIndex();
                return 0;
            }
            byte tmp3 = buffer.readByte();
            if (tmp3 >= 0) {
                result = result3 | (tmp3 << 14);
            } else {
                int result4 = result3 | ((tmp3 & Byte.MAX_VALUE) << 14);
                if (!buffer.isReadable()) {
                    buffer.resetReaderIndex();
                    return 0;
                }
                byte tmp4 = buffer.readByte();
                if (tmp4 >= 0) {
                    result = result4 | (tmp4 << 21);
                } else {
                    int result5 = result4 | ((tmp4 & Byte.MAX_VALUE) << 21);
                    if (!buffer.isReadable()) {
                        buffer.resetReaderIndex();
                        return 0;
                    }
                    byte tmp5 = buffer.readByte();
                    result = result5 | (tmp5 << 28);
                    if (tmp5 < 0) {
                        throw new CorruptedFrameException("malformed varint.");
                    }
                }
            }
        }
        return result;
    }
}
