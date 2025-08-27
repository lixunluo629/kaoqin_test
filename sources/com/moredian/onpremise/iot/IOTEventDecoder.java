package com.moredian.onpremise.iot;

import com.moredian.onpremise.event.EventFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: onpremise-iot-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/iot/IOTEventDecoder.class */
public class IOTEventDecoder extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) IOTEventDecoder.class);

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf out, List<Object> list) throws Exception {
        byte[] array = new byte[out.readableBytes()];
        out.getBytes(0, array);
        logger.debug("Buffer length is :{}", Integer.valueOf(array.length));
        ByteBuffer buffer = ByteBuffer.wrap(array);
        while (buffer.hasRemaining()) {
            try {
                try {
                    int position = buffer.position();
                    list.add(EventFactory.getEvent(buffer));
                    int stepLength = buffer.position() - position;
                    out.skipBytes(stepLength);
                } catch (BufferUnderflowException e) {
                    logger.error("Buffer is not complete");
                    buffer.clear();
                    logger.debug("Already decoded {} events from buffer:{}", Integer.valueOf(list.size()), Integer.valueOf(array.length));
                    return;
                }
            } finally {
                buffer.clear();
                logger.debug("Already decoded {} events from buffer:{}", Integer.valueOf(list.size()), Integer.valueOf(array.length));
            }
        }
    }
}
