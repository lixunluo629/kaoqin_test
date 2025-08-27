package org.springframework.data.querydsl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/querydsl/QSort.class */
public class QSort extends Sort implements Serializable {
    private static final long serialVersionUID = -6701117396842171930L;
    private final List<OrderSpecifier<?>> orderSpecifiers;

    public QSort(OrderSpecifier<?>... orderSpecifiers) {
        this((List<OrderSpecifier<?>>) Arrays.asList(orderSpecifiers));
    }

    public QSort(List<OrderSpecifier<?>> orderSpecifiers) {
        super(toOrders(orderSpecifiers));
        Assert.notEmpty(orderSpecifiers, "Order specifiers must not be null or empty!");
        this.orderSpecifiers = orderSpecifiers;
    }

    private static List<Sort.Order> toOrders(List<OrderSpecifier<?>> orderSpecifiers) {
        Assert.notEmpty(orderSpecifiers, "Order specifiers must not be null or empty!");
        List<Sort.Order> orders = new ArrayList<>();
        for (OrderSpecifier<?> orderSpecifier : orderSpecifiers) {
            orders.add(toOrder(orderSpecifier));
        }
        return orders;
    }

    private static Sort.Order toOrder(OrderSpecifier<?> orderSpecifier) {
        Assert.notNull(orderSpecifier, "Order specifier must not be null!");
        Object target = orderSpecifier.getTarget();
        Object targetElement = target instanceof Path ? preparePropertyPath((Path) target) : target;
        Assert.notNull(targetElement, "Target element must not be null!");
        return new Sort.Order(targetElement.toString()).with(orderSpecifier.isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC);
    }

    public List<OrderSpecifier<?>> getOrderSpecifiers() {
        return this.orderSpecifiers;
    }

    public QSort and(QSort sort) {
        return sort == null ? this : and(sort.getOrderSpecifiers());
    }

    public QSort and(List<OrderSpecifier<?>> orderSpecifiers) {
        Assert.notEmpty(orderSpecifiers, "OrderSpecifiers must not be null or empty!");
        List<OrderSpecifier<?>> newOrderSpecifiers = new ArrayList<>(this.orderSpecifiers);
        newOrderSpecifiers.addAll(orderSpecifiers);
        return new QSort(newOrderSpecifiers);
    }

    public QSort and(OrderSpecifier<?>... orderSpecifiers) {
        Assert.notEmpty(orderSpecifiers, "OrderSpecifiers must not be null or empty!");
        return and(Arrays.asList(orderSpecifiers));
    }

    private static String preparePropertyPath(Path<?> path) {
        Path<?> root = path.getRoot();
        return (root == null || path.equals(root)) ? path.toString() : path.toString().substring(root.toString().length() + 1);
    }
}
