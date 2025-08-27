package org.springframework.beans.factory.support;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyAccessorUtils;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.Aware;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.ResolvableType;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/AbstractAutowireCapableBeanFactory.class */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {
    private InstantiationStrategy instantiationStrategy;
    private ParameterNameDiscoverer parameterNameDiscoverer;
    private boolean allowCircularReferences;
    private boolean allowRawInjectionDespiteWrapping;
    private final Set<Class<?>> ignoredDependencyTypes;
    private final Set<Class<?>> ignoredDependencyInterfaces;
    private final ConcurrentMap<String, BeanWrapper> factoryBeanInstanceCache;
    private final ConcurrentMap<Class<?>, PropertyDescriptor[]> filteredPropertyDescriptorsCache;

    public AbstractAutowireCapableBeanFactory() {
        this.instantiationStrategy = new CglibSubclassingInstantiationStrategy();
        this.parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
        this.allowCircularReferences = true;
        this.allowRawInjectionDespiteWrapping = false;
        this.ignoredDependencyTypes = new HashSet();
        this.ignoredDependencyInterfaces = new HashSet();
        this.factoryBeanInstanceCache = new ConcurrentHashMap(16);
        this.filteredPropertyDescriptorsCache = new ConcurrentHashMap(256);
        ignoreDependencyInterface(BeanNameAware.class);
        ignoreDependencyInterface(BeanFactoryAware.class);
        ignoreDependencyInterface(BeanClassLoaderAware.class);
    }

    public AbstractAutowireCapableBeanFactory(BeanFactory parentBeanFactory) {
        this();
        setParentBeanFactory(parentBeanFactory);
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }

    protected InstantiationStrategy getInstantiationStrategy() {
        return this.instantiationStrategy;
    }

    public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }

    protected ParameterNameDiscoverer getParameterNameDiscoverer() {
        return this.parameterNameDiscoverer;
    }

    public void setAllowCircularReferences(boolean allowCircularReferences) {
        this.allowCircularReferences = allowCircularReferences;
    }

    public void setAllowRawInjectionDespiteWrapping(boolean allowRawInjectionDespiteWrapping) {
        this.allowRawInjectionDespiteWrapping = allowRawInjectionDespiteWrapping;
    }

    public void ignoreDependencyType(Class<?> type) {
        this.ignoredDependencyTypes.add(type);
    }

    public void ignoreDependencyInterface(Class<?> ifc) {
        this.ignoredDependencyInterfaces.add(ifc);
    }

    @Override // org.springframework.beans.factory.support.AbstractBeanFactory, org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void copyConfigurationFrom(ConfigurableBeanFactory otherFactory) {
        super.copyConfigurationFrom(otherFactory);
        if (otherFactory instanceof AbstractAutowireCapableBeanFactory) {
            AbstractAutowireCapableBeanFactory otherAutowireFactory = (AbstractAutowireCapableBeanFactory) otherFactory;
            this.instantiationStrategy = otherAutowireFactory.instantiationStrategy;
            this.allowCircularReferences = otherAutowireFactory.allowCircularReferences;
            this.ignoredDependencyTypes.addAll(otherAutowireFactory.ignoredDependencyTypes);
            this.ignoredDependencyInterfaces.addAll(otherAutowireFactory.ignoredDependencyInterfaces);
        }
    }

    @Override // org.springframework.beans.factory.config.AutowireCapableBeanFactory
    public <T> T createBean(Class<T> cls) throws BeansException {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition((Class<?>) cls);
        rootBeanDefinition.setScope("prototype");
        rootBeanDefinition.allowCaching = ClassUtils.isCacheSafe(cls, getBeanClassLoader());
        return (T) createBean(cls.getName(), rootBeanDefinition, (Object[]) null);
    }

    @Override // org.springframework.beans.factory.config.AutowireCapableBeanFactory
    public void autowireBean(Object existingBean) throws BeansException {
        RootBeanDefinition bd = new RootBeanDefinition(ClassUtils.getUserClass(existingBean));
        bd.setScope("prototype");
        bd.allowCaching = ClassUtils.isCacheSafe(bd.getBeanClass(), getBeanClassLoader());
        BeanWrapper bw = new BeanWrapperImpl(existingBean);
        initBeanWrapper(bw);
        populateBean(bd.getBeanClass().getName(), bd, bw);
    }

    @Override // org.springframework.beans.factory.config.AutowireCapableBeanFactory
    public Object configureBean(Object existingBean, String beanName) throws BeansException {
        markBeanAsCreated(beanName);
        BeanDefinition mbd = getMergedBeanDefinition(beanName);
        RootBeanDefinition bd = null;
        if (mbd instanceof RootBeanDefinition) {
            RootBeanDefinition rbd = (RootBeanDefinition) mbd;
            bd = rbd.isPrototype() ? rbd : rbd.cloneBeanDefinition();
        }
        if (!mbd.isPrototype()) {
            if (bd == null) {
                bd = new RootBeanDefinition(mbd);
            }
            bd.setScope("prototype");
            bd.allowCaching = ClassUtils.isCacheSafe(ClassUtils.getUserClass(existingBean), getBeanClassLoader());
        }
        BeanWrapper bw = new BeanWrapperImpl(existingBean);
        initBeanWrapper(bw);
        populateBean(beanName, bd, bw);
        return initializeBean(beanName, existingBean, bd);
    }

    @Override // org.springframework.beans.factory.config.AutowireCapableBeanFactory
    public Object resolveDependency(DependencyDescriptor descriptor, String requestingBeanName) throws BeansException {
        return resolveDependency(descriptor, requestingBeanName, null, null);
    }

    @Override // org.springframework.beans.factory.config.AutowireCapableBeanFactory
    public Object createBean(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException {
        RootBeanDefinition bd = new RootBeanDefinition(beanClass, autowireMode, dependencyCheck);
        bd.setScope("prototype");
        return createBean(beanClass.getName(), bd, (Object[]) null);
    }

    @Override // org.springframework.beans.factory.config.AutowireCapableBeanFactory
    public Object autowire(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException {
        Object bean;
        final RootBeanDefinition bd = new RootBeanDefinition(beanClass, autowireMode, dependencyCheck);
        bd.setScope("prototype");
        if (bd.getResolvedAutowireMode() == 3) {
            return autowireConstructor(beanClass.getName(), bd, null, null).getWrappedInstance();
        }
        if (System.getSecurityManager() == null) {
            bean = getInstantiationStrategy().instantiate(bd, null, this);
        } else {
            bean = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.1
                @Override // java.security.PrivilegedAction
                public Object run() {
                    return AbstractAutowireCapableBeanFactory.this.getInstantiationStrategy().instantiate(bd, null, this);
                }
            }, getAccessControlContext());
        }
        populateBean(beanClass.getName(), bd, new BeanWrapperImpl(bean));
        return bean;
    }

    @Override // org.springframework.beans.factory.config.AutowireCapableBeanFactory
    public void autowireBeanProperties(Object existingBean, int autowireMode, boolean dependencyCheck) throws BeansException {
        if (autowireMode == 3) {
            throw new IllegalArgumentException("AUTOWIRE_CONSTRUCTOR not supported for existing bean instance");
        }
        RootBeanDefinition bd = new RootBeanDefinition(ClassUtils.getUserClass(existingBean), autowireMode, dependencyCheck);
        bd.setScope("prototype");
        BeanWrapper bw = new BeanWrapperImpl(existingBean);
        initBeanWrapper(bw);
        populateBean(bd.getBeanClass().getName(), bd, bw);
    }

    @Override // org.springframework.beans.factory.config.AutowireCapableBeanFactory
    public void applyBeanPropertyValues(Object existingBean, String beanName) throws BeansException {
        markBeanAsCreated(beanName);
        BeanDefinition bd = getMergedBeanDefinition(beanName);
        BeanWrapper bw = new BeanWrapperImpl(existingBean);
        initBeanWrapper(bw);
        applyPropertyValues(beanName, bd, bw, bd.getPropertyValues());
    }

    @Override // org.springframework.beans.factory.config.AutowireCapableBeanFactory
    public Object initializeBean(Object existingBean, String beanName) {
        return initializeBean(beanName, existingBean, null);
    }

    @Override // org.springframework.beans.factory.config.AutowireCapableBeanFactory
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            result = processor.postProcessBeforeInitialization(result, beanName);
            if (result == null) {
                return result;
            }
        }
        return result;
    }

    @Override // org.springframework.beans.factory.config.AutowireCapableBeanFactory
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            result = processor.postProcessAfterInitialization(result, beanName);
            if (result == null) {
                return result;
            }
        }
        return result;
    }

    @Override // org.springframework.beans.factory.config.AutowireCapableBeanFactory
    public void destroyBean(Object existingBean) {
        new DisposableBeanAdapter(existingBean, getBeanPostProcessors(), getAccessControlContext()).destroy();
    }

    @Override // org.springframework.beans.factory.support.AbstractBeanFactory
    protected Object createBean(String beanName, RootBeanDefinition mbd, Object[] args) throws BeansException {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Creating instance of bean '" + beanName + "'");
        }
        RootBeanDefinition mbdToUse = mbd;
        Class<?> resolvedClass = resolveBeanClass(mbd, beanName, new Class[0]);
        if (resolvedClass != null && !mbd.hasBeanClass() && mbd.getBeanClassName() != null) {
            mbdToUse = new RootBeanDefinition(mbd);
            mbdToUse.setBeanClass(resolvedClass);
        }
        try {
            mbdToUse.prepareMethodOverrides();
            try {
                Object bean = resolveBeforeInstantiation(beanName, mbdToUse);
                if (bean != null) {
                    return bean;
                }
                Object beanInstance = doCreateBean(beanName, mbdToUse, args);
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Finished creating instance of bean '" + beanName + "'");
                }
                return beanInstance;
            } catch (Throwable ex) {
                throw new BeanCreationException(mbdToUse.getResourceDescription(), beanName, "BeanPostProcessor before instantiation of bean failed", ex);
            }
        } catch (BeanDefinitionValidationException ex2) {
            throw new BeanDefinitionStoreException(mbdToUse.getResourceDescription(), beanName, "Validation of method overrides failed", ex2);
        }
    }

    protected Object doCreateBean(final String beanName, final RootBeanDefinition mbd, Object[] args) throws BeansException {
        Object earlySingletonReference;
        BeanWrapper instanceWrapper = null;
        if (mbd.isSingleton()) {
            instanceWrapper = this.factoryBeanInstanceCache.remove(beanName);
        }
        if (instanceWrapper == null) {
            instanceWrapper = createBeanInstance(beanName, mbd, args);
        }
        final Object bean = instanceWrapper != null ? instanceWrapper.getWrappedInstance() : null;
        Class<?> beanType = instanceWrapper != null ? instanceWrapper.getWrappedClass() : null;
        mbd.resolvedTargetType = beanType;
        synchronized (mbd.postProcessingLock) {
            if (!mbd.postProcessed) {
                try {
                    applyMergedBeanDefinitionPostProcessors(mbd, beanType, beanName);
                    mbd.postProcessed = true;
                } catch (Throwable ex) {
                    throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Post-processing of merged bean definition failed", ex);
                }
            }
        }
        boolean earlySingletonExposure = mbd.isSingleton() && this.allowCircularReferences && isSingletonCurrentlyInCreation(beanName);
        if (earlySingletonExposure) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Eagerly caching bean '" + beanName + "' to allow for resolving potential circular references");
            }
            addSingletonFactory(beanName, new ObjectFactory<Object>() { // from class: org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.2
                @Override // org.springframework.beans.factory.ObjectFactory
                public Object getObject() throws BeansException {
                    return AbstractAutowireCapableBeanFactory.this.getEarlyBeanReference(beanName, mbd, bean);
                }
            });
        }
        Object exposedObject = bean;
        try {
            populateBean(beanName, mbd, instanceWrapper);
            if (exposedObject != null) {
                exposedObject = initializeBean(beanName, exposedObject, mbd);
            }
            if (earlySingletonExposure && (earlySingletonReference = getSingleton(beanName, false)) != null) {
                if (exposedObject == bean) {
                    exposedObject = earlySingletonReference;
                } else if (!this.allowRawInjectionDespiteWrapping && hasDependentBean(beanName)) {
                    String[] dependentBeans = getDependentBeans(beanName);
                    Set<String> actualDependentBeans = new LinkedHashSet<>(dependentBeans.length);
                    for (String dependentBean : dependentBeans) {
                        if (!removeSingletonIfCreatedForTypeCheckOnly(dependentBean)) {
                            actualDependentBeans.add(dependentBean);
                        }
                    }
                    if (!actualDependentBeans.isEmpty()) {
                        throw new BeanCurrentlyInCreationException(beanName, "Bean with name '" + beanName + "' has been injected into other beans [" + StringUtils.collectionToCommaDelimitedString(actualDependentBeans) + "] in its raw version as part of a circular reference, but has eventually been wrapped. This means that said other beans do not use the final version of the bean. This is often the result of over-eager type matching - consider using 'getBeanNamesOfType' with the 'allowEagerInit' flag turned off, for example.");
                    }
                }
            }
            try {
                registerDisposableBeanIfNecessary(beanName, bean, mbd);
                return exposedObject;
            } catch (BeanDefinitionValidationException ex2) {
                throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Invalid destruction signature", ex2);
            }
        } catch (Throwable ex3) {
            if ((ex3 instanceof BeanCreationException) && beanName.equals(((BeanCreationException) ex3).getBeanName())) {
                throw ((BeanCreationException) ex3);
            }
            throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Initialization of bean failed", ex3);
        }
    }

    @Override // org.springframework.beans.factory.support.AbstractBeanFactory
    protected Class<?> predictBeanType(String beanName, RootBeanDefinition mbd, Class<?>... typesToMatch) throws SecurityException, BeansException, IllegalArgumentException {
        Class<?> targetType = determineTargetType(beanName, mbd, typesToMatch);
        if (targetType != null && !mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
            for (BeanPostProcessor bp : getBeanPostProcessors()) {
                if (bp instanceof SmartInstantiationAwareBeanPostProcessor) {
                    SmartInstantiationAwareBeanPostProcessor ibp = (SmartInstantiationAwareBeanPostProcessor) bp;
                    Class<?> predicted = ibp.predictBeanType(targetType, beanName);
                    if (predicted != null && (typesToMatch.length != 1 || FactoryBean.class != typesToMatch[0] || FactoryBean.class.isAssignableFrom(predicted))) {
                        return predicted;
                    }
                }
            }
        }
        return targetType;
    }

    protected Class<?> determineTargetType(String beanName, RootBeanDefinition mbd, Class<?>... typesToMatch) throws SecurityException, IllegalArgumentException {
        Class<?> clsResolveBeanClass;
        Class<?> targetType = mbd.getTargetType();
        if (targetType == null) {
            if (mbd.getFactoryMethodName() != null) {
                clsResolveBeanClass = getTypeForFactoryMethod(beanName, mbd, typesToMatch);
            } else {
                clsResolveBeanClass = resolveBeanClass(mbd, beanName, typesToMatch);
            }
            targetType = clsResolveBeanClass;
            if (ObjectUtils.isEmpty((Object[]) typesToMatch) || getTempClassLoader() == null) {
                mbd.resolvedTargetType = targetType;
            }
        }
        return targetType;
    }

    protected Class<?> getTypeForFactoryMethod(String beanName, RootBeanDefinition mbd, Class<?>... typesToMatch) throws SecurityException, IllegalArgumentException {
        Class<?> factoryClass;
        ResolvableType cachedReturnType = mbd.factoryMethodReturnType;
        if (cachedReturnType != null) {
            return cachedReturnType.resolve();
        }
        boolean isStatic = true;
        String factoryBeanName = mbd.getFactoryBeanName();
        if (factoryBeanName != null) {
            if (factoryBeanName.equals(beanName)) {
                throw new BeanDefinitionStoreException(mbd.getResourceDescription(), beanName, "factory-bean reference points back to the same bean definition");
            }
            factoryClass = getType(factoryBeanName);
            isStatic = false;
        } else {
            factoryClass = resolveBeanClass(mbd, beanName, typesToMatch);
        }
        if (factoryClass == null) {
            return null;
        }
        Class<?> factoryClass2 = ClassUtils.getUserClass(factoryClass);
        Class<?> commonType = null;
        Method uniqueCandidate = null;
        int minNrOfArgs = mbd.getConstructorArgumentValues().getArgumentCount();
        Method[] candidates = ReflectionUtils.getUniqueDeclaredMethods(factoryClass2);
        for (Method candidate : candidates) {
            if (Modifier.isStatic(candidate.getModifiers()) == isStatic && mbd.isFactoryMethod(candidate) && candidate.getParameterTypes().length >= minNrOfArgs) {
                if (candidate.getTypeParameters().length > 0) {
                    try {
                        Class<?>[] paramTypes = candidate.getParameterTypes();
                        String[] paramNames = null;
                        ParameterNameDiscoverer pnd = getParameterNameDiscoverer();
                        if (pnd != null) {
                            paramNames = pnd.getParameterNames(candidate);
                        }
                        ConstructorArgumentValues cav = mbd.getConstructorArgumentValues();
                        Set<ConstructorArgumentValues.ValueHolder> usedValueHolders = new HashSet<>(paramTypes.length);
                        Object[] args = new Object[paramTypes.length];
                        for (int i = 0; i < args.length; i++) {
                            ConstructorArgumentValues.ValueHolder valueHolder = cav.getArgumentValue(i, paramTypes[i], paramNames != null ? paramNames[i] : null, usedValueHolders);
                            if (valueHolder == null) {
                                valueHolder = cav.getGenericArgumentValue(null, null, usedValueHolders);
                            }
                            if (valueHolder != null) {
                                args[i] = valueHolder.getValue();
                                usedValueHolders.add(valueHolder);
                            }
                        }
                        Class<?> returnType = AutowireUtils.resolveReturnTypeForFactoryMethod(candidate, args, getBeanClassLoader());
                        if (returnType == null) {
                            continue;
                        } else {
                            uniqueCandidate = (commonType == null && returnType == candidate.getReturnType()) ? candidate : null;
                            commonType = ClassUtils.determineCommonAncestor(returnType, commonType);
                            if (commonType == null) {
                                return null;
                            }
                        }
                    } catch (Throwable ex) {
                        if (this.logger.isDebugEnabled()) {
                            this.logger.debug("Failed to resolve generic return type for factory method: " + ex);
                        }
                    }
                } else {
                    uniqueCandidate = commonType == null ? candidate : null;
                    commonType = ClassUtils.determineCommonAncestor(candidate.getReturnType(), commonType);
                    if (commonType == null) {
                        return null;
                    }
                }
            }
        }
        if (commonType == null) {
            return null;
        }
        ResolvableType cachedReturnType2 = uniqueCandidate != null ? ResolvableType.forMethodReturnType(uniqueCandidate) : ResolvableType.forClass(commonType);
        mbd.factoryMethodReturnType = cachedReturnType2;
        return cachedReturnType2.resolve();
    }

    @Override // org.springframework.beans.factory.support.AbstractBeanFactory
    protected Class<?> getTypeForFactoryBean(String beanName, RootBeanDefinition mbd) {
        FactoryBean<?> nonSingletonFactoryBeanForTypeCheck;
        Class<?> result;
        String factoryBeanName = mbd.getFactoryBeanName();
        String factoryMethodName = mbd.getFactoryMethodName();
        if (factoryBeanName != null) {
            if (factoryMethodName != null) {
                BeanDefinition fbDef = getBeanDefinition(factoryBeanName);
                if (fbDef instanceof AbstractBeanDefinition) {
                    AbstractBeanDefinition afbDef = (AbstractBeanDefinition) fbDef;
                    if (afbDef.hasBeanClass() && (result = getTypeForFactoryBeanFromMethod(afbDef.getBeanClass(), factoryMethodName)) != null) {
                        return result;
                    }
                }
            }
            if (!isBeanEligibleForMetadataCaching(factoryBeanName)) {
                return null;
            }
        }
        if (mbd.isSingleton()) {
            nonSingletonFactoryBeanForTypeCheck = getSingletonFactoryBeanForTypeCheck(beanName, mbd);
        } else {
            nonSingletonFactoryBeanForTypeCheck = getNonSingletonFactoryBeanForTypeCheck(beanName, mbd);
        }
        FactoryBean<?> fb = nonSingletonFactoryBeanForTypeCheck;
        if (fb != null) {
            Class<?> result2 = getTypeForFactoryBean(fb);
            if (result2 != null) {
                return result2;
            }
            return super.getTypeForFactoryBean(beanName, mbd);
        }
        if (factoryBeanName == null && mbd.hasBeanClass()) {
            if (factoryMethodName != null) {
                return getTypeForFactoryBeanFromMethod(mbd.getBeanClass(), factoryMethodName);
            }
            return GenericTypeResolver.resolveTypeArgument(mbd.getBeanClass(), FactoryBean.class);
        }
        return null;
    }

    /* renamed from: org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory$1Holder, reason: invalid class name */
    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/AbstractAutowireCapableBeanFactory$1Holder.class */
    class C1Holder {
        Class<?> value = null;

        C1Holder() {
        }
    }

    private Class<?> getTypeForFactoryBeanFromMethod(Class<?> beanClass, final String factoryMethodName) throws SecurityException, IllegalArgumentException {
        final C1Holder objectType = new C1Holder();
        Class<?> fbClass = ClassUtils.getUserClass(beanClass);
        ReflectionUtils.doWithMethods(fbClass, new ReflectionUtils.MethodCallback() { // from class: org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.3
            @Override // org.springframework.util.ReflectionUtils.MethodCallback
            public void doWith(Method method) {
                Class<?> currentType;
                if (method.getName().equals(factoryMethodName) && FactoryBean.class.isAssignableFrom(method.getReturnType()) && (currentType = GenericTypeResolver.resolveReturnTypeArgument(method, FactoryBean.class)) != null) {
                    objectType.value = ClassUtils.determineCommonAncestor(currentType, objectType.value);
                }
            }
        });
        if (objectType.value == null || Object.class == objectType.value) {
            return null;
        }
        return objectType.value;
    }

    protected Object getEarlyBeanReference(String beanName, RootBeanDefinition mbd, Object bean) throws BeansException {
        Object exposedObject = bean;
        if (bean != null && !mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
            for (BeanPostProcessor bp : getBeanPostProcessors()) {
                if (bp instanceof SmartInstantiationAwareBeanPostProcessor) {
                    SmartInstantiationAwareBeanPostProcessor ibp = (SmartInstantiationAwareBeanPostProcessor) bp;
                    exposedObject = ibp.getEarlyBeanReference(exposedObject, beanName);
                    if (exposedObject == null) {
                        return null;
                    }
                }
            }
        }
        return exposedObject;
    }

    /* JADX WARN: Finally extract failed */
    private FactoryBean<?> getSingletonFactoryBeanForTypeCheck(String beanName, RootBeanDefinition mbd) {
        synchronized (getSingletonMutex()) {
            BeanWrapper bw = this.factoryBeanInstanceCache.get(beanName);
            if (bw != null) {
                return (FactoryBean) bw.getWrappedInstance();
            }
            Object beanInstance = getSingleton(beanName, false);
            if (beanInstance instanceof FactoryBean) {
                return (FactoryBean) beanInstance;
            }
            if (isSingletonCurrentlyInCreation(beanName) || (mbd.getFactoryBeanName() != null && isSingletonCurrentlyInCreation(mbd.getFactoryBeanName()))) {
                return null;
            }
            try {
                beforeSingletonCreation(beanName);
                Object instance = resolveBeforeInstantiation(beanName, mbd);
                if (instance == null) {
                    bw = createBeanInstance(beanName, mbd, null);
                    instance = bw.getWrappedInstance();
                }
                afterSingletonCreation(beanName);
                FactoryBean<?> fb = getFactoryBean(beanName, instance);
                if (bw != null) {
                    this.factoryBeanInstanceCache.put(beanName, bw);
                }
                return fb;
            } catch (Throwable th) {
                afterSingletonCreation(beanName);
                throw th;
            }
        }
    }

    private FactoryBean<?> getNonSingletonFactoryBeanForTypeCheck(String beanName, RootBeanDefinition mbd) {
        if (isPrototypeCurrentlyInCreation(beanName)) {
            return null;
        }
        try {
            try {
                beforePrototypeCreation(beanName);
                Object instance = resolveBeforeInstantiation(beanName, mbd);
                if (instance == null) {
                    BeanWrapper bw = createBeanInstance(beanName, mbd, null);
                    instance = bw.getWrappedInstance();
                }
                return getFactoryBean(beanName, instance);
            } catch (BeanCreationException ex) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Bean creation exception on non-singleton FactoryBean type check: " + ex);
                }
                onSuppressedException(ex);
                afterPrototypeCreation(beanName);
                return null;
            }
        } finally {
            afterPrototypeCreation(beanName);
        }
    }

    protected void applyMergedBeanDefinitionPostProcessors(RootBeanDefinition mbd, Class<?> beanType, String beanName) {
        for (BeanPostProcessor bp : getBeanPostProcessors()) {
            if (bp instanceof MergedBeanDefinitionPostProcessor) {
                MergedBeanDefinitionPostProcessor bdp = (MergedBeanDefinitionPostProcessor) bp;
                bdp.postProcessMergedBeanDefinition(mbd, beanType, beanName);
            }
        }
    }

    protected Object resolveBeforeInstantiation(String beanName, RootBeanDefinition mbd) throws BeansException {
        Class<?> targetType;
        Object bean = null;
        if (!Boolean.FALSE.equals(mbd.beforeInstantiationResolved)) {
            if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors() && (targetType = determineTargetType(beanName, mbd, new Class[0])) != null) {
                bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
                if (bean != null) {
                    bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
                }
            }
            mbd.beforeInstantiationResolved = Boolean.valueOf(bean != null);
        }
        return bean;
    }

    protected Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        for (BeanPostProcessor bp : getBeanPostProcessors()) {
            if (bp instanceof InstantiationAwareBeanPostProcessor) {
                InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
                Object result = ibp.postProcessBeforeInstantiation(beanClass, beanName);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    protected BeanWrapper createBeanInstance(String beanName, RootBeanDefinition mbd, Object[] args) throws BeansException {
        Class<?> beanClass = resolveBeanClass(mbd, beanName, new Class[0]);
        if (beanClass != null && !Modifier.isPublic(beanClass.getModifiers()) && !mbd.isNonPublicAccessAllowed()) {
            throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Bean class isn't public, and non-public access not allowed: " + beanClass.getName());
        }
        if (mbd.getFactoryMethodName() != null) {
            return instantiateUsingFactoryMethod(beanName, mbd, args);
        }
        boolean resolved = false;
        boolean autowireNecessary = false;
        if (args == null) {
            synchronized (mbd.constructorArgumentLock) {
                if (mbd.resolvedConstructorOrFactoryMethod != null) {
                    resolved = true;
                    autowireNecessary = mbd.constructorArgumentsResolved;
                }
            }
        }
        if (resolved) {
            if (autowireNecessary) {
                return autowireConstructor(beanName, mbd, null, null);
            }
            return instantiateBean(beanName, mbd);
        }
        Constructor<?>[] ctors = determineConstructorsFromBeanPostProcessors(beanClass, beanName);
        if (ctors != null || mbd.getResolvedAutowireMode() == 3 || mbd.hasConstructorArgumentValues() || !ObjectUtils.isEmpty(args)) {
            return autowireConstructor(beanName, mbd, ctors, args);
        }
        return instantiateBean(beanName, mbd);
    }

    protected Constructor<?>[] determineConstructorsFromBeanPostProcessors(Class<?> beanClass, String beanName) throws BeansException {
        if (beanClass != null && hasInstantiationAwareBeanPostProcessors()) {
            for (BeanPostProcessor bp : getBeanPostProcessors()) {
                if (bp instanceof SmartInstantiationAwareBeanPostProcessor) {
                    SmartInstantiationAwareBeanPostProcessor ibp = (SmartInstantiationAwareBeanPostProcessor) bp;
                    Constructor<?>[] ctors = ibp.determineCandidateConstructors(beanClass, beanName);
                    if (ctors != null) {
                        return ctors;
                    }
                }
            }
            return null;
        }
        return null;
    }

    protected BeanWrapper instantiateBean(final String beanName, final RootBeanDefinition mbd) {
        Object beanInstance;
        try {
            if (System.getSecurityManager() == null) {
                beanInstance = getInstantiationStrategy().instantiate(mbd, beanName, this);
            } else {
                beanInstance = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.4
                    @Override // java.security.PrivilegedAction
                    public Object run() {
                        return AbstractAutowireCapableBeanFactory.this.getInstantiationStrategy().instantiate(mbd, beanName, this);
                    }
                }, getAccessControlContext());
            }
            BeanWrapper bw = new BeanWrapperImpl(beanInstance);
            initBeanWrapper(bw);
            return bw;
        } catch (Throwable ex) {
            throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Instantiation of bean failed", ex);
        }
    }

    protected BeanWrapper instantiateUsingFactoryMethod(String beanName, RootBeanDefinition mbd, Object[] explicitArgs) {
        return new ConstructorResolver(this).instantiateUsingFactoryMethod(beanName, mbd, explicitArgs);
    }

    protected BeanWrapper autowireConstructor(String beanName, RootBeanDefinition mbd, Constructor<?>[] ctors, Object[] explicitArgs) {
        return new ConstructorResolver(this).autowireConstructor(beanName, mbd, ctors, explicitArgs);
    }

    protected void populateBean(String beanName, RootBeanDefinition mbd, BeanWrapper bw) throws BeansException {
        PropertyValues pvs = mbd.getPropertyValues();
        if (bw == null) {
            if (!pvs.isEmpty()) {
                throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Cannot apply property values to null instance");
            }
            return;
        }
        boolean continueWithPropertyPopulation = true;
        if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
            Iterator<BeanPostProcessor> it = getBeanPostProcessors().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                BeanPostProcessor bp = it.next();
                if (bp instanceof InstantiationAwareBeanPostProcessor) {
                    InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
                    if (!ibp.postProcessAfterInstantiation(bw.getWrappedInstance(), beanName)) {
                        continueWithPropertyPopulation = false;
                        break;
                    }
                }
            }
        }
        if (!continueWithPropertyPopulation) {
            return;
        }
        if (mbd.getResolvedAutowireMode() == 1 || mbd.getResolvedAutowireMode() == 2) {
            MutablePropertyValues newPvs = new MutablePropertyValues(pvs);
            if (mbd.getResolvedAutowireMode() == 1) {
                autowireByName(beanName, mbd, bw, newPvs);
            }
            if (mbd.getResolvedAutowireMode() == 2) {
                autowireByType(beanName, mbd, bw, newPvs);
            }
            pvs = newPvs;
        }
        boolean hasInstAwareBpps = hasInstantiationAwareBeanPostProcessors();
        boolean needsDepCheck = mbd.getDependencyCheck() != 0;
        if (hasInstAwareBpps || needsDepCheck) {
            PropertyDescriptor[] filteredPds = filterPropertyDescriptorsForDependencyCheck(bw, mbd.allowCaching);
            if (hasInstAwareBpps) {
                for (BeanPostProcessor bp2 : getBeanPostProcessors()) {
                    if (bp2 instanceof InstantiationAwareBeanPostProcessor) {
                        InstantiationAwareBeanPostProcessor ibp2 = (InstantiationAwareBeanPostProcessor) bp2;
                        pvs = ibp2.postProcessPropertyValues(pvs, filteredPds, bw.getWrappedInstance(), beanName);
                        if (pvs == null) {
                            return;
                        }
                    }
                }
            }
            if (needsDepCheck) {
                checkDependencies(beanName, mbd, filteredPds, pvs);
            }
        }
        applyPropertyValues(beanName, mbd, bw, pvs);
    }

    protected void autowireByName(String beanName, AbstractBeanDefinition mbd, BeanWrapper bw, MutablePropertyValues pvs) {
        String[] propertyNames = unsatisfiedNonSimpleProperties(mbd, bw);
        for (String propertyName : propertyNames) {
            if (containsBean(propertyName)) {
                Object bean = getBean(propertyName);
                pvs.add(propertyName, bean);
                registerDependentBean(propertyName, beanName);
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Added autowiring by name from bean name '" + beanName + "' via property '" + propertyName + "' to bean named '" + propertyName + "'");
                }
            } else if (this.logger.isTraceEnabled()) {
                this.logger.trace("Not autowiring property '" + propertyName + "' of bean '" + beanName + "' by name: no matching bean found");
            }
        }
    }

    protected void autowireByType(String beanName, AbstractBeanDefinition mbd, BeanWrapper bw, MutablePropertyValues pvs) {
        TypeConverter converter = getCustomTypeConverter();
        if (converter == null) {
            converter = bw;
        }
        Set<String> autowiredBeanNames = new LinkedHashSet<>(4);
        String[] propertyNames = unsatisfiedNonSimpleProperties(mbd, bw);
        for (String propertyName : propertyNames) {
            try {
                PropertyDescriptor pd = bw.getPropertyDescriptor(propertyName);
                if (Object.class != pd.getPropertyType()) {
                    MethodParameter methodParam = BeanUtils.getWriteMethodParameter(pd);
                    boolean eager = !PriorityOrdered.class.isAssignableFrom(bw.getWrappedClass());
                    DependencyDescriptor desc = new AutowireByTypeDependencyDescriptor(methodParam, eager);
                    Object autowiredArgument = resolveDependency(desc, beanName, autowiredBeanNames, converter);
                    if (autowiredArgument != null) {
                        pvs.add(propertyName, autowiredArgument);
                    }
                    for (String autowiredBeanName : autowiredBeanNames) {
                        registerDependentBean(autowiredBeanName, beanName);
                        if (this.logger.isDebugEnabled()) {
                            this.logger.debug("Autowiring by type from bean name '" + beanName + "' via property '" + propertyName + "' to bean named '" + autowiredBeanName + "'");
                        }
                    }
                    autowiredBeanNames.clear();
                }
            } catch (BeansException ex) {
                throw new UnsatisfiedDependencyException(mbd.getResourceDescription(), beanName, propertyName, ex);
            }
        }
    }

    protected String[] unsatisfiedNonSimpleProperties(AbstractBeanDefinition mbd, BeanWrapper bw) {
        Set<String> result = new TreeSet<>();
        PropertyValues pvs = mbd.getPropertyValues();
        PropertyDescriptor[] pds = bw.getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            if (pd.getWriteMethod() != null && !isExcludedFromDependencyCheck(pd) && !pvs.contains(pd.getName()) && !BeanUtils.isSimpleProperty(pd.getPropertyType())) {
                result.add(pd.getName());
            }
        }
        return StringUtils.toStringArray(result);
    }

    protected PropertyDescriptor[] filterPropertyDescriptorsForDependencyCheck(BeanWrapper bw, boolean cache) {
        PropertyDescriptor[] existing;
        PropertyDescriptor[] filtered = this.filteredPropertyDescriptorsCache.get(bw.getWrappedClass());
        if (filtered == null) {
            filtered = filterPropertyDescriptorsForDependencyCheck(bw);
            if (cache && (existing = this.filteredPropertyDescriptorsCache.putIfAbsent(bw.getWrappedClass(), filtered)) != null) {
                filtered = existing;
            }
        }
        return filtered;
    }

    protected PropertyDescriptor[] filterPropertyDescriptorsForDependencyCheck(BeanWrapper bw) {
        List<PropertyDescriptor> pds = new ArrayList<>(Arrays.asList(bw.getPropertyDescriptors()));
        Iterator<PropertyDescriptor> it = pds.iterator();
        while (it.hasNext()) {
            PropertyDescriptor pd = it.next();
            if (isExcludedFromDependencyCheck(pd)) {
                it.remove();
            }
        }
        return (PropertyDescriptor[]) pds.toArray(new PropertyDescriptor[pds.size()]);
    }

    protected boolean isExcludedFromDependencyCheck(PropertyDescriptor pd) {
        return AutowireUtils.isExcludedFromDependencyCheck(pd) || this.ignoredDependencyTypes.contains(pd.getPropertyType()) || AutowireUtils.isSetterDefinedInInterface(pd, this.ignoredDependencyInterfaces);
    }

    protected void checkDependencies(String beanName, AbstractBeanDefinition mbd, PropertyDescriptor[] pds, PropertyValues pvs) throws UnsatisfiedDependencyException {
        int dependencyCheck = mbd.getDependencyCheck();
        for (PropertyDescriptor pd : pds) {
            if (pd.getWriteMethod() != null && !pvs.contains(pd.getName())) {
                boolean isSimple = BeanUtils.isSimpleProperty(pd.getPropertyType());
                boolean unsatisfied = dependencyCheck == 3 || (isSimple && dependencyCheck == 2) || (!isSimple && dependencyCheck == 1);
                if (unsatisfied) {
                    throw new UnsatisfiedDependencyException(mbd.getResourceDescription(), beanName, pd.getName(), "Set this property value or disable dependency checking for this bean.");
                }
            }
        }
    }

    protected void applyPropertyValues(String beanName, BeanDefinition mbd, BeanWrapper bw, PropertyValues pvs) throws InvalidPropertyException {
        List<PropertyValue> original;
        if (pvs == null || pvs.isEmpty()) {
            return;
        }
        if (System.getSecurityManager() != null && (bw instanceof BeanWrapperImpl)) {
            ((BeanWrapperImpl) bw).setSecurityContext(getAccessControlContext());
        }
        MutablePropertyValues mpvs = null;
        if (pvs instanceof MutablePropertyValues) {
            mpvs = (MutablePropertyValues) pvs;
            if (mpvs.isConverted()) {
                try {
                    bw.setPropertyValues(mpvs);
                    return;
                } catch (BeansException ex) {
                    throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Error setting property values", ex);
                }
            }
            original = mpvs.getPropertyValueList();
        } else {
            original = Arrays.asList(pvs.getPropertyValues());
        }
        TypeConverter converter = getCustomTypeConverter();
        if (converter == null) {
            converter = bw;
        }
        BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this, beanName, mbd, converter);
        List<PropertyValue> deepCopy = new ArrayList<>(original.size());
        boolean resolveNecessary = false;
        for (PropertyValue pv : original) {
            if (pv.isConverted()) {
                deepCopy.add(pv);
            } else {
                String propertyName = pv.getName();
                Object originalValue = pv.getValue();
                Object resolvedValue = valueResolver.resolveValueIfNecessary(pv, originalValue);
                Object convertedValue = resolvedValue;
                boolean convertible = bw.isWritableProperty(propertyName) && !PropertyAccessorUtils.isNestedOrIndexedProperty(propertyName);
                if (convertible) {
                    convertedValue = convertForProperty(resolvedValue, propertyName, bw, converter);
                }
                if (resolvedValue == originalValue) {
                    if (convertible) {
                        pv.setConvertedValue(convertedValue);
                    }
                    deepCopy.add(pv);
                } else if (convertible && (originalValue instanceof TypedStringValue) && !((TypedStringValue) originalValue).isDynamic() && !(convertedValue instanceof Collection) && !ObjectUtils.isArray(convertedValue)) {
                    pv.setConvertedValue(convertedValue);
                    deepCopy.add(pv);
                } else {
                    resolveNecessary = true;
                    deepCopy.add(new PropertyValue(pv, convertedValue));
                }
            }
        }
        if (mpvs != null && !resolveNecessary) {
            mpvs.setConverted();
        }
        try {
            bw.setPropertyValues(new MutablePropertyValues(deepCopy));
        } catch (BeansException ex2) {
            throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Error setting property values", ex2);
        }
    }

    private Object convertForProperty(Object value, String propertyName, BeanWrapper bw, TypeConverter converter) throws InvalidPropertyException {
        if (converter instanceof BeanWrapperImpl) {
            return ((BeanWrapperImpl) converter).convertForProperty(value, propertyName);
        }
        PropertyDescriptor pd = bw.getPropertyDescriptor(propertyName);
        MethodParameter methodParam = BeanUtils.getWriteMethodParameter(pd);
        return converter.convertIfNecessary(value, pd.getPropertyType(), methodParam);
    }

    protected Object initializeBean(final String beanName, final Object bean, RootBeanDefinition mbd) throws BeansException {
        if (System.getSecurityManager() != null) {
            AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.5
                @Override // java.security.PrivilegedAction
                public Object run() throws BeansException {
                    AbstractAutowireCapableBeanFactory.this.invokeAwareMethods(beanName, bean);
                    return null;
                }
            }, getAccessControlContext());
        } else {
            invokeAwareMethods(beanName, bean);
        }
        Object wrappedBean = bean;
        if (mbd == null || !mbd.isSynthetic()) {
            wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
        }
        try {
            invokeInitMethods(beanName, wrappedBean, mbd);
            if (mbd == null || !mbd.isSynthetic()) {
                wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
            }
            return wrappedBean;
        } catch (Throwable ex) {
            throw new BeanCreationException(mbd != null ? mbd.getResourceDescription() : null, beanName, "Invocation of init method failed", ex);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeAwareMethods(String beanName, Object bean) throws BeansException {
        if (bean instanceof Aware) {
            if (bean instanceof BeanNameAware) {
                ((BeanNameAware) bean).setBeanName(beanName);
            }
            if (bean instanceof BeanClassLoaderAware) {
                ((BeanClassLoaderAware) bean).setBeanClassLoader(getBeanClassLoader());
            }
            if (bean instanceof BeanFactoryAware) {
                ((BeanFactoryAware) bean).setBeanFactory(this);
            }
        }
    }

    protected void invokeInitMethods(String beanName, final Object bean, RootBeanDefinition mbd) throws Exception {
        String initMethodName;
        boolean isInitializingBean = bean instanceof InitializingBean;
        if (isInitializingBean && (mbd == null || !mbd.isExternallyManagedInitMethod("afterPropertiesSet"))) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Invoking afterPropertiesSet() on bean with name '" + beanName + "'");
            }
            if (System.getSecurityManager() != null) {
                try {
                    AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() { // from class: org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.6
                        @Override // java.security.PrivilegedExceptionAction
                        public Object run() throws Exception {
                            ((InitializingBean) bean).afterPropertiesSet();
                            return null;
                        }
                    }, getAccessControlContext());
                } catch (PrivilegedActionException pae) {
                    throw pae.getException();
                }
            } else {
                ((InitializingBean) bean).afterPropertiesSet();
            }
        }
        if (mbd != null && (initMethodName = mbd.getInitMethodName()) != null) {
            if ((!isInitializingBean || !"afterPropertiesSet".equals(initMethodName)) && !mbd.isExternallyManagedInitMethod(initMethodName)) {
                invokeCustomInitMethod(beanName, bean, mbd);
            }
        }
    }

    protected void invokeCustomInitMethod(String beanName, final Object bean, RootBeanDefinition mbd) throws Throwable {
        Method methodIfAvailable;
        String initMethodName = mbd.getInitMethodName();
        if (mbd.isNonPublicAccessAllowed()) {
            methodIfAvailable = BeanUtils.findMethod(bean.getClass(), initMethodName, new Class[0]);
        } else {
            methodIfAvailable = ClassUtils.getMethodIfAvailable(bean.getClass(), initMethodName, new Class[0]);
        }
        final Method initMethod = methodIfAvailable;
        if (initMethod == null) {
            if (mbd.isEnforceInitMethod()) {
                throw new BeanDefinitionValidationException("Couldn't find an init method named '" + initMethodName + "' on bean with name '" + beanName + "'");
            }
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("No default init method named '" + initMethodName + "' found on bean with name '" + beanName + "'");
                return;
            }
            return;
        }
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Invoking init method  '" + initMethodName + "' on bean with name '" + beanName + "'");
        }
        if (System.getSecurityManager() != null) {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() { // from class: org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.7
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws Exception {
                    ReflectionUtils.makeAccessible(initMethod);
                    return null;
                }
            });
            try {
                AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() { // from class: org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.8
                    @Override // java.security.PrivilegedExceptionAction
                    public Object run() throws Exception {
                        initMethod.invoke(bean, new Object[0]);
                        return null;
                    }
                }, getAccessControlContext());
                return;
            } catch (PrivilegedActionException pae) {
                InvocationTargetException ex = (InvocationTargetException) pae.getException();
                throw ex.getTargetException();
            }
        }
        try {
            ReflectionUtils.makeAccessible(initMethod);
            initMethod.invoke(bean, new Object[0]);
        } catch (InvocationTargetException ex2) {
            throw ex2.getTargetException();
        }
    }

    @Override // org.springframework.beans.factory.support.FactoryBeanRegistrySupport
    protected Object postProcessObjectFromFactoryBean(Object object, String beanName) {
        return applyBeanPostProcessorsAfterInitialization(object, beanName);
    }

    @Override // org.springframework.beans.factory.support.FactoryBeanRegistrySupport, org.springframework.beans.factory.support.DefaultSingletonBeanRegistry
    protected void removeSingleton(String beanName) {
        synchronized (getSingletonMutex()) {
            super.removeSingleton(beanName);
            this.factoryBeanInstanceCache.remove(beanName);
        }
    }

    @Override // org.springframework.beans.factory.support.FactoryBeanRegistrySupport, org.springframework.beans.factory.support.DefaultSingletonBeanRegistry
    protected void clearSingletonCache() {
        synchronized (getSingletonMutex()) {
            super.clearSingletonCache();
            this.factoryBeanInstanceCache.clear();
        }
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/AbstractAutowireCapableBeanFactory$AutowireByTypeDependencyDescriptor.class */
    private static class AutowireByTypeDependencyDescriptor extends DependencyDescriptor {
        public AutowireByTypeDependencyDescriptor(MethodParameter methodParameter, boolean eager) {
            super(methodParameter, false, eager);
        }

        @Override // org.springframework.beans.factory.config.DependencyDescriptor
        public String getDependencyName() {
            return null;
        }
    }
}
