package org.springframework.data.repository.util;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/util/NullableWrapper.class */
public class NullableWrapper {
    private final Object value;

    public NullableWrapper(Object value) {
        this.value = value;
    }

    public Class<?> getValueType() {
        return this.value == null ? Object.class : this.value.getClass();
    }

    public Object getValue() {
        return this.value;
    }
}
