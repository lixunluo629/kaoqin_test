package org.bouncycastle.i18n;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/i18n/LocaleString.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/i18n/LocaleString.class */
public class LocaleString extends LocalizedMessage {
    public LocaleString(String str, String str2) {
        super(str, str2);
    }

    public LocaleString(String str, String str2, String str3) throws UnsupportedEncodingException, NullPointerException {
        super(str, str2, str3);
    }

    public String getLocaleString(Locale locale) {
        return getEntry(null, locale, null);
    }
}
