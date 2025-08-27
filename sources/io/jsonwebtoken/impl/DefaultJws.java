package io.jsonwebtoken.impl;

import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/DefaultJws.class */
public class DefaultJws<B> implements Jws<B> {
    private final JwsHeader header;
    private final B body;
    private final String signature;

    public DefaultJws(JwsHeader header, B body, String signature) {
        this.header = header;
        this.body = body;
        this.signature = signature;
    }

    @Override // io.jsonwebtoken.Jwt
    public JwsHeader getHeader() {
        return this.header;
    }

    @Override // io.jsonwebtoken.Jwt
    public B getBody() {
        return this.body;
    }

    @Override // io.jsonwebtoken.Jws
    public String getSignature() {
        return this.signature;
    }

    public String toString() {
        return "header=" + this.header + ",body=" + this.body + ",signature=" + this.signature;
    }
}
