package org.springframework.data.repository.core.support;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.CrudMethods;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.util.QueryExecutionConverters;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/AbstractRepositoryMetadata.class */
public abstract class AbstractRepositoryMetadata implements RepositoryMetadata {
    private final TypeInformation<?> typeInformation;
    private final Class<?> repositoryInterface;
    private CrudMethods crudMethods;

    public AbstractRepositoryMetadata(Class<?> repositoryInterface) {
        Assert.notNull(repositoryInterface, "Given type must not be null!");
        Assert.isTrue(repositoryInterface.isInterface(), "Given type must be an interface!");
        this.repositoryInterface = repositoryInterface;
        this.typeInformation = ClassTypeInformation.from(repositoryInterface);
    }

    public static RepositoryMetadata getMetadata(Class<?> repositoryInterface) {
        Assert.notNull(repositoryInterface, "Repository interface must not be null!");
        return Repository.class.isAssignableFrom(repositoryInterface) ? new DefaultRepositoryMetadata(repositoryInterface) : new AnnotationRepositoryMetadata(repositoryInterface);
    }

    @Override // org.springframework.data.repository.core.RepositoryMetadata
    public Class<?> getReturnedDomainClass(Method method) {
        return QueryExecutionConverters.unwrapWrapperTypes(this.typeInformation.getReturnType(method)).getType();
    }

    @Override // org.springframework.data.repository.core.RepositoryMetadata
    public Class<?> getRepositoryInterface() {
        return this.repositoryInterface;
    }

    @Override // org.springframework.data.repository.core.RepositoryMetadata
    public CrudMethods getCrudMethods() {
        if (this.crudMethods == null) {
            this.crudMethods = new DefaultCrudMethods(this);
        }
        return this.crudMethods;
    }

    @Override // org.springframework.data.repository.core.RepositoryMetadata
    public boolean isPagingRepository() {
        Method findAllMethod = getCrudMethods().getFindAllMethod();
        if (findAllMethod == null) {
            return false;
        }
        return Arrays.asList(findAllMethod.getParameterTypes()).contains(Pageable.class);
    }

    @Override // org.springframework.data.repository.core.RepositoryMetadata
    public Set<Class<?>> getAlternativeDomainTypes() {
        return Collections.emptySet();
    }

    private static Class<?> unwrapWrapperTypes(TypeInformation<?> type) {
        Class<?> rawType = type.getType();
        boolean needToUnwrap = Iterable.class.isAssignableFrom(rawType) || rawType.isArray() || QueryExecutionConverters.supports(rawType) || ReflectionUtils.isJava8StreamType(rawType);
        return needToUnwrap ? unwrapWrapperTypes(type.getComponentType()) : rawType;
    }
}
