package org.springframework.context.annotation;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceRef;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.InitDestroyAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.config.EmbeddedValueResolver;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodParameter;
import org.springframework.jndi.support.SimpleJndiBeanFactory;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.StringValueResolver;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/CommonAnnotationBeanPostProcessor.class */
public class CommonAnnotationBeanPostProcessor extends InitDestroyAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware, Serializable {
    private static final Method lookupAttribute = ClassUtils.getMethodIfAvailable(Resource.class, "lookup", new Class[0]);
    private static Class<? extends Annotation> webServiceRefClass;
    private static Class<? extends Annotation> ejbRefClass;
    private transient BeanFactory resourceFactory;
    private transient BeanFactory beanFactory;
    private transient StringValueResolver embeddedValueResolver;
    private final Set<String> ignoredResourceTypes = new HashSet(1);
    private boolean fallbackToDefaultTypeMatch = true;
    private boolean alwaysUseJndiLookup = false;
    private transient BeanFactory jndiFactory = new SimpleJndiBeanFactory();
    private final transient Map<String, InjectionMetadata> injectionMetadataCache = new ConcurrentHashMap(256);

    static {
        webServiceRefClass = null;
        ejbRefClass = null;
        try {
            webServiceRefClass = ClassUtils.forName("javax.xml.ws.WebServiceRef", CommonAnnotationBeanPostProcessor.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            webServiceRefClass = null;
        }
        try {
            ejbRefClass = ClassUtils.forName("javax.ejb.EJB", CommonAnnotationBeanPostProcessor.class.getClassLoader());
        } catch (ClassNotFoundException e2) {
            ejbRefClass = null;
        }
    }

    public CommonAnnotationBeanPostProcessor() {
        setOrder(2147483644);
        setInitAnnotationType(PostConstruct.class);
        setDestroyAnnotationType(PreDestroy.class);
        ignoreResourceType("javax.xml.ws.WebServiceContext");
    }

    public void ignoreResourceType(String resourceType) {
        Assert.notNull(resourceType, "Ignored resource type must not be null");
        this.ignoredResourceTypes.add(resourceType);
    }

    public void setFallbackToDefaultTypeMatch(boolean fallbackToDefaultTypeMatch) {
        this.fallbackToDefaultTypeMatch = fallbackToDefaultTypeMatch;
    }

    public void setAlwaysUseJndiLookup(boolean alwaysUseJndiLookup) {
        this.alwaysUseJndiLookup = alwaysUseJndiLookup;
    }

    public void setJndiFactory(BeanFactory jndiFactory) {
        Assert.notNull(jndiFactory, "BeanFactory must not be null");
        this.jndiFactory = jndiFactory;
    }

    public void setResourceFactory(BeanFactory resourceFactory) {
        Assert.notNull(resourceFactory, "BeanFactory must not be null");
        this.resourceFactory = resourceFactory;
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) {
        Assert.notNull(beanFactory, "BeanFactory must not be null");
        this.beanFactory = beanFactory;
        if (this.resourceFactory == null) {
            this.resourceFactory = beanFactory;
        }
        if (beanFactory instanceof ConfigurableBeanFactory) {
            this.embeddedValueResolver = new EmbeddedValueResolver((ConfigurableBeanFactory) beanFactory);
        }
    }

    @Override // org.springframework.beans.factory.annotation.InitDestroyAnnotationBeanPostProcessor, org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
        super.postProcessMergedBeanDefinition(beanDefinition, beanType, beanName);
        if (beanType != null) {
            InjectionMetadata metadata = findResourceMetadata(beanName, beanType, null);
            metadata.checkConfigMembers(beanDefinition);
        }
    }

