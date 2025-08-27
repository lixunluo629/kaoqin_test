package org.springframework.data.repository.core;

import java.io.Serializable;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/EntityInformation.class */
public interface EntityInformation<T, ID extends Serializable> extends EntityMetadata<T> {
    boolean isNew(T t);

    ID getId(T t);

    Class<ID> getIdType();
}
