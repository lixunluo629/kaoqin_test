package io.jsonwebtoken.impl;

import io.jsonwebtoken.Header;
import java.util.Map;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/DefaultHeader.class */
public class DefaultHeader<T extends Header<T>> extends JwtMap implements Header<T> {
    public DefaultHeader() {
    }

    public DefaultHeader(Map<String, Object> map) {
        super(map);
    }

    @Override // io.jsonwebtoken.Header
    public String getType() {
        return getString(Header.TYPE);
    }

    @Override // io.jsonwebtoken.Header
    public T setType(String typ) {
        setValue(Header.TYPE, typ);
        return this;
    }

    @Override // io.jsonwebtoken.Header
    public String getContentType() {
        return getString(Header.CONTENT_TYPE);
    }

    @Override // io.jsonwebtoken.Header
    public T setContentType(String cty) {
        setValue(Header.CONTENT_TYPE, cty);
        return this;
    }

    @Override // io.jsonwebtoken.Header
    public String getCompressionAlgorithm() {
        return getString(Header.COMPRESSION_ALGORITHM);
    }

    @Override // io.jsonwebtoken.Header
    public T setCompressionAlgorithm(String compressionAlgorithm) {
        setValue(Header.COMPRESSION_ALGORITHM, compressionAlgorithm);
        return this;
    }
}
