package io.netty.channel.epoll;

import io.netty.channel.RecvByteBufAllocator;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/epoll/EpollRecvByteAllocatorStreamingHandle.class */
final class EpollRecvByteAllocatorStreamingHandle extends EpollRecvByteAllocatorHandle {
    EpollRecvByteAllocatorStreamingHandle(RecvByteBufAllocator.ExtendedHandle handle) {
        super(handle);
    }

    @Override // io.netty.channel.epoll.EpollRecvByteAllocatorHandle
    boolean maybeMoreDataToRead() {
        return lastBytesRead() == attemptedBytesRead() || isReceivedRdHup();
    }
}
