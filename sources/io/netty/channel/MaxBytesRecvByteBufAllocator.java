package io.netty.channel;

import java.util.Map;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/MaxBytesRecvByteBufAllocator.class */
public interface MaxBytesRecvByteBufAllocator extends RecvByteBufAllocator {
    int maxBytesPerRead();

    MaxBytesRecvByteBufAllocator maxBytesPerRead(int i);

    int maxBytesPerIndividualRead();

    MaxBytesRecvByteBufAllocator maxBytesPerIndividualRead(int i);

    Map.Entry<Integer, Integer> maxBytesPerReadPair();

    MaxBytesRecvByteBufAllocator maxBytesPerReadPair(int i, int i2);
}
