package org.apache.ibatis.javassist.scopedpool;

import org.apache.ibatis.javassist.ClassPool;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/scopedpool/ScopedClassPoolFactory.class */
public interface ScopedClassPoolFactory {
    ScopedClassPool create(ClassLoader classLoader, ClassPool classPool, ScopedClassPoolRepository scopedClassPoolRepository);

    ScopedClassPool create(ClassPool classPool, ScopedClassPoolRepository scopedClassPoolRepository);
}
