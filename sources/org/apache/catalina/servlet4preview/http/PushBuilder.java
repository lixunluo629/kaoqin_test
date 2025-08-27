package org.apache.catalina.servlet4preview.http;

import java.util.Set;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/servlet4preview/http/PushBuilder.class */
public interface PushBuilder {
    PushBuilder method(String str);

    PushBuilder queryString(String str);

    PushBuilder sessionId(String str);

    PushBuilder setHeader(String str, String str2);

    PushBuilder addHeader(String str, String str2);

    PushBuilder removeHeader(String str);

    PushBuilder path(String str);

    void push();

    String getMethod();

    String getQueryString();

    String getSessionId();

    Set<String> getHeaderNames();

    String getHeader(String str);

    String getPath();
}
