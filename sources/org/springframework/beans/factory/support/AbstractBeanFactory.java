package org.springframework.beans.factory.support;

import java.beans.PropertyEditor;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.PropertyEditorRegistrySupport;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.BeanIsAbstractException;
import org.springframework.beans.factory.BeanIsNotAFactoryException;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.config.Scope;
import org.springframework.core.DecoratingClassLoader;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.StringValueResolver;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/AbstractBeanFactory.class */
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {
    private BeanFactory parentBeanFactory;
    private ClassLoader tempClassLoader;
    private BeanExpressionResolver beanExpressionResolver;
    private ConversionService conversionService;
    private TypeConverter typeConverter;
    private boolean hasInstantiationAwareBeanPostProcessors;
    private boolean hasDestructionAwareBeanPostProcessors;
    private SecurityContextProvider securityContextProvider;
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
    private boolean cacheBeanMetadata = true;
    private final Set<PropertyEditorRegistrar> propertyEditorRegistrars = new LinkedHashSet(4);
    private final Map<Class<?>, Class<? extends PropertyEditor>> customEditors = new HashMap(4);
    private final List<StringValueResolver> embeddedValueResolvers = new LinkedList();
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList();
    private final Map<String, Scope> scopes = new LinkedHashMap(8);
    private final Map<String, RootBeanDefinition> mergedBeanDefinitions = new ConcurrentHashMap(256);
    private final Set<String> alreadyCreated = Collections.newSetFromMap(new ConcurrentHashMap(256));
    private final ThreadLocal<Object> prototypesCurrentlyInCreation = new NamedThreadLocal("Prototype beans currently in creation");

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract boolean containsBeanDefinition(String str);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract BeanDefinition getBeanDefinition(String str) throws BeansException;

    protected abstract Object createBean(String str, RootBeanDefinition rootBeanDefinition, Object[] objArr) throws BeanCreationException;

    public AbstractBeanFactory() {
    }

    public AbstractBeanFactory(BeanFactory parentBeanFactory) {
        this.parentBeanFactory = parentBeanFactory;
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public Object getBean(String name) throws BeansException {
        return doGetBean(name, null, null, false);
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public <T> T getBean(String str, Class<T> cls) throws BeansException {
        return (T) doGetBean(str, cls, null, false);
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public Object getBean(String name, Object... args) throws BeansException {
        return doGetBean(name, null, args, false);
    }

    public <T> T getBean(String str, Class<T> cls, Object... objArr) throws BeansException {
        return (T) doGetBean(str, cls, objArr, false);
    }

    protected <T> T doGetBean(String str, Class<T> cls, final Object[] objArr, boolean z) throws BeansException {
        Object objectForBeanInstance;
        final String strTransformedBeanName = transformedBeanName(str);
        Object singleton = getSingleton(strTransformedBeanName);
        if (singleton != null && objArr == null) {
            if (this.logger.isDebugEnabled()) {
                if (isSingletonCurrentlyInCreation(strTransformedBeanName)) {
                    this.logger.debug("Returning eagerly cached instance of singleton bean '" + strTransformedBeanName + "' that is not fully initialized yet - a consequence of a circular reference");
                } else {
                    this.logger.debug("Returning cached instance of singleton bean '" + strTransformedBeanName + "'");
                }
            }
            objectForBeanInstance = getObjectForBeanInstance(singleton, str, strTransformedBeanName, null);
        } else {
            if (isPrototypeCurrentlyInCreation(strTransformedBeanName)) {
                throw new BeanCurrentlyInCreationException(strTransformedBeanName);
            }
            BeanFactory parentBeanFactory = getParentBeanFactory();
            if (parentBeanFactory != null && !containsBeanDefinition(strTransformedBeanName)) {
                String strOriginalBeanName = originalBeanName(str);
                if (objArr != null) {
                    return (T) parentBeanFactory.getBean(strOriginalBeanName, objArr);
                }
                return (T) parentBeanFactory.getBean(strOriginalBeanName, cls);
            }
            if (!z) {
                markBeanAsCreated(strTransformedBeanName);
            }
            try {
                final RootBeanDefinition mergedLocalBeanDefinition = getMergedLocalBeanDefinition(strTransformedBeanName);
                checkMergedBeanDefinition(mergedLocalBeanDefinition, strTransformedBeanName, objArr);
                String[] dependsOn = mergedLocalBeanDefinition.getDependsOn();
                if (dependsOn != null) {
                    for (String str2 : dependsOn) {
                        if (isDependent(strTransformedBeanName, str2)) {
                            throw new BeanCreationException(mergedLocalBeanDefinition.getResourceDescription(), strTransformedBeanName, "Circular depends-on relationship between '" + strTransformedBeanName + "' and '" + str2 + "'");
                        }
                        registerDependentBean(str2, strTransformedBeanName);
                        try {
                            getBean(str2);
                        } catch (NoSuchBeanDefinitionException e) {
                            throw new BeanCreationException(mergedLocalBeanDefinition.getResourceDescription(), strTransformedBeanName, "'" + strTransformedBeanName + "' depends on missing bean '" + str2 + "'", e);
                        }
                    }
                }
                if (mergedLocalBeanDefinition.isSingleton()) {
                    objectForBeanInstance = getObjectForBeanInstance(getSingleton(strTransformedBeanName, new ObjectFactory<Object>() { // from class: org.springframework.beans.factory.support.AbstractBeanFactory.1
                        @Override // org.springframework.beans.factory.ObjectFactory
                        public Object getObject() throws BeansException {
                            try {
                                return AbstractBeanFactory.this.createBean(strTransformedBeanName, mergedLocalBeanDefinition, objArr);
                            } catch (BeansException ex) {
                                AbstractBeanFactory.this.destroySingleton(strTransformedBeanName);
                                throw ex;
                            }
                        }
                    }), str, strTransformedBeanName, mergedLocalBeanDefinition);
                } else if (mergedLocalBeanDefinition.isPrototype()) {
                    try {
                        beforePrototypeCreation(strTransformedBeanName);
                        Object objCreateBean = createBean(strTransformedBeanName, mergedLocalBeanDefinition, objArr);
                        afterPrototypeCreation(strTransformedBeanName);
                        objectForBeanInstance = getObjectForBeanInstance(objCreateBean, str, strTransformedBeanName, mergedLocalBeanDefinition);
                    } catch (Throwable th) {
                        afterPrototypeCreation(strTransformedBeanName);
                        throw th;
                    }
                } else {
                    String scope = mergedLocalBeanDefinition.getScope();
                    Scope scope2 = this.scopes.get(scope);
                    if (scope2 == null) {
                        throw new IllegalStateException("No Scope registered for scope name '" + scope + "'");
                    }
                    try {
                        objectForBeanInstance = getObjectForBeanInstance(scope2.get(strTransformedBeanName, new ObjectFactory<Object>() { // from class: org.springframework.beans.factory.support.AbstractBeanFactory.2
                            @Override // org.springframework.beans.factory.ObjectFactory
                            public Object getObject() throws BeansException {
                                AbstractBeanFactory.this.beforePrototypeCreation(strTransformedBeanName);
                                try {
                                    return AbstractBeanFactory.this.createBean(strTransformedBeanName, mergedLocalBeanDefinition, objArr);
                                } finally {
                                    AbstractBeanFactory.this.afterPrototypeCreation(strTransformedBeanName);
                                }
                            }
                        }), str, strTransformedBeanName, mergedLocalBeanDefinition);
                    } catch (IllegalStateException e2) {
                        throw new BeanCreationException(strTransformedBeanName, "Scope '" + scope + "' is not active for the current thread; consider defining a scoped proxy for this bean if you intend to refer to it from a singleton", e2);
                    }
                }
            } catch (BeansException e3) {
                cleanupAfterBeanCreationFailure(strTransformedBeanName);
                throw e3;
            }
        }
        if (cls != null && objectForBeanInstance != null && !cls.isInstance(objectForBeanInstance)) {
            try {
                return (T) getTypeConverter().convertIfNecessary(objectForBeanInstance, cls);
            } catch (TypeMismatchException e4) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Failed to convert bean '" + str + "' to required type '" + ClassUtils.getQualifiedName(cls) + "'", e4);
                }
                throw new BeanNotOfRequiredTypeException(str, cls, objectForBeanInstance.getClass());
            }
        }
        return (T) objectForBeanInstance;
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public boolean containsBean(String name) {
        String beanName = transformedBeanName(name);
        if (containsSingleton(beanName) || containsBeanDefinition(beanName)) {
            return !BeanFactoryUtils.isFactoryDereference(name) || isFactoryBean(name);
        }
        BeanFactory parentBeanFactory = getParentBeanFactory();
        return parentBeanFactory != null && parentBeanFactory.containsBean(originalBeanName(name));
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public boolean isSingleton(String name) throws BeansException {
        String beanName = transformedBeanName(name);
        Object beanInstance = getSingleton(beanName, false);
        if (beanInstance != null) {
            return beanInstance instanceof FactoryBean ? BeanFactoryUtils.isFactoryDereference(name) || ((FactoryBean) beanInstance).isSingleton() : !BeanFactoryUtils.isFactoryDereference(name);
        }
        if (containsSingleton(beanName)) {
            return true;
        }
        BeanFactory parentBeanFactory = getParentBeanFactory();
        if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
            return parentBeanFactory.isSingleton(originalBeanName(name));
        }
        RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
        if (mbd.isSingleton()) {
            if (!isFactoryBean(beanName, mbd)) {
                return !BeanFactoryUtils.isFactoryDereference(name);
            }
            if (BeanFactoryUtils.isFactoryDereference(name)) {
                return true;
            }
            FactoryBean<?> factoryBean = (FactoryBean) getBean("&" + beanName);
            return factoryBean.isSingleton();
        }
        return false;
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public boolean isPrototype(String name) throws BeansException {
        String beanName = transformedBeanName(name);
        BeanFactory parentBeanFactory = getParentBeanFactory();
        if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
            return parentBeanFactory.isPrototype(originalBeanName(name));
        }
        RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
        if (mbd.isPrototype()) {
            return !BeanFactoryUtils.isFactoryDereference(name) || isFactoryBean(beanName, mbd);
        }
        if (!BeanFactoryUtils.isFactoryDereference(name) && isFactoryBean(beanName, mbd)) {
            final FactoryBean<?> fb = (FactoryBean) getBean("&" + beanName);
            if (System.getSecurityManager() != null) {
                return ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: org.springframework.beans.factory.support.AbstractBeanFactory.3
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    public Boolean run() {
                        return Boolean.valueOf(((fb instanceof SmartFactoryBean) && ((SmartFactoryBean) fb).isPrototype()) || !fb.isSingleton());
                    }
                }, getAccessControlContext())).booleanValue();
            }
            return ((fb instanceof SmartFactoryBean) && ((SmartFactoryBean) fb).isPrototype()) || !fb.isSingleton();
        }
        return false;
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public boolean isTypeMatch(String name, ResolvableType typeToMatch) throws BeansException {
        String beanName = transformedBeanName(name);
        Object beanInstance = getSingleton(beanName, false);
        if (beanInstance != null) {
            if (beanInstance instanceof FactoryBean) {
                if (!BeanFactoryUtils.isFactoryDereference(name)) {
                    Class<?> type = getTypeForFactoryBean((FactoryBean) beanInstance);
                    return type != null && typeToMatch.isAssignableFrom(type);
                }
                return typeToMatch.isInstance(beanInstance);
            }
            if (!BeanFactoryUtils.isFactoryDereference(name)) {
                if (typeToMatch.isInstance(beanInstance)) {
                    return true;
                }
                if (typeToMatch.hasGenerics() && containsBeanDefinition(beanName)) {
                    RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
                    Class<?> targetType = mbd.getTargetType();
                    if (targetType != null && targetType != ClassUtils.getUserClass(beanInstance)) {
                        Class<?> classToMatch = typeToMatch.resolve();
                        if (classToMatch != null && !classToMatch.isInstance(beanInstance)) {
                            return false;
                        }
                        if (typeToMatch.isAssignableFrom(targetType)) {
                            return true;
                        }
                    }
                    ResolvableType resolvableType = mbd.targetType;
                    if (resolvableType == null) {
                        resolvableType = mbd.factoryMethodReturnType;
                    }
                    return resolvableType != null && typeToMatch.isAssignableFrom(resolvableType);
                }
                return false;
            }
            return false;
        }
        if (containsSingleton(beanName) && !containsBeanDefinition(beanName)) {
            return false;
        }
        BeanFactory parentBeanFactory = getParentBeanFactory();
        if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
            return parentBeanFactory.isTypeMatch(originalBeanName(name), typeToMatch);
        }
        RootBeanDefinition mbd2 = getMergedLocalBeanDefinition(beanName);
        Class<?> classToMatch2 = typeToMatch.resolve();
        if (classToMatch2 == null) {
            classToMatch2 = FactoryBean.class;
        }
        Class<?>[] typesToMatch = FactoryBean.class == classToMatch2 ? new Class[]{classToMatch2} : new Class[]{FactoryBean.class, classToMatch2};
        BeanDefinitionHolder dbd = mbd2.getDecoratedDefinition();
        if (dbd != null && !BeanFactoryUtils.isFactoryDereference(name)) {
            RootBeanDefinition tbd = getMergedBeanDefinition(dbd.getBeanName(), dbd.getBeanDefinition(), mbd2);
            Class<?> targetClass = predictBeanType(dbd.getBeanName(), tbd, typesToMatch);
            if (targetClass != null && !FactoryBean.class.isAssignableFrom(targetClass)) {
                return typeToMatch.isAssignableFrom(targetClass);
            }
        }
        Class<?> beanType = predictBeanType(beanName, mbd2, typesToMatch);
        if (beanType == null) {
            return false;
        }
        if (FactoryBean.class.isAssignableFrom(beanType)) {
            if (!BeanFactoryUtils.isFactoryDereference(name)) {
                beanType = getTypeForFactoryBean(beanName, mbd2);
                if (beanType == null) {
                    return false;
                }
            }
        } else if (BeanFactoryUtils.isFactoryDereference(name)) {
            beanType = predictBeanType(beanName, mbd2, FactoryBean.class);
            if (beanType == null || !FactoryBean.class.isAssignableFrom(beanType)) {
                return false;
            }
        }
        ResolvableType resolvableType2 = mbd2.targetType;
        if (resolvableType2 == null) {
            resolvableType2 = mbd2.factoryMethodReturnType;
        }
        if (resolvableType2 != null && resolvableType2.resolve() == beanType) {
            return typeToMatch.isAssignableFrom(resolvableType2);
        }
        return typeToMatch.isAssignableFrom(beanType);
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException {
        return isTypeMatch(name, ResolvableType.forRawClass(typeToMatch));
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public Class<?> getType(String name) throws BeansException {
        String beanName = transformedBeanName(name);
        Object beanInstance = getSingleton(beanName, false);
        if (beanInstance != null) {
            if ((beanInstance instanceof FactoryBean) && !BeanFactoryUtils.isFactoryDereference(name)) {
                return getTypeForFactoryBean((FactoryBean) beanInstance);
            }
            return beanInstance.getClass();
        }
        if (containsSingleton(beanName) && !containsBeanDefinition(beanName)) {
            return null;
        }
        BeanFactory parentBeanFactory = getParentBeanFactory();
        if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
            return parentBeanFactory.getType(originalBeanName(name));
        }
        RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
        BeanDefinitionHolder dbd = mbd.getDecoratedDefinition();
        if (dbd != null && !BeanFactoryUtils.isFactoryDereference(name)) {
            RootBeanDefinition tbd = getMergedBeanDefinition(dbd.getBeanName(), dbd.getBeanDefinition(), mbd);
            Class<?> targetClass = predictBeanType(dbd.getBeanName(), tbd, new Class[0]);
            if (targetClass != null && !FactoryBean.class.isAssignableFrom(targetClass)) {
                return targetClass;
            }
        }
        Class<?> beanClass = predictBeanType(beanName, mbd, new Class[0]);
        if (beanClass != null && FactoryBean.class.isAssignableFrom(beanClass)) {
            if (!BeanFactoryUtils.isFactoryDereference(name)) {
                return getTypeForFactoryBean(beanName, mbd);
            }
            return beanClass;
        }
        if (BeanFactoryUtils.isFactoryDereference(name)) {
            return null;
        }
        return beanClass;
    }

    @Override // org.springframework.core.SimpleAliasRegistry, org.springframework.core.AliasRegistry, org.springframework.beans.factory.BeanFactory
    public String[] getAliases(String name) {
        BeanFactory parentBeanFactory;
        String beanName = transformedBeanName(name);
        List<String> aliases = new ArrayList<>();
        boolean factoryPrefix = name.startsWith("&");
        String fullBeanName = beanName;
        if (factoryPrefix) {
            fullBeanName = "&" + beanName;
        }
        if (!fullBeanName.equals(name)) {
            aliases.add(fullBeanName);
        }
        String[] retrievedAliases = super.getAliases(beanName);
        for (String retrievedAlias : retrievedAliases) {
            String alias = (factoryPrefix ? "&" : "") + retrievedAlias;
            if (!alias.equals(name)) {
                aliases.add(alias);
            }
        }
        if (!containsSingleton(beanName) && !containsBeanDefinition(beanName) && (parentBeanFactory = getParentBeanFactory()) != null) {
            aliases.addAll(Arrays.asList(parentBeanFactory.getAliases(fullBeanName)));
        }
        return StringUtils.toStringArray(aliases);
    }

    @Override // org.springframework.beans.factory.HierarchicalBeanFactory
    public BeanFactory getParentBeanFactory() {
        return this.parentBeanFactory;
    }

    @Override // org.springframework.beans.factory.HierarchicalBeanFactory
    public boolean containsLocalBean(String name) {
        String beanName = transformedBeanName(name);
        return (containsSingleton(beanName) || containsBeanDefinition(beanName)) && (!BeanFactoryUtils.isFactoryDereference(name) || isFactoryBean(beanName));
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void setParentBeanFactory(BeanFactory parentBeanFactory) {
        if (this.parentBeanFactory != null && this.parentBeanFactory != parentBeanFactory) {
            throw new IllegalStateException("Already associated with parent BeanFactory: " + this.parentBeanFactory);
        }
        this.parentBeanFactory = parentBeanFactory;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader != null ? beanClassLoader : ClassUtils.getDefaultClassLoader();
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public ClassLoader getBeanClassLoader() {
        return this.beanClassLoader;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void setTempClassLoader(ClassLoader tempClassLoader) {
        this.tempClassLoader = tempClassLoader;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public ClassLoader getTempClassLoader() {
        return this.tempClassLoader;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void setCacheBeanMetadata(boolean cacheBeanMetadata) {
        this.cacheBeanMetadata = cacheBeanMetadata;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public boolean isCacheBeanMetadata() {
        return this.cacheBeanMetadata;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void setBeanExpressionResolver(BeanExpressionResolver resolver) {
        this.beanExpressionResolver = resolver;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public BeanExpressionResolver getBeanExpressionResolver() {
        return this.beanExpressionResolver;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public ConversionService getConversionService() {
        return this.conversionService;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar) {
        Assert.notNull(registrar, "PropertyEditorRegistrar must not be null");
        this.propertyEditorRegistrars.add(registrar);
    }

    public Set<PropertyEditorRegistrar> getPropertyEditorRegistrars() {
        return this.propertyEditorRegistrars;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void registerCustomEditor(Class<?> requiredType, Class<? extends PropertyEditor> propertyEditorClass) {
        Assert.notNull(requiredType, "Required type must not be null");
        Assert.notNull(propertyEditorClass, "PropertyEditor class must not be null");
        this.customEditors.put(requiredType, propertyEditorClass);
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void copyRegisteredEditorsTo(PropertyEditorRegistry registry) {
        registerCustomEditors(registry);
    }

    public Map<Class<?>, Class<? extends PropertyEditor>> getCustomEditors() {
        return this.customEditors;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void setTypeConverter(TypeConverter typeConverter) {
        this.typeConverter = typeConverter;
    }

    protected TypeConverter getCustomTypeConverter() {
        return this.typeConverter;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public TypeConverter getTypeConverter() {
        TypeConverter customConverter = getCustomTypeConverter();
        if (customConverter != null) {
            return customConverter;
        }
        SimpleTypeConverter typeConverter = new SimpleTypeConverter();
        typeConverter.setConversionService(getConversionService());
        registerCustomEditors(typeConverter);
        return typeConverter;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void addEmbeddedValueResolver(StringValueResolver valueResolver) {
        Assert.notNull(valueResolver, "StringValueResolver must not be null");
        this.embeddedValueResolvers.add(valueResolver);
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public boolean hasEmbeddedValueResolver() {
        return !this.embeddedValueResolvers.isEmpty();
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public String resolveEmbeddedValue(String value) {
        if (value == null) {
            return null;
        }
        String result = value;
        for (StringValueResolver resolver : this.embeddedValueResolvers) {
            result = resolver.resolveStringValue(result);
            if (result == null) {
                return null;
            }
        }
        return result;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        Assert.notNull(beanPostProcessor, "BeanPostProcessor must not be null");
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
        if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
            this.hasInstantiationAwareBeanPostProcessors = true;
        }
        if (beanPostProcessor instanceof DestructionAwareBeanPostProcessor) {
            this.hasDestructionAwareBeanPostProcessors = true;
        }
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public int getBeanPostProcessorCount() {
        return this.beanPostProcessors.size();
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    protected boolean hasInstantiationAwareBeanPostProcessors() {
        return this.hasInstantiationAwareBeanPostProcessors;
    }

    protected boolean hasDestructionAwareBeanPostProcessors() {
        return this.hasDestructionAwareBeanPostProcessors;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void registerScope(String scopeName, Scope scope) {
        Assert.notNull(scopeName, "Scope identifier must not be null");
        Assert.notNull(scope, "Scope must not be null");
        if ("singleton".equals(scopeName) || "prototype".equals(scopeName)) {
            throw new IllegalArgumentException("Cannot replace existing scopes 'singleton' and 'prototype'");
        }
        Scope previous = this.scopes.put(scopeName, scope);
        if (previous != null && previous != scope) {
            if (this.logger.isInfoEnabled()) {
                this.logger.info("Replacing scope '" + scopeName + "' from [" + previous + "] to [" + scope + "]");
            }
        } else if (this.logger.isDebugEnabled()) {
            this.logger.debug("Registering scope '" + scopeName + "' with implementation [" + scope + "]");
        }
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public String[] getRegisteredScopeNames() {
        return StringUtils.toStringArray(this.scopes.keySet());
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public Scope getRegisteredScope(String scopeName) {
        Assert.notNull(scopeName, "Scope identifier must not be null");
        return this.scopes.get(scopeName);
    }

    public void setSecurityContextProvider(SecurityContextProvider securityProvider) {
        this.securityContextProvider = securityProvider;
    }

    @Override // org.springframework.beans.factory.support.FactoryBeanRegistrySupport, org.springframework.beans.factory.config.ConfigurableBeanFactory
    public AccessControlContext getAccessControlContext() {
        if (this.securityContextProvider != null) {
            return this.securityContextProvider.getAccessControlContext();
        }
        return AccessController.getContext();
    }

    public void copyConfigurationFrom(ConfigurableBeanFactory otherFactory) {
        Assert.notNull(otherFactory, "BeanFactory must not be null");
        setBeanClassLoader(otherFactory.getBeanClassLoader());
        setCacheBeanMetadata(otherFactory.isCacheBeanMetadata());
        setBeanExpressionResolver(otherFactory.getBeanExpressionResolver());
        setConversionService(otherFactory.getConversionService());
        if (otherFactory instanceof AbstractBeanFactory) {
            AbstractBeanFactory otherAbstractFactory = (AbstractBeanFactory) otherFactory;
            this.propertyEditorRegistrars.addAll(otherAbstractFactory.propertyEditorRegistrars);
            this.customEditors.putAll(otherAbstractFactory.customEditors);
            this.typeConverter = otherAbstractFactory.typeConverter;
            this.beanPostProcessors.addAll(otherAbstractFactory.beanPostProcessors);
            this.hasInstantiationAwareBeanPostProcessors = this.hasInstantiationAwareBeanPostProcessors || otherAbstractFactory.hasInstantiationAwareBeanPostProcessors;
            this.hasDestructionAwareBeanPostProcessors = this.hasDestructionAwareBeanPostProcessors || otherAbstractFactory.hasDestructionAwareBeanPostProcessors;
            this.scopes.putAll(otherAbstractFactory.scopes);
            this.securityContextProvider = otherAbstractFactory.securityContextProvider;
            return;
        }
        setTypeConverter(otherFactory.getTypeConverter());
        String[] otherScopeNames = otherFactory.getRegisteredScopeNames();
        for (String scopeName : otherScopeNames) {
            this.scopes.put(scopeName, otherFactory.getRegisteredScope(scopeName));
        }
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public BeanDefinition getMergedBeanDefinition(String name) throws BeansException {
        String beanName = transformedBeanName(name);
        if (!containsBeanDefinition(beanName) && (getParentBeanFactory() instanceof ConfigurableBeanFactory)) {
            return ((ConfigurableBeanFactory) getParentBeanFactory()).getMergedBeanDefinition(beanName);
        }
        return getMergedLocalBeanDefinition(beanName);
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public boolean isFactoryBean(String name) throws NoSuchBeanDefinitionException {
        String beanName = transformedBeanName(name);
        Object beanInstance = getSingleton(beanName, false);
        if (beanInstance != null) {
            return beanInstance instanceof FactoryBean;
        }
        if (containsSingleton(beanName)) {
            return false;
        }
        if (!containsBeanDefinition(beanName) && (getParentBeanFactory() instanceof ConfigurableBeanFactory)) {
            return ((ConfigurableBeanFactory) getParentBeanFactory()).isFactoryBean(name);
        }
        return isFactoryBean(beanName, getMergedLocalBeanDefinition(beanName));
    }

    @Override // org.springframework.beans.factory.support.DefaultSingletonBeanRegistry
    public boolean isActuallyInCreation(String beanName) {
        return isSingletonCurrentlyInCreation(beanName) || isPrototypeCurrentlyInCreation(beanName);
    }

    protected boolean isPrototypeCurrentlyInCreation(String beanName) {
        Object curVal = this.prototypesCurrentlyInCreation.get();
        return curVal != null && (curVal.equals(beanName) || ((curVal instanceof Set) && ((Set) curVal).contains(beanName)));
    }

    protected void beforePrototypeCreation(String beanName) {
        Object curVal = this.prototypesCurrentlyInCreation.get();
        if (curVal == null) {
            this.prototypesCurrentlyInCreation.set(beanName);
            return;
        }
        if (curVal instanceof String) {
            Set<String> beanNameSet = new HashSet<>(2);
            beanNameSet.add((String) curVal);
            beanNameSet.add(beanName);
            this.prototypesCurrentlyInCreation.set(beanNameSet);
            return;
        }
        ((Set) curVal).add(beanName);
    }

    protected void afterPrototypeCreation(String beanName) {
        Object curVal = this.prototypesCurrentlyInCreation.get();
        if (curVal instanceof String) {
            this.prototypesCurrentlyInCreation.remove();
            return;
        }
        if (curVal instanceof Set) {
            Set<String> beanNameSet = (Set) curVal;
            beanNameSet.remove(beanName);
            if (beanNameSet.isEmpty()) {
                this.prototypesCurrentlyInCreation.remove();
            }
        }
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void destroyBean(String beanName, Object beanInstance) {
        destroyBean(beanName, beanInstance, getMergedLocalBeanDefinition(beanName));
    }

    protected void destroyBean(String beanName, Object bean, RootBeanDefinition mbd) {
        new DisposableBeanAdapter(bean, beanName, mbd, getBeanPostProcessors(), getAccessControlContext()).destroy();
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void destroyScopedBean(String beanName) throws BeansException {
        RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
        if (mbd.isSingleton() || mbd.isPrototype()) {
            throw new IllegalArgumentException("Bean name '" + beanName + "' does not correspond to an object in a mutable scope");
        }
        String scopeName = mbd.getScope();
        Scope scope = this.scopes.get(scopeName);
        if (scope == null) {
            throw new IllegalStateException("No Scope SPI registered for scope name '" + scopeName + "'");
        }
        Object bean = scope.remove(beanName);
        if (bean != null) {
            destroyBean(beanName, bean, mbd);
        }
    }

    protected String transformedBeanName(String name) {
        return canonicalName(BeanFactoryUtils.transformedBeanName(name));
    }

    protected String originalBeanName(String name) {
        String beanName = transformedBeanName(name);
        if (name.startsWith("&")) {
            beanName = "&" + beanName;
        }
        return beanName;
    }

    protected void initBeanWrapper(BeanWrapper bw) {
        bw.setConversionService(getConversionService());
        registerCustomEditors(bw);
    }

    protected void registerCustomEditors(PropertyEditorRegistry registry) {
        PropertyEditorRegistrySupport registrySupport = registry instanceof PropertyEditorRegistrySupport ? (PropertyEditorRegistrySupport) registry : null;
        if (registrySupport != null) {
            registrySupport.useConfigValueEditors();
        }
        if (!this.propertyEditorRegistrars.isEmpty()) {
            for (PropertyEditorRegistrar registrar : this.propertyEditorRegistrars) {
                try {
                    registrar.registerCustomEditors(registry);
                } catch (BeanCreationException ex) {
                    Throwable rootCause = ex.getMostSpecificCause();
                    if (rootCause instanceof BeanCurrentlyInCreationException) {
                        BeanCreationException bce = (BeanCreationException) rootCause;
                        if (isCurrentlyInCreation(bce.getBeanName())) {
                            if (this.logger.isDebugEnabled()) {
                                this.logger.debug("PropertyEditorRegistrar [" + registrar.getClass().getName() + "] failed because it tried to obtain currently created bean '" + ex.getBeanName() + "': " + ex.getMessage());
                            }
                            onSuppressedException(ex);
                        }
                    }
                    throw ex;
                }
            }
        }
        if (!this.customEditors.isEmpty()) {
            for (Map.Entry<Class<?>, Class<? extends PropertyEditor>> entry : this.customEditors.entrySet()) {
                Class<?> requiredType = entry.getKey();
                Class<? extends PropertyEditor> editorClass = entry.getValue();
                registry.registerCustomEditor(requiredType, (PropertyEditor) BeanUtils.instantiateClass(editorClass));
            }
        }
    }

    protected RootBeanDefinition getMergedLocalBeanDefinition(String beanName) throws BeansException {
        RootBeanDefinition mbd = this.mergedBeanDefinitions.get(beanName);
        if (mbd != null) {
            return mbd;
        }
        return getMergedBeanDefinition(beanName, getBeanDefinition(beanName));
    }

    protected RootBeanDefinition getMergedBeanDefinition(String beanName, BeanDefinition bd) throws BeanDefinitionStoreException {
        return getMergedBeanDefinition(beanName, bd, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:9:0x0023 A[Catch: all -> 0x014f, TryCatch #1 {, blocks: (B:6:0x000f, B:9:0x0023, B:11:0x002c, B:13:0x0033, B:28:0x00ff, B:30:0x010a, B:33:0x0115, B:35:0x011e, B:37:0x0126, B:40:0x0135, B:42:0x013c, B:14:0x003f, B:15:0x004c, B:17:0x0061, B:27:0x00ee, B:18:0x006c, B:20:0x007a, B:21:0x008b, B:22:0x00b9, B:25:0x00bf, B:26:0x00ed, B:44:0x014d), top: B:52:0x000f, inners: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected org.springframework.beans.factory.support.RootBeanDefinition getMergedBeanDefinition(java.lang.String r8, org.springframework.beans.factory.config.BeanDefinition r9, org.springframework.beans.factory.config.BeanDefinition r10) throws org.springframework.beans.factory.BeanDefinitionStoreException {
        /*
            Method dump skipped, instructions count: 343
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.springframework.beans.factory.support.AbstractBeanFactory.getMergedBeanDefinition(java.lang.String, org.springframework.beans.factory.config.BeanDefinition, org.springframework.beans.factory.config.BeanDefinition):org.springframework.beans.factory.support.RootBeanDefinition");
    }

    protected void checkMergedBeanDefinition(RootBeanDefinition mbd, String beanName, Object[] args) throws BeanDefinitionStoreException {
        if (mbd.isAbstract()) {
            throw new BeanIsAbstractException(beanName);
        }
    }

    protected void clearMergedBeanDefinition(String beanName) {
        this.mergedBeanDefinitions.remove(beanName);
    }

    public void clearMetadataCache() {
        Iterator<String> mergedBeans = this.mergedBeanDefinitions.keySet().iterator();
        while (mergedBeans.hasNext()) {
            if (!isBeanEligibleForMetadataCaching(mergedBeans.next())) {
                mergedBeans.remove();
            }
        }
    }

    protected Class<?> resolveBeanClass(final RootBeanDefinition mbd, String beanName, final Class<?>... typesToMatch) throws CannotLoadBeanClassException {
        try {
            if (mbd.hasBeanClass()) {
                return mbd.getBeanClass();
            }
            if (System.getSecurityManager() != null) {
                return (Class) AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>() { // from class: org.springframework.beans.factory.support.AbstractBeanFactory.4
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public Class<?> run() throws Exception {
                        return AbstractBeanFactory.this.doResolveBeanClass(mbd, typesToMatch);
                    }
                }, getAccessControlContext());
            }
            return doResolveBeanClass(mbd, typesToMatch);
        } catch (ClassNotFoundException ex) {
            throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), ex);
        } catch (LinkageError err) {
            throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), err);
        } catch (PrivilegedActionException pae) {
            ClassNotFoundException ex2 = (ClassNotFoundException) pae.getException();
            throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), ex2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Class<?> doResolveBeanClass(RootBeanDefinition mbd, Class<?>... typesToMatch) throws ClassNotFoundException {
        ClassLoader tempClassLoader;
        ClassLoader beanClassLoader = getBeanClassLoader();
        ClassLoader classLoaderToUse = beanClassLoader;
        if (!ObjectUtils.isEmpty((Object[]) typesToMatch) && (tempClassLoader = getTempClassLoader()) != null) {
            classLoaderToUse = tempClassLoader;
            if (tempClassLoader instanceof DecoratingClassLoader) {
                DecoratingClassLoader dcl = (DecoratingClassLoader) tempClassLoader;
                for (Class<?> typeToMatch : typesToMatch) {
                    dcl.excludeClass(typeToMatch.getName());
                }
            }
        }
        String className = mbd.getBeanClassName();
        if (className != null) {
            Object evaluated = evaluateBeanDefinitionString(className, mbd);
            if (!className.equals(evaluated)) {
                if (evaluated instanceof Class) {
                    return (Class) evaluated;
                }
                if (evaluated instanceof String) {
                    return ClassUtils.forName((String) evaluated, classLoaderToUse);
                }
                throw new IllegalStateException("Invalid class name expression result: " + evaluated);
            }
            if (classLoaderToUse != beanClassLoader) {
                return ClassUtils.forName(className, classLoaderToUse);
            }
        }
        return mbd.resolveBeanClass(beanClassLoader);
    }

    protected Object evaluateBeanDefinitionString(String value, BeanDefinition beanDefinition) {
        if (this.beanExpressionResolver == null) {
            return value;
        }
        Scope scope = beanDefinition != null ? getRegisteredScope(beanDefinition.getScope()) : null;
        return this.beanExpressionResolver.evaluate(value, new BeanExpressionContext(this, scope));
    }

    protected Class<?> predictBeanType(String beanName, RootBeanDefinition mbd, Class<?>... typesToMatch) {
        Class<?> targetType = mbd.getTargetType();
        if (targetType != null) {
            return targetType;
        }
        if (mbd.getFactoryMethodName() != null) {
            return null;
        }
        return resolveBeanClass(mbd, beanName, typesToMatch);
    }

    protected boolean isFactoryBean(String beanName, RootBeanDefinition mbd) {
        Class<?> beanType = predictBeanType(beanName, mbd, FactoryBean.class);
        return beanType != null && FactoryBean.class.isAssignableFrom(beanType);
    }

    protected Class<?> getTypeForFactoryBean(String beanName, RootBeanDefinition mbd) {
        if (!mbd.isSingleton()) {
            return null;
        }
        try {
            FactoryBean<?> factoryBean = (FactoryBean) doGetBean("&" + beanName, FactoryBean.class, null, true);
            return getTypeForFactoryBean(factoryBean);
        } catch (BeanCreationException ex) {
            if (ex.contains(BeanCurrentlyInCreationException.class)) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Bean currently in creation on FactoryBean type check: " + ex);
                }
            } else if (mbd.isLazyInit()) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Bean creation exception on lazy FactoryBean type check: " + ex);
                }
            } else if (this.logger.isWarnEnabled()) {
                this.logger.warn("Bean creation exception on non-lazy FactoryBean type check: " + ex);
            }
            onSuppressedException(ex);
            return null;
        }
    }

    protected void markBeanAsCreated(String beanName) {
        if (!this.alreadyCreated.contains(beanName)) {
            synchronized (this.mergedBeanDefinitions) {
                if (!this.alreadyCreated.contains(beanName)) {
                    clearMergedBeanDefinition(beanName);
                    this.alreadyCreated.add(beanName);
                }
            }
        }
    }

    protected void cleanupAfterBeanCreationFailure(String beanName) {
        synchronized (this.mergedBeanDefinitions) {
            this.alreadyCreated.remove(beanName);
        }
    }

    protected boolean isBeanEligibleForMetadataCaching(String beanName) {
        return this.alreadyCreated.contains(beanName);
    }

    protected boolean removeSingletonIfCreatedForTypeCheckOnly(String beanName) {
        if (!this.alreadyCreated.contains(beanName)) {
            removeSingleton(beanName);
            return true;
        }
        return false;
    }

    protected boolean hasBeanCreationStarted() {
        return !this.alreadyCreated.isEmpty();
    }

    protected Object getObjectForBeanInstance(Object beanInstance, String name, String beanName, RootBeanDefinition mbd) throws BeansException {
        if (BeanFactoryUtils.isFactoryDereference(name) && !(beanInstance instanceof FactoryBean)) {
            throw new BeanIsNotAFactoryException(transformedBeanName(name), beanInstance.getClass());
        }
        if (!(beanInstance instanceof FactoryBean) || BeanFactoryUtils.isFactoryDereference(name)) {
            return beanInstance;
        }
        Object object = null;
        if (mbd == null) {
            object = getCachedObjectForFactoryBean(beanName);
        }
        if (object == null) {
            FactoryBean<?> factory = (FactoryBean) beanInstance;
            if (mbd == null && containsBeanDefinition(beanName)) {
                mbd = getMergedLocalBeanDefinition(beanName);
            }
            boolean synthetic = mbd != null && mbd.isSynthetic();
            object = getObjectFromFactoryBean(factory, beanName, !synthetic);
        }
        return object;
    }

    public boolean isBeanNameInUse(String beanName) {
        return isAlias(beanName) || containsLocalBean(beanName) || hasDependentBean(beanName);
    }

    protected boolean requiresDestruction(Object bean, RootBeanDefinition mbd) {
        return bean != null && (DisposableBeanAdapter.hasDestroyMethod(bean, mbd) || (hasDestructionAwareBeanPostProcessors() && DisposableBeanAdapter.hasApplicableProcessors(bean, getBeanPostProcessors())));
    }

    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, RootBeanDefinition mbd) {
        AccessControlContext acc = System.getSecurityManager() != null ? getAccessControlContext() : null;
        if (!mbd.isPrototype() && requiresDestruction(bean, mbd)) {
            if (mbd.isSingleton()) {
                registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, mbd, getBeanPostProcessors(), acc));
                return;
            }
            Scope scope = this.scopes.get(mbd.getScope());
            if (scope == null) {
                throw new IllegalStateException("No Scope registered for scope name '" + mbd.getScope() + "'");
            }
            scope.registerDestructionCallback(beanName, new DisposableBeanAdapter(bean, beanName, mbd, getBeanPostProcessors(), acc));
        }
    }
}
