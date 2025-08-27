package org.hibernate.validator.internal.metadata.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import javax.validation.Constraint;
import javax.validation.ConstraintTarget;
import javax.validation.ConstraintValidator;
import javax.validation.ValidationException;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import org.hibernate.validator.constraints.ConstraintComposition;
import org.hibernate.validator.constraints.EAN;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.LuhnCheck;
import org.hibernate.validator.constraints.Mod10Check;
import org.hibernate.validator.constraints.Mod11Check;
import org.hibernate.validator.constraints.ModCheck;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.ScriptAssert;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import org.hibernate.validator.internal.constraintvalidators.bv.AssertFalseValidator;
import org.hibernate.validator.internal.constraintvalidators.bv.AssertTrueValidator;
import org.hibernate.validator.internal.constraintvalidators.bv.DecimalMaxValidatorForCharSequence;
import org.hibernate.validator.internal.constraintvalidators.bv.DecimalMaxValidatorForNumber;
import org.hibernate.validator.internal.constraintvalidators.bv.DecimalMinValidatorForCharSequence;
import org.hibernate.validator.internal.constraintvalidators.bv.DecimalMinValidatorForNumber;
import org.hibernate.validator.internal.constraintvalidators.bv.DigitsValidatorForCharSequence;
import org.hibernate.validator.internal.constraintvalidators.bv.DigitsValidatorForNumber;
import org.hibernate.validator.internal.constraintvalidators.bv.MaxValidatorForCharSequence;
import org.hibernate.validator.internal.constraintvalidators.bv.MaxValidatorForNumber;
import org.hibernate.validator.internal.constraintvalidators.bv.MinValidatorForCharSequence;
import org.hibernate.validator.internal.constraintvalidators.bv.MinValidatorForNumber;
import org.hibernate.validator.internal.constraintvalidators.bv.NotNullValidator;
import org.hibernate.validator.internal.constraintvalidators.bv.NullValidator;
import org.hibernate.validator.internal.constraintvalidators.bv.PatternValidator;
import org.hibernate.validator.internal.constraintvalidators.bv.future.FutureValidatorForCalendar;
import org.hibernate.validator.internal.constraintvalidators.bv.future.FutureValidatorForChronoZonedDateTime;
import org.hibernate.validator.internal.constraintvalidators.bv.future.FutureValidatorForDate;
import org.hibernate.validator.internal.constraintvalidators.bv.future.FutureValidatorForInstant;
import org.hibernate.validator.internal.constraintvalidators.bv.future.FutureValidatorForOffsetDateTime;
import org.hibernate.validator.internal.constraintvalidators.bv.future.FutureValidatorForReadableInstant;
import org.hibernate.validator.internal.constraintvalidators.bv.future.FutureValidatorForReadablePartial;
import org.hibernate.validator.internal.constraintvalidators.bv.past.PastValidatorForCalendar;
import org.hibernate.validator.internal.constraintvalidators.bv.past.PastValidatorForChronoZonedDateTime;
import org.hibernate.validator.internal.constraintvalidators.bv.past.PastValidatorForDate;
import org.hibernate.validator.internal.constraintvalidators.bv.past.PastValidatorForInstant;
import org.hibernate.validator.internal.constraintvalidators.bv.past.PastValidatorForOffsetDateTime;
import org.hibernate.validator.internal.constraintvalidators.bv.past.PastValidatorForReadableInstant;
import org.hibernate.validator.internal.constraintvalidators.bv.past.PastValidatorForReadablePartial;
import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForArray;
import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForArraysOfBoolean;
import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForArraysOfByte;
import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForArraysOfChar;
import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForArraysOfDouble;
import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForArraysOfFloat;
import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForArraysOfInt;
import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForArraysOfLong;
import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForCharSequence;
import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForCollection;
import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForMap;
import org.hibernate.validator.internal.constraintvalidators.hv.EANValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.LengthValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.LuhnCheckValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.Mod10CheckValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.Mod11CheckValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.ModCheckValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.NotBlankValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.ParameterScriptAssertValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.SafeHtmlValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.ScriptAssertValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.URLValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CNPJValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.Contracts;
import org.hibernate.validator.internal.util.Version;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.internal.util.logging.Messages;
import org.hibernate.validator.internal.util.privilegedactions.GetAnnotationParameter;
import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredMethods;
import org.hibernate.validator.internal.util.privilegedactions.GetMethod;
import org.hibernate.validator.internal.util.privilegedactions.LoadClass;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/core/ConstraintHelper.class */
public class ConstraintHelper {
    public static final String GROUPS = "groups";
    public static final String PAYLOAD = "payload";
    public static final String MESSAGE = "message";
    public static final String VALIDATION_APPLIES_TO = "validationAppliesTo";
    private static final Log log = LoggerFactory.make();
    private static final String JODA_TIME_CLASS_NAME = "org.joda.time.ReadableInstant";
    private final Map<Class<? extends Annotation>, List<? extends Class<?>>> builtinConstraints;
    private final ValidatorClassMap validatorClasses = new ValidatorClassMap();

