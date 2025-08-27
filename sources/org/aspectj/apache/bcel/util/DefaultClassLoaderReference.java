package org.aspectj.apache.bcel.util;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/util/DefaultClassLoaderReference.class */
public class DefaultClassLoaderReference implements ClassLoaderReference {
    private ClassLoader loader;

    public DefaultClassLoaderReference(ClassLoader classLoader) {
        this.loader = classLoader;
    }

    @Override // org.aspectj.apache.bcel.util.ClassLoaderReference
    public ClassLoader getClassLoader() {
        return this.loader;
    }
}
