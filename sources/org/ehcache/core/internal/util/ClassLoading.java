package org.ehcache.core.internal.util;

import java.io.IOException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.ServiceLoader;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/internal/util/ClassLoading.class */
public class ClassLoading {
    private static final ClassLoader DEFAULT_CLASSLOADER = (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: org.ehcache.core.internal.util.ClassLoading.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        public ClassLoader run() {
            return new DefaultClassLoader();
        }
    });

    public static ClassLoader getDefaultClassLoader() {
        return DEFAULT_CLASSLOADER;
    }

    public static <T> ServiceLoader<T> libraryServiceLoaderFor(Class<T> serviceType) {
        return ServiceLoader.load(serviceType, ClassLoading.class.getClassLoader());
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/internal/util/ClassLoading$DefaultClassLoader.class */
    private static class DefaultClassLoader extends ClassLoader {
        private static final ClassLoader THIS_LOADER = DefaultClassLoader.class.getClassLoader();

        private DefaultClassLoader() {
        }

        @Override // java.lang.ClassLoader
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader != null) {
                try {
                    return loader.loadClass(name);
                } catch (ClassNotFoundException e) {
                }
            }
            return THIS_LOADER.loadClass(name);
        }

        @Override // java.lang.ClassLoader
        public URL getResource(String name) {
            URL res;
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader != null && (res = loader.getResource(name)) != null) {
                return res;
            }
            return THIS_LOADER.getResource(name);
        }

        @Override // java.lang.ClassLoader
        public Enumeration<URL> getResources(String name) throws IOException {
            Enumeration<URL> resources;
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader != null && (resources = loader.getResources(name)) != null && resources.hasMoreElements()) {
                return resources;
            }
            return THIS_LOADER.getResources(name);
        }
    }
}
