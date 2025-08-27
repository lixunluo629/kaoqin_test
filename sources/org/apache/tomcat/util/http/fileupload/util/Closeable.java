package org.apache.tomcat.util.http.fileupload.util;

import java.io.IOException;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/http/fileupload/util/Closeable.class */
public interface Closeable {
    void close() throws IOException;

    boolean isClosed() throws IOException;
}
