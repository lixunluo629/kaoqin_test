package org.apache.xmlbeans.impl.schema;

import java.io.InputStream;
import org.apache.xmlbeans.ResourceLoader;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/ClassLoaderResourceLoader.class */
public class ClassLoaderResourceLoader implements ResourceLoader {
    ClassLoader _classLoader;

    public ClassLoaderResourceLoader(ClassLoader classLoader) {
        this._classLoader = classLoader;
    }

    @Override // org.apache.xmlbeans.ResourceLoader
    public InputStream getResourceAsStream(String resourceName) {
        return this._classLoader.getResourceAsStream(resourceName);
    }

    @Override // org.apache.xmlbeans.ResourceLoader
    public void close() {
    }
}
