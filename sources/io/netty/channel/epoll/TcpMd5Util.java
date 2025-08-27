package io.netty.channel.epoll;

import io.netty.util.internal.ObjectUtil;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/epoll/TcpMd5Util.class */
final class TcpMd5Util {
    static Collection<InetAddress> newTcpMd5Sigs(AbstractEpollChannel channel, Collection<InetAddress> current, Map<InetAddress, byte[]> newKeys) throws IOException {
        ObjectUtil.checkNotNull(channel, "channel");
        ObjectUtil.checkNotNull(current, "current");
        ObjectUtil.checkNotNull(newKeys, "newKeys");
        for (Map.Entry<InetAddress, byte[]> e : newKeys.entrySet()) {
            byte[] key = e.getValue();
            if (e.getKey() == null) {
                throw new IllegalArgumentException("newKeys contains an entry with null address: " + newKeys);
            }
            ObjectUtil.checkNotNull(key, "newKeys[" + e.getKey() + ']');
            if (key.length == 0) {
                throw new IllegalArgumentException("newKeys[" + e.getKey() + "] has an empty key.");
            }
            if (key.length > Native.TCP_MD5SIG_MAXKEYLEN) {
                throw new IllegalArgumentException("newKeys[" + e.getKey() + "] has a key with invalid length; should not exceed the maximum length (" + Native.TCP_MD5SIG_MAXKEYLEN + ')');
            }
        }
        for (InetAddress addr : current) {
            if (!newKeys.containsKey(addr)) {
                channel.socket.setTcpMd5Sig(addr, null);
            }
        }
        if (newKeys.isEmpty()) {
            return Collections.emptySet();
        }
        Collection<InetAddress> addresses = new ArrayList<>(newKeys.size());
        for (Map.Entry<InetAddress, byte[]> e2 : newKeys.entrySet()) {
            channel.socket.setTcpMd5Sig(e2.getKey(), e2.getValue());
            addresses.add(e2.getKey());
        }
        return addresses;
    }

    private TcpMd5Util() {
    }
}
