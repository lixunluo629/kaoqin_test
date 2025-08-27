package org.springframework.plugin.core.support;

import java.util.List;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.plugin.core.OrderAwarePluginRegistry;
import org.springframework.plugin.core.Plugin;
import org.springframework.plugin.core.PluginRegistry;

/* loaded from: spring-plugin-core-1.2.0.RELEASE.jar:org/springframework/plugin/core/support/PluginRegistryFactoryBean.class */
public class PluginRegistryFactoryBean<T extends Plugin<S>, S> extends AbstractTypeAwareSupport<T> implements FactoryBean<PluginRegistry<T, S>> {
    @Override // org.springframework.beans.factory.FactoryBean
    public OrderAwarePluginRegistry<T, S> getObject() {
        return OrderAwarePluginRegistry.create((List) getBeans());
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Class<?> getObjectType() {
        return OrderAwarePluginRegistry.class;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public boolean isSingleton() {
        return true;
    }
}
