package org.springframework.hateoas.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/core/MethodParameters.class */
public class MethodParameters {
    private static ParameterNameDiscoverer DISCOVERER = new DefaultParameterNameDiscoverer();
    private final List<MethodParameter> parameters;
    private final Map<Class<?>, List<MethodParameter>> parametersWithAnnotationCache;

    public MethodParameters(Method method) {
        this(method, null);
    }

    public MethodParameters(Method method, AnnotationAttribute namingAnnotation) {
        this.parametersWithAnnotationCache = new ConcurrentReferenceHashMap();
        Assert.notNull(method);
        this.parameters = new ArrayList();
        for (int i = 0; i < method.getParameterTypes().length; i++) {
            MethodParameter parameter = new AnnotationNamingMethodParameter(method, i, namingAnnotation);
            parameter.initParameterNameDiscovery(DISCOVERER);
            this.parameters.add(parameter);
        }
    }

    public List<MethodParameter> getParameters() {
        return this.parameters;
    }

    public MethodParameter getParameter(String name) {
        Assert.hasText(name, "Parameter name must not be null!");
        for (MethodParameter parameter : this.parameters) {
            if (name.equals(parameter.getParameterName())) {
                return parameter;
            }
        }
        return null;
    }

    public List<MethodParameter> getParametersOfType(Class<?> type) {
        Assert.notNull(type, "Type must not be null!");
        List<MethodParameter> result = new ArrayList<>();
        for (MethodParameter parameter : getParameters()) {
            if (parameter.getParameterType().equals(type)) {
                result.add(parameter);
            }
        }
        return result;
    }

    public List<MethodParameter> getParametersWith(Class<? extends Annotation> annotation) {
        List<MethodParameter> cached = this.parametersWithAnnotationCache.get(annotation);
        if (cached != null) {
            return cached;
        }
        Assert.notNull(annotation);
        List<MethodParameter> result = new ArrayList<>();
        for (MethodParameter parameter : getParameters()) {
            if (parameter.hasParameterAnnotation(annotation)) {
                result.add(parameter);
            }
        }
        this.parametersWithAnnotationCache.put(annotation, result);
        return result;
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/core/MethodParameters$AnnotationNamingMethodParameter.class */
    private static class AnnotationNamingMethodParameter extends MethodParameter {
        private final AnnotationAttribute attribute;
        private String name;

        public AnnotationNamingMethodParameter(Method method, int parameterIndex, AnnotationAttribute attribute) {
            super(method, parameterIndex);
            this.attribute = attribute;
        }

        @Override // org.springframework.core.MethodParameter
        public String getParameterName() {
            String foundName;
            if (this.name != null) {
                return this.name;
            }
            if (this.attribute != null && (foundName = this.attribute.getValueFrom(this)) != null) {
                this.name = foundName;
                return this.name;
            }
            this.name = super.getParameterName();
            return this.name;
        }
    }
}
