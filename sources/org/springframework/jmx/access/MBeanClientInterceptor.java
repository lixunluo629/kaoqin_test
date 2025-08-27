package org.springframework.jmx.access;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.management.Attribute;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.JMException;
import javax.management.JMRuntimeException;
import javax.management.JMX;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.OperationsException;
import javax.management.ReflectionException;
import javax.management.RuntimeErrorException;
import javax.management.RuntimeMBeanException;
import javax.management.RuntimeOperationsException;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;
import javax.management.remote.JMXServiceURL;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.CollectionFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.jmx.support.JmxUtils;
import org.springframework.jmx.support.ObjectNameManager;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jmx/access/MBeanClientInterceptor.class */
public class MBeanClientInterceptor implements MethodInterceptor, BeanClassLoaderAware, InitializingBean, DisposableBean {
    private MBeanServerConnection server;
    private JMXServiceURL serviceUrl;
    private Map<String, ?> environment;
    private String agentId;
    private ObjectName objectName;
    private Class<?> managementInterface;
    private MBeanServerConnection serverToUse;
    private MBeanServerInvocationHandler invocationHandler;
    private Map<String, MBeanAttributeInfo> allowedAttributes;
    private Map<MethodCacheKey, MBeanOperationInfo> allowedOperations;
    protected final Log logger = LogFactory.getLog(getClass());
    private boolean connectOnStartup = true;
    private boolean refreshOnConnectFailure = false;
    private boolean useStrictCasing = true;
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
    private final ConnectorDelegate connector = new ConnectorDelegate();
    private final Map<Method, String[]> signatureCache = new HashMap();
    private final Object preparationMonitor = new Object();

    public void setServer(MBeanServerConnection server) {
        this.server = server;
    }

    public void setServiceUrl(String url) throws MalformedURLException {
        this.serviceUrl = new JMXServiceURL(url);
    }

    public void setEnvironment(Map<String, ?> environment) {
        this.environment = environment;
    }

    public Map<String, ?> getEnvironment() {
        return this.environment;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public void setConnectOnStartup(boolean connectOnStartup) {
        this.connectOnStartup = connectOnStartup;
    }

    public void setRefreshOnConnectFailure(boolean refreshOnConnectFailure) {
        this.refreshOnConnectFailure = refreshOnConnectFailure;
    }

    public void setObjectName(Object objectName) throws MalformedObjectNameException {
        this.objectName = ObjectNameManager.getInstance(objectName);
    }

    public void setUseStrictCasing(boolean useStrictCasing) {
        this.useStrictCasing = useStrictCasing;
    }

    public void setManagementInterface(Class<?> managementInterface) {
        this.managementInterface = managementInterface;
    }

    protected final Class<?> getManagementInterface() {
        return this.managementInterface;
    }

    @Override // org.springframework.beans.factory.BeanClassLoaderAware
    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        if (this.server != null && this.refreshOnConnectFailure) {
            throw new IllegalArgumentException("'refreshOnConnectFailure' does not work when setting a 'server' reference. Prefer 'serviceUrl' etc instead.");
        }
        if (this.connectOnStartup) {
            prepare();
        }
    }

    public void prepare() {
        synchronized (this.preparationMonitor) {
            if (this.server != null) {
                this.serverToUse = this.server;
            } else {
                this.serverToUse = null;
                this.serverToUse = this.connector.connect(this.serviceUrl, this.environment, this.agentId);
            }
            this.invocationHandler = null;
            if (this.useStrictCasing) {
                this.invocationHandler = new MBeanServerInvocationHandler(this.serverToUse, this.objectName, this.managementInterface != null && JMX.isMXBeanInterface(this.managementInterface));
            } else {
                retrieveMBeanInfo();
            }
        }
    }

    private void retrieveMBeanInfo() throws MBeanInfoRetrievalException {
        try {
            MBeanInfo info = this.serverToUse.getMBeanInfo(this.objectName);
            MBeanAttributeInfo[] attributeInfo = info.getAttributes();
            this.allowedAttributes = new HashMap(attributeInfo.length);
            for (MBeanAttributeInfo infoEle : attributeInfo) {
                this.allowedAttributes.put(infoEle.getName(), infoEle);
            }
            MBeanOperationInfo[] operationInfo = info.getOperations();
            this.allowedOperations = new HashMap(operationInfo.length);
            for (MBeanOperationInfo infoEle2 : operationInfo) {
                Class<?>[] paramTypes = JmxUtils.parameterInfoToTypes(infoEle2.getSignature(), this.beanClassLoader);
                this.allowedOperations.put(new MethodCacheKey(infoEle2.getName(), paramTypes), infoEle2);
            }
        } catch (IOException ex) {
            throw new MBeanInfoRetrievalException("An IOException occurred when communicating with the MBeanServer. It is likely that you are communicating with a remote MBeanServer. Check the inner exception for exact details.", ex);
        } catch (InstanceNotFoundException ex2) {
            throw new MBeanInfoRetrievalException("Unable to obtain MBean info for bean [" + this.objectName + "]: it is likely that this bean was unregistered during the proxy creation process", ex2);
        } catch (IntrospectionException ex3) {
            throw new MBeanInfoRetrievalException("Unable to obtain MBean info for bean [" + this.objectName + "]", ex3);
        } catch (ReflectionException ex4) {
            throw new MBeanInfoRetrievalException("Unable to read MBean info for bean [ " + this.objectName + "]", ex4);
        } catch (ClassNotFoundException ex5) {
            throw new MBeanInfoRetrievalException("Unable to locate class specified in method signature", ex5);
        }
    }

