package org.springframework.data.repository.core;

import java.lang.reflect.Method;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/RepositoryInformation.class */
public interface RepositoryInformation extends RepositoryMetadata {
    Class<?> getRepositoryBaseClass();

    boolean hasCustomMethod();

    boolean isCustomMethod(Method method);

    boolean isQueryMethod(Method method);

    boolean isBaseClassMethod(Method method);

    Iterable<Method> getQueryMethods();

    Method getTargetClassMethod(Method method);
}
