package org.aspectj.apache.bcel;

import java.io.IOException;
import org.aspectj.apache.bcel.classfile.JavaClass;
import org.aspectj.apache.bcel.util.ClassPath;
import org.aspectj.apache.bcel.util.SyntheticRepository;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/Repository.class */
public abstract class Repository {
    private static org.aspectj.apache.bcel.util.Repository _repository = null;

    public static org.aspectj.apache.bcel.util.Repository getRepository() {
        if (_repository == null) {
            _repository = SyntheticRepository.getInstance();
        }
        return _repository;
    }

    public static void setRepository(org.aspectj.apache.bcel.util.Repository repository) {
        _repository = repository;
    }

    public static JavaClass lookupClass(String str) {
        try {
            JavaClass javaClassFindClass = getRepository().findClass(str);
            return javaClassFindClass != null ? javaClassFindClass : getRepository().loadClass(str);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static ClassPath.ClassFile lookupClassFile(String str) {
        try {
            return ClassPath.getSystemClassPath().getClassFile(str);
        } catch (IOException e) {
            return null;
        }
    }

    public static void clearCache() {
        getRepository().clear();
    }

    public static JavaClass addClass(JavaClass javaClass) {
        JavaClass javaClassFindClass = getRepository().findClass(javaClass.getClassName());
        getRepository().storeClass(javaClass);
        return javaClassFindClass;
    }

    public static void removeClass(String str) {
        getRepository().removeClass(getRepository().findClass(str));
    }

    public static boolean instanceOf(JavaClass javaClass, JavaClass javaClass2) {
        return javaClass.instanceOf(javaClass2);
    }

    public static boolean instanceOf(String str, String str2) {
        return instanceOf(lookupClass(str), lookupClass(str2));
    }

    public static boolean implementationOf(JavaClass javaClass, JavaClass javaClass2) {
        return javaClass.implementationOf(javaClass2);
    }

    public static boolean implementationOf(String str, String str2) {
        return implementationOf(lookupClass(str), lookupClass(str2));
    }
}
