package org.aspectj.weaver;

import java.lang.ref.WeakReference;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/WeakClassLoaderReference.class */
public class WeakClassLoaderReference {
    protected final int hashcode;
    private final WeakReference loaderRef;

    public WeakClassLoaderReference(ClassLoader loader) {
        this.loaderRef = new WeakReference(loader);
        if (loader == null) {
            this.hashcode = System.identityHashCode(this);
        } else {
            this.hashcode = loader.hashCode() * 37;
        }
    }

    public ClassLoader getClassLoader() {
        ClassLoader instance = (ClassLoader) this.loaderRef.get();
        return instance;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof WeakClassLoaderReference)) {
            return false;
        }
        WeakClassLoaderReference other = (WeakClassLoaderReference) obj;
        return other.hashcode == this.hashcode;
    }

    public int hashCode() {
        return this.hashcode;
    }
}
