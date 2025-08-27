package org.springframework.hateoas;

import org.springframework.http.MediaType;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.util.Assert;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/LinkDiscoverers.class */
public class LinkDiscoverers {
    private final PluginRegistry<LinkDiscoverer, MediaType> discoverers;

    public LinkDiscoverers(PluginRegistry<LinkDiscoverer, MediaType> discoverers) {
        Assert.notNull(discoverers, "Registry of LinkDiscoverer must not be null!");
        this.discoverers = discoverers;
    }

    public LinkDiscoverer getLinkDiscovererFor(MediaType mediaType) {
        return (LinkDiscoverer) this.discoverers.getPluginFor(mediaType);
    }

    public LinkDiscoverer getLinkDiscovererFor(String mediaType) {
        return getLinkDiscovererFor(MediaType.valueOf(mediaType));
    }
}
