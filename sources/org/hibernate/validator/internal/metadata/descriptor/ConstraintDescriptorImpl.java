package org.hibernate.validator.internal.metadata.descriptor;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.Constraint;
import javax.validation.ConstraintTarget;
import javax.validation.ConstraintValidator;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.ValidationException;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import javax.validation.groups.Default;
import javax.validation.metadata.ConstraintDescriptor;
import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.core.ConstraintOrigin;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.TypeHelper;
import org.hibernate.validator.internal.util.annotationfactory.AnnotationDescriptor;
import org.hibernate.validator.internal.util.annotationfactory.AnnotationFactory;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.internal.util.privilegedactions.GetAnnotationParameter;
import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredMethods;
import org.hibernate.validator.internal.util.privilegedactions.GetMethod;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/descriptor/ConstraintDescriptorImpl.class */
public class ConstraintDescriptorImpl<T extends Annotation> implements ConstraintDescriptor<T>, Serializable {
    private static final long serialVersionUID = -2563102960314069246L;
    private static final int OVERRIDES_PARAMETER_DEFAULT_INDEX = -1;
    private final T annotation;
    private final Class<T> annotationType;
    private final List<Class<? extends ConstraintValidator<T, ?>>> constraintValidatorClasses;
    private final List<Class<? extends ConstraintValidator<T, ?>>> matchingConstraintValidatorClasses;
    private final Set<Class<?>> groups;
    private final Map<String, Object> attributes;
    private final Set<Class<? extends Payload>> payloads;
    private final Set<ConstraintDescriptorImpl<?>> composingConstraints;
    private final boolean isReportAsSingleInvalidConstraint;
    private final ElementType elementType;
    private final ConstraintOrigin definedOn;
    private final ConstraintType constraintType;
    private final CompositionType compositionType;
    private final int hashCode;
    private static final Log log = LoggerFactory.make();
    private static final List<String> NON_COMPOSING_CONSTRAINT_ANNOTATIONS = Arrays.asList(Documented.class.getName(), Retention.class.getName(), Target.class.getName(), Constraint.class.getName(), ReportAsSingleViolation.class.getName());

    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/descriptor/ConstraintDescriptorImpl$ConstraintType.class */
    public enum ConstraintType {
        GENERIC,
        CROSS_PARAMETER
    }

    public ConstraintDescriptorImpl(ConstraintHelper constraintHelper, Member member, T t, ElementType elementType, Class<?> cls, ConstraintOrigin constraintOrigin, ConstraintType constraintType) {
        this.annotation = t;
        this.annotationType = (Class<T>) this.annotation.annotationType();
        this.elementType = elementType;
        this.definedOn = constraintOrigin;
        this.isReportAsSingleInvalidConstraint = this.annotationType.isAnnotationPresent(ReportAsSingleViolation.class);
        this.attributes = buildAnnotationParameterMap(t);
        this.groups = buildGroupSet(cls);
        this.payloads = buildPayloadSet(t);
        this.constraintValidatorClasses = constraintHelper.getAllValidatorClasses(this.annotationType);
        List listFindValidatorClasses = constraintHelper.findValidatorClasses(this.annotationType, ValidationTarget.PARAMETERS);
        List listFindValidatorClasses2 = constraintHelper.findValidatorClasses(this.annotationType, ValidationTarget.ANNOTATED_ELEMENT);
        if (listFindValidatorClasses.size() > 1) {
            throw log.getMultipleCrossParameterValidatorClassesException(this.annotationType.getName());
        }
        this.constraintType = determineConstraintType(t.annotationType(), member, elementType, !listFindValidatorClasses2.isEmpty(), !listFindValidatorClasses.isEmpty(), constraintType);
        this.composingConstraints = parseComposingConstraints(member, constraintHelper, this.constraintType);
        this.compositionType = parseCompositionType(constraintHelper);
        validateComposingConstraintTypes();
        if (this.constraintType == ConstraintType.GENERIC) {
            this.matchingConstraintValidatorClasses = Collections.unmodifiableList(listFindValidatorClasses2);
        } else {
            this.matchingConstraintValidatorClasses = Collections.unmodifiableList(listFindValidatorClasses);
        }
        this.hashCode = t.hashCode();
    }

    public ConstraintDescriptorImpl(ConstraintHelper constraintHelper, Member member, T annotation, ElementType type) {
        this(constraintHelper, member, annotation, type, null, ConstraintOrigin.DEFINED_LOCALLY, null);
    }

