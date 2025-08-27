package org.hibernate.validator.internal.xml;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import javax.validation.BootstrapConfiguration;
import javax.validation.executable.ExecutableType;
import org.hibernate.validator.internal.util.CollectionHelper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/BootstrapConfigurationImpl.class */
public class BootstrapConfigurationImpl implements BootstrapConfiguration {
    private static final Set<ExecutableType> DEFAULT_VALIDATED_EXECUTABLE_TYPES = Collections.unmodifiableSet(EnumSet.of(ExecutableType.CONSTRUCTORS, ExecutableType.NON_GETTER_METHODS));
    private static final Set<ExecutableType> ALL_VALIDATED_EXECUTABLE_TYPES = Collections.unmodifiableSet(EnumSet.complementOf(EnumSet.of(ExecutableType.ALL, ExecutableType.NONE, ExecutableType.IMPLICIT)));
    private static final BootstrapConfiguration DEFAULT_BOOTSTRAP_CONFIGURATION = new BootstrapConfigurationImpl();
    private final String defaultProviderClassName;
    private final String constraintValidatorFactoryClassName;
    private final String messageInterpolatorClassName;
    private final String traversableResolverClassName;
    private final String parameterNameProviderClassName;
    private final Set<String> constraintMappingResourcePaths;
    private final Map<String, String> properties;
    private final Set<ExecutableType> validatedExecutableTypes;
    private final boolean isExecutableValidationEnabled;

    private BootstrapConfigurationImpl() {
        this.defaultProviderClassName = null;
        this.constraintValidatorFactoryClassName = null;
        this.messageInterpolatorClassName = null;
        this.traversableResolverClassName = null;
        this.parameterNameProviderClassName = null;
        this.validatedExecutableTypes = DEFAULT_VALIDATED_EXECUTABLE_TYPES;
        this.isExecutableValidationEnabled = true;
        this.constraintMappingResourcePaths = CollectionHelper.newHashSet();
        this.properties = CollectionHelper.newHashMap();
    }

    public BootstrapConfigurationImpl(String defaultProviderClassName, String constraintValidatorFactoryClassName, String messageInterpolatorClassName, String traversableResolverClassName, String parameterNameProviderClassName, EnumSet<ExecutableType> validatedExecutableTypes, boolean isExecutableValidationEnabled, Set<String> constraintMappingResourcePaths, Map<String, String> properties) {
        this.defaultProviderClassName = defaultProviderClassName;
        this.constraintValidatorFactoryClassName = constraintValidatorFactoryClassName;
        this.messageInterpolatorClassName = messageInterpolatorClassName;
        this.traversableResolverClassName = traversableResolverClassName;
        this.parameterNameProviderClassName = parameterNameProviderClassName;
        this.validatedExecutableTypes = prepareValidatedExecutableTypes(validatedExecutableTypes);
        this.isExecutableValidationEnabled = isExecutableValidationEnabled;
        this.constraintMappingResourcePaths = constraintMappingResourcePaths;
        this.properties = properties;
    }

    public static BootstrapConfiguration getDefaultBootstrapConfiguration() {
        return DEFAULT_BOOTSTRAP_CONFIGURATION;
    }

    private Set<ExecutableType> prepareValidatedExecutableTypes(EnumSet<ExecutableType> validatedExecutableTypes) {
        if (validatedExecutableTypes == null) {
            return DEFAULT_VALIDATED_EXECUTABLE_TYPES;
        }
        if (validatedExecutableTypes.contains(ExecutableType.ALL)) {
            return ALL_VALIDATED_EXECUTABLE_TYPES;
        }
        if (validatedExecutableTypes.contains(ExecutableType.NONE)) {
            return EnumSet.noneOf(ExecutableType.class);
        }
        return validatedExecutableTypes;
    }

    @Override // javax.validation.BootstrapConfiguration
    public String getDefaultProviderClassName() {
        return this.defaultProviderClassName;
    }

    @Override // javax.validation.BootstrapConfiguration
    public String getConstraintValidatorFactoryClassName() {
        return this.constraintValidatorFactoryClassName;
    }

    @Override // javax.validation.BootstrapConfiguration
    public String getMessageInterpolatorClassName() {
        return this.messageInterpolatorClassName;
    }

    @Override // javax.validation.BootstrapConfiguration
    public String getTraversableResolverClassName() {
        return this.traversableResolverClassName;
    }

    @Override // javax.validation.BootstrapConfiguration
    public String getParameterNameProviderClassName() {
        return this.parameterNameProviderClassName;
    }

    @Override // javax.validation.BootstrapConfiguration
    public Set<String> getConstraintMappingResourcePaths() {
        return CollectionHelper.newHashSet((Collection) this.constraintMappingResourcePaths);
    }

    @Override // javax.validation.BootstrapConfiguration
    public boolean isExecutableValidationEnabled() {
        return this.isExecutableValidationEnabled;
    }

    @Override // javax.validation.BootstrapConfiguration
    public Set<ExecutableType> getDefaultValidatedExecutableTypes() {
        return CollectionHelper.newHashSet((Collection) this.validatedExecutableTypes);
    }

    @Override // javax.validation.BootstrapConfiguration
    public Map<String, String> getProperties() {
        return CollectionHelper.newHashMap(this.properties);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BootstrapConfigurationImpl");
        sb.append("{defaultProviderClassName='").append(this.defaultProviderClassName).append('\'');
        sb.append(", constraintValidatorFactoryClassName='").append(this.constraintValidatorFactoryClassName).append('\'');
        sb.append(", messageInterpolatorClassName='").append(this.messageInterpolatorClassName).append('\'');
        sb.append(", traversableResolverClassName='").append(this.traversableResolverClassName).append('\'');
        sb.append(", parameterNameProviderClassName='").append(this.parameterNameProviderClassName).append('\'');
        sb.append(", validatedExecutableTypes='").append(this.validatedExecutableTypes).append('\'');
        sb.append(", constraintMappingResourcePaths=").append(this.constraintMappingResourcePaths).append('\'');
        sb.append(", properties=").append(this.properties);
        sb.append('}');
        return sb.toString();
    }
}
