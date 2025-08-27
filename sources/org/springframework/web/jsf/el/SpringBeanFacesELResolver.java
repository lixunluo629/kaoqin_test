package org.springframework.web.jsf.el;

import javax.el.ELContext;
import javax.faces.context.FacesContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.el.SpringBeanELResolver;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/jsf/el/SpringBeanFacesELResolver.class */
public class SpringBeanFacesELResolver extends SpringBeanELResolver {
    @Override // org.springframework.beans.factory.access.el.SpringBeanELResolver
    protected BeanFactory getBeanFactory(ELContext elContext) {
        return getWebApplicationContext(elContext);
    }

    protected WebApplicationContext getWebApplicationContext(ELContext elContext) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return FacesContextUtils.getRequiredWebApplicationContext(facesContext);
    }
}
