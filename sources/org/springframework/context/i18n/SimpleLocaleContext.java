package org.springframework.context.i18n;

import java.util.Locale;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/i18n/SimpleLocaleContext.class */
public class SimpleLocaleContext implements LocaleContext {
    private final Locale locale;

    public SimpleLocaleContext(Locale locale) {
        this.locale = locale;
    }

    @Override // org.springframework.context.i18n.LocaleContext
    public Locale getLocale() {
        return this.locale;
    }

    public String toString() {
        return this.locale != null ? this.locale.toString() : "-";
    }
}
