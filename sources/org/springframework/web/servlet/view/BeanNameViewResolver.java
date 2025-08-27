package org.springframework.web.servlet.view;

import java.util.Locale;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/BeanNameViewResolver.class */
public class BeanNameViewResolver extends WebApplicationObjectSupport implements ViewResolver, Ordered {
    private int order = Integer.MAX_VALUE;

    public void setOrder(int order) {
        this.order = order;
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return this.order;
    }

    @Override // org.springframework.web.servlet.ViewResolver
    public View resolveViewName(String viewName, Locale locale) throws BeansException {
        ApplicationContext context = getApplicationContext();
        if (!context.containsBean(viewName)) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("No matching bean found for view name '" + viewName + "'");
                return null;
            }
            return null;
        }
        if (!context.isTypeMatch(viewName, View.class)) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Found matching bean for view name '" + viewName + "' - to be ignored since it does not implement View");
                return null;
            }
            return null;
        }
        return (View) context.getBean(viewName, View.class);
    }
}
