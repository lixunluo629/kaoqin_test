package org.apache.ibatis.ognl.internal;

import java.util.Arrays;
import org.apache.ibatis.ognl.ClassCacheInspector;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/internal/ClassCacheImpl.class */
public class ClassCacheImpl implements ClassCache {
    private static final int TABLE_SIZE = 512;
    private static final int TABLE_SIZE_MASK = 511;
    private ClassCacheInspector _classInspector;
    private int _size = 0;
    private Entry[] _table = new Entry[512];

    @Override // org.apache.ibatis.ognl.internal.ClassCache
    public void setClassInspector(ClassCacheInspector inspector) {
        this._classInspector = inspector;
    }

    @Override // org.apache.ibatis.ognl.internal.ClassCache
    public void clear() {
        for (int i = 0; i < this._table.length; i++) {
            this._table[i] = null;
        }
        this._size = 0;
    }

    @Override // org.apache.ibatis.ognl.internal.ClassCache
    public int getSize() {
        return this._size;
    }

    @Override // org.apache.ibatis.ognl.internal.ClassCache
    public final Object get(Class key) {
        Object result = null;
        int i = key.hashCode() & 511;
        Entry entry = this._table[i];
        while (true) {
            Entry entry2 = entry;
            if (entry2 == null) {
                break;
            }
            if (entry2.key != key) {
                entry = entry2.next;
            } else {
                result = entry2.value;
                break;
            }
        }
        return result;
    }

    @Override // org.apache.ibatis.ognl.internal.ClassCache
    public final Object put(Class key, Object value) {
        if (this._classInspector != null && !this._classInspector.shouldCache(key)) {
            return value;
        }
        Object result = null;
        int i = key.hashCode() & 511;
        Entry entry = this._table[i];
        if (entry == null) {
            this._table[i] = new Entry(key, value);
            this._size++;
        } else if (entry.key == key) {
            result = entry.value;
            entry.value = value;
        } else {
            while (true) {
                if (entry.key == key) {
                    result = entry.value;
                    entry.value = value;
                    break;
                }
                if (entry.next == null) {
                    entry.next = new Entry(key, value);
                    break;
                }
                entry = entry.next;
            }
        }
        return result;
    }

    public String toString() {
        return "ClassCacheImpl[_table=" + (this._table == null ? null : Arrays.asList(this._table)) + "\n, _classInspector=" + this._classInspector + "\n, _size=" + this._size + "\n]";
    }
}
