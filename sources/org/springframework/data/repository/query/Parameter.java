package org.springframework.data.repository.query;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.util.QueryExecutionConverters;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/Parameter.class */
public class Parameter {
    static final List<Class<?>> TYPES = Arrays.asList(Pageable.class, Sort.class);
    private static final String NAMED_PARAMETER_TEMPLATE = ":%s";
    private static final String POSITION_PARAMETER_TEMPLATE = "?%s";
    private final MethodParameter parameter;
    private final Class<?> parameterType;
    private final boolean isDynamicProjectionParameter;

    protected Parameter(MethodParameter parameter) {
        Assert.notNull(parameter, "MethodParameter must not be null!");
        this.parameter = parameter;
        this.parameterType = potentiallyUnwrapParameterType(parameter);
        this.isDynamicProjectionParameter = isDynamicProjectionParameter(parameter);
    }

    public boolean isSpecialParameter() {
        return this.isDynamicProjectionParameter || TYPES.contains(this.parameter.getParameterType());
    }

    public boolean isBindable() {
        return !isSpecialParameter();
    }

    public boolean isDynamicProjectionParameter() {
        return this.isDynamicProjectionParameter;
    }

    public String getPlaceholder() {
        if (isNamedParameter()) {
            return String.format(NAMED_PARAMETER_TEMPLATE, getName());
        }
        return String.format(POSITION_PARAMETER_TEMPLATE, Integer.valueOf(getIndex()));
    }

    public int getIndex() {
        return this.parameter.getParameterIndex();
    }

    public boolean isNamedParameter() {
        return (isSpecialParameter() || getName() == null) ? false : true;
    }

    public String getName() {
        Param annotation = (Param) this.parameter.getParameterAnnotation(Param.class);
        return annotation == null ? this.parameter.getParameterName() : annotation.value();
    }

    public Class<?> getType() {
        return this.parameterType;
    }

    public boolean isExplicitlyNamed() {
        return this.parameter.hasParameterAnnotation(Param.class);
    }

    public String toString() {
        Object[] objArr = new Object[2];
        objArr[0] = isNamedParameter() ? getName() : "#" + getIndex();
        objArr[1] = getType().getName();
        return String.format("%s:%s", objArr);
    }

    boolean isPageable() {
        return Pageable.class.isAssignableFrom(getType());
    }

    boolean isSort() {
        return Sort.class.isAssignableFrom(getType());
    }

    private static boolean isDynamicProjectionParameter(MethodParameter parameter) {
        Method method = parameter.getMethod();
        ClassTypeInformation<?> ownerType = ClassTypeInformation.from(parameter.getDeclaringClass());
        TypeInformation<?> parameterTypes = (TypeInformation) ownerType.getParameterTypes(method).get(parameter.getParameterIndex());
        if (!parameterTypes.getType().equals(Class.class)) {
            return false;
        }
        TypeInformation<?> bound = parameterTypes.getTypeArguments().get(0);
        TypeInformation<Object> returnType = ClassTypeInformation.fromReturnTypeOf(method);
        return bound.equals(QueryExecutionConverters.unwrapWrapperTypes(returnType));
    }

    private static Class<?> potentiallyUnwrapParameterType(MethodParameter parameter) {
        Class<?> originalType = parameter.getParameterType();
        return QueryExecutionConverters.supports(originalType) ? ResolvableType.forMethodParameter(parameter).getGeneric(0).getRawClass() : originalType;
    }
}
