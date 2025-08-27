package org.apache.commons.httpclient.auth;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/auth/HttpAuthRealm.class */
public class HttpAuthRealm extends AuthScope {
    public HttpAuthRealm(String domain, String realm) {
        super(domain, -1, realm, ANY_SCHEME);
    }
}
