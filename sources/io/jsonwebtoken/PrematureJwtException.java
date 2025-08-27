package io.jsonwebtoken;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/PrematureJwtException.class */
public class PrematureJwtException extends ClaimJwtException {
    public PrematureJwtException(Header header, Claims claims, String message) {
        super(header, claims, message);
    }

    public PrematureJwtException(Header header, Claims claims, String message, Throwable cause) {
        super(header, claims, message, cause);
    }
}
