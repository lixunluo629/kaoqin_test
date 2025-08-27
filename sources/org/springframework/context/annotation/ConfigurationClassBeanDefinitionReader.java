package org.springframework.context.annotation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader;
import org.springframework.beans.factory.parsing.SourceExtractor;
import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.GroovyWebApplicationContext;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/ConfigurationClassBeanDefinitionReader.class */
class ConfigurationClassBeanDefinitionReader {
    private static final Log logger = LogFactory.getLog(ConfigurationClassBeanDefinitionReader.class);
    private static final ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();
    private final BeanDefinitionRegistry registry;
    private final SourceExtractor sourceExtractor;
    private final ResourceLoader resourceLoader;
    private final Environment environment;
    private final BeanNameGenerator importBeanNameGenerator;
    private final ImportRegistry importRegistry;
    private final ConditionEvaluator conditionEvaluator;

    ConfigurationClassBeanDefinitionReader(BeanDefinitionRegistry registry, SourceExtractor sourceExtractor, ResourceLoader resourceLoader, Environment environment, BeanNameGenerator importBeanNameGenerator, ImportRegistry importRegistry) {
        this.registry = registry;
        this.sourceExtractor = sourceExtractor;
        this.resourceLoader = resourceLoader;
        this.environment = environment;
        this.importBeanNameGenerator = importBeanNameGenerator;
        this.importRegistry = importRegistry;
        this.conditionEvaluator = new ConditionEvaluator(registry, environment, resourceLoader);
    }

    public void loadBeanDefinitions(Set<ConfigurationClass> configurationModel) throws NoSuchBeanDefinitionException, BeanDefinitionStoreException {
        TrackedConditionEvaluator trackedConditionEvaluator = new TrackedConditionEvaluator();
        for (ConfigurationClass configClass : configurationModel) {
            loadBeanDefinitionsForConfigurationClass(configClass, trackedConditionEvaluator);
        }
    }

    private void loadBeanDefinitionsForConfigurationClass(ConfigurationClass configClass, TrackedConditionEvaluator trackedConditionEvaluator) throws NoSuchBeanDefinitionException, BeanDefinitionStoreException {
        if (trackedConditionEvaluator.shouldSkip(configClass)) {
            String beanName = configClass.getBeanName();
            if (StringUtils.hasLength(beanName) && this.registry.containsBeanDefinition(beanName)) {
                this.registry.removeBeanDefinition(beanName);
            }
            this.importRegistry.removeImportingClass(configClass.getMetadata().getClassName());
            return;
        }
        if (configClass.isImported()) {
            registerBeanDefinitionForImportedConfigurationClass(configClass);
        }
        for (BeanMethod beanMethod : configClass.getBeanMethods()) {
            loadBeanDefinitionsForBeanMethod(beanMethod);
        }
        loadBeanDefinitionsFromImportedResources(configClass.getImportedResources());
        loadBeanDefinitionsFromRegistrars(configClass.getImportBeanDefinitionRegistrars());
    }

    private void registerBeanDefinitionForImportedConfigurationClass(ConfigurationClass configClass) throws BeanDefinitionStoreException {
        AnnotationMetadata metadata = configClass.getMetadata();
        AnnotatedGenericBeanDefinition configBeanDef = new AnnotatedGenericBeanDefinition(metadata);
        ScopeMetadata scopeMetadata = scopeMetadataResolver.resolveScopeMetadata(configBeanDef);
        configBeanDef.setScope(scopeMetadata.getScopeName());
        String configBeanName = this.importBeanNameGenerator.generateBeanName(configBeanDef, this.registry);
        AnnotationConfigUtils.processCommonDefinitionAnnotations(configBeanDef, metadata);
        BeanDefinitionHolder definitionHolder = AnnotationConfigUtils.applyScopedProxyMode(scopeMetadata, new BeanDefinitionHolder(configBeanDef, configBeanName), this.registry);
        this.registry.registerBeanDefinition(definitionHolder.getBeanName(), definitionHolder.getBeanDefinition());
        configClass.setBeanName(configBeanName);
        if (logger.isDebugEnabled()) {
            logger.debug("Registered bean definition for imported class '" + configBeanName + "'");
        }
    }

