package org.apache.commons.httpclient.methods.multipart;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/methods/multipart/PartSource.class */
public interface PartSource {
    long getLength();

    String getFileName();

    InputStream createInputStream() throws IOException;
}
