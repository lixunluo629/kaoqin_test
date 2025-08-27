package io.jsonwebtoken;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/ExpiredJwtException.class */
public class ExpiredJwtException extends ClaimJwtException {
    public ExpiredJwtException(Header header, Claims claims, String message) {
        super(header, claims, message);
    }

    public ExpiredJwtException(Header header, Claims claims, String message, Throwable cause) {
        super(header, claims, message, cause);
    }
}
