package io.netty.channel.epoll;

import io.netty.channel.ChannelException;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.epoll.NativeDatagramPacketArray;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.channel.unix.Errors;
import io.netty.channel.unix.NativeInetAddress;
import io.netty.channel.unix.PeerCredentials;
import io.netty.channel.unix.Socket;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SocketUtils;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import org.hyperic.sigar.NetFlags;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/epoll/LinuxSocket.class */
final class LinuxSocket extends Socket {
    static final InetAddress INET6_ANY = unsafeInetAddrByName(NetFlags.ANY_ADDR_V6);
    private static final InetAddress INET_ANY = unsafeInetAddrByName("0.0.0.0");
    private static final long MAX_UINT32_T = 4294967295L;

    private static native void joinGroup(int i, boolean z, byte[] bArr, byte[] bArr2, int i2, int i3) throws IOException;

    private static native void joinSsmGroup(int i, boolean z, byte[] bArr, byte[] bArr2, int i2, int i3, byte[] bArr3) throws IOException;

    private static native void leaveGroup(int i, boolean z, byte[] bArr, byte[] bArr2, int i2, int i3) throws IOException;

    private static native void leaveSsmGroup(int i, boolean z, byte[] bArr, byte[] bArr2, int i2, int i3, byte[] bArr3) throws IOException;

    private static native long sendFile(int i, DefaultFileRegion defaultFileRegion, long j, long j2, long j3) throws IOException;

    private static native int getTcpDeferAccept(int i) throws IOException;

    private static native int isTcpQuickAck(int i) throws IOException;

    private static native int isTcpCork(int i) throws IOException;

    private static native int getSoBusyPoll(int i) throws IOException;

    private static native int getTcpNotSentLowAt(int i) throws IOException;

    private static native int getTcpKeepIdle(int i) throws IOException;

    private static native int getTcpKeepIntvl(int i) throws IOException;

    private static native int getTcpKeepCnt(int i) throws IOException;

    private static native int getTcpUserTimeout(int i) throws IOException;

    private static native int getTimeToLive(int i) throws IOException;

    private static native int isIpFreeBind(int i) throws IOException;

    private static native int isIpTransparent(int i) throws IOException;

    private static native int isIpRecvOrigDestAddr(int i) throws IOException;

    private static native void getTcpInfo(int i, long[] jArr) throws IOException;

    private static native PeerCredentials getPeerCredentials(int i) throws IOException;

    private static native int isTcpFastOpenConnect(int i) throws IOException;

    private static native void setTcpDeferAccept(int i, int i2) throws IOException;

    private static native void setTcpQuickAck(int i, int i2) throws IOException;

    private static native void setTcpCork(int i, int i2) throws IOException;

    private static native void setSoBusyPoll(int i, int i2) throws IOException;

    private static native void setTcpNotSentLowAt(int i, int i2) throws IOException;

    private static native void setTcpFastOpen(int i, int i2) throws IOException;

    private static native void setTcpFastOpenConnect(int i, int i2) throws IOException;

    private static native void setTcpKeepIdle(int i, int i2) throws IOException;

    private static native void setTcpKeepIntvl(int i, int i2) throws IOException;

    private static native void setTcpKeepCnt(int i, int i2) throws IOException;

    private static native void setTcpUserTimeout(int i, int i2) throws IOException;

    private static native void setIpFreeBind(int i, int i2) throws IOException;

    private static native void setIpTransparent(int i, int i2) throws IOException;

    private static native void setIpRecvOrigDestAddr(int i, int i2) throws IOException;

    private static native void setTcpMd5Sig(int i, boolean z, byte[] bArr, int i2, byte[] bArr2) throws IOException;

    private static native void setInterface(int i, boolean z, byte[] bArr, int i2, int i3) throws IOException;

    private static native int getInterface(int i, boolean z);

    private static native int getIpMulticastLoop(int i, boolean z) throws IOException;

    private static native void setIpMulticastLoop(int i, boolean z, int i2) throws IOException;

    private static native void setTimeToLive(int i, int i2) throws IOException;

    LinuxSocket(int fd) {
        super(fd);
    }

    private InternetProtocolFamily family() {
        return this.ipv6 ? InternetProtocolFamily.IPv6 : InternetProtocolFamily.IPv4;
    }

    int sendmmsg(NativeDatagramPacketArray.NativeDatagramPacket[] msgs, int offset, int len) throws IOException {
        return Native.sendmmsg(intValue(), this.ipv6, msgs, offset, len);
    }

    int recvmmsg(NativeDatagramPacketArray.NativeDatagramPacket[] msgs, int offset, int len) throws IOException {
        return Native.recvmmsg(intValue(), this.ipv6, msgs, offset, len);
    }

    void setTimeToLive(int ttl) throws IOException {
        setTimeToLive(intValue(), ttl);
    }

    void setInterface(InetAddress address) throws IOException {
        NativeInetAddress a = NativeInetAddress.newInstance(address);
        setInterface(intValue(), this.ipv6, a.address(), a.scopeId(), interfaceIndex(address));
    }

