package org.springframework.data.keyvalue.repository.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.keyvalue.core.IterableConverter;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.util.Assert;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/repository/support/SimpleKeyValueRepository.class */
public class SimpleKeyValueRepository<T, ID extends Serializable> implements KeyValueRepository<T, ID> {
    private final KeyValueOperations operations;
    private final EntityInformation<T, ID> entityInformation;

    public SimpleKeyValueRepository(EntityInformation<T, ID> metadata, KeyValueOperations operations) {
        Assert.notNull(metadata, "EntityInformation must not be null!");
        Assert.notNull(operations, "KeyValueOperations must not be null!");
        this.entityInformation = metadata;
        this.operations = operations;
    }

    @Override // org.springframework.data.repository.PagingAndSortingRepository
    public Iterable<T> findAll(Sort sort) {
        return this.operations.findAll(sort, this.entityInformation.getJavaType());
    }

    @Override // org.springframework.data.repository.PagingAndSortingRepository
    public Page<T> findAll(Pageable pageable) {
        if (pageable == null) {
            List<T> result = findAll();
            return new PageImpl(result, null, result.size());
        }
        Iterable<T> content = this.operations.findInRange(pageable.getOffset(), pageable.getPageSize(), pageable.getSort(), this.entityInformation.getJavaType());
        return new PageImpl(IterableConverter.toList(content), pageable, this.operations.count(this.entityInformation.getJavaType()));
    }

    @Override // org.springframework.data.repository.CrudRepository
    public <S extends T> S save(S entity) {
        Assert.notNull(entity, "Entity must not be null!");
        if (this.entityInformation.isNew(entity)) {
            this.operations.insert(entity);
        } else {
            this.operations.update(this.entityInformation.getId(entity), entity);
        }
        return entity;
    }

    @Override // org.springframework.data.repository.CrudRepository
    public <S extends T> Iterable<S> save(Iterable<S> entities) {
        for (S entity : entities) {
            save((SimpleKeyValueRepository<T, ID>) entity);
        }
        return entities;
    }

    @Override // org.springframework.data.repository.CrudRepository
    public T findOne(ID id) {
        return (T) this.operations.findById(id, this.entityInformation.getJavaType());
    }

    @Override // org.springframework.data.repository.CrudRepository
    public boolean exists(ID id) {
        return findOne(id) != null;
    }

    @Override // org.springframework.data.repository.CrudRepository
    public List<T> findAll() {
        return IterableConverter.toList(this.operations.findAll(this.entityInformation.getJavaType()));
    }

    @Override // org.springframework.data.repository.CrudRepository
    public Iterable<T> findAll(Iterable<ID> ids) {
        List<T> result = new ArrayList<>();
        for (ID id : ids) {
            T candidate = findOne(id);
            if (candidate != null) {
                result.add(candidate);
            }
        }
        return result;
    }

    @Override // org.springframework.data.repository.CrudRepository
    public long count() {
        return this.operations.count(this.entityInformation.getJavaType());
    }

    @Override // org.springframework.data.repository.CrudRepository
    public void delete(ID id) {
        this.operations.delete(id, this.entityInformation.getJavaType());
    }

    @Override // org.springframework.data.repository.CrudRepository
    public void delete(T entity) {
        delete((SimpleKeyValueRepository<T, ID>) this.entityInformation.getId(entity));
    }

    @Override // org.springframework.data.repository.CrudRepository
    public void delete(Iterable<? extends T> entities) {
        for (T entity : entities) {
            delete((SimpleKeyValueRepository<T, ID>) entity);
        }
    }

    @Override // org.springframework.data.repository.CrudRepository
    public void deleteAll() {
        this.operations.delete(this.entityInformation.getJavaType());
    }
}
