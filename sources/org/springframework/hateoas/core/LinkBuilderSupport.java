package org.springframework.hateoas.core;

import java.net.URI;
import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/core/LinkBuilderSupport.class */
public abstract class LinkBuilderSupport<T extends LinkBuilder> implements LinkBuilder {
    private final UriComponents uriComponents;

    protected abstract T getThis();

    protected abstract T createNewInstance(UriComponentsBuilder uriComponentsBuilder);

    public LinkBuilderSupport(UriComponentsBuilder builder) {
        Assert.notNull(builder, "UriComponentsBuilder must not be null!");
        this.uriComponents = builder.build();
    }

    public LinkBuilderSupport(UriComponents uriComponents) {
        Assert.notNull(uriComponents, "UriComponents must not be null!");
        this.uriComponents = uriComponents;
    }

    @Override // org.springframework.hateoas.LinkBuilder
    public T slash(Object obj) {
        if (obj == null) {
            return (T) getThis();
        }
        if (obj instanceof Identifiable) {
            return (T) slash((Identifiable<?>) obj);
        }
        String string = obj.toString();
        if (string.endsWith("#")) {
            string = string.substring(0, string.length() - 1);
        }
        if (!StringUtils.hasText(string)) {
            return (T) getThis();
        }
        return (T) slash(UriComponentsBuilder.fromUriString(string).build(), false);
    }

    protected T slash(UriComponents uriComponents, boolean z) throws IllegalArgumentException {
        String uriString = this.uriComponents.toUriString();
        UriComponentsBuilder uriComponentsBuilderFromUri = uriString.isEmpty() ? UriComponentsBuilder.fromUri(this.uriComponents.toUri()) : UriComponentsBuilder.fromUriString(uriString);
        for (String str : uriComponents.getPathSegments()) {
            String[] strArr = new String[1];
            strArr[0] = z ? str : EncodingUtils.encodePath(str);
            uriComponentsBuilderFromUri.pathSegment(strArr);
        }
        String fragment = uriComponents.getFragment();
        if (StringUtils.hasText(fragment)) {
            uriComponentsBuilderFromUri.fragment(z ? fragment : EncodingUtils.encodeFragment(fragment));
        }
        return (T) createNewInstance(uriComponentsBuilderFromUri.query(uriComponents.getQuery()));
    }

    @Override // org.springframework.hateoas.LinkBuilder
    public T slash(Identifiable<?> identifiable) {
        if (identifiable == null) {
            return (T) getThis();
        }
        return (T) slash(identifiable.getId());
    }

    @Override // org.springframework.hateoas.LinkBuilder
    public URI toUri() {
        return this.uriComponents.encode().toUri().normalize();
    }

    @Override // org.springframework.hateoas.LinkBuilder
    public Link withRel(String rel) {
        return new Link(toString(), rel);
    }

    @Override // org.springframework.hateoas.LinkBuilder
    public Link withSelfRel() {
        return withRel(Link.REL_SELF);
    }

    public String toString() {
        return this.uriComponents.toUriString();
    }
}
