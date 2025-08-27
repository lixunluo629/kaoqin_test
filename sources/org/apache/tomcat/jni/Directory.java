package org.apache.tomcat.jni;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/jni/Directory.class */
public class Directory {
    public static native int make(String str, int i, long j);

    public static native int makeRecursive(String str, int i, long j);

    public static native int remove(String str, long j);

    public static native String tempGet(long j);

    public static native long open(String str, long j) throws Error;

    public static native int close(long j);

    public static native int rewind(long j);

    public static native int read(FileInfo fileInfo, int i, long j);
}
