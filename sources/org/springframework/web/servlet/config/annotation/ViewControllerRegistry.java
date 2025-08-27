package org.springframework.web.servlet.config.annotation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/config/annotation/ViewControllerRegistry.class */
public class ViewControllerRegistry {
    private ApplicationContext applicationContext;
    private final List<ViewControllerRegistration> registrations = new ArrayList(4);
    private final List<RedirectViewControllerRegistration> redirectRegistrations = new ArrayList(10);
    private int order = 1;

    public ViewControllerRegistry(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Deprecated
    public ViewControllerRegistry() {
    }

    public ViewControllerRegistration addViewController(String urlPath) {
        ViewControllerRegistration registration = new ViewControllerRegistration(urlPath);
        registration.setApplicationContext(this.applicationContext);
        this.registrations.add(registration);
        return registration;
    }

    public RedirectViewControllerRegistration addRedirectViewController(String urlPath, String redirectUrl) {
        RedirectViewControllerRegistration registration = new RedirectViewControllerRegistration(urlPath, redirectUrl);
        registration.setApplicationContext(this.applicationContext);
        this.redirectRegistrations.add(registration);
        return registration;
    }

    public void addStatusController(String urlPath, HttpStatus statusCode) {
        ViewControllerRegistration registration = new ViewControllerRegistration(urlPath);
        registration.setApplicationContext(this.applicationContext);
        registration.setStatusCode(statusCode);
        registration.getViewController().setStatusOnly(true);
        this.registrations.add(registration);
    }

    public void setOrder(int order) {
        this.order = order;
    }

    protected SimpleUrlHandlerMapping buildHandlerMapping() {
        if (this.registrations.isEmpty() && this.redirectRegistrations.isEmpty()) {
            return null;
        }
        Map<String, Object> urlMap = new LinkedHashMap<>();
        for (ViewControllerRegistration registration : this.registrations) {
            urlMap.put(registration.getUrlPath(), registration.getViewController());
        }
        for (RedirectViewControllerRegistration registration2 : this.redirectRegistrations) {
            urlMap.put(registration2.getUrlPath(), registration2.getViewController());
        }
        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        handlerMapping.setUrlMap(urlMap);
        handlerMapping.setOrder(this.order);
        return handlerMapping;
    }

    @Deprecated
    protected AbstractHandlerMapping getHandlerMapping() {
        return buildHandlerMapping();
    }

    @Deprecated
    protected void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
