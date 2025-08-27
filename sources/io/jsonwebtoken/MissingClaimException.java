package io.jsonwebtoken;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/MissingClaimException.class */
public class MissingClaimException extends InvalidClaimException {
    public MissingClaimException(Header header, Claims claims, String message) {
        super(header, claims, message);
    }

    public MissingClaimException(Header header, Claims claims, String message, Throwable cause) {
        super(header, claims, message, cause);
    }
}
