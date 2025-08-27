package org.springframework.hateoas.core;

import org.springframework.hateoas.RelProvider;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.util.Assert;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/core/DelegatingRelProvider.class */
public class DelegatingRelProvider implements RelProvider {
    private final PluginRegistry<RelProvider, Class<?>> providers;

    public DelegatingRelProvider(PluginRegistry<RelProvider, Class<?>> providers) {
        Assert.notNull(providers, "RelProviders must not be null!");
        this.providers = providers;
    }

    @Override // org.springframework.hateoas.RelProvider
    public String getItemResourceRelFor(Class<?> type) {
        return ((RelProvider) this.providers.getPluginFor(type)).getItemResourceRelFor(type);
    }

    @Override // org.springframework.hateoas.RelProvider
    public String getCollectionResourceRelFor(Class<?> type) {
        return ((RelProvider) this.providers.getPluginFor(type)).getCollectionResourceRelFor(type);
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(Class<?> delimiter) {
        return this.providers.hasPluginFor(delimiter);
    }
}
