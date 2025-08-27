package io.jsonwebtoken.impl;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/DefaultJwt.class */
public class DefaultJwt<B> implements Jwt<Header, B> {
    private final Header header;
    private final B body;

    public DefaultJwt(Header header, B body) {
        this.header = header;
        this.body = body;
    }

    @Override // io.jsonwebtoken.Jwt
    public Header getHeader() {
        return this.header;
    }

    @Override // io.jsonwebtoken.Jwt
    public B getBody() {
        return this.body;
    }

    public String toString() {
        return "header=" + this.header + ",body=" + this.body;
    }
}
