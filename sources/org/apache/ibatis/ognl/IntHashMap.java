package org.apache.ibatis.ognl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import redis.clients.jedis.Protocol;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/IntHashMap.class */
public class IntHashMap implements Map {
    private Entry[] table;
    private int count;
    private int threshold;
    private float loadFactor;

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/IntHashMap$Entry.class */
    public static class Entry {
        int hash;
        int key;
        Object value;
        Entry next;
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/IntHashMap$IntHashMapIterator.class */
    private static class IntHashMapIterator implements Iterator {
        boolean keys;
        int index;
        Entry[] table;
        Entry entry;

        IntHashMapIterator(Entry[] table, boolean keys) {
            this.table = table;
            this.keys = keys;
            this.index = table.length;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            Entry entry;
            if (this.entry != null) {
                return true;
            }
            do {
                int i = this.index;
                this.index = i - 1;
                if (i > 0) {
                    entry = this.table[this.index];
                    this.entry = entry;
                } else {
                    return false;
                }
            } while (entry == null);
            return true;
        }

        @Override // java.util.Iterator
        public Object next() {
            Entry entry;
            if (this.entry == null) {
                do {
                    int i = this.index;
                    this.index = i - 1;
                    if (i <= 0) {
                        break;
                    }
                    entry = this.table[this.index];
                    this.entry = entry;
                } while (entry == null);
            }
            if (this.entry != null) {
                Entry e = this.entry;
                this.entry = e.next;
                return this.keys ? new Integer(e.key) : e.value;
            }
            throw new NoSuchElementException("IntHashMapIterator");
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException(Protocol.SENTINEL_REMOVE);
        }
    }

    public IntHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0 || loadFactor <= 0.0d) {
            throw new IllegalArgumentException();
        }
        this.loadFactor = loadFactor;
        this.table = new Entry[initialCapacity];
        this.threshold = (int) (initialCapacity * loadFactor);
    }

    public IntHashMap(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    public IntHashMap() {
        this(101, 0.75f);
    }

    protected void rehash() {
        int oldCapacity = this.table.length;
        Entry[] oldTable = this.table;
        int newCapacity = (oldCapacity * 2) + 1;
        Entry[] newTable = new Entry[newCapacity];
        this.threshold = (int) (newCapacity * this.loadFactor);
        this.table = newTable;
        int i = oldCapacity;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                Entry old = oldTable[i];
                while (old != null) {
                    Entry e = old;
                    int index = (e.hash & Integer.MAX_VALUE) % newCapacity;
                    old = old.next;
                    e.next = newTable[index];
                    newTable[index] = e;
                }
            } else {
                return;
            }
        }
    }

    public final boolean containsKey(int key) {
        int index = (key & Integer.MAX_VALUE) % this.table.length;
        Entry entry = this.table[index];
        while (true) {
            Entry e = entry;
            if (e != null) {
                if (e.hash != key || e.key != key) {
                    entry = e.next;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    public final Object get(int key) {
        int index = (key & Integer.MAX_VALUE) % this.table.length;
        Entry entry = this.table[index];
        while (true) {
            Entry e = entry;
            if (e != null) {
                if (e.hash != key || e.key != key) {
                    entry = e.next;
                } else {
                    return e.value;
                }
            } else {
                return null;
            }
        }
    }

    public final Object put(int key, Object value) {
        int index = (key & Integer.MAX_VALUE) % this.table.length;
        if (value == null) {
            throw new IllegalArgumentException();
        }
        Entry entry = this.table[index];
        while (true) {
            Entry e = entry;
            if (e != null) {
                if (e.hash != key || e.key != key) {
                    entry = e.next;
                } else {
                    Object old = e.value;
                    e.value = value;
                    return old;
                }
            } else {
                if (this.count >= this.threshold) {
                    rehash();
                    return put(key, value);
                }
                Entry e2 = new Entry();
                e2.hash = key;
                e2.key = key;
                e2.value = value;
                e2.next = this.table[index];
                this.table[index] = e2;
                this.count++;
                return null;
            }
        }
    }

    public final Object remove(int key) {
        int index = (key & Integer.MAX_VALUE) % this.table.length;
        Entry prev = null;
        for (Entry e = this.table[index]; e != null; e = e.next) {
            if (e.hash != key || e.key != key) {
                prev = e;
            } else {
                if (prev != null) {
                    prev.next = e.next;
                } else {
                    this.table[index] = e.next;
                }
                this.count--;
                return e.value;
            }
        }
        return null;
    }

    @Override // java.util.Map
    public int size() {
        return this.count;
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.count == 0;
    }

    @Override // java.util.Map
    public Object get(Object key) {
        if (!(key instanceof Number)) {
            throw new IllegalArgumentException("key is not an Number subclass");
        }
        return get(((Number) key).intValue());
    }

    @Override // java.util.Map
    public Object put(Object key, Object value) {
        if (!(key instanceof Number)) {
            throw new IllegalArgumentException("key cannot be null");
        }
        return put(((Number) key).intValue(), value);
    }

    @Override // java.util.Map
    public void putAll(Map otherMap) {
        for (Object k : otherMap.keySet()) {
            put(k, otherMap.get(k));
        }
    }

    @Override // java.util.Map
    public Object remove(Object key) {
        if (!(key instanceof Number)) {
            throw new IllegalArgumentException("key cannot be null");
        }
        return remove(((Number) key).intValue());
    }

    @Override // java.util.Map
    public void clear() {
        Entry[] tab = this.table;
        int index = tab.length;
        while (true) {
            index--;
            if (index >= 0) {
                tab[index] = null;
            } else {
                this.count = 0;
                return;
            }
        }
    }

    @Override // java.util.Map
    public boolean containsKey(Object key) {
        if (!(key instanceof Number)) {
            throw new InternalError("key is not an Number subclass");
        }
        return containsKey(((Number) key).intValue());
    }

    @Override // java.util.Map
    public boolean containsValue(Object value) {
        Entry[] tab = this.table;
        if (value == null) {
            throw new IllegalArgumentException();
        }
        int i = tab.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                Entry entry = tab[i];
                while (true) {
                    Entry e = entry;
                    if (e != null) {
                        if (!e.value.equals(value)) {
                            entry = e.next;
                        } else {
                            return true;
                        }
                    }
                }
            } else {
                return false;
            }
        }
    }

    @Override // java.util.Map
    public Set keySet() {
        Set result = new HashSet();
        Iterator it = new IntHashMapIterator(this.table, true);
        while (it.hasNext()) {
            result.add(it.next());
        }
        return result;
    }

    @Override // java.util.Map
    public Collection values() {
        List result = new ArrayList();
        Iterator it = new IntHashMapIterator(this.table, false);
        while (it.hasNext()) {
            result.add(it.next());
        }
        return result;
    }

    @Override // java.util.Map
    public Set entrySet() {
        throw new UnsupportedOperationException("entrySet");
    }
}
