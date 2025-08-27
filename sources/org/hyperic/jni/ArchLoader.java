package org.hyperic.jni;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.StringTokenizer;
import org.hyperic.sigar.OperatingSystem;
import org.springframework.util.ResourceUtils;

/* loaded from: sigar-1.6.4.jar:org/hyperic/jni/ArchLoader.class */
public class ArchLoader {
    private static final String osName = System.getProperty("os.name");
    public static final boolean IS_WIN32 = osName.startsWith("Windows");
    public static final boolean IS_AIX = osName.equals(OperatingSystem.NAME_AIX);
    public static final boolean IS_HPUX = osName.equals("HP-UX");
    public static final boolean IS_SOLARIS = osName.equals("SunOS");
    public static final boolean IS_LINUX = osName.equals(OperatingSystem.NAME_LINUX);
    public static final boolean IS_DARWIN;
    public static final boolean IS_OSF1;
    public static final boolean IS_FREEBSD;
    public static final boolean IS_NETWARE;
    private String packageName;
    private String name;
    private String resourcePath;
    private Class loaderClass;
    private String jarName;
    private File nativeLibrary;
    private String version;
    private Object loadLock = new Object();
    private boolean loaded = false;
    private String libName = null;

    static {
        IS_DARWIN = osName.equals("Mac OS X") || osName.equals("Darwin");
        IS_OSF1 = osName.equals("OSF1");
        IS_FREEBSD = osName.equals(OperatingSystem.NAME_FREEBSD);
        IS_NETWARE = osName.equals(OperatingSystem.NAME_NETWARE);
    }

    public ArchLoader() {
    }

    public ArchLoader(Class loaderClass) {
        setLoaderClass(loaderClass);
        String pname = loaderClass.getName();
        int ix = pname.lastIndexOf(".");
        String pname2 = pname.substring(0, ix);
        setPackageName(pname2);
        int ix2 = pname2.lastIndexOf(".");
        setName(pname2.substring(ix2 + 1));
        setJarName(new StringBuffer().append(getName()).append(".jar").toString());
        setResourcePath(toResName(pname2));
    }

    public Class getLoaderClass() {
        return this.loaderClass;
    }

    public void setLoaderClass(Class value) {
        this.loaderClass = value;
    }

    public ClassLoader getClassLoader() {
        return getLoaderClass().getClassLoader();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String value) {
        this.packageName = value;
    }

    public String getResourcePath() {
        return this.resourcePath;
    }

    public void setResourcePath(String value) {
        this.resourcePath = value;
    }

    public String getJarName() {
        return this.jarName;
    }

    public void setJarName(String value) {
        this.jarName = value;
    }

    public String getLibName() {
        return this.libName;
    }

    public void setLibName(String value) {
        this.libName = value;
    }

    public String getArchLibName() throws ArchNotSupportedException {
        return new StringBuffer().append(getName()).append("-").append(ArchName.getName()).toString();
    }

    public String getDefaultLibName() throws ArchNotSupportedException {
        return System.getProperty(new StringBuffer().append(getPackageName()).append(".libname").toString(), new StringBuffer().append("java").append(getArchLibName()).toString());
    }

    public File getNativeLibrary() {
        return this.nativeLibrary;
    }

