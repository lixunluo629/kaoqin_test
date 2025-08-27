package org.apache.tomcat.jni;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/jni/Multicast.class */
public class Multicast {
    public static native int join(long j, long j2, long j3, long j4);

    public static native int leave(long j, long j2, long j3, long j4);

    public static native int hops(long j, int i);

    public static native int loopback(long j, boolean z);

    public static native int ointerface(long j, long j2);
}
