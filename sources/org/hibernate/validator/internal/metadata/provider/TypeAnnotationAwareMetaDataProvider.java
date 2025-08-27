package org.hibernate.validator.internal.metadata.provider;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.validation.ParameterNameProvider;
import javax.validation.Valid;
import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptions;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.IgnoreJava6Requirement;
import org.hibernate.validator.internal.util.ReflectionHelper;
import org.hibernate.validator.internal.util.TypeHelper;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.internal.util.logging.Messages;

@IgnoreJava6Requirement
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/provider/TypeAnnotationAwareMetaDataProvider.class */
public class TypeAnnotationAwareMetaDataProvider extends AnnotationMetaDataProvider {
    private static final Log log = LoggerFactory.make();

    public TypeAnnotationAwareMetaDataProvider(ConstraintHelper constraintHelper, ParameterNameProvider parameterNameProvider, AnnotationProcessingOptions annotationProcessingOptions) {
        super(constraintHelper, parameterNameProvider, annotationProcessingOptions);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider
    protected Set<MetaConstraint<?>> findTypeAnnotationConstraintsForMember(Member member) {
        AnnotatedType annotatedType = null;
        if (member instanceof Field) {
            annotatedType = ((Field) member).getAnnotatedType();
        }
        if (member instanceof Method) {
            annotatedType = ((Method) member).getAnnotatedReturnType();
        }
        return findTypeArgumentsConstraints(member, annotatedType, ((AccessibleObject) member).isAnnotationPresent(Valid.class));
    }

    @Override // org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider
    protected Set<MetaConstraint<?>> findTypeAnnotationConstraintsForExecutableParameter(Member member, int i) {
        Parameter parameter = ((Executable) member).getParameters()[i];
        try {
            return findTypeArgumentsConstraints(member, parameter.getAnnotatedType(), parameter.isAnnotationPresent(Valid.class));
        } catch (ArrayIndexOutOfBoundsException ex) {
            log.warn(Messages.MESSAGES.constraintOnConstructorOfNonStaticInnerClass(), ex);
            return Collections.emptySet();
        }
    }

    private Set<MetaConstraint<?>> findTypeArgumentsConstraints(Member member, AnnotatedType annotatedType, boolean isCascaded) {
        Optional<AnnotatedType> typeParameter = getTypeParameter(annotatedType);
        if (!typeParameter.isPresent()) {
            return Collections.emptySet();
        }
        List<ConstraintDescriptorImpl<?>> constraintDescriptors = findTypeUseConstraints(member, typeParameter.get());
        if (constraintDescriptors.isEmpty()) {
            return Collections.emptySet();
        }
        Type validatedType = annotatedType.getType();
        if (ReflectionHelper.isIterable(annotatedType.getType()) || ReflectionHelper.isMap(annotatedType.getType())) {
            if (!isCascaded) {
                throw log.getTypeAnnotationConstraintOnIterableRequiresUseOfValidAnnotationException(member.getDeclaringClass().getName(), member.getName());
            }
            validatedType = typeParameter.get().getType();
        }
        return convertToTypeArgumentMetaConstraints(constraintDescriptors, member, validatedType);
    }

    private List<ConstraintDescriptorImpl<?>> findTypeUseConstraints(Member member, AnnotatedType typeArgument) {
        List<ConstraintDescriptorImpl<?>> metaData = CollectionHelper.newArrayList();
        for (Annotation annotation : typeArgument.getAnnotations()) {
            metaData.addAll(findConstraintAnnotations(member, annotation, ElementType.TYPE_USE));
        }
        return metaData;
    }

    private Set<MetaConstraint<?>> convertToTypeArgumentMetaConstraints(List<ConstraintDescriptorImpl<?>> constraintDescriptors, Member member, Type type) {
        Set<MetaConstraint<?>> constraints = CollectionHelper.newHashSet(constraintDescriptors.size());
        for (ConstraintDescriptorImpl<?> constraintDescription : constraintDescriptors) {
            MetaConstraint<?> metaConstraint = createTypeArgumentMetaConstraint(member, constraintDescription, type);
            constraints.add(metaConstraint);
        }
        return constraints;
    }

    private <A extends Annotation> MetaConstraint<?> createTypeArgumentMetaConstraint(Member member, ConstraintDescriptorImpl<A> descriptor, Type type) {
        return new MetaConstraint<>(descriptor, ConstraintLocation.forTypeArgument(member, type));
    }

    private Optional<AnnotatedType> getTypeParameter(AnnotatedType annotatedType) {
        if (annotatedType == null) {
            return Optional.empty();
        }
        if (!TypeHelper.isAssignable(AnnotatedParameterizedType.class, annotatedType.getClass())) {
            return Optional.empty();
        }
        AnnotatedType[] annotatedArguments = ((AnnotatedParameterizedType) annotatedType).getAnnotatedActualTypeArguments();
        if (annotatedArguments.length == 1) {
            return Optional.of(annotatedArguments[0]);
        }
        if (annotatedArguments.length > 1) {
            if (ReflectionHelper.isMap(annotatedType.getType())) {
                return Optional.of(annotatedArguments[1]);
            }
            log.parameterizedTypeWithMoreThanOneTypeArgumentIsNotSupported(annotatedType.getType().getTypeName());
        }
        return Optional.empty();
    }
}
