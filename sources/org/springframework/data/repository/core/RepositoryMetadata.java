package org.springframework.data.repository.core;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Set;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/RepositoryMetadata.class */
public interface RepositoryMetadata {
    Class<? extends Serializable> getIdType();

    Class<?> getDomainType();

    Class<?> getRepositoryInterface();

    Class<?> getReturnedDomainClass(Method method);

    CrudMethods getCrudMethods();

    boolean isPagingRepository();

    Set<Class<?>> getAlternativeDomainTypes();
}
