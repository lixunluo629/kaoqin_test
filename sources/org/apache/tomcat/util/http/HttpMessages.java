package org.apache.tomcat.util.http;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.tomcat.util.res.StringManager;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/http/HttpMessages.class */
public class HttpMessages {
    private static final Map<Locale, HttpMessages> instances = new ConcurrentHashMap();
    private static final HttpMessages DEFAULT = new HttpMessages(StringManager.getManager("org.apache.tomcat.util.http.res", Locale.getDefault()));
    private final StringManager sm;
    private String st_200 = null;
    private String st_302 = null;
    private String st_400 = null;
    private String st_404 = null;
    private String st_500 = null;

    private HttpMessages(StringManager sm) {
        this.sm = sm;
    }

    public String getMessage(int status) {
        switch (status) {
            case 200:
                if (this.st_200 == null) {
                    this.st_200 = this.sm.getString("sc.200");
                }
                return this.st_200;
            case 302:
                if (this.st_302 == null) {
                    this.st_302 = this.sm.getString("sc.302");
                }
                return this.st_302;
            case 400:
                if (this.st_400 == null) {
                    this.st_400 = this.sm.getString("sc.400");
                }
                return this.st_400;
            case 404:
                if (this.st_404 == null) {
                    this.st_404 = this.sm.getString("sc.404");
                }
                return this.st_404;
            case 500:
                if (this.st_500 == null) {
                    this.st_500 = this.sm.getString("sc.500");
                }
                return this.st_500;
            default:
                return this.sm.getString("sc." + status);
        }
    }

    public static HttpMessages getInstance(Locale locale) {
        HttpMessages result = instances.get(locale);
        if (result == null) {
            StringManager sm = StringManager.getManager("org.apache.tomcat.util.http.res", locale);
            if (Locale.getDefault().equals(sm.getLocale())) {
                result = DEFAULT;
            } else {
                result = new HttpMessages(sm);
            }
            instances.put(locale, result);
        }
        return result;
    }

    public static boolean isSafeInHttpHeader(String msg) {
        if (msg == null) {
            return true;
        }
        int len = msg.length();
        for (int i = 0; i < len; i++) {
            char c = msg.charAt(i);
            if ((' ' > c || c > '~') && ((128 > c || c > 255) && c != '\t')) {
                return false;
            }
        }
        return true;
    }
}
