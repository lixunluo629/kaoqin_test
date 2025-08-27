package org.hibernate.validator.internal.metadata.provider;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.GroupSequence;
import javax.validation.ParameterNameProvider;
import javax.validation.Valid;
import javax.validation.groups.ConvertGroup;
import org.hibernate.validator.group.GroupSequenceProvider;
import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptions;
import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptionsImpl;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
import org.hibernate.validator.internal.metadata.raw.BeanConfiguration;
import org.hibernate.validator.internal.metadata.raw.ConfigurationSource;
import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;
import org.hibernate.validator.internal.metadata.raw.ConstrainedExecutable;
import org.hibernate.validator.internal.metadata.raw.ConstrainedField;
import org.hibernate.validator.internal.metadata.raw.ConstrainedParameter;
import org.hibernate.validator.internal.metadata.raw.ConstrainedType;
import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.ConcurrentReferenceHashMap;
import org.hibernate.validator.internal.util.ReflectionHelper;
import org.hibernate.validator.internal.util.classhierarchy.ClassHierarchyHelper;
import org.hibernate.validator.internal.util.classhierarchy.Filter;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredConstructors;
import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredFields;
import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredMethods;
import org.hibernate.validator.internal.util.privilegedactions.GetMethods;
import org.hibernate.validator.internal.util.privilegedactions.NewInstance;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/provider/AnnotationMetaDataProvider.class */
public class AnnotationMetaDataProvider implements MetaDataProvider {
    private static final Log log = LoggerFactory.make();
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    protected final ConstraintHelper constraintHelper;
    protected final ConcurrentReferenceHashMap<Class<?>, BeanConfiguration<?>> configuredBeans = new ConcurrentReferenceHashMap<>(16, ConcurrentReferenceHashMap.ReferenceType.SOFT, ConcurrentReferenceHashMap.ReferenceType.SOFT);
    protected final AnnotationProcessingOptions annotationProcessingOptions;
    protected final ParameterNameProvider parameterNameProvider;

    public AnnotationMetaDataProvider(ConstraintHelper constraintHelper, ParameterNameProvider parameterNameProvider, AnnotationProcessingOptions annotationProcessingOptions) {
        this.constraintHelper = constraintHelper;
        this.parameterNameProvider = parameterNameProvider;
        this.annotationProcessingOptions = annotationProcessingOptions;
    }

    @Override // org.hibernate.validator.internal.metadata.provider.MetaDataProvider
    public AnnotationProcessingOptions getAnnotationProcessingOptions() {
        return new AnnotationProcessingOptionsImpl();
    }

    @Override // org.hibernate.validator.internal.metadata.provider.MetaDataProvider
    public <T> List<BeanConfiguration<? super T>> getBeanConfigurationForHierarchy(Class<T> beanClass) {
        List<BeanConfiguration<? super T>> configurations = CollectionHelper.newArrayList();
        Iterator it = ClassHierarchyHelper.getHierarchy(beanClass, new Filter[0]).iterator();
        while (it.hasNext()) {
            BeanConfiguration<T> beanConfiguration = getBeanConfiguration((Class) it.next());
            if (beanConfiguration != null) {
                configurations.add(beanConfiguration);
            }
        }
        return configurations;
    }

    private <T> BeanConfiguration<T> getBeanConfiguration(Class<T> beanClass) {
        BeanConfiguration<T> configuration = (BeanConfiguration) this.configuredBeans.get(beanClass);
        if (configuration != null) {
            return configuration;
        }
        BeanConfiguration<T> configuration2 = retrieveBeanConfiguration(beanClass);
        this.configuredBeans.put(beanClass, configuration2);
        return configuration2;
    }

