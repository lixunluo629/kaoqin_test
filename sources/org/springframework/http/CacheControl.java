package org.springframework.http;

import io.netty.handler.codec.http.HttpHeaders;
import java.util.concurrent.TimeUnit;
import org.springframework.util.StringUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/CacheControl.class */
public class CacheControl {
    private long maxAge = -1;
    private boolean noCache = false;
    private boolean noStore = false;
    private boolean mustRevalidate = false;
    private boolean noTransform = false;
    private boolean cachePublic = false;
    private boolean cachePrivate = false;
    private boolean proxyRevalidate = false;
    private long staleWhileRevalidate = -1;
    private long staleIfError = -1;
    private long sMaxAge = -1;

    protected CacheControl() {
    }

    public static CacheControl empty() {
        return new CacheControl();
    }

    public static CacheControl maxAge(long maxAge, TimeUnit unit) {
        CacheControl cc = new CacheControl();
        cc.maxAge = unit.toSeconds(maxAge);
        return cc;
    }

    public static CacheControl noCache() {
        CacheControl cc = new CacheControl();
        cc.noCache = true;
        return cc;
    }

    public static CacheControl noStore() {
        CacheControl cc = new CacheControl();
        cc.noStore = true;
        return cc;
    }

    public CacheControl mustRevalidate() {
        this.mustRevalidate = true;
        return this;
    }

    public CacheControl noTransform() {
        this.noTransform = true;
        return this;
    }

    public CacheControl cachePublic() {
        this.cachePublic = true;
        return this;
    }

    public CacheControl cachePrivate() {
        this.cachePrivate = true;
        return this;
    }

    public CacheControl proxyRevalidate() {
        this.proxyRevalidate = true;
        return this;
    }

    public CacheControl sMaxAge(long sMaxAge, TimeUnit unit) {
        this.sMaxAge = unit.toSeconds(sMaxAge);
        return this;
    }

    public CacheControl staleWhileRevalidate(long staleWhileRevalidate, TimeUnit unit) {
        this.staleWhileRevalidate = unit.toSeconds(staleWhileRevalidate);
        return this;
    }

    public CacheControl staleIfError(long staleIfError, TimeUnit unit) {
        this.staleIfError = unit.toSeconds(staleIfError);
        return this;
    }

    public String getHeaderValue() {
        StringBuilder headerValue = new StringBuilder();
        if (this.maxAge != -1) {
            appendDirective(headerValue, "max-age=" + this.maxAge);
        }
        if (this.noCache) {
            appendDirective(headerValue, "no-cache");
        }
        if (this.noStore) {
            appendDirective(headerValue, HttpHeaders.Values.NO_STORE);
        }
        if (this.mustRevalidate) {
            appendDirective(headerValue, "must-revalidate");
        }
        if (this.noTransform) {
            appendDirective(headerValue, "no-transform");
        }
        if (this.cachePublic) {
            appendDirective(headerValue, "public");
        }
        if (this.cachePrivate) {
            appendDirective(headerValue, "private");
        }
        if (this.proxyRevalidate) {
            appendDirective(headerValue, "proxy-revalidate");
        }
        if (this.sMaxAge != -1) {
            appendDirective(headerValue, "s-maxage=" + this.sMaxAge);
        }
        if (this.staleIfError != -1) {
            appendDirective(headerValue, "stale-if-error=" + this.staleIfError);
        }
        if (this.staleWhileRevalidate != -1) {
            appendDirective(headerValue, "stale-while-revalidate=" + this.staleWhileRevalidate);
        }
        String valueString = headerValue.toString();
        if (StringUtils.hasText(valueString)) {
            return valueString;
        }
        return null;
    }

    private void appendDirective(StringBuilder builder, String value) {
        if (builder.length() > 0) {
            builder.append(", ");
        }
        builder.append(value);
    }
}
