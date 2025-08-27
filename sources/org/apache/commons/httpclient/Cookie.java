package org.apache.commons.httpclient;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.cookie.CookieSpec;
import org.apache.commons.httpclient.util.LangUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/Cookie.class */
public class Cookie extends NameValuePair implements Serializable, Comparator {
    private String cookieComment;
    private String cookieDomain;
    private Date cookieExpiryDate;
    private String cookiePath;
    private boolean isSecure;
    private boolean hasPathAttribute;
    private boolean hasDomainAttribute;
    private int cookieVersion;
    private static final Log LOG;
    static Class class$org$apache$commons$httpclient$Cookie;

    public Cookie() {
        this((String) null, "noname", (String) null, (String) null, (Date) null, false);
    }

    public Cookie(String domain, String name, String value) {
        this(domain, name, value, (String) null, (Date) null, false);
    }

    public Cookie(String domain, String name, String value, String path, Date expires, boolean secure) {
        super(name, value);
        this.hasPathAttribute = false;
        this.hasDomainAttribute = false;
        this.cookieVersion = 0;
        LOG.trace("enter Cookie(String, String, String, String, Date, boolean)");
        if (name == null) {
            throw new IllegalArgumentException("Cookie name may not be null");
        }
        if (name.trim().equals("")) {
            throw new IllegalArgumentException("Cookie name may not be blank");
        }
        setPath(path);
        setDomain(domain);
        setExpiryDate(expires);
        setSecure(secure);
    }

    public Cookie(String domain, String name, String value, String path, int maxAge, boolean secure) {
        this(domain, name, value, path, (Date) null, secure);
        if (maxAge < -1) {
            throw new IllegalArgumentException(new StringBuffer().append("Invalid max age:  ").append(Integer.toString(maxAge)).toString());
        }
        if (maxAge >= 0) {
            setExpiryDate(new Date(System.currentTimeMillis() + (maxAge * 1000)));
        }
    }

    public String getComment() {
        return this.cookieComment;
    }

    public void setComment(String comment) {
        this.cookieComment = comment;
    }

    public Date getExpiryDate() {
        return this.cookieExpiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.cookieExpiryDate = expiryDate;
    }

    public boolean isPersistent() {
        return null != this.cookieExpiryDate;
    }

    public String getDomain() {
        return this.cookieDomain;
    }

    public void setDomain(String domain) {
        if (domain != null) {
            int ndx = domain.indexOf(":");
            if (ndx != -1) {
                domain = domain.substring(0, ndx);
            }
            this.cookieDomain = domain.toLowerCase();
        }
    }

    public String getPath() {
        return this.cookiePath;
    }

    public void setPath(String path) {
        this.cookiePath = path;
    }

    public boolean getSecure() {
        return this.isSecure;
    }

    public void setSecure(boolean secure) {
        this.isSecure = secure;
    }

    public int getVersion() {
        return this.cookieVersion;
    }

    public void setVersion(int version) {
        this.cookieVersion = version;
    }

    public boolean isExpired() {
        return this.cookieExpiryDate != null && this.cookieExpiryDate.getTime() <= System.currentTimeMillis();
    }

    public boolean isExpired(Date now) {
        return this.cookieExpiryDate != null && this.cookieExpiryDate.getTime() <= now.getTime();
    }

    public void setPathAttributeSpecified(boolean value) {
        this.hasPathAttribute = value;
    }

    public boolean isPathAttributeSpecified() {
        return this.hasPathAttribute;
    }

    public void setDomainAttributeSpecified(boolean value) {
        this.hasDomainAttribute = value;
    }

    public boolean isDomainAttributeSpecified() {
        return this.hasDomainAttribute;
    }

    @Override // org.apache.commons.httpclient.NameValuePair
    public int hashCode() {
        int hash = LangUtils.hashCode(17, getName());
        return LangUtils.hashCode(LangUtils.hashCode(hash, this.cookieDomain), this.cookiePath);
    }

    @Override // org.apache.commons.httpclient.NameValuePair, java.util.Comparator
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof Cookie) {
            Cookie that = (Cookie) obj;
            return LangUtils.equals(getName(), that.getName()) && LangUtils.equals(this.cookieDomain, that.cookieDomain) && LangUtils.equals(this.cookiePath, that.cookiePath);
        }
        return false;
    }

    public String toExternalForm() throws IllegalStateException {
        CookieSpec spec;
        if (getVersion() > 0) {
            spec = CookiePolicy.getDefaultSpec();
        } else {
            spec = CookiePolicy.getCookieSpec(CookiePolicy.NETSCAPE);
        }
        return spec.formatCookie(this);
    }

    @Override // java.util.Comparator
    public int compare(Object o1, Object o2) {
        LOG.trace("enter Cookie.compare(Object, Object)");
        if (!(o1 instanceof Cookie)) {
            throw new ClassCastException(o1.getClass().getName());
        }
        if (!(o2 instanceof Cookie)) {
            throw new ClassCastException(o2.getClass().getName());
        }
        Cookie c1 = (Cookie) o1;
        Cookie c2 = (Cookie) o2;
        if (c1.getPath() == null && c2.getPath() == null) {
            return 0;
        }
        if (c1.getPath() == null) {
            if (c2.getPath().equals("/")) {
                return 0;
            }
            return -1;
        }
        if (c2.getPath() == null) {
            if (c1.getPath().equals("/")) {
                return 0;
            }
            return 1;
        }
        return c1.getPath().compareTo(c2.getPath());
    }

    @Override // org.apache.commons.httpclient.NameValuePair
    public String toString() {
        return toExternalForm();
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$org$apache$commons$httpclient$Cookie == null) {
            clsClass$ = class$("org.apache.commons.httpclient.Cookie");
            class$org$apache$commons$httpclient$Cookie = clsClass$;
        } else {
            clsClass$ = class$org$apache$commons$httpclient$Cookie;
        }
        LOG = LogFactory.getLog(clsClass$);
    }
}
