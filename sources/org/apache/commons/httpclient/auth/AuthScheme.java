package org.apache.commons.httpclient.auth;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpMethod;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/auth/AuthScheme.class */
public interface AuthScheme {
    void processChallenge(String str) throws MalformedChallengeException;

    String getSchemeName();

    String getParameter(String str);

    String getRealm();

    String getID();

    boolean isConnectionBased();

    boolean isComplete();

    String authenticate(Credentials credentials, String str, String str2) throws AuthenticationException;

    String authenticate(Credentials credentials, HttpMethod httpMethod) throws AuthenticationException;
}
