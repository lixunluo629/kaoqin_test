package org.springframework.data.repository.history.support;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/history/support/RevisionEntityInformation.class */
public interface RevisionEntityInformation {
    Class<?> getRevisionNumberType();

    boolean isDefaultRevisionEntity();

    Class<?> getRevisionEntityClass();
}
