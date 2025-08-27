package io.netty.handler.codec.http.cookie;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/cookie/CookieHeaderNames.class */
public final class CookieHeaderNames {
    public static final String PATH = "Path";
    public static final String EXPIRES = "Expires";
    public static final String MAX_AGE = "Max-Age";
    public static final String DOMAIN = "Domain";
    public static final String SECURE = "Secure";
    public static final String HTTPONLY = "HTTPOnly";
    public static final String SAMESITE = "SameSite";

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/cookie/CookieHeaderNames$SameSite.class */
    public enum SameSite {
        Lax,
        Strict,
        None;

        static SameSite of(String name) {
            if (name != null) {
                for (SameSite each : (SameSite[]) SameSite.class.getEnumConstants()) {
                    if (each.name().equalsIgnoreCase(name)) {
                        return each;
                    }
                }
                return null;
            }
            return null;
        }
    }

    private CookieHeaderNames() {
    }
}
