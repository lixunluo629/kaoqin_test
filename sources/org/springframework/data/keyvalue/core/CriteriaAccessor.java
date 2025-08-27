package org.springframework.data.keyvalue.core;

import org.springframework.data.keyvalue.core.query.KeyValueQuery;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/CriteriaAccessor.class */
public interface CriteriaAccessor<T> {
    T resolve(KeyValueQuery<?> keyValueQuery);
}
