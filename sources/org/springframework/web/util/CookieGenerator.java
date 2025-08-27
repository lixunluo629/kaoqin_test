package org.springframework.web.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/util/CookieGenerator.class */
public class CookieGenerator {
    public static final String DEFAULT_COOKIE_PATH = "/";
    private String cookieName;
    private String cookieDomain;
    private Integer cookieMaxAge;
    protected final Log logger = LogFactory.getLog(getClass());
    private String cookiePath = "/";
    private boolean cookieSecure = false;
    private boolean cookieHttpOnly = false;

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public String getCookieName() {
        return this.cookieName;
    }

    public void setCookieDomain(String cookieDomain) {
        this.cookieDomain = cookieDomain;
    }

    public String getCookieDomain() {
        return this.cookieDomain;
    }

    public void setCookiePath(String cookiePath) {
        this.cookiePath = cookiePath;
    }

    public String getCookiePath() {
        return this.cookiePath;
    }

    public void setCookieMaxAge(Integer cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }

    public Integer getCookieMaxAge() {
        return this.cookieMaxAge;
    }

    public void setCookieSecure(boolean cookieSecure) {
        this.cookieSecure = cookieSecure;
    }

    public boolean isCookieSecure() {
        return this.cookieSecure;
    }

    public void setCookieHttpOnly(boolean cookieHttpOnly) {
        this.cookieHttpOnly = cookieHttpOnly;
    }

    public boolean isCookieHttpOnly() {
        return this.cookieHttpOnly;
    }

    public void addCookie(HttpServletResponse response, String cookieValue) {
        Assert.notNull(response, "HttpServletResponse must not be null");
        Cookie cookie = createCookie(cookieValue);
        Integer maxAge = getCookieMaxAge();
        if (maxAge != null) {
            cookie.setMaxAge(maxAge.intValue());
        }
        if (isCookieSecure()) {
            cookie.setSecure(true);
        }
        if (isCookieHttpOnly()) {
            cookie.setHttpOnly(true);
        }
        response.addCookie(cookie);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Added cookie with name [" + getCookieName() + "] and value [" + cookieValue + "]");
        }
    }

    public void removeCookie(HttpServletResponse response) {
        Assert.notNull(response, "HttpServletResponse must not be null");
        Cookie cookie = createCookie("");
        cookie.setMaxAge(0);
        if (isCookieSecure()) {
            cookie.setSecure(true);
        }
        if (isCookieHttpOnly()) {
            cookie.setHttpOnly(true);
        }
        response.addCookie(cookie);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Removed cookie with name [" + getCookieName() + "]");
        }
    }

    protected Cookie createCookie(String cookieValue) {
        Cookie cookie = new Cookie(getCookieName(), cookieValue);
        if (getCookieDomain() != null) {
            cookie.setDomain(getCookieDomain());
        }
        cookie.setPath(getCookiePath());
        return cookie;
    }
}
