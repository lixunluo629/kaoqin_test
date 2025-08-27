package org.springframework.web.servlet.config.annotation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.core.OrderComparator;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/config/annotation/InterceptorRegistry.class */
public class InterceptorRegistry {
    private final List<InterceptorRegistration> registrations = new ArrayList();
    private static final Comparator<Object> INTERCEPTOR_ORDER_COMPARATOR = OrderComparator.INSTANCE.withSourceProvider(new OrderComparator.OrderSourceProvider() { // from class: org.springframework.web.servlet.config.annotation.InterceptorRegistry.1
        @Override // org.springframework.core.OrderComparator.OrderSourceProvider
        public Object getOrderSource(Object object) {
            if (object instanceof InterceptorRegistration) {
                return ((InterceptorRegistration) object).toOrdered();
            }
            return null;
        }
    });

    public InterceptorRegistration addInterceptor(HandlerInterceptor interceptor) {
        InterceptorRegistration registration = new InterceptorRegistration(interceptor);
        this.registrations.add(registration);
        return registration;
    }

    public InterceptorRegistration addWebRequestInterceptor(WebRequestInterceptor interceptor) {
        WebRequestHandlerInterceptorAdapter adapted = new WebRequestHandlerInterceptorAdapter(interceptor);
        InterceptorRegistration registration = new InterceptorRegistration(adapted);
        this.registrations.add(registration);
        return registration;
    }

    protected List<Object> getInterceptors() {
        Collections.sort(this.registrations, INTERCEPTOR_ORDER_COMPARATOR);
        List<Object> result = new ArrayList<>(this.registrations.size());
        for (InterceptorRegistration registration : this.registrations) {
            result.add(registration.getInterceptor());
        }
        return result;
    }
}
