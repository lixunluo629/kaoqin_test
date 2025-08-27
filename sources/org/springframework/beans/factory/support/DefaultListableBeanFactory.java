package org.springframework.beans.factory.support;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Provider;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.config.NamedBeanHolder;
import org.springframework.core.OrderComparator;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.UsesJava8;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CompositeIterator;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/DefaultListableBeanFactory.class */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements ConfigurableListableBeanFactory, BeanDefinitionRegistry, Serializable {
    private static Class<?> javaUtilOptionalClass;
    private static Class<?> javaxInjectProviderClass;
    private static final Map<String, Reference<DefaultListableBeanFactory>> serializableFactories;
    private String serializationId;
    private boolean allowBeanDefinitionOverriding;
    private boolean allowEagerClassLoading;
    private Comparator<Object> dependencyComparator;
    private AutowireCandidateResolver autowireCandidateResolver;
    private final Map<Class<?>, Object> resolvableDependencies;
    private final Map<String, BeanDefinition> beanDefinitionMap;
    private final Map<Class<?>, String[]> allBeanNamesByType;
    private final Map<Class<?>, String[]> singletonBeanNamesByType;
    private volatile List<String> beanDefinitionNames;
    private volatile Set<String> manualSingletonNames;
    private volatile String[] frozenBeanDefinitionNames;
    private volatile boolean configurationFrozen;

    static {
        javaUtilOptionalClass = null;
        javaxInjectProviderClass = null;
        try {
            javaUtilOptionalClass = ClassUtils.forName("java.util.Optional", DefaultListableBeanFactory.class.getClassLoader());
        } catch (ClassNotFoundException e) {
        }
        try {
            javaxInjectProviderClass = ClassUtils.forName("javax.inject.Provider", DefaultListableBeanFactory.class.getClassLoader());
        } catch (ClassNotFoundException e2) {
        }
        serializableFactories = new ConcurrentHashMap(8);
    }

    public DefaultListableBeanFactory() {
        this.allowBeanDefinitionOverriding = true;
        this.allowEagerClassLoading = true;
        this.autowireCandidateResolver = new SimpleAutowireCandidateResolver();
        this.resolvableDependencies = new ConcurrentHashMap(16);
        this.beanDefinitionMap = new ConcurrentHashMap(256);
        this.allBeanNamesByType = new ConcurrentHashMap(64);
        this.singletonBeanNamesByType = new ConcurrentHashMap(64);
        this.beanDefinitionNames = new ArrayList(256);
        this.manualSingletonNames = new LinkedHashSet(16);
        this.configurationFrozen = false;
    }

    public DefaultListableBeanFactory(BeanFactory parentBeanFactory) {
        super(parentBeanFactory);
        this.allowBeanDefinitionOverriding = true;
        this.allowEagerClassLoading = true;
        this.autowireCandidateResolver = new SimpleAutowireCandidateResolver();
        this.resolvableDependencies = new ConcurrentHashMap(16);
        this.beanDefinitionMap = new ConcurrentHashMap(256);
        this.allBeanNamesByType = new ConcurrentHashMap(64);
        this.singletonBeanNamesByType = new ConcurrentHashMap(64);
        this.beanDefinitionNames = new ArrayList(256);
        this.manualSingletonNames = new LinkedHashSet(16);
        this.configurationFrozen = false;
    }

    public void setSerializationId(String serializationId) {
        if (serializationId != null) {
            serializableFactories.put(serializationId, new WeakReference(this));
        } else if (this.serializationId != null) {
            serializableFactories.remove(this.serializationId);
        }
        this.serializationId = serializationId;
    }

    public String getSerializationId() {
        return this.serializationId;
    }

    public void setAllowBeanDefinitionOverriding(boolean allowBeanDefinitionOverriding) {
        this.allowBeanDefinitionOverriding = allowBeanDefinitionOverriding;
    }

    public boolean isAllowBeanDefinitionOverriding() {
        return this.allowBeanDefinitionOverriding;
    }

    public void setAllowEagerClassLoading(boolean allowEagerClassLoading) {
        this.allowEagerClassLoading = allowEagerClassLoading;
    }

    public boolean isAllowEagerClassLoading() {
        return this.allowEagerClassLoading;
    }

    public void setDependencyComparator(Comparator<Object> dependencyComparator) {
        this.dependencyComparator = dependencyComparator;
    }

    public Comparator<Object> getDependencyComparator() {
        return this.dependencyComparator;
    }

    public void setAutowireCandidateResolver(final AutowireCandidateResolver autowireCandidateResolver) {
        Assert.notNull(autowireCandidateResolver, "AutowireCandidateResolver must not be null");
        if (autowireCandidateResolver instanceof BeanFactoryAware) {
            if (System.getSecurityManager() != null) {
                AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: org.springframework.beans.factory.support.DefaultListableBeanFactory.1
                    @Override // java.security.PrivilegedAction
                    public Object run() throws BeansException {
                        ((BeanFactoryAware) autowireCandidateResolver).setBeanFactory(DefaultListableBeanFactory.this);
                        return null;
                    }
                }, getAccessControlContext());
            } else {
                ((BeanFactoryAware) autowireCandidateResolver).setBeanFactory(this);
            }
        }
        this.autowireCandidateResolver = autowireCandidateResolver;
    }

    public AutowireCandidateResolver getAutowireCandidateResolver() {
        return this.autowireCandidateResolver;
    }

    @Override // org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory, org.springframework.beans.factory.support.AbstractBeanFactory, org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void copyConfigurationFrom(ConfigurableBeanFactory otherFactory) {
        super.copyConfigurationFrom(otherFactory);
        if (otherFactory instanceof DefaultListableBeanFactory) {
            DefaultListableBeanFactory otherListableFactory = (DefaultListableBeanFactory) otherFactory;
            this.allowBeanDefinitionOverriding = otherListableFactory.allowBeanDefinitionOverriding;
            this.allowEagerClassLoading = otherListableFactory.allowEagerClassLoading;
            this.dependencyComparator = otherListableFactory.dependencyComparator;
            setAutowireCandidateResolver((AutowireCandidateResolver) BeanUtils.instantiateClass(getAutowireCandidateResolver().getClass()));
            this.resolvableDependencies.putAll(otherListableFactory.resolvableDependencies);
        }
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public <T> T getBean(Class<T> cls) throws BeansException {
        return (T) getBean(cls, (Object[]) null);
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public <T> T getBean(Class<T> cls, Object... objArr) throws BeansException {
        NamedBeanHolder<T> namedBeanHolderResolveNamedBean = resolveNamedBean(cls, objArr);
        if (namedBeanHolderResolveNamedBean != null) {
            return namedBeanHolderResolveNamedBean.getBeanInstance();
        }
        BeanFactory parentBeanFactory = getParentBeanFactory();
        if (parentBeanFactory != null) {
            return (T) parentBeanFactory.getBean(cls, objArr);
        }
        throw new NoSuchBeanDefinitionException((Class<?>) cls);
    }

    @Override // org.springframework.beans.factory.support.AbstractBeanFactory, org.springframework.beans.factory.ListableBeanFactory, org.springframework.beans.factory.support.BeanDefinitionRegistry
    public boolean containsBeanDefinition(String beanName) {
        Assert.notNull(beanName, "Bean name must not be null");
        return this.beanDefinitionMap.containsKey(beanName);
    }

    @Override // org.springframework.beans.factory.ListableBeanFactory, org.springframework.beans.factory.support.BeanDefinitionRegistry
    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    @Override // org.springframework.beans.factory.ListableBeanFactory, org.springframework.beans.factory.support.BeanDefinitionRegistry
    public String[] getBeanDefinitionNames() {
        String[] frozenNames = this.frozenBeanDefinitionNames;
        if (frozenNames != null) {
            return (String[]) frozenNames.clone();
        }
        return StringUtils.toStringArray(this.beanDefinitionNames);
    }

    @Override // org.springframework.beans.factory.ListableBeanFactory
    public String[] getBeanNamesForType(ResolvableType type) {
        return doGetBeanNamesForType(type, true, true);
    }

    @Override // org.springframework.beans.factory.ListableBeanFactory
    public String[] getBeanNamesForType(Class<?> type) {
        return getBeanNamesForType(type, true, true);
    }

    @Override // org.springframework.beans.factory.ListableBeanFactory
    public String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
        if (!isConfigurationFrozen() || type == null || !allowEagerInit) {
            return doGetBeanNamesForType(ResolvableType.forRawClass(type), includeNonSingletons, allowEagerInit);
        }
        Map<Class<?>, String[]> cache = includeNonSingletons ? this.allBeanNamesByType : this.singletonBeanNamesByType;
        String[] resolvedBeanNames = cache.get(type);
        if (resolvedBeanNames != null) {
            return resolvedBeanNames;
        }
        String[] resolvedBeanNames2 = doGetBeanNamesForType(ResolvableType.forRawClass(type), includeNonSingletons, true);
        if (ClassUtils.isCacheSafe(type, getBeanClassLoader())) {
            cache.put(type, resolvedBeanNames2);
        }
        return resolvedBeanNames2;
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x00b7 A[Catch: CannotLoadBeanClassException -> 0x0117, BeanDefinitionStoreException -> 0x0159, TryCatch #3 {BeanDefinitionStoreException -> 0x0159, CannotLoadBeanClassException -> 0x0117, blocks: (B:7:0x0033, B:11:0x0047, B:13:0x004f, B:15:0x0057, B:17:0x005e, B:19:0x006a, B:25:0x0089, B:27:0x0091, B:46:0x00d2, B:48:0x00ec, B:50:0x00f4, B:57:0x010a, B:33:0x00a3, B:36:0x00ae, B:38:0x00b7), top: B:105:0x0033 }] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00c1  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00c5  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00cd  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x010a A[Catch: CannotLoadBeanClassException -> 0x0117, BeanDefinitionStoreException -> 0x0159, TryCatch #3 {BeanDefinitionStoreException -> 0x0159, CannotLoadBeanClassException -> 0x0117, blocks: (B:7:0x0033, B:11:0x0047, B:13:0x004f, B:15:0x0057, B:17:0x005e, B:19:0x006a, B:25:0x0089, B:27:0x0091, B:46:0x00d2, B:48:0x00ec, B:50:0x00f4, B:57:0x010a, B:33:0x00a3, B:36:0x00ae, B:38:0x00b7), top: B:105:0x0033 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.String[] doGetBeanNamesForType(org.springframework.core.ResolvableType r5, boolean r6, boolean r7) {
        /*
            Method dump skipped, instructions count: 593
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.springframework.beans.factory.support.DefaultListableBeanFactory.doGetBeanNamesForType(org.springframework.core.ResolvableType, boolean, boolean):java.lang.String[]");
    }

    private boolean requiresEagerInitForType(String factoryBeanName) {
        return (factoryBeanName == null || !isFactoryBean(factoryBeanName) || containsSingleton(factoryBeanName)) ? false : true;
    }

    @Override // org.springframework.beans.factory.ListableBeanFactory
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeansOfType(type, true, true);
    }

    @Override // org.springframework.beans.factory.ListableBeanFactory
    public <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) throws BeansException {
        String[] beanNames = getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
        LinkedHashMap linkedHashMap = new LinkedHashMap(beanNames.length);
        for (String beanName : beanNames) {
            try {
                linkedHashMap.put(beanName, getBean(beanName, type));
            } catch (BeanCreationException ex) {
                Throwable rootCause = ex.getMostSpecificCause();
                if (rootCause instanceof BeanCurrentlyInCreationException) {
                    BeanCreationException bce = (BeanCreationException) rootCause;
                    if (isCurrentlyInCreation(bce.getBeanName())) {
                        if (this.logger.isDebugEnabled()) {
                            this.logger.debug("Ignoring match to currently created bean '" + beanName + "': " + ex.getMessage());
                        }
                        onSuppressedException(ex);
                    }
                }
                throw ex;
            }
        }
        return linkedHashMap;
    }

    @Override // org.springframework.beans.factory.ListableBeanFactory
    public String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType) throws NoSuchBeanDefinitionException {
        List<String> result = new ArrayList<>();
        for (String beanName : this.beanDefinitionNames) {
            BeanDefinition beanDefinition = getBeanDefinition(beanName);
            if (!beanDefinition.isAbstract() && findAnnotationOnBean(beanName, annotationType) != null) {
                result.add(beanName);
            }
        }
        for (String beanName2 : this.manualSingletonNames) {
            if (!result.contains(beanName2) && findAnnotationOnBean(beanName2, annotationType) != null) {
                result.add(beanName2);
            }
        }
        return StringUtils.toStringArray(result);
    }

    @Override // org.springframework.beans.factory.ListableBeanFactory
    public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws NoSuchBeanDefinitionException {
        String[] beanNames = getBeanNamesForAnnotation(annotationType);
        Map<String, Object> result = new LinkedHashMap<>(beanNames.length);
        for (String beanName : beanNames) {
            result.put(beanName, getBean(beanName));
        }
        return result;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v16, types: [java.lang.annotation.Annotation] */
    /* JADX WARN: Type inference failed for: r0v18, types: [java.lang.annotation.Annotation] */
    @Override // org.springframework.beans.factory.ListableBeanFactory
    public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) throws NoSuchBeanDefinitionException {
        Class<?> beanClass;
        A ann = null;
        Class<?> beanType = getType(beanName);
        if (beanType != null) {
            ann = AnnotationUtils.findAnnotation(beanType, (Class) annotationType);
        }
        if (ann == null && containsBeanDefinition(beanName)) {
            RootBeanDefinition bd = getMergedLocalBeanDefinition(beanName);
            if (bd.hasBeanClass() && (beanClass = bd.getBeanClass()) != beanType) {
                ann = AnnotationUtils.findAnnotation(beanClass, (Class) annotationType);
            }
        }
        return ann;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableListableBeanFactory
    public void registerResolvableDependency(Class<?> dependencyType, Object autowiredValue) {
        Assert.notNull(dependencyType, "Dependency type must not be null");
        if (autowiredValue != null) {
            if (!(autowiredValue instanceof ObjectFactory) && !dependencyType.isInstance(autowiredValue)) {
                throw new IllegalArgumentException("Value [" + autowiredValue + "] does not implement specified dependency type [" + dependencyType.getName() + "]");
            }
            this.resolvableDependencies.put(dependencyType, autowiredValue);
        }
    }

    @Override // org.springframework.beans.factory.config.ConfigurableListableBeanFactory
    public boolean isAutowireCandidate(String beanName, DependencyDescriptor descriptor) throws NoSuchBeanDefinitionException {
        return isAutowireCandidate(beanName, descriptor, getAutowireCandidateResolver());
    }

    protected boolean isAutowireCandidate(String beanName, DependencyDescriptor descriptor, AutowireCandidateResolver resolver) throws NoSuchBeanDefinitionException {
        String beanDefinitionName = BeanFactoryUtils.transformedBeanName(beanName);
        if (containsBeanDefinition(beanDefinitionName)) {
            return isAutowireCandidate(beanName, getMergedLocalBeanDefinition(beanDefinitionName), descriptor, resolver);
        }
        if (containsSingleton(beanName)) {
            return isAutowireCandidate(beanName, new RootBeanDefinition(getType(beanName)), descriptor, resolver);
        }
        BeanFactory parent = getParentBeanFactory();
        if (parent instanceof DefaultListableBeanFactory) {
            return ((DefaultListableBeanFactory) parent).isAutowireCandidate(beanName, descriptor, resolver);
        }
        if (parent instanceof ConfigurableListableBeanFactory) {
            return ((ConfigurableListableBeanFactory) parent).isAutowireCandidate(beanName, descriptor);
        }
        return true;
    }

    protected boolean isAutowireCandidate(String beanName, RootBeanDefinition mbd, DependencyDescriptor descriptor, AutowireCandidateResolver resolver) {
        boolean resolve;
        String beanDefinitionName = BeanFactoryUtils.transformedBeanName(beanName);
        resolveBeanClass(mbd, beanDefinitionName, new Class[0]);
        if (mbd.isFactoryMethodUnique) {
            synchronized (mbd.constructorArgumentLock) {
                resolve = mbd.resolvedConstructorOrFactoryMethod == null;
            }
            if (resolve) {
                new ConstructorResolver(this).resolveFactoryMethodIfPossible(mbd);
            }
        }
        return resolver.isAutowireCandidate(new BeanDefinitionHolder(mbd, beanName, getAliases(beanDefinitionName)), descriptor);
    }

    @Override // org.springframework.beans.factory.support.AbstractBeanFactory, org.springframework.beans.factory.config.ConfigurableListableBeanFactory, org.springframework.beans.factory.support.BeanDefinitionRegistry
    public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        BeanDefinition bd = this.beanDefinitionMap.get(beanName);
        if (bd == null) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("No bean named '" + beanName + "' found in " + this);
            }
            throw new NoSuchBeanDefinitionException(beanName);
        }
        return bd;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableListableBeanFactory
    public Iterator<String> getBeanNamesIterator() {
        CompositeIterator<String> iterator = new CompositeIterator<>();
        iterator.add(this.beanDefinitionNames.iterator());
        iterator.add(this.manualSingletonNames.iterator());
        return iterator;
    }

    @Override // org.springframework.beans.factory.support.AbstractBeanFactory, org.springframework.beans.factory.config.ConfigurableListableBeanFactory
    public void clearMetadataCache() {
        super.clearMetadataCache();
        clearByTypeCache();
    }

    @Override // org.springframework.beans.factory.config.ConfigurableListableBeanFactory
    public void freezeConfiguration() {
        this.configurationFrozen = true;
        this.frozenBeanDefinitionNames = StringUtils.toStringArray(this.beanDefinitionNames);
    }

    @Override // org.springframework.beans.factory.config.ConfigurableListableBeanFactory
    public boolean isConfigurationFrozen() {
        return this.configurationFrozen;
    }

    @Override // org.springframework.beans.factory.support.AbstractBeanFactory
    protected boolean isBeanEligibleForMetadataCaching(String beanName) {
        return this.configurationFrozen || super.isBeanEligibleForMetadataCaching(beanName);
    }

    @Override // org.springframework.beans.factory.config.ConfigurableListableBeanFactory
    public void preInstantiateSingletons() throws BeansException {
        boolean isEagerInit;
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Pre-instantiating singletons in " + this);
        }
        List<String> beanNames = new ArrayList<>(this.beanDefinitionNames);
        for (String beanName : beanNames) {
            RootBeanDefinition bd = getMergedLocalBeanDefinition(beanName);
            if (!bd.isAbstract() && bd.isSingleton() && !bd.isLazyInit()) {
                if (isFactoryBean(beanName)) {
                    final FactoryBean<?> factory = (FactoryBean) getBean("&" + beanName);
                    if (System.getSecurityManager() != null && (factory instanceof SmartFactoryBean)) {
                        isEagerInit = ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: org.springframework.beans.factory.support.DefaultListableBeanFactory.2
                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // java.security.PrivilegedAction
                            public Boolean run() {
                                return Boolean.valueOf(((SmartFactoryBean) factory).isEagerInit());
                            }
                        }, getAccessControlContext())).booleanValue();
                    } else {
                        isEagerInit = (factory instanceof SmartFactoryBean) && ((SmartFactoryBean) factory).isEagerInit();
                    }
                    if (isEagerInit) {
                        getBean(beanName);
                    }
                } else {
                    getBean(beanName);
                }
            }
        }
        Iterator<String> it = beanNames.iterator();
        while (it.hasNext()) {
            Object singletonInstance = getSingleton(it.next());
            if (singletonInstance instanceof SmartInitializingSingleton) {
                final SmartInitializingSingleton smartSingleton = (SmartInitializingSingleton) singletonInstance;
                if (System.getSecurityManager() != null) {
                    AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: org.springframework.beans.factory.support.DefaultListableBeanFactory.3
                        @Override // java.security.PrivilegedAction
                        public Object run() {
                            smartSingleton.afterSingletonsInstantiated();
                            return null;
                        }
                    }, getAccessControlContext());
                } else {
                    smartSingleton.afterSingletonsInstantiated();
                }
            }
        }
    }

    @Override // org.springframework.beans.factory.support.BeanDefinitionRegistry
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
        Assert.hasText(beanName, "Bean name must not be empty");
        Assert.notNull(beanDefinition, "BeanDefinition must not be null");
        if (beanDefinition instanceof AbstractBeanDefinition) {
            try {
                ((AbstractBeanDefinition) beanDefinition).validate();
            } catch (BeanDefinitionValidationException ex) {
                throw new BeanDefinitionStoreException(beanDefinition.getResourceDescription(), beanName, "Validation of bean definition failed", ex);
            }
        }
        BeanDefinition existingDefinition = this.beanDefinitionMap.get(beanName);
        if (existingDefinition != null) {
            if (!isAllowBeanDefinitionOverriding()) {
                throw new BeanDefinitionStoreException(beanDefinition.getResourceDescription(), beanName, "Cannot register bean definition [" + beanDefinition + "] for bean '" + beanName + "': There is already [" + existingDefinition + "] bound.");
            }
            if (existingDefinition.getRole() < beanDefinition.getRole()) {
                if (this.logger.isWarnEnabled()) {
                    this.logger.warn("Overriding user-defined bean definition for bean '" + beanName + "' with a framework-generated bean definition: replacing [" + existingDefinition + "] with [" + beanDefinition + "]");
                }
            } else if (!beanDefinition.equals(existingDefinition)) {
                if (this.logger.isInfoEnabled()) {
                    this.logger.info("Overriding bean definition for bean '" + beanName + "' with a different definition: replacing [" + existingDefinition + "] with [" + beanDefinition + "]");
                }
            } else if (this.logger.isDebugEnabled()) {
                this.logger.debug("Overriding bean definition for bean '" + beanName + "' with an equivalent definition: replacing [" + existingDefinition + "] with [" + beanDefinition + "]");
            }
            this.beanDefinitionMap.put(beanName, beanDefinition);
        } else {
            if (hasBeanCreationStarted()) {
                synchronized (this.beanDefinitionMap) {
                    this.beanDefinitionMap.put(beanName, beanDefinition);
                    List<String> updatedDefinitions = new ArrayList<>(this.beanDefinitionNames.size() + 1);
                    updatedDefinitions.addAll(this.beanDefinitionNames);
                    updatedDefinitions.add(beanName);
                    this.beanDefinitionNames = updatedDefinitions;
                    if (this.manualSingletonNames.contains(beanName)) {
                        Set<String> updatedSingletons = new LinkedHashSet<>(this.manualSingletonNames);
                        updatedSingletons.remove(beanName);
                        this.manualSingletonNames = updatedSingletons;
                    }
                }
            } else {
                this.beanDefinitionMap.put(beanName, beanDefinition);
                this.beanDefinitionNames.add(beanName);
                this.manualSingletonNames.remove(beanName);
            }
            this.frozenBeanDefinitionNames = null;
        }
        if (existingDefinition != null || containsSingleton(beanName)) {
            resetBeanDefinition(beanName);
        }
    }

    @Override // org.springframework.beans.factory.support.BeanDefinitionRegistry
    public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        Assert.hasText(beanName, "'beanName' must not be empty");
        BeanDefinition bd = this.beanDefinitionMap.remove(beanName);
        if (bd == null) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("No bean named '" + beanName + "' found in " + this);
            }
            throw new NoSuchBeanDefinitionException(beanName);
        }
        if (hasBeanCreationStarted()) {
            synchronized (this.beanDefinitionMap) {
                List<String> updatedDefinitions = new ArrayList<>(this.beanDefinitionNames);
                updatedDefinitions.remove(beanName);
                this.beanDefinitionNames = updatedDefinitions;
            }
        } else {
            this.beanDefinitionNames.remove(beanName);
        }
        this.frozenBeanDefinitionNames = null;
        resetBeanDefinition(beanName);
    }

    protected void resetBeanDefinition(String beanName) {
        clearMergedBeanDefinition(beanName);
        destroySingleton(beanName);
        for (String bdName : this.beanDefinitionNames) {
            if (!beanName.equals(bdName)) {
                BeanDefinition bd = this.beanDefinitionMap.get(bdName);
                if (beanName.equals(bd.getParentName())) {
                    resetBeanDefinition(bdName);
                }
            }
        }
    }

    @Override // org.springframework.core.SimpleAliasRegistry
    protected boolean allowAliasOverriding() {
        return isAllowBeanDefinitionOverriding();
    }

    @Override // org.springframework.beans.factory.support.DefaultSingletonBeanRegistry, org.springframework.beans.factory.config.SingletonBeanRegistry
    public void registerSingleton(String beanName, Object singletonObject) throws IllegalStateException {
        super.registerSingleton(beanName, singletonObject);
        if (hasBeanCreationStarted()) {
            synchronized (this.beanDefinitionMap) {
                if (!this.beanDefinitionMap.containsKey(beanName)) {
                    Set<String> updatedSingletons = new LinkedHashSet<>(this.manualSingletonNames.size() + 1);
                    updatedSingletons.addAll(this.manualSingletonNames);
                    updatedSingletons.add(beanName);
                    this.manualSingletonNames = updatedSingletons;
                }
            }
        } else if (!this.beanDefinitionMap.containsKey(beanName)) {
            this.manualSingletonNames.add(beanName);
        }
        clearByTypeCache();
    }

    @Override // org.springframework.beans.factory.support.DefaultSingletonBeanRegistry
    public void destroySingleton(String beanName) {
        super.destroySingleton(beanName);
        this.manualSingletonNames.remove(beanName);
        clearByTypeCache();
    }

    @Override // org.springframework.beans.factory.support.DefaultSingletonBeanRegistry, org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void destroySingletons() {
        super.destroySingletons();
        this.manualSingletonNames.clear();
        clearByTypeCache();
    }

    private void clearByTypeCache() {
        this.allBeanNamesByType.clear();
        this.singletonBeanNamesByType.clear();
    }

    @Override // org.springframework.beans.factory.config.AutowireCapableBeanFactory
    public <T> NamedBeanHolder<T> resolveNamedBean(Class<T> requiredType) throws BeansException {
        NamedBeanHolder<T> namedBean = resolveNamedBean(requiredType, (Object[]) null);
        if (namedBean != null) {
            return namedBean;
        }
        BeanFactory parent = getParentBeanFactory();
        if (parent instanceof AutowireCapableBeanFactory) {
            return ((AutowireCapableBeanFactory) parent).resolveNamedBean(requiredType);
        }
        throw new NoSuchBeanDefinitionException((Class<?>) requiredType);
    }

    private <T> NamedBeanHolder<T> resolveNamedBean(Class<T> requiredType, Object... args) throws BeansException {
        Assert.notNull(requiredType, "Required type must not be null");
        String[] candidateNames = getBeanNamesForType((Class<?>) requiredType);
        if (candidateNames.length > 1) {
            List<String> autowireCandidates = new ArrayList<>(candidateNames.length);
            for (String beanName : candidateNames) {
                if (!containsBeanDefinition(beanName) || getBeanDefinition(beanName).isAutowireCandidate()) {
                    autowireCandidates.add(beanName);
                }
            }
            if (!autowireCandidates.isEmpty()) {
                candidateNames = StringUtils.toStringArray(autowireCandidates);
            }
        }
        if (candidateNames.length == 1) {
            String beanName2 = candidateNames[0];
            return new NamedBeanHolder<>(beanName2, getBean(beanName2, requiredType, args));
        }
        if (candidateNames.length > 1) {
            Map<String, Object> candidates = new LinkedHashMap<>(candidateNames.length);
            for (String beanName3 : candidateNames) {
                if (containsSingleton(beanName3)) {
                    candidates.put(beanName3, getBean(beanName3, requiredType, args));
                } else {
                    candidates.put(beanName3, getType(beanName3));
                }
            }
            String candidateName = determinePrimaryCandidate(candidates, requiredType);
            if (candidateName == null) {
                candidateName = determineHighestPriorityCandidate(candidates, requiredType);
            }
            if (candidateName != null) {
                Object beanInstance = candidates.get(candidateName);
                if (beanInstance instanceof Class) {
                    beanInstance = getBean(candidateName, requiredType, args);
                }
                return new NamedBeanHolder<>(candidateName, beanInstance);
            }
            throw new NoUniqueBeanDefinitionException((Class<?>) requiredType, (Collection<String>) candidates.keySet());
        }
        return null;
    }

    @Override // org.springframework.beans.factory.config.AutowireCapableBeanFactory
    public Object resolveDependency(DependencyDescriptor descriptor, String requestingBeanName, Set<String> autowiredBeanNames, TypeConverter typeConverter) throws BeansException {
        descriptor.initParameterNameDiscovery(getParameterNameDiscoverer());
        if (javaUtilOptionalClass == descriptor.getDependencyType()) {
            return new OptionalDependencyFactory().createOptionalDependency(descriptor, requestingBeanName, new Object[0]);
        }
        if (ObjectFactory.class == descriptor.getDependencyType() || ObjectProvider.class == descriptor.getDependencyType()) {
            return new DependencyObjectProvider(descriptor, requestingBeanName);
        }
        if (javaxInjectProviderClass == descriptor.getDependencyType()) {
            return new Jsr330ProviderFactory().createDependencyProvider(descriptor, requestingBeanName);
        }
        Object result = getAutowireCandidateResolver().getLazyResolutionProxyIfNecessary(descriptor, requestingBeanName);
        if (result == null) {
            result = doResolveDependency(descriptor, requestingBeanName, autowiredBeanNames, typeConverter);
        }
        return result;
    }

    public Object doResolveDependency(DependencyDescriptor descriptor, String beanName, Set<String> autowiredBeanNames, TypeConverter typeConverter) throws BeansException {
        String autowiredBeanName;
        Object instanceCandidate;
        InjectionPoint previousInjectionPoint = ConstructorResolver.setCurrentInjectionPoint(descriptor);
        try {
            Object shortcut = descriptor.resolveShortcut(this);
            if (shortcut != null) {
                return shortcut;
            }
            Class<?> type = descriptor.getDependencyType();
            Object value = getAutowireCandidateResolver().getSuggestedValue(descriptor);
            if (value != null) {
                if (value instanceof String) {
                    String strVal = resolveEmbeddedValue((String) value);
                    BeanDefinition bd = (beanName == null || !containsBean(beanName)) ? null : getMergedBeanDefinition(beanName);
                    value = evaluateBeanDefinitionString(strVal, bd);
                }
                TypeConverter converter = typeConverter != null ? typeConverter : getTypeConverter();
                Object objConvertIfNecessary = descriptor.getField() != null ? converter.convertIfNecessary(value, type, descriptor.getField()) : converter.convertIfNecessary(value, type, descriptor.getMethodParameter());
                ConstructorResolver.setCurrentInjectionPoint(previousInjectionPoint);
                return objConvertIfNecessary;
            }
            Object multipleBeans = resolveMultipleBeans(descriptor, beanName, autowiredBeanNames, typeConverter);
            if (multipleBeans != null) {
                ConstructorResolver.setCurrentInjectionPoint(previousInjectionPoint);
                return multipleBeans;
            }
            Map<String, Object> matchingBeans = findAutowireCandidates(beanName, type, descriptor);
            if (matchingBeans.isEmpty()) {
                if (isRequired(descriptor)) {
                    raiseNoMatchingBeanFound(type, descriptor.getResolvableType(), descriptor);
                }
                ConstructorResolver.setCurrentInjectionPoint(previousInjectionPoint);
                return null;
            }
            if (matchingBeans.size() > 1) {
                autowiredBeanName = determineAutowireCandidate(matchingBeans, descriptor);
                if (autowiredBeanName == null) {
                    if (!isRequired(descriptor) && indicatesMultipleBeans(type)) {
                        ConstructorResolver.setCurrentInjectionPoint(previousInjectionPoint);
                        return null;
                    }
                    Object objResolveNotUnique = descriptor.resolveNotUnique(type, matchingBeans);
                    ConstructorResolver.setCurrentInjectionPoint(previousInjectionPoint);
                    return objResolveNotUnique;
                }
                instanceCandidate = matchingBeans.get(autowiredBeanName);
            } else {
                Map.Entry<String, Object> entry = matchingBeans.entrySet().iterator().next();
                autowiredBeanName = entry.getKey();
                instanceCandidate = entry.getValue();
            }
            if (autowiredBeanNames != null) {
                autowiredBeanNames.add(autowiredBeanName);
            }
            Object objResolveCandidate = instanceCandidate instanceof Class ? descriptor.resolveCandidate(autowiredBeanName, type, this) : instanceCandidate;
            ConstructorResolver.setCurrentInjectionPoint(previousInjectionPoint);
            return objResolveCandidate;
        } finally {
            ConstructorResolver.setCurrentInjectionPoint(previousInjectionPoint);
        }
    }

    private Object resolveMultipleBeans(DependencyDescriptor descriptor, String beanName, Set<String> autowiredBeanNames, TypeConverter typeConverter) throws IllegalArgumentException, TypeMismatchException {
        Class<?> valueType;
        Class<?> type = descriptor.getDependencyType();
        if (type.isArray()) {
            Class<?> componentType = type.getComponentType();
            ResolvableType resolvableType = descriptor.getResolvableType();
            Class<?> resolvedArrayType = resolvableType.resolve();
            if (resolvedArrayType != null && resolvedArrayType != type) {
                type = resolvedArrayType;
                componentType = resolvableType.getComponentType().resolve();
            }
            if (componentType == null) {
                return null;
            }
            Map<String, Object> matchingBeans = findAutowireCandidates(beanName, componentType, new MultiElementDescriptor(descriptor));
            if (matchingBeans.isEmpty()) {
                return null;
            }
            if (autowiredBeanNames != null) {
                autowiredBeanNames.addAll(matchingBeans.keySet());
            }
            TypeConverter converter = typeConverter != null ? typeConverter : getTypeConverter();
            Object result = converter.convertIfNecessary(matchingBeans.values(), type);
            if (getDependencyComparator() != null && (result instanceof Object[])) {
                Arrays.sort((Object[]) result, adaptDependencyComparator(matchingBeans));
            }
            return result;
        }
        if (Collection.class.isAssignableFrom(type) && type.isInterface()) {
            Class<?> elementType = descriptor.getResolvableType().asCollection().resolveGeneric(new int[0]);
            if (elementType == null) {
                return null;
            }
            Map<String, Object> matchingBeans2 = findAutowireCandidates(beanName, elementType, new MultiElementDescriptor(descriptor));
            if (matchingBeans2.isEmpty()) {
                return null;
            }
            if (autowiredBeanNames != null) {
                autowiredBeanNames.addAll(matchingBeans2.keySet());
            }
            TypeConverter converter2 = typeConverter != null ? typeConverter : getTypeConverter();
            Object result2 = converter2.convertIfNecessary(matchingBeans2.values(), type);
            if (getDependencyComparator() != null && (result2 instanceof List)) {
                Collections.sort((List) result2, adaptDependencyComparator(matchingBeans2));
            }
            return result2;
        }
        if (Map.class == type) {
            ResolvableType mapType = descriptor.getResolvableType().asMap();
            Class<?> keyType = mapType.resolveGeneric(0);
            if (String.class != keyType || (valueType = mapType.resolveGeneric(1)) == null) {
                return null;
            }
            Map<String, Object> matchingBeans3 = findAutowireCandidates(beanName, valueType, new MultiElementDescriptor(descriptor));
            if (matchingBeans3.isEmpty()) {
                return null;
            }
            if (autowiredBeanNames != null) {
                autowiredBeanNames.addAll(matchingBeans3.keySet());
            }
            return matchingBeans3;
        }
        return null;
    }

    private boolean isRequired(DependencyDescriptor descriptor) {
        AutowireCandidateResolver resolver = getAutowireCandidateResolver();
        if (resolver instanceof SimpleAutowireCandidateResolver) {
            return ((SimpleAutowireCandidateResolver) resolver).isRequired(descriptor);
        }
        return descriptor.isRequired();
    }

    private boolean indicatesMultipleBeans(Class<?> type) {
        return type.isArray() || (type.isInterface() && (Collection.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type)));
    }

    private Comparator<Object> adaptDependencyComparator(Map<String, Object> matchingBeans) {
        Comparator<Object> comparator = getDependencyComparator();
        if (comparator instanceof OrderComparator) {
            return ((OrderComparator) comparator).withSourceProvider(createFactoryAwareOrderSourceProvider(matchingBeans));
        }
        return comparator;
    }

    private OrderComparator.OrderSourceProvider createFactoryAwareOrderSourceProvider(Map<String, Object> beans) {
        IdentityHashMap<Object, String> instancesToBeanNames = new IdentityHashMap<>();
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            instancesToBeanNames.put(entry.getValue(), entry.getKey());
        }
        return new FactoryAwareOrderSourceProvider(instancesToBeanNames);
    }

    protected Map<String, Object> findAutowireCandidates(String beanName, Class<?> requiredType, DependencyDescriptor descriptor) throws IllegalArgumentException {
        String[] candidateNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this, requiredType, true, descriptor.isEager());
        Map<String, Object> result = new LinkedHashMap<>(candidateNames.length);
        Iterator<Class<?>> it = this.resolvableDependencies.keySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Class<?> autowiringType = it.next();
            if (autowiringType.isAssignableFrom(requiredType)) {
                Object autowiringValue = AutowireUtils.resolveAutowiringValue(this.resolvableDependencies.get(autowiringType), requiredType);
                if (requiredType.isInstance(autowiringValue)) {
                    result.put(ObjectUtils.identityToString(autowiringValue), autowiringValue);
                    break;
                }
            }
        }
        for (String candidate : candidateNames) {
            if (!isSelfReference(beanName, candidate) && isAutowireCandidate(candidate, descriptor)) {
                addCandidateEntry(result, candidate, descriptor, requiredType);
            }
        }
        if (result.isEmpty() && !indicatesMultipleBeans(requiredType)) {
            DependencyDescriptor fallbackDescriptor = descriptor.forFallbackMatch();
            for (String candidate2 : candidateNames) {
                if (!isSelfReference(beanName, candidate2) && isAutowireCandidate(candidate2, fallbackDescriptor)) {
                    addCandidateEntry(result, candidate2, descriptor, requiredType);
                }
            }
            if (result.isEmpty()) {
                for (String candidate3 : candidateNames) {
                    if (isSelfReference(beanName, candidate3) && ((!(descriptor instanceof MultiElementDescriptor) || !beanName.equals(candidate3)) && isAutowireCandidate(candidate3, fallbackDescriptor))) {
                        addCandidateEntry(result, candidate3, descriptor, requiredType);
                    }
                }
            }
        }
        return result;
    }

    private void addCandidateEntry(Map<String, Object> candidates, String candidateName, DependencyDescriptor descriptor, Class<?> requiredType) {
        if ((descriptor instanceof MultiElementDescriptor) || containsSingleton(candidateName)) {
            candidates.put(candidateName, descriptor.resolveCandidate(candidateName, requiredType, this));
        } else {
            candidates.put(candidateName, getType(candidateName));
        }
    }

    protected String determineAutowireCandidate(Map<String, Object> candidates, DependencyDescriptor descriptor) {
        Class<?> requiredType = descriptor.getDependencyType();
        String primaryCandidate = determinePrimaryCandidate(candidates, requiredType);
        if (primaryCandidate != null) {
            return primaryCandidate;
        }
        String priorityCandidate = determineHighestPriorityCandidate(candidates, requiredType);
        if (priorityCandidate != null) {
            return priorityCandidate;
        }
        for (Map.Entry<String, Object> entry : candidates.entrySet()) {
            String candidateName = entry.getKey();
            Object beanInstance = entry.getValue();
            if ((beanInstance != null && this.resolvableDependencies.containsValue(beanInstance)) || matchesBeanName(candidateName, descriptor.getDependencyName())) {
                return candidateName;
            }
        }
        return null;
    }

    protected String determinePrimaryCandidate(Map<String, Object> candidates, Class<?> requiredType) {
        String primaryBeanName = null;
        for (Map.Entry<String, Object> entry : candidates.entrySet()) {
            String candidateBeanName = entry.getKey();
            Object beanInstance = entry.getValue();
            if (isPrimary(candidateBeanName, beanInstance)) {
                if (primaryBeanName != null) {
                    boolean candidateLocal = containsBeanDefinition(candidateBeanName);
                    boolean primaryLocal = containsBeanDefinition(primaryBeanName);
                    if (candidateLocal && primaryLocal) {
                        throw new NoUniqueBeanDefinitionException(requiredType, candidates.size(), "more than one 'primary' bean found among candidates: " + candidates.keySet());
                    }
                    if (candidateLocal) {
                        primaryBeanName = candidateBeanName;
                    }
                } else {
                    primaryBeanName = candidateBeanName;
                }
            }
        }
        return primaryBeanName;
    }

    protected String determineHighestPriorityCandidate(Map<String, Object> candidates, Class<?> requiredType) {
        String highestPriorityBeanName = null;
        Integer highestPriority = null;
        for (Map.Entry<String, Object> entry : candidates.entrySet()) {
            String candidateBeanName = entry.getKey();
            Object beanInstance = entry.getValue();
            Integer candidatePriority = getPriority(beanInstance);
            if (candidatePriority != null) {
                if (highestPriorityBeanName != null) {
                    if (candidatePriority.equals(highestPriority)) {
                        throw new NoUniqueBeanDefinitionException(requiredType, candidates.size(), "Multiple beans found with the same priority ('" + highestPriority + "') among candidates: " + candidates.keySet());
                    }
                    if (candidatePriority.intValue() < highestPriority.intValue()) {
                        highestPriorityBeanName = candidateBeanName;
                        highestPriority = candidatePriority;
                    }
                } else {
                    highestPriorityBeanName = candidateBeanName;
                    highestPriority = candidatePriority;
                }
            }
        }
        return highestPriorityBeanName;
    }

    protected boolean isPrimary(String beanName, Object beanInstance) {
        if (containsBeanDefinition(beanName)) {
            return getMergedLocalBeanDefinition(beanName).isPrimary();
        }
        BeanFactory parent = getParentBeanFactory();
        return (parent instanceof DefaultListableBeanFactory) && ((DefaultListableBeanFactory) parent).isPrimary(beanName, beanInstance);
    }

    protected Integer getPriority(Object beanInstance) {
        Comparator<Object> comparator = getDependencyComparator();
        if (comparator instanceof OrderComparator) {
            return ((OrderComparator) comparator).getPriority(beanInstance);
        }
        return null;
    }

    protected boolean matchesBeanName(String beanName, String candidateName) {
        return candidateName != null && (candidateName.equals(beanName) || ObjectUtils.containsElement(getAliases(beanName), candidateName));
    }

    private boolean isSelfReference(String beanName, String candidateName) {
        return (beanName == null || candidateName == null || (!beanName.equals(candidateName) && (!containsBeanDefinition(candidateName) || !beanName.equals(getMergedLocalBeanDefinition(candidateName).getFactoryBeanName())))) ? false : true;
    }

    private void raiseNoMatchingBeanFound(Class<?> type, ResolvableType resolvableType, DependencyDescriptor descriptor) throws BeansException {
        checkBeanNotOfRequiredType(type, descriptor);
        throw new NoSuchBeanDefinitionException(resolvableType, "expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: " + ObjectUtils.nullSafeToString((Object[]) descriptor.getAnnotations()));
    }

    private void checkBeanNotOfRequiredType(Class<?> type, DependencyDescriptor descriptor) {
        for (String beanName : this.beanDefinitionNames) {
            RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
            Class<?> targetType = mbd.getTargetType();
            if (targetType != null && type.isAssignableFrom(targetType) && isAutowireCandidate(beanName, mbd, descriptor, getAutowireCandidateResolver())) {
                Object beanInstance = getSingleton(beanName, false);
                Class<?> beanType = beanInstance != null ? beanInstance.getClass() : predictBeanType(beanName, mbd, new Class[0]);
                if (!type.isAssignableFrom(beanType)) {
                    throw new BeanNotOfRequiredTypeException(beanName, type, beanType);
                }
            }
        }
        BeanFactory parent = getParentBeanFactory();
        if (parent instanceof DefaultListableBeanFactory) {
            ((DefaultListableBeanFactory) parent).checkBeanNotOfRequiredType(type, descriptor);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(ObjectUtils.identityToString(this));
        sb.append(": defining beans [");
        sb.append(StringUtils.collectionToCommaDelimitedString(this.beanDefinitionNames));
        sb.append("]; ");
        BeanFactory parent = getParentBeanFactory();
        if (parent == null) {
            sb.append("root of factory hierarchy");
        } else {
            sb.append("parent: ").append(ObjectUtils.identityToString(parent));
        }
        return sb.toString();
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        throw new NotSerializableException("DefaultListableBeanFactory itself is not deserializable - just a SerializedBeanFactoryReference is");
    }

    protected Object writeReplace() throws ObjectStreamException {
        if (this.serializationId != null) {
            return new SerializedBeanFactoryReference(this.serializationId);
        }
        throw new NotSerializableException("DefaultListableBeanFactory has no serialization id");
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/DefaultListableBeanFactory$SerializedBeanFactoryReference.class */
    private static class SerializedBeanFactoryReference implements Serializable {
        private final String id;

        public SerializedBeanFactoryReference(String id) {
            this.id = id;
        }

        private Object readResolve() {
            Object result;
            Reference<?> ref = (Reference) DefaultListableBeanFactory.serializableFactories.get(this.id);
            if (ref != null && (result = ref.get()) != null) {
                return result;
            }
            DefaultListableBeanFactory dummyFactory = new DefaultListableBeanFactory();
            dummyFactory.serializationId = this.id;
            return dummyFactory;
        }
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/DefaultListableBeanFactory$NestedDependencyDescriptor.class */
    private static class NestedDependencyDescriptor extends DependencyDescriptor {
        public NestedDependencyDescriptor(DependencyDescriptor original) {
            super(original);
            increaseNestingLevel();
        }
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/DefaultListableBeanFactory$MultiElementDescriptor.class */
    private static class MultiElementDescriptor extends NestedDependencyDescriptor {
        public MultiElementDescriptor(DependencyDescriptor original) {
            super(original);
        }
    }

    @UsesJava8
    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/DefaultListableBeanFactory$OptionalDependencyFactory.class */
    private class OptionalDependencyFactory {
        private OptionalDependencyFactory() {
        }

        public Object createOptionalDependency(DependencyDescriptor descriptor, String beanName, final Object... args) throws BeansException {
            DependencyDescriptor descriptorToUse = new NestedDependencyDescriptor(descriptor) { // from class: org.springframework.beans.factory.support.DefaultListableBeanFactory.OptionalDependencyFactory.1
                @Override // org.springframework.beans.factory.config.DependencyDescriptor
                public boolean isRequired() {
                    return false;
                }

                @Override // org.springframework.beans.factory.config.DependencyDescriptor
                public Object resolveCandidate(String beanName2, Class<?> requiredType, BeanFactory beanFactory) {
                    return !ObjectUtils.isEmpty(args) ? beanFactory.getBean(beanName2, requiredType, args) : super.resolveCandidate(beanName2, requiredType, beanFactory);
                }
            };
            Object result = DefaultListableBeanFactory.this.doResolveDependency(descriptorToUse, beanName, null, null);
            return result instanceof Optional ? (Optional) result : Optional.ofNullable(result);
        }
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/DefaultListableBeanFactory$DependencyObjectProvider.class */
    private class DependencyObjectProvider implements ObjectProvider<Object>, Serializable {
        private final DependencyDescriptor descriptor;
        private final boolean optional;
        private final String beanName;

        public DependencyObjectProvider(DependencyDescriptor descriptor, String beanName) {
            this.descriptor = new NestedDependencyDescriptor(descriptor);
            this.optional = this.descriptor.getDependencyType() == DefaultListableBeanFactory.javaUtilOptionalClass;
            this.beanName = beanName;
        }

        @Override // org.springframework.beans.factory.ObjectFactory
        public Object getObject() throws BeansException {
            if (this.optional) {
                return new OptionalDependencyFactory().createOptionalDependency(this.descriptor, this.beanName, new Object[0]);
            }
            return DefaultListableBeanFactory.this.doResolveDependency(this.descriptor, this.beanName, null, null);
        }

        @Override // org.springframework.beans.factory.ObjectProvider
        public Object getObject(final Object... args) throws BeansException {
            if (this.optional) {
                return new OptionalDependencyFactory().createOptionalDependency(this.descriptor, this.beanName, args);
            }
            DependencyDescriptor descriptorToUse = new DependencyDescriptor(this.descriptor) { // from class: org.springframework.beans.factory.support.DefaultListableBeanFactory.DependencyObjectProvider.1
                @Override // org.springframework.beans.factory.config.DependencyDescriptor
                public Object resolveCandidate(String beanName, Class<?> requiredType, BeanFactory beanFactory) {
                    return ((AbstractBeanFactory) beanFactory).getBean(beanName, requiredType, args);
                }
            };
            return DefaultListableBeanFactory.this.doResolveDependency(descriptorToUse, this.beanName, null, null);
        }

        @Override // org.springframework.beans.factory.ObjectProvider
        public Object getIfAvailable() throws BeansException {
            if (this.optional) {
                return new OptionalDependencyFactory().createOptionalDependency(this.descriptor, this.beanName, new Object[0]);
            }
            DependencyDescriptor descriptorToUse = new DependencyDescriptor(this.descriptor) { // from class: org.springframework.beans.factory.support.DefaultListableBeanFactory.DependencyObjectProvider.2
                @Override // org.springframework.beans.factory.config.DependencyDescriptor
                public boolean isRequired() {
                    return false;
                }
            };
            return DefaultListableBeanFactory.this.doResolveDependency(descriptorToUse, this.beanName, null, null);
        }

        @Override // org.springframework.beans.factory.ObjectProvider
        public Object getIfUnique() throws BeansException {
            DependencyDescriptor descriptorToUse = new DependencyDescriptor(this.descriptor) { // from class: org.springframework.beans.factory.support.DefaultListableBeanFactory.DependencyObjectProvider.3
                @Override // org.springframework.beans.factory.config.DependencyDescriptor
                public boolean isRequired() {
                    return false;
                }

                @Override // org.springframework.beans.factory.config.DependencyDescriptor
                public Object resolveNotUnique(Class<?> type, Map<String, Object> matchingBeans) {
                    return null;
                }
            };
            if (this.optional) {
                return new OptionalDependencyFactory().createOptionalDependency(descriptorToUse, this.beanName, new Object[0]);
            }
            return DefaultListableBeanFactory.this.doResolveDependency(descriptorToUse, this.beanName, null, null);
        }
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/DefaultListableBeanFactory$Jsr330DependencyProvider.class */
    private class Jsr330DependencyProvider extends DependencyObjectProvider implements Provider<Object> {
        public Jsr330DependencyProvider(DependencyDescriptor descriptor, String beanName) {
            super(descriptor, beanName);
        }

        public Object get() throws BeansException {
            return getObject();
        }
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/DefaultListableBeanFactory$Jsr330ProviderFactory.class */
    private class Jsr330ProviderFactory {
        private Jsr330ProviderFactory() {
        }

        public Object createDependencyProvider(DependencyDescriptor descriptor, String beanName) {
            return DefaultListableBeanFactory.this.new Jsr330DependencyProvider(descriptor, beanName);
        }
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/DefaultListableBeanFactory$FactoryAwareOrderSourceProvider.class */
    private class FactoryAwareOrderSourceProvider implements OrderComparator.OrderSourceProvider {
        private final Map<Object, String> instancesToBeanNames;

        public FactoryAwareOrderSourceProvider(Map<Object, String> instancesToBeanNames) {
            this.instancesToBeanNames = instancesToBeanNames;
        }

        @Override // org.springframework.core.OrderComparator.OrderSourceProvider
        public Object getOrderSource(Object obj) {
            String beanName = this.instancesToBeanNames.get(obj);
            if (beanName == null || !DefaultListableBeanFactory.this.containsBeanDefinition(beanName)) {
                return null;
            }
            RootBeanDefinition beanDefinition = DefaultListableBeanFactory.this.getMergedLocalBeanDefinition(beanName);
            List<Object> sources = new ArrayList<>(2);
            Method factoryMethod = beanDefinition.getResolvedFactoryMethod();
            if (factoryMethod != null) {
                sources.add(factoryMethod);
            }
            Class<?> targetType = beanDefinition.getTargetType();
            if (targetType != null && targetType != obj.getClass()) {
                sources.add(targetType);
            }
            return sources.toArray();
        }
    }
}
