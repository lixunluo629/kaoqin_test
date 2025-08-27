package org.springframework.web.servlet.handler;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/handler/AbstractDetectingUrlHandlerMapping.class */
public abstract class AbstractDetectingUrlHandlerMapping extends AbstractUrlHandlerMapping {
    private boolean detectHandlersInAncestorContexts = false;

    protected abstract String[] determineUrlsForHandler(String str);

    public void setDetectHandlersInAncestorContexts(boolean detectHandlersInAncestorContexts) {
        this.detectHandlersInAncestorContexts = detectHandlersInAncestorContexts;
    }

    @Override // org.springframework.web.servlet.handler.AbstractHandlerMapping, org.springframework.context.support.ApplicationObjectSupport
    public void initApplicationContext() throws BeansException {
        super.initApplicationContext();
        detectHandlers();
    }

    protected void detectHandlers() throws BeansException {
        String[] beanNamesForType;
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Looking for URL mappings in application context: " + getApplicationContext());
        }
        if (this.detectHandlersInAncestorContexts) {
            beanNamesForType = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(getApplicationContext(), (Class<?>) Object.class);
        } else {
            beanNamesForType = getApplicationContext().getBeanNamesForType(Object.class);
        }
        String[] beanNames = beanNamesForType;
        for (String beanName : beanNames) {
            String[] urls = determineUrlsForHandler(beanName);
            if (!ObjectUtils.isEmpty((Object[]) urls)) {
                registerHandler(urls, beanName);
            } else if (this.logger.isDebugEnabled()) {
                this.logger.debug("Rejected bean name '" + beanName + "': no URL paths identified");
            }
        }
    }
}
