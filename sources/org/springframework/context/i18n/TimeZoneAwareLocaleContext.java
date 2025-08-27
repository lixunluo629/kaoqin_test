package org.springframework.context.i18n;

import java.util.TimeZone;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/i18n/TimeZoneAwareLocaleContext.class */
public interface TimeZoneAwareLocaleContext extends LocaleContext {
    TimeZone getTimeZone();
}
