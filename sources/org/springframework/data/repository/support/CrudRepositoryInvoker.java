package org.springframework.data.repository.support;

import java.io.Serializable;
import java.lang.reflect.Method;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.core.CrudMethods;
import org.springframework.data.repository.core.RepositoryMetadata;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/support/CrudRepositoryInvoker.class */
class CrudRepositoryInvoker extends ReflectionRepositoryInvoker {
    private final CrudRepository<Object, Serializable> repository;
    private final boolean customSaveMethod;
    private final boolean customFindOneMethod;
    private final boolean customFindAllMethod;
    private final boolean customDeleteMethod;

    public CrudRepositoryInvoker(CrudRepository<Object, Serializable> repository, RepositoryMetadata metadata, ConversionService conversionService) {
        super(repository, metadata, conversionService);
        CrudMethods crudMethods = metadata.getCrudMethods();
        this.customSaveMethod = isRedeclaredMethod(crudMethods.getSaveMethod());
        this.customFindOneMethod = isRedeclaredMethod(crudMethods.getFindOneMethod());
        this.customDeleteMethod = isRedeclaredMethod(crudMethods.getDeleteMethod());
        this.customFindAllMethod = isRedeclaredMethod(crudMethods.getFindAllMethod());
        this.repository = repository;
    }

    @Override // org.springframework.data.repository.support.ReflectionRepositoryInvoker, org.springframework.data.repository.support.RepositoryInvoker
    public Iterable<Object> invokeFindAll(Sort sort) {
        return this.customFindAllMethod ? super.invokeFindAll(sort) : this.repository.findAll();
    }

    @Override // org.springframework.data.repository.support.ReflectionRepositoryInvoker, org.springframework.data.repository.support.RepositoryInvoker
    public Iterable<Object> invokeFindAll(Pageable pageable) {
        return this.customFindAllMethod ? super.invokeFindAll(pageable) : this.repository.findAll();
    }

    @Override // org.springframework.data.repository.support.ReflectionRepositoryInvoker, org.springframework.data.repository.support.RepositoryInvoker
    public <T> T invokeFindOne(Serializable serializable) {
        return this.customFindOneMethod ? (T) super.invokeFindOne(serializable) : (T) this.repository.findOne(convertId(serializable));
    }

    @Override // org.springframework.data.repository.support.ReflectionRepositoryInvoker, org.springframework.data.repository.support.RepositoryInvoker
    public <T> T invokeSave(T t) {
        return this.customSaveMethod ? (T) super.invokeSave(t) : (T) this.repository.save((CrudRepository<Object, Serializable>) t);
    }

    @Override // org.springframework.data.repository.support.ReflectionRepositoryInvoker, org.springframework.data.repository.support.RepositoryInvoker
    public void invokeDelete(Serializable id) {
        if (this.customDeleteMethod) {
            super.invokeDelete(id);
        } else {
            this.repository.delete((CrudRepository<Object, Serializable>) convertId(id));
        }
    }

    private boolean isRedeclaredMethod(Method method) {
        return !method.getDeclaringClass().equals(CrudRepository.class);
    }
}
