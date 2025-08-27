package org.springframework.boot.autoconfigure.web;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.validation.DefaultMessageCodesResolver;

@ConfigurationProperties(prefix = "spring.mvc")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/WebMvcProperties.class */
public class WebMvcProperties {
    private DefaultMessageCodesResolver.Format messageCodesResolverFormat;
    private Locale locale;
    private String dateFormat;
    private LocaleResolver localeResolver = LocaleResolver.ACCEPT_HEADER;
    private boolean dispatchTraceRequest = false;
    private boolean dispatchOptionsRequest = true;
    private boolean ignoreDefaultModelOnRedirect = true;
    private boolean throwExceptionIfNoHandlerFound = false;
    private boolean logResolvedException = false;
    private Map<String, MediaType> mediaTypes = new LinkedHashMap();
    private String staticPathPattern = "/**";
    private final Async async = new Async();
    private final Servlet servlet = new Servlet();
    private final View view = new View();

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/WebMvcProperties$LocaleResolver.class */
    public enum LocaleResolver {
        FIXED,
        ACCEPT_HEADER
    }

    public DefaultMessageCodesResolver.Format getMessageCodesResolverFormat() {
        return this.messageCodesResolverFormat;
    }

    public void setMessageCodesResolverFormat(DefaultMessageCodesResolver.Format messageCodesResolverFormat) {
        this.messageCodesResolverFormat = messageCodesResolverFormat;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public LocaleResolver getLocaleResolver() {
        return this.localeResolver;
    }

    public void setLocaleResolver(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    public String getDateFormat() {
        return this.dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public boolean isIgnoreDefaultModelOnRedirect() {
        return this.ignoreDefaultModelOnRedirect;
    }

    public void setIgnoreDefaultModelOnRedirect(boolean ignoreDefaultModelOnRedirect) {
        this.ignoreDefaultModelOnRedirect = ignoreDefaultModelOnRedirect;
    }

    public boolean isThrowExceptionIfNoHandlerFound() {
        return this.throwExceptionIfNoHandlerFound;
    }

    public void setThrowExceptionIfNoHandlerFound(boolean throwExceptionIfNoHandlerFound) {
        this.throwExceptionIfNoHandlerFound = throwExceptionIfNoHandlerFound;
    }

    public boolean isLogResolvedException() {
        return this.logResolvedException;
    }

    public void setLogResolvedException(boolean logResolvedException) {
        this.logResolvedException = logResolvedException;
    }

    public Map<String, MediaType> getMediaTypes() {
        return this.mediaTypes;
    }

    public void setMediaTypes(Map<String, MediaType> mediaTypes) {
        this.mediaTypes = mediaTypes;
    }

    public boolean isDispatchOptionsRequest() {
        return this.dispatchOptionsRequest;
    }

    public void setDispatchOptionsRequest(boolean dispatchOptionsRequest) {
        this.dispatchOptionsRequest = dispatchOptionsRequest;
    }

    public boolean isDispatchTraceRequest() {
        return this.dispatchTraceRequest;
    }

    public void setDispatchTraceRequest(boolean dispatchTraceRequest) {
        this.dispatchTraceRequest = dispatchTraceRequest;
    }

    public String getStaticPathPattern() {
        return this.staticPathPattern;
    }

    public void setStaticPathPattern(String staticPathPattern) {
        this.staticPathPattern = staticPathPattern;
    }

    public Async getAsync() {
        return this.async;
    }

    public Servlet getServlet() {
        return this.servlet;
    }

    public View getView() {
        return this.view;
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/WebMvcProperties$Async.class */
    public static class Async {
        private Long requestTimeout;

        public Long getRequestTimeout() {
            return this.requestTimeout;
        }

        public void setRequestTimeout(Long requestTimeout) {
            this.requestTimeout = requestTimeout;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/WebMvcProperties$Servlet.class */
    public static class Servlet {
        private int loadOnStartup = -1;

        public int getLoadOnStartup() {
            return this.loadOnStartup;
        }

        public void setLoadOnStartup(int loadOnStartup) {
            this.loadOnStartup = loadOnStartup;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/WebMvcProperties$View.class */
    public static class View {
        private String prefix;
        private String suffix;

        public String getPrefix() {
            return this.prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getSuffix() {
            return this.suffix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }
    }
}
