package io.netty.channel;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/MaxMessagesRecvByteBufAllocator.class */
public interface MaxMessagesRecvByteBufAllocator extends RecvByteBufAllocator {
    int maxMessagesPerRead();

    MaxMessagesRecvByteBufAllocator maxMessagesPerRead(int i);
}
