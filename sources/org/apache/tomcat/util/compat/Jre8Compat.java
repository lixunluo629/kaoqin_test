package org.apache.tomcat.util.compat;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.security.KeyStore;
import java.util.Collections;
import java.util.Map;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/compat/Jre8Compat.class */
class Jre8Compat extends JreCompat {
    private static final int RUNTIME_MAJOR_VERSION = 8;
    private static final Method setUseCipherSuitesOrderMethod;
    private static final Constructor<?> domainLoadStoreParameterConstructor;

    Jre8Compat() {
    }

    static {
        Method m1 = null;
        Constructor<?> c2 = null;
        try {
            Class<?> clazz1 = Class.forName("javax.net.ssl.SSLParameters");
            m1 = clazz1.getMethod("setUseCipherSuitesOrder", Boolean.TYPE);
            Class<?> clazz2 = Class.forName("java.security.DomainLoadStoreParameter");
            c2 = clazz2.getConstructor(URI.class, Map.class);
        } catch (ClassNotFoundException e) {
        } catch (NoSuchMethodException e2) {
        } catch (SecurityException e3) {
        }
        setUseCipherSuitesOrderMethod = m1;
        domainLoadStoreParameterConstructor = c2;
    }

    static boolean isSupported() {
        return setUseCipherSuitesOrderMethod != null;
    }

    @Override // org.apache.tomcat.util.compat.JreCompat
    public void setUseServerCipherSuitesOrder(SSLEngine engine, boolean useCipherSuitesOrder) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        SSLParameters sslParameters = engine.getSSLParameters();
        try {
            setUseCipherSuitesOrderMethod.invoke(sslParameters, Boolean.valueOf(useCipherSuitesOrder));
            engine.setSSLParameters(sslParameters);
        } catch (IllegalAccessException e) {
            throw new UnsupportedOperationException(e);
        } catch (IllegalArgumentException e2) {
            throw new UnsupportedOperationException(e2);
        } catch (InvocationTargetException e3) {
            throw new UnsupportedOperationException(e3);
        }
    }

    @Override // org.apache.tomcat.util.compat.JreCompat
    public KeyStore.LoadStoreParameter getDomainLoadStoreParameter(URI uri) {
        try {
            return (KeyStore.LoadStoreParameter) domainLoadStoreParameterConstructor.newInstance(uri, Collections.EMPTY_MAP);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override // org.apache.tomcat.util.compat.JreCompat
    public int jarFileRuntimeMajorVersion() {
        return 8;
    }
}
