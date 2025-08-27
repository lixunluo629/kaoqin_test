package io.jsonwebtoken;

import java.util.Date;
import java.util.Map;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/Claims.class */
public interface Claims extends Map<String, Object>, ClaimsMutator<Claims> {
    public static final String ISSUER = "iss";
    public static final String SUBJECT = "sub";
    public static final String AUDIENCE = "aud";
    public static final String EXPIRATION = "exp";
    public static final String NOT_BEFORE = "nbf";
    public static final String ISSUED_AT = "iat";
    public static final String ID = "jti";

    String getIssuer();

    @Override // io.jsonwebtoken.ClaimsMutator
    Claims setIssuer(String str);

    String getSubject();

    @Override // io.jsonwebtoken.ClaimsMutator
    Claims setSubject(String str);

    String getAudience();

    @Override // io.jsonwebtoken.ClaimsMutator
    Claims setAudience(String str);

    Date getExpiration();

    @Override // io.jsonwebtoken.ClaimsMutator
    Claims setExpiration(Date date);

    Date getNotBefore();

    @Override // io.jsonwebtoken.ClaimsMutator
    Claims setNotBefore(Date date);

    Date getIssuedAt();

    @Override // io.jsonwebtoken.ClaimsMutator
    Claims setIssuedAt(Date date);

    String getId();

    @Override // io.jsonwebtoken.ClaimsMutator
    Claims setId(String str);

    <T> T get(String str, Class<T> cls);
}
