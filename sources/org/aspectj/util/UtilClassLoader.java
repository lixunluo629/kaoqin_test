package org.aspectj.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.springframework.util.ClassUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/util/UtilClassLoader.class */
public class UtilClassLoader extends URLClassLoader {
    List<File> dirs;
    private URL[] urlsForDebugString;

    public UtilClassLoader(URL[] urls, File[] dirs) {
        super(urls);
        LangUtil.throwIaxIfNotAssignable((Object[]) dirs, (Class<?>) File.class, "dirs");
        this.urlsForDebugString = urls;
        ArrayList<File> dcopy = new ArrayList<>();
        if (!LangUtil.isEmpty(dirs)) {
            dcopy.addAll(Arrays.asList(dirs));
        }
        this.dirs = Collections.unmodifiableList(dcopy);
    }

    @Override // java.lang.ClassLoader
    public URL getResource(String name) {
        return ClassLoader.getSystemResource(name);
    }

    @Override // java.net.URLClassLoader, java.lang.ClassLoader
    public InputStream getResourceAsStream(String name) {
        return ClassLoader.getSystemResourceAsStream(name);
    }

    @Override // java.lang.ClassLoader
    public synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        byte[] data;
        ClassNotFoundException thrown = null;
        Class<?> result = findLoadedClass(name);
        if (null != result) {
            resolve = false;
        } else {
            try {
                result = findSystemClass(name);
            } catch (ClassNotFoundException e) {
                thrown = e;
            }
        }
        if (null == result) {
            try {
                result = super.loadClass(name, resolve);
            } catch (ClassNotFoundException e2) {
                thrown = e2;
            }
            if (null != result) {
                return result;
            }
        }
        if (null == result && (data = readClass(name)) != null) {
            result = defineClass(name, data, 0, data.length);
        }
        if (null == result) {
            if (null != thrown) {
                throw thrown;
            }
            throw new ClassNotFoundException(name);
        }
        if (resolve) {
            resolveClass(result);
        }
        return result;
    }

    private byte[] readClass(String className) throws ClassNotFoundException {
        String fileName = className.replace('.', '/') + ClassUtils.CLASS_FILE_SUFFIX;
        Iterator<File> iter = this.dirs.iterator();
        while (iter.hasNext()) {
            File file = new File(iter.next(), fileName);
            if (file.canRead()) {
                return getClassData(file);
            }
        }
        return null;
    }

    private byte[] getClassData(File f) throws IOException {
        try {
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[4096];
            while (true) {
                int n = stream.read(b);
                if (n != -1) {
                    out.write(b, 0, n);
                } else {
                    stream.close();
                    out.close();
                    return out.toByteArray();
                }
            }
        } catch (IOException e) {
            return null;
        }
    }

    public String toString() {
        return "UtilClassLoader(urls=" + Arrays.asList(this.urlsForDebugString) + ", dirs=" + this.dirs + ")";
    }
}