    private void loadBeanDefinitionsForBeanMethod(BeanMethod beanMethod) throws BeanDefinitionStoreException {
        ConfigurationClass configClass = beanMethod.getConfigurationClass();
        MethodMetadata metadata = beanMethod.getMetadata();
        String methodName = metadata.getMethodName();
        if (this.conditionEvaluator.shouldSkip(metadata, ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN)) {
            configClass.skippedBeanMethods.add(methodName);
            return;
        }
        if (configClass.skippedBeanMethods.contains(methodName)) {
            return;
        }
        AnnotationAttributes bean = AnnotationConfigUtils.attributesFor(metadata, (Class<?>) Bean.class);
        List<String> names = new ArrayList<>(Arrays.asList(bean.getStringArray("name")));
        String beanName = !names.isEmpty() ? names.remove(0) : methodName;
        for (String alias : names) {
            this.registry.registerAlias(beanName, alias);
        }
        if (isOverriddenByExistingDefinition(beanMethod, beanName)) {
            if (beanName.equals(beanMethod.getConfigurationClass().getBeanName())) {
                throw new BeanDefinitionStoreException(beanMethod.getConfigurationClass().getResource().getDescription(), beanName, "Bean name derived from @Bean method '" + beanMethod.getMetadata().getMethodName() + "' clashes with bean name for containing configuration class; please make those names unique!");
            }
            return;
        }
        ConfigurationClassBeanDefinition beanDef = new ConfigurationClassBeanDefinition(configClass, metadata);
        beanDef.setResource(configClass.getResource());
        beanDef.setSource(this.sourceExtractor.extractSource(metadata, configClass.getResource()));
        if (metadata.isStatic()) {
            beanDef.setBeanClassName(configClass.getMetadata().getClassName());
            beanDef.setFactoryMethodName(methodName);
        } else {
            beanDef.setFactoryBeanName(configClass.getBeanName());
            beanDef.setUniqueFactoryMethodName(methodName);
        }
        beanDef.setAutowireMode(3);
        beanDef.setAttribute(RequiredAnnotationBeanPostProcessor.SKIP_REQUIRED_CHECK_ATTRIBUTE, Boolean.TRUE);
        AnnotationConfigUtils.processCommonDefinitionAnnotations(beanDef, metadata);
        Autowire autowire = (Autowire) bean.getEnum(BeanDefinitionParserDelegate.AUTOWIRE_ATTRIBUTE);
        if (autowire.isAutowire()) {
            beanDef.setAutowireMode(autowire.value());
        }
        String initMethodName = bean.getString("initMethod");
        if (StringUtils.hasText(initMethodName)) {
            beanDef.setInitMethodName(initMethodName);
        }
        String destroyMethodName = bean.getString("destroyMethod");
        if (destroyMethodName != null) {
            beanDef.setDestroyMethodName(destroyMethodName);
        }
        ScopedProxyMode proxyMode = ScopedProxyMode.NO;
        AnnotationAttributes attributes = AnnotationConfigUtils.attributesFor(metadata, (Class<?>) Scope.class);
        if (attributes != null) {
            beanDef.setScope(attributes.getString("value"));
            proxyMode = (ScopedProxyMode) attributes.getEnum("proxyMode");
            if (proxyMode == ScopedProxyMode.DEFAULT) {
                proxyMode = ScopedProxyMode.NO;
            }
        }
        BeanDefinition beanDefToRegister = beanDef;
        if (proxyMode != ScopedProxyMode.NO) {
            BeanDefinitionHolder proxyDef = ScopedProxyCreator.createScopedProxy(new BeanDefinitionHolder(beanDef, beanName), this.registry, proxyMode == ScopedProxyMode.TARGET_CLASS);
            beanDefToRegister = new ConfigurationClassBeanDefinition((RootBeanDefinition) proxyDef.getBeanDefinition(), configClass, metadata);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Registering bean definition for @Bean method %s.%s()", configClass.getMetadata().getClassName(), beanName));
        }
        this.registry.registerBeanDefinition(beanName, beanDefToRegister);
    }

    protected boolean isOverriddenByExistingDefinition(BeanMethod beanMethod, String beanName) throws NoSuchBeanDefinitionException {
        if (!this.registry.containsBeanDefinition(beanName)) {
            return false;
        }
        BeanDefinition existingBeanDef = this.registry.getBeanDefinition(beanName);
        if (existingBeanDef instanceof ConfigurationClassBeanDefinition) {
            ConfigurationClassBeanDefinition ccbd = (ConfigurationClassBeanDefinition) existingBeanDef;
            return ccbd.getMetadata().getClassName().equals(beanMethod.getConfigurationClass().getMetadata().getClassName());
        }
        if ((existingBeanDef instanceof ScannedGenericBeanDefinition) || existingBeanDef.getRole() > 0) {
            return false;
        }
        if ((this.registry instanceof DefaultListableBeanFactory) && !((DefaultListableBeanFactory) this.registry).isAllowBeanDefinitionOverriding()) {
            throw new BeanDefinitionStoreException(beanMethod.getConfigurationClass().getResource().getDescription(), beanName, "@Bean definition illegally overridden by existing bean definition: " + existingBeanDef);
        }
        if (logger.isInfoEnabled()) {
            logger.info(String.format("Skipping bean definition for %s: a definition for bean '%s' already exists. This top-level bean definition is considered as an override.", beanMethod, beanName));
            return true;
        }
        return true;
    }

