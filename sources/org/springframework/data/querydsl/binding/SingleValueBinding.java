package org.springframework.data.querydsl.binding;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;

@FunctionalInterface
/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/querydsl/binding/SingleValueBinding.class */
public interface SingleValueBinding<T extends Path<? extends S>, S> {
    Predicate bind(T t, S s);
}
