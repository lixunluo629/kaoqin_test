package org.bouncycastle.i18n;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.TimeZone;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/i18n/ErrorBundle.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/i18n/ErrorBundle.class */
public class ErrorBundle extends MessageBundle {
    public static final String SUMMARY_ENTRY = "summary";
    public static final String DETAIL_ENTRY = "details";

    public ErrorBundle(String str, String str2) throws NullPointerException {
        super(str, str2);
    }

    public ErrorBundle(String str, String str2, String str3) throws UnsupportedEncodingException, NullPointerException {
        super(str, str2, str3);
    }

    public ErrorBundle(String str, String str2, Object[] objArr) throws NullPointerException {
        super(str, str2, objArr);
    }

    public ErrorBundle(String str, String str2, String str3, Object[] objArr) throws UnsupportedEncodingException, NullPointerException {
        super(str, str2, str3, objArr);
    }

    public String getSummary(Locale locale, TimeZone timeZone) throws MissingEntryException {
        return getEntry(SUMMARY_ENTRY, locale, timeZone);
    }

    public String getSummary(Locale locale) throws MissingEntryException {
        return getEntry(SUMMARY_ENTRY, locale, TimeZone.getDefault());
    }

    public String getDetail(Locale locale, TimeZone timeZone) throws MissingEntryException {
        return getEntry(DETAIL_ENTRY, locale, timeZone);
    }

    public String getDetail(Locale locale) throws MissingEntryException {
        return getEntry(DETAIL_ENTRY, locale, TimeZone.getDefault());
    }
}
