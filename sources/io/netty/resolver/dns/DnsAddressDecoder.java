package io.netty.resolver.dns;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.handler.codec.dns.DnsRawRecord;
import io.netty.handler.codec.dns.DnsRecord;
import java.net.IDN;
import java.net.InetAddress;
import java.net.UnknownHostException;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/DnsAddressDecoder.class */
final class DnsAddressDecoder {
    private static final int INADDRSZ4 = 4;
    private static final int INADDRSZ6 = 16;

    static InetAddress decodeAddress(DnsRecord record, String name, boolean decodeIdn) {
        String unicode;
        if (!(record instanceof DnsRawRecord)) {
            return null;
        }
        ByteBuf content = ((ByteBufHolder) record).content();
        int contentLen = content.readableBytes();
        if (contentLen != 4 && contentLen != 16) {
            return null;
        }
        byte[] addrBytes = new byte[contentLen];
        content.getBytes(content.readerIndex(), addrBytes);
        if (decodeIdn) {
            try {
                unicode = IDN.toUnicode(name);
            } catch (UnknownHostException e) {
                throw new Error(e);
            }
        } else {
            unicode = name;
        }
        return InetAddress.getByAddress(unicode, addrBytes);
    }

    private DnsAddressDecoder() {
    }
}
