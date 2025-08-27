package io.netty.handler.codec.dns;

import io.netty.util.ReferenceCounted;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/dns/DnsMessage.class */
public interface DnsMessage extends ReferenceCounted {
    int id();

    DnsMessage setId(int i);

    DnsOpCode opCode();

    DnsMessage setOpCode(DnsOpCode dnsOpCode);

    boolean isRecursionDesired();

    DnsMessage setRecursionDesired(boolean z);

    int z();

    DnsMessage setZ(int i);

    int count(DnsSection dnsSection);

    int count();

    <T extends DnsRecord> T recordAt(DnsSection dnsSection);

    <T extends DnsRecord> T recordAt(DnsSection dnsSection, int i);

    DnsMessage setRecord(DnsSection dnsSection, DnsRecord dnsRecord);

    <T extends DnsRecord> T setRecord(DnsSection dnsSection, int i, DnsRecord dnsRecord);

    DnsMessage addRecord(DnsSection dnsSection, DnsRecord dnsRecord);

    DnsMessage addRecord(DnsSection dnsSection, int i, DnsRecord dnsRecord);

    <T extends DnsRecord> T removeRecord(DnsSection dnsSection, int i);

    DnsMessage clear(DnsSection dnsSection);

    DnsMessage clear();

    @Override // io.netty.util.ReferenceCounted
    DnsMessage touch();

    @Override // io.netty.util.ReferenceCounted
    DnsMessage touch(Object obj);

    @Override // io.netty.util.ReferenceCounted
    DnsMessage retain();

    @Override // io.netty.util.ReferenceCounted
    DnsMessage retain(int i);
}
