package org.hibernate.validator.internal.xml;

import java.lang.reflect.Method;
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
import org.hibernate.validator.internal.metadata.raw.ConstrainedExecutable;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.internal.util.privilegedactions.GetMethodFromPropertyName;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/ConstrainedGetterBuilder.class */
class ConstrainedGetterBuilder {
    private static final Log log = LoggerFactory.make();
    private final GroupConversionBuilder groupConversionBuilder;
    private final MetaConstraintBuilder metaConstraintBuilder;
    private final AnnotationProcessingOptionsImpl annotationProcessingOptions;

    ConstrainedGetterBuilder(MetaConstraintBuilder metaConstraintBuilder, GroupConversionBuilder groupConversionBuilder, AnnotationProcessingOptionsImpl annotationProcessingOptions) {
        this.metaConstraintBuilder = metaConstraintBuilder;
        this.groupConversionBuilder = groupConversionBuilder;
        this.annotationProcessingOptions = annotationProcessingOptions;
    }

    Set<ConstrainedExecutable> buildConstrainedGetters(List<GetterType> getterList, Class<?> beanClass, String defaultPackage) {
        Set<ConstrainedExecutable> constrainedExecutables = CollectionHelper.newHashSet();
        List<String> alreadyProcessedGetterNames = CollectionHelper.newArrayList();
        for (GetterType getterType : getterList) {
            String getterName = getterType.getName();
            Method getter = findGetter(beanClass, getterName, alreadyProcessedGetterNames);
            ConstraintLocation constraintLocation = ConstraintLocation.forProperty(getter);
            Set<MetaConstraint<?>> metaConstraints = CollectionHelper.newHashSet();
            for (ConstraintType constraint : getterType.getConstraint()) {
                MetaConstraint<?> metaConstraint = this.metaConstraintBuilder.buildMetaConstraint(constraintLocation, constraint, java.lang.annotation.ElementType.METHOD, defaultPackage, null);
                metaConstraints.add(metaConstraint);
            }
            Map<Class<?>, Class<?>> groupConversions = this.groupConversionBuilder.buildGroupConversionMap(getterType.getConvertGroup(), defaultPackage);
            ConstrainedExecutable constrainedGetter = new ConstrainedExecutable(ConfigurationSource.XML, constraintLocation, Collections.emptyList(), Collections.emptySet(), metaConstraints, Collections.emptySet(), groupConversions, getterType.getValid() != null, UnwrapMode.AUTOMATIC);
            constrainedExecutables.add(constrainedGetter);
            if (getterType.getIgnoreAnnotations() != null) {
                this.annotationProcessingOptions.ignoreConstraintAnnotationsOnMember(getter, getterType.getIgnoreAnnotations());
            }
        }
        return constrainedExecutables;
    }

    private static Method findGetter(Class<?> beanClass, String getterName, List<String> alreadyProcessedGetterNames) {
        if (alreadyProcessedGetterNames.contains(getterName)) {
            throw log.getIsDefinedTwiceInMappingXmlForBeanException(getterName, beanClass.getName());
        }
        alreadyProcessedGetterNames.add(getterName);
        Method method = (Method) run(GetMethodFromPropertyName.action(beanClass, getterName));
        if (method == null) {
            throw log.getBeanDoesNotContainThePropertyException(beanClass.getName(), getterName);
        }
        return method;
    }

    private static <T> T run(PrivilegedAction<T> privilegedAction) {
        return System.getSecurityManager() != null ? (T) AccessController.doPrivileged(privilegedAction) : privilegedAction.run();
    }
}
