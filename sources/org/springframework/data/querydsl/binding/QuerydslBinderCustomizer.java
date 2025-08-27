package org.springframework.data.querydsl.binding;

import com.querydsl.core.types.EntityPath;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/querydsl/binding/QuerydslBinderCustomizer.class */
public interface QuerydslBinderCustomizer<T extends EntityPath<?>> {
    void customize(QuerydslBindings querydslBindings, T t);
}
