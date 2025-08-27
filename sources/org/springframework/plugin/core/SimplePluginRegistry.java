package org.springframework.plugin.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.plugin.core.Plugin;

/* loaded from: spring-plugin-core-1.2.0.RELEASE.jar:org/springframework/plugin/core/SimplePluginRegistry.class */
public class SimplePluginRegistry<T extends Plugin<S>, S> extends PluginRegistrySupport<T, S> {
    protected SimplePluginRegistry(List<? extends T> plugins) {
        super(plugins);
    }

    public static <S, T extends Plugin<S>> SimplePluginRegistry<T, S> create() {
        return create(Collections.emptyList());
    }

    public static <S, T extends Plugin<S>> SimplePluginRegistry<T, S> create(List<? extends T> plugins) {
        return new SimplePluginRegistry<>(plugins);
    }

    @Override // org.springframework.plugin.core.PluginRegistrySupport, org.springframework.plugin.core.PluginRegistry
    public List<T> getPlugins() {
        return Collections.unmodifiableList(super.getPlugins());
    }

    @Override // org.springframework.plugin.core.PluginRegistry
    public T getPluginFor(S delimiter) {
        for (T plugin : super.getPlugins()) {
            if (plugin != null && plugin.supports(delimiter)) {
                return plugin;
            }
        }
        return null;
    }

    @Override // org.springframework.plugin.core.PluginRegistry
    public List<T> getPluginsFor(S delimiter) {
        List<T> result = new ArrayList<>();
        for (T plugin : super.getPlugins()) {
            if (plugin != null && plugin.supports(delimiter)) {
                result.add(plugin);
            }
        }
        return result;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: E extends java.lang.Exception */
    @Override // org.springframework.plugin.core.PluginRegistry
    public <E extends Exception> T getPluginFor(S s, E e) throws Exception {
        T t = (T) getPluginFor(s);
        if (null == t) {
            throw e;
        }
        return t;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: E extends java.lang.Exception */
    @Override // org.springframework.plugin.core.PluginRegistry
    public <E extends Exception> List<T> getPluginsFor(S delimiter, E ex) throws Exception {
        List<T> result = getPluginsFor(delimiter);
        if (result.isEmpty()) {
            throw ex;
        }
        return result;
    }

    @Override // org.springframework.plugin.core.PluginRegistry
    public T getPluginFor(S s, T t) {
        T t2 = (T) getPluginFor(s);
        return null == t2 ? t : t2;
    }

    @Override // org.springframework.plugin.core.PluginRegistry
    public List<T> getPluginsFor(S delimiter, List<? extends T> plugins) {
        List<T> candidates = getPluginsFor(delimiter);
        return candidates.isEmpty() ? new ArrayList(plugins) : candidates;
    }

    @Override // org.springframework.plugin.core.PluginRegistry
    public int countPlugins() {
        return super.getPlugins().size();
    }

    @Override // org.springframework.plugin.core.PluginRegistry
    public boolean contains(T plugin) {
        return super.getPlugins().contains(plugin);
    }

    @Override // org.springframework.plugin.core.PluginRegistry
    public boolean hasPluginFor(S delimiter) {
        return null != getPluginFor(delimiter);
    }
}
