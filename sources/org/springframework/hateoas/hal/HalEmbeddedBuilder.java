package org.springframework.hateoas.hal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.core.EmbeddedWrapper;
import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/hal/HalEmbeddedBuilder.class */
class HalEmbeddedBuilder {
    private static final String DEFAULT_REL = "content";
    private static final String INVALID_EMBEDDED_WRAPPER = "Embedded wrapper %s returned null for both the static rel and the rel target type! Make sure one of the two returns a non-null value!";
    private final Map<String, Object> embeddeds = new HashMap();
    private final RelProvider provider;
    private final CurieProvider curieProvider;
    private final EmbeddedWrappers wrappers;

    public HalEmbeddedBuilder(RelProvider provider, CurieProvider curieProvider, boolean preferCollectionRels) {
        Assert.notNull(provider, "Relprovider must not be null!");
        this.provider = provider;
        this.curieProvider = curieProvider;
        this.wrappers = new EmbeddedWrappers(preferCollectionRels);
    }

    public void add(Object source) {
        EmbeddedWrapper wrapper = this.wrappers.wrap(source);
        if (wrapper == null) {
            return;
        }
        String collectionRel = getDefaultedRelFor(wrapper, true);
        String collectionOrItemRel = collectionRel;
        if (!this.embeddeds.containsKey(collectionRel)) {
            collectionOrItemRel = getDefaultedRelFor(wrapper, wrapper.isCollectionValue());
        }
        Object currentValue = this.embeddeds.get(collectionOrItemRel);
        Object value = wrapper.getValue();
        if (currentValue == null && !wrapper.isCollectionValue()) {
            this.embeddeds.put(collectionOrItemRel, value);
            return;
        }
        List<Object> list = new ArrayList<>();
        list.addAll(asCollection(currentValue));
        list.addAll(asCollection(wrapper.getValue()));
        this.embeddeds.remove(collectionOrItemRel);
        this.embeddeds.put(collectionRel, list);
    }

    private Collection<Object> asCollection(Object source) {
        return source instanceof Collection ? (Collection) source : source == null ? Collections.emptySet() : Collections.singleton(source);
    }

    private String getDefaultedRelFor(EmbeddedWrapper wrapper, boolean forCollection) {
        String valueRel = wrapper.getRel();
        if (StringUtils.hasText(valueRel)) {
            return valueRel;
        }
        if (this.provider == null) {
            return DEFAULT_REL;
        }
        Class<?> type = wrapper.getRelTargetType();
        if (type == null) {
            throw new IllegalStateException(String.format(INVALID_EMBEDDED_WRAPPER, wrapper));
        }
        String rel = forCollection ? this.provider.getCollectionResourceRelFor(type) : this.provider.getItemResourceRelFor(type);
        if (this.curieProvider != null) {
            rel = this.curieProvider.getNamespacedRelFor(rel);
        }
        return rel == null ? DEFAULT_REL : rel;
    }

    public Map<String, Object> asMap() {
        return Collections.unmodifiableMap(this.embeddeds);
    }
}
