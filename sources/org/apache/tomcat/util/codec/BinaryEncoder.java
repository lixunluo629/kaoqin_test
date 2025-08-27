package org.apache.tomcat.util.codec;

@Deprecated
/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/codec/BinaryEncoder.class */
public interface BinaryEncoder extends Encoder {
    byte[] encode(byte[] bArr) throws EncoderException;
}
