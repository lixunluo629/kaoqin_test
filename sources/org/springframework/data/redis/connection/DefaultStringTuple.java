package org.springframework.data.redis.connection;

import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.connection.StringRedisConnection;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/DefaultStringTuple.class */
public class DefaultStringTuple extends DefaultTuple implements StringRedisConnection.StringTuple {
    private final String valueAsString;

    public DefaultStringTuple(byte[] value, String valueAsString, Double score) {
        super(value, score);
        this.valueAsString = valueAsString;
    }

    public DefaultStringTuple(RedisZSetCommands.Tuple tuple, String valueAsString) {
        super(tuple.getValue(), tuple.getScore());
        this.valueAsString = valueAsString;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection.StringTuple
    public String getValueAsString() {
        return this.valueAsString;
    }

    @Override // org.springframework.data.redis.connection.DefaultTuple
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + (this.valueAsString == null ? 0 : this.valueAsString.hashCode());
    }

    @Override // org.springframework.data.redis.connection.DefaultTuple
    public boolean equals(Object obj) {
        if (!super.equals(obj) || !(obj instanceof DefaultStringTuple)) {
            return false;
        }
        DefaultStringTuple other = (DefaultStringTuple) obj;
        if (this.valueAsString == null) {
            if (other.valueAsString != null) {
                return false;
            }
            return true;
        }
        if (!this.valueAsString.equals(other.valueAsString)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "DefaultStringTuple[value=" + getValueAsString() + ", score=" + getScore() + "]";
    }
}
