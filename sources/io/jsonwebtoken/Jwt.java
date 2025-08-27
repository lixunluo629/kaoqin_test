package io.jsonwebtoken;

import io.jsonwebtoken.Header;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/Jwt.class */
public interface Jwt<H extends Header, B> {
    H getHeader();

    B getBody();
}
