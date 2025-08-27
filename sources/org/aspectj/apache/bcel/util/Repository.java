package org.aspectj.apache.bcel.util;

import org.aspectj.apache.bcel.classfile.JavaClass;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/util/Repository.class */
public interface Repository {
    void storeClass(JavaClass javaClass);

    void removeClass(JavaClass javaClass);

    JavaClass findClass(String str);

    JavaClass loadClass(String str) throws ClassNotFoundException;

    JavaClass loadClass(Class cls) throws ClassNotFoundException;

    void clear();
}
