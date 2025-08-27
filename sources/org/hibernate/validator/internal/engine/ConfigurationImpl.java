package org.hibernate.validator.internal.engine;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.BootstrapConfiguration;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.TraversableResolver;
import javax.validation.ValidationException;
import javax.validation.ValidationProviderResolver;
import javax.validation.ValidatorFactory;
import javax.validation.spi.BootstrapState;
import javax.validation.spi.ConfigurationState;
import javax.validation.spi.ValidationProvider;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.cfg.ConstraintMapping;
import org.hibernate.validator.internal.cfg.context.DefaultConstraintMapping;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorFactoryImpl;
import org.hibernate.validator.internal.engine.resolver.DefaultTraversableResolver;
import org.hibernate.validator.internal.engine.valuehandling.OptionalValueUnwrapper;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.Contracts;
import org.hibernate.validator.internal.util.TypeResolutionHelper;
import org.hibernate.validator.internal.util.Version;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.internal.util.logging.Messages;
import org.hibernate.validator.internal.util.privilegedactions.GetClassLoader;
import org.hibernate.validator.internal.util.privilegedactions.LoadClass;
import org.hibernate.validator.internal.util.privilegedactions.SetContextClassLoader;
import org.hibernate.validator.internal.xml.ValidationBootstrapParameters;
import org.hibernate.validator.internal.xml.ValidationXmlParser;
import org.hibernate.validator.messageinterpolation.AbstractMessageInterpolator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.hibernate.validator.spi.cfg.ConstraintMappingContributor;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;
import org.hibernate.validator.spi.time.TimeProvider;
import org.hibernate.validator.spi.valuehandling.ValidatedValueUnwrapper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/ConfigurationImpl.class */
public class ConfigurationImpl implements HibernateValidatorConfiguration, ConfigurationState {
    private static final String JFX_UNWRAPPER_CLASS = "org.hibernate.validator.internal.engine.valuehandling.JavaFXPropertyValueUnwrapper";
    private static final Log log;
    private final ResourceBundleLocator defaultResourceBundleLocator;
    private MessageInterpolator defaultMessageInterpolator;
    private MessageInterpolator messageInterpolator;
    private final TraversableResolver defaultTraversableResolver;
    private final ConstraintValidatorFactory defaultConstraintValidatorFactory;
    private final ParameterNameProvider defaultParameterNameProvider;
    private final ConstraintMappingContributor serviceLoaderBasedConstraintMappingContributor;
    private ValidationProviderResolver providerResolver;
    private final ValidationBootstrapParameters validationBootstrapParameters;
    private boolean ignoreXmlConfiguration;
    private final Set<InputStream> configurationStreams;
    private BootstrapConfiguration bootstrapConfiguration;
    private final Set<DefaultConstraintMapping> programmaticMappings;
    private boolean failFast;
    private final List<ValidatedValueUnwrapper<?>> validatedValueHandlers;
    private ClassLoader externalClassLoader;
    private TimeProvider timeProvider;
    private final MethodValidationConfiguration methodValidationConfiguration;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ConfigurationImpl.class.desiredAssertionStatus();
        Version.touch();
        log = LoggerFactory.make();
    }

    public ConfigurationImpl(BootstrapState state) {
        this();
        if (state.getValidationProviderResolver() == null) {
            this.providerResolver = state.getDefaultValidationProviderResolver();
        } else {
            this.providerResolver = state.getValidationProviderResolver();
        }
    }

    public ConfigurationImpl(ValidationProvider<?> provider) {
        this();
        if (provider == null) {
            throw log.getInconsistentConfigurationException();
        }
        this.providerResolver = null;
        this.validationBootstrapParameters.setProvider(provider);
    }

    private ConfigurationImpl() {
        this.ignoreXmlConfiguration = false;
        this.configurationStreams = CollectionHelper.newHashSet();
        this.programmaticMappings = CollectionHelper.newHashSet();
        this.validatedValueHandlers = CollectionHelper.newArrayList();
        this.methodValidationConfiguration = new MethodValidationConfiguration();
        this.validationBootstrapParameters = new ValidationBootstrapParameters();
        TypeResolutionHelper typeResolutionHelper = new TypeResolutionHelper();
        if (isJavaFxInClasspath()) {
            this.validatedValueHandlers.add(createJavaFXUnwrapperClass(typeResolutionHelper));
        }
        if (Version.getJavaRelease() >= 8) {
            this.validatedValueHandlers.add(new OptionalValueUnwrapper(typeResolutionHelper));
        }
        this.defaultResourceBundleLocator = new PlatformResourceBundleLocator(AbstractMessageInterpolator.USER_VALIDATION_MESSAGES);
        this.defaultTraversableResolver = new DefaultTraversableResolver();
        this.defaultConstraintValidatorFactory = new ConstraintValidatorFactoryImpl();
        this.defaultParameterNameProvider = new DefaultParameterNameProvider();
        this.serviceLoaderBasedConstraintMappingContributor = new ServiceLoaderBasedConstraintMappingContributor(typeResolutionHelper);
    }

    private ValidatedValueUnwrapper<?> createJavaFXUnwrapperClass(TypeResolutionHelper typeResolutionHelper) {
        try {
            Class<?> jfxUnwrapperClass = (Class) run(LoadClass.action(JFX_UNWRAPPER_CLASS, getClass().getClassLoader()));
            return (ValidatedValueUnwrapper) jfxUnwrapperClass.getConstructor(TypeResolutionHelper.class).newInstance(typeResolutionHelper);
        } catch (Exception e) {
            throw log.validatedValueUnwrapperCannotBeCreated(JFX_UNWRAPPER_CLASS, e);
        }
    }

    @Override // javax.validation.Configuration
    public final HibernateValidatorConfiguration ignoreXmlConfiguration() {
        this.ignoreXmlConfiguration = true;
        return this;
    }

    @Override // javax.validation.Configuration
    public final ConfigurationImpl messageInterpolator(MessageInterpolator interpolator) {
        if (log.isDebugEnabled() && interpolator != null) {
            log.debug("Setting custom MessageInterpolator of type " + interpolator.getClass().getName());
        }
        this.validationBootstrapParameters.setMessageInterpolator(interpolator);
        return this;
    }

    @Override // javax.validation.Configuration
    public final ConfigurationImpl traversableResolver(TraversableResolver resolver) {
        if (log.isDebugEnabled() && resolver != null) {
            log.debug("Setting custom TraversableResolver of type " + resolver.getClass().getName());
        }
        this.validationBootstrapParameters.setTraversableResolver(resolver);
        return this;
    }

    @Override // javax.validation.Configuration
    public final ConfigurationImpl constraintValidatorFactory(ConstraintValidatorFactory constraintValidatorFactory) {
        if (log.isDebugEnabled() && constraintValidatorFactory != null) {
            log.debug("Setting custom ConstraintValidatorFactory of type " + constraintValidatorFactory.getClass().getName());
        }
        this.validationBootstrapParameters.setConstraintValidatorFactory(constraintValidatorFactory);
        return this;
    }

    @Override // javax.validation.Configuration
    public HibernateValidatorConfiguration parameterNameProvider(ParameterNameProvider parameterNameProvider) {
        if (log.isDebugEnabled() && parameterNameProvider != null) {
            log.debug("Setting custom ParameterNameProvider of type " + parameterNameProvider.getClass().getName());
        }
        this.validationBootstrapParameters.setParameterNameProvider(parameterNameProvider);
        return this;
    }

    @Override // javax.validation.Configuration
    public final HibernateValidatorConfiguration addMapping(InputStream stream) {
        Contracts.assertNotNull(stream, Messages.MESSAGES.inputStreamCannotBeNull());
        this.validationBootstrapParameters.addMapping(stream.markSupported() ? stream : new BufferedInputStream(stream));
        return this;
    }

    @Override // org.hibernate.validator.HibernateValidatorConfiguration
    public final HibernateValidatorConfiguration failFast(boolean failFast) {
        this.failFast = failFast;
        return this;
    }

    @Override // org.hibernate.validator.HibernateValidatorConfiguration
    public HibernateValidatorConfiguration allowOverridingMethodAlterParameterConstraint(boolean allow) {
        this.methodValidationConfiguration.allowOverridingMethodAlterParameterConstraint(allow);
        return this;
    }

    public boolean isAllowOverridingMethodAlterParameterConstraint() {
        return this.methodValidationConfiguration.isAllowOverridingMethodAlterParameterConstraint();
    }

    @Override // org.hibernate.validator.HibernateValidatorConfiguration
    public HibernateValidatorConfiguration allowMultipleCascadedValidationOnReturnValues(boolean allow) {
        this.methodValidationConfiguration.allowMultipleCascadedValidationOnReturnValues(allow);
        return this;
    }

    public boolean isAllowMultipleCascadedValidationOnReturnValues() {
        return this.methodValidationConfiguration.isAllowMultipleCascadedValidationOnReturnValues();
    }

    @Override // org.hibernate.validator.HibernateValidatorConfiguration
    public HibernateValidatorConfiguration allowParallelMethodsDefineParameterConstraints(boolean allow) {
        this.methodValidationConfiguration.allowParallelMethodsDefineParameterConstraints(allow);
        return this;
    }

    public boolean isAllowParallelMethodsDefineParameterConstraints() {
        return this.methodValidationConfiguration.isAllowParallelMethodsDefineParameterConstraints();
    }

    public MethodValidationConfiguration getMethodValidationConfiguration() {
        return this.methodValidationConfiguration;
    }

    @Override // org.hibernate.validator.HibernateValidatorConfiguration
    public final DefaultConstraintMapping createConstraintMapping() {
        return new DefaultConstraintMapping();
    }

    @Override // org.hibernate.validator.HibernateValidatorConfiguration
    public final HibernateValidatorConfiguration addMapping(ConstraintMapping mapping) {
        Contracts.assertNotNull(mapping, Messages.MESSAGES.parameterMustNotBeNull("mapping"));
        this.programmaticMappings.add((DefaultConstraintMapping) mapping);
        return this;
    }

    @Override // javax.validation.Configuration
    public final HibernateValidatorConfiguration addProperty(String name, String value) {
        if (value != null) {
            this.validationBootstrapParameters.addConfigProperty(name, value);
        }
        return this;
    }

    @Override // org.hibernate.validator.HibernateValidatorConfiguration
    public HibernateValidatorConfiguration addValidatedValueHandler(ValidatedValueUnwrapper<?> handler) {
        Contracts.assertNotNull(handler, Messages.MESSAGES.parameterMustNotBeNull("handler"));
        this.validatedValueHandlers.add(handler);
        return this;
    }

    public final ConstraintMappingContributor getServiceLoaderBasedConstraintMappingContributor() {
        return this.serviceLoaderBasedConstraintMappingContributor;
    }

    @Override // org.hibernate.validator.HibernateValidatorConfiguration
    public HibernateValidatorConfiguration externalClassLoader(ClassLoader externalClassLoader) {
        Contracts.assertNotNull(externalClassLoader, Messages.MESSAGES.parameterMustNotBeNull("externalClassLoader"));
        this.externalClassLoader = externalClassLoader;
        return this;
    }

    @Override // org.hibernate.validator.HibernateValidatorConfiguration
    public HibernateValidatorConfiguration timeProvider(TimeProvider timeProvider) {
        Contracts.assertNotNull(timeProvider, Messages.MESSAGES.parameterMustNotBeNull("timeProvider"));
        this.timeProvider = timeProvider;
        return this;
    }

    @Override // javax.validation.Configuration
    public final ValidatorFactory buildValidatorFactory() throws IOException {
        parseValidationXml();
        ValidatorFactory factory = null;
        try {
            if (isSpecificProvider()) {
                factory = this.validationBootstrapParameters.getProvider().buildValidatorFactory(this);
            } else {
                Class<? extends ValidationProvider<?>> providerClass = this.validationBootstrapParameters.getProviderClass();
                if (providerClass != null) {
                    Iterator<ValidationProvider<?>> it = this.providerResolver.getValidationProviders().iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        ValidationProvider<?> provider = it.next();
                        if (providerClass.isAssignableFrom(provider.getClass())) {
                            factory = provider.buildValidatorFactory(this);
                            break;
                        }
                    }
                    if (factory == null) {
                        throw log.getUnableToFindProviderException(providerClass);
                    }
                } else {
                    List<ValidationProvider<?>> providers = this.providerResolver.getValidationProviders();
                    if (!$assertionsDisabled && providers.size() == 0) {
                        throw new AssertionError();
                    }
                    factory = providers.get(0).buildValidatorFactory(this);
                }
            }
            return factory;
        } finally {
            for (InputStream in : this.configurationStreams) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.unableToCloseInputStream();
                }
            }
        }
    }

    @Override // javax.validation.spi.ConfigurationState
    public final boolean isIgnoreXmlConfiguration() {
        return this.ignoreXmlConfiguration;
    }

    @Override // javax.validation.spi.ConfigurationState
    public final MessageInterpolator getMessageInterpolator() {
        if (this.messageInterpolator == null) {
            MessageInterpolator interpolator = this.validationBootstrapParameters.getMessageInterpolator();
            if (interpolator != null) {
                this.messageInterpolator = interpolator;
            } else {
                this.messageInterpolator = getDefaultMessageInterpolatorConfiguredWithClassLoader();
            }
        }
        return this.messageInterpolator;
    }

    @Override // javax.validation.spi.ConfigurationState
    public final Set<InputStream> getMappingStreams() {
        return this.validationBootstrapParameters.getMappings();
    }

    public final boolean getFailFast() {
        return this.failFast;
    }

    @Override // javax.validation.spi.ConfigurationState
    public final ConstraintValidatorFactory getConstraintValidatorFactory() {
        return this.validationBootstrapParameters.getConstraintValidatorFactory();
    }

    @Override // javax.validation.spi.ConfigurationState
    public final TraversableResolver getTraversableResolver() {
        return this.validationBootstrapParameters.getTraversableResolver();
    }

    @Override // javax.validation.Configuration
    public BootstrapConfiguration getBootstrapConfiguration() {
        if (this.bootstrapConfiguration == null) {
            this.bootstrapConfiguration = new ValidationXmlParser(this.externalClassLoader).parseValidationXml();
        }
        return this.bootstrapConfiguration;
    }

    @Override // javax.validation.spi.ConfigurationState
    public ParameterNameProvider getParameterNameProvider() {
        return this.validationBootstrapParameters.getParameterNameProvider();
    }

    public List<ValidatedValueUnwrapper<?>> getValidatedValueHandlers() {
        return this.validatedValueHandlers;
    }

    public TimeProvider getTimeProvider() {
        return this.timeProvider;
    }

    @Override // javax.validation.spi.ConfigurationState
    public final Map<String, String> getProperties() {
        return this.validationBootstrapParameters.getConfigProperties();
    }

    public ClassLoader getExternalClassLoader() {
        return this.externalClassLoader;
    }

    @Override // javax.validation.Configuration
    public final MessageInterpolator getDefaultMessageInterpolator() {
        if (this.defaultMessageInterpolator == null) {
            this.defaultMessageInterpolator = new ResourceBundleMessageInterpolator(this.defaultResourceBundleLocator);
        }
        return this.defaultMessageInterpolator;
    }

    @Override // javax.validation.Configuration
    public final TraversableResolver getDefaultTraversableResolver() {
        return this.defaultTraversableResolver;
    }

    @Override // javax.validation.Configuration
    public final ConstraintValidatorFactory getDefaultConstraintValidatorFactory() {
        return this.defaultConstraintValidatorFactory;
    }

    @Override // org.hibernate.validator.HibernateValidatorConfiguration
    public final ResourceBundleLocator getDefaultResourceBundleLocator() {
        return this.defaultResourceBundleLocator;
    }

    @Override // javax.validation.Configuration
    public ParameterNameProvider getDefaultParameterNameProvider() {
        return this.defaultParameterNameProvider;
    }

    public final Set<DefaultConstraintMapping> getProgrammaticMappings() {
        return this.programmaticMappings;
    }

    private boolean isSpecificProvider() {
        return this.validationBootstrapParameters.getProvider() != null;
    }

    private void parseValidationXml() {
        if (this.ignoreXmlConfiguration) {
            log.ignoringXmlConfiguration();
            if (this.validationBootstrapParameters.getTraversableResolver() == null) {
                this.validationBootstrapParameters.setTraversableResolver(this.defaultTraversableResolver);
            }
            if (this.validationBootstrapParameters.getConstraintValidatorFactory() == null) {
                this.validationBootstrapParameters.setConstraintValidatorFactory(this.defaultConstraintValidatorFactory);
            }
            if (this.validationBootstrapParameters.getParameterNameProvider() == null) {
                this.validationBootstrapParameters.setParameterNameProvider(this.defaultParameterNameProvider);
                return;
            }
            return;
        }
        ValidationBootstrapParameters xmlParameters = new ValidationBootstrapParameters(getBootstrapConfiguration(), this.externalClassLoader);
        applyXmlSettings(xmlParameters);
    }

    private void applyXmlSettings(ValidationBootstrapParameters xmlParameters) {
        this.validationBootstrapParameters.setProviderClass(xmlParameters.getProviderClass());
        if (this.validationBootstrapParameters.getMessageInterpolator() == null && xmlParameters.getMessageInterpolator() != null) {
            this.validationBootstrapParameters.setMessageInterpolator(xmlParameters.getMessageInterpolator());
        }
        if (this.validationBootstrapParameters.getTraversableResolver() == null) {
            if (xmlParameters.getTraversableResolver() != null) {
                this.validationBootstrapParameters.setTraversableResolver(xmlParameters.getTraversableResolver());
            } else {
                this.validationBootstrapParameters.setTraversableResolver(this.defaultTraversableResolver);
            }
        }
        if (this.validationBootstrapParameters.getConstraintValidatorFactory() == null) {
            if (xmlParameters.getConstraintValidatorFactory() != null) {
                this.validationBootstrapParameters.setConstraintValidatorFactory(xmlParameters.getConstraintValidatorFactory());
            } else {
                this.validationBootstrapParameters.setConstraintValidatorFactory(this.defaultConstraintValidatorFactory);
            }
        }
        if (this.validationBootstrapParameters.getParameterNameProvider() == null) {
            if (xmlParameters.getParameterNameProvider() != null) {
                this.validationBootstrapParameters.setParameterNameProvider(xmlParameters.getParameterNameProvider());
            } else {
                this.validationBootstrapParameters.setParameterNameProvider(this.defaultParameterNameProvider);
            }
        }
        this.validationBootstrapParameters.addAllMappings(xmlParameters.getMappings());
        this.configurationStreams.addAll(xmlParameters.getMappings());
        for (Map.Entry<String, String> entry : xmlParameters.getConfigProperties().entrySet()) {
            if (this.validationBootstrapParameters.getConfigProperties().get(entry.getKey()) == null) {
                this.validationBootstrapParameters.addConfigProperty(entry.getKey(), entry.getValue());
            }
        }
    }

    private boolean isJavaFxInClasspath() {
        return isClassPresent("javafx.application.Application", false);
    }

    private boolean isClassPresent(String className, boolean fallbackOnTCCL) {
        try {
            run(LoadClass.action(className, getClass().getClassLoader(), fallbackOnTCCL));
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }

    private MessageInterpolator getDefaultMessageInterpolatorConfiguredWithClassLoader() {
        if (this.externalClassLoader != null) {
            PlatformResourceBundleLocator userResourceBundleLocator = new PlatformResourceBundleLocator(AbstractMessageInterpolator.USER_VALIDATION_MESSAGES, this.externalClassLoader);
            PlatformResourceBundleLocator contributorResourceBundleLocator = new PlatformResourceBundleLocator(AbstractMessageInterpolator.CONTRIBUTOR_VALIDATION_MESSAGES, this.externalClassLoader, true);
            ClassLoader originalContextClassLoader = (ClassLoader) run(GetClassLoader.fromContext());
            try {
                run(SetContextClassLoader.action(this.externalClassLoader));
                ResourceBundleMessageInterpolator resourceBundleMessageInterpolator = new ResourceBundleMessageInterpolator(userResourceBundleLocator, contributorResourceBundleLocator);
                run(SetContextClassLoader.action(originalContextClassLoader));
                return resourceBundleMessageInterpolator;
            } catch (Throwable th) {
                run(SetContextClassLoader.action(originalContextClassLoader));
                throw th;
            }
        }
        return getDefaultMessageInterpolator();
    }

    private static <T> T run(PrivilegedAction<T> privilegedAction) {
        return System.getSecurityManager() != null ? (T) AccessController.doPrivileged(privilegedAction) : privilegedAction.run();
    }
}
