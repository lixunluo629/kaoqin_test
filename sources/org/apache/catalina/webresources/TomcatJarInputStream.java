package org.apache.catalina.webresources;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import net.dongliu.apk.parser.struct.AndroidConstants;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/webresources/TomcatJarInputStream.class */
public class TomcatJarInputStream extends JarInputStream {
    private JarEntry metaInfEntry;
    private JarEntry manifestEntry;

    TomcatJarInputStream(InputStream in) throws IOException {
        super(in);
    }

    @Override // java.util.jar.JarInputStream, java.util.zip.ZipInputStream
    protected ZipEntry createZipEntry(String name) {
        ZipEntry ze = super.createZipEntry(name);
        if (this.metaInfEntry == null && AndroidConstants.META_PREFIX.equals(name)) {
            this.metaInfEntry = (JarEntry) ze;
        } else if (this.manifestEntry == null && "META-INF/MANIFESR.MF".equals(name)) {
            this.manifestEntry = (JarEntry) ze;
        }
        return ze;
    }

    JarEntry getMetaInfEntry() {
        return this.metaInfEntry;
    }

    JarEntry getManifestEntry() {
        return this.manifestEntry;
    }
}
