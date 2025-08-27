package io.jsonwebtoken.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodec;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSigner;
import io.jsonwebtoken.impl.crypto.JwtSigner;
import io.jsonwebtoken.lang.Assert;
import io.jsonwebtoken.lang.Collections;
import io.jsonwebtoken.lang.Objects;
import io.jsonwebtoken.lang.Strings;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/DefaultJwtBuilder.class */
public class DefaultJwtBuilder implements JwtBuilder {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private Header header;
    private Claims claims;
    private String payload;
    private SignatureAlgorithm algorithm;
    private Key key;
    private byte[] keyBytes;
    private CompressionCodec compressionCodec;

    @Override // io.jsonwebtoken.JwtBuilder
    public JwtBuilder setHeader(Header header) {
        this.header = header;
        return this;
    }

    @Override // io.jsonwebtoken.JwtBuilder
    public JwtBuilder setHeader(Map<String, Object> header) {
        this.header = new DefaultHeader(header);
        return this;
    }

    @Override // io.jsonwebtoken.JwtBuilder
    public JwtBuilder setHeaderParams(Map<String, Object> params) {
        if (!Collections.isEmpty(params)) {
            Header header = ensureHeader();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                header.put(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    protected Header ensureHeader() {
        if (this.header == null) {
            this.header = new DefaultHeader();
        }
        return this.header;
    }

    @Override // io.jsonwebtoken.JwtBuilder
    public JwtBuilder setHeaderParam(String name, Object value) {
        ensureHeader().put(name, value);
        return this;
    }

    @Override // io.jsonwebtoken.JwtBuilder
    public JwtBuilder signWith(SignatureAlgorithm alg, byte[] secretKey) {
        Assert.notNull(alg, "SignatureAlgorithm cannot be null.");
        Assert.notEmpty(secretKey, "secret key byte array cannot be null or empty.");
        Assert.isTrue(alg.isHmac(), "Key bytes may only be specified for HMAC signatures.  If using RSA or Elliptic Curve, use the signWith(SignatureAlgorithm, Key) method instead.");
        this.algorithm = alg;
        this.keyBytes = secretKey;
        return this;
    }

    @Override // io.jsonwebtoken.JwtBuilder
    public JwtBuilder signWith(SignatureAlgorithm alg, String base64EncodedSecretKey) {
        Assert.hasText(base64EncodedSecretKey, "base64-encoded secret key cannot be null or empty.");
        Assert.isTrue(alg.isHmac(), "Base64-encoded key bytes may only be specified for HMAC signatures.  If using RSA or Elliptic Curve, use the signWith(SignatureAlgorithm, Key) method instead.");
        byte[] bytes = TextCodec.BASE64.decode(base64EncodedSecretKey);
        return signWith(alg, bytes);
    }

    @Override // io.jsonwebtoken.JwtBuilder
    public JwtBuilder signWith(SignatureAlgorithm alg, Key key) {
        Assert.notNull(alg, "SignatureAlgorithm cannot be null.");
        Assert.notNull(key, "Key argument cannot be null.");
        this.algorithm = alg;
        this.key = key;
        return this;
    }

    @Override // io.jsonwebtoken.JwtBuilder
    public JwtBuilder compressWith(CompressionCodec compressionCodec) {
        Assert.notNull(compressionCodec, "compressionCodec cannot be null");
        this.compressionCodec = compressionCodec;
        return this;
    }

    @Override // io.jsonwebtoken.JwtBuilder
    public JwtBuilder setPayload(String payload) {
        this.payload = payload;
        return this;
    }

    protected Claims ensureClaims() {
        if (this.claims == null) {
            this.claims = new DefaultClaims();
        }
        return this.claims;
    }

    @Override // io.jsonwebtoken.JwtBuilder
    public JwtBuilder setClaims(Claims claims) {
        this.claims = claims;
        return this;
    }

    @Override // io.jsonwebtoken.JwtBuilder
    public JwtBuilder setClaims(Map<String, Object> claims) {
        this.claims = Jwts.claims(claims);
        return this;
    }

    @Override // io.jsonwebtoken.ClaimsMutator
    public JwtBuilder setIssuer(String iss) {
        if (Strings.hasText(iss)) {
            ensureClaims().setIssuer(iss);
        } else if (this.claims != null) {
            this.claims.setIssuer(iss);
        }
        return this;
    }

    @Override // io.jsonwebtoken.ClaimsMutator
    public JwtBuilder setSubject(String sub) {
        if (Strings.hasText(sub)) {
            ensureClaims().setSubject(sub);
        } else if (this.claims != null) {
            this.claims.setSubject(sub);
        }
        return this;
    }

    @Override // io.jsonwebtoken.ClaimsMutator
    public JwtBuilder setAudience(String aud) {
        if (Strings.hasText(aud)) {
            ensureClaims().setAudience(aud);
        } else if (this.claims != null) {
            this.claims.setAudience(aud);
        }
        return this;
    }

    @Override // io.jsonwebtoken.ClaimsMutator
    public JwtBuilder setExpiration(Date exp) {
        if (exp != null) {
            ensureClaims().setExpiration(exp);
        } else if (this.claims != null) {
            this.claims.setExpiration(exp);
        }
        return this;
    }

    @Override // io.jsonwebtoken.ClaimsMutator
    public JwtBuilder setNotBefore(Date nbf) {
        if (nbf != null) {
            ensureClaims().setNotBefore(nbf);
        } else if (this.claims != null) {
            this.claims.setNotBefore(nbf);
        }
        return this;
    }

    @Override // io.jsonwebtoken.ClaimsMutator
    public JwtBuilder setIssuedAt(Date iat) {
        if (iat != null) {
            ensureClaims().setIssuedAt(iat);
        } else if (this.claims != null) {
            this.claims.setIssuedAt(iat);
        }
        return this;
    }

    @Override // io.jsonwebtoken.ClaimsMutator
    public JwtBuilder setId(String jti) {
        if (Strings.hasText(jti)) {
            ensureClaims().setId(jti);
        } else if (this.claims != null) {
            this.claims.setId(jti);
        }
        return this;
    }

    @Override // io.jsonwebtoken.JwtBuilder
    public JwtBuilder claim(String name, Object value) {
        Assert.hasText(name, "Claim property name cannot be null or empty.");
        if (this.claims == null) {
            if (value != null) {
                ensureClaims().put(name, value);
            }
        } else if (value == null) {
            this.claims.remove(name);
        } else {
            this.claims.put(name, value);
        }
        return this;
    }

    @Override // io.jsonwebtoken.JwtBuilder
    public String compact() {
        JwsHeader jwsHeader;
        String base64UrlEncodedBody;
        String jwt;
        String strBase64UrlEncode;
        if (this.payload == null && Collections.isEmpty(this.claims)) {
            throw new IllegalStateException("Either 'payload' or 'claims' must be specified.");
        }
        if (this.payload != null && !Collections.isEmpty(this.claims)) {
            throw new IllegalStateException("Both 'payload' and 'claims' cannot both be specified. Choose either one.");
        }
        if (this.key != null && this.keyBytes != null) {
            throw new IllegalStateException("A key object and key bytes cannot both be specified. Choose either one.");
        }
        Header header = ensureHeader();
        Key key = this.key;
        if (key == null && !Objects.isEmpty(this.keyBytes)) {
            key = new SecretKeySpec(this.keyBytes, this.algorithm.getJcaName());
        }
        if (header instanceof JwsHeader) {
            jwsHeader = (JwsHeader) header;
        } else {
            jwsHeader = new DefaultJwsHeader(header);
        }
        if (key != null) {
            jwsHeader.setAlgorithm(this.algorithm.getValue());
        } else {
            jwsHeader.setAlgorithm(SignatureAlgorithm.NONE.getValue());
        }
        if (this.compressionCodec != null) {
            jwsHeader.setCompressionAlgorithm(this.compressionCodec.getAlgorithmName());
        }
        String base64UrlEncodedHeader = base64UrlEncode(jwsHeader, "Unable to serialize header to json.");
        if (this.compressionCodec != null) {
            try {
                byte[] bytes = this.payload != null ? this.payload.getBytes(Strings.UTF_8) : toJson(this.claims);
                base64UrlEncodedBody = TextCodec.BASE64URL.encode(this.compressionCodec.compress(bytes));
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Unable to serialize claims object to json.");
            }
        } else {
            if (this.payload != null) {
                strBase64UrlEncode = TextCodec.BASE64URL.encode(this.payload);
            } else {
                strBase64UrlEncode = base64UrlEncode(this.claims, "Unable to serialize claims object to json.");
            }
            base64UrlEncodedBody = strBase64UrlEncode;
        }
        String jwt2 = base64UrlEncodedHeader + '.' + base64UrlEncodedBody;
        if (key != null) {
            JwtSigner signer = createSigner(this.algorithm, key);
            String base64UrlSignature = signer.sign(jwt2);
            jwt = jwt2 + '.' + base64UrlSignature;
        } else {
            jwt = jwt2 + '.';
        }
        return jwt;
    }

    protected JwtSigner createSigner(SignatureAlgorithm alg, Key key) {
        return new DefaultJwtSigner(alg, key);
    }

    protected String base64UrlEncode(Object o, String errMsg) {
        try {
            byte[] bytes = toJson(o);
            return TextCodec.BASE64URL.encode(bytes);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(errMsg, e);
        }
    }

    protected byte[] toJson(Object object) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsBytes(object);
    }
}
