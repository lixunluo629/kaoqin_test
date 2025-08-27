package org.aspectj.weaver.bcel;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import org.aspectj.util.FileUtil;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.bcel.ClassPathManager;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/ExtensibleURLClassLoader.class */
public abstract class ExtensibleURLClassLoader extends URLClassLoader {
    private ClassPathManager classPath;

    public ExtensibleURLClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
        try {
            this.classPath = new ClassPathManager(FileUtil.makeClasspath(urls), null);
        } catch (ExceptionInInitializerError ex) {
            ex.printStackTrace(System.out);
            throw ex;
        }
    }

    @Override // java.net.URLClassLoader
    protected void addURL(URL url) {
        super.addURL(url);
        this.classPath.addPath(url.getPath(), null);
    }

    @Override // java.net.URLClassLoader, java.lang.ClassLoader
    protected Class findClass(String name) throws ClassNotFoundException {
        try {
            byte[] bytes = getBytes(name);
            if (bytes != null) {
                return defineClass(name, bytes);
            }
            throw new ClassNotFoundException(name);
        } catch (IOException e) {
            throw new ClassNotFoundException(name);
        }
    }

    protected Class defineClass(String name, byte[] b, CodeSource cs) throws IOException {
        return defineClass(name, b, 0, b.length, cs);
    }

    protected byte[] getBytes(String name) throws IOException {
        byte[] b = null;
        try {
            UnresolvedType unresolvedType = UnresolvedType.forName(name);
            ClassPathManager.ClassFile classFile = this.classPath.find(unresolvedType);
            if (classFile != null) {
                try {
                    b = FileUtil.readAsByteArray(classFile.getInputStream());
                    classFile.close();
                } catch (Throwable th) {
                    classFile.close();
                    throw th;
                }
            }
            return b;
        } catch (BCException bce) {
            if (bce.getMessage().indexOf("nameToSignature") == -1) {
                bce.printStackTrace(System.err);
                return null;
            }
            return null;
        }
    }

    private Class defineClass(String name, byte[] bytes) throws IOException {
        String packageName = getPackageName(name);
        if (packageName != null) {
            Package pakkage = getPackage(packageName);
            if (pakkage == null) {
                definePackage(packageName, null, null, null, null, null, null, null);
            }
        }
        return defineClass(name, bytes, (CodeSource) null);
    }

    private String getPackageName(String className) {
        int offset = className.lastIndexOf(46);
        if (offset == -1) {
            return null;
        }
        return className.substring(0, offset);
    }

    @Override // java.net.URLClassLoader, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        this.classPath.closeArchives();
    }
}
