package org.hibernate.validator.internal.engine.constraintvalidation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintViolation;
import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.internal.engine.ValidationContext;
import org.hibernate.validator.internal.engine.ValueContext;
import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.spi.valuehandling.ValidatedValueUnwrapper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/constraintvalidation/ConstraintTree.class */
public class ConstraintTree<A extends Annotation> {
    private static final String TYPE_USE = "TYPE_USE";
    private static final Log log;
    private final ConstraintTree<?> parent;
    private final List<ConstraintTree<?>> children;
    private final ConstraintDescriptorImpl<A> descriptor;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ConstraintTree.class.desiredAssertionStatus();
        log = LoggerFactory.make();
    }

    public ConstraintTree(ConstraintDescriptorImpl<A> descriptor) {
        this(descriptor, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private ConstraintTree(ConstraintDescriptorImpl<A> descriptor, ConstraintTree<?> parent) {
        this.parent = parent;
        this.descriptor = descriptor;
        Set<ConstraintDescriptorImpl<?>> composingConstraints = descriptor.getComposingConstraintImpls();
        this.children = CollectionHelper.newArrayList(composingConstraints.size());
        Iterator<ConstraintDescriptorImpl<?>> it = composingConstraints.iterator();
        while (it.hasNext()) {
            this.children.add(createConstraintTree((ConstraintDescriptorImpl) it.next()));
        }
    }

    private <U extends Annotation> ConstraintTree<U> createConstraintTree(ConstraintDescriptorImpl<U> composingDescriptor) {
        return new ConstraintTree<>(composingDescriptor, this);
    }

    public final List<ConstraintTree<?>> getChildren() {
        return this.children;
    }

    public final ConstraintDescriptorImpl<A> getDescriptor() {
        return this.descriptor;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <T> boolean validateConstraints(ValidationContext<T> executionContext, ValueContext<?, ?> valueContext) {
        Set<ConstraintViolation<T>> constraintViolations = CollectionHelper.newHashSet();
        validateConstraints(executionContext, valueContext, constraintViolations);
        if (!constraintViolations.isEmpty()) {
            executionContext.addConstraintFailures(constraintViolations);
            return false;
        }
        return true;
    }

    private <T, V> void validateConstraints(ValidationContext<T> validationContext, ValueContext<?, V> valueContext, Set<ConstraintViolation<T>> constraintViolations) throws NegativeArraySizeException {
        Set<ConstraintViolation<T>> localViolations;
        CompositionResult compositionResult = validateComposingConstraints(validationContext, valueContext, constraintViolations);
        if (mainConstraintNeedsEvaluation(validationContext, constraintViolations)) {
            if (log.isTraceEnabled()) {
                log.tracef("Validating value %s against constraint defined by %s.", valueContext.getCurrentValidatedValue(), this.descriptor);
            }
            ConstraintValidator<A, V> validator = getInitializedConstraintValidator(validationContext, valueContext);
            ConstraintValidatorContextImpl constraintValidatorContext = new ConstraintValidatorContextImpl(validationContext.getParameterNames(), validationContext.getTimeProvider(), valueContext.getPropertyPath(), this.descriptor);
            localViolations = validateSingleConstraint(validationContext, valueContext, constraintValidatorContext, validator);
            if (localViolations.isEmpty()) {
                compositionResult.setAtLeastOneTrue(true);
            } else {
                compositionResult.setAllTrue(false);
            }
        } else {
            localViolations = Collections.emptySet();
        }
        if (!passesCompositionTypeRequirement(constraintViolations, compositionResult)) {
            prepareFinalConstraintViolations(validationContext, valueContext, constraintViolations, localViolations);
        }
    }

    private <T, V> ConstraintValidator<A, V> getInitializedConstraintValidator(ValidationContext<T> validationContext, ValueContext<?, V> valueContext) throws NegativeArraySizeException {
        Type validatedValueType = valueContext.getDeclaredTypeOfValidatedElement();
        ValidatedValueUnwrapper<?> validatedValueUnwrapper = validationContext.getValidatedValueUnwrapper(validatedValueType);
        if (valueContext.getUnwrapMode().equals(UnwrapMode.AUTOMATIC)) {
            return getConstraintValidatorInstanceForAutomaticUnwrapping(validationContext, valueContext);
        }
        if (valueContext.getUnwrapMode().equals(UnwrapMode.UNWRAP) || TYPE_USE.equals(valueContext.getElementType().name())) {
            return getInitializedValidatorInstanceForWrappedInstance(validationContext, valueContext, validatedValueType, validatedValueUnwrapper);
        }
        return getConstraintValidatorNoUnwrapping(validationContext, valueContext);
    }

    private <T, V> ConstraintValidator<A, V> getInitializedValidatorInstanceForWrappedInstance(ValidationContext<T> validationContext, ValueContext<?, V> valueContext, Type validatedValueType, ValidatedValueUnwrapper<V> validatedValueUnwrapper) {
        if (validatedValueUnwrapper == null) {
            throw log.getNoUnwrapperFoundForTypeException(valueContext.getDeclaredTypeOfValidatedElement().toString());
        }
        valueContext.setValidatedValueHandler(validatedValueUnwrapper);
        Type validatedValueType2 = validatedValueUnwrapper.getValidatedValueType(validatedValueType);
        ConstraintValidator<A, V> validator = validationContext.getConstraintValidatorManager().getInitializedValidator(validatedValueType2, this.descriptor, validationContext.getConstraintValidatorFactory());
        if (validator == null) {
            throwExceptionForNullValidator(validatedValueType2, valueContext.getPropertyPath().asString());
        }
        return validator;
    }

    private void throwExceptionForNullValidator(Type validatedValueType, String path) {
        if (this.descriptor.getConstraintType() == ConstraintDescriptorImpl.ConstraintType.CROSS_PARAMETER) {
            throw log.getValidatorForCrossParameterConstraintMustEitherValidateObjectOrObjectArrayException(this.descriptor.getAnnotationType().getName());
        }
        String className = validatedValueType.toString();
        if (validatedValueType instanceof Class) {
            Class<?> clazz = (Class) validatedValueType;
            if (clazz.isArray()) {
                className = clazz.getComponentType().toString() + "[]";
            } else {
                className = clazz.getName();
            }
        }
        throw log.getNoValidatorFoundForTypeException(this.descriptor.getAnnotationType().getName(), className, path);
    }

    private <T, V> ConstraintValidator<A, V> getConstraintValidatorInstanceForAutomaticUnwrapping(ValidationContext<T> validationContext, ValueContext<?, V> valueContext) throws NegativeArraySizeException {
        Type validatedValueType = valueContext.getDeclaredTypeOfValidatedElement();
        ValidatedValueUnwrapper<?> validatedValueUnwrapper = validationContext.getValidatedValueUnwrapper(validatedValueType);
        if (validatedValueUnwrapper == null) {
            return getConstraintValidatorNoUnwrapping(validationContext, valueContext);
        }
        ConstraintValidator<A, V> validatorForWrappedValue = validationContext.getConstraintValidatorManager().getInitializedValidator(validatedValueUnwrapper.getValidatedValueType(validatedValueType), this.descriptor, validationContext.getConstraintValidatorFactory());
        ConstraintValidator<A, V> validatorForWrapper = validationContext.getConstraintValidatorManager().getInitializedValidator(valueContext.getDeclaredTypeOfValidatedElement(), this.descriptor, validationContext.getConstraintValidatorFactory());
        if (validatorForWrappedValue != null && validatorForWrapper != null) {
            throw log.getConstraintValidatorExistsForWrapperAndWrappedValueException(valueContext.getPropertyPath().toString(), this.descriptor.getAnnotationType().getName(), validatedValueUnwrapper.getClass().getName());
        }
        if (validatorForWrappedValue == null && validatorForWrapper == null) {
            throw log.getNoValidatorFoundForTypeException(this.descriptor.getAnnotationType().getName(), validatedValueType.toString(), valueContext.getPropertyPath().toString());
        }
        if (validatorForWrappedValue != null) {
            valueContext.setValidatedValueHandler(validatedValueUnwrapper);
            return validatorForWrappedValue;
        }
        valueContext.setValidatedValueHandler(null);
        return validatorForWrapper;
    }

    private <T, V> ConstraintValidator<A, V> getConstraintValidatorNoUnwrapping(ValidationContext<T> validationContext, ValueContext<?, V> valueContext) {
        valueContext.setValidatedValueHandler(null);
        Type validatedValueType = valueContext.getDeclaredTypeOfValidatedElement();
        ConstraintValidator<A, V> validator = validationContext.getConstraintValidatorManager().getInitializedValidator(validatedValueType, this.descriptor, validationContext.getConstraintValidatorFactory());
        if (validator == null) {
            throwExceptionForNullValidator(validatedValueType, valueContext.getPropertyPath().asString());
        }
        return validator;
    }

    private <T> boolean mainConstraintNeedsEvaluation(ValidationContext<T> executionContext, Set<ConstraintViolation<T>> constraintViolations) {
        if (!this.descriptor.getComposingConstraints().isEmpty() && this.descriptor.getMatchingConstraintValidatorClasses().isEmpty()) {
            return false;
        }
        if (this.descriptor.isReportAsSingleViolation() && this.descriptor.getCompositionType() == CompositionType.AND && !constraintViolations.isEmpty()) {
            return false;
        }
        if (executionContext.isFailFastModeEnabled() && !constraintViolations.isEmpty()) {
            return false;
        }
        return true;
    }

    private <T> void prepareFinalConstraintViolations(ValidationContext<T> executionContext, ValueContext<?, ?> valueContext, Set<ConstraintViolation<T>> constraintViolations, Set<ConstraintViolation<T>> localViolations) {
        if (reportAsSingleViolation()) {
            constraintViolations.clear();
            if (localViolations.isEmpty()) {
                String message = (String) getDescriptor().getAttributes().get(ConstraintHelper.MESSAGE);
                ConstraintViolationCreationContext constraintViolationCreationContext = new ConstraintViolationCreationContext(message, valueContext.getPropertyPath());
                ConstraintViolation<T> violation = executionContext.createConstraintViolation(valueContext, constraintViolationCreationContext, this.descriptor);
                constraintViolations.add(violation);
            }
        }
        constraintViolations.addAll(localViolations);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <T> CompositionResult validateComposingConstraints(ValidationContext<T> executionContext, ValueContext<?, ?> valueContext, Set<ConstraintViolation<T>> constraintViolations) {
        CompositionResult compositionResult = new CompositionResult(true, false);
        List<ConstraintTree<?>> children = getChildren();
        for (ConstraintTree<?> tree : children) {
            Set<ConstraintViolation<T>> tmpViolations = CollectionHelper.newHashSet();
            tree.validateConstraints(executionContext, valueContext, tmpViolations);
            constraintViolations.addAll(tmpViolations);
            if (tmpViolations.isEmpty()) {
                compositionResult.setAtLeastOneTrue(true);
                if (this.descriptor.getCompositionType() == CompositionType.OR) {
                    break;
                }
            } else {
                compositionResult.setAllTrue(false);
                if (this.descriptor.getCompositionType() == CompositionType.AND && (executionContext.isFailFastModeEnabled() || this.descriptor.isReportAsSingleViolation())) {
                    break;
                }
            }
        }
        return compositionResult;
    }

    private boolean passesCompositionTypeRequirement(Set<?> constraintViolations, CompositionResult compositionResult) {
        CompositionType compositionType = getDescriptor().getCompositionType();
        boolean passedValidation = false;
        switch (compositionType) {
            case OR:
                passedValidation = compositionResult.isAtLeastOneTrue();
                break;
            case AND:
                passedValidation = compositionResult.isAllTrue();
                break;
            case ALL_FALSE:
                passedValidation = !compositionResult.isAtLeastOneTrue();
                break;
        }
        if (!$assertionsDisabled && passedValidation && compositionType == CompositionType.AND && !constraintViolations.isEmpty()) {
            throw new AssertionError();
        }
        if (passedValidation) {
            constraintViolations.clear();
        }
        return passedValidation;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <T, V> Set<ConstraintViolation<T>> validateSingleConstraint(ValidationContext<T> executionContext, ValueContext<?, ?> valueContext, ConstraintValidatorContextImpl constraintValidatorContext, ConstraintValidator<A, V> constraintValidator) {
        try {
            boolean isValid = constraintValidator.isValid(valueContext.getCurrentValidatedValue(), constraintValidatorContext);
            if (!isValid) {
                return executionContext.createConstraintViolations(valueContext, constraintValidatorContext);
            }
            return Collections.emptySet();
        } catch (RuntimeException e) {
            throw log.getExceptionDuringIsValidCallException(e);
        }
    }

    private boolean reportAsSingleViolation() {
        return getDescriptor().isReportAsSingleViolation() || getDescriptor().getCompositionType() == CompositionType.ALL_FALSE;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ConstraintTree");
        sb.append("{ descriptor=").append(this.descriptor);
        sb.append(", isRoot=").append(this.parent == null);
        sb.append('}');
        return sb.toString();
    }

    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/constraintvalidation/ConstraintTree$CompositionResult.class */
    private static final class CompositionResult {
        private boolean allTrue;
        private boolean atLeastOneTrue;

        CompositionResult(boolean allTrue, boolean atLeastOneTrue) {
            this.allTrue = allTrue;
            this.atLeastOneTrue = atLeastOneTrue;
        }

        public boolean isAllTrue() {
            return this.allTrue;
        }

        public boolean isAtLeastOneTrue() {
            return this.atLeastOneTrue;
        }

        public void setAllTrue(boolean allTrue) {
            this.allTrue = allTrue;
        }

        public void setAtLeastOneTrue(boolean atLeastOneTrue) {
            this.atLeastOneTrue = atLeastOneTrue;
        }
    }
}
