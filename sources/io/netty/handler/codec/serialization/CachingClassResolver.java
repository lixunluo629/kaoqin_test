package io.netty.handler.codec.serialization;

import java.util.Map;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/serialization/CachingClassResolver.class */
class CachingClassResolver implements ClassResolver {
    private final Map<String, Class<?>> classCache;
    private final ClassResolver delegate;

    CachingClassResolver(ClassResolver delegate, Map<String, Class<?>> classCache) {
        this.delegate = delegate;
        this.classCache = classCache;
    }

    @Override // io.netty.handler.codec.serialization.ClassResolver
    public Class<?> resolve(String className) throws ClassNotFoundException {
        Class<?> clazz = this.classCache.get(className);
        if (clazz != null) {
            return clazz;
        }
        Class<?> clazz2 = this.delegate.resolve(className);
        this.classCache.put(className, clazz2);
        return clazz2;
    }
}
