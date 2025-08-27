package org.springframework.hateoas.core;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.util.Assert;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/core/DelegatingEntityLinks.class */
public class DelegatingEntityLinks extends AbstractEntityLinks {
    private final PluginRegistry<EntityLinks, Class<?>> delegates;

    public DelegatingEntityLinks(PluginRegistry<EntityLinks, Class<?>> plugins) {
        Assert.notNull(plugins, "PluginRegistry must not be null!");
        this.delegates = plugins;
    }

    @Override // org.springframework.hateoas.EntityLinks
    public LinkBuilder linkFor(Class<?> type) {
        return getPluginFor(type).linkFor(type);
    }

    @Override // org.springframework.hateoas.EntityLinks
    public LinkBuilder linkFor(Class<?> type, Object... parameters) {
        return getPluginFor(type).linkFor(type, parameters);
    }

    @Override // org.springframework.hateoas.EntityLinks
    public Link linkToCollectionResource(Class<?> type) {
        return getPluginFor(type).linkToCollectionResource(type);
    }

    @Override // org.springframework.hateoas.EntityLinks
    public Link linkToSingleResource(Class<?> type, Object id) {
        return getPluginFor(type).linkToSingleResource(type, id);
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(Class<?> delimiter) {
        return this.delegates.hasPluginFor(delimiter);
    }

    private EntityLinks getPluginFor(Class<?> type) {
        EntityLinks plugin = (EntityLinks) this.delegates.getPluginFor(type);
        if (plugin == null) {
            throw new IllegalArgumentException(String.format("Cannot determine link for %s! No EntityLinks instance found supporting the domain type!", type.getName()));
        }
        return plugin;
    }
}
