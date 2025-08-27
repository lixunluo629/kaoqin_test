package org.apache.tomcat;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/JarScanFilter.class */
public interface JarScanFilter {
    boolean check(JarScanType jarScanType, String str);
}
