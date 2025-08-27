package org.apache.commons.httpclient.cookie;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/cookie/CookiePolicy.class */
public abstract class CookiePolicy {
    private static Map SPECS = Collections.synchronizedMap(new HashMap());
    public static final String BROWSER_COMPATIBILITY = "compatibility";
    public static final String NETSCAPE = "netscape";
    public static final String RFC_2109 = "rfc2109";
    public static final String RFC_2965 = "rfc2965";
    public static final String IGNORE_COOKIES = "ignoreCookies";
    public static final String DEFAULT = "default";
    public static final int COMPATIBILITY = 0;
    public static final int NETSCAPE_DRAFT = 1;
    public static final int RFC2109 = 2;
    public static final int RFC2965 = 3;
    private static int defaultPolicy;
    protected static final Log LOG;
    static Class class$org$apache$commons$httpclient$cookie$RFC2109Spec;
    static Class class$org$apache$commons$httpclient$cookie$RFC2965Spec;
    static Class class$org$apache$commons$httpclient$cookie$CookieSpecBase;
    static Class class$org$apache$commons$httpclient$cookie$NetscapeDraftSpec;
    static Class class$org$apache$commons$httpclient$cookie$IgnoreCookiesSpec;
    static Class class$org$apache$commons$httpclient$cookie$CookiePolicy;

    static {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        Class clsClass$7;
        if (class$org$apache$commons$httpclient$cookie$RFC2109Spec == null) {
            clsClass$ = class$("org.apache.commons.httpclient.cookie.RFC2109Spec");
            class$org$apache$commons$httpclient$cookie$RFC2109Spec = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$cookie$RFC2109Spec;
        }
        registerCookieSpec("default", clsClass$);
        if (class$org$apache$commons$httpclient$cookie$RFC2109Spec == null) {
            clsClass$2 = class$("org.apache.commons.httpclient.cookie.RFC2109Spec");
            class$org$apache$commons$httpclient$cookie$RFC2109Spec = clsClass$2;
        } else {
            clsClass$2 = class$org$apache$commons$httpclient$cookie$RFC2109Spec;
        }
        registerCookieSpec(RFC_2109, clsClass$2);
        if (class$org$apache$commons$httpclient$cookie$RFC2965Spec == null) {
            clsClass$3 = class$("org.apache.commons.httpclient.cookie.RFC2965Spec");
            class$org$apache$commons$httpclient$cookie$RFC2965Spec = clsClass$3;
        } else {
            clsClass$3 = class$org$apache$commons$httpclient$cookie$RFC2965Spec;
        }
        registerCookieSpec(RFC_2965, clsClass$3);
        if (class$org$apache$commons$httpclient$cookie$CookieSpecBase == null) {
            clsClass$4 = class$("org.apache.commons.httpclient.cookie.CookieSpecBase");
            class$org$apache$commons$httpclient$cookie$CookieSpecBase = clsClass$4;
        } else {
            clsClass$4 = class$org$apache$commons$httpclient$cookie$CookieSpecBase;
        }
        registerCookieSpec(BROWSER_COMPATIBILITY, clsClass$4);
        if (class$org$apache$commons$httpclient$cookie$NetscapeDraftSpec == null) {
            clsClass$5 = class$("org.apache.commons.httpclient.cookie.NetscapeDraftSpec");
            class$org$apache$commons$httpclient$cookie$NetscapeDraftSpec = clsClass$5;
        } else {
            clsClass$5 = class$org$apache$commons$httpclient$cookie$NetscapeDraftSpec;
        }
        registerCookieSpec(NETSCAPE, clsClass$5);
        if (class$org$apache$commons$httpclient$cookie$IgnoreCookiesSpec == null) {
            clsClass$6 = class$("org.apache.commons.httpclient.cookie.IgnoreCookiesSpec");
            class$org$apache$commons$httpclient$cookie$IgnoreCookiesSpec = clsClass$6;
        } else {
            clsClass$6 = class$org$apache$commons$httpclient$cookie$IgnoreCookiesSpec;
        }
        registerCookieSpec(IGNORE_COOKIES, clsClass$6);
        defaultPolicy = 2;
        if (class$org$apache$commons$httpclient$cookie$CookiePolicy == null) {
            clsClass$7 = class$("org.apache.commons.httpclient.cookie.CookiePolicy");
            class$org$apache$commons$httpclient$cookie$CookiePolicy = clsClass$7;
        } else {
            clsClass$7 = class$org$apache$commons$httpclient$cookie$CookiePolicy;
        }
        LOG = LogFactory.getLog(clsClass$7);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public static void registerCookieSpec(String id, Class clazz) {
        if (id == null) {
            throw new IllegalArgumentException("Id may not be null");
        }
        if (clazz == null) {
            throw new IllegalArgumentException("Cookie spec class may not be null");
        }
        SPECS.put(id.toLowerCase(), clazz);
    }

    public static void unregisterCookieSpec(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id may not be null");
        }
        SPECS.remove(id.toLowerCase());
    }

    public static CookieSpec getCookieSpec(String id) throws IllegalStateException {
        if (id == null) {
            throw new IllegalArgumentException("Id may not be null");
        }
        Class clazz = (Class) SPECS.get(id.toLowerCase());
        if (clazz != null) {
            try {
                return (CookieSpec) clazz.newInstance();
            } catch (Exception e) {
                LOG.error(new StringBuffer().append("Error initializing cookie spec: ").append(id).toString(), e);
                throw new IllegalStateException(new StringBuffer().append(id).append(" cookie spec implemented by ").append(clazz.getName()).append(" could not be initialized").toString());
            }
        }
        throw new IllegalStateException(new StringBuffer().append("Unsupported cookie spec ").append(id).toString());
    }

    public static int getDefaultPolicy() {
        return defaultPolicy;
    }

    public static void setDefaultPolicy(int policy) {
        defaultPolicy = policy;
    }

    public static CookieSpec getSpecByPolicy(int policy) {
        switch (policy) {
            case 0:
                return new CookieSpecBase();
            case 1:
                return new NetscapeDraftSpec();
            case 2:
                return new RFC2109Spec();
            case 3:
                return new RFC2965Spec();
            default:
                return getDefaultSpec();
        }
    }

    public static CookieSpec getDefaultSpec() {
        try {
            return getCookieSpec("default");
        } catch (IllegalStateException e) {
            LOG.warn("Default cookie policy is not registered");
            return new RFC2109Spec();
        }
    }

    public static CookieSpec getSpecByVersion(int ver) {
        switch (ver) {
            case 0:
                return new NetscapeDraftSpec();
            case 1:
                return new RFC2109Spec();
            default:
                return getDefaultSpec();
        }
    }

    public static CookieSpec getCompatibilitySpec() {
        return getSpecByPolicy(0);
    }

    public static String[] getRegisteredCookieSpecs() {
        return (String[]) SPECS.keySet().toArray(new String[SPECS.size()]);
    }
}
