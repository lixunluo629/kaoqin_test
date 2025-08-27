package org.springframework.hateoas;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.util.Assert;
import org.springframework.web.util.TagUtils;

@XmlRootElement(name = "pagedEntities")
/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/PagedResources.class */
public class PagedResources<T> extends Resources<T> {
    public static PagedResources<?> NO_PAGE = new PagedResources<>();
    private PageMetadata metadata;

    protected PagedResources() {
        this(new ArrayList(), (PageMetadata) null, new Link[0]);
    }

    public PagedResources(Collection<T> content, PageMetadata metadata, Link... links) {
        this(content, metadata, Arrays.asList(links));
    }

    public PagedResources(Collection<T> content, PageMetadata metadata, Iterable<Link> links) {
        super(content, links);
        this.metadata = metadata;
    }

    @JsonProperty(TagUtils.SCOPE_PAGE)
    public PageMetadata getMetadata() {
        return this.metadata;
    }

    public static <T extends Resource<S>, S> PagedResources<T> wrap(Iterable<S> content, PageMetadata metadata) {
        Assert.notNull(content);
        ArrayList<T> resources = new ArrayList<>();
        for (S element : content) {
            resources.add(new Resource(element, new Link[0]));
        }
        return new PagedResources<>(resources, metadata, new Link[0]);
    }

    @JsonIgnore
    public Link getNextLink() {
        return getLink(Link.REL_NEXT);
    }

    @JsonIgnore
    public Link getPreviousLink() {
        return getLink(Link.REL_PREVIOUS);
    }

    @Override // org.springframework.hateoas.Resources, org.springframework.hateoas.ResourceSupport
    public String toString() {
        return String.format("PagedResource { content: %s, metadata: %s, links: %s }", getContent(), this.metadata, getLinks());
    }

    @Override // org.springframework.hateoas.Resources, org.springframework.hateoas.ResourceSupport
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !getClass().equals(obj.getClass())) {
            return false;
        }
        PagedResources<?> that = (PagedResources) obj;
        boolean metadataEquals = this.metadata == null ? that.metadata == null : this.metadata.equals(that.metadata);
        if (metadataEquals) {
            return super.equals(obj);
        }
        return false;
    }

    @Override // org.springframework.hateoas.Resources, org.springframework.hateoas.ResourceSupport
    public int hashCode() {
        int result = super.hashCode();
        return result + (this.metadata == null ? 0 : 31 * this.metadata.hashCode());
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/PagedResources$PageMetadata.class */
    public static class PageMetadata {

        @JsonProperty
        @XmlAttribute
        private long size;

        @JsonProperty
        @XmlAttribute
        private long totalElements;

        @JsonProperty
        @XmlAttribute
        private long totalPages;

        @JsonProperty
        @XmlAttribute
        private long number;

        protected PageMetadata() {
        }

        public PageMetadata(long size, long number, long totalElements, long totalPages) {
            Assert.isTrue(size > -1, "Size must not be negative!");
            Assert.isTrue(number > -1, "Number must not be negative!");
            Assert.isTrue(totalElements > -1, "Total elements must not be negative!");
            Assert.isTrue(totalPages > -1, "Total pages must not be negative!");
            this.size = size;
            this.number = number;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
        }

        public PageMetadata(long size, long number, long totalElements) {
            this(size, number, totalElements, size == 0 ? 0L : (long) Math.ceil(totalElements / size));
        }

        public long getSize() {
            return this.size;
        }

        public long getTotalElements() {
            return this.totalElements;
        }

        public long getTotalPages() {
            return this.totalPages;
        }

        public long getNumber() {
            return this.number;
        }

        public String toString() {
            return String.format("Metadata { number: %d, total pages: %d, total elements: %d, size: %d }", Long.valueOf(this.number), Long.valueOf(this.totalPages), Long.valueOf(this.totalElements), Long.valueOf(this.size));
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !obj.getClass().equals(getClass())) {
                return false;
            }
            PageMetadata that = (PageMetadata) obj;
            return this.number == that.number && this.size == that.size && this.totalElements == that.totalElements && this.totalPages == that.totalPages;
        }

        public int hashCode() {
            int result = 17 + (31 * ((int) (this.number ^ (this.number >>> 32))));
            return result + (31 * ((int) (this.size ^ (this.size >>> 32)))) + (31 * ((int) (this.totalElements ^ (this.totalElements >>> 32)))) + (31 * ((int) (this.totalPages ^ (this.totalPages >>> 32))));
        }
    }
}
