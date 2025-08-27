package org.apache.ibatis.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/io/DefaultVFS.class */
public class DefaultVFS extends VFS {
    private static final Log log = LogFactory.getLog((Class<?>) DefaultVFS.class);
    private static final byte[] JAR_MAGIC = {80, 75, 3, 4};

    @Override // org.apache.ibatis.io.VFS
    public boolean isValid() {
        return true;
    }

    @Override // org.apache.ibatis.io.VFS
    public List<String> list(URL url, String path) throws IOException {
        InputStream is = null;
        try {
            List<String> resources = new ArrayList();
            URL jarUrl = findJarForResource(url);
            if (jarUrl != null) {
                is = jarUrl.openStream();
                if (log.isDebugEnabled()) {
                    log.debug("Listing " + url);
                }
                resources = listResources(new JarInputStream(is), path);
            } else {
                List<String> children = new ArrayList<>();
                try {
                    if (isJar(url)) {
                        is = url.openStream();
                        JarInputStream jarInput = new JarInputStream(is);
                        if (log.isDebugEnabled()) {
                            log.debug("Listing " + url);
                        }
                        while (true) {
                            JarEntry entry = jarInput.getNextJarEntry();
                            if (entry == null) {
                                break;
                            }
                            if (log.isDebugEnabled()) {
                                log.debug("Jar entry: " + entry.getName());
                            }
                            children.add(entry.getName());
                        }
                        jarInput.close();
                    } else {
                        is = url.openStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                        List<String> lines = new ArrayList<>();
                        while (true) {
                            String line = reader.readLine();
                            if (line == null) {
                                break;
                            }
                            if (log.isDebugEnabled()) {
                                log.debug("Reader entry: " + line);
                            }
                            lines.add(line);
                            if (getResources(path + "/" + line).isEmpty()) {
                                lines.clear();
                                break;
                            }
                        }
                        if (!lines.isEmpty()) {
                            if (log.isDebugEnabled()) {
                                log.debug("Listing " + url);
                            }
                            children.addAll(lines);
                        }
                    }
                } catch (FileNotFoundException e) {
                    if ("file".equals(url.getProtocol())) {
                        File file = new File(url.getFile());
                        if (log.isDebugEnabled()) {
                            log.debug("Listing directory " + file.getAbsolutePath());
                        }
                        if (file.isDirectory()) {
                            if (log.isDebugEnabled()) {
                                log.debug("Listing " + url);
                            }
                            children = Arrays.asList(file.list());
                        }
                    } else {
                        throw e;
                    }
                }
                String prefix = url.toExternalForm();
                if (!prefix.endsWith("/")) {
                    prefix = prefix + "/";
                }
                for (String child : children) {
                    String resourcePath = path + "/" + child;
                    resources.add(resourcePath);
                    URL childUrl = new URL(prefix + child);
                    resources.addAll(list(childUrl, resourcePath));
                }
            }
            List<String> list = resources;
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e2) {
                }
            }
            return list;
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    is.close();
                } catch (Exception e3) {
                }
            }
            throw th;
        }
    }

    protected List<String> listResources(JarInputStream jar, String path) throws IOException {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        List<String> resources = new ArrayList<>();
        while (true) {
            JarEntry entry = jar.getNextJarEntry();
            if (entry != null) {
                if (!entry.isDirectory()) {
                    String name = entry.getName();
                    if (!name.startsWith("/")) {
                        name = "/" + name;
                    }
                    if (name.startsWith(path)) {
                        if (log.isDebugEnabled()) {
                            log.debug("Found resource: " + name);
                        }
                        resources.add(name.substring(1));
                    }
                }
            } else {
                return resources;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:46:0x01c6  */
    /* JADX WARN: Removed duplicated region for block: B:60:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected java.net.URL findJarForResource(java.net.URL r6) throws java.net.MalformedURLException {
        /*
            Method dump skipped, instructions count: 483
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.ibatis.io.DefaultVFS.findJarForResource(java.net.URL):java.net.URL");
    }

    protected String getPackagePath(String packageName) {
        if (packageName == null) {
            return null;
        }
        return packageName.replace('.', '/');
    }

    protected boolean isJar(URL url) {
        return isJar(url, new byte[JAR_MAGIC.length]);
    }

    protected boolean isJar(URL url, byte[] buffer) throws IOException {
        InputStream is = null;
        try {
            is = url.openStream();
            is.read(buffer, 0, JAR_MAGIC.length);
            if (Arrays.equals(buffer, JAR_MAGIC)) {
                if (log.isDebugEnabled()) {
                    log.debug("Found JAR: " + url);
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception e) {
                    }
                }
                return true;
            }
            if (is == null) {
                return false;
            }
            try {
                is.close();
                return false;
            } catch (Exception e2) {
                return false;
            }
        } catch (Exception e3) {
            if (is == null) {
                return false;
            }
            try {
                is.close();
                return false;
            } catch (Exception e4) {
                return false;
            }
        } catch (Throwable th) {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e5) {
                }
            }
            throw th;
        }
    }
}
