package org.hyperic.sigar.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/util/ReferenceMap.class */
public class ReferenceMap extends AbstractMap {
    private static final boolean SOFTCACHE;
    protected ReferenceQueue queue;
    protected Map map;

    /* renamed from: org.hyperic.sigar.util.ReferenceMap$1, reason: invalid class name */
    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/util/ReferenceMap$1.class */
    static class AnonymousClass1 {
    }

    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/util/ReferenceMap$MapReference.class */
    protected interface MapReference {
        Object getKey();
    }

    static {
        SOFTCACHE = !"false".equals(System.getProperty("org.hyperic.sigar.softcache"));
    }

    public ReferenceMap() {
        this(new HashMap());
    }

    public ReferenceMap(Map map) {
        this.map = map;
        this.queue = new ReferenceQueue();
    }

    public static Map synchronizedMap() {
        Map map = Collections.synchronizedMap(new HashMap());
        return newInstance(map);
    }

    public static Map newInstance() {
        return newInstance(new HashMap());
    }

    public static Map newInstance(Map map) {
        if (SOFTCACHE) {
            return new ReferenceMap(map);
        }
        return map;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object get(Object key) {
        Reference ref = (Reference) this.map.get(key);
        if (ref == null) {
            return null;
        }
        Object o = ref.get();
        if (o == null) {
            this.map.remove(key);
        }
        return o;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object put(Object key, Object value) {
        poll();
        return this.map.put(key, new SoftValue(key, value, this.queue, null));
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object remove(Object key) {
        poll();
        return this.map.remove(key);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        poll();
        this.map.clear();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        poll();
        return this.map.size();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set entrySet() {
        throw new UnsupportedOperationException();
    }

    protected void poll() {
        while (true) {
            MapReference ref = (MapReference) this.queue.poll();
            if (ref != null) {
                this.map.remove(ref.getKey());
            } else {
                return;
            }
        }
    }

    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/util/ReferenceMap$SoftValue.class */
    protected static final class SoftValue extends SoftReference implements MapReference {
        private Object key;

        SoftValue(Object x0, Object x1, ReferenceQueue x2, AnonymousClass1 x3) {
            this(x0, x1, x2);
        }

        @Override // org.hyperic.sigar.util.ReferenceMap.MapReference
        public Object getKey() {
            return this.key;
        }

        private SoftValue(Object key, Object value, ReferenceQueue queue) {
            super(value, queue);
            this.key = key;
        }
    }

    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/util/ReferenceMap$WeakValue.class */
    protected static final class WeakValue extends WeakReference implements MapReference {
        private Object key;

        @Override // org.hyperic.sigar.util.ReferenceMap.MapReference
        public Object getKey() {
            return this.key;
        }

        protected WeakValue(Object key, Object value, ReferenceQueue queue) {
            super(value, queue);
            this.key = key;
        }
    }
}
