package org.apache.commons.httpclient.auth;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/auth/BasicScheme.class */
public class BasicScheme extends RFC2617Scheme {
    private static final Log LOG;
    private boolean complete;
    static Class class$org$apache$commons$httpclient$auth$BasicScheme;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$auth$BasicScheme == null) {
            clsClass$ = class$("org.apache.commons.httpclient.auth.BasicScheme");
            class$org$apache$commons$httpclient$auth$BasicScheme = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$auth$BasicScheme;
        }
        LOG = LogFactory.getLog(clsClass$);
    }

    public BasicScheme() {
        this.complete = false;
    }

    public BasicScheme(String challenge) throws MalformedChallengeException {
        super(challenge);
        this.complete = true;
    }

    @Override // org.apache.commons.httpclient.auth.AuthScheme
    public String getSchemeName() {
        return "basic";
    }

    @Override // org.apache.commons.httpclient.auth.RFC2617Scheme, org.apache.commons.httpclient.auth.AuthScheme
    public void processChallenge(String challenge) throws MalformedChallengeException {
        super.processChallenge(challenge);
        this.complete = true;
    }

    @Override // org.apache.commons.httpclient.auth.AuthScheme
    public boolean isComplete() {
        return this.complete;
    }

    @Override // org.apache.commons.httpclient.auth.AuthScheme
    public String authenticate(Credentials credentials, String method, String uri) throws AuthenticationException {
        LOG.trace("enter BasicScheme.authenticate(Credentials, String, String)");
        try {
            UsernamePasswordCredentials usernamepassword = (UsernamePasswordCredentials) credentials;
            return authenticate(usernamepassword);
        } catch (ClassCastException e) {
            throw new InvalidCredentialsException(new StringBuffer().append("Credentials cannot be used for basic authentication: ").append(credentials.getClass().getName()).toString());
        }
    }

    @Override // org.apache.commons.httpclient.auth.AuthScheme
    public boolean isConnectionBased() {
        return false;
    }

    @Override // org.apache.commons.httpclient.auth.AuthScheme
    public String authenticate(Credentials credentials, HttpMethod method) throws AuthenticationException {
        LOG.trace("enter BasicScheme.authenticate(Credentials, HttpMethod)");
        if (method == null) {
            throw new IllegalArgumentException("Method may not be null");
        }
        try {
            UsernamePasswordCredentials usernamepassword = (UsernamePasswordCredentials) credentials;
            return authenticate(usernamepassword, method.getParams().getCredentialCharset());
        } catch (ClassCastException e) {
            throw new InvalidCredentialsException(new StringBuffer().append("Credentials cannot be used for basic authentication: ").append(credentials.getClass().getName()).toString());
        }
    }

    public static String authenticate(UsernamePasswordCredentials credentials) {
        return authenticate(credentials, "ISO-8859-1");
    }

    public static String authenticate(UsernamePasswordCredentials credentials, String charset) {
        LOG.trace("enter BasicScheme.authenticate(UsernamePasswordCredentials, String)");
        if (credentials == null) {
            throw new IllegalArgumentException("Credentials may not be null");
        }
        if (charset == null || charset.length() == 0) {
            throw new IllegalArgumentException("charset may not be null or empty");
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append(credentials.getUserName());
        buffer.append(":");
        buffer.append(credentials.getPassword());
        return new StringBuffer().append("Basic ").append(EncodingUtil.getAsciiString(Base64.encodeBase64(EncodingUtil.getBytes(buffer.toString(), charset)))).toString();
    }
}
