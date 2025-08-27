package org.apache.tomcat.jni;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/jni/Mmap.class */
public class Mmap {
    public static final int APR_MMAP_READ = 1;
    public static final int APR_MMAP_WRITE = 2;

    public static native long create(long j, long j2, long j3, int i, long j4) throws Error;

    public static native long dup(long j, long j2) throws Error;

    public static native int delete(long j);

    public static native long offset(long j, long j2) throws Error;
}
