package org.springframework.data.web;

import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MethodLinkBuilderFactory;
import org.springframework.hateoas.core.MethodParameters;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/PagedResourcesAssemblerArgumentResolver.class */
public class PagedResourcesAssemblerArgumentResolver implements HandlerMethodArgumentResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) PagedResourcesAssemblerArgumentResolver.class);
    private static final String SUPERFLOUS_QUALIFIER = "Found qualified {} parameter, but a unique unqualified {} parameter. Using that one, but you might wanna check your controller method configuration!";
    private static final String PARAMETER_AMBIGUITY = "Discovered muliple parameters of type Pageable but no qualifier annotations to disambiguate!";
    private final HateoasPageableHandlerMethodArgumentResolver resolver;
    private final MethodLinkBuilderFactory<?> linkBuilderFactory;

    public PagedResourcesAssemblerArgumentResolver(HateoasPageableHandlerMethodArgumentResolver resolver, MethodLinkBuilderFactory<?> linkBuilderFactory) {
        this.resolver = resolver;
        this.linkBuilderFactory = linkBuilderFactory == null ? new ControllerLinkBuilderFactory() : linkBuilderFactory;
    }

    @Override // org.springframework.web.method.support.HandlerMethodArgumentResolver
    public boolean supportsParameter(MethodParameter parameter) {
        return PagedResourcesAssembler.class.equals(parameter.getParameterType());
    }

    @Override // org.springframework.web.method.support.HandlerMethodArgumentResolver
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        UriComponents fromUriString = resolveBaseUri(parameter);
        MethodParameter pageableParameter = findMatchingPageableParameter(parameter);
        if (pageableParameter != null) {
            return new MethodParameterAwarePagedResourcesAssembler(pageableParameter, this.resolver, fromUriString);
        }
        return new PagedResourcesAssembler(this.resolver, fromUriString);
    }

    private UriComponents resolveBaseUri(MethodParameter parameter) {
        try {
            Link linkToMethod = this.linkBuilderFactory.linkTo(parameter.getDeclaringClass(), parameter.getMethod(), new Object[0]).withSelfRel();
            return UriComponentsBuilder.fromUriString(linkToMethod.getHref()).build();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static final MethodParameter findMatchingPageableParameter(MethodParameter parameter) {
        MethodParameters parameters = new MethodParameters(parameter.getMethod());
        List<MethodParameter> pageableParameters = parameters.getParametersOfType(Pageable.class);
        Qualifier assemblerQualifier = (Qualifier) parameter.getParameterAnnotation(Qualifier.class);
        if (pageableParameters.isEmpty()) {
            return null;
        }
        if (pageableParameters.size() == 1) {
            MethodParameter pageableParameter = pageableParameters.get(0);
            if (returnIfQualifiersMatch(pageableParameter, assemblerQualifier) == null) {
                LOGGER.info(SUPERFLOUS_QUALIFIER, PagedResourcesAssembler.class.getSimpleName(), Pageable.class.getName());
            }
            return pageableParameter;
        }
        if (assemblerQualifier == null) {
            throw new IllegalStateException(PARAMETER_AMBIGUITY);
        }
        Iterator<MethodParameter> it = pageableParameters.iterator();
        while (it.hasNext()) {
            MethodParameter matchingParameter = returnIfQualifiersMatch(it.next(), assemblerQualifier);
            if (matchingParameter != null) {
                return matchingParameter;
            }
        }
        throw new IllegalStateException(PARAMETER_AMBIGUITY);
    }

    private static MethodParameter returnIfQualifiersMatch(MethodParameter pageableParameter, Qualifier assemblerQualifier) {
        if (assemblerQualifier == null) {
            return pageableParameter;
        }
        Qualifier pageableParameterQualifier = (Qualifier) pageableParameter.getParameterAnnotation(Qualifier.class);
        if (pageableParameterQualifier != null && pageableParameterQualifier.value().equals(assemblerQualifier.value())) {
            return pageableParameter;
        }
        return null;
    }
}
