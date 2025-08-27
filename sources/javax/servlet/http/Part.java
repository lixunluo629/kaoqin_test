package javax.servlet.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/http/Part.class */
public interface Part {
    InputStream getInputStream() throws IOException;

    String getContentType();

    String getName();

    String getSubmittedFileName();

    long getSize();

    void write(String str) throws IOException;

    void delete() throws IOException;

    String getHeader(String str);

    Collection<String> getHeaders(String str);

    Collection<String> getHeaderNames();
}
