package com.moredian.onpremise.iot;

import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.common.constants.Constants;
import com.moredian.onpremise.core.utils.CacheKeyGenerateUtils;
import com.moredian.onpremise.core.utils.CacheUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: onpremise-iot-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/iot/IOTSession.class */
public class IOTSession {
    private ChannelId channelId;
    private String target;
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) IOTSession.class);
    static final ConcurrentMap<String, ChannelId> TARGET_CHANNEL_MAP = new ConcurrentHashMap();
    static final ChannelGroup CHANNELS = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private IOTSession(ChannelId channelId, String target) {
        this.channelId = channelId;
        this.target = target;
    }

    public ChannelId getChannelId() {
        return this.channelId;
    }

    public void setChannelId(ChannelId channelId) {
        this.channelId = channelId;
    }

    public String getTarget() {
        return this.target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Channel getChannel() {
        Channel ch2 = CHANNELS.find(this.channelId);
        if (ch2 == null) {
            ch2 = getChannelByTarget(getTarget());
        }
        if (ch2 == null) {
            throw new RuntimeException("No channel found with id:" + this.channelId);
        }
        return ch2;
    }

    public static ConcurrentMap<String, ChannelId> getTargetChannelMap() {
        return TARGET_CHANNEL_MAP;
    }

    public static void add(Channel channel) {
        CHANNELS.add(channel);
    }

    public static ChannelGroup getChannels() {
        return CHANNELS;
    }

    public static IOTSession buildSession(ChannelId channelId, String target) {
        IOTSession session = new IOTSession(channelId, target);
        ChannelId channelId1 = TARGET_CHANNEL_MAP.get(target);
        if (channelId1 != null && !channelId.equals(channelId1)) {
            Channel channel = CHANNELS.find(channelId1);
            channel.close();
        }
        TARGET_CHANNEL_MAP.put(target, channelId);
        CacheAdapter.cacheDeviceServerMapInfo(target, CacheUtils.getServerIpAddressInfo(Constants.SERVER_IP_ADDRESS_KEY));
        return session;
    }

    public static void removeChannelById(ChannelId id) {
        Iterator<Map.Entry<String, ChannelId>> entries = TARGET_CHANNEL_MAP.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, ChannelId> entry = entries.next();
            if (id.equals(entry.getValue())) {
                logger.info("==========remove key:{} ,value :{}=======", entry.getKey(), entry.getValue());
                CacheAdapter.removeHeartBeatInfo(CacheKeyGenerateUtils.getHeartBeatCacheKey(entry.getKey()));
                entries.remove();
                CacheAdapter.removeDeviceServerMapInfo(entry.getKey());
                return;
            }
        }
    }

    public static Channel getChannelByTarget(String target) {
        ChannelId id = TARGET_CHANNEL_MAP.get(target);
        if (id == null) {
            logger.info("No channel found with target : {}", target);
            return null;
        }
        Channel ch2 = CHANNELS.find(id);
        if (ch2 == null) {
            logger.info("No channel found with id : {}", id);
        }
        return ch2;
    }
}
