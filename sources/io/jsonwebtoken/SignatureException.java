package io.jsonwebtoken;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/SignatureException.class */
public class SignatureException extends JwtException {
    public SignatureException(String message) {
        super(message);
    }

    public SignatureException(String message, Throwable cause) {
        super(message, cause);
    }
}
