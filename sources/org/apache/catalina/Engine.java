package org.apache.catalina;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/Engine.class */
public interface Engine extends Container {
    String getDefaultHost();

    void setDefaultHost(String str);

    String getJvmRoute();

    void setJvmRoute(String str);

    Service getService();

    void setService(Service service);
}
