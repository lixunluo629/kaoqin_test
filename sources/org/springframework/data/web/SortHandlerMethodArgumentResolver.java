package org.springframework.data.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/SortHandlerMethodArgumentResolver.class */
public class SortHandlerMethodArgumentResolver implements SortArgumentResolver {
    private static final String DEFAULT_PARAMETER = "sort";
    private static final String DEFAULT_PROPERTY_DELIMITER = ",";
    private static final String DEFAULT_QUALIFIER_DELIMITER = "_";
    private static final Sort DEFAULT_SORT = null;
    private static final String SORT_DEFAULTS_NAME = SortDefault.SortDefaults.class.getSimpleName();
    private static final String SORT_DEFAULT_NAME = SortDefault.class.getSimpleName();
    private Sort fallbackSort = DEFAULT_SORT;
    private String sortParameter = DEFAULT_PARAMETER;
    private String propertyDelimiter = ",";
    private String qualifierDelimiter = "_";

    public void setSortParameter(String sortParameter) {
        Assert.hasText(sortParameter, "SortParameter must not be null nor empty!");
        this.sortParameter = sortParameter;
    }

    public void setPropertyDelimiter(String propertyDelimiter) {
        Assert.hasText(propertyDelimiter, "Property delimiter must not be null or empty!");
        this.propertyDelimiter = propertyDelimiter;
    }

    public void setQualifierDelimiter(String qualifierDelimiter) {
        this.qualifierDelimiter = qualifierDelimiter == null ? "_" : qualifierDelimiter;
    }

    @Override // org.springframework.web.method.support.HandlerMethodArgumentResolver
    public boolean supportsParameter(MethodParameter parameter) {
        return Sort.class.equals(parameter.getParameterType());
    }

