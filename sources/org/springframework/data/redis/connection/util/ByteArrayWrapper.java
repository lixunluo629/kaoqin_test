package org.springframework.data.redis.connection.util;

import java.util.Arrays;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/util/ByteArrayWrapper.class */
public class ByteArrayWrapper {
    private final byte[] array;
    private final int hashCode;

    public ByteArrayWrapper(byte[] array) {
        this.array = array;
        this.hashCode = Arrays.hashCode(array);
    }

    public boolean equals(Object obj) {
        if (obj instanceof ByteArrayWrapper) {
            return Arrays.equals(this.array, ((ByteArrayWrapper) obj).array);
        }
        return false;
    }

    public int hashCode() {
        return this.hashCode;
    }

    public byte[] getArray() {
        return this.array;
    }
}
