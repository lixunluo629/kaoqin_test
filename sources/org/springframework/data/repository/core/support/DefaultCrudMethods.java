package org.springframework.data.repository.core.support;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.core.CrudMethods;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/DefaultCrudMethods.class */
public class DefaultCrudMethods implements CrudMethods {
    private static final String FIND_ONE = "findOne";
    private static final String SAVE = "save";
    private static final String FIND_ALL = "findAll";
    private static final String DELETE = "delete";
    private final Method findAllMethod;
    private final Method findOneMethod;
    private final Method saveMethod;
    private final Method deleteMethod;

    public DefaultCrudMethods(RepositoryMetadata metadata) {
        Assert.notNull(metadata, "RepositoryInformation must not be null!");
        this.findOneMethod = selectMostSuitableFindOneMethod(metadata);
        this.findAllMethod = selectMostSuitableFindAllMethod(metadata);
        this.deleteMethod = selectMostSuitableDeleteMethod(metadata);
        this.saveMethod = selectMostSuitableSaveMethod(metadata);
    }

    private Method selectMostSuitableSaveMethod(RepositoryMetadata metadata) {
        for (Class<?> type : Arrays.asList(metadata.getDomainType(), Object.class)) {
            Method saveMethodCandidate = ReflectionUtils.findMethod(metadata.getRepositoryInterface(), SAVE, type);
            if (saveMethodCandidate != null) {
                return getMostSpecificMethod(saveMethodCandidate, metadata.getRepositoryInterface());
            }
        }
        return null;
    }

    private Method selectMostSuitableDeleteMethod(RepositoryMetadata metadata) {
        for (Class<?> type : Arrays.asList(metadata.getDomainType(), metadata.getIdType(), Serializable.class, Iterable.class)) {
            Method candidate = ReflectionUtils.findMethod(metadata.getRepositoryInterface(), DELETE, type);
            if (candidate != null) {
                return getMostSpecificMethod(candidate, metadata.getRepositoryInterface());
            }
        }
        return null;
    }

    private Method selectMostSuitableFindAllMethod(RepositoryMetadata metadata) {
        Method candidate;
        for (Class<?> type : Arrays.asList(Pageable.class, Sort.class)) {
            if (ClassUtils.hasMethod(metadata.getRepositoryInterface(), FIND_ALL, type) && (candidate = ReflectionUtils.findMethod(PagingAndSortingRepository.class, FIND_ALL, type)) != null) {
                return getMostSpecificMethod(candidate, metadata.getRepositoryInterface());
            }
        }
        if (ClassUtils.hasMethod(metadata.getRepositoryInterface(), FIND_ALL, new Class[0])) {
            return getMostSpecificMethod(ReflectionUtils.findMethod(CrudRepository.class, FIND_ALL), metadata.getRepositoryInterface());
        }
        return null;
    }

    private Method selectMostSuitableFindOneMethod(RepositoryMetadata metadata) {
        for (Class<?> type : Arrays.asList(metadata.getIdType(), Serializable.class)) {
            Method candidate = ReflectionUtils.findMethod(metadata.getRepositoryInterface(), FIND_ONE, type);
            if (candidate != null) {
                return getMostSpecificMethod(candidate, metadata.getRepositoryInterface());
            }
        }
        return null;
    }

    private static Method getMostSpecificMethod(Method method, Class<?> type) {
        Method result = ClassUtils.getMostSpecificMethod(method, type);
        if (result == null) {
            return null;
        }
        ReflectionUtils.makeAccessible(result);
        return result;
    }

    @Override // org.springframework.data.repository.core.CrudMethods
    public Method getSaveMethod() {
        return this.saveMethod;
    }

    @Override // org.springframework.data.repository.core.CrudMethods
    public boolean hasSaveMethod() {
        return this.saveMethod != null;
    }

    @Override // org.springframework.data.repository.core.CrudMethods
    public Method getFindAllMethod() {
        return this.findAllMethod;
    }

    @Override // org.springframework.data.repository.core.CrudMethods
    public boolean hasFindAllMethod() {
        return this.findAllMethod != null;
    }

    @Override // org.springframework.data.repository.core.CrudMethods
    public Method getFindOneMethod() {
        return this.findOneMethod;
    }

    @Override // org.springframework.data.repository.core.CrudMethods
    public boolean hasFindOneMethod() {
        return this.findOneMethod != null;
    }

    @Override // org.springframework.data.repository.core.CrudMethods
    public boolean hasDelete() {
        return this.deleteMethod != null;
    }

    @Override // org.springframework.data.repository.core.CrudMethods
    public Method getDeleteMethod() {
        return this.deleteMethod;
    }
}
