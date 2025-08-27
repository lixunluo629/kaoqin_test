package org.springframework.context.i18n;

import java.util.Locale;
import java.util.TimeZone;
import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.core.NamedThreadLocal;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/i18n/LocaleContextHolder.class */
public abstract class LocaleContextHolder {
    private static final ThreadLocal<LocaleContext> localeContextHolder = new NamedThreadLocal("LocaleContext");
    private static final ThreadLocal<LocaleContext> inheritableLocaleContextHolder = new NamedInheritableThreadLocal("LocaleContext");
    private static Locale defaultLocale;
    private static TimeZone defaultTimeZone;

    public static void resetLocaleContext() {
        localeContextHolder.remove();
        inheritableLocaleContextHolder.remove();
    }

    public static void setLocaleContext(LocaleContext localeContext) {
        setLocaleContext(localeContext, false);
    }

    public static void setLocaleContext(LocaleContext localeContext, boolean inheritable) {
        if (localeContext == null) {
            resetLocaleContext();
        } else if (inheritable) {
            inheritableLocaleContextHolder.set(localeContext);
            localeContextHolder.remove();
        } else {
            localeContextHolder.set(localeContext);
            inheritableLocaleContextHolder.remove();
        }
    }

    public static LocaleContext getLocaleContext() {
        LocaleContext localeContext = localeContextHolder.get();
        if (localeContext == null) {
            localeContext = inheritableLocaleContextHolder.get();
        }
        return localeContext;
    }

    public static void setLocale(Locale locale) {
        setLocale(locale, false);
    }

    public static void setLocale(Locale locale, boolean inheritable) {
        LocaleContext localeContext;
        LocaleContext localeContext2 = getLocaleContext();
        TimeZone timeZone = localeContext2 instanceof TimeZoneAwareLocaleContext ? ((TimeZoneAwareLocaleContext) localeContext2).getTimeZone() : null;
        if (timeZone != null) {
            localeContext = new SimpleTimeZoneAwareLocaleContext(locale, timeZone);
        } else if (locale != null) {
            localeContext = new SimpleLocaleContext(locale);
        } else {
            localeContext = null;
        }
        setLocaleContext(localeContext, inheritable);
    }

    public static void setDefaultLocale(Locale locale) {
        defaultLocale = locale;
    }

    public static Locale getLocale() {
        Locale locale;
        LocaleContext localeContext = getLocaleContext();
        if (localeContext == null || (locale = localeContext.getLocale()) == null) {
            return defaultLocale != null ? defaultLocale : Locale.getDefault();
        }
        return locale;
    }

    public static void setTimeZone(TimeZone timeZone) {
        setTimeZone(timeZone, false);
    }

    public static void setTimeZone(TimeZone timeZone, boolean inheritable) {
        LocaleContext localeContext;
        LocaleContext localeContext2 = getLocaleContext();
        Locale locale = localeContext2 != null ? localeContext2.getLocale() : null;
        if (timeZone != null) {
            localeContext = new SimpleTimeZoneAwareLocaleContext(locale, timeZone);
        } else if (locale != null) {
            localeContext = new SimpleLocaleContext(locale);
        } else {
            localeContext = null;
        }
        setLocaleContext(localeContext, inheritable);
    }

    public static void setDefaultTimeZone(TimeZone timeZone) {
        defaultTimeZone = timeZone;
    }

    public static TimeZone getTimeZone() {
        TimeZone timeZone;
        LocaleContext localeContext = getLocaleContext();
        if (!(localeContext instanceof TimeZoneAwareLocaleContext) || (timeZone = ((TimeZoneAwareLocaleContext) localeContext).getTimeZone()) == null) {
            return defaultTimeZone != null ? defaultTimeZone : TimeZone.getDefault();
        }
        return timeZone;
    }
}