    public ConstraintDescriptorImpl(ConstraintHelper constraintHelper, Member member, T annotation, ElementType type, ConstraintType constraintType) {
        this(constraintHelper, member, annotation, type, null, ConstraintOrigin.DEFINED_LOCALLY, constraintType);
    }

    @Override // javax.validation.metadata.ConstraintDescriptor
    public T getAnnotation() {
        return this.annotation;
    }

    public Class<T> getAnnotationType() {
        return this.annotationType;
    }

    @Override // javax.validation.metadata.ConstraintDescriptor
    public String getMessageTemplate() {
        return (String) getAttributes().get(ConstraintHelper.MESSAGE);
    }

    @Override // javax.validation.metadata.ConstraintDescriptor
    public Set<Class<?>> getGroups() {
        return this.groups;
    }

    @Override // javax.validation.metadata.ConstraintDescriptor
    public Set<Class<? extends Payload>> getPayload() {
        return this.payloads;
    }

    @Override // javax.validation.metadata.ConstraintDescriptor
    public ConstraintTarget getValidationAppliesTo() {
        return (ConstraintTarget) this.attributes.get(ConstraintHelper.VALIDATION_APPLIES_TO);
    }

    @Override // javax.validation.metadata.ConstraintDescriptor
    public List<Class<? extends ConstraintValidator<T, ?>>> getConstraintValidatorClasses() {
        return this.constraintValidatorClasses;
    }

    public List<Class<? extends ConstraintValidator<T, ?>>> getMatchingConstraintValidatorClasses() {
        return this.matchingConstraintValidatorClasses;
    }

    public Map<Type, Class<? extends ConstraintValidator<T, ?>>> getAvailableValidatorTypes() {
        Map<Type, Class<? extends ConstraintValidator<T, ?>>> availableValidatorTypes = TypeHelper.getValidatorsTypes(getAnnotationType(), getMatchingConstraintValidatorClasses());
        return availableValidatorTypes;
    }

    @Override // javax.validation.metadata.ConstraintDescriptor
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override // javax.validation.metadata.ConstraintDescriptor
    public Set<ConstraintDescriptor<?>> getComposingConstraints() {
        return Collections.unmodifiableSet(this.composingConstraints);
    }

    public Set<ConstraintDescriptorImpl<?>> getComposingConstraintImpls() {
        return this.composingConstraints;
    }

    @Override // javax.validation.metadata.ConstraintDescriptor
    public boolean isReportAsSingleViolation() {
        return this.isReportAsSingleInvalidConstraint;
    }

    public ElementType getElementType() {
        return this.elementType;
    }

    public ConstraintOrigin getDefinedOn() {
        return this.definedOn;
    }

