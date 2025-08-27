package org.springframework.data.web.querydsl;

import com.querydsl.core.types.Predicate;
import java.util.Arrays;
import java.util.Map;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.QuerydslBindingsFactory;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.querydsl.binding.QuerydslPredicateBuilder;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/querydsl/QuerydslPredicateArgumentResolver.class */
public class QuerydslPredicateArgumentResolver implements HandlerMethodArgumentResolver {
    private final QuerydslBindingsFactory bindingsFactory;
    private final QuerydslPredicateBuilder predicateBuilder;

    public QuerydslPredicateArgumentResolver(QuerydslBindingsFactory factory, ConversionService conversionService) {
        this.bindingsFactory = factory;
        this.predicateBuilder = new QuerydslPredicateBuilder(conversionService == null ? new DefaultConversionService() : conversionService, factory.getEntityPathResolver());
    }

    @Override // org.springframework.web.method.support.HandlerMethodArgumentResolver
    public boolean supportsParameter(MethodParameter parameter) {
        if (Predicate.class.equals(parameter.getParameterType())) {
            return true;
        }
        if (parameter.hasParameterAnnotation(QuerydslPredicate.class)) {
            throw new IllegalArgumentException(String.format("Parameter at position %s must be of type Predicate but was %s.", Integer.valueOf(parameter.getParameterIndex()), parameter.getParameterType()));
        }
        return false;
    }

    @Override // org.springframework.web.method.support.HandlerMethodArgumentResolver
    public Predicate resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        for (Map.Entry<String, String[]> entry : webRequest.getParameterMap().entrySet()) {
            parameters.put(entry.getKey(), Arrays.asList(entry.getValue()));
        }
        QuerydslPredicate annotation = (QuerydslPredicate) parameter.getParameterAnnotation(QuerydslPredicate.class);
        TypeInformation<?> domainType = extractTypeInfo(parameter).getActualType();
        Class<? extends QuerydslBinderCustomizer<?>> customizer = annotation == null ? null : annotation.bindings();
        QuerydslBindings bindings = this.bindingsFactory.createBindingsFor(customizer, domainType);
        return this.predicateBuilder.getPredicate(domainType, parameters, bindings);
    }

    static TypeInformation<?> extractTypeInfo(MethodParameter parameter) {
        QuerydslPredicate annotation = (QuerydslPredicate) parameter.getParameterAnnotation(QuerydslPredicate.class);
        if (annotation != null && !Object.class.equals(annotation.root())) {
            return ClassTypeInformation.from(annotation.root());
        }
        return detectDomainType(ClassTypeInformation.fromReturnTypeOf(parameter.getMethod()));
    }

    private static TypeInformation<?> detectDomainType(TypeInformation<?> source) {
        if (source.getTypeArguments().isEmpty()) {
            return source;
        }
        TypeInformation<?> actualType = source.getActualType();
        if (source != actualType) {
            return detectDomainType(actualType);
        }
        if (source instanceof Iterable) {
            return source;
        }
        return detectDomainType(source.getComponentType());
    }
}
