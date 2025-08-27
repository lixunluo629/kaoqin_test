package org.apache.commons.httpclient.cookie;

import java.util.Date;
import org.apache.commons.httpclient.Cookie;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/cookie/Cookie2.class */
public class Cookie2 extends Cookie {
    public static final String DOMAIN = "domain";
    public static final String PATH = "path";
    public static final String PORT = "port";
    public static final String VERSION = "version";
    public static final String SECURE = "secure";
    public static final String MAXAGE = "max-age";
    public static final String COMMENT = "comment";
    public static final String COMMENTURL = "commenturl";
    public static final String DISCARD = "discard";
    private String cookieCommentURL;
    private int[] cookiePorts;
    private boolean discard;
    private boolean hasPortAttribute;
    private boolean isPortAttributeBlank;
    private boolean hasVersionAttribute;

    public Cookie2() {
        super((String) null, "noname", (String) null, (String) null, (Date) null, false);
        this.discard = false;
        this.hasPortAttribute = false;
        this.isPortAttributeBlank = false;
        this.hasVersionAttribute = false;
    }

    public Cookie2(String domain, String name, String value) {
        super(domain, name, value);
        this.discard = false;
        this.hasPortAttribute = false;
        this.isPortAttributeBlank = false;
        this.hasVersionAttribute = false;
    }

    public Cookie2(String domain, String name, String value, String path, Date expires, boolean secure) {
        super(domain, name, value, path, expires, secure);
        this.discard = false;
        this.hasPortAttribute = false;
        this.isPortAttributeBlank = false;
        this.hasVersionAttribute = false;
    }

    public Cookie2(String domain, String name, String value, String path, Date expires, boolean secure, int[] ports) {
        super(domain, name, value, path, expires, secure);
        this.discard = false;
        this.hasPortAttribute = false;
        this.isPortAttributeBlank = false;
        this.hasVersionAttribute = false;
        setPorts(ports);
    }

    public String getCommentURL() {
        return this.cookieCommentURL;
    }

    public void setCommentURL(String commentURL) {
        this.cookieCommentURL = commentURL;
    }

    public int[] getPorts() {
        return this.cookiePorts;
    }

    public void setPorts(int[] ports) {
        this.cookiePorts = ports;
    }

    public void setDiscard(boolean toDiscard) {
        this.discard = toDiscard;
    }

    @Override // org.apache.commons.httpclient.Cookie
    public boolean isPersistent() {
        return (null == getExpiryDate() || this.discard) ? false : true;
    }

    public void setPortAttributeSpecified(boolean value) {
        this.hasPortAttribute = value;
    }

    public boolean isPortAttributeSpecified() {
        return this.hasPortAttribute;
    }

    public void setPortAttributeBlank(boolean value) {
        this.isPortAttributeBlank = value;
    }

    public boolean isPortAttributeBlank() {
        return this.isPortAttributeBlank;
    }

    public void setVersionAttributeSpecified(boolean value) {
        this.hasVersionAttribute = value;
    }

    public boolean isVersionAttributeSpecified() {
        return this.hasVersionAttribute;
    }

    @Override // org.apache.commons.httpclient.Cookie
    public String toExternalForm() throws IllegalStateException {
        CookieSpec spec = CookiePolicy.getCookieSpec(CookiePolicy.RFC_2965);
        return spec.formatCookie(this);
    }
}