    public ConstraintHelper() {
        Map<Class<? extends Annotation>, List<? extends Class<?>>> tmpConstraints = CollectionHelper.newHashMap();
        putConstraint(tmpConstraints, AssertFalse.class, AssertFalseValidator.class);
        putConstraint(tmpConstraints, AssertTrue.class, AssertTrueValidator.class);
        putConstraint(tmpConstraints, CNPJ.class, CNPJValidator.class);
        putConstraint(tmpConstraints, CPF.class, CPFValidator.class);
        putConstraints(tmpConstraints, DecimalMax.class, DecimalMaxValidatorForNumber.class, DecimalMaxValidatorForCharSequence.class);
        putConstraints(tmpConstraints, DecimalMin.class, DecimalMinValidatorForNumber.class, DecimalMinValidatorForCharSequence.class);
        putConstraints(tmpConstraints, Digits.class, DigitsValidatorForCharSequence.class, DigitsValidatorForNumber.class);
        List<Class<? extends ConstraintValidator<Future, ?>>> futureValidators = CollectionHelper.newArrayList(11);
        futureValidators.add(FutureValidatorForCalendar.class);
        futureValidators.add(FutureValidatorForDate.class);
        if (isJodaTimeInClasspath()) {
            futureValidators.add(FutureValidatorForReadableInstant.class);
            futureValidators.add(FutureValidatorForReadablePartial.class);
        }
        if (Version.getJavaRelease() >= 8) {
            futureValidators.add(FutureValidatorForChronoZonedDateTime.class);
            futureValidators.add(FutureValidatorForInstant.class);
            futureValidators.add(FutureValidatorForOffsetDateTime.class);
        }
        putConstraints(tmpConstraints, Future.class, futureValidators);
        putConstraints(tmpConstraints, Max.class, MaxValidatorForNumber.class, MaxValidatorForCharSequence.class);
        putConstraints(tmpConstraints, Min.class, MinValidatorForNumber.class, MinValidatorForCharSequence.class);
        putConstraint(tmpConstraints, NotNull.class, NotNullValidator.class);
        putConstraint(tmpConstraints, Null.class, NullValidator.class);
        List<Class<? extends ConstraintValidator<Past, ?>>> pastValidators = CollectionHelper.newArrayList(11);
        pastValidators.add(PastValidatorForCalendar.class);
        pastValidators.add(PastValidatorForDate.class);
        if (isJodaTimeInClasspath()) {
            pastValidators.add(PastValidatorForReadableInstant.class);
            pastValidators.add(PastValidatorForReadablePartial.class);
        }
        if (Version.getJavaRelease() >= 8) {
            pastValidators.add(PastValidatorForChronoZonedDateTime.class);
            pastValidators.add(PastValidatorForInstant.class);
            pastValidators.add(PastValidatorForOffsetDateTime.class);
        }
        putConstraints(tmpConstraints, Past.class, pastValidators);
        putConstraint(tmpConstraints, Pattern.class, PatternValidator.class);
        List<Class<? extends ConstraintValidator<Size, ?>>> sizeValidators = CollectionHelper.newArrayList(11);
        sizeValidators.add(SizeValidatorForCharSequence.class);
        sizeValidators.add(SizeValidatorForCollection.class);
        sizeValidators.add(SizeValidatorForArray.class);
        sizeValidators.add(SizeValidatorForMap.class);
        sizeValidators.add(SizeValidatorForArraysOfBoolean.class);
        sizeValidators.add(SizeValidatorForArraysOfByte.class);
        sizeValidators.add(SizeValidatorForArraysOfChar.class);
        sizeValidators.add(SizeValidatorForArraysOfDouble.class);
        sizeValidators.add(SizeValidatorForArraysOfFloat.class);
        sizeValidators.add(SizeValidatorForArraysOfInt.class);
        sizeValidators.add(SizeValidatorForArraysOfLong.class);
        putConstraints(tmpConstraints, Size.class, sizeValidators);
        putConstraint(tmpConstraints, EAN.class, EANValidator.class);
        putConstraint(tmpConstraints, Email.class, EmailValidator.class);
        putConstraint(tmpConstraints, Length.class, LengthValidator.class);
        putConstraint(tmpConstraints, ModCheck.class, ModCheckValidator.class);
        putConstraint(tmpConstraints, LuhnCheck.class, LuhnCheckValidator.class);
        putConstraint(tmpConstraints, Mod10Check.class, Mod10CheckValidator.class);
        putConstraint(tmpConstraints, Mod11Check.class, Mod11CheckValidator.class);
        putConstraint(tmpConstraints, NotBlank.class, NotBlankValidator.class);
        putConstraint(tmpConstraints, ParameterScriptAssert.class, ParameterScriptAssertValidator.class);
        putConstraint(tmpConstraints, SafeHtml.class, SafeHtmlValidator.class);
        putConstraint(tmpConstraints, ScriptAssert.class, ScriptAssertValidator.class);
        putConstraint(tmpConstraints, URL.class, URLValidator.class);
        this.builtinConstraints = Collections.unmodifiableMap(tmpConstraints);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <A extends Annotation> void putConstraint(Map<Class<? extends Annotation>, List<? extends Class<?>>> validators, Class<A> cls, Class<? extends ConstraintValidator<A, ?>> validatorType) {
        validators.put(cls, Collections.singletonList(validatorType));
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <A extends Annotation> void putConstraints(Map<Class<? extends Annotation>, List<? extends Class<?>>> validators, Class<A> cls, Class<? extends ConstraintValidator<A, ?>> validatorType1, Class<? extends ConstraintValidator<A, ?>> validatorType2) {
        validators.put(cls, Collections.unmodifiableList(Arrays.asList(validatorType1, validatorType2)));
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <A extends Annotation> void putConstraints(Map<Class<? extends Annotation>, List<? extends Class<?>>> validators, Class<A> cls, List<Class<? extends ConstraintValidator<A, ?>>> validatorTypes) {
        validators.put(cls, Collections.unmodifiableList(validatorTypes));
    }

    private boolean isBuiltinConstraint(Class<? extends Annotation> annotationType) {
        return this.builtinConstraints.containsKey(annotationType);
    }

    public <A extends Annotation> List<Class<? extends ConstraintValidator<A, ?>>> getAllValidatorClasses(Class<A> annotationType) {
        Contracts.assertNotNull(annotationType, Messages.MESSAGES.classCannotBeNull());
        List<Class<? extends ConstraintValidator<A, ?>>> classes = this.validatorClasses.get(annotationType);
        if (classes == null) {
            classes = getDefaultValidatorClasses(annotationType);
            List<Class<? extends ConstraintValidator<A, ?>>> cachedValidatorClasses = this.validatorClasses.putIfAbsent(annotationType, classes);
            if (cachedValidatorClasses != null) {
                classes = cachedValidatorClasses;
            }
        }
        return Collections.unmodifiableList(classes);
    }

    public <A extends Annotation> List<Class<? extends ConstraintValidator<A, ?>>> findValidatorClasses(Class<A> annotationType, ValidationTarget validationTarget) {
        List<Class<? extends ConstraintValidator<A, ?>>> validatorClasses = getAllValidatorClasses(annotationType);
        List<Class<? extends ConstraintValidator<A, ?>>> matchingValidatorClasses = CollectionHelper.newArrayList();
        for (Class<? extends ConstraintValidator<A, ?>> validatorClass : validatorClasses) {
            if (supportsValidationTarget(validatorClass, validationTarget)) {
                matchingValidatorClasses.add(validatorClass);
            }
        }
        return matchingValidatorClasses;
    }

    private boolean supportsValidationTarget(Class<? extends ConstraintValidator<?, ?>> validatorClass, ValidationTarget target) {
        SupportedValidationTarget supportedTargetAnnotation = (SupportedValidationTarget) validatorClass.getAnnotation(SupportedValidationTarget.class);
        if (supportedTargetAnnotation == null) {
            return target == ValidationTarget.ANNOTATED_ELEMENT;
        }
        return Arrays.asList(supportedTargetAnnotation.value()).contains(target);
    }

    public <A extends Annotation> void putValidatorClasses(Class<A> annotationType, List<Class<? extends ConstraintValidator<A, ?>>> definitionClasses, boolean keepExistingClasses) {
        List<Class<? extends ConstraintValidator<A, ?>>> existingClasses;
        if (keepExistingClasses && (existingClasses = getAllValidatorClasses(annotationType)) != null) {
            definitionClasses.addAll(0, existingClasses);
        }
        this.validatorClasses.put(annotationType, definitionClasses);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean isMultiValueConstraint(Class<? extends Annotation> annotationType) {
        boolean isMultiValueConstraint = false;
        Method method = (Method) run(GetMethod.action(annotationType, "value"));
        if (method != null) {
            Class<?> returnType = method.getReturnType();
            if (returnType.isArray() && returnType.getComponentType().isAnnotation()) {
                Class<?> componentType = returnType.getComponentType();
                isMultiValueConstraint = isConstraintAnnotation(componentType) || isBuiltinConstraint(componentType);
            }
        }
        return isMultiValueConstraint;
    }

    public <A extends Annotation> List<Annotation> getConstraintsFromMultiValueConstraint(A multiValueConstraint) {
        Annotation[] annotations = (Annotation[]) run(GetAnnotationParameter.action(multiValueConstraint, "value", Annotation[].class));
        return Arrays.asList(annotations);
    }

    public boolean isConstraintAnnotation(Class<? extends Annotation> annotationType) {
        if (annotationType.getAnnotation(Constraint.class) == null) {
            return false;
        }
        assertMessageParameterExists(annotationType);
        assertGroupsParameterExists(annotationType);
        assertPayloadParameterExists(annotationType);
        assertValidationAppliesToParameterSetUpCorrectly(annotationType);
        assertNoParameterStartsWithValid(annotationType);
        return true;
    }

    private void assertNoParameterStartsWithValid(Class<? extends Annotation> annotationType) {
        Method[] methods = (Method[]) run(GetDeclaredMethods.action(annotationType));
        for (Method m : methods) {
            if (m.getName().startsWith("valid") && !m.getName().equals(VALIDATION_APPLIES_TO)) {
                throw log.getConstraintParametersCannotStartWithValidException();
            }
        }
    }

    private void assertPayloadParameterExists(Class<? extends Annotation> annotationType) {
        try {
            Method method = (Method) run(GetMethod.action(annotationType, PAYLOAD));
            if (method == null) {
                throw log.getConstraintWithoutMandatoryParameterException(PAYLOAD, annotationType.getName());
            }
            Class<?>[] defaultPayload = (Class[]) method.getDefaultValue();
            if (defaultPayload.length != 0) {
                throw log.getWrongDefaultValueForPayloadParameterException(annotationType.getName());
            }
        } catch (ClassCastException e) {
            throw log.getWrongTypeForPayloadParameterException(annotationType.getName(), e);
        }
    }

    private void assertGroupsParameterExists(Class<? extends Annotation> annotationType) {
        try {
            Method method = (Method) run(GetMethod.action(annotationType, GROUPS));
            if (method == null) {
                throw log.getConstraintWithoutMandatoryParameterException(GROUPS, annotationType.getName());
            }
            Class<?>[] defaultGroups = (Class[]) method.getDefaultValue();
            if (defaultGroups.length != 0) {
                throw log.getWrongDefaultValueForGroupsParameterException(annotationType.getName());
            }
        } catch (ClassCastException e) {
            throw log.getWrongTypeForGroupsParameterException(annotationType.getName(), e);
        }
    }

    private void assertMessageParameterExists(Class<? extends Annotation> annotationType) {
        Method method = (Method) run(GetMethod.action(annotationType, MESSAGE));
        if (method == null) {
            throw log.getConstraintWithoutMandatoryParameterException(MESSAGE, annotationType.getName());
        }
        if (method.getReturnType() != String.class) {
            throw log.getWrongTypeForMessageParameterException(annotationType.getName());
        }
    }

    private void assertValidationAppliesToParameterSetUpCorrectly(Class<? extends Annotation> annotationType) {
        boolean hasGenericValidators = !findValidatorClasses(annotationType, ValidationTarget.ANNOTATED_ELEMENT).isEmpty();
        boolean hasCrossParameterValidator = !findValidatorClasses(annotationType, ValidationTarget.PARAMETERS).isEmpty();
        Method method = (Method) run(GetMethod.action(annotationType, VALIDATION_APPLIES_TO));
        if (hasGenericValidators && hasCrossParameterValidator) {
            if (method == null) {
                throw log.getGenericAndCrossParameterConstraintDoesNotDefineValidationAppliesToParameterException(annotationType.getName());
            }
            if (method.getReturnType() != ConstraintTarget.class) {
                throw log.getValidationAppliesToParameterMustHaveReturnTypeConstraintTargetException(annotationType.getName());
            }
            ConstraintTarget defaultValue = (ConstraintTarget) method.getDefaultValue();
            if (defaultValue != ConstraintTarget.IMPLICIT) {
                throw log.getValidationAppliesToParameterMustHaveDefaultValueImplicitException(annotationType.getName());
            }
            return;
        }
        if (method != null) {
            throw log.getValidationAppliesToParameterMustNotBeDefinedForNonGenericAndCrossParameterConstraintException(annotationType.getName());
        }
    }

    public boolean isConstraintComposition(Class<? extends Annotation> annotationType) {
        return annotationType == ConstraintComposition.class;
    }

    private static boolean isJodaTimeInClasspath() {
        return isClassPresent(JODA_TIME_CLASS_NAME);
    }

    private <A extends Annotation> List<Class<? extends ConstraintValidator<A, ?>>> getDefaultValidatorClasses(Class<A> annotationType) {
        List<Class<? extends ConstraintValidator<A, ?>>> builtInValidators = (List) this.builtinConstraints.get(annotationType);
        if (builtInValidators != null) {
            return builtInValidators;
        }
        return Arrays.asList(((Constraint) annotationType.getAnnotation(Constraint.class)).validatedBy());
    }

    private static boolean isClassPresent(String className) {
        try {
            run(LoadClass.action(className, ConstraintHelper.class.getClassLoader()));
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }

    private static <T> T run(PrivilegedAction<T> privilegedAction) {
        return System.getSecurityManager() != null ? (T) AccessController.doPrivileged(privilegedAction) : privilegedAction.run();
    }

    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/core/ConstraintHelper$ValidatorClassMap.class */
    private static class ValidatorClassMap {
        private final ConcurrentMap<Class<? extends Annotation>, List<? extends Class<?>>> constraintValidatorClasses;

        private ValidatorClassMap() {
            this.constraintValidatorClasses = CollectionHelper.newConcurrentHashMap();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public <A extends Annotation> List<Class<? extends ConstraintValidator<A, ?>>> get(Class<A> annotationType) {
            return (List) this.constraintValidatorClasses.get(annotationType);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public <A extends Annotation> void put(Class<A> annotationType, List<Class<? extends ConstraintValidator<A, ?>>> validatorClasses) {
            this.constraintValidatorClasses.put(annotationType, validatorClasses);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public <A extends Annotation> List<Class<? extends ConstraintValidator<A, ?>>> putIfAbsent(Class<A> annotationType, List<Class<? extends ConstraintValidator<A, ?>>> classes) {
            return (List) this.constraintValidatorClasses.putIfAbsent(annotationType, classes);
        }
    }
}
