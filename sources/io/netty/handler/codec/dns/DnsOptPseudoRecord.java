package io.netty.handler.codec.dns;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/dns/DnsOptPseudoRecord.class */
public interface DnsOptPseudoRecord extends DnsRecord {
    int extendedRcode();

    int version();

    int flags();
}
