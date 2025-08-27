package org.springframework.data.domain;

import java.io.Serializable;
import org.joda.time.DateTime;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/Auditable.class */
public interface Auditable<U, ID extends Serializable> extends Persistable<ID> {
    U getCreatedBy();

    void setCreatedBy(U u);

    DateTime getCreatedDate();

    void setCreatedDate(DateTime dateTime);

    U getLastModifiedBy();

    void setLastModifiedBy(U u);

    DateTime getLastModifiedDate();

    void setLastModifiedDate(DateTime dateTime);
}
