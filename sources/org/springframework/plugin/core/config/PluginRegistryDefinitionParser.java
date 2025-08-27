package org.springframework.plugin.core.config;

/* loaded from: spring-plugin-core-1.2.0.RELEASE.jar:org/springframework/plugin/core/config/PluginRegistryDefinitionParser.class */
public class PluginRegistryDefinitionParser extends PluginListDefinitionParser {
    @Override // org.springframework.plugin.core.config.PluginListDefinitionParser
    protected String getPostProcessorName() {
        return "org.springframework.plugin.core.support.PluginRegistryFactoryBean";
    }
}
