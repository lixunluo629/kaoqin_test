package io.jsonwebtoken.impl;

import javax.xml.bind.DatatypeConverter;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/Base64Codec.class */
public class Base64Codec extends AbstractTextCodec {
    @Override // io.jsonwebtoken.impl.TextCodec
    public String encode(byte[] data) {
        return DatatypeConverter.printBase64Binary(data);
    }

    @Override // io.jsonwebtoken.impl.TextCodec
    public byte[] decode(String encoded) {
        return DatatypeConverter.parseBase64Binary(encoded);
    }
}
