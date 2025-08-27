package org.apache.commons.httpclient.auth;

import org.apache.commons.httpclient.util.LangUtils;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/auth/AuthScope.class */
public class AuthScope {
    public static final int ANY_PORT = -1;
    private String scheme;
    private String realm;
    private String host;
    private int port;
    public static final String ANY_HOST = null;
    public static final String ANY_REALM = null;
    public static final String ANY_SCHEME = null;
    public static final AuthScope ANY = new AuthScope(ANY_HOST, -1, ANY_REALM, ANY_SCHEME);

    public AuthScope(String host, int port, String realm, String scheme) {
        this.scheme = null;
        this.realm = null;
        this.host = null;
        this.port = -1;
        this.host = host == null ? ANY_HOST : host.toLowerCase();
        this.port = port < 0 ? -1 : port;
        this.realm = realm == null ? ANY_REALM : realm;
        this.scheme = scheme == null ? ANY_SCHEME : scheme.toUpperCase();
    }

    public AuthScope(String host, int port, String realm) {
        this(host, port, realm, ANY_SCHEME);
    }

    public AuthScope(String host, int port) {
        this(host, port, ANY_REALM, ANY_SCHEME);
    }

    public AuthScope(AuthScope authscope) {
        this.scheme = null;
        this.realm = null;
        this.host = null;
        this.port = -1;
        if (authscope == null) {
            throw new IllegalArgumentException("Scope may not be null");
        }
        this.host = authscope.getHost();
        this.port = authscope.getPort();
        this.realm = authscope.getRealm();
        this.scheme = authscope.getScheme();
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public String getRealm() {
        return this.realm;
    }

    public String getScheme() {
        return this.scheme;
    }

    private static boolean paramsEqual(String p1, String p2) {
        if (p1 == null) {
            return p1 == p2;
        }
        return p1.equals(p2);
    }

    private static boolean paramsEqual(int p1, int p2) {
        return p1 == p2;
    }

    public int match(AuthScope that) {
        int factor = 0;
        if (paramsEqual(this.scheme, that.scheme)) {
            factor = 0 + 1;
        } else if (this.scheme != ANY_SCHEME && that.scheme != ANY_SCHEME) {
            return -1;
        }
        if (paramsEqual(this.realm, that.realm)) {
            factor += 2;
        } else if (this.realm != ANY_REALM && that.realm != ANY_REALM) {
            return -1;
        }
        if (paramsEqual(this.port, that.port)) {
            factor += 4;
        } else if (this.port != -1 && that.port != -1) {
            return -1;
        }
        if (paramsEqual(this.host, that.host)) {
            factor += 8;
        } else if (this.host != ANY_HOST && that.host != ANY_HOST) {
            return -1;
        }
        return factor;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof AuthScope)) {
            return super.equals(o);
        }
        AuthScope that = (AuthScope) o;
        return paramsEqual(this.host, that.host) && paramsEqual(this.port, that.port) && paramsEqual(this.realm, that.realm) && paramsEqual(this.scheme, that.scheme);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        if (this.scheme != null) {
            buffer.append(this.scheme.toUpperCase());
            buffer.append(' ');
        }
        if (this.realm != null) {
            buffer.append('\'');
            buffer.append(this.realm);
            buffer.append('\'');
        } else {
            buffer.append("<any realm>");
        }
        if (this.host != null) {
            buffer.append('@');
            buffer.append(this.host);
            if (this.port >= 0) {
                buffer.append(':');
                buffer.append(this.port);
            }
        }
        return buffer.toString();
    }

    public int hashCode() {
        int hash = LangUtils.hashCode(17, this.host);
        return LangUtils.hashCode(LangUtils.hashCode(LangUtils.hashCode(hash, this.port), this.realm), this.scheme);
    }
}
