package org.springframework.remoting.rmi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.omg.CORBA.SystemException;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jndi.JndiObjectLocator;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.remoting.RemoteInvocationFailureException;
import org.springframework.remoting.RemoteLookupFailureException;
import org.springframework.remoting.support.DefaultRemoteInvocationFactory;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationFactory;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/remoting/rmi/JndiRmiClientInterceptor.class */
public class JndiRmiClientInterceptor extends JndiObjectLocator implements MethodInterceptor, InitializingBean {
    private Class<?> serviceInterface;
    private Object cachedStub;
    private RemoteInvocationFactory remoteInvocationFactory = new DefaultRemoteInvocationFactory();
    private boolean lookupStubOnStartup = true;
    private boolean cacheStub = true;
    private boolean refreshStubOnConnectFailure = false;
    private boolean exposeAccessContext = false;
    private final Object stubMonitor = new Object();

    public void setServiceInterface(Class<?> serviceInterface) {
        if (serviceInterface != null && !serviceInterface.isInterface()) {
            throw new IllegalArgumentException("'serviceInterface' must be an interface");
        }
        this.serviceInterface = serviceInterface;
    }

    public Class<?> getServiceInterface() {
        return this.serviceInterface;
    }

    public void setRemoteInvocationFactory(RemoteInvocationFactory remoteInvocationFactory) {
        this.remoteInvocationFactory = remoteInvocationFactory;
    }

    public RemoteInvocationFactory getRemoteInvocationFactory() {
        return this.remoteInvocationFactory;
    }

    public void setLookupStubOnStartup(boolean lookupStubOnStartup) {
        this.lookupStubOnStartup = lookupStubOnStartup;
    }

    public void setCacheStub(boolean cacheStub) {
        this.cacheStub = cacheStub;
    }

    public void setRefreshStubOnConnectFailure(boolean refreshStubOnConnectFailure) {
        this.refreshStubOnConnectFailure = refreshStubOnConnectFailure;
    }

    public void setExposeAccessContext(boolean exposeAccessContext) {
        this.exposeAccessContext = exposeAccessContext;
    }

