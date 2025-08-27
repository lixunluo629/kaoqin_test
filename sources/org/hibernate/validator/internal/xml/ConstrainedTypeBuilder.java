package org.hibernate.validator.internal.xml;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptionsImpl;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;
import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
import org.hibernate.validator.internal.metadata.raw.ConfigurationSource;
import org.hibernate.validator.internal.metadata.raw.ConstrainedType;
import org.hibernate.validator.internal.util.CollectionHelper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/ConstrainedTypeBuilder.class */
class ConstrainedTypeBuilder {
    private final ClassLoadingHelper classLoadingHelper;
    private final MetaConstraintBuilder metaConstraintBuilder;
    private final AnnotationProcessingOptionsImpl annotationProcessingOptions;
    private final Map<Class<?>, List<Class<?>>> defaultSequences;

    public ConstrainedTypeBuilder(ClassLoadingHelper classLoadingHelper, MetaConstraintBuilder metaConstraintBuilder, AnnotationProcessingOptionsImpl annotationProcessingOptions, Map<Class<?>, List<Class<?>>> defaultSequences) {
        this.classLoadingHelper = classLoadingHelper;
        this.metaConstraintBuilder = metaConstraintBuilder;
        this.annotationProcessingOptions = annotationProcessingOptions;
        this.defaultSequences = defaultSequences;
    }

    ConstrainedType buildConstrainedType(ClassType classType, Class<?> beanClass, String defaultPackage) {
        if (classType == null) {
            return null;
        }
        List<Class<?>> groupSequence = createGroupSequence(classType.getGroupSequence(), defaultPackage);
        if (!groupSequence.isEmpty()) {
            this.defaultSequences.put(beanClass, groupSequence);
        }
        ConstraintLocation constraintLocation = ConstraintLocation.forClass(beanClass);
        Set<MetaConstraint<?>> metaConstraints = CollectionHelper.newHashSet();
        for (ConstraintType constraint : classType.getConstraint()) {
            MetaConstraint<?> metaConstraint = this.metaConstraintBuilder.buildMetaConstraint(constraintLocation, constraint, java.lang.annotation.ElementType.TYPE, defaultPackage, null);
            metaConstraints.add(metaConstraint);
        }
        if (classType.getIgnoreAnnotations() != null) {
            this.annotationProcessingOptions.ignoreClassLevelConstraintAnnotations(beanClass, classType.getIgnoreAnnotations().booleanValue());
        }
        return new ConstrainedType(ConfigurationSource.XML, constraintLocation, metaConstraints);
    }

    private List<Class<?>> createGroupSequence(GroupSequenceType groupSequenceType, String defaultPackage) {
        List<Class<?>> groupSequence = CollectionHelper.newArrayList();
        if (groupSequenceType != null) {
            for (String groupName : groupSequenceType.getValue()) {
                Class<?> group = this.classLoadingHelper.loadClass(groupName, defaultPackage);
                groupSequence.add(group);
            }
        }
        return groupSequence;
    }
}
