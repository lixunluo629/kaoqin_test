package io.jsonwebtoken;

import java.security.Key;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/SigningKeyResolver.class */
public interface SigningKeyResolver {
    Key resolveSigningKey(JwsHeader jwsHeader, Claims claims);

    Key resolveSigningKey(JwsHeader jwsHeader, String str);
}
