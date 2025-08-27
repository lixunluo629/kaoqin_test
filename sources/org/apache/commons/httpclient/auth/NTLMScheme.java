package org.apache.commons.httpclient.auth;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/auth/NTLMScheme.class */
public class NTLMScheme implements AuthScheme {
    private static final Log LOG;
    private String ntlmchallenge;
    private static final int UNINITIATED = 0;
    private static final int INITIATED = 1;
    private static final int TYPE1_MSG_GENERATED = 2;
    private static final int TYPE2_MSG_RECEIVED = 3;
    private static final int TYPE3_MSG_GENERATED = 4;
    private static final int FAILED = Integer.MAX_VALUE;
    private int state;
    static Class class$org$apache$commons$httpclient$auth$NTLMScheme;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$auth$NTLMScheme == null) {
            clsClass$ = class$("org.apache.commons.httpclient.auth.NTLMScheme");
            class$org$apache$commons$httpclient$auth$NTLMScheme = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$auth$NTLMScheme;
        }
        LOG = LogFactory.getLog(clsClass$);
    }

    public NTLMScheme() {
        this.ntlmchallenge = null;
        this.state = 0;
    }

    public NTLMScheme(String challenge) throws MalformedChallengeException {
        this.ntlmchallenge = null;
        processChallenge(challenge);
    }

    @Override // org.apache.commons.httpclient.auth.AuthScheme
    public void processChallenge(String challenge) throws MalformedChallengeException {
        String s = AuthChallengeParser.extractScheme(challenge);
        if (!s.equalsIgnoreCase(getSchemeName())) {
            throw new MalformedChallengeException(new StringBuffer().append("Invalid NTLM challenge: ").append(challenge).toString());
        }
        int i = challenge.indexOf(32);
        if (i != -1) {
            String s2 = challenge.substring(i, challenge.length());
            this.ntlmchallenge = s2.trim();
            this.state = 3;
        } else {
            this.ntlmchallenge = "";
            if (this.state == 0) {
                this.state = 1;
            } else {
                this.state = Integer.MAX_VALUE;
            }
        }
    }

    @Override // org.apache.commons.httpclient.auth.AuthScheme
    public boolean isComplete() {
        return this.state == 4 || this.state == Integer.MAX_VALUE;
    }

    @Override // org.apache.commons.httpclient.auth.AuthScheme
    public String getSchemeName() {
        return "ntlm";
    }

    @Override // org.apache.commons.httpclient.auth.AuthScheme
    public String getRealm() {
        return null;
    }

    @Override // org.apache.commons.httpclient.auth.AuthScheme
    public String getID() {
        return this.ntlmchallenge;
    }

    @Override // org.apache.commons.httpclient.auth.AuthScheme
    public String getParameter(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Parameter name may not be null");
        }
        return null;
    }

    @Override // org.apache.commons.httpclient.auth.AuthScheme
    public boolean isConnectionBased() {
        return true;
    }

    public static String authenticate(NTCredentials credentials, String challenge) throws AuthenticationException {
        LOG.trace("enter NTLMScheme.authenticate(NTCredentials, String)");
        if (credentials == null) {
            throw new IllegalArgumentException("Credentials may not be null");
        }
        NTLM ntlm = new NTLM();
        String s = ntlm.getResponseFor(challenge, credentials.getUserName(), credentials.getPassword(), credentials.getHost(), credentials.getDomain());
        return new StringBuffer().append("NTLM ").append(s).toString();
    }

    public static String authenticate(NTCredentials credentials, String challenge, String charset) throws AuthenticationException {
        LOG.trace("enter NTLMScheme.authenticate(NTCredentials, String)");
        if (credentials == null) {
            throw new IllegalArgumentException("Credentials may not be null");
        }
        NTLM ntlm = new NTLM();
        ntlm.setCredentialCharset(charset);
        String s = ntlm.getResponseFor(challenge, credentials.getUserName(), credentials.getPassword(), credentials.getHost(), credentials.getDomain());
        return new StringBuffer().append("NTLM ").append(s).toString();
    }

    @Override // org.apache.commons.httpclient.auth.AuthScheme
    public String authenticate(Credentials credentials, String method, String uri) throws AuthenticationException {
        LOG.trace("enter NTLMScheme.authenticate(Credentials, String, String)");
        try {
            NTCredentials ntcredentials = (NTCredentials) credentials;
            return authenticate(ntcredentials, this.ntlmchallenge);
        } catch (ClassCastException e) {
            throw new InvalidCredentialsException(new StringBuffer().append("Credentials cannot be used for NTLM authentication: ").append(credentials.getClass().getName()).toString());
        }
    }

    @Override // org.apache.commons.httpclient.auth.AuthScheme
    public String authenticate(Credentials credentials, HttpMethod method) throws AuthenticationException {
        String response;
        LOG.trace("enter NTLMScheme.authenticate(Credentials, HttpMethod)");
        if (this.state == 0) {
            throw new IllegalStateException("NTLM authentication process has not been initiated");
        }
        try {
            NTCredentials ntcredentials = (NTCredentials) credentials;
            NTLM ntlm = new NTLM();
            ntlm.setCredentialCharset(method.getParams().getCredentialCharset());
            if (this.state == 1 || this.state == Integer.MAX_VALUE) {
                response = ntlm.getType1Message(ntcredentials.getHost(), ntcredentials.getDomain());
                this.state = 2;
            } else {
                response = ntlm.getType3Message(ntcredentials.getUserName(), ntcredentials.getPassword(), ntcredentials.getHost(), ntcredentials.getDomain(), ntlm.parseType2Message(this.ntlmchallenge));
                this.state = 4;
            }
            return new StringBuffer().append("NTLM ").append(response).toString();
        } catch (ClassCastException e) {
            throw new InvalidCredentialsException(new StringBuffer().append("Credentials cannot be used for NTLM authentication: ").append(credentials.getClass().getName()).toString());
        }
    }
}