    private void loadBeanDefinitionsFromImportedResources(Map<String, Class<? extends BeanDefinitionReader>> importedResources) throws BeanDefinitionStoreException {
        Map<Class<?>, BeanDefinitionReader> readerInstanceCache = new HashMap<>();
        for (Map.Entry<String, Class<? extends BeanDefinitionReader>> entry : importedResources.entrySet()) {
            String resource = entry.getKey();
            Class<?> value = entry.getValue();
            if (BeanDefinitionReader.class == value) {
                if (StringUtils.endsWithIgnoreCase(resource, GroovyWebApplicationContext.DEFAULT_CONFIG_LOCATION_SUFFIX)) {
                    value = GroovyBeanDefinitionReader.class;
                } else {
                    value = XmlBeanDefinitionReader.class;
                }
            }
            BeanDefinitionReader reader = readerInstanceCache.get(value);
            if (reader == null) {
                try {
                    reader = value.getConstructor(BeanDefinitionRegistry.class).newInstance(this.registry);
                    if (reader instanceof AbstractBeanDefinitionReader) {
                        AbstractBeanDefinitionReader abdr = (AbstractBeanDefinitionReader) reader;
                        abdr.setResourceLoader(this.resourceLoader);
                        abdr.setEnvironment(this.environment);
                    }
                    readerInstanceCache.put(value, reader);
                } catch (Throwable th) {
                    throw new IllegalStateException("Could not instantiate BeanDefinitionReader class [" + value.getName() + "]");
                }
            }
            reader.loadBeanDefinitions(resource);
        }
    }

    private void loadBeanDefinitionsFromRegistrars(Map<ImportBeanDefinitionRegistrar, AnnotationMetadata> registrars) {
        for (Map.Entry<ImportBeanDefinitionRegistrar, AnnotationMetadata> entry : registrars.entrySet()) {
            entry.getKey().registerBeanDefinitions(entry.getValue(), this.registry);
        }
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/ConfigurationClassBeanDefinitionReader$ConfigurationClassBeanDefinition.class */
    private static class ConfigurationClassBeanDefinition extends RootBeanDefinition implements AnnotatedBeanDefinition {
        private final AnnotationMetadata annotationMetadata;
        private final MethodMetadata factoryMethodMetadata;

        public ConfigurationClassBeanDefinition(ConfigurationClass configClass, MethodMetadata beanMethodMetadata) {
            this.annotationMetadata = configClass.getMetadata();
            this.factoryMethodMetadata = beanMethodMetadata;
            setLenientConstructorResolution(false);
        }

        public ConfigurationClassBeanDefinition(RootBeanDefinition original, ConfigurationClass configClass, MethodMetadata beanMethodMetadata) {
            super(original);
            this.annotationMetadata = configClass.getMetadata();
            this.factoryMethodMetadata = beanMethodMetadata;
        }

        private ConfigurationClassBeanDefinition(ConfigurationClassBeanDefinition original) {
            super((RootBeanDefinition) original);
            this.annotationMetadata = original.annotationMetadata;
            this.factoryMethodMetadata = original.factoryMethodMetadata;
        }

        @Override // org.springframework.beans.factory.annotation.AnnotatedBeanDefinition
        public AnnotationMetadata getMetadata() {
            return this.annotationMetadata;
        }

        @Override // org.springframework.beans.factory.annotation.AnnotatedBeanDefinition
        public MethodMetadata getFactoryMethodMetadata() {
            return this.factoryMethodMetadata;
        }

        @Override // org.springframework.beans.factory.support.RootBeanDefinition
        public boolean isFactoryMethod(Method candidate) {
            return super.isFactoryMethod(candidate) && BeanAnnotationHelper.isBeanAnnotated(candidate);
        }

        @Override // org.springframework.beans.factory.support.RootBeanDefinition, org.springframework.beans.factory.support.AbstractBeanDefinition
        public ConfigurationClassBeanDefinition cloneBeanDefinition() {
            return new ConfigurationClassBeanDefinition(this);
        }
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/ConfigurationClassBeanDefinitionReader$TrackedConditionEvaluator.class */
    private class TrackedConditionEvaluator {
        private final Map<ConfigurationClass, Boolean> skipped;

        private TrackedConditionEvaluator() {
            this.skipped = new HashMap();
        }

        public boolean shouldSkip(ConfigurationClass configClass) {
            Boolean skip = this.skipped.get(configClass);
            if (skip == null) {
                if (configClass.isImported()) {
                    boolean allSkipped = true;
                    Iterator<ConfigurationClass> it = configClass.getImportedBy().iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        ConfigurationClass importedBy = it.next();
                        if (!shouldSkip(importedBy)) {
                            allSkipped = false;
                            break;
                        }
                    }
                    if (allSkipped) {
                        skip = true;
                    }
                }
                if (skip == null) {
                    skip = Boolean.valueOf(ConfigurationClassBeanDefinitionReader.this.conditionEvaluator.shouldSkip(configClass.getMetadata(), ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN));
                }
                this.skipped.put(configClass, skip);
            }
            return skip.booleanValue();
        }
    }
}
