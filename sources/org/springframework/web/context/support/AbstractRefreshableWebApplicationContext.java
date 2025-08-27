package org.springframework.web.context.support;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.ui.context.Theme;
import org.springframework.ui.context.ThemeSource;
import org.springframework.ui.context.support.UiApplicationContextUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ConfigurableWebEnvironment;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.ServletContextAware;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/support/AbstractRefreshableWebApplicationContext.class */
public abstract class AbstractRefreshableWebApplicationContext extends AbstractRefreshableConfigApplicationContext implements ConfigurableWebApplicationContext, ThemeSource {
    private ServletContext servletContext;
    private ServletConfig servletConfig;
    private String namespace;
    private ThemeSource themeSource;

    public AbstractRefreshableWebApplicationContext() {
        setDisplayName("Root WebApplicationContext");
    }

    @Override // org.springframework.web.context.ConfigurableWebApplicationContext
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override // org.springframework.web.context.WebApplicationContext
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    @Override // org.springframework.web.context.ConfigurableWebApplicationContext
    public void setServletConfig(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
        if (servletConfig != null && this.servletContext == null) {
            setServletContext(servletConfig.getServletContext());
        }
    }

    @Override // org.springframework.web.context.ConfigurableWebApplicationContext
    public ServletConfig getServletConfig() {
        return this.servletConfig;
    }

    @Override // org.springframework.web.context.ConfigurableWebApplicationContext
    public void setNamespace(String namespace) {
        this.namespace = namespace;
        if (namespace != null) {
            setDisplayName("WebApplicationContext for namespace '" + namespace + "'");
        }
    }

    @Override // org.springframework.web.context.ConfigurableWebApplicationContext
    public String getNamespace() {
        return this.namespace;
    }

    @Override // org.springframework.context.support.AbstractRefreshableConfigApplicationContext, org.springframework.web.context.ConfigurableWebApplicationContext
    public String[] getConfigLocations() {
        return super.getConfigLocations();
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
        beanFactory.addBeanPostProcessor(new ServletContextAwareProcessor(this.servletContext, this.servletConfig));
        beanFactory.ignoreDependencyInterface(ServletContextAware.class);
        beanFactory.ignoreDependencyInterface(ServletConfigAware.class);
        WebApplicationContextUtils.registerWebApplicationScopes(beanFactory, this.servletContext);
        WebApplicationContextUtils.registerEnvironmentBeans(beanFactory, this.servletContext, this.servletConfig);
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
            ((ConfigurableWebEnvironment) env).initPropertySources(this.servletContext, this.servletConfig);
        }
    }

    @Override // org.springframework.ui.context.ThemeSource
    public Theme getTheme(String themeName) {
        return this.themeSource.getTheme(themeName);
    }
}
