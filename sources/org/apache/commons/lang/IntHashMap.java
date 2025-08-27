package org.apache.commons.lang;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/IntHashMap.class */
class IntHashMap {
    private transient Entry[] table;
    private transient int count;
    private int threshold;
    private final float loadFactor;

    /* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/IntHashMap$Entry.class */
    private static class Entry {
        final int hash;
        final int key;
        Object value;
        Entry next;

        protected Entry(int hash, int key, Object value, Entry next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public IntHashMap() {
        this(20, 0.75f);
    }

    public IntHashMap(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    public IntHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException(new StringBuffer().append("Illegal Capacity: ").append(initialCapacity).toString());
        }
        if (loadFactor <= 0.0f) {
            throw new IllegalArgumentException(new StringBuffer().append("Illegal Load: ").append(loadFactor).toString());
        }
        initialCapacity = initialCapacity == 0 ? 1 : initialCapacity;
        this.loadFactor = loadFactor;
        this.table = new Entry[initialCapacity];
        this.threshold = (int) (initialCapacity * loadFactor);
    }

    public int size() {
        return this.count;
    }

    public boolean isEmpty() {
        return this.count == 0;
    }

    public boolean contains(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        Entry[] tab = this.table;
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

    public boolean containsValue(Object value) {
        return contains(value);
    }

    public boolean containsKey(int key) {
        Entry[] tab = this.table;
        int index = (key & Integer.MAX_VALUE) % tab.length;
        Entry entry = tab[index];
        while (true) {
            Entry e = entry;
            if (e != null) {
                if (e.hash != key) {
                    entry = e.next;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    public Object get(int key) {
        Entry[] tab = this.table;
        int index = (key & Integer.MAX_VALUE) % tab.length;
        Entry entry = tab[index];
        while (true) {
            Entry e = entry;
            if (e != null) {
                if (e.hash != key) {
                    entry = e.next;
                } else {
                    return e.value;
                }
            } else {
                return null;
            }
        }
    }

    protected void rehash() {
        int oldCapacity = this.table.length;
        Entry[] oldMap = this.table;
        int newCapacity = (oldCapacity * 2) + 1;
        Entry[] newMap = new Entry[newCapacity];
        this.threshold = (int) (newCapacity * this.loadFactor);
        this.table = newMap;
        int i = oldCapacity;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                Entry old = oldMap[i];
                while (old != null) {
                    Entry e = old;
                    old = old.next;
                    int index = (e.hash & Integer.MAX_VALUE) % newCapacity;
                    e.next = newMap[index];
                    newMap[index] = e;
                }
            } else {
                return;
            }
        }
    }

    public Object put(int key, Object value) {
        Entry[] tab = this.table;
        int index = (key & Integer.MAX_VALUE) % tab.length;
        Entry entry = tab[index];
        while (true) {
            Entry e = entry;
            if (e != null) {
                if (e.hash != key) {
                    entry = e.next;
                } else {
                    Object old = e.value;
                    e.value = value;
                    return old;
                }
            } else {
                if (this.count >= this.threshold) {
                    rehash();
                    tab = this.table;
                    index = (key & Integer.MAX_VALUE) % tab.length;
                }
                tab[index] = new Entry(key, key, value, tab[index]);
                this.count++;
                return null;
            }
        }
    }

    public Object remove(int key) {
        Entry[] tab = this.table;
        int index = (key & Integer.MAX_VALUE) % tab.length;
        Entry prev = null;
        for (Entry e = tab[index]; e != null; e = e.next) {
            if (e.hash != key) {
                prev = e;
            } else {
                if (prev != null) {
                    prev.next = e.next;
                } else {
                    tab[index] = e.next;
                }
                this.count--;
                Object oldValue = e.value;
                e.value = null;
                return oldValue;
            }
        }
        return null;
    }

    public synchronized void clear() {
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
}
