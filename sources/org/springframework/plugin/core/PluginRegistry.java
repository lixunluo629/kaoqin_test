package org.springframework.plugin.core;

import java.util.List;
import org.springframework.plugin.core.Plugin;

/* loaded from: spring-plugin-core-1.2.0.RELEASE.jar:org/springframework/plugin/core/PluginRegistry.class */
public interface PluginRegistry<T extends Plugin<S>, S> extends Iterable<T> {
    T getPluginFor(S s);

    List<T> getPluginsFor(S s);

    <E extends Exception> T getPluginFor(S s, E e) throws Exception;

    <E extends Exception> List<T> getPluginsFor(S s, E e) throws Exception;

    T getPluginFor(S s, T t);

    List<T> getPluginsFor(S s, List<? extends T> list);

    int countPlugins();

    boolean contains(T t);

    boolean hasPluginFor(S s);

    List<T> getPlugins();
}
