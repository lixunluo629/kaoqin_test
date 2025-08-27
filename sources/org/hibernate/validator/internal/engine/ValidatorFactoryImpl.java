package org.hibernate.validator.internal.engine;

import java.lang.annotation.Annotation;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.TraversableResolver;
import javax.validation.Validator;
import javax.validation.spi.ConfigurationState;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.HibernateValidatorContext;
import org.hibernate.validator.HibernateValidatorFactory;
import org.hibernate.validator.cfg.ConstraintMapping;
import org.hibernate.validator.internal.cfg.context.DefaultConstraintMapping;
import org.hibernate.validator.internal.engine.constraintdefinition.ConstraintDefinitionContribution;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorManager;
import org.hibernate.validator.internal.engine.time.DefaultTimeProvider;
import org.hibernate.validator.internal.metadata.BeanMetaDataManager;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.provider.MetaDataProvider;
import org.hibernate.validator.internal.metadata.provider.ProgrammaticMetaDataProvider;
import org.hibernate.validator.internal.metadata.provider.XmlMetaDataProvider;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.ExecutableHelper;
import org.hibernate.validator.internal.util.StringHelper;
import org.hibernate.validator.internal.util.TypeResolutionHelper;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.internal.util.privilegedactions.LoadClass;
import org.hibernate.validator.internal.util.privilegedactions.NewInstance;
import org.hibernate.validator.spi.cfg.ConstraintMappingContributor;
import org.hibernate.validator.spi.time.TimeProvider;
import org.hibernate.validator.spi.valuehandling.ValidatedValueUnwrapper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/ValidatorFactoryImpl.class */
public class ValidatorFactoryImpl implements HibernateValidatorFactory {
    private static final Log log = LoggerFactory.make();
    private final MessageInterpolator messageInterpolator;
    private final TraversableResolver traversableResolver;
    private final ParameterNameProvider parameterNameProvider;
    private final TimeProvider timeProvider;
    private final ConstraintValidatorManager constraintValidatorManager;
    private final Set<DefaultConstraintMapping> constraintMappings;
    private final ConstraintHelper constraintHelper;
    private final TypeResolutionHelper typeResolutionHelper;
    private final ExecutableHelper executableHelper;
    private final boolean failFast;
    private final MethodValidationConfiguration methodValidationConfiguration;
    private XmlMetaDataProvider xmlMetaDataProvider;
    private final Map<ParameterNameProvider, BeanMetaDataManager> beanMetaDataManagerMap;
    private final List<ValidatedValueUnwrapper<?>> validatedValueHandlers;

