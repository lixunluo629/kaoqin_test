package org.springframework.remoting.caucho;

import com.caucho.burlap.client.BurlapProxyFactory;
import com.caucho.burlap.client.BurlapRuntimeException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.remoting.RemoteLookupFailureException;
import org.springframework.remoting.RemoteProxyFailureException;
import org.springframework.remoting.support.UrlBasedRemoteAccessor;
import org.springframework.util.Assert;

@Deprecated
/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/remoting/caucho/BurlapClientInterceptor.class */
public class BurlapClientInterceptor extends UrlBasedRemoteAccessor implements MethodInterceptor {
    private BurlapProxyFactory proxyFactory = new BurlapProxyFactory();
    private Object burlapProxy;

    public void setProxyFactory(BurlapProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory != null ? proxyFactory : new BurlapProxyFactory();
    }

    public void setUsername(String username) {
        this.proxyFactory.setUser(username);
    }

    public void setPassword(String password) {
        this.proxyFactory.setPassword(password);
    }

    public void setOverloadEnabled(boolean overloadEnabled) {
        this.proxyFactory.setOverloadEnabled(overloadEnabled);
    }

    @Override // org.springframework.remoting.support.UrlBasedRemoteAccessor, org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws RemoteLookupFailureException {
        super.afterPropertiesSet();
        prepare();
    }

    public void prepare() throws RemoteLookupFailureException {
        try {
            this.burlapProxy = createBurlapProxy(this.proxyFactory);
        } catch (MalformedURLException ex) {
            throw new RemoteLookupFailureException("Service URL [" + getServiceUrl() + "] is invalid", ex);
        }
    }

    protected Object createBurlapProxy(BurlapProxyFactory proxyFactory) throws MalformedURLException {
        Assert.notNull(getServiceInterface(), "Property 'serviceInterface' is required");
        return proxyFactory.create(getServiceInterface(), getServiceUrl());
    }

    @Override // org.aopalliance.intercept.MethodInterceptor
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (this.burlapProxy == null) {
            throw new IllegalStateException("BurlapClientInterceptor is not properly initialized - invoke 'prepare' before attempting any operations");
        }
        ClassLoader originalClassLoader = overrideThreadContextClassLoader();
        try {
            try {
                Object objInvoke = invocation.getMethod().invoke(this.burlapProxy, invocation.getArguments());
                resetThreadContextClassLoader(originalClassLoader);
                return objInvoke;
            } catch (InvocationTargetException ex) {
                Throwable targetEx = ex.getTargetException();
                if (targetEx instanceof BurlapRuntimeException) {
                    Throwable cause = targetEx.getCause();
                    throw convertBurlapAccessException(cause != null ? cause : targetEx);
                }
                if (targetEx instanceof UndeclaredThrowableException) {
                    UndeclaredThrowableException utex = (UndeclaredThrowableException) targetEx;
                    throw convertBurlapAccessException(utex.getUndeclaredThrowable());
                }
                throw targetEx;
            } catch (Throwable ex2) {
                throw new RemoteProxyFailureException("Failed to invoke Burlap proxy for remote service [" + getServiceUrl() + "]", ex2);
            }
        } catch (Throwable th) {
            resetThreadContextClassLoader(originalClassLoader);
            throw th;
        }
    }

    protected RemoteAccessException convertBurlapAccessException(Throwable ex) {
        if (ex instanceof ConnectException) {
            return new RemoteConnectFailureException("Cannot connect to Burlap remote service at [" + getServiceUrl() + "]", ex);
        }
        return new RemoteAccessException("Cannot access Burlap remote service at [" + getServiceUrl() + "]", ex);
    }
}
