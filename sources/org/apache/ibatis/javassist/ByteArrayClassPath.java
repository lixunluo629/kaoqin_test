package org.apache.ibatis.javassist;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.springframework.util.ClassUtils;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/ByteArrayClassPath.class */
public class ByteArrayClassPath implements ClassPath {
    protected String classname;
    protected byte[] classfile;

    public ByteArrayClassPath(String name, byte[] classfile) {
        this.classname = name;
        this.classfile = classfile;
    }

    @Override // org.apache.ibatis.javassist.ClassPath
    public void close() {
    }

    public String toString() {
        return "byte[]:" + this.classname;
    }

    @Override // org.apache.ibatis.javassist.ClassPath
    public InputStream openClassfile(String classname) {
        if (this.classname.equals(classname)) {
            return new ByteArrayInputStream(this.classfile);
        }
        return null;
    }

    @Override // org.apache.ibatis.javassist.ClassPath
    public URL find(String classname) {
        if (this.classname.equals(classname)) {
            String cname = classname.replace('.', '/') + ClassUtils.CLASS_FILE_SUFFIX;
            try {
                return new URL("file:/ByteArrayClassPath/" + cname);
            } catch (MalformedURLException e) {
                return null;
            }
        }
        return null;
    }
}
