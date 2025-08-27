package io.jsonwebtoken.impl;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/TextCodec.class */
public interface TextCodec {
    public static final TextCodec BASE64 = new DefaultTextCodecFactory().getTextCodec();
    public static final TextCodec BASE64URL = new Base64UrlCodec();

    String encode(String str);

    String encode(byte[] bArr);

    byte[] decode(String str);

    String decodeToString(String str);
}
