package org.springframework.data.repository.core.support;

import java.lang.reflect.Method;
import java.util.Map;
import org.springframework.core.CollectionFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.repository.util.NullableWrapper;
import org.springframework.data.repository.util.QueryExecutionConverters;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/QueryExecutionResultHandler.class */
class QueryExecutionResultHandler {
    private static final TypeDescriptor WRAPPER_TYPE = TypeDescriptor.valueOf(NullableWrapper.class);
    private final GenericConversionService conversionService;

    public QueryExecutionResultHandler() {
        GenericConversionService conversionService = new DefaultConversionService();
        QueryExecutionConverters.registerConvertersIn(conversionService);
        this.conversionService = conversionService;
    }

    public Object postProcessInvocationResult(Object result, Method method) {
        Assert.notNull(method, "Method must not be null!");
        if (method.getReturnType().isInstance(result)) {
            return result;
        }
        MethodParameter parameter = new MethodParameter(method, -1);
        TypeDescriptor methodReturnTypeDescriptor = TypeDescriptor.nested(parameter, 0);
        return postProcessInvocationResult(result, methodReturnTypeDescriptor);
    }

    Object postProcessInvocationResult(Object result, TypeDescriptor returnTypeDesciptor) {
        if (returnTypeDesciptor == null) {
            return result;
        }
        Class<?> expectedReturnType = returnTypeDesciptor.getType();
        if (result != null && expectedReturnType.isInstance(result)) {
            return result;
        }
        if (QueryExecutionConverters.supports(expectedReturnType) && this.conversionService.canConvert(WRAPPER_TYPE, returnTypeDesciptor) && !this.conversionService.canBypassConvert(WRAPPER_TYPE, TypeDescriptor.valueOf(expectedReturnType))) {
            return this.conversionService.convert(new NullableWrapper(result), expectedReturnType);
        }
        if (result != null) {
            return this.conversionService.canConvert(result.getClass(), expectedReturnType) ? this.conversionService.convert(result, expectedReturnType) : result;
        }
        if (Map.class.equals(expectedReturnType)) {
            return CollectionFactory.createMap(expectedReturnType, 0);
        }
        return null;
    }
}
