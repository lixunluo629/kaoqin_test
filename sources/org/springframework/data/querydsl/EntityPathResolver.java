package org.springframework.data.querydsl;

import com.querydsl.core.types.EntityPath;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/querydsl/EntityPathResolver.class */
public interface EntityPathResolver {
    <T> EntityPath<T> createPath(Class<T> cls);
}