    void setNetworkInterface(NetworkInterface netInterface) throws IOException {
        InetAddress address = deriveInetAddress(netInterface, family() == InternetProtocolFamily.IPv6);
        if (address.equals(family() == InternetProtocolFamily.IPv4 ? INET_ANY : INET6_ANY)) {
            throw new IOException("NetworkInterface does not support " + family());
        }
        NativeInetAddress nativeAddress = NativeInetAddress.newInstance(address);
        setInterface(intValue(), this.ipv6, nativeAddress.address(), nativeAddress.scopeId(), interfaceIndex(netInterface));
    }

    InetAddress getInterface() throws IOException {
        NetworkInterface inf = getNetworkInterface();
        if (inf != null) {
            Enumeration<InetAddress> addresses = SocketUtils.addressesFromNetworkInterface(inf);
            if (addresses.hasMoreElements()) {
                return addresses.nextElement();
            }
            return null;
        }
        return null;
    }

    NetworkInterface getNetworkInterface() throws IOException {
        int ret = getInterface(intValue(), this.ipv6);
        if (this.ipv6) {
            if (PlatformDependent.javaVersion() >= 7) {
                return NetworkInterface.getByIndex(ret);
            }
            return null;
        }
        InetAddress address = inetAddress(ret);
        if (address != null) {
            return NetworkInterface.getByInetAddress(address);
        }
        return null;
    }

    private static InetAddress inetAddress(int value) {
        byte[] var1 = {(byte) ((value >>> 24) & 255), (byte) ((value >>> 16) & 255), (byte) ((value >>> 8) & 255), (byte) (value & 255)};
        try {
            return InetAddress.getByAddress(var1);
        } catch (UnknownHostException e) {
            return null;
        }
    }

    void joinGroup(InetAddress group, NetworkInterface netInterface, InetAddress source) throws IOException {
        NativeInetAddress g = NativeInetAddress.newInstance(group);
        boolean isIpv6 = group instanceof Inet6Address;
        NativeInetAddress i = NativeInetAddress.newInstance(deriveInetAddress(netInterface, isIpv6));
        if (source != null) {
            NativeInetAddress s = NativeInetAddress.newInstance(source);
            joinSsmGroup(intValue(), this.ipv6, g.address(), i.address(), g.scopeId(), interfaceIndex(netInterface), s.address());
        } else {
            joinGroup(intValue(), this.ipv6, g.address(), i.address(), g.scopeId(), interfaceIndex(netInterface));
        }
    }

    void leaveGroup(InetAddress group, NetworkInterface netInterface, InetAddress source) throws IOException {
        NativeInetAddress g = NativeInetAddress.newInstance(group);
        boolean isIpv6 = group instanceof Inet6Address;
        NativeInetAddress i = NativeInetAddress.newInstance(deriveInetAddress(netInterface, isIpv6));
        if (source != null) {
            NativeInetAddress s = NativeInetAddress.newInstance(source);
            leaveSsmGroup(intValue(), this.ipv6, g.address(), i.address(), g.scopeId(), interfaceIndex(netInterface), s.address());
        } else {
            leaveGroup(intValue(), this.ipv6, g.address(), i.address(), g.scopeId(), interfaceIndex(netInterface));
        }
    }

    private static int interfaceIndex(NetworkInterface networkInterface) {
        if (PlatformDependent.javaVersion() >= 7) {
            return networkInterface.getIndex();
        }
        return -1;
    }

    private static int interfaceIndex(InetAddress address) throws IOException {
        NetworkInterface iface;
        if (PlatformDependent.javaVersion() >= 7 && (iface = NetworkInterface.getByInetAddress(address)) != null) {
            return iface.getIndex();
        }
        return -1;
    }

    void setTcpDeferAccept(int deferAccept) throws IOException {
        setTcpDeferAccept(intValue(), deferAccept);
    }

    void setTcpQuickAck(boolean quickAck) throws IOException {
        setTcpQuickAck(intValue(), quickAck ? 1 : 0);
    }

    void setTcpCork(boolean tcpCork) throws IOException {
        setTcpCork(intValue(), tcpCork ? 1 : 0);
    }

    void setSoBusyPoll(int loopMicros) throws IOException {
        setSoBusyPoll(intValue(), loopMicros);
    }

    void setTcpNotSentLowAt(long tcpNotSentLowAt) throws IOException {
        if (tcpNotSentLowAt < 0 || tcpNotSentLowAt > 4294967295L) {
            throw new IllegalArgumentException("tcpNotSentLowAt must be a uint32_t");
        }
        setTcpNotSentLowAt(intValue(), (int) tcpNotSentLowAt);
    }

    void setTcpFastOpen(int tcpFastopenBacklog) throws IOException {
        setTcpFastOpen(intValue(), tcpFastopenBacklog);
    }

    void setTcpFastOpenConnect(boolean tcpFastOpenConnect) throws IOException {
        setTcpFastOpenConnect(intValue(), tcpFastOpenConnect ? 1 : 0);
    }

