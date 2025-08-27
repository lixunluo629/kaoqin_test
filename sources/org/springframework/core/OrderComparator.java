package org.springframework.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/OrderComparator.class */
public class OrderComparator implements Comparator<Object> {
    public static final OrderComparator INSTANCE = new OrderComparator();

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/OrderComparator$OrderSourceProvider.class */
    public interface OrderSourceProvider {
        Object getOrderSource(Object obj);
    }

    public Comparator<Object> withSourceProvider(final OrderSourceProvider sourceProvider) {
        return new Comparator<Object>() { // from class: org.springframework.core.OrderComparator.1
            @Override // java.util.Comparator
            public int compare(Object o1, Object o2) {
                return OrderComparator.this.doCompare(o1, o2, sourceProvider);
            }
        };
    }

    @Override // java.util.Comparator
    public int compare(Object o1, Object o2) {
        return doCompare(o1, o2, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int doCompare(Object o1, Object o2, OrderSourceProvider sourceProvider) {
        boolean p1 = o1 instanceof PriorityOrdered;
        boolean p2 = o2 instanceof PriorityOrdered;
        if (p1 && !p2) {
            return -1;
        }
        if (p2 && !p1) {
            return 1;
        }
        int i1 = getOrder(o1, sourceProvider);
        int i2 = getOrder(o2, sourceProvider);
        if (i1 < i2) {
            return -1;
        }
        return i1 > i2 ? 1 : 0;
    }

    private int getOrder(Object obj, OrderSourceProvider sourceProvider) {
        Integer order = null;
        if (sourceProvider != null) {
            Object orderSource = sourceProvider.getOrderSource(obj);
            if (orderSource != null && orderSource.getClass().isArray()) {
                Object[] sources = ObjectUtils.toObjectArray(orderSource);
                for (Object source : sources) {
                    order = findOrder(source);
                    if (order != null) {
                        break;
                    }
                }
            } else {
                order = findOrder(orderSource);
            }
        }
        return order != null ? order.intValue() : getOrder(obj);
    }

    protected int getOrder(Object obj) {
        Integer order = findOrder(obj);
        if (order != null) {
            return order.intValue();
        }
        return Integer.MAX_VALUE;
    }

    protected Integer findOrder(Object obj) {
        if (obj instanceof Ordered) {
            return Integer.valueOf(((Ordered) obj).getOrder());
        }
        return null;
    }

    public Integer getPriority(Object obj) {
        return null;
    }

    public static void sort(List<?> list) {
        if (list.size() > 1) {
            Collections.sort(list, INSTANCE);
        }
    }

    public static void sort(Object[] array) {
        if (array.length > 1) {
            Arrays.sort(array, INSTANCE);
        }
    }

    public static void sortIfNecessary(Object value) {
        if (value instanceof Object[]) {
            sort((Object[]) value);
        } else if (value instanceof List) {
            sort((List<?>) value);
        }
    }
}
