package org.springframework.data.repository.support;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/support/RepositoryInvocationInformation.class */
public interface RepositoryInvocationInformation {
    boolean hasSaveMethod();

    boolean hasDeleteMethod();

    boolean hasFindOneMethod();

    boolean hasFindAllMethod();
}
