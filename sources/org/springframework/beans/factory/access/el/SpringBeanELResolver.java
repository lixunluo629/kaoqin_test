package org.springframework.beans.factory.access.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotWritableException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/access/el/SpringBeanELResolver.class */
public abstract class SpringBeanELResolver extends ELResolver {
    protected final Log logger = LogFactory.getLog(getClass());

    protected abstract BeanFactory getBeanFactory(ELContext eLContext);

    @Override // javax.el.ELResolver
    public Object getValue(ELContext elContext, Object base, Object property) throws ELException {
        if (base == null) {
            String beanName = property.toString();
            BeanFactory bf = getBeanFactory(elContext);
            if (bf.containsBean(beanName)) {
                if (this.logger.isTraceEnabled()) {
                    this.logger.trace("Successfully resolved variable '" + beanName + "' in Spring BeanFactory");
                }
                elContext.setPropertyResolved(true);
                return bf.getBean(beanName);
            }
            return null;
        }
        return null;
    }

    @Override // javax.el.ELResolver
    public Class<?> getType(ELContext elContext, Object base, Object property) throws ELException {
        if (base == null) {
            String beanName = property.toString();
            BeanFactory bf = getBeanFactory(elContext);
            if (bf.containsBean(beanName)) {
                elContext.setPropertyResolved(true);
                return bf.getType(beanName);
            }
            return null;
        }
        return null;
    }

    @Override // javax.el.ELResolver
    public void setValue(ELContext elContext, Object base, Object property, Object value) throws ELException {
        if (base == null) {
            String beanName = property.toString();
            BeanFactory bf = getBeanFactory(elContext);
            if (bf.containsBean(beanName)) {
                if (value == bf.getBean(beanName)) {
                    elContext.setPropertyResolved(true);
                    return;
                }
                throw new PropertyNotWritableException("Variable '" + beanName + "' refers to a Spring bean which by definition is not writable");
            }
        }
    }

    @Override // javax.el.ELResolver
    public boolean isReadOnly(ELContext elContext, Object base, Object property) throws ELException {
        if (base == null) {
            String beanName = property.toString();
            BeanFactory bf = getBeanFactory(elContext);
            if (bf.containsBean(beanName)) {
                return true;
            }
            return false;
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
}
