package org.apache.tomcat.util.compat;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.util.Deque;
import java.util.jar.JarFile;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import org.apache.tomcat.util.res.StringManager;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/compat/JreCompat.class */
public class JreCompat {
    private static final int RUNTIME_MAJOR_VERSION = 7;
    private static final JreCompat instance;
    private static StringManager sm = StringManager.getManager(JreCompat.class.getPackage().getName());
    private static final boolean jre9Available;
    private static final boolean jre8Available;

    static {
        if (Jre9Compat.isSupported()) {
            instance = new Jre9Compat();
            jre9Available = true;
            jre8Available = true;
        } else if (Jre8Compat.isSupported()) {
            instance = new Jre8Compat();
            jre9Available = false;
            jre8Available = true;
        } else {
            instance = new JreCompat();
            jre9Available = false;
            jre8Available = false;
        }
    }

    public static JreCompat getInstance() {
        return instance;
    }

    public static boolean isJre8Available() {
        return jre8Available;
    }

    public void setUseServerCipherSuitesOrder(SSLEngine engine, boolean useCipherSuitesOrder) {
        throw new UnsupportedOperationException(sm.getString("jreCompat.noServerCipherSuiteOrder"));
    }

    public KeyStore.LoadStoreParameter getDomainLoadStoreParameter(URI uri) {
        throw new UnsupportedOperationException(sm.getString("jreCompat.noDomainLoadStoreParameter"));
    }

    public static boolean isJre9Available() {
        return jre9Available;
    }

    public boolean isInstanceOfInaccessibleObjectException(Throwable t) {
        return false;
    }

    public void setApplicationProtocols(SSLParameters sslParameters, String[] protocols) {
        throw new UnsupportedOperationException(sm.getString("jreCompat.noApplicationProtocols"));
    }

    public String getApplicationProtocol(SSLEngine sslEngine) {
        throw new UnsupportedOperationException(sm.getString("jreCompat.noApplicationProtocol"));
    }

    public void disableCachingForJarUrlConnections() throws IOException {
        URL url = new URL("jar:file://dummy.jar!/");
        URLConnection uConn = url.openConnection();
        uConn.setDefaultUseCaches(false);
    }

    public void addBootModulePath(Deque<URL> classPathUrlsToProcess) {
    }

    public final JarFile jarFileNewInstance(String s) throws IOException {
        return jarFileNewInstance(new File(s));
    }

    public JarFile jarFileNewInstance(File f) throws IOException {
        return new JarFile(f);
    }

    public boolean jarFileIsMultiRelease(JarFile jarFile) {
        return false;
    }

    public int jarFileRuntimeMajorVersion() {
        return 7;
    }
}
