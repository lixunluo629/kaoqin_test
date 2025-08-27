package org.springframework.hateoas.mvc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.core.MethodParameter;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.hateoas.MethodLinkBuilderFactory;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.core.AnnotationAttribute;
import org.springframework.hateoas.core.AnnotationMappingDiscoverer;
import org.springframework.hateoas.core.DummyInvocationUtils;
import org.springframework.hateoas.core.EncodingUtils;
import org.springframework.hateoas.core.MappingDiscoverer;
import org.springframework.hateoas.core.MethodParameters;
import org.springframework.hateoas.mvc.AnnotatedParametersParameterAccessor;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriTemplate;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/ControllerLinkBuilderFactory.class */
public class ControllerLinkBuilderFactory implements MethodLinkBuilderFactory<ControllerLinkBuilder> {
    private static final MappingDiscoverer DISCOVERER = new AnnotationMappingDiscoverer(RequestMapping.class);
    private static final AnnotatedParametersParameterAccessor PATH_VARIABLE_ACCESSOR = new AnnotatedParametersParameterAccessor(new AnnotationAttribute(PathVariable.class));
    private static final AnnotatedParametersParameterAccessor REQUEST_PARAM_ACCESSOR = new RequestParamParameterAccessor();
    private List<UriComponentsContributor> uriComponentsContributors = new ArrayList();

    @Override // org.springframework.hateoas.MethodLinkBuilderFactory
    public /* bridge */ /* synthetic */ LinkBuilder linkTo(Class cls, Method method, Object[] objArr) {
        return linkTo((Class<?>) cls, method, objArr);
    }

    @Override // org.springframework.hateoas.LinkBuilderFactory
    public /* bridge */ /* synthetic */ LinkBuilder linkTo(Class cls, Map map) {
        return linkTo((Class<?>) cls, (Map<String, ?>) map);
    }

    @Override // org.springframework.hateoas.LinkBuilderFactory
    public /* bridge */ /* synthetic */ LinkBuilder linkTo(Class cls, Object[] objArr) {
        return linkTo((Class<?>) cls, objArr);
    }

    @Override // org.springframework.hateoas.LinkBuilderFactory
    public /* bridge */ /* synthetic */ LinkBuilder linkTo(Class cls) {
        return linkTo((Class<?>) cls);
    }

    public void setUriComponentsContributors(List<? extends UriComponentsContributor> uriComponentsContributors) {
        this.uriComponentsContributors = Collections.unmodifiableList(uriComponentsContributors);
    }

    @Override // org.springframework.hateoas.LinkBuilderFactory
    public ControllerLinkBuilder linkTo(Class<?> controller) {
        return ControllerLinkBuilder.linkTo(controller);
    }

    @Override // org.springframework.hateoas.LinkBuilderFactory
    public ControllerLinkBuilder linkTo(Class<?> controller, Object... parameters) {
        return ControllerLinkBuilder.linkTo(controller, parameters);
    }

    @Override // org.springframework.hateoas.LinkBuilderFactory
    public ControllerLinkBuilder linkTo(Class<?> controller, Map<String, ?> parameters) {
        return ControllerLinkBuilder.linkTo(controller, parameters);
    }

    @Override // org.springframework.hateoas.MethodLinkBuilderFactory
    public ControllerLinkBuilder linkTo(Class<?> controller, Method method, Object... parameters) {
        return ControllerLinkBuilder.linkTo(controller, method, parameters);
    }

    @Override // org.springframework.hateoas.MethodLinkBuilderFactory
    public ControllerLinkBuilder linkTo(Object invocationValue) {
        Assert.isInstanceOf(DummyInvocationUtils.LastInvocationAware.class, invocationValue);
        DummyInvocationUtils.LastInvocationAware invocations = (DummyInvocationUtils.LastInvocationAware) invocationValue;
        DummyInvocationUtils.MethodInvocation invocation = invocations.getLastInvocation();
        Iterator<Object> classMappingParameters = invocations.getObjectParameters();
        Method method = invocation.getMethod();
        String mapping = DISCOVERER.getMapping(invocation.getTargetType(), method);
        UriComponentsBuilder builder = ControllerLinkBuilder.getBuilder().path(mapping);
        UriTemplate template = new UriTemplate(mapping);
        Map<String, Object> values = new HashMap<>();
        Iterator<String> names = template.getVariableNames().iterator();
        while (classMappingParameters.hasNext()) {
            values.put(names.next(), EncodingUtils.encodePath(classMappingParameters.next()));
        }
        for (AnnotatedParametersParameterAccessor.BoundMethodParameter parameter : PATH_VARIABLE_ACCESSOR.getBoundParameters(invocation)) {
            values.put(parameter.getVariableName(), EncodingUtils.encodePath(parameter.asString()));
        }
        List<String> optionalEmptyParameters = new ArrayList<>();
        for (AnnotatedParametersParameterAccessor.BoundMethodParameter parameter2 : REQUEST_PARAM_ACCESSOR.getBoundParameters(invocation)) {
            bindRequestParameters(builder, parameter2);
            if (UriComponents.UriTemplateVariables.SKIP_VALUE.equals(parameter2.getValue())) {
                values.put(parameter2.getVariableName(), UriComponents.UriTemplateVariables.SKIP_VALUE);
                if (!parameter2.isRequired()) {
                    optionalEmptyParameters.add(parameter2.getVariableName());
                }
            }
        }
        for (String variable : template.getVariableNames()) {
            if (!values.containsKey(variable)) {
                values.put(variable, UriComponents.UriTemplateVariables.SKIP_VALUE);
            }
        }
        UriComponents components = applyUriComponentsContributer(builder, invocation).buildAndExpand((Map<String, ?>) values);
        TemplateVariables variables = TemplateVariables.NONE;
        for (String parameter3 : optionalEmptyParameters) {
            boolean previousRequestParameter = components.getQueryParams().isEmpty() && variables.equals(TemplateVariables.NONE);
            variables = variables.concat(new TemplateVariable(parameter3, previousRequestParameter ? TemplateVariable.VariableType.REQUEST_PARAM : TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED));
        }
        return new ControllerLinkBuilder(components, variables);
    }

