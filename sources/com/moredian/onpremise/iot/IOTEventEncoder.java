package com.moredian.onpremise.iot;

import com.moredian.onpremise.event.IOTEvent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/* loaded from: onpremise-iot-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/iot/IOTEventEncoder.class */
public class IOTEventEncoder extends MessageToByteEncoder {
    @Override // io.netty.handler.codec.MessageToByteEncoder
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf out) throws Exception {
        IOTEvent event = (IOTEvent) o;
        out.writeBytes(event.buildPacket());
    }
}
