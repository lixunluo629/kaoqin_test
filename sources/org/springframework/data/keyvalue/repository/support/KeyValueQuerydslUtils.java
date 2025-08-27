package org.springframework.data.keyvalue.repository.support;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.querydsl.QSort;
import org.springframework.util.Assert;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/repository/support/KeyValueQuerydslUtils.class */
abstract class KeyValueQuerydslUtils {
    private KeyValueQuerydslUtils() {
    }

    public static OrderSpecifier<?>[] toOrderSpecifier(Sort sort, PathBuilder<?> builder) {
        List<OrderSpecifier<?>> specifiers;
        Assert.notNull(builder, "Builder must not be 'null'.");
        if (sort == null) {
            return new OrderSpecifier[0];
        }
        if (sort instanceof QSort) {
            specifiers = ((QSort) sort).getOrderSpecifiers();
        } else {
            specifiers = new ArrayList<>();
            Iterator<Sort.Order> it = sort.iterator();
            while (it.hasNext()) {
                Sort.Order order = it.next();
                specifiers.add(toOrderSpecifier(order, builder));
            }
        }
        return (OrderSpecifier[]) specifiers.toArray(new OrderSpecifier[specifiers.size()]);
    }

    private static OrderSpecifier<?> toOrderSpecifier(Sort.Order order, PathBuilder<?> builder) {
        return new OrderSpecifier<>(order.isAscending() ? Order.ASC : Order.DESC, buildOrderPropertyPathFrom(order, builder), toQueryDslNullHandling(order.getNullHandling()));
    }

    private static Expression<?> buildOrderPropertyPathFrom(Sort.Order order, PathBuilder<?> builder) {
        PathBuilder<?> pathBuilderPath;
        Assert.notNull(order, "Order must not be null!");
        Assert.notNull(builder, "Builder must not be null!");
        PathBuilder<?> pathBuilder = builder;
        for (PropertyPath path = PropertyPath.from(order.getProperty(), (Class<?>) builder.getType()); path != null; path = path.next()) {
            if (!path.hasNext() && order.isIgnoreCase()) {
                pathBuilderPath = Expressions.stringPath((Path) pathBuilder, path.getSegment()).lower();
            } else {
                pathBuilderPath = Expressions.path(path.getType(), (Path) pathBuilder, path.getSegment());
            }
            pathBuilder = pathBuilderPath;
        }
        return pathBuilder;
    }

    private static OrderSpecifier.NullHandling toQueryDslNullHandling(Sort.NullHandling nullHandling) {
        Assert.notNull(nullHandling, "NullHandling must not be null!");
        switch (nullHandling) {
            case NULLS_FIRST:
                return OrderSpecifier.NullHandling.NullsFirst;
            case NULLS_LAST:
                return OrderSpecifier.NullHandling.NullsLast;
            case NATIVE:
            default:
                return OrderSpecifier.NullHandling.Default;
        }
    }
}
