package org.apache.coyote;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/AsyncContextCallback.class */
public interface AsyncContextCallback {
    void fireOnComplete();

    boolean isAvailable();
}
