package org.hibernate.validator;

import javax.validation.Configuration;
import org.hibernate.validator.cfg.ConstraintMapping;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;
import org.hibernate.validator.spi.time.TimeProvider;
import org.hibernate.validator.spi.valuehandling.ValidatedValueUnwrapper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/HibernateValidatorConfiguration.class */
public interface HibernateValidatorConfiguration extends Configuration<HibernateValidatorConfiguration> {
    public static final String FAIL_FAST = "hibernate.validator.fail_fast";
    public static final String ALLOW_PARAMETER_CONSTRAINT_OVERRIDE = "hibernate.validator.allow_parameter_constraint_override";
    public static final String ALLOW_MULTIPLE_CASCADED_VALIDATION_ON_RESULT = "hibernate.validator.allow_multiple_cascaded_validation_on_result";
    public static final String ALLOW_PARALLEL_METHODS_DEFINE_PARAMETER_CONSTRAINTS = "hibernate.validator.allow_parallel_method_parameter_constraint";
    public static final String VALIDATED_VALUE_HANDLERS = "hibernate.validator.validated_value_handlers";

    @Deprecated
    public static final String CONSTRAINT_MAPPING_CONTRIBUTOR = "hibernate.validator.constraint_mapping_contributor";
    public static final String CONSTRAINT_MAPPING_CONTRIBUTORS = "hibernate.validator.constraint_mapping_contributors";
    public static final String TIME_PROVIDER = "hibernate.validator.time_provider";

    ResourceBundleLocator getDefaultResourceBundleLocator();

    ConstraintMapping createConstraintMapping();

    HibernateValidatorConfiguration addMapping(ConstraintMapping constraintMapping);

    HibernateValidatorConfiguration failFast(boolean z);

    HibernateValidatorConfiguration addValidatedValueHandler(ValidatedValueUnwrapper<?> validatedValueUnwrapper);

    HibernateValidatorConfiguration externalClassLoader(ClassLoader classLoader);

    HibernateValidatorConfiguration timeProvider(TimeProvider timeProvider);

    HibernateValidatorConfiguration allowOverridingMethodAlterParameterConstraint(boolean z);

    HibernateValidatorConfiguration allowMultipleCascadedValidationOnReturnValues(boolean z);

    HibernateValidatorConfiguration allowParallelMethodsDefineParameterConstraints(boolean z);
}
