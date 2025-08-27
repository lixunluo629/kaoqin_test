package org.springframework.context.support;

import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.util.StringValueResolver;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/support/EmbeddedValueResolutionSupport.class */
public class EmbeddedValueResolutionSupport implements EmbeddedValueResolverAware {
    private StringValueResolver embeddedValueResolver;

    @Override // org.springframework.context.EmbeddedValueResolverAware
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.embeddedValueResolver = resolver;
    }

    protected String resolveEmbeddedValue(String value) {
        return this.embeddedValueResolver != null ? this.embeddedValueResolver.resolveStringValue(value) : value;
    }
}
