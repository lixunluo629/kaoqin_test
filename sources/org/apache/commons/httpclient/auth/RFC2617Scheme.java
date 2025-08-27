package org.apache.commons.httpclient.auth;

import java.util.Map;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/auth/RFC2617Scheme.class */
public abstract class RFC2617Scheme implements AuthScheme {
    private Map params = null;

    public RFC2617Scheme() {
    }

    public RFC2617Scheme(String challenge) throws MalformedChallengeException {
        processChallenge(challenge);
    }

    @Override // org.apache.commons.httpclient.auth.AuthScheme
    public void processChallenge(String challenge) throws MalformedChallengeException {
        String s = AuthChallengeParser.extractScheme(challenge);
        if (!s.equalsIgnoreCase(getSchemeName())) {
            throw new MalformedChallengeException(new StringBuffer().append("Invalid ").append(getSchemeName()).append(" challenge: ").append(challenge).toString());
        }
        this.params = AuthChallengeParser.extractParams(challenge);
    }

    protected Map getParameters() {
        return this.params;
    }

    @Override // org.apache.commons.httpclient.auth.AuthScheme
    public String getParameter(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Parameter name may not be null");
        }
        if (this.params == null) {
            return null;
        }
        return (String) this.params.get(name.toLowerCase());
    }

    @Override // org.apache.commons.httpclient.auth.AuthScheme
    public String getRealm() {
        return getParameter("realm");
    }

    @Override // org.apache.commons.httpclient.auth.AuthScheme
    public String getID() {
        return getRealm();
    }
}
