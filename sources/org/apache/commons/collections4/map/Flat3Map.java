package org.apache.commons.collections4.map;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.apache.commons.collections4.IterableMap;
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.ResettableIterator;
import org.apache.commons.collections4.iterators.EmptyIterator;
import org.apache.commons.collections4.iterators.EmptyMapIterator;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/Flat3Map.class */
public class Flat3Map<K, V> implements IterableMap<K, V>, Serializable, Cloneable {
    private static final long serialVersionUID = -6701087419741928296L;
    private transient int size;
    private transient int hash1;
    private transient int hash2;
    private transient int hash3;
    private transient K key1;
    private transient K key2;
    private transient K key3;
    private transient V value1;
    private transient V value2;
    private transient V value3;
    private transient AbstractHashedMap<K, V> delegateMap;

    public Flat3Map() {
    }

    public Flat3Map(Map<? extends K, ? extends V> map) {
        putAll(map);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0047  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0053  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:48:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:51:? A[RETURN, SYNTHETIC] */
    @Override // java.util.Map, org.apache.commons.collections4.Get
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public V get(java.lang.Object r4) {
        /*
            r3 = this;
            r0 = r3
            org.apache.commons.collections4.map.AbstractHashedMap<K, V> r0 = r0.delegateMap
            if (r0 == 0) goto L10
            r0 = r3
            org.apache.commons.collections4.map.AbstractHashedMap<K, V> r0 = r0.delegateMap
            r1 = r4
            java.lang.Object r0 = r0.get(r1)
            return r0
        L10:
            r0 = r4
            if (r0 != 0) goto L5b
            r0 = r3
            int r0 = r0.size
            switch(r0) {
                case 1: goto L4c;
                case 2: goto L40;
                case 3: goto L34;
                default: goto L58;
            }
        L34:
            r0 = r3
            K r0 = r0.key3
            if (r0 != 0) goto L40
            r0 = r3
            V r0 = r0.value3
            return r0
        L40:
            r0 = r3
            K r0 = r0.key2
            if (r0 != 0) goto L4c
            r0 = r3
            V r0 = r0.value2
            return r0
        L4c:
            r0 = r3
            K r0 = r0.key1
            if (r0 != 0) goto L58
            r0 = r3
            V r0 = r0.value1
            return r0
        L58:
            goto Lcc
        L5b:
            r0 = r3
            int r0 = r0.size
            if (r0 <= 0) goto Lcc
            r0 = r4
            int r0 = r0.hashCode()
            r5 = r0
            r0 = r3
            int r0 = r0.size
            switch(r0) {
                case 1: goto Lb4;
                case 2: goto L9c;
                case 3: goto L84;
                default: goto Lcc;
            }
        L84:
            r0 = r3
            int r0 = r0.hash3
            r1 = r5
            if (r0 != r1) goto L9c
            r0 = r4
            r1 = r3
            K r1 = r1.key3
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L9c
            r0 = r3
            V r0 = r0.value3
            return r0
        L9c:
            r0 = r3
            int r0 = r0.hash2
            r1 = r5
            if (r0 != r1) goto Lb4
            r0 = r4
            r1 = r3
            K r1 = r1.key2
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto Lb4
            r0 = r3
            V r0 = r0.value2
            return r0
        Lb4:
            r0 = r3
            int r0 = r0.hash1
            r1 = r5
            if (r0 != r1) goto Lcc
            r0 = r4
            r1 = r3
            K r1 = r1.key1
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto Lcc
            r0 = r3
            V r0 = r0.value1
            return r0
        Lcc:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.collections4.map.Flat3Map.get(java.lang.Object):java.lang.Object");
    }

    @Override // java.util.Map, org.apache.commons.collections4.Get
    public int size() {
        if (this.delegateMap != null) {
            return this.delegateMap.size();
        }
        return this.size;
    }

    @Override // java.util.Map, org.apache.commons.collections4.Get
    public boolean isEmpty() {
        return size() == 0;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0044 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x004d A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00ae  */
    /* JADX WARN: Removed duplicated region for block: B:48:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:51:? A[RETURN, SYNTHETIC] */
    @Override // java.util.Map, org.apache.commons.collections4.Get
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean containsKey(java.lang.Object r4) {
        /*
            r3 = this;
            r0 = r3
            org.apache.commons.collections4.map.AbstractHashedMap<K, V> r0 = r0.delegateMap
            if (r0 == 0) goto L10
            r0 = r3
            org.apache.commons.collections4.map.AbstractHashedMap<K, V> r0 = r0.delegateMap
            r1 = r4
            boolean r0 = r0.containsKey(r1)
            return r0
        L10:
            r0 = r4
            if (r0 != 0) goto L52
            r0 = r3
            int r0 = r0.size
            switch(r0) {
                case 1: goto L46;
                case 2: goto L3d;
                case 3: goto L34;
                default: goto L4f;
            }
        L34:
            r0 = r3
            K r0 = r0.key3
            if (r0 != 0) goto L3d
            r0 = 1
            return r0
        L3d:
            r0 = r3
            K r0 = r0.key2
            if (r0 != 0) goto L46
            r0 = 1
            return r0
        L46:
            r0 = r3
            K r0 = r0.key1
            if (r0 != 0) goto L4f
            r0 = 1
            return r0
        L4f:
            goto Lbb
        L52:
            r0 = r3
            int r0 = r0.size
            if (r0 <= 0) goto Lbb
            r0 = r4
            int r0 = r0.hashCode()
            r5 = r0
            r0 = r3
            int r0 = r0.size
            switch(r0) {
                case 1: goto La6;
                case 2: goto L91;
                case 3: goto L7c;
                default: goto Lbb;
            }
        L7c:
            r0 = r3
            int r0 = r0.hash3
            r1 = r5
            if (r0 != r1) goto L91
            r0 = r4
            r1 = r3
            K r1 = r1.key3
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L91
            r0 = 1
            return r0
        L91:
            r0 = r3
            int r0 = r0.hash2
            r1 = r5
            if (r0 != r1) goto La6
            r0 = r4
            r1 = r3
            K r1 = r1.key2
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto La6
            r0 = 1
            return r0
        La6:
            r0 = r3
            int r0 = r0.hash1
            r1 = r5
            if (r0 != r1) goto Lbb
            r0 = r4
            r1 = r3
            K r1 = r1.key1
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto Lbb
            r0 = 1
            return r0
        Lbb:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.collections4.map.Flat3Map.containsKey(java.lang.Object):boolean");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0044 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x004d A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0088 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0095 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:39:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:41:? A[RETURN, SYNTHETIC] */
    @Override // java.util.Map, org.apache.commons.collections4.Get
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean containsValue(java.lang.Object r4) {
        /*
            r3 = this;
            r0 = r3
            org.apache.commons.collections4.map.AbstractHashedMap<K, V> r0 = r0.delegateMap
            if (r0 == 0) goto L10
            r0 = r3
            org.apache.commons.collections4.map.AbstractHashedMap<K, V> r0 = r0.delegateMap
            r1 = r4
            boolean r0 = r0.containsValue(r1)
            return r0
        L10:
            r0 = r4
            if (r0 != 0) goto L52
            r0 = r3
            int r0 = r0.size
            switch(r0) {
                case 1: goto L46;
                case 2: goto L3d;
                case 3: goto L34;
                default: goto L4f;
            }
        L34:
            r0 = r3
            V r0 = r0.value3
            if (r0 != 0) goto L3d
            r0 = 1
            return r0
        L3d:
            r0 = r3
            V r0 = r0.value2
            if (r0 != 0) goto L46
            r0 = 1
            return r0
        L46:
            r0 = r3
            V r0 = r0.value1
            if (r0 != 0) goto L4f
            r0 = 1
            return r0
        L4f:
            goto L97
        L52:
            r0 = r3
            int r0 = r0.size
            switch(r0) {
                case 1: goto L8a;
                case 2: goto L7d;
                case 3: goto L70;
                default: goto L97;
            }
        L70:
            r0 = r4
            r1 = r3
            V r1 = r1.value3
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L7d
            r0 = 1
            return r0
        L7d:
            r0 = r4
            r1 = r3
            V r1 = r1.value2
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L8a
            r0 = 1
            return r0
        L8a:
            r0 = r4
            r1 = r3
            V r1 = r1.value1
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L97
            r0 = 1
            return r0
        L97:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.collections4.map.Flat3Map.containsValue(java.lang.Object):boolean");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:16:0x004e  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0061  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00e6  */
    @Override // java.util.Map, org.apache.commons.collections4.Put
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public V put(K r5, V r6) {
        /*
            Method dump skipped, instructions count: 396
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.collections4.map.Flat3Map.put(java.lang.Object, java.lang.Object):java.lang.Object");
    }

    @Override // java.util.Map, org.apache.commons.collections4.Put
    public void putAll(Map<? extends K, ? extends V> map) {
        int size = map.size();
        if (size == 0) {
            return;
        }
        if (this.delegateMap != null) {
            this.delegateMap.putAll(map);
            return;
        }
        if (size < 4) {
            for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
            return;
        }
        convertToMap();
        this.delegateMap.putAll(map);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private void convertToMap() {
        this.delegateMap = createDelegateMap();
        switch (this.size) {
            case 0:
                this.size = 0;
                this.hash3 = 0;
                this.hash2 = 0;
                this.hash1 = 0;
                this.key3 = null;
                this.key2 = null;
                this.key1 = null;
                this.value3 = null;
                this.value2 = null;
                this.value1 = null;
                return;
            case 1:
                this.delegateMap.put(this.key1, this.value1);
                this.size = 0;
                this.hash3 = 0;
                this.hash2 = 0;
                this.hash1 = 0;
                this.key3 = null;
                this.key2 = null;
                this.key1 = null;
                this.value3 = null;
                this.value2 = null;
                this.value1 = null;
                return;
            case 2:
                this.delegateMap.put(this.key2, this.value2);
                this.delegateMap.put(this.key1, this.value1);
                this.size = 0;
                this.hash3 = 0;
                this.hash2 = 0;
                this.hash1 = 0;
                this.key3 = null;
                this.key2 = null;
                this.key1 = null;
                this.value3 = null;
                this.value2 = null;
                this.value1 = null;
                return;
            case 3:
                this.delegateMap.put(this.key3, this.value3);
                this.delegateMap.put(this.key2, this.value2);
                this.delegateMap.put(this.key1, this.value1);
                this.size = 0;
                this.hash3 = 0;
                this.hash2 = 0;
                this.hash1 = 0;
                this.key3 = null;
                this.key2 = null;
                this.key1 = null;
                this.value3 = null;
                this.value2 = null;
                this.value1 = null;
                return;
            default:
                throw new IllegalStateException("Invalid map index: " + this.size);
        }
    }

    protected AbstractHashedMap<K, V> createDelegateMap() {
        return new HashedMap();
    }

    @Override // java.util.Map, org.apache.commons.collections4.Get
    public V remove(Object key) {
        if (this.delegateMap != null) {
            return this.delegateMap.remove(key);
        }
        if (this.size == 0) {
            return null;
        }
        if (key == null) {
            switch (this.size) {
                case 1:
                    if (this.key1 == null) {
                        V old = this.value1;
                        this.hash1 = 0;
                        this.key1 = null;
                        this.value1 = null;
                        this.size = 0;
                        return old;
                    }
                    return null;
                case 2:
                    if (this.key2 == null) {
                        V old2 = this.value2;
                        this.hash2 = 0;
                        this.key2 = null;
                        this.value2 = null;
                        this.size = 1;
                        return old2;
                    }
                    if (this.key1 == null) {
                        V old3 = this.value1;
                        this.hash1 = this.hash2;
                        this.key1 = this.key2;
                        this.value1 = this.value2;
                        this.hash2 = 0;
                        this.key2 = null;
                        this.value2 = null;
                        this.size = 1;
                        return old3;
                    }
                    return null;
                case 3:
                    if (this.key3 == null) {
                        V old4 = this.value3;
                        this.hash3 = 0;
                        this.key3 = null;
                        this.value3 = null;
                        this.size = 2;
                        return old4;
                    }
                    if (this.key2 == null) {
                        V old5 = this.value2;
                        this.hash2 = this.hash3;
                        this.key2 = this.key3;
                        this.value2 = this.value3;
                        this.hash3 = 0;
                        this.key3 = null;
                        this.value3 = null;
                        this.size = 2;
                        return old5;
                    }
                    if (this.key1 == null) {
                        V old6 = this.value1;
                        this.hash1 = this.hash3;
                        this.key1 = this.key3;
                        this.value1 = this.value3;
                        this.hash3 = 0;
                        this.key3 = null;
                        this.value3 = null;
                        this.size = 2;
                        return old6;
                    }
                    return null;
                default:
                    return null;
            }
        }
        if (this.size > 0) {
            int hashCode = key.hashCode();
            switch (this.size) {
                case 1:
                    if (this.hash1 == hashCode && key.equals(this.key1)) {
                        V old7 = this.value1;
                        this.hash1 = 0;
                        this.key1 = null;
                        this.value1 = null;
                        this.size = 0;
                        return old7;
                    }
                    return null;
                case 2:
                    if (this.hash2 == hashCode && key.equals(this.key2)) {
                        V old8 = this.value2;
                        this.hash2 = 0;
                        this.key2 = null;
                        this.value2 = null;
                        this.size = 1;
                        return old8;
                    }
                    if (this.hash1 == hashCode && key.equals(this.key1)) {
                        V old9 = this.value1;
                        this.hash1 = this.hash2;
                        this.key1 = this.key2;
                        this.value1 = this.value2;
                        this.hash2 = 0;
                        this.key2 = null;
                        this.value2 = null;
                        this.size = 1;
                        return old9;
                    }
                    return null;
                case 3:
                    if (this.hash3 == hashCode && key.equals(this.key3)) {
                        V old10 = this.value3;
                        this.hash3 = 0;
                        this.key3 = null;
                        this.value3 = null;
                        this.size = 2;
                        return old10;
                    }
                    if (this.hash2 == hashCode && key.equals(this.key2)) {
                        V old11 = this.value2;
                        this.hash2 = this.hash3;
                        this.key2 = this.key3;
                        this.value2 = this.value3;
                        this.hash3 = 0;
                        this.key3 = null;
                        this.value3 = null;
                        this.size = 2;
                        return old11;
                    }
                    if (this.hash1 == hashCode && key.equals(this.key1)) {
                        V old12 = this.value1;
                        this.hash1 = this.hash3;
                        this.key1 = this.key3;
                        this.value1 = this.value3;
                        this.hash3 = 0;
                        this.key3 = null;
                        this.value3 = null;
                        this.size = 2;
                        return old12;
                    }
                    return null;
                default:
                    return null;
            }
        }
        return null;
    }

    @Override // java.util.Map, org.apache.commons.collections4.Put
    public void clear() {
        if (this.delegateMap != null) {
            this.delegateMap.clear();
            this.delegateMap = null;
            return;
        }
        this.size = 0;
        this.hash3 = 0;
        this.hash2 = 0;
        this.hash1 = 0;
        this.key3 = null;
        this.key2 = null;
        this.key1 = null;
        this.value3 = null;
        this.value2 = null;
        this.value1 = null;
    }

    @Override // org.apache.commons.collections4.IterableGet
    public MapIterator<K, V> mapIterator() {
        if (this.delegateMap != null) {
            return this.delegateMap.mapIterator();
        }
        if (this.size == 0) {
            return EmptyMapIterator.emptyMapIterator();
        }
        return new FlatMapIterator(this);
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/Flat3Map$FlatMapIterator.class */
    static class FlatMapIterator<K, V> implements MapIterator<K, V>, ResettableIterator<K> {
        private final Flat3Map<K, V> parent;
        private int nextIndex = 0;
        private boolean canRemove = false;

        FlatMapIterator(Flat3Map<K, V> parent) {
            this.parent = parent;
        }

        @Override // org.apache.commons.collections4.MapIterator, java.util.Iterator
        public boolean hasNext() {
            return this.nextIndex < ((Flat3Map) this.parent).size;
        }

        @Override // org.apache.commons.collections4.MapIterator, java.util.Iterator
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No next() entry in the iteration");
            }
            this.canRemove = true;
            this.nextIndex++;
            return getKey();
        }

        @Override // org.apache.commons.collections4.MapIterator, java.util.Iterator
        public void remove() {
            if (!this.canRemove) {
                throw new IllegalStateException("remove() can only be called once after next()");
            }
            this.parent.remove(getKey());
            this.nextIndex--;
            this.canRemove = false;
        }

        @Override // org.apache.commons.collections4.MapIterator
        public K getKey() {
            if (this.canRemove) {
                switch (this.nextIndex) {
                    case 1:
                        return (K) ((Flat3Map) this.parent).key1;
                    case 2:
                        return (K) ((Flat3Map) this.parent).key2;
                    case 3:
                        return (K) ((Flat3Map) this.parent).key3;
                    default:
                        throw new IllegalStateException("Invalid map index: " + this.nextIndex);
                }
            }
            throw new IllegalStateException("getKey() can only be called after next() and before remove()");
        }

        @Override // org.apache.commons.collections4.MapIterator
        public V getValue() {
            if (this.canRemove) {
                switch (this.nextIndex) {
                    case 1:
                        return (V) ((Flat3Map) this.parent).value1;
                    case 2:
                        return (V) ((Flat3Map) this.parent).value2;
                    case 3:
                        return (V) ((Flat3Map) this.parent).value3;
                    default:
                        throw new IllegalStateException("Invalid map index: " + this.nextIndex);
                }
            }
            throw new IllegalStateException("getValue() can only be called after next() and before remove()");
        }

        @Override // org.apache.commons.collections4.MapIterator
        public V setValue(V value) {
            if (!this.canRemove) {
                throw new IllegalStateException("setValue() can only be called after next() and before remove()");
            }
            V old = getValue();
            switch (this.nextIndex) {
                case 1:
                    ((Flat3Map) this.parent).value1 = value;
                    break;
                case 2:
                    ((Flat3Map) this.parent).value2 = value;
                    break;
                case 3:
                    ((Flat3Map) this.parent).value3 = value;
                    break;
                default:
                    throw new IllegalStateException("Invalid map index: " + this.nextIndex);
            }
            return old;
        }

        @Override // org.apache.commons.collections4.ResettableIterator
        public void reset() {
            this.nextIndex = 0;
            this.canRemove = false;
        }

        public String toString() {
            if (this.canRemove) {
                return "Iterator[" + getKey() + SymbolConstants.EQUAL_SYMBOL + getValue() + "]";
            }
            return "Iterator[]";
        }
    }

    @Override // java.util.Map, org.apache.commons.collections4.Get
    public Set<Map.Entry<K, V>> entrySet() {
        if (this.delegateMap != null) {
            return this.delegateMap.entrySet();
        }
        return new EntrySet(this);
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/Flat3Map$EntrySet.class */
    static class EntrySet<K, V> extends AbstractSet<Map.Entry<K, V>> {
        private final Flat3Map<K, V> parent;

        EntrySet(Flat3Map<K, V> parent) {
            this.parent = parent;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.parent.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            this.parent.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> entry = (Map.Entry) obj;
            Object key = entry.getKey();
            boolean result = this.parent.containsKey(key);
            this.parent.remove(key);
            return result;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, V>> iterator() {
            if (((Flat3Map) this.parent).delegateMap != null) {
                return ((Flat3Map) this.parent).delegateMap.entrySet().iterator();
            }
            if (this.parent.size() == 0) {
                return EmptyIterator.emptyIterator();
            }
            return new EntrySetIterator(this.parent);
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/Flat3Map$FlatMapEntry.class */
    static class FlatMapEntry<K, V> implements Map.Entry<K, V> {
        private final Flat3Map<K, V> parent;
        private final int index;
        private volatile boolean removed = false;

        public FlatMapEntry(Flat3Map<K, V> parent, int index) {
            this.parent = parent;
            this.index = index;
        }

        void setRemoved(boolean flag) {
            this.removed = flag;
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            if (!this.removed) {
                switch (this.index) {
                    case 1:
                        return (K) ((Flat3Map) this.parent).key1;
                    case 2:
                        return (K) ((Flat3Map) this.parent).key2;
                    case 3:
                        return (K) ((Flat3Map) this.parent).key3;
                    default:
                        throw new IllegalStateException("Invalid map index: " + this.index);
                }
            }
            throw new IllegalStateException("getKey() can only be called after next() and before remove()");
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            if (!this.removed) {
                switch (this.index) {
                    case 1:
                        return (V) ((Flat3Map) this.parent).value1;
                    case 2:
                        return (V) ((Flat3Map) this.parent).value2;
                    case 3:
                        return (V) ((Flat3Map) this.parent).value3;
                    default:
                        throw new IllegalStateException("Invalid map index: " + this.index);
                }
            }
            throw new IllegalStateException("getValue() can only be called after next() and before remove()");
        }

        @Override // java.util.Map.Entry
        public V setValue(V value) {
            if (this.removed) {
                throw new IllegalStateException("setValue() can only be called after next() and before remove()");
            }
            V old = getValue();
            switch (this.index) {
                case 1:
                    ((Flat3Map) this.parent).value1 = value;
                    break;
                case 2:
                    ((Flat3Map) this.parent).value2 = value;
                    break;
                case 3:
                    ((Flat3Map) this.parent).value3 = value;
                    break;
                default:
                    throw new IllegalStateException("Invalid map index: " + this.index);
            }
            return old;
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object obj) {
            if (this.removed || !(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> other = (Map.Entry) obj;
            Object key = getKey();
            Object value = getValue();
            if (key != null ? key.equals(other.getKey()) : other.getKey() == null) {
                if (value != null ? value.equals(other.getValue()) : other.getValue() == null) {
                    return true;
                }
            }
            return false;
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            if (this.removed) {
                return 0;
            }
            Object key = getKey();
            Object value = getValue();
            return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
        }

        public String toString() {
            if (!this.removed) {
                return getKey() + SymbolConstants.EQUAL_SYMBOL + getValue();
            }
            return "";
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/Flat3Map$EntryIterator.class */
    static abstract class EntryIterator<K, V> {
        private final Flat3Map<K, V> parent;
        private int nextIndex = 0;
        private FlatMapEntry<K, V> currentEntry = null;

        public EntryIterator(Flat3Map<K, V> parent) {
            this.parent = parent;
        }

        public boolean hasNext() {
            return this.nextIndex < ((Flat3Map) this.parent).size;
        }

        public Map.Entry<K, V> nextEntry() {
            if (!hasNext()) {
                throw new NoSuchElementException("No next() entry in the iteration");
            }
            Flat3Map<K, V> flat3Map = this.parent;
            int i = this.nextIndex + 1;
            this.nextIndex = i;
            this.currentEntry = new FlatMapEntry<>(flat3Map, i);
            return this.currentEntry;
        }

        public void remove() {
            if (this.currentEntry == null) {
                throw new IllegalStateException("remove() can only be called once after next()");
            }
            this.currentEntry.setRemoved(true);
            this.parent.remove(this.currentEntry.getKey());
            this.nextIndex--;
            this.currentEntry = null;
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/Flat3Map$EntrySetIterator.class */
    static class EntrySetIterator<K, V> extends EntryIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        EntrySetIterator(Flat3Map<K, V> parent) {
            super(parent);
        }

        @Override // java.util.Iterator
        public Map.Entry<K, V> next() {
            return nextEntry();
        }
    }

    @Override // java.util.Map, org.apache.commons.collections4.Get
    public Set<K> keySet() {
        if (this.delegateMap != null) {
            return this.delegateMap.keySet();
        }
        return new KeySet(this);
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/Flat3Map$KeySet.class */
    static class KeySet<K> extends AbstractSet<K> {
        private final Flat3Map<K, ?> parent;

        KeySet(Flat3Map<K, ?> parent) {
            this.parent = parent;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.parent.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            this.parent.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object key) {
            return this.parent.containsKey(key);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object key) {
            boolean result = this.parent.containsKey(key);
            this.parent.remove(key);
            return result;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            if (((Flat3Map) this.parent).delegateMap != null) {
                return ((Flat3Map) this.parent).delegateMap.keySet().iterator();
            }
            if (this.parent.size() == 0) {
                return EmptyIterator.emptyIterator();
            }
            return new KeySetIterator(this.parent);
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/Flat3Map$KeySetIterator.class */
    static class KeySetIterator<K> extends EntryIterator<K, Object> implements Iterator<K> {
        KeySetIterator(Flat3Map<K, ?> parent) {
            super(parent);
        }

        @Override // java.util.Iterator
        public K next() {
            return nextEntry().getKey();
        }
    }

    @Override // java.util.Map, org.apache.commons.collections4.Get
    public Collection<V> values() {
        if (this.delegateMap != null) {
            return this.delegateMap.values();
        }
        return new Values(this);
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/Flat3Map$Values.class */
    static class Values<V> extends AbstractCollection<V> {
        private final Flat3Map<?, V> parent;

        Values(Flat3Map<?, V> parent) {
            this.parent = parent;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return this.parent.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            this.parent.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object value) {
            return this.parent.containsValue(value);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            if (((Flat3Map) this.parent).delegateMap != null) {
                return ((Flat3Map) this.parent).delegateMap.values().iterator();
            }
            if (this.parent.size() == 0) {
                return EmptyIterator.emptyIterator();
            }
            return new ValuesIterator(this.parent);
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/Flat3Map$ValuesIterator.class */
    static class ValuesIterator<V> extends EntryIterator<Object, V> implements Iterator<V> {
        ValuesIterator(Flat3Map<?, V> parent) {
            super(parent);
        }

        @Override // java.util.Iterator
        public V next() {
            return nextEntry().getValue();
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(size());
        MapIterator<K, V> mapIterator = mapIterator();
        while (mapIterator.hasNext()) {
            out.writeObject(mapIterator.next());
            out.writeObject(mapIterator.getValue());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        int count = in.readInt();
        if (count > 3) {
            this.delegateMap = createDelegateMap();
        }
        for (int i = count; i > 0; i--) {
            put(in.readObject(), in.readObject());
        }
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Flat3Map<K, V> m2794clone() {
        try {
            Flat3Map<K, V> cloned = (Flat3Map) super.clone();
            if (cloned.delegateMap != null) {
                cloned.delegateMap = cloned.delegateMap.clone();
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:37:0x009e A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00a0  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00d3 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00d5  */
    @Override // java.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean equals(java.lang.Object r4) {
        /*
            Method dump skipped, instructions count: 253
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.collections4.map.Flat3Map.equals(java.lang.Object):boolean");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:15:0x005a  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x005e  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0074  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0078  */
    @Override // java.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int hashCode() {
        /*
            r5 = this;
            r0 = r5
            org.apache.commons.collections4.map.AbstractHashedMap<K, V> r0 = r0.delegateMap
            if (r0 == 0) goto Lf
            r0 = r5
            org.apache.commons.collections4.map.AbstractHashedMap<K, V> r0 = r0.delegateMap
            int r0 = r0.hashCode()
            return r0
        Lf:
            r0 = 0
            r6 = r0
            r0 = r5
            int r0 = r0.size
            switch(r0) {
                case 0: goto L82;
                case 1: goto L68;
                case 2: goto L4e;
                case 3: goto L34;
                default: goto L85;
            }
        L34:
            r0 = r6
            r1 = r5
            int r1 = r1.hash3
            r2 = r5
            V r2 = r2.value3
            if (r2 != 0) goto L44
            r2 = 0
            goto L4b
        L44:
            r2 = r5
            V r2 = r2.value3
            int r2 = r2.hashCode()
        L4b:
            r1 = r1 ^ r2
            int r0 = r0 + r1
            r6 = r0
        L4e:
            r0 = r6
            r1 = r5
            int r1 = r1.hash2
            r2 = r5
            V r2 = r2.value2
            if (r2 != 0) goto L5e
            r2 = 0
            goto L65
        L5e:
            r2 = r5
            V r2 = r2.value2
            int r2 = r2.hashCode()
        L65:
            r1 = r1 ^ r2
            int r0 = r0 + r1
            r6 = r0
        L68:
            r0 = r6
            r1 = r5
            int r1 = r1.hash1
            r2 = r5
            V r2 = r2.value1
            if (r2 != 0) goto L78
            r2 = 0
            goto L7f
        L78:
            r2 = r5
            V r2 = r2.value1
            int r2 = r2.hashCode()
        L7f:
            r1 = r1 ^ r2
            int r0 = r0 + r1
            r6 = r0
        L82:
            goto La3
        L85:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            r1 = r0
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r3 = r2
            r3.<init>()
            java.lang.String r3 = "Invalid map index: "
            java.lang.StringBuilder r2 = r2.append(r3)
            r3 = r5
            int r3 = r3.size
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r0
        La3:
            r0 = r6
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.collections4.map.Flat3Map.hashCode():int");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:23:0x008b  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0090  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00a8  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00ad  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00c5  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00ca  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00e2  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00e7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String toString() {
        /*
            Method dump skipped, instructions count: 284
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.collections4.map.Flat3Map.toString():java.lang.String");
    }
}
