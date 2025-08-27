package org.springframework.context.annotation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.naming.factory.Constants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.parsing.Location;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.beans.factory.parsing.ProblemReporter;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.NestedIOException;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/ConfigurationClassParser.class */
class ConfigurationClassParser {
    private static final PropertySourceFactory DEFAULT_PROPERTY_SOURCE_FACTORY = new DefaultPropertySourceFactory();
    private static final Comparator<DeferredImportSelectorHolder> DEFERRED_IMPORT_COMPARATOR = new Comparator<DeferredImportSelectorHolder>() { // from class: org.springframework.context.annotation.ConfigurationClassParser.1
        @Override // java.util.Comparator
        public int compare(DeferredImportSelectorHolder o1, DeferredImportSelectorHolder o2) {
            return AnnotationAwareOrderComparator.INSTANCE.compare(o1.getImportSelector(), o2.getImportSelector());
        }
    };
    private final MetadataReaderFactory metadataReaderFactory;
    private final ProblemReporter problemReporter;
    private final Environment environment;
    private final ResourceLoader resourceLoader;
    private final BeanDefinitionRegistry registry;
    private final ComponentScanAnnotationParser componentScanParser;
    private final ConditionEvaluator conditionEvaluator;
    private List<DeferredImportSelectorHolder> deferredImportSelectors;
    private final Log logger = LogFactory.getLog(getClass());
    private final Map<ConfigurationClass, ConfigurationClass> configurationClasses = new LinkedHashMap();
    private final Map<String, ConfigurationClass> knownSuperclasses = new HashMap();
    private final List<String> propertySourceNames = new ArrayList();
    private final ImportStack importStack = new ImportStack();

    public ConfigurationClassParser(MetadataReaderFactory metadataReaderFactory, ProblemReporter problemReporter, Environment environment, ResourceLoader resourceLoader, BeanNameGenerator componentScanBeanNameGenerator, BeanDefinitionRegistry registry) {
        this.metadataReaderFactory = metadataReaderFactory;
        this.problemReporter = problemReporter;
        this.environment = environment;
        this.resourceLoader = resourceLoader;
        this.registry = registry;
        this.componentScanParser = new ComponentScanAnnotationParser(environment, resourceLoader, componentScanBeanNameGenerator, registry);
        this.conditionEvaluator = new ConditionEvaluator(registry, environment, resourceLoader);
    }

    public void parse(Set<BeanDefinitionHolder> configCandidates) {
        this.deferredImportSelectors = new LinkedList();
        for (BeanDefinitionHolder holder : configCandidates) {
            BeanDefinition bd = holder.getBeanDefinition();
            try {
                if (bd instanceof AnnotatedBeanDefinition) {
                    parse(((AnnotatedBeanDefinition) bd).getMetadata(), holder.getBeanName());
                } else if ((bd instanceof AbstractBeanDefinition) && ((AbstractBeanDefinition) bd).hasBeanClass()) {
                    parse(((AbstractBeanDefinition) bd).getBeanClass(), holder.getBeanName());
                } else {
                    parse(bd.getBeanClassName(), holder.getBeanName());
                }
            } catch (BeanDefinitionStoreException ex) {
                throw ex;
            } catch (Throwable ex2) {
                throw new BeanDefinitionStoreException("Failed to parse configuration class [" + bd.getBeanClassName() + "]", ex2);
            }
        }
        processDeferredImportSelectors();
    }

    protected final void parse(String className, String beanName) throws IOException {
        MetadataReader reader = this.metadataReaderFactory.getMetadataReader(className);
        processConfigurationClass(new ConfigurationClass(reader, beanName));
    }

    protected final void parse(Class<?> clazz, String beanName) throws IOException {
        processConfigurationClass(new ConfigurationClass(clazz, beanName));
    }

    protected final void parse(AnnotationMetadata metadata, String beanName) throws IOException {
        processConfigurationClass(new ConfigurationClass(metadata, beanName));
    }

    public void validate() {
        for (ConfigurationClass configClass : this.configurationClasses.keySet()) {
            configClass.validate(this.problemReporter);
        }
    }

