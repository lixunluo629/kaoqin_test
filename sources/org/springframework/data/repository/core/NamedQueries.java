package org.springframework.data.repository.core;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/NamedQueries.class */
public interface NamedQueries {
    boolean hasQuery(String str);

    String getQuery(String str);
}
