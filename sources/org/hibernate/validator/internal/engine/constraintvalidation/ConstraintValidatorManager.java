package org.hibernate.validator.internal.engine.constraintvalidation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.constraints.Null;
import javax.validation.metadata.ConstraintDescriptor;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.Contracts;
import org.hibernate.validator.internal.util.TypeHelper;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/constraintvalidation/ConstraintValidatorManager.class */
public class ConstraintValidatorManager {
    private static final Log log = LoggerFactory.make();
    private static ConstraintValidator<?, ?> DUMMY_CONSTRAINT_VALIDATOR = new ConstraintValidator<Null, Object>() { // from class: org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorManager.1
        @Override // javax.validation.ConstraintValidator
        public void initialize(Null constraintAnnotation) {
        }

        @Override // javax.validation.ConstraintValidator
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            return false;
        }
    };
    private final ConstraintValidatorFactory defaultConstraintValidatorFactory;
    private ConstraintValidatorFactory leastRecentlyUsedNonDefaultConstraintValidatorFactory;
    private final ConcurrentHashMap<CacheKey, ConstraintValidator<?, ?>> constraintValidatorCache = new ConcurrentHashMap<>();

    public ConstraintValidatorManager(ConstraintValidatorFactory constraintValidatorFactory) {
        this.defaultConstraintValidatorFactory = constraintValidatorFactory;
    }

    public <V, A extends Annotation> ConstraintValidator<A, V> getInitializedValidator(Type validatedValueType, ConstraintDescriptorImpl<A> descriptor, ConstraintValidatorFactory constraintFactory) {
        Contracts.assertNotNull(validatedValueType);
        Contracts.assertNotNull(descriptor);
        Contracts.assertNotNull(constraintFactory);
        CacheKey key = new CacheKey(descriptor.getAnnotation(), validatedValueType, constraintFactory);
        if (this.constraintValidatorCache.containsKey(key)) {
            ConstraintValidator<A, V> constraintValidator = (ConstraintValidator) this.constraintValidatorCache.get(key);
            if (DUMMY_CONSTRAINT_VALIDATOR.equals(constraintValidator)) {
                return null;
            }
            log.tracef("Constraint validator %s found in cache.", constraintValidator);
            return constraintValidator;
        }
        ConstraintValidator<A, V> constraintValidator2 = createAndInitializeValidator(constraintFactory, findMatchingValidatorClass(descriptor, validatedValueType), descriptor);
        if (constraintValidator2 == null) {
            putInitializedValidator(validatedValueType, descriptor.getAnnotation(), constraintFactory, DUMMY_CONSTRAINT_VALIDATOR);
            return null;
        }
        putInitializedValidator(validatedValueType, descriptor.getAnnotation(), constraintFactory, constraintValidator2);
        return constraintValidator2;
    }

    private void putInitializedValidator(Type validatedValueType, Annotation annotation, ConstraintValidatorFactory constraintFactory, ConstraintValidator<?, ?> constraintValidator) {
        if (constraintFactory != this.defaultConstraintValidatorFactory && constraintFactory != this.leastRecentlyUsedNonDefaultConstraintValidatorFactory) {
            clearEntriesForFactory(this.leastRecentlyUsedNonDefaultConstraintValidatorFactory);
            this.leastRecentlyUsedNonDefaultConstraintValidatorFactory = constraintFactory;
        }
        CacheKey key = new CacheKey(annotation, validatedValueType, constraintFactory);
        this.constraintValidatorCache.putIfAbsent(key, constraintValidator);
    }

    private <V, A extends Annotation> ConstraintValidator<A, V> createAndInitializeValidator(ConstraintValidatorFactory constraintFactory, Class<? extends ConstraintValidator<?, ?>> validatorClass, ConstraintDescriptor<A> descriptor) {
        if (validatorClass == null) {
            return null;
        }
        ConstraintValidator<A, V> constraintValidator = constraintFactory.getInstance(validatorClass);
        if (constraintValidator == null) {
            throw log.getConstraintFactoryMustNotReturnNullException(validatorClass.getName());
        }
        initializeConstraint(descriptor, constraintValidator);
        return constraintValidator;
    }

    private void clearEntriesForFactory(ConstraintValidatorFactory constraintFactory) {
        List<CacheKey> entriesToRemove = new ArrayList<>();
        for (Map.Entry<CacheKey, ConstraintValidator<?, ?>> entry : this.constraintValidatorCache.entrySet()) {
            if (entry.getKey().getConstraintFactory() == constraintFactory) {
                entriesToRemove.add(entry.getKey());
            }
        }
        for (CacheKey key : entriesToRemove) {
            this.constraintValidatorCache.remove(key);
        }
    }

    public void clear() {
        for (Map.Entry<CacheKey, ConstraintValidator<?, ?>> entry : this.constraintValidatorCache.entrySet()) {
            entry.getKey().getConstraintFactory().releaseInstance(entry.getValue());
        }
        this.constraintValidatorCache.clear();
    }

    public ConstraintValidatorFactory getDefaultConstraintValidatorFactory() {
        return this.defaultConstraintValidatorFactory;
    }

    public int numberOfCachedConstraintValidatorInstances() {
        return this.constraintValidatorCache.size();
    }

    private <A extends Annotation> Class<? extends ConstraintValidator<A, ?>> findMatchingValidatorClass(ConstraintDescriptorImpl<A> descriptor, Type validatedValueType) {
        Map<Type, Class<? extends ConstraintValidator<A, ?>>> availableValidatorTypes = TypeHelper.getValidatorsTypes(descriptor.getAnnotationType(), descriptor.getMatchingConstraintValidatorClasses());
        List<Type> discoveredSuitableTypes = findSuitableValidatorTypes(validatedValueType, availableValidatorTypes);
        resolveAssignableTypes(discoveredSuitableTypes);
        if (discoveredSuitableTypes.size() == 0) {
            return null;
        }
        if (discoveredSuitableTypes.size() > 1) {
            StringBuilder builder = new StringBuilder();
            for (Type clazz : discoveredSuitableTypes) {
                builder.append(clazz);
                builder.append(", ");
            }
            builder.delete(builder.length() - 2, builder.length());
            throw log.getMoreThanOneValidatorFoundForTypeException(validatedValueType, builder.toString());
        }
        Type suitableType = discoveredSuitableTypes.get(0);
        return availableValidatorTypes.get(suitableType);
    }

    private <A extends Annotation> List<Type> findSuitableValidatorTypes(Type type, Map<Type, Class<? extends ConstraintValidator<A, ?>>> availableValidatorTypes) {
        List<Type> determinedSuitableTypes = CollectionHelper.newArrayList();
        for (Type validatorType : availableValidatorTypes.keySet()) {
            if (TypeHelper.isAssignable(validatorType, type) && !determinedSuitableTypes.contains(validatorType)) {
                determinedSuitableTypes.add(validatorType);
            }
        }
        return determinedSuitableTypes;
    }

    private <A extends Annotation> void initializeConstraint(ConstraintDescriptor<A> descriptor, ConstraintValidator<A, ?> constraintValidator) {
        try {
            constraintValidator.initialize(descriptor.getAnnotation());
        } catch (RuntimeException e) {
            throw log.getUnableToInitializeConstraintValidatorException(constraintValidator.getClass().getName(), e);
        }
    }

    private void resolveAssignableTypes(List<Type> assignableTypes) {
        if (assignableTypes.size() == 0 || assignableTypes.size() == 1) {
            return;
        }
        List<Type> typesToRemove = new ArrayList<>();
        do {
            typesToRemove.clear();
            Type type = assignableTypes.get(0);
            for (int i = 1; i < assignableTypes.size(); i++) {
                if (TypeHelper.isAssignable(type, assignableTypes.get(i))) {
                    typesToRemove.add(type);
                } else if (TypeHelper.isAssignable(assignableTypes.get(i), type)) {
                    typesToRemove.add(assignableTypes.get(i));
                }
            }
            assignableTypes.removeAll(typesToRemove);
        } while (typesToRemove.size() > 0);
    }

    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/constraintvalidation/ConstraintValidatorManager$CacheKey.class */
    private static final class CacheKey {
        private final Annotation annotation;
        private final Type validatedType;
        private final ConstraintValidatorFactory constraintFactory;
        private final int hashCode;

        private CacheKey(Annotation annotation, Type validatorType, ConstraintValidatorFactory constraintFactory) {
            this.annotation = annotation;
            this.validatedType = validatorType;
            this.constraintFactory = constraintFactory;
            this.hashCode = createHashCode();
        }

        public ConstraintValidatorFactory getConstraintFactory() {
            return this.constraintFactory;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            CacheKey cacheKey = (CacheKey) o;
            if (this.annotation != null) {
                if (!this.annotation.equals(cacheKey.annotation)) {
                    return false;
                }
            } else if (cacheKey.annotation != null) {
                return false;
            }
            if (this.constraintFactory != null) {
                if (!this.constraintFactory.equals(cacheKey.constraintFactory)) {
                    return false;
                }
            } else if (cacheKey.constraintFactory != null) {
                return false;
            }
            if (this.validatedType != null) {
                if (!this.validatedType.equals(cacheKey.validatedType)) {
                    return false;
                }
                return true;
            }
            if (cacheKey.validatedType != null) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return this.hashCode;
        }

        private int createHashCode() {
            int result = this.annotation != null ? this.annotation.hashCode() : 0;
            return (31 * ((31 * result) + (this.validatedType != null ? this.validatedType.hashCode() : 0))) + (this.constraintFactory != null ? this.constraintFactory.hashCode() : 0);
        }
    }
}
