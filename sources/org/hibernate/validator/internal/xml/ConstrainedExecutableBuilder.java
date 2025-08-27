package org.hibernate.validator.internal.xml;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.ParameterNameProvider;
import javax.validation.ValidationException;
import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptionsImpl;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
import org.hibernate.validator.internal.metadata.raw.ConfigurationSource;
import org.hibernate.validator.internal.metadata.raw.ConstrainedExecutable;
import org.hibernate.validator.internal.metadata.raw.ConstrainedParameter;
import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.StringHelper;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredConstructor;
import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredMethod;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/ConstrainedExecutableBuilder.class */
class ConstrainedExecutableBuilder {
    private static final Log log = LoggerFactory.make();
    private final ClassLoadingHelper classLoadingHelper;
    private final MetaConstraintBuilder metaConstraintBuilder;
    private final GroupConversionBuilder groupConversionBuilder;
    private final ConstrainedParameterBuilder constrainedParameterBuilder;
    private final AnnotationProcessingOptionsImpl annotationProcessingOptions;

    ConstrainedExecutableBuilder(ClassLoadingHelper classLoadingHelper, ParameterNameProvider parameterNameProvider, MetaConstraintBuilder metaConstraintBuilder, GroupConversionBuilder groupConversionBuilder, AnnotationProcessingOptionsImpl annotationProcessingOptions) {
        this.classLoadingHelper = classLoadingHelper;
        this.metaConstraintBuilder = metaConstraintBuilder;
        this.groupConversionBuilder = groupConversionBuilder;
        this.constrainedParameterBuilder = new ConstrainedParameterBuilder(metaConstraintBuilder, parameterNameProvider, groupConversionBuilder, annotationProcessingOptions);
        this.annotationProcessingOptions = annotationProcessingOptions;
    }

    Set<ConstrainedExecutable> buildMethodConstrainedExecutable(List<MethodType> methods, Class<?> beanClass, String defaultPackage) {
        Set<ConstrainedExecutable> constrainedExecutables = CollectionHelper.newHashSet();
        List<Method> alreadyProcessedMethods = CollectionHelper.newArrayList();
        for (MethodType methodType : methods) {
            List<Class<?>> parameterTypes = createParameterTypes(methodType.getParameter(), beanClass, defaultPackage);
            String methodName = methodType.getName();
            Method method = (Method) run(GetDeclaredMethod.action(beanClass, methodName, (Class[]) parameterTypes.toArray(new Class[parameterTypes.size()])));
            if (method == null) {
                throw log.getBeanDoesNotContainMethodException(beanClass.getName(), methodName, parameterTypes);
            }
            if (alreadyProcessedMethods.contains(method)) {
                throw log.getMethodIsDefinedTwiceInMappingXmlForBeanException(method.toString(), beanClass.getName());
            }
            alreadyProcessedMethods.add(method);
            ExecutableElement methodExecutableElement = ExecutableElement.forMethod(method);
            if (methodType.getIgnoreAnnotations() != null) {
                this.annotationProcessingOptions.ignoreConstraintAnnotationsOnMember(method, methodType.getIgnoreAnnotations());
            }
            ConstrainedExecutable constrainedExecutable = parseExecutableType(defaultPackage, methodType.getParameter(), methodType.getCrossParameter(), methodType.getReturnValue(), methodExecutableElement);
            constrainedExecutables.add(constrainedExecutable);
        }
        return constrainedExecutables;
    }

    Set<ConstrainedExecutable> buildConstructorConstrainedExecutable(List<ConstructorType> constructors, Class<?> beanClass, String defaultPackage) {
        Set<ConstrainedExecutable> constrainedExecutables = CollectionHelper.newHashSet();
        List<Constructor<?>> alreadyProcessedConstructors = CollectionHelper.newArrayList();
        for (ConstructorType constructorType : constructors) {
            List<Class<?>> constructorParameterTypes = createParameterTypes(constructorType.getParameter(), beanClass, defaultPackage);
            Constructor<?> constructor = (Constructor) run(GetDeclaredConstructor.action(beanClass, (Class[]) constructorParameterTypes.toArray(new Class[constructorParameterTypes.size()])));
            if (constructor == null) {
                throw log.getBeanDoesNotContainConstructorException(beanClass.getName(), StringHelper.join(constructorParameterTypes, ", "));
            }
            if (alreadyProcessedConstructors.contains(constructor)) {
                throw log.getConstructorIsDefinedTwiceInMappingXmlForBeanException(constructor.toString(), beanClass.getName());
            }
            alreadyProcessedConstructors.add(constructor);
            ExecutableElement constructorExecutableElement = ExecutableElement.forConstructor(constructor);
            if (constructorType.getIgnoreAnnotations() != null) {
                this.annotationProcessingOptions.ignoreConstraintAnnotationsOnMember(constructor, constructorType.getIgnoreAnnotations());
            }
            ConstrainedExecutable constrainedExecutable = parseExecutableType(defaultPackage, constructorType.getParameter(), constructorType.getCrossParameter(), constructorType.getReturnValue(), constructorExecutableElement);
            constrainedExecutables.add(constrainedExecutable);
        }
        return constrainedExecutables;
    }