    private String toResName(String name) {
        StringBuffer sb = new StringBuffer(name);
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '.') {
                sb.setCharAt(i, '/');
            }
        }
        return sb.toString();
    }

    public static String getLibraryPrefix() {
        if (IS_WIN32 || IS_NETWARE) {
            return "";
        }
        return "lib";
    }

    public static String getLibraryExtension() {
        if (IS_WIN32) {
            return ".dll";
        }
        if (IS_NETWARE) {
            return ".nlm";
        }
        if (IS_DARWIN) {
            return ".dylib";
        }
        if (IS_HPUX) {
            return ".sl";
        }
        return ".so";
    }

    public String getLibraryName() throws ArchNotSupportedException {
        String libName = getLibName();
        String libName2 = libName;
        if (libName == null) {
            libName2 = getDefaultLibName();
            setLibName(libName2);
        }
        String prefix = getLibraryPrefix();
        String ext = getLibraryExtension();
        return new StringBuffer().append(prefix).append(libName2).append(ext).toString();
    }

    public String getVersionedLibraryName() {
        if (this.version == null) {
            return null;
        }
        try {
            getLibraryName();
            String prefix = getLibraryPrefix();
            String ext = getLibraryExtension();
            return new StringBuffer().append(prefix).append(this.libName).append('-').append(this.version).append(ext).toString();
        } catch (ArchNotSupportedException e) {
            return null;
        }
    }

    private boolean isJarURL(URL url) {
        int ix;
        String jarName;
        int ix2;
        if (url == null) {
            return false;
        }
        String name = url.getFile();
        String jarName2 = getJarName();
        if (name.indexOf(jarName2) != -1) {
            return true;
        }
        int ix3 = jarName2.indexOf(".jar");
        if (ix3 == -1 || (ix = name.lastIndexOf(new StringBuffer().append(jarName2.substring(0, ix3)).append("-").toString())) == -1 || (ix2 = (jarName = name.substring(ix)).indexOf(".jar")) == -1) {
            return false;
        }
        this.version = jarName.substring(jarName.indexOf(45) + 1, ix2);
        setJarName(jarName.substring(0, ix2 + 4));
        return true;
    }

    public String findJarPath(String libName) throws ArchLoaderException {
        return findJarPath(libName, true);
    }

    private String findJarPath(String libName, boolean isRequired) throws ArchLoaderException {
        File file;
        if (getJarName() == null) {
            throw new ArchLoaderException("jarName is null");
        }
        String path = getResourcePath();
        ClassLoader loader = getClassLoader();
        URL url = loader.getResource(path);
        if (!isJarURL(url)) {
            url = null;
        }
        if (url == null && (loader instanceof URLClassLoader)) {
            URL[] urls = ((URLClassLoader) loader).getURLs();
            int i = 0;
            while (true) {
                if (i >= urls.length) {
                    break;
                }
                if (!isJarURL(urls[i])) {
                    i++;
                } else {
                    url = urls[i];
                    break;
                }
            }
        }
        if (url == null) {
            if (isRequired) {
                throw new ArchLoaderException(new StringBuffer().append("Unable to find ").append(getJarName()).toString());
            }
            return null;
        }
        String path2 = url.getFile();
        if (path2.startsWith(ResourceUtils.FILE_URL_PREFIX)) {
            path2 = path2.substring(5);
        }
        File file2 = new File(path2);
        String jarName = getJarName();
        while (file2 != null && !file2.getName().startsWith(jarName)) {
            file2 = file2.getParentFile();
        }
        if (libName == null) {
            libName = jarName;
        }
        if (file2 != null && (file = file2.getParentFile()) != null) {
            String dir = URLDecoder.decode(file.toString());
            if (findNativeLibrary(dir, libName)) {
                return dir;
            }
            return null;
        }
        return null;
    }

    protected void systemLoadLibrary(String name) {
        System.loadLibrary(name);
    }

    protected void systemLoad(String name) {
        System.load(name);
    }

    protected boolean containsNativeLibrary(File dir, String name) {
        if (name == null) {
            return false;
        }
        File file = new File(dir, name);
        if (file.exists()) {
            this.nativeLibrary = file;
            return true;
        }
        return false;
    }

    protected boolean findNativeLibrary(String dir, String name) {
        File path = new File(dir).getAbsoluteFile();
        if (containsNativeLibrary(path, name) || containsNativeLibrary(path, getVersionedLibraryName())) {
            return true;
        }
        return containsNativeLibrary(path, new StringBuffer().append(getLibraryPrefix()).append(getName()).append(getLibraryExtension()).toString());
    }

    protected boolean findInJavaLibraryPath(String libName) {
        String path = System.getProperty("java.library.path", "");
        StringTokenizer tok = new StringTokenizer(path, File.pathSeparator);
        while (tok.hasMoreTokens()) {
            String path2 = tok.nextToken();
            if (findNativeLibrary(path2, libName)) {
                return true;
            }
        }
        return false;
    }

    protected void loadLibrary(String path) throws ArchLoaderException, ArchNotSupportedException {
        try {
            String libName = getLibraryName();
            if (path == null) {
                path = System.getProperty(new StringBuffer().append(getPackageName()).append(".path").toString());
            }
            if (path != null) {
                if (path.equals("-")) {
                    return;
                }
                findJarPath(null, false);
                findNativeLibrary(path, libName);
            } else if (findJarPath(libName, false) == null) {
                findInJavaLibraryPath(libName);
            }
            if (this.nativeLibrary != null) {
                systemLoad(this.nativeLibrary.toString());
            } else {
                systemLoadLibrary(libName);
            }
        } catch (RuntimeException e) {
            String reason = e.getMessage();
            if (reason == null) {
                reason = e.getClass().getName();
            }
            String msg = new StringBuffer().append("Failed to load ").append(this.libName).append(": ").append(reason).toString();
            throw new ArchLoaderException(msg);
        }
    }

    public void load() throws ArchLoaderException, ArchNotSupportedException {
        load(null);
    }

    public void load(String path) throws ArchLoaderException, ArchNotSupportedException {
        synchronized (this.loadLock) {
            if (this.loaded) {
                return;
            }
            loadLibrary(path);
            this.loaded = true;
        }
    }
}
