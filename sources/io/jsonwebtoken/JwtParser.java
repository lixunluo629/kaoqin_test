package io.jsonwebtoken;

import java.security.Key;
import java.util.Date;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/JwtParser.class */
public interface JwtParser {
    public static final char SEPARATOR_CHAR = '.';

    JwtParser requireId(String str);

    JwtParser requireSubject(String str);

    JwtParser requireAudience(String str);

    JwtParser requireIssuer(String str);

    JwtParser requireIssuedAt(Date date);

    JwtParser requireExpiration(Date date);

    JwtParser requireNotBefore(Date date);

    JwtParser require(String str, Object obj);

    JwtParser setSigningKey(byte[] bArr);

    JwtParser setSigningKey(String str);

    JwtParser setSigningKey(Key key);

    JwtParser setSigningKeyResolver(SigningKeyResolver signingKeyResolver);

    JwtParser setCompressionCodecResolver(CompressionCodecResolver compressionCodecResolver);

    boolean isSigned(String str);

    Jwt parse(String str) throws MalformedJwtException, ExpiredJwtException, SignatureException, IllegalArgumentException;

    <T> T parse(String str, JwtHandler<T> jwtHandler) throws UnsupportedJwtException, MalformedJwtException, ExpiredJwtException, SignatureException, IllegalArgumentException;

    Jwt<Header, String> parsePlaintextJwt(String str) throws UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException;

    Jwt<Header, Claims> parseClaimsJwt(String str) throws UnsupportedJwtException, MalformedJwtException, ExpiredJwtException, SignatureException, IllegalArgumentException;

    Jws<String> parsePlaintextJws(String str) throws UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException;

    Jws<Claims> parseClaimsJws(String str) throws UnsupportedJwtException, MalformedJwtException, ExpiredJwtException, SignatureException, IllegalArgumentException;
}
