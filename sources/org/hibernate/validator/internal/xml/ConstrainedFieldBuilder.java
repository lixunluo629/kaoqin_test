package org.hibernate.validator.internal.xml;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptionsImpl;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;
import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
import org.hibernate.validator.internal.metadata.raw.ConfigurationSource;
import org.hibernate.validator.internal.metadata.raw.ConstrainedField;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredField;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/ConstrainedFieldBuilder.class */
class ConstrainedFieldBuilder {
    private static final Log log = LoggerFactory.make();
    private final GroupConversionBuilder groupConversionBuilder;
    private final MetaConstraintBuilder metaConstraintBuilder;
    private final AnnotationProcessingOptionsImpl annotationProcessingOptions;

    ConstrainedFieldBuilder(MetaConstraintBuilder metaConstraintBuilder, GroupConversionBuilder groupConversionBuilder, AnnotationProcessingOptionsImpl annotationProcessingOptions) {
        this.metaConstraintBuilder = metaConstraintBuilder;
        this.groupConversionBuilder = groupConversionBuilder;
        this.annotationProcessingOptions = annotationProcessingOptions;
    }

    Set<ConstrainedField> buildConstrainedFields(List<FieldType> fields, Class<?> beanClass, String defaultPackage) {
        Set<ConstrainedField> constrainedFields = CollectionHelper.newHashSet();
        List<String> alreadyProcessedFieldNames = CollectionHelper.newArrayList();
        for (FieldType fieldType : fields) {
            Field field = findField(beanClass, fieldType.getName(), alreadyProcessedFieldNames);
            ConstraintLocation constraintLocation = ConstraintLocation.forProperty(field);
            Set<MetaConstraint<?>> metaConstraints = CollectionHelper.newHashSet();
            for (ConstraintType constraint : fieldType.getConstraint()) {
                MetaConstraint<?> metaConstraint = this.metaConstraintBuilder.buildMetaConstraint(constraintLocation, constraint, java.lang.annotation.ElementType.FIELD, defaultPackage, null);
                metaConstraints.add(metaConstraint);
            }
            Map<Class<?>, Class<?>> groupConversions = this.groupConversionBuilder.buildGroupConversionMap(fieldType.getConvertGroup(), defaultPackage);
            ConstrainedField constrainedField = new ConstrainedField(ConfigurationSource.XML, constraintLocation, metaConstraints, Collections.emptySet(), groupConversions, fieldType.getValid() != null, UnwrapMode.AUTOMATIC);
            constrainedFields.add(constrainedField);
            if (fieldType.getIgnoreAnnotations() != null) {
                this.annotationProcessingOptions.ignoreConstraintAnnotationsOnMember(field, fieldType.getIgnoreAnnotations());
            }
        }
        return constrainedFields;
    }

    private static Field findField(Class<?> beanClass, String fieldName, List<String> alreadyProcessedFieldNames) {
        if (alreadyProcessedFieldNames.contains(fieldName)) {
            throw log.getIsDefinedTwiceInMappingXmlForBeanException(fieldName, beanClass.getName());
        }
        alreadyProcessedFieldNames.add(fieldName);
        Field field = (Field) run(GetDeclaredField.action(beanClass, fieldName));
        if (field == null) {
            throw log.getBeanDoesNotContainTheFieldException(beanClass.getName(), fieldName);
        }
        return field;
    }

    private static <T> T run(PrivilegedAction<T> privilegedAction) {
        return System.getSecurityManager() != null ? (T) AccessController.doPrivileged(privilegedAction) : privilegedAction.run();
    }
}
