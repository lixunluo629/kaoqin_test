package io.jsonwebtoken.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodec;
import io.jsonwebtoken.CompressionCodecResolver;
import io.jsonwebtoken.CompressionException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtHandler;
import io.jsonwebtoken.JwtHandlerAdapter;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.PrematureJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.SigningKeyResolver;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.compression.DefaultCompressionCodecResolver;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import io.jsonwebtoken.impl.crypto.JwtSignatureValidator;
import io.jsonwebtoken.lang.Assert;
import io.jsonwebtoken.lang.Objects;
import io.jsonwebtoken.lang.Strings;
import java.io.IOException;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/DefaultJwtParser.class */
public class DefaultJwtParser implements JwtParser {
    private static final String ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    private byte[] keyBytes;
    private Key key;
    private SigningKeyResolver signingKeyResolver;
    private ObjectMapper objectMapper = new ObjectMapper();
    private CompressionCodecResolver compressionCodecResolver = new DefaultCompressionCodecResolver();
    Claims expectedClaims = new DefaultClaims();

    @Override // io.jsonwebtoken.JwtParser
    public JwtParser requireIssuedAt(Date issuedAt) {
        this.expectedClaims.setIssuedAt(issuedAt);
        return this;
    }

    @Override // io.jsonwebtoken.JwtParser
    public JwtParser requireIssuer(String issuer) {
        this.expectedClaims.setIssuer(issuer);
        return this;
    }

    @Override // io.jsonwebtoken.JwtParser
    public JwtParser requireAudience(String audience) {
        this.expectedClaims.setAudience(audience);
        return this;
    }

    @Override // io.jsonwebtoken.JwtParser
    public JwtParser requireSubject(String subject) {
        this.expectedClaims.setSubject(subject);
        return this;
    }

    @Override // io.jsonwebtoken.JwtParser
    public JwtParser requireId(String id) {
        this.expectedClaims.setId(id);
        return this;
    }

    @Override // io.jsonwebtoken.JwtParser
    public JwtParser requireExpiration(Date expiration) {
        this.expectedClaims.setExpiration(expiration);
        return this;
    }

    @Override // io.jsonwebtoken.JwtParser
    public JwtParser requireNotBefore(Date notBefore) {
        this.expectedClaims.setNotBefore(notBefore);
        return this;
    }

    @Override // io.jsonwebtoken.JwtParser
    public JwtParser require(String claimName, Object value) {
        Assert.hasText(claimName, "claim name cannot be null or empty.");
        Assert.notNull(value, "The value cannot be null for claim name: " + claimName);
        this.expectedClaims.put(claimName, value);
        return this;
    }

    @Override // io.jsonwebtoken.JwtParser
    public JwtParser setSigningKey(byte[] key) {
        Assert.notEmpty(key, "signing key cannot be null or empty.");
        this.keyBytes = key;
        return this;
    }

    @Override // io.jsonwebtoken.JwtParser
    public JwtParser setSigningKey(String base64EncodedKeyBytes) {
        Assert.hasText(base64EncodedKeyBytes, "signing key cannot be null or empty.");
        this.keyBytes = TextCodec.BASE64.decode(base64EncodedKeyBytes);
        return this;
    }

    @Override // io.jsonwebtoken.JwtParser
    public JwtParser setSigningKey(Key key) {
        Assert.notNull(key, "signing key cannot be null.");
        this.key = key;
        return this;
    }

    @Override // io.jsonwebtoken.JwtParser
    public JwtParser setSigningKeyResolver(SigningKeyResolver signingKeyResolver) {
        Assert.notNull(signingKeyResolver, "SigningKeyResolver cannot be null.");
        this.signingKeyResolver = signingKeyResolver;
        return this;
    }

    @Override // io.jsonwebtoken.JwtParser
    public JwtParser setCompressionCodecResolver(CompressionCodecResolver compressionCodecResolver) {
        Assert.notNull(compressionCodecResolver, "compressionCodecResolver cannot be null.");
        this.compressionCodecResolver = compressionCodecResolver;
        return this;
    }

