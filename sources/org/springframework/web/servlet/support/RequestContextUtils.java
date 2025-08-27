package org.springframework.web.servlet.support;

import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
import org.springframework.ui.context.Theme;
import org.springframework.ui.context.ThemeSource;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.LocaleContextResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ThemeResolver;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/support/RequestContextUtils.class */
public abstract class RequestContextUtils {
    public static final String REQUEST_DATA_VALUE_PROCESSOR_BEAN_NAME = "requestDataValueProcessor";

    @Deprecated
    public static WebApplicationContext getWebApplicationContext(ServletRequest request) throws IllegalStateException {
        return getWebApplicationContext(request, null);
    }

    @Deprecated
    public static WebApplicationContext getWebApplicationContext(ServletRequest request, ServletContext servletContext) throws IllegalStateException {
        WebApplicationContext webApplicationContext = (WebApplicationContext) request.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        if (webApplicationContext == null) {
            if (servletContext == null) {
                throw new IllegalStateException("No WebApplicationContext found: not in a DispatcherServlet request?");
            }
            webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        }
        return webApplicationContext;
    }

    public static WebApplicationContext findWebApplicationContext(HttpServletRequest request, ServletContext servletContext) {
        WebApplicationContext webApplicationContext = (WebApplicationContext) request.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        if (webApplicationContext == null) {
            if (servletContext != null) {
                webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            }
            if (webApplicationContext == null) {
                webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
            }
        }
        return webApplicationContext;
    }

    public static WebApplicationContext findWebApplicationContext(HttpServletRequest request) {
        return findWebApplicationContext(request, request.getServletContext());
    }

    public static LocaleResolver getLocaleResolver(HttpServletRequest request) {
        return (LocaleResolver) request.getAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE);
    }

    public static Locale getLocale(HttpServletRequest request) {
        LocaleResolver localeResolver = getLocaleResolver(request);
        return localeResolver != null ? localeResolver.resolveLocale(request) : request.getLocale();
    }

    public static TimeZone getTimeZone(HttpServletRequest request) {
        LocaleResolver localeResolver = getLocaleResolver(request);
        if (localeResolver instanceof LocaleContextResolver) {
            LocaleContext localeContext = ((LocaleContextResolver) localeResolver).resolveLocaleContext(request);
            if (localeContext instanceof TimeZoneAwareLocaleContext) {
                return ((TimeZoneAwareLocaleContext) localeContext).getTimeZone();
            }
            return null;
        }
        return null;
    }

    public static ThemeResolver getThemeResolver(HttpServletRequest request) {
        return (ThemeResolver) request.getAttribute(DispatcherServlet.THEME_RESOLVER_ATTRIBUTE);
    }

    public static ThemeSource getThemeSource(HttpServletRequest request) {
        return (ThemeSource) request.getAttribute(DispatcherServlet.THEME_SOURCE_ATTRIBUTE);
    }

    public static Theme getTheme(HttpServletRequest request) {
        ThemeResolver themeResolver = getThemeResolver(request);
        ThemeSource themeSource = getThemeSource(request);
        if (themeResolver != null && themeSource != null) {
            String themeName = themeResolver.resolveThemeName(request);
            return themeSource.getTheme(themeName);
        }
        return null;
    }

    public static Map<String, ?> getInputFlashMap(HttpServletRequest request) {
        return (Map) request.getAttribute(DispatcherServlet.INPUT_FLASH_MAP_ATTRIBUTE);
    }

    public static FlashMap getOutputFlashMap(HttpServletRequest request) {
        return (FlashMap) request.getAttribute(DispatcherServlet.OUTPUT_FLASH_MAP_ATTRIBUTE);
    }

    public static FlashMapManager getFlashMapManager(HttpServletRequest request) {
        return (FlashMapManager) request.getAttribute(DispatcherServlet.FLASH_MAP_MANAGER_ATTRIBUTE);
    }
}
