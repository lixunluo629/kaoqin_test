package org.springframework.data.repository.query.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/parser/OrderBySource.class */
public class OrderBySource {
    private static final String BLOCK_SPLIT = "(?<=Asc|Desc)(?=\\p{Lu})";
    private static final String INVALID_ORDER_SYNTAX = "Invalid order syntax for part %s!";
    private final List<Sort.Order> orders;
    private static final Pattern DIRECTION_SPLIT = Pattern.compile("(.+?)(Asc|Desc)?$");
    private static final Set<String> DIRECTION_KEYWORDS = new HashSet(Arrays.asList("Asc", "Desc"));

    public OrderBySource(String clause) {
        this(clause, null);
    }

    public OrderBySource(String clause, Class<?> domainClass) {
        this.orders = new ArrayList();
        for (String part : clause.split(BLOCK_SPLIT)) {
            Matcher matcher = DIRECTION_SPLIT.matcher(part);
            if (!matcher.find()) {
                throw new IllegalArgumentException(String.format(INVALID_ORDER_SYNTAX, part));
            }
            String propertyString = matcher.group(1);
            String directionString = matcher.group(2);
            if (DIRECTION_KEYWORDS.contains(propertyString) && directionString == null) {
                throw new IllegalArgumentException(String.format(INVALID_ORDER_SYNTAX, part));
            }
            Sort.Direction direction = StringUtils.hasText(directionString) ? Sort.Direction.fromString(directionString) : null;
            this.orders.add(createOrder(propertyString, direction, domainClass));
        }
    }

    private Sort.Order createOrder(String propertySource, Sort.Direction direction, Class<?> domainClass) {
        if (null == domainClass) {
            return new Sort.Order(direction, StringUtils.uncapitalize(propertySource));
        }
        PropertyPath propertyPath = PropertyPath.from(propertySource, domainClass);
        return new Sort.Order(direction, propertyPath.toDotPath());
    }

    public Sort toSort() {
        if (this.orders.isEmpty()) {
            return null;
        }
        return new Sort(this.orders);
    }

    public String toString() {
        return "Order By " + StringUtils.collectionToDelimitedString(this.orders, ", ");
    }
}