    public ValidatorFactoryImpl(ConfigurationState configurationState) {
        ClassLoader externalClassLoader = getExternalClassLoader(configurationState);
        this.messageInterpolator = configurationState.getMessageInterpolator();
        this.traversableResolver = configurationState.getTraversableResolver();
        this.parameterNameProvider = configurationState.getParameterNameProvider();
        this.timeProvider = getTimeProvider(configurationState, externalClassLoader);
        this.beanMetaDataManagerMap = Collections.synchronizedMap(new IdentityHashMap());
        this.constraintHelper = new ConstraintHelper();
        this.typeResolutionHelper = new TypeResolutionHelper();
        this.executableHelper = new ExecutableHelper(this.typeResolutionHelper);
        if (configurationState.getMappingStreams().isEmpty()) {
            this.xmlMetaDataProvider = null;
        } else {
            this.xmlMetaDataProvider = new XmlMetaDataProvider(this.constraintHelper, this.parameterNameProvider, configurationState.getMappingStreams(), externalClassLoader);
        }
        this.constraintMappings = Collections.unmodifiableSet(getConstraintMappings(configurationState, externalClassLoader));
        Map<String, String> properties = configurationState.getProperties();
        boolean tmpFailFast = false;
        boolean tmpAllowOverridingMethodAlterParameterConstraint = false;
        boolean tmpAllowMultipleCascadedValidationOnReturnValues = false;
        boolean tmpAllowParallelMethodsDefineParameterConstraints = false;
        List<ValidatedValueUnwrapper<?>> tmpValidatedValueHandlers = CollectionHelper.newArrayList(5);
        if (configurationState instanceof ConfigurationImpl) {
            ConfigurationImpl hibernateSpecificConfig = (ConfigurationImpl) configurationState;
            tmpFailFast = hibernateSpecificConfig.getFailFast();
            tmpAllowOverridingMethodAlterParameterConstraint = hibernateSpecificConfig.getMethodValidationConfiguration().isAllowOverridingMethodAlterParameterConstraint();
            tmpAllowMultipleCascadedValidationOnReturnValues = hibernateSpecificConfig.getMethodValidationConfiguration().isAllowMultipleCascadedValidationOnReturnValues();
            tmpAllowParallelMethodsDefineParameterConstraints = hibernateSpecificConfig.getMethodValidationConfiguration().isAllowParallelMethodsDefineParameterConstraints();
            tmpValidatedValueHandlers.addAll(hibernateSpecificConfig.getValidatedValueHandlers());
        }
        registerCustomConstraintValidators(this.constraintMappings, this.constraintHelper);
        tmpValidatedValueHandlers.addAll(getPropertyConfiguredValidatedValueHandlers(properties, externalClassLoader));
        this.validatedValueHandlers = Collections.unmodifiableList(tmpValidatedValueHandlers);
        this.failFast = checkPropertiesForBoolean(properties, HibernateValidatorConfiguration.FAIL_FAST, tmpFailFast);
        this.methodValidationConfiguration = new MethodValidationConfiguration();
        this.methodValidationConfiguration.allowOverridingMethodAlterParameterConstraint(checkPropertiesForBoolean(properties, HibernateValidatorConfiguration.ALLOW_PARAMETER_CONSTRAINT_OVERRIDE, tmpAllowOverridingMethodAlterParameterConstraint));
        this.methodValidationConfiguration.allowMultipleCascadedValidationOnReturnValues(checkPropertiesForBoolean(properties, HibernateValidatorConfiguration.ALLOW_MULTIPLE_CASCADED_VALIDATION_ON_RESULT, tmpAllowMultipleCascadedValidationOnReturnValues));
        this.methodValidationConfiguration.allowParallelMethodsDefineParameterConstraints(checkPropertiesForBoolean(properties, HibernateValidatorConfiguration.ALLOW_PARALLEL_METHODS_DEFINE_PARAMETER_CONSTRAINTS, tmpAllowParallelMethodsDefineParameterConstraints));
        this.constraintValidatorManager = new ConstraintValidatorManager(configurationState.getConstraintValidatorFactory());
    }

    private static ClassLoader getExternalClassLoader(ConfigurationState configurationState) {
        if (configurationState instanceof ConfigurationImpl) {
            return ((ConfigurationImpl) configurationState).getExternalClassLoader();
        }
        return null;
    }

    private static Set<DefaultConstraintMapping> getConstraintMappings(ConfigurationState configurationState, ClassLoader externalClassLoader) {
        Set<DefaultConstraintMapping> constraintMappings = CollectionHelper.newHashSet();
        if (configurationState instanceof ConfigurationImpl) {
            ConfigurationImpl hibernateConfiguration = (ConfigurationImpl) configurationState;
            constraintMappings.addAll(hibernateConfiguration.getProgrammaticMappings());
            ConstraintMappingContributor serviceLoaderBasedContributor = hibernateConfiguration.getServiceLoaderBasedConstraintMappingContributor();
            DefaultConstraintMappingBuilder builder = new DefaultConstraintMappingBuilder(constraintMappings);
            serviceLoaderBasedContributor.createConstraintMappings(builder);
        }
        List<ConstraintMappingContributor> contributors = getPropertyConfiguredConstraintMappingContributors(configurationState.getProperties(), externalClassLoader);
        for (ConstraintMappingContributor contributor : contributors) {
            DefaultConstraintMappingBuilder builder2 = new DefaultConstraintMappingBuilder(constraintMappings);
            contributor.createConstraintMappings(builder2);
        }
        return constraintMappings;
    }

    private static TimeProvider getTimeProvider(ConfigurationState configurationState, ClassLoader externalClassLoader) {
        String timeProviderClassName;
        TimeProvider timeProvider = null;
        if (configurationState instanceof ConfigurationImpl) {
            ConfigurationImpl hvConfig = (ConfigurationImpl) configurationState;
            timeProvider = hvConfig.getTimeProvider();
        }
        if (timeProvider == null && (timeProviderClassName = configurationState.getProperties().get(HibernateValidatorConfiguration.TIME_PROVIDER)) != null) {
            Class<? extends TimeProvider> handlerType = (Class) run(LoadClass.action(timeProviderClassName, externalClassLoader));
            timeProvider = (TimeProvider) run(NewInstance.action(handlerType, "time provider class"));
        }
        return timeProvider != null ? timeProvider : DefaultTimeProvider.getInstance();
    }

