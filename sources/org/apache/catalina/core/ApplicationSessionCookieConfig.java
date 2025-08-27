package org.apache.catalina.core;

import javax.servlet.SessionCookieConfig;
import javax.servlet.http.Cookie;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.util.SessionConfig;
import org.apache.commons.httpclient.cookie.Cookie2;
import org.apache.tomcat.util.res.StringManager;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/core/ApplicationSessionCookieConfig.class */
public class ApplicationSessionCookieConfig implements SessionCookieConfig {
    private static final StringManager sm = StringManager.getManager(Constants.Package);
    private boolean httpOnly;
    private boolean secure;
    private int maxAge = -1;
    private String comment;
    private String domain;
    private String name;
    private String path;
    private StandardContext context;

    public ApplicationSessionCookieConfig(StandardContext context) {
        this.context = context;
    }

    @Override // javax.servlet.SessionCookieConfig
    public String getComment() {
        return this.comment;
    }

    @Override // javax.servlet.SessionCookieConfig
    public String getDomain() {
        return this.domain;
    }

    @Override // javax.servlet.SessionCookieConfig
    public int getMaxAge() {
        return this.maxAge;
    }

    @Override // javax.servlet.SessionCookieConfig
    public String getName() {
        return this.name;
    }

    @Override // javax.servlet.SessionCookieConfig
    public String getPath() {
        return this.path;
    }

    @Override // javax.servlet.SessionCookieConfig
    public boolean isHttpOnly() {
        return this.httpOnly;
    }

    @Override // javax.servlet.SessionCookieConfig
    public boolean isSecure() {
        return this.secure;
    }

    @Override // javax.servlet.SessionCookieConfig
    public void setComment(String comment) {
        if (!this.context.getState().equals(LifecycleState.STARTING_PREP)) {
            throw new IllegalStateException(sm.getString("applicationSessionCookieConfig.ise", "comment", this.context.getPath()));
        }
        this.comment = comment;
    }

    @Override // javax.servlet.SessionCookieConfig
    public void setDomain(String domain) {
        if (!this.context.getState().equals(LifecycleState.STARTING_PREP)) {
            throw new IllegalStateException(sm.getString("applicationSessionCookieConfig.ise", "domain name", this.context.getPath()));
        }
        this.domain = domain;
    }

    @Override // javax.servlet.SessionCookieConfig
    public void setHttpOnly(boolean httpOnly) {
        if (!this.context.getState().equals(LifecycleState.STARTING_PREP)) {
            throw new IllegalStateException(sm.getString("applicationSessionCookieConfig.ise", "HttpOnly", this.context.getPath()));
        }
        this.httpOnly = httpOnly;
    }

    @Override // javax.servlet.SessionCookieConfig
    public void setMaxAge(int maxAge) {
        if (!this.context.getState().equals(LifecycleState.STARTING_PREP)) {
            throw new IllegalStateException(sm.getString("applicationSessionCookieConfig.ise", "max age", this.context.getPath()));
        }
        this.maxAge = maxAge;
    }

    @Override // javax.servlet.SessionCookieConfig
    public void setName(String name) {
        if (!this.context.getState().equals(LifecycleState.STARTING_PREP)) {
            throw new IllegalStateException(sm.getString("applicationSessionCookieConfig.ise", "name", this.context.getPath()));
        }
        this.name = name;
    }

    @Override // javax.servlet.SessionCookieConfig
    public void setPath(String path) {
        if (!this.context.getState().equals(LifecycleState.STARTING_PREP)) {
            throw new IllegalStateException(sm.getString("applicationSessionCookieConfig.ise", Cookie2.PATH, this.context.getPath()));
        }
        this.path = path;
    }

    @Override // javax.servlet.SessionCookieConfig
    public void setSecure(boolean secure) {
        if (!this.context.getState().equals(LifecycleState.STARTING_PREP)) {
            throw new IllegalStateException(sm.getString("applicationSessionCookieConfig.ise", Cookie2.SECURE, this.context.getPath()));
        }
        this.secure = secure;
    }

    public static Cookie createSessionCookie(Context context, String sessionId, boolean secure) {
        SessionCookieConfig scc = context.getServletContext().getSessionCookieConfig();
        Cookie cookie = new Cookie(SessionConfig.getSessionCookieName(context), sessionId);
        cookie.setMaxAge(scc.getMaxAge());
        cookie.setComment(scc.getComment());
        if (context.getSessionCookieDomain() == null) {
            if (scc.getDomain() != null) {
                cookie.setDomain(scc.getDomain());
            }
        } else {
            cookie.setDomain(context.getSessionCookieDomain());
        }
        if (scc.isSecure() || secure) {
            cookie.setSecure(true);
        }
        if (scc.isHttpOnly() || context.getUseHttpOnly()) {
            cookie.setHttpOnly(true);
        }
        cookie.setPath(SessionConfig.getSessionCookiePath(context));
        return cookie;
    }
}
