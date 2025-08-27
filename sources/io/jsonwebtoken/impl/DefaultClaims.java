package io.jsonwebtoken.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.RequiredTypeException;
import java.util.Date;
import java.util.Map;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/DefaultClaims.class */
public class DefaultClaims extends JwtMap implements Claims {
    public DefaultClaims() {
    }

    public DefaultClaims(Map<String, Object> map) {
        super(map);
    }

    @Override // io.jsonwebtoken.Claims
    public String getIssuer() {
        return getString(Claims.ISSUER);
    }

    @Override // io.jsonwebtoken.ClaimsMutator
    public Claims setIssuer(String iss) {
        setValue(Claims.ISSUER, iss);
        return this;
    }

    @Override // io.jsonwebtoken.Claims
    public String getSubject() {
        return getString(Claims.SUBJECT);
    }

    @Override // io.jsonwebtoken.ClaimsMutator
    public Claims setSubject(String sub) {
        setValue(Claims.SUBJECT, sub);
        return this;
    }

    @Override // io.jsonwebtoken.Claims
    public String getAudience() {
        return getString(Claims.AUDIENCE);
    }

    @Override // io.jsonwebtoken.ClaimsMutator
    public Claims setAudience(String aud) {
        setValue(Claims.AUDIENCE, aud);
        return this;
    }

    @Override // io.jsonwebtoken.Claims
    public Date getExpiration() {
        return (Date) get(Claims.EXPIRATION, Date.class);
    }

    @Override // io.jsonwebtoken.ClaimsMutator
    public Claims setExpiration(Date exp) {
        setDate(Claims.EXPIRATION, exp);
        return this;
    }

    @Override // io.jsonwebtoken.Claims
    public Date getNotBefore() {
        return (Date) get(Claims.NOT_BEFORE, Date.class);
    }

    @Override // io.jsonwebtoken.ClaimsMutator
    public Claims setNotBefore(Date nbf) {
        setDate(Claims.NOT_BEFORE, nbf);
        return this;
    }

    @Override // io.jsonwebtoken.Claims
    public Date getIssuedAt() {
        return (Date) get(Claims.ISSUED_AT, Date.class);
    }

    @Override // io.jsonwebtoken.ClaimsMutator
    public Claims setIssuedAt(Date iat) {
        setDate(Claims.ISSUED_AT, iat);
        return this;
    }

    @Override // io.jsonwebtoken.Claims
    public String getId() {
        return getString(Claims.ID);
    }

    @Override // io.jsonwebtoken.ClaimsMutator
    public Claims setId(String jti) {
        setValue(Claims.ID, jti);
        return this;
    }

    @Override // io.jsonwebtoken.Claims
    public <T> T get(String claimName, Class<T> requiredType) {
        Object value = get(claimName);
        if (value == null) {
            return null;
        }
        if (Claims.EXPIRATION.equals(claimName) || Claims.ISSUED_AT.equals(claimName) || Claims.NOT_BEFORE.equals(claimName)) {
            value = getDate(claimName);
        }
        if (requiredType == Date.class && (value instanceof Long)) {
            value = new Date(((Long) value).longValue());
        }
        if (!requiredType.isInstance(value)) {
            throw new RequiredTypeException("Expected value to be of type: " + requiredType + ", but was " + value.getClass());
        }
        return requiredType.cast(value);
    }
}