    @Override // javax.validation.ValidatorFactory
    public Validator getValidator() {
        return createValidator(this.constraintValidatorManager.getDefaultConstraintValidatorFactory(), this.messageInterpolator, this.traversableResolver, this.parameterNameProvider, this.failFast, this.validatedValueHandlers, this.timeProvider, this.methodValidationConfiguration);
    }

    @Override // javax.validation.ValidatorFactory
    public MessageInterpolator getMessageInterpolator() {
        return this.messageInterpolator;
    }

    @Override // javax.validation.ValidatorFactory
    public TraversableResolver getTraversableResolver() {
        return this.traversableResolver;
    }

    @Override // javax.validation.ValidatorFactory
    public ConstraintValidatorFactory getConstraintValidatorFactory() {
        return this.constraintValidatorManager.getDefaultConstraintValidatorFactory();
    }

    @Override // javax.validation.ValidatorFactory
    public ParameterNameProvider getParameterNameProvider() {
        return this.parameterNameProvider;
    }

    public boolean isFailFast() {
        return this.failFast;
    }

    public List<ValidatedValueUnwrapper<?>> getValidatedValueHandlers() {
        return this.validatedValueHandlers;
    }

    TimeProvider getTimeProvider() {
        return this.timeProvider;
    }

    @Override // javax.validation.ValidatorFactory
    public <T> T unwrap(Class<T> type) {
        if (type.isAssignableFrom(HibernateValidatorFactory.class)) {
            return type.cast(this);
        }
        throw log.getTypeNotSupportedForUnwrappingException(type);
    }

    @Override // javax.validation.ValidatorFactory
    public HibernateValidatorContext usingContext() {
        return new ValidatorContextImpl(this);
    }

    @Override // javax.validation.ValidatorFactory
    public void close() {
        this.constraintValidatorManager.clear();
        for (BeanMetaDataManager beanMetaDataManager : this.beanMetaDataManagerMap.values()) {
            beanMetaDataManager.clear();
        }
        this.xmlMetaDataProvider = null;
    }

    Validator createValidator(ConstraintValidatorFactory constraintValidatorFactory, MessageInterpolator messageInterpolator, TraversableResolver traversableResolver, ParameterNameProvider parameterNameProvider, boolean failFast, List<ValidatedValueUnwrapper<?>> validatedValueHandlers, TimeProvider timeProvider, MethodValidationConfiguration methodValidationConfiguration) {
        BeanMetaDataManager beanMetaDataManager;
        if (!this.beanMetaDataManagerMap.containsKey(parameterNameProvider)) {
            beanMetaDataManager = new BeanMetaDataManager(this.constraintHelper, this.executableHelper, parameterNameProvider, buildDataProviders(parameterNameProvider), methodValidationConfiguration);
            this.beanMetaDataManagerMap.put(parameterNameProvider, beanMetaDataManager);
        } else {
            beanMetaDataManager = this.beanMetaDataManagerMap.get(parameterNameProvider);
        }
        return new ValidatorImpl(constraintValidatorFactory, messageInterpolator, traversableResolver, beanMetaDataManager, parameterNameProvider, timeProvider, this.typeResolutionHelper, validatedValueHandlers, this.constraintValidatorManager, failFast);
    }

    private List<MetaDataProvider> buildDataProviders(ParameterNameProvider parameterNameProvider) {
        List<MetaDataProvider> metaDataProviders = CollectionHelper.newArrayList();
        if (this.xmlMetaDataProvider != null) {
            metaDataProviders.add(this.xmlMetaDataProvider);
        }
        if (!this.constraintMappings.isEmpty()) {
            metaDataProviders.add(new ProgrammaticMetaDataProvider(this.constraintHelper, parameterNameProvider, this.constraintMappings));
        }
        return metaDataProviders;
    }

    private boolean checkPropertiesForBoolean(Map<String, String> properties, String propertyKey, boolean programmaticValue) {
        boolean value = programmaticValue;
        String propertyStringValue = properties.get(propertyKey);
        if (propertyStringValue != null) {
            boolean configurationValue = Boolean.valueOf(propertyStringValue).booleanValue();
            if (programmaticValue && !configurationValue) {
                throw log.getInconsistentFailFastConfigurationException();
            }
            value = configurationValue;
        }
        return value;
    }

