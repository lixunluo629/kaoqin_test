package org.apache.tomcat.util.codec;

@Deprecated
/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/codec/BinaryDecoder.class */
public interface BinaryDecoder extends Decoder {
    byte[] decode(byte[] bArr) throws DecoderException;
}