    @Override // org.springframework.jndi.JndiObjectLocator, org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws RemoteLookupFailureException, NamingException, IllegalArgumentException {
        super.afterPropertiesSet();
        prepare();
    }

    public void prepare() throws RemoteLookupFailureException {
        if (this.lookupStubOnStartup) {
            Object remoteObj = lookupStub();
            if (this.logger.isDebugEnabled()) {
                if (remoteObj instanceof RmiInvocationHandler) {
                    this.logger.debug("JNDI RMI object [" + getJndiName() + "] is an RMI invoker");
                } else if (getServiceInterface() != null) {
                    boolean isImpl = getServiceInterface().isInstance(remoteObj);
                    this.logger.debug("Using service interface [" + getServiceInterface().getName() + "] for JNDI RMI object [" + getJndiName() + "] - " + (!isImpl ? "not " : "") + "directly implemented");
                }
            }
            if (this.cacheStub) {
                this.cachedStub = remoteObj;
            }
        }
    }

    protected Object lookupStub() throws RemoteLookupFailureException {
        try {
            Object stub = lookup();
            if (getServiceInterface() != null && !(stub instanceof RmiInvocationHandler)) {
                try {
                    stub = PortableRemoteObject.narrow(stub, getServiceInterface());
                } catch (ClassCastException ex) {
                    throw new RemoteLookupFailureException("Could not narrow RMI stub to service interface [" + getServiceInterface().getName() + "]", ex);
                }
            }
            return stub;
        } catch (NamingException ex2) {
            throw new RemoteLookupFailureException("JNDI lookup for RMI service [" + getJndiName() + "] failed", ex2);
        }
    }

    protected Object getStub() throws RemoteLookupFailureException, NamingException {
        Object obj;
        if (!this.cacheStub || (this.lookupStubOnStartup && !this.refreshStubOnConnectFailure)) {
            return this.cachedStub != null ? this.cachedStub : lookupStub();
        }
        synchronized (this.stubMonitor) {
            if (this.cachedStub == null) {
                this.cachedStub = lookupStub();
            }
            obj = this.cachedStub;
        }
        return obj;
    }

    @Override // org.aopalliance.intercept.MethodInterceptor
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            Object stub = getStub();
            Context ctx = this.exposeAccessContext ? getJndiTemplate().getContext() : null;
            try {
                try {
                    try {
                        Object objDoInvoke = doInvoke(invocation, stub);
                        getJndiTemplate().releaseContext(ctx);
                        return objDoInvoke;
                    } catch (SystemException ex) {
                        if (!isConnectFailure(ex)) {
                            throw ex;
                        }
                        Object objHandleRemoteConnectFailure = handleRemoteConnectFailure(invocation, ex);
                        getJndiTemplate().releaseContext(ctx);
                        return objHandleRemoteConnectFailure;
                    } catch (RemoteException ex2) {
                        if (!isConnectFailure(ex2)) {
                            throw ex2;
                        }
                        Object objHandleRemoteConnectFailure2 = handleRemoteConnectFailure(invocation, ex2);
                        getJndiTemplate().releaseContext(ctx);
                        return objHandleRemoteConnectFailure2;
                    }
                } catch (RemoteConnectFailureException ex3) {
                    Object objHandleRemoteConnectFailure3 = handleRemoteConnectFailure(invocation, ex3);
                    getJndiTemplate().releaseContext(ctx);
                    return objHandleRemoteConnectFailure3;
                }
            } catch (Throwable th) {
                getJndiTemplate().releaseContext(ctx);
                throw th;
            }
        } catch (NamingException ex4) {
            throw new RemoteLookupFailureException("JNDI lookup for RMI service [" + getJndiName() + "] failed", ex4);
        }
    }

    protected boolean isConnectFailure(RemoteException ex) {
        return RmiClientInterceptorUtils.isConnectFailure(ex);
    }

    protected boolean isConnectFailure(SystemException ex) {
        return ex instanceof OBJECT_NOT_EXIST;
    }

    private Object handleRemoteConnectFailure(MethodInvocation invocation, Exception ex) throws Exception {
        if (this.refreshStubOnConnectFailure) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Could not connect to RMI service [" + getJndiName() + "] - retrying", ex);
            } else if (this.logger.isWarnEnabled()) {
                this.logger.warn("Could not connect to RMI service [" + getJndiName() + "] - retrying");
            }
            return refreshAndRetry(invocation);
        }
        throw ex;
    }

    protected Object refreshAndRetry(MethodInvocation invocation) throws Throwable {
        Object freshStub;
        synchronized (this.stubMonitor) {
            this.cachedStub = null;
            freshStub = lookupStub();
            if (this.cacheStub) {
                this.cachedStub = freshStub;
            }
        }
        return doInvoke(invocation, freshStub);
    }

    protected Object doInvoke(MethodInvocation invocation, Object stub) throws Exception {
        if (stub instanceof RmiInvocationHandler) {
            try {
                return doInvoke(invocation, (RmiInvocationHandler) stub);
            } catch (RemoteException ex) {
                throw convertRmiAccessException(ex, invocation.getMethod());
            } catch (SystemException ex2) {
                throw convertCorbaAccessException(ex2, invocation.getMethod());
            } catch (InvocationTargetException ex3) {
                throw ex3.getTargetException();
            } catch (Throwable ex4) {
                throw new RemoteInvocationFailureException("Invocation of method [" + invocation.getMethod() + "] failed in RMI service [" + getJndiName() + "]", ex4);
            }
        }
        try {
            return RmiClientInterceptorUtils.invokeRemoteMethod(invocation, stub);
        } catch (InvocationTargetException ex5) {
            Throwable targetEx = ex5.getTargetException();
            if (targetEx instanceof RemoteException) {
                throw convertRmiAccessException((RemoteException) targetEx, invocation.getMethod());
            }
            if (targetEx instanceof SystemException) {
                throw convertCorbaAccessException((SystemException) targetEx, invocation.getMethod());
            }
            throw targetEx;
        }
    }

    protected Object doInvoke(MethodInvocation methodInvocation, RmiInvocationHandler invocationHandler) throws IllegalAccessException, NoSuchMethodException, RemoteException, InvocationTargetException {
        if (AopUtils.isToStringMethod(methodInvocation.getMethod())) {
            return "RMI invoker proxy for service URL [" + getJndiName() + "]";
        }
        return invocationHandler.invoke(createRemoteInvocation(methodInvocation));
    }

    protected RemoteInvocation createRemoteInvocation(MethodInvocation methodInvocation) {
        return getRemoteInvocationFactory().createRemoteInvocation(methodInvocation);
    }

    private Exception convertRmiAccessException(RemoteException ex, Method method) {
        return RmiClientInterceptorUtils.convertRmiAccessException(method, ex, isConnectFailure(ex), getJndiName());
    }

    private Exception convertCorbaAccessException(SystemException ex, Method method) {
        if (ReflectionUtils.declaresException(method, RemoteException.class)) {
            return new RemoteException("Failed to access CORBA service [" + getJndiName() + "]", ex);
        }
        if (isConnectFailure(ex)) {
            return new RemoteConnectFailureException("Could not connect to CORBA service [" + getJndiName() + "]", ex);
        }
        return new RemoteAccessException("Could not access CORBA service [" + getJndiName() + "]", ex);
    }
}
