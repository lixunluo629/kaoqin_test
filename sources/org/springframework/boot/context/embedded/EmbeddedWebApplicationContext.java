package org.springframework.boot.context.embedded;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletContextInitializerBeans;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.context.support.ServletContextScope;
import org.springframework.web.context.support.WebApplicationContextUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/EmbeddedWebApplicationContext.class */
public class EmbeddedWebApplicationContext extends GenericWebApplicationContext {
    private static final Log logger = LogFactory.getLog(EmbeddedWebApplicationContext.class);
    public static final String DISPATCHER_SERVLET_NAME = "dispatcherServlet";
    private volatile EmbeddedServletContainer embeddedServletContainer;
    private ServletConfig servletConfig;
    private String namespace;

    @Override // org.springframework.web.context.support.GenericWebApplicationContext, org.springframework.context.support.AbstractApplicationContext
    protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new WebApplicationContextServletContextAwareProcessor(this));
        beanFactory.ignoreDependencyInterface(ServletContextAware.class);
        registerWebApplicationScopes();
    }

    @Override // org.springframework.context.support.AbstractApplicationContext, org.springframework.context.ConfigurableApplicationContext
    public final void refresh() throws IllegalStateException, BeansException {
        try {
            super.refresh();
        } catch (RuntimeException ex) {
            stopAndReleaseEmbeddedServletContainer();
            throw ex;
        }
    }

    @Override // org.springframework.web.context.support.GenericWebApplicationContext, org.springframework.context.support.AbstractApplicationContext
    protected void onRefresh() {
        super.onRefresh();
        try {
            createEmbeddedServletContainer();
        } catch (Throwable ex) {
            throw new ApplicationContextException("Unable to start embedded container", ex);
        }
    }

    @Override // org.springframework.context.support.AbstractApplicationContext
    protected void finishRefresh() throws EmbeddedServletContainerException {
        super.finishRefresh();
        EmbeddedServletContainer localContainer = startEmbeddedServletContainer();
        if (localContainer != null) {
            publishEvent((ApplicationEvent) new EmbeddedServletContainerInitializedEvent(this, localContainer));
        }
    }

    @Override // org.springframework.context.support.AbstractApplicationContext
    protected void onClose() {
        super.onClose();
        stopAndReleaseEmbeddedServletContainer();
    }

    private void createEmbeddedServletContainer() {
        EmbeddedServletContainer localContainer = this.embeddedServletContainer;
        ServletContext localServletContext = getServletContext();
        if (localContainer == null && localServletContext == null) {
            EmbeddedServletContainerFactory containerFactory = getEmbeddedServletContainerFactory();
            this.embeddedServletContainer = containerFactory.getEmbeddedServletContainer(getSelfInitializer());
        } else if (localServletContext != null) {
            try {
                getSelfInitializer().onStartup(localServletContext);
            } catch (ServletException ex) {
                throw new ApplicationContextException("Cannot initialize servlet context", ex);
            }
        }
        initPropertySources();
    }

    protected EmbeddedServletContainerFactory getEmbeddedServletContainerFactory() {
        String[] beanNames = getBeanFactory().getBeanNamesForType(EmbeddedServletContainerFactory.class);
        if (beanNames.length == 0) {
            throw new ApplicationContextException("Unable to start EmbeddedWebApplicationContext due to missing EmbeddedServletContainerFactory bean.");
        }
        if (beanNames.length > 1) {
            throw new ApplicationContextException("Unable to start EmbeddedWebApplicationContext due to multiple EmbeddedServletContainerFactory beans : " + StringUtils.arrayToCommaDelimitedString(beanNames));
        }
        return (EmbeddedServletContainerFactory) getBeanFactory().getBean(beanNames[0], EmbeddedServletContainerFactory.class);
    }

    private ServletContextInitializer getSelfInitializer() {
        return new ServletContextInitializer() { // from class: org.springframework.boot.context.embedded.EmbeddedWebApplicationContext.1
            @Override // org.springframework.boot.web.servlet.ServletContextInitializer
            public void onStartup(ServletContext servletContext) throws ServletException, LogConfigurationException {
                EmbeddedWebApplicationContext.this.selfInitialize(servletContext);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void selfInitialize(ServletContext servletContext) throws ServletException, LogConfigurationException {
        prepareEmbeddedWebApplicationContext(servletContext);
        registerApplicationScope(servletContext);
        WebApplicationContextUtils.registerEnvironmentBeans(getBeanFactory(), servletContext);
        for (ServletContextInitializer beans : getServletContextInitializerBeans()) {
            beans.onStartup(servletContext);
        }
    }

    private void registerApplicationScope(ServletContext servletContext) {
        ServletContextScope appScope = new ServletContextScope(servletContext);
        getBeanFactory().registerScope("application", appScope);
        servletContext.setAttribute(ServletContextScope.class.getName(), appScope);
    }

    private void registerWebApplicationScopes() {
        ExistingWebApplicationScopes existingScopes = new ExistingWebApplicationScopes(getBeanFactory());
        WebApplicationContextUtils.registerWebApplicationScopes(getBeanFactory());
        existingScopes.restore();
    }

    protected Collection<ServletContextInitializer> getServletContextInitializerBeans() {
        return new ServletContextInitializerBeans(getBeanFactory());
    }

    protected void prepareEmbeddedWebApplicationContext(ServletContext servletContext) throws LogConfigurationException {
        Object rootContext = servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        if (rootContext != null) {
            if (rootContext == this) {
                throw new IllegalStateException("Cannot initialize context because there is already a root application context present - check whether you have multiple ServletContextInitializers!");
            }
            return;
        }
        Log logger2 = LogFactory.getLog(ContextLoader.class);
        servletContext.log("Initializing Spring embedded WebApplicationContext");
        try {
            servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this);
            if (logger2.isDebugEnabled()) {
                logger2.debug("Published root WebApplicationContext as ServletContext attribute with name [" + WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE + "]");
            }
            setServletContext(servletContext);
            if (logger2.isInfoEnabled()) {
                long elapsedTime = System.currentTimeMillis() - getStartupDate();
                logger2.info("Root WebApplicationContext: initialization completed in " + elapsedTime + " ms");
            }
        } catch (Error ex) {
            logger2.error("Context initialization failed", ex);
            servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, ex);
            throw ex;
        } catch (RuntimeException ex2) {
            logger2.error("Context initialization failed", ex2);
            servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, ex2);
            throw ex2;
        }
    }

    private EmbeddedServletContainer startEmbeddedServletContainer() throws EmbeddedServletContainerException {
        EmbeddedServletContainer localContainer = this.embeddedServletContainer;
        if (localContainer != null) {
            localContainer.start();
        }
        return localContainer;
    }

    private void stopAndReleaseEmbeddedServletContainer() {
        EmbeddedServletContainer localContainer = this.embeddedServletContainer;
        if (localContainer != null) {
            try {
                localContainer.stop();
                this.embeddedServletContainer = null;
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }
    }

    @Override // org.springframework.web.context.support.GenericWebApplicationContext, org.springframework.core.io.DefaultResourceLoader
    protected Resource getResourceByPath(String path) {
        if (getServletContext() == null) {
            return new DefaultResourceLoader.ClassPathContextResource(path, getClassLoader());
        }
        return new ServletContextResource(getServletContext(), path);
    }

    @Override // org.springframework.web.context.support.GenericWebApplicationContext, org.springframework.web.context.ConfigurableWebApplicationContext
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override // org.springframework.web.context.support.GenericWebApplicationContext, org.springframework.web.context.ConfigurableWebApplicationContext
    public String getNamespace() {
        return this.namespace;
    }

    @Override // org.springframework.web.context.support.GenericWebApplicationContext, org.springframework.web.context.ConfigurableWebApplicationContext
    public void setServletConfig(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    @Override // org.springframework.web.context.support.GenericWebApplicationContext, org.springframework.web.context.ConfigurableWebApplicationContext
    public ServletConfig getServletConfig() {
        return this.servletConfig;
    }

    public EmbeddedServletContainer getEmbeddedServletContainer() {
        return this.embeddedServletContainer;
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/EmbeddedWebApplicationContext$ExistingWebApplicationScopes.class */
    public static class ExistingWebApplicationScopes {
        private static final Set<String> SCOPES;
        private final ConfigurableListableBeanFactory beanFactory;
        private final Map<String, Scope> scopes = new HashMap();

        static {
            Set<String> scopes = new LinkedHashSet<>();
            scopes.add("request");
            scopes.add("session");
            scopes.add(WebApplicationContext.SCOPE_GLOBAL_SESSION);
            SCOPES = Collections.unmodifiableSet(scopes);
        }

        public ExistingWebApplicationScopes(ConfigurableListableBeanFactory beanFactory) {
            this.beanFactory = beanFactory;
            for (String scopeName : SCOPES) {
                Scope scope = beanFactory.getRegisteredScope(scopeName);
                if (scope != null) {
                    this.scopes.put(scopeName, scope);
                }
            }
        }

        public void restore() {
            for (Map.Entry<String, Scope> entry : this.scopes.entrySet()) {
                if (EmbeddedWebApplicationContext.logger.isInfoEnabled()) {
                    EmbeddedWebApplicationContext.logger.info("Restoring user defined scope " + entry.getKey());
                }
                this.beanFactory.registerScope(entry.getKey(), entry.getValue());
            }
        }
    }
}
