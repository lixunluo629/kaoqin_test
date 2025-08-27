package io.jsonwebtoken;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/JwtHandler.class */
public interface JwtHandler<T> {
    T onPlaintextJwt(Jwt<Header, String> jwt);

    T onClaimsJwt(Jwt<Header, Claims> jwt);

    T onPlaintextJws(Jws<String> jws);

    T onClaimsJws(Jws<Claims> jws);
}
