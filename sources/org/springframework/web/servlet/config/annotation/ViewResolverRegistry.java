package org.springframework.web.servlet.config.annotation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerProperties;
import org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import org.springframework.web.servlet.view.groovy.GroovyMarkupConfigurer;
import org.springframework.web.servlet.view.groovy.GroovyMarkupViewResolver;
import org.springframework.web.servlet.view.script.ScriptTemplateConfigurer;
import org.springframework.web.servlet.view.script.ScriptTemplateViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/config/annotation/ViewResolverRegistry.class */
public class ViewResolverRegistry {
    private ContentNegotiationManager contentNegotiationManager;
    private ApplicationContext applicationContext;
    private ContentNegotiatingViewResolver contentNegotiatingResolver;
    private final List<ViewResolver> viewResolvers = new ArrayList(4);
    private Integer order;

    public ViewResolverRegistry(ContentNegotiationManager contentNegotiationManager, ApplicationContext context) {
        this.contentNegotiationManager = contentNegotiationManager;
        this.applicationContext = context;
    }

    @Deprecated
    public ViewResolverRegistry() {
    }

    public boolean hasRegistrations() {
        return (this.contentNegotiatingResolver == null && this.viewResolvers.isEmpty()) ? false : true;
    }

    public void enableContentNegotiation(View... defaultViews) {
        initContentNegotiatingViewResolver(defaultViews);
    }

    public void enableContentNegotiation(boolean useNotAcceptableStatus, View... defaultViews) {
        initContentNegotiatingViewResolver(defaultViews);
        this.contentNegotiatingResolver.setUseNotAcceptableStatusCode(useNotAcceptableStatus);
    }

    private void initContentNegotiatingViewResolver(View[] defaultViews) {
        this.order = Integer.valueOf(this.order != null ? this.order.intValue() : Integer.MIN_VALUE);
        if (this.contentNegotiatingResolver != null) {
            if (!ObjectUtils.isEmpty((Object[]) defaultViews) && !CollectionUtils.isEmpty(this.contentNegotiatingResolver.getDefaultViews())) {
                List<View> views = new ArrayList<>(this.contentNegotiatingResolver.getDefaultViews());
                views.addAll(Arrays.asList(defaultViews));
                this.contentNegotiatingResolver.setDefaultViews(views);
                return;
            }
            return;
        }
        this.contentNegotiatingResolver = new ContentNegotiatingViewResolver();
        this.contentNegotiatingResolver.setDefaultViews(Arrays.asList(defaultViews));
        this.contentNegotiatingResolver.setViewResolvers(this.viewResolvers);
        this.contentNegotiatingResolver.setContentNegotiationManager(this.contentNegotiationManager);
    }

    public UrlBasedViewResolverRegistration jsp() {
        return jsp("/WEB-INF/", ".jsp");
    }

    public UrlBasedViewResolverRegistration jsp(String prefix, String suffix) {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix(prefix);
        resolver.setSuffix(suffix);
        this.viewResolvers.add(resolver);
        return new UrlBasedViewResolverRegistration(resolver);
    }

    public UrlBasedViewResolverRegistration tiles() {
        if (!checkBeanOfType(TilesConfigurer.class)) {
            throw new BeanInitializationException("In addition to a Tiles view resolver there must also be a single TilesConfigurer bean in this web application context (or its parent).");
        }
        TilesRegistration registration = new TilesRegistration();
        this.viewResolvers.add(registration.getViewResolver());
        return registration;
    }

    public UrlBasedViewResolverRegistration freeMarker() {
        if (!checkBeanOfType(FreeMarkerConfigurer.class)) {
            throw new BeanInitializationException("In addition to a FreeMarker view resolver there must also be a single FreeMarkerConfig bean in this web application context (or its parent): FreeMarkerConfigurer is the usual implementation. This bean may be given any name.");
        }
        FreeMarkerRegistration registration = new FreeMarkerRegistration();
        this.viewResolvers.add(registration.getViewResolver());
        return registration;
    }

    @Deprecated
    public UrlBasedViewResolverRegistration velocity() {
        if (!checkBeanOfType(VelocityConfigurer.class)) {
            throw new BeanInitializationException("In addition to a Velocity view resolver there must also be a single VelocityConfig bean in this web application context (or its parent): VelocityConfigurer is the usual implementation. This bean may be given any name.");
        }
        VelocityRegistration registration = new VelocityRegistration();
        this.viewResolvers.add(registration.getViewResolver());
        return registration;
    }