    private static List<ValidatedValueUnwrapper<?>> getPropertyConfiguredValidatedValueHandlers(Map<String, String> properties, ClassLoader externalClassLoader) {
        String propertyValue = properties.get(HibernateValidatorConfiguration.VALIDATED_VALUE_HANDLERS);
        if (propertyValue == null || propertyValue.isEmpty()) {
            return Collections.emptyList();
        }
        String[] handlerNames = propertyValue.split(",");
        ArrayList arrayListNewArrayList = CollectionHelper.newArrayList(handlerNames.length);
        for (String handlerName : handlerNames) {
            Class<? extends ValidatedValueUnwrapper<?>> handlerType = (Class) run(LoadClass.action(handlerName, externalClassLoader));
            arrayListNewArrayList.add(run(NewInstance.action(handlerType, "validated value handler class")));
        }
        return arrayListNewArrayList;
    }

    private static List<ConstraintMappingContributor> getPropertyConfiguredConstraintMappingContributors(Map<String, String> properties, ClassLoader externalClassLoader) {
        String deprecatedPropertyValue = properties.get(HibernateValidatorConfiguration.CONSTRAINT_MAPPING_CONTRIBUTOR);
        String propertyValue = properties.get(HibernateValidatorConfiguration.CONSTRAINT_MAPPING_CONTRIBUTORS);
        if (StringHelper.isNullOrEmptyString(deprecatedPropertyValue) && StringHelper.isNullOrEmptyString(propertyValue)) {
            return Collections.emptyList();
        }
        StringBuilder assembledPropertyValue = new StringBuilder();
        if (!StringHelper.isNullOrEmptyString(deprecatedPropertyValue)) {
            assembledPropertyValue.append(deprecatedPropertyValue);
        }
        if (!StringHelper.isNullOrEmptyString(propertyValue)) {
            if (assembledPropertyValue.length() > 0) {
                assembledPropertyValue.append(",");
            }
            assembledPropertyValue.append(propertyValue);
        }
        String[] contributorNames = assembledPropertyValue.toString().split(",");
        ArrayList arrayListNewArrayList = CollectionHelper.newArrayList(contributorNames.length);
        for (String contributorName : contributorNames) {
            Class<? extends ConstraintMappingContributor> contributorType = (Class) run(LoadClass.action(contributorName, externalClassLoader));
            arrayListNewArrayList.add(run(NewInstance.action(contributorType, "constraint mapping contributor class")));
        }
        return arrayListNewArrayList;
    }

    private static void registerCustomConstraintValidators(Set<DefaultConstraintMapping> constraintMappings, ConstraintHelper constraintHelper) {
        Set<Class<?>> definedConstraints = CollectionHelper.newHashSet();
        for (DefaultConstraintMapping constraintMapping : constraintMappings) {
            for (ConstraintDefinitionContribution<?> contribution : constraintMapping.getConstraintDefinitionContributions()) {
                processConstraintDefinitionContribution(contribution, constraintHelper, definedConstraints);
            }
        }
    }

    private static <A extends Annotation> void processConstraintDefinitionContribution(ConstraintDefinitionContribution<A> constraintDefinitionContribution, ConstraintHelper constraintHelper, Set<Class<?>> definedConstraints) {
        Class<A> constraintType = constraintDefinitionContribution.getConstraintType();
        if (definedConstraints.contains(constraintType)) {
            throw log.getConstraintHasAlreadyBeenConfiguredViaProgrammaticApiException(constraintType.getName());
        }
        definedConstraints.add(constraintType);
        constraintHelper.putValidatorClasses(constraintType, constraintDefinitionContribution.getConstraintValidators(), constraintDefinitionContribution.includeExisting());
    }

    private static <T> T run(PrivilegedAction<T> privilegedAction) {
        return System.getSecurityManager() != null ? (T) AccessController.doPrivileged(privilegedAction) : privilegedAction.run();
    }

    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/ValidatorFactoryImpl$DefaultConstraintMappingBuilder.class */
    private static class DefaultConstraintMappingBuilder implements ConstraintMappingContributor.ConstraintMappingBuilder {
        private final Set<DefaultConstraintMapping> mappings;

        public DefaultConstraintMappingBuilder(Set<DefaultConstraintMapping> mappings) {
            this.mappings = mappings;
        }

        @Override // org.hibernate.validator.spi.cfg.ConstraintMappingContributor.ConstraintMappingBuilder
        public ConstraintMapping addConstraintMapping() {
            DefaultConstraintMapping mapping = new DefaultConstraintMapping();
            this.mappings.add(mapping);
            return mapping;
        }
    }
}
