package org.springframework.core.style;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/style/ToStringStyler.class */
public interface ToStringStyler {
    void styleStart(StringBuilder sb, Object obj);

    void styleEnd(StringBuilder sb, Object obj);

    void styleField(StringBuilder sb, String str, Object obj);

    void styleValue(StringBuilder sb, Object obj);

    void styleFieldSeparator(StringBuilder sb);
}