    @Override // org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override // org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    @Override // org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
        InjectionMetadata metadata = findResourceMetadata(beanName, bean.getClass(), pvs);
        try {
            metadata.inject(bean, beanName, pvs);
            return pvs;
        } catch (Throwable ex) {
            throw new BeanCreationException(beanName, "Injection of resource dependencies failed", ex);
        }
    }

    private InjectionMetadata findResourceMetadata(String beanName, Class<?> clazz, PropertyValues pvs) {
        String cacheKey = StringUtils.hasLength(beanName) ? beanName : clazz.getName();
        InjectionMetadata metadata = this.injectionMetadataCache.get(cacheKey);
        if (InjectionMetadata.needsRefresh(metadata, clazz)) {
            synchronized (this.injectionMetadataCache) {
                metadata = this.injectionMetadataCache.get(cacheKey);
                if (InjectionMetadata.needsRefresh(metadata, clazz)) {
                    if (metadata != null) {
                        metadata.clear(pvs);
                    }
                    try {
                        metadata = buildResourceMetadata(clazz);
                        this.injectionMetadataCache.put(cacheKey, metadata);
                    } catch (NoClassDefFoundError err) {
                        throw new IllegalStateException("Failed to introspect bean class [" + clazz.getName() + "] for resource metadata: could not find class that it depends on", err);
                    }
                }
            }
        }
        return metadata;
    }

    private InjectionMetadata buildResourceMetadata(final Class<?> clazz) throws SecurityException, IllegalArgumentException {
        LinkedList<InjectionMetadata.InjectedElement> elements = new LinkedList<>();
        Class<?> targetClass = clazz;
        do {
            final LinkedList<InjectionMetadata.InjectedElement> currElements = new LinkedList<>();
            ReflectionUtils.doWithLocalFields(targetClass, new ReflectionUtils.FieldCallback() { // from class: org.springframework.context.annotation.CommonAnnotationBeanPostProcessor.1
                @Override // org.springframework.util.ReflectionUtils.FieldCallback
                public void doWith(Field field) throws IllegalAccessException, IllegalArgumentException {
                    if (CommonAnnotationBeanPostProcessor.webServiceRefClass == null || !field.isAnnotationPresent(CommonAnnotationBeanPostProcessor.webServiceRefClass)) {
                        if (CommonAnnotationBeanPostProcessor.ejbRefClass != null && field.isAnnotationPresent(CommonAnnotationBeanPostProcessor.ejbRefClass)) {
                            if (Modifier.isStatic(field.getModifiers())) {
                                throw new IllegalStateException("@EJB annotation is not supported on static fields");
                            }
                            currElements.add(CommonAnnotationBeanPostProcessor.this.new EjbRefElement(field, field, null));
                            return;
                        } else {
                            if (field.isAnnotationPresent(Resource.class)) {
                                if (!Modifier.isStatic(field.getModifiers())) {
                                    if (!CommonAnnotationBeanPostProcessor.this.ignoredResourceTypes.contains(field.getType().getName())) {
                                        currElements.add(CommonAnnotationBeanPostProcessor.this.new ResourceElement(field, field, null));
                                        return;
                                    }
                                    return;
                                }
                                throw new IllegalStateException("@Resource annotation is not supported on static fields");
                            }
                            return;
                        }
                    }
                    if (Modifier.isStatic(field.getModifiers())) {
                        throw new IllegalStateException("@WebServiceRef annotation is not supported on static fields");
                    }
                    currElements.add(CommonAnnotationBeanPostProcessor.this.new WebServiceRefElement(field, field, null));
                }
            });
            ReflectionUtils.doWithLocalMethods(targetClass, new ReflectionUtils.MethodCallback() { // from class: org.springframework.context.annotation.CommonAnnotationBeanPostProcessor.2
                @Override // org.springframework.util.ReflectionUtils.MethodCallback
                public void doWith(Method method) throws IllegalAccessException, SecurityException, BeansException, IllegalArgumentException {
                    Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
                    if (BridgeMethodResolver.isVisibilityBridgeMethodPair(method, bridgedMethod) && method.equals(ClassUtils.getMostSpecificMethod(method, clazz))) {
                        if (CommonAnnotationBeanPostProcessor.webServiceRefClass == null || !bridgedMethod.isAnnotationPresent(CommonAnnotationBeanPostProcessor.webServiceRefClass)) {
                            if (CommonAnnotationBeanPostProcessor.ejbRefClass != null && bridgedMethod.isAnnotationPresent(CommonAnnotationBeanPostProcessor.ejbRefClass)) {
                                if (Modifier.isStatic(method.getModifiers())) {
                                    throw new IllegalStateException("@EJB annotation is not supported on static methods");
                                }
                                if (method.getParameterTypes().length != 1) {
                                    throw new IllegalStateException("@EJB annotation requires a single-arg method: " + method);
                                }
                                PropertyDescriptor pd = BeanUtils.findPropertyForMethod(bridgedMethod, clazz);
                                currElements.add(CommonAnnotationBeanPostProcessor.this.new EjbRefElement(method, bridgedMethod, pd));
                                return;
                            }
                            if (bridgedMethod.isAnnotationPresent(Resource.class)) {
                                if (Modifier.isStatic(method.getModifiers())) {
                                    throw new IllegalStateException("@Resource annotation is not supported on static methods");
                                }
                                Class<?>[] paramTypes = method.getParameterTypes();
                                if (paramTypes.length == 1) {
                                    if (!CommonAnnotationBeanPostProcessor.this.ignoredResourceTypes.contains(paramTypes[0].getName())) {
                                        PropertyDescriptor pd2 = BeanUtils.findPropertyForMethod(bridgedMethod, clazz);
                                        currElements.add(CommonAnnotationBeanPostProcessor.this.new ResourceElement(method, bridgedMethod, pd2));
                                        return;
                                    }
                                    return;
                                }
                                throw new IllegalStateException("@Resource annotation requires a single-arg method: " + method);
                            }
                            return;
                        }
                        if (Modifier.isStatic(method.getModifiers())) {
                            throw new IllegalStateException("@WebServiceRef annotation is not supported on static methods");
                        }
                        if (method.getParameterTypes().length != 1) {
                            throw new IllegalStateException("@WebServiceRef annotation requires a single-arg method: " + method);
                        }
                        PropertyDescriptor pd3 = BeanUtils.findPropertyForMethod(bridgedMethod, clazz);
                        currElements.add(CommonAnnotationBeanPostProcessor.this.new WebServiceRefElement(method, bridgedMethod, pd3));
                    }
                }
            });
            elements.addAll(0, currElements);
            targetClass = targetClass.getSuperclass();
            if (targetClass == null) {
                break;
            }
        } while (targetClass != Object.class);
        return new InjectionMetadata(clazz, elements);
    }

    protected Object buildLazyResourceProxy(final LookupElement element, final String requestingBeanName) {
        TargetSource ts = new TargetSource() { // from class: org.springframework.context.annotation.CommonAnnotationBeanPostProcessor.3
            @Override // org.springframework.aop.TargetSource, org.springframework.aop.TargetClassAware
            public Class<?> getTargetClass() {
                return element.lookupType;
            }

            @Override // org.springframework.aop.TargetSource
            public boolean isStatic() {
                return false;
            }

            @Override // org.springframework.aop.TargetSource
            public Object getTarget() {
                return CommonAnnotationBeanPostProcessor.this.getResource(element, requestingBeanName);
            }

            @Override // org.springframework.aop.TargetSource
            public void releaseTarget(Object target) {
            }
        };
        ProxyFactory pf = new ProxyFactory();
        pf.setTargetSource(ts);
        if (element.lookupType.isInterface()) {
            pf.addInterface(element.lookupType);
        }
        ClassLoader classLoader = this.beanFactory instanceof ConfigurableBeanFactory ? ((ConfigurableBeanFactory) this.beanFactory).getBeanClassLoader() : null;
        return pf.getProxy(classLoader);
    }

    protected Object getResource(LookupElement element, String requestingBeanName) throws BeansException {
        if (StringUtils.hasLength(element.mappedName)) {
            return this.jndiFactory.getBean(element.mappedName, element.lookupType);
        }
        if (this.alwaysUseJndiLookup) {
            return this.jndiFactory.getBean(element.name, element.lookupType);
        }
        if (this.resourceFactory == null) {
            throw new NoSuchBeanDefinitionException(element.lookupType, "No resource factory configured - specify the 'resourceFactory' property");
        }
        return autowireResource(this.resourceFactory, element, requestingBeanName);
    }

    protected Object autowireResource(BeanFactory factory, LookupElement element, String requestingBeanName) throws BeansException {
        Object resource;
        Set<String> autowiredBeanNames;
        String name = element.name;
        if (this.fallbackToDefaultTypeMatch && element.isDefaultName && (factory instanceof AutowireCapableBeanFactory) && !factory.containsBean(name)) {
            autowiredBeanNames = new LinkedHashSet();
            resource = ((AutowireCapableBeanFactory) factory).resolveDependency(element.getDependencyDescriptor(), requestingBeanName, autowiredBeanNames, null);
        } else {
            resource = factory.getBean(name, element.lookupType);
            autowiredBeanNames = Collections.singleton(name);
        }
        if (factory instanceof ConfigurableBeanFactory) {
            ConfigurableBeanFactory beanFactory = (ConfigurableBeanFactory) factory;
            for (String autowiredBeanName : autowiredBeanNames) {
                if (beanFactory.containsBean(autowiredBeanName)) {
                    beanFactory.registerDependentBean(autowiredBeanName, requestingBeanName);
                }
            }
        }
        return resource;
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/CommonAnnotationBeanPostProcessor$LookupElement.class */
    protected abstract class LookupElement extends InjectionMetadata.InjectedElement {
        protected String name;
        protected boolean isDefaultName;
        protected Class<?> lookupType;
        protected String mappedName;

        public LookupElement(Member member, PropertyDescriptor pd) {
            super(member, pd);
            this.isDefaultName = false;
        }

        public final String getName() {
            return this.name;
        }

        public final Class<?> getLookupType() {
            return this.lookupType;
        }

        public final DependencyDescriptor getDependencyDescriptor() {
            if (this.isField) {
                return new LookupDependencyDescriptor((Field) this.member, this.lookupType);
            }
            return new LookupDependencyDescriptor((Method) this.member, this.lookupType);
        }
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/CommonAnnotationBeanPostProcessor$ResourceElement.class */
    private class ResourceElement extends LookupElement {
        private final boolean lazyLookup;

        public ResourceElement(Member member, AnnotatedElement ae, PropertyDescriptor pd) {
            super(member, pd);
            Resource resource = (Resource) ae.getAnnotation(Resource.class);
            String resourceName = resource.name();
            Class<?> resourceType = resource.type();
            this.isDefaultName = !StringUtils.hasLength(resourceName);
            if (!this.isDefaultName) {
                if (CommonAnnotationBeanPostProcessor.this.embeddedValueResolver != null) {
                    resourceName = CommonAnnotationBeanPostProcessor.this.embeddedValueResolver.resolveStringValue(resourceName);
                }
            } else {
                resourceName = this.member.getName();
                if ((this.member instanceof Method) && resourceName.startsWith("set") && resourceName.length() > 3) {
                    resourceName = Introspector.decapitalize(resourceName.substring(3));
                }
            }
            if (resourceType != null && Object.class != resourceType) {
                checkResourceType(resourceType);
            } else {
                resourceType = getResourceType();
            }
            this.name = resourceName;
            this.lookupType = resourceType;
            String lookupValue = CommonAnnotationBeanPostProcessor.lookupAttribute != null ? (String) ReflectionUtils.invokeMethod(CommonAnnotationBeanPostProcessor.lookupAttribute, resource) : null;
            this.mappedName = StringUtils.hasLength(lookupValue) ? lookupValue : resource.mappedName();
            Lazy lazy = (Lazy) ae.getAnnotation(Lazy.class);
            this.lazyLookup = lazy != null && lazy.value();
        }

        @Override // org.springframework.beans.factory.annotation.InjectionMetadata.InjectedElement
        protected Object getResourceToInject(Object target, String requestingBeanName) {
            return this.lazyLookup ? CommonAnnotationBeanPostProcessor.this.buildLazyResourceProxy(this, requestingBeanName) : CommonAnnotationBeanPostProcessor.this.getResource(this, requestingBeanName);
        }
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/CommonAnnotationBeanPostProcessor$WebServiceRefElement.class */
    private class WebServiceRefElement extends LookupElement {
        private final Class<?> elementType;
        private final String wsdlLocation;

        public WebServiceRefElement(Member member, AnnotatedElement ae, PropertyDescriptor pd) {
            super(member, pd);
            WebServiceRef resource = ae.getAnnotation(WebServiceRef.class);
            String resourceName = resource.name();
            Class<?> resourceType = resource.type();
            this.isDefaultName = !StringUtils.hasLength(resourceName);
            if (this.isDefaultName) {
                resourceName = this.member.getName();
                if ((this.member instanceof Method) && resourceName.startsWith("set") && resourceName.length() > 3) {
                    resourceName = Introspector.decapitalize(resourceName.substring(3));
                }
            }
            if (resourceType != null && Object.class != resourceType) {
                checkResourceType(resourceType);
            } else {
                resourceType = getResourceType();
            }
            this.name = resourceName;
            this.elementType = resourceType;
            if (Service.class.isAssignableFrom(resourceType)) {
                this.lookupType = resourceType;
            } else {
                this.lookupType = resource.value();
            }
            this.mappedName = resource.mappedName();
            this.wsdlLocation = resource.wsdlLocation();
        }

        @Override // org.springframework.beans.factory.annotation.InjectionMetadata.InjectedElement
        protected Object getResourceToInject(Object target, String requestingBeanName) throws NoSuchMethodException, SecurityException {
            Service service;
            try {
                service = (Service) CommonAnnotationBeanPostProcessor.this.getResource(this, requestingBeanName);
            } catch (NoSuchBeanDefinitionException e) {
                if (Service.class == this.lookupType) {
                    throw new IllegalStateException("No resource with name '" + this.name + "' found in context, and no specific JAX-WS Service subclass specified. The typical solution is to either specify a LocalJaxWsServiceFactoryBean with the given name or to specify the (generated) Service subclass as @WebServiceRef(...) value.");
                }
                if (StringUtils.hasLength(this.wsdlLocation)) {
                    try {
                        Constructor<?> ctor = this.lookupType.getConstructor(URL.class, QName.class);
                        WebServiceClient clientAnn = this.lookupType.getAnnotation(WebServiceClient.class);
                        if (clientAnn == null) {
                            throw new IllegalStateException("JAX-WS Service class [" + this.lookupType.getName() + "] does not carry a WebServiceClient annotation");
                        }
                        service = (Service) BeanUtils.instantiateClass(ctor, new URL(this.wsdlLocation), new QName(clientAnn.targetNamespace(), clientAnn.name()));
                    } catch (NoSuchMethodException e2) {
                        throw new IllegalStateException("JAX-WS Service class [" + this.lookupType.getName() + "] does not have a (URL, QName) constructor. Cannot apply specified WSDL location [" + this.wsdlLocation + "].");
                    } catch (MalformedURLException e3) {
                        throw new IllegalArgumentException("Specified WSDL location [" + this.wsdlLocation + "] isn't a valid URL");
                    }
                } else {
                    service = (Service) BeanUtils.instantiateClass(this.lookupType);
                }
            }
            return service.getPort(this.elementType);
        }
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/CommonAnnotationBeanPostProcessor$EjbRefElement.class */
    private class EjbRefElement extends LookupElement {
        private final String beanName;

        public EjbRefElement(Member member, AnnotatedElement ae, PropertyDescriptor pd) {
            super(member, pd);
            EJB resource = ae.getAnnotation(EJB.class);
            String resourceBeanName = resource.beanName();
            String resourceName = resource.name();
            this.isDefaultName = !StringUtils.hasLength(resourceName);
            if (this.isDefaultName) {
                resourceName = this.member.getName();
                if ((this.member instanceof Method) && resourceName.startsWith("set") && resourceName.length() > 3) {
                    resourceName = Introspector.decapitalize(resourceName.substring(3));
                }
            }
            Class<?> resourceType = resource.beanInterface();
            if (resourceType != null && Object.class != resourceType) {
                checkResourceType(resourceType);
            } else {
                resourceType = getResourceType();
            }
            this.beanName = resourceBeanName;
            this.name = resourceName;
            this.lookupType = resourceType;
            this.mappedName = resource.mappedName();
        }

        @Override // org.springframework.beans.factory.annotation.InjectionMetadata.InjectedElement
        protected Object getResourceToInject(Object target, String requestingBeanName) throws BeansException {
            if (StringUtils.hasLength(this.beanName)) {
                if (CommonAnnotationBeanPostProcessor.this.beanFactory != null && CommonAnnotationBeanPostProcessor.this.beanFactory.containsBean(this.beanName)) {
                    Object bean = CommonAnnotationBeanPostProcessor.this.beanFactory.getBean(this.beanName, this.lookupType);
                    if (CommonAnnotationBeanPostProcessor.this.beanFactory instanceof ConfigurableBeanFactory) {
                        ((ConfigurableBeanFactory) CommonAnnotationBeanPostProcessor.this.beanFactory).registerDependentBean(this.beanName, requestingBeanName);
                    }
                    return bean;
                }
                if (this.isDefaultName && !StringUtils.hasLength(this.mappedName)) {
                    throw new NoSuchBeanDefinitionException(this.beanName, "Cannot resolve 'beanName' in local BeanFactory. Consider specifying a general 'name' value instead.");
                }
            }
            return CommonAnnotationBeanPostProcessor.this.getResource(this, requestingBeanName);
        }
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/CommonAnnotationBeanPostProcessor$LookupDependencyDescriptor.class */
    private static class LookupDependencyDescriptor extends DependencyDescriptor {
        private final Class<?> lookupType;

        public LookupDependencyDescriptor(Field field, Class<?> lookupType) {
            super(field, true);
            this.lookupType = lookupType;
        }

        public LookupDependencyDescriptor(Method method, Class<?> lookupType) {
            super(new MethodParameter(method, 0), true);
            this.lookupType = lookupType;
        }

        @Override // org.springframework.beans.factory.config.DependencyDescriptor
        public Class<?> getDependencyType() {
            return this.lookupType;
        }
    }
}
