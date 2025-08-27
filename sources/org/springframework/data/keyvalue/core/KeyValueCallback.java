package org.springframework.data.keyvalue.core;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/KeyValueCallback.class */
public interface KeyValueCallback<T> {
    T doInKeyValue(KeyValueAdapter keyValueAdapter);
}