    public Set<ConfigurationClass> getConfigurationClasses() {
        return this.configurationClasses.keySet();
    }

    protected void processConfigurationClass(ConfigurationClass configClass) throws IOException {
        if (this.conditionEvaluator.shouldSkip(configClass.getMetadata(), ConfigurationCondition.ConfigurationPhase.PARSE_CONFIGURATION)) {
            return;
        }
        ConfigurationClass existingClass = this.configurationClasses.get(configClass);
        if (existingClass != null) {
            if (configClass.isImported()) {
                if (existingClass.isImported()) {
                    existingClass.mergeImportedBy(configClass);
                    return;
                }
                return;
            } else {
                this.configurationClasses.remove(configClass);
                Iterator<ConfigurationClass> it = this.knownSuperclasses.values().iterator();
                while (it.hasNext()) {
                    if (configClass.equals(it.next())) {
                        it.remove();
                    }
                }
            }
        }
        SourceClass sourceClass = asSourceClass(configClass);
        do {
            sourceClass = doProcessConfigurationClass(configClass, sourceClass);
        } while (sourceClass != null);
        this.configurationClasses.put(configClass, configClass);
    }

    protected final SourceClass doProcessConfigurationClass(ConfigurationClass configClass, SourceClass sourceClass) throws IOException {
        processMemberClasses(configClass, sourceClass);
        for (AnnotationAttributes propertySource : AnnotationConfigUtils.attributesForRepeatable(sourceClass.getMetadata(), (Class<?>) PropertySources.class, (Class<?>) PropertySource.class)) {
            if (this.environment instanceof ConfigurableEnvironment) {
                processPropertySource(propertySource);
            } else {
                this.logger.warn("Ignoring @PropertySource annotation on [" + sourceClass.getMetadata().getClassName() + "]. Reason: Environment must implement ConfigurableEnvironment");
            }
        }
        Set<AnnotationAttributes> componentScans = AnnotationConfigUtils.attributesForRepeatable(sourceClass.getMetadata(), (Class<?>) ComponentScans.class, (Class<?>) ComponentScan.class);
        if (!componentScans.isEmpty() && !this.conditionEvaluator.shouldSkip(sourceClass.getMetadata(), ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN)) {
            for (AnnotationAttributes componentScan : componentScans) {
                Set<BeanDefinitionHolder> scannedBeanDefinitions = this.componentScanParser.parse(componentScan, sourceClass.getMetadata().getClassName());
                for (BeanDefinitionHolder holder : scannedBeanDefinitions) {
                    BeanDefinition bdCand = holder.getBeanDefinition().getOriginatingBeanDefinition();
                    if (bdCand == null) {
                        bdCand = holder.getBeanDefinition();
                    }
                    if (ConfigurationClassUtils.checkConfigurationClassCandidate(bdCand, this.metadataReaderFactory)) {
                        parse(bdCand.getBeanClassName(), holder.getBeanName());
                    }
                }
            }
        }
        processImports(configClass, sourceClass, getImports(sourceClass), true);
        if (sourceClass.getMetadata().isAnnotated(ImportResource.class.getName())) {
            AnnotationAttributes importResource = AnnotationConfigUtils.attributesFor(sourceClass.getMetadata(), (Class<?>) ImportResource.class);
            String[] resources = importResource.getStringArray("locations");
            Class<? extends BeanDefinitionReader> readerClass = importResource.getClass("reader");
            for (String resource : resources) {
                String resolvedResource = this.environment.resolveRequiredPlaceholders(resource);
                configClass.addImportedResource(resolvedResource, readerClass);
            }
        }
        Set<MethodMetadata> beanMethods = retrieveBeanMethodMetadata(sourceClass);
        for (MethodMetadata methodMetadata : beanMethods) {
            configClass.addBeanMethod(new BeanMethod(methodMetadata, configClass));
        }
        processInterfaces(configClass, sourceClass);
        if (sourceClass.getMetadata().hasSuperClass()) {
            String superclass = sourceClass.getMetadata().getSuperClassName();
            if (!superclass.startsWith("java") && !this.knownSuperclasses.containsKey(superclass)) {
                this.knownSuperclasses.put(superclass, configClass);
                return sourceClass.getSuperClass();
            }
            return null;
        }
        return null;
    }

