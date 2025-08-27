package org.apache.commons.httpclient;

import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/HttpConnectionManager.class */
public interface HttpConnectionManager {
    HttpConnection getConnection(HostConfiguration hostConfiguration);

    HttpConnection getConnection(HostConfiguration hostConfiguration, long j) throws HttpException;

    HttpConnection getConnectionWithTimeout(HostConfiguration hostConfiguration, long j) throws ConnectionPoolTimeoutException;

    void releaseConnection(HttpConnection httpConnection);

    void closeIdleConnections(long j);

    HttpConnectionManagerParams getParams();

    void setParams(HttpConnectionManagerParams httpConnectionManagerParams);
}
