package org.springframework.boot.context.embedded;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.jar.JarFile;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.util.TempFile;
import org.springframework.boot.ApplicationHome;
import org.springframework.boot.ApplicationTemp;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/AbstractEmbeddedServletContainerFactory.class */
public abstract class AbstractEmbeddedServletContainerFactory extends AbstractConfigurableEmbeddedServletContainer implements EmbeddedServletContainerFactory {
    protected final Log logger;
    private static final String[] COMMON_DOC_ROOTS = {"src/main/webapp", "public", "static"};

    public AbstractEmbeddedServletContainerFactory() {
        this.logger = LogFactory.getLog(getClass());
    }

    public AbstractEmbeddedServletContainerFactory(int port) {
        super(port);
        this.logger = LogFactory.getLog(getClass());
    }

    public AbstractEmbeddedServletContainerFactory(String contextPath, int port) {
        super(contextPath, port);
        this.logger = LogFactory.getLog(getClass());
    }

    protected final File getValidDocumentRoot() {
        File file = getDocumentRoot();
        File file2 = file != null ? file : getWarFileDocumentRoot();
        File file3 = file2 != null ? file2 : getExplodedWarFileDocumentRoot();
        File file4 = file3 != null ? file3 : getCommonDocumentRoot();
        if (file4 == null && this.logger.isDebugEnabled()) {
            this.logger.debug("None of the document roots " + Arrays.asList(COMMON_DOC_ROOTS) + " point to a directory and will be ignored.");
        } else if (this.logger.isDebugEnabled()) {
            this.logger.debug("Document root: " + file4);
        }
        return file4;
    }

    private File getExplodedWarFileDocumentRoot() {
        return getExplodedWarFileDocumentRoot(getCodeSourceArchive());
    }

    protected List<URL> getUrlsOfJarsWithMetaInfResources() {
        ClassLoader classLoader = getClass().getClassLoader();
        List<URL> staticResourceUrls = new ArrayList<>();
        if (classLoader instanceof URLClassLoader) {
            for (URL url : ((URLClassLoader) classLoader).getURLs()) {
                if (isStaticResourceJar(url)) {
                    staticResourceUrls.add(url);
                }
            }
        }
        return staticResourceUrls;
    }

    private boolean isStaticResourceJar(URL url) throws IOException {
        try {
            if ("file".equals(url.getProtocol())) {
                File file = new File(url.toURI());
                return (file.isDirectory() && new File(file, "META-INF/resources").isDirectory()) || isResourcesJar(file);
            }
            URLConnection connection = url.openConnection();
            if ((connection instanceof JarURLConnection) && isResourcesJar((JarURLConnection) connection)) {
                return true;
            }
            return false;
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Deprecated
    protected final String getDecodedFile(URL url) {
        try {
            return URLDecoder.decode(url.getFile(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Failed to decode '" + url.getFile() + "' using UTF-8");
        }
    }

    private boolean isResourcesJar(JarURLConnection connection) {
        try {
            return isResourcesJar(connection.getJarFile());
        } catch (IOException ex) {
            this.logger.warn("Unable to open jar from connection '" + connection + "' to determine if it contains static resources", ex);
            return false;
        }
    }

    private boolean isResourcesJar(File file) {
        try {
            if (file.getName().endsWith(".jar")) {
                if (isResourcesJar(new JarFile(file))) {
                    return true;
                }
            }
            return false;
        } catch (IOException ex) {
            this.logger.warn("Unable to open jar '" + file + "' to determine if it contains static resources", ex);
            return false;
        }
    }

    private boolean isResourcesJar(JarFile jar) throws IOException {
        try {
            return jar.getJarEntry("META-INF/resources") != null;
        } finally {
            jar.close();
        }
    }

    File getExplodedWarFileDocumentRoot(File codeSourceFile) {
        String path;
        int webInfPathIndex;
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Code archive: " + codeSourceFile);
        }
        if (codeSourceFile != null && codeSourceFile.exists() && (webInfPathIndex = (path = codeSourceFile.getAbsolutePath()).indexOf(File.separatorChar + "WEB-INF" + File.separatorChar)) >= 0) {
            return new File(path.substring(0, webInfPathIndex));
        }
        return null;
    }

    private File getWarFileDocumentRoot() {
        return getArchiveFileDocumentRoot(".war");
    }

    private File getArchiveFileDocumentRoot(String extension) {
        File file = getCodeSourceArchive();
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Code archive: " + file);
        }
        if (file != null && file.exists() && !file.isDirectory() && file.getName().toLowerCase(Locale.ENGLISH).endsWith(extension)) {
            return file.getAbsoluteFile();
        }
        return null;
    }

    private File getCommonDocumentRoot() {
        for (String commonDocRoot : COMMON_DOC_ROOTS) {
            File root = new File(commonDocRoot);
            if (root.exists() && root.isDirectory()) {
                return root.getAbsoluteFile();
            }
        }
        return null;
    }

    private File getCodeSourceArchive() {
        return getCodeSourceArchive(getClass().getProtectionDomain().getCodeSource());
    }

    File getCodeSourceArchive(CodeSource codeSource) throws IOException {
        URL location;
        String path;
        if (codeSource != null) {
            try {
                location = codeSource.getLocation();
            } catch (Exception e) {
                return null;
            }
        } else {
            location = null;
        }
        URL location2 = location;
        if (location2 == null) {
            return null;
        }
        URLConnection connection = location2.openConnection();
        if (connection instanceof JarURLConnection) {
            path = ((JarURLConnection) connection).getJarFile().getName();
        } else {
            path = location2.toURI().getPath();
        }
        if (path.contains(ResourceUtils.JAR_URL_SEPARATOR)) {
            path = path.substring(0, path.indexOf(ResourceUtils.JAR_URL_SEPARATOR));
        }
        return new File(path);
    }

    protected final File getValidSessionStoreDir() {
        return getValidSessionStoreDir(true);
    }

    protected final File getValidSessionStoreDir(boolean mkdirs) {
        File dir = getSessionStoreDir();
        if (dir == null) {
            return new ApplicationTemp().getDir("servlet-sessions");
        }
        if (!dir.isAbsolute()) {
            dir = new File(new ApplicationHome().getDir(), dir.getPath());
        }
        if (!dir.exists() && mkdirs) {
            dir.mkdirs();
        }
        Assert.state(!mkdirs || dir.exists(), "Session dir " + dir + " does not exist");
        Assert.state(!dir.isFile(), "Session dir " + dir + " points to a file");
        return dir;
    }

    protected File createTempDir(String prefix) throws IOException {
        try {
            File tempDir = File.createTempFile(prefix + ".", "." + getPort());
            tempDir.delete();
            tempDir.mkdir();
            tempDir.deleteOnExit();
            return tempDir;
        } catch (IOException ex) {
            throw new EmbeddedServletContainerException("Unable to create tempDir. java.io.tmpdir is set to " + System.getProperty(TempFile.JAVA_IO_TMPDIR), ex);
        }
    }
}
