package com.moredian.onpremise.iot.broadcast;

import com.moredian.onpremise.event.AckFromDeviceEvent;
import com.moredian.onpremise.event.DeviceHeartbeatEvent;
import com.moredian.onpremise.event.EventFactory;
import com.moredian.onpremise.event.HelloWorldEvent;
import com.moredian.onpremise.event.IOTEvent;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: onpremise-iot-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/iot/broadcast/BroadcastHandler.class */
public class BroadcastHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) BroadcastHandler.class);

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.SimpleChannelInboundHandler
    public void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        logger.info("Receive one packet from '{}'", datagramPacket.sender());
        IOTEvent event = EventFactory.getEvent(((ByteBuf) datagramPacket.content()).nioBuffer());
        if (event != null) {
            if (event instanceof HelloWorldEvent) {
                HelloWorldEvent hwEvent = (HelloWorldEvent) event;
                logger.info("Received HelloWorld event from '{}'", hwEvent.getSerialNumber());
                HelloWorldEvent hello = new HelloWorldEvent();
                channelHandlerContext.writeAndFlush(new DatagramPacket(Unpooled.wrappedBuffer(hello.buildPacket()), datagramPacket.sender()));
                logger.info("Answer HelloWorldEvent to '{}'", hwEvent.getSerialNumber());
                return;
            }
            if (event instanceof AckFromDeviceEvent) {
                AckFromDeviceEvent ackFromDeviceEvent = (AckFromDeviceEvent) event;
                logger.info("Received ackFromDevice event from '{}'", ackFromDeviceEvent.getSerialNumber());
            } else if (event instanceof DeviceHeartbeatEvent) {
                DeviceHeartbeatEvent deviceHeartbeatEvent = (DeviceHeartbeatEvent) event;
                logger.info("Received deviceHeartbeat event from '{}'", deviceHeartbeatEvent.getSerialNumber());
            }
        }
    }
}
