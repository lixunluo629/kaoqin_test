package com.moredian.onpremise.iot.broadcast;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
/* loaded from: onpremise-iot-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/iot/broadcast/BroadcastReceiver.class */
public class BroadcastReceiver implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) BroadcastHandler.class);

    @Value("${onpremise.broadcast.port:19001}")
    private int broadcastPort = 19001;
    private EventLoopGroup group = new NioEventLoopGroup();

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws Exception {
    }

    @PreDestroy
    public void close() {
    }
}
