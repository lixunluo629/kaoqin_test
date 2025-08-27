package org.apache.xmlbeans.impl.common;

import java.lang.ref.SoftReference;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.SystemProperties;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/SystemCache.class */
public class SystemCache {
    private static SystemCache INSTANCE;
    private ThreadLocal tl_saxLoaders = new ThreadLocal();

    static {
        INSTANCE = new SystemCache();
        String cacheClass = SystemProperties.getProperty("xmlbean.systemcacheimpl");
        Object impl = null;
        if (cacheClass != null) {
            try {
                impl = Class.forName(cacheClass).newInstance();
                if (!(impl instanceof SystemCache)) {
                    throw new ClassCastException("Value for system property \"xmlbean.systemcacheimpl\" points to a class (" + cacheClass + ") which does not derive from SystemCache");
                }
            } catch (ClassNotFoundException cnfe) {
                throw new RuntimeException("Cache class " + cacheClass + " specified by \"xmlbean.systemcacheimpl\" was not found.", cnfe);
            } catch (IllegalAccessException iae) {
                throw new RuntimeException("Could not instantiate class " + cacheClass + " as specified by \"xmlbean.systemcacheimpl\". A public empty constructor may be missing.", iae);
            } catch (InstantiationException ie) {
                throw new RuntimeException("Could not instantiate class " + cacheClass + " as specified by \"xmlbean.systemcacheimpl\". An empty constructor may be missing.", ie);
            }
        }
        if (impl != null) {
            INSTANCE = (SystemCache) impl;
        }
    }

    public static final synchronized void set(SystemCache instance) {
        INSTANCE = instance;
    }

    public static final SystemCache get() {
        return INSTANCE;
    }

    public SchemaTypeLoader getFromTypeLoaderCache(ClassLoader cl) {
        return null;
    }

    public void addToTypeLoaderCache(SchemaTypeLoader stl, ClassLoader cl) {
    }

    public void clearThreadLocals() {
        this.tl_saxLoaders.remove();
    }

    public Object getSaxLoader() {
        SoftReference s = (SoftReference) this.tl_saxLoaders.get();
        if (s == null) {
            return null;
        }
        return s.get();
    }

    public void setSaxLoader(Object saxLoader) {
        this.tl_saxLoaders.set(new SoftReference(saxLoader));
    }
}
