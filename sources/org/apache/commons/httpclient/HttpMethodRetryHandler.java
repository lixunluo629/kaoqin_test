package org.apache.commons.httpclient;

import java.io.IOException;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/HttpMethodRetryHandler.class */
public interface HttpMethodRetryHandler {
    boolean retryMethod(HttpMethod httpMethod, IOException iOException, int i);
}
