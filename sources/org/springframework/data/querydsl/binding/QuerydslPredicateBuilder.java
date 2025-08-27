package org.springframework.data.querydsl.binding;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.Property;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/querydsl/binding/QuerydslPredicateBuilder.class */
public class QuerydslPredicateBuilder {
    private final ConversionService conversionService;
    private final MultiValueBinding<?, ?> defaultBinding;
    private final Map<PathInformation, Path<?>> paths;
    private final EntityPathResolver resolver;

    public QuerydslPredicateBuilder(ConversionService conversionService, EntityPathResolver resolver) {
        Assert.notNull(conversionService, "ConversionService must not be null!");
        this.defaultBinding = new QuerydslDefaultBinding();
        this.conversionService = conversionService;
        this.paths = new ConcurrentHashMap();
        this.resolver = resolver;
    }

    public Predicate getPredicate(TypeInformation<?> type, MultiValueMap<String, String> values, QuerydslBindings bindings) {
        PathInformation propertyPath;
        Assert.notNull(bindings, "Context must not be null!");
        BooleanBuilder builder = new BooleanBuilder();
        if (values.isEmpty()) {
            return builder.getValue();
        }
        for (Map.Entry<String, String> entry : values.entrySet()) {
            if (!isSingleElementCollectionWithoutText((List) entry.getValue())) {
                String path = entry.getKey();
                if (bindings.isPathAvailable(path, type) && (propertyPath = bindings.getPropertyPath(path, type)) != null) {
                    Collection<Object> value = convertToPropertyPathSpecificType((List) entry.getValue(), propertyPath);
                    Predicate predicate = invokeBinding(propertyPath, bindings, value);
                    if (predicate != null) {
                        builder.and(predicate);
                    }
                }
            }
        }
        return builder.getValue();
    }

    private Predicate invokeBinding(PathInformation dotPath, QuerydslBindings bindings, Collection<Object> values) {
        Path<?> path = getPath(dotPath, bindings);
        MultiValueBinding binding = bindings.getBindingForPath(dotPath);
        return (binding == null ? this.defaultBinding : binding).bind(path, values);
    }

    private Path<?> getPath(PathInformation path, QuerydslBindings bindings) {
        Path<?> resolvedPath = bindings.getExistingPath(path);
        if (resolvedPath != null) {
            return resolvedPath;
        }
        Path<?> resolvedPath2 = this.paths.get(path);
        if (resolvedPath2 != null) {
            return resolvedPath2;
        }
        Path<?> resolvedPath3 = path.reifyPath(this.resolver);
        this.paths.put(path, resolvedPath3);
        return resolvedPath3;
    }

    private Collection<Object> convertToPropertyPathSpecificType(List<String> source, PathInformation path) {
        Class<?> targetType = path.getLeafType();
        if (source.isEmpty() || isSingleElementCollectionWithoutText(source)) {
            return Collections.emptyList();
        }
        Collection<Object> target = new ArrayList<>(source.size());
        for (String value : source) {
            target.add(this.conversionService.canConvert(String.class, targetType) ? this.conversionService.convert(value, TypeDescriptor.forObject(value), getTargetTypeDescriptor(path)) : value);
        }
        return target;
    }

    private static TypeDescriptor getTargetTypeDescriptor(PathInformation path) {
        PropertyDescriptor descriptor = path.getLeafPropertyDescriptor();
        Class<?> owningType = path.getLeafParentType();
        String leafProperty = path.getLeafProperty();
        if (descriptor == null) {
            return TypeDescriptor.nested(ReflectionUtils.findField(owningType, leafProperty), 0);
        }
        return TypeDescriptor.nested(new Property(owningType, descriptor.getReadMethod(), descriptor.getWriteMethod(), leafProperty), 0);
    }

    private static boolean isSingleElementCollectionWithoutText(List<String> source) {
        return source.size() == 1 && !StringUtils.hasLength(source.get(0));
    }
}
