package org.springframework.data.querydsl.binding;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import java.util.Collection;

@FunctionalInterface
/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/querydsl/binding/MultiValueBinding.class */
public interface MultiValueBinding<T extends Path<? extends S>, S> {
    Predicate bind(T t, Collection<? extends S> collection);
}
