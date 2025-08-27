package org.springframework.hateoas;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.util.Assert;

@XmlRootElement(name = "entities")
/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/Resources.class */
public class Resources<T> extends ResourceSupport implements Iterable<T> {
    private final Collection<T> content;

    protected Resources() {
        this(new ArrayList(), new Link[0]);
    }

    public Resources(Iterable<T> content, Link... links) {
        this(content, Arrays.asList(links));
    }

    public Resources(Iterable<T> content, Iterable<Link> links) {
        Assert.notNull(content);
        this.content = new ArrayList();
        for (T element : content) {
            this.content.add(element);
        }
        add(links);
    }

    public static <T extends Resource<S>, S> Resources<T> wrap(Iterable<S> content) {
        Assert.notNull(content);
        ArrayList<T> resources = new ArrayList<>();
        for (S element : content) {
            resources.add(new Resource(element, new Link[0]));
        }
        return new Resources<>(resources, new Link[0]);
    }

    @JsonProperty("content")
    @XmlAnyElement
    @XmlElementWrapper
    public Collection<T> getContent() {
        return Collections.unmodifiableCollection(this.content);
    }

    @Override // java.lang.Iterable
    public Iterator<T> iterator() {
        return this.content.iterator();
    }

    @Override // org.springframework.hateoas.ResourceSupport
    public String toString() {
        return String.format("Resources { content: %s, %s }", getContent(), super.toString());
    }

    @Override // org.springframework.hateoas.ResourceSupport
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        Resources<?> that = (Resources) obj;
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
