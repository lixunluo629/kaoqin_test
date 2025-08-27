package org.apache.tomcat.jni;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/jni/Address.class */
public class Address {
    public static final String APR_ANYADDR = "0.0.0.0";

    public static native boolean fill(Sockaddr sockaddr, long j);

    public static native Sockaddr getInfo(long j);

    public static native long info(String str, int i, int i2, int i3, long j) throws Exception;

    public static native String getnameinfo(long j, int i);

    public static native String getip(long j);

    public static native int getservbyname(long j, String str);

    public static native long get(int i, long j) throws Exception;

    public static native boolean equal(long j, long j2);
}
