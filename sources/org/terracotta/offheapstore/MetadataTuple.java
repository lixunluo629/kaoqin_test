package org.terracotta.offheapstore;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/MetadataTuple.class */
public final class MetadataTuple<V> {
    private final V value;
    private final int metadata;

    private MetadataTuple(V value, int metadata) {
        this.value = value;
        this.metadata = metadata;
    }

    public V value() {
        return this.value;
    }

    public int metadata() {
        return this.metadata;
    }

    public boolean equals(Object o) {
        if (o instanceof MetadataTuple) {
            MetadataTuple<?> other = (MetadataTuple) o;
            return value().equals(other.value()) && metadata() == other.metadata();
        }
        return false;
    }

    public int hashCode() {
        return this.value.hashCode() ^ this.metadata;
    }

    public static final <T> MetadataTuple<T> metadataTuple(T value, int metadata) {
        return new MetadataTuple<>(value, metadata);
    }
}
