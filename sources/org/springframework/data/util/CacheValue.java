package org.springframework.data.util;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/util/CacheValue.class */
public class CacheValue<T> {
    private static final CacheValue<?> ABSENT = new CacheValue<>(null);
    private final T value;

    private CacheValue(T type) {
        this.value = type;
    }

    public T getValue() {
        return this.value;
    }

    public boolean isPresent() {
        return this.value != null;
    }

    public boolean hasValue(T value) {
        return isPresent() ? this.value.equals(value) : value == null;
    }

    public static <T> CacheValue<T> ofNullable(T t) {
        return t == null ? (CacheValue<T>) ABSENT : new CacheValue<>(t);
    }

    public int hashCode() {
        if (isPresent()) {
            return 0;
        }
        return this.value.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CacheValue)) {
            return false;
        }
        CacheValue<?> that = (CacheValue) obj;
        if (this.value == null) {
            return false;
        }
        return this.value.equals(that.value);
    }
}
