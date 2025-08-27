package org.apache.coyote;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/ContainerThreadMarker.class */
public class ContainerThreadMarker {
    public static boolean isContainerThread() {
        return org.apache.tomcat.util.net.ContainerThreadMarker.isContainerThread();
    }

    public static void set() {
        org.apache.tomcat.util.net.ContainerThreadMarker.set();
    }

    public static void clear() {
        org.apache.tomcat.util.net.ContainerThreadMarker.clear();
    }
}
