package org.springframework.data.querydsl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/querydsl/QueryDslPredicateExecutor.class */
public interface QueryDslPredicateExecutor<T> {
    T findOne(Predicate predicate);

    Iterable<T> findAll(Predicate predicate);

    Iterable<T> findAll(Predicate predicate, Sort sort);

    Iterable<T> findAll(Predicate predicate, OrderSpecifier<?>... orderSpecifierArr);

    Iterable<T> findAll(OrderSpecifier<?>... orderSpecifierArr);

    Page<T> findAll(Predicate predicate, Pageable pageable);

    long count(Predicate predicate);

    boolean exists(Predicate predicate);
}
