package org.apache.catalina.servlet4preview.http;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/servlet4preview/http/HttpServletRequest.class */
public interface HttpServletRequest extends javax.servlet.http.HttpServletRequest {
    ServletMapping getServletMapping();

    PushBuilder newPushBuilder();
}
