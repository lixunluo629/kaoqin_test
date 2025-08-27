package org.apache.ibatis.javassist;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

/* compiled from: ClassPoolTail.java */
/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/JarClassPath.class */
final class JarClassPath implements ClassPath {
    JarFile jarfile;
    String jarfileURL;

    JarClassPath(String pathname) throws NotFoundException {
        try {
            this.jarfile = new JarFile(pathname);
            this.jarfileURL = new File(pathname).getCanonicalFile().toURI().toURL().toString();
        } catch (IOException e) {
            throw new NotFoundException(pathname);
        }
    }

    @Override // org.apache.ibatis.javassist.ClassPath
    public InputStream openClassfile(String classname) throws NotFoundException {
        try {
            String jarname = classname.replace('.', '/') + ClassUtils.CLASS_FILE_SUFFIX;
            JarEntry je = this.jarfile.getJarEntry(jarname);
            if (je != null) {
                return this.jarfile.getInputStream(je);
            }
            return null;
        } catch (IOException e) {
            throw new NotFoundException("broken jar file?: " + this.jarfile.getName());
        }
    }

    @Override // org.apache.ibatis.javassist.ClassPath
    public URL find(String classname) {
        String jarname = classname.replace('.', '/') + ClassUtils.CLASS_FILE_SUFFIX;
        JarEntry je = this.jarfile.getJarEntry(jarname);
        if (je != null) {
            try {
                return new URL(ResourceUtils.JAR_URL_PREFIX + this.jarfileURL + ResourceUtils.JAR_URL_SEPARATOR + jarname);
            } catch (MalformedURLException e) {
                return null;
            }
        }
        return null;
    }

    @Override // org.apache.ibatis.javassist.ClassPath
    public void close() {
        try {
            this.jarfile.close();
            this.jarfile = null;
        } catch (IOException e) {
        }
    }

    public String toString() {
        return this.jarfile == null ? "<null>" : this.jarfile.toString();
    }
}
