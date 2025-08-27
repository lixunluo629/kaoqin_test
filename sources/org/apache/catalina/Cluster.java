package org.apache.catalina;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/Cluster.class */
public interface Cluster {
    String getClusterName();

    void setClusterName(String str);

    void setContainer(Container container);

    Container getContainer();

    Manager createManager(String str);

    void registerManager(Manager manager);

    void removeManager(Manager manager);

    void backgroundProcess();
}
