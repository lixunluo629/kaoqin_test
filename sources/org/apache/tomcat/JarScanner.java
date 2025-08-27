package org.apache.tomcat;

import javax.servlet.ServletContext;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/JarScanner.class */
public interface JarScanner {
    void scan(JarScanType jarScanType, ServletContext servletContext, JarScannerCallback jarScannerCallback);

    JarScanFilter getJarScanFilter();

    void setJarScanFilter(JarScanFilter jarScanFilter);
}
