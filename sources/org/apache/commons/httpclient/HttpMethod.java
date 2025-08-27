package org.apache.commons.httpclient;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.httpclient.auth.AuthState;
import org.apache.commons.httpclient.params.HttpMethodParams;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/HttpMethod.class */
public interface HttpMethod {
    String getName();

    HostConfiguration getHostConfiguration();

    void setPath(String str);

    String getPath();

    URI getURI() throws URIException;

    void setURI(URI uri) throws URIException;

    void setStrictMode(boolean z);

    boolean isStrictMode();

    void setRequestHeader(String str, String str2);

    void setRequestHeader(Header header);

    void addRequestHeader(String str, String str2);

    void addRequestHeader(Header header);

    Header getRequestHeader(String str);

    void removeRequestHeader(String str);

    void removeRequestHeader(Header header);

    boolean getFollowRedirects();

    void setFollowRedirects(boolean z);

    void setQueryString(String str);

    void setQueryString(NameValuePair[] nameValuePairArr);

    String getQueryString();

    Header[] getRequestHeaders();

    Header[] getRequestHeaders(String str);

    boolean validate();

    int getStatusCode();

    String getStatusText();

    Header[] getResponseHeaders();

    Header getResponseHeader(String str);

    Header[] getResponseHeaders(String str);

    Header[] getResponseFooters();

    Header getResponseFooter(String str);

    byte[] getResponseBody() throws IOException;

    String getResponseBodyAsString() throws IOException;

    InputStream getResponseBodyAsStream() throws IOException;

    boolean hasBeenUsed();

    int execute(HttpState httpState, HttpConnection httpConnection) throws IOException;

    void abort();

    void recycle();

    void releaseConnection();

    void addResponseFooter(Header header);

    StatusLine getStatusLine();

    boolean getDoAuthentication();

    void setDoAuthentication(boolean z);

    HttpMethodParams getParams();

    void setParams(HttpMethodParams httpMethodParams);

    AuthState getHostAuthState();

    AuthState getProxyAuthState();

    boolean isRequestSent();
}
