package org.springframework.hateoas;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.util.Arrays;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.util.Assert;

@XmlRootElement
/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/Resource.class */
public class Resource<T> extends ResourceSupport {
    private final T content;

    Resource() {
        this.content = null;
    }

    public Resource(T content, Link... links) {
        this(content, Arrays.asList(links));
    }

    public Resource(T content, Iterable<Link> links) {
        Assert.notNull(content, "Content must not be null!");
        Assert.isTrue(!(content instanceof Collection), "Content must not be a collection! Use Resources instead!");
        this.content = content;
        add(links);
    }

    @JsonUnwrapped
    @XmlAnyElement
    public T getContent() {
        return this.content;
    }

    @Override // org.springframework.hateoas.ResourceSupport
    public String toString() {
        return String.format("Resource { content: %s, %s }", getContent(), super.toString());
    }

    @Override // org.springframework.hateoas.ResourceSupport
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        Resource<?> that = (Resource) obj;
        boolean contentEqual = this.content == null ? that.content == null : this.content.equals(that.content);
        if (contentEqual) {
            return super.equals(obj);
        }
        return false;
    }

    @Override // org.springframework.hateoas.ResourceSupport
    public int hashCode() {
        int result = super.hashCode();
        return result + (this.content == null ? 0 : 17 * this.content.hashCode());
    }
}
