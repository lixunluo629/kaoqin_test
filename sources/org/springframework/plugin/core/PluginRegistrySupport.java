package org.springframework.plugin.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.plugin.core.Plugin;
import org.springframework.util.Assert;

/* loaded from: spring-plugin-core-1.2.0.RELEASE.jar:org/springframework/plugin/core/PluginRegistrySupport.class */
public abstract class PluginRegistrySupport<T extends Plugin<S>, S> implements PluginRegistry<T, S>, Iterable<T> {
    private List<T> plugins;
    private boolean initialized;

    public PluginRegistrySupport(List<? extends T> list) {
        Assert.notNull(list);
        this.plugins = list == null ? new ArrayList() : (List<T>) list;
        this.initialized = false;
    }

    @Override // org.springframework.plugin.core.PluginRegistry
    public List<T> getPlugins() {
        if (!this.initialized) {
            this.plugins = initialize(this.plugins);
            this.initialized = true;
        }
        return this.plugins;
    }

    protected synchronized List<T> initialize(List<T> plugins) {
        Assert.notNull(plugins);
        List<T> result = new ArrayList<>();
        for (T plugin : this.plugins) {
            if (plugin != null) {
                result.add(plugin);
            }
        }
        return result;
    }

    @Override // java.lang.Iterable
    public Iterator<T> iterator() {
        return getPlugins().iterator();
    }
}