    public UrlBasedViewResolverRegistration groovy() {
        if (!checkBeanOfType(GroovyMarkupConfigurer.class)) {
            throw new BeanInitializationException("In addition to a Groovy markup view resolver there must also be a single GroovyMarkupConfig bean in this web application context (or its parent): GroovyMarkupConfigurer is the usual implementation. This bean may be given any name.");
        }
        GroovyMarkupRegistration registration = new GroovyMarkupRegistration();
        this.viewResolvers.add(registration.getViewResolver());
        return registration;
    }

    public UrlBasedViewResolverRegistration scriptTemplate() {
        if (!checkBeanOfType(ScriptTemplateConfigurer.class)) {
            throw new BeanInitializationException("In addition to a script template view resolver there must also be a single ScriptTemplateConfig bean in this web application context (or its parent): ScriptTemplateConfigurer is the usual implementation. This bean may be given any name.");
        }
        ScriptRegistration registration = new ScriptRegistration();
        this.viewResolvers.add(registration.getViewResolver());
        return registration;
    }

    public void beanName() {
        BeanNameViewResolver resolver = new BeanNameViewResolver();
        this.viewResolvers.add(resolver);
    }

    public void viewResolver(ViewResolver viewResolver) {
        if (viewResolver instanceof ContentNegotiatingViewResolver) {
            throw new BeanInitializationException("addViewResolver cannot be used to configure a ContentNegotiatingViewResolver. Please use the method enableContentNegotiation instead.");
        }
        this.viewResolvers.add(viewResolver);
    }

    public void order(int order) {
        this.order = Integer.valueOf(order);
    }

    protected int getOrder() {
        if (this.order != null) {
            return this.order.intValue();
        }
        return Integer.MAX_VALUE;
    }

    protected List<ViewResolver> getViewResolvers() {
        if (this.contentNegotiatingResolver != null) {
            return Collections.singletonList(this.contentNegotiatingResolver);
        }
        return this.viewResolvers;
    }

    private boolean checkBeanOfType(Class<?> beanType) {
        return this.applicationContext == null || !ObjectUtils.isEmpty((Object[]) BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this.applicationContext, beanType, false, false));
    }

    @Deprecated
    protected boolean hasBeanOfType(Class<?> beanType) {
        return !ObjectUtils.isEmpty((Object[]) BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this.applicationContext, beanType, false, false));
    }

    @Deprecated
    protected void setContentNegotiationManager(ContentNegotiationManager contentNegotiationManager) {
        this.contentNegotiationManager = contentNegotiationManager;
    }

    @Deprecated
    protected void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/config/annotation/ViewResolverRegistry$TilesRegistration.class */
    private static class TilesRegistration extends UrlBasedViewResolverRegistration {
        public TilesRegistration() {
            super(new TilesViewResolver());
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/config/annotation/ViewResolverRegistry$VelocityRegistration.class */
    private static class VelocityRegistration extends UrlBasedViewResolverRegistration {
        public VelocityRegistration() {
            super(new VelocityViewResolver());
            getViewResolver().setSuffix(".vm");
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/config/annotation/ViewResolverRegistry$FreeMarkerRegistration.class */
    private static class FreeMarkerRegistration extends UrlBasedViewResolverRegistration {
        public FreeMarkerRegistration() {
            super(new FreeMarkerViewResolver());
            getViewResolver().setSuffix(FreeMarkerProperties.DEFAULT_SUFFIX);
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/config/annotation/ViewResolverRegistry$GroovyMarkupRegistration.class */
    private static class GroovyMarkupRegistration extends UrlBasedViewResolverRegistration {
        public GroovyMarkupRegistration() {
            super(new GroovyMarkupViewResolver());
            getViewResolver().setSuffix(GroovyTemplateProperties.DEFAULT_SUFFIX);
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/config/annotation/ViewResolverRegistry$ScriptRegistration.class */
    private static class ScriptRegistration extends UrlBasedViewResolverRegistration {
        public ScriptRegistration() {
            super(new ScriptTemplateViewResolver());
            getViewResolver();
        }
    }
}
