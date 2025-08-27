package net.dongliu.apk.parser.bean;

import java.util.Locale;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/bean/Locales.class */
public class Locales {
    public static final Locale any = new Locale("", "");

    public static int match(Locale locale, Locale targetLocale) {
        if (locale == null) {
            return -1;
        }
        if (locale.getLanguage().equals(targetLocale.getLanguage())) {
            if (locale.getCountry().equals(targetLocale.getCountry())) {
                return 3;
            }
            if (targetLocale.getCountry().isEmpty()) {
                return 2;
            }
            return 0;
        }
        if (targetLocale.getCountry().isEmpty() || targetLocale.getLanguage().isEmpty()) {
            return 1;
        }
        return 0;
    }
}
