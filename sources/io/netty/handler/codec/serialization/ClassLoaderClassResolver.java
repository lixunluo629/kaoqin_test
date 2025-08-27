package io.netty.handler.codec.serialization;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/serialization/ClassLoaderClassResolver.class */
class ClassLoaderClassResolver implements ClassResolver {
    private final ClassLoader classLoader;

    ClassLoaderClassResolver(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override // io.netty.handler.codec.serialization.ClassResolver
    public Class<?> resolve(String className) throws ClassNotFoundException {
        try {
            return this.classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            return Class.forName(className, false, this.classLoader);
        }
    }
}
