package org.springframework.data.repository.core.support;

import org.springframework.data.repository.query.RepositoryQuery;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/QueryCreationListener.class */
public interface QueryCreationListener<T extends RepositoryQuery> {
    void onCreation(T t);
}
