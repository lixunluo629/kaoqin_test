package org.springframework.remoting.jaxws;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.WebServiceProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.lang.UsesJava7;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/remoting/jaxws/AbstractJaxWsServiceExporter.class */
public abstract class AbstractJaxWsServiceExporter implements BeanFactoryAware, InitializingBean, DisposableBean {
    private Map<String, Object> endpointProperties;
    private Executor executor;
    private String bindingType;
    private WebServiceFeature[] endpointFeatures;
    private Object[] webServiceFeatures;
    private ListableBeanFactory beanFactory;
    private final Set<Endpoint> publishedEndpoints = new LinkedHashSet();

    protected abstract void publishEndpoint(Endpoint endpoint, WebService webService);

    protected abstract void publishEndpoint(Endpoint endpoint, WebServiceProvider webServiceProvider);

    public void setEndpointProperties(Map<String, Object> endpointProperties) {
        this.endpointProperties = endpointProperties;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public void setBindingType(String bindingType) {
        this.bindingType = bindingType;
    }

    public void setEndpointFeatures(WebServiceFeature... endpointFeatures) {
        this.endpointFeatures = endpointFeatures;
    }

    @Deprecated
    public void setWebServiceFeatures(Object[] webServiceFeatures) {
        this.webServiceFeatures = webServiceFeatures;
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) {
        if (!(beanFactory instanceof ListableBeanFactory)) {
            throw new IllegalStateException(getClass().getSimpleName() + " requires a ListableBeanFactory");
        }
        this.beanFactory = (ListableBeanFactory) beanFactory;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws Exception {
        publishEndpoints();
    }

    public void publishEndpoints() {
        Set<String> beanNames = new LinkedHashSet<>(this.beanFactory.getBeanDefinitionCount());
        beanNames.addAll(Arrays.asList(this.beanFactory.getBeanDefinitionNames()));
        if (this.beanFactory instanceof ConfigurableBeanFactory) {
            beanNames.addAll(Arrays.asList(((ConfigurableBeanFactory) this.beanFactory).getSingletonNames()));
        }
        for (String beanName : beanNames) {
            try {
                Class<?> type = this.beanFactory.getType(beanName);
                if (type != null && !type.isInterface()) {
                    WebService wsAnnotation = (WebService) type.getAnnotation(WebService.class);
                    WebServiceProvider wsProviderAnnotation = (WebServiceProvider) type.getAnnotation(WebServiceProvider.class);
                    if (wsAnnotation != null || wsProviderAnnotation != null) {
                        Endpoint endpoint = createEndpoint(this.beanFactory.getBean(beanName));
                        if (this.endpointProperties != null) {
                            endpoint.setProperties(this.endpointProperties);
                        }
                        if (this.executor != null) {
                            endpoint.setExecutor(this.executor);
                        }
                        if (wsAnnotation != null) {
                            publishEndpoint(endpoint, wsAnnotation);
                        } else {
                            publishEndpoint(endpoint, wsProviderAnnotation);
                        }
                        this.publishedEndpoints.add(endpoint);
                    }
                }
            } catch (CannotLoadBeanClassException e) {
            }
        }
    }

    @UsesJava7
    protected Endpoint createEndpoint(Object bean) {
        if (this.endpointFeatures != null || this.webServiceFeatures != null) {
            WebServiceFeature[] endpointFeaturesToUse = this.endpointFeatures;
            if (endpointFeaturesToUse == null) {
                endpointFeaturesToUse = new WebServiceFeature[this.webServiceFeatures.length];
                for (int i = 0; i < this.webServiceFeatures.length; i++) {
                    endpointFeaturesToUse[i] = convertWebServiceFeature(this.webServiceFeatures[i]);
                }
            }
            return Endpoint.create(this.bindingType, bean, endpointFeaturesToUse);
        }
        return Endpoint.create(this.bindingType, bean);
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

    private ClassLoader getBeanClassLoader() {
        return this.beanFactory instanceof ConfigurableBeanFactory ? ((ConfigurableBeanFactory) this.beanFactory).getBeanClassLoader() : ClassUtils.getDefaultClassLoader();
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() {
        for (Endpoint endpoint : this.publishedEndpoints) {
            endpoint.stop();
        }
    }
}
