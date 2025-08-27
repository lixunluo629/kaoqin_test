package org.apache.ibatis.javassist;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/Translator.class */
public interface Translator {
    void start(ClassPool classPool) throws NotFoundException, CannotCompileException;

    void onLoad(ClassPool classPool, String str) throws NotFoundException, CannotCompileException;
}
