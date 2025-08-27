package org.apache.ibatis.javassist;

import java.io.InputStream;
import java.net.URL;
import org.springframework.util.ClassUtils;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/ClassClassPath.class */
public class ClassClassPath implements ClassPath {
    private Class thisClass;

    public ClassClassPath(Class c) {
        this.thisClass = c;
    }

    ClassClassPath() {
        this(Object.class);
    }

    @Override // org.apache.ibatis.javassist.ClassPath
    public InputStream openClassfile(String classname) throws NotFoundException {
        String filename = '/' + classname.replace('.', '/') + ClassUtils.CLASS_FILE_SUFFIX;
        return this.thisClass.getResourceAsStream(filename);
    }

    @Override // org.apache.ibatis.javassist.ClassPath
    public URL find(String classname) {
        String filename = '/' + classname.replace('.', '/') + ClassUtils.CLASS_FILE_SUFFIX;
        return this.thisClass.getResource(filename);
    }

    @Override // org.apache.ibatis.javassist.ClassPath
    public void close() {
    }

    public String toString() {
        return this.thisClass.getName() + ClassUtils.CLASS_FILE_SUFFIX;
    }
}