    private <T> BeanConfiguration<T> retrieveBeanConfiguration(Class<T> beanClass) {
        Set<ConstrainedElement> constrainedElements = getFieldMetaData(beanClass);
        constrainedElements.addAll(getMethodMetaData(beanClass));
        constrainedElements.addAll(getConstructorMetaData(beanClass));
        Set<MetaConstraint<?>> classLevelConstraints = getClassLevelConstraints(beanClass);
        if (!classLevelConstraints.isEmpty()) {
            ConstrainedType classLevelMetaData = new ConstrainedType(ConfigurationSource.ANNOTATION, ConstraintLocation.forClass(beanClass), classLevelConstraints);
            constrainedElements.add(classLevelMetaData);
        }
        return new BeanConfiguration<>(ConfigurationSource.ANNOTATION, beanClass, constrainedElements, getDefaultGroupSequence(beanClass), getDefaultGroupSequenceProvider(beanClass));
    }

    private List<Class<?>> getDefaultGroupSequence(Class<?> beanClass) {
        GroupSequence groupSequenceAnnotation = (GroupSequence) beanClass.getAnnotation(GroupSequence.class);
        if (groupSequenceAnnotation != null) {
            return Arrays.asList(groupSequenceAnnotation.value());
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <T> DefaultGroupSequenceProvider<? super T> getDefaultGroupSequenceProvider(Class<T> beanClass) {
        GroupSequenceProvider groupSequenceProviderAnnotation = (GroupSequenceProvider) beanClass.getAnnotation(GroupSequenceProvider.class);
        if (groupSequenceProviderAnnotation != null) {
            return newGroupSequenceProviderClassInstance(beanClass, groupSequenceProviderAnnotation.value());
        }
        return null;
    }

    private <T> DefaultGroupSequenceProvider<? super T> newGroupSequenceProviderClassInstance(Class<T> beanClass, Class<? extends DefaultGroupSequenceProvider<? super T>> providerClass) {
        Method[] providerMethods = (Method[]) run(GetMethods.action(providerClass));
        for (Method method : providerMethods) {
            Class<?>[] paramTypes = method.getParameterTypes();
            if ("getValidationGroups".equals(method.getName()) && !method.isBridge() && paramTypes.length == 1 && paramTypes[0].isAssignableFrom(beanClass)) {
                return (DefaultGroupSequenceProvider) run(NewInstance.action(providerClass, "the default group sequence provider"));
            }
        }
        throw log.getWrongDefaultGroupSequenceProviderTypeException(beanClass.getName());
    }

    private Set<MetaConstraint<?>> getClassLevelConstraints(Class<?> clazz) {
        if (this.annotationProcessingOptions.areClassLevelConstraintsIgnoredFor(clazz)) {
            return Collections.emptySet();
        }
        Set<MetaConstraint<?>> classLevelConstraints = CollectionHelper.newHashSet();
        List<ConstraintDescriptorImpl<?>> classMetaData = findClassLevelConstraints(clazz);
        for (ConstraintDescriptorImpl<?> constraintDescription : classMetaData) {
            classLevelConstraints.add(createMetaConstraint(clazz, constraintDescription));
        }
        return classLevelConstraints;
    }

    private Set<ConstrainedElement> getFieldMetaData(Class<?> beanClass) {
        Set<ConstrainedElement> propertyMetaData = CollectionHelper.newHashSet();
        for (Field field : (Field[]) run(GetDeclaredFields.action(beanClass))) {
            if (!Modifier.isStatic(field.getModifiers()) && !this.annotationProcessingOptions.areMemberConstraintsIgnoredFor(field) && !field.isSynthetic()) {
                propertyMetaData.add(findPropertyMetaData(field));
            }
        }
        return propertyMetaData;
    }

    private ConstrainedField findPropertyMetaData(Field field) {
        Set<MetaConstraint<?>> constraints = convertToMetaConstraints(findConstraints(field, ElementType.FIELD), field);
        Map<Class<?>, Class<?>> groupConversions = getGroupConversions((ConvertGroup) field.getAnnotation(ConvertGroup.class), (ConvertGroup.List) field.getAnnotation(ConvertGroup.List.class));
        boolean isCascading = field.isAnnotationPresent(Valid.class);
        Set<MetaConstraint<?>> typeArgumentsConstraints = findTypeAnnotationConstraintsForMember(field);
        boolean typeArgumentAnnotated = !typeArgumentsConstraints.isEmpty();
        UnwrapMode unwrapMode = unwrapMode(field, typeArgumentAnnotated);
        return new ConstrainedField(ConfigurationSource.ANNOTATION, ConstraintLocation.forProperty(field), constraints, typeArgumentsConstraints, groupConversions, isCascading, unwrapMode);
    }

    private UnwrapMode unwrapMode(Field field, boolean typeArgumentAnnotated) {
        boolean isCollection = ReflectionHelper.isCollection(ReflectionHelper.typeOf(field));
        UnwrapValidatedValue unwrapValidatedValue = (UnwrapValidatedValue) field.getAnnotation(UnwrapValidatedValue.class);
        return unwrapMode(typeArgumentAnnotated, isCollection, unwrapValidatedValue);
    }

    private UnwrapMode unwrapMode(ExecutableElement executable, boolean typeArgumentAnnotated) {
        boolean isCollection = ReflectionHelper.isCollection(ReflectionHelper.typeOf(executable.getMember()));
        UnwrapValidatedValue unwrapValidatedValue = (UnwrapValidatedValue) executable.getAccessibleObject().getAnnotation(UnwrapValidatedValue.class);
        return unwrapMode(typeArgumentAnnotated, isCollection, unwrapValidatedValue);
    }

    private Set<MetaConstraint<?>> convertToMetaConstraints(List<ConstraintDescriptorImpl<?>> constraintDescriptors, Field field) {
        Set<MetaConstraint<?>> constraints = CollectionHelper.newHashSet();
        for (ConstraintDescriptorImpl<?> constraintDescription : constraintDescriptors) {
            constraints.add(createMetaConstraint(field, constraintDescription));
        }
        return constraints;
    }

    private UnwrapMode unwrapMode(boolean typeArgumentAnnotated, boolean isCollection, UnwrapValidatedValue unwrapValidatedValue) {
        if (unwrapValidatedValue == null && typeArgumentAnnotated && !isCollection) {
            return UnwrapMode.UNWRAP;
        }
        if (unwrapValidatedValue != null) {
            return unwrapValidatedValue.value() ? UnwrapMode.UNWRAP : UnwrapMode.SKIP_UNWRAP;
        }
        return UnwrapMode.AUTOMATIC;
    }

    private Set<ConstrainedExecutable> getConstructorMetaData(Class<?> clazz) {
        List<ExecutableElement> declaredConstructors = ExecutableElement.forConstructors((Constructor[]) run(GetDeclaredConstructors.action(clazz)));
        return getMetaData(declaredConstructors);
    }

    private Set<ConstrainedExecutable> getMethodMetaData(Class<?> clazz) {
        List<ExecutableElement> declaredMethods = ExecutableElement.forMethods((Method[]) run(GetDeclaredMethods.action(clazz)));
        return getMetaData(declaredMethods);
    }

    private Set<ConstrainedExecutable> getMetaData(List<ExecutableElement> executableElements) {
        Set<ConstrainedExecutable> executableMetaData = CollectionHelper.newHashSet();
        for (ExecutableElement executable : executableElements) {
            Member member = executable.getMember();
            if (!Modifier.isStatic(member.getModifiers()) && !member.isSynthetic()) {
                executableMetaData.add(findExecutableMetaData(executable));
            }
        }
        return executableMetaData;
    }

    private ConstrainedExecutable findExecutableMetaData(ExecutableElement executable) {
        Set<MetaConstraint<?>> crossParameterConstraints;
        Set<MetaConstraint<?>> typeArgumentsConstraints;
        Set<MetaConstraint<?>> returnValueConstraints;
        Map<Class<?>, Class<?>> groupConversions;
        boolean isCascading;
        List<ConstrainedParameter> parameterConstraints = getParameterMetaData(executable);
        AccessibleObject member = executable.getAccessibleObject();
        Map<ConstraintDescriptorImpl.ConstraintType, List<ConstraintDescriptorImpl<?>>> executableConstraints = CollectionHelper.partition(findConstraints(executable.getMember(), executable.getElementType()), byType());
        if (this.annotationProcessingOptions.areCrossParameterConstraintsIgnoredFor(executable.getMember())) {
            crossParameterConstraints = Collections.emptySet();
        } else {
            crossParameterConstraints = convertToMetaConstraints(executableConstraints.get(ConstraintDescriptorImpl.ConstraintType.CROSS_PARAMETER), executable);
        }
        UnwrapMode unwrapMode = UnwrapMode.AUTOMATIC;
        if (this.annotationProcessingOptions.areReturnValueConstraintsIgnoredFor(executable.getMember())) {
            returnValueConstraints = Collections.emptySet();
            typeArgumentsConstraints = Collections.emptySet();
            groupConversions = Collections.emptyMap();
            isCascading = false;
        } else {
            typeArgumentsConstraints = findTypeAnnotationConstraintsForMember(executable.getMember());
            boolean typeArgumentAnnotated = !typeArgumentsConstraints.isEmpty();
            unwrapMode = unwrapMode(executable, typeArgumentAnnotated);
            returnValueConstraints = convertToMetaConstraints(executableConstraints.get(ConstraintDescriptorImpl.ConstraintType.GENERIC), executable);
            groupConversions = getGroupConversions((ConvertGroup) member.getAnnotation(ConvertGroup.class), (ConvertGroup.List) member.getAnnotation(ConvertGroup.List.class));
            isCascading = executable.getAccessibleObject().isAnnotationPresent(Valid.class);
        }
        return new ConstrainedExecutable(ConfigurationSource.ANNOTATION, ConstraintLocation.forReturnValue(executable), parameterConstraints, crossParameterConstraints, returnValueConstraints, typeArgumentsConstraints, groupConversions, isCascading, unwrapMode);
    }

    private Set<MetaConstraint<?>> convertToMetaConstraints(List<ConstraintDescriptorImpl<?>> constraintsDescriptors, ExecutableElement executable) {
        MetaConstraint<?> metaConstraintCreateCrossParameterMetaConstraint;
        if (constraintsDescriptors == null) {
            return Collections.emptySet();
        }
        Set<MetaConstraint<?>> constraints = CollectionHelper.newHashSet();
        for (ConstraintDescriptorImpl<?> constraintDescriptor : constraintsDescriptors) {
            if (constraintDescriptor.getConstraintType() == ConstraintDescriptorImpl.ConstraintType.GENERIC) {
                metaConstraintCreateCrossParameterMetaConstraint = createReturnValueMetaConstraint(executable, constraintDescriptor);
            } else {
                metaConstraintCreateCrossParameterMetaConstraint = createCrossParameterMetaConstraint(executable, constraintDescriptor);
            }
            constraints.add(metaConstraintCreateCrossParameterMetaConstraint);
        }
        return constraints;
    }

    private List<ConstrainedParameter> getParameterMetaData(ExecutableElement executable) {
        List<ConstrainedParameter> metaData = CollectionHelper.newArrayList();
        List<String> parameterNames = executable.getParameterNames(this.parameterNameProvider);
        int i = 0;
        for (Annotation[] parameterAnnotations : executable.getParameterAnnotations()) {
            boolean parameterIsCascading = false;
            String parameterName = parameterNames.get(i);
            Set<MetaConstraint<?>> parameterConstraints = CollectionHelper.newHashSet();
            Set<MetaConstraint<?>> typeArgumentsConstraints = CollectionHelper.newHashSet();
            ConvertGroup groupConversion = null;
            ConvertGroup.List groupConversionList = null;
            if (this.annotationProcessingOptions.areParameterConstraintsIgnoredFor(executable.getMember(), i)) {
                metaData.add(new ConstrainedParameter(ConfigurationSource.ANNOTATION, ConstraintLocation.forParameter(executable, i), ReflectionHelper.typeOf(executable, i), i, parameterName, parameterConstraints, typeArgumentsConstraints, getGroupConversions(null, null), false, UnwrapMode.AUTOMATIC));
            } else {
                UnwrapValidatedValue unwrapValidatedValue = null;
                for (Annotation parameterAnnotation : parameterAnnotations) {
                    if (parameterAnnotation.annotationType().equals(Valid.class)) {
                        parameterIsCascading = true;
                    } else if (parameterAnnotation.annotationType().equals(ConvertGroup.class)) {
                        groupConversion = (ConvertGroup) parameterAnnotation;
                    } else if (parameterAnnotation.annotationType().equals(ConvertGroup.List.class)) {
                        groupConversionList = (ConvertGroup.List) parameterAnnotation;
                    } else if (parameterAnnotation.annotationType().equals(UnwrapValidatedValue.class)) {
                        unwrapValidatedValue = (UnwrapValidatedValue) parameterAnnotation;
                    }
                    List<ConstraintDescriptorImpl<?>> constraints = findConstraintAnnotations(executable.getMember(), parameterAnnotation, ElementType.PARAMETER);
                    for (ConstraintDescriptorImpl<?> constraintDescriptorImpl : constraints) {
                        parameterConstraints.add(createParameterMetaConstraint(executable, i, constraintDescriptorImpl));
                    }
                }
                Set<MetaConstraint<?>> typeArgumentsConstraints2 = findTypeAnnotationConstraintsForExecutableParameter(executable.getMember(), i);
                boolean typeArgumentAnnotated = !typeArgumentsConstraints2.isEmpty();
                boolean isCollection = ReflectionHelper.isCollection(ReflectionHelper.typeOf(executable, i));
                UnwrapMode unwrapMode = unwrapMode(typeArgumentAnnotated, isCollection, unwrapValidatedValue);
                metaData.add(new ConstrainedParameter(ConfigurationSource.ANNOTATION, ConstraintLocation.forParameter(executable, i), ReflectionHelper.typeOf(executable, i), i, parameterName, parameterConstraints, typeArgumentsConstraints2, getGroupConversions(groupConversion, groupConversionList), parameterIsCascading, unwrapMode));
            }
            i++;
        }
        return metaData;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private List<ConstraintDescriptorImpl<?>> findConstraints(Member member, ElementType type) {
        List<ConstraintDescriptorImpl<?>> metaData = CollectionHelper.newArrayList();
        for (Annotation annotation : ((AccessibleObject) member).getDeclaredAnnotations()) {
            metaData.addAll(findConstraintAnnotations(member, annotation, type));
        }
        return metaData;
    }

    private List<ConstraintDescriptorImpl<?>> findClassLevelConstraints(Class<?> beanClass) {
        List<ConstraintDescriptorImpl<?>> metaData = CollectionHelper.newArrayList();
        for (Annotation annotation : beanClass.getDeclaredAnnotations()) {
            metaData.addAll(findConstraintAnnotations(null, annotation, ElementType.TYPE));
        }
        return metaData;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected <A extends Annotation> List<ConstraintDescriptorImpl<?>> findConstraintAnnotations(Member member, A annotation, ElementType type) {
        if (isJdkInternalType(annotation)) {
            return Collections.emptyList();
        }
        List<ConstraintDescriptorImpl<?>> constraintDescriptors = CollectionHelper.newArrayList();
        List<Annotation> constraints = CollectionHelper.newArrayList();
        Class<? extends Annotation> annotationType = annotation.annotationType();
        if (this.constraintHelper.isConstraintAnnotation(annotationType)) {
            constraints.add(annotation);
        } else if (this.constraintHelper.isMultiValueConstraint(annotationType)) {
            constraints.addAll(this.constraintHelper.getConstraintsFromMultiValueConstraint(annotation));
        }
        for (Annotation constraint : constraints) {
            ConstraintDescriptorImpl<?> constraintDescriptor = buildConstraintDescriptor(member, constraint, type);
            constraintDescriptors.add(constraintDescriptor);
        }
        return constraintDescriptors;
    }

    private <A extends Annotation> boolean isJdkInternalType(A annotation) {
        Package pakkage = annotation.annotationType().getPackage();
        return pakkage != null && "jdk.internal".equals(pakkage.getName());
    }

    private Map<Class<?>, Class<?>> getGroupConversions(ConvertGroup groupConversion, ConvertGroup.List groupConversionList) {
        Map<Class<?>, Class<?>> groupConversions = CollectionHelper.newHashMap();
        if (groupConversion != null) {
            groupConversions.put(groupConversion.from(), groupConversion.to());
        }
        if (groupConversionList != null) {
            for (ConvertGroup conversion : groupConversionList.value()) {
                if (groupConversions.containsKey(conversion.from())) {
                    throw log.getMultipleGroupConversionsForSameSourceException(conversion.from(), CollectionHelper.asSet(groupConversions.get(conversion.from()), conversion.to()));
                }
                groupConversions.put(conversion.from(), conversion.to());
            }
        }
        return groupConversions;
    }

    private CollectionHelper.Partitioner<ConstraintDescriptorImpl.ConstraintType, ConstraintDescriptorImpl<?>> byType() {
        return new CollectionHelper.Partitioner<ConstraintDescriptorImpl.ConstraintType, ConstraintDescriptorImpl<?>>() { // from class: org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider.1
            @Override // org.hibernate.validator.internal.util.CollectionHelper.Partitioner
            public ConstraintDescriptorImpl.ConstraintType getPartition(ConstraintDescriptorImpl<?> v) {
                return v.getConstraintType();
            }
        };
    }

    private <A extends Annotation> MetaConstraint<?> createMetaConstraint(Class<?> declaringClass, ConstraintDescriptorImpl<A> descriptor) {
        return new MetaConstraint<>(descriptor, ConstraintLocation.forClass(declaringClass));
    }

    private <A extends Annotation> MetaConstraint<?> createMetaConstraint(Member member, ConstraintDescriptorImpl<A> descriptor) {
        return new MetaConstraint<>(descriptor, ConstraintLocation.forProperty(member));
    }

    private <A extends Annotation> MetaConstraint<A> createParameterMetaConstraint(ExecutableElement member, int parameterIndex, ConstraintDescriptorImpl<A> descriptor) {
        return new MetaConstraint<>(descriptor, ConstraintLocation.forParameter(member, parameterIndex));
    }

    private <A extends Annotation> MetaConstraint<A> createReturnValueMetaConstraint(ExecutableElement member, ConstraintDescriptorImpl<A> descriptor) {
        return new MetaConstraint<>(descriptor, ConstraintLocation.forReturnValue(member));
    }

    private <A extends Annotation> MetaConstraint<A> createCrossParameterMetaConstraint(ExecutableElement member, ConstraintDescriptorImpl<A> descriptor) {
        return new MetaConstraint<>(descriptor, ConstraintLocation.forCrossParameter(member));
    }

    private <A extends Annotation> ConstraintDescriptorImpl<A> buildConstraintDescriptor(Member member, A annotation, ElementType type) {
        return new ConstraintDescriptorImpl<>(this.constraintHelper, member, annotation, type);
    }

    private <T> T run(PrivilegedAction<T> privilegedAction) {
        return System.getSecurityManager() != null ? (T) AccessController.doPrivileged(privilegedAction) : privilegedAction.run();
    }

    protected Set<MetaConstraint<?>> findTypeAnnotationConstraintsForMember(Member member) {
        return Collections.emptySet();
    }

    protected Set<MetaConstraint<?>> findTypeAnnotationConstraintsForExecutableParameter(Member member, int i) {
        return Collections.emptySet();
    }
}
