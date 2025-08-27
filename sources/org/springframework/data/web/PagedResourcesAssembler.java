package org.springframework.data.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.core.EmbeddedWrapper;
import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.util.Assert;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/PagedResourcesAssembler.class */
public class PagedResourcesAssembler<T> implements ResourceAssembler<Page<T>, PagedResources<Resource<T>>> {
    private final HateoasPageableHandlerMethodArgumentResolver pageableResolver;
    private final UriComponents baseUri;
    private final EmbeddedWrappers wrappers = new EmbeddedWrappers(false);
    private boolean forceFirstAndLastRels = false;

    public PagedResourcesAssembler(HateoasPageableHandlerMethodArgumentResolver resolver, UriComponents baseUri) {
        this.pageableResolver = resolver == null ? new HateoasPageableHandlerMethodArgumentResolver() : resolver;
        this.baseUri = baseUri;
    }

    public void setForceFirstAndLastRels(boolean forceFirstAndLastRels) {
        this.forceFirstAndLastRels = forceFirstAndLastRels;
    }

    @Override // org.springframework.hateoas.ResourceAssembler
    public PagedResources<Resource<T>> toResource(Page<T> page) {
        return (PagedResources<Resource<T>>) toResource(page, new SimplePagedResourceAssembler());
    }

    public PagedResources<Resource<T>> toResource(Page<T> page, Link link) {
        return (PagedResources<Resource<T>>) toResource(page, new SimplePagedResourceAssembler(), link);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <R extends ResourceSupport> PagedResources<R> toResource(Page<T> page, ResourceAssembler<T, R> resourceAssembler) {
        return createResource(page, resourceAssembler, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <R extends ResourceSupport> PagedResources<R> toResource(Page<T> page, ResourceAssembler<T, R> resourceAssembler, Link link) {
        Assert.notNull(link, "Link must not be null!");
        return createResource(page, resourceAssembler, link);
    }

    public PagedResources<?> toEmptyResource(Page<?> page, Class<?> type, Link link) {
        Assert.notNull(page, "Page must not be null!");
        Assert.isTrue(!page.hasContent(), "Page must not have any content!");
        Assert.notNull(type, "Type must not be null!");
        PagedResources.PageMetadata metadata = asPageMetadata(page);
        EmbeddedWrapper wrapper = this.wrappers.emptyCollectionOf(type);
        List<EmbeddedWrapper> embedded = Collections.singletonList(wrapper);
        return addPaginationLinks(new PagedResources<>(embedded, metadata, new Link[0]), page, link);
    }

    @Deprecated
    public Link appendPaginationParameterTemplates(Link link) {
        Assert.notNull(link, "Link must not be null!");
        return createLink(new UriTemplate(link.getHref()), null, link.getRel());
    }

    protected <R extends ResourceSupport, S> PagedResources<R> createPagedResource(List<R> resources, PagedResources.PageMetadata metadata, Page<S> page) {
        Assert.notNull(resources, "Content resources must not be null!");
        Assert.notNull(metadata, "PageMetadata must not be null!");
        Assert.notNull(page, "Page must not be null!");
        return new PagedResources<>(resources, metadata, new Link[0]);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <S, R extends ResourceSupport> PagedResources<R> createResource(Page<S> page, ResourceAssembler<S, R> resourceAssembler, Link link) {
        Assert.notNull(page, "Page must not be null!");
        Assert.notNull(resourceAssembler, "ResourceAssembler must not be null!");
        ArrayList arrayList = new ArrayList(page.getNumberOfElements());
        Iterator it = page.iterator();
        while (it.hasNext()) {
            arrayList.add(resourceAssembler.toResource(it.next()));
        }
        PagedResources<R> resource = createPagedResource(arrayList, asPageMetadata(page), page);
        return addPaginationLinks(resource, page, link);
    }

    private <R> PagedResources<R> addPaginationLinks(PagedResources<R> resources, Page<?> page, Link link) {
        UriTemplate base = getUriTemplate(link);
        boolean isNavigable = page.hasPrevious() || page.hasNext();
        if (isNavigable || this.forceFirstAndLastRels) {
            resources.add(createLink(base, new PageRequest(0, page.getSize(), page.getSort()), Link.REL_FIRST));
        }
        if (page.hasPrevious()) {
            resources.add(createLink(base, page.previousPageable(), Link.REL_PREVIOUS));
        }
        Pageable current = new PageRequest(page.getNumber(), page.getSize(), page.getSort());
        resources.add(link == null ? createLink(base, current, Link.REL_SELF) : link.withSelfRel());
        if (page.hasNext()) {
            resources.add(createLink(base, page.nextPageable(), Link.REL_NEXT));
        }
        if (isNavigable || this.forceFirstAndLastRels) {
            int lastIndex = page.getTotalPages() == 0 ? 0 : page.getTotalPages() - 1;
            resources.add(createLink(base, new PageRequest(lastIndex, page.getSize(), page.getSort()), Link.REL_LAST));
        }
        return resources;
    }

    private UriTemplate getUriTemplate(Link baseLink) {
        String string;
        if (baseLink != null) {
            string = baseLink.getHref();
        } else {
            string = this.baseUri == null ? ServletUriComponentsBuilder.fromCurrentRequest().build().toString() : this.baseUri.toString();
        }
        String href = string;
        return new UriTemplate(href);
    }

    private Link createLink(UriTemplate base, Pageable pageable, String rel) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(base.expand(new Object[0]));
        this.pageableResolver.enhance(builder, getMethodParameter(), pageable);
        return new Link(new UriTemplate(builder.build().toString()), rel);
    }

    protected MethodParameter getMethodParameter() {
        return null;
    }

    private PagedResources.PageMetadata asPageMetadata(Page<?> page) {
        Assert.notNull(page, "Page must not be null!");
        int number = this.pageableResolver.isOneIndexedParameters() ? page.getNumber() + 1 : page.getNumber();
        return new PagedResources.PageMetadata(page.getSize(), number, page.getTotalElements(), page.getTotalPages());
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/PagedResourcesAssembler$SimplePagedResourceAssembler.class */
    private static class SimplePagedResourceAssembler<T> implements ResourceAssembler<T, Resource<T>> {
        private SimplePagedResourceAssembler() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // org.springframework.hateoas.ResourceAssembler
        public /* bridge */ /* synthetic */ ResourceSupport toResource(Object obj) {
            return toResource((SimplePagedResourceAssembler<T>) obj);
        }

        @Override // org.springframework.hateoas.ResourceAssembler
        public Resource<T> toResource(T entity) {
            return new Resource<>(entity, new Link[0]);
        }
    }
}