    @Override // org.springframework.web.method.support.HandlerMethodArgumentResolver
    public Sort resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String[] directionParameter = webRequest.getParameterValues(getSortParameter(parameter));
        if (directionParameter == null) {
            return getDefaultFromAnnotationOrFallback(parameter);
        }
        if (directionParameter.length == 1 && !StringUtils.hasText(directionParameter[0])) {
            return getDefaultFromAnnotationOrFallback(parameter);
        }
        return parseParameterIntoSort(directionParameter, this.propertyDelimiter);
    }

    private Sort getDefaultFromAnnotationOrFallback(MethodParameter parameter) {
        SortDefault.SortDefaults annotatedDefaults = (SortDefault.SortDefaults) parameter.getParameterAnnotation(SortDefault.SortDefaults.class);
        SortDefault annotatedDefault = (SortDefault) parameter.getParameterAnnotation(SortDefault.class);
        if (annotatedDefault != null && annotatedDefaults != null) {
            throw new IllegalArgumentException(String.format("Cannot use both @%s and @%s on parameter %s! Move %s into %s to define sorting order!", SORT_DEFAULTS_NAME, SORT_DEFAULT_NAME, parameter.toString(), SORT_DEFAULT_NAME, SORT_DEFAULTS_NAME));
        }
        if (annotatedDefault != null) {
            return appendOrCreateSortTo(annotatedDefault, null);
        }
        if (annotatedDefaults != null) {
            Sort sort = null;
            for (SortDefault currentAnnotatedDefault : annotatedDefaults.value()) {
                sort = appendOrCreateSortTo(currentAnnotatedDefault, sort);
            }
            return sort;
        }
        return this.fallbackSort;
    }

    private Sort appendOrCreateSortTo(SortDefault sortDefault, Sort sortOrNull) {
        String[] fields = (String[]) SpringDataAnnotationUtils.getSpecificPropertyOrDefaultFromValue(sortDefault, DEFAULT_PARAMETER);
        if (fields.length == 0) {
            return null;
        }
        Sort sort = new Sort(sortDefault.direction(), fields);
        return sortOrNull == null ? sort : sortOrNull.and(sort);
    }

    protected String getSortParameter(MethodParameter parameter) {
        StringBuilder builder = new StringBuilder();
        if (parameter != null && parameter.hasParameterAnnotation(Qualifier.class)) {
            builder.append(((Qualifier) parameter.getParameterAnnotation(Qualifier.class)).value()).append(this.qualifierDelimiter);
        }
        return builder.append(this.sortParameter).toString();
    }

    Sort parseParameterIntoSort(String[] source, String delimiter) {
        List<Sort.Order> allOrders = new ArrayList<>();
        for (String part : source) {
            if (part != null) {
                List<String> elements = new ArrayList<>();
                for (String candidate : part.split(delimiter)) {
                    if (StringUtils.hasText(candidate.replace(".", ""))) {
                        elements.add(candidate);
                    }
                }
                int numberOfElements = elements.size();
                Sort.Direction direction = numberOfElements == 0 ? null : Sort.Direction.fromStringOrNull(elements.get(numberOfElements - 1));
                for (int i = 0; i < numberOfElements; i++) {
                    if (i != numberOfElements - 1 || direction == null) {
                        String property = elements.get(i);
                        if (StringUtils.hasText(property)) {
                            allOrders.add(new Sort.Order(direction, property));
                        }
                    }
                }
            }
        }
        if (allOrders.isEmpty()) {
            return null;
        }
        return new Sort(allOrders);
    }

    protected List<String> foldIntoExpressions(Sort sort) {
        List<String> expressions = new ArrayList<>();
        ExpressionBuilder builder = null;
        Iterator<Sort.Order> it = sort.iterator();
        while (it.hasNext()) {
            Sort.Order order = it.next();
            Sort.Direction direction = order.getDirection();
            if (builder == null) {
                builder = new ExpressionBuilder(direction);
            } else if (!builder.hasSameDirectionAs(order)) {
                builder.dumpExpressionIfPresentInto(expressions);
                builder = new ExpressionBuilder(direction);
            }
            builder.add(order.getProperty());
        }
        return builder == null ? Collections.emptyList() : builder.dumpExpressionIfPresentInto(expressions);
    }

    protected List<String> legacyFoldExpressions(Sort sort) {
        List<String> expressions = new ArrayList<>();
        ExpressionBuilder builder = null;
        Iterator<Sort.Order> it = sort.iterator();
        while (it.hasNext()) {
            Sort.Order order = it.next();
            Sort.Direction direction = order.getDirection();
            if (builder == null) {
                builder = new ExpressionBuilder(direction);
            } else if (!builder.hasSameDirectionAs(order)) {
                throw new IllegalArgumentException(String.format("%s in legacy configuration only supports a single direction to sort by!", getClass().getSimpleName()));
            }
            builder.add(order.getProperty());
        }
        return builder == null ? Collections.emptyList() : builder.dumpExpressionIfPresentInto(expressions);
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/SortHandlerMethodArgumentResolver$ExpressionBuilder.class */
    class ExpressionBuilder {
        private final List<String> elements = new ArrayList();
        private final Sort.Direction direction;

        public ExpressionBuilder(Sort.Direction direction) {
            Assert.notNull(direction, "Direction must not be null!");
            this.direction = direction;
        }

        public boolean hasSameDirectionAs(Sort.Order order) {
            return this.direction == order.getDirection();
        }

        public void add(String property) {
            this.elements.add(property);
        }

        public List<String> dumpExpressionIfPresentInto(List<String> expressions) {
            if (this.elements.isEmpty()) {
                return expressions;
            }
            this.elements.add(this.direction.name().toLowerCase());
            expressions.add(StringUtils.collectionToDelimitedString(this.elements, SortHandlerMethodArgumentResolver.this.propertyDelimiter));
            return expressions;
        }
    }

    public void setFallbackSort(Sort fallbackSort) {
        this.fallbackSort = fallbackSort;
    }
}
