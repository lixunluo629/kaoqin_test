package io.netty.handler.codec.dns;

import io.netty.buffer.ByteBuf;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/dns/DefaultDnsRecordDecoder.class */
public class DefaultDnsRecordDecoder implements DnsRecordDecoder {
    static final String ROOT = ".";

    protected DefaultDnsRecordDecoder() {
    }

    @Override // io.netty.handler.codec.dns.DnsRecordDecoder
    public final DnsQuestion decodeQuestion(ByteBuf in) throws Exception {
        String name = decodeName(in);
        DnsRecordType type = DnsRecordType.valueOf(in.readUnsignedShort());
        int qClass = in.readUnsignedShort();
        return new DefaultDnsQuestion(name, type, qClass);
    }

    @Override // io.netty.handler.codec.dns.DnsRecordDecoder
    public final <T extends DnsRecord> T decodeRecord(ByteBuf byteBuf) throws Exception {
        int i = byteBuf.readerIndex();
        String strDecodeName = decodeName(byteBuf);
        int iWriterIndex = byteBuf.writerIndex();
        if (iWriterIndex - byteBuf.readerIndex() < 10) {
            byteBuf.readerIndex(i);
            return null;
        }
        DnsRecordType dnsRecordTypeValueOf = DnsRecordType.valueOf(byteBuf.readUnsignedShort());
        int unsignedShort = byteBuf.readUnsignedShort();
        long unsignedInt = byteBuf.readUnsignedInt();
        int unsignedShort2 = byteBuf.readUnsignedShort();
        int i2 = byteBuf.readerIndex();
        if (iWriterIndex - i2 < unsignedShort2) {
            byteBuf.readerIndex(i);
            return null;
        }
        T t = (T) decodeRecord(strDecodeName, dnsRecordTypeValueOf, unsignedShort, unsignedInt, byteBuf, i2, unsignedShort2);
        byteBuf.readerIndex(i2 + unsignedShort2);
        return t;
    }

    protected DnsRecord decodeRecord(String name, DnsRecordType type, int dnsClass, long timeToLive, ByteBuf in, int offset, int length) throws Exception {
        if (type == DnsRecordType.PTR) {
            return new DefaultDnsPtrRecord(name, dnsClass, timeToLive, decodeName0(in.duplicate().setIndex(offset, offset + length)));
        }
        if (type == DnsRecordType.CNAME || type == DnsRecordType.NS) {
            return new DefaultDnsRawRecord(name, type, dnsClass, timeToLive, DnsCodecUtil.decompressDomainName(in.duplicate().setIndex(offset, offset + length)));
        }
        return new DefaultDnsRawRecord(name, type, dnsClass, timeToLive, in.retainedDuplicate().setIndex(offset, offset + length));
    }

    protected String decodeName0(ByteBuf in) {
        return decodeName(in);
    }

    public static String decodeName(ByteBuf in) {
        return DnsCodecUtil.decodeDomainName(in);
    }
}
