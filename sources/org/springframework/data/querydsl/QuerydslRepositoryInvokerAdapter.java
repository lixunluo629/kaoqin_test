package org.springframework.data.querydsl;

import com.querydsl.core.types.Predicate;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.support.RepositoryInvoker;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/querydsl/QuerydslRepositoryInvokerAdapter.class */
public class QuerydslRepositoryInvokerAdapter implements RepositoryInvoker {
    private final RepositoryInvoker delegate;
    private final QueryDslPredicateExecutor<Object> executor;
    private final Predicate predicate;

    public QuerydslRepositoryInvokerAdapter(RepositoryInvoker delegate, QueryDslPredicateExecutor<Object> executor, Predicate predicate) {
        Assert.notNull(delegate, "Delegate RepositoryInvoker must not be null!");
        Assert.notNull(executor, "QuerydslPredicateExecutor must not be null!");
        this.delegate = delegate;
        this.executor = executor;
        this.predicate = predicate;
    }

    @Override // org.springframework.data.repository.support.RepositoryInvoker
    public Iterable<Object> invokeFindAll(Pageable pageable) {
        return this.executor.findAll(this.predicate, pageable);
    }

    @Override // org.springframework.data.repository.support.RepositoryInvoker
    public Iterable<Object> invokeFindAll(Sort sort) {
        return this.executor.findAll(this.predicate, sort);
    }

    @Override // org.springframework.data.repository.support.RepositoryInvocationInformation
    public boolean hasDeleteMethod() {
        return this.delegate.hasDeleteMethod();
    }

    @Override // org.springframework.data.repository.support.RepositoryInvocationInformation
    public boolean hasFindAllMethod() {
        return this.delegate.hasFindAllMethod();
    }

    @Override // org.springframework.data.repository.support.RepositoryInvocationInformation
    public boolean hasFindOneMethod() {
        return this.delegate.hasFindOneMethod();
    }

    @Override // org.springframework.data.repository.support.RepositoryInvocationInformation
    public boolean hasSaveMethod() {
        return this.delegate.hasSaveMethod();
    }

    @Override // org.springframework.data.repository.support.RepositoryInvoker
    public void invokeDelete(Serializable id) {
        this.delegate.invokeDelete(id);
    }

    @Override // org.springframework.data.repository.support.RepositoryInvoker
    public <T> T invokeFindOne(Serializable serializable) {
        return (T) this.delegate.invokeFindOne(serializable);
    }

    @Override // org.springframework.data.repository.support.RepositoryInvoker
    public Object invokeQueryMethod(Method method, Map<String, String[]> parameters, Pageable pageable, Sort sort) {
        return this.delegate.invokeQueryMethod(method, parameters, pageable, sort);
    }

    @Override // org.springframework.data.repository.support.RepositoryInvoker
    public Object invokeQueryMethod(Method method, MultiValueMap<String, ? extends Object> parameters, Pageable pageable, Sort sort) {
        return this.delegate.invokeQueryMethod(method, parameters, pageable, sort);
    }

    @Override // org.springframework.data.repository.support.RepositoryInvoker
    public <T> T invokeSave(T t) {
        return (T) this.delegate.invokeSave(t);
    }
}
