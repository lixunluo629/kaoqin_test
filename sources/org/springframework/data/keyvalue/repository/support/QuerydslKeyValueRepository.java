package org.springframework.data.keyvalue.repository.support;

import com.querydsl.collections.AbstractCollQuery;
import com.querydsl.collections.CollQuery;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import java.io.Serializable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/repository/support/QuerydslKeyValueRepository.class */
public class QuerydslKeyValueRepository<T, ID extends Serializable> extends SimpleKeyValueRepository<T, ID> implements QueryDslPredicateExecutor<T> {
    private static final EntityPathResolver DEFAULT_ENTITY_PATH_RESOLVER = SimpleEntityPathResolver.INSTANCE;
    private final EntityPath<T> path;
    private final PathBuilder<T> builder;

    public QuerydslKeyValueRepository(EntityInformation<T, ID> entityInformation, KeyValueOperations operations) {
        this(entityInformation, operations, DEFAULT_ENTITY_PATH_RESOLVER);
    }

    public QuerydslKeyValueRepository(EntityInformation<T, ID> entityInformation, KeyValueOperations operations, EntityPathResolver resolver) {
        super(entityInformation, operations);
        Assert.notNull(resolver, "EntityPathResolver must not be null!");
        this.path = resolver.createPath(entityInformation.getJavaType());
        this.builder = new PathBuilder<>(this.path.getType(), this.path.getMetadata());
    }

    @Override // org.springframework.data.querydsl.QueryDslPredicateExecutor
    public T findOne(Predicate predicate) {
        return (T) prepareQuery(predicate).fetchOne();
    }

    @Override // org.springframework.data.querydsl.QueryDslPredicateExecutor
    public Iterable<T> findAll(Predicate predicate) {
        return prepareQuery(predicate).fetchResults().getResults();
    }

    @Override // org.springframework.data.querydsl.QueryDslPredicateExecutor
    public Iterable<T> findAll(Predicate predicate, OrderSpecifier<?>... orders) {
        AbstractCollQuery<T, ?> query = prepareQuery(predicate);
        query.orderBy(orders);
        return query.fetchResults().getResults();
    }

    @Override // org.springframework.data.querydsl.QueryDslPredicateExecutor
    public Iterable<T> findAll(Predicate predicate, Sort sort) {
        return findAll(predicate, KeyValueQuerydslUtils.toOrderSpecifier(sort, (PathBuilder<?>) this.builder));
    }

    @Override // org.springframework.data.querydsl.QueryDslPredicateExecutor
    public Page<T> findAll(Predicate predicate, Pageable pageable) {
        AbstractCollQuery<T, ?> query = prepareQuery(predicate);
        if (pageable != null) {
            query.offset(pageable.getOffset());
            query.limit(pageable.getPageSize());
            if (pageable.getSort() != null) {
                query.orderBy(KeyValueQuerydslUtils.toOrderSpecifier(pageable.getSort(), (PathBuilder<?>) this.builder));
            }
        }
        return new PageImpl(query.fetchResults().getResults(), pageable, count(predicate));
    }

    @Override // org.springframework.data.querydsl.QueryDslPredicateExecutor
    public Iterable<T> findAll(OrderSpecifier<?>... orders) {
        if (ObjectUtils.isEmpty((Object[]) orders)) {
            return findAll();
        }
        AbstractCollQuery<T, ?> query = prepareQuery(null);
        query.orderBy(orders);
        return query.fetchResults().getResults();
    }

    @Override // org.springframework.data.querydsl.QueryDslPredicateExecutor
    public long count(Predicate predicate) {
        return prepareQuery(predicate).fetchCount();
    }

    @Override // org.springframework.data.querydsl.QueryDslPredicateExecutor
    public boolean exists(Predicate predicate) {
        return count(predicate) > 0;
    }

    protected AbstractCollQuery<T, ?> prepareQuery(Predicate predicate) {
        CollQuery<T> query = new CollQuery<>();
        query.from(this.builder, findAll());
        return predicate != null ? query.where(predicate) : query;
    }
}