    private void processMemberClasses(ConfigurationClass configClass, SourceClass sourceClass) throws IOException {
        for (SourceClass memberClass : sourceClass.getMemberClasses()) {
            if (ConfigurationClassUtils.isConfigurationCandidate(memberClass.getMetadata()) && !memberClass.getMetadata().getClassName().equals(configClass.getMetadata().getClassName())) {
                if (this.importStack.contains(configClass)) {
                    this.problemReporter.error(new CircularImportProblem(configClass, this.importStack));
                } else {
                    this.importStack.push(configClass);
                    try {
                        processConfigurationClass(memberClass.asConfigClass(configClass));
                        this.importStack.pop();
                    } catch (Throwable th) {
                        this.importStack.pop();
                        throw th;
                    }
                }
            }
        }
    }

    private void processInterfaces(ConfigurationClass configClass, SourceClass sourceClass) throws IOException {
        for (SourceClass ifc : sourceClass.getInterfaces()) {
            Set<MethodMetadata> beanMethods = retrieveBeanMethodMetadata(ifc);
            for (MethodMetadata methodMetadata : beanMethods) {
                if (!methodMetadata.isAbstract()) {
                    configClass.addBeanMethod(new BeanMethod(methodMetadata, configClass));
                }
            }
            processInterfaces(configClass, ifc);
        }
    }

    private Set<MethodMetadata> retrieveBeanMethodMetadata(SourceClass sourceClass) {
        AnnotationMetadata original = sourceClass.getMetadata();
        Set<MethodMetadata> beanMethods = original.getAnnotatedMethods(Bean.class.getName());
        if (beanMethods.size() > 1 && (original instanceof StandardAnnotationMetadata)) {
            try {
                AnnotationMetadata asm = this.metadataReaderFactory.getMetadataReader(original.getClassName()).getAnnotationMetadata();
                Set<MethodMetadata> asmMethods = asm.getAnnotatedMethods(Bean.class.getName());
                if (asmMethods.size() >= beanMethods.size()) {
                    Set<MethodMetadata> selectedMethods = new LinkedHashSet<>(asmMethods.size());
                    for (MethodMetadata asmMethod : asmMethods) {
                        Iterator<MethodMetadata> it = beanMethods.iterator();
                        while (true) {
                            if (it.hasNext()) {
                                MethodMetadata beanMethod = it.next();
                                if (beanMethod.getMethodName().equals(asmMethod.getMethodName())) {
                                    selectedMethods.add(beanMethod);
                                    break;
                                }
                            }
                        }
                    }
                    if (selectedMethods.size() == beanMethods.size()) {
                        beanMethods = selectedMethods;
                    }
                }
            } catch (IOException ex) {
                this.logger.debug("Failed to read class file via ASM for determining @Bean method order", ex);
            }
        }
        return beanMethods;
    }

    private void processPropertySource(AnnotationAttributes propertySource) throws IOException {
        String name = propertySource.getString("name");
        if (!StringUtils.hasLength(name)) {
            name = null;
        }
        String encoding = propertySource.getString("encoding");
        if (!StringUtils.hasLength(encoding)) {
            encoding = null;
        }
        String[] locations = propertySource.getStringArray("value");
        Assert.isTrue(locations.length > 0, "At least one @PropertySource(value) location is required");
        boolean ignoreResourceNotFound = propertySource.getBoolean("ignoreResourceNotFound");
        Class<? extends PropertySourceFactory> factoryClass = propertySource.getClass(Constants.FACTORY);
        PropertySourceFactory factory = factoryClass == PropertySourceFactory.class ? DEFAULT_PROPERTY_SOURCE_FACTORY : (PropertySourceFactory) BeanUtils.instantiateClass(factoryClass);
        for (String location : locations) {
            try {
                String resolvedLocation = this.environment.resolveRequiredPlaceholders(location);
                Resource resource = this.resourceLoader.getResource(resolvedLocation);
                addPropertySource(factory.createPropertySource(name, new EncodedResource(resource, encoding)));
            } catch (IOException ex) {
                if (ignoreResourceNotFound && ((ex instanceof FileNotFoundException) || (ex instanceof UnknownHostException))) {
                    if (this.logger.isInfoEnabled()) {
                        this.logger.info("Properties location [" + location + "] not resolvable: " + ex.getMessage());
                    }
                } else {
                    throw ex;
                }
            } catch (IllegalArgumentException ex2) {
                if (ignoreResourceNotFound) {
                    if (this.logger.isInfoEnabled()) {
                        this.logger.info("Properties location [" + location + "] not resolvable: " + ex2.getMessage());
                    }
                } else {
                    throw ex2;
                }
            }
        }
    }

