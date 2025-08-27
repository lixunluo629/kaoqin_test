package org.springframework.data.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/Sort.class */
public class Sort implements Iterable<Order>, Serializable {
    private static final long serialVersionUID = 5737186511678863905L;
    public static final Direction DEFAULT_DIRECTION = Direction.ASC;
    private final List<Order> orders;

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/Sort$NullHandling.class */
    public enum NullHandling {
        NATIVE,
        NULLS_FIRST,
        NULLS_LAST
    }

    public Sort(Order... orders) {
        this((List<Order>) Arrays.asList(orders));
    }

    public Sort(List<Order> orders) {
        if (null == orders || orders.isEmpty()) {
            throw new IllegalArgumentException("You have to provide at least one sort property to sort by!");
        }
        this.orders = orders;
    }

    public Sort(String... properties) {
        this(DEFAULT_DIRECTION, properties);
    }

    public Sort(Direction direction, String... properties) {
        this(direction, (List<String>) (properties == null ? new ArrayList() : Arrays.asList(properties)));
    }

    public Sort(Direction direction, List<String> properties) {
        if (properties == null || properties.isEmpty()) {
            throw new IllegalArgumentException("You have to provide at least one property to sort by!");
        }
        this.orders = new ArrayList(properties.size());
        for (String property : properties) {
            this.orders.add(new Order(direction, property));
        }
    }

    public Sort and(Sort sort) {
        if (sort == null) {
            return this;
        }
        ArrayList<Order> these = new ArrayList<>(this.orders);
        Iterator<Order> it = sort.iterator();
        while (it.hasNext()) {
            Order order = it.next();
            these.add(order);
        }
        return new Sort(these);
    }

    public Order getOrderFor(String property) {
        Iterator<Order> it = iterator();
        while (it.hasNext()) {
            Order order = it.next();
            if (order.getProperty().equals(property)) {
                return order;
            }
        }
        return null;
    }

    @Override // java.lang.Iterable
    public Iterator<Order> iterator() {
        return this.orders.iterator();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Sort)) {
            return false;
        }
        Sort that = (Sort) obj;
        return this.orders.equals(that.orders);
    }

    public int hashCode() {
        int result = (31 * 17) + this.orders.hashCode();
        return result;
    }

    public String toString() {
        return StringUtils.collectionToCommaDelimitedString(this.orders);
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/Sort$Direction.class */
    public enum Direction {
        ASC,
        DESC;

        public boolean isAscending() {
            return equals(ASC);
        }

        public boolean isDescending() {
            return equals(DESC);
        }

        public static Direction fromString(String value) {
            try {
                return valueOf(value.toUpperCase(Locale.US));
            } catch (Exception e) {
                throw new IllegalArgumentException(String.format("Invalid value '%s' for orders given! Has to be either 'desc' or 'asc' (case insensitive).", value), e);
            }
        }

        public static Direction fromStringOrNull(String value) {
            try {
                return fromString(value);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/Sort$Order.class */
    public static class Order implements Serializable {
        private static final long serialVersionUID = 1522511010900108987L;
        private static final boolean DEFAULT_IGNORE_CASE = false;
        private final Direction direction;
        private final String property;
        private final boolean ignoreCase;
        private final NullHandling nullHandling;

        public Order(Direction direction, String property) {
            this(direction, property, false, null);
        }

        public Order(Direction direction, String property, NullHandling nullHandlingHint) {
            this(direction, property, false, nullHandlingHint);
        }

        public Order(String property) {
            this(Sort.DEFAULT_DIRECTION, property);
        }

        private Order(Direction direction, String property, boolean ignoreCase, NullHandling nullHandling) {
            if (!StringUtils.hasText(property)) {
                throw new IllegalArgumentException("Property must not null or empty!");
            }
            this.direction = direction == null ? Sort.DEFAULT_DIRECTION : direction;
            this.property = property;
            this.ignoreCase = ignoreCase;
            this.nullHandling = nullHandling == null ? NullHandling.NATIVE : nullHandling;
        }

        public Direction getDirection() {
            return this.direction;
        }

        public String getProperty() {
            return this.property;
        }

        public boolean isAscending() {
            return this.direction.isAscending();
        }

        public boolean isDescending() {
            return this.direction.isDescending();
        }

        public boolean isIgnoreCase() {
            return this.ignoreCase;
        }

        public Order with(Direction direction) {
            return new Order(direction, this.property, this.ignoreCase, this.nullHandling);
        }

        public Order withProperty(String property) {
            return new Order(this.direction, property, this.ignoreCase, this.nullHandling);
        }

        public Sort withProperties(String... properties) {
            return new Sort(this.direction, properties);
        }

        public Order ignoreCase() {
            return new Order(this.direction, this.property, true, this.nullHandling);
        }

        public Order with(NullHandling nullHandling) {
            return new Order(this.direction, this.property, this.ignoreCase, nullHandling);
        }

        public Order nullsFirst() {
            return with(NullHandling.NULLS_FIRST);
        }

        public Order nullsLast() {
            return with(NullHandling.NULLS_LAST);
        }

        public Order nullsNative() {
            return with(NullHandling.NATIVE);
        }

        public NullHandling getNullHandling() {
            return this.nullHandling;
        }

        public int hashCode() {
            int result = (31 * 17) + this.direction.hashCode();
            return (31 * ((31 * ((31 * result) + this.property.hashCode())) + (this.ignoreCase ? 1 : 0))) + this.nullHandling.hashCode();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Order)) {
                return false;
            }
            Order that = (Order) obj;
            return this.direction.equals(that.direction) && this.property.equals(that.property) && this.ignoreCase == that.ignoreCase && this.nullHandling.equals(that.nullHandling);
        }

        public String toString() {
            String result = String.format("%s: %s", this.property, this.direction);
            if (!NullHandling.NATIVE.equals(this.nullHandling)) {
                result = result + ", " + this.nullHandling;
            }
            if (this.ignoreCase) {
                result = result + ", ignoring case";
            }
            return result;
        }
    }
}
