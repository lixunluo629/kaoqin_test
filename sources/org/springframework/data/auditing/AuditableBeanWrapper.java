package org.springframework.data.auditing;

import java.util.Calendar;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/auditing/AuditableBeanWrapper.class */
public interface AuditableBeanWrapper {
    void setCreatedBy(Object obj);

    void setCreatedDate(Calendar calendar);

    void setLastModifiedBy(Object obj);

    Calendar getLastModifiedDate();

    void setLastModifiedDate(Calendar calendar);
}
