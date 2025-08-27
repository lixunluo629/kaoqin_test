package io.netty.handler.codec;

import io.netty.handler.codec.Headers;
import io.netty.util.HashingStrategy;
import io.netty.util.internal.MathUtil;
import io.netty.util.internal.ObjectUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/DefaultHeaders.class */
public class DefaultHeaders<K, V, T extends Headers<K, V, T>> implements Headers<K, V, T> {
    static final int HASH_CODE_SEED = -1028477387;
    private final HeaderEntry<K, V>[] entries;
    protected final HeaderEntry<K, V> head;
    private final byte hashMask;
    private final ValueConverter<V> valueConverter;
    private final NameValidator<K> nameValidator;
    private final HashingStrategy<K> hashingStrategy;
    int size;

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/DefaultHeaders$NameValidator.class */
    public interface NameValidator<K> {
        public static final NameValidator NOT_NULL = new NameValidator() { // from class: io.netty.handler.codec.DefaultHeaders.NameValidator.1
            @Override // io.netty.handler.codec.DefaultHeaders.NameValidator
            public void validateName(Object name) {
                ObjectUtil.checkNotNull(name, "name");
            }
        };

        void validateName(K k);
    }

    public DefaultHeaders(ValueConverter<V> valueConverter) {
        this(HashingStrategy.JAVA_HASHER, valueConverter);
    }

    public DefaultHeaders(ValueConverter<V> valueConverter, NameValidator<K> nameValidator) {
        this(HashingStrategy.JAVA_HASHER, valueConverter, nameValidator);
    }

    public DefaultHeaders(HashingStrategy<K> nameHashingStrategy, ValueConverter<V> valueConverter) {
        this(nameHashingStrategy, valueConverter, NameValidator.NOT_NULL);
    }

    public DefaultHeaders(HashingStrategy<K> nameHashingStrategy, ValueConverter<V> valueConverter, NameValidator<K> nameValidator) {
        this(nameHashingStrategy, valueConverter, nameValidator, 16);
    }

    public DefaultHeaders(HashingStrategy<K> nameHashingStrategy, ValueConverter<V> valueConverter, NameValidator<K> nameValidator, int arraySizeHint) {
        this.valueConverter = (ValueConverter) ObjectUtil.checkNotNull(valueConverter, "valueConverter");
        this.nameValidator = (NameValidator) ObjectUtil.checkNotNull(nameValidator, "nameValidator");
        this.hashingStrategy = (HashingStrategy) ObjectUtil.checkNotNull(nameHashingStrategy, "nameHashingStrategy");
        this.entries = new HeaderEntry[MathUtil.findNextPositivePowerOfTwo(Math.max(2, Math.min(arraySizeHint, 128)))];
        this.hashMask = (byte) (this.entries.length - 1);
        this.head = new HeaderEntry<>();
    }

    @Override // io.netty.handler.codec.Headers
    public V get(K name) {
        ObjectUtil.checkNotNull(name, "name");
        int h = this.hashingStrategy.hashCode(name);
        int i = index(h);
        V value = null;
        for (HeaderEntry<K, V> e = this.entries[i]; e != null; e = e.next) {
            if (e.hash == h && this.hashingStrategy.equals(name, e.key)) {
                value = e.value;
            }
        }
        return value;
    }

