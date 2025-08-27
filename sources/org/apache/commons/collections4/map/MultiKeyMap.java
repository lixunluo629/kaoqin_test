package org.apache.commons.collections4.map;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.AbstractHashedMap;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/MultiKeyMap.class */
public class MultiKeyMap<K, V> extends AbstractMapDecorator<MultiKey<? extends K>, V> implements Serializable, Cloneable {
    private static final long serialVersionUID = -1788199231038721040L;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.collections4.map.AbstractMapDecorator, java.util.Map, org.apache.commons.collections4.Put
    public /* bridge */ /* synthetic */ Object put(Object x0, Object obj) {
        return put((MultiKey) x0, (MultiKey<? extends K>) obj);
    }

    public static <K, V> MultiKeyMap<K, V> multiKeyMap(AbstractHashedMap<MultiKey<? extends K>, V> map) {
        if (map == null) {
            throw new NullPointerException("Map must not be null");
        }
        if (map.size() > 0) {
            throw new IllegalArgumentException("Map must be empty");
        }
        return new MultiKeyMap<>(map);
    }

    public MultiKeyMap() {
        this(new HashedMap());
    }

    protected MultiKeyMap(AbstractHashedMap<MultiKey<? extends K>, V> map) {
        super(map);
        this.map = map;
    }

