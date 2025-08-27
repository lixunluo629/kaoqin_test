package org.springframework.boot.web.support;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.ParentContextApplicationContextInitializer;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.Assert;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ConfigurableWebEnvironment;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/web/support/SpringBootServletInitializer.class */
public abstract class SpringBootServletInitializer implements WebApplicationInitializer {
    protected Log logger;
    private boolean registerErrorPageFilter = true;

    protected final void setRegisterErrorPageFilter(boolean registerErrorPageFilter) {
        this.registerErrorPageFilter = registerErrorPageFilter;
    }

    @Override // org.springframework.web.WebApplicationInitializer
    public void onStartup(ServletContext servletContext) throws ServletException {
        this.logger = LogFactory.getLog(getClass());
        WebApplicationContext rootAppContext = createRootApplicationContext(servletContext);
        if (rootAppContext != null) {
            servletContext.addListener((ServletContext) new ContextLoaderListener(rootAppContext) { // from class: org.springframework.boot.web.support.SpringBootServletInitializer.1
                @Override // org.springframework.web.context.ContextLoaderListener, javax.servlet.ServletContextListener
                public void contextInitialized(ServletContextEvent event) {
                }
            });
        } else {
            this.logger.debug("No ContextLoaderListener registered, as createRootApplicationContext() did not return an application context");
        }
    }

    protected WebApplicationContext createRootApplicationContext(ServletContext servletContext) {
        SpringApplicationBuilder builder = createSpringApplicationBuilder();
        builder.main(getClass());
        ApplicationContext parent = getExistingRootWebApplicationContext(servletContext);
        if (parent != null) {
            this.logger.info("Root context already created (using as parent).");
            servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, null);
            builder.initializers(new ParentContextApplicationContextInitializer(parent));
        }
        builder.initializers(new ServletContextApplicationContextInitializer(servletContext));
        builder.contextClass(AnnotationConfigEmbeddedWebApplicationContext.class);
        SpringApplicationBuilder builder2 = configure(builder);
        builder2.listeners(new WebEnvironmentPropertySourceInitializer(servletContext));
        SpringApplication application = builder2.build();
        if (application.getSources().isEmpty() && AnnotationUtils.findAnnotation(getClass(), Configuration.class) != null) {
            application.getSources().add(getClass());
        }
        Assert.state(!application.getSources().isEmpty(), "No SpringApplication sources have been defined. Either override the configure method or add an @Configuration annotation");
        if (this.registerErrorPageFilter) {
            application.getSources().add(ErrorPageFilterConfiguration.class);
        }
        return run(application);
    }

    protected SpringApplicationBuilder createSpringApplicationBuilder() {
        return new SpringApplicationBuilder(new Object[0]);
    }

    protected WebApplicationContext run(SpringApplication application) {
        return (WebApplicationContext) application.run(new String[0]);
    }

    private ApplicationContext getExistingRootWebApplicationContext(ServletContext servletContext) {
        Object context = servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        if (context instanceof ApplicationContext) {
            return (ApplicationContext) context;
        }
        return null;
    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder;
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/web/support/SpringBootServletInitializer$WebEnvironmentPropertySourceInitializer.class */
    private static final class WebEnvironmentPropertySourceInitializer implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered {
        private final ServletContext servletContext;

        private WebEnvironmentPropertySourceInitializer(ServletContext servletContext) {
            this.servletContext = servletContext;
        }

        @Override // org.springframework.context.ApplicationListener
        public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
            ConfigurableEnvironment environment = event.getEnvironment();
            if (environment instanceof ConfigurableWebEnvironment) {
                ((ConfigurableWebEnvironment) environment).initPropertySources(this.servletContext, null);
            }
        }

        @Override // org.springframework.core.Ordered
        public int getOrder() {
            return Integer.MIN_VALUE;
        }
    }
}
