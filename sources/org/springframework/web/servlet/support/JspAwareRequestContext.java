package org.springframework.web.servlet.support;

import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.core.Config;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/support/JspAwareRequestContext.class */
public class JspAwareRequestContext extends RequestContext {
    private PageContext pageContext;

    public JspAwareRequestContext(PageContext pageContext) {
        initContext(pageContext, null);
    }

    public JspAwareRequestContext(PageContext pageContext, Map<String, Object> model) {
        initContext(pageContext, model);
    }

    protected void initContext(PageContext pageContext, Map<String, Object> model) {
        if (!(pageContext.getRequest() instanceof HttpServletRequest)) {
            throw new IllegalArgumentException("RequestContext only supports HTTP requests");
        }
        this.pageContext = pageContext;
        initContext((HttpServletRequest) pageContext.getRequest(), (HttpServletResponse) pageContext.getResponse(), pageContext.getServletContext(), model);
    }

    protected final PageContext getPageContext() {
        return this.pageContext;
    }

    @Override // org.springframework.web.servlet.support.RequestContext
    protected Locale getFallbackLocale() {
        Locale locale;
        if (jstlPresent && (locale = JstlPageLocaleResolver.getJstlLocale(getPageContext())) != null) {
            return locale;
        }
        return getRequest().getLocale();
    }

    @Override // org.springframework.web.servlet.support.RequestContext
    protected TimeZone getFallbackTimeZone() {
        TimeZone timeZone;
        if (jstlPresent && (timeZone = JstlPageLocaleResolver.getJstlTimeZone(getPageContext())) != null) {
            return timeZone;
        }
        return null;
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/support/JspAwareRequestContext$JstlPageLocaleResolver.class */
    private static class JstlPageLocaleResolver {
        private JstlPageLocaleResolver() {
        }

        public static Locale getJstlLocale(PageContext pageContext) {
            Object localeObject = Config.find(pageContext, "javax.servlet.jsp.jstl.fmt.locale");
            if (localeObject instanceof Locale) {
                return (Locale) localeObject;
            }
            return null;
        }

        public static TimeZone getJstlTimeZone(PageContext pageContext) {
            Object timeZoneObject = Config.find(pageContext, "javax.servlet.jsp.jstl.fmt.timeZone");
            if (timeZoneObject instanceof TimeZone) {
                return (TimeZone) timeZoneObject;
            }
            return null;
        }
    }
}
