package org.apache.tomcat;

import java.io.File;
import java.io.IOException;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/JarScannerCallback.class */
public interface JarScannerCallback {
    void scan(Jar jar, String str, boolean z) throws IOException;

    void scan(File file, String str, boolean z) throws IOException;

    void scanWebInfClasses() throws IOException;
}
