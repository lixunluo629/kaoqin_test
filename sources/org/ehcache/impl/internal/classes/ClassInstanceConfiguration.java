package org.ehcache.impl.internal.classes;

import java.util.Arrays;
import java.util.List;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/classes/ClassInstanceConfiguration.class */
public class ClassInstanceConfiguration<T> {
    private final Class<? extends T> clazz;
    private final List<Object> arguments;
    private final T instance;

    public ClassInstanceConfiguration(Class<? extends T> clazz, Object... arguments) {
        this.clazz = clazz;
        this.arguments = Arrays.asList((Object[]) arguments.clone());
        this.instance = null;
    }

    public ClassInstanceConfiguration(T t) {
        this.instance = t;
        this.clazz = (Class<? extends T>) t.getClass();
        this.arguments = null;
    }

    public Class<? extends T> getClazz() {
        return this.clazz;
    }

    public Object[] getArguments() {
        return this.arguments.toArray();
    }

    public T getInstance() {
        return this.instance;
    }
}
