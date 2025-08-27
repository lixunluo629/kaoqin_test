package com.moredian.onpremise.core.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/JwtUtils.class */
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) JwtUtils.class);
    public static String secretKey = "";

    public static String generateSecretKey() {
        String uuid = new BASE64Encoder().encode(UUID.randomUUID().toString().getBytes());
        return uuid;
    }

    public static Key generateKey(String secretKey2) {
        Key key = null;
        try {
            byte[] keyBytes = new BASE64Decoder().decodeBuffer(secretKey2);
            key = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
        } catch (Exception e) {
        }
        return key;
    }

    public static Key decodeKey(String secretKey2) {
        Key key = null;
        try {
            byte[] keyBytes = new BASE64Decoder().decodeBuffer(secretKey2);
            key = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
        } catch (Exception e) {
            logger.error("error:{}", (Throwable) e);
        }
        return key;
    }

    public static String tokenParams(Map<String, Object> claims, String secretKey2, Date date) {
        Key key = decodeKey(secretKey2);
        String token = Jwts.builder().setClaims(claims).setExpiration(date).signWith(SignatureAlgorithm.HS256, key).compact();
        String base64Token = new BASE64Encoder().encode(token.getBytes());
        return base64Token.replace('=', '*').replace(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR, "").replace("\r", "");
    }

    public static Map<String, Object> verifyParams(String token, String secretKey2) {
        try {
            String token2 = token.replace('*', '=');
            Key key = decodeKey(secretKey2);
            String decodeToken = new String(new BASE64Decoder().decodeBuffer(token2));
            Map<String, Object> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(decodeToken).getBody();
            return claims;
        } catch (Exception e) {
            return null;
        }
    }

    public static String token(String subject, String secretKey2, Date date) {
        Key key = decodeKey(secretKey2);
        String token = Jwts.builder().setExpiration(date).setSubject(subject).signWith(SignatureAlgorithm.HS256, key).compact();
        String base64Token = new BASE64Encoder().encode(token.getBytes());
        return base64Token.replace('=', '*').replace(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR, "").replace("\r", "");
    }

    public static String verify(String token, String secretKey2) {
        try {
            String token2 = token.replace('*', '=');
            Key key = decodeKey(secretKey2);
            String decodeToken = new String(new BASE64Decoder().decodeBuffer(token2));
            String subject = Jwts.parser().setSigningKey(key).parseClaimsJws(decodeToken).getBody().getSubject();
            return subject;
        } catch (Exception e) {
            return null;
        }
    }
}
