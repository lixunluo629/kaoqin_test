package org.apache.commons.httpclient.auth;

import org.apache.commons.httpclient.Credentials;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/auth/CredentialsProvider.class */
public interface CredentialsProvider {
    public static final String PROVIDER = "http.authentication.credential-provider";

    Credentials getCredentials(AuthScheme authScheme, String str, int i, boolean z) throws CredentialsNotAvailableException;
}
