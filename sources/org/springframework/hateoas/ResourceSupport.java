package org.springframework.hateoas;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import org.apache.naming.EjbRef;
import org.springframework.util.Assert;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/ResourceSupport.class */
public class ResourceSupport implements Identifiable<Link> {
    private final List<Link> links = new ArrayList();

    @Override // org.springframework.hateoas.Identifiable
    @JsonIgnore
    public Link getId() {
        return getLink(Link.REL_SELF);
    }

    public void add(Link link) {
        Assert.notNull(link, "Link must not be null!");
        this.links.add(link);
    }

    public void add(Iterable<Link> links) {
        Assert.notNull(links, "Given links must not be null!");
        for (Link candidate : links) {
            add(candidate);
        }
    }

    public void add(Link... links) {
        Assert.notNull(links, "Given links must not be null!");
        add(Arrays.asList(links));
    }

    public boolean hasLinks() {
        return !this.links.isEmpty();
    }

    public boolean hasLink(String rel) {
        return getLink(rel) != null;
    }

    @JsonProperty("links")
    @XmlElement(name = EjbRef.LINK, namespace = Link.ATOM_NAMESPACE)
    public List<Link> getLinks() {
        return this.links;
    }

    public void removeLinks() {
        this.links.clear();
    }

    public Link getLink(String rel) {
        for (Link link : this.links) {
            if (link.getRel().equals(rel)) {
                return link;
            }
        }
        return null;
    }

    public String toString() {
        return String.format("links: %s", this.links.toString());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        ResourceSupport that = (ResourceSupport) obj;
        return this.links.equals(that.links);
    }

    public int hashCode() {
        return this.links.hashCode();
    }
}
