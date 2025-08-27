package io.netty.channel.kqueue;

import io.netty.channel.DefaultFileRegion;
import io.netty.channel.unix.Errors;
import io.netty.channel.unix.PeerCredentials;
import io.netty.channel.unix.Socket;
import java.io.IOException;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/kqueue/BsdSocket.class */
final class BsdSocket extends Socket {
    private static final int APPLE_SND_LOW_AT_MAX = 131072;
    private static final int FREEBSD_SND_LOW_AT_MAX = 32768;
    static final int BSD_SND_LOW_AT_MAX = Math.min(131072, 32768);

    private static native long sendFile(int i, DefaultFileRegion defaultFileRegion, long j, long j2, long j3) throws IOException;

    private static native String[] getAcceptFilter(int i) throws IOException;

    private static native int getTcpNoPush(int i) throws IOException;

    private static native int getSndLowAt(int i) throws IOException;

    private static native PeerCredentials getPeerCredentials(int i) throws IOException;

    private static native void setAcceptFilter(int i, String str, String str2) throws IOException;

    private static native void setTcpNoPush(int i, int i2) throws IOException;

    private static native void setSndLowAt(int i, int i2) throws IOException;

    BsdSocket(int fd) {
        super(fd);
    }

    void setAcceptFilter(AcceptFilter acceptFilter) throws IOException {
        setAcceptFilter(intValue(), acceptFilter.filterName(), acceptFilter.filterArgs());
    }

    void setTcpNoPush(boolean tcpNoPush) throws IOException {
        setTcpNoPush(intValue(), tcpNoPush ? 1 : 0);
    }

    void setSndLowAt(int lowAt) throws IOException {
        setSndLowAt(intValue(), lowAt);
    }

    boolean isTcpNoPush() throws IOException {
        return getTcpNoPush(intValue()) != 0;
    }

    int getSndLowAt() throws IOException {
        return getSndLowAt(intValue());
    }

    AcceptFilter getAcceptFilter() throws IOException {
        String[] result = getAcceptFilter(intValue());
        return result == null ? AcceptFilter.PLATFORM_UNSUPPORTED : new AcceptFilter(result[0], result[1]);
    }

    PeerCredentials getPeerCredentials() throws IOException {
        return getPeerCredentials(intValue());
    }

    long sendFile(DefaultFileRegion src, long baseOffset, long offset, long length) throws IOException {
        src.open();
        long res = sendFile(intValue(), src, baseOffset, offset, length);
        if (res >= 0) {
            return res;
        }
        return Errors.ioResult("sendfile", (int) res);
    }

    public static BsdSocket newSocketStream() {
        return new BsdSocket(newSocketStream0());
    }

    public static BsdSocket newSocketDgram() {
        return new BsdSocket(newSocketDgram0());
    }

    public static BsdSocket newSocketDomain() {
        return new BsdSocket(newSocketDomain0());
    }
}
