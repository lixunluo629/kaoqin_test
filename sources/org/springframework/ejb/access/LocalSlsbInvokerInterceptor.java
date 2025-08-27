package org.springframework.ejb.access;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.naming.NamingException;
import org.aopalliance.intercept.MethodInvocation;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/ejb/access/LocalSlsbInvokerInterceptor.class */
public class LocalSlsbInvokerInterceptor extends AbstractSlsbInvokerInterceptor {
    private volatile boolean homeAsComponent = false;

    @Override // org.springframework.ejb.access.AbstractSlsbInvokerInterceptor
    public Object invokeInContext(MethodInvocation invocation) throws Throwable {
        Object ejb = null;
        try {
            try {
                try {
                    Object ejb2 = getSessionBeanInstance();
                    Method method = invocation.getMethod();
                    if (method.getDeclaringClass().isInstance(ejb2)) {
                        Object objInvoke = method.invoke(ejb2, invocation.getArguments());
                        if (ejb2 instanceof EJBLocalObject) {
                            releaseSessionBeanInstance((EJBLocalObject) ejb2);
                        }
                        return objInvoke;
                    }
                    Method ejbMethod = ejb2.getClass().getMethod(method.getName(), method.getParameterTypes());
                    Object objInvoke2 = ejbMethod.invoke(ejb2, invocation.getArguments());
                    if (ejb2 instanceof EJBLocalObject) {
                        releaseSessionBeanInstance((EJBLocalObject) ejb2);
                    }
                    return objInvoke2;
                } catch (NamingException ex) {
                    throw new EjbAccessException("Failed to locate local EJB [" + getJndiName() + "]", ex);
                } catch (IllegalAccessException ex2) {
                    throw new EjbAccessException("Could not access method [" + invocation.getMethod().getName() + "] of local EJB [" + getJndiName() + "]", ex2);
                }
            } catch (InvocationTargetException ex3) {
                Throwable targetEx = ex3.getTargetException();
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Method of local EJB [" + getJndiName() + "] threw exception", targetEx);
                }
                if (targetEx instanceof CreateException) {
                    throw new EjbAccessException("Could not create local EJB [" + getJndiName() + "]", targetEx);
                }
                throw targetEx;
            }
        } catch (Throwable th) {
            if (ejb instanceof EJBLocalObject) {
                releaseSessionBeanInstance((EJBLocalObject) null);
            }
            throw th;
        }
    }

    @Override // org.springframework.ejb.access.AbstractSlsbInvokerInterceptor
    protected Method getCreateMethod(Object home) throws EjbAccessException {
        if (this.homeAsComponent) {
            return null;
        }
        if (!(home instanceof EJBLocalHome)) {
            this.homeAsComponent = true;
            return null;
        }
        return super.getCreateMethod(home);
    }

    protected Object getSessionBeanInstance() throws NamingException, InvocationTargetException {
        return newSessionBeanInstance();
    }

    protected void releaseSessionBeanInstance(EJBLocalObject ejb) {
        removeSessionBeanInstance(ejb);
    }

    protected Object newSessionBeanInstance() throws NamingException, InvocationTargetException {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Trying to create reference to local EJB");
        }
        Object ejbInstance = create();
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Obtained reference to local EJB: " + ejbInstance);
        }
        return ejbInstance;
    }

    protected void removeSessionBeanInstance(EJBLocalObject ejb) {
        if (ejb != null && !this.homeAsComponent) {
            try {
                ejb.remove();
            } catch (Throwable ex) {
                this.logger.warn("Could not invoke 'remove' on local EJB proxy", ex);
            }
        }
    }
}
