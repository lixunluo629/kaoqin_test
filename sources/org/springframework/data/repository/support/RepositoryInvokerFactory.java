package org.springframework.data.repository.support;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/support/RepositoryInvokerFactory.class */
public interface RepositoryInvokerFactory {
    RepositoryInvoker getInvokerFor(Class<?> cls);
}
