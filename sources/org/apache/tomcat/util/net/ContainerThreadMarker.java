package org.apache.tomcat.util.net;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/net/ContainerThreadMarker.class */
public class ContainerThreadMarker {
    private static final ThreadLocal<Boolean> marker = new ThreadLocal<>();

    public static boolean isContainerThread() {
        Boolean flag = marker.get();
        if (flag == null) {
            return false;
        }
        return flag.booleanValue();
    }

    public static void set() {
        marker.set(Boolean.TRUE);
    }

    public static void clear() {
        marker.set(Boolean.FALSE);
    }
}
