package org.springframework.data.web;

import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponents;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/MethodParameterAwarePagedResourcesAssembler.class */
class MethodParameterAwarePagedResourcesAssembler<T> extends PagedResourcesAssembler<T> {
    private final MethodParameter parameter;

    public MethodParameterAwarePagedResourcesAssembler(MethodParameter parameter, HateoasPageableHandlerMethodArgumentResolver resolver, UriComponents baseUri) {
        super(resolver, baseUri);
        Assert.notNull(parameter, "Method parameter must not be null!");
        this.parameter = parameter;
    }

    @Override // org.springframework.data.web.PagedResourcesAssembler
    protected MethodParameter getMethodParameter() {
        return this.parameter;
    }
}
