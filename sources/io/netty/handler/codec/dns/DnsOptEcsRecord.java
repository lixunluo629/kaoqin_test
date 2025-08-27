package io.netty.handler.codec.dns;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/dns/DnsOptEcsRecord.class */
public interface DnsOptEcsRecord extends DnsOptPseudoRecord {
    int sourcePrefixLength();

    int scopePrefixLength();

    byte[] address();
}
