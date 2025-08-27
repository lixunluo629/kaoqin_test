package org.springframework.boot.bind;

import org.springframework.core.env.PropertySource;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/PropertyOrigin.class */
public class PropertyOrigin {
    private final PropertySource<?> source;
    private final String name;

    PropertyOrigin(PropertySource<?> source, String name) {
        this.name = name;
        this.source = source;
    }

    public PropertySource<?> getSource() {
        return this.source;
    }

    public String getName() {
        return this.name;
    }
}
