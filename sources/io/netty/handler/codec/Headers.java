package io.netty.handler.codec;

import io.netty.handler.codec.Headers;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/Headers.class */
public interface Headers<K, V, T extends Headers<K, V, T>> extends Iterable<Map.Entry<K, V>> {
    V get(K k);

    V get(K k, V v);

    V getAndRemove(K k);

    V getAndRemove(K k, V v);

    List<V> getAll(K k);

    List<V> getAllAndRemove(K k);

    Boolean getBoolean(K k);

    boolean getBoolean(K k, boolean z);

    Byte getByte(K k);

    byte getByte(K k, byte b);

    Character getChar(K k);

    char getChar(K k, char c);

    Short getShort(K k);

    short getShort(K k, short s);

    Integer getInt(K k);

    int getInt(K k, int i);

    Long getLong(K k);

    long getLong(K k, long j);

    Float getFloat(K k);

    float getFloat(K k, float f);

    Double getDouble(K k);

    double getDouble(K k, double d);

    Long getTimeMillis(K k);

    long getTimeMillis(K k, long j);

    Boolean getBooleanAndRemove(K k);

    boolean getBooleanAndRemove(K k, boolean z);

    Byte getByteAndRemove(K k);

    byte getByteAndRemove(K k, byte b);

    Character getCharAndRemove(K k);

    char getCharAndRemove(K k, char c);

    Short getShortAndRemove(K k);

    short getShortAndRemove(K k, short s);

    Integer getIntAndRemove(K k);

    int getIntAndRemove(K k, int i);

    Long getLongAndRemove(K k);

    long getLongAndRemove(K k, long j);

    Float getFloatAndRemove(K k);

    float getFloatAndRemove(K k, float f);

    Double getDoubleAndRemove(K k);

    double getDoubleAndRemove(K k, double d);

    Long getTimeMillisAndRemove(K k);

    long getTimeMillisAndRemove(K k, long j);

    boolean contains(K k);

    boolean contains(K k, V v);

    boolean containsObject(K k, Object obj);

    boolean containsBoolean(K k, boolean z);

    boolean containsByte(K k, byte b);

    boolean containsChar(K k, char c);

    boolean containsShort(K k, short s);

    boolean containsInt(K k, int i);

    boolean containsLong(K k, long j);

    boolean containsFloat(K k, float f);

    boolean containsDouble(K k, double d);

    boolean containsTimeMillis(K k, long j);

    int size();

    boolean isEmpty();

    Set<K> names();

    T add(K k, V v);

    T add(K k, Iterable<? extends V> iterable);

    T add(K k, V... vArr);

    T addObject(K k, Object obj);

    T addObject(K k, Iterable<?> iterable);

    T addObject(K k, Object... objArr);

    T addBoolean(K k, boolean z);

    T addByte(K k, byte b);

    T addChar(K k, char c);

    T addShort(K k, short s);

    T addInt(K k, int i);

    T addLong(K k, long j);

    T addFloat(K k, float f);

    T addDouble(K k, double d);

    T addTimeMillis(K k, long j);

    T add(Headers<? extends K, ? extends V, ?> headers);

    T set(K k, V v);

    T set(K k, Iterable<? extends V> iterable);

    T set(K k, V... vArr);

    T setObject(K k, Object obj);

    T setObject(K k, Iterable<?> iterable);

    T setObject(K k, Object... objArr);

    T setBoolean(K k, boolean z);

    T setByte(K k, byte b);

    T setChar(K k, char c);

    T setShort(K k, short s);

    T setInt(K k, int i);

    T setLong(K k, long j);

    T setFloat(K k, float f);

    T setDouble(K k, double d);

    T setTimeMillis(K k, long j);

    T set(Headers<? extends K, ? extends V, ?> headers);

    T setAll(Headers<? extends K, ? extends V, ?> headers);

    boolean remove(K k);

    T clear();

    @Override // java.lang.Iterable
    Iterator<Map.Entry<K, V>> iterator();
}
