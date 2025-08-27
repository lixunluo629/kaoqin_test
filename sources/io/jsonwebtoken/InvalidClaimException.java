package io.jsonwebtoken;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/InvalidClaimException.class */
public class InvalidClaimException extends ClaimJwtException {
    private String claimName;
    private Object claimValue;

    protected InvalidClaimException(Header header, Claims claims, String message) {
        super(header, claims, message);
    }

    protected InvalidClaimException(Header header, Claims claims, String message, Throwable cause) {
        super(header, claims, message, cause);
    }

    public String getClaimName() {
        return this.claimName;
    }

    public void setClaimName(String claimName) {
        this.claimName = claimName;
    }

    public Object getClaimValue() {
        return this.claimValue;
    }

    public void setClaimValue(Object claimValue) {
        this.claimValue = claimValue;
    }
}
