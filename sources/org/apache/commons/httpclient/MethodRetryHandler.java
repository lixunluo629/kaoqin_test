package org.apache.commons.httpclient;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/MethodRetryHandler.class */
public interface MethodRetryHandler {
    boolean retryMethod(HttpMethod httpMethod, HttpConnection httpConnection, HttpRecoverableException httpRecoverableException, int i, boolean z);
}
