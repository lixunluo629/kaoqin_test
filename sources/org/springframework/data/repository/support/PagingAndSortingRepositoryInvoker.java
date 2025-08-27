package org.springframework.data.repository.support;

import java.io.Serializable;
import java.lang.reflect.Method;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.core.CrudMethods;
import org.springframework.data.repository.core.RepositoryMetadata;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/support/PagingAndSortingRepositoryInvoker.class */
class PagingAndSortingRepositoryInvoker extends CrudRepositoryInvoker {
    private final PagingAndSortingRepository<Object, Serializable> repository;
    private final boolean customFindAll;

    public PagingAndSortingRepositoryInvoker(PagingAndSortingRepository<Object, Serializable> repository, RepositoryMetadata metadata, ConversionService conversionService) {
        super(repository, metadata, conversionService);
        CrudMethods crudMethods = metadata.getCrudMethods();
        this.repository = repository;
        this.customFindAll = isRedeclaredMethod(crudMethods.getFindAllMethod());
    }

    @Override // org.springframework.data.repository.support.CrudRepositoryInvoker, org.springframework.data.repository.support.ReflectionRepositoryInvoker, org.springframework.data.repository.support.RepositoryInvoker
    public Iterable<Object> invokeFindAll(Sort sort) {
        return this.customFindAll ? invokeFindAllReflectively(sort) : this.repository.findAll(sort);
    }

    @Override // org.springframework.data.repository.support.CrudRepositoryInvoker, org.springframework.data.repository.support.ReflectionRepositoryInvoker, org.springframework.data.repository.support.RepositoryInvoker
    public Iterable<Object> invokeFindAll(Pageable pageable) {
        return this.customFindAll ? invokeFindAllReflectively(pageable) : this.repository.findAll(pageable);
    }

    private boolean isRedeclaredMethod(Method method) {
        return !method.getDeclaringClass().equals(PagingAndSortingRepository.class);
    }
}
