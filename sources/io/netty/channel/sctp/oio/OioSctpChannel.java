package io.netty.channel.sctp.oio;

import com.sun.nio.sctp.Association;
import com.sun.nio.sctp.MessageInfo;
import com.sun.nio.sctp.NotificationHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.oio.AbstractOioMessageChannel;
import io.netty.channel.sctp.DefaultSctpChannelConfig;
import io.netty.channel.sctp.SctpChannel;
import io.netty.channel.sctp.SctpChannelConfig;
import io.netty.channel.sctp.SctpMessage;
import io.netty.channel.sctp.SctpNotificationHandler;
import io.netty.channel.sctp.SctpServerChannel;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Deprecated
/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/sctp/oio/OioSctpChannel.class */
public class OioSctpChannel extends AbstractOioMessageChannel implements SctpChannel {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) OioSctpChannel.class);
    private static final ChannelMetadata METADATA = new ChannelMetadata(false);
    private static final String EXPECTED_TYPE = " (expected: " + StringUtil.simpleClassName((Class<?>) SctpMessage.class) + ')';

    /* renamed from: ch, reason: collision with root package name */
    private final com.sun.nio.sctp.SctpChannel f6ch;
    private final SctpChannelConfig config;
    private final Selector readSelector;
    private final Selector writeSelector;
    private final Selector connectSelector;
    private final NotificationHandler<?> notificationHandler;

    private static com.sun.nio.sctp.SctpChannel openChannel() {
        try {
            return com.sun.nio.sctp.SctpChannel.open();
        } catch (IOException e) {
            throw new ChannelException("Failed to open a sctp channel.", e);
        }
    }

    public OioSctpChannel() {
        this(openChannel());
    }

    public OioSctpChannel(com.sun.nio.sctp.SctpChannel ch2) {
        this(null, ch2);
    }

    public OioSctpChannel(Channel parent, com.sun.nio.sctp.SctpChannel ch2) {
        super(parent);
        this.f6ch = ch2;
        boolean success = false;
        try {
            try {
                ch2.configureBlocking(false);
                this.readSelector = Selector.open();
                this.writeSelector = Selector.open();
                this.connectSelector = Selector.open();
                ch2.register(this.readSelector, 1);
                ch2.register(this.writeSelector, 4);
                ch2.register(this.connectSelector, 8);
                this.config = new OioSctpChannelConfig(this, ch2);
                this.notificationHandler = new SctpNotificationHandler(this);
                success = true;
                if (1 == 0) {
                    try {
                        ch2.close();
                    } catch (IOException e) {
                        logger.warn("Failed to close a sctp channel.", (Throwable) e);
                    }
                }
            } catch (Throwable th) {
                if (!success) {
                    try {
                        ch2.close();
                    } catch (IOException e2) {
                        logger.warn("Failed to close a sctp channel.", (Throwable) e2);
                    }
                }
                throw th;
            }
        } catch (Exception e3) {
            throw new ChannelException("failed to initialize a sctp channel", e3);
        }
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public InetSocketAddress localAddress() {
        return (InetSocketAddress) super.localAddress();
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress) super.remoteAddress();
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public SctpServerChannel parent() {
        return (SctpServerChannel) super.parent();
    }

    @Override // io.netty.channel.Channel
    public ChannelMetadata metadata() {
        return METADATA;
    }

    @Override // io.netty.channel.Channel
    public SctpChannelConfig config() {
        return this.config;
    }

    @Override // io.netty.channel.Channel
    public boolean isOpen() {
        return this.f6ch.isOpen();
    }

    @Override // io.netty.channel.oio.AbstractOioMessageChannel
    protected int doReadMessages(List<Object> msgs) throws Exception {
        ByteBuffer data;
        MessageInfo messageInfo;
        if (!this.readSelector.isOpen()) {
            return 0;
        }
        int readMessages = 0;
        int selectedKeys = this.readSelector.select(1000L);
        boolean keysSelected = selectedKeys > 0;
        if (!keysSelected) {
            return 0;
        }
        this.readSelector.selectedKeys().clear();
        RecvByteBufAllocator.Handle allocHandle = unsafe().recvBufAllocHandle();
        ByteBuf buffer = allocHandle.allocate(config().getAllocator());
        boolean free = true;
        try {
            try {
                data = buffer.nioBuffer(buffer.writerIndex(), buffer.writableBytes());
                messageInfo = this.f6ch.receive(data, (Object) null, this.notificationHandler);
            } catch (Throwable cause) {
                PlatformDependent.throwException(cause);
                if (free) {
                    buffer.release();
                }
            }
            if (messageInfo == null) {
                if (1 != 0) {
                    buffer.release();
                }
                return 0;
            }
            data.flip();
            allocHandle.lastBytesRead(data.remaining());
            msgs.add(new SctpMessage(messageInfo, buffer.writerIndex(buffer.writerIndex() + allocHandle.lastBytesRead())));
            free = false;
            readMessages = 0 + 1;
            if (0 != 0) {
                buffer.release();
            }
            return readMessages;
        } catch (Throwable th) {
            if (free) {
                buffer.release();
            }
            throw th;
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doWrite(ChannelOutboundBuffer in) throws Exception {
        ByteBuffer nioData;
        if (!this.writeSelector.isOpen()) {
            return;
        }
        int size = in.size();
        int selectedKeys = this.writeSelector.select(1000L);
        if (selectedKeys > 0) {
            Set<SelectionKey> writableKeys = this.writeSelector.selectedKeys();
            if (writableKeys.isEmpty()) {
                return;
            }
            Iterator<SelectionKey> writableKeysIt = writableKeys.iterator();
            int written = 0;
            while (written != size) {
                writableKeysIt.next();
                writableKeysIt.remove();
                SctpMessage packet = (SctpMessage) in.current();
                if (packet == null) {
                    return;
                }
                ByteBuf data = packet.content();
                int dataLen = data.readableBytes();
                if (data.nioBufferCount() != -1) {
                    nioData = data.nioBuffer();
                } else {
                    nioData = ByteBuffer.allocate(dataLen);
                    data.getBytes(data.readerIndex(), nioData);
                    nioData.flip();
                }
                MessageInfo mi = MessageInfo.createOutgoing(association(), (SocketAddress) null, packet.streamIdentifier());
                mi.payloadProtocolID(packet.protocolIdentifier());
                mi.streamNumber(packet.streamIdentifier());
                mi.unordered(packet.isUnordered());
                this.f6ch.send(nioData, mi);
                written++;
                in.remove();
                if (!writableKeysIt.hasNext()) {
                    return;
                }
            }
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected Object filterOutboundMessage(Object msg) throws Exception {
        if (msg instanceof SctpMessage) {
            return msg;
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EXPECTED_TYPE);
    }

    @Override // io.netty.channel.sctp.SctpChannel
    public Association association() {
        try {
            return this.f6ch.association();
        } catch (IOException e) {
            return null;
        }
    }

    @Override // io.netty.channel.Channel
    public boolean isActive() {
        return isOpen() && association() != null;
    }

    @Override // io.netty.channel.AbstractChannel
    protected SocketAddress localAddress0() {
        try {
            Iterator<SocketAddress> i = this.f6ch.getAllLocalAddresses().iterator();
            if (i.hasNext()) {
                return i.next();
            }
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    @Override // io.netty.channel.sctp.SctpChannel
    public Set<InetSocketAddress> allLocalAddresses() {
        try {
            Set<SocketAddress> allLocalAddresses = this.f6ch.getAllLocalAddresses();
            Set<InetSocketAddress> addresses = new LinkedHashSet<>(allLocalAddresses.size());
            for (SocketAddress socketAddress : allLocalAddresses) {
                addresses.add((InetSocketAddress) socketAddress);
            }
            return addresses;
        } catch (Throwable th) {
            return Collections.emptySet();
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected SocketAddress remoteAddress0() {
        try {
            Iterator<SocketAddress> i = this.f6ch.getRemoteAddresses().iterator();
            if (i.hasNext()) {
                return i.next();
            }
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    @Override // io.netty.channel.sctp.SctpChannel
    public Set<InetSocketAddress> allRemoteAddresses() {
        try {
            Set<SocketAddress> allLocalAddresses = this.f6ch.getRemoteAddresses();
            Set<InetSocketAddress> addresses = new LinkedHashSet<>(allLocalAddresses.size());
            for (SocketAddress socketAddress : allLocalAddresses) {
                addresses.add((InetSocketAddress) socketAddress);
            }
            return addresses;
        } catch (Throwable th) {
            return Collections.emptySet();
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doBind(SocketAddress localAddress) throws Exception {
        this.f6ch.bind(localAddress);
    }

    @Override // io.netty.channel.oio.AbstractOioChannel
    protected void doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
        if (localAddress != null) {
            this.f6ch.bind(localAddress);
        }
        boolean success = false;
        try {
            this.f6ch.connect(remoteAddress);
            boolean finishConnect = false;
            while (!finishConnect) {
                if (this.connectSelector.select(1000L) >= 0) {
                    Set<SelectionKey> selectionKeys = this.connectSelector.selectedKeys();
                    Iterator<SelectionKey> it = selectionKeys.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        SelectionKey key = it.next();
                        if (key.isConnectable()) {
                            selectionKeys.clear();
                            finishConnect = true;
                            break;
                        }
                    }
                    selectionKeys.clear();
                }
            }
            success = this.f6ch.finishConnect();
            if (!success) {
                doClose();
            }
        } catch (Throwable th) {
            if (!success) {
                doClose();
            }
            throw th;
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doDisconnect() throws Exception {
        doClose();
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doClose() throws Exception {
        closeSelector("read", this.readSelector);
        closeSelector("write", this.writeSelector);
        closeSelector("connect", this.connectSelector);
        this.f6ch.close();
    }

    private static void closeSelector(String selectorName, Selector selector) throws IOException {
        try {
            selector.close();
        } catch (IOException e) {
            if (logger.isWarnEnabled()) {
                logger.warn("Failed to close a " + selectorName + " selector.", (Throwable) e);
            }
        }
    }

    @Override // io.netty.channel.sctp.SctpChannel
    public ChannelFuture bindAddress(InetAddress localAddress) {
        return bindAddress(localAddress, newPromise());
    }

    @Override // io.netty.channel.sctp.SctpChannel
    public ChannelFuture bindAddress(final InetAddress localAddress, final ChannelPromise promise) {
        if (eventLoop().inEventLoop()) {
            try {
                this.f6ch.bindAddress(localAddress);
                promise.setSuccess();
            } catch (Throwable t) {
                promise.setFailure(t);
            }
        } else {
            eventLoop().execute(new Runnable() { // from class: io.netty.channel.sctp.oio.OioSctpChannel.1
                @Override // java.lang.Runnable
                public void run() {
                    OioSctpChannel.this.bindAddress(localAddress, promise);
                }
            });
        }
        return promise;
    }

    @Override // io.netty.channel.sctp.SctpChannel
    public ChannelFuture unbindAddress(InetAddress localAddress) {
        return unbindAddress(localAddress, newPromise());
    }

    @Override // io.netty.channel.sctp.SctpChannel
    public ChannelFuture unbindAddress(final InetAddress localAddress, final ChannelPromise promise) {
        if (eventLoop().inEventLoop()) {
            try {
                this.f6ch.unbindAddress(localAddress);
                promise.setSuccess();
            } catch (Throwable t) {
                promise.setFailure(t);
            }
        } else {
            eventLoop().execute(new Runnable() { // from class: io.netty.channel.sctp.oio.OioSctpChannel.2
                @Override // java.lang.Runnable
                public void run() {
                    OioSctpChannel.this.unbindAddress(localAddress, promise);
                }
            });
        }
        return promise;
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/sctp/oio/OioSctpChannel$OioSctpChannelConfig.class */
    private final class OioSctpChannelConfig extends DefaultSctpChannelConfig {
        private OioSctpChannelConfig(OioSctpChannel channel, com.sun.nio.sctp.SctpChannel javaChannel) {
            super(channel, javaChannel);
        }

        @Override // io.netty.channel.DefaultChannelConfig
        protected void autoReadCleared() {
            OioSctpChannel.this.clearReadPending();
        }
    }
}
