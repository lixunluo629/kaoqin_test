package org.springframework.data.domain;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/Pageable.class */
public interface Pageable {
    int getPageNumber();

    int getPageSize();

    int getOffset();

    Sort getSort();

    Pageable next();

    Pageable previousOrFirst();

    Pageable first();

    boolean hasPrevious();
}
