package org.springframework.data.repository.query;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/QueryByExampleExecutor.class */
public interface QueryByExampleExecutor<T> {
    <S extends T> S findOne(Example<S> example);

    <S extends T> Iterable<S> findAll(Example<S> example);

    <S extends T> Iterable<S> findAll(Example<S> example, Sort sort);

    <S extends T> Page<S> findAll(Example<S> example, Pageable pageable);

    <S extends T> long count(Example<S> example);

    <S extends T> boolean exists(Example<S> example);
}
