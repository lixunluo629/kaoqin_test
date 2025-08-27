package org.springframework.hateoas.jaxrs;

import java.util.Map;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.hateoas.LinkBuilderFactory;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/jaxrs/JaxRsLinkBuilderFactory.class */
public class JaxRsLinkBuilderFactory implements LinkBuilderFactory<JaxRsLinkBuilder> {
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

    @Override // org.springframework.hateoas.LinkBuilderFactory
    public JaxRsLinkBuilder linkTo(Class<?> service) {
        return JaxRsLinkBuilder.linkTo(service);
    }

    @Override // org.springframework.hateoas.LinkBuilderFactory
    public JaxRsLinkBuilder linkTo(Class<?> service, Object... parameters) {
        return JaxRsLinkBuilder.linkTo(service, parameters);
    }

    @Override // org.springframework.hateoas.LinkBuilderFactory
    public JaxRsLinkBuilder linkTo(Class<?> service, Map<String, ?> parameters) {
        return JaxRsLinkBuilder.linkTo(service, parameters);
    }
}
