package org.apache.ibatis.ognl.enhance;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/enhance/EnhancedClassLoader.class */
public class EnhancedClassLoader extends ClassLoader {
    public EnhancedClassLoader(ClassLoader parentClassLoader) {
        super(parentClassLoader);
    }

    public Class defineClass(String enhancedClassName, byte[] byteCode) {
        return defineClass(enhancedClassName, byteCode, 0, byteCode.length);
    }
}
