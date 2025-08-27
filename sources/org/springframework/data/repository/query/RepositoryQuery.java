package org.springframework.data.repository.query;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/RepositoryQuery.class */
public interface RepositoryQuery {
    Object execute(Object[] objArr);

    QueryMethod getQueryMethod();
}
