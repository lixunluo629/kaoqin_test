package org.springframework.data.repository.core.support;

import java.io.Serializable;
import java.util.List;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.query.QueryMethod;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/RepositoryFactoryInformation.class */
public interface RepositoryFactoryInformation<T, ID extends Serializable> {
    EntityInformation<T, ID> getEntityInformation();

    RepositoryInformation getRepositoryInformation();

    PersistentEntity<?, ?> getPersistentEntity();

    List<QueryMethod> getQueryMethods();
}