    private void addPropertySource(org.springframework.core.env.PropertySource<?> propertySource) {
        String name = propertySource.getName();
        MutablePropertySources propertySources = ((ConfigurableEnvironment) this.environment).getPropertySources();
        if (propertySources.contains(name) && this.propertySourceNames.contains(name)) {
            org.springframework.core.env.PropertySource<?> existing = propertySources.get(name);
            org.springframework.core.env.PropertySource<?> newSource = propertySource instanceof ResourcePropertySource ? ((ResourcePropertySource) propertySource).withResourceName() : propertySource;
            if (existing instanceof CompositePropertySource) {
                ((CompositePropertySource) existing).addFirstPropertySource(newSource);
            } else {
                if (existing instanceof ResourcePropertySource) {
                    existing = ((ResourcePropertySource) existing).withResourceName();
                }
                CompositePropertySource composite = new CompositePropertySource(name);
                composite.addPropertySource(newSource);
                composite.addPropertySource(existing);
                propertySources.replace(name, composite);
            }
        } else if (this.propertySourceNames.isEmpty()) {
            propertySources.addLast(propertySource);
        } else {
            String firstProcessed = this.propertySourceNames.get(this.propertySourceNames.size() - 1);
            propertySources.addBefore(firstProcessed, propertySource);
        }
        this.propertySourceNames.add(name);
    }

    private Set<SourceClass> getImports(SourceClass sourceClass) throws IOException {
        Set<SourceClass> imports = new LinkedHashSet<>();
        Set<SourceClass> visited = new LinkedHashSet<>();
        collectImports(sourceClass, imports, visited);
        return imports;
    }

    private void collectImports(SourceClass sourceClass, Set<SourceClass> imports, Set<SourceClass> visited) throws IOException {
        if (visited.add(sourceClass)) {
            for (SourceClass annotation : sourceClass.getAnnotations()) {
                String annName = annotation.getMetadata().getClassName();
                if (!annName.startsWith("java") && !annName.equals(Import.class.getName())) {
                    collectImports(annotation, imports, visited);
                }
            }
            imports.addAll(sourceClass.getAnnotationAttributes(Import.class.getName(), "value"));
        }
    }

    private void processDeferredImportSelectors() {
        List<DeferredImportSelectorHolder> deferredImports = this.deferredImportSelectors;
        this.deferredImportSelectors = null;
        Collections.sort(deferredImports, DEFERRED_IMPORT_COMPARATOR);
        for (DeferredImportSelectorHolder deferredImport : deferredImports) {
            ConfigurationClass configClass = deferredImport.getConfigurationClass();
            try {
                String[] imports = deferredImport.getImportSelector().selectImports(configClass.getMetadata());
                processImports(configClass, asSourceClass(configClass), asSourceClasses(imports), false);
            } catch (BeanDefinitionStoreException ex) {
                throw ex;
            } catch (Throwable ex2) {
                throw new BeanDefinitionStoreException("Failed to process import candidates for configuration class [" + configClass.getMetadata().getClassName() + "]", ex2);
            }
        }
    }

