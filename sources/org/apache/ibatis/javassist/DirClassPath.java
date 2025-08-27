package org.apache.ibatis.javassist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.springframework.util.ClassUtils;

/* compiled from: ClassPoolTail.java */
/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/DirClassPath.class */
final class DirClassPath implements ClassPath {
    String directory;

    DirClassPath(String dirName) {
        this.directory = dirName;
    }

    @Override // org.apache.ibatis.javassist.ClassPath
    public InputStream openClassfile(String classname) {
        try {
            char sep = File.separatorChar;
            String filename = this.directory + sep + classname.replace('.', sep) + ClassUtils.CLASS_FILE_SUFFIX;
            return new FileInputStream(filename.toString());
        } catch (FileNotFoundException | SecurityException e) {
            return null;
        }
    }

    @Override // org.apache.ibatis.javassist.ClassPath
    public URL find(String classname) {
        char sep = File.separatorChar;
        String filename = this.directory + sep + classname.replace('.', sep) + ClassUtils.CLASS_FILE_SUFFIX;
        File f = new File(filename);
        if (f.exists()) {
            try {
                return f.getCanonicalFile().toURI().toURL();
            } catch (MalformedURLException e) {
                return null;
            } catch (IOException e2) {
                return null;
            }
        }
        return null;
    }

    @Override // org.apache.ibatis.javassist.ClassPath
    public void close() {
    }

    public String toString() {
        return this.directory;
    }
}
