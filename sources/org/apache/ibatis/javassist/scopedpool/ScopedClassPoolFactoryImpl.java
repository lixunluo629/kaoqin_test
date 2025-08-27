package org.apache.ibatis.javassist.scopedpool;

import org.apache.ibatis.javassist.ClassPool;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/scopedpool/ScopedClassPoolFactoryImpl.class */
public class ScopedClassPoolFactoryImpl implements ScopedClassPoolFactory {
    @Override // org.apache.ibatis.javassist.scopedpool.ScopedClassPoolFactory
    public ScopedClassPool create(ClassLoader cl, ClassPool src, ScopedClassPoolRepository repository) {
        return new ScopedClassPool(cl, src, repository, false);
    }

    @Override // org.apache.ibatis.javassist.scopedpool.ScopedClassPoolFactory
    public ScopedClassPool create(ClassPool src, ScopedClassPoolRepository repository) {
        return new ScopedClassPool(null, src, repository, true);
    }
}
