package javax.validation;

import java.util.Map;
import java.util.Set;
import javax.validation.executable.ExecutableType;

/* loaded from: validation-api-1.1.0.Final.jar:javax/validation/BootstrapConfiguration.class */
public interface BootstrapConfiguration {
    String getDefaultProviderClassName();

    String getConstraintValidatorFactoryClassName();

    String getMessageInterpolatorClassName();

    String getTraversableResolverClassName();

    String getParameterNameProviderClassName();

    Set<String> getConstraintMappingResourcePaths();

    boolean isExecutableValidationEnabled();

    Set<ExecutableType> getDefaultValidatedExecutableTypes();

    Map<String, String> getProperties();
}
