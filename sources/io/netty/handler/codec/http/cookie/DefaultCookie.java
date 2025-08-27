package io.netty.handler.codec.http.cookie;

import io.netty.handler.codec.http.cookie.CookieHeaderNames;
import io.netty.util.internal.ObjectUtil;
import org.apache.commons.httpclient.cookie.Cookie2;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/cookie/DefaultCookie.class */
public class DefaultCookie implements Cookie {
    private final String name;
    private String value;
    private boolean wrap;
    private String domain;
    private String path;
    private long maxAge = Long.MIN_VALUE;
    private boolean secure;
    private boolean httpOnly;
    private CookieHeaderNames.SameSite sameSite;

    public DefaultCookie(String name, String value) {
        String name2 = ((String) ObjectUtil.checkNotNull(name, "name")).trim();
        if (name2.isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }
        this.name = name2;
        setValue(value);
    }

    @Override // io.netty.handler.codec.http.cookie.Cookie
    public String name() {
        return this.name;
    }

    @Override // io.netty.handler.codec.http.cookie.Cookie
    public String value() {
        return this.value;
    }

    @Override // io.netty.handler.codec.http.cookie.Cookie
    public void setValue(String value) {
        this.value = (String) ObjectUtil.checkNotNull(value, "value");
    }

    @Override // io.netty.handler.codec.http.cookie.Cookie
    public boolean wrap() {
        return this.wrap;
    }

    @Override // io.netty.handler.codec.http.cookie.Cookie
    public void setWrap(boolean wrap) {
        this.wrap = wrap;
    }

    @Override // io.netty.handler.codec.http.cookie.Cookie
    public String domain() {
        return this.domain;
    }

    @Override // io.netty.handler.codec.http.cookie.Cookie
    public void setDomain(String domain) {
        this.domain = CookieUtil.validateAttributeValue(Cookie2.DOMAIN, domain);
    }

    @Override // io.netty.handler.codec.http.cookie.Cookie
    public String path() {
        return this.path;
    }

    @Override // io.netty.handler.codec.http.cookie.Cookie
    public void setPath(String path) {
        this.path = CookieUtil.validateAttributeValue(Cookie2.PATH, path);
    }

    @Override // io.netty.handler.codec.http.cookie.Cookie
    public long maxAge() {
        return this.maxAge;
    }

    @Override // io.netty.handler.codec.http.cookie.Cookie
    public void setMaxAge(long maxAge) {
        this.maxAge = maxAge;
    }

    @Override // io.netty.handler.codec.http.cookie.Cookie
    public boolean isSecure() {
        return this.secure;
    }

    @Override // io.netty.handler.codec.http.cookie.Cookie
    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    @Override // io.netty.handler.codec.http.cookie.Cookie
    public boolean isHttpOnly() {
        return this.httpOnly;
    }

    @Override // io.netty.handler.codec.http.cookie.Cookie
    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public CookieHeaderNames.SameSite sameSite() {
        return this.sameSite;
    }

    public void setSameSite(CookieHeaderNames.SameSite sameSite) {
        this.sameSite = sameSite;
    }

    public int hashCode() {
        return name().hashCode();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cookie)) {
            return false;
        }
        Cookie that = (Cookie) o;
        if (!name().equals(that.name())) {
            return false;
        }
        if (path() == null) {
            if (that.path() != null) {
                return false;
            }
        } else if (that.path() == null || !path().equals(that.path())) {
            return false;
        }
        if (domain() == null) {
            if (that.domain() != null) {
                return false;
            }
            return true;
        }
        return domain().equalsIgnoreCase(that.domain());
    }

    @Override // java.lang.Comparable
    public int compareTo(Cookie c) {
        int v = name().compareTo(c.name());
        if (v != 0) {
            return v;
        }
        if (path() == null) {
            if (c.path() != null) {
                return -1;
            }
        } else {
            if (c.path() == null) {
                return 1;
            }
            int v2 = path().compareTo(c.path());
            if (v2 != 0) {
                return v2;
            }
        }
        if (domain() == null) {
            if (c.domain() != null) {
                return -1;
            }
            return 0;
        }
        if (c.domain() == null) {
            return 1;
        }
        return domain().compareToIgnoreCase(c.domain());
    }

    @Deprecated
    protected String validateValue(String name, String value) {
        return CookieUtil.validateAttributeValue(name, value);
    }

    public String toString() {
        StringBuilder buf = CookieUtil.stringBuilder().append(name()).append('=').append(value());
        if (domain() != null) {
            buf.append(", domain=").append(domain());
        }
        if (path() != null) {
            buf.append(", path=").append(path());
        }
        if (maxAge() >= 0) {
            buf.append(", maxAge=").append(maxAge()).append('s');
        }
        if (isSecure()) {
            buf.append(", secure");
        }
        if (isHttpOnly()) {
            buf.append(", HTTPOnly");
        }
        if (sameSite() != null) {
            buf.append(", SameSite=").append(sameSite());
        }
        return buf.toString();
    }
}
