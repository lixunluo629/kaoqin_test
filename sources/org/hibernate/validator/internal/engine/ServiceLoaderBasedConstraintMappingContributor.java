package org.hibernate.validator.internal.engine;

import com.fasterxml.classmate.ResolvedType;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.validation.ConstraintValidator;
import org.hibernate.validator.cfg.ConstraintMapping;
import org.hibernate.validator.cfg.context.ConstraintDefinitionContext;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.TypeResolutionHelper;
import org.hibernate.validator.internal.util.privilegedactions.GetConstraintValidatorList;
import org.hibernate.validator.spi.cfg.ConstraintMappingContributor;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/ServiceLoaderBasedConstraintMappingContributor.class */
public class ServiceLoaderBasedConstraintMappingContributor implements ConstraintMappingContributor {
    private final TypeResolutionHelper typeResolutionHelper;

    public ServiceLoaderBasedConstraintMappingContributor(TypeResolutionHelper typeResolutionHelper) {
        this.typeResolutionHelper = typeResolutionHelper;
    }

    @Override // org.hibernate.validator.spi.cfg.ConstraintMappingContributor
    public void createConstraintMappings(ConstraintMappingContributor.ConstraintMappingBuilder builder) throws NegativeArraySizeException {
        Map<Class<?>, List<Class<?>>> customValidators = CollectionHelper.newHashMap();
        GetConstraintValidatorList constraintValidatorListAction = new GetConstraintValidatorList();
        List<ConstraintValidator<?, ?>> discoveredConstraintValidators = (List) run(constraintValidatorListAction);
        for (ConstraintValidator<?, ?> constraintValidator : discoveredConstraintValidators) {
            Class<?> constraintValidatorClass = constraintValidator.getClass();
            Class<?> annotationType = determineAnnotationType(constraintValidatorClass);
            List<Class<?>> validators = customValidators.get(annotationType);
            if (annotationType != null && validators == null) {
                validators = new ArrayList<>();
                customValidators.put(annotationType, validators);
            }
            validators.add(constraintValidatorClass);
        }
        ConstraintMapping constraintMapping = builder.addConstraintMapping();
        for (Map.Entry<Class<?>, List<Class<?>>> entry : customValidators.entrySet()) {
            registerConstraintDefinition(constraintMapping, entry.getKey(), entry.getValue());
        }
    }

    private <A extends Annotation> void registerConstraintDefinition(ConstraintMapping constraintMapping, Class<?> constraintType, List<Class<?>> validatorTypes) {
        ConstraintDefinitionContext<A> context = constraintMapping.constraintDefinition(constraintType).includeExistingValidators(true);
        Iterator<Class<?>> it = validatorTypes.iterator();
        while (it.hasNext()) {
            context.validatedBy((Class) it.next());
        }
    }

    private Class<?> determineAnnotationType(Class<?> constraintValidatorClass) throws NegativeArraySizeException {
        ResolvedType resolvedType = this.typeResolutionHelper.getTypeResolver().resolve(constraintValidatorClass, new Type[0]);
        return resolvedType.typeParametersFor(ConstraintValidator.class).get(0).getErasedType();
    }

    private <T> T run(PrivilegedAction<T> privilegedAction) {
        return System.getSecurityManager() != null ? (T) AccessController.doPrivileged(privilegedAction) : privilegedAction.run();
    }
}
