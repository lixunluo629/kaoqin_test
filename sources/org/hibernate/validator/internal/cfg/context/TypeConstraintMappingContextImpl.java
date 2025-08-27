package org.hibernate.validator.internal.cfg.context;

import java.lang.annotation.ElementType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.validation.ParameterNameProvider;
import org.hibernate.validator.cfg.ConstraintDef;
import org.hibernate.validator.cfg.context.Constrainable;
import org.hibernate.validator.cfg.context.ConstraintDefinitionContext;
import org.hibernate.validator.cfg.context.ConstructorConstraintMappingContext;
import org.hibernate.validator.cfg.context.MethodConstraintMappingContext;
import org.hibernate.validator.cfg.context.PropertyConstraintMappingContext;
import org.hibernate.validator.cfg.context.TypeConstraintMappingContext;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
import org.hibernate.validator.internal.metadata.raw.BeanConfiguration;
import org.hibernate.validator.internal.metadata.raw.ConfigurationSource;
import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;
import org.hibernate.validator.internal.metadata.raw.ConstrainedType;
import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.Contracts;
import org.hibernate.validator.internal.util.ReflectionHelper;
import org.hibernate.validator.internal.util.StringHelper;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.internal.util.logging.Messages;
import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredConstructor;
import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredField;
import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredMethod;
import org.hibernate.validator.internal.util.privilegedactions.GetMethod;
import org.hibernate.validator.internal.util.privilegedactions.NewInstance;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/cfg/context/TypeConstraintMappingContextImpl.class */
public final class TypeConstraintMappingContextImpl<C> extends ConstraintMappingContextImplBase implements TypeConstraintMappingContext<C> {
    private static final Log log = LoggerFactory.make();
    private final Class<C> beanClass;
    private final Set<ExecutableConstraintMappingContextImpl> executableContexts;
    private final Set<PropertyConstraintMappingContextImpl> propertyContexts;
    private final Set<Member> configuredMembers;
    private List<Class<?>> defaultGroupSequence;
    private Class<? extends DefaultGroupSequenceProvider<? super C>> defaultGroupSequenceProviderClass;

    @Override // org.hibernate.validator.internal.cfg.context.ConstraintContextImplBase, org.hibernate.validator.cfg.context.ConstraintDefinitionTarget
    public /* bridge */ /* synthetic */ ConstraintDefinitionContext constraintDefinition(Class cls) {
        return super.constraintDefinition(cls);
    }

    @Override // org.hibernate.validator.internal.cfg.context.ConstraintContextImplBase, org.hibernate.validator.cfg.context.TypeTarget
    public /* bridge */ /* synthetic */ TypeConstraintMappingContext type(Class cls) {
        return super.type(cls);
    }

    @Override // org.hibernate.validator.cfg.context.Constrainable
    public /* bridge */ /* synthetic */ Constrainable constraint(ConstraintDef constraintDef) {
        return constraint((ConstraintDef<?, ?>) constraintDef);
    }

    TypeConstraintMappingContextImpl(DefaultConstraintMapping mapping, Class<C> beanClass) {
        super(mapping);
        this.executableContexts = CollectionHelper.newHashSet();
        this.propertyContexts = CollectionHelper.newHashSet();
        this.configuredMembers = CollectionHelper.newHashSet();
        this.beanClass = beanClass;
        mapping.getAnnotationProcessingOptions().ignoreAnnotationConstraintForClass(beanClass, Boolean.FALSE);
    }

    @Override // org.hibernate.validator.cfg.context.Constrainable
    public TypeConstraintMappingContext<C> constraint(ConstraintDef<?, ?> definition) {
        addConstraint(ConfiguredConstraint.forType(definition, this.beanClass));
        return this;
    }

    @Override // org.hibernate.validator.cfg.context.AnnotationProcessingOptions
    public TypeConstraintMappingContext<C> ignoreAnnotations() {
        return ignoreAnnotations(true);
    }

    @Override // org.hibernate.validator.cfg.context.AnnotationIgnoreOptions
    public TypeConstraintMappingContext<C> ignoreAnnotations(boolean ignoreAnnotations) {
        this.mapping.getAnnotationProcessingOptions().ignoreClassLevelConstraintAnnotations(this.beanClass, ignoreAnnotations);
        return this;
    }

    @Override // org.hibernate.validator.cfg.context.TypeConstraintMappingContext
    public TypeConstraintMappingContext<C> ignoreAllAnnotations() {
        this.mapping.getAnnotationProcessingOptions().ignoreAnnotationConstraintForClass(this.beanClass, Boolean.TRUE);
        return this;
    }

    @Override // org.hibernate.validator.cfg.context.TypeConstraintMappingContext
    public TypeConstraintMappingContext<C> defaultGroupSequence(Class<?>... defaultGroupSequence) {
        this.defaultGroupSequence = Arrays.asList(defaultGroupSequence);
        return this;
    }

    @Override // org.hibernate.validator.cfg.context.TypeConstraintMappingContext
    public TypeConstraintMappingContext<C> defaultGroupSequenceProviderClass(Class<? extends DefaultGroupSequenceProvider<? super C>> defaultGroupSequenceProviderClass) {
        this.defaultGroupSequenceProviderClass = defaultGroupSequenceProviderClass;
        return this;
    }

