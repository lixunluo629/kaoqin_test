package com.moredian.onpremise.iot;

import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.model.info.CacheHeartBeatInfo;
import com.moredian.onpremise.core.utils.CacheKeyGenerateUtils;
import com.moredian.onpremise.core.utils.MyDateUtils;
import com.moredian.onpremise.event.IOTEvent;
import com.moredian.onpremise.event.IOTEventType;
import com.moredian.onpremise.iot.handle.IOTEventHandler;
import com.moredian.onpremise.iot.handle.IOTEventListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
/* loaded from: onpremise-iot-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/iot/IOTHubHandler.class */
public class IOTHubHandler extends SimpleChannelInboundHandler<IOTEvent> implements IOTEventHandler {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) IOTHubHandler.class);
    private static final IOTHubHandler INSTANCE = new IOTHubHandler();
    protected final Map<Class<?>, List<IOTEventListener>> listeners = new HashMap(5);

    private IOTHubHandler() {
    }

    public static IOTHubHandler getInstance() {
        return INSTANCE;
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("channel {} active ", ctx.channel().id());
        IOTSession.add(ctx.channel());
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("channel {} inactive ", ctx.channel().id());
        super.channelInactive(ctx);
        IOTSession.removeChannelById(ctx.channel().id());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.SimpleChannelInboundHandler
    public void channelRead0(ChannelHandlerContext channelHandlerContext, IOTEvent iotEvent) throws Exception {
        logger.debug("channel {} read0 ,remote address :{} ,local address :{}", channelHandlerContext.channel().id(), ((InetSocketAddress) channelHandlerContext.channel().remoteAddress()).getAddress().getHostAddress(), ((InetSocketAddress) channelHandlerContext.channel().localAddress()).getAddress().getHostAddress());
        IOTSession session = IOTSession.buildSession(channelHandlerContext.channel().id(), iotEvent.target());
        IOTContext context = IOTContext.buildContext(iotEvent, session);
        handle(context);
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("channel {} registered", ctx.channel().id());
        super.channelRegistered(ctx);
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof IOException) {
            logger.error("Channel {} caught exception:{}", ctx.channel().id(), cause.getMessage());
        } else {
            logger.error("Channel {} caught exception:{}", ctx.channel().id(), cause);
        }
    }

    private void handle(IOTContext<IOTEvent> context) {
        IOTEvent event = context.getSourceEvent();
        logger.debug("Begin to handle an event: {} from {}", event.toString(), event.getSerialNumber());
        List<IOTEventListener> list = this.listeners.get(event.getClass());
        if (list == null) {
            logger.error("No listener for event:{}", event.getClass().getName());
            return;
        }
        if (!IOTEventType.HEARTBEAT.equals(event.getEventType())) {
            logger.debug("Cache heart beat info :{}", Integer.valueOf(event.getEventType()));
            cacheDeviceInfo(event);
        }
        for (IOTEventListener h : list) {
            try {
                h.handleEvent(context);
            } catch (Exception e) {
                logger.error("Failed to handle {} by {}", event, h, e);
            }
        }
    }

    @Override // com.moredian.onpremise.iot.handle.IOTEventHandler
    public void registry(IOTEventListener listener) {
        for (Class<?> c : listener.getSupportedEvents()) {
            List<IOTEventListener> list = this.listeners.get(c);
            if (list == null) {
                list = new LinkedList();
                this.listeners.put(c, list);
            }
            if (!list.contains(listener)) {
                list.add(listener);
            }
        }
    }

    private void cacheDeviceInfo(IOTEvent event) {
        String key = CacheKeyGenerateUtils.getHeartBeatCacheKey(event.getSerialNumber());
        if (CacheAdapter.getHeartBeatInfo(key) != null) {
            CacheAdapter.removeHeartBeatInfo(key);
        }
        CacheHeartBeatInfo heartBeatInfo = new CacheHeartBeatInfo();
        heartBeatInfo.setIpAddress(event.getIpAddress());
        heartBeatInfo.setMacAddress(event.getMacAddress());
        heartBeatInfo.setDeviceSn(event.getSerialNumber());
        heartBeatInfo.setExpireTime(Long.valueOf(MyDateUtils.addSeconds(new Date(), 35).getTime()));
        heartBeatInfo.setSoftVersion(event.getSoftwareVersion());
        heartBeatInfo.setRomVersion(event.getRomVersion());
        CacheAdapter.cacheHeartBeatInfo(key, heartBeatInfo);
    }
}
