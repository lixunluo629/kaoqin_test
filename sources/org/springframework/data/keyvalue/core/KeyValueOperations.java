package org.springframework.data.keyvalue.core;

import java.io.Serializable;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.data.domain.Sort;
import org.springframework.data.keyvalue.core.query.KeyValueQuery;
import org.springframework.data.mapping.context.MappingContext;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/KeyValueOperations.class */
public interface KeyValueOperations extends DisposableBean {
    <T> T insert(T t);

    void insert(Serializable serializable, Object obj);

    <T> Iterable<T> findAll(Class<T> cls);

    <T> Iterable<T> findAll(Sort sort, Class<T> cls);

    <T> T findById(Serializable serializable, Class<T> cls);

    <T> T execute(KeyValueCallback<T> keyValueCallback);

    <T> Iterable<T> find(KeyValueQuery<?> keyValueQuery, Class<T> cls);

    <T> Iterable<T> findInRange(int i, int i2, Class<T> cls);

    <T> Iterable<T> findInRange(int i, int i2, Sort sort, Class<T> cls);

    void update(Object obj);

    void update(Serializable serializable, Object obj);

    void delete(Class<?> cls);

    <T> T delete(T t);

    <T> T delete(Serializable serializable, Class<T> cls);

    long count(Class<?> cls);

    long count(KeyValueQuery<?> keyValueQuery, Class<?> cls);

    MappingContext<?, ?> getMappingContext();
}
