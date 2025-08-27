package org.hibernate.validator.internal.xml;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.ParameterNameProvider;
import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptionsImpl;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;
import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
import org.hibernate.validator.internal.metadata.raw.ConfigurationSource;
import org.hibernate.validator.internal.metadata.raw.ConstrainedParameter;
import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.ReflectionHelper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/ConstrainedParameterBuilder.class */
class ConstrainedParameterBuilder {
    private final GroupConversionBuilder groupConversionBuilder;
    private final ParameterNameProvider parameterNameProvider;
    private final MetaConstraintBuilder metaConstraintBuilder;
    private final AnnotationProcessingOptionsImpl annotationProcessingOptions;

    ConstrainedParameterBuilder(MetaConstraintBuilder metaConstraintBuilder, ParameterNameProvider parameterNameProvider, GroupConversionBuilder groupConversionBuilder, AnnotationProcessingOptionsImpl annotationProcessingOptions) {
        this.metaConstraintBuilder = metaConstraintBuilder;
        this.parameterNameProvider = parameterNameProvider;
        this.groupConversionBuilder = groupConversionBuilder;
        this.annotationProcessingOptions = annotationProcessingOptions;
    }

    List<ConstrainedParameter> buildConstrainedParameters(List<ParameterType> parameterList, ExecutableElement executableElement, String defaultPackage) {
        List<ConstrainedParameter> constrainedParameters = CollectionHelper.newArrayList();
        int i = 0;
        List<String> parameterNames = executableElement.getParameterNames(this.parameterNameProvider);
        for (ParameterType parameterType : parameterList) {
            ConstraintLocation constraintLocation = ConstraintLocation.forParameter(executableElement, i);
            Set<MetaConstraint<?>> metaConstraints = CollectionHelper.newHashSet();
            for (ConstraintType constraint : parameterType.getConstraint()) {
                MetaConstraint<?> metaConstraint = this.metaConstraintBuilder.buildMetaConstraint(constraintLocation, constraint, java.lang.annotation.ElementType.PARAMETER, defaultPackage, null);
                metaConstraints.add(metaConstraint);
            }
            Map<Class<?>, Class<?>> groupConversions = this.groupConversionBuilder.buildGroupConversionMap(parameterType.getConvertGroup(), defaultPackage);
            if (parameterType.getIgnoreAnnotations() != null) {
                this.annotationProcessingOptions.ignoreConstraintAnnotationsOnParameter(executableElement.getMember(), i, parameterType.getIgnoreAnnotations());
            }
            ConstrainedParameter constrainedParameter = new ConstrainedParameter(ConfigurationSource.XML, constraintLocation, ReflectionHelper.typeOf(executableElement, i), i, parameterNames.get(i), metaConstraints, Collections.emptySet(), groupConversions, parameterType.getValid() != null, UnwrapMode.AUTOMATIC);
            constrainedParameters.add(constrainedParameter);
            i++;
        }
        return constrainedParameters;
    }
}
