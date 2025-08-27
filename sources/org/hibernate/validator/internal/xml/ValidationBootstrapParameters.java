package org.hibernate.validator.internal.xml;

import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.validation.BootstrapConfiguration;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.TraversableResolver;
import javax.validation.ValidationException;
import javax.validation.spi.ValidationProvider;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.internal.util.privilegedactions.LoadClass;
import org.hibernate.validator.internal.util.privilegedactions.NewInstance;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/ValidationBootstrapParameters.class */
public class ValidationBootstrapParameters {
    private static final Log log = LoggerFactory.make();
    private ConstraintValidatorFactory constraintValidatorFactory;
    private MessageInterpolator messageInterpolator;
    private TraversableResolver traversableResolver;
    private ParameterNameProvider parameterNameProvider;
    private ValidationProvider<?> provider;
    private Class<? extends ValidationProvider<?>> providerClass = null;
    private final Map<String, String> configProperties = CollectionHelper.newHashMap();
    private final Set<InputStream> mappings = CollectionHelper.newHashSet();

    public ValidationBootstrapParameters() {
    }

    public ValidationBootstrapParameters(BootstrapConfiguration bootstrapConfiguration, ClassLoader externalClassLoader) {
        setProviderClass(bootstrapConfiguration.getDefaultProviderClassName(), externalClassLoader);
        setMessageInterpolator(bootstrapConfiguration.getMessageInterpolatorClassName(), externalClassLoader);
        setTraversableResolver(bootstrapConfiguration.getTraversableResolverClassName(), externalClassLoader);
        setConstraintFactory(bootstrapConfiguration.getConstraintValidatorFactoryClassName(), externalClassLoader);
        setParameterNameProvider(bootstrapConfiguration.getParameterNameProviderClassName(), externalClassLoader);
        setMappingStreams(bootstrapConfiguration.getConstraintMappingResourcePaths(), externalClassLoader);
        setConfigProperties(bootstrapConfiguration.getProperties());
    }

    public final ConstraintValidatorFactory getConstraintValidatorFactory() {
        return this.constraintValidatorFactory;
    }

    public final void setConstraintValidatorFactory(ConstraintValidatorFactory constraintValidatorFactory) {
        this.constraintValidatorFactory = constraintValidatorFactory;
    }

    public final MessageInterpolator getMessageInterpolator() {
        return this.messageInterpolator;
    }

    public final void setMessageInterpolator(MessageInterpolator messageInterpolator) {
        this.messageInterpolator = messageInterpolator;
    }

    public final ValidationProvider<?> getProvider() {
        return this.provider;
    }

    public final void setProvider(ValidationProvider<?> provider) {
        this.provider = provider;
    }

    public final Class<? extends ValidationProvider<?>> getProviderClass() {
        return this.providerClass;
    }

    public final void setProviderClass(Class<? extends ValidationProvider<?>> providerClass) {
        this.providerClass = providerClass;
    }

    public final TraversableResolver getTraversableResolver() {
        return this.traversableResolver;
    }

    public final void setTraversableResolver(TraversableResolver traversableResolver) {
        this.traversableResolver = traversableResolver;
    }

    public final void addConfigProperty(String key, String value) {
        this.configProperties.put(key, value);
    }

    public final void addMapping(InputStream in) {
        this.mappings.add(in);
    }

    public final void addAllMappings(Set<InputStream> mappings) {
        this.mappings.addAll(mappings);
    }

    public final Set<InputStream> getMappings() {
        return Collections.unmodifiableSet(this.mappings);
    }

    public final Map<String, String> getConfigProperties() {
        return Collections.unmodifiableMap(this.configProperties);
    }

    public ParameterNameProvider getParameterNameProvider() {
        return this.parameterNameProvider;
    }

    public void setParameterNameProvider(ParameterNameProvider parameterNameProvider) {
        this.parameterNameProvider = parameterNameProvider;
    }

