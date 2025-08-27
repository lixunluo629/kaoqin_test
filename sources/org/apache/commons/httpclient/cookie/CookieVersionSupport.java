package org.apache.commons.httpclient.cookie;

import org.apache.commons.httpclient.Header;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/cookie/CookieVersionSupport.class */
public interface CookieVersionSupport {
    int getVersion();

    Header getVersionHeader();
}
