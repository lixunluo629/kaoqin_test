package io.netty.channel;

import java.io.Serializable;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/ChannelId.class */
public interface ChannelId extends Serializable, Comparable<ChannelId> {
    String asShortText();

    String asLongText();
}
