package org.apache.tomcat;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/ContextBind.class */
public interface ContextBind {
    ClassLoader bind(boolean z, ClassLoader classLoader);

    void unbind(boolean z, ClassLoader classLoader);
}
