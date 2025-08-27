package org.springframework.data.repository.support;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.core.CrudMethods;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.Param;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/support/ReflectionRepositoryInvoker.class */
class ReflectionRepositoryInvoker implements RepositoryInvoker {
    private static final AnnotationAttribute PARAM_ANNOTATION = new AnnotationAttribute(Param.class);
    private static final String NAME_NOT_FOUND = "Unable to detect parameter names for query method %s! Use @Param or compile with -parameters on JDK 8.";
    private final Object repository;
    private final CrudMethods methods;
    private final Class<? extends Serializable> idType;
    private final ConversionService conversionService;

    public ReflectionRepositoryInvoker(Object repository, RepositoryMetadata metadata, ConversionService conversionService) {
        Assert.notNull(repository, "Repository must not be null!");
        Assert.notNull(metadata, "RepositoryMetadata must not be null!");
        Assert.notNull(conversionService, "ConversionService must not be null!");
        this.repository = repository;
        this.methods = metadata.getCrudMethods();
        this.idType = metadata.getIdType();
        this.conversionService = conversionService;
    }

    @Override // org.springframework.data.repository.support.RepositoryInvocationInformation
    public boolean hasFindAllMethod() {
        return this.methods.hasFindAllMethod();
    }

    @Override // org.springframework.data.repository.support.RepositoryInvoker
    public Iterable<Object> invokeFindAll(Sort sort) {
        return invokeFindAllReflectively(sort);
    }

    @Override // org.springframework.data.repository.support.RepositoryInvoker
    public Iterable<Object> invokeFindAll(Pageable pageable) {
        return invokeFindAllReflectively(pageable);
    }

    @Override // org.springframework.data.repository.support.RepositoryInvocationInformation
    public boolean hasSaveMethod() {
        return this.methods.hasSaveMethod();
    }

    @Override // org.springframework.data.repository.support.RepositoryInvoker
    public <T> T invokeSave(T t) {
        Assert.state(hasSaveMethod(), "Repository doesn't have a save-method declared!");
        return (T) invoke(this.methods.getSaveMethod(), t);
    }

    @Override // org.springframework.data.repository.support.RepositoryInvocationInformation
    public boolean hasFindOneMethod() {
        return this.methods.hasFindOneMethod();
    }

    @Override // org.springframework.data.repository.support.RepositoryInvoker
    public <T> T invokeFindOne(Serializable serializable) {
        Assert.state(hasFindOneMethod(), "Repository doesn't have a find-one-method declared!");
        return (T) invoke(this.methods.getFindOneMethod(), convertId(serializable));
    }

    @Override // org.springframework.data.repository.support.RepositoryInvocationInformation
    public boolean hasDeleteMethod() {
        return this.methods.hasDelete();
    }

    @Override // org.springframework.data.repository.support.RepositoryInvoker
    public void invokeDelete(Serializable id) {
        Assert.notNull(id, "Identifier must not be null!");
        Assert.state(hasDeleteMethod(), "Repository doesn't have a delete-method declared!");
        Method method = this.methods.getDeleteMethod();
        Class<?> parameterType = method.getParameterTypes()[0];
        List<Class<? extends Serializable>> idTypes = Arrays.asList(this.idType, Serializable.class);
        if (idTypes.contains(parameterType)) {
            invoke(method, convertId(id));
        } else {
            invoke(method, invokeFindOne(id));
        }
    }

    @Override // org.springframework.data.repository.support.RepositoryInvoker
    public Object invokeQueryMethod(Method method, Map<String, String[]> parameters, Pageable pageable, Sort sort) {
        Assert.notNull(method, "Method must not be null!");
        Assert.notNull(parameters, "Parameters must not be null!");
        MultiValueMap<String, String> forward = new LinkedMultiValueMap<>(parameters.size());
        for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
            forward.put(entry.getKey(), Arrays.asList(entry.getValue()));
        }
        return invokeQueryMethod(method, (MultiValueMap<String, ? extends Object>) forward, pageable, sort);
    }

    @Override // org.springframework.data.repository.support.RepositoryInvoker
    public Object invokeQueryMethod(Method method, MultiValueMap<String, ? extends Object> parameters, Pageable pageable, Sort sort) {
        Assert.notNull(method, "Method must not be null!");
        Assert.notNull(parameters, "Parameters must not be null!");
        ReflectionUtils.makeAccessible(method);
        return invoke(method, prepareParameters(method, parameters, pageable, sort));
    }

    private Object[] prepareParameters(Method method, MultiValueMap<String, ? extends Object> rawParameters, Pageable pageable, Sort sort) {
        List<MethodParameter> parameters = new MethodParameters(method, PARAM_ANNOTATION).getParameters();
        if (parameters.isEmpty()) {
            return new Object[0];
        }
        Object[] result = new Object[parameters.size()];
        Sort sortToUse = pageable == null ? sort : pageable.getSort();
        for (int i = 0; i < result.length; i++) {
            MethodParameter param = parameters.get(i);
            Class<?> targetType = param.getParameterType();
            if (Pageable.class.isAssignableFrom(targetType)) {
                result[i] = pageable;
            } else if (Sort.class.isAssignableFrom(targetType)) {
                result[i] = sortToUse;
            } else {
                String parameterName = param.getParameterName();
                if (!StringUtils.hasText(parameterName)) {
                    throw new IllegalArgumentException(String.format(NAME_NOT_FOUND, ClassUtils.getQualifiedMethodName(method)));
                }
                Object value = unwrapSingleElement((List) rawParameters.get(parameterName));
                result[i] = targetType.isInstance(value) ? value : convert(value, param);
            }
        }
        return result;
    }

    private Object convert(Object value, MethodParameter parameter) {
        try {
            return this.conversionService.convert(value, TypeDescriptor.forObject(value), new TypeDescriptor(parameter));
        } catch (ConversionException o_O) {
            throw new QueryMethodParameterConversionException(value, parameter, o_O);
        }
    }

    private <T> T invoke(Method method, Object... objArr) {
        return (T) ReflectionUtils.invokeMethod(method, this.repository, objArr);
    }

    protected Serializable convertId(Serializable id) {
        Assert.notNull(id, "Id must not be null!");
        return (Serializable) this.conversionService.convert(id, this.idType);
    }

    protected Iterable<Object> invokeFindAllReflectively(Pageable pageable) {
        Assert.state(hasFindAllMethod(), "Repository doesn't have a find-all-method declared!");
        Method method = this.methods.getFindAllMethod();
        Class<?>[] types = method.getParameterTypes();
        if (types.length == 0) {
            return (Iterable) invoke(method, new Object[0]);
        }
        if (Pageable.class.isAssignableFrom(types[0])) {
            return (Iterable) invoke(method, pageable);
        }
        Sort sort = pageable == null ? null : pageable.getSort();
        return invokeFindAll(sort);
    }

    protected Iterable<Object> invokeFindAllReflectively(Sort sort) {
        Assert.state(hasFindAllMethod(), "Repository doesn't have a find-all-method declared!");
        Method method = this.methods.getFindAllMethod();
        Class<?>[] types = method.getParameterTypes();
        if (types.length == 0) {
            return (Iterable) invoke(method, new Object[0]);
        }
        return (Iterable) invoke(method, sort);
    }

    private static Object unwrapSingleElement(List<? extends Object> source) {
        if (source == null) {
            return null;
        }
        return source.size() == 1 ? source.get(0) : source;
    }
}
