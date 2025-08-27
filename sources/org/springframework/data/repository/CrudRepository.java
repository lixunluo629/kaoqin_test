package org.springframework.data.repository;

import java.io.Serializable;

@NoRepositoryBean
/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/CrudRepository.class */
public interface CrudRepository<T, ID extends Serializable> extends Repository<T, ID> {
    <S extends T> S save(S s);

    <S extends T> Iterable<S> save(Iterable<S> iterable);

    T findOne(ID id);

    boolean exists(ID id);

    Iterable<T> findAll();

    Iterable<T> findAll(Iterable<ID> iterable);

    long count();

    void delete(ID id);

    void delete(T t);

    void delete(Iterable<? extends T> iterable);

    void deleteAll();
}
