package org.springframework.boot.web.servlet;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Registration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.util.Assert;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/web/servlet/AbstractFilterRegistrationBean.class */
public abstract class AbstractFilterRegistrationBean extends RegistrationBean {
    protected static final int REQUEST_WRAPPER_FILTER_MAX_ORDER = 0;
    private static final EnumSet<DispatcherType> ASYNC_DISPATCHER_TYPES = EnumSet.of(DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.REQUEST, DispatcherType.ASYNC);
    private static final EnumSet<DispatcherType> NON_ASYNC_DISPATCHER_TYPES = EnumSet.of(DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.REQUEST);
    private static final String[] DEFAULT_URL_MAPPINGS = {ScriptUtils.DEFAULT_BLOCK_COMMENT_START_DELIMITER};
    private EnumSet<DispatcherType> dispatcherTypes;
    private final Log logger = LogFactory.getLog(getClass());
    private Set<ServletRegistrationBean> servletRegistrationBeans = new LinkedHashSet();
    private Set<String> servletNames = new LinkedHashSet();
    private Set<String> urlPatterns = new LinkedHashSet();
    private boolean matchAfter = false;

    public abstract Filter getFilter();

    AbstractFilterRegistrationBean(ServletRegistrationBean... servletRegistrationBeans) {
        Assert.notNull(servletRegistrationBeans, "ServletRegistrationBeans must not be null");
        Collections.addAll(this.servletRegistrationBeans, servletRegistrationBeans);
    }

    public void setServletRegistrationBeans(Collection<? extends ServletRegistrationBean> servletRegistrationBeans) {
        Assert.notNull(servletRegistrationBeans, "ServletRegistrationBeans must not be null");
        this.servletRegistrationBeans = new LinkedHashSet(servletRegistrationBeans);
    }

    public Collection<ServletRegistrationBean> getServletRegistrationBeans() {
        return this.servletRegistrationBeans;
    }

    public void addServletRegistrationBeans(ServletRegistrationBean... servletRegistrationBeans) {
        Assert.notNull(servletRegistrationBeans, "ServletRegistrationBeans must not be null");
        Collections.addAll(this.servletRegistrationBeans, servletRegistrationBeans);
    }

    public void setServletNames(Collection<String> servletNames) {
        Assert.notNull(servletNames, "ServletNames must not be null");
        this.servletNames = new LinkedHashSet(servletNames);
    }

    public Collection<String> getServletNames() {
        return this.servletNames;
    }

    public void addServletNames(String... servletNames) {
        Assert.notNull(servletNames, "ServletNames must not be null");
        this.servletNames.addAll(Arrays.asList(servletNames));
    }

    public void setUrlPatterns(Collection<String> urlPatterns) {
        Assert.notNull(urlPatterns, "UrlPatterns must not be null");
        this.urlPatterns = new LinkedHashSet(urlPatterns);
    }

    public Collection<String> getUrlPatterns() {
        return this.urlPatterns;
    }

    public void addUrlPatterns(String... urlPatterns) {
        Assert.notNull(urlPatterns, "UrlPatterns must not be null");
        Collections.addAll(this.urlPatterns, urlPatterns);
    }

    public void setDispatcherTypes(DispatcherType first, DispatcherType... rest) {
        this.dispatcherTypes = EnumSet.of(first, rest);
    }

    public void setDispatcherTypes(EnumSet<DispatcherType> dispatcherTypes) {
        this.dispatcherTypes = dispatcherTypes;
    }

    public void setMatchAfter(boolean matchAfter) {
        this.matchAfter = matchAfter;
    }

    public boolean isMatchAfter() {
        return this.matchAfter;
    }

    @Override // org.springframework.boot.web.servlet.ServletContextInitializer
    public void onStartup(ServletContext servletContext) throws ServletException {
        Filter filter = getFilter();
        Assert.notNull(filter, "Filter must not be null");
        String name = getOrDeduceName(filter);
        if (!isEnabled()) {
            this.logger.info("Filter " + name + " was not registered (disabled)");
            return;
        }
        FilterRegistration.Dynamic added = servletContext.addFilter(name, filter);
        if (added == null) {
            this.logger.info("Filter " + name + " was not registered (possibly already registered?)");
        } else {
            configure(added);
        }
    }

    protected void configure(FilterRegistration.Dynamic registration) {
        super.configure((Registration.Dynamic) registration);
        EnumSet<DispatcherType> dispatcherTypes = this.dispatcherTypes;
        if (dispatcherTypes == null) {
            dispatcherTypes = isAsyncSupported() ? ASYNC_DISPATCHER_TYPES : NON_ASYNC_DISPATCHER_TYPES;
        }
        Set<String> servletNames = new LinkedHashSet<>();
        for (ServletRegistrationBean servletRegistrationBean : this.servletRegistrationBeans) {
            servletNames.add(servletRegistrationBean.getServletName());
        }
        servletNames.addAll(this.servletNames);
        if (servletNames.isEmpty() && this.urlPatterns.isEmpty()) {
            this.logger.info("Mapping filter: '" + registration.getName() + "' to: " + Arrays.asList(DEFAULT_URL_MAPPINGS));
            registration.addMappingForUrlPatterns(dispatcherTypes, this.matchAfter, DEFAULT_URL_MAPPINGS);
            return;
        }
        if (!servletNames.isEmpty()) {
            this.logger.info("Mapping filter: '" + registration.getName() + "' to servlets: " + servletNames);
            registration.addMappingForServletNames(dispatcherTypes, this.matchAfter, (String[]) servletNames.toArray(new String[servletNames.size()]));
        }
        if (!this.urlPatterns.isEmpty()) {
            this.logger.info("Mapping filter: '" + registration.getName() + "' to urls: " + this.urlPatterns);
            registration.addMappingForUrlPatterns(dispatcherTypes, this.matchAfter, (String[]) this.urlPatterns.toArray(new String[this.urlPatterns.size()]));
        }
    }
}
