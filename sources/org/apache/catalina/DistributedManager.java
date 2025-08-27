package org.apache.catalina;

import java.util.Set;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/DistributedManager.class */
public interface DistributedManager {
    int getActiveSessionsFull();

    Set<String> getSessionIdsFull();
}
