package org.ehcache.impl.internal.concurrent;

import com.drew.metadata.exif.makernotes.FujifilmMakernoteDirectory;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import org.ehcache.config.EvictionAdvisor;
import org.ehcache.core.spi.function.BiFunction;
import org.ehcache.core.spi.function.Function;
import org.ehcache.impl.internal.concurrent.JSR166Helper;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap.class */
public class ConcurrentHashMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V>, Serializable {
    private static final long serialVersionUID = 7249069246763182397L;
    private static final int MAXIMUM_CAPACITY = 1073741824;
    private static final int DEFAULT_CAPACITY = 16;
    static final int MAX_ARRAY_SIZE = 2147483639;
    private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
    private static final float LOAD_FACTOR = 0.75f;
    static final int TREEIFY_THRESHOLD = 8;
    static final int UNTREEIFY_THRESHOLD = 6;
    static final int MIN_TREEIFY_CAPACITY = 64;
    private static final int MIN_TRANSFER_STRIDE = 16;
    static final int MOVED = -1;
    static final int TREEBIN = -2;
    static final int RESERVED = -3;
    static final int HASH_BITS = Integer.MAX_VALUE;
    volatile transient Node<K, V>[] table;
    private volatile transient Node<K, V>[] nextTable;
    private volatile transient long baseCount;
    private volatile transient int sizeCtl;
    private volatile transient int transferIndex;
    private volatile transient int cellsBusy;
    private volatile transient CounterCell[] counterCells;
    private transient KeySetView<K, V> keySet;
    private transient ValuesView<K, V> values;
    private transient EntrySetView<K, V> entrySet;
    private static final JSR166Helper.Unsafe U;
    private static final long SIZECTL;
    private static final long TRANSFERINDEX;
    private static final long BASECOUNT;
    private static final long CELLSBUSY;
    private static final long CELLVALUE;
    private static final long ABASE;
    private static final int ASHIFT;
    public static final TreeBin FAKE_TREE_BIN = new TreeBin(new TreeNode(0, null, null, null, null));
    private static int RESIZE_STAMP_BITS = 16;
    private static final int MAX_RESIZERS = (1 << (32 - RESIZE_STAMP_BITS)) - 1;
    private static final int RESIZE_STAMP_SHIFT = 32 - RESIZE_STAMP_BITS;
    static final int NCPU = Runtime.getRuntime().availableProcessors();
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("segments", Segment[].class), new ObjectStreamField("segmentMask", Integer.TYPE), new ObjectStreamField("segmentShift", Integer.TYPE)};

    static {
        try {
            U = JSR166Helper.Unsafe.getUnsafe();
            SIZECTL = U.objectFieldOffset(ConcurrentHashMap.class.getDeclaredField("sizeCtl"));
            TRANSFERINDEX = U.objectFieldOffset(ConcurrentHashMap.class.getDeclaredField("transferIndex"));
            BASECOUNT = U.objectFieldOffset(ConcurrentHashMap.class.getDeclaredField("baseCount"));
            CELLSBUSY = U.objectFieldOffset(ConcurrentHashMap.class.getDeclaredField("cellsBusy"));
            CELLVALUE = U.objectFieldOffset(CounterCell.class.getDeclaredField("value"));
            ABASE = U.arrayBaseOffset(Node[].class);
            int scale = U.arrayIndexScale(Node[].class);
            if ((scale & (scale - 1)) != 0) {
                throw new Error("data type scale not a power of two");
            }
            ASHIFT = 31 - Integer.numberOfLeadingZeros(scale);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$Node.class */
    static class Node<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        volatile V val;
        volatile Node<K, V> next;

        Node(int hash, K key, V val, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.val = val;
            this.next = next;
        }

        @Override // java.util.Map.Entry
        public final K getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public final V getValue() {
            return this.val;
        }

        @Override // java.util.Map.Entry
        public final int hashCode() {
            return this.key.hashCode() ^ this.val.hashCode();
        }

        public final String toString() {
            return this.key + SymbolConstants.EQUAL_SYMBOL + this.val;
        }

        @Override // java.util.Map.Entry
        public final V setValue(V value) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map.Entry
        public final boolean equals(Object o) {
            Map.Entry<?, ?> e;
            Object k;
            Object v;
            Object u;
            return (o instanceof Map.Entry) && (k = (e = (Map.Entry) o).getKey()) != null && (v = e.getValue()) != null && (k == this.key || k.equals(this.key)) && (v == (u = this.val) || v.equals(u));
        }

        Node<K, V> find(int h, Object k) {
            Node<K, V> node;
            K ek;
            Node<K, V> e = this;
            if (k != null) {
                do {
                    if (e.hash == h && ((ek = e.key) == k || (ek != null && k.equals(ek)))) {
                        return e;
                    }
                    node = e.next;
                    e = node;
                } while (node != null);
                return null;
            }
            return null;
        }
    }

    static final int spread(int h) {
        int h2 = Integer.rotateLeft(h, 1);
        return (h2 ^ (h2 >>> 16)) & Integer.MAX_VALUE;
    }

    private static final int tableSizeFor(int c) {
        int n = c - 1;
        int n2 = n | (n >>> 1);
        int n3 = n2 | (n2 >>> 2);
        int n4 = n3 | (n3 >>> 4);
        int n5 = n4 | (n4 >>> 8);
        int n6 = n5 | (n5 >>> 16);
        if (n6 < 0) {
            return 1;
        }
        if (n6 >= 1073741824) {
            return 1073741824;
        }
        return n6 + 1;
    }

    static Class<?> comparableClassFor(Object x) {
        Type[] as;
        if (x instanceof Comparable) {
            Class<?> c = x.getClass();
            if (c == String.class) {
                return c;
            }
            Type[] ts = c.getGenericInterfaces();
            if (ts != null) {
                for (Type t : ts) {
                    if (t instanceof ParameterizedType) {
                        ParameterizedType p = (ParameterizedType) t;
                        if (p.getRawType() == Comparable.class && (as = p.getActualTypeArguments()) != null && as.length == 1 && as[0] == c) {
                            return c;
                        }
                    }
                }
                return null;
            }
            return null;
        }
        return null;
    }

    static int compareComparables(Class<?> kc, Object k, Object x) {
        if (x == null || x.getClass() != kc) {
            return 0;
        }
        return ((Comparable) k).compareTo(x);
    }

    static final <K, V> Node<K, V> tabAt(Node<K, V>[] tab, int i) {
        return (Node) U.getObjectVolatile(tab, (i << ASHIFT) + ABASE);
    }

    static final <K, V> boolean casTabAt(Node<K, V>[] tab, int i, Node<K, V> c, Node<K, V> v) {
        return U.compareAndSwapObject(tab, (i << ASHIFT) + ABASE, c, v);
    }

    static final <K, V> void setTabAt(Node<K, V>[] tab, int i, Node<K, V> v) {
        U.putObjectVolatile(tab, (i << ASHIFT) + ABASE, v);
    }

    public ConcurrentHashMap() {
    }

    public ConcurrentHashMap(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException();
        }
        int cap = initialCapacity >= 536870912 ? 1073741824 : tableSizeFor(initialCapacity + (initialCapacity >>> 1) + 1);
        this.sizeCtl = cap;
    }

    public ConcurrentHashMap(Map<? extends K, ? extends V> m) {
        this.sizeCtl = 16;
        putAll(m);
    }

    public ConcurrentHashMap(int initialCapacity, float loadFactor) {
        this(initialCapacity, loadFactor, 1);
    }

    public ConcurrentHashMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
        if (loadFactor <= 0.0f || initialCapacity < 0 || concurrencyLevel <= 0) {
            throw new IllegalArgumentException();
        }
        long size = (long) (1.0d + ((initialCapacity < concurrencyLevel ? concurrencyLevel : initialCapacity) / loadFactor));
        int cap = size >= 1073741824 ? 1073741824 : tableSizeFor((int) size);
        this.sizeCtl = cap;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        long n = sumCount();
        if (n < 0) {
            return 0;
        }
        if (n > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) n;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean isEmpty() {
        return sumCount() <= 0;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object key) {
        int n;
        K ek;
        int h = spread(key.hashCode());
        Node<K, V>[] tab = this.table;
        if (tab == null || (n = tab.length) <= 0) {
            return null;
        }
        Node<K, V> nodeTabAt = tabAt(tab, (n - 1) & h);
        Node<K, V> e = nodeTabAt;
        if (nodeTabAt != null) {
            int eh = e.hash;
            if (eh == h) {
                K ek2 = e.key;
                if (ek2 == key || (ek2 != null && key.equals(ek2))) {
                    return e.val;
                }
            } else if (eh < 0) {
                Node<K, V> p = e.find(h, key);
                if (p != null) {
                    return p.val;
                }
                return null;
            }
            while (true) {
                Node<K, V> node = e.next;
                e = node;
                if (node != null) {
                    if (e.hash == h && ((ek = e.key) == key || (ek != null && key.equals(ek)))) {
                        break;
                    }
                } else {
                    return null;
                }
            }
            return e.val;
        }
        return null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        Node<K, V>[] t = this.table;
        if (t != null) {
            Traverser<K, V> it = new Traverser<>(t, t.length, 0, t.length);
            while (true) {
                Node<K, V> p = it.advance();
                if (p != null) {
                    V v = p.val;
                    if (v == value) {
                        return true;
                    }
                    if (v != null && value.equals(v)) {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V put(K key, V value) {
        return putVal(key, value, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:37:0x00c6, code lost:
    
        r21 = r23.val;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00ce, code lost:
    
        if (r13 != false) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00d1, code lost:
    
        r23.val = r12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    final V putVal(K r11, V r12, boolean r13) {
        /*
            Method dump skipped, instructions count: 362
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.ehcache.impl.internal.concurrent.ConcurrentHashMap.putVal(java.lang.Object, java.lang.Object, boolean):java.lang.Object");
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void putAll(Map<? extends K, ? extends V> m) {
        tryPresize(m.size());
        for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            putVal(e.getKey(), e.getValue(), false);
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V remove(Object key) {
        return replaceNode(key, null, null);
    }

    final V replaceNode(Object key, V value, Object cv) {
        int n;
        int i;
        Node<K, V> f;
        TreeNode<K, V> p;
        Node<K, V> node;
        K ek;
        int hash = spread(key.hashCode());
        Node<K, V>[] tab = this.table;
        while (tab != null && (n = tab.length) != 0 && (f = tabAt(tab, (i = (n - 1) & hash))) != null) {
            int fh = f.hash;
            if (fh == -1) {
                tab = helpTransfer(tab, f);
            } else {
                V oldVal = null;
                boolean validated = false;
                synchronized (f) {
                    if (tabAt(tab, i) == f) {
                        if (fh >= 0) {
                            validated = true;
                            Node<K, V> e = f;
                            Node<K, V> pred = null;
                            do {
                                if (e.hash == hash && ((ek = e.key) == key || (ek != null && key.equals(ek)))) {
                                    V ev = e.val;
                                    if (cv == null || cv == ev || (ev != null && cv.equals(ev))) {
                                        oldVal = ev;
                                        if (value != null) {
                                            e.val = value;
                                        } else if (pred != null) {
                                            pred.next = e.next;
                                        } else {
                                            setTabAt(tab, i, e.next);
                                        }
                                    }
                                } else {
                                    pred = e;
                                    node = e.next;
                                    e = node;
                                }
                            } while (node != null);
                        } else if (f instanceof TreeBin) {
                            validated = true;
                            TreeBin<K, V> t = (TreeBin) f;
                            TreeNode<K, V> r = t.root;
                            if (r != null && (p = r.findTreeNode(hash, key, null)) != null) {
                                V pv = p.val;
                                if (cv == null || cv == pv || (pv != null && cv.equals(pv))) {
                                    oldVal = pv;
                                    if (value != null) {
                                        p.val = value;
                                    } else if (t.removeTreeNode(p)) {
                                        setTabAt(tab, i, untreeify(t.first));
                                    }
                                }
                            }
                        }
                    }
                }
                if (validated) {
                    if (oldVal != null) {
                        if (value == null) {
                            addCount(-1L, -1);
                        }
                        return oldVal;
                    }
                    return null;
                }
            }
        }
        return null;
    }

    public final Map<K, V> removeAllWithHash(int keyHash) {
        int n;
        int i;
        Node<K, V> f;
        Map<K, V> invalidated = new HashMap<>();
        int hash = spread(keyHash);
        Node<K, V>[] tab = this.table;
        while (tab != null && (n = tab.length) != 0 && (f = tabAt(tab, (i = (n - 1) & hash))) != null) {
            if (f.hash == -1) {
                tab = helpTransfer(tab, f);
            } else {
                int nodesCount = 0;
                synchronized (f) {
                    if (tabAt(tab, i) == f) {
                        nodesCount = nodesAt(f, invalidated);
                        setTabAt(tab, i, null);
                    }
                }
                if (nodesCount > 0) {
                    addCount(-nodesCount, -nodesCount);
                }
            }
        }
        return invalidated;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        long delta = 0;
        int i = 0;
        Node<K, V>[] tab = this.table;
        while (tab != null && i < tab.length) {
            Node<K, V> f = tabAt(tab, i);
            if (f == null) {
                i++;
            } else {
                int fh = f.hash;
                if (fh == -1) {
                    tab = helpTransfer(tab, f);
                    i = 0;
                } else {
                    synchronized (f) {
                        if (tabAt(tab, i) == f) {
                            for (Node<K, V> p = fh >= 0 ? f : f instanceof TreeBin ? ((TreeBin) f).first : null; p != null; p = p.next) {
                                delta--;
                            }
                            int i2 = i;
                            i++;
                            setTabAt(tab, i2, null);
                        }
                    }
                }
            }
        }
        if (delta != 0) {
            addCount(delta, -1);
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public KeySetView<K, V> keySet() {
        KeySetView<K, V> ks = this.keySet;
        if (ks != null) {
            return ks;
        }
        KeySetView<K, V> keySetView = new KeySetView<>(this, null);
        this.keySet = keySetView;
        return keySetView;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Collection<V> values() {
        ValuesView<K, V> vs = this.values;
        if (vs != null) {
            return vs;
        }
        ValuesView<K, V> valuesView = new ValuesView<>(this);
        this.values = valuesView;
        return valuesView;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        EntrySetView<K, V> es = this.entrySet;
        if (es != null) {
            return es;
        }
        EntrySetView<K, V> entrySetView = new EntrySetView<>(this);
        this.entrySet = entrySetView;
        return entrySetView;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int hashCode() {
        int h = 0;
        Node<K, V>[] t = this.table;
        if (t != null) {
            Traverser<K, V> it = new Traverser<>(t, t.length, 0, t.length);
            while (true) {
                Node<K, V> p = it.advance();
                if (p == null) {
                    break;
                }
                h += p.key.hashCode() ^ p.val.hashCode();
            }
        }
        return h;
    }

    @Override // java.util.AbstractMap
    public String toString() {
        Node<K, V>[] t = this.table;
        int f = t == null ? 0 : t.length;
        Traverser<K, V> it = new Traverser<>(t, f, 0, f);
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        Node<K, V> nodeAdvance = it.advance();
        Node<K, V> p = nodeAdvance;
        if (nodeAdvance != null) {
            while (true) {
                K k = p.key;
                V v = p.val;
                sb.append(k == this ? "(this Map)" : k);
                sb.append('=');
                sb.append(v == this ? "(this Map)" : v);
                Node<K, V> nodeAdvance2 = it.advance();
                p = nodeAdvance2;
                if (nodeAdvance2 == null) {
                    break;
                }
                sb.append(',').append(' ');
            }
        }
        return sb.append('}').toString();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean equals(Object o) {
        Object mv;
        Object v;
        if (o != this) {
            if (!(o instanceof Map)) {
                return false;
            }
            Map<?, ?> m = (Map) o;
            Node<K, V>[] t = this.table;
            int f = t == null ? 0 : t.length;
            Traverser<K, V> it = new Traverser<>(t, f, 0, f);
            while (true) {
                Node<K, V> p = it.advance();
                if (p != null) {
                    V val = p.val;
                    Object v2 = m.get(p.key);
                    if (v2 == null) {
                        return false;
                    }
                    if (v2 != val && !v2.equals(val)) {
                        return false;
                    }
                } else {
                    for (Map.Entry<K, V> entry : m.entrySet()) {
                        Object mk = entry.getKey();
                        if (mk == null || (mv = entry.getValue()) == null || (v = get(mk)) == null) {
                            return false;
                        }
                        if (mv != v && !mv.equals(v)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        } else {
            return true;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$Segment.class */
    static class Segment<K, V> extends ReentrantLock implements Serializable {
        private static final long serialVersionUID = 2249069246763182397L;
        final float loadFactor;

        Segment(float lf) {
            this.loadFactor = lf;
        }
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        int ssize;
        int sshift = 0;
        int i = 1;
        while (true) {
            ssize = i;
            if (ssize >= 16) {
                break;
            }
            sshift++;
            i = ssize << 1;
        }
        int segmentShift = 32 - sshift;
        int segmentMask = ssize - 1;
        Segment<K, V>[] segments = new Segment[16];
        for (int i2 = 0; i2 < segments.length; i2++) {
            segments[i2] = new Segment<>(LOAD_FACTOR);
        }
        s.putFields().put("segments", segments);
        s.putFields().put("segmentShift", segmentShift);
        s.putFields().put("segmentMask", segmentMask);
        s.writeFields();
        Node<K, V>[] t = this.table;
        if (t != null) {
            Traverser<K, V> it = new Traverser<>(t, t.length, 0, t.length);
            while (true) {
                Node<K, V> p = it.advance();
                if (p == null) {
                    break;
                }
                s.writeObject(p.key);
                s.writeObject(p.val);
            }
        }
        s.writeObject(null);
        s.writeObject(null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:38:0x0119, code lost:
    
        r18 = false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void readObject(java.io.ObjectInputStream r9) throws java.lang.ClassNotFoundException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 482
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.ehcache.impl.internal.concurrent.ConcurrentHashMap.readObject(java.io.ObjectInputStream):void");
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public V putIfAbsent(K key, V value) {
        return putVal(key, value, true);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public boolean remove(Object key, Object value) {
        if (key == null) {
            throw new NullPointerException();
        }
        return (value == null || replaceNode(key, null, value) == null) ? false : true;
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public boolean replace(K key, V oldValue, V newValue) {
        if (key == null || oldValue == null || newValue == null) {
            throw new NullPointerException();
        }
        return replaceNode(key, newValue, oldValue) != null;
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public V replace(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException();
        }
        return replaceNode(key, value, null);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public V getOrDefault(Object key, V defaultValue) {
        V v = get(key);
        return v == null ? defaultValue : v;
    }

    public void forEach(JSR166Helper.BiConsumer<? super K, ? super V> biConsumer) {
        if (biConsumer == null) {
            throw new NullPointerException();
        }
        Node<K, V>[] nodeArr = this.table;
        if (nodeArr != null) {
            Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
            while (true) {
                Node<K, V> nodeAdvance = traverser.advance();
                if (nodeAdvance != null) {
                    biConsumer.accept(nodeAdvance.key, nodeAdvance.val);
                } else {
                    return;
                }
            }
        }
    }

    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
        V v;
        if (biFunction == null) {
            throw new NullPointerException();
        }
        Node<K, V>[] nodeArr = this.table;
        if (nodeArr != null) {
            Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
            while (true) {
                Node<K, V> nodeAdvance = traverser.advance();
                if (nodeAdvance != null) {
                    V v2 = nodeAdvance.val;
                    K k = nodeAdvance.key;
                    do {
                        V vApply = biFunction.apply(k, (Object) v2);
                        if (vApply == null) {
                            throw new NullPointerException();
                        }
                        if (replaceNode(k, vApply, v2) == null) {
                            v = get(k);
                            v2 = v;
                        }
                    } while (v != null);
                } else {
                    return;
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:60:0x011c, code lost:
    
        r12 = r21.val;
     */
    /* JADX WARN: Finally extract failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public V computeIfAbsent(K r9, org.ehcache.core.spi.function.Function<? super K, ? extends V> r10) {
        /*
            Method dump skipped, instructions count: 493
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.ehcache.impl.internal.concurrent.ConcurrentHashMap.computeIfAbsent(java.lang.Object, org.ehcache.core.spi.function.Function):java.lang.Object");
    }

    public V computeIfPresent(K k, BiFunction<? super K, ? super V, ? extends V> biFunction) {
        int length;
        TreeNode<K, V> treeNodeFindTreeNode;
        K k2;
        if (k == null || biFunction == null) {
            throw new NullPointerException();
        }
        int iSpread = spread(k.hashCode());
        V vApply = null;
        int i = 0;
        int i2 = 0;
        Node<K, V>[] nodeArrInitTable = this.table;
        while (true) {
            if (nodeArrInitTable == null || (length = nodeArrInitTable.length) == 0) {
                nodeArrInitTable = initTable();
            } else {
                int i3 = (length - 1) & iSpread;
                Node<K, V> nodeTabAt = tabAt(nodeArrInitTable, i3);
                if (nodeTabAt == null) {
                    break;
                }
                int i4 = nodeTabAt.hash;
                if (i4 == -1) {
                    nodeArrInitTable = helpTransfer(nodeArrInitTable, nodeTabAt);
                } else {
                    synchronized (nodeTabAt) {
                        if (tabAt(nodeArrInitTable, i3) == nodeTabAt) {
                            if (i4 >= 0) {
                                i2 = 1;
                                Node<K, V> node = nodeTabAt;
                                Node<K, V> node2 = null;
                                while (true) {
                                    if (node.hash == iSpread && ((k2 = node.key) == k || (k2 != null && k.equals(k2)))) {
                                        break;
                                    }
                                    node2 = node;
                                    Node<K, V> node3 = node.next;
                                    node = node3;
                                    if (node3 == null) {
                                        break;
                                    }
                                    i2++;
                                }
                                vApply = biFunction.apply(k, node.val);
                                if (vApply != null) {
                                    node.val = vApply;
                                } else {
                                    i = -1;
                                    Node<K, V> node4 = node.next;
                                    if (node2 != null) {
                                        node2.next = node4;
                                    } else {
                                        setTabAt(nodeArrInitTable, i3, node4);
                                    }
                                }
                            } else if (nodeTabAt instanceof TreeBin) {
                                i2 = 2;
                                TreeBin treeBin = (TreeBin) nodeTabAt;
                                TreeNode<K, V> treeNode = treeBin.root;
                                if (treeNode != null && (treeNodeFindTreeNode = treeNode.findTreeNode(iSpread, k, null)) != null) {
                                    vApply = biFunction.apply(k, treeNodeFindTreeNode.val);
                                    if (vApply != null) {
                                        treeNodeFindTreeNode.val = vApply;
                                    } else {
                                        i = -1;
                                        if (treeBin.removeTreeNode(treeNodeFindTreeNode)) {
                                            setTabAt(nodeArrInitTable, i3, untreeify(treeBin.first));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (i2 != 0) {
                        break;
                    }
                }
            }
        }
        if (i != 0) {
            addCount(i, i2);
        }
        return vApply;
    }

    /* JADX WARN: Finally extract failed */
    public V compute(K k, BiFunction<? super K, ? super V, ? extends V> biFunction) {
        int length;
        TreeNode<K, V> treeNodeFindTreeNode;
        K k2;
        if (k == null || biFunction == null) {
            throw new NullPointerException();
        }
        int iSpread = spread(k.hashCode());
        V vApply = null;
        int i = 0;
        int i2 = 0;
        Node<K, V>[] nodeArrInitTable = this.table;
        while (true) {
            if (nodeArrInitTable == null || (length = nodeArrInitTable.length) == 0) {
                nodeArrInitTable = initTable();
            } else {
                int i3 = (length - 1) & iSpread;
                Node<K, V> nodeTabAt = tabAt(nodeArrInitTable, i3);
                if (nodeTabAt == null) {
                    ReservationNode reservationNode = new ReservationNode();
                    synchronized (reservationNode) {
                        if (casTabAt(nodeArrInitTable, i3, null, reservationNode)) {
                            i2 = 1;
                            Node node = null;
                            try {
                                V vApply2 = biFunction.apply(k, null);
                                vApply = vApply2;
                                if (vApply2 != null) {
                                    i = 1;
                                    node = new Node(iSpread, k, vApply, null);
                                }
                                setTabAt(nodeArrInitTable, i3, node);
                            } catch (Throwable th) {
                                setTabAt(nodeArrInitTable, i3, null);
                                throw th;
                            }
                        }
                    }
                    if (i2 != 0) {
                        break;
                    }
                } else {
                    int i4 = nodeTabAt.hash;
                    if (i4 == -1) {
                        nodeArrInitTable = helpTransfer(nodeArrInitTable, nodeTabAt);
                    } else {
                        synchronized (nodeTabAt) {
                            if (tabAt(nodeArrInitTable, i3) == nodeTabAt) {
                                if (i4 >= 0) {
                                    i2 = 1;
                                    Node<K, V> node2 = nodeTabAt;
                                    Node<K, V> node3 = null;
                                    while (true) {
                                        if (node2.hash == iSpread && ((k2 = node2.key) == k || (k2 != null && k.equals(k2)))) {
                                            break;
                                        }
                                        node3 = node2;
                                        Node<K, V> node4 = node2.next;
                                        node2 = node4;
                                        if (node4 != null) {
                                            i2++;
                                        } else {
                                            vApply = biFunction.apply(k, null);
                                            if (vApply != null) {
                                                i = 1;
                                                node3.next = new Node<>(iSpread, k, vApply, null);
                                            }
                                        }
                                    }
                                    vApply = biFunction.apply(k, node2.val);
                                    if (vApply != null) {
                                        node2.val = vApply;
                                    } else {
                                        i = -1;
                                        Node<K, V> node5 = node2.next;
                                        if (node3 != null) {
                                            node3.next = node5;
                                        } else {
                                            setTabAt(nodeArrInitTable, i3, node5);
                                        }
                                    }
                                } else if (nodeTabAt instanceof TreeBin) {
                                    i2 = 1;
                                    TreeBin treeBin = (TreeBin) nodeTabAt;
                                    TreeNode<K, V> treeNode = treeBin.root;
                                    if (treeNode != null) {
                                        treeNodeFindTreeNode = treeNode.findTreeNode(iSpread, k, null);
                                    } else {
                                        treeNodeFindTreeNode = null;
                                    }
                                    vApply = biFunction.apply(k, (Object) (treeNodeFindTreeNode == null ? null : treeNodeFindTreeNode.val));
                                    if (vApply != null) {
                                        if (treeNodeFindTreeNode != null) {
                                            treeNodeFindTreeNode.val = vApply;
                                        } else {
                                            i = 1;
                                            treeBin.putTreeVal(iSpread, k, vApply);
                                        }
                                    } else if (treeNodeFindTreeNode != null) {
                                        i = -1;
                                        if (treeBin.removeTreeNode(treeNodeFindTreeNode)) {
                                            setTabAt(nodeArrInitTable, i3, untreeify(treeBin.first));
                                        }
                                    }
                                }
                            }
                        }
                        if (i2 != 0) {
                            if (i2 >= 8) {
                                treeifyBin(nodeArrInitTable, i3);
                            }
                        }
                    }
                }
            }
        }
        if (i != 0) {
            addCount(i, i2);
        }
        return vApply;
    }

    public V merge(K k, V v, BiFunction<? super V, ? super V, ? extends V> biFunction) {
        int length;
        K k2;
        if (k == null || v == null || biFunction == null) {
            throw new NullPointerException();
        }
        int iSpread = spread(k.hashCode());
        V vApply = null;
        int i = 0;
        int i2 = 0;
        Node<K, V>[] nodeArrInitTable = this.table;
        while (true) {
            if (nodeArrInitTable == null || (length = nodeArrInitTable.length) == 0) {
                nodeArrInitTable = initTable();
            } else {
                int i3 = (length - 1) & iSpread;
                Node<K, V> nodeTabAt = tabAt(nodeArrInitTable, i3);
                if (nodeTabAt == null) {
                    if (casTabAt(nodeArrInitTable, i3, null, new Node(iSpread, k, v, null))) {
                        i = 1;
                        vApply = v;
                        break;
                    }
                } else {
                    int i4 = nodeTabAt.hash;
                    if (i4 == -1) {
                        nodeArrInitTable = helpTransfer(nodeArrInitTable, nodeTabAt);
                    } else {
                        synchronized (nodeTabAt) {
                            if (tabAt(nodeArrInitTable, i3) == nodeTabAt) {
                                if (i4 >= 0) {
                                    i2 = 1;
                                    Node<K, V> node = nodeTabAt;
                                    Node<K, V> node2 = null;
                                    while (true) {
                                        if (node.hash == iSpread && ((k2 = node.key) == k || (k2 != null && k.equals(k2)))) {
                                            break;
                                        }
                                        node2 = node;
                                        Node<K, V> node3 = node.next;
                                        node = node3;
                                        if (node3 != null) {
                                            i2++;
                                        } else {
                                            i = 1;
                                            vApply = v;
                                            node2.next = new Node<>(iSpread, k, vApply, null);
                                            break;
                                        }
                                    }
                                    vApply = biFunction.apply(node.val, v);
                                    if (vApply != null) {
                                        node.val = vApply;
                                    } else {
                                        i = -1;
                                        Node<K, V> node4 = node.next;
                                        if (node2 != null) {
                                            node2.next = node4;
                                        } else {
                                            setTabAt(nodeArrInitTable, i3, node4);
                                        }
                                    }
                                } else if (nodeTabAt instanceof TreeBin) {
                                    i2 = 2;
                                    TreeBin treeBin = (TreeBin) nodeTabAt;
                                    TreeNode<K, V> treeNode = treeBin.root;
                                    TreeNode<K, V> treeNodeFindTreeNode = treeNode == null ? null : treeNode.findTreeNode(iSpread, k, null);
                                    vApply = treeNodeFindTreeNode == null ? v : biFunction.apply(treeNodeFindTreeNode.val, v);
                                    if (vApply != null) {
                                        if (treeNodeFindTreeNode != null) {
                                            treeNodeFindTreeNode.val = vApply;
                                        } else {
                                            i = 1;
                                            treeBin.putTreeVal(iSpread, k, vApply);
                                        }
                                    } else if (treeNodeFindTreeNode != null) {
                                        i = -1;
                                        if (treeBin.removeTreeNode(treeNodeFindTreeNode)) {
                                            setTabAt(nodeArrInitTable, i3, untreeify(treeBin.first));
                                        }
                                    }
                                }
                            }
                        }
                        if (i2 != 0) {
                            if (i2 >= 8) {
                                treeifyBin(nodeArrInitTable, i3);
                            }
                        }
                    }
                }
            }
        }
        if (i != 0) {
            addCount(i, i2);
        }
        return vApply;
    }

    @Deprecated
    public boolean contains(Object value) {
        return containsValue(value);
    }

    public Enumeration<K> keys() {
        Node<K, V>[] t = this.table;
        int f = t == null ? 0 : t.length;
        return new KeyIterator(t, f, 0, f, this);
    }

    public Enumeration<V> elements() {
        Node<K, V>[] t = this.table;
        int f = t == null ? 0 : t.length;
        return new ValueIterator(t, f, 0, f, this);
    }

    public long mappingCount() {
        long n = sumCount();
        if (n < 0) {
            return 0L;
        }
        return n;
    }

    public static <K> KeySetView<K, Boolean> newKeySet() {
        return new KeySetView<>(new ConcurrentHashMap(), Boolean.TRUE);
    }

    public static <K> KeySetView<K, Boolean> newKeySet(int initialCapacity) {
        return new KeySetView<>(new ConcurrentHashMap(initialCapacity), Boolean.TRUE);
    }

    public KeySetView<K, V> keySet(V mappedValue) {
        if (mappedValue == null) {
            throw new NullPointerException();
        }
        return new KeySetView<>(this, mappedValue);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$ForwardingNode.class */
    static final class ForwardingNode<K, V> extends Node<K, V> {
        final Node<K, V>[] nextTable;

        ForwardingNode(Node<K, V>[] tab) {
            super(-1, null, null, null);
            this.nextTable = tab;
        }

        /* JADX WARN: Code restructure failed: missing block: B:22:0x004f, code lost:
        
            return r8;
         */
        @Override // org.ehcache.impl.internal.concurrent.ConcurrentHashMap.Node
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        org.ehcache.impl.internal.concurrent.ConcurrentHashMap.Node<K, V> find(int r5, java.lang.Object r6) {
            /*
                r4 = this;
                r0 = r4
                org.ehcache.impl.internal.concurrent.ConcurrentHashMap$Node<K, V>[] r0 = r0.nextTable
                r7 = r0
            L5:
                r0 = r6
                if (r0 == 0) goto L25
                r0 = r7
                if (r0 == 0) goto L25
                r0 = r7
                int r0 = r0.length
                r1 = r0
                r9 = r1
                if (r0 == 0) goto L25
                r0 = r7
                r1 = r9
                r2 = 1
                int r1 = r1 - r2
                r2 = r5
                r1 = r1 & r2
                org.ehcache.impl.internal.concurrent.ConcurrentHashMap$Node r0 = org.ehcache.impl.internal.concurrent.ConcurrentHashMap.tabAt(r0, r1)
                r1 = r0
                r8 = r1
                if (r0 != 0) goto L27
            L25:
                r0 = 0
                return r0
            L27:
                r0 = r8
                int r0 = r0.hash
                r1 = r0
                r10 = r1
                r1 = r5
                if (r0 != r1) goto L50
                r0 = r8
                K r0 = r0.key
                r1 = r0
                r11 = r1
                r1 = r6
                if (r0 == r1) goto L4d
                r0 = r11
                if (r0 == 0) goto L50
                r0 = r6
                r1 = r11
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto L50
            L4d:
                r0 = r8
                return r0
            L50:
                r0 = r10
                if (r0 >= 0) goto L71
                r0 = r8
                boolean r0 = r0 instanceof org.ehcache.impl.internal.concurrent.ConcurrentHashMap.ForwardingNode
                if (r0 == 0) goto L69
                r0 = r8
                org.ehcache.impl.internal.concurrent.ConcurrentHashMap$ForwardingNode r0 = (org.ehcache.impl.internal.concurrent.ConcurrentHashMap.ForwardingNode) r0
                org.ehcache.impl.internal.concurrent.ConcurrentHashMap$Node<K, V>[] r0 = r0.nextTable
                r7 = r0
                goto L5
            L69:
                r0 = r8
                r1 = r5
                r2 = r6
                org.ehcache.impl.internal.concurrent.ConcurrentHashMap$Node r0 = r0.find(r1, r2)
                return r0
            L71:
                r0 = r8
                org.ehcache.impl.internal.concurrent.ConcurrentHashMap$Node<K, V> r0 = r0.next
                r1 = r0
                r8 = r1
                if (r0 != 0) goto L7e
                r0 = 0
                return r0
            L7e:
                goto L27
            */
            throw new UnsupportedOperationException("Method not decompiled: org.ehcache.impl.internal.concurrent.ConcurrentHashMap.ForwardingNode.find(int, java.lang.Object):org.ehcache.impl.internal.concurrent.ConcurrentHashMap$Node");
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$ReservationNode.class */
    static final class ReservationNode<K, V> extends Node<K, V> {
        ReservationNode() {
            super(-3, null, null, null);
        }

        @Override // org.ehcache.impl.internal.concurrent.ConcurrentHashMap.Node
        Node<K, V> find(int h, Object k) {
            return null;
        }
    }

    static final int resizeStamp(int n) {
        return Integer.numberOfLeadingZeros(n) | (1 << (RESIZE_STAMP_BITS - 1));
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x0072, code lost:
    
        return r8;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private final org.ehcache.impl.internal.concurrent.ConcurrentHashMap.Node<K, V>[] initTable() {
        /*
            r7 = this;
        L0:
            r0 = r7
            org.ehcache.impl.internal.concurrent.ConcurrentHashMap$Node<K, V>[] r0 = r0.table
            r1 = r0
            r8 = r1
            if (r0 == 0) goto Le
            r0 = r8
            int r0 = r0.length
            if (r0 != 0) goto L71
        Le:
            r0 = r7
            int r0 = r0.sizeCtl
            r1 = r0
            r9 = r1
            if (r0 >= 0) goto L1d
            java.lang.Thread.yield()
            goto L0
        L1d:
            org.ehcache.impl.internal.concurrent.JSR166Helper$Unsafe r0 = org.ehcache.impl.internal.concurrent.ConcurrentHashMap.U
            r1 = r7
            long r2 = org.ehcache.impl.internal.concurrent.ConcurrentHashMap.SIZECTL
            r3 = r9
            r4 = -1
            boolean r0 = r0.compareAndSwapInt(r1, r2, r3, r4)
            if (r0 == 0) goto L0
            r0 = r7
            org.ehcache.impl.internal.concurrent.ConcurrentHashMap$Node<K, V>[] r0 = r0.table     // Catch: java.lang.Throwable -> L64
            r1 = r0
            r8 = r1
            if (r0 == 0) goto L3a
            r0 = r8
            int r0 = r0.length     // Catch: java.lang.Throwable -> L64
            if (r0 != 0) goto L5c
        L3a:
            r0 = r9
            if (r0 <= 0) goto L42
            r0 = r9
            goto L44
        L42:
            r0 = 16
        L44:
            r10 = r0
            r0 = r10
            org.ehcache.impl.internal.concurrent.ConcurrentHashMap$Node[] r0 = new org.ehcache.impl.internal.concurrent.ConcurrentHashMap.Node[r0]     // Catch: java.lang.Throwable -> L64
            org.ehcache.impl.internal.concurrent.ConcurrentHashMap$Node[] r0 = (org.ehcache.impl.internal.concurrent.ConcurrentHashMap.Node[]) r0     // Catch: java.lang.Throwable -> L64
            r11 = r0
            r0 = r7
            r1 = r11
            r2 = r1
            r8 = r2
            r0.table = r1     // Catch: java.lang.Throwable -> L64
            r0 = r10
            r1 = r10
            r2 = 2
            int r1 = r1 >>> r2
            int r0 = r0 - r1
            r9 = r0
        L5c:
            r0 = r7
            r1 = r9
            r0.sizeCtl = r1
            goto L6e
        L64:
            r12 = move-exception
            r0 = r7
            r1 = r9
            r0.sizeCtl = r1
            r0 = r12
            throw r0
        L6e:
            goto L71
        L71:
            r0 = r8
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.ehcache.impl.internal.concurrent.ConcurrentHashMap.initTable():org.ehcache.impl.internal.concurrent.ConcurrentHashMap$Node[]");
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x0025  */
    /* JADX WARN: Type inference failed for: r0v41, types: [org.ehcache.impl.internal.concurrent.JSR166Helper$Unsafe] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private final void addCount(long r12, int r14) {
        /*
            Method dump skipped, instructions count: 292
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.ehcache.impl.internal.concurrent.ConcurrentHashMap.addCount(long, int):void");
    }

    final Node<K, V>[] helpTransfer(Node<K, V>[] tab, Node<K, V> f) {
        Node<K, V>[] nextTab;
        int sc;
        if (tab != null && (f instanceof ForwardingNode) && (nextTab = ((ForwardingNode) f).nextTable) != null) {
            int rs = resizeStamp(tab.length);
            while (true) {
                if (nextTab != this.nextTable || this.table != tab || (sc = this.sizeCtl) >= 0 || (sc >>> RESIZE_STAMP_SHIFT) != rs || sc == rs + 1 || sc == rs + MAX_RESIZERS || this.transferIndex <= 0) {
                    break;
                }
                if (U.compareAndSwapInt(this, SIZECTL, sc, sc + 1)) {
                    transfer(tab, nextTab);
                    break;
                }
            }
            return nextTab;
        }
        return this.table;
    }

    private final void tryPresize(int size) {
        int n;
        Node<K, V>[] nt;
        int c = size >= 536870912 ? 1073741824 : tableSizeFor(size + (size >>> 1) + 1);
        while (true) {
            int i = this.sizeCtl;
            int sc = i;
            if (i >= 0) {
                Node<K, V>[] tab = this.table;
                if (tab == null || (n = tab.length) == 0) {
                    int n2 = sc > c ? sc : c;
                    if (U.compareAndSwapInt(this, SIZECTL, sc, -1)) {
                        try {
                            if (this.table == tab) {
                                this.table = new Node[n2];
                                sc = n2 - (n2 >>> 2);
                            }
                            this.sizeCtl = sc;
                        } catch (Throwable th) {
                            this.sizeCtl = sc;
                            throw th;
                        }
                    } else {
                        continue;
                    }
                } else if (c > sc && n < 1073741824) {
                    if (tab == this.table) {
                        int rs = resizeStamp(n);
                        if (sc < 0) {
                            if ((sc >>> RESIZE_STAMP_SHIFT) == rs && sc != rs + 1 && sc != rs + MAX_RESIZERS && (nt = this.nextTable) != null && this.transferIndex > 0) {
                                if (U.compareAndSwapInt(this, SIZECTL, sc, sc + 1)) {
                                    transfer(tab, nt);
                                }
                            } else {
                                return;
                            }
                        } else if (U.compareAndSwapInt(this, SIZECTL, sc, (rs << RESIZE_STAMP_SHIFT) + 2)) {
                            transfer(tab, null);
                        }
                    } else {
                        continue;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private final void transfer(Node<K, V>[] tab, Node<K, V>[] nextTab) {
        Node<K, V> hn;
        Node<K, V> ln;
        int n = tab.length;
        int i = NCPU > 1 ? (n >>> 3) / NCPU : n;
        int stride = i;
        if (i < 16) {
            stride = 16;
        }
        if (nextTab == null) {
            try {
                Node<K, V>[] nt = new Node[n << 1];
                nextTab = nt;
                this.nextTable = nextTab;
                this.transferIndex = n;
            } catch (Throwable th) {
                this.sizeCtl = Integer.MAX_VALUE;
                return;
            }
        }
        int nextn = nextTab.length;
        ForwardingNode<K, V> fwd = new ForwardingNode<>(nextTab);
        boolean advance = true;
        boolean finishing = false;
        int i2 = 0;
        int bound = 0;
        while (true) {
            if (advance) {
                i2--;
                if (i2 >= bound || finishing) {
                    advance = false;
                } else {
                    int nextIndex = this.transferIndex;
                    if (nextIndex <= 0) {
                        i2 = -1;
                        advance = false;
                    } else {
                        JSR166Helper.Unsafe unsafe = U;
                        long j = TRANSFERINDEX;
                        int i3 = nextIndex > stride ? nextIndex - stride : 0;
                        int nextBound = i3;
                        if (unsafe.compareAndSwapInt(this, j, nextIndex, i3)) {
                            bound = nextBound;
                            i2 = nextIndex - 1;
                            advance = false;
                        }
                    }
                }
            } else if (i2 < 0 || i2 >= n || i2 + n >= nextn) {
                if (finishing) {
                    this.nextTable = null;
                    this.table = nextTab;
                    this.sizeCtl = (n << 1) - (n >>> 1);
                    return;
                }
                JSR166Helper.Unsafe unsafe2 = U;
                long j2 = SIZECTL;
                int sc = this.sizeCtl;
                if (!unsafe2.compareAndSwapInt(this, j2, sc, sc - 1)) {
                    continue;
                } else {
                    if (sc - 2 != (resizeStamp(n) << RESIZE_STAMP_SHIFT)) {
                        return;
                    }
                    advance = true;
                    finishing = true;
                    i2 = n;
                }
            } else {
                Node<K, V> f = tabAt(tab, i2);
                if (f == null) {
                    advance = casTabAt(tab, i2, null, fwd);
                } else {
                    int fh = f.hash;
                    if (fh == -1) {
                        advance = true;
                    } else {
                        synchronized (f) {
                            if (tabAt(tab, i2) == f) {
                                if (fh >= 0) {
                                    int runBit = fh & n;
                                    Node<K, V> lastRun = f;
                                    for (Node<K, V> p = f.next; p != null; p = p.next) {
                                        int b = p.hash & n;
                                        if (b != runBit) {
                                            runBit = b;
                                            lastRun = p;
                                        }
                                    }
                                    if (runBit == 0) {
                                        ln = lastRun;
                                        hn = null;
                                    } else {
                                        hn = lastRun;
                                        ln = null;
                                    }
                                    for (Node<K, V> p2 = f; p2 != lastRun; p2 = p2.next) {
                                        int ph = p2.hash;
                                        K pk = p2.key;
                                        V pv = p2.val;
                                        if ((ph & n) == 0) {
                                            ln = new Node<>(ph, pk, pv, ln);
                                        } else {
                                            hn = new Node<>(ph, pk, pv, hn);
                                        }
                                    }
                                    setTabAt(nextTab, i2, ln);
                                    setTabAt(nextTab, i2 + n, hn);
                                    setTabAt(tab, i2, fwd);
                                    advance = true;
                                } else if (f instanceof TreeBin) {
                                    TreeBin<K, V> t = (TreeBin) f;
                                    TreeNode<K, V> lo = null;
                                    TreeNode<K, V> loTail = null;
                                    TreeNode<K, V> hi = null;
                                    TreeNode<K, V> hiTail = null;
                                    int lc = 0;
                                    int hc = 0;
                                    for (Node<K, V> e = t.first; e != null; e = e.next) {
                                        int h = e.hash;
                                        TreeNode<K, V> p3 = new TreeNode<>(h, e.key, e.val, null, null);
                                        if ((h & n) == 0) {
                                            TreeNode<K, V> treeNode = loTail;
                                            p3.prev = treeNode;
                                            if (treeNode == null) {
                                                lo = p3;
                                            } else {
                                                loTail.next = p3;
                                            }
                                            loTail = p3;
                                            lc++;
                                        } else {
                                            TreeNode<K, V> treeNode2 = hiTail;
                                            p3.prev = treeNode2;
                                            if (treeNode2 == null) {
                                                hi = p3;
                                            } else {
                                                hiTail.next = p3;
                                            }
                                            hiTail = p3;
                                            hc++;
                                        }
                                    }
                                    Node<K, V> ln2 = lc <= 6 ? untreeify(lo) : hc != 0 ? new TreeBin<>(lo) : t;
                                    Node<K, V> hn2 = hc <= 6 ? untreeify(hi) : lc != 0 ? new TreeBin<>(hi) : t;
                                    setTabAt(nextTab, i2, ln2);
                                    setTabAt(nextTab, i2 + n, hn2);
                                    setTabAt(tab, i2, fwd);
                                    advance = true;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$CounterCell.class */
    static final class CounterCell {
        volatile long value;

        CounterCell(long x) {
            this.value = x;
        }
    }

    final long sumCount() {
        CounterCell[] as = this.counterCells;
        long sum = this.baseCount;
        if (as != null) {
            for (CounterCell a : as) {
                if (a != null) {
                    sum += a.value;
                }
            }
        }
        return sum;
    }

    private final void fullAddCount(long x, boolean wasUncontended) {
        int n;
        int m;
        int probe = ThreadLocalRandom.getProbe();
        int h = probe;
        if (probe == 0) {
            ThreadLocalRandom.localInit();
            h = ThreadLocalRandom.getProbe();
            wasUncontended = true;
        }
        boolean collide = false;
        while (true) {
            CounterCell[] as = this.counterCells;
            if (as != null && (n = as.length) > 0) {
                CounterCell a = as[(n - 1) & h];
                if (a == null) {
                    if (this.cellsBusy == 0) {
                        CounterCell r = new CounterCell(x);
                        if (this.cellsBusy == 0 && U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
                            boolean created = false;
                            try {
                                CounterCell[] rs = this.counterCells;
                                if (rs != null && (m = rs.length) > 0) {
                                    int j = (m - 1) & h;
                                    if (rs[j] == null) {
                                        rs[j] = r;
                                        created = true;
                                    }
                                }
                                if (created) {
                                    return;
                                }
                            } finally {
                                this.cellsBusy = 0;
                            }
                        }
                    }
                    collide = false;
                    h = ThreadLocalRandom.advanceProbe(h);
                } else {
                    if (!wasUncontended) {
                        wasUncontended = true;
                    } else {
                        JSR166Helper.Unsafe unsafe = U;
                        long j2 = CELLVALUE;
                        long v = a.value;
                        if (!unsafe.compareAndSwapLong(unsafe, j2, v, v + x)) {
                            if (this.counterCells != as || n >= NCPU) {
                                collide = false;
                            } else if (!collide) {
                                collide = true;
                            } else if (this.cellsBusy == 0 && U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
                                try {
                                    if (this.counterCells == as) {
                                        CounterCell[] rs2 = new CounterCell[n << 1];
                                        for (int i = 0; i < n; i++) {
                                            rs2[i] = as[i];
                                        }
                                        this.counterCells = rs2;
                                    }
                                    this.cellsBusy = 0;
                                    collide = false;
                                } finally {
                                    this.cellsBusy = 0;
                                }
                            }
                        } else {
                            return;
                        }
                    }
                    h = ThreadLocalRandom.advanceProbe(h);
                }
            } else if (this.cellsBusy == 0 && this.counterCells == as && U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
                boolean init = false;
                try {
                    if (this.counterCells == as) {
                        CounterCell[] rs3 = new CounterCell[2];
                        rs3[h & 1] = new CounterCell(x);
                        this.counterCells = rs3;
                        init = true;
                    }
                    this.cellsBusy = 0;
                    if (init) {
                        return;
                    }
                } finally {
                    this.cellsBusy = 0;
                }
            } else {
                JSR166Helper.Unsafe unsafe2 = U;
                long j3 = BASECOUNT;
                long v2 = this.baseCount;
                if (unsafe2.compareAndSwapLong(unsafe2, j3, v2, v2 + x)) {
                    return;
                }
            }
        }
    }

    private final void treeifyBin(Node<K, V>[] tab, int index) {
        if (tab != null) {
            int n = tab.length;
            if (n < 64) {
                tryPresize(n << 1);
                return;
            }
            Node<K, V> b = tabAt(tab, index);
            if (b != null && b.hash >= 0) {
                synchronized (b) {
                    if (tabAt(tab, index) == b) {
                        TreeNode<K, V> hd = null;
                        TreeNode<K, V> tl = null;
                        for (Node<K, V> e = b; e != null; e = e.next) {
                            TreeNode<K, V> p = new TreeNode<>(e.hash, e.key, e.val, null, null);
                            TreeNode<K, V> treeNode = tl;
                            p.prev = treeNode;
                            if (treeNode == null) {
                                hd = p;
                            } else {
                                tl.next = p;
                            }
                            tl = p;
                        }
                        setTabAt(tab, index, new TreeBin(hd));
                    }
                }
            }
        }
    }

    static <K, V> Node<K, V> untreeify(Node<K, V> b) {
        Node<K, V> hd = null;
        Node<K, V> tl = null;
        Node<K, V> node = b;
        while (true) {
            Node<K, V> q = node;
            if (q != null) {
                Node<K, V> p = new Node<>(q.hash, q.key, q.val, null);
                if (tl == null) {
                    hd = p;
                } else {
                    tl.next = p;
                }
                tl = p;
                node = q.next;
            } else {
                return hd;
            }
        }
    }

    private static <K, V> int nodesAt(Node<K, V> b, Map<K, V> nodes) {
        if (b instanceof TreeBin) {
            return treeNodesAt(((TreeBin) b).root, nodes);
        }
        int count = 0;
        Node<K, V> node = b;
        while (true) {
            Node<K, V> q = node;
            if (q != null) {
                nodes.put(q.key, q.val);
                count++;
                node = q.next;
            } else {
                return count;
            }
        }
    }

    private static <K, V> int treeNodesAt(TreeNode<K, V> root, Map<K, V> nodes) {
        if (root == null) {
            return 0;
        }
        nodes.put(root.key, root.val);
        int count = 1 + treeNodesAt(root.left, nodes);
        return count + treeNodesAt(root.right, nodes);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$TreeNode.class */
    static final class TreeNode<K, V> extends Node<K, V> {
        TreeNode<K, V> parent;
        TreeNode<K, V> left;
        TreeNode<K, V> right;
        TreeNode<K, V> prev;
        boolean red;

        TreeNode(int hash, K key, V val, Node<K, V> next, TreeNode<K, V> parent) {
            super(hash, key, val, next);
            this.parent = parent;
        }

        @Override // org.ehcache.impl.internal.concurrent.ConcurrentHashMap.Node
        Node<K, V> find(int h, Object k) {
            return findTreeNode(h, k, null);
        }

        /* JADX WARN: Removed duplicated region for block: B:29:0x0077 A[PHI: r8
  0x0077: PHI (r8v2 'kc' java.lang.Class<?>) = (r8v1 'kc' java.lang.Class<?>), (r8v4 'kc' java.lang.Class<?>) binds: [B:26:0x006b, B:28:0x0074] A[DONT_GENERATE, DONT_INLINE]] */
        /* JADX WARN: Removed duplicated region for block: B:36:0x0095 A[PHI: r8
  0x0095: PHI (r8v3 'kc' java.lang.Class<?>) = (r8v2 'kc' java.lang.Class<?>), (r8v4 'kc' java.lang.Class<?>) binds: [B:30:0x0081, B:28:0x0074] A[DONT_GENERATE, DONT_INLINE]] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        final org.ehcache.impl.internal.concurrent.ConcurrentHashMap.TreeNode<K, V> findTreeNode(int r6, java.lang.Object r7, java.lang.Class<?> r8) {
            /*
                r5 = this;
                r0 = r7
                if (r0 == 0) goto Laf
                r0 = r5
                r9 = r0
            L7:
                r0 = r9
                org.ehcache.impl.internal.concurrent.ConcurrentHashMap$TreeNode<K, V> r0 = r0.left
                r14 = r0
                r0 = r9
                org.ehcache.impl.internal.concurrent.ConcurrentHashMap$TreeNode<K, V> r0 = r0.right
                r15 = r0
                r0 = r9
                int r0 = r0.hash
                r1 = r0
                r10 = r1
                r1 = r6
                if (r0 <= r1) goto L28
                r0 = r14
                r9 = r0
                goto Laa
            L28:
                r0 = r10
                r1 = r6
                if (r0 >= r1) goto L35
                r0 = r15
                r9 = r0
                goto Laa
            L35:
                r0 = r9
                K r0 = r0.key
                r1 = r0
                r12 = r1
                r1 = r7
                if (r0 == r1) goto L4f
                r0 = r12
                if (r0 == 0) goto L52
                r0 = r7
                r1 = r12
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto L52
            L4f:
                r0 = r9
                return r0
            L52:
                r0 = r14
                if (r0 != 0) goto L5e
                r0 = r15
                r9 = r0
                goto Laa
            L5e:
                r0 = r15
                if (r0 != 0) goto L6a
                r0 = r14
                r9 = r0
                goto Laa
            L6a:
                r0 = r8
                if (r0 != 0) goto L77
                r0 = r7
                java.lang.Class r0 = org.ehcache.impl.internal.concurrent.ConcurrentHashMap.comparableClassFor(r0)
                r1 = r0
                r8 = r1
                if (r0 == 0) goto L95
            L77:
                r0 = r8
                r1 = r7
                r2 = r12
                int r0 = org.ehcache.impl.internal.concurrent.ConcurrentHashMap.compareComparables(r0, r1, r2)
                r1 = r0
                r11 = r1
                if (r0 == 0) goto L95
                r0 = r11
                if (r0 >= 0) goto L8e
                r0 = r14
                goto L90
            L8e:
                r0 = r15
            L90:
                r9 = r0
                goto Laa
            L95:
                r0 = r15
                r1 = r6
                r2 = r7
                r3 = r8
                org.ehcache.impl.internal.concurrent.ConcurrentHashMap$TreeNode r0 = r0.findTreeNode(r1, r2, r3)
                r1 = r0
                r13 = r1
                if (r0 == 0) goto La6
                r0 = r13
                return r0
            La6:
                r0 = r14
                r9 = r0
            Laa:
                r0 = r9
                if (r0 != 0) goto L7
            Laf:
                r0 = 0
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: org.ehcache.impl.internal.concurrent.ConcurrentHashMap.TreeNode.findTreeNode(int, java.lang.Object, java.lang.Class):org.ehcache.impl.internal.concurrent.ConcurrentHashMap$TreeNode");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$TreeBin.class */
    public static final class TreeBin<K, V> extends Node<K, V> {
        TreeNode<K, V> root;
        volatile TreeNode<K, V> first;
        volatile Thread waiter;
        volatile int lockState;
        static final int WRITER = 1;
        static final int WAITER = 2;
        static final int READER = 4;
        private static final JSR166Helper.Unsafe U;
        private static final long LOCKSTATE;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ConcurrentHashMap.class.desiredAssertionStatus();
            try {
                U = JSR166Helper.Unsafe.getUnsafe();
                LOCKSTATE = U.objectFieldOffset(TreeBin.class.getDeclaredField("lockState"));
            } catch (Exception e) {
                throw new Error(e);
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:8:0x001e  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        static int tieBreakOrder(java.lang.Object r3, java.lang.Object r4) {
            /*
                r0 = r3
                if (r0 == 0) goto L1e
                r0 = r4
                if (r0 == 0) goto L1e
                r0 = r3
                java.lang.Class r0 = r0.getClass()
                java.lang.String r0 = r0.getName()
                r1 = r4
                java.lang.Class r1 = r1.getClass()
                java.lang.String r1 = r1.getName()
                int r0 = r0.compareTo(r1)
                r1 = r0
                r5 = r1
                if (r0 != 0) goto L2f
            L1e:
                r0 = r3
                int r0 = java.lang.System.identityHashCode(r0)
                r1 = r4
                int r1 = java.lang.System.identityHashCode(r1)
                if (r0 > r1) goto L2d
                r0 = -1
                goto L2e
            L2d:
                r0 = 1
            L2e:
                r5 = r0
            L2f:
                r0 = r5
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: org.ehcache.impl.internal.concurrent.ConcurrentHashMap.TreeBin.tieBreakOrder(java.lang.Object, java.lang.Object):int");
        }

        /* JADX WARN: Removed duplicated region for block: B:19:0x0085 A[PHI: r13
  0x0085: PHI (r13v2 'kc' java.lang.Class<?>) = (r13v1 'kc' java.lang.Class<?>), (r13v4 'kc' java.lang.Class<?>) binds: [B:16:0x0077, B:18:0x0082] A[DONT_GENERATE, DONT_INLINE]] */
        /* JADX WARN: Removed duplicated region for block: B:21:0x0094 A[PHI: r13
  0x0094: PHI (r13v3 'kc' java.lang.Class<?>) = (r13v2 'kc' java.lang.Class<?>), (r13v4 'kc' java.lang.Class<?>) binds: [B:20:0x0091, B:18:0x0082] A[DONT_GENERATE, DONT_INLINE]] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        TreeBin(org.ehcache.impl.internal.concurrent.ConcurrentHashMap.TreeNode<K, V> r7) {
            /*
                Method dump skipped, instructions count: 259
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: org.ehcache.impl.internal.concurrent.ConcurrentHashMap.TreeBin.<init>(org.ehcache.impl.internal.concurrent.ConcurrentHashMap$TreeNode):void");
        }

        private final void lockRoot() {
            if (!U.compareAndSwapInt(this, LOCKSTATE, 0, 1)) {
                contendedLock();
            }
        }

        private final void unlockRoot() {
            this.lockState = 0;
        }

        private final void contendedLock() {
            boolean waiting = false;
            while (true) {
                int s = this.lockState;
                if ((s & (-3)) == 0) {
                    if (U.compareAndSwapInt(this, LOCKSTATE, s, 1)) {
                        break;
                    }
                } else if ((s & 2) == 0) {
                    if (U.compareAndSwapInt(this, LOCKSTATE, s, s | 2)) {
                        waiting = true;
                        this.waiter = Thread.currentThread();
                    }
                } else if (waiting) {
                    LockSupport.park(this);
                }
            }
            if (waiting) {
                this.waiter = null;
            }
        }

        @Override // org.ehcache.impl.internal.concurrent.ConcurrentHashMap.Node
        final Node<K, V> find(int h, Object k) {
            K ek;
            Thread w;
            Thread w2;
            if (k != null) {
                Node<K, V> e = this.first;
                while (e != null) {
                    int s = this.lockState;
                    if ((s & 3) != 0) {
                        if (e.hash == h && ((ek = e.key) == k || (ek != null && k.equals(ek)))) {
                            return e;
                        }
                        e = e.next;
                    } else if (U.compareAndSwapInt(this, LOCKSTATE, s, s + 4)) {
                        try {
                            TreeNode<K, V> r = this.root;
                            TreeNode<K, V> p = r == null ? null : r.findTreeNode(h, k, null);
                            if (U.getAndAddInt(this, LOCKSTATE, -4) == 6 && (w2 = this.waiter) != null) {
                                LockSupport.unpark(w2);
                            }
                            return p;
                        } catch (Throwable th) {
                            if (U.getAndAddInt(this, LOCKSTATE, -4) == 6 && (w = this.waiter) != null) {
                                LockSupport.unpark(w);
                            }
                            throw th;
                        }
                    }
                }
                return null;
            }
            return null;
        }

        /* JADX WARN: Code restructure failed: missing block: B:19:0x0063, code lost:
        
            return r16;
         */
        /* JADX WARN: Code restructure failed: missing block: B:37:0x00bf, code lost:
        
            return r20;
         */
        /* JADX WARN: Code restructure failed: missing block: B:65:0x015c, code lost:
        
            if (org.ehcache.impl.internal.concurrent.ConcurrentHashMap.TreeBin.$assertionsDisabled != false) goto L70;
         */
        /* JADX WARN: Code restructure failed: missing block: B:67:0x0166, code lost:
        
            if (checkInvariants(r10.root) != false) goto L81;
         */
        /* JADX WARN: Code restructure failed: missing block: B:69:0x0170, code lost:
        
            throw new java.lang.AssertionError();
         */
        /* JADX WARN: Code restructure failed: missing block: B:70:0x0171, code lost:
        
            return null;
         */
        /* JADX WARN: Code restructure failed: missing block: B:81:?, code lost:
        
            return null;
         */
        /* JADX WARN: Removed duplicated region for block: B:24:0x0073 A[PHI: r14
  0x0073: PHI (r14v2 'kc' java.lang.Class<?>) = (r14v1 'kc' java.lang.Class<?>), (r14v4 'kc' java.lang.Class<?>) binds: [B:21:0x0066, B:23:0x0070] A[DONT_GENERATE, DONT_INLINE]] */
        /* JADX WARN: Removed duplicated region for block: B:26:0x0081 A[PHI: r14
  0x0081: PHI (r14v3 'kc' java.lang.Class<?>) = (r14v2 'kc' java.lang.Class<?>), (r14v4 'kc' java.lang.Class<?>) binds: [B:25:0x007e, B:23:0x0070] A[DONT_GENERATE, DONT_INLINE]] */
        /* JADX WARN: Removed duplicated region for block: B:32:0x00a3  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        final org.ehcache.impl.internal.concurrent.ConcurrentHashMap.TreeNode<K, V> putTreeVal(int r11, K r12, V r13) {
            /*
                Method dump skipped, instructions count: 371
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: org.ehcache.impl.internal.concurrent.ConcurrentHashMap.TreeBin.putTreeVal(int, java.lang.Object, java.lang.Object):org.ehcache.impl.internal.concurrent.ConcurrentHashMap$TreeNode");
        }

        final boolean removeTreeNode(TreeNode<K, V> p) {
            TreeNode<K, V> rl;
            TreeNode<K, V> replacement;
            TreeNode<K, V> pp;
            TreeNode<K, V> next = (TreeNode) p.next;
            TreeNode<K, V> pred = p.prev;
            if (pred == null) {
                this.first = next;
            } else {
                pred.next = next;
            }
            if (next != null) {
                next.prev = pred;
            }
            if (this.first == null) {
                this.root = null;
                return true;
            }
            TreeNode<K, V> treeNode = this.root;
            TreeNode<K, V> r = treeNode;
            if (treeNode == null || r.right == null || (rl = r.left) == null || rl.left == null) {
                return true;
            }
            lockRoot();
            try {
                TreeNode<K, V> pl = p.left;
                TreeNode<K, V> pr = p.right;
                if (pl != null && pr != null) {
                    TreeNode<K, V> s = pr;
                    while (true) {
                        TreeNode<K, V> sl = s.left;
                        if (sl == null) {
                            break;
                        }
                        s = sl;
                    }
                    boolean c = s.red;
                    s.red = p.red;
                    p.red = c;
                    TreeNode<K, V> sr = s.right;
                    TreeNode<K, V> pp2 = p.parent;
                    if (s == pr) {
                        p.parent = s;
                        s.right = p;
                    } else {
                        TreeNode<K, V> sp = s.parent;
                        p.parent = sp;
                        if (sp != null) {
                            if (s == sp.left) {
                                sp.left = p;
                            } else {
                                sp.right = p;
                            }
                        }
                        s.right = pr;
                        if (pr != null) {
                            pr.parent = s;
                        }
                    }
                    p.left = null;
                    p.right = sr;
                    if (sr != null) {
                        sr.parent = p;
                    }
                    s.left = pl;
                    if (pl != null) {
                        pl.parent = s;
                    }
                    s.parent = pp2;
                    if (pp2 == null) {
                        r = s;
                    } else if (p == pp2.left) {
                        pp2.left = s;
                    } else {
                        pp2.right = s;
                    }
                    if (sr != null) {
                        replacement = sr;
                    } else {
                        replacement = p;
                    }
                } else if (pl != null) {
                    replacement = pl;
                } else if (pr != null) {
                    replacement = pr;
                } else {
                    replacement = p;
                }
                if (replacement != p) {
                    TreeNode<K, V> pp3 = p.parent;
                    replacement.parent = pp3;
                    if (pp3 == null) {
                        r = replacement;
                    } else if (p == pp3.left) {
                        pp3.left = replacement;
                    } else {
                        pp3.right = replacement;
                    }
                    p.parent = null;
                    p.right = null;
                    p.left = null;
                }
                this.root = p.red ? r : balanceDeletion(r, replacement);
                if (p == replacement && (pp = p.parent) != null) {
                    if (p == pp.left) {
                        pp.left = null;
                    } else if (p == pp.right) {
                        pp.right = null;
                    }
                    p.parent = null;
                }
                if ($assertionsDisabled || checkInvariants(this.root)) {
                    return false;
                }
                throw new AssertionError();
            } finally {
                unlockRoot();
            }
        }

        static <K, V> TreeNode<K, V> rotateLeft(TreeNode<K, V> root, TreeNode<K, V> p) {
            TreeNode<K, V> r;
            if (p != null && (r = p.right) != null) {
                TreeNode<K, V> rl = r.left;
                p.right = rl;
                if (rl != null) {
                    rl.parent = p;
                }
                TreeNode<K, V> pp = p.parent;
                r.parent = pp;
                if (pp == null) {
                    root = r;
                    r.red = false;
                } else if (pp.left == p) {
                    pp.left = r;
                } else {
                    pp.right = r;
                }
                r.left = p;
                p.parent = r;
            }
            return root;
        }

        static <K, V> TreeNode<K, V> rotateRight(TreeNode<K, V> root, TreeNode<K, V> p) {
            TreeNode<K, V> l;
            if (p != null && (l = p.left) != null) {
                TreeNode<K, V> lr = l.right;
                p.left = lr;
                if (lr != null) {
                    lr.parent = p;
                }
                TreeNode<K, V> pp = p.parent;
                l.parent = pp;
                if (pp == null) {
                    root = l;
                    l.red = false;
                } else if (pp.right == p) {
                    pp.right = l;
                } else {
                    pp.left = l;
                }
                l.right = p;
                p.parent = l;
            }
            return root;
        }

        static <K, V> TreeNode<K, V> balanceInsertion(TreeNode<K, V> root, TreeNode<K, V> x) {
            x.red = true;
            while (true) {
                TreeNode<K, V> treeNode = x.parent;
                TreeNode<K, V> xp = treeNode;
                if (treeNode == null) {
                    x.red = false;
                    return x;
                }
                if (!xp.red) {
                    break;
                }
                TreeNode<K, V> treeNode2 = xp.parent;
                TreeNode<K, V> xpp = treeNode2;
                if (treeNode2 == null) {
                    break;
                }
                TreeNode<K, V> xppl = xpp.left;
                if (xp == xppl) {
                    TreeNode<K, V> xppr = xpp.right;
                    if (xppr != null && xppr.red) {
                        xppr.red = false;
                        xp.red = false;
                        xpp.red = true;
                        x = xpp;
                    } else {
                        if (x == xp.right) {
                            x = xp;
                            root = rotateLeft(root, xp);
                            TreeNode<K, V> treeNode3 = x.parent;
                            xp = treeNode3;
                            xpp = treeNode3 == null ? null : xp.parent;
                        }
                        if (xp != null) {
                            xp.red = false;
                            if (xpp != null) {
                                xpp.red = true;
                                root = rotateRight(root, xpp);
                            }
                        }
                    }
                } else if (xppl != null && xppl.red) {
                    xppl.red = false;
                    xp.red = false;
                    xpp.red = true;
                    x = xpp;
                } else {
                    if (x == xp.left) {
                        x = xp;
                        root = rotateRight(root, xp);
                        TreeNode<K, V> treeNode4 = x.parent;
                        xp = treeNode4;
                        xpp = treeNode4 == null ? null : xp.parent;
                    }
                    if (xp != null) {
                        xp.red = false;
                        if (xpp != null) {
                            xpp.red = true;
                            root = rotateLeft(root, xpp);
                        }
                    }
                }
            }
            return root;
        }

        static <K, V> TreeNode<K, V> balanceDeletion(TreeNode<K, V> root, TreeNode<K, V> x) {
            while (x != null && x != root) {
                TreeNode<K, V> treeNode = x.parent;
                TreeNode<K, V> xp = treeNode;
                if (treeNode == null) {
                    x.red = false;
                    return x;
                }
                if (x.red) {
                    x.red = false;
                    return root;
                }
                TreeNode<K, V> treeNode2 = xp.left;
                TreeNode<K, V> xpl = treeNode2;
                if (treeNode2 == x) {
                    TreeNode<K, V> treeNode3 = xp.right;
                    TreeNode<K, V> xpr = treeNode3;
                    if (treeNode3 != null && xpr.red) {
                        xpr.red = false;
                        xp.red = true;
                        root = rotateLeft(root, xp);
                        TreeNode<K, V> treeNode4 = x.parent;
                        xp = treeNode4;
                        xpr = treeNode4 == null ? null : xp.right;
                    }
                    if (xpr == null) {
                        x = xp;
                    } else {
                        TreeNode<K, V> sl = xpr.left;
                        TreeNode<K, V> sr = xpr.right;
                        if ((sr == null || !sr.red) && (sl == null || !sl.red)) {
                            xpr.red = true;
                            x = xp;
                        } else {
                            if (sr == null || !sr.red) {
                                if (sl != null) {
                                    sl.red = false;
                                }
                                xpr.red = true;
                                root = rotateRight(root, xpr);
                                TreeNode<K, V> treeNode5 = x.parent;
                                xp = treeNode5;
                                xpr = treeNode5 == null ? null : xp.right;
                            }
                            if (xpr != null) {
                                xpr.red = xp == null ? false : xp.red;
                                TreeNode<K, V> sr2 = xpr.right;
                                if (sr2 != null) {
                                    sr2.red = false;
                                }
                            }
                            if (xp != null) {
                                xp.red = false;
                                root = rotateLeft(root, xp);
                            }
                            x = root;
                        }
                    }
                } else {
                    if (xpl != null && xpl.red) {
                        xpl.red = false;
                        xp.red = true;
                        root = rotateRight(root, xp);
                        TreeNode<K, V> treeNode6 = x.parent;
                        xp = treeNode6;
                        xpl = treeNode6 == null ? null : xp.left;
                    }
                    if (xpl == null) {
                        x = xp;
                    } else {
                        TreeNode<K, V> sl2 = xpl.left;
                        TreeNode<K, V> sr3 = xpl.right;
                        if ((sl2 == null || !sl2.red) && (sr3 == null || !sr3.red)) {
                            xpl.red = true;
                            x = xp;
                        } else {
                            if (sl2 == null || !sl2.red) {
                                if (sr3 != null) {
                                    sr3.red = false;
                                }
                                xpl.red = true;
                                root = rotateLeft(root, xpl);
                                TreeNode<K, V> treeNode7 = x.parent;
                                xp = treeNode7;
                                xpl = treeNode7 == null ? null : xp.left;
                            }
                            if (xpl != null) {
                                xpl.red = xp == null ? false : xp.red;
                                TreeNode<K, V> sl3 = xpl.left;
                                if (sl3 != null) {
                                    sl3.red = false;
                                }
                            }
                            if (xp != null) {
                                xp.red = false;
                                root = rotateRight(root, xp);
                            }
                            x = root;
                        }
                    }
                }
            }
            return root;
        }

        static <K, V> boolean checkInvariants(TreeNode<K, V> t) {
            TreeNode<K, V> tp = t.parent;
            TreeNode<K, V> tl = t.left;
            TreeNode<K, V> tr = t.right;
            TreeNode<K, V> tb = t.prev;
            TreeNode<K, V> tn = (TreeNode) t.next;
            if (tb != null && tb.next != t) {
                return false;
            }
            if (tn != null && tn.prev != t) {
                return false;
            }
            if (tp != null && t != tp.left && t != tp.right) {
                return false;
            }
            if (tl != null && (tl.parent != t || tl.hash > t.hash)) {
                return false;
            }
            if (tr != null && (tr.parent != t || tr.hash < t.hash)) {
                return false;
            }
            if (t.red && tl != null && tl.red && tr != null && tr.red) {
                return false;
            }
            if (tl != null && !checkInvariants(tl)) {
                return false;
            }
            if (tr != null && !checkInvariants(tr)) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$TableStack.class */
    static final class TableStack<K, V> {
        int length;
        int index;
        Node<K, V>[] tab;
        TableStack<K, V> next;

        TableStack() {
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$Traverser.class */
    static class Traverser<K, V> {
        Node<K, V>[] tab;
        Node<K, V> next = null;
        TableStack<K, V> stack;
        TableStack<K, V> spare;
        int index;
        int baseIndex;
        int baseLimit;
        final int baseSize;

        Traverser(Node<K, V>[] tab, int size, int index, int limit) {
            this.tab = tab;
            this.baseSize = size;
            this.index = index;
            this.baseIndex = index;
            this.baseLimit = limit;
        }

        final Node<K, V> advance() {
            Node<K, V>[] t;
            int n;
            int i;
            Node<K, V> node = this.next;
            Node<K, V> e = node;
            if (node != null) {
                e = e.next;
            }
            while (e == null) {
                if (this.baseIndex >= this.baseLimit || (t = this.tab) == null || (n = t.length) <= (i = this.index) || i < 0) {
                    this.next = null;
                    return null;
                }
                Node<K, V> nodeTabAt = ConcurrentHashMap.tabAt(t, i);
                e = nodeTabAt;
                if (nodeTabAt != null && e.hash < 0) {
                    if (e instanceof ForwardingNode) {
                        this.tab = ((ForwardingNode) e).nextTable;
                        e = null;
                        pushState(t, i, n);
                    } else if (e instanceof TreeBin) {
                        e = ((TreeBin) e).first;
                    } else {
                        e = null;
                    }
                }
                if (this.stack != null) {
                    recoverState(n);
                } else {
                    int i2 = i + this.baseSize;
                    this.index = i2;
                    if (i2 >= n) {
                        int i3 = this.baseIndex + 1;
                        this.baseIndex = i3;
                        this.index = i3;
                    }
                }
            }
            Node<K, V> node2 = e;
            this.next = node2;
            return node2;
        }

        private void pushState(Node<K, V>[] t, int i, int n) {
            TableStack<K, V> s = this.spare;
            if (s != null) {
                this.spare = s.next;
            } else {
                s = new TableStack<>();
            }
            s.tab = t;
            s.length = n;
            s.index = i;
            s.next = this.stack;
            this.stack = s;
        }

        private void recoverState(int n) {
            TableStack<K, V> s;
            while (true) {
                s = this.stack;
                if (s == null) {
                    break;
                }
                int i = this.index;
                int len = s.length;
                int i2 = i + len;
                this.index = i2;
                if (i2 < n) {
                    break;
                }
                n = len;
                this.index = s.index;
                this.tab = s.tab;
                s.tab = null;
                TableStack<K, V> next = s.next;
                s.next = this.spare;
                this.stack = next;
                this.spare = s;
            }
            if (s == null) {
                int i3 = this.index + this.baseSize;
                this.index = i3;
                if (i3 >= n) {
                    int i4 = this.baseIndex + 1;
                    this.baseIndex = i4;
                    this.index = i4;
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$BaseIterator.class */
    static class BaseIterator<K, V> extends Traverser<K, V> {
        final ConcurrentHashMap<K, V> map;
        Node<K, V> lastReturned;

        BaseIterator(Node<K, V>[] tab, int size, int index, int limit, ConcurrentHashMap<K, V> map) {
            super(tab, size, index, limit);
            this.map = map;
            advance();
        }

        public final boolean hasNext() {
            return this.next != null;
        }

        public final boolean hasMoreElements() {
            return this.next != null;
        }

        public final void remove() {
            Node<K, V> p = this.lastReturned;
            if (p == null) {
                throw new IllegalStateException();
            }
            this.lastReturned = null;
            this.map.replaceNode(p.key, null, null);
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$KeyIterator.class */
    static final class KeyIterator<K, V> extends BaseIterator<K, V> implements Iterator<K>, Enumeration<K> {
        KeyIterator(Node<K, V>[] tab, int index, int size, int limit, ConcurrentHashMap<K, V> map) {
            super(tab, index, size, limit, map);
        }

        @Override // java.util.Iterator
        public final K next() {
            Node<K, V> p = this.next;
            if (p == null) {
                throw new NoSuchElementException();
            }
            K k = p.key;
            this.lastReturned = p;
            advance();
            return k;
        }

        @Override // java.util.Enumeration
        public final K nextElement() {
            return next();
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$ValueIterator.class */
    static final class ValueIterator<K, V> extends BaseIterator<K, V> implements Iterator<V>, Enumeration<V> {
        ValueIterator(Node<K, V>[] tab, int index, int size, int limit, ConcurrentHashMap<K, V> map) {
            super(tab, index, size, limit, map);
        }

        @Override // java.util.Iterator
        public final V next() {
            Node<K, V> p = this.next;
            if (p == null) {
                throw new NoSuchElementException();
            }
            V v = p.val;
            this.lastReturned = p;
            advance();
            return v;
        }

        @Override // java.util.Enumeration
        public final V nextElement() {
            return next();
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$EntryIterator.class */
    static final class EntryIterator<K, V> extends BaseIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        EntryIterator(Node<K, V>[] tab, int index, int size, int limit, ConcurrentHashMap<K, V> map) {
            super(tab, index, size, limit, map);
        }

        @Override // java.util.Iterator
        public final Map.Entry<K, V> next() {
            Node<K, V> p = this.next;
            if (p == null) {
                throw new NoSuchElementException();
            }
            K k = p.key;
            V v = p.val;
            this.lastReturned = p;
            advance();
            return new MapEntry(k, v, this.map);
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$MapEntry.class */
    static final class MapEntry<K, V> implements Map.Entry<K, V> {
        final K key;
        V val;
        final ConcurrentHashMap<K, V> map;

        MapEntry(K key, V val, ConcurrentHashMap<K, V> map) {
            this.key = key;
            this.val = val;
            this.map = map;
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            return this.val;
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return this.key.hashCode() ^ this.val.hashCode();
        }

        public String toString() {
            return this.key + SymbolConstants.EQUAL_SYMBOL + this.val;
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object o) {
            Map.Entry<?, ?> e;
            Object k;
            Object v;
            return (o instanceof Map.Entry) && (k = (e = (Map.Entry) o).getKey()) != null && (v = e.getValue()) != null && (k == this.key || k.equals(this.key)) && (v == this.val || v.equals(this.val));
        }

        @Override // java.util.Map.Entry
        public V setValue(V value) {
            if (value == null) {
                throw new NullPointerException();
            }
            V v = this.val;
            this.val = value;
            this.map.put(this.key, value);
            return v;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$KeySpliterator.class */
    static final class KeySpliterator<K, V> extends Traverser<K, V> implements JSR166Helper.Spliterator<K> {
        long est;

        KeySpliterator(Node<K, V>[] tab, int size, int index, int limit, long est) {
            super(tab, size, index, limit);
            this.est = est;
        }

        @Override // org.ehcache.impl.internal.concurrent.JSR166Helper.Spliterator
        public JSR166Helper.Spliterator<K> trySplit() {
            int i = this.baseIndex;
            int f = this.baseLimit;
            int h = (i + f) >>> 1;
            if (h <= i) {
                return null;
            }
            Node<K, V>[] nodeArr = this.tab;
            int i2 = this.baseSize;
            this.baseLimit = h;
            long j = this.est >>> 1;
            this.est = j;
            return new KeySpliterator(nodeArr, i2, h, f, j);
        }

        @Override // org.ehcache.impl.internal.concurrent.JSR166Helper.Spliterator
        public void forEachRemaining(JSR166Helper.Consumer<? super K> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            while (true) {
                Node<K, V> nodeAdvance = advance();
                if (nodeAdvance != null) {
                    consumer.accept(nodeAdvance.key);
                } else {
                    return;
                }
            }
        }

        @Override // org.ehcache.impl.internal.concurrent.JSR166Helper.Spliterator
        public boolean tryAdvance(JSR166Helper.Consumer<? super K> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Node<K, V> nodeAdvance = advance();
            if (nodeAdvance == null) {
                return false;
            }
            consumer.accept(nodeAdvance.key);
            return true;
        }

        @Override // org.ehcache.impl.internal.concurrent.JSR166Helper.Spliterator
        public long estimateSize() {
            return this.est;
        }

        @Override // org.ehcache.impl.internal.concurrent.JSR166Helper.Spliterator
        public int characteristics() {
            return FujifilmMakernoteDirectory.TAG_SEQUENCE_NUMBER;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$ValueSpliterator.class */
    static final class ValueSpliterator<K, V> extends Traverser<K, V> implements JSR166Helper.Spliterator<V> {
        long est;

        ValueSpliterator(Node<K, V>[] tab, int size, int index, int limit, long est) {
            super(tab, size, index, limit);
            this.est = est;
        }

        @Override // org.ehcache.impl.internal.concurrent.JSR166Helper.Spliterator
        public JSR166Helper.Spliterator<V> trySplit() {
            int i = this.baseIndex;
            int f = this.baseLimit;
            int h = (i + f) >>> 1;
            if (h <= i) {
                return null;
            }
            Node<K, V>[] nodeArr = this.tab;
            int i2 = this.baseSize;
            this.baseLimit = h;
            long j = this.est >>> 1;
            this.est = j;
            return new ValueSpliterator(nodeArr, i2, h, f, j);
        }

        @Override // org.ehcache.impl.internal.concurrent.JSR166Helper.Spliterator
        public void forEachRemaining(JSR166Helper.Consumer<? super V> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            while (true) {
                Node<K, V> nodeAdvance = advance();
                if (nodeAdvance != null) {
                    consumer.accept(nodeAdvance.val);
                } else {
                    return;
                }
            }
        }

        @Override // org.ehcache.impl.internal.concurrent.JSR166Helper.Spliterator
        public boolean tryAdvance(JSR166Helper.Consumer<? super V> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Node<K, V> nodeAdvance = advance();
            if (nodeAdvance == null) {
                return false;
            }
            consumer.accept(nodeAdvance.val);
            return true;
        }

        @Override // org.ehcache.impl.internal.concurrent.JSR166Helper.Spliterator
        public long estimateSize() {
            return this.est;
        }

        @Override // org.ehcache.impl.internal.concurrent.JSR166Helper.Spliterator
        public int characteristics() {
            return FujifilmMakernoteDirectory.TAG_AUTO_BRACKETING;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$EntrySpliterator.class */
    static final class EntrySpliterator<K, V> extends Traverser<K, V> implements JSR166Helper.Spliterator<Map.Entry<K, V>> {
        final ConcurrentHashMap<K, V> map;
        long est;

        EntrySpliterator(Node<K, V>[] tab, int size, int index, int limit, long est, ConcurrentHashMap<K, V> map) {
            super(tab, size, index, limit);
            this.map = map;
            this.est = est;
        }

        @Override // org.ehcache.impl.internal.concurrent.JSR166Helper.Spliterator
        public JSR166Helper.Spliterator<Map.Entry<K, V>> trySplit() {
            int i = this.baseIndex;
            int f = this.baseLimit;
            int h = (i + f) >>> 1;
            if (h <= i) {
                return null;
            }
            Node<K, V>[] nodeArr = this.tab;
            int i2 = this.baseSize;
            this.baseLimit = h;
            long j = this.est >>> 1;
            this.est = j;
            return new EntrySpliterator(nodeArr, i2, h, f, j, this.map);
        }

        @Override // org.ehcache.impl.internal.concurrent.JSR166Helper.Spliterator
        public void forEachRemaining(JSR166Helper.Consumer<? super Map.Entry<K, V>> action) {
            if (action == null) {
                throw new NullPointerException();
            }
            while (true) {
                Node<K, V> p = advance();
                if (p != null) {
                    action.accept(new MapEntry(p.key, p.val, this.map));
                } else {
                    return;
                }
            }
        }

        @Override // org.ehcache.impl.internal.concurrent.JSR166Helper.Spliterator
        public boolean tryAdvance(JSR166Helper.Consumer<? super Map.Entry<K, V>> action) {
            if (action == null) {
                throw new NullPointerException();
            }
            Node<K, V> p = advance();
            if (p == null) {
                return false;
            }
            action.accept(new MapEntry(p.key, p.val, this.map));
            return true;
        }

        @Override // org.ehcache.impl.internal.concurrent.JSR166Helper.Spliterator
        public long estimateSize() {
            return this.est;
        }

        @Override // org.ehcache.impl.internal.concurrent.JSR166Helper.Spliterator
        public int characteristics() {
            return FujifilmMakernoteDirectory.TAG_SEQUENCE_NUMBER;
        }
    }

    final int batchFor(long b) {
        if (b == Long.MAX_VALUE) {
            return 0;
        }
        long n = sumCount();
        if (n <= 1 || n < b) {
            return 0;
        }
        int sp = ForkJoinPool.getCommonPoolParallelism() << 2;
        if (b > 0) {
            long n2 = n / b;
            if (n2 < sp) {
                return (int) n2;
            }
        }
        return sp;
    }

    public void forEach(long parallelismThreshold, JSR166Helper.BiConsumer<? super K, ? super V> action) {
        if (action == null) {
            throw new NullPointerException();
        }
        new ForEachMappingTask(null, batchFor(parallelismThreshold), 0, 0, this.table, action).invoke();
    }

    public <U> void forEach(long parallelismThreshold, BiFunction<? super K, ? super V, ? extends U> transformer, JSR166Helper.Consumer<? super U> action) {
        if (transformer == null || action == null) {
            throw new NullPointerException();
        }
        new ForEachTransformedMappingTask(null, batchFor(parallelismThreshold), 0, 0, this.table, transformer, action).invoke();
    }

    public <U> U search(long parallelismThreshold, BiFunction<? super K, ? super V, ? extends U> searchFunction) {
        if (searchFunction == null) {
            throw new NullPointerException();
        }
        return new SearchMappingsTask(null, batchFor(parallelismThreshold), 0, 0, this.table, searchFunction, new AtomicReference()).invoke();
    }

    public <U> U reduce(long parallelismThreshold, BiFunction<? super K, ? super V, ? extends U> transformer, BiFunction<? super U, ? super U, ? extends U> reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return new MapReduceMappingsTask(null, batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, reducer).invoke();
    }

    public double reduceToDouble(long parallelismThreshold, JSR166Helper.ToDoubleBiFunction<? super K, ? super V> transformer, double basis, JSR166Helper.DoubleBinaryOperator reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return new MapReduceMappingsToDoubleTask(null, batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, basis, reducer).invoke().doubleValue();
    }

    public long reduceToLong(long parallelismThreshold, JSR166Helper.ToLongBiFunction<? super K, ? super V> transformer, long basis, JSR166Helper.LongBinaryOperator reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return new MapReduceMappingsToLongTask(null, batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, basis, reducer).invoke().longValue();
    }

    public int reduceToInt(long parallelismThreshold, JSR166Helper.ToIntBiFunction<? super K, ? super V> transformer, int basis, JSR166Helper.IntBinaryOperator reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return new MapReduceMappingsToIntTask(null, batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, basis, reducer).invoke().intValue();
    }

    public void forEachKey(long parallelismThreshold, JSR166Helper.Consumer<? super K> action) {
        if (action == null) {
            throw new NullPointerException();
        }
        new ForEachKeyTask(null, batchFor(parallelismThreshold), 0, 0, this.table, action).invoke();
    }

    public <U> void forEachKey(long parallelismThreshold, Function<? super K, ? extends U> transformer, JSR166Helper.Consumer<? super U> action) {
        if (transformer == null || action == null) {
            throw new NullPointerException();
        }
        new ForEachTransformedKeyTask(null, batchFor(parallelismThreshold), 0, 0, this.table, transformer, action).invoke();
    }

    public <U> U searchKeys(long parallelismThreshold, Function<? super K, ? extends U> searchFunction) {
        if (searchFunction == null) {
            throw new NullPointerException();
        }
        return new SearchKeysTask(null, batchFor(parallelismThreshold), 0, 0, this.table, searchFunction, new AtomicReference()).invoke();
    }

    public K reduceKeys(long parallelismThreshold, BiFunction<? super K, ? super K, ? extends K> reducer) {
        if (reducer == null) {
            throw new NullPointerException();
        }
        return new ReduceKeysTask(null, batchFor(parallelismThreshold), 0, 0, this.table, null, reducer).invoke();
    }

    public <U> U reduceKeys(long parallelismThreshold, Function<? super K, ? extends U> transformer, BiFunction<? super U, ? super U, ? extends U> reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return new MapReduceKeysTask(null, batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, reducer).invoke();
    }

    public double reduceKeysToDouble(long parallelismThreshold, JSR166Helper.ToDoubleFunction<? super K> transformer, double basis, JSR166Helper.DoubleBinaryOperator reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return new MapReduceKeysToDoubleTask(null, batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, basis, reducer).invoke().doubleValue();
    }

    public long reduceKeysToLong(long parallelismThreshold, JSR166Helper.ToLongFunction<? super K> transformer, long basis, JSR166Helper.LongBinaryOperator reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return new MapReduceKeysToLongTask(null, batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, basis, reducer).invoke().longValue();
    }

    public int reduceKeysToInt(long parallelismThreshold, JSR166Helper.ToIntFunction<? super K> transformer, int basis, JSR166Helper.IntBinaryOperator reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return new MapReduceKeysToIntTask(null, batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, basis, reducer).invoke().intValue();
    }

    public void forEachValue(long parallelismThreshold, JSR166Helper.Consumer<? super V> action) {
        if (action == null) {
            throw new NullPointerException();
        }
        new ForEachValueTask(null, batchFor(parallelismThreshold), 0, 0, this.table, action).invoke();
    }

    public <U> void forEachValue(long parallelismThreshold, Function<? super V, ? extends U> transformer, JSR166Helper.Consumer<? super U> action) {
        if (transformer == null || action == null) {
            throw new NullPointerException();
        }
        new ForEachTransformedValueTask(null, batchFor(parallelismThreshold), 0, 0, this.table, transformer, action).invoke();
    }

    public <U> U searchValues(long parallelismThreshold, Function<? super V, ? extends U> searchFunction) {
        if (searchFunction == null) {
            throw new NullPointerException();
        }
        return new SearchValuesTask(null, batchFor(parallelismThreshold), 0, 0, this.table, searchFunction, new AtomicReference()).invoke();
    }

    public V reduceValues(long parallelismThreshold, BiFunction<? super V, ? super V, ? extends V> reducer) {
        if (reducer == null) {
            throw new NullPointerException();
        }
        return new ReduceValuesTask(null, batchFor(parallelismThreshold), 0, 0, this.table, null, reducer).invoke();
    }

    public <U> U reduceValues(long parallelismThreshold, Function<? super V, ? extends U> transformer, BiFunction<? super U, ? super U, ? extends U> reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return new MapReduceValuesTask(null, batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, reducer).invoke();
    }

    public double reduceValuesToDouble(long parallelismThreshold, JSR166Helper.ToDoubleFunction<? super V> transformer, double basis, JSR166Helper.DoubleBinaryOperator reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return new MapReduceValuesToDoubleTask(null, batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, basis, reducer).invoke().doubleValue();
    }

    public long reduceValuesToLong(long parallelismThreshold, JSR166Helper.ToLongFunction<? super V> transformer, long basis, JSR166Helper.LongBinaryOperator reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return new MapReduceValuesToLongTask(null, batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, basis, reducer).invoke().longValue();
    }

    public int reduceValuesToInt(long parallelismThreshold, JSR166Helper.ToIntFunction<? super V> transformer, int basis, JSR166Helper.IntBinaryOperator reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return new MapReduceValuesToIntTask(null, batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, basis, reducer).invoke().intValue();
    }

    public void forEachEntry(long parallelismThreshold, JSR166Helper.Consumer<? super Map.Entry<K, V>> action) {
        if (action == null) {
            throw new NullPointerException();
        }
        new ForEachEntryTask(null, batchFor(parallelismThreshold), 0, 0, this.table, action).invoke();
    }

    public <U> void forEachEntry(long parallelismThreshold, Function<Map.Entry<K, V>, ? extends U> transformer, JSR166Helper.Consumer<? super U> action) {
        if (transformer == null || action == null) {
            throw new NullPointerException();
        }
        new ForEachTransformedEntryTask(null, batchFor(parallelismThreshold), 0, 0, this.table, transformer, action).invoke();
    }

    public <U> U searchEntries(long parallelismThreshold, Function<Map.Entry<K, V>, ? extends U> searchFunction) {
        if (searchFunction == null) {
            throw new NullPointerException();
        }
        return new SearchEntriesTask(null, batchFor(parallelismThreshold), 0, 0, this.table, searchFunction, new AtomicReference()).invoke();
    }

    public Map.Entry<K, V> reduceEntries(long parallelismThreshold, BiFunction<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> reducer) {
        if (reducer == null) {
            throw new NullPointerException();
        }
        return new ReduceEntriesTask(null, batchFor(parallelismThreshold), 0, 0, this.table, null, reducer).invoke();
    }

    public <U> U reduceEntries(long parallelismThreshold, Function<Map.Entry<K, V>, ? extends U> transformer, BiFunction<? super U, ? super U, ? extends U> reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return new MapReduceEntriesTask(null, batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, reducer).invoke();
    }

    public double reduceEntriesToDouble(long parallelismThreshold, JSR166Helper.ToDoubleFunction<Map.Entry<K, V>> transformer, double basis, JSR166Helper.DoubleBinaryOperator reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return new MapReduceEntriesToDoubleTask(null, batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, basis, reducer).invoke().doubleValue();
    }

    public long reduceEntriesToLong(long parallelismThreshold, JSR166Helper.ToLongFunction<Map.Entry<K, V>> transformer, long basis, JSR166Helper.LongBinaryOperator reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return new MapReduceEntriesToLongTask(null, batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, basis, reducer).invoke().longValue();
    }

    public int reduceEntriesToInt(long parallelismThreshold, JSR166Helper.ToIntFunction<Map.Entry<K, V>> transformer, int basis, JSR166Helper.IntBinaryOperator reducer) {
        if (transformer == null || reducer == null) {
            throw new NullPointerException();
        }
        return new MapReduceEntriesToIntTask(null, batchFor(parallelismThreshold), 0, 0, this.table, null, transformer, basis, reducer).invoke().intValue();
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$CollectionView.class */
    static abstract class CollectionView<K, V, E> implements Collection<E>, Serializable {
        private static final long serialVersionUID = 7249069246763182397L;
        final ConcurrentHashMap<K, V> map;
        private static final String oomeMsg = "Required array size too large";

        @Override // java.util.Collection, java.lang.Iterable
        public abstract Iterator<E> iterator();

        @Override // java.util.Collection
        public abstract boolean contains(Object obj);

        @Override // java.util.Collection
        public abstract boolean remove(Object obj);

        CollectionView(ConcurrentHashMap<K, V> map) {
            this.map = map;
        }

        public ConcurrentHashMap<K, V> getMap() {
            return this.map;
        }

        @Override // java.util.Collection
        public final void clear() {
            this.map.clear();
        }

        @Override // java.util.Collection
        public final int size() {
            return this.map.size();
        }

        @Override // java.util.Collection
        public final boolean isEmpty() {
            return this.map.isEmpty();
        }

        @Override // java.util.Collection
        public final Object[] toArray() {
            long sz = this.map.mappingCount();
            if (sz > 2147483639) {
                throw new OutOfMemoryError(oomeMsg);
            }
            int n = (int) sz;
            Object[] r = new Object[n];
            int i = 0;
            Iterator i$ = iterator();
            while (i$.hasNext()) {
                E e = i$.next();
                if (i == n) {
                    if (n >= 2147483639) {
                        throw new OutOfMemoryError(oomeMsg);
                    }
                    if (n >= 1073741819) {
                        n = 2147483639;
                    } else {
                        n += (n >>> 1) + 1;
                    }
                    r = Arrays.copyOf(r, n);
                }
                int i2 = i;
                i++;
                r[i2] = e;
            }
            return i == n ? r : Arrays.copyOf(r, i);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v14, types: [java.lang.Object[]] */
        /* JADX WARN: Type inference failed for: r0v34 */
        /* JADX WARN: Type inference failed for: r0v41, types: [java.lang.Object[]] */
        @Override // java.util.Collection
        public final <T> T[] toArray(T[] tArr) {
            long jMappingCount = this.map.mappingCount();
            if (jMappingCount > 2147483639) {
                throw new OutOfMemoryError(oomeMsg);
            }
            int i = (int) jMappingCount;
            T[] tArrCopyOf = tArr.length >= i ? tArr : (Object[]) Array.newInstance(tArr.getClass().getComponentType(), i);
            int length = tArrCopyOf.length;
            int i2 = 0;
            Iterator<E> it = iterator();
            while (it.hasNext()) {
                E next = it.next();
                if (i2 == length) {
                    if (length >= 2147483639) {
                        throw new OutOfMemoryError(oomeMsg);
                    }
                    if (length >= 1073741819) {
                        length = 2147483639;
                    } else {
                        length += (length >>> 1) + 1;
                    }
                    tArrCopyOf = Arrays.copyOf(tArrCopyOf, length);
                }
                int i3 = i2;
                i2++;
                tArrCopyOf[i3] = next;
            }
            if (tArr != tArrCopyOf || i2 >= length) {
                return i2 == length ? tArrCopyOf : (T[]) Arrays.copyOf(tArrCopyOf, i2);
            }
            tArrCopyOf[i2] = null;
            return tArrCopyOf;
        }

        public final String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append('[');
            Iterator<E> it = iterator();
            if (it.hasNext()) {
                while (true) {
                    Object e = it.next();
                    sb.append(e == this ? "(this Collection)" : e);
                    if (!it.hasNext()) {
                        break;
                    }
                    sb.append(',').append(' ');
                }
            }
            return sb.append(']').toString();
        }

        @Override // java.util.Collection
        public final boolean containsAll(Collection<?> c) {
            if (c != this) {
                for (Object e : c) {
                    if (e == null || !contains(e)) {
                        return false;
                    }
                }
                return true;
            }
            return true;
        }

        @Override // java.util.Collection
        public final boolean removeAll(Collection<?> c) {
            if (c == null) {
                throw new NullPointerException();
            }
            boolean modified = false;
            Iterator<E> it = iterator();
            while (it.hasNext()) {
                if (c.contains(it.next())) {
                    it.remove();
                    modified = true;
                }
            }
            return modified;
        }

        @Override // java.util.Collection
        public final boolean retainAll(Collection<?> c) {
            if (c == null) {
                throw new NullPointerException();
            }
            boolean modified = false;
            Iterator<E> it = iterator();
            while (it.hasNext()) {
                if (!c.contains(it.next())) {
                    it.remove();
                    modified = true;
                }
            }
            return modified;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$KeySetView.class */
    public static class KeySetView<K, V> extends CollectionView<K, V, K> implements Set<K>, Serializable {
        private static final long serialVersionUID = 7249069246763182397L;
        private final V value;

        @Override // org.ehcache.impl.internal.concurrent.ConcurrentHashMap.CollectionView
        public /* bridge */ /* synthetic */ ConcurrentHashMap getMap() {
            return super.getMap();
        }

        KeySetView(ConcurrentHashMap<K, V> map, V value) {
            super(map);
            this.value = value;
        }

        public V getMappedValue() {
            return this.value;
        }

        @Override // org.ehcache.impl.internal.concurrent.ConcurrentHashMap.CollectionView, java.util.Collection
        public boolean contains(Object o) {
            return this.map.containsKey(o);
        }

        @Override // org.ehcache.impl.internal.concurrent.ConcurrentHashMap.CollectionView, java.util.Collection
        public boolean remove(Object o) {
            return this.map.remove(o) != null;
        }

        @Override // org.ehcache.impl.internal.concurrent.ConcurrentHashMap.CollectionView, java.util.Collection, java.lang.Iterable
        public Iterator<K> iterator() {
            ConcurrentHashMap<K, V> m = this.map;
            Node<K, V>[] t = m.table;
            int f = t == null ? 0 : t.length;
            return new KeyIterator(t, f, 0, f, m);
        }

        @Override // java.util.Collection, java.util.Set
        public boolean add(K e) {
            V v = this.value;
            if (v == null) {
                throw new UnsupportedOperationException();
            }
            return this.map.putVal(e, v, true) == null;
        }

        @Override // java.util.Collection, java.util.Set
        public boolean addAll(Collection<? extends K> c) {
            boolean added = false;
            V v = this.value;
            if (v == null) {
                throw new UnsupportedOperationException();
            }
            for (K e : c) {
                if (this.map.putVal(e, v, true) == null) {
                    added = true;
                }
            }
            return added;
        }

        @Override // java.util.Collection, java.util.Set
        public int hashCode() {
            int h = 0;
            Iterator i$ = iterator();
            while (i$.hasNext()) {
                K e = i$.next();
                h += e.hashCode();
            }
            return h;
        }

        @Override // java.util.Collection, java.util.Set
        public boolean equals(Object o) {
            Set<?> c;
            return (o instanceof Set) && ((c = (Set) o) == this || (containsAll(c) && c.containsAll(this)));
        }

        public JSR166Helper.Spliterator<K> _spliterator() {
            ConcurrentHashMap<K, V> m = this.map;
            long n = m.sumCount();
            Node<K, V>[] t = m.table;
            int f = t == null ? 0 : t.length;
            return new KeySpliterator(t, f, 0, f, n < 0 ? 0L : n);
        }

        public void forEach(JSR166Helper.Consumer<? super K> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Node<K, V>[] nodeArr = this.map.table;
            if (nodeArr != null) {
                Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
                while (true) {
                    Node<K, V> nodeAdvance = traverser.advance();
                    if (nodeAdvance != null) {
                        consumer.accept(nodeAdvance.key);
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$ValuesView.class */
    static final class ValuesView<K, V> extends CollectionView<K, V, V> implements Collection<V>, Serializable {
        private static final long serialVersionUID = 2249069246763182397L;

        ValuesView(ConcurrentHashMap<K, V> map) {
            super(map);
        }

        @Override // org.ehcache.impl.internal.concurrent.ConcurrentHashMap.CollectionView, java.util.Collection
        public final boolean contains(Object o) {
            return this.map.containsValue(o);
        }

        @Override // org.ehcache.impl.internal.concurrent.ConcurrentHashMap.CollectionView, java.util.Collection
        public final boolean remove(Object o) {
            if (o != null) {
                Iterator<V> it = iterator();
                while (it.hasNext()) {
                    if (o.equals(it.next())) {
                        it.remove();
                        return true;
                    }
                }
                return false;
            }
            return false;
        }

        @Override // org.ehcache.impl.internal.concurrent.ConcurrentHashMap.CollectionView, java.util.Collection, java.lang.Iterable
        public final Iterator<V> iterator() {
            ConcurrentHashMap<K, V> m = this.map;
            Node<K, V>[] t = m.table;
            int f = t == null ? 0 : t.length;
            return new ValueIterator(t, f, 0, f, m);
        }

        @Override // java.util.Collection
        public final boolean add(V e) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection
        public final boolean addAll(Collection<? extends V> c) {
            throw new UnsupportedOperationException();
        }

        public JSR166Helper.Spliterator<V> _spliterator() {
            ConcurrentHashMap<K, V> m = this.map;
            long n = m.sumCount();
            Node<K, V>[] t = m.table;
            int f = t == null ? 0 : t.length;
            return new ValueSpliterator(t, f, 0, f, n < 0 ? 0L : n);
        }

        public void forEach(JSR166Helper.Consumer<? super V> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Node<K, V>[] nodeArr = this.map.table;
            if (nodeArr != null) {
                Traverser traverser = new Traverser(nodeArr, nodeArr.length, 0, nodeArr.length);
                while (true) {
                    Node<K, V> nodeAdvance = traverser.advance();
                    if (nodeAdvance != null) {
                        consumer.accept(nodeAdvance.val);
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$EntrySetView.class */
    static final class EntrySetView<K, V> extends CollectionView<K, V, Map.Entry<K, V>> implements Set<Map.Entry<K, V>>, Serializable {
        private static final long serialVersionUID = 2249069246763182397L;

        EntrySetView(ConcurrentHashMap<K, V> map) {
            super(map);
        }

        @Override // org.ehcache.impl.internal.concurrent.ConcurrentHashMap.CollectionView, java.util.Collection
        public boolean contains(Object o) {
            Map.Entry<?, ?> e;
            Object k;
            Object r;
            Object v;
            return (!(o instanceof Map.Entry) || (k = (e = (Map.Entry) o).getKey()) == null || (r = this.map.get(k)) == null || (v = e.getValue()) == null || (v != r && !v.equals(r))) ? false : true;
        }

        @Override // org.ehcache.impl.internal.concurrent.ConcurrentHashMap.CollectionView, java.util.Collection
        public boolean remove(Object o) {
            Map.Entry<?, ?> e;
            Object k;
            Object v;
            return (o instanceof Map.Entry) && (k = (e = (Map.Entry) o).getKey()) != null && (v = e.getValue()) != null && this.map.remove(k, v);
        }

        @Override // org.ehcache.impl.internal.concurrent.ConcurrentHashMap.CollectionView, java.util.Collection, java.lang.Iterable
        public Iterator<Map.Entry<K, V>> iterator() {
            ConcurrentHashMap<K, V> m = this.map;
            Node<K, V>[] t = m.table;
            int f = t == null ? 0 : t.length;
            return new EntryIterator(t, f, 0, f, m);
        }

        @Override // java.util.Collection, java.util.Set
        public boolean add(Map.Entry<K, V> e) {
            return this.map.putVal(e.getKey(), e.getValue(), false) == null;
        }

        @Override // java.util.Collection, java.util.Set
        public boolean addAll(Collection<? extends Map.Entry<K, V>> c) {
            boolean added = false;
            for (Map.Entry<K, V> e : c) {
                if (add((Map.Entry) e)) {
                    added = true;
                }
            }
            return added;
        }

        @Override // java.util.Collection, java.util.Set
        public final int hashCode() {
            int h = 0;
            Node<K, V>[] t = this.map.table;
            if (t != null) {
                Traverser<K, V> it = new Traverser<>(t, t.length, 0, t.length);
                while (true) {
                    Node<K, V> p = it.advance();
                    if (p == null) {
                        break;
                    }
                    h += p.hashCode();
                }
            }
            return h;
        }

        @Override // java.util.Collection, java.util.Set
        public final boolean equals(Object o) {
            Set<?> c;
            return (o instanceof Set) && ((c = (Set) o) == this || (containsAll(c) && c.containsAll(this)));
        }

        public JSR166Helper.Spliterator<Map.Entry<K, V>> _spliterator() {
            ConcurrentHashMap<K, V> m = this.map;
            long n = m.sumCount();
            Node<K, V>[] t = m.table;
            int f = t == null ? 0 : t.length;
            return new EntrySpliterator(t, f, 0, f, n < 0 ? 0L : n, m);
        }

        public void forEach(JSR166Helper.Consumer<? super Map.Entry<K, V>> action) {
            if (action == null) {
                throw new NullPointerException();
            }
            Node<K, V>[] t = this.map.table;
            if (t != null) {
                Traverser<K, V> it = new Traverser<>(t, t.length, 0, t.length);
                while (true) {
                    Node<K, V> p = it.advance();
                    if (p != null) {
                        action.accept(new MapEntry(p.key, p.val, this.map));
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$BulkTask.class */
    static abstract class BulkTask<K, V, R> extends CountedCompleter<R> {
        Node<K, V>[] tab;
        Node<K, V> next;
        TableStack<K, V> stack;
        TableStack<K, V> spare;
        int index;
        int baseIndex;
        int baseLimit;
        final int baseSize;
        int batch;

        BulkTask(BulkTask<K, V, ?> par, int b, int i, int f, Node<K, V>[] t) {
            super(par);
            this.batch = b;
            this.baseIndex = i;
            this.index = i;
            this.tab = t;
            if (t == null) {
                this.baseLimit = 0;
                this.baseSize = 0;
            } else if (par == null) {
                int length = t.length;
                this.baseLimit = length;
                this.baseSize = length;
            } else {
                this.baseLimit = f;
                this.baseSize = par.baseSize;
            }
        }

        final Node<K, V> advance() {
            Node<K, V>[] t;
            int n;
            int i;
            Node<K, V> node = this.next;
            Node<K, V> e = node;
            if (node != null) {
                e = e.next;
            }
            while (e == null) {
                if (this.baseIndex >= this.baseLimit || (t = this.tab) == null || (n = t.length) <= (i = this.index) || i < 0) {
                    this.next = null;
                    return null;
                }
                Node<K, V> nodeTabAt = ConcurrentHashMap.tabAt(t, i);
                e = nodeTabAt;
                if (nodeTabAt != null && e.hash < 0) {
                    if (e instanceof ForwardingNode) {
                        this.tab = ((ForwardingNode) e).nextTable;
                        e = null;
                        pushState(t, i, n);
                    } else if (e instanceof TreeBin) {
                        e = ((TreeBin) e).first;
                    } else {
                        e = null;
                    }
                }
                if (this.stack != null) {
                    recoverState(n);
                } else {
                    int i2 = i + this.baseSize;
                    this.index = i2;
                    if (i2 >= n) {
                        int i3 = this.baseIndex + 1;
                        this.baseIndex = i3;
                        this.index = i3;
                    }
                }
            }
            Node<K, V> node2 = e;
            this.next = node2;
            return node2;
        }

        private void pushState(Node<K, V>[] t, int i, int n) {
            TableStack<K, V> s = this.spare;
            if (s != null) {
                this.spare = s.next;
            } else {
                s = new TableStack<>();
            }
            s.tab = t;
            s.length = n;
            s.index = i;
            s.next = this.stack;
            this.stack = s;
        }

        private void recoverState(int n) {
            TableStack<K, V> s;
            while (true) {
                s = this.stack;
                if (s == null) {
                    break;
                }
                int i = this.index;
                int len = s.length;
                int i2 = i + len;
                this.index = i2;
                if (i2 < n) {
                    break;
                }
                n = len;
                this.index = s.index;
                this.tab = s.tab;
                s.tab = null;
                TableStack<K, V> next = s.next;
                s.next = this.spare;
                this.stack = next;
                this.spare = s;
            }
            if (s == null) {
                int i3 = this.index + this.baseSize;
                this.index = i3;
                if (i3 >= n) {
                    int i4 = this.baseIndex + 1;
                    this.baseIndex = i4;
                    this.index = i4;
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$ForEachKeyTask.class */
    static final class ForEachKeyTask<K, V> extends BulkTask<K, V, Void> {
        final JSR166Helper.Consumer<? super K> action;

        ForEachKeyTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, JSR166Helper.Consumer<? super K> action) {
            super(p, b, i, f, t);
            this.action = action;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            JSR166Helper.Consumer<? super K> consumer = this.action;
            if (consumer != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    new ForEachKeyTask(this, i4, i3, i2, this.tab, consumer).fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance != null) {
                        consumer.accept(nodeAdvance.key);
                    } else {
                        propagateCompletion();
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$ForEachValueTask.class */
    static final class ForEachValueTask<K, V> extends BulkTask<K, V, Void> {
        final JSR166Helper.Consumer<? super V> action;

        ForEachValueTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, JSR166Helper.Consumer<? super V> action) {
            super(p, b, i, f, t);
            this.action = action;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            JSR166Helper.Consumer<? super V> consumer = this.action;
            if (consumer != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    new ForEachValueTask(this, i4, i3, i2, this.tab, consumer).fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance != null) {
                        consumer.accept(nodeAdvance.val);
                    } else {
                        propagateCompletion();
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$ForEachEntryTask.class */
    static final class ForEachEntryTask<K, V> extends BulkTask<K, V, Void> {
        final JSR166Helper.Consumer<? super Map.Entry<K, V>> action;

        ForEachEntryTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, JSR166Helper.Consumer<? super Map.Entry<K, V>> action) {
            super(p, b, i, f, t);
            this.action = action;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            JSR166Helper.Consumer<? super Map.Entry<K, V>> action = this.action;
            if (action != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int f = this.baseLimit;
                    int h = (f + i) >>> 1;
                    if (h <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i2 = this.batch >>> 1;
                    this.batch = i2;
                    this.baseLimit = h;
                    new ForEachEntryTask(this, i2, h, f, this.tab, action).fork();
                }
                while (true) {
                    Node<K, V> p = advance();
                    if (p != null) {
                        action.accept(p);
                    } else {
                        propagateCompletion();
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$ForEachMappingTask.class */
    static final class ForEachMappingTask<K, V> extends BulkTask<K, V, Void> {
        final JSR166Helper.BiConsumer<? super K, ? super V> action;

        ForEachMappingTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, JSR166Helper.BiConsumer<? super K, ? super V> action) {
            super(p, b, i, f, t);
            this.action = action;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            JSR166Helper.BiConsumer<? super K, ? super V> biConsumer = this.action;
            if (biConsumer != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    new ForEachMappingTask(this, i4, i3, i2, this.tab, biConsumer).fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance != null) {
                        biConsumer.accept(nodeAdvance.key, nodeAdvance.val);
                    } else {
                        propagateCompletion();
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$ForEachTransformedKeyTask.class */
    static final class ForEachTransformedKeyTask<K, V, U> extends BulkTask<K, V, Void> {
        final Function<? super K, ? extends U> transformer;
        final JSR166Helper.Consumer<? super U> action;

        ForEachTransformedKeyTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, Function<? super K, ? extends U> transformer, JSR166Helper.Consumer<? super U> action) {
            super(p, b, i, f, t);
            this.transformer = transformer;
            this.action = action;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            JSR166Helper.Consumer<? super U> consumer;
            Function<? super K, ? extends U> function = this.transformer;
            if (function != null && (consumer = this.action) != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    new ForEachTransformedKeyTask(this, i4, i3, i2, this.tab, function, consumer).fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance != null) {
                        U uApply = function.apply(nodeAdvance.key);
                        if (uApply != null) {
                            consumer.accept(uApply);
                        }
                    } else {
                        propagateCompletion();
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$ForEachTransformedValueTask.class */
    static final class ForEachTransformedValueTask<K, V, U> extends BulkTask<K, V, Void> {
        final Function<? super V, ? extends U> transformer;
        final JSR166Helper.Consumer<? super U> action;

        ForEachTransformedValueTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, Function<? super V, ? extends U> transformer, JSR166Helper.Consumer<? super U> action) {
            super(p, b, i, f, t);
            this.transformer = transformer;
            this.action = action;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            JSR166Helper.Consumer<? super U> consumer;
            Function<? super V, ? extends U> function = this.transformer;
            if (function != null && (consumer = this.action) != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    new ForEachTransformedValueTask(this, i4, i3, i2, this.tab, function, consumer).fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance != null) {
                        U uApply = function.apply(nodeAdvance.val);
                        if (uApply != null) {
                            consumer.accept(uApply);
                        }
                    } else {
                        propagateCompletion();
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$ForEachTransformedEntryTask.class */
    static final class ForEachTransformedEntryTask<K, V, U> extends BulkTask<K, V, Void> {
        final Function<Map.Entry<K, V>, ? extends U> transformer;
        final JSR166Helper.Consumer<? super U> action;

        ForEachTransformedEntryTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, Function<Map.Entry<K, V>, ? extends U> transformer, JSR166Helper.Consumer<? super U> action) {
            super(p, b, i, f, t);
            this.transformer = transformer;
            this.action = action;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            JSR166Helper.Consumer<? super U> consumer;
            Function<Map.Entry<K, V>, ? extends U> function = this.transformer;
            if (function != null && (consumer = this.action) != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    new ForEachTransformedEntryTask(this, i4, i3, i2, this.tab, function, consumer).fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance != null) {
                        U uApply = function.apply(nodeAdvance);
                        if (uApply != null) {
                            consumer.accept(uApply);
                        }
                    } else {
                        propagateCompletion();
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$ForEachTransformedMappingTask.class */
    static final class ForEachTransformedMappingTask<K, V, U> extends BulkTask<K, V, Void> {
        final BiFunction<? super K, ? super V, ? extends U> transformer;
        final JSR166Helper.Consumer<? super U> action;

        ForEachTransformedMappingTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, BiFunction<? super K, ? super V, ? extends U> transformer, JSR166Helper.Consumer<? super U> action) {
            super(p, b, i, f, t);
            this.transformer = transformer;
            this.action = action;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            JSR166Helper.Consumer<? super U> consumer;
            BiFunction<? super K, ? super V, ? extends U> biFunction = this.transformer;
            if (biFunction != null && (consumer = this.action) != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    new ForEachTransformedMappingTask(this, i4, i3, i2, this.tab, biFunction, consumer).fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance != null) {
                        U uApply = biFunction.apply(nodeAdvance.key, nodeAdvance.val);
                        if (uApply != null) {
                            consumer.accept(uApply);
                        }
                    } else {
                        propagateCompletion();
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$SearchKeysTask.class */
    static final class SearchKeysTask<K, V, U> extends BulkTask<K, V, U> {
        final Function<? super K, ? extends U> searchFunction;
        final AtomicReference<U> result;

        SearchKeysTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, Function<? super K, ? extends U> searchFunction, AtomicReference<U> result) {
            super(p, b, i, f, t);
            this.searchFunction = searchFunction;
            this.result = result;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter, org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final U getRawResult() {
            return this.result.get();
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            AtomicReference<U> atomicReference;
            Function<? super K, ? extends U> function = this.searchFunction;
            if (function != null && (atomicReference = this.result) != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    if (atomicReference.get() != null) {
                        return;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    new SearchKeysTask(this, i4, i3, i2, this.tab, function, atomicReference).fork();
                }
                while (atomicReference.get() == null) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        propagateCompletion();
                        return;
                    }
                    U uApply = function.apply(nodeAdvance.key);
                    if (uApply != null) {
                        if (atomicReference.compareAndSet(null, uApply)) {
                            quietlyCompleteRoot();
                            return;
                        }
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$SearchValuesTask.class */
    static final class SearchValuesTask<K, V, U> extends BulkTask<K, V, U> {
        final Function<? super V, ? extends U> searchFunction;
        final AtomicReference<U> result;

        SearchValuesTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, Function<? super V, ? extends U> searchFunction, AtomicReference<U> result) {
            super(p, b, i, f, t);
            this.searchFunction = searchFunction;
            this.result = result;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter, org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final U getRawResult() {
            return this.result.get();
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            AtomicReference<U> atomicReference;
            Function<? super V, ? extends U> function = this.searchFunction;
            if (function != null && (atomicReference = this.result) != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    if (atomicReference.get() != null) {
                        return;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    new SearchValuesTask(this, i4, i3, i2, this.tab, function, atomicReference).fork();
                }
                while (atomicReference.get() == null) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        propagateCompletion();
                        return;
                    }
                    U uApply = function.apply(nodeAdvance.val);
                    if (uApply != null) {
                        if (atomicReference.compareAndSet(null, uApply)) {
                            quietlyCompleteRoot();
                            return;
                        }
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$SearchEntriesTask.class */
    static final class SearchEntriesTask<K, V, U> extends BulkTask<K, V, U> {
        final Function<Map.Entry<K, V>, ? extends U> searchFunction;
        final AtomicReference<U> result;

        SearchEntriesTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, Function<Map.Entry<K, V>, ? extends U> searchFunction, AtomicReference<U> result) {
            super(p, b, i, f, t);
            this.searchFunction = searchFunction;
            this.result = result;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter, org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final U getRawResult() {
            return this.result.get();
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            AtomicReference<U> result;
            Function<Map.Entry<K, V>, ? extends U> searchFunction = this.searchFunction;
            if (searchFunction != null && (result = this.result) != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int f = this.baseLimit;
                    int h = (f + i) >>> 1;
                    if (h <= i) {
                        break;
                    }
                    if (result.get() != null) {
                        return;
                    }
                    addToPendingCount(1);
                    int i2 = this.batch >>> 1;
                    this.batch = i2;
                    this.baseLimit = h;
                    new SearchEntriesTask(this, i2, h, f, this.tab, searchFunction, result).fork();
                }
                while (result.get() == null) {
                    Node<K, V> p = advance();
                    if (p == null) {
                        propagateCompletion();
                        return;
                    }
                    U u = searchFunction.apply(p);
                    if (u != null) {
                        if (result.compareAndSet(null, u)) {
                            quietlyCompleteRoot();
                            return;
                        }
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$SearchMappingsTask.class */
    static final class SearchMappingsTask<K, V, U> extends BulkTask<K, V, U> {
        final BiFunction<? super K, ? super V, ? extends U> searchFunction;
        final AtomicReference<U> result;

        SearchMappingsTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, BiFunction<? super K, ? super V, ? extends U> searchFunction, AtomicReference<U> result) {
            super(p, b, i, f, t);
            this.searchFunction = searchFunction;
            this.result = result;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter, org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final U getRawResult() {
            return this.result.get();
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            AtomicReference<U> atomicReference;
            BiFunction<? super K, ? super V, ? extends U> biFunction = this.searchFunction;
            if (biFunction != null && (atomicReference = this.result) != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    if (atomicReference.get() != null) {
                        return;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    new SearchMappingsTask(this, i4, i3, i2, this.tab, biFunction, atomicReference).fork();
                }
                while (atomicReference.get() == null) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        propagateCompletion();
                        return;
                    }
                    U uApply = biFunction.apply(nodeAdvance.key, nodeAdvance.val);
                    if (uApply != null) {
                        if (atomicReference.compareAndSet(null, uApply)) {
                            quietlyCompleteRoot();
                            return;
                        }
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$ReduceKeysTask.class */
    static final class ReduceKeysTask<K, V> extends BulkTask<K, V, K> {
        final BiFunction<? super K, ? super K, ? extends K> reducer;
        K result;
        ReduceKeysTask<K, V> rights;
        ReduceKeysTask<K, V> nextRight;

        ReduceKeysTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, ReduceKeysTask<K, V> nextRight, BiFunction<? super K, ? super K, ? extends K> reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.reducer = reducer;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter, org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final K getRawResult() {
            return this.result;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v24, types: [K, java.lang.Object] */
        /* JADX WARN: Type inference failed for: r13v1, types: [K, java.lang.Object] */
        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            ?? r13;
            BiFunction<? super K, ? super K, ? extends K> biFunction = this.reducer;
            if (biFunction != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    ReduceKeysTask<K, V> reduceKeysTask = new ReduceKeysTask<>(this, i4, i3, i2, this.tab, this.rights, biFunction);
                    this.rights = reduceKeysTask;
                    reduceKeysTask.fork();
                }
                Object objApply = null;
                while (true) {
                    r13 = (Object) objApply;
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    }
                    Object obj = (K) nodeAdvance.key;
                    objApply = r13 == 0 ? obj : obj == null ? r13 : biFunction.apply(r13, obj);
                }
                this.result = r13;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        ReduceKeysTask reduceKeysTask2 = (ReduceKeysTask) countedCompleter;
                        ReduceKeysTask<K, V> reduceKeysTask3 = reduceKeysTask2.rights;
                        while (true) {
                            ReduceKeysTask<K, V> reduceKeysTask4 = reduceKeysTask3;
                            if (reduceKeysTask4 != null) {
                                K k = reduceKeysTask4.result;
                                if (k != 0) {
                                    Object obj2 = (K) reduceKeysTask2.result;
                                    reduceKeysTask2.result = obj2 == null ? k : biFunction.apply(obj2, k);
                                }
                                ReduceKeysTask<K, V> reduceKeysTask5 = reduceKeysTask4.nextRight;
                                reduceKeysTask3 = reduceKeysTask5;
                                reduceKeysTask2.rights = reduceKeysTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$ReduceValuesTask.class */
    static final class ReduceValuesTask<K, V> extends BulkTask<K, V, V> {
        final BiFunction<? super V, ? super V, ? extends V> reducer;
        V result;
        ReduceValuesTask<K, V> rights;
        ReduceValuesTask<K, V> nextRight;

        ReduceValuesTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, ReduceValuesTask<K, V> nextRight, BiFunction<? super V, ? super V, ? extends V> reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.reducer = reducer;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter, org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final V getRawResult() {
            return this.result;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v24, types: [V, java.lang.Object] */
        /* JADX WARN: Type inference failed for: r13v1, types: [V, java.lang.Object] */
        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            ?? r13;
            BiFunction<? super V, ? super V, ? extends V> biFunction = this.reducer;
            if (biFunction != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    ReduceValuesTask<K, V> reduceValuesTask = new ReduceValuesTask<>(this, i4, i3, i2, this.tab, this.rights, biFunction);
                    this.rights = reduceValuesTask;
                    reduceValuesTask.fork();
                }
                Object objApply = null;
                while (true) {
                    r13 = (Object) objApply;
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    }
                    Object obj = (V) nodeAdvance.val;
                    objApply = r13 == 0 ? obj : biFunction.apply(r13, obj);
                }
                this.result = r13;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        ReduceValuesTask reduceValuesTask2 = (ReduceValuesTask) countedCompleter;
                        ReduceValuesTask<K, V> reduceValuesTask3 = reduceValuesTask2.rights;
                        while (true) {
                            ReduceValuesTask<K, V> reduceValuesTask4 = reduceValuesTask3;
                            if (reduceValuesTask4 != null) {
                                V v = reduceValuesTask4.result;
                                if (v != 0) {
                                    Object obj2 = (V) reduceValuesTask2.result;
                                    reduceValuesTask2.result = obj2 == null ? v : biFunction.apply(obj2, v);
                                }
                                ReduceValuesTask<K, V> reduceValuesTask5 = reduceValuesTask4.nextRight;
                                reduceValuesTask3 = reduceValuesTask5;
                                reduceValuesTask2.rights = reduceValuesTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$ReduceEntriesTask.class */
    static final class ReduceEntriesTask<K, V> extends BulkTask<K, V, Map.Entry<K, V>> {
        final BiFunction<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> reducer;
        Map.Entry<K, V> result;
        ReduceEntriesTask<K, V> rights;
        ReduceEntriesTask<K, V> nextRight;

        ReduceEntriesTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, ReduceEntriesTask<K, V> nextRight, BiFunction<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.reducer = reducer;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter, org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final Map.Entry<K, V> getRawResult() {
            return this.result;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            Map.Entry<K, V> r;
            BiFunction<Map.Entry<K, V>, Map.Entry<K, V>, ? extends Map.Entry<K, V>> reducer = this.reducer;
            if (reducer != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int f = this.baseLimit;
                    int h = (f + i) >>> 1;
                    if (h <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i2 = this.batch >>> 1;
                    this.batch = i2;
                    this.baseLimit = h;
                    ReduceEntriesTask<K, V> reduceEntriesTask = new ReduceEntriesTask<>(this, i2, h, f, this.tab, this.rights, reducer);
                    this.rights = reduceEntriesTask;
                    reduceEntriesTask.fork();
                }
                Map.Entry<K, V> entryApply = null;
                while (true) {
                    r = entryApply;
                    Node<K, V> p = advance();
                    if (p == null) {
                        break;
                    } else {
                        entryApply = r == null ? p : reducer.apply(r, p);
                    }
                }
                this.result = r;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> c = countedCompleterFirstComplete;
                    if (c != null) {
                        ReduceEntriesTask<K, V> t = (ReduceEntriesTask) c;
                        ReduceEntriesTask<K, V> reduceEntriesTask2 = t.rights;
                        while (true) {
                            ReduceEntriesTask<K, V> s = reduceEntriesTask2;
                            if (s != null) {
                                Map.Entry<K, V> sr = s.result;
                                if (sr != null) {
                                    Map.Entry<K, V> tr = t.result;
                                    t.result = tr == null ? sr : reducer.apply(tr, sr);
                                }
                                ReduceEntriesTask<K, V> reduceEntriesTask3 = s.nextRight;
                                reduceEntriesTask2 = reduceEntriesTask3;
                                t.rights = reduceEntriesTask3;
                            }
                        }
                        countedCompleterFirstComplete = c.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$MapReduceKeysTask.class */
    static final class MapReduceKeysTask<K, V, U> extends BulkTask<K, V, U> {
        final Function<? super K, ? extends U> transformer;
        final BiFunction<? super U, ? super U, ? extends U> reducer;
        U result;
        MapReduceKeysTask<K, V, U> rights;
        MapReduceKeysTask<K, V, U> nextRight;

        MapReduceKeysTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, MapReduceKeysTask<K, V, U> nextRight, Function<? super K, ? extends U> transformer, BiFunction<? super U, ? super U, ? extends U> reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.reducer = reducer;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter, org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final U getRawResult() {
            return this.result;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v25, types: [U, java.lang.Object] */
        /* JADX WARN: Type inference failed for: r0v30, types: [java.lang.Object] */
        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            BiFunction<? super U, ? super U, ? extends U> biFunction;
            Function<? super K, ? extends U> function = this.transformer;
            if (function != null && (biFunction = this.reducer) != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    MapReduceKeysTask<K, V, U> mapReduceKeysTask = new MapReduceKeysTask<>(this, i4, i3, i2, this.tab, this.rights, function, biFunction);
                    this.rights = mapReduceKeysTask;
                    mapReduceKeysTask.fork();
                }
                U uApply = null;
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    }
                    U uApply2 = function.apply((K) nodeAdvance.key);
                    if (uApply2 != 0) {
                        uApply = uApply == null ? uApply2 : biFunction.apply((Object) uApply, uApply2);
                    }
                }
                this.result = uApply;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceKeysTask mapReduceKeysTask2 = (MapReduceKeysTask) countedCompleter;
                        MapReduceKeysTask<K, V, U> mapReduceKeysTask3 = mapReduceKeysTask2.rights;
                        while (true) {
                            MapReduceKeysTask<K, V, U> mapReduceKeysTask4 = mapReduceKeysTask3;
                            if (mapReduceKeysTask4 != null) {
                                U u = mapReduceKeysTask4.result;
                                if (u != 0) {
                                    Object obj = (U) mapReduceKeysTask2.result;
                                    mapReduceKeysTask2.result = obj == null ? u : biFunction.apply(obj, u);
                                }
                                MapReduceKeysTask<K, V, U> mapReduceKeysTask5 = mapReduceKeysTask4.nextRight;
                                mapReduceKeysTask3 = mapReduceKeysTask5;
                                mapReduceKeysTask2.rights = mapReduceKeysTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$MapReduceValuesTask.class */
    static final class MapReduceValuesTask<K, V, U> extends BulkTask<K, V, U> {
        final Function<? super V, ? extends U> transformer;
        final BiFunction<? super U, ? super U, ? extends U> reducer;
        U result;
        MapReduceValuesTask<K, V, U> rights;
        MapReduceValuesTask<K, V, U> nextRight;

        MapReduceValuesTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, MapReduceValuesTask<K, V, U> nextRight, Function<? super V, ? extends U> transformer, BiFunction<? super U, ? super U, ? extends U> reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.reducer = reducer;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter, org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final U getRawResult() {
            return this.result;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v25, types: [U, java.lang.Object] */
        /* JADX WARN: Type inference failed for: r0v30, types: [java.lang.Object] */
        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            BiFunction<? super U, ? super U, ? extends U> biFunction;
            Function<? super V, ? extends U> function = this.transformer;
            if (function != null && (biFunction = this.reducer) != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    MapReduceValuesTask<K, V, U> mapReduceValuesTask = new MapReduceValuesTask<>(this, i4, i3, i2, this.tab, this.rights, function, biFunction);
                    this.rights = mapReduceValuesTask;
                    mapReduceValuesTask.fork();
                }
                U uApply = null;
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    }
                    U uApply2 = function.apply((V) nodeAdvance.val);
                    if (uApply2 != 0) {
                        uApply = uApply == null ? uApply2 : biFunction.apply((Object) uApply, uApply2);
                    }
                }
                this.result = uApply;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceValuesTask mapReduceValuesTask2 = (MapReduceValuesTask) countedCompleter;
                        MapReduceValuesTask<K, V, U> mapReduceValuesTask3 = mapReduceValuesTask2.rights;
                        while (true) {
                            MapReduceValuesTask<K, V, U> mapReduceValuesTask4 = mapReduceValuesTask3;
                            if (mapReduceValuesTask4 != null) {
                                U u = mapReduceValuesTask4.result;
                                if (u != 0) {
                                    Object obj = (U) mapReduceValuesTask2.result;
                                    mapReduceValuesTask2.result = obj == null ? u : biFunction.apply(obj, u);
                                }
                                MapReduceValuesTask<K, V, U> mapReduceValuesTask5 = mapReduceValuesTask4.nextRight;
                                mapReduceValuesTask3 = mapReduceValuesTask5;
                                mapReduceValuesTask2.rights = mapReduceValuesTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$MapReduceEntriesTask.class */
    static final class MapReduceEntriesTask<K, V, U> extends BulkTask<K, V, U> {
        final Function<Map.Entry<K, V>, ? extends U> transformer;
        final BiFunction<? super U, ? super U, ? extends U> reducer;
        U result;
        MapReduceEntriesTask<K, V, U> rights;
        MapReduceEntriesTask<K, V, U> nextRight;

        MapReduceEntriesTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, MapReduceEntriesTask<K, V, U> nextRight, Function<Map.Entry<K, V>, ? extends U> transformer, BiFunction<? super U, ? super U, ? extends U> reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.reducer = reducer;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter, org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final U getRawResult() {
            return this.result;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v25, types: [U, java.lang.Object] */
        /* JADX WARN: Type inference failed for: r0v30, types: [java.lang.Object] */
        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            BiFunction<? super U, ? super U, ? extends U> biFunction;
            Function<Map.Entry<K, V>, ? extends U> function = this.transformer;
            if (function != null && (biFunction = this.reducer) != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    MapReduceEntriesTask<K, V, U> mapReduceEntriesTask = new MapReduceEntriesTask<>(this, i4, i3, i2, this.tab, this.rights, function, biFunction);
                    this.rights = mapReduceEntriesTask;
                    mapReduceEntriesTask.fork();
                }
                U uApply = null;
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    }
                    U uApply2 = function.apply(nodeAdvance);
                    if (uApply2 != 0) {
                        uApply = uApply == null ? uApply2 : biFunction.apply((Object) uApply, uApply2);
                    }
                }
                this.result = uApply;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceEntriesTask mapReduceEntriesTask2 = (MapReduceEntriesTask) countedCompleter;
                        MapReduceEntriesTask<K, V, U> mapReduceEntriesTask3 = mapReduceEntriesTask2.rights;
                        while (true) {
                            MapReduceEntriesTask<K, V, U> mapReduceEntriesTask4 = mapReduceEntriesTask3;
                            if (mapReduceEntriesTask4 != null) {
                                U u = mapReduceEntriesTask4.result;
                                if (u != 0) {
                                    Object obj = (U) mapReduceEntriesTask2.result;
                                    mapReduceEntriesTask2.result = obj == null ? u : biFunction.apply(obj, u);
                                }
                                MapReduceEntriesTask<K, V, U> mapReduceEntriesTask5 = mapReduceEntriesTask4.nextRight;
                                mapReduceEntriesTask3 = mapReduceEntriesTask5;
                                mapReduceEntriesTask2.rights = mapReduceEntriesTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$MapReduceMappingsTask.class */
    static final class MapReduceMappingsTask<K, V, U> extends BulkTask<K, V, U> {
        final BiFunction<? super K, ? super V, ? extends U> transformer;
        final BiFunction<? super U, ? super U, ? extends U> reducer;
        U result;
        MapReduceMappingsTask<K, V, U> rights;
        MapReduceMappingsTask<K, V, U> nextRight;

        MapReduceMappingsTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, MapReduceMappingsTask<K, V, U> nextRight, BiFunction<? super K, ? super V, ? extends U> transformer, BiFunction<? super U, ? super U, ? extends U> reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.reducer = reducer;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter, org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final U getRawResult() {
            return this.result;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v25, types: [U, java.lang.Object] */
        /* JADX WARN: Type inference failed for: r0v30, types: [java.lang.Object] */
        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            BiFunction<? super U, ? super U, ? extends U> biFunction;
            BiFunction<? super K, ? super V, ? extends U> biFunction2 = this.transformer;
            if (biFunction2 != null && (biFunction = this.reducer) != null) {
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    MapReduceMappingsTask<K, V, U> mapReduceMappingsTask = new MapReduceMappingsTask<>(this, i4, i3, i2, this.tab, this.rights, biFunction2, biFunction);
                    this.rights = mapReduceMappingsTask;
                    mapReduceMappingsTask.fork();
                }
                U uApply = null;
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    }
                    U uApply2 = biFunction2.apply((K) nodeAdvance.key, (V) nodeAdvance.val);
                    if (uApply2 != 0) {
                        uApply = uApply == null ? uApply2 : biFunction.apply((Object) uApply, uApply2);
                    }
                }
                this.result = uApply;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceMappingsTask mapReduceMappingsTask2 = (MapReduceMappingsTask) countedCompleter;
                        MapReduceMappingsTask<K, V, U> mapReduceMappingsTask3 = mapReduceMappingsTask2.rights;
                        while (true) {
                            MapReduceMappingsTask<K, V, U> mapReduceMappingsTask4 = mapReduceMappingsTask3;
                            if (mapReduceMappingsTask4 != null) {
                                U u = mapReduceMappingsTask4.result;
                                if (u != 0) {
                                    Object obj = (U) mapReduceMappingsTask2.result;
                                    mapReduceMappingsTask2.result = obj == null ? u : biFunction.apply(obj, u);
                                }
                                MapReduceMappingsTask<K, V, U> mapReduceMappingsTask5 = mapReduceMappingsTask4.nextRight;
                                mapReduceMappingsTask3 = mapReduceMappingsTask5;
                                mapReduceMappingsTask2.rights = mapReduceMappingsTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$MapReduceKeysToDoubleTask.class */
    static final class MapReduceKeysToDoubleTask<K, V> extends BulkTask<K, V, Double> {
        final JSR166Helper.ToDoubleFunction<? super K> transformer;
        final JSR166Helper.DoubleBinaryOperator reducer;
        final double basis;
        double result;
        MapReduceKeysToDoubleTask<K, V> rights;
        MapReduceKeysToDoubleTask<K, V> nextRight;

        MapReduceKeysToDoubleTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, MapReduceKeysToDoubleTask<K, V> nextRight, JSR166Helper.ToDoubleFunction<? super K> transformer, double basis, JSR166Helper.DoubleBinaryOperator reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter, org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final Double getRawResult() {
            return Double.valueOf(this.result);
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            JSR166Helper.DoubleBinaryOperator doubleBinaryOperator;
            JSR166Helper.ToDoubleFunction<? super K> toDoubleFunction = this.transformer;
            if (toDoubleFunction != null && (doubleBinaryOperator = this.reducer) != null) {
                double dApplyAsDouble = this.basis;
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    MapReduceKeysToDoubleTask<K, V> mapReduceKeysToDoubleTask = new MapReduceKeysToDoubleTask<>(this, i4, i3, i2, this.tab, this.rights, toDoubleFunction, dApplyAsDouble, doubleBinaryOperator);
                    this.rights = mapReduceKeysToDoubleTask;
                    mapReduceKeysToDoubleTask.fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    } else {
                        dApplyAsDouble = doubleBinaryOperator.applyAsDouble(dApplyAsDouble, toDoubleFunction.applyAsDouble(nodeAdvance.key));
                    }
                }
                this.result = dApplyAsDouble;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceKeysToDoubleTask mapReduceKeysToDoubleTask2 = (MapReduceKeysToDoubleTask) countedCompleter;
                        MapReduceKeysToDoubleTask<K, V> mapReduceKeysToDoubleTask3 = mapReduceKeysToDoubleTask2.rights;
                        while (true) {
                            MapReduceKeysToDoubleTask<K, V> mapReduceKeysToDoubleTask4 = mapReduceKeysToDoubleTask3;
                            if (mapReduceKeysToDoubleTask4 != null) {
                                mapReduceKeysToDoubleTask2.result = doubleBinaryOperator.applyAsDouble(mapReduceKeysToDoubleTask2.result, mapReduceKeysToDoubleTask4.result);
                                MapReduceKeysToDoubleTask<K, V> mapReduceKeysToDoubleTask5 = mapReduceKeysToDoubleTask4.nextRight;
                                mapReduceKeysToDoubleTask3 = mapReduceKeysToDoubleTask5;
                                mapReduceKeysToDoubleTask2.rights = mapReduceKeysToDoubleTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$MapReduceValuesToDoubleTask.class */
    static final class MapReduceValuesToDoubleTask<K, V> extends BulkTask<K, V, Double> {
        final JSR166Helper.ToDoubleFunction<? super V> transformer;
        final JSR166Helper.DoubleBinaryOperator reducer;
        final double basis;
        double result;
        MapReduceValuesToDoubleTask<K, V> rights;
        MapReduceValuesToDoubleTask<K, V> nextRight;

        MapReduceValuesToDoubleTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, MapReduceValuesToDoubleTask<K, V> nextRight, JSR166Helper.ToDoubleFunction<? super V> transformer, double basis, JSR166Helper.DoubleBinaryOperator reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter, org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final Double getRawResult() {
            return Double.valueOf(this.result);
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            JSR166Helper.DoubleBinaryOperator doubleBinaryOperator;
            JSR166Helper.ToDoubleFunction<? super V> toDoubleFunction = this.transformer;
            if (toDoubleFunction != null && (doubleBinaryOperator = this.reducer) != null) {
                double dApplyAsDouble = this.basis;
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    MapReduceValuesToDoubleTask<K, V> mapReduceValuesToDoubleTask = new MapReduceValuesToDoubleTask<>(this, i4, i3, i2, this.tab, this.rights, toDoubleFunction, dApplyAsDouble, doubleBinaryOperator);
                    this.rights = mapReduceValuesToDoubleTask;
                    mapReduceValuesToDoubleTask.fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    } else {
                        dApplyAsDouble = doubleBinaryOperator.applyAsDouble(dApplyAsDouble, toDoubleFunction.applyAsDouble(nodeAdvance.val));
                    }
                }
                this.result = dApplyAsDouble;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceValuesToDoubleTask mapReduceValuesToDoubleTask2 = (MapReduceValuesToDoubleTask) countedCompleter;
                        MapReduceValuesToDoubleTask<K, V> mapReduceValuesToDoubleTask3 = mapReduceValuesToDoubleTask2.rights;
                        while (true) {
                            MapReduceValuesToDoubleTask<K, V> mapReduceValuesToDoubleTask4 = mapReduceValuesToDoubleTask3;
                            if (mapReduceValuesToDoubleTask4 != null) {
                                mapReduceValuesToDoubleTask2.result = doubleBinaryOperator.applyAsDouble(mapReduceValuesToDoubleTask2.result, mapReduceValuesToDoubleTask4.result);
                                MapReduceValuesToDoubleTask<K, V> mapReduceValuesToDoubleTask5 = mapReduceValuesToDoubleTask4.nextRight;
                                mapReduceValuesToDoubleTask3 = mapReduceValuesToDoubleTask5;
                                mapReduceValuesToDoubleTask2.rights = mapReduceValuesToDoubleTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$MapReduceEntriesToDoubleTask.class */
    static final class MapReduceEntriesToDoubleTask<K, V> extends BulkTask<K, V, Double> {
        final JSR166Helper.ToDoubleFunction<Map.Entry<K, V>> transformer;
        final JSR166Helper.DoubleBinaryOperator reducer;
        final double basis;
        double result;
        MapReduceEntriesToDoubleTask<K, V> rights;
        MapReduceEntriesToDoubleTask<K, V> nextRight;

        MapReduceEntriesToDoubleTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, MapReduceEntriesToDoubleTask<K, V> nextRight, JSR166Helper.ToDoubleFunction<Map.Entry<K, V>> transformer, double basis, JSR166Helper.DoubleBinaryOperator reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter, org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final Double getRawResult() {
            return Double.valueOf(this.result);
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            JSR166Helper.DoubleBinaryOperator reducer;
            JSR166Helper.ToDoubleFunction<Map.Entry<K, V>> transformer = this.transformer;
            if (transformer != null && (reducer = this.reducer) != null) {
                double r = this.basis;
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int f = this.baseLimit;
                    int h = (f + i) >>> 1;
                    if (h <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i2 = this.batch >>> 1;
                    this.batch = i2;
                    this.baseLimit = h;
                    MapReduceEntriesToDoubleTask<K, V> mapReduceEntriesToDoubleTask = new MapReduceEntriesToDoubleTask<>(this, i2, h, f, this.tab, this.rights, transformer, r, reducer);
                    this.rights = mapReduceEntriesToDoubleTask;
                    mapReduceEntriesToDoubleTask.fork();
                }
                while (true) {
                    Node<K, V> p = advance();
                    if (p == null) {
                        break;
                    } else {
                        r = reducer.applyAsDouble(r, transformer.applyAsDouble(p));
                    }
                }
                this.result = r;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> c = countedCompleterFirstComplete;
                    if (c != null) {
                        MapReduceEntriesToDoubleTask<K, V> t = (MapReduceEntriesToDoubleTask) c;
                        MapReduceEntriesToDoubleTask<K, V> mapReduceEntriesToDoubleTask2 = t.rights;
                        while (true) {
                            MapReduceEntriesToDoubleTask<K, V> s = mapReduceEntriesToDoubleTask2;
                            if (s != null) {
                                t.result = reducer.applyAsDouble(t.result, s.result);
                                MapReduceEntriesToDoubleTask<K, V> mapReduceEntriesToDoubleTask3 = s.nextRight;
                                mapReduceEntriesToDoubleTask2 = mapReduceEntriesToDoubleTask3;
                                t.rights = mapReduceEntriesToDoubleTask3;
                            }
                        }
                        countedCompleterFirstComplete = c.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$MapReduceMappingsToDoubleTask.class */
    static final class MapReduceMappingsToDoubleTask<K, V> extends BulkTask<K, V, Double> {
        final JSR166Helper.ToDoubleBiFunction<? super K, ? super V> transformer;
        final JSR166Helper.DoubleBinaryOperator reducer;
        final double basis;
        double result;
        MapReduceMappingsToDoubleTask<K, V> rights;
        MapReduceMappingsToDoubleTask<K, V> nextRight;

        MapReduceMappingsToDoubleTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, MapReduceMappingsToDoubleTask<K, V> nextRight, JSR166Helper.ToDoubleBiFunction<? super K, ? super V> transformer, double basis, JSR166Helper.DoubleBinaryOperator reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter, org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final Double getRawResult() {
            return Double.valueOf(this.result);
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            JSR166Helper.DoubleBinaryOperator doubleBinaryOperator;
            JSR166Helper.ToDoubleBiFunction<? super K, ? super V> toDoubleBiFunction = this.transformer;
            if (toDoubleBiFunction != null && (doubleBinaryOperator = this.reducer) != null) {
                double dApplyAsDouble = this.basis;
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    MapReduceMappingsToDoubleTask<K, V> mapReduceMappingsToDoubleTask = new MapReduceMappingsToDoubleTask<>(this, i4, i3, i2, this.tab, this.rights, toDoubleBiFunction, dApplyAsDouble, doubleBinaryOperator);
                    this.rights = mapReduceMappingsToDoubleTask;
                    mapReduceMappingsToDoubleTask.fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    } else {
                        dApplyAsDouble = doubleBinaryOperator.applyAsDouble(dApplyAsDouble, toDoubleBiFunction.applyAsDouble(nodeAdvance.key, nodeAdvance.val));
                    }
                }
                this.result = dApplyAsDouble;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceMappingsToDoubleTask mapReduceMappingsToDoubleTask2 = (MapReduceMappingsToDoubleTask) countedCompleter;
                        MapReduceMappingsToDoubleTask<K, V> mapReduceMappingsToDoubleTask3 = mapReduceMappingsToDoubleTask2.rights;
                        while (true) {
                            MapReduceMappingsToDoubleTask<K, V> mapReduceMappingsToDoubleTask4 = mapReduceMappingsToDoubleTask3;
                            if (mapReduceMappingsToDoubleTask4 != null) {
                                mapReduceMappingsToDoubleTask2.result = doubleBinaryOperator.applyAsDouble(mapReduceMappingsToDoubleTask2.result, mapReduceMappingsToDoubleTask4.result);
                                MapReduceMappingsToDoubleTask<K, V> mapReduceMappingsToDoubleTask5 = mapReduceMappingsToDoubleTask4.nextRight;
                                mapReduceMappingsToDoubleTask3 = mapReduceMappingsToDoubleTask5;
                                mapReduceMappingsToDoubleTask2.rights = mapReduceMappingsToDoubleTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$MapReduceKeysToLongTask.class */
    static final class MapReduceKeysToLongTask<K, V> extends BulkTask<K, V, Long> {
        final JSR166Helper.ToLongFunction<? super K> transformer;
        final JSR166Helper.LongBinaryOperator reducer;
        final long basis;
        long result;
        MapReduceKeysToLongTask<K, V> rights;
        MapReduceKeysToLongTask<K, V> nextRight;

        MapReduceKeysToLongTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, MapReduceKeysToLongTask<K, V> nextRight, JSR166Helper.ToLongFunction<? super K> transformer, long basis, JSR166Helper.LongBinaryOperator reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter, org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final Long getRawResult() {
            return Long.valueOf(this.result);
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            JSR166Helper.LongBinaryOperator longBinaryOperator;
            JSR166Helper.ToLongFunction<? super K> toLongFunction = this.transformer;
            if (toLongFunction != null && (longBinaryOperator = this.reducer) != null) {
                long jApplyAsLong = this.basis;
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    MapReduceKeysToLongTask<K, V> mapReduceKeysToLongTask = new MapReduceKeysToLongTask<>(this, i4, i3, i2, this.tab, this.rights, toLongFunction, jApplyAsLong, longBinaryOperator);
                    this.rights = mapReduceKeysToLongTask;
                    mapReduceKeysToLongTask.fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    } else {
                        jApplyAsLong = longBinaryOperator.applyAsLong(jApplyAsLong, toLongFunction.applyAsLong(nodeAdvance.key));
                    }
                }
                this.result = jApplyAsLong;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceKeysToLongTask mapReduceKeysToLongTask2 = (MapReduceKeysToLongTask) countedCompleter;
                        MapReduceKeysToLongTask<K, V> mapReduceKeysToLongTask3 = mapReduceKeysToLongTask2.rights;
                        while (true) {
                            MapReduceKeysToLongTask<K, V> mapReduceKeysToLongTask4 = mapReduceKeysToLongTask3;
                            if (mapReduceKeysToLongTask4 != null) {
                                mapReduceKeysToLongTask2.result = longBinaryOperator.applyAsLong(mapReduceKeysToLongTask2.result, mapReduceKeysToLongTask4.result);
                                MapReduceKeysToLongTask<K, V> mapReduceKeysToLongTask5 = mapReduceKeysToLongTask4.nextRight;
                                mapReduceKeysToLongTask3 = mapReduceKeysToLongTask5;
                                mapReduceKeysToLongTask2.rights = mapReduceKeysToLongTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$MapReduceValuesToLongTask.class */
    static final class MapReduceValuesToLongTask<K, V> extends BulkTask<K, V, Long> {
        final JSR166Helper.ToLongFunction<? super V> transformer;
        final JSR166Helper.LongBinaryOperator reducer;
        final long basis;
        long result;
        MapReduceValuesToLongTask<K, V> rights;
        MapReduceValuesToLongTask<K, V> nextRight;

        MapReduceValuesToLongTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, MapReduceValuesToLongTask<K, V> nextRight, JSR166Helper.ToLongFunction<? super V> transformer, long basis, JSR166Helper.LongBinaryOperator reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter, org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final Long getRawResult() {
            return Long.valueOf(this.result);
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            JSR166Helper.LongBinaryOperator longBinaryOperator;
            JSR166Helper.ToLongFunction<? super V> toLongFunction = this.transformer;
            if (toLongFunction != null && (longBinaryOperator = this.reducer) != null) {
                long jApplyAsLong = this.basis;
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    MapReduceValuesToLongTask<K, V> mapReduceValuesToLongTask = new MapReduceValuesToLongTask<>(this, i4, i3, i2, this.tab, this.rights, toLongFunction, jApplyAsLong, longBinaryOperator);
                    this.rights = mapReduceValuesToLongTask;
                    mapReduceValuesToLongTask.fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    } else {
                        jApplyAsLong = longBinaryOperator.applyAsLong(jApplyAsLong, toLongFunction.applyAsLong(nodeAdvance.val));
                    }
                }
                this.result = jApplyAsLong;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceValuesToLongTask mapReduceValuesToLongTask2 = (MapReduceValuesToLongTask) countedCompleter;
                        MapReduceValuesToLongTask<K, V> mapReduceValuesToLongTask3 = mapReduceValuesToLongTask2.rights;
                        while (true) {
                            MapReduceValuesToLongTask<K, V> mapReduceValuesToLongTask4 = mapReduceValuesToLongTask3;
                            if (mapReduceValuesToLongTask4 != null) {
                                mapReduceValuesToLongTask2.result = longBinaryOperator.applyAsLong(mapReduceValuesToLongTask2.result, mapReduceValuesToLongTask4.result);
                                MapReduceValuesToLongTask<K, V> mapReduceValuesToLongTask5 = mapReduceValuesToLongTask4.nextRight;
                                mapReduceValuesToLongTask3 = mapReduceValuesToLongTask5;
                                mapReduceValuesToLongTask2.rights = mapReduceValuesToLongTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$MapReduceEntriesToLongTask.class */
    static final class MapReduceEntriesToLongTask<K, V> extends BulkTask<K, V, Long> {
        final JSR166Helper.ToLongFunction<Map.Entry<K, V>> transformer;
        final JSR166Helper.LongBinaryOperator reducer;
        final long basis;
        long result;
        MapReduceEntriesToLongTask<K, V> rights;
        MapReduceEntriesToLongTask<K, V> nextRight;

        MapReduceEntriesToLongTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, MapReduceEntriesToLongTask<K, V> nextRight, JSR166Helper.ToLongFunction<Map.Entry<K, V>> transformer, long basis, JSR166Helper.LongBinaryOperator reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter, org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final Long getRawResult() {
            return Long.valueOf(this.result);
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            JSR166Helper.LongBinaryOperator reducer;
            JSR166Helper.ToLongFunction<Map.Entry<K, V>> transformer = this.transformer;
            if (transformer != null && (reducer = this.reducer) != null) {
                long r = this.basis;
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int f = this.baseLimit;
                    int h = (f + i) >>> 1;
                    if (h <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i2 = this.batch >>> 1;
                    this.batch = i2;
                    this.baseLimit = h;
                    MapReduceEntriesToLongTask<K, V> mapReduceEntriesToLongTask = new MapReduceEntriesToLongTask<>(this, i2, h, f, this.tab, this.rights, transformer, r, reducer);
                    this.rights = mapReduceEntriesToLongTask;
                    mapReduceEntriesToLongTask.fork();
                }
                while (true) {
                    Node<K, V> p = advance();
                    if (p == null) {
                        break;
                    } else {
                        r = reducer.applyAsLong(r, transformer.applyAsLong(p));
                    }
                }
                this.result = r;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> c = countedCompleterFirstComplete;
                    if (c != null) {
                        MapReduceEntriesToLongTask<K, V> t = (MapReduceEntriesToLongTask) c;
                        MapReduceEntriesToLongTask<K, V> mapReduceEntriesToLongTask2 = t.rights;
                        while (true) {
                            MapReduceEntriesToLongTask<K, V> s = mapReduceEntriesToLongTask2;
                            if (s != null) {
                                t.result = reducer.applyAsLong(t.result, s.result);
                                MapReduceEntriesToLongTask<K, V> mapReduceEntriesToLongTask3 = s.nextRight;
                                mapReduceEntriesToLongTask2 = mapReduceEntriesToLongTask3;
                                t.rights = mapReduceEntriesToLongTask3;
                            }
                        }
                        countedCompleterFirstComplete = c.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$MapReduceMappingsToLongTask.class */
    static final class MapReduceMappingsToLongTask<K, V> extends BulkTask<K, V, Long> {
        final JSR166Helper.ToLongBiFunction<? super K, ? super V> transformer;
        final JSR166Helper.LongBinaryOperator reducer;
        final long basis;
        long result;
        MapReduceMappingsToLongTask<K, V> rights;
        MapReduceMappingsToLongTask<K, V> nextRight;

        MapReduceMappingsToLongTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, MapReduceMappingsToLongTask<K, V> nextRight, JSR166Helper.ToLongBiFunction<? super K, ? super V> transformer, long basis, JSR166Helper.LongBinaryOperator reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter, org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final Long getRawResult() {
            return Long.valueOf(this.result);
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            JSR166Helper.LongBinaryOperator longBinaryOperator;
            JSR166Helper.ToLongBiFunction<? super K, ? super V> toLongBiFunction = this.transformer;
            if (toLongBiFunction != null && (longBinaryOperator = this.reducer) != null) {
                long jApplyAsLong = this.basis;
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    MapReduceMappingsToLongTask<K, V> mapReduceMappingsToLongTask = new MapReduceMappingsToLongTask<>(this, i4, i3, i2, this.tab, this.rights, toLongBiFunction, jApplyAsLong, longBinaryOperator);
                    this.rights = mapReduceMappingsToLongTask;
                    mapReduceMappingsToLongTask.fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    } else {
                        jApplyAsLong = longBinaryOperator.applyAsLong(jApplyAsLong, toLongBiFunction.applyAsLong(nodeAdvance.key, nodeAdvance.val));
                    }
                }
                this.result = jApplyAsLong;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceMappingsToLongTask mapReduceMappingsToLongTask2 = (MapReduceMappingsToLongTask) countedCompleter;
                        MapReduceMappingsToLongTask<K, V> mapReduceMappingsToLongTask3 = mapReduceMappingsToLongTask2.rights;
                        while (true) {
                            MapReduceMappingsToLongTask<K, V> mapReduceMappingsToLongTask4 = mapReduceMappingsToLongTask3;
                            if (mapReduceMappingsToLongTask4 != null) {
                                mapReduceMappingsToLongTask2.result = longBinaryOperator.applyAsLong(mapReduceMappingsToLongTask2.result, mapReduceMappingsToLongTask4.result);
                                MapReduceMappingsToLongTask<K, V> mapReduceMappingsToLongTask5 = mapReduceMappingsToLongTask4.nextRight;
                                mapReduceMappingsToLongTask3 = mapReduceMappingsToLongTask5;
                                mapReduceMappingsToLongTask2.rights = mapReduceMappingsToLongTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$MapReduceKeysToIntTask.class */
    static final class MapReduceKeysToIntTask<K, V> extends BulkTask<K, V, Integer> {
        final JSR166Helper.ToIntFunction<? super K> transformer;
        final JSR166Helper.IntBinaryOperator reducer;
        final int basis;
        int result;
        MapReduceKeysToIntTask<K, V> rights;
        MapReduceKeysToIntTask<K, V> nextRight;

        MapReduceKeysToIntTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, MapReduceKeysToIntTask<K, V> nextRight, JSR166Helper.ToIntFunction<? super K> transformer, int basis, JSR166Helper.IntBinaryOperator reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter, org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final Integer getRawResult() {
            return Integer.valueOf(this.result);
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            JSR166Helper.IntBinaryOperator intBinaryOperator;
            JSR166Helper.ToIntFunction<? super K> toIntFunction = this.transformer;
            if (toIntFunction != null && (intBinaryOperator = this.reducer) != null) {
                int iApplyAsInt = this.basis;
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    MapReduceKeysToIntTask<K, V> mapReduceKeysToIntTask = new MapReduceKeysToIntTask<>(this, i4, i3, i2, this.tab, this.rights, toIntFunction, iApplyAsInt, intBinaryOperator);
                    this.rights = mapReduceKeysToIntTask;
                    mapReduceKeysToIntTask.fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    } else {
                        iApplyAsInt = intBinaryOperator.applyAsInt(iApplyAsInt, toIntFunction.applyAsInt(nodeAdvance.key));
                    }
                }
                this.result = iApplyAsInt;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceKeysToIntTask mapReduceKeysToIntTask2 = (MapReduceKeysToIntTask) countedCompleter;
                        MapReduceKeysToIntTask<K, V> mapReduceKeysToIntTask3 = mapReduceKeysToIntTask2.rights;
                        while (true) {
                            MapReduceKeysToIntTask<K, V> mapReduceKeysToIntTask4 = mapReduceKeysToIntTask3;
                            if (mapReduceKeysToIntTask4 != null) {
                                mapReduceKeysToIntTask2.result = intBinaryOperator.applyAsInt(mapReduceKeysToIntTask2.result, mapReduceKeysToIntTask4.result);
                                MapReduceKeysToIntTask<K, V> mapReduceKeysToIntTask5 = mapReduceKeysToIntTask4.nextRight;
                                mapReduceKeysToIntTask3 = mapReduceKeysToIntTask5;
                                mapReduceKeysToIntTask2.rights = mapReduceKeysToIntTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$MapReduceValuesToIntTask.class */
    static final class MapReduceValuesToIntTask<K, V> extends BulkTask<K, V, Integer> {
        final JSR166Helper.ToIntFunction<? super V> transformer;
        final JSR166Helper.IntBinaryOperator reducer;
        final int basis;
        int result;
        MapReduceValuesToIntTask<K, V> rights;
        MapReduceValuesToIntTask<K, V> nextRight;

        MapReduceValuesToIntTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, MapReduceValuesToIntTask<K, V> nextRight, JSR166Helper.ToIntFunction<? super V> transformer, int basis, JSR166Helper.IntBinaryOperator reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter, org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final Integer getRawResult() {
            return Integer.valueOf(this.result);
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            JSR166Helper.IntBinaryOperator intBinaryOperator;
            JSR166Helper.ToIntFunction<? super V> toIntFunction = this.transformer;
            if (toIntFunction != null && (intBinaryOperator = this.reducer) != null) {
                int iApplyAsInt = this.basis;
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    MapReduceValuesToIntTask<K, V> mapReduceValuesToIntTask = new MapReduceValuesToIntTask<>(this, i4, i3, i2, this.tab, this.rights, toIntFunction, iApplyAsInt, intBinaryOperator);
                    this.rights = mapReduceValuesToIntTask;
                    mapReduceValuesToIntTask.fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    } else {
                        iApplyAsInt = intBinaryOperator.applyAsInt(iApplyAsInt, toIntFunction.applyAsInt(nodeAdvance.val));
                    }
                }
                this.result = iApplyAsInt;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceValuesToIntTask mapReduceValuesToIntTask2 = (MapReduceValuesToIntTask) countedCompleter;
                        MapReduceValuesToIntTask<K, V> mapReduceValuesToIntTask3 = mapReduceValuesToIntTask2.rights;
                        while (true) {
                            MapReduceValuesToIntTask<K, V> mapReduceValuesToIntTask4 = mapReduceValuesToIntTask3;
                            if (mapReduceValuesToIntTask4 != null) {
                                mapReduceValuesToIntTask2.result = intBinaryOperator.applyAsInt(mapReduceValuesToIntTask2.result, mapReduceValuesToIntTask4.result);
                                MapReduceValuesToIntTask<K, V> mapReduceValuesToIntTask5 = mapReduceValuesToIntTask4.nextRight;
                                mapReduceValuesToIntTask3 = mapReduceValuesToIntTask5;
                                mapReduceValuesToIntTask2.rights = mapReduceValuesToIntTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$MapReduceEntriesToIntTask.class */
    static final class MapReduceEntriesToIntTask<K, V> extends BulkTask<K, V, Integer> {
        final JSR166Helper.ToIntFunction<Map.Entry<K, V>> transformer;
        final JSR166Helper.IntBinaryOperator reducer;
        final int basis;
        int result;
        MapReduceEntriesToIntTask<K, V> rights;
        MapReduceEntriesToIntTask<K, V> nextRight;

        MapReduceEntriesToIntTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, MapReduceEntriesToIntTask<K, V> nextRight, JSR166Helper.ToIntFunction<Map.Entry<K, V>> transformer, int basis, JSR166Helper.IntBinaryOperator reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter, org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final Integer getRawResult() {
            return Integer.valueOf(this.result);
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            JSR166Helper.IntBinaryOperator reducer;
            JSR166Helper.ToIntFunction<Map.Entry<K, V>> transformer = this.transformer;
            if (transformer != null && (reducer = this.reducer) != null) {
                int r = this.basis;
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int f = this.baseLimit;
                    int h = (f + i) >>> 1;
                    if (h <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i2 = this.batch >>> 1;
                    this.batch = i2;
                    this.baseLimit = h;
                    MapReduceEntriesToIntTask<K, V> mapReduceEntriesToIntTask = new MapReduceEntriesToIntTask<>(this, i2, h, f, this.tab, this.rights, transformer, r, reducer);
                    this.rights = mapReduceEntriesToIntTask;
                    mapReduceEntriesToIntTask.fork();
                }
                while (true) {
                    Node<K, V> p = advance();
                    if (p == null) {
                        break;
                    } else {
                        r = reducer.applyAsInt(r, transformer.applyAsInt(p));
                    }
                }
                this.result = r;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> c = countedCompleterFirstComplete;
                    if (c != null) {
                        MapReduceEntriesToIntTask<K, V> t = (MapReduceEntriesToIntTask) c;
                        MapReduceEntriesToIntTask<K, V> mapReduceEntriesToIntTask2 = t.rights;
                        while (true) {
                            MapReduceEntriesToIntTask<K, V> s = mapReduceEntriesToIntTask2;
                            if (s != null) {
                                t.result = reducer.applyAsInt(t.result, s.result);
                                MapReduceEntriesToIntTask<K, V> mapReduceEntriesToIntTask3 = s.nextRight;
                                mapReduceEntriesToIntTask2 = mapReduceEntriesToIntTask3;
                                t.rights = mapReduceEntriesToIntTask3;
                            }
                        }
                        countedCompleterFirstComplete = c.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ConcurrentHashMap$MapReduceMappingsToIntTask.class */
    static final class MapReduceMappingsToIntTask<K, V> extends BulkTask<K, V, Integer> {
        final JSR166Helper.ToIntBiFunction<? super K, ? super V> transformer;
        final JSR166Helper.IntBinaryOperator reducer;
        final int basis;
        int result;
        MapReduceMappingsToIntTask<K, V> rights;
        MapReduceMappingsToIntTask<K, V> nextRight;

        MapReduceMappingsToIntTask(BulkTask<K, V, ?> p, int b, int i, int f, Node<K, V>[] t, MapReduceMappingsToIntTask<K, V> nextRight, JSR166Helper.ToIntBiFunction<? super K, ? super V> transformer, int basis, JSR166Helper.IntBinaryOperator reducer) {
            super(p, b, i, f, t);
            this.nextRight = nextRight;
            this.transformer = transformer;
            this.basis = basis;
            this.reducer = reducer;
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter, org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final Integer getRawResult() {
            return Integer.valueOf(this.result);
        }

        @Override // org.ehcache.impl.internal.concurrent.CountedCompleter
        public final void compute() {
            JSR166Helper.IntBinaryOperator intBinaryOperator;
            JSR166Helper.ToIntBiFunction<? super K, ? super V> toIntBiFunction = this.transformer;
            if (toIntBiFunction != null && (intBinaryOperator = this.reducer) != null) {
                int iApplyAsInt = this.basis;
                int i = this.baseIndex;
                while (this.batch > 0) {
                    int i2 = this.baseLimit;
                    int i3 = (i2 + i) >>> 1;
                    if (i3 <= i) {
                        break;
                    }
                    addToPendingCount(1);
                    int i4 = this.batch >>> 1;
                    this.batch = i4;
                    this.baseLimit = i3;
                    MapReduceMappingsToIntTask<K, V> mapReduceMappingsToIntTask = new MapReduceMappingsToIntTask<>(this, i4, i3, i2, this.tab, this.rights, toIntBiFunction, iApplyAsInt, intBinaryOperator);
                    this.rights = mapReduceMappingsToIntTask;
                    mapReduceMappingsToIntTask.fork();
                }
                while (true) {
                    Node<K, V> nodeAdvance = advance();
                    if (nodeAdvance == null) {
                        break;
                    } else {
                        iApplyAsInt = intBinaryOperator.applyAsInt(iApplyAsInt, toIntBiFunction.applyAsInt(nodeAdvance.key, nodeAdvance.val));
                    }
                }
                this.result = iApplyAsInt;
                CountedCompleter<?> countedCompleterFirstComplete = firstComplete();
                while (true) {
                    CountedCompleter<?> countedCompleter = countedCompleterFirstComplete;
                    if (countedCompleter != null) {
                        MapReduceMappingsToIntTask mapReduceMappingsToIntTask2 = (MapReduceMappingsToIntTask) countedCompleter;
                        MapReduceMappingsToIntTask<K, V> mapReduceMappingsToIntTask3 = mapReduceMappingsToIntTask2.rights;
                        while (true) {
                            MapReduceMappingsToIntTask<K, V> mapReduceMappingsToIntTask4 = mapReduceMappingsToIntTask3;
                            if (mapReduceMappingsToIntTask4 != null) {
                                mapReduceMappingsToIntTask2.result = intBinaryOperator.applyAsInt(mapReduceMappingsToIntTask2.result, mapReduceMappingsToIntTask4.result);
                                MapReduceMappingsToIntTask<K, V> mapReduceMappingsToIntTask5 = mapReduceMappingsToIntTask4.nextRight;
                                mapReduceMappingsToIntTask3 = mapReduceMappingsToIntTask5;
                                mapReduceMappingsToIntTask2.rights = mapReduceMappingsToIntTask5;
                            }
                        }
                        countedCompleterFirstComplete = countedCompleter.nextComplete();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    public Map.Entry<K, V> getEvictionCandidate(Random rndm, int size, Comparator<? super V> prioritizer, EvictionAdvisor<? super K, ? super V> evictionAdvisor) {
        Node<K, V>[] tab = this.table;
        if (tab == null || size == 0) {
            return null;
        }
        K maxKey = null;
        V maxValue = null;
        int n = tab.length;
        int start = rndm.nextInt(n);
        Traverser<K, V> t = new Traverser<>(tab, n, start, n);
        while (true) {
            Node<K, V> p = t.advance();
            if (p != null) {
                K key = p.key;
                V val = p.val;
                if (!evictionAdvisor.adviseAgainstEviction(key, val)) {
                    if (maxKey == null || prioritizer.compare(val, maxValue) > 0) {
                        maxKey = key;
                        maxValue = val;
                    }
                    size--;
                    if (size == 0) {
                        int terminalIndex = t.index;
                        while (true) {
                            Node<K, V> p2 = t.advance();
                            if (p2 == null || t.index != terminalIndex) {
                                break;
                            }
                            K key2 = p2.key;
                            V val2 = p2.val;
                            if (!evictionAdvisor.adviseAgainstEviction(key2, val2) && prioritizer.compare(val2, maxValue) > 0) {
                                maxKey = key2;
                                maxValue = val2;
                            }
                        }
                        return new MapEntry(maxKey, maxValue, this);
                    }
                }
            } else {
                return getEvictionCandidateWrap(tab, start, size, maxKey, maxValue, prioritizer, evictionAdvisor);
            }
        }
    }

    private Map.Entry<K, V> getEvictionCandidateWrap(Node<K, V>[] tab, int start, int size, K maxKey, V maxVal, Comparator<? super V> prioritizer, EvictionAdvisor<? super K, ? super V> evictionAdvisor) {
        Traverser<K, V> t = new Traverser<>(tab, tab.length, 0, start);
        while (true) {
            Node<K, V> p = t.advance();
            if (p != null) {
                K key = p.key;
                V val = p.val;
                if (!evictionAdvisor.adviseAgainstEviction(key, val)) {
                    if (maxKey == null || prioritizer.compare(val, maxVal) > 0) {
                        maxKey = key;
                        maxVal = val;
                    }
                    size--;
                    if (size == 0) {
                        int terminalIndex = t.index;
                        while (true) {
                            Node<K, V> p2 = t.advance();
                            if (p2 == null || t.index != terminalIndex) {
                                break;
                            }
                            K key2 = p2.key;
                            V val2 = p2.val;
                            if (!evictionAdvisor.adviseAgainstEviction(key2, val2) && prioritizer.compare(val2, maxVal) > 0) {
                                maxKey = key2;
                                maxVal = val2;
                            }
                        }
                        return new MapEntry(maxKey, maxVal, this);
                    }
                }
            } else {
                if (maxKey == null) {
                    return null;
                }
                return new MapEntry(maxKey, maxVal, this);
            }
        }
    }
}