    private ConstrainedExecutable parseExecutableType(String defaultPackage, List<ParameterType> parameterTypeList, CrossParameterType crossParameterType, ReturnValueType returnValueType, ExecutableElement executableElement) {
        List<ConstrainedParameter> parameterMetaData = this.constrainedParameterBuilder.buildConstrainedParameters(parameterTypeList, executableElement, defaultPackage);
        Set<MetaConstraint<?>> crossParameterConstraints = parseCrossParameterConstraints(defaultPackage, crossParameterType, executableElement);
        Set<MetaConstraint<?>> returnValueConstraints = CollectionHelper.newHashSet();
        Map<Class<?>, Class<?>> groupConversions = CollectionHelper.newHashMap();
        boolean isCascaded = parseReturnValueType(returnValueType, executableElement, returnValueConstraints, groupConversions, defaultPackage);
        return new ConstrainedExecutable(ConfigurationSource.XML, ConstraintLocation.forReturnValue(executableElement), parameterMetaData, crossParameterConstraints, returnValueConstraints, Collections.emptySet(), groupConversions, isCascaded, UnwrapMode.AUTOMATIC);
    }

    private Set<MetaConstraint<?>> parseCrossParameterConstraints(String defaultPackage, CrossParameterType crossParameterType, ExecutableElement executableElement) {
        Set<MetaConstraint<?>> crossParameterConstraints = CollectionHelper.newHashSet();
        if (crossParameterType == null) {
            return crossParameterConstraints;
        }
        ConstraintLocation constraintLocation = ConstraintLocation.forCrossParameter(executableElement);
        for (ConstraintType constraintType : crossParameterType.getConstraint()) {
            MetaConstraint<?> metaConstraint = this.metaConstraintBuilder.buildMetaConstraint(constraintLocation, constraintType, executableElement.getElementType(), defaultPackage, ConstraintDescriptorImpl.ConstraintType.CROSS_PARAMETER);
            crossParameterConstraints.add(metaConstraint);
        }
        if (crossParameterType.getIgnoreAnnotations() != null) {
            this.annotationProcessingOptions.ignoreConstraintAnnotationsForCrossParameterConstraint(executableElement.getMember(), crossParameterType.getIgnoreAnnotations());
        }
        return crossParameterConstraints;
    }

    private boolean parseReturnValueType(ReturnValueType returnValueType, ExecutableElement executableElement, Set<MetaConstraint<?>> returnValueConstraints, Map<Class<?>, Class<?>> groupConversions, String defaultPackage) {
        if (returnValueType == null) {
            return false;
        }
        ConstraintLocation constraintLocation = ConstraintLocation.forReturnValue(executableElement);
        for (ConstraintType constraint : returnValueType.getConstraint()) {
            MetaConstraint<?> metaConstraint = this.metaConstraintBuilder.buildMetaConstraint(constraintLocation, constraint, executableElement.getElementType(), defaultPackage, ConstraintDescriptorImpl.ConstraintType.GENERIC);
            returnValueConstraints.add(metaConstraint);
        }
        groupConversions.putAll(this.groupConversionBuilder.buildGroupConversionMap(returnValueType.getConvertGroup(), defaultPackage));
        if (returnValueType.getIgnoreAnnotations() != null) {
            this.annotationProcessingOptions.ignoreConstraintAnnotationsForReturnValue(executableElement.getMember(), returnValueType.getIgnoreAnnotations());
        }
        return returnValueType.getValid() != null;
    }

    private List<Class<?>> createParameterTypes(List<ParameterType> parameterList, Class<?> beanClass, String defaultPackage) {
        List<Class<?>> parameterTypes = CollectionHelper.newArrayList();
        for (ParameterType parameterType : parameterList) {
            String type = null;
            try {
                type = parameterType.getType();
                Class<?> parameterClass = this.classLoadingHelper.loadClass(type, defaultPackage);
                parameterTypes.add(parameterClass);
            } catch (ValidationException e) {
                throw log.getInvalidParameterTypeException(type, beanClass.getName());
            }
        }
        return parameterTypes;
    }

    private static <T> T run(PrivilegedAction<T> privilegedAction) {
        return System.getSecurityManager() != null ? (T) AccessController.doPrivileged(privilegedAction) : privilegedAction.run();
    }
}
