package org.springframework.context.i18n;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Locale;
import java.util.TimeZone;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/i18n/SimpleTimeZoneAwareLocaleContext.class */
public class SimpleTimeZoneAwareLocaleContext extends SimpleLocaleContext implements TimeZoneAwareLocaleContext {
    private final TimeZone timeZone;

    public SimpleTimeZoneAwareLocaleContext(Locale locale, TimeZone timeZone) {
        super(locale);
        this.timeZone = timeZone;
    }

    @Override // org.springframework.context.i18n.TimeZoneAwareLocaleContext
    public TimeZone getTimeZone() {
        return this.timeZone;
    }

    @Override // org.springframework.context.i18n.SimpleLocaleContext
    public String toString() {
        return super.toString() + SymbolConstants.SPACE_SYMBOL + (this.timeZone != null ? this.timeZone.toString() : "-");
    }
}
