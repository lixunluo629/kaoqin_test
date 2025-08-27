package org.apache.tomcat.jni;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/jni/BIOCallback.class */
public interface BIOCallback {
    int write(byte[] bArr);

    int read(byte[] bArr);

    int puts(String str);

    String gets(int i);
}
