package org.apache.catalina.webresources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResource;
import org.apache.catalina.WebResourceRoot;
import org.apache.tomcat.util.buf.UriUtil;
import org.apache.tomcat.util.compat.JreCompat;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/webresources/JarWarResourceSet.class */
public class JarWarResourceSet extends AbstractArchiveResourceSet {
    private final String archivePath;

    public JarWarResourceSet(WebResourceRoot root, String webAppMount, String base, String archivePath, String internalPath) throws IllegalArgumentException {
        setRoot(root);
        setWebAppMount(webAppMount);
        setBase(base);
        this.archivePath = archivePath;
        setInternalPath(internalPath);
        if (getRoot().getState().isAvailable()) {
            try {
                start();
            } catch (LifecycleException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    @Override // org.apache.catalina.webresources.AbstractArchiveResourceSet
    protected WebResource createArchiveResource(JarEntry jarEntry, String webAppPath, Manifest manifest) {
        return new JarWarResource(this, webAppPath, getBaseUrlString(), jarEntry, this.archivePath);
    }

    /* JADX WARN: Finally extract failed */
    @Override // org.apache.catalina.webresources.AbstractArchiveResourceSet
    protected HashMap<String, JarEntry> getArchiveEntries(boolean single) {
        HashMap<String, JarEntry> map;
        String value;
        synchronized (this.archiveLock) {
            if (this.archiveEntries == null) {
                JarFile warFile = null;
                InputStream jarFileIs = null;
                this.archiveEntries = new HashMap<>();
                boolean multiRelease = false;
                try {
                    try {
                        warFile = openJarFile();
                        JarEntry jarFileInWar = warFile.getJarEntry(this.archivePath);
                        jarFileIs = warFile.getInputStream(jarFileInWar);
                        TomcatJarInputStream jarIs = new TomcatJarInputStream(jarFileIs);
                        Throwable th = null;
                        try {
                            for (JarEntry entry = jarIs.getNextJarEntry(); entry != null; entry = jarIs.getNextJarEntry()) {
                                this.archiveEntries.put(entry.getName(), entry);
                            }
                            Manifest m = jarIs.getManifest();
                            setManifest(m);
                            if (m != null && JreCompat.isJre9Available() && (value = m.getMainAttributes().getValue("Multi-Release")) != null) {
                                multiRelease = Boolean.parseBoolean(value);
                            }
                            JarEntry entry2 = jarIs.getMetaInfEntry();
                            if (entry2 != null) {
                                this.archiveEntries.put(entry2.getName(), entry2);
                            }
                            JarEntry entry3 = jarIs.getManifestEntry();
                            if (entry3 != null) {
                                this.archiveEntries.put(entry3.getName(), entry3);
                            }
                            if (jarIs != null) {
                                if (0 != 0) {
                                    try {
                                        jarIs.close();
                                    } catch (Throwable x2) {
                                        th.addSuppressed(x2);
                                    }
                                } else {
                                    jarIs.close();
                                }
                            }
                            if (multiRelease) {
                                processArchivesEntriesForMultiRelease();
                            }
                        } catch (Throwable th2) {
                            if (jarIs != null) {
                                if (0 != 0) {
                                    try {
                                        jarIs.close();
                                    } catch (Throwable x22) {
                                        th.addSuppressed(x22);
                                    }
                                } else {
                                    jarIs.close();
                                }
                            }
                            throw th2;
                        }
                    } catch (IOException ioe) {
                        this.archiveEntries = null;
                        throw new IllegalStateException(ioe);
                    }
                } finally {
                    if (warFile != null) {
                        closeJarFile();
                    }
                    if (jarFileIs != null) {
                        try {
                            jarFileIs.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
            map = this.archiveEntries;
        }
        return map;
    }

    protected void processArchivesEntriesForMultiRelease() throws NumberFormatException {
        int targetVersion = JreCompat.getInstance().jarFileRuntimeMajorVersion();
        Map<String, VersionedJarEntry> versionedEntries = new HashMap<>();
        Iterator<Map.Entry<String, JarEntry>> iter = this.archiveEntries.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, JarEntry> entry = iter.next();
            String name = entry.getKey();
            if (name.startsWith("META-INF/versions/")) {
                iter.remove();
                int i = name.indexOf(47, 18);
                if (i > 0) {
                    String baseName = name.substring(i + 1);
                    int version = Integer.parseInt(name.substring(18, i));
                    if (version <= targetVersion) {
                        VersionedJarEntry versionedJarEntry = versionedEntries.get(baseName);
                        if (versionedJarEntry == null) {
                            versionedEntries.put(baseName, new VersionedJarEntry(version, entry.getValue()));
                        } else if (version > versionedJarEntry.getVersion()) {
                            versionedEntries.put(baseName, new VersionedJarEntry(version, entry.getValue()));
                        }
                    }
                }
            }
        }
        for (Map.Entry<String, VersionedJarEntry> versionedJarEntry2 : versionedEntries.entrySet()) {
            this.archiveEntries.put(versionedJarEntry2.getKey(), versionedJarEntry2.getValue().getJarEntry());
        }
    }

    @Override // org.apache.catalina.webresources.AbstractArchiveResourceSet
    protected JarEntry getArchiveEntry(String pathInArchive) {
        throw new IllegalStateException("Coding error");
    }

    @Override // org.apache.catalina.webresources.AbstractArchiveResourceSet
    protected boolean isMultiRelease() {
        return false;
    }

    /* JADX WARN: Finally extract failed */
    @Override // org.apache.catalina.util.LifecycleBase
    protected void initInternal() throws LifecycleException {
        try {
            JarFile warFile = new JarFile(getBase());
            Throwable th = null;
            try {
                JarEntry jarFileInWar = warFile.getJarEntry(this.archivePath);
                InputStream jarFileIs = warFile.getInputStream(jarFileInWar);
                JarInputStream jarIs = new JarInputStream(jarFileIs);
                Throwable th2 = null;
                try {
                    try {
                        setManifest(jarIs.getManifest());
                        if (jarIs != null) {
                            if (0 != 0) {
                                try {
                                    jarIs.close();
                                } catch (Throwable x2) {
                                    th2.addSuppressed(x2);
                                }
                            } else {
                                jarIs.close();
                            }
                        }
                        if (warFile != null) {
                            if (0 != 0) {
                                try {
                                    warFile.close();
                                } catch (Throwable x22) {
                                    th.addSuppressed(x22);
                                }
                            } else {
                                warFile.close();
                            }
                        }
                        try {
                            setBaseUrl(UriUtil.buildJarSafeUrl(new File(getBase())));
                        } catch (MalformedURLException e) {
                            throw new IllegalArgumentException(e);
                        }
                    } catch (Throwable th3) {
                        if (jarIs != null) {
                            if (th2 != null) {
                                try {
                                    jarIs.close();
                                } catch (Throwable x23) {
                                    th2.addSuppressed(x23);
                                }
                            } else {
                                jarIs.close();
                            }
                        }
                        throw th3;
                    }
                } finally {
                }
            } catch (Throwable th4) {
                if (warFile != null) {
                    if (0 != 0) {
                        try {
                            warFile.close();
                        } catch (Throwable x24) {
                            th.addSuppressed(x24);
                        }
                    } else {
                        warFile.close();
                    }
                }
                throw th4;
            }
        } catch (IOException ioe) {
            throw new IllegalArgumentException(ioe);
        }
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/webresources/JarWarResourceSet$VersionedJarEntry.class */
    private static final class VersionedJarEntry {
        private final int version;
        private final JarEntry jarEntry;

        public VersionedJarEntry(int version, JarEntry jarEntry) {
            this.version = version;
            this.jarEntry = jarEntry;
        }

        public int getVersion() {
            return this.version;
        }

        public JarEntry getJarEntry() {
            return this.jarEntry;
        }
    }
}