    @Override // io.netty.handler.codec.Headers
    public V get(K name, V defaultValue) {
        V value = get(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.handler.codec.Headers
    public V getAndRemove(K k) {
        int iHashCode = this.hashingStrategy.hashCode(k);
        return (V) remove0(iHashCode, index(iHashCode), ObjectUtil.checkNotNull(k, "name"));
    }

    @Override // io.netty.handler.codec.Headers
    public V getAndRemove(K name, V defaultValue) {
        V value = getAndRemove(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    @Override // io.netty.handler.codec.Headers
    public List<V> getAll(K name) {
        ObjectUtil.checkNotNull(name, "name");
        LinkedList<V> values = new LinkedList<>();
        int h = this.hashingStrategy.hashCode(name);
        int i = index(h);
        HeaderEntry<K, V> headerEntry = this.entries[i];
        while (true) {
            HeaderEntry<K, V> e = headerEntry;
            if (e != null) {
                if (e.hash == h && this.hashingStrategy.equals(name, e.key)) {
                    values.addFirst(e.getValue());
                }
                headerEntry = e.next;
            } else {
                return values;
            }
        }
    }

    public Iterator<V> valueIterator(K name) {
        return new ValueIterator(name);
    }

    @Override // io.netty.handler.codec.Headers
    public List<V> getAllAndRemove(K name) {
        List<V> all = getAll(name);
        remove(name);
        return all;
    }

    @Override // io.netty.handler.codec.Headers
    public boolean contains(K name) {
        return get(name) != null;
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsObject(K name, Object value) {
        return contains(name, this.valueConverter.convertObject(ObjectUtil.checkNotNull(value, "value")));
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsBoolean(K name, boolean value) {
        return contains(name, this.valueConverter.convertBoolean(value));
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsByte(K name, byte value) {
        return contains(name, this.valueConverter.convertByte(value));
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsChar(K name, char value) {
        return contains(name, this.valueConverter.convertChar(value));
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsShort(K name, short value) {
        return contains(name, this.valueConverter.convertShort(value));
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsInt(K name, int value) {
        return contains(name, this.valueConverter.convertInt(value));
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsLong(K name, long value) {
        return contains(name, this.valueConverter.convertLong(value));
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsFloat(K name, float value) {
        return contains(name, this.valueConverter.convertFloat(value));
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsDouble(K name, double value) {
        return contains(name, this.valueConverter.convertDouble(value));
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsTimeMillis(K name, long value) {
        return contains(name, this.valueConverter.convertTimeMillis(value));
    }

    @Override // io.netty.handler.codec.Headers
    public boolean contains(K name, V value) {
        return contains(name, value, HashingStrategy.JAVA_HASHER);
    }

    public final boolean contains(K k, V v, HashingStrategy<? super V> hashingStrategy) {
        ObjectUtil.checkNotNull(k, "name");
        int iHashCode = this.hashingStrategy.hashCode(k);
        HeaderEntry<K, V> headerEntry = this.entries[index(iHashCode)];
        while (true) {
            HeaderEntry<K, V> headerEntry2 = headerEntry;
            if (headerEntry2 != null) {
                if (headerEntry2.hash == iHashCode && this.hashingStrategy.equals(k, headerEntry2.key) && hashingStrategy.equals(v, headerEntry2.value)) {
                    return true;
                }
                headerEntry = headerEntry2.next;
            } else {
                return false;
            }
        }
    }

    @Override // io.netty.handler.codec.Headers
    public int size() {
        return this.size;
    }

    @Override // io.netty.handler.codec.Headers
    public boolean isEmpty() {
        return this.head == this.head.after;
    }

    @Override // io.netty.handler.codec.Headers
    public Set<K> names() {
        if (isEmpty()) {
            return Collections.emptySet();
        }
        Set<K> names = new LinkedHashSet<>(size());
        HeaderEntry<K, V> headerEntry = this.head.after;
        while (true) {
            HeaderEntry<K, V> e = headerEntry;
            if (e != this.head) {
                names.add(e.getKey());
                headerEntry = e.after;
            } else {
                return names;
            }
        }
    }

    @Override // io.netty.handler.codec.Headers
    public T add(K k, V v) {
        this.nameValidator.validateName(k);
        ObjectUtil.checkNotNull(v, "value");
        int iHashCode = this.hashingStrategy.hashCode(k);
        add0(iHashCode, index(iHashCode), k, v);
        return (T) thisT();
    }

    @Override // io.netty.handler.codec.Headers
    public T add(K k, Iterable<? extends V> iterable) {
        this.nameValidator.validateName(k);
        int iHashCode = this.hashingStrategy.hashCode(k);
        int iIndex = index(iHashCode);
        Iterator<? extends V> it = iterable.iterator();
        while (it.hasNext()) {
            add0(iHashCode, iIndex, k, it.next());
        }
        return (T) thisT();
    }

    @Override // io.netty.handler.codec.Headers
    public T add(K k, V... vArr) {
        this.nameValidator.validateName(k);
        int iHashCode = this.hashingStrategy.hashCode(k);
        int iIndex = index(iHashCode);
        for (V v : vArr) {
            add0(iHashCode, iIndex, k, v);
        }
        return (T) thisT();
    }

    @Override // io.netty.handler.codec.Headers
    public T addObject(K k, Object obj) {
        return (T) add((DefaultHeaders<K, V, T>) k, (K) this.valueConverter.convertObject(ObjectUtil.checkNotNull(obj, "value")));
    }

    @Override // io.netty.handler.codec.Headers
    public T addObject(K k, Iterable<?> iterable) {
        Iterator<?> it = iterable.iterator();
        while (it.hasNext()) {
            addObject((DefaultHeaders<K, V, T>) k, it.next());
        }
        return (T) thisT();
    }

    @Override // io.netty.handler.codec.Headers
    public T addObject(K k, Object... objArr) {
        for (Object obj : objArr) {
            addObject((DefaultHeaders<K, V, T>) k, obj);
        }
        return (T) thisT();
    }

    @Override // io.netty.handler.codec.Headers
    public T addInt(K k, int i) {
        return (T) add((DefaultHeaders<K, V, T>) k, (K) this.valueConverter.convertInt(i));
    }

    @Override // io.netty.handler.codec.Headers
    public T addLong(K k, long j) {
        return (T) add((DefaultHeaders<K, V, T>) k, (K) this.valueConverter.convertLong(j));
    }

    @Override // io.netty.handler.codec.Headers
    public T addDouble(K k, double d) {
        return (T) add((DefaultHeaders<K, V, T>) k, (K) this.valueConverter.convertDouble(d));
    }

    @Override // io.netty.handler.codec.Headers
    public T addTimeMillis(K k, long j) {
        return (T) add((DefaultHeaders<K, V, T>) k, (K) this.valueConverter.convertTimeMillis(j));
    }

    @Override // io.netty.handler.codec.Headers
    public T addChar(K k, char c) {
        return (T) add((DefaultHeaders<K, V, T>) k, (K) this.valueConverter.convertChar(c));
    }

    @Override // io.netty.handler.codec.Headers
    public T addBoolean(K k, boolean z) {
        return (T) add((DefaultHeaders<K, V, T>) k, (K) this.valueConverter.convertBoolean(z));
    }

    @Override // io.netty.handler.codec.Headers
    public T addFloat(K k, float f) {
        return (T) add((DefaultHeaders<K, V, T>) k, (K) this.valueConverter.convertFloat(f));
    }

    @Override // io.netty.handler.codec.Headers
    public T addByte(K k, byte b) {
        return (T) add((DefaultHeaders<K, V, T>) k, (K) this.valueConverter.convertByte(b));
    }

    @Override // io.netty.handler.codec.Headers
    public T addShort(K k, short s) {
        return (T) add((DefaultHeaders<K, V, T>) k, (K) this.valueConverter.convertShort(s));
    }

    @Override // io.netty.handler.codec.Headers
    public T add(Headers<? extends K, ? extends V, ?> headers) {
        if (headers == this) {
            throw new IllegalArgumentException("can't add to itself.");
        }
        addImpl(headers);
        return (T) thisT();
    }

    protected void addImpl(Headers<? extends K, ? extends V, ?> headers) {
        if (headers instanceof DefaultHeaders) {
            DefaultHeaders<? extends K, ? extends V, T> defaultHeaders = (DefaultHeaders) headers;
            HeaderEntry<K, V> headerEntry = defaultHeaders.head.after;
            if (defaultHeaders.hashingStrategy == this.hashingStrategy && defaultHeaders.nameValidator == this.nameValidator) {
                while (headerEntry != defaultHeaders.head) {
                    add0(headerEntry.hash, index(headerEntry.hash), headerEntry.key, headerEntry.value);
                    headerEntry = headerEntry.after;
                }
                return;
            } else {
                while (headerEntry != defaultHeaders.head) {
                    add((DefaultHeaders<K, V, T>) headerEntry.key, (K) headerEntry.value);
                    headerEntry = headerEntry.after;
                }
                return;
            }
        }
        for (Map.Entry<? extends K, ? extends V> header : headers) {
            add((DefaultHeaders<K, V, T>) header.getKey(), (K) header.getValue());
        }
    }

    @Override // io.netty.handler.codec.Headers
    public T set(K k, V v) {
        this.nameValidator.validateName(k);
        ObjectUtil.checkNotNull(v, "value");
        int iHashCode = this.hashingStrategy.hashCode(k);
        int iIndex = index(iHashCode);
        remove0(iHashCode, iIndex, k);
        add0(iHashCode, iIndex, k, v);
        return (T) thisT();
    }

    @Override // io.netty.handler.codec.Headers
    public T set(K k, Iterable<? extends V> iterable) {
        V next;
        this.nameValidator.validateName(k);
        ObjectUtil.checkNotNull(iterable, "values");
        int iHashCode = this.hashingStrategy.hashCode(k);
        int iIndex = index(iHashCode);
        remove0(iHashCode, iIndex, k);
        Iterator<? extends V> it = iterable.iterator();
        while (it.hasNext() && (next = it.next()) != null) {
            add0(iHashCode, iIndex, k, next);
        }
        return (T) thisT();
    }

    @Override // io.netty.handler.codec.Headers
    public T set(K k, V... vArr) {
        V v;
        this.nameValidator.validateName(k);
        ObjectUtil.checkNotNull(vArr, "values");
        int iHashCode = this.hashingStrategy.hashCode(k);
        int iIndex = index(iHashCode);
        remove0(iHashCode, iIndex, k);
        int length = vArr.length;
        for (int i = 0; i < length && (v = vArr[i]) != null; i++) {
            add0(iHashCode, iIndex, k, v);
        }
        return (T) thisT();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.handler.codec.Headers
    public T setObject(K k, Object obj) {
        ObjectUtil.checkNotNull(obj, "value");
        return (T) set((DefaultHeaders<K, V, T>) k, (K) ObjectUtil.checkNotNull(this.valueConverter.convertObject(obj), "convertedValue"));
    }

    @Override // io.netty.handler.codec.Headers
    public T setObject(K k, Iterable<?> iterable) {
        Object next;
        this.nameValidator.validateName(k);
        int iHashCode = this.hashingStrategy.hashCode(k);
        int iIndex = index(iHashCode);
        remove0(iHashCode, iIndex, k);
        Iterator<?> it = iterable.iterator();
        while (it.hasNext() && (next = it.next()) != null) {
            add0(iHashCode, iIndex, k, this.valueConverter.convertObject(next));
        }
        return (T) thisT();
    }

    @Override // io.netty.handler.codec.Headers
    public T setObject(K k, Object... objArr) {
        Object obj;
        this.nameValidator.validateName(k);
        int iHashCode = this.hashingStrategy.hashCode(k);
        int iIndex = index(iHashCode);
        remove0(iHashCode, iIndex, k);
        int length = objArr.length;
        for (int i = 0; i < length && (obj = objArr[i]) != null; i++) {
            add0(iHashCode, iIndex, k, this.valueConverter.convertObject(obj));
        }
        return (T) thisT();
    }

    @Override // io.netty.handler.codec.Headers
    public T setInt(K k, int i) {
        return (T) set((DefaultHeaders<K, V, T>) k, (K) this.valueConverter.convertInt(i));
    }

    @Override // io.netty.handler.codec.Headers
    public T setLong(K k, long j) {
        return (T) set((DefaultHeaders<K, V, T>) k, (K) this.valueConverter.convertLong(j));
    }

    @Override // io.netty.handler.codec.Headers
    public T setDouble(K k, double d) {
        return (T) set((DefaultHeaders<K, V, T>) k, (K) this.valueConverter.convertDouble(d));
    }

    @Override // io.netty.handler.codec.Headers
    public T setTimeMillis(K k, long j) {
        return (T) set((DefaultHeaders<K, V, T>) k, (K) this.valueConverter.convertTimeMillis(j));
    }

    @Override // io.netty.handler.codec.Headers
    public T setFloat(K k, float f) {
        return (T) set((DefaultHeaders<K, V, T>) k, (K) this.valueConverter.convertFloat(f));
    }

    @Override // io.netty.handler.codec.Headers
    public T setChar(K k, char c) {
        return (T) set((DefaultHeaders<K, V, T>) k, (K) this.valueConverter.convertChar(c));
    }

    @Override // io.netty.handler.codec.Headers
    public T setBoolean(K k, boolean z) {
        return (T) set((DefaultHeaders<K, V, T>) k, (K) this.valueConverter.convertBoolean(z));
    }

    @Override // io.netty.handler.codec.Headers
    public T setByte(K k, byte b) {
        return (T) set((DefaultHeaders<K, V, T>) k, (K) this.valueConverter.convertByte(b));
    }

    @Override // io.netty.handler.codec.Headers
    public T setShort(K k, short s) {
        return (T) set((DefaultHeaders<K, V, T>) k, (K) this.valueConverter.convertShort(s));
    }

    @Override // io.netty.handler.codec.Headers
    public T set(Headers<? extends K, ? extends V, ?> headers) {
        if (headers != this) {
            clear();
            addImpl(headers);
        }
        return (T) thisT();
    }

    @Override // io.netty.handler.codec.Headers
    public T setAll(Headers<? extends K, ? extends V, ?> headers) {
        if (headers != this) {
            Iterator<? extends K> it = headers.names().iterator();
            while (it.hasNext()) {
                remove(it.next());
            }
            addImpl(headers);
        }
        return (T) thisT();
    }

    @Override // io.netty.handler.codec.Headers
    public boolean remove(K name) {
        return getAndRemove(name) != null;
    }

    @Override // io.netty.handler.codec.Headers
    public T clear() {
        Arrays.fill(this.entries, (Object) null);
        HeaderEntry<K, V> headerEntry = this.head;
        HeaderEntry<K, V> headerEntry2 = this.head;
        HeaderEntry<K, V> headerEntry3 = this.head;
        headerEntry2.after = headerEntry3;
        headerEntry.before = headerEntry3;
        this.size = 0;
        return (T) thisT();
    }

    @Override // io.netty.handler.codec.Headers, java.lang.Iterable
    public Iterator<Map.Entry<K, V>> iterator() {
        return new HeaderIterator();
    }

    @Override // io.netty.handler.codec.Headers
    public Boolean getBoolean(K name) {
        V v = get(name);
        if (v == null) {
            return null;
        }
        try {
            return Boolean.valueOf(this.valueConverter.convertToBoolean(v));
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override // io.netty.handler.codec.Headers
    public boolean getBoolean(K name, boolean defaultValue) {
        Boolean v = getBoolean(name);
        return v != null ? v.booleanValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Byte getByte(K name) {
        V v = get(name);
        if (v == null) {
            return null;
        }
        try {
            return Byte.valueOf(this.valueConverter.convertToByte(v));
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override // io.netty.handler.codec.Headers
    public byte getByte(K name, byte defaultValue) {
        Byte v = getByte(name);
        return v != null ? v.byteValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Character getChar(K name) {
        V v = get(name);
        if (v == null) {
            return null;
        }
        try {
            return Character.valueOf(this.valueConverter.convertToChar(v));
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override // io.netty.handler.codec.Headers
    public char getChar(K name, char defaultValue) {
        Character v = getChar(name);
        return v != null ? v.charValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Short getShort(K name) {
        V v = get(name);
        if (v == null) {
            return null;
        }
        try {
            return Short.valueOf(this.valueConverter.convertToShort(v));
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override // io.netty.handler.codec.Headers
    public short getShort(K name, short defaultValue) {
        Short v = getShort(name);
        return v != null ? v.shortValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Integer getInt(K name) {
        V v = get(name);
        if (v == null) {
            return null;
        }
        try {
            return Integer.valueOf(this.valueConverter.convertToInt(v));
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override // io.netty.handler.codec.Headers
    public int getInt(K name, int defaultValue) {
        Integer v = getInt(name);
        return v != null ? v.intValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Long getLong(K name) {
        V v = get(name);
        if (v == null) {
            return null;
        }
        try {
            return Long.valueOf(this.valueConverter.convertToLong(v));
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override // io.netty.handler.codec.Headers
    public long getLong(K name, long defaultValue) {
        Long v = getLong(name);
        return v != null ? v.longValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Float getFloat(K name) {
        V v = get(name);
        if (v == null) {
            return null;
        }
        try {
            return Float.valueOf(this.valueConverter.convertToFloat(v));
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override // io.netty.handler.codec.Headers
    public float getFloat(K name, float defaultValue) {
        Float v = getFloat(name);
        return v != null ? v.floatValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Double getDouble(K name) {
        V v = get(name);
        if (v == null) {
            return null;
        }
        try {
            return Double.valueOf(this.valueConverter.convertToDouble(v));
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override // io.netty.handler.codec.Headers
    public double getDouble(K name, double defaultValue) {
        Double v = getDouble(name);
        return v != null ? v.doubleValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Long getTimeMillis(K name) {
        V v = get(name);
        if (v == null) {
            return null;
        }
        try {
            return Long.valueOf(this.valueConverter.convertToTimeMillis(v));
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override // io.netty.handler.codec.Headers
    public long getTimeMillis(K name, long defaultValue) {
        Long v = getTimeMillis(name);
        return v != null ? v.longValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Boolean getBooleanAndRemove(K name) {
        V v = getAndRemove(name);
        if (v == null) {
            return null;
        }
        try {
            return Boolean.valueOf(this.valueConverter.convertToBoolean(v));
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override // io.netty.handler.codec.Headers
    public boolean getBooleanAndRemove(K name, boolean defaultValue) {
        Boolean v = getBooleanAndRemove(name);
        return v != null ? v.booleanValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Byte getByteAndRemove(K name) {
        V v = getAndRemove(name);
        if (v == null) {
            return null;
        }
        try {
            return Byte.valueOf(this.valueConverter.convertToByte(v));
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override // io.netty.handler.codec.Headers
    public byte getByteAndRemove(K name, byte defaultValue) {
        Byte v = getByteAndRemove(name);
        return v != null ? v.byteValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Character getCharAndRemove(K name) {
        V v = getAndRemove(name);
        if (v == null) {
            return null;
        }
        try {
            return Character.valueOf(this.valueConverter.convertToChar(v));
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override // io.netty.handler.codec.Headers
    public char getCharAndRemove(K name, char defaultValue) {
        Character v = getCharAndRemove(name);
        return v != null ? v.charValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Short getShortAndRemove(K name) {
        V v = getAndRemove(name);
        if (v == null) {
            return null;
        }
        try {
            return Short.valueOf(this.valueConverter.convertToShort(v));
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override // io.netty.handler.codec.Headers
    public short getShortAndRemove(K name, short defaultValue) {
        Short v = getShortAndRemove(name);
        return v != null ? v.shortValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Integer getIntAndRemove(K name) {
        V v = getAndRemove(name);
        if (v == null) {
            return null;
        }
        try {
            return Integer.valueOf(this.valueConverter.convertToInt(v));
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override // io.netty.handler.codec.Headers
    public int getIntAndRemove(K name, int defaultValue) {
        Integer v = getIntAndRemove(name);
        return v != null ? v.intValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Long getLongAndRemove(K name) {
        V v = getAndRemove(name);
        if (v == null) {
            return null;
        }
        try {
            return Long.valueOf(this.valueConverter.convertToLong(v));
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override // io.netty.handler.codec.Headers
    public long getLongAndRemove(K name, long defaultValue) {
        Long v = getLongAndRemove(name);
        return v != null ? v.longValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Float getFloatAndRemove(K name) {
        V v = getAndRemove(name);
        if (v == null) {
            return null;
        }
        try {
            return Float.valueOf(this.valueConverter.convertToFloat(v));
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override // io.netty.handler.codec.Headers
    public float getFloatAndRemove(K name, float defaultValue) {
        Float v = getFloatAndRemove(name);
        return v != null ? v.floatValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Double getDoubleAndRemove(K name) {
        V v = getAndRemove(name);
        if (v == null) {
            return null;
        }
        try {
            return Double.valueOf(this.valueConverter.convertToDouble(v));
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override // io.netty.handler.codec.Headers
    public double getDoubleAndRemove(K name, double defaultValue) {
        Double v = getDoubleAndRemove(name);
        return v != null ? v.doubleValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Long getTimeMillisAndRemove(K name) {
        V v = getAndRemove(name);
        if (v == null) {
            return null;
        }
        try {
            return Long.valueOf(this.valueConverter.convertToTimeMillis(v));
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override // io.netty.handler.codec.Headers
    public long getTimeMillisAndRemove(K name, long defaultValue) {
        Long v = getTimeMillisAndRemove(name);
        return v != null ? v.longValue() : defaultValue;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Headers)) {
            return false;
        }
        return equals((Headers) o, HashingStrategy.JAVA_HASHER);
    }

    public int hashCode() {
        return hashCode(HashingStrategy.JAVA_HASHER);
    }

    public final boolean equals(Headers<K, V, ?> h2, HashingStrategy<V> valueHashingStrategy) {
        if (h2.size() != size()) {
            return false;
        }
        if (this == h2) {
            return true;
        }
        for (K name : names()) {
            List<V> otherValues = h2.getAll(name);
            List<V> values = getAll(name);
            if (otherValues.size() != values.size()) {
                return false;
            }
            for (int i = 0; i < otherValues.size(); i++) {
                if (!valueHashingStrategy.equals(otherValues.get(i), values.get(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public final int hashCode(HashingStrategy<V> valueHashingStrategy) {
        int result = HASH_CODE_SEED;
        for (K name : names()) {
            result = (31 * result) + this.hashingStrategy.hashCode(name);
            List<V> values = getAll(name);
            for (int i = 0; i < values.size(); i++) {
                result = (31 * result) + valueHashingStrategy.hashCode(values.get(i));
            }
        }
        return result;
    }

    public String toString() {
        return HeadersUtils.toString(getClass(), iterator(), size());
    }

    protected HeaderEntry<K, V> newHeaderEntry(int h, K name, V value, HeaderEntry<K, V> next) {
        return new HeaderEntry<>(h, name, value, next, this.head);
    }

    protected ValueConverter<V> valueConverter() {
        return this.valueConverter;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int index(int hash) {
        return hash & this.hashMask;
    }

    private void add0(int h, int i, K name, V value) {
        this.entries[i] = newHeaderEntry(h, name, value, this.entries[i]);
        this.size++;
    }

    private V remove0(int h, int i, K name) {
        HeaderEntry<K, V> e = this.entries[i];
        if (e == null) {
            return null;
        }
        V value = null;
        HeaderEntry<K, V> headerEntry = e.next;
        while (true) {
            HeaderEntry<K, V> next = headerEntry;
            if (next == null) {
                break;
            }
            if (next.hash == h && this.hashingStrategy.equals(name, next.key)) {
                value = next.value;
                e.next = next.next;
                next.remove();
                this.size--;
            } else {
                e = next;
            }
            headerEntry = e.next;
        }
        HeaderEntry<K, V> e2 = this.entries[i];
        if (e2.hash == h && this.hashingStrategy.equals(name, e2.key)) {
            if (value == null) {
                value = e2.value;
            }
            this.entries[i] = e2.next;
            e2.remove();
            this.size--;
        }
        return value;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public HeaderEntry<K, V> remove0(HeaderEntry<K, V> entry, HeaderEntry<K, V> previous) {
        int i = index(entry.hash);
        HeaderEntry<K, V> e = this.entries[i];
        if (e == entry) {
            this.entries[i] = entry.next;
            previous = this.entries[i];
        } else {
            previous.next = entry.next;
        }
        entry.remove();
        this.size--;
        return previous;
    }

    private T thisT() {
        return this;
    }

    public DefaultHeaders<K, V, T> copy() {
        DefaultHeaders<K, V, T> copy = new DefaultHeaders<>(this.hashingStrategy, this.valueConverter, this.nameValidator, this.entries.length);
        copy.addImpl(this);
        return copy;
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/DefaultHeaders$HeaderIterator.class */
    private final class HeaderIterator implements Iterator<Map.Entry<K, V>> {
        private HeaderEntry<K, V> current;

        private HeaderIterator() {
            this.current = DefaultHeaders.this.head;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.current.after != DefaultHeaders.this.head;
        }

        @Override // java.util.Iterator
        public Map.Entry<K, V> next() {
            this.current = this.current.after;
            if (this.current == DefaultHeaders.this.head) {
                throw new NoSuchElementException();
            }
            return this.current;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("read only");
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/DefaultHeaders$ValueIterator.class */
    private final class ValueIterator implements Iterator<V> {
        private final K name;
        private final int hash;
        private HeaderEntry<K, V> removalPrevious;
        private HeaderEntry<K, V> previous;
        private HeaderEntry<K, V> next;

        ValueIterator(K k) {
            this.name = (K) ObjectUtil.checkNotNull(k, "name");
            this.hash = DefaultHeaders.this.hashingStrategy.hashCode(k);
            calculateNext(DefaultHeaders.this.entries[DefaultHeaders.this.index(this.hash)]);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.next != null;
        }

        @Override // java.util.Iterator
        public V next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (this.previous != null) {
                this.removalPrevious = this.previous;
            }
            this.previous = this.next;
            calculateNext(this.next.next);
            return this.previous.value;
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.previous == null) {
                throw new IllegalStateException();
            }
            this.removalPrevious = DefaultHeaders.this.remove0(this.previous, this.removalPrevious);
            this.previous = null;
        }

        private void calculateNext(HeaderEntry<K, V> entry) {
            while (entry != null) {
                if (entry.hash == this.hash && DefaultHeaders.this.hashingStrategy.equals(this.name, entry.key)) {
                    this.next = entry;
                    return;
                }
                entry = entry.next;
            }
            this.next = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/DefaultHeaders$HeaderEntry.class */
    public static class HeaderEntry<K, V> implements Map.Entry<K, V> {
        protected final int hash;
        protected final K key;
        protected V value;
        protected HeaderEntry<K, V> next;
        protected HeaderEntry<K, V> before;
        protected HeaderEntry<K, V> after;

        protected HeaderEntry(int hash, K key) {
            this.hash = hash;
            this.key = key;
        }

        HeaderEntry(int hash, K key, V value, HeaderEntry<K, V> next, HeaderEntry<K, V> head) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
            this.after = head;
            this.before = head.before;
            pointNeighborsToThis();
        }

        HeaderEntry() {
            this.hash = -1;
            this.key = null;
            this.after = this;
            this.before = this;
        }

        protected final void pointNeighborsToThis() {
            this.before.after = this;
            this.after.before = this;
        }

        public final HeaderEntry<K, V> before() {
            return this.before;
        }

        public final HeaderEntry<K, V> after() {
            return this.after;
        }

        protected void remove() {
            this.before.after = this.after;
            this.after.before = this.before;
        }

        @Override // java.util.Map.Entry
        public final K getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public final V getValue() {
            return this.value;
        }

        @Override // java.util.Map.Entry
        public final V setValue(V value) {
            ObjectUtil.checkNotNull(value, "value");
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        public final String toString() {
            return this.key.toString() + '=' + this.value.toString();
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> other = (Map.Entry) o;
            if (getKey() != null ? getKey().equals(other.getKey()) : other.getKey() == null) {
                if (getValue() != null ? getValue().equals(other.getValue()) : other.getValue() == null) {
                    return true;
                }
            }
            return false;
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
        }
    }
}
