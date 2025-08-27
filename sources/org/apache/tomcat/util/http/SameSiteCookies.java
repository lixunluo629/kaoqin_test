package org.apache.tomcat.util.http;

import org.apache.tomcat.util.res.StringManager;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/http/SameSiteCookies.class */
public enum SameSiteCookies {
    NONE("None"),
    LAX("Lax"),
    STRICT("Strict");

    private static final StringManager sm = StringManager.getManager((Class<?>) SameSiteCookies.class);
    private final String value;

    SameSiteCookies(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static SameSiteCookies fromString(String value) {
        SameSiteCookies[] arr$ = values();
        for (SameSiteCookies sameSiteCookies : arr$) {
            if (sameSiteCookies.getValue().equalsIgnoreCase(value)) {
                return sameSiteCookies;
            }
        }
        throw new IllegalStateException(sm.getString("cookies.invalidSameSiteCookies", value));
    }
}
