package org.apache.catalina;

import java.io.InputStream;
import java.net.URL;
import java.util.Set;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/WebResourceSet.class */
public interface WebResourceSet extends Lifecycle {
    WebResource getResource(String str);

    String[] list(String str);

    Set<String> listWebAppPaths(String str);

    boolean mkdir(String str);

    boolean write(String str, InputStream inputStream, boolean z);

    void setRoot(WebResourceRoot webResourceRoot);

    boolean getClassLoaderOnly();

    void setClassLoaderOnly(boolean z);

    boolean getStaticOnly();

    void setStaticOnly(boolean z);

    URL getBaseUrl();

    void setReadOnly(boolean z);

    boolean isReadOnly();

    void gc();
}