    public V get(Object key1, Object key2) {
        int hashCode = hash(key1, key2);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> hashEntry = decorated().data[decorated().hashIndex(hashCode, decorated().data.length)];
        while (true) {
            AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = hashEntry;
            if (entry != null) {
                if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2)) {
                    return entry.getValue();
                }
                hashEntry = entry.next;
            } else {
                return null;
            }
        }
    }

    public boolean containsKey(Object key1, Object key2) {
        int hashCode = hash(key1, key2);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> hashEntry = decorated().data[decorated().hashIndex(hashCode, decorated().data.length)];
        while (true) {
            AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = hashEntry;
            if (entry != null) {
                if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2)) {
                    return true;
                }
                hashEntry = entry.next;
            } else {
                return false;
            }
        }
    }

    public V put(K key1, K key2, V value) {
        int hashCode = hash(key1, key2);
        int index = decorated().hashIndex(hashCode, decorated().data.length);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> hashEntry = decorated().data[index];
        while (true) {
            AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = hashEntry;
            if (entry != null) {
                if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2)) {
                    V oldValue = entry.getValue();
                    decorated().updateEntry(entry, value);
                    return oldValue;
                }
                hashEntry = entry.next;
            } else {
                decorated().addMapping(index, hashCode, new MultiKey<>(key1, key2), value);
                return null;
            }
        }
    }

    public V removeMultiKey(Object key1, Object key2) {
        int hashCode = hash(key1, key2);
        int index = decorated().hashIndex(hashCode, decorated().data.length);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> previous = null;
        for (AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = decorated().data[index]; entry != null; entry = entry.next) {
            if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2)) {
                V oldValue = entry.getValue();
                decorated().removeMapping(entry, index, previous);
                return oldValue;
            }
            previous = entry;
        }
        return null;
    }

    protected int hash(Object key1, Object key2) {
        int h = 0;
        if (key1 != null) {
            h = 0 ^ key1.hashCode();
        }
        if (key2 != null) {
            h ^= key2.hashCode();
        }
        int h2 = h + ((h << 9) ^ (-1));
        int h3 = h2 ^ (h2 >>> 14);
        int h4 = h3 + (h3 << 4);
        return h4 ^ (h4 >>> 10);
    }

    protected boolean isEqualKey(AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry, Object key1, Object key2) {
        MultiKey<? extends K> multi = entry.getKey();
        return multi.size() == 2 && (key1 == multi.getKey(0) || (key1 != null && key1.equals(multi.getKey(0)))) && (key2 == multi.getKey(1) || (key2 != null && key2.equals(multi.getKey(1))));
    }

    public V get(Object key1, Object key2, Object key3) {
        int hashCode = hash(key1, key2, key3);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> hashEntry = decorated().data[decorated().hashIndex(hashCode, decorated().data.length)];
        while (true) {
            AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = hashEntry;
            if (entry != null) {
                if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3)) {
                    return entry.getValue();
                }
                hashEntry = entry.next;
            } else {
                return null;
            }
        }
    }

    public boolean containsKey(Object key1, Object key2, Object key3) {
        int hashCode = hash(key1, key2, key3);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> hashEntry = decorated().data[decorated().hashIndex(hashCode, decorated().data.length)];
        while (true) {
            AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = hashEntry;
            if (entry != null) {
                if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3)) {
                    return true;
                }
                hashEntry = entry.next;
            } else {
                return false;
            }
        }
    }

    public V put(K key1, K key2, K key3, V value) {
        int hashCode = hash(key1, key2, key3);
        int index = decorated().hashIndex(hashCode, decorated().data.length);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> hashEntry = decorated().data[index];
        while (true) {
            AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = hashEntry;
            if (entry != null) {
                if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3)) {
                    V oldValue = entry.getValue();
                    decorated().updateEntry(entry, value);
                    return oldValue;
                }
                hashEntry = entry.next;
            } else {
                decorated().addMapping(index, hashCode, new MultiKey<>(key1, key2, key3), value);
                return null;
            }
        }
    }

    public V removeMultiKey(Object key1, Object key2, Object key3) {
        int hashCode = hash(key1, key2, key3);
        int index = decorated().hashIndex(hashCode, decorated().data.length);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> previous = null;
        for (AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = decorated().data[index]; entry != null; entry = entry.next) {
            if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3)) {
                V oldValue = entry.getValue();
                decorated().removeMapping(entry, index, previous);
                return oldValue;
            }
            previous = entry;
        }
        return null;
    }

    protected int hash(Object key1, Object key2, Object key3) {
        int h = 0;
        if (key1 != null) {
            h = 0 ^ key1.hashCode();
        }
        if (key2 != null) {
            h ^= key2.hashCode();
        }
        if (key3 != null) {
            h ^= key3.hashCode();
        }
        int h2 = h + ((h << 9) ^ (-1));
        int h3 = h2 ^ (h2 >>> 14);
        int h4 = h3 + (h3 << 4);
        return h4 ^ (h4 >>> 10);
    }

    protected boolean isEqualKey(AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry, Object key1, Object key2, Object key3) {
        MultiKey<? extends K> multi = entry.getKey();
        return multi.size() == 3 && (key1 == multi.getKey(0) || (key1 != null && key1.equals(multi.getKey(0)))) && ((key2 == multi.getKey(1) || (key2 != null && key2.equals(multi.getKey(1)))) && (key3 == multi.getKey(2) || (key3 != null && key3.equals(multi.getKey(2)))));
    }

    public V get(Object key1, Object key2, Object key3, Object key4) {
        int hashCode = hash(key1, key2, key3, key4);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> hashEntry = decorated().data[decorated().hashIndex(hashCode, decorated().data.length)];
        while (true) {
            AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = hashEntry;
            if (entry != null) {
                if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3, key4)) {
                    return entry.getValue();
                }
                hashEntry = entry.next;
            } else {
                return null;
            }
        }
    }

    public boolean containsKey(Object key1, Object key2, Object key3, Object key4) {
        int hashCode = hash(key1, key2, key3, key4);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> hashEntry = decorated().data[decorated().hashIndex(hashCode, decorated().data.length)];
        while (true) {
            AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = hashEntry;
            if (entry != null) {
                if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3, key4)) {
                    return true;
                }
                hashEntry = entry.next;
            } else {
                return false;
            }
        }
    }

    public V put(K key1, K key2, K key3, K key4, V value) {
        int hashCode = hash(key1, key2, key3, key4);
        int index = decorated().hashIndex(hashCode, decorated().data.length);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> hashEntry = decorated().data[index];
        while (true) {
            AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = hashEntry;
            if (entry != null) {
                if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3, key4)) {
                    V oldValue = entry.getValue();
                    decorated().updateEntry(entry, value);
                    return oldValue;
                }
                hashEntry = entry.next;
            } else {
                decorated().addMapping(index, hashCode, new MultiKey<>(key1, key2, key3, key4), value);
                return null;
            }
        }
    }

    public V removeMultiKey(Object key1, Object key2, Object key3, Object key4) {
        int hashCode = hash(key1, key2, key3, key4);
        int index = decorated().hashIndex(hashCode, decorated().data.length);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> previous = null;
        for (AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = decorated().data[index]; entry != null; entry = entry.next) {
            if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3, key4)) {
                V oldValue = entry.getValue();
                decorated().removeMapping(entry, index, previous);
                return oldValue;
            }
            previous = entry;
        }
        return null;
    }

    protected int hash(Object key1, Object key2, Object key3, Object key4) {
        int h = 0;
        if (key1 != null) {
            h = 0 ^ key1.hashCode();
        }
        if (key2 != null) {
            h ^= key2.hashCode();
        }
        if (key3 != null) {
            h ^= key3.hashCode();
        }
        if (key4 != null) {
            h ^= key4.hashCode();
        }
        int h2 = h + ((h << 9) ^ (-1));
        int h3 = h2 ^ (h2 >>> 14);
        int h4 = h3 + (h3 << 4);
        return h4 ^ (h4 >>> 10);
    }

    protected boolean isEqualKey(AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry, Object key1, Object key2, Object key3, Object key4) {
        MultiKey<? extends K> multi = entry.getKey();
        return multi.size() == 4 && (key1 == multi.getKey(0) || (key1 != null && key1.equals(multi.getKey(0)))) && ((key2 == multi.getKey(1) || (key2 != null && key2.equals(multi.getKey(1)))) && ((key3 == multi.getKey(2) || (key3 != null && key3.equals(multi.getKey(2)))) && (key4 == multi.getKey(3) || (key4 != null && key4.equals(multi.getKey(3))))));
    }

    public V get(Object key1, Object key2, Object key3, Object key4, Object key5) {
        int hashCode = hash(key1, key2, key3, key4, key5);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> hashEntry = decorated().data[decorated().hashIndex(hashCode, decorated().data.length)];
        while (true) {
            AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = hashEntry;
            if (entry != null) {
                if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3, key4, key5)) {
                    return entry.getValue();
                }
                hashEntry = entry.next;
            } else {
                return null;
            }
        }
    }

    public boolean containsKey(Object key1, Object key2, Object key3, Object key4, Object key5) {
        int hashCode = hash(key1, key2, key3, key4, key5);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> hashEntry = decorated().data[decorated().hashIndex(hashCode, decorated().data.length)];
        while (true) {
            AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = hashEntry;
            if (entry != null) {
                if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3, key4, key5)) {
                    return true;
                }
                hashEntry = entry.next;
            } else {
                return false;
            }
        }
    }

    public V put(K key1, K key2, K key3, K key4, K key5, V value) {
        int hashCode = hash(key1, key2, key3, key4, key5);
        int index = decorated().hashIndex(hashCode, decorated().data.length);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> hashEntry = decorated().data[index];
        while (true) {
            AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = hashEntry;
            if (entry != null) {
                if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3, key4, key5)) {
                    V oldValue = entry.getValue();
                    decorated().updateEntry(entry, value);
                    return oldValue;
                }
                hashEntry = entry.next;
            } else {
                decorated().addMapping(index, hashCode, new MultiKey<>(key1, key2, key3, key4, key5), value);
                return null;
            }
        }
    }

    public V removeMultiKey(Object key1, Object key2, Object key3, Object key4, Object key5) {
        int hashCode = hash(key1, key2, key3, key4, key5);
        int index = decorated().hashIndex(hashCode, decorated().data.length);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> previous = null;
        for (AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = decorated().data[index]; entry != null; entry = entry.next) {
            if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3, key4, key5)) {
                V oldValue = entry.getValue();
                decorated().removeMapping(entry, index, previous);
                return oldValue;
            }
            previous = entry;
        }
        return null;
    }

    protected int hash(Object key1, Object key2, Object key3, Object key4, Object key5) {
        int h = 0;
        if (key1 != null) {
            h = 0 ^ key1.hashCode();
        }
        if (key2 != null) {
            h ^= key2.hashCode();
        }
        if (key3 != null) {
            h ^= key3.hashCode();
        }
        if (key4 != null) {
            h ^= key4.hashCode();
        }
        if (key5 != null) {
            h ^= key5.hashCode();
        }
        int h2 = h + ((h << 9) ^ (-1));
        int h3 = h2 ^ (h2 >>> 14);
        int h4 = h3 + (h3 << 4);
        return h4 ^ (h4 >>> 10);
    }

    protected boolean isEqualKey(AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry, Object key1, Object key2, Object key3, Object key4, Object key5) {
        MultiKey<? extends K> multi = entry.getKey();
        return multi.size() == 5 && (key1 == multi.getKey(0) || (key1 != null && key1.equals(multi.getKey(0)))) && ((key2 == multi.getKey(1) || (key2 != null && key2.equals(multi.getKey(1)))) && ((key3 == multi.getKey(2) || (key3 != null && key3.equals(multi.getKey(2)))) && ((key4 == multi.getKey(3) || (key4 != null && key4.equals(multi.getKey(3)))) && (key5 == multi.getKey(4) || (key5 != null && key5.equals(multi.getKey(4)))))));
    }

    public boolean removeAll(Object key1) {
        boolean modified = false;
        MapIterator<MultiKey<? extends K>, V> it = mapIterator();
        while (it.hasNext()) {
            MultiKey<? extends K> multi = it.next();
            if (multi.size() >= 1) {
                if (key1 == null) {
                    if (multi.getKey(0) == null) {
                        it.remove();
                        modified = true;
                    }
                } else if (key1.equals(multi.getKey(0))) {
                    it.remove();
                    modified = true;
                }
            }
        }
        return modified;
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x0054 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0048 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean removeAll(java.lang.Object r5, java.lang.Object r6) {
        /*
            r4 = this;
            r0 = 0
            r7 = r0
            r0 = r4
            org.apache.commons.collections4.MapIterator r0 = r0.mapIterator()
            r8 = r0
        L8:
            r0 = r8
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto L6d
            r0 = r8
            java.lang.Object r0 = r0.next()
            org.apache.commons.collections4.keyvalue.MultiKey r0 = (org.apache.commons.collections4.keyvalue.MultiKey) r0
            r9 = r0
            r0 = r9
            int r0 = r0.size()
            r1 = 2
            if (r0 < r1) goto L6a
            r0 = r5
            if (r0 != 0) goto L37
            r0 = r9
            r1 = 0
            java.lang.Object r0 = r0.getKey(r1)
            if (r0 != 0) goto L6a
            goto L44
        L37:
            r0 = r5
            r1 = r9
            r2 = 0
            java.lang.Object r1 = r1.getKey(r2)
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L6a
        L44:
            r0 = r6
            if (r0 != 0) goto L54
            r0 = r9
            r1 = 1
            java.lang.Object r0 = r0.getKey(r1)
            if (r0 != 0) goto L6a
            goto L61
        L54:
            r0 = r6
            r1 = r9
            r2 = 1
            java.lang.Object r1 = r1.getKey(r2)
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L6a
        L61:
            r0 = r8
            r0.remove()
            r0 = 1
            r7 = r0
        L6a:
            goto L8
        L6d:
            r0 = r7
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.collections4.map.MultiKeyMap.removeAll(java.lang.Object, java.lang.Object):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x0055 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0072 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0066 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0049 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean removeAll(java.lang.Object r5, java.lang.Object r6, java.lang.Object r7) {
        /*
            r4 = this;
            r0 = 0
            r8 = r0
            r0 = r4
            org.apache.commons.collections4.MapIterator r0 = r0.mapIterator()
            r9 = r0
        L9:
            r0 = r9
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto L8c
            r0 = r9
            java.lang.Object r0 = r0.next()
            org.apache.commons.collections4.keyvalue.MultiKey r0 = (org.apache.commons.collections4.keyvalue.MultiKey) r0
            r10 = r0
            r0 = r10
            int r0 = r0.size()
            r1 = 3
            if (r0 < r1) goto L89
            r0 = r5
            if (r0 != 0) goto L38
            r0 = r10
            r1 = 0
            java.lang.Object r0 = r0.getKey(r1)
            if (r0 != 0) goto L89
            goto L45
        L38:
            r0 = r5
            r1 = r10
            r2 = 0
            java.lang.Object r1 = r1.getKey(r2)
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L89
        L45:
            r0 = r6
            if (r0 != 0) goto L55
            r0 = r10
            r1 = 1
            java.lang.Object r0 = r0.getKey(r1)
            if (r0 != 0) goto L89
            goto L62
        L55:
            r0 = r6
            r1 = r10
            r2 = 1
            java.lang.Object r1 = r1.getKey(r2)
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L89
        L62:
            r0 = r7
            if (r0 != 0) goto L72
            r0 = r10
            r1 = 2
            java.lang.Object r0 = r0.getKey(r1)
            if (r0 != 0) goto L89
            goto L7f
        L72:
            r0 = r7
            r1 = r10
            r2 = 2
            java.lang.Object r1 = r1.getKey(r2)
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L89
        L7f:
            r0 = r9
            r0.remove()
            r0 = 1
            r8 = r0
        L89:
            goto L9
        L8c:
            r0 = r8
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.collections4.map.MultiKeyMap.removeAll(java.lang.Object, java.lang.Object, java.lang.Object):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:43:0x0055 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0072 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0090 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0084 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0066 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0049 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean removeAll(java.lang.Object r5, java.lang.Object r6, java.lang.Object r7, java.lang.Object r8) {
        /*
            r4 = this;
            r0 = 0
            r9 = r0
            r0 = r4
            org.apache.commons.collections4.MapIterator r0 = r0.mapIterator()
            r10 = r0
        L9:
            r0 = r10
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto Lab
            r0 = r10
            java.lang.Object r0 = r0.next()
            org.apache.commons.collections4.keyvalue.MultiKey r0 = (org.apache.commons.collections4.keyvalue.MultiKey) r0
            r11 = r0
            r0 = r11
            int r0 = r0.size()
            r1 = 4
            if (r0 < r1) goto La8
            r0 = r5
            if (r0 != 0) goto L38
            r0 = r11
            r1 = 0
            java.lang.Object r0 = r0.getKey(r1)
            if (r0 != 0) goto La8
            goto L45
        L38:
            r0 = r5
            r1 = r11
            r2 = 0
            java.lang.Object r1 = r1.getKey(r2)
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto La8
        L45:
            r0 = r6
            if (r0 != 0) goto L55
            r0 = r11
            r1 = 1
            java.lang.Object r0 = r0.getKey(r1)
            if (r0 != 0) goto La8
            goto L62
        L55:
            r0 = r6
            r1 = r11
            r2 = 1
            java.lang.Object r1 = r1.getKey(r2)
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto La8
        L62:
            r0 = r7
            if (r0 != 0) goto L72
            r0 = r11
            r1 = 2
            java.lang.Object r0 = r0.getKey(r1)
            if (r0 != 0) goto La8
            goto L7f
        L72:
            r0 = r7
            r1 = r11
            r2 = 2
            java.lang.Object r1 = r1.getKey(r2)
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto La8
        L7f:
            r0 = r8
            if (r0 != 0) goto L90
            r0 = r11
            r1 = 3
            java.lang.Object r0 = r0.getKey(r1)
            if (r0 != 0) goto La8
            goto L9e
        L90:
            r0 = r8
            r1 = r11
            r2 = 3
            java.lang.Object r1 = r1.getKey(r2)
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto La8
        L9e:
            r0 = r10
            r0.remove()
            r0 = 1
            r9 = r0
        La8:
            goto L9
        Lab:
            r0 = r9
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.collections4.map.MultiKeyMap.removeAll(java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object):boolean");
    }

    protected void checkKey(MultiKey<?> key) {
        if (key == null) {
            throw new NullPointerException("Key must not be null");
        }
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public MultiKeyMap<K, V> m2795clone() {
        try {
            return (MultiKeyMap) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public V put(MultiKey<? extends K> multiKey, V v) {
        checkKey(multiKey);
        return (V) super.put((MultiKeyMap<K, V>) multiKey, (MultiKey<? extends K>) v);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.collections4.map.AbstractMapDecorator, java.util.Map, org.apache.commons.collections4.Put
    public void putAll(Map<? extends MultiKey<? extends K>, ? extends V> map) {
        for (MultiKey<? extends K> key : map.keySet()) {
            checkKey(key);
        }
        super.putAll(map);
    }

    @Override // org.apache.commons.collections4.map.AbstractIterableMap, org.apache.commons.collections4.IterableGet
    public MapIterator<MultiKey<? extends K>, V> mapIterator() {
        return decorated().mapIterator();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.collections4.map.AbstractMapDecorator
    public AbstractHashedMap<MultiKey<? extends K>, V> decorated() {
        return (AbstractHashedMap) super.decorated();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(this.map);
    }

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        this.map = (Map) in.readObject();
    }
}
