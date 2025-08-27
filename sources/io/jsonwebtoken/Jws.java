package io.jsonwebtoken;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/Jws.class */
public interface Jws<B> extends Jwt<JwsHeader, B> {
    String getSignature();
}
