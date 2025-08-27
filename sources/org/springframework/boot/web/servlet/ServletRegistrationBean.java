package org.springframework.boot.web.servlet;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.servlet.MultipartConfigElement;
import javax.servlet.Registration;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/web/servlet/ServletRegistrationBean.class */
public class ServletRegistrationBean extends RegistrationBean {
    private static final Log logger = LogFactory.getLog(ServletRegistrationBean.class);
    private static final String[] DEFAULT_MAPPINGS = {ScriptUtils.DEFAULT_BLOCK_COMMENT_START_DELIMITER};
    private Servlet servlet;
    private Set<String> urlMappings;
    private boolean alwaysMapUrl;
    private int loadOnStartup;
    private MultipartConfigElement multipartConfig;

    public ServletRegistrationBean() {
        this.urlMappings = new LinkedHashSet();
        this.alwaysMapUrl = true;
        this.loadOnStartup = -1;
    }

    public ServletRegistrationBean(Servlet servlet, String... urlMappings) {
        this(servlet, true, urlMappings);
    }

    public ServletRegistrationBean(Servlet servlet, boolean alwaysMapUrl, String... urlMappings) {
        this.urlMappings = new LinkedHashSet();
        this.alwaysMapUrl = true;
        this.loadOnStartup = -1;
        Assert.notNull(servlet, "Servlet must not be null");
        Assert.notNull(urlMappings, "UrlMappings must not be null");
        this.servlet = servlet;
        this.alwaysMapUrl = alwaysMapUrl;
        this.urlMappings.addAll(Arrays.asList(urlMappings));
    }

    protected Servlet getServlet() {
        return this.servlet;
    }

    public void setServlet(Servlet servlet) {
        Assert.notNull(servlet, "Servlet must not be null");
        this.servlet = servlet;
    }

    public void setUrlMappings(Collection<String> urlMappings) {
        Assert.notNull(urlMappings, "UrlMappings must not be null");
        this.urlMappings = new LinkedHashSet(urlMappings);
    }

    public Collection<String> getUrlMappings() {
        return this.urlMappings;
    }

    public void addUrlMappings(String... urlMappings) {
        Assert.notNull(urlMappings, "UrlMappings must not be null");
        this.urlMappings.addAll(Arrays.asList(urlMappings));
    }

    public void setLoadOnStartup(int loadOnStartup) {
        this.loadOnStartup = loadOnStartup;
    }

    public void setMultipartConfig(MultipartConfigElement multipartConfig) {
        this.multipartConfig = multipartConfig;
    }

    public MultipartConfigElement getMultipartConfig() {
        return this.multipartConfig;
    }

    public String getServletName() {
        return getOrDeduceName(this.servlet);
    }

    @Override // org.springframework.boot.web.servlet.ServletContextInitializer
    public void onStartup(ServletContext servletContext) throws ServletException {
        Assert.notNull(this.servlet, "Servlet must not be null");
        String name = getServletName();
        if (!isEnabled()) {
            logger.info("Servlet " + name + " was not registered (disabled)");
            return;
        }
        logger.info("Mapping servlet: '" + name + "' to " + this.urlMappings);
        ServletRegistration.Dynamic added = servletContext.addServlet(name, this.servlet);
        if (added == null) {
            logger.info("Servlet " + name + " was not registered (possibly already registered?)");
        } else {
            configure(added);
        }
    }

    protected void configure(ServletRegistration.Dynamic registration) {
        super.configure((Registration.Dynamic) registration);
        String[] urlMapping = (String[]) this.urlMappings.toArray(new String[this.urlMappings.size()]);
        if (urlMapping.length == 0 && this.alwaysMapUrl) {
            urlMapping = DEFAULT_MAPPINGS;
        }
        if (!ObjectUtils.isEmpty((Object[]) urlMapping)) {
            registration.addMapping(urlMapping);
        }
        registration.setLoadOnStartup(this.loadOnStartup);
        if (this.multipartConfig != null) {
            registration.setMultipartConfig(this.multipartConfig);
        }
    }
}
