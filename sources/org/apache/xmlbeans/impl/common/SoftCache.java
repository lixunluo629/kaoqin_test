package org.apache.xmlbeans.impl.common;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/SoftCache.class */
public class SoftCache {
    private HashMap map = new HashMap();

    public Object get(Object key) {
        SoftReference softRef = (SoftReference) this.map.get(key);
        if (softRef == null) {
            return null;
        }
        return softRef.get();
    }

    public Object put(Object key, Object value) {
        SoftReference softRef = (SoftReference) this.map.put(key, new SoftReference(value));
        if (softRef == null) {
            return null;
        }
        Object oldValue = softRef.get();
        softRef.clear();
        return oldValue;
    }

    public Object remove(Object key) {
        SoftReference softRef = (SoftReference) this.map.remove(key);
        if (softRef == null) {
            return null;
        }
        Object oldValue = softRef.get();
        softRef.clear();
        return oldValue;
    }
}
