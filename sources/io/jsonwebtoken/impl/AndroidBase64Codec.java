package io.jsonwebtoken.impl;

import android.util.Base64;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/AndroidBase64Codec.class */
public class AndroidBase64Codec extends AbstractTextCodec {
    @Override // io.jsonwebtoken.impl.TextCodec
    public String encode(byte[] data) {
        return Base64.encodeToString(data, 3);
    }

    @Override // io.jsonwebtoken.impl.TextCodec
    public byte[] decode(String encoded) {
        return Base64.decode(encoded, 0);
    }
}
