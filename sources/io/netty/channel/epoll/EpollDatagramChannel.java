package io.netty.channel.epoll;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultAddressedEnvelope;
import io.netty.channel.epoll.AbstractEpollChannel;
import io.netty.channel.epoll.NativeDatagramPacketArray;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramChannelConfig;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.channel.unix.DatagramSocketAddress;
import io.netty.channel.unix.Errors;
import io.netty.channel.unix.IovArray;
import io.netty.channel.unix.Socket;
import io.netty.channel.unix.UnixChannelUtil;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.PortUnreachableException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/epoll/EpollDatagramChannel.class */
public final class EpollDatagramChannel extends AbstractEpollChannel implements DatagramChannel {
    private static final ChannelMetadata METADATA;
    private static final String EXPECTED_TYPES;
    private final EpollDatagramChannelConfig config;
    private volatile boolean connected;
    static final /* synthetic */ boolean $assertionsDisabled;

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.Channel
    public /* bridge */ /* synthetic */ boolean isOpen() {
        return super.isOpen();
    }

    static {
        $assertionsDisabled = !EpollDatagramChannel.class.desiredAssertionStatus();
        METADATA = new ChannelMetadata(true);
        EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName((Class<?>) DatagramPacket.class) + ", " + StringUtil.simpleClassName((Class<?>) AddressedEnvelope.class) + '<' + StringUtil.simpleClassName((Class<?>) ByteBuf.class) + ", " + StringUtil.simpleClassName((Class<?>) InetSocketAddress.class) + ">, " + StringUtil.simpleClassName((Class<?>) ByteBuf.class) + ')';
    }

