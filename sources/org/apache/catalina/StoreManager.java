package org.apache.catalina;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/StoreManager.class */
public interface StoreManager extends DistributedManager {
    Store getStore();

    void removeSuper(Session session);
}
