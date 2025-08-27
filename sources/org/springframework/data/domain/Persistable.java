package org.springframework.data.domain;

import java.io.Serializable;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/Persistable.class */
public interface Persistable<ID extends Serializable> extends Serializable {
    ID getId();

    boolean isNew();
}
