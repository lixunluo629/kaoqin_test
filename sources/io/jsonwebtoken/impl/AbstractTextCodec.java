package io.jsonwebtoken.impl;

import io.jsonwebtoken.lang.Assert;
import java.nio.charset.Charset;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/AbstractTextCodec.class */
public abstract class AbstractTextCodec implements TextCodec {
    protected static final Charset UTF8 = Charset.forName("UTF-8");
    protected static final Charset US_ASCII = Charset.forName("US-ASCII");

    @Override // io.jsonwebtoken.impl.TextCodec
    public String encode(String data) {
        Assert.hasText(data, "String argument to encode cannot be null or empty.");
        byte[] bytes = data.getBytes(UTF8);
        return encode(bytes);
    }

    @Override // io.jsonwebtoken.impl.TextCodec
    public String decodeToString(String encoded) {
        byte[] bytes = decode(encoded);
        return new String(bytes, UTF8);
    }
}
