package org.springframework.data.repository.core;

import java.lang.reflect.Method;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/CrudMethods.class */
public interface CrudMethods {
    Method getSaveMethod();

    boolean hasSaveMethod();

    Method getFindAllMethod();

    boolean hasFindAllMethod();

    Method getFindOneMethod();

    boolean hasFindOneMethod();

    Method getDeleteMethod();

    boolean hasDelete();
}