    boolean isTcpFastOpenConnect() throws IOException {
        return isTcpFastOpenConnect(intValue()) != 0;
    }

    void setTcpKeepIdle(int seconds) throws IOException {
        setTcpKeepIdle(intValue(), seconds);
    }

    void setTcpKeepIntvl(int seconds) throws IOException {
        setTcpKeepIntvl(intValue(), seconds);
    }

    void setTcpKeepCnt(int probes) throws IOException {
        setTcpKeepCnt(intValue(), probes);
    }

    void setTcpUserTimeout(int milliseconds) throws IOException {
        setTcpUserTimeout(intValue(), milliseconds);
    }

    void setIpFreeBind(boolean enabled) throws IOException {
        setIpFreeBind(intValue(), enabled ? 1 : 0);
    }

    void setIpTransparent(boolean enabled) throws IOException {
        setIpTransparent(intValue(), enabled ? 1 : 0);
    }

    void setIpRecvOrigDestAddr(boolean enabled) throws IOException {
        setIpRecvOrigDestAddr(intValue(), enabled ? 1 : 0);
    }

    int getTimeToLive() throws IOException {
        return getTimeToLive(intValue());
    }

    void getTcpInfo(EpollTcpInfo info) throws IOException {
        getTcpInfo(intValue(), info.info);
    }

    void setTcpMd5Sig(InetAddress address, byte[] key) throws IOException {
        NativeInetAddress a = NativeInetAddress.newInstance(address);
        setTcpMd5Sig(intValue(), this.ipv6, a.address(), a.scopeId(), key);
    }

    boolean isTcpCork() throws IOException {
        return isTcpCork(intValue()) != 0;
    }

    int getSoBusyPoll() throws IOException {
        return getSoBusyPoll(intValue());
    }

    int getTcpDeferAccept() throws IOException {
        return getTcpDeferAccept(intValue());
    }

    boolean isTcpQuickAck() throws IOException {
        return isTcpQuickAck(intValue()) != 0;
    }

    long getTcpNotSentLowAt() throws IOException {
        return getTcpNotSentLowAt(intValue()) & 4294967295L;
    }

    int getTcpKeepIdle() throws IOException {
        return getTcpKeepIdle(intValue());
    }

    int getTcpKeepIntvl() throws IOException {
        return getTcpKeepIntvl(intValue());
    }

    int getTcpKeepCnt() throws IOException {
        return getTcpKeepCnt(intValue());
    }

    int getTcpUserTimeout() throws IOException {
        return getTcpUserTimeout(intValue());
    }

    boolean isIpFreeBind() throws IOException {
        return isIpFreeBind(intValue()) != 0;
    }

    boolean isIpTransparent() throws IOException {
        return isIpTransparent(intValue()) != 0;
    }

    boolean isIpRecvOrigDestAddr() throws IOException {
        return isIpRecvOrigDestAddr(intValue()) != 0;
    }

    PeerCredentials getPeerCredentials() throws IOException {
        return getPeerCredentials(intValue());
    }

    boolean isLoopbackModeDisabled() throws IOException {
        return getIpMulticastLoop(intValue(), this.ipv6) == 0;
    }

    void setLoopbackModeDisabled(boolean loopbackModeDisabled) throws IOException {
        setIpMulticastLoop(intValue(), this.ipv6, loopbackModeDisabled ? 0 : 1);
    }

    long sendFile(DefaultFileRegion src, long baseOffset, long offset, long length) throws IOException {
        src.open();
        long res = sendFile(intValue(), src, baseOffset, offset, length);
        if (res >= 0) {
            return res;
        }
        return Errors.ioResult("sendfile", (int) res);
    }

    private static InetAddress deriveInetAddress(NetworkInterface netInterface, boolean ipv6) {
        InetAddress ipAny = ipv6 ? INET6_ANY : INET_ANY;
        if (netInterface != null) {
            Enumeration<InetAddress> ias = netInterface.getInetAddresses();
            while (ias.hasMoreElements()) {
                InetAddress ia = ias.nextElement();
                boolean isV6 = ia instanceof Inet6Address;
                if (isV6 == ipv6) {
                    return ia;
                }
            }
        }
        return ipAny;
    }

    public static LinuxSocket newSocketStream(boolean ipv6) {
        return new LinuxSocket(newSocketStream0(ipv6));
    }

    public static LinuxSocket newSocketStream() {
        return newSocketStream(isIPv6Preferred());
    }

    public static LinuxSocket newSocketDgram(boolean ipv6) {
        return new LinuxSocket(newSocketDgram0(ipv6));
    }

    public static LinuxSocket newSocketDgram() {
        return newSocketDgram(isIPv6Preferred());
    }

    public static LinuxSocket newSocketDomain() {
        return new LinuxSocket(newSocketDomain0());
    }

    private static InetAddress unsafeInetAddrByName(String inetName) {
        try {
            return InetAddress.getByName(inetName);
        } catch (UnknownHostException uhe) {
            throw new ChannelException(uhe);
        }
    }
}