    private void processImports(ConfigurationClass configClass, SourceClass currentSourceClass, Collection<SourceClass> importCandidates, boolean checkForCircularImports) {
        if (importCandidates.isEmpty()) {
            return;
        }
        if (checkForCircularImports && isChainedImportOnStack(configClass)) {
            this.problemReporter.error(new CircularImportProblem(configClass, this.importStack));
            return;
        }
        this.importStack.push(configClass);
        try {
            try {
                for (SourceClass candidate : importCandidates) {
                    if (candidate.isAssignable(ImportSelector.class)) {
                        Class<?> candidateClass = candidate.loadClass();
                        ImportSelector selector = (ImportSelector) BeanUtils.instantiateClass(candidateClass, ImportSelector.class);
                        ParserStrategyUtils.invokeAwareMethods(selector, this.environment, this.resourceLoader, this.registry);
                        if (this.deferredImportSelectors != null && (selector instanceof DeferredImportSelector)) {
                            this.deferredImportSelectors.add(new DeferredImportSelectorHolder(configClass, (DeferredImportSelector) selector));
                        } else {
                            String[] importClassNames = selector.selectImports(currentSourceClass.getMetadata());
                            Collection<SourceClass> importSourceClasses = asSourceClasses(importClassNames);
                            processImports(configClass, currentSourceClass, importSourceClasses, false);
                        }
                    } else if (candidate.isAssignable(ImportBeanDefinitionRegistrar.class)) {
                        Class<?> candidateClass2 = candidate.loadClass();
                        ImportBeanDefinitionRegistrar registrar = (ImportBeanDefinitionRegistrar) BeanUtils.instantiateClass(candidateClass2, ImportBeanDefinitionRegistrar.class);
                        ParserStrategyUtils.invokeAwareMethods(registrar, this.environment, this.resourceLoader, this.registry);
                        configClass.addImportBeanDefinitionRegistrar(registrar, currentSourceClass.getMetadata());
                    } else {
                        this.importStack.registerImport(currentSourceClass.getMetadata(), candidate.getMetadata().getClassName());
                        processConfigurationClass(candidate.asConfigClass(configClass));
                    }
                }
            } catch (BeanDefinitionStoreException ex) {
                throw ex;
            } catch (Throwable ex2) {
                throw new BeanDefinitionStoreException("Failed to process import candidates for configuration class [" + configClass.getMetadata().getClassName() + "]", ex2);
            }
        } finally {
            this.importStack.pop();
        }
    }

