package org.springframework.hateoas.alps;

import java.util.Locale;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/alps/Format.class */
public enum Format {
    TEXT,
    HTML,
    ASCIIDOC;

    @Override // java.lang.Enum
    public String toString() {
        return name().toLowerCase(Locale.US);
    }
}
