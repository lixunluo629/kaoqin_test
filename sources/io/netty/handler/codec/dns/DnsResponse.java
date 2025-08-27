package io.netty.handler.codec.dns;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/dns/DnsResponse.class */
public interface DnsResponse extends DnsMessage {
    boolean isAuthoritativeAnswer();

    DnsResponse setAuthoritativeAnswer(boolean z);

    boolean isTruncated();

    DnsResponse setTruncated(boolean z);

    boolean isRecursionAvailable();

    DnsResponse setRecursionAvailable(boolean z);

    DnsResponseCode code();

    DnsResponse setCode(DnsResponseCode dnsResponseCode);

    @Override // io.netty.handler.codec.dns.DnsMessage
    DnsResponse setId(int i);

    @Override // io.netty.handler.codec.dns.DnsMessage
    DnsResponse setOpCode(DnsOpCode dnsOpCode);

    @Override // io.netty.handler.codec.dns.DnsMessage
    DnsResponse setRecursionDesired(boolean z);

    @Override // io.netty.handler.codec.dns.DnsMessage
    DnsResponse setZ(int i);

    @Override // io.netty.handler.codec.dns.DnsMessage
    DnsResponse setRecord(DnsSection dnsSection, DnsRecord dnsRecord);

    @Override // io.netty.handler.codec.dns.DnsMessage
    DnsResponse addRecord(DnsSection dnsSection, DnsRecord dnsRecord);

    @Override // io.netty.handler.codec.dns.DnsMessage
    DnsResponse addRecord(DnsSection dnsSection, int i, DnsRecord dnsRecord);

    @Override // io.netty.handler.codec.dns.DnsMessage
    DnsResponse clear(DnsSection dnsSection);

    @Override // io.netty.handler.codec.dns.DnsMessage
    DnsResponse clear();

    @Override // io.netty.handler.codec.dns.DnsMessage, io.netty.util.ReferenceCounted
    DnsResponse touch();

    @Override // io.netty.handler.codec.dns.DnsMessage, io.netty.util.ReferenceCounted
    DnsResponse touch(Object obj);

    @Override // io.netty.handler.codec.dns.DnsMessage, io.netty.util.ReferenceCounted
    DnsResponse retain();

    @Override // io.netty.handler.codec.dns.DnsMessage, io.netty.util.ReferenceCounted
    DnsResponse retain(int i);
}
