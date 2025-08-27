package org.springframework.plugin.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.plugin.core.Plugin;
import org.springframework.util.comparator.InvertibleComparator;

/* loaded from: spring-plugin-core-1.2.0.RELEASE.jar:org/springframework/plugin/core/OrderAwarePluginRegistry.class */
public class OrderAwarePluginRegistry<T extends Plugin<S>, S> extends SimplePluginRegistry<T, S> {
    private static final Comparator<Object> DEFAULT_COMPARATOR = new AnnotationAwareOrderComparator();
    private static final Comparator<Object> DEFAULT_REVERSE_COMPARATOR = new InvertibleComparator(DEFAULT_COMPARATOR, false);
    private Comparator<? super T> comparator;

    protected OrderAwarePluginRegistry(List<? extends T> plugins, Comparator<? super T> comparator) {
        super(plugins);
        this.comparator = comparator == null ? DEFAULT_COMPARATOR : comparator;
    }

    public static <S, T extends Plugin<S>> OrderAwarePluginRegistry<T, S> create() {
        return create(Collections.emptyList());
    }

    public static <S, T extends Plugin<S>> OrderAwarePluginRegistry<T, S> create(Comparator<? super T> comparator) {
        return create(Collections.emptyList(), comparator);
    }

    public static <S, T extends Plugin<S>> OrderAwarePluginRegistry<T, S> create(List<? extends T> plugins) {
        return create(plugins, DEFAULT_COMPARATOR);
    }

    public static <S, T extends Plugin<S>> OrderAwarePluginRegistry<T, S> createReverse(List<? extends T> plugins) {
        return create(plugins, DEFAULT_REVERSE_COMPARATOR);
    }

    public static <S, T extends Plugin<S>> OrderAwarePluginRegistry<T, S> create(List<? extends T> plugins, Comparator<? super T> comparator) {
        return new OrderAwarePluginRegistry<>(plugins, comparator);
    }

    @Override // org.springframework.plugin.core.PluginRegistrySupport
    protected List<T> initialize(List<T> plugins) {
        List<T> result = super.initialize(plugins);
        Collections.sort(result, this.comparator);
        return result;
    }

    public OrderAwarePluginRegistry<T, S> reverse() {
        ArrayList<T> copy = new ArrayList<>(getPlugins());
        return create(copy, new InvertibleComparator(this.comparator, false));
    }
}