    @Override // org.springframework.hateoas.MethodLinkBuilderFactory
    public ControllerLinkBuilder linkTo(Method method, Object... parameters) {
        return ControllerLinkBuilder.linkTo(method, parameters);
    }

    protected UriComponentsBuilder applyUriComponentsContributer(UriComponentsBuilder builder, DummyInvocationUtils.MethodInvocation invocation) {
        MethodParameters parameters = new MethodParameters(invocation.getMethod());
        Iterator<Object> parameterValues = Arrays.asList(invocation.getArguments()).iterator();
        for (MethodParameter parameter : parameters.getParameters()) {
            Object parameterValue = parameterValues.next();
            for (UriComponentsContributor contributor : this.uriComponentsContributors) {
                if (contributor.supportsParameter(parameter)) {
                    contributor.enhance(builder, parameter, parameterValue);
                }
            }
        }
        return builder;
    }

    private static void bindRequestParameters(UriComponentsBuilder builder, AnnotatedParametersParameterAccessor.BoundMethodParameter parameter) {
        Object value = parameter.getValue();
        String key = parameter.getVariableName();
        if (value instanceof MultiValueMap) {
            MultiValueMap<String, String> requestParams = (MultiValueMap) value;
            for (Map.Entry<String, String> entry : requestParams.entrySet()) {
                for (String singleEntryValue : (List) entry.getValue()) {
                    builder.queryParam(entry.getKey(), EncodingUtils.encodeParameter(singleEntryValue));
                }
            }
            return;
        }
        if (value instanceof Map) {
            Map<String, String> requestParams2 = (Map) value;
            for (Map.Entry<String, String> requestParamEntry : requestParams2.entrySet()) {
                builder.queryParam(requestParamEntry.getKey(), EncodingUtils.encodeParameter(requestParamEntry.getValue()));
            }
            return;
        }
        if (value instanceof Collection) {
            for (Object element : (Collection) value) {
                builder.queryParam(key, EncodingUtils.encodeParameter(element));
            }
            return;
        }
        if (UriComponents.UriTemplateVariables.SKIP_VALUE.equals(value)) {
            if (parameter.isRequired()) {
                builder.queryParam(key, String.format("{%s}", parameter.getVariableName()));
                return;
            }
            return;
        }
        builder.queryParam(key, EncodingUtils.encodeParameter(parameter.asString()));
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/ControllerLinkBuilderFactory$RequestParamParameterAccessor.class */
    private static class RequestParamParameterAccessor extends AnnotatedParametersParameterAccessor {
        public RequestParamParameterAccessor() {
            super(new AnnotationAttribute(RequestParam.class));
        }

        @Override // org.springframework.hateoas.mvc.AnnotatedParametersParameterAccessor
        protected AnnotatedParametersParameterAccessor.BoundMethodParameter createParameter(final MethodParameter parameter, Object value, AnnotationAttribute attribute) {
            return new AnnotatedParametersParameterAccessor.BoundMethodParameter(parameter, value, attribute) { // from class: org.springframework.hateoas.mvc.ControllerLinkBuilderFactory.RequestParamParameterAccessor.1
                @Override // org.springframework.hateoas.mvc.AnnotatedParametersParameterAccessor.BoundMethodParameter
                public boolean isRequired() {
                    RequestParam annotation = (RequestParam) parameter.getParameterAnnotation(RequestParam.class);
                    return !parameter.getParameterType().getName().equals("java.lang.Optional") && annotation.required() && annotation.defaultValue().equals(ValueConstants.DEFAULT_NONE);
                }
            };
        }

        @Override // org.springframework.hateoas.mvc.AnnotatedParametersParameterAccessor
        protected Object verifyParameterValue(MethodParameter parameter, Object value) {
            RequestParam annotation = (RequestParam) parameter.getParameterAnnotation(RequestParam.class);
            if (value != null) {
                return value;
            }
            if (!annotation.required()) {
                return UriComponents.UriTemplateVariables.SKIP_VALUE;
            }
            if (annotation.defaultValue().equals(ValueConstants.DEFAULT_NONE)) {
                return UriComponents.UriTemplateVariables.SKIP_VALUE;
            }
            return null;
        }
    }
}
