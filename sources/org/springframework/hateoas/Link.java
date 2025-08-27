package org.springframework.hateoas;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.apache.naming.EjbRef;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@JsonIgnoreProperties({"templated"})
@XmlType(name = EjbRef.LINK, namespace = Link.ATOM_NAMESPACE)
/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/Link.class */
public class Link implements Serializable {
    private static final long serialVersionUID = -9037755944661782121L;
    private static final String URI_PATTERN = "(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    public static final String ATOM_NAMESPACE = "http://www.w3.org/2005/Atom";
    public static final String REL_SELF = "self";
    public static final String REL_FIRST = "first";
    public static final String REL_PREVIOUS = "prev";
    public static final String REL_NEXT = "next";
    public static final String REL_LAST = "last";

    @XmlAttribute
    private String rel;

    @XmlAttribute
    private String href;

    @JsonIgnore
    @XmlTransient
    private UriTemplate template;

    public Link(String href) {
        this(href, REL_SELF);
    }

    public Link(String href, String rel) {
        this(new UriTemplate(href), rel);
    }

    public Link(UriTemplate template, String rel) {
        Assert.notNull(template, "UriTempalte must not be null!");
        Assert.hasText(rel, "Rel must not be null or empty!");
        this.template = template;
        this.href = template.toString();
        this.rel = rel;
    }

    protected Link() {
    }

    public String getHref() {
        return this.href;
    }

    public String getRel() {
        return this.rel;
    }

    public Link withRel(String rel) {
        return new Link(this.href, rel);
    }

    public Link withSelfRel() {
        return withRel(REL_SELF);
    }

    @JsonIgnore
    public List<String> getVariableNames() {
        return getUriTemplate().getVariableNames();
    }

    @JsonIgnore
    public List<TemplateVariable> getVariables() {
        return getUriTemplate().getVariables();
    }

    public boolean isTemplated() {
        return !getUriTemplate().getVariables().isEmpty();
    }

    public Link expand(Object... arguments) {
        return new Link(getUriTemplate().expand(arguments).toString(), getRel());
    }

    public Link expand(Map<String, ? extends Object> arguments) {
        return new Link(getUriTemplate().expand(arguments).toString(), getRel());
    }

    private UriTemplate getUriTemplate() {
        if (this.template == null) {
            this.template = new UriTemplate(this.href);
        }
        return this.template;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Link)) {
            return false;
        }
        Link that = (Link) obj;
        return this.href.equals(that.href) && this.rel.equals(that.rel);
    }

    public int hashCode() {
        int result = 17 + (31 * this.href.hashCode());
        return result + (31 * this.rel.hashCode());
    }

    public String toString() {
        return String.format("<%s>;rel=\"%s\"", this.href, this.rel);
    }

    public static Link valueOf(String element) {
        if (!StringUtils.hasText(element)) {
            return null;
        }
        Pattern uriAndAttributes = Pattern.compile("<(.*)>;(.*)");
        Matcher matcher = uriAndAttributes.matcher(element);
        if (matcher.find()) {
            Map<String, String> attributes = getAttributeMap(matcher.group(2));
            if (!attributes.containsKey("rel")) {
                throw new IllegalArgumentException("Link does not provide a rel attribute!");
            }
            return new Link(matcher.group(1), attributes.get("rel"));
        }
        throw new IllegalArgumentException(String.format("Given link header %s is not RFC5988 compliant!", element));
    }

    private static Map<String, String> getAttributeMap(String source) {
        if (!StringUtils.hasText(source)) {
            return Collections.emptyMap();
        }
        Map<String, String> attributes = new HashMap<>();
        Pattern keyAndValue = Pattern.compile("(\\w+)=\"(\\p{Lower}[\\p{Lower}\\p{Digit}\\.\\-]*|(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])\"");
        Matcher matcher = keyAndValue.matcher(source);
        while (matcher.find()) {
            attributes.put(matcher.group(1), matcher.group(2));
        }
        return attributes;
    }
}
