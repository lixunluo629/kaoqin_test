package io.netty.channel.group;

import io.netty.channel.Channel;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/group/ChannelMatcher.class */
public interface ChannelMatcher {
    boolean matches(Channel channel);
}
