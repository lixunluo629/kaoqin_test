package io.netty.channel.pool;

import io.netty.channel.pool.ChannelPool;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/pool/ChannelPoolMap.class */
public interface ChannelPoolMap<K, P extends ChannelPool> {
    P get(K k);

    boolean contains(K k);
}
