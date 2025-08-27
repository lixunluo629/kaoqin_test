package org.apache.commons.httpclient.auth;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/auth/AuthSchemeBase.class */
public abstract class AuthSchemeBase implements AuthScheme {
    private String challenge;

    public AuthSchemeBase(String challenge) throws MalformedChallengeException {
        this.challenge = null;
        if (challenge == null) {
            throw new IllegalArgumentException("Challenge may not be null");
        }
        this.challenge = challenge;
    }

    public boolean equals(Object obj) {
        if (obj instanceof AuthSchemeBase) {
            return this.challenge.equals(((AuthSchemeBase) obj).challenge);
        }
        return super.equals(obj);
    }

    public int hashCode() {
        return this.challenge.hashCode();
    }

    public String toString() {
        return this.challenge;
    }
}
