package org.apache.tomcat.jni;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/jni/Global.class */
public class Global {
    public static native long create(String str, int i, long j) throws Error;

    public static native long childInit(String str, long j) throws Error;

    public static native int lock(long j);

    public static native int trylock(long j);

    public static native int unlock(long j);

    public static native int destroy(long j);
}
