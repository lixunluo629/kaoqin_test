package org.apache.tomcat.jni;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/jni/CertificateVerifier.class */
public interface CertificateVerifier {
    boolean verify(long j, byte[][] bArr, String str);
}
