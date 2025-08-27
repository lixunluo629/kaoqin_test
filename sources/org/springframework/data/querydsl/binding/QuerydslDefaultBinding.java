package org.springframework.data.querydsl.binding;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.CollectionPathBase;
import com.querydsl.core.types.dsl.SimpleExpression;
import java.util.Collection;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/querydsl/binding/QuerydslDefaultBinding.class */
class QuerydslDefaultBinding implements MultiValueBinding<Path<? extends Object>, Object> {
    QuerydslDefaultBinding() {
    }

    @Override // org.springframework.data.querydsl.binding.MultiValueBinding
    public Predicate bind(Path<?> path, Collection<? extends Object> value) {
        Assert.notNull(path, "Path must not be null!");
        Assert.notNull(value, "Value must not be null!");
        if (value.isEmpty()) {
            return null;
        }
        if (path instanceof CollectionPathBase) {
            BooleanBuilder builder = new BooleanBuilder();
            for (Object element : value) {
                builder.and(((CollectionPathBase) path).contains(element));
            }
            return builder.getValue();
        }
        if (path instanceof SimpleExpression) {
            if (value.size() > 1) {
                return ((SimpleExpression) path).in(value);
            }
            return ((SimpleExpression) path).eq(value.iterator().next());
        }
        throw new IllegalArgumentException(String.format("Cannot create predicate for path '%s' with type '%s'.", path, path.getMetadata().getPathType()));
    }
}
