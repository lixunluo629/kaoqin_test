package org.springframework.remoting.jaxws;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.ProtocolException;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.soap.SOAPFaultException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.remoting.RemoteLookupFailureException;
import org.springframework.remoting.RemoteProxyFailureException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/remoting/jaxws/JaxWsPortClientInterceptor.class */
public class JaxWsPortClientInterceptor extends LocalJaxWsServiceFactory implements MethodInterceptor, BeanClassLoaderAware, InitializingBean {
    private Service jaxWsService;
    private String portName;
    private String username;
    private String password;
    private String endpointAddress;
    private boolean maintainSession;
    private boolean useSoapAction;
    private String soapActionUri;
    private Map<String, Object> customProperties;
    private WebServiceFeature[] portFeatures;
    private Object[] webServiceFeatures;
    private Class<?> serviceInterface;
    private QName portQName;
    private Object portStub;
    private boolean lookupServiceOnStartup = true;
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
    private final Object preparationMonitor = new Object();

    public void setJaxWsService(Service jaxWsService) {
        this.jaxWsService = jaxWsService;
    }

    public Service getJaxWsService() {
        return this.jaxWsService;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getPortName() {
        return this.portName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setEndpointAddress(String endpointAddress) {
        this.endpointAddress = endpointAddress;
    }

    public String getEndpointAddress() {
        return this.endpointAddress;
    }

    public void setMaintainSession(boolean maintainSession) {
        this.maintainSession = maintainSession;
    }

    public boolean isMaintainSession() {
        return this.maintainSession;
    }

    public void setUseSoapAction(boolean useSoapAction) {
        this.useSoapAction = useSoapAction;
    }

    public boolean isUseSoapAction() {
        return this.useSoapAction;
    }

    public void setSoapActionUri(String soapActionUri) {
        this.soapActionUri = soapActionUri;
    }

    public String getSoapActionUri() {
        return this.soapActionUri;
    }

    public void setCustomProperties(Map<String, Object> customProperties) {
        this.customProperties = customProperties;
    }

    public Map<String, Object> getCustomProperties() {
        if (this.customProperties == null) {
            this.customProperties = new HashMap();
        }
        return this.customProperties;
    }

    public void addCustomProperty(String name, Object value) {
        getCustomProperties().put(name, value);
    }

    public void setPortFeatures(WebServiceFeature... features) {
        this.portFeatures = features;
    }

    @Deprecated
    public void setWebServiceFeatures(Object[] webServiceFeatures) {
        this.webServiceFeatures = webServiceFeatures;
    }

    public void setServiceInterface(Class<?> serviceInterface) {
        if (serviceInterface != null && !serviceInterface.isInterface()) {
            throw new IllegalArgumentException("'serviceInterface' must be an interface");
        }
        this.serviceInterface = serviceInterface;
    }

    public Class<?> getServiceInterface() {
        return this.serviceInterface;
    }

    public void setLookupServiceOnStartup(boolean lookupServiceOnStartup) {
        this.lookupServiceOnStartup = lookupServiceOnStartup;
    }

    @Override // org.springframework.beans.factory.BeanClassLoaderAware
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    protected ClassLoader getBeanClassLoader() {
        return this.beanClassLoader;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        if (this.lookupServiceOnStartup) {
            prepare();
        }
    }

    public void prepare() {
        Class<?> ifc = getServiceInterface();
        if (ifc == null) {
            throw new IllegalArgumentException("Property 'serviceInterface' is required");
        }
        WebService ann = (WebService) ifc.getAnnotation(WebService.class);
        if (ann != null) {
            applyDefaultsFromAnnotation(ann);
        }
        Service serviceToUse = getJaxWsService();
        if (serviceToUse == null) {
            serviceToUse = createJaxWsService();
        }
        this.portQName = getQName(getPortName() != null ? getPortName() : getServiceInterface().getName());
        Object stub = getPortStub(serviceToUse, getPortName() != null ? this.portQName : null);
        preparePortStub(stub);
        this.portStub = stub;
    }

    protected void applyDefaultsFromAnnotation(WebService ann) {
        if (getWsdlDocumentUrl() == null) {
            String wsdl = ann.wsdlLocation();
            if (StringUtils.hasText(wsdl)) {
                try {
                    setWsdlDocumentUrl(new URL(wsdl));
                } catch (MalformedURLException ex) {
                    throw new IllegalStateException("Encountered invalid @Service wsdlLocation value [" + wsdl + "]", ex);
                }
            }
        }
        if (getNamespaceUri() == null) {
            String ns = ann.targetNamespace();
            if (StringUtils.hasText(ns)) {
                setNamespaceUri(ns);
            }
        }
        if (getServiceName() == null) {
            String sn = ann.serviceName();
            if (StringUtils.hasText(sn)) {
                setServiceName(sn);
            }
        }
        if (getPortName() == null) {
            String pn = ann.portName();
            if (StringUtils.hasText(pn)) {
                setPortName(pn);
            }
        }
    }

    protected boolean isPrepared() {
        boolean z;
        synchronized (this.preparationMonitor) {
            z = this.portStub != null;
        }
        return z;
    }

    protected final QName getPortQName() {
        return this.portQName;
    }

    protected Object getPortStub(Service service, QName portQName) {
        if (this.portFeatures == null && this.webServiceFeatures == null) {
            return portQName != null ? service.getPort(portQName, getServiceInterface()) : service.getPort(getServiceInterface());
        }
        WebServiceFeature[] portFeaturesToUse = this.portFeatures;
        if (portFeaturesToUse == null) {
            portFeaturesToUse = new WebServiceFeature[this.webServiceFeatures.length];
            for (int i = 0; i < this.webServiceFeatures.length; i++) {
                portFeaturesToUse[i] = convertWebServiceFeature(this.webServiceFeatures[i]);
            }
        }
        return portQName != null ? service.getPort(portQName, getServiceInterface(), portFeaturesToUse) : service.getPort(getServiceInterface(), portFeaturesToUse);
    }

    private WebServiceFeature convertWebServiceFeature(Object feature) throws ClassNotFoundException {
        Assert.notNull(feature, "WebServiceFeature specification object must not be null");
        if (feature instanceof WebServiceFeature) {
            return (WebServiceFeature) feature;
        }
        if (feature instanceof Class) {
            return (WebServiceFeature) BeanUtils.instantiate((Class) feature);
        }
        if (feature instanceof String) {
            try {
                Class<?> featureClass = getBeanClassLoader().loadClass((String) feature);
                return (WebServiceFeature) BeanUtils.instantiate(featureClass);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("Could not load WebServiceFeature class [" + feature + "]");
            }
        }
        throw new IllegalArgumentException("Unknown WebServiceFeature specification type: " + feature.getClass());
    }

    protected void preparePortStub(Object stub) {
        HashMap map = new HashMap();
        String username = getUsername();
        if (username != null) {
            map.put("javax.xml.ws.security.auth.username", username);
        }
        String password = getPassword();
        if (password != null) {
            map.put("javax.xml.ws.security.auth.password", password);
        }
        String endpointAddress = getEndpointAddress();
        if (endpointAddress != null) {
            map.put("javax.xml.ws.service.endpoint.address", endpointAddress);
        }
        if (isMaintainSession()) {
            map.put("javax.xml.ws.session.maintain", Boolean.TRUE);
        }
        if (isUseSoapAction()) {
            map.put("javax.xml.ws.soap.http.soapaction.use", Boolean.TRUE);
        }
        String soapActionUri = getSoapActionUri();
        if (soapActionUri != null) {
            map.put("javax.xml.ws.soap.http.soapaction.uri", soapActionUri);
        }
        map.putAll(getCustomProperties());
        if (!map.isEmpty()) {
            if (!(stub instanceof BindingProvider)) {
                throw new RemoteLookupFailureException("Port stub of class [" + stub.getClass().getName() + "] is not a customizable JAX-WS stub: it does not implement interface [javax.xml.ws.BindingProvider]");
            }
            ((BindingProvider) stub).getRequestContext().putAll(map);
        }
    }

    protected Object getPortStub() {
        return this.portStub;
    }

    @Override // org.aopalliance.intercept.MethodInterceptor
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (AopUtils.isToStringMethod(invocation.getMethod())) {
            return "JAX-WS proxy for port [" + getPortName() + "] of service [" + getServiceName() + "]";
        }
        synchronized (this.preparationMonitor) {
            if (!isPrepared()) {
                prepare();
            }
        }
        return doInvoke(invocation);
    }

    protected Object doInvoke(MethodInvocation invocation) throws Throwable {
        try {
            return doInvoke(invocation, getPortStub());
        } catch (WebServiceException ex) {
            throw new RemoteAccessException("Could not access remote service at [" + getEndpointAddress() + "]", ex);
        } catch (SOAPFaultException ex2) {
            throw new JaxWsSoapFaultException(ex2);
        } catch (ProtocolException ex3) {
            throw new RemoteConnectFailureException("Could not connect to remote service [" + getEndpointAddress() + "]", ex3);
        }
    }

    protected Object doInvoke(MethodInvocation invocation, Object portStub) throws Throwable {
        Method method = invocation.getMethod();
        try {
            return method.invoke(portStub, invocation.getArguments());
        } catch (InvocationTargetException ex) {
            throw ex.getTargetException();
        } catch (Throwable ex2) {
            throw new RemoteProxyFailureException("Invocation of stub method failed: " + method, ex2);
        }
    }
}
