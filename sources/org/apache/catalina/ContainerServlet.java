package org.apache.catalina;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/ContainerServlet.class */
public interface ContainerServlet {
    Wrapper getWrapper();

    void setWrapper(Wrapper wrapper);
}
