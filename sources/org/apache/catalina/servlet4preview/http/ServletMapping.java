package org.apache.catalina.servlet4preview.http;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/servlet4preview/http/ServletMapping.class */
public interface ServletMapping {
    String getMatchValue();

    String getPattern();

    MappingMatch getMappingMatch();

    String getServletName();
}
