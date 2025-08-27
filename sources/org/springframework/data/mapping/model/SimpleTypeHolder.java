package org.springframework.data.mapping.model;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/SimpleTypeHolder.class */
public class SimpleTypeHolder {
    private static final Set<Class<?>> DEFAULTS = new HashSet();
    private final Set<Class<?>> simpleTypes;

    static {
        DEFAULTS.add(Boolean.TYPE);
        DEFAULTS.add(boolean[].class);
        DEFAULTS.add(Long.TYPE);
        DEFAULTS.add(long[].class);
        DEFAULTS.add(Short.TYPE);
        DEFAULTS.add(short[].class);
        DEFAULTS.add(Integer.TYPE);
        DEFAULTS.add(int[].class);
        DEFAULTS.add(Byte.TYPE);
        DEFAULTS.add(byte[].class);
        DEFAULTS.add(Float.TYPE);
        DEFAULTS.add(float[].class);
        DEFAULTS.add(Double.TYPE);
        DEFAULTS.add(double[].class);
        DEFAULTS.add(Character.TYPE);
        DEFAULTS.add(char[].class);
        DEFAULTS.add(Boolean.class);
        DEFAULTS.add(Long.class);
        DEFAULTS.add(Short.class);
        DEFAULTS.add(Integer.class);
        DEFAULTS.add(Byte.class);
        DEFAULTS.add(Float.class);
        DEFAULTS.add(Double.class);
        DEFAULTS.add(Character.class);
        DEFAULTS.add(String.class);
        DEFAULTS.add(Date.class);
        DEFAULTS.add(Locale.class);
        DEFAULTS.add(Class.class);
        DEFAULTS.add(Enum.class);
    }

    public SimpleTypeHolder() {
        this((Set<? extends Class<?>>) Collections.EMPTY_SET, true);
    }

    public SimpleTypeHolder(Set<? extends Class<?>> customSimpleTypes, boolean registerDefaults) {
        Assert.notNull(customSimpleTypes, "CustomSimpleTypes must not be null!");
        this.simpleTypes = new CopyOnWriteArraySet(customSimpleTypes);
        if (registerDefaults) {
            this.simpleTypes.addAll(DEFAULTS);
        }
    }

    public SimpleTypeHolder(Set<? extends Class<?>> customSimpleTypes, SimpleTypeHolder source) {
        Assert.notNull(customSimpleTypes, "CustomSimpleTypes must not be null!");
        Assert.notNull(source, "SourceTypeHolder must not be null!");
        this.simpleTypes = new CopyOnWriteArraySet(customSimpleTypes);
        this.simpleTypes.addAll(source.simpleTypes);
    }

    public boolean isSimpleType(Class<?> type) {
        Assert.notNull(type, "Type must not be null!");
        if (Object.class.equals(type) || this.simpleTypes.contains(type)) {
            return true;
        }
        String typeName = type.getName();
        if (typeName.startsWith("java.lang") || typeName.startsWith("java.time")) {
            return true;
        }
        for (Class<?> clazz : this.simpleTypes) {
            if (clazz.isAssignableFrom(type)) {
                this.simpleTypes.add(type);
                return true;
            }
        }
        return false;
    }
}
