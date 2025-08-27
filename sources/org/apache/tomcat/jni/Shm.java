package org.apache.tomcat.jni;

import java.nio.ByteBuffer;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/jni/Shm.class */
public class Shm {
    public static native long create(long j, String str, long j2) throws Error;

    public static native int remove(String str, long j);

    public static native int destroy(long j);

    public static native long attach(String str, long j) throws Error;

    public static native int detach(long j);

    public static native long baseaddr(long j);

    public static native long size(long j);

    public static native ByteBuffer buffer(long j);
}
