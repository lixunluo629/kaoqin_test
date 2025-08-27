package org.springframework.hateoas.mvc;

import org.springframework.hateoas.core.LinkBuilderSupport;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/BasicLinkBuilder.class */
public class BasicLinkBuilder extends LinkBuilderSupport<BasicLinkBuilder> {
    private BasicLinkBuilder(UriComponentsBuilder builder) {
        super(builder);
    }

    public static BasicLinkBuilder linkToCurrentMapping() {
        return new BasicLinkBuilder(ServletUriComponentsBuilder.fromCurrentServletMapping());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.hateoas.core.LinkBuilderSupport
    public BasicLinkBuilder createNewInstance(UriComponentsBuilder builder) {
        return new BasicLinkBuilder(builder);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.hateoas.core.LinkBuilderSupport
    public BasicLinkBuilder getThis() {
        return this;
    }
}
