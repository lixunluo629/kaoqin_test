package io.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.UncheckedBooleanSupplier;
import io.netty.util.internal.ObjectUtil;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/RecvByteBufAllocator.class */
public interface RecvByteBufAllocator {

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/RecvByteBufAllocator$ExtendedHandle.class */
    public interface ExtendedHandle extends Handle {
        boolean continueReading(UncheckedBooleanSupplier uncheckedBooleanSupplier);
    }

    @Deprecated
    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/RecvByteBufAllocator$Handle.class */
    public interface Handle {
        ByteBuf allocate(ByteBufAllocator byteBufAllocator);

        int guess();

        void reset(ChannelConfig channelConfig);

        void incMessagesRead(int i);

        void lastBytesRead(int i);

        int lastBytesRead();

        void attemptedBytesRead(int i);

        int attemptedBytesRead();

        boolean continueReading();

        void readComplete();
    }

    Handle newHandle();

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/RecvByteBufAllocator$DelegatingHandle.class */
    public static class DelegatingHandle implements Handle {
        private final Handle delegate;

        public DelegatingHandle(Handle delegate) {
            this.delegate = (Handle) ObjectUtil.checkNotNull(delegate, "delegate");
        }

        protected final Handle delegate() {
            return this.delegate;
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public ByteBuf allocate(ByteBufAllocator alloc) {
            return this.delegate.allocate(alloc);
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public int guess() {
            return this.delegate.guess();
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public void reset(ChannelConfig config) {
            this.delegate.reset(config);
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public void incMessagesRead(int numMessages) {
            this.delegate.incMessagesRead(numMessages);
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public void lastBytesRead(int bytes) {
            this.delegate.lastBytesRead(bytes);
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public int lastBytesRead() {
            return this.delegate.lastBytesRead();
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public boolean continueReading() {
            return this.delegate.continueReading();
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public int attemptedBytesRead() {
            return this.delegate.attemptedBytesRead();
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public void attemptedBytesRead(int bytes) {
            this.delegate.attemptedBytesRead(bytes);
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public void readComplete() {
            this.delegate.readComplete();
        }
    }
}
