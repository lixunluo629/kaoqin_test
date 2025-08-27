package org.springframework.hateoas.alps;

import java.util.Locale;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/alps/Type.class */
public enum Type {
    SEMANTIC,
    SAFE,
    IDEMPOTENT,
    UNSAFE;

    @Override // java.lang.Enum
    public String toString() {
        return name().toLowerCase(Locale.US);
    }
}
