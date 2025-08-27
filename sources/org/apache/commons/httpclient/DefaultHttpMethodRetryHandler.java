package org.apache.commons.httpclient;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.NoRouteToHostException;
import java.net.UnknownHostException;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/DefaultHttpMethodRetryHandler.class */
public class DefaultHttpMethodRetryHandler implements HttpMethodRetryHandler {
    private static Class SSL_HANDSHAKE_EXCEPTION;
    private int retryCount;
    private boolean requestSentRetryEnabled;

    static {
        SSL_HANDSHAKE_EXCEPTION = null;
        try {
            SSL_HANDSHAKE_EXCEPTION = Class.forName("javax.net.ssl.SSLHandshakeException");
        } catch (ClassNotFoundException e) {
        }
    }

    public DefaultHttpMethodRetryHandler(int retryCount, boolean requestSentRetryEnabled) {
        this.retryCount = retryCount;
        this.requestSentRetryEnabled = requestSentRetryEnabled;
    }

    public DefaultHttpMethodRetryHandler() {
        this(3, false);
    }

    @Override // org.apache.commons.httpclient.HttpMethodRetryHandler
    public boolean retryMethod(HttpMethod method, IOException exception, int executionCount) {
        if (method == null) {
            throw new IllegalArgumentException("HTTP method may not be null");
        }
        if (exception == null) {
            throw new IllegalArgumentException("Exception parameter may not be null");
        }
        if (((method instanceof HttpMethodBase) && ((HttpMethodBase) method).isAborted()) || executionCount > this.retryCount) {
            return false;
        }
        if (exception instanceof NoHttpResponseException) {
            return true;
        }
        if ((exception instanceof InterruptedIOException) || (exception instanceof UnknownHostException) || (exception instanceof NoRouteToHostException)) {
            return false;
        }
        if (SSL_HANDSHAKE_EXCEPTION != null && SSL_HANDSHAKE_EXCEPTION.isInstance(exception)) {
            return false;
        }
        if (!method.isRequestSent() || this.requestSentRetryEnabled) {
            return true;
        }
        return false;
    }

    public boolean isRequestSentRetryEnabled() {
        return this.requestSentRetryEnabled;
    }

    public int getRetryCount() {
        return this.retryCount;
    }
}