    protected boolean isPrepared() {
        boolean z;
        synchronized (this.preparationMonitor) {
            z = this.serverToUse != null;
        }
        return z;
    }

    @Override // org.aopalliance.intercept.MethodInterceptor
    public Object invoke(MethodInvocation invocation) throws Throwable {
        synchronized (this.preparationMonitor) {
            if (!isPrepared()) {
                prepare();
            }
        }
        try {
            return doInvoke(invocation);
        } catch (IOException ex) {
            return handleConnectFailure(invocation, ex);
        } catch (MBeanConnectFailureException ex2) {
            return handleConnectFailure(invocation, ex2);
        }
    }

    protected Object handleConnectFailure(MethodInvocation invocation, Exception ex) throws Exception {
        if (this.refreshOnConnectFailure) {
            if (this.logger.isDebugEnabled()) {
                this.logger.warn("Could not connect to JMX server - retrying", ex);
            } else if (this.logger.isWarnEnabled()) {
                this.logger.warn("Could not connect to JMX server - retrying");
            }
            prepare();
            return doInvoke(invocation);
        }
        throw ex;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.management.JMException */
    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.management.JMRuntimeException */
    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.management.OperationsException */
    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.management.RuntimeMBeanException */
    protected Object doInvoke(MethodInvocation invocation) throws Exception {
        Object result;
        Method method = invocation.getMethod();
        try {
            if (this.invocationHandler != null) {
                result = this.invocationHandler.invoke(invocation.getThis(), method, invocation.getArguments());
            } else {
                PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
                if (pd != null) {
                    result = invokeAttribute(pd, invocation);
                } else {
                    result = invokeOperation(method, invocation.getArguments());
                }
            }
            return convertResultValueIfNecessary(result, new MethodParameter(method, -1));
        } catch (RuntimeErrorException ex) {
            throw ex.getTargetError();
        } catch (IOException ex2) {
            if (ReflectionUtils.declaresException(method, ex2.getClass())) {
                throw ex2;
            }
            throw new MBeanConnectFailureException("I/O failure during JMX access", ex2);
        } catch (RuntimeMBeanException ex3) {
            throw ex3.getTargetException();
        } catch (RuntimeOperationsException ex4) {
            RuntimeMBeanException targetException = ex4.getTargetException();
            if (targetException instanceof RuntimeMBeanException) {
                throw targetException.getTargetException();
            }
            if (targetException instanceof RuntimeErrorException) {
                throw ((RuntimeErrorException) targetException).getTargetError();
            }
            throw targetException;
        } catch (JMException ex5) {
            if (ReflectionUtils.declaresException(method, ex5.getClass())) {
                throw ex5;
            }
            throw new InvocationFailureException("JMX access failed", ex5);
        } catch (OperationsException ex6) {
            if (ReflectionUtils.declaresException(method, ex6.getClass())) {
                throw ex6;
            }
            throw new InvalidInvocationException(ex6.getMessage());
        } catch (MBeanException ex7) {
            throw ex7.getTargetException();
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.management.JMRuntimeException */
    private Object invokeAttribute(PropertyDescriptor pd, MethodInvocation invocation) throws JMRuntimeException, JMException, IOException {
        String attributeName = JmxUtils.getAttributeName(pd, this.useStrictCasing);
        MBeanAttributeInfo inf = this.allowedAttributes.get(attributeName);
        if (inf == null) {
            throw new InvalidInvocationException("Attribute '" + pd.getName() + "' is not exposed on the management interface");
        }
        if (invocation.getMethod().equals(pd.getReadMethod())) {
            if (inf.isReadable()) {
                return this.serverToUse.getAttribute(this.objectName, attributeName);
            }
            throw new InvalidInvocationException("Attribute '" + attributeName + "' is not readable");
        }
        if (invocation.getMethod().equals(pd.getWriteMethod())) {
            if (inf.isWritable()) {
                this.serverToUse.setAttribute(this.objectName, new Attribute(attributeName, invocation.getArguments()[0]));
                return null;
            }
            throw new InvalidInvocationException("Attribute '" + attributeName + "' is not writable");
        }
        throw new IllegalStateException("Method [" + invocation.getMethod() + "] is neither a bean property getter nor a setter");
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.management.JMRuntimeException */
    private Object invokeOperation(Method method, Object[] args) throws JMRuntimeException, JMException, IOException {
        String[] signature;
        MethodCacheKey key = new MethodCacheKey(method.getName(), method.getParameterTypes());
        MBeanOperationInfo info = this.allowedOperations.get(key);
        if (info == null) {
            throw new InvalidInvocationException("Operation '" + method.getName() + "' is not exposed on the management interface");
        }
        synchronized (this.signatureCache) {
            signature = this.signatureCache.get(method);
            if (signature == null) {
                signature = JmxUtils.getMethodSignature(method);
                this.signatureCache.put(method, signature);
            }
        }
        return this.serverToUse.invoke(this.objectName, method.getName(), args, signature);
    }

    protected Object convertResultValueIfNecessary(Object result, MethodParameter parameter) throws NoSuchMethodException, SecurityException {
        Class<?> elementType;
        Class<?> elementType2;
        Class<?> targetClass = parameter.getParameterType();
        if (result == null) {
            return null;
        }
        try {
            if (ClassUtils.isAssignableValue(targetClass, result)) {
                return result;
            }
            if (result instanceof CompositeData) {
                Method fromMethod = targetClass.getMethod("from", CompositeData.class);
                return ReflectionUtils.invokeMethod(fromMethod, null, result);
            }
            if (result instanceof CompositeData[]) {
                CompositeData[] array = (CompositeData[]) result;
                if (targetClass.isArray()) {
                    return convertDataArrayToTargetArray(array, targetClass);
                }
                if (Collection.class.isAssignableFrom(targetClass) && (elementType2 = ResolvableType.forMethodParameter(parameter).asCollection().resolveGeneric(new int[0])) != null) {
                    return convertDataArrayToTargetCollection(array, targetClass, elementType2);
                }
            } else {
                if (result instanceof TabularData) {
                    Method fromMethod2 = targetClass.getMethod("from", TabularData.class);
                    return ReflectionUtils.invokeMethod(fromMethod2, null, result);
                }
                if (result instanceof TabularData[]) {
                    TabularData[] array2 = (TabularData[]) result;
                    if (targetClass.isArray()) {
                        return convertDataArrayToTargetArray(array2, targetClass);
                    }
                    if (Collection.class.isAssignableFrom(targetClass) && (elementType = ResolvableType.forMethodParameter(parameter).asCollection().resolveGeneric(new int[0])) != null) {
                        return convertDataArrayToTargetCollection(array2, targetClass, elementType);
                    }
                }
            }
            throw new InvocationFailureException("Incompatible result value [" + result + "] for target type [" + targetClass.getName() + "]");
        } catch (NoSuchMethodException e) {
            throw new InvocationFailureException("Could not obtain 'from(CompositeData)' / 'from(TabularData)' method on target type [" + targetClass.getName() + "] for conversion of MXBean data structure [" + result + "]");
        }
    }

    private Object convertDataArrayToTargetArray(Object[] array, Class<?> targetClass) throws NoSuchMethodException, SecurityException, ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        Class<?> targetType = targetClass.getComponentType();
        Method fromMethod = targetType.getMethod("from", array.getClass().getComponentType());
        Object resultArray = Array.newInstance(targetType, array.length);
        for (int i = 0; i < array.length; i++) {
            Array.set(resultArray, i, ReflectionUtils.invokeMethod(fromMethod, null, array[i]));
        }
        return resultArray;
    }

    private Collection<?> convertDataArrayToTargetCollection(Object[] array, Class<?> collectionType, Class<?> elementType) throws NoSuchMethodException, SecurityException {
        Method fromMethod = elementType.getMethod("from", array.getClass().getComponentType());
        Collection<Object> resultColl = CollectionFactory.createCollection(collectionType, Array.getLength(array));
        for (Object obj : array) {
            resultColl.add(ReflectionUtils.invokeMethod(fromMethod, null, obj));
        }
        return resultColl;
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() {
        this.connector.close();
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jmx/access/MBeanClientInterceptor$MethodCacheKey.class */
    private static final class MethodCacheKey implements Comparable<MethodCacheKey> {
        private final String name;
        private final Class<?>[] parameterTypes;

        public MethodCacheKey(String name, Class<?>[] parameterTypes) {
            this.name = name;
            this.parameterTypes = parameterTypes != null ? parameterTypes : new Class[0];
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            MethodCacheKey otherKey = (MethodCacheKey) other;
            return this.name.equals(otherKey.name) && Arrays.equals(this.parameterTypes, otherKey.parameterTypes);
        }

        public int hashCode() {
            return this.name.hashCode();
        }

        public String toString() {
            return this.name + "(" + StringUtils.arrayToCommaDelimitedString(this.parameterTypes) + ")";
        }

        @Override // java.lang.Comparable
        public int compareTo(MethodCacheKey other) {
            int result = this.name.compareTo(other.name);
            if (result != 0) {
                return result;
            }
            if (this.parameterTypes.length < other.parameterTypes.length) {
                return -1;
            }
            if (this.parameterTypes.length > other.parameterTypes.length) {
                return 1;
            }
            for (int i = 0; i < this.parameterTypes.length; i++) {
                int result2 = this.parameterTypes[i].getName().compareTo(other.parameterTypes[i].getName());
                if (result2 != 0) {
                    return result2;
                }
            }
            return 0;
        }
    }
}
