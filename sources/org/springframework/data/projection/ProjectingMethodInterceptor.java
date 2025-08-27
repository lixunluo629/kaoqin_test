package org.springframework.data.projection;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import lombok.NonNull;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.CollectionFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/projection/ProjectingMethodInterceptor.class */
class ProjectingMethodInterceptor implements MethodInterceptor {

    @NonNull
    private final ProjectionFactory factory;

    @NonNull
    private final MethodInterceptor delegate;

    @NonNull
    private final ConversionService conversionService;

    @Generated
    public ProjectingMethodInterceptor(@NonNull ProjectionFactory factory, @NonNull MethodInterceptor delegate, @NonNull ConversionService conversionService) {
        if (factory == null) {
            throw new IllegalArgumentException("factory is marked @NonNull but is null");
        }
        if (delegate == null) {
            throw new IllegalArgumentException("delegate is marked @NonNull but is null");
        }
        if (conversionService == null) {
            throw new IllegalArgumentException("conversionService is marked @NonNull but is null");
        }
        this.factory = factory;
        this.delegate = delegate;
        this.conversionService = conversionService;
    }

    @Override // org.aopalliance.intercept.MethodInterceptor
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object result = this.delegate.invoke(invocation);
        if (result == null) {
            return null;
        }
        TypeInformation<?> type = ClassTypeInformation.fromReturnTypeOf(invocation.getMethod());
        Class<?> rawType = type.getType();
        if (type.isCollectionLike() && !ClassUtils.isPrimitiveArray(rawType)) {
            return projectCollectionElements(asCollection(result), type);
        }
        if (type.isMap()) {
            return projectMapValues((Map) result, type);
        }
        if (conversionRequiredAndPossible(result, rawType)) {
            return this.conversionService.convert(result, rawType);
        }
        return getProjection(result, rawType);
    }

    private Object projectCollectionElements(Collection<?> sources, TypeInformation<?> type) {
        Class<?> rawType = type.getType();
        Collection<Object> result = CollectionFactory.createCollection(rawType.isArray() ? List.class : rawType, sources.size());
        for (Object source : sources) {
            result.add(getProjection(source, type.getComponentType().getType()));
        }
        if (rawType.isArray()) {
            return result.toArray((Object[]) Array.newInstance(type.getComponentType().getType(), result.size()));
        }
        return result;
    }

    private Map<Object, Object> projectMapValues(Map<?, ?> sources, TypeInformation<?> type) {
        Map<Object, Object> result = CollectionFactory.createMap(type.getType(), sources.size());
        for (Map.Entry<?, ?> source : sources.entrySet()) {
            result.put(source.getKey(), getProjection(source.getValue(), type.getMapValueType().getType()));
        }
        return result;
    }

    private Object getProjection(Object result, Class<?> returnType) {
        return (result == null || ClassUtils.isAssignable(returnType, result.getClass())) ? result : this.factory.createProjection(returnType, result);
    }

    private boolean conversionRequiredAndPossible(Object source, Class<?> targetType) {
        if (source == null || targetType.isInstance(source)) {
            return false;
        }
        return this.conversionService.canConvert(source.getClass(), targetType);
    }

    private static Collection<?> asCollection(Object source) {
        Assert.notNull(source, "Source object must not be null!");
        if (source instanceof Collection) {
            return (Collection) source;
        }
        if (source.getClass().isArray()) {
            return Arrays.asList(ObjectUtils.toObjectArray(source));
        }
        return Collections.singleton(source);
    }
}
