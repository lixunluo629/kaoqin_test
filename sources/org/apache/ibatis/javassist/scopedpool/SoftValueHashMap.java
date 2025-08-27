package org.apache.ibatis.javassist.scopedpool;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/scopedpool/SoftValueHashMap.class */
public class SoftValueHashMap extends AbstractMap implements Map {
    private Map hash;
    private ReferenceQueue queue;

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/scopedpool/SoftValueHashMap$SoftValueRef.class */
    private static class SoftValueRef extends SoftReference {
        public Object key;

        private SoftValueRef(Object key, Object val, ReferenceQueue q) {
            super(val, q);
            this.key = key;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static SoftValueRef create(Object key, Object val, ReferenceQueue q) {
            if (val == null) {
                return null;
            }
            return new SoftValueRef(key, val, q);
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set entrySet() {
        processQueue();
        return this.hash.entrySet();
    }

    private void processQueue() {
        while (true) {
            SoftValueRef ref = (SoftValueRef) this.queue.poll();
            if (ref != null) {
                if (ref == ((SoftValueRef) this.hash.get(ref.key))) {
                    this.hash.remove(ref.key);
                }
            } else {
                return;
            }
        }
    }

    public SoftValueHashMap(int initialCapacity, float loadFactor) {
        this.queue = new ReferenceQueue();
        this.hash = new HashMap(initialCapacity, loadFactor);
    }

    public SoftValueHashMap(int initialCapacity) {
        this.queue = new ReferenceQueue();
        this.hash = new HashMap(initialCapacity);
    }

    public SoftValueHashMap() {
        this.queue = new ReferenceQueue();
        this.hash = new HashMap();
    }

    public SoftValueHashMap(Map t) {
        this(Math.max(2 * t.size(), 11), 0.75f);
        putAll(t);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        processQueue();
        return this.hash.size();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean isEmpty() {
        processQueue();
        return this.hash.isEmpty();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object key) {
        processQueue();
        return this.hash.containsKey(key);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object get(Object key) {
        processQueue();
        SoftReference ref = (SoftReference) this.hash.get(key);
        if (ref != null) {
            return ref.get();
        }
        return null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object put(Object key, Object value) {
        processQueue();
        Object rtn = this.hash.put(key, SoftValueRef.create(key, value, this.queue));
        if (rtn != null) {
            rtn = ((SoftReference) rtn).get();
        }
        return rtn;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object remove(Object key) {
        processQueue();
        return this.hash.remove(key);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        processQueue();
        this.hash.clear();
    }
}
