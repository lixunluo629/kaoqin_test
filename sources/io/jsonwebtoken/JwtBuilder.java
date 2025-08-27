package io.jsonwebtoken;

import java.security.Key;
import java.util.Date;
import java.util.Map;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/JwtBuilder.class */
public interface JwtBuilder extends ClaimsMutator<JwtBuilder> {
    JwtBuilder setHeader(Header header);

    JwtBuilder setHeader(Map<String, Object> map);

    JwtBuilder setHeaderParams(Map<String, Object> map);

    JwtBuilder setHeaderParam(String str, Object obj);

    JwtBuilder setPayload(String str);

    JwtBuilder setClaims(Claims claims);

    JwtBuilder setClaims(Map<String, Object> map);

    @Override // io.jsonwebtoken.ClaimsMutator
    JwtBuilder setIssuer(String str);

    @Override // io.jsonwebtoken.ClaimsMutator
    JwtBuilder setSubject(String str);

    @Override // io.jsonwebtoken.ClaimsMutator
    JwtBuilder setAudience(String str);

    @Override // io.jsonwebtoken.ClaimsMutator
    JwtBuilder setExpiration(Date date);

    @Override // io.jsonwebtoken.ClaimsMutator
    JwtBuilder setNotBefore(Date date);

    @Override // io.jsonwebtoken.ClaimsMutator
    JwtBuilder setIssuedAt(Date date);

    @Override // io.jsonwebtoken.ClaimsMutator
    JwtBuilder setId(String str);

    JwtBuilder claim(String str, Object obj);

    JwtBuilder signWith(SignatureAlgorithm signatureAlgorithm, byte[] bArr);

    JwtBuilder signWith(SignatureAlgorithm signatureAlgorithm, String str);

    JwtBuilder signWith(SignatureAlgorithm signatureAlgorithm, Key key);

    JwtBuilder compressWith(CompressionCodec compressionCodec);

    String compact();
}
