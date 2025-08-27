package org.springframework.jca.endpoint;

import javax.resource.ResourceException;
import javax.resource.spi.UnavailableException;
import javax.resource.spi.endpoint.MessageEndpoint;
import javax.transaction.xa.XAResource;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DelegatingIntroductionInterceptor;
import org.springframework.jca.endpoint.AbstractMessageEndpointFactory;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/jca/endpoint/GenericMessageEndpointFactory.class */
public class GenericMessageEndpointFactory extends AbstractMessageEndpointFactory {
    private Object messageListener;

    public void setMessageListener(Object messageListener) {
        this.messageListener = messageListener;
    }

    @Override // org.springframework.jca.endpoint.AbstractMessageEndpointFactory
    public MessageEndpoint createEndpoint(XAResource xaResource) throws UnavailableException {
        GenericMessageEndpoint endpoint = (GenericMessageEndpoint) super.createEndpoint(xaResource);
        ProxyFactory proxyFactory = new ProxyFactory(this.messageListener);
        DelegatingIntroductionInterceptor introduction = new DelegatingIntroductionInterceptor(endpoint);
        introduction.suppressInterface(MethodInterceptor.class);
        proxyFactory.addAdvice(introduction);
        return (MessageEndpoint) proxyFactory.getProxy();
    }

    @Override // org.springframework.jca.endpoint.AbstractMessageEndpointFactory
    protected AbstractMessageEndpointFactory.AbstractMessageEndpoint createEndpointInternal() throws UnavailableException {
        return new GenericMessageEndpoint();
    }

    /* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/jca/endpoint/GenericMessageEndpointFactory$GenericMessageEndpoint.class */
    private class GenericMessageEndpoint extends AbstractMessageEndpointFactory.AbstractMessageEndpoint implements MethodInterceptor {
        private GenericMessageEndpoint() {
            super();
        }

        @Override // org.aopalliance.intercept.MethodInterceptor
        public Object invoke(MethodInvocation methodInvocation) throws Exception {
            Throwable endpointEx = null;
            boolean applyDeliveryCalls = !hasBeforeDeliveryBeenCalled();
            if (applyDeliveryCalls) {
                try {
                    beforeDelivery(null);
                } catch (ResourceException ex) {
                    throw adaptExceptionIfNecessary(methodInvocation, ex);
                }
            }
            try {
                try {
                    Object objProceed = methodInvocation.proceed();
                    if (applyDeliveryCalls) {
                        try {
                            afterDelivery();
                        } catch (ResourceException ex2) {
                            if (0 == 0) {
                                throw adaptExceptionIfNecessary(methodInvocation, ex2);
                            }
                        }
                    }
                    return objProceed;
                } finally {
                }
            } catch (Throwable th) {
                if (applyDeliveryCalls) {
                    try {
                        afterDelivery();
                    } catch (ResourceException ex3) {
                        if (endpointEx == null) {
                            throw adaptExceptionIfNecessary(methodInvocation, ex3);
                        }
                    }
                }
                throw th;
            }
        }

        private Exception adaptExceptionIfNecessary(MethodInvocation methodInvocation, ResourceException ex) {
            if (ReflectionUtils.declaresException(methodInvocation.getMethod(), ex.getClass())) {
                return ex;
            }
            return new InternalResourceException(ex);
        }

        @Override // org.springframework.jca.endpoint.AbstractMessageEndpointFactory.AbstractMessageEndpoint
        protected ClassLoader getEndpointClassLoader() {
            return GenericMessageEndpointFactory.this.messageListener.getClass().getClassLoader();
        }
    }

    /* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/jca/endpoint/GenericMessageEndpointFactory$InternalResourceException.class */
    public static class InternalResourceException extends RuntimeException {
        protected InternalResourceException(ResourceException cause) {
            super((Throwable) cause);
        }
    }
}