    @Override // io.jsonwebtoken.JwtParser
    public boolean isSigned(String jwt) {
        if (jwt == null) {
            return false;
        }
        int delimiterCount = 0;
        for (int i = 0; i < jwt.length(); i++) {
            char c = jwt.charAt(i);
            if (delimiterCount == 2) {
                return (Character.isWhitespace(c) || c == '.') ? false : true;
            }
            if (c == '.') {
                delimiterCount++;
            }
        }
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v146, types: [io.jsonwebtoken.SigningKeyResolver] */
    /* JADX WARN: Type inference failed for: r0v163, types: [io.jsonwebtoken.impl.DefaultClaims] */
    /* JADX WARN: Type inference failed for: r0v35, types: [io.jsonwebtoken.Claims] */
    /* JADX WARN: Type inference failed for: r0v38, types: [io.jsonwebtoken.Claims] */
    /* JADX WARN: Type inference failed for: r2v21, types: [io.jsonwebtoken.Claims] */
    /* JADX WARN: Type inference failed for: r3v2, types: [io.jsonwebtoken.Claims] */
    /* JADX WARN: Type inference failed for: r3v3, types: [io.jsonwebtoken.Claims] */
    @Override // io.jsonwebtoken.JwtParser
    public Jwt parse(String jwt) throws MalformedJwtException, SignatureException, ExpiredJwtException, CompressionException {
        String payload;
        Assert.hasText(jwt, "JWT String argument cannot be null or empty.");
        String base64UrlEncodedHeader = null;
        String base64UrlEncodedPayload = null;
        String base64UrlEncodedDigest = null;
        int delimiterCount = 0;
        StringBuilder sb = new StringBuilder(128);
        for (char c : jwt.toCharArray()) {
            if (c == '.') {
                String token = Strings.clean(sb.toString());
                if (delimiterCount == 0) {
                    base64UrlEncodedHeader = token;
                } else if (delimiterCount == 1) {
                    base64UrlEncodedPayload = token;
                }
                delimiterCount++;
                sb = new StringBuilder(128);
            } else {
                sb.append(c);
            }
        }
        if (delimiterCount != 2) {
            String msg = "JWT strings must contain exactly 2 period characters. Found: " + delimiterCount;
            throw new MalformedJwtException(msg);
        }
        if (sb.length() > 0) {
            base64UrlEncodedDigest = sb.toString();
        }
        if (base64UrlEncodedPayload == null) {
            throw new MalformedJwtException("JWT string '" + jwt + "' is missing a body/payload.");
        }
        Header header = null;
        CompressionCodec compressionCodec = null;
        if (base64UrlEncodedHeader != null) {
            String origValue = TextCodec.BASE64URL.decodeToString(base64UrlEncodedHeader);
            Map<String, Object> m = readValue(origValue);
            if (base64UrlEncodedDigest != null) {
                header = new DefaultJwsHeader(m);
            } else {
                header = new DefaultHeader(m);
            }
            compressionCodec = this.compressionCodecResolver.resolveCompressionCodec(header);
        }
        if (compressionCodec != null) {
            byte[] decompressed = compressionCodec.decompress(TextCodec.BASE64URL.decode(base64UrlEncodedPayload));
            payload = new String(decompressed, Strings.UTF_8);
        } else {
            payload = TextCodec.BASE64URL.decodeToString(base64UrlEncodedPayload);
        }
        String defaultClaims = null;
        if (payload.charAt(0) == '{' && payload.charAt(payload.length() - 1) == '}') {
            Map<String, Object> claimsMap = readValue(payload);
            defaultClaims = new DefaultClaims(claimsMap);
        }
        if (base64UrlEncodedDigest != null) {
            JwsHeader jwsHeader = (JwsHeader) header;
            SignatureAlgorithm algorithm = null;
            if (header != null) {
                String alg = jwsHeader.getAlgorithm();
                if (Strings.hasText(alg)) {
                    algorithm = SignatureAlgorithm.forName(alg);
                }
            }
            if (algorithm == null || algorithm == SignatureAlgorithm.NONE) {
                throw new MalformedJwtException("JWT string has a digest/signature, but the header does not reference a valid signature algorithm.");
            }
            if (this.key != null && this.keyBytes != null) {
                throw new IllegalStateException("A key object and key bytes cannot both be specified. Choose either.");
            }
            if ((this.key != null || this.keyBytes != null) && this.signingKeyResolver != null) {
                String object = this.key != null ? "a key object" : "key bytes";
                throw new IllegalStateException("A signing key resolver and " + object + " cannot both be specified. Choose either.");
            }
            Key key = this.key;
            if (key == null) {
                byte[] keyBytes = this.keyBytes;
                if (Objects.isEmpty(keyBytes) && this.signingKeyResolver != null) {
                    key = defaultClaims != null ? this.signingKeyResolver.resolveSigningKey(jwsHeader, defaultClaims) : this.signingKeyResolver.resolveSigningKey(jwsHeader, payload);
                }
                if (!Objects.isEmpty(keyBytes)) {
                    Assert.isTrue(!algorithm.isRsa(), "Key bytes cannot be specified for RSA signatures.  Please specify a PublicKey or PrivateKey instance.");
                    key = new SecretKeySpec(keyBytes, algorithm.getJcaName());
                }
            }
            Assert.notNull(key, "A signing key must be specified if the specified JWT is digitally signed.");
            String jwtWithoutSignature = base64UrlEncodedHeader + '.' + base64UrlEncodedPayload;
            try {
                JwtSignatureValidator validator = createSignatureValidator(algorithm, key);
                if (!validator.isValid(jwtWithoutSignature, base64UrlEncodedDigest)) {
                    throw new SignatureException("JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.");
                }
            } catch (IllegalArgumentException e) {
                String algName = algorithm.getValue();
                String msg2 = "The parsed JWT indicates it was signed with the " + algName + " signature algorithm, but the specified signing key of type " + key.getClass().getName() + " may not be used to validate " + algName + " signatures.  Because the specified signing key reflects a specific and expected algorithm, and the JWT does not reflect this algorithm, it is likely that the JWT was not expected and therefore should not be trusted.  Another possibility is that the parser was configured with the incorrect signing key, but this cannot be assumed for security reasons.";
                throw new UnsupportedJwtException(msg2, e);
            }
        }
        if (defaultClaims != null) {
            Date now = null;
            Date exp = defaultClaims.getExpiration();
            if (exp != null) {
                now = new Date();
                if (now.equals(exp) || now.after(exp)) {
                    SimpleDateFormat sdf = new SimpleDateFormat(ISO_8601_FORMAT);
                    String expVal = sdf.format(exp);
                    String nowVal = sdf.format(now);
                    String msg3 = "JWT expired at " + expVal + ". Current time: " + nowVal;
                    throw new ExpiredJwtException(header, defaultClaims, msg3);
                }
            }
            Date nbf = defaultClaims.getNotBefore();
            if (nbf != null) {
                if (now == null) {
                    now = new Date();
                }
                if (now.before(nbf)) {
                    SimpleDateFormat sdf2 = new SimpleDateFormat(ISO_8601_FORMAT);
                    String nbfVal = sdf2.format(nbf);
                    String nowVal2 = sdf2.format(now);
                    String msg4 = "JWT must not be accepted before " + nbfVal + ". Current time: " + nowVal2;
                    throw new PrematureJwtException(header, defaultClaims, msg4);
                }
            }
            validateExpectedClaims(header, defaultClaims);
        }
        Object body = defaultClaims != null ? defaultClaims : payload;
        if (base64UrlEncodedDigest != null) {
            return new DefaultJws((JwsHeader) header, body, base64UrlEncodedDigest);
        }
        return new DefaultJwt(header, body);
    }

    private void validateExpectedClaims(Header header, Claims claims) {
        for (String expectedClaimName : this.expectedClaims.keySet()) {
            Object expectedClaimValue = this.expectedClaims.get(expectedClaimName);
            Object actualClaimValue = claims.get(expectedClaimName);
            if (Claims.ISSUED_AT.equals(expectedClaimName) || Claims.EXPIRATION.equals(expectedClaimName) || Claims.NOT_BEFORE.equals(expectedClaimName)) {
                expectedClaimValue = this.expectedClaims.get(expectedClaimName, Date.class);
                actualClaimValue = claims.get(expectedClaimName, Date.class);
            } else if ((expectedClaimValue instanceof Date) && actualClaimValue != null && (actualClaimValue instanceof Long)) {
                actualClaimValue = new Date(((Long) actualClaimValue).longValue());
            }
            InvalidClaimException invalidClaimException = null;
            if (actualClaimValue == null) {
                String msg = String.format(ClaimJwtException.MISSING_EXPECTED_CLAIM_MESSAGE_TEMPLATE, expectedClaimName, expectedClaimValue);
                invalidClaimException = new MissingClaimException(header, claims, msg);
            } else if (!expectedClaimValue.equals(actualClaimValue)) {
                String msg2 = String.format(ClaimJwtException.INCORRECT_EXPECTED_CLAIM_MESSAGE_TEMPLATE, expectedClaimName, expectedClaimValue, actualClaimValue);
                invalidClaimException = new IncorrectClaimException(header, claims, msg2);
            }
            if (invalidClaimException != null) {
                invalidClaimException.setClaimName(expectedClaimName);
                invalidClaimException.setClaimValue(expectedClaimValue);
                throw invalidClaimException;
            }
        }
    }

    protected JwtSignatureValidator createSignatureValidator(SignatureAlgorithm alg, Key key) {
        return new DefaultJwtSignatureValidator(alg, key);
    }

    @Override // io.jsonwebtoken.JwtParser
    public <T> T parse(String compact, JwtHandler<T> handler) throws MalformedJwtException, SignatureException, ExpiredJwtException, CompressionException {
        Assert.notNull(handler, "JwtHandler argument cannot be null.");
        Assert.hasText(compact, "JWT String argument cannot be null or empty.");
        Jwt jwt = parse(compact);
        if (jwt instanceof Jws) {
            Jws jws = (Jws) jwt;
            Object body = jws.getBody();
            if (body instanceof Claims) {
                return handler.onClaimsJws(jws);
            }
            return handler.onPlaintextJws(jws);
        }
        Object body2 = jwt.getBody();
        if (body2 instanceof Claims) {
            return handler.onClaimsJwt(jwt);
        }
        return handler.onPlaintextJwt(jwt);
    }

    @Override // io.jsonwebtoken.JwtParser
    public Jwt<Header, String> parsePlaintextJwt(String plaintextJwt) {
        return (Jwt) parse(plaintextJwt, new JwtHandlerAdapter<Jwt<Header, String>>() { // from class: io.jsonwebtoken.impl.DefaultJwtParser.1
            @Override // io.jsonwebtoken.JwtHandlerAdapter, io.jsonwebtoken.JwtHandler
            public /* bridge */ /* synthetic */ Object onPlaintextJwt(Jwt jwt) {
                return onPlaintextJwt((Jwt<Header, String>) jwt);
            }

            @Override // io.jsonwebtoken.JwtHandlerAdapter, io.jsonwebtoken.JwtHandler
            public Jwt<Header, String> onPlaintextJwt(Jwt<Header, String> jwt) {
                return jwt;
            }
        });
    }

    @Override // io.jsonwebtoken.JwtParser
    public Jwt<Header, Claims> parseClaimsJwt(String claimsJwt) {
        try {
            return (Jwt) parse(claimsJwt, new JwtHandlerAdapter<Jwt<Header, Claims>>() { // from class: io.jsonwebtoken.impl.DefaultJwtParser.2
                @Override // io.jsonwebtoken.JwtHandlerAdapter, io.jsonwebtoken.JwtHandler
                public /* bridge */ /* synthetic */ Object onClaimsJwt(Jwt jwt) {
                    return onClaimsJwt((Jwt<Header, Claims>) jwt);
                }

                @Override // io.jsonwebtoken.JwtHandlerAdapter, io.jsonwebtoken.JwtHandler
                public Jwt<Header, Claims> onClaimsJwt(Jwt<Header, Claims> jwt) {
                    return jwt;
                }
            });
        } catch (IllegalArgumentException iae) {
            throw new UnsupportedJwtException("Signed JWSs are not supported.", iae);
        }
    }

    @Override // io.jsonwebtoken.JwtParser
    public Jws<String> parsePlaintextJws(String plaintextJws) {
        try {
            return (Jws) parse(plaintextJws, new JwtHandlerAdapter<Jws<String>>() { // from class: io.jsonwebtoken.impl.DefaultJwtParser.3
                @Override // io.jsonwebtoken.JwtHandlerAdapter, io.jsonwebtoken.JwtHandler
                public /* bridge */ /* synthetic */ Object onPlaintextJws(Jws jws) {
                    return onPlaintextJws((Jws<String>) jws);
                }

                @Override // io.jsonwebtoken.JwtHandlerAdapter, io.jsonwebtoken.JwtHandler
                public Jws<String> onPlaintextJws(Jws<String> jws) {
                    return jws;
                }
            });
        } catch (IllegalArgumentException iae) {
            throw new UnsupportedJwtException("Signed JWSs are not supported.", iae);
        }
    }

    @Override // io.jsonwebtoken.JwtParser
    public Jws<Claims> parseClaimsJws(String claimsJws) {
        return (Jws) parse(claimsJws, new JwtHandlerAdapter<Jws<Claims>>() { // from class: io.jsonwebtoken.impl.DefaultJwtParser.4
            @Override // io.jsonwebtoken.JwtHandlerAdapter, io.jsonwebtoken.JwtHandler
            public /* bridge */ /* synthetic */ Object onClaimsJws(Jws jws) {
                return onClaimsJws((Jws<Claims>) jws);
            }

            @Override // io.jsonwebtoken.JwtHandlerAdapter, io.jsonwebtoken.JwtHandler
            public Jws<Claims> onClaimsJws(Jws<Claims> jws) {
                return jws;
            }
        });
    }

    protected Map<String, Object> readValue(String val) {
        try {
            return (Map) this.objectMapper.readValue(val, Map.class);
        } catch (IOException e) {
            throw new MalformedJwtException("Unable to read JSON value: " + val, e);
        }
    }
}
