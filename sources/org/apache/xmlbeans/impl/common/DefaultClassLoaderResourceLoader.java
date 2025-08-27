package org.apache.xmlbeans.impl.common;

import java.io.InputStream;
import org.apache.xmlbeans.ResourceLoader;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/DefaultClassLoaderResourceLoader.class */
public class DefaultClassLoaderResourceLoader implements ResourceLoader {
    @Override // org.apache.xmlbeans.ResourceLoader
    public InputStream getResourceAsStream(String resourceName) {
        InputStream in = null;
        try {
            in = getResourceAsStream(Thread.currentThread().getContextClassLoader(), resourceName);
        } catch (SecurityException e) {
        }
        if (in == null) {
            in = getResourceAsStream(DefaultClassLoaderResourceLoader.class.getClassLoader(), resourceName);
        }
        if (in == null) {
            in = DefaultClassLoaderResourceLoader.class.getResourceAsStream(resourceName);
        }
        return in;
    }

    @Override // org.apache.xmlbeans.ResourceLoader
    public void close() {
    }

    private InputStream getResourceAsStream(ClassLoader loader, String resourceName) {
        if (loader == null) {
            return null;
        }
        return loader.getResourceAsStream(resourceName);
    }
}