    private boolean isChainedImportOnStack(ConfigurationClass configClass) {
        if (this.importStack.contains(configClass)) {
            String configClassName = configClass.getMetadata().getClassName();
            AnnotationMetadata importingClassFor = this.importStack.getImportingClassFor(configClassName);
            while (true) {
                AnnotationMetadata importingClass = importingClassFor;
                if (importingClass != null) {
                    if (configClassName.equals(importingClass.getClassName())) {
                        return true;
                    }
                    importingClassFor = this.importStack.getImportingClassFor(importingClass.getClassName());
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    ImportRegistry getImportRegistry() {
        return this.importStack;
    }

    private SourceClass asSourceClass(ConfigurationClass configurationClass) throws IOException {
        AnnotationMetadata metadata = configurationClass.getMetadata();
        if (metadata instanceof StandardAnnotationMetadata) {
            return asSourceClass(((StandardAnnotationMetadata) metadata).getIntrospectedClass());
        }
        return asSourceClass(metadata.getClassName());
    }

    SourceClass asSourceClass(Class<?> classType) throws IOException {
        try {
            for (Annotation ann : classType.getAnnotations()) {
                AnnotationUtils.validateAnnotation(ann);
            }
            return new SourceClass(classType);
        } catch (Throwable th) {
            return asSourceClass(classType.getName());
        }
    }

    private Collection<SourceClass> asSourceClasses(String[] classNames) throws IOException {
        List<SourceClass> annotatedClasses = new ArrayList<>(classNames.length);
        for (String className : classNames) {
            annotatedClasses.add(asSourceClass(className));
        }
        return annotatedClasses;
    }

    SourceClass asSourceClass(String className) throws IOException {
        if (className.startsWith("java")) {
            try {
                return new SourceClass(this.resourceLoader.getClassLoader().loadClass(className));
            } catch (ClassNotFoundException ex) {
                throw new NestedIOException("Failed to load class [" + className + "]", ex);
            }
        }
        return new SourceClass(this.metadataReaderFactory.getMetadataReader(className));
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/ConfigurationClassParser$ImportStack.class */
    private static class ImportStack extends ArrayDeque<ConfigurationClass> implements ImportRegistry {
        private final MultiValueMap<String, AnnotationMetadata> imports;

        private ImportStack() {
            this.imports = new LinkedMultiValueMap();
        }

        public void registerImport(AnnotationMetadata importingClass, String importedClass) {
            this.imports.add(importedClass, importingClass);
        }

        @Override // org.springframework.context.annotation.ImportRegistry
        public AnnotationMetadata getImportingClassFor(String importedClass) {
            List<AnnotationMetadata> list = (List) this.imports.get(importedClass);
            if (CollectionUtils.isEmpty(list)) {
                return null;
            }
            return list.get(list.size() - 1);
        }

        @Override // org.springframework.context.annotation.ImportRegistry
        public void removeImportingClass(String importingClass) {
            Iterator<AnnotationMetadata> it = this.imports.values().iterator();
            while (it.hasNext()) {
                List<AnnotationMetadata> list = (List) it.next();
                Iterator<AnnotationMetadata> iterator = list.iterator();
                while (true) {
                    if (!iterator.hasNext()) {
                        break;
                    } else if (iterator.next().getClassName().equals(importingClass)) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }

        @Override // java.util.AbstractCollection
        public String toString() {
            StringBuilder builder = new StringBuilder(PropertyAccessor.PROPERTY_KEY_PREFIX);
            Iterator<ConfigurationClass> iterator = iterator();
            while (iterator.hasNext()) {
                builder.append(iterator.next().getSimpleName());
                if (iterator.hasNext()) {
                    builder.append("->");
                }
            }
            return builder.append(']').toString();
        }
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/ConfigurationClassParser$DeferredImportSelectorHolder.class */
    private static class DeferredImportSelectorHolder {
        private final ConfigurationClass configurationClass;
        private final DeferredImportSelector importSelector;

        public DeferredImportSelectorHolder(ConfigurationClass configClass, DeferredImportSelector selector) {
            this.configurationClass = configClass;
            this.importSelector = selector;
        }

        public ConfigurationClass getConfigurationClass() {
            return this.configurationClass;
        }

        public DeferredImportSelector getImportSelector() {
            return this.importSelector;
        }
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/ConfigurationClassParser$SourceClass.class */
    private class SourceClass {
        private final Object source;
        private final AnnotationMetadata metadata;

        public SourceClass(Object source) {
            this.source = source;
            if (source instanceof Class) {
                this.metadata = new StandardAnnotationMetadata((Class) source, true);
            } else {
                this.metadata = ((MetadataReader) source).getAnnotationMetadata();
            }
        }

        public final AnnotationMetadata getMetadata() {
            return this.metadata;
        }

        public Class<?> loadClass() throws ClassNotFoundException {
            if (this.source instanceof Class) {
                return (Class) this.source;
            }
            String className = ((MetadataReader) this.source).getClassMetadata().getClassName();
            return ConfigurationClassParser.this.resourceLoader.getClassLoader().loadClass(className);
        }

        public boolean isAssignable(Class<?> clazz) throws IOException {
            if (!(this.source instanceof Class)) {
                return new AssignableTypeFilter(clazz).match((MetadataReader) this.source, ConfigurationClassParser.this.metadataReaderFactory);
            }
            return clazz.isAssignableFrom((Class) this.source);
        }

        public ConfigurationClass asConfigClass(ConfigurationClass importedBy) {
            if (this.source instanceof Class) {
                return new ConfigurationClass((Class<?>) this.source, importedBy);
            }
            return new ConfigurationClass((MetadataReader) this.source, importedBy);
        }

        public Collection<SourceClass> getMemberClasses() throws IOException {
            Object sourceToProcess = this.source;
            if (sourceToProcess instanceof Class) {
                Class<?> sourceClass = (Class) sourceToProcess;
                try {
                    Class<?>[] declaredClasses = sourceClass.getDeclaredClasses();
                    List<SourceClass> members = new ArrayList<>(declaredClasses.length);
                    for (Class<?> declaredClass : declaredClasses) {
                        members.add(ConfigurationClassParser.this.asSourceClass(declaredClass));
                    }
                    return members;
                } catch (NoClassDefFoundError e) {
                    sourceToProcess = ConfigurationClassParser.this.metadataReaderFactory.getMetadataReader(sourceClass.getName());
                }
            }
            MetadataReader sourceReader = (MetadataReader) sourceToProcess;
            String[] memberClassNames = sourceReader.getClassMetadata().getMemberClassNames();
            List<SourceClass> members2 = new ArrayList<>(memberClassNames.length);
            for (String memberClassName : memberClassNames) {
                try {
                    members2.add(ConfigurationClassParser.this.asSourceClass(memberClassName));
                } catch (IOException e2) {
                    if (ConfigurationClassParser.this.logger.isDebugEnabled()) {
                        ConfigurationClassParser.this.logger.debug("Failed to resolve member class [" + memberClassName + "] - not considering it as a configuration class candidate");
                    }
                }
            }
            return members2;
        }

        public SourceClass getSuperClass() throws IOException {
            if (this.source instanceof Class) {
                return ConfigurationClassParser.this.asSourceClass(((Class) this.source).getSuperclass());
            }
            return ConfigurationClassParser.this.asSourceClass(((MetadataReader) this.source).getClassMetadata().getSuperClassName());
        }

        public Set<SourceClass> getInterfaces() throws IOException {
            Set<SourceClass> result = new LinkedHashSet<>();
            if (this.source instanceof Class) {
                Class<?> sourceClass = (Class) this.source;
                for (Class<?> ifcClass : sourceClass.getInterfaces()) {
                    result.add(ConfigurationClassParser.this.asSourceClass(ifcClass));
                }
            } else {
                for (String className : this.metadata.getInterfaceNames()) {
                    result.add(ConfigurationClassParser.this.asSourceClass(className));
                }
            }
            return result;
        }

        public Set<SourceClass> getAnnotations() {
            Set<SourceClass> result = new LinkedHashSet<>();
            for (String className : this.metadata.getAnnotationTypes()) {
                try {
                    result.add(getRelated(className));
                } catch (Throwable th) {
                }
            }
            return result;
        }

        public Collection<SourceClass> getAnnotationAttributes(String annType, String attribute) throws IOException {
            Map<String, Object> annotationAttributes = this.metadata.getAnnotationAttributes(annType, true);
            if (annotationAttributes == null || !annotationAttributes.containsKey(attribute)) {
                return Collections.emptySet();
            }
            String[] classNames = (String[]) annotationAttributes.get(attribute);
            Set<SourceClass> result = new LinkedHashSet<>();
            for (String className : classNames) {
                result.add(getRelated(className));
            }
            return result;
        }

        private SourceClass getRelated(String className) throws ClassNotFoundException, IOException {
            if (this.source instanceof Class) {
                try {
                    Class<?> clazz = ((Class) this.source).getClassLoader().loadClass(className);
                    return ConfigurationClassParser.this.asSourceClass(clazz);
                } catch (ClassNotFoundException ex) {
                    if (className.startsWith("java")) {
                        throw new NestedIOException("Failed to load class [" + className + "]", ex);
                    }
                    return ConfigurationClassParser.this.new SourceClass(ConfigurationClassParser.this.metadataReaderFactory.getMetadataReader(className));
                }
            }
            return ConfigurationClassParser.this.asSourceClass(className);
        }

        public boolean equals(Object other) {
            return this == other || ((other instanceof SourceClass) && this.metadata.getClassName().equals(((SourceClass) other).metadata.getClassName()));
        }

        public int hashCode() {
            return this.metadata.getClassName().hashCode();
        }

        public String toString() {
            return this.metadata.getClassName();
        }
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/ConfigurationClassParser$CircularImportProblem.class */
    private static class CircularImportProblem extends Problem {
        public CircularImportProblem(ConfigurationClass attemptedImport, Deque<ConfigurationClass> importStack) {
            super(String.format("A circular @Import has been detected: Illegal attempt by @Configuration class '%s' to import class '%s' as '%s' is already present in the current import stack %s", importStack.element().getSimpleName(), attemptedImport.getSimpleName(), attemptedImport.getSimpleName(), importStack), new Location(importStack.element().getResource(), attemptedImport.getMetadata()));
        }
    }
}
