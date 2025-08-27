package org.apache.ibatis.javassist.runtime;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/runtime/DotClass.class */
public class DotClass {
    public static NoClassDefFoundError fail(ClassNotFoundException e) {
        return new NoClassDefFoundError(e.getMessage());
    }
}
