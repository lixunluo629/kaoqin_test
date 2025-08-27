package org.springframework.data.redis.connection;

import java.util.Arrays;
import org.springframework.data.redis.connection.RedisZSetCommands;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/DefaultTuple.class */
public class DefaultTuple implements RedisZSetCommands.Tuple {
    private final Double score;
    private final byte[] value;

    public DefaultTuple(byte[] value, Double score) {
        this.score = score;
        this.value = value;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands.Tuple
    public Double getScore() {
        return this.score;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands.Tuple
    public byte[] getValue() {
        return this.value;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof DefaultTuple)) {
            return false;
        }
        DefaultTuple other = (DefaultTuple) obj;
        if (this.score == null) {
            if (other.score != null) {
                return false;
            }
        } else if (!this.score.equals(other.score)) {
            return false;
        }
        if (!Arrays.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.score == null ? 0 : this.score.hashCode());
        return (31 * result) + Arrays.hashCode(this.value);
    }

    @Override // java.lang.Comparable
    public int compareTo(Double o) {
        Double d = this.score == null ? Double.valueOf(0.0d) : this.score;
        Double a = o == null ? Double.valueOf(0.0d) : o;
        return d.compareTo(a);
    }
}
