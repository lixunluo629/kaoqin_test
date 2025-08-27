package org.springframework.web.servlet.view;

import java.util.Locale;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.View;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/XmlViewResolver.class */
public class XmlViewResolver extends AbstractCachingViewResolver implements Ordered, InitializingBean, DisposableBean {
    public static final String DEFAULT_LOCATION = "/WEB-INF/views.xml";
    private Resource location;
    private ConfigurableApplicationContext cachedFactory;
    private int order = Integer.MAX_VALUE;

    public void setLocation(Resource location) {
        this.location = location;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return this.order;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws BeansException {
        if (isCache()) {
            initFactory();
        }
    }

    @Override // org.springframework.web.servlet.view.AbstractCachingViewResolver
    protected Object getCacheKey(String viewName, Locale locale) {
        return viewName;
    }

    @Override // org.springframework.web.servlet.view.AbstractCachingViewResolver
    protected View loadView(String viewName, Locale locale) throws BeansException {
        BeanFactory factory = initFactory();
        try {
            return (View) factory.getBean(viewName, View.class);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    protected synchronized BeanFactory initFactory() throws BeansException {
        if (this.cachedFactory != null) {
            return this.cachedFactory;
        }
        Resource actualLocation = this.location;
        if (actualLocation == null) {
            actualLocation = getApplicationContext().getResource(DEFAULT_LOCATION);
        }
        GenericWebApplicationContext factory = new GenericWebApplicationContext();
        factory.setParent(getApplicationContext());
        factory.setServletContext(getServletContext());
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.setEnvironment(getApplicationContext().getEnvironment());
        reader.setEntityResolver(new ResourceEntityResolver(getApplicationContext()));
        reader.loadBeanDefinitions(actualLocation);
        factory.refresh();
        if (isCache()) {
            this.cachedFactory = factory;
        }
        return factory;
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() throws BeansException {
        if (this.cachedFactory != null) {
            this.cachedFactory.close();
        }
    }
}