    @Override // org.hibernate.validator.cfg.context.PropertyTarget
    public PropertyConstraintMappingContext property(String property, ElementType elementType) {
        Contracts.assertNotNull(property, "The property name must not be null.");
        Contracts.assertNotNull(elementType, "The element type must not be null.");
        Contracts.assertNotEmpty(property, Messages.MESSAGES.propertyNameMustNotBeEmpty());
        Member member = getMember(this.beanClass, property, elementType);
        if (member == null || member.getDeclaringClass() != this.beanClass) {
            throw log.getUnableToFindPropertyWithAccessException(this.beanClass, property, elementType);
        }
        if (this.configuredMembers.contains(member)) {
            throw log.getPropertyHasAlreadyBeConfiguredViaProgrammaticApiException(this.beanClass.getName(), property);
        }
        PropertyConstraintMappingContextImpl context = new PropertyConstraintMappingContextImpl(this, member);
        this.configuredMembers.add(member);
        this.propertyContexts.add(context);
        return context;
    }

    @Override // org.hibernate.validator.cfg.context.MethodTarget
    public MethodConstraintMappingContext method(String name, Class<?>... parameterTypes) {
        Contracts.assertNotNull(name, Messages.MESSAGES.methodNameMustNotBeNull());
        Method method = (Method) run(GetDeclaredMethod.action(this.beanClass, name, parameterTypes));
        if (method == null || method.getDeclaringClass() != this.beanClass) {
            throw log.getUnableToFindMethodException(this.beanClass, ExecutableElement.getExecutableAsString(name, parameterTypes));
        }
        if (this.configuredMembers.contains(method)) {
            throw log.getMethodHasAlreadyBeConfiguredViaProgrammaticApiException(this.beanClass.getName(), ExecutableElement.getExecutableAsString(name, parameterTypes));
        }
        MethodConstraintMappingContextImpl context = new MethodConstraintMappingContextImpl(this, method);
        this.configuredMembers.add(method);
        this.executableContexts.add(context);
        return context;
    }

    @Override // org.hibernate.validator.cfg.context.ConstructorTarget
    public ConstructorConstraintMappingContext constructor(Class<?>... parameterTypes) {
        Constructor<C> constructor = (Constructor) run(GetDeclaredConstructor.action(this.beanClass, parameterTypes));
        if (constructor == null || constructor.getDeclaringClass() != this.beanClass) {
            throw log.getBeanDoesNotContainConstructorException(this.beanClass.getName(), StringHelper.join(parameterTypes, ", "));
        }
        if (this.configuredMembers.contains(constructor)) {
            throw log.getConstructorHasAlreadyBeConfiguredViaProgrammaticApiException(this.beanClass.getName(), ExecutableElement.getExecutableAsString(this.beanClass.getSimpleName(), parameterTypes));
        }
        ConstructorConstraintMappingContextImpl context = new ConstructorConstraintMappingContextImpl(this, constructor);
        this.configuredMembers.add(constructor);
        this.executableContexts.add(context);
        return context;
    }

    BeanConfiguration<C> build(ConstraintHelper constraintHelper, ParameterNameProvider parameterNameProvider) {
        return new BeanConfiguration<>(ConfigurationSource.API, this.beanClass, buildConstraintElements(constraintHelper, parameterNameProvider), this.defaultGroupSequence, getDefaultGroupSequenceProvider());
    }

    private Set<ConstrainedElement> buildConstraintElements(ConstraintHelper constraintHelper, ParameterNameProvider parameterNameProvider) {
        Set<ConstrainedElement> elements = CollectionHelper.newHashSet();
        elements.add(new ConstrainedType(ConfigurationSource.API, ConstraintLocation.forClass(this.beanClass), getConstraints(constraintHelper)));
        for (ExecutableConstraintMappingContextImpl executableContext : this.executableContexts) {
            elements.add(executableContext.build(constraintHelper, parameterNameProvider));
        }
        for (PropertyConstraintMappingContextImpl propertyContext : this.propertyContexts) {
            elements.add(propertyContext.build(constraintHelper));
        }
        return elements;
    }

    private DefaultGroupSequenceProvider<? super C> getDefaultGroupSequenceProvider() {
        if (this.defaultGroupSequenceProviderClass != null) {
            return (DefaultGroupSequenceProvider) run(NewInstance.action(this.defaultGroupSequenceProviderClass, "default group sequence provider"));
        }
        return null;
    }

    Class<?> getBeanClass() {
        return this.beanClass;
    }

    @Override // org.hibernate.validator.internal.cfg.context.ConstraintMappingContextImplBase
    protected ConstraintDescriptorImpl.ConstraintType getConstraintType() {
        return ConstraintDescriptorImpl.ConstraintType.GENERIC;
    }

    private Member getMember(Class<?> clazz, String property, ElementType elementType) {
        Contracts.assertNotNull(clazz, Messages.MESSAGES.classCannotBeNull());
        if (property == null || property.length() == 0) {
            throw log.getPropertyNameCannotBeNullOrEmptyException();
        }
        if (!ElementType.FIELD.equals(elementType) && !ElementType.METHOD.equals(elementType)) {
            throw log.getElementTypeHasToBeFieldOrMethodException();
        }
        Member member = null;
        if (ElementType.FIELD.equals(elementType)) {
            member = (Member) run(GetDeclaredField.action(clazz, property));
        } else {
            String methodName = property.substring(0, 1).toUpperCase() + property.substring(1);
            for (String prefix : ReflectionHelper.PROPERTY_ACCESSOR_PREFIXES) {
                member = (Member) run(GetMethod.action(clazz, prefix + methodName));
                if (member != null) {
                    break;
                }
            }
        }
        return member;
    }

    private <T> T run(PrivilegedAction<T> privilegedAction) {
        return System.getSecurityManager() != null ? (T) AccessController.doPrivileged(privilegedAction) : privilegedAction.run();
    }
}
