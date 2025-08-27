package redis.clients.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* loaded from: jedis-2.9.3.jar:redis/clients/util/JedisByteHashMap.class */
public class JedisByteHashMap implements Map<byte[], byte[]>, Cloneable, Serializable {
    private static final long serialVersionUID = -6971431362627219416L;
    private transient Map<ByteArrayWrapper, byte[]> internalMap = new HashMap();

    @Override // java.util.Map
    public void clear() {
        this.internalMap.clear();
    }

    @Override // java.util.Map
    public boolean containsKey(Object key) {
        return key instanceof byte[] ? this.internalMap.containsKey(new ByteArrayWrapper((byte[]) key)) : this.internalMap.containsKey(key);
    }

    @Override // java.util.Map
    public boolean containsValue(Object value) {
        return this.internalMap.containsValue(value);
    }

    @Override // java.util.Map
    public Set<Map.Entry<byte[], byte[]>> entrySet() {
        HashSet<Map.Entry<byte[], byte[]>> hashSet = new HashSet<>();
        for (Map.Entry<ByteArrayWrapper, byte[]> entry : this.internalMap.entrySet()) {
            hashSet.add(new JedisByteEntry(entry.getKey().data, entry.getValue()));
        }
        return hashSet;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Map
    public byte[] get(Object key) {
        return key instanceof byte[] ? this.internalMap.get(new ByteArrayWrapper((byte[]) key)) : this.internalMap.get(key);
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.internalMap.isEmpty();
    }

    @Override // java.util.Map
    public Set<byte[]> keySet() {
        Set<byte[]> keySet = new HashSet<>();
        Iterator<ByteArrayWrapper> iterator = this.internalMap.keySet().iterator();
        while (iterator.hasNext()) {
            keySet.add(iterator.next().data);
        }
        return keySet;
    }

    @Override // java.util.Map
    public byte[] put(byte[] key, byte[] value) {
        return this.internalMap.put(new ByteArrayWrapper(key), value);
    }

    @Override // java.util.Map
    public void putAll(Map<? extends byte[], ? extends byte[]> m) {
        for (Map.Entry<? extends byte[], ? extends byte[]> next : m.entrySet()) {
            this.internalMap.put(new ByteArrayWrapper(next.getKey()), next.getValue());
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Map
    public byte[] remove(Object key) {
        return key instanceof byte[] ? this.internalMap.remove(new ByteArrayWrapper((byte[]) key)) : this.internalMap.remove(key);
    }

    @Override // java.util.Map
    public int size() {
        return this.internalMap.size();
    }

    @Override // java.util.Map
    public Collection<byte[]> values() {
        return this.internalMap.values();
    }

    /* loaded from: jedis-2.9.3.jar:redis/clients/util/JedisByteHashMap$ByteArrayWrapper.class */
    private static final class ByteArrayWrapper {
        private final byte[] data;

        public ByteArrayWrapper(byte[] data) {
            if (data == null) {
                throw new NullPointerException();
            }
            this.data = data;
        }

        public boolean equals(Object other) {
            if (!(other instanceof ByteArrayWrapper)) {
                return false;
            }
            return Arrays.equals(this.data, ((ByteArrayWrapper) other).data);
        }

        public int hashCode() {
            return Arrays.hashCode(this.data);
        }
    }

    /* loaded from: jedis-2.9.3.jar:redis/clients/util/JedisByteHashMap$JedisByteEntry.class */
    private static final class JedisByteEntry implements Map.Entry<byte[], byte[]> {
        private byte[] value;
        private byte[] key;

        public JedisByteEntry(byte[] key, byte[] value) {
            this.key = key;
            this.value = value;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map.Entry
        public byte[] getKey() {
            return this.key;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map.Entry
        public byte[] getValue() {
            return this.value;
        }

        @Override // java.util.Map.Entry
        public byte[] setValue(byte[] value) {
            this.value = value;
            return value;
        }
    }
}