    public ConstraintType getConstraintType() {
        return this.constraintType;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConstraintDescriptorImpl<?> that = (ConstraintDescriptorImpl) o;
        if (this.annotation != null) {
            if (!this.annotation.equals(that.annotation)) {
                return false;
            }
            return true;
        }
        if (that.annotation != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.hashCode;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ConstraintDescriptorImpl");
        sb.append("{annotation=").append(this.annotationType.getName());
        sb.append(", payloads=").append(this.payloads);
        sb.append(", hasComposingConstraints=").append(this.composingConstraints.isEmpty());
        sb.append(", isReportAsSingleInvalidConstraint=").append(this.isReportAsSingleInvalidConstraint);
        sb.append(", elementType=").append(this.elementType);
        sb.append(", definedOn=").append(this.definedOn);
        sb.append(", groups=").append(this.groups);
        sb.append(", attributes=").append(this.attributes);
        sb.append(", constraintType=").append(this.constraintType);
        sb.append('}');
        return sb.toString();
    }

    private ConstraintType determineConstraintType(Class<? extends Annotation> constraintAnnotationType, Member member, ElementType elementType, boolean hasGenericValidators, boolean hasCrossParameterValidator, ConstraintType externalConstraintType) {
        ConstraintTarget constraintTarget = (ConstraintTarget) this.attributes.get(ConstraintHelper.VALIDATION_APPLIES_TO);
        ConstraintType constraintType = null;
        boolean isExecutable = isExecutable(elementType);
        if (constraintTarget == ConstraintTarget.RETURN_VALUE) {
            if (!isExecutable) {
                throw log.getParametersOrReturnValueConstraintTargetGivenAtNonExecutableException(this.annotationType.getName(), ConstraintTarget.RETURN_VALUE);
            }
            constraintType = ConstraintType.GENERIC;
        } else if (constraintTarget == ConstraintTarget.PARAMETERS) {
            if (!isExecutable) {
                throw log.getParametersOrReturnValueConstraintTargetGivenAtNonExecutableException(this.annotationType.getName(), ConstraintTarget.PARAMETERS);
            }
            constraintType = ConstraintType.CROSS_PARAMETER;
        } else if (externalConstraintType != null) {
            constraintType = externalConstraintType;
        } else if (hasGenericValidators && !hasCrossParameterValidator) {
            constraintType = ConstraintType.GENERIC;
        } else if (!hasGenericValidators && hasCrossParameterValidator) {
            constraintType = ConstraintType.CROSS_PARAMETER;
        } else if (!isExecutable) {
            constraintType = ConstraintType.GENERIC;
        } else if (constraintAnnotationType.isAnnotationPresent(SupportedValidationTarget.class)) {
            SupportedValidationTarget supportedValidationTarget = (SupportedValidationTarget) constraintAnnotationType.getAnnotation(SupportedValidationTarget.class);
            if (supportedValidationTarget.value().length == 1) {
                constraintType = supportedValidationTarget.value()[0] == ValidationTarget.ANNOTATED_ELEMENT ? ConstraintType.GENERIC : ConstraintType.CROSS_PARAMETER;
            }
        } else {
            boolean hasParameters = hasParameters(member);
            boolean hasReturnValue = hasReturnValue(member);
            if (!hasParameters && hasReturnValue) {
                constraintType = ConstraintType.GENERIC;
            } else if (hasParameters && !hasReturnValue) {
                constraintType = ConstraintType.CROSS_PARAMETER;
            }
        }
        if (constraintType == null) {
            throw log.getImplicitConstraintTargetInAmbiguousConfigurationException(this.annotationType.getName());
        }
        if (constraintType == ConstraintType.CROSS_PARAMETER) {
            validateCrossParameterConstraintType(member, hasCrossParameterValidator);
        }
        return constraintType;
    }

    private void validateCrossParameterConstraintType(Member member, boolean hasCrossParameterValidator) {
        if (!hasCrossParameterValidator) {
            throw log.getCrossParameterConstraintHasNoValidatorException(this.annotationType.getName());
        }
        if (member == null) {
            throw log.getCrossParameterConstraintOnClassException(this.annotationType.getName());
        }
        if (member instanceof Field) {
            throw log.getCrossParameterConstraintOnFieldException(this.annotationType.getName(), member.toString());
        }
        if (!hasParameters(member)) {
            throw log.getCrossParameterConstraintOnMethodWithoutParametersException(this.annotationType.getName(), member.toString());
        }
    }

    private void validateComposingConstraintTypes() {
        for (ConstraintDescriptorImpl<?> composingConstraint : this.composingConstraints) {
            if (composingConstraint.constraintType != this.constraintType) {
                throw log.getComposedAndComposingConstraintsHaveDifferentTypesException(this.annotationType.getName(), composingConstraint.annotationType.getName(), this.constraintType, composingConstraint.constraintType);
            }
        }
    }

    private boolean hasParameters(Member member) {
        boolean hasParameters = false;
        if (member instanceof Constructor) {
            Constructor<?> constructor = (Constructor) member;
            hasParameters = constructor.getParameterTypes().length > 0;
        } else if (member instanceof Method) {
            Method method = (Method) member;
            hasParameters = method.getParameterTypes().length > 0;
        }
        return hasParameters;
    }

    private boolean hasReturnValue(Member member) {
        boolean hasReturnValue;
        if (member instanceof Constructor) {
            hasReturnValue = true;
        } else if (member instanceof Method) {
            Method method = (Method) member;
            hasReturnValue = method.getGenericReturnType() != Void.TYPE;
        } else {
            hasReturnValue = false;
        }
        return hasReturnValue;
    }

    private boolean isExecutable(ElementType elementType) {
        return elementType == ElementType.METHOD || elementType == ElementType.CONSTRUCTOR;
    }

    private Set<Class<? extends Payload>> buildPayloadSet(T annotation) {
        Class<Payload>[] payloadFromAnnotation;
        Set<Class<? extends Payload>> payloadSet = CollectionHelper.newHashSet();
        try {
            payloadFromAnnotation = (Class[]) run(GetAnnotationParameter.action(annotation, ConstraintHelper.PAYLOAD, Class[].class));
        } catch (ValidationException e) {
            payloadFromAnnotation = null;
        }
        if (payloadFromAnnotation != null) {
            payloadSet.addAll(Arrays.asList(payloadFromAnnotation));
        }
        return Collections.unmodifiableSet(payloadSet);
    }

    private Set<Class<?>> buildGroupSet(Class<?> implicitGroup) {
        Set<Class<?>> groupSet = CollectionHelper.newHashSet();
        Class<?>[] groupsFromAnnotation = (Class[]) run(GetAnnotationParameter.action(this.annotation, ConstraintHelper.GROUPS, Class[].class));
        if (groupsFromAnnotation.length == 0) {
            groupSet.add(Default.class);
        } else {
            groupSet.addAll(Arrays.asList(groupsFromAnnotation));
        }
        if (implicitGroup != null && groupSet.contains(Default.class)) {
            groupSet.add(implicitGroup);
        }
        return Collections.unmodifiableSet(groupSet);
    }

    private Map<String, Object> buildAnnotationParameterMap(Annotation annotation) {
        Method[] declaredMethods = (Method[]) run(GetDeclaredMethods.action(annotation.annotationType()));
        Map<String, Object> parameters = CollectionHelper.newHashMap(declaredMethods.length);
        for (Method m : declaredMethods) {
            Object value = run(GetAnnotationParameter.action(annotation, m.getName(), Object.class));
            parameters.put(m.getName(), value);
        }
        return Collections.unmodifiableMap(parameters);
    }

    private Map<ConstraintDescriptorImpl<T>.ClassIndexWrapper, Map<String, Object>> parseOverrideParameters() {
        HashMap mapNewHashMap = CollectionHelper.newHashMap();
        Method[] methods = (Method[]) run(GetDeclaredMethods.action(this.annotationType));
        for (Method m : methods) {
            if (m.getAnnotation(OverridesAttribute.class) != null) {
                addOverrideAttributes(mapNewHashMap, m, (OverridesAttribute) m.getAnnotation(OverridesAttribute.class));
            } else if (m.getAnnotation(OverridesAttribute.List.class) != null) {
                addOverrideAttributes(mapNewHashMap, m, ((OverridesAttribute.List) m.getAnnotation(OverridesAttribute.List.class)).value());
            }
        }
        return mapNewHashMap;
    }

    private void addOverrideAttributes(Map<ConstraintDescriptorImpl<T>.ClassIndexWrapper, Map<String, Object>> overrideParameters, Method m, OverridesAttribute... attributes) {
        Object value = run(GetAnnotationParameter.action(this.annotation, m.getName(), Object.class));
        for (OverridesAttribute overridesAttribute : attributes) {
            ensureAttributeIsOverridable(m, overridesAttribute);
            ConstraintDescriptorImpl<T>.ClassIndexWrapper wrapper = new ClassIndexWrapper(overridesAttribute.constraint(), overridesAttribute.constraintIndex());
            Map<String, Object> map = overrideParameters.get(wrapper);
            if (map == null) {
                map = CollectionHelper.newHashMap();
                overrideParameters.put(wrapper, map);
            }
            map.put(overridesAttribute.name(), value);
        }
    }

    private void ensureAttributeIsOverridable(Method m, OverridesAttribute overridesAttribute) {
        Method method = (Method) run(GetMethod.action(overridesAttribute.constraint(), overridesAttribute.name()));
        if (method == null) {
            throw log.getOverriddenConstraintAttributeNotFoundException(overridesAttribute.name());
        }
        Class<?> returnTypeOfOverriddenConstraint = method.getReturnType();
        if (!returnTypeOfOverriddenConstraint.equals(m.getReturnType())) {
            throw log.getWrongAttributeTypeForOverriddenConstraintException(returnTypeOfOverriddenConstraint.getName(), m.getReturnType());
        }
    }

    private Set<ConstraintDescriptorImpl<?>> parseComposingConstraints(Member member, ConstraintHelper constraintHelper, ConstraintType constraintType) {
        HashSet hashSetNewHashSet = CollectionHelper.newHashSet();
        Map<ConstraintDescriptorImpl<T>.ClassIndexWrapper, Map<String, Object>> overrideParameters = parseOverrideParameters();
        for (Annotation declaredAnnotation : this.annotationType.getDeclaredAnnotations()) {
            Class<? extends Annotation> declaredAnnotationType = declaredAnnotation.annotationType();
            if (!NON_COMPOSING_CONSTRAINT_ANNOTATIONS.contains(declaredAnnotationType.getName())) {
                if (constraintHelper.isConstraintAnnotation(declaredAnnotationType)) {
                    Object objCreateComposingConstraintDescriptor = createComposingConstraintDescriptor(member, overrideParameters, -1, declaredAnnotation, constraintType, constraintHelper);
                    hashSetNewHashSet.add(objCreateComposingConstraintDescriptor);
                    log.debugf("Adding composing constraint: %s.", objCreateComposingConstraintDescriptor);
                } else if (constraintHelper.isMultiValueConstraint(declaredAnnotationType)) {
                    List<Annotation> multiValueConstraints = constraintHelper.getConstraintsFromMultiValueConstraint(declaredAnnotation);
                    int index = 0;
                    for (Annotation constraintAnnotation : multiValueConstraints) {
                        Object objCreateComposingConstraintDescriptor2 = createComposingConstraintDescriptor(member, overrideParameters, index, constraintAnnotation, constraintType, constraintHelper);
                        hashSetNewHashSet.add(objCreateComposingConstraintDescriptor2);
                        log.debugf("Adding composing constraint: %s.", objCreateComposingConstraintDescriptor2);
                        index++;
                    }
                }
            }
        }
        return Collections.unmodifiableSet(hashSetNewHashSet);
    }

    private CompositionType parseCompositionType(ConstraintHelper constraintHelper) {
        for (Annotation declaredAnnotation : this.annotationType.getDeclaredAnnotations()) {
            Class<? extends Annotation> declaredAnnotationType = declaredAnnotation.annotationType();
            if (!NON_COMPOSING_CONSTRAINT_ANNOTATIONS.contains(declaredAnnotationType.getName()) && constraintHelper.isConstraintComposition(declaredAnnotationType)) {
                if (log.isDebugEnabled()) {
                    log.debugf("Adding Bool %s.", declaredAnnotationType.getName());
                }
                return ((ConstraintComposition) declaredAnnotation).value();
            }
        }
        return CompositionType.AND;
    }

    private <U extends Annotation> ConstraintDescriptorImpl<U> createComposingConstraintDescriptor(Member member, Map<ConstraintDescriptorImpl<T>.ClassIndexWrapper, Map<String, Object>> overrideParameters, int index, U constraintAnnotation, ConstraintType constraintType, ConstraintHelper constraintHelper) {
        Class<? extends Annotation> clsAnnotationType = constraintAnnotation.annotationType();
        AnnotationDescriptor<U> annotationDescriptor = new AnnotationDescriptor<>(clsAnnotationType, buildAnnotationParameterMap(constraintAnnotation));
        Map<String, Object> overrides = overrideParameters.get(new ClassIndexWrapper(clsAnnotationType, index));
        if (overrides != null) {
            for (Map.Entry<String, Object> entry : overrides.entrySet()) {
                annotationDescriptor.setValue(entry.getKey(), entry.getValue());
            }
        }
        annotationDescriptor.setValue(ConstraintHelper.GROUPS, this.groups.toArray(new Class[this.groups.size()]));
        annotationDescriptor.setValue(ConstraintHelper.PAYLOAD, this.payloads.toArray(new Class[this.payloads.size()]));
        if (annotationDescriptor.getElements().containsKey(ConstraintHelper.VALIDATION_APPLIES_TO)) {
            ConstraintTarget validationAppliesTo = getValidationAppliesTo();
            if (validationAppliesTo == null) {
                if (constraintType == ConstraintType.CROSS_PARAMETER) {
                    validationAppliesTo = ConstraintTarget.PARAMETERS;
                } else {
                    validationAppliesTo = ConstraintTarget.IMPLICIT;
                }
            }
            annotationDescriptor.setValue(ConstraintHelper.VALIDATION_APPLIES_TO, validationAppliesTo);
        }
        return new ConstraintDescriptorImpl<>(constraintHelper, member, AnnotationFactory.create(annotationDescriptor), this.elementType, null, this.definedOn, constraintType);
    }

    private <P> P run(PrivilegedAction<P> privilegedAction) {
        return System.getSecurityManager() != null ? (P) AccessController.doPrivileged(privilegedAction) : privilegedAction.run();
    }

    public CompositionType getCompositionType() {
        return this.compositionType;
    }

    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/descriptor/ConstraintDescriptorImpl$ClassIndexWrapper.class */
    private class ClassIndexWrapper {
        final Class<?> clazz;
        final int index;

        ClassIndexWrapper(Class<?> clazz, int index) {
            this.clazz = clazz;
            this.index = index;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ConstraintDescriptorImpl<T>.ClassIndexWrapper that = (ClassIndexWrapper) o;
            if (this.index != that.index) {
                return false;
            }
            if (this.clazz != null && !this.clazz.equals(that.clazz)) {
                return false;
            }
            if (this.clazz == null && that.clazz != null) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            int result = this.clazz != null ? this.clazz.hashCode() : 0;
            return (31 * result) + this.index;
        }
    }
}
