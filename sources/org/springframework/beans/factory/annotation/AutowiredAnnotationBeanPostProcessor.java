package org.springframework.beans.factory.annotation;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.beans.factory.support.LookupOverride;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.DefaultBindingErrorProcessor;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/annotation/AutowiredAnnotationBeanPostProcessor.class */
public class AutowiredAnnotationBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter implements MergedBeanDefinitionPostProcessor, PriorityOrdered, BeanFactoryAware {
    private ConfigurableListableBeanFactory beanFactory;
    protected final Log logger = LogFactory.getLog(getClass());
    private final Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet(4);
    private String requiredParameterName = DefaultBindingErrorProcessor.MISSING_FIELD_ERROR_CODE;
    private boolean requiredParameterValue = true;
    private int order = 2147483645;
    private final Set<String> lookupMethodsChecked = Collections.newSetFromMap(new ConcurrentHashMap(256));
    private final Map<Class<?>, Constructor<?>[]> candidateConstructorsCache = new ConcurrentHashMap(256);
    private final Map<String, InjectionMetadata> injectionMetadataCache = new ConcurrentHashMap(256);

    /* JADX WARN: Multi-variable type inference failed */
    public AutowiredAnnotationBeanPostProcessor() {
        this.autowiredAnnotationTypes.add(Autowired.class);
        this.autowiredAnnotationTypes.add(Value.class);
        try {
            this.autowiredAnnotationTypes.add(ClassUtils.forName("javax.inject.Inject", AutowiredAnnotationBeanPostProcessor.class.getClassLoader()));
            this.logger.info("JSR-330 'javax.inject.Inject' annotation found and supported for autowiring");
        } catch (ClassNotFoundException e) {
        }
    }

    public void setAutowiredAnnotationType(Class<? extends Annotation> autowiredAnnotationType) {
        Assert.notNull(autowiredAnnotationType, "'autowiredAnnotationType' must not be null");
        this.autowiredAnnotationTypes.clear();
        this.autowiredAnnotationTypes.add(autowiredAnnotationType);
    }

    public void setAutowiredAnnotationTypes(Set<Class<? extends Annotation>> autowiredAnnotationTypes) {
        Assert.notEmpty(autowiredAnnotationTypes, "'autowiredAnnotationTypes' must not be empty");
        this.autowiredAnnotationTypes.clear();
        this.autowiredAnnotationTypes.addAll(autowiredAnnotationTypes);
    }

    public void setRequiredParameterName(String requiredParameterName) {
        this.requiredParameterName = requiredParameterName;
    }

