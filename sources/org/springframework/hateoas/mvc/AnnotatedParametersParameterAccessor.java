package org.springframework.hateoas.mvc;

import java.beans.ConstructorProperties;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.hateoas.core.AnnotationAttribute;
import org.springframework.hateoas.core.DummyInvocationUtils;
import org.springframework.hateoas.core.MethodParameters;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.StringUtils;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/AnnotatedParametersParameterAccessor.class */
class AnnotatedParametersParameterAccessor {
    private static final Map<Method, MethodParameters> METHOD_PARAMETERS_CACHE = new ConcurrentReferenceHashMap(16, ConcurrentReferenceHashMap.ReferenceType.WEAK);

    @NonNull
    private final AnnotationAttribute attribute;

    @ConstructorProperties({BeanDefinitionParserDelegate.QUALIFIER_ATTRIBUTE_ELEMENT})
    public AnnotatedParametersParameterAccessor(@NonNull AnnotationAttribute attribute) {
        if (attribute == null) {
            throw new NullPointerException(BeanDefinitionParserDelegate.QUALIFIER_ATTRIBUTE_ELEMENT);
        }
        this.attribute = attribute;
    }

    public List<BoundMethodParameter> getBoundParameters(DummyInvocationUtils.MethodInvocation invocation) {
        Assert.notNull(invocation, "MethodInvocation must not be null!");
        MethodParameters parameters = getOrCreateMethodParametersFor(invocation.getMethod());
        Object[] arguments = invocation.getArguments();
        List<BoundMethodParameter> result = new ArrayList<>();
        for (MethodParameter parameter : parameters.getParametersWith(this.attribute.getAnnotationType())) {
            Object value = arguments[parameter.getParameterIndex()];
            Object verifiedValue = verifyParameterValue(parameter, value);
            if (verifiedValue != null) {
                result.add(createParameter(parameter, verifiedValue, this.attribute));
            }
        }
        return result;
    }

    protected BoundMethodParameter createParameter(MethodParameter parameter, Object value, AnnotationAttribute attribute) {
        return new BoundMethodParameter(parameter, value, attribute);
    }

    protected Object verifyParameterValue(MethodParameter parameter, Object value) {
        return value;
    }

    private static MethodParameters getOrCreateMethodParametersFor(Method method) {
        MethodParameters methodParameters = METHOD_PARAMETERS_CACHE.get(method);
        if (methodParameters != null) {
            return methodParameters;
        }
        MethodParameters methodParameters2 = new MethodParameters(method);
        METHOD_PARAMETERS_CACHE.put(method, methodParameters2);
        return methodParameters2;
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/AnnotatedParametersParameterAccessor$BoundMethodParameter.class */
    static class BoundMethodParameter {
        private static final ConversionService CONVERSION_SERVICE = new DefaultFormattingConversionService();
        private static final TypeDescriptor STRING_DESCRIPTOR = TypeDescriptor.valueOf(String.class);
        private final MethodParameter parameter;
        private final Object value;
        private final AnnotationAttribute attribute;
        private final TypeDescriptor parameterTypeDecsriptor;

        public BoundMethodParameter(MethodParameter parameter, Object value, AnnotationAttribute attribute) {
            Assert.notNull(parameter, "MethodParameter must not be null!");
            this.parameter = parameter;
            this.value = value;
            this.attribute = attribute;
            this.parameterTypeDecsriptor = TypeDescriptor.nested(parameter, 0);
        }

        public String getVariableName() {
            if (this.attribute == null) {
                return this.parameter.getParameterName();
            }
            Annotation annotation = this.parameter.getParameterAnnotation(this.attribute.getAnnotationType());
            String annotationAttributeValue = this.attribute.getValueFrom(annotation);
            return StringUtils.hasText(annotationAttributeValue) ? annotationAttributeValue : this.parameter.getParameterName();
        }

        public Object getValue() {
            return this.value;
        }

        public String asString() {
            if (this.value == null) {
                return null;
            }
            return (String) CONVERSION_SERVICE.convert(this.value, this.parameterTypeDecsriptor, STRING_DESCRIPTOR);
        }

        public boolean isRequired() {
            return true;
        }
    }
}
