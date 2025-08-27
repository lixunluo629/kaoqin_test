package io.jsonwebtoken;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/MalformedJwtException.class */
public class MalformedJwtException extends JwtException {
    public MalformedJwtException(String message) {
        super(message);
    }

    public MalformedJwtException(String message, Throwable cause) {
        super(message, cause);
    }
}
