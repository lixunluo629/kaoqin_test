package org.springframework.plugin.core.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/* loaded from: spring-plugin-core-1.2.0.RELEASE.jar:org/springframework/plugin/core/config/PluginNamespaceHandler.class */
public class PluginNamespaceHandler extends NamespaceHandlerSupport {
    @Override // org.springframework.beans.factory.xml.NamespaceHandler
    public void init() {
        registerBeanDefinitionParser("list", new PluginListDefinitionParser());
        registerBeanDefinitionParser("registry", new PluginRegistryDefinitionParser());
    }
}