    public void setRequiredParameterValue(boolean requiredParameterValue) {
        this.requiredParameterValue = requiredParameterValue;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return this.order;
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) {
        if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
            throw new IllegalArgumentException("AutowiredAnnotationBeanPostProcessor requires a ConfigurableListableBeanFactory: " + beanFactory);
        }
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override // org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
        if (beanType != null) {
            InjectionMetadata metadata = findAutowiringMetadata(beanName, beanType, null);
            metadata.checkConfigMembers(beanDefinition);
        }
    }

    @Override // org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter, org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor
    public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, final String beanName) throws BeanCreationException, SecurityException, IllegalArgumentException {
        Class<?> userClass;
        if (!this.lookupMethodsChecked.contains(beanName)) {
            try {
                ReflectionUtils.doWithMethods(beanClass, new ReflectionUtils.MethodCallback() { // from class: org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.1
                    @Override // org.springframework.util.ReflectionUtils.MethodCallback
                    public void doWith(Method method) throws IllegalAccessException, IllegalArgumentException {
                        Lookup lookup = (Lookup) method.getAnnotation(Lookup.class);
                        if (lookup != null) {
                            LookupOverride override = new LookupOverride(method, lookup.value());
                            try {
                                RootBeanDefinition mbd = (RootBeanDefinition) AutowiredAnnotationBeanPostProcessor.this.beanFactory.getMergedBeanDefinition(beanName);
                                mbd.getMethodOverrides().addOverride(override);
                            } catch (NoSuchBeanDefinitionException e) {
                                throw new BeanCreationException(beanName, "Cannot apply @Lookup to beans without corresponding bean definition");
                            }
                        }
                    }
                });
                this.lookupMethodsChecked.add(beanName);
            } catch (IllegalStateException ex) {
                throw new BeanCreationException(beanName, "Lookup method resolution failed", ex);
            } catch (NoClassDefFoundError err) {
                throw new BeanCreationException(beanName, "Failed to introspect bean class [" + beanClass.getName() + "] for lookup method metadata: could not find class that it depends on", err);
            }
        }
        Constructor<?>[] candidateConstructors = this.candidateConstructorsCache.get(beanClass);
        if (candidateConstructors == null) {
            synchronized (this.candidateConstructorsCache) {
                candidateConstructors = this.candidateConstructorsCache.get(beanClass);
                if (candidateConstructors == null) {
                    try {
                        Constructor<?>[] rawCandidates = beanClass.getDeclaredConstructors();
                        List<Constructor<?>> candidates = new ArrayList<>(rawCandidates.length);
                        Constructor<?> requiredConstructor = null;
                        Constructor<?> defaultConstructor = null;
                        for (Constructor<?> candidate : rawCandidates) {
                            AnnotationAttributes ann = findAutowiredAnnotation(candidate);
                            if (ann == null && (userClass = ClassUtils.getUserClass(beanClass)) != beanClass) {
                                try {
                                    Constructor<?> superCtor = userClass.getDeclaredConstructor(candidate.getParameterTypes());
                                    ann = findAutowiredAnnotation(superCtor);
                                } catch (NoSuchMethodException e) {
                                }
                            }
                            if (ann != null) {
                                if (requiredConstructor != null) {
                                    throw new BeanCreationException(beanName, "Invalid autowire-marked constructor: " + candidate + ". Found constructor with 'required' Autowired annotation already: " + requiredConstructor);
                                }
                                boolean required = determineRequiredStatus(ann);
                                if (required) {
                                    if (!candidates.isEmpty()) {
                                        throw new BeanCreationException(beanName, "Invalid autowire-marked constructors: " + candidates + ". Found constructor with 'required' Autowired annotation: " + candidate);
                                    }
                                    requiredConstructor = candidate;
                                }
                                candidates.add(candidate);
                            } else if (candidate.getParameterTypes().length == 0) {
                                defaultConstructor = candidate;
                            }
                        }
                        if (!candidates.isEmpty()) {
                            if (requiredConstructor == null) {
                                if (defaultConstructor != null) {
                                    candidates.add(defaultConstructor);
                                } else if (candidates.size() == 1 && this.logger.isWarnEnabled()) {
                                    this.logger.warn("Inconsistent constructor declaration on bean with name '" + beanName + "': single autowire-marked constructor flagged as optional - this constructor is effectively required since there is no default constructor to fall back to: " + candidates.get(0));
                                }
                            }
                            candidateConstructors = (Constructor[]) candidates.toArray(new Constructor[candidates.size()]);
                        } else if (rawCandidates.length == 1 && rawCandidates[0].getParameterTypes().length > 0) {
                            candidateConstructors = new Constructor[]{rawCandidates[0]};
                        } else {
                            candidateConstructors = new Constructor[0];
                        }
                        this.candidateConstructorsCache.put(beanClass, candidateConstructors);
                    } catch (Throwable ex2) {
                        throw new BeanCreationException(beanName, "Resolution of declared constructors on bean Class [" + beanClass.getName() + "] from ClassLoader [" + beanClass.getClassLoader() + "] failed", ex2);
                    }
                }
            }
        }
        if (candidateConstructors.length > 0) {
            return candidateConstructors;
        }
        return null;
    }

    @Override // org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter, org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeanCreationException {
        InjectionMetadata metadata = findAutowiringMetadata(beanName, bean.getClass(), pvs);
        try {
            metadata.inject(bean, beanName, pvs);
            return pvs;
        } catch (BeanCreationException ex) {
            throw ex;
        } catch (Throwable ex2) {
            throw new BeanCreationException(beanName, "Injection of autowired dependencies failed", ex2);
        }
    }

    public void processInjection(Object bean) throws BeanCreationException {
        Class<?> clazz = bean.getClass();
        InjectionMetadata metadata = findAutowiringMetadata(clazz.getName(), clazz, null);
        try {
            metadata.inject(bean, null, null);
        } catch (BeanCreationException ex) {
            throw ex;
        } catch (Throwable ex2) {
            throw new BeanCreationException("Injection of autowired dependencies failed for class [" + clazz + "]", ex2);
        }
    }

    private InjectionMetadata findAutowiringMetadata(String beanName, Class<?> clazz, PropertyValues pvs) {
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
                        metadata = buildAutowiringMetadata(clazz);
                        this.injectionMetadataCache.put(cacheKey, metadata);
                    } catch (NoClassDefFoundError err) {
                        throw new IllegalStateException("Failed to introspect bean class [" + clazz.getName() + "] for autowiring metadata: could not find class that it depends on", err);
                    }
                }
            }
        }
        return metadata;
    }

    private InjectionMetadata buildAutowiringMetadata(final Class<?> clazz) throws SecurityException, IllegalArgumentException {
        LinkedList<InjectionMetadata.InjectedElement> elements = new LinkedList<>();
        Class<?> targetClass = clazz;
        do {
            final LinkedList<InjectionMetadata.InjectedElement> currElements = new LinkedList<>();
            ReflectionUtils.doWithLocalFields(targetClass, new ReflectionUtils.FieldCallback() { // from class: org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.2
                @Override // org.springframework.util.ReflectionUtils.FieldCallback
                public void doWith(Field field) throws IllegalAccessException, IllegalArgumentException {
                    AnnotationAttributes ann = AutowiredAnnotationBeanPostProcessor.this.findAutowiredAnnotation(field);
                    if (ann != null) {
                        if (Modifier.isStatic(field.getModifiers())) {
                            if (AutowiredAnnotationBeanPostProcessor.this.logger.isWarnEnabled()) {
                                AutowiredAnnotationBeanPostProcessor.this.logger.warn("Autowired annotation is not supported on static fields: " + field);
                            }
                        } else {
                            boolean required = AutowiredAnnotationBeanPostProcessor.this.determineRequiredStatus(ann);
                            currElements.add(AutowiredAnnotationBeanPostProcessor.this.new AutowiredFieldElement(field, required));
                        }
                    }
                }
            });
            ReflectionUtils.doWithLocalMethods(targetClass, new ReflectionUtils.MethodCallback() { // from class: org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.3
                @Override // org.springframework.util.ReflectionUtils.MethodCallback
                public void doWith(Method method) throws IllegalAccessException, SecurityException, BeansException, IllegalArgumentException {
                    AnnotationAttributes ann;
                    Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
                    if (BridgeMethodResolver.isVisibilityBridgeMethodPair(method, bridgedMethod) && (ann = AutowiredAnnotationBeanPostProcessor.this.findAutowiredAnnotation(bridgedMethod)) != null && method.equals(ClassUtils.getMostSpecificMethod(method, clazz))) {
                        if (Modifier.isStatic(method.getModifiers())) {
                            if (AutowiredAnnotationBeanPostProcessor.this.logger.isWarnEnabled()) {
                                AutowiredAnnotationBeanPostProcessor.this.logger.warn("Autowired annotation is not supported on static methods: " + method);
                            }
                        } else {
                            if (method.getParameterTypes().length == 0 && AutowiredAnnotationBeanPostProcessor.this.logger.isWarnEnabled()) {
                                AutowiredAnnotationBeanPostProcessor.this.logger.warn("Autowired annotation should only be used on methods with parameters: " + method);
                            }
                            boolean required = AutowiredAnnotationBeanPostProcessor.this.determineRequiredStatus(ann);
                            PropertyDescriptor pd = BeanUtils.findPropertyForMethod(bridgedMethod, clazz);
                            currElements.add(AutowiredAnnotationBeanPostProcessor.this.new AutowiredMethodElement(method, required, pd));
                        }
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

    /* JADX INFO: Access modifiers changed from: private */
    public AnnotationAttributes findAutowiredAnnotation(AccessibleObject ao) {
        if (ao.getAnnotations().length > 0) {
            for (Class<? extends Annotation> type : this.autowiredAnnotationTypes) {
                AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(ao, type);
                if (attributes != null) {
                    return attributes;
                }
            }
            return null;
        }
        return null;
    }

    protected boolean determineRequiredStatus(AnnotationAttributes ann) {
        return !ann.containsKey(this.requiredParameterName) || this.requiredParameterValue == ann.getBoolean(this.requiredParameterName);
    }

    protected <T> Map<String, T> findAutowireCandidates(Class<T> type) throws BeansException {
        if (this.beanFactory == null) {
            throw new IllegalStateException("No BeanFactory configured - override the getBeanOfType method or specify the 'beanFactory' property");
        }
        return BeanFactoryUtils.beansOfTypeIncludingAncestors(this.beanFactory, type);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerDependentBeans(String beanName, Set<String> autowiredBeanNames) {
        if (beanName != null) {
            for (String autowiredBeanName : autowiredBeanNames) {
                if (this.beanFactory.containsBean(autowiredBeanName)) {
                    this.beanFactory.registerDependentBean(autowiredBeanName, beanName);
                }
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Autowiring by type from bean name '" + beanName + "' to bean named '" + autowiredBeanName + "'");
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object resolvedCachedArgument(String beanName, Object cachedArgument) {
        if (cachedArgument instanceof DependencyDescriptor) {
            DependencyDescriptor descriptor = (DependencyDescriptor) cachedArgument;
            return this.beanFactory.resolveDependency(descriptor, beanName, null, null);
        }
        return cachedArgument;
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/annotation/AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.class */
    private class AutowiredFieldElement extends InjectionMetadata.InjectedElement {
        private final boolean required;
        private volatile boolean cached;
        private volatile Object cachedFieldValue;

        public AutowiredFieldElement(Field field, boolean required) {
            super(field, null);
            this.cached = false;
            this.required = required;
        }

        @Override // org.springframework.beans.factory.annotation.InjectionMetadata.InjectedElement
        protected void inject(Object bean, String beanName, PropertyValues pvs) throws Throwable {
            Object value;
            Field field = (Field) this.member;
            if (this.cached) {
                value = AutowiredAnnotationBeanPostProcessor.this.resolvedCachedArgument(beanName, this.cachedFieldValue);
            } else {
                DependencyDescriptor desc = new DependencyDescriptor(field, this.required);
                desc.setContainingClass(bean.getClass());
                Set<String> autowiredBeanNames = new LinkedHashSet<>(1);
                TypeConverter typeConverter = AutowiredAnnotationBeanPostProcessor.this.beanFactory.getTypeConverter();
                try {
                    value = AutowiredAnnotationBeanPostProcessor.this.beanFactory.resolveDependency(desc, beanName, autowiredBeanNames, typeConverter);
                    synchronized (this) {
                        if (!this.cached) {
                            if (value != null || this.required) {
                                this.cachedFieldValue = desc;
                                AutowiredAnnotationBeanPostProcessor.this.registerDependentBeans(beanName, autowiredBeanNames);
                                if (autowiredBeanNames.size() == 1) {
                                    String autowiredBeanName = autowiredBeanNames.iterator().next();
                                    if (AutowiredAnnotationBeanPostProcessor.this.beanFactory.containsBean(autowiredBeanName) && AutowiredAnnotationBeanPostProcessor.this.beanFactory.isTypeMatch(autowiredBeanName, field.getType())) {
                                        this.cachedFieldValue = new ShortcutDependencyDescriptor(desc, autowiredBeanName, field.getType());
                                    }
                                }
                            } else {
                                this.cachedFieldValue = null;
                            }
                            this.cached = true;
                        }
                    }
                } catch (BeansException ex) {
                    throw new UnsatisfiedDependencyException((String) null, beanName, new InjectionPoint(field), ex);
                }
            }
            if (value != null) {
                ReflectionUtils.makeAccessible(field);
                field.set(bean, value);
            }
        }
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/annotation/AutowiredAnnotationBeanPostProcessor$AutowiredMethodElement.class */
    private class AutowiredMethodElement extends InjectionMetadata.InjectedElement {
        private final boolean required;
        private volatile boolean cached;
        private volatile Object[] cachedMethodArguments;

        public AutowiredMethodElement(Method method, boolean required, PropertyDescriptor pd) {
            super(method, pd);
            this.cached = false;
            this.required = required;
        }

        @Override // org.springframework.beans.factory.annotation.InjectionMetadata.InjectedElement
        protected void inject(Object bean, String beanName, PropertyValues pvs) throws Throwable {
            Object[] arguments;
            if (checkPropertySkipping(pvs)) {
                return;
            }
            Method method = (Method) this.member;
            if (this.cached) {
                arguments = resolveCachedArguments(beanName);
            } else {
                Class<?>[] paramTypes = method.getParameterTypes();
                arguments = new Object[paramTypes.length];
                DependencyDescriptor[] descriptors = new DependencyDescriptor[paramTypes.length];
                Set<String> autowiredBeans = new LinkedHashSet<>(paramTypes.length);
                TypeConverter typeConverter = AutowiredAnnotationBeanPostProcessor.this.beanFactory.getTypeConverter();
                int i = 0;
                while (true) {
                    if (i >= arguments.length) {
                        break;
                    }
                    MethodParameter methodParam = new MethodParameter(method, i);
                    DependencyDescriptor currDesc = new DependencyDescriptor(methodParam, this.required);
                    currDesc.setContainingClass(bean.getClass());
                    descriptors[i] = currDesc;
                    try {
                        Object arg = AutowiredAnnotationBeanPostProcessor.this.beanFactory.resolveDependency(currDesc, beanName, autowiredBeans, typeConverter);
                        if (arg == null && !this.required) {
                            arguments = null;
                            break;
                        } else {
                            arguments[i] = arg;
                            i++;
                        }
                    } catch (BeansException ex) {
                        throw new UnsatisfiedDependencyException((String) null, beanName, new InjectionPoint(methodParam), ex);
                    }
                }
                synchronized (this) {
                    if (!this.cached) {
                        if (arguments != null) {
                            this.cachedMethodArguments = new Object[paramTypes.length];
                            for (int i2 = 0; i2 < arguments.length; i2++) {
                                this.cachedMethodArguments[i2] = descriptors[i2];
                            }
                            AutowiredAnnotationBeanPostProcessor.this.registerDependentBeans(beanName, autowiredBeans);
                            if (autowiredBeans.size() == paramTypes.length) {
                                Iterator<String> it = autowiredBeans.iterator();
                                for (int i3 = 0; i3 < paramTypes.length; i3++) {
                                    String autowiredBeanName = it.next();
                                    if (AutowiredAnnotationBeanPostProcessor.this.beanFactory.containsBean(autowiredBeanName) && AutowiredAnnotationBeanPostProcessor.this.beanFactory.isTypeMatch(autowiredBeanName, paramTypes[i3])) {
                                        this.cachedMethodArguments[i3] = new ShortcutDependencyDescriptor(descriptors[i3], autowiredBeanName, paramTypes[i3]);
                                    }
                                }
                            }
                        } else {
                            this.cachedMethodArguments = null;
                        }
                        this.cached = true;
                    }
                }
            }
            if (arguments != null) {
                try {
                    ReflectionUtils.makeAccessible(method);
                    method.invoke(bean, arguments);
                } catch (InvocationTargetException ex2) {
                    throw ex2.getTargetException();
                }
            }
        }

        private Object[] resolveCachedArguments(String beanName) {
            if (this.cachedMethodArguments == null) {
                return null;
            }
            Object[] arguments = new Object[this.cachedMethodArguments.length];
            for (int i = 0; i < arguments.length; i++) {
                arguments[i] = AutowiredAnnotationBeanPostProcessor.this.resolvedCachedArgument(beanName, this.cachedMethodArguments[i]);
            }
            return arguments;
        }
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/annotation/AutowiredAnnotationBeanPostProcessor$ShortcutDependencyDescriptor.class */
    private static class ShortcutDependencyDescriptor extends DependencyDescriptor {
        private final String shortcut;
        private final Class<?> requiredType;

        public ShortcutDependencyDescriptor(DependencyDescriptor original, String shortcut, Class<?> requiredType) {
            super(original);
            this.shortcut = shortcut;
            this.requiredType = requiredType;
        }

        @Override // org.springframework.beans.factory.config.DependencyDescriptor
        public Object resolveShortcut(BeanFactory beanFactory) {
            return resolveCandidate(this.shortcut, this.requiredType, beanFactory);
        }
    }
}
