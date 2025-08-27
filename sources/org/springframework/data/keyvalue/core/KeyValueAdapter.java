package org.springframework.data.keyvalue.core;

import java.io.Serializable;
import java.util.Map;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.data.keyvalue.core.query.KeyValueQuery;
import org.springframework.data.util.CloseableIterator;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/KeyValueAdapter.class */
public interface KeyValueAdapter extends DisposableBean {
    Object put(Serializable serializable, Object obj, Serializable serializable2);

    boolean contains(Serializable serializable, Serializable serializable2);

    Object get(Serializable serializable, Serializable serializable2);

    <T> T get(Serializable serializable, Serializable serializable2, Class<T> cls);

    Object delete(Serializable serializable, Serializable serializable2);

    <T> T delete(Serializable serializable, Serializable serializable2, Class<T> cls);

    Iterable<?> getAllOf(Serializable serializable);

    CloseableIterator<Map.Entry<Serializable, Object>> entries(Serializable serializable);

    void deleteAllOf(Serializable serializable);

    void clear();

    Iterable<?> find(KeyValueQuery<?> keyValueQuery, Serializable serializable);

    <T> Iterable<T> find(KeyValueQuery<?> keyValueQuery, Serializable serializable, Class<T> cls);

    long count(Serializable serializable);

    long count(KeyValueQuery<?> keyValueQuery, Serializable serializable);
}
