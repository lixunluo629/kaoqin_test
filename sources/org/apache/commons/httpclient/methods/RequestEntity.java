package org.apache.commons.httpclient.methods;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/methods/RequestEntity.class */
public interface RequestEntity {
    boolean isRepeatable();

    void writeRequest(OutputStream outputStream) throws IOException;

    long getContentLength();

    String getContentType();
}
