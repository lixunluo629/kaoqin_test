package com.itextpdf.io.util;

import java.io.Serializable;
import java.util.Arrays;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/util/IntHashtable.class */
public class IntHashtable implements Cloneable, Serializable {
    private static final long serialVersionUID = 7354463962269093965L;
    private Entry[] table;
    private int count;
    private int threshold;
    private float loadFactor;

    public IntHashtable() {
        this(150, 0.75f);
    }

    public IntHashtable(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    public IntHashtable(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException(MessageFormatUtil.format("Illegal Capacity: {0}", Integer.valueOf(initialCapacity)));
        }
        if (loadFactor <= 0.0f) {
            throw new IllegalArgumentException(MessageFormatUtil.format("Illegal Load: {0}", Float.valueOf(loadFactor)));
        }
        initialCapacity = initialCapacity == 0 ? 1 : initialCapacity;
        this.loadFactor = loadFactor;
        this.table = new Entry[initialCapacity];
        this.threshold = (int) (initialCapacity * loadFactor);
    }

    public IntHashtable(IntHashtable o) {
        this(o.table.length, o.loadFactor);
    }

    public int size() {
        return this.count;
    }

    public boolean isEmpty() {
        return this.count == 0;
    }

    public boolean contains(int value) {
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
                        if (e.value != value) {
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

    public boolean containsValue(int value) {
        return contains(value);
    }

    public boolean containsKey(int key) {
        Entry[] tab = this.table;
        int index = (key & Integer.MAX_VALUE) % tab.length;
        Entry entry = tab[index];
        while (true) {
            Entry e = entry;
            if (e != null) {
                if (e.key != key) {
                    entry = e.next;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    public int get(int key) {
        Entry[] tab = this.table;
        int index = (key & Integer.MAX_VALUE) % tab.length;
        Entry entry = tab[index];
        while (true) {
            Entry e = entry;
            if (e != null) {
                if (e.key != key) {
                    entry = e.next;
                } else {
                    return e.value;
                }
            } else {
                return 0;
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
                    int index = (e.key & Integer.MAX_VALUE) % newCapacity;
                    e.next = newMap[index];
                    newMap[index] = e;
                }
            } else {
                return;
            }
        }
    }

    public int put(int key, int value) {
        Entry[] tab = this.table;
        int index = (key & Integer.MAX_VALUE) % tab.length;
        Entry entry = tab[index];
        while (true) {
            Entry e = entry;
            if (e != null) {
                if (e.key != key) {
                    entry = e.next;
                } else {
                    int old = e.value;
                    e.value = value;
                    return old;
                }
            } else {
                if (this.count >= this.threshold) {
                    rehash();
                    tab = this.table;
                    index = (key & Integer.MAX_VALUE) % tab.length;
                }
                tab[index] = new Entry(key, value, tab[index]);
                this.count++;
                return 0;
            }
        }
    }

    public int remove(int key) {
        Entry[] tab = this.table;
        int index = (key & Integer.MAX_VALUE) % tab.length;
        Entry prev = null;
        for (Entry e = tab[index]; e != null; e = e.next) {
            if (e.key != key) {
                prev = e;
            } else {
                if (prev != null) {
                    prev.next = e.next;
                } else {
                    tab[index] = e.next;
                }
                this.count--;
                int oldValue = e.value;
                e.value = 0;
                return oldValue;
            }
        }
        return 0;
    }

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

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/util/IntHashtable$Entry.class */
    public static class Entry implements Serializable {
        private static final long serialVersionUID = 8057670534065316193L;
        int key;
        int value;
        Entry next;

        Entry(int key, int value, Entry next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public int getKey() {
            return this.key;
        }

        public int getValue() {
            return this.value;
        }

        protected Object clone() throws CloneNotSupportedException {
            return new Entry(this.key, this.value, this.next != null ? (Entry) this.next.clone() : null);
        }

        public String toString() {
            return MessageFormatUtil.format("{0}={1}", Integer.valueOf(this.key), Integer.valueOf(this.value));
        }
    }

    public int[] toOrderedKeys() {
        int[] res = getKeys();
        Arrays.sort(res);
        return res;
    }

    public int[] getKeys() {
        Entry entry;
        int[] res = new int[this.count];
        int ptr = 0;
        int index = this.table.length;
        Entry entry2 = null;
        while (true) {
            if (entry2 == null) {
                do {
                    int i = index;
                    index--;
                    if (i <= 0) {
                        break;
                    }
                    entry = this.table[index];
                    entry2 = entry;
                } while (entry == null);
            }
            if (entry2 != null) {
                Entry e = entry2;
                entry2 = e.next;
                int i2 = ptr;
                ptr++;
                res[i2] = e.key;
            } else {
                return res;
            }
        }
    }

    public int getOneKey() {
        Entry entry;
        if (this.count == 0) {
            return 0;
        }
        int index = this.table.length;
        Entry entry2 = null;
        do {
            int i = index;
            index--;
            if (i <= 0) {
                break;
            }
            entry = this.table[index];
            entry2 = entry;
        } while (entry == null);
        if (entry2 == null) {
            return 0;
        }
        return entry2.key;
    }

    public Object clone() throws CloneNotSupportedException {
        try {
            IntHashtable t = new IntHashtable(this);
            t.table = new Entry[this.table.length];
            int i = this.table.length;
            while (true) {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    t.table[i] = this.table[i] != null ? (Entry) this.table[i].clone() : null;
                } else {
                    return t;
                }
            }
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }
}
