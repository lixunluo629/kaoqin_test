package io.jsonwebtoken;

import io.jsonwebtoken.ClaimsMutator;
import java.util.Date;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/ClaimsMutator.class */
public interface ClaimsMutator<T extends ClaimsMutator> {
    T setIssuer(String str);

    T setSubject(String str);

    T setAudience(String str);

    T setExpiration(Date date);

    T setNotBefore(Date date);

    T setIssuedAt(Date date);

    T setId(String str);
}