    private void setProviderClass(String providerFqcn, ClassLoader externalClassLoader) {
        if (providerFqcn != null) {
            try {
                this.providerClass = (Class) run(LoadClass.action(providerFqcn, externalClassLoader));
                log.usingValidationProvider(providerFqcn);
            } catch (Exception e) {
                throw log.getUnableToInstantiateValidationProviderClassException(providerFqcn, e);
            }
        }
    }

    private void setMessageInterpolator(String messageInterpolatorFqcn, ClassLoader externalClassLoader) {
        if (messageInterpolatorFqcn != null) {
            try {
                Class<MessageInterpolator> messageInterpolatorClass = (Class) run(LoadClass.action(messageInterpolatorFqcn, externalClassLoader));
                this.messageInterpolator = (MessageInterpolator) run(NewInstance.action(messageInterpolatorClass, "message interpolator"));
                log.usingMessageInterpolator(messageInterpolatorFqcn);
            } catch (ValidationException e) {
                throw log.getUnableToInstantiateMessageInterpolatorClassException(messageInterpolatorFqcn, e);
            }
        }
    }

    private void setTraversableResolver(String traversableResolverFqcn, ClassLoader externalClassLoader) {
        if (traversableResolverFqcn != null) {
            try {
                Class<TraversableResolver> clazz = (Class) run(LoadClass.action(traversableResolverFqcn, externalClassLoader));
                this.traversableResolver = (TraversableResolver) run(NewInstance.action(clazz, "traversable resolver"));
                log.usingTraversableResolver(traversableResolverFqcn);
            } catch (ValidationException e) {
                throw log.getUnableToInstantiateTraversableResolverClassException(traversableResolverFqcn, e);
            }
        }
    }

    private void setConstraintFactory(String constraintFactoryFqcn, ClassLoader externalClassLoader) {
        if (constraintFactoryFqcn != null) {
            try {
                Class<ConstraintValidatorFactory> clazz = (Class) run(LoadClass.action(constraintFactoryFqcn, externalClassLoader));
                this.constraintValidatorFactory = (ConstraintValidatorFactory) run(NewInstance.action(clazz, "constraint factory class"));
                log.usingConstraintFactory(constraintFactoryFqcn);
            } catch (ValidationException e) {
                throw log.getUnableToInstantiateConstraintFactoryClassException(constraintFactoryFqcn, e);
            }
        }
    }

    private void setParameterNameProvider(String parameterNameProviderFqcn, ClassLoader externalClassLoader) {
        if (parameterNameProviderFqcn != null) {
            try {
                Class<ParameterNameProvider> clazz = (Class) run(LoadClass.action(parameterNameProviderFqcn, externalClassLoader));
                this.parameterNameProvider = (ParameterNameProvider) run(NewInstance.action(clazz, "parameter name provider class"));
                log.usingParameterNameProvider(parameterNameProviderFqcn);
            } catch (ValidationException e) {
                throw log.getUnableToInstantiateParameterNameProviderClassException(parameterNameProviderFqcn, e);
            }
        }
    }

    private void setMappingStreams(Set<String> mappingFileNames, ClassLoader externalClassLoader) {
        for (String mappingFileName : mappingFileNames) {
            log.debugf("Trying to open input stream for %s.", mappingFileName);
            InputStream in = ResourceLoaderHelper.getResettableInputStreamForPath(mappingFileName, externalClassLoader);
            if (in == null) {
                throw log.getUnableToOpenInputStreamForMappingFileException(mappingFileName);
            }
            this.mappings.add(in);
        }
    }

    private void setConfigProperties(Map<String, String> properties) {
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            this.configProperties.put(entry.getKey(), entry.getValue());
        }
    }

    private <T> T run(PrivilegedAction<T> privilegedAction) {
        return System.getSecurityManager() != null ? (T) AccessController.doPrivileged(privilegedAction) : privilegedAction.run();
    }
}
