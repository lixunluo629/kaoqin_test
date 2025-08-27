package org.apache.tomcat.util.http.fileupload;

import java.util.Iterator;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/http/fileupload/FileItemHeaders.class */
public interface FileItemHeaders {
    String getHeader(String str);

    Iterator<String> getHeaders(String str);

    Iterator<String> getHeaderNames();
}
