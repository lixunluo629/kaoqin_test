package org.springframework.web.servlet.i18n;

import java.util.Locale;
import org.springframework.web.servlet.LocaleResolver;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/i18n/AbstractLocaleResolver.class */
public abstract class AbstractLocaleResolver implements LocaleResolver {
    private Locale defaultLocale;

    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    protected Locale getDefaultLocale() {
        return this.defaultLocale;
    }
}
