package org.apache.ibatis.javassist;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import org.springframework.util.ClassUtils;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/LoaderClassPath.class */
public class LoaderClassPath implements ClassPath {
    private WeakReference clref;

    public LoaderClassPath(ClassLoader cl) {
        this.clref = new WeakReference(cl);
    }

    public String toString() {
        Object cl = null;
        if (this.clref != null) {
            cl = this.clref.get();
        }
        return cl == null ? "<null>" : cl.toString();
    }

    @Override // org.apache.ibatis.javassist.ClassPath
    public InputStream openClassfile(String classname) throws NotFoundException {
        String cname = classname.replace('.', '/') + ClassUtils.CLASS_FILE_SUFFIX;
        ClassLoader cl = (ClassLoader) this.clref.get();
        if (cl == null) {
            return null;
        }
        InputStream is = cl.getResourceAsStream(cname);
        return is;
    }

    @Override // org.apache.ibatis.javassist.ClassPath
    public URL find(String classname) {
        String cname = classname.replace('.', '/') + ClassUtils.CLASS_FILE_SUFFIX;
        ClassLoader cl = (ClassLoader) this.clref.get();
        if (cl == null) {
            return null;
        }
        URL url = cl.getResource(cname);
        return url;
    }

    @Override // org.apache.ibatis.javassist.ClassPath
    public void close() {
        this.clref = null;
    }
}
