package org.springframework.data.repository.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/support/MethodParameters.class */
class MethodParameters {
    private final ParameterNameDiscoverer discoverer;
    private final List<MethodParameter> parameters;

    public MethodParameters(Method method) {
        this(method, null);
    }

    public MethodParameters(Method method, AnnotationAttribute namingAnnotation) {
        this.discoverer = new DefaultParameterNameDiscoverer();
        Assert.notNull(method, "Method must not be null!");
        this.parameters = new ArrayList();
        for (int i = 0; i < method.getParameterTypes().length; i++) {
            MethodParameter parameter = new AnnotationNamingMethodParameter(method, i, namingAnnotation);
            parameter.initParameterNameDiscovery(this.discoverer);
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
        Assert.notNull(annotation, "Annotation must not be null!");
        List<MethodParameter> result = new ArrayList<>();
        for (MethodParameter parameter : getParameters()) {
            if (parameter.hasParameterAnnotation(annotation)) {
                result.add(parameter);
            }
        }
        return result;
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/support/MethodParameters$AnnotationNamingMethodParameter.class */
    private static class AnnotationNamingMethodParameter extends MethodParameter {
        private final AnnotationAttribute attribute;
        private String name;

        public AnnotationNamingMethodParameter(Method method, int parameterIndex, AnnotationAttribute attribute) {
            super(method, parameterIndex);
            this.attribute = attribute;
        }

        @Override // org.springframework.core.MethodParameter
        public String getParameterName() {
            Object foundName;
            if (this.name != null) {
                return this.name;
            }
            if (this.attribute != null && (foundName = this.attribute.getValueFrom(this)) != null) {
                this.name = foundName.toString();
                return this.name;
            }
            this.name = super.getParameterName();
            return this.name;
        }
    }
}
