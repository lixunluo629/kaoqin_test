package org.springframework.web.context.support;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.ui.context.Theme;
import org.springframework.ui.context.ThemeSource;
import org.springframework.ui.context.support.UiApplicationContextUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ConfigurableWebEnvironment;
import org.springframework.web.context.ServletContextAware;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/support/GenericWebApplicationContext.class */
public class GenericWebApplicationContext extends GenericApplicationContext implements ConfigurableWebApplicationContext, ThemeSource {
    private ServletContext servletContext;
    private ThemeSource themeSource;

    public GenericWebApplicationContext() {
    }

    public GenericWebApplicationContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public GenericWebApplicationContext(DefaultListableBeanFactory beanFactory) {
        super(beanFactory);
    }

    public GenericWebApplicationContext(DefaultListableBeanFactory beanFactory, ServletContext servletContext) {
        super(beanFactory);
        this.servletContext = servletContext;
    }

    @Override // org.springframework.web.context.ConfigurableWebApplicationContext
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override // org.springframework.web.context.WebApplicationContext
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    @Override // org.springframework.context.support.AbstractApplicationContext, org.springframework.context.ApplicationContext
    public String getApplicationName() {
        return this.servletContext != null ? this.servletContext.getContextPath() : "";
    }

    @Override // org.springframework.context.support.AbstractApplicationContext
    protected ConfigurableEnvironment createEnvironment() {
        return new StandardServletEnvironment();
    }

    @Override // org.springframework.context.support.AbstractApplicationContext
    protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new ServletContextAwareProcessor(this.servletContext));
        beanFactory.ignoreDependencyInterface(ServletContextAware.class);
        WebApplicationContextUtils.registerWebApplicationScopes(beanFactory, this.servletContext);
        WebApplicationContextUtils.registerEnvironmentBeans(beanFactory, this.servletContext);
    }

    @Override // org.springframework.core.io.DefaultResourceLoader
    protected Resource getResourceByPath(String path) {
        return new ServletContextResource(this.servletContext, path);
    }

    @Override // org.springframework.context.support.AbstractApplicationContext
    protected ResourcePatternResolver getResourcePatternResolver() {
        return new ServletContextResourcePatternResolver(this);
    }

    @Override // org.springframework.context.support.AbstractApplicationContext
    protected void onRefresh() {
        this.themeSource = UiApplicationContextUtils.initThemeSource(this);
    }

    @Override // org.springframework.context.support.AbstractApplicationContext
    protected void initPropertySources() {
        ConfigurableEnvironment env = getEnvironment();
        if (env instanceof ConfigurableWebEnvironment) {
            ((ConfigurableWebEnvironment) env).initPropertySources(this.servletContext, null);
        }
    }

    @Override // org.springframework.ui.context.ThemeSource
    public Theme getTheme(String themeName) {
        return this.themeSource.getTheme(themeName);
    }

    public void setServletConfig(ServletConfig servletConfig) {
    }

    public ServletConfig getServletConfig() {
        throw new UnsupportedOperationException("GenericWebApplicationContext does not support getServletConfig()");
    }

    public void setNamespace(String namespace) {
    }

    public String getNamespace() {
        throw new UnsupportedOperationException("GenericWebApplicationContext does not support getNamespace()");
    }

    @Override // org.springframework.web.context.ConfigurableWebApplicationContext
    public void setConfigLocation(String configLocation) {
        if (StringUtils.hasText(configLocation)) {
            throw new UnsupportedOperationException("GenericWebApplicationContext does not support setConfigLocation(). Do you still have an 'contextConfigLocations' init-param set?");
        }
    }

    @Override // org.springframework.web.context.ConfigurableWebApplicationContext
    public void setConfigLocations(String... configLocations) {
        if (!ObjectUtils.isEmpty((Object[]) configLocations)) {
            throw new UnsupportedOperationException("GenericWebApplicationContext does not support setConfigLocations(). Do you still have an 'contextConfigLocations' init-param set?");
        }
    }

    @Override // org.springframework.web.context.ConfigurableWebApplicationContext
    public String[] getConfigLocations() {
        throw new UnsupportedOperationException("GenericWebApplicationContext does not support getConfigLocations()");
    }
}
