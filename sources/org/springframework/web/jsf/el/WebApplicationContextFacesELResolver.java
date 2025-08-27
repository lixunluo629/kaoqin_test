package org.springframework.web.jsf.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.faces.context.FacesContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/jsf/el/WebApplicationContextFacesELResolver.class */
public class WebApplicationContextFacesELResolver extends ELResolver {
    public static final String WEB_APPLICATION_CONTEXT_VARIABLE_NAME = "webApplicationContext";
    protected final Log logger = LogFactory.getLog(getClass());

    @Override // javax.el.ELResolver
    public Object getValue(ELContext elContext, Object base, Object property) throws ELException {
        if (base != null) {
            if (base instanceof WebApplicationContext) {
                WebApplicationContext wac = (WebApplicationContext) base;
                String beanName = property.toString();
                if (this.logger.isTraceEnabled()) {
                    this.logger.trace("Attempting to resolve property '" + beanName + "' in root WebApplicationContext");
                }
                if (wac.containsBean(beanName)) {
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Successfully resolved property '" + beanName + "' in root WebApplicationContext");
                    }
                    elContext.setPropertyResolved(true);
                    try {
                        return wac.getBean(beanName);
                    } catch (BeansException ex) {
                        throw new ELException(ex);
                    }
                }
                return null;
            }
            return null;
        }
        if (WEB_APPLICATION_CONTEXT_VARIABLE_NAME.equals(property)) {
            elContext.setPropertyResolved(true);
            return getWebApplicationContext(elContext);
        }
        return null;
    }

    @Override // javax.el.ELResolver
    public Class<?> getType(ELContext elContext, Object base, Object property) throws ELException {
        if (base != null) {
            if (base instanceof WebApplicationContext) {
                WebApplicationContext wac = (WebApplicationContext) base;
                String beanName = property.toString();
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Attempting to resolve property '" + beanName + "' in root WebApplicationContext");
                }
                if (wac.containsBean(beanName)) {
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Successfully resolved property '" + beanName + "' in root WebApplicationContext");
                    }
                    elContext.setPropertyResolved(true);
                    try {
                        return wac.getType(beanName);
                    } catch (BeansException ex) {
                        throw new ELException(ex);
                    }
                }
                return null;
            }
            return null;
        }
        if (WEB_APPLICATION_CONTEXT_VARIABLE_NAME.equals(property)) {
            elContext.setPropertyResolved(true);
            return WebApplicationContext.class;
        }
        return null;
    }

    @Override // javax.el.ELResolver
    public void setValue(ELContext elContext, Object base, Object property, Object value) throws ELException {
    }

    @Override // javax.el.ELResolver
    public boolean isReadOnly(ELContext elContext, Object base, Object property) throws ELException {
        if (base instanceof WebApplicationContext) {
            elContext.setPropertyResolved(true);
            return true;
        }
        return false;
    }

    @Override // javax.el.ELResolver
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext elContext, Object base) {
        return null;
    }

    @Override // javax.el.ELResolver
    public Class<?> getCommonPropertyType(ELContext elContext, Object base) {
        return Object.class;
    }

    protected WebApplicationContext getWebApplicationContext(ELContext elContext) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return FacesContextUtils.getRequiredWebApplicationContext(facesContext);
    }
}
