package org.springframework.plugin.metadata;

import org.springframework.plugin.core.Plugin;

/* loaded from: spring-plugin-metadata-1.2.0.RELEASE.jar:org/springframework/plugin/metadata/AbstractMetadataBasedPlugin.class */
public abstract class AbstractMetadataBasedPlugin implements Plugin<PluginMetadata>, MetadataProvider {
    private final PluginMetadata metadata;

    public AbstractMetadataBasedPlugin(String name, String version) {
        this.metadata = new SimplePluginMetadata(name, version);
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(PluginMetadata delimiter) {
        return getMetadata().equals(delimiter);
    }

    @Override // org.springframework.plugin.metadata.MetadataProvider
    public PluginMetadata getMetadata() {
        return this.metadata;
    }
}
