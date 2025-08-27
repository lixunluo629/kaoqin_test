package org.springframework.hateoas.hal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.hateoas.IanaRels;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.UriTemplate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/hal/DefaultCurieProvider.class */
public class DefaultCurieProvider implements CurieProvider {
    private final Map<String, UriTemplate> curies;
    private final String defaultCurie;

    public DefaultCurieProvider(String name, UriTemplate uriTemplate) {
        this(Collections.singletonMap(name, uriTemplate));
    }

    public DefaultCurieProvider(Map<String, UriTemplate> curies) {
        this(curies, (String) null);
    }

    public DefaultCurieProvider(Map<String, UriTemplate> curies, String defaultCurieName) {
        String next;
        Assert.notNull(curies, "Curies must not be null!");
        for (Map.Entry<String, UriTemplate> entry : curies.entrySet()) {
            String name = entry.getKey();
            UriTemplate template = entry.getValue();
            Assert.hasText(name, "Curie name must not be null or empty!");
            Assert.notNull(template, "UriTemplate must not be null!");
            Assert.isTrue(template.getVariableNames().size() == 1, String.format("Expected a single template variable in the UriTemplate %s!", template.toString()));
        }
        if (StringUtils.hasText(defaultCurieName)) {
            next = defaultCurieName;
        } else {
            next = curies.size() == 1 ? curies.keySet().iterator().next() : null;
        }
        this.defaultCurie = next;
        this.curies = Collections.unmodifiableMap(curies);
    }

    @Override // org.springframework.hateoas.hal.CurieProvider
    public Collection<? extends Object> getCurieInformation(Links links) {
        List<Curie> result = new ArrayList<>(this.curies.size());
        for (Map.Entry<String, UriTemplate> source : this.curies.entrySet()) {
            String name = source.getKey();
            UriTemplate template = source.getValue();
            result.add(new Curie(name, getCurieHref(name, template)));
        }
        return Collections.unmodifiableCollection(result);
    }

    @Override // org.springframework.hateoas.hal.CurieProvider
    public String getNamespacedRelFrom(Link link) {
        return getNamespacedRelFor(link.getRel());
    }

    @Override // org.springframework.hateoas.hal.CurieProvider
    public String getNamespacedRelFor(String rel) {
        boolean prefixingNeeded = (this.defaultCurie == null || IanaRels.isIanaRel(rel) || rel.contains(":")) ? false : true;
        return prefixingNeeded ? String.format("%s:%s", this.defaultCurie, rel) : rel;
    }

    protected String getCurieHref(String name, UriTemplate template) {
        if (template.toString().startsWith("http")) {
            return template.toString();
        }
        String applicationUri = ServletUriComponentsBuilder.fromCurrentServletMapping().build().expand(new Object[0]).toString();
        return applicationUri.concat(template.toString());
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/hal/DefaultCurieProvider$Curie.class */
    protected static class Curie extends Link {
        private static final long serialVersionUID = 1;
        private final String name;

        public Curie(String name, String href) {
            super(href, "curies");
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}
