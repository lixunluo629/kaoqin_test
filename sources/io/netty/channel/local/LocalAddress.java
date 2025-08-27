package io.netty.channel.local;

import io.netty.channel.Channel;
import io.netty.util.internal.ObjectUtil;
import java.net.SocketAddress;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/local/LocalAddress.class */
public final class LocalAddress extends SocketAddress implements Comparable<LocalAddress> {
    private static final long serialVersionUID = 4644331421130916435L;
    public static final LocalAddress ANY = new LocalAddress("ANY");
    private final String id;
    private final String strVal;

    LocalAddress(Channel channel) {
        StringBuilder buf = new StringBuilder(16);
        buf.append("local:E");
        buf.append(Long.toHexString((channel.hashCode() & 4294967295L) | 4294967296L));
        buf.setCharAt(7, ':');
        this.id = buf.substring(6);
        this.strVal = buf.toString();
    }

    public LocalAddress(String id) {
        ObjectUtil.checkNotNull(id, "id");
        String id2 = id.trim().toLowerCase();
        if (id2.isEmpty()) {
            throw new IllegalArgumentException("empty id");
        }
        this.id = id2;
        this.strVal = "local:" + id2;
    }

    public String id() {
        return this.id;
    }

    public int hashCode() {
        return this.id.hashCode();
    }

    public boolean equals(Object o) {
        if (!(o instanceof LocalAddress)) {
            return false;
        }
        return this.id.equals(((LocalAddress) o).id);
    }

    @Override // java.lang.Comparable
    public int compareTo(LocalAddress o) {
        return this.id.compareTo(o.id);
    }

    public String toString() {
        return this.strVal;
    }
}
