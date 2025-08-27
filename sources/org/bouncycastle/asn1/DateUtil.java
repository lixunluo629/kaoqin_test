package org.bouncycastle.asn1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/DateUtil.class */
class DateUtil {
    private static Long ZERO = longValueOf(0);
    private static final Map localeCache = new HashMap();
    static Locale EN_Locale = forEN();

    DateUtil() {
    }

    private static Locale forEN() {
        if ("en".equalsIgnoreCase(Locale.getDefault().getLanguage())) {
            return Locale.getDefault();
        }
        Locale[] availableLocales = Locale.getAvailableLocales();
        for (int i = 0; i != availableLocales.length; i++) {
            if ("en".equalsIgnoreCase(availableLocales[i].getLanguage())) {
                return availableLocales[i];
            }
        }
        return Locale.getDefault();
    }

    static Date epochAdjust(Date date) throws ParseException {
        Locale locale = Locale.getDefault();
        if (locale == null) {
            return date;
        }
        synchronized (localeCache) {
            Long lLongValueOf = (Long) localeCache.get(locale);
            if (lLongValueOf == null) {
                long time = new SimpleDateFormat("yyyyMMddHHmmssz").parse("19700101000000GMT+00:00").getTime();
                lLongValueOf = time == 0 ? ZERO : longValueOf(time);
                localeCache.put(locale, lLongValueOf);
            }
            if (lLongValueOf != ZERO) {
                return new Date(date.getTime() - lLongValueOf.longValue());
            }
            return date;
        }
    }

    private static Long longValueOf(long j) {
        return Long.valueOf(j);
    }
}