    public EpollDatagramChannel() {
        this((InternetProtocolFamily) null);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public EpollDatagramChannel(InternetProtocolFamily family) {
        LinuxSocket linuxSocketNewSocketDgram;
        if (family == null) {
            linuxSocketNewSocketDgram = LinuxSocket.newSocketDgram(Socket.isIPv6Preferred());
        } else {
            linuxSocketNewSocketDgram = LinuxSocket.newSocketDgram(family == InternetProtocolFamily.IPv6);
        }
        this(linuxSocketNewSocketDgram, false);
    }

    public EpollDatagramChannel(int fd) {
        this(new LinuxSocket(fd), true);
    }

    private EpollDatagramChannel(LinuxSocket fd, boolean active) {
        super((Channel) null, fd, active);
        this.config = new EpollDatagramChannelConfig(this);
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress) super.remoteAddress();
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public InetSocketAddress localAddress() {
        return (InetSocketAddress) super.localAddress();
    }

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.Channel
    public ChannelMetadata metadata() {
        return METADATA;
    }

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.Channel
    public boolean isActive() {
        return this.socket.isOpen() && ((this.config.getActiveOnOpen() && isRegistered()) || this.active);
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public boolean isConnected() {
        return this.connected;
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture joinGroup(InetAddress multicastAddress) {
        return joinGroup(multicastAddress, newPromise());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture joinGroup(InetAddress multicastAddress, ChannelPromise promise) {
        try {
            return joinGroup(multicastAddress, NetworkInterface.getByInetAddress(localAddress().getAddress()), null, promise);
        } catch (IOException e) {
            promise.setFailure((Throwable) e);
            return promise;
        }
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture joinGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface) {
        return joinGroup(multicastAddress, networkInterface, newPromise());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture joinGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface, ChannelPromise promise) {
        return joinGroup(multicastAddress.getAddress(), networkInterface, null, promise);
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture joinGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source) {
        return joinGroup(multicastAddress, networkInterface, source, newPromise());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture joinGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise promise) {
        ObjectUtil.checkNotNull(multicastAddress, "multicastAddress");
        ObjectUtil.checkNotNull(networkInterface, "networkInterface");
        try {
            this.socket.joinGroup(multicastAddress, networkInterface, source);
            promise.setSuccess();
        } catch (IOException e) {
            promise.setFailure((Throwable) e);
        }
        return promise;
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture leaveGroup(InetAddress multicastAddress) {
        return leaveGroup(multicastAddress, newPromise());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture leaveGroup(InetAddress multicastAddress, ChannelPromise promise) {
        try {
            return leaveGroup(multicastAddress, NetworkInterface.getByInetAddress(localAddress().getAddress()), null, promise);
        } catch (IOException e) {
            promise.setFailure((Throwable) e);
            return promise;
        }
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture leaveGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface) {
        return leaveGroup(multicastAddress, networkInterface, newPromise());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture leaveGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface, ChannelPromise promise) {
        return leaveGroup(multicastAddress.getAddress(), networkInterface, null, promise);
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture leaveGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source) {
        return leaveGroup(multicastAddress, networkInterface, source, newPromise());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture leaveGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise promise) {
        ObjectUtil.checkNotNull(multicastAddress, "multicastAddress");
        ObjectUtil.checkNotNull(networkInterface, "networkInterface");
        try {
            this.socket.leaveGroup(multicastAddress, networkInterface, source);
            promise.setSuccess();
        } catch (IOException e) {
            promise.setFailure((Throwable) e);
        }
        return promise;
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture block(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress sourceToBlock) {
        return block(multicastAddress, networkInterface, sourceToBlock, newPromise());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture block(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress sourceToBlock, ChannelPromise promise) {
        ObjectUtil.checkNotNull(multicastAddress, "multicastAddress");
        ObjectUtil.checkNotNull(sourceToBlock, "sourceToBlock");
        ObjectUtil.checkNotNull(networkInterface, "networkInterface");
        promise.setFailure((Throwable) new UnsupportedOperationException("Multicast not supported"));
        return promise;
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture block(InetAddress multicastAddress, InetAddress sourceToBlock) {
        return block(multicastAddress, sourceToBlock, newPromise());
    }

    @Override // io.netty.channel.socket.DatagramChannel
    public ChannelFuture block(InetAddress multicastAddress, InetAddress sourceToBlock, ChannelPromise promise) {
        try {
            return block(multicastAddress, NetworkInterface.getByInetAddress(localAddress().getAddress()), sourceToBlock, promise);
        } catch (Throwable e) {
            promise.setFailure(e);
            return promise;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    public AbstractEpollChannel.AbstractEpollUnsafe newUnsafe() {
        return new EpollDatagramChannelUnsafe();
    }

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    protected void doBind(SocketAddress localAddress) throws Exception {
        if (localAddress instanceof InetSocketAddress) {
            InetSocketAddress socketAddress = (InetSocketAddress) localAddress;
            if (socketAddress.getAddress().isAnyLocalAddress() && (socketAddress.getAddress() instanceof Inet4Address) && Socket.isIPv6Preferred()) {
                localAddress = new InetSocketAddress(LinuxSocket.INET6_ANY, socketAddress.getPort());
            }
        }
        super.doBind(localAddress);
        this.active = true;
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doWrite(ChannelOutboundBuffer in) throws Exception {
        while (true) {
            Object msg = in.current();
            if (msg == null) {
                clearFlag(Native.EPOLLOUT);
                return;
            }
            try {
            } catch (IOException e) {
                in.remove(e);
            }
            if (Native.IS_SUPPORTING_SENDMMSG && in.size() > 1) {
                NativeDatagramPacketArray array = cleanDatagramPacketArray();
                array.add(in, isConnected());
                int cnt = array.count();
                if (cnt >= 1) {
                    int offset = 0;
                    NativeDatagramPacketArray.NativeDatagramPacket[] packets = array.packets();
                    while (cnt > 0) {
                        int send = this.socket.sendmmsg(packets, offset, cnt);
                        if (send == 0) {
                            setFlag(Native.EPOLLOUT);
                            return;
                        }
                        for (int i = 0; i < send; i++) {
                            in.remove();
                        }
                        cnt -= send;
                        offset += send;
                    }
                }
            }
            boolean done = false;
            int i2 = config().getWriteSpinCount();
            while (true) {
                if (i2 <= 0) {
                    break;
                }
                if (doWriteMessage(msg)) {
                    done = true;
                    break;
                }
                i2--;
            }
            if (done) {
                in.remove();
            } else {
                setFlag(Native.EPOLLOUT);
                return;
            }
        }
    }

    private boolean doWriteMessage(Object msg) throws Exception {
        ByteBuf data;
        InetSocketAddress remoteAddress;
        long writtenBytes;
        if (msg instanceof AddressedEnvelope) {
            AddressedEnvelope<ByteBuf, InetSocketAddress> envelope = (AddressedEnvelope) msg;
            data = envelope.content();
            remoteAddress = (InetSocketAddress) envelope.recipient();
        } else {
            data = (ByteBuf) msg;
            remoteAddress = null;
        }
        int dataLen = data.readableBytes();
        if (dataLen == 0) {
            return true;
        }
        if (data.hasMemoryAddress()) {
            long memoryAddress = data.memoryAddress();
            if (remoteAddress == null) {
                writtenBytes = this.socket.writeAddress(memoryAddress, data.readerIndex(), data.writerIndex());
            } else {
                writtenBytes = this.socket.sendToAddress(memoryAddress, data.readerIndex(), data.writerIndex(), remoteAddress.getAddress(), remoteAddress.getPort());
            }
        } else if (data.nioBufferCount() > 1) {
            IovArray array = ((EpollEventLoop) eventLoop()).cleanIovArray();
            array.add(data, data.readerIndex(), data.readableBytes());
            int cnt = array.count();
            if (!$assertionsDisabled && cnt == 0) {
                throw new AssertionError();
            }
            if (remoteAddress == null) {
                writtenBytes = this.socket.writevAddresses(array.memoryAddress(0), cnt);
            } else {
                writtenBytes = this.socket.sendToAddresses(array.memoryAddress(0), cnt, remoteAddress.getAddress(), remoteAddress.getPort());
            }
        } else {
            ByteBuffer nioData = data.internalNioBuffer(data.readerIndex(), data.readableBytes());
            if (remoteAddress == null) {
                writtenBytes = this.socket.write(nioData, nioData.position(), nioData.limit());
            } else {
                writtenBytes = this.socket.sendTo(nioData, nioData.position(), nioData.limit(), remoteAddress.getAddress(), remoteAddress.getPort());
            }
        }
        return writtenBytes > 0;
    }

    @Override // io.netty.channel.AbstractChannel
    protected Object filterOutboundMessage(Object msg) {
        if (msg instanceof DatagramPacket) {
            DatagramPacket packet = (DatagramPacket) msg;
            ByteBuf content = (ByteBuf) packet.content();
            return UnixChannelUtil.isBufferCopyNeededForWrite(content) ? new DatagramPacket(newDirectBuffer(packet, content), packet.recipient()) : msg;
        }
        if (msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf) msg;
            return UnixChannelUtil.isBufferCopyNeededForWrite(buf) ? newDirectBuffer(buf) : buf;
        }
        if (msg instanceof AddressedEnvelope) {
            AddressedEnvelope<Object, SocketAddress> e = (AddressedEnvelope) msg;
            if ((e.content() instanceof ByteBuf) && (e.recipient() == null || (e.recipient() instanceof InetSocketAddress))) {
                ByteBuf content2 = (ByteBuf) e.content();
                return UnixChannelUtil.isBufferCopyNeededForWrite(content2) ? new DefaultAddressedEnvelope(newDirectBuffer(e, content2), (InetSocketAddress) e.recipient()) : e;
            }
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
    }

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.Channel
    public EpollDatagramChannelConfig config() {
        return this.config;
    }

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    protected void doDisconnect() throws Exception {
        this.socket.disconnect();
        this.active = false;
        this.connected = false;
        resetCachedAddresses();
    }

    @Override // io.netty.channel.epoll.AbstractEpollChannel
    protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
        if (super.doConnect(remoteAddress, localAddress)) {
            this.connected = true;
            return true;
        }
        return false;
    }

    @Override // io.netty.channel.epoll.AbstractEpollChannel, io.netty.channel.AbstractChannel
    protected void doClose() throws Exception {
        super.doClose();
        this.connected = false;
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/epoll/EpollDatagramChannel$EpollDatagramChannelUnsafe.class */
    final class EpollDatagramChannelUnsafe extends AbstractEpollChannel.AbstractEpollUnsafe {
        static final /* synthetic */ boolean $assertionsDisabled;

        EpollDatagramChannelUnsafe() {
            super();
        }

        static {
            $assertionsDisabled = !EpollDatagramChannel.class.desiredAssertionStatus();
        }

        @Override // io.netty.channel.epoll.AbstractEpollChannel.AbstractEpollUnsafe
        void epollInReady() {
            int iWritableBytes;
            boolean read;
            if (!$assertionsDisabled && !EpollDatagramChannel.this.eventLoop().inEventLoop()) {
                throw new AssertionError();
            }
            DatagramChannelConfig config = EpollDatagramChannel.this.config();
            if (EpollDatagramChannel.this.shouldBreakEpollInReady(config)) {
                clearEpollIn0();
                return;
            }
            EpollRecvByteAllocatorHandle allocHandle = recvBufAllocHandle();
            allocHandle.edgeTriggered(EpollDatagramChannel.this.isFlagSet(Native.EPOLLET));
            ChannelPipeline pipeline = EpollDatagramChannel.this.pipeline();
            ByteBufAllocator allocator = config.getAllocator();
            allocHandle.reset(config);
            epollInBefore();
            Throwable exception = null;
            try {
                try {
                    boolean connected = EpollDatagramChannel.this.isConnected();
                    do {
                        ByteBuf byteBuf = allocHandle.allocate(allocator);
                        int datagramSize = EpollDatagramChannel.this.config().getMaxDatagramPayloadSize();
                        if (Native.IS_SUPPORTING_RECVMMSG) {
                            iWritableBytes = datagramSize == 0 ? 1 : byteBuf.writableBytes() / datagramSize;
                        } else {
                            iWritableBytes = 0;
                        }
                        int numDatagram = iWritableBytes;
                        if (numDatagram > 1) {
                            read = EpollDatagramChannel.this.scatteringRead(allocHandle, byteBuf, datagramSize, numDatagram);
                        } else if (connected) {
                            try {
                                read = EpollDatagramChannel.this.connectedRead(allocHandle, byteBuf, datagramSize);
                            } catch (Errors.NativeIoException e) {
                                if (connected) {
                                    throw EpollDatagramChannel.this.translateForConnected(e);
                                }
                                throw e;
                            }
                        } else {
                            read = EpollDatagramChannel.this.read(allocHandle, byteBuf, datagramSize);
                        }
                        if (!read) {
                            break;
                        } else {
                            this.readPending = false;
                        }
                    } while (allocHandle.continueReading());
                } catch (Throwable t) {
                    exception = t;
                }
                allocHandle.readComplete();
                pipeline.fireChannelReadComplete();
                if (exception != null) {
                    pipeline.fireExceptionCaught(exception);
                }
            } finally {
                epollInFinally(config);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean connectedRead(EpollRecvByteAllocatorHandle allocHandle, ByteBuf byteBuf, int maxDatagramPacketSize) throws Exception {
        int localReadAmount;
        try {
            int writable = maxDatagramPacketSize != 0 ? Math.min(byteBuf.writableBytes(), maxDatagramPacketSize) : byteBuf.writableBytes();
            allocHandle.attemptedBytesRead(writable);
            int writerIndex = byteBuf.writerIndex();
            if (byteBuf.hasMemoryAddress()) {
                localReadAmount = this.socket.readAddress(byteBuf.memoryAddress(), writerIndex, writerIndex + writable);
            } else {
                ByteBuffer buf = byteBuf.internalNioBuffer(writerIndex, writable);
                localReadAmount = this.socket.read(buf, buf.position(), buf.limit());
            }
            if (localReadAmount <= 0) {
                allocHandle.lastBytesRead(localReadAmount);
                if (byteBuf != null) {
                    byteBuf.release();
                }
                return false;
            }
            byteBuf.writerIndex(writerIndex + localReadAmount);
            allocHandle.lastBytesRead(maxDatagramPacketSize <= 0 ? localReadAmount : writable);
            DatagramPacket packet = new DatagramPacket(byteBuf, localAddress(), remoteAddress());
            allocHandle.incMessagesRead(1);
            pipeline().fireChannelRead((Object) packet);
            ByteBuf byteBuf2 = null;
            if (0 != 0) {
                byteBuf2.release();
            }
            return true;
        } catch (Throwable th) {
            if (byteBuf != null) {
                byteBuf.release();
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public IOException translateForConnected(Errors.NativeIoException e) {
        if (e.expectedErr() == Errors.ERROR_ECONNREFUSED_NEGATIVE) {
            PortUnreachableException error = new PortUnreachableException(e.getMessage());
            error.initCause(e);
            return error;
        }
        return e;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean scatteringRead(EpollRecvByteAllocatorHandle allocHandle, ByteBuf byteBuf, int datagramSize, int numDatagram) throws IOException {
        RecyclableArrayList bufferPackets = null;
        try {
            int offset = byteBuf.writerIndex();
            NativeDatagramPacketArray array = cleanDatagramPacketArray();
            int i = 0;
            while (i < numDatagram && array.addWritable(byteBuf, offset, datagramSize)) {
                i++;
                offset += datagramSize;
            }
            allocHandle.attemptedBytesRead(offset - byteBuf.writerIndex());
            NativeDatagramPacketArray.NativeDatagramPacket[] packets = array.packets();
            int received = this.socket.recvmmsg(packets, 0, array.count());
            if (received == 0) {
                allocHandle.lastBytesRead(-1);
                if (byteBuf != null) {
                    byteBuf.release();
                }
                if (0 != 0) {
                    for (int i2 = 0; i2 < bufferPackets.size(); i2++) {
                        ReferenceCountUtil.release(bufferPackets.get(i2));
                    }
                    bufferPackets.recycle();
                }
                return false;
            }
            int bytesReceived = received * datagramSize;
            byteBuf.writerIndex(bytesReceived);
            InetSocketAddress local = localAddress();
            if (received == 1) {
                DatagramPacket packet = packets[0].newDatagramPacket(byteBuf, local);
                allocHandle.lastBytesRead(datagramSize);
                allocHandle.incMessagesRead(1);
                pipeline().fireChannelRead((Object) packet);
                ByteBuf byteBuf2 = null;
                if (0 != 0) {
                    byteBuf2.release();
                }
                if (0 != 0) {
                    for (int i3 = 0; i3 < bufferPackets.size(); i3++) {
                        ReferenceCountUtil.release(bufferPackets.get(i3));
                    }
                    bufferPackets.recycle();
                }
                return true;
            }
            RecyclableArrayList bufferPackets2 = RecyclableArrayList.newInstance();
            for (int i4 = 0; i4 < received; i4++) {
                DatagramPacket packet2 = packets[i4].newDatagramPacket(byteBuf.readRetainedSlice(datagramSize), local);
                bufferPackets2.add(packet2);
            }
            allocHandle.lastBytesRead(bytesReceived);
            allocHandle.incMessagesRead(received);
            for (int i5 = 0; i5 < received; i5++) {
                pipeline().fireChannelRead(bufferPackets2.set(i5, Unpooled.EMPTY_BUFFER));
            }
            bufferPackets2.recycle();
            RecyclableArrayList bufferPackets3 = null;
            if (byteBuf != null) {
                byteBuf.release();
            }
            if (0 != 0) {
                for (int i6 = 0; i6 < bufferPackets3.size(); i6++) {
                    ReferenceCountUtil.release(bufferPackets3.get(i6));
                }
                bufferPackets3.recycle();
            }
            return true;
        } catch (Throwable th) {
            if (byteBuf != null) {
                byteBuf.release();
            }
            if (0 != 0) {
                for (int i7 = 0; i7 < bufferPackets.size(); i7++) {
                    ReferenceCountUtil.release(bufferPackets.get(i7));
                }
                bufferPackets.recycle();
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean read(EpollRecvByteAllocatorHandle allocHandle, ByteBuf byteBuf, int maxDatagramPacketSize) throws IOException {
        DatagramSocketAddress remoteAddress;
        try {
            int writable = maxDatagramPacketSize != 0 ? Math.min(byteBuf.writableBytes(), maxDatagramPacketSize) : byteBuf.writableBytes();
            allocHandle.attemptedBytesRead(writable);
            int writerIndex = byteBuf.writerIndex();
            if (byteBuf.hasMemoryAddress()) {
                remoteAddress = this.socket.recvFromAddress(byteBuf.memoryAddress(), writerIndex, writerIndex + writable);
            } else {
                ByteBuffer nioData = byteBuf.internalNioBuffer(writerIndex, writable);
                remoteAddress = this.socket.recvFrom(nioData, nioData.position(), nioData.limit());
            }
            if (remoteAddress == null) {
                allocHandle.lastBytesRead(-1);
                if (byteBuf != null) {
                    byteBuf.release();
                }
                return false;
            }
            InetSocketAddress localAddress = remoteAddress.localAddress();
            if (localAddress == null) {
                localAddress = localAddress();
            }
            int received = remoteAddress.receivedAmount();
            allocHandle.lastBytesRead(maxDatagramPacketSize <= 0 ? received : writable);
            byteBuf.writerIndex(writerIndex + received);
            allocHandle.incMessagesRead(1);
            pipeline().fireChannelRead((Object) new DatagramPacket(byteBuf, localAddress, remoteAddress));
            ByteBuf byteBuf2 = null;
            if (0 != 0) {
                byteBuf2.release();
            }
            return true;
        } catch (Throwable th) {
            if (byteBuf != null) {
                byteBuf.release();
            }
            throw th;
        }
    }

    private NativeDatagramPacketArray cleanDatagramPacketArray() {
        return ((EpollEventLoop) eventLoop()).cleanDatagramPacketArray();
    }
}
