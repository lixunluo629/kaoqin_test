package org.springframework.hateoas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.util.StringUtils;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/Links.class */
public class Links implements Iterable<Link> {
    private static final Pattern LINK_HEADER_PATTERN = Pattern.compile("(<[^>]*>;rel=\"[^\"]*\")");
    static final Links NO_LINKS = new Links((List<Link>) Collections.emptyList());
    private final List<Link> links;

    public Links(List<Link> links) {
        this.links = links == null ? Collections.emptyList() : Collections.unmodifiableList(links);
    }

    public Links(Link... links) {
        this((List<Link>) Arrays.asList(links));
    }

    public Link getLink(String rel) {
        for (Link link : this.links) {
            if (link.getRel().equals(rel)) {
                return link;
            }
        }
        return null;
    }

    public List<Link> getLinks(String rel) {
        List<Link> result = new ArrayList<>();
        for (Link link : this.links) {
            if (link.getRel().endsWith(rel)) {
                result.add(link);
            }
        }
        return result;
    }

    public boolean hasLink(String rel) {
        return getLink(rel) != null;
    }

    public static Links valueOf(String source) {
        if (!StringUtils.hasText(source)) {
            return NO_LINKS;
        }
        Matcher matcher = LINK_HEADER_PATTERN.matcher(source);
        List<Link> links = new ArrayList<>();
        while (matcher.find()) {
            Link link = Link.valueOf(matcher.group());
            if (link != null) {
                links.add(link);
            }
        }
        return new Links(links);
    }

    public boolean isEmpty() {
        return this.links.isEmpty();
    }

    public String toString() {
        return StringUtils.collectionToCommaDelimitedString(this.links);
    }

    @Override // java.lang.Iterable
    public Iterator<Link> iterator() {
        return this.links.iterator();
    }

    public boolean equals(Object arg0) {
        if (!(arg0 instanceof Links)) {
            return false;
        }
        Links that = (Links) arg0;
        return this.links.equals(that.links);
    }

    public int hashCode() {
        int result = 17 + (31 * this.links.hashCode());
        return result;
    }
}
