package org.springframework.data.redis.core;

import java.util.Arrays;
import org.springframework.data.redis.core.ZSetOperations;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/DefaultTypedTuple.class */
public class DefaultTypedTuple<V> implements ZSetOperations.TypedTuple<V> {
    private final Double score;
    private final V value;

    public DefaultTypedTuple(V value, Double score) {
        this.score = score;
        this.value = value;
    }

    @Override // org.springframework.data.redis.core.ZSetOperations.TypedTuple
    public Double getScore() {
        return this.score;
    }

    @Override // org.springframework.data.redis.core.ZSetOperations.TypedTuple
    public V getValue() {
        return this.value;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.score == null ? 0 : this.score.hashCode());
        return (31 * result) + (this.value == null ? 0 : this.value.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof DefaultTypedTuple)) {
            return false;
        }
        DefaultTypedTuple defaultTypedTuple = (DefaultTypedTuple) obj;
        if (this.score == null) {
            if (defaultTypedTuple.score != null) {
                return false;
            }
        } else if (!this.score.equals(defaultTypedTuple.score)) {
            return false;
        }
        if (this.value == null) {
            if (defaultTypedTuple.value != null) {
                return false;
            }
            return true;
        }
        if (this.value instanceof byte[]) {
            if (!(defaultTypedTuple.value instanceof byte[])) {
                return false;
            }
            return Arrays.equals((byte[]) this.value, (byte[]) defaultTypedTuple.value);
        }
        if (!this.value.equals(defaultTypedTuple.value)) {
            return false;
        }
        return true;
    }

    public int compareTo(Double o) {
        double thisScore = this.score == null ? 0.0d : this.score.doubleValue();
        double otherScore = o == null ? 0.0d : o.doubleValue();
        return Double.compare(thisScore, otherScore);
    }

    @Override // java.lang.Comparable
    public int compareTo(ZSetOperations.TypedTuple<V> o) {
        if (o == null) {
            return compareTo(Double.valueOf(0.0d));
        }
        return compareTo(o.getScore());
    }
}
