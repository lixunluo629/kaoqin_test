package org.apache.catalina.servlet4preview.http;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/servlet4preview/http/HttpServletRequestWrapper.class */
public class HttpServletRequestWrapper extends javax.servlet.http.HttpServletRequestWrapper implements HttpServletRequest {
    public HttpServletRequestWrapper(javax.servlet.http.HttpServletRequest request) {
        super(request);
    }

    private HttpServletRequest _getHttpServletRequest() {
        return (HttpServletRequest) super.getRequest();
    }

    public ServletMapping getServletMapping() {
        return _getHttpServletRequest().getServletMapping();
    }

    public PushBuilder newPushBuilder() {
        return _getHttpServletRequest().newPushBuilder();
    }
}
