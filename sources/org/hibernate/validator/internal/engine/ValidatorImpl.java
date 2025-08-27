package org.hibernate.validator.internal.engine;

import java.lang.annotation.ElementType;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.ConstraintViolation;
import javax.validation.ElementKind;
import javax.validation.GroupDefinitionException;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.Path;
import javax.validation.TraversableResolver;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import javax.validation.groups.Default;
import javax.validation.metadata.BeanDescriptor;
import org.hibernate.validator.HibernateValidatorPermission;
import org.hibernate.validator.internal.engine.ValidationContext;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorManager;
import org.hibernate.validator.internal.engine.groups.Group;
import org.hibernate.validator.internal.engine.groups.GroupWithInheritance;
import org.hibernate.validator.internal.engine.groups.Sequence;
import org.hibernate.validator.internal.engine.groups.ValidationOrder;
import org.hibernate.validator.internal.engine.groups.ValidationOrderGenerator;
import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.hibernate.validator.internal.engine.resolver.CachingTraversableResolverForSingleValidation;
import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
import org.hibernate.validator.internal.metadata.BeanMetaDataManager;
import org.hibernate.validator.internal.metadata.aggregated.BeanMetaData;
import org.hibernate.validator.internal.metadata.aggregated.ExecutableMetaData;
import org.hibernate.validator.internal.metadata.aggregated.ParameterMetaData;
import org.hibernate.validator.internal.metadata.aggregated.PropertyMetaData;
import org.hibernate.validator.internal.metadata.aggregated.ReturnValueMetaData;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;
import org.hibernate.validator.internal.metadata.facets.Cascadable;
import org.hibernate.validator.internal.metadata.facets.Validatable;
import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.ConcurrentReferenceHashMap;
import org.hibernate.validator.internal.util.Contracts;
import org.hibernate.validator.internal.util.ReflectionHelper;
import org.hibernate.validator.internal.util.TypeHelper;
import org.hibernate.validator.internal.util.TypeResolutionHelper;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.internal.util.logging.Messages;
import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredField;
import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredMethod;
import org.hibernate.validator.internal.util.privilegedactions.SetAccessibility;
import org.hibernate.validator.spi.time.TimeProvider;
import org.hibernate.validator.spi.valuehandling.ValidatedValueUnwrapper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/ValidatorImpl.class */
public class ValidatorImpl implements Validator, ExecutableValidator {
    private static final String TYPE_USE = "TYPE_USE";
    private static final Log log = LoggerFactory.make();
    private static final Collection<Class<?>> DEFAULT_GROUPS = Collections.singletonList(Default.class);
    private final ConstraintValidatorFactory constraintValidatorFactory;
    private final MessageInterpolator messageInterpolator;
    private final TraversableResolver traversableResolver;
    private final BeanMetaDataManager beanMetaDataManager;
    private final ConstraintValidatorManager constraintValidatorManager;
    private final ParameterNameProvider parameterNameProvider;
    private final TimeProvider timeProvider;
    private final boolean failFast;
    private final TypeResolutionHelper typeResolutionHelper;
    private final List<ValidatedValueUnwrapper<?>> validatedValueHandlers;
    private final transient ValidationOrderGenerator validationOrderGenerator = new ValidationOrderGenerator();
    private final ConcurrentMap<Member, Member> accessibleMembers = new ConcurrentReferenceHashMap(100, ConcurrentReferenceHashMap.ReferenceType.SOFT, ConcurrentReferenceHashMap.ReferenceType.SOFT);

    public ValidatorImpl(ConstraintValidatorFactory constraintValidatorFactory, MessageInterpolator messageInterpolator, TraversableResolver traversableResolver, BeanMetaDataManager beanMetaDataManager, ParameterNameProvider parameterNameProvider, TimeProvider timeProvider, TypeResolutionHelper typeResolutionHelper, List<ValidatedValueUnwrapper<?>> validatedValueHandlers, ConstraintValidatorManager constraintValidatorManager, boolean failFast) {
        this.constraintValidatorFactory = constraintValidatorFactory;
        this.messageInterpolator = messageInterpolator;
        this.traversableResolver = traversableResolver;
        this.beanMetaDataManager = beanMetaDataManager;
        this.parameterNameProvider = parameterNameProvider;
        this.timeProvider = timeProvider;
        this.typeResolutionHelper = typeResolutionHelper;
        this.validatedValueHandlers = validatedValueHandlers;
        this.constraintValidatorManager = constraintValidatorManager;
        this.failFast = failFast;
    }

    @Override // javax.validation.Validator
    public final <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
        Contracts.assertNotNull(object, Messages.MESSAGES.validatedObjectMustNotBeNull());
        if (!this.beanMetaDataManager.isConstrained(object.getClass())) {
            return Collections.emptySet();
        }
        ValidationOrder validationOrder = determineGroupValidationOrder(groups);
        ValidationContext<T> validationContext = getValidationContext().forValidate(object);
        ValueContext<?, Object> valueContext = ValueContext.getLocalExecutionContext(object, this.beanMetaDataManager.getBeanMetaData(object.getClass()), PathImpl.createRootPath());
        return validateInContext(valueContext, validationContext, validationOrder);
    }

    @Override // javax.validation.Validator
    public final <T> Set<ConstraintViolation<T>> validateProperty(T object, String propertyName, Class<?>... groups) {
        Contracts.assertNotNull(object, Messages.MESSAGES.validatedObjectMustNotBeNull());
        sanityCheckPropertyPath(propertyName);
        ValidationOrder validationOrder = determineGroupValidationOrder(groups);
        ValidationContext<T> context = getValidationContext().forValidateProperty(object);
        if (!this.beanMetaDataManager.isConstrained(context.getRootBeanClass())) {
            return Collections.emptySet();
        }
        return validatePropertyInContext(context, PathImpl.createPathFromString(propertyName), validationOrder);
    }

    @Override // javax.validation.Validator
    public final <T> Set<ConstraintViolation<T>> validateValue(Class<T> beanType, String propertyName, Object value, Class<?>... groups) {
        Contracts.assertNotNull(beanType, Messages.MESSAGES.beanTypeCannotBeNull());
        if (!this.beanMetaDataManager.isConstrained(beanType)) {
            return Collections.emptySet();
        }
        sanityCheckPropertyPath(propertyName);
        ValidationOrder validationOrder = determineGroupValidationOrder(groups);
        ValidationContext<T> context = getValidationContext().forValidateValue(beanType);
        return validateValueInContext(context, value, PathImpl.createPathFromString(propertyName), validationOrder);
    }

    @Override // javax.validation.executable.ExecutableValidator
    public <T> Set<ConstraintViolation<T>> validateParameters(T object, Method method, Object[] parameterValues, Class<?>... groups) {
        Contracts.assertNotNull(object, Messages.MESSAGES.validatedObjectMustNotBeNull());
        Contracts.assertNotNull(method, Messages.MESSAGES.validatedMethodMustNotBeNull());
        Contracts.assertNotNull(parameterValues, Messages.MESSAGES.validatedParameterArrayMustNotBeNull());
        return validateParameters((ValidatorImpl) object, ExecutableElement.forMethod(method), parameterValues, groups);
    }

    @Override // javax.validation.executable.ExecutableValidator
    public <T> Set<ConstraintViolation<T>> validateConstructorParameters(Constructor<? extends T> constructor, Object[] parameterValues, Class<?>... groups) {
        Contracts.assertNotNull(constructor, Messages.MESSAGES.validatedConstructorMustNotBeNull());
        Contracts.assertNotNull(parameterValues, Messages.MESSAGES.validatedParameterArrayMustNotBeNull());
        return validateParameters((ValidatorImpl) null, ExecutableElement.forConstructor(constructor), parameterValues, groups);
    }

    @Override // javax.validation.executable.ExecutableValidator
    public <T> Set<ConstraintViolation<T>> validateConstructorReturnValue(Constructor<? extends T> constructor, T createdObject, Class<?>... groups) {
        Contracts.assertNotNull(constructor, Messages.MESSAGES.validatedConstructorMustNotBeNull());
        Contracts.assertNotNull(createdObject, Messages.MESSAGES.validatedConstructorCreatedInstanceMustNotBeNull());
        return validateReturnValue((ValidatorImpl) null, ExecutableElement.forConstructor(constructor), createdObject, groups);
    }

    @Override // javax.validation.executable.ExecutableValidator
    public <T> Set<ConstraintViolation<T>> validateReturnValue(T object, Method method, Object returnValue, Class<?>... groups) {
        Contracts.assertNotNull(object, Messages.MESSAGES.validatedObjectMustNotBeNull());
        Contracts.assertNotNull(method, Messages.MESSAGES.validatedMethodMustNotBeNull());
        return validateReturnValue((ValidatorImpl) object, ExecutableElement.forMethod(method), returnValue, groups);
    }

    private <T> Set<ConstraintViolation<T>> validateParameters(T object, ExecutableElement executable, Object[] parameterValues, Class<?>... groups) throws ConstraintDeclarationException, GroupDefinitionException {
        if (parameterValues == null) {
            return Collections.emptySet();
        }
        ValidationOrder validationOrder = determineGroupValidationOrder(groups);
        ValidationContext<T> context = getValidationContext().forValidateParameters(this.parameterNameProvider, object, executable, parameterValues);
        if (!this.beanMetaDataManager.isConstrained(context.getRootBeanClass())) {
            return Collections.emptySet();
        }
        validateParametersInContext(context, parameterValues, validationOrder);
        return context.getFailingConstraints();
    }

    private <T> Set<ConstraintViolation<T>> validateReturnValue(T object, ExecutableElement executable, Object returnValue, Class<?>... groups) throws ConstraintDeclarationException, GroupDefinitionException {
        ValidationOrder validationOrder = determineGroupValidationOrder(groups);
        ValidationContext<T> context = getValidationContext().forValidateReturnValue(object, executable, returnValue);
        if (!this.beanMetaDataManager.isConstrained(context.getRootBeanClass())) {
            return Collections.emptySet();
        }
        validateReturnValueInContext(context, object, returnValue, validationOrder);
        return context.getFailingConstraints();
    }

    @Override // javax.validation.Validator
    public final BeanDescriptor getConstraintsForClass(Class<?> clazz) {
        return this.beanMetaDataManager.getBeanMetaData(clazz).getBeanDescriptor();
    }

    @Override // javax.validation.Validator
    public final <T> T unwrap(Class<T> type) {
        if (type.isAssignableFrom(Validator.class)) {
            return type.cast(this);
        }
        throw log.getTypeNotSupportedForUnwrappingException(type);
    }

    @Override // javax.validation.Validator
    public ExecutableValidator forExecutables() {
        return this;
    }

    private ValidationContext.ValidationContextBuilder getValidationContext() {
        return ValidationContext.getValidationContext(this.constraintValidatorManager, this.messageInterpolator, this.constraintValidatorFactory, getCachingTraversableResolver(), this.timeProvider, this.validatedValueHandlers, this.typeResolutionHelper, this.failFast);
    }

    private void sanityCheckPropertyPath(String propertyName) {
        if (propertyName == null || propertyName.length() == 0) {
            throw log.getInvalidPropertyPathException();
        }
    }

    private ValidationOrder determineGroupValidationOrder(Class<?>[] groups) {
        Collection<Class<?>> resultGroups;
        Contracts.assertNotNull(groups, Messages.MESSAGES.groupMustNotBeNull());
        for (Class<?> clazz : groups) {
            if (clazz == null) {
                throw new IllegalArgumentException(Messages.MESSAGES.groupMustNotBeNull());
            }
        }
        if (groups.length == 0) {
            resultGroups = DEFAULT_GROUPS;
        } else {
            resultGroups = Arrays.asList(groups);
        }
        return this.validationOrderGenerator.getValidationOrder(resultGroups);
    }

    private <T, U> Set<ConstraintViolation<T>> validateInContext(ValueContext<U, Object> valueContext, ValidationContext<T> context, ValidationOrder validationOrder) throws GroupDefinitionException {
        if (valueContext.getCurrentBean() == null) {
            return Collections.emptySet();
        }
        BeanMetaData<T> beanMetaData = this.beanMetaDataManager.getBeanMetaData(valueContext.getCurrentBeanType());
        if (beanMetaData.defaultGroupSequenceIsRedefined()) {
            validationOrder.assertDefaultGroupSequenceIsExpandable(beanMetaData.getDefaultGroupSequence(valueContext.getCurrentBean()));
        }
        Iterator<Group> groupIterator = validationOrder.getGroupIterator();
        while (groupIterator.hasNext()) {
            Group group = groupIterator.next();
            valueContext.setCurrentGroup(group.getDefiningClass());
            validateConstraintsForCurrentGroup(context, valueContext);
            if (shouldFailFast(context)) {
                return context.getFailingConstraints();
            }
        }
        Iterator<Group> groupIterator2 = validationOrder.getGroupIterator();
        while (groupIterator2.hasNext()) {
            Group group2 = groupIterator2.next();
            valueContext.setCurrentGroup(group2.getDefiningClass());
            validateCascadedConstraints(context, valueContext);
            if (shouldFailFast(context)) {
                return context.getFailingConstraints();
            }
        }
        Iterator<Sequence> sequenceIterator = validationOrder.getSequenceIterator();
        while (sequenceIterator.hasNext()) {
            Sequence sequence = sequenceIterator.next();
            Iterator<GroupWithInheritance> it = sequence.iterator();
            while (it.hasNext()) {
                GroupWithInheritance groupOfGroups = it.next();
                int numberOfViolations = context.getFailingConstraints().size();
                Iterator<Group> it2 = groupOfGroups.iterator();
                while (it2.hasNext()) {
                    Group group3 = it2.next();
                    valueContext.setCurrentGroup(group3.getDefiningClass());
                    validateConstraintsForCurrentGroup(context, valueContext);
                    if (shouldFailFast(context)) {
                        return context.getFailingConstraints();
                    }
                    validateCascadedConstraints(context, valueContext);
                    if (shouldFailFast(context)) {
                        return context.getFailingConstraints();
                    }
                }
                if (context.getFailingConstraints().size() > numberOfViolations) {
                    break;
                }
            }
        }
        return context.getFailingConstraints();
    }

    private void validateConstraintsForCurrentGroup(ValidationContext<?> validationContext, ValueContext<?, Object> valueContext) {
        if (!valueContext.validatingDefault()) {
            validateConstraintsForNonDefaultGroup(validationContext, valueContext);
        } else {
            validateConstraintsForDefaultGroup(validationContext, valueContext);
        }
    }

    private <U> void validateConstraintsForDefaultGroup(ValidationContext<?> validationContext, ValueContext<U, Object> valueContext) {
        BeanMetaData<U> beanMetaData = this.beanMetaDataManager.getBeanMetaData(valueContext.getCurrentBeanType());
        Map<Class<?>, Class<?>> validatedInterfaces = CollectionHelper.newHashMap();
        for (Class<? super U> clazz : beanMetaData.getClassHierarchy()) {
            BeanMetaData<? super U> hostingBeanMetaData = this.beanMetaDataManager.getBeanMetaData(clazz);
            boolean defaultGroupSequenceIsRedefined = hostingBeanMetaData.defaultGroupSequenceIsRedefined();
            if (defaultGroupSequenceIsRedefined) {
                Iterator<Sequence> defaultGroupSequence = hostingBeanMetaData.getDefaultValidationSequence(valueContext.getCurrentBean());
                Set<MetaConstraint<?>> metaConstraints = hostingBeanMetaData.getMetaConstraints();
                while (defaultGroupSequence.hasNext()) {
                    Iterator<GroupWithInheritance> it = defaultGroupSequence.next().iterator();
                    while (it.hasNext()) {
                        GroupWithInheritance groupOfGroups = it.next();
                        boolean validationSuccessful = true;
                        Iterator<Group> it2 = groupOfGroups.iterator();
                        while (it2.hasNext()) {
                            Group defaultSequenceMember = it2.next();
                            validationSuccessful = validateConstraintsForSingleDefaultGroupElement(validationContext, valueContext, validatedInterfaces, clazz, metaConstraints, defaultSequenceMember);
                        }
                        if (!validationSuccessful) {
                            break;
                        }
                    }
                }
            } else {
                Set<MetaConstraint<?>> metaConstraints2 = hostingBeanMetaData.getDirectMetaConstraints();
                validateConstraintsForSingleDefaultGroupElement(validationContext, valueContext, validatedInterfaces, clazz, metaConstraints2, Group.DEFAULT_GROUP);
            }
            validationContext.markCurrentBeanAsProcessed(valueContext);
            if (defaultGroupSequenceIsRedefined) {
                return;
            }
        }
    }

    private <U> boolean validateConstraintsForSingleDefaultGroupElement(ValidationContext<?> validationContext, ValueContext<U, Object> valueContext, Map<Class<?>, Class<?>> validatedInterfaces, Class<? super U> clazz, Set<MetaConstraint<?>> metaConstraints, Group defaultSequenceMember) {
        boolean validationSuccessful = true;
        valueContext.setCurrentGroup(defaultSequenceMember.getDefiningClass());
        PathImpl currentPath = valueContext.getPropertyPath();
        for (MetaConstraint<?> metaConstraint : metaConstraints) {
            Class<?> declaringClass = metaConstraint.getLocation().getDeclaringClass();
            if (declaringClass.isInterface()) {
                Class<?> validatedForClass = validatedInterfaces.get(declaringClass);
                if (validatedForClass == null || validatedForClass.equals(clazz)) {
                    validatedInterfaces.put(declaringClass, clazz);
                }
            }
            boolean tmp = validateConstraint(validationContext, valueContext, false, metaConstraint);
            if (shouldFailFast(validationContext)) {
                return false;
            }
            validationSuccessful = validationSuccessful && tmp;
            valueContext.setPropertyPath(currentPath);
        }
        return validationSuccessful;
    }

    private void validateConstraintsForNonDefaultGroup(ValidationContext<?> validationContext, ValueContext<?, Object> valueContext) {
        BeanMetaData<?> beanMetaData = this.beanMetaDataManager.getBeanMetaData(valueContext.getCurrentBeanType());
        PathImpl currentPath = valueContext.getPropertyPath();
        for (MetaConstraint<?> metaConstraint : beanMetaData.getMetaConstraints()) {
            validateConstraint(validationContext, valueContext, false, metaConstraint);
            if (shouldFailFast(validationContext)) {
                return;
            } else {
                valueContext.setPropertyPath(currentPath);
            }
        }
        validationContext.markCurrentBeanAsProcessed(valueContext);
    }

    private boolean validateConstraint(ValidationContext<?> validationContext, ValueContext<?, Object> valueContext, boolean propertyPathComplete, MetaConstraint<?> metaConstraint) {
        if (metaConstraint.getElementType() != ElementType.TYPE) {
            PropertyMetaData propertyMetaData = this.beanMetaDataManager.getBeanMetaData(valueContext.getCurrentBeanType()).getMetaDataFor(ReflectionHelper.getPropertyName(metaConstraint.getLocation().getMember()));
            if (!propertyPathComplete) {
                valueContext.appendNode(propertyMetaData);
            }
            if (TYPE_USE.equals(metaConstraint.getElementType().name())) {
                valueContext.setUnwrapMode(UnwrapMode.UNWRAP);
            } else {
                valueContext.setUnwrapMode(propertyMetaData.unwrapMode());
            }
        } else {
            valueContext.appendBeanNode();
        }
        boolean validationSuccessful = validateMetaConstraint(validationContext, valueContext, metaConstraint);
        valueContext.setUnwrapMode(UnwrapMode.AUTOMATIC);
        return validationSuccessful;
    }

    private boolean validatePropertyTypeConstraint(ValidationContext<?> validationContext, ValueContext<?, Object> valueContext, boolean propertyPathComplete, MetaConstraint<?> metaConstraint) {
        PropertyMetaData propertyMetaData = this.beanMetaDataManager.getBeanMetaData(valueContext.getCurrentBeanType()).getMetaDataFor(ReflectionHelper.getPropertyName(metaConstraint.getLocation().getMember()));
        if (!propertyPathComplete) {
            valueContext.appendNode(propertyMetaData);
        }
        valueContext.setUnwrapMode(UnwrapMode.UNWRAP);
        boolean validationSuccessful = validateMetaConstraint(validationContext, valueContext, metaConstraint);
        valueContext.setUnwrapMode(UnwrapMode.AUTOMATIC);
        return validationSuccessful;
    }

    private boolean validateMetaConstraint(ValidationContext<?> validationContext, ValueContext<?, Object> valueContext, MetaConstraint<?> metaConstraint) {
        if (isValidationRequired(validationContext, valueContext, metaConstraint)) {
            if (valueContext.getCurrentBean() != null) {
                Object valueToValidate = getBeanMemberValue(valueContext.getCurrentBean(), metaConstraint.getLocation().getMember());
                valueContext.setCurrentValidatedValue(valueToValidate);
            }
            return metaConstraint.validateConstraint(validationContext, valueContext);
        }
        return true;
    }

    private void validateCascadedConstraints(ValidationContext<?> validationContext, ValueContext<?, Object> valueContext) throws GroupDefinitionException {
        Object value;
        Validatable validatable = valueContext.getCurrentValidatable();
        PathImpl originalPath = valueContext.getPropertyPath();
        Class<?> originalGroup = valueContext.getCurrentGroup();
        for (Cascadable cascadable : validatable.getCascadables()) {
            valueContext.appendNode(cascadable);
            Class<?> group = cascadable.convertGroup(originalGroup);
            valueContext.setCurrentGroup(group);
            ElementType elementType = cascadable.getElementType();
            if (isCascadeRequired(validationContext, valueContext.getCurrentBean(), valueContext.getPropertyPath(), elementType) && (value = getBeanPropertyValue(validationContext, valueContext.getCurrentBean(), cascadable)) != null) {
                ValidationOrder validationOrder = this.validationOrderGenerator.getValidationOrder(group, group != originalGroup);
                Type type = value.getClass();
                if (ReflectionHelper.isIterable(type) || ReflectionHelper.isMap(type)) {
                    Iterator<?> valueIter = Collections.singletonList(value).iterator();
                    validateCascadedConstraint(validationContext, valueIter, false, valueContext, validationOrder, Collections.emptySet());
                    if (shouldFailFast(validationContext)) {
                        return;
                    }
                }
                Iterator<?> elementsIter = createIteratorForCascadedValue(type, value, valueContext);
                boolean isIndexable = ReflectionHelper.isIndexable(type);
                validateCascadedConstraint(validationContext, elementsIter, isIndexable, valueContext, validationOrder, cascadable.getTypeArgumentsConstraints());
                if (shouldFailFast(validationContext)) {
                    return;
                }
            }
            valueContext.setPropertyPath(originalPath);
            valueContext.setCurrentGroup(originalGroup);
        }
    }

    private Iterator<?> createIteratorForCascadedValue(Type type, Object value, ValueContext<?, ?> valueContext) {
        Iterator<?> iter;
        if (ReflectionHelper.isIterable(type)) {
            iter = ((Iterable) value).iterator();
            valueContext.markCurrentPropertyAsIterable();
        } else if (ReflectionHelper.isMap(type)) {
            Map<?, ?> map = (Map) value;
            iter = map.entrySet().iterator();
            valueContext.markCurrentPropertyAsIterable();
        } else if (TypeHelper.isArray(type)) {
            List<?> arrayList = Arrays.asList((Object[]) value);
            iter = arrayList.iterator();
            valueContext.markCurrentPropertyAsIterable();
        } else {
            iter = Collections.singletonList(value).iterator();
        }
        return iter;
    }

    private void validateCascadedConstraint(ValidationContext<?> context, Iterator<?> iter, boolean isIndexable, ValueContext<?, Object> valueContext, ValidationOrder validationOrder, Set<MetaConstraint<?>> typeArgumentsConstraint) throws GroupDefinitionException {
        int i = 0;
        while (iter.hasNext()) {
            Object value = iter.next();
            if (value instanceof Map.Entry) {
                Object mapKey = ((Map.Entry) value).getKey();
                valueContext.setKey(mapKey);
                value = ((Map.Entry) value).getValue();
            } else if (isIndexable) {
                valueContext.setIndex(Integer.valueOf(i));
            }
            if (!context.isBeanAlreadyValidated(value, valueContext.getCurrentGroup(), valueContext.getPropertyPath())) {
                validateTypeArgumentConstraints(context, buildNewLocalExecutionContext(valueContext, value), value, typeArgumentsConstraint);
                validateInContext(buildNewLocalExecutionContext(valueContext, value), context, validationOrder);
                if (shouldFailFast(context)) {
                    return;
                }
            }
            i++;
        }
    }

    private ValueContext<?, Object> buildNewLocalExecutionContext(ValueContext<?, Object> valueContext, Object value) {
        ValueContext<?, Object> newValueContext;
        if (value != null) {
            newValueContext = ValueContext.getLocalExecutionContext(value, this.beanMetaDataManager.getBeanMetaData(value.getClass()), valueContext.getPropertyPath());
        } else {
            newValueContext = ValueContext.getLocalExecutionContext((Class) valueContext.getCurrentBeanType(), (Validatable) this.beanMetaDataManager.getBeanMetaData(valueContext.getCurrentBeanType()), valueContext.getPropertyPath());
        }
        return newValueContext;
    }

    private void validateTypeArgumentConstraints(ValidationContext<?> context, ValueContext<?, Object> valueContext, Object value, Set<MetaConstraint<?>> typeArgumentsConstraints) {
        valueContext.setCurrentValidatedValue(value);
        valueContext.appendCollectionElementNode();
        for (MetaConstraint<?> metaConstraint : typeArgumentsConstraints) {
            metaConstraint.validateConstraint(context, valueContext);
            if (shouldFailFast(context)) {
                return;
            }
        }
    }

    private <T> Set<ConstraintViolation<T>> validatePropertyInContext(ValidationContext<T> context, PathImpl propertyPath, ValidationOrder validationOrder) throws GroupDefinitionException {
        List<MetaConstraint<?>> metaConstraints = CollectionHelper.newArrayList();
        List<MetaConstraint<?>> typeUseConstraints = CollectionHelper.newArrayList();
        ValueContext<T, ?> valueContextCollectMetaConstraintsForPathWithValue = collectMetaConstraintsForPathWithValue(context, propertyPath, metaConstraints, typeUseConstraints);
        if (valueContextCollectMetaConstraintsForPathWithValue.getCurrentBean() == null) {
            throw log.getUnableToReachPropertyToValidateException(context.getRootBean(), propertyPath);
        }
        if (metaConstraints.size() == 0 && typeUseConstraints.size() == 0) {
            return context.getFailingConstraints();
        }
        assertDefaultGroupSequenceIsExpandable(valueContextCollectMetaConstraintsForPathWithValue, validationOrder);
        Iterator<Group> groupIterator = validationOrder.getGroupIterator();
        while (groupIterator.hasNext()) {
            Group group = groupIterator.next();
            valueContextCollectMetaConstraintsForPathWithValue.setCurrentGroup(group.getDefiningClass());
            validatePropertyForCurrentGroup(valueContextCollectMetaConstraintsForPathWithValue, context, metaConstraints, typeUseConstraints);
            if (shouldFailFast(context)) {
                return context.getFailingConstraints();
            }
        }
        Iterator<Sequence> sequenceIterator = validationOrder.getSequenceIterator();
        while (sequenceIterator.hasNext()) {
            Sequence sequence = sequenceIterator.next();
            Iterator<GroupWithInheritance> it = sequence.iterator();
            while (it.hasNext()) {
                GroupWithInheritance groupOfGroups = it.next();
                int numberOfConstraintViolations = 0;
                Iterator<Group> it2 = groupOfGroups.iterator();
                while (it2.hasNext()) {
                    Group group2 = it2.next();
                    valueContextCollectMetaConstraintsForPathWithValue.setCurrentGroup(group2.getDefiningClass());
                    numberOfConstraintViolations += validatePropertyForCurrentGroup(valueContextCollectMetaConstraintsForPathWithValue, context, metaConstraints, typeUseConstraints);
                    if (shouldFailFast(context)) {
                        return context.getFailingConstraints();
                    }
                }
                if (numberOfConstraintViolations > 0) {
                    break;
                }
            }
        }
        return context.getFailingConstraints();
    }

    private <T> void assertDefaultGroupSequenceIsExpandable(ValueContext<T, ?> valueContext, ValidationOrder validationOrder) throws GroupDefinitionException {
        BeanMetaData<T> beanMetaData = this.beanMetaDataManager.getBeanMetaData(valueContext.getCurrentBeanType());
        if (beanMetaData.defaultGroupSequenceIsRedefined()) {
            validationOrder.assertDefaultGroupSequenceIsExpandable(beanMetaData.getDefaultGroupSequence(valueContext.getCurrentBean()));
        }
    }

    private <T> Set<ConstraintViolation<T>> validateValueInContext(ValidationContext<T> context, Object value, PathImpl propertyPath, ValidationOrder validationOrder) throws GroupDefinitionException {
        List<MetaConstraint<?>> metaConstraints = CollectionHelper.newArrayList();
        List<MetaConstraint<?>> typeArgumentConstraints = CollectionHelper.newArrayList();
        ValueContext<?, Object> valueContext = collectMetaConstraintsForPathWithoutValue(context, propertyPath, metaConstraints, typeArgumentConstraints);
        valueContext.setCurrentValidatedValue(value);
        if (metaConstraints.size() == 0 && typeArgumentConstraints.size() == 0) {
            return context.getFailingConstraints();
        }
        BeanMetaData<T> beanMetaData = this.beanMetaDataManager.getBeanMetaData(valueContext.getCurrentBeanType());
        if (beanMetaData.defaultGroupSequenceIsRedefined()) {
            validationOrder.assertDefaultGroupSequenceIsExpandable(beanMetaData.getDefaultGroupSequence(null));
        }
        Iterator<Group> groupIterator = validationOrder.getGroupIterator();
        while (groupIterator.hasNext()) {
            Group group = groupIterator.next();
            valueContext.setCurrentGroup(group.getDefiningClass());
            validatePropertyForCurrentGroup(valueContext, context, metaConstraints, typeArgumentConstraints);
            if (shouldFailFast(context)) {
                return context.getFailingConstraints();
            }
        }
        Iterator<Sequence> sequenceIterator = validationOrder.getSequenceIterator();
        while (sequenceIterator.hasNext()) {
            Sequence sequence = sequenceIterator.next();
            Iterator<GroupWithInheritance> it = sequence.iterator();
            while (it.hasNext()) {
                GroupWithInheritance groupOfGroups = it.next();
                int numberOfConstraintViolations = 0;
                Iterator<Group> it2 = groupOfGroups.iterator();
                while (it2.hasNext()) {
                    Group group2 = it2.next();
                    valueContext.setCurrentGroup(group2.getDefiningClass());
                    numberOfConstraintViolations += validatePropertyForCurrentGroup(valueContext, context, metaConstraints, typeArgumentConstraints);
                    if (shouldFailFast(context)) {
                        return context.getFailingConstraints();
                    }
                }
                if (numberOfConstraintViolations > 0) {
                    break;
                }
            }
        }
        return context.getFailingConstraints();
    }

    private int validatePropertyForCurrentGroup(ValueContext<?, Object> valueContext, ValidationContext<?> validationContext, List<MetaConstraint<?>> metaConstraints, List<MetaConstraint<?>> typeUseConstraints) {
        if (!valueContext.validatingDefault()) {
            return validatePropertyForNonDefaultGroup(valueContext, validationContext, metaConstraints, typeUseConstraints);
        }
        return validatePropertyForDefaultGroup(valueContext, validationContext, metaConstraints, typeUseConstraints);
    }

    private int validatePropertyForNonDefaultGroup(ValueContext<?, Object> valueContext, ValidationContext<?> validationContext, List<MetaConstraint<?>> metaConstraints, List<MetaConstraint<?>> typeArgumentConstraints) {
        int numberOfConstraintViolationsBefore = validationContext.getFailingConstraints().size();
        for (MetaConstraint<?> metaConstraint : metaConstraints) {
            validateConstraint(validationContext, valueContext, true, metaConstraint);
            if (shouldFailFast(validationContext)) {
                return validationContext.getFailingConstraints().size() - numberOfConstraintViolationsBefore;
            }
        }
        for (MetaConstraint<?> metaConstraint2 : typeArgumentConstraints) {
            validatePropertyTypeConstraint(validationContext, valueContext, true, metaConstraint2);
            if (shouldFailFast(validationContext)) {
                return validationContext.getFailingConstraints().size() - numberOfConstraintViolationsBefore;
            }
        }
        return validationContext.getFailingConstraints().size() - numberOfConstraintViolationsBefore;
    }

    private <U> int validatePropertyForDefaultGroup(ValueContext<U, Object> valueContext, ValidationContext<?> validationContext, List<MetaConstraint<?>> constraintList, List<MetaConstraint<?>> typeUseConstraints) {
        int numberOfConstraintViolationsBefore = validationContext.getFailingConstraints().size();
        BeanMetaData<U> beanMetaData = this.beanMetaDataManager.getBeanMetaData(valueContext.getCurrentBeanType());
        Map<Class<?>, Class<?>> validatedInterfaces = CollectionHelper.newHashMap();
        for (Class<? super U> clazz : beanMetaData.getClassHierarchy()) {
            BeanMetaData<? super U> hostingBeanMetaData = this.beanMetaDataManager.getBeanMetaData(clazz);
            boolean defaultGroupSequenceIsRedefined = hostingBeanMetaData.defaultGroupSequenceIsRedefined();
            if (defaultGroupSequenceIsRedefined) {
                Iterator<Sequence> defaultGroupSequence = hostingBeanMetaData.getDefaultValidationSequence(valueContext.getCurrentBean());
                Set<MetaConstraint<?>> metaConstraints = hostingBeanMetaData.getMetaConstraints();
                while (defaultGroupSequence.hasNext()) {
                    Iterator<GroupWithInheritance> it = defaultGroupSequence.next().iterator();
                    while (it.hasNext()) {
                        GroupWithInheritance groupOfGroups = it.next();
                        boolean validationSuccessful = true;
                        Iterator<Group> it2 = groupOfGroups.iterator();
                        while (it2.hasNext()) {
                            Group groupClass = it2.next();
                            validationSuccessful = validatePropertyForSingleDefaultGroupElement(valueContext, validationContext, constraintList, typeUseConstraints, validatedInterfaces, clazz, metaConstraints, groupClass);
                        }
                        if (!validationSuccessful) {
                            break;
                        }
                    }
                }
            } else {
                Set<MetaConstraint<?>> metaConstraints2 = hostingBeanMetaData.getDirectMetaConstraints();
                validatePropertyForSingleDefaultGroupElement(valueContext, validationContext, constraintList, typeUseConstraints, validatedInterfaces, clazz, metaConstraints2, Group.DEFAULT_GROUP);
            }
            if (defaultGroupSequenceIsRedefined) {
                break;
            }
        }
        return validationContext.getFailingConstraints().size() - numberOfConstraintViolationsBefore;
    }

    private <U> boolean validatePropertyForSingleDefaultGroupElement(ValueContext<U, Object> valueContext, ValidationContext<?> validationContext, List<MetaConstraint<?>> constraintList, List<MetaConstraint<?>> typeUseConstraints, Map<Class<?>, Class<?>> validatedInterfaces, Class<? super U> clazz, Set<MetaConstraint<?>> metaConstraints, Group groupClass) {
        valueContext.setCurrentGroup(groupClass.getDefiningClass());
        boolean validationSuccessful = true;
        for (MetaConstraint<?> metaConstraint : metaConstraints) {
            Class<?> declaringClass = metaConstraint.getLocation().getDeclaringClass();
            if (declaringClass.isInterface()) {
                Class<?> validatedForClass = validatedInterfaces.get(declaringClass);
                if (validatedForClass == null || validatedForClass.equals(clazz)) {
                    validatedInterfaces.put(declaringClass, clazz);
                }
            }
            if (constraintList.contains(metaConstraint)) {
                boolean tmp = validateConstraint(validationContext, valueContext, true, metaConstraint);
                validationSuccessful = validationSuccessful && tmp;
                if (shouldFailFast(validationContext)) {
                    return false;
                }
            }
            if (typeUseConstraints.contains(metaConstraint)) {
                boolean tmp2 = validatePropertyTypeConstraint(validationContext, valueContext, true, metaConstraint);
                validationSuccessful = validationSuccessful && tmp2;
                if (shouldFailFast(validationContext)) {
                    return false;
                }
            } else {
                continue;
            }
        }
        return validationSuccessful;
    }

    private <T> void validateParametersInContext(ValidationContext<T> validationContext, Object[] parameterValues, ValidationOrder validationOrder) throws ConstraintDeclarationException, GroupDefinitionException {
        BeanMetaData<T> beanMetaData = this.beanMetaDataManager.getBeanMetaData(validationContext.getRootBeanClass());
        ExecutableMetaData executableMetaData = beanMetaData.getMetaDataFor(validationContext.getExecutable());
        if (executableMetaData == null) {
            throw log.getMethodOrConstructorNotDefinedByValidatedTypeException(beanMetaData.getBeanClass().getName(), validationContext.getExecutable().getMember());
        }
        if (beanMetaData.defaultGroupSequenceIsRedefined()) {
            validationOrder.assertDefaultGroupSequenceIsExpandable(beanMetaData.getDefaultGroupSequence(validationContext.getRootBean()));
        }
        Iterator<Group> groupIterator = validationOrder.getGroupIterator();
        while (groupIterator.hasNext()) {
            validateParametersForGroup(validationContext, parameterValues, groupIterator.next());
            if (shouldFailFast(validationContext)) {
                return;
            }
        }
        ValueContext<?, Object> localExecutionContext = ValueContext.getLocalExecutionContext(parameterValues, executableMetaData.getValidatableParametersMetaData(), PathImpl.createPathForExecutable(executableMetaData));
        localExecutionContext.setUnwrapMode(executableMetaData.unwrapMode());
        Iterator<Group> groupIterator2 = validationOrder.getGroupIterator();
        while (groupIterator2.hasNext()) {
            localExecutionContext.setCurrentGroup(groupIterator2.next().getDefiningClass());
            validateCascadedConstraints(validationContext, localExecutionContext);
            if (shouldFailFast(validationContext)) {
                return;
            }
        }
        Iterator<Sequence> sequenceIterator = validationOrder.getSequenceIterator();
        while (sequenceIterator.hasNext()) {
            Sequence sequence = sequenceIterator.next();
            Iterator<GroupWithInheritance> it = sequence.iterator();
            while (it.hasNext()) {
                GroupWithInheritance groupOfGroups = it.next();
                int numberOfViolations = validationContext.getFailingConstraints().size();
                Iterator<Group> it2 = groupOfGroups.iterator();
                while (it2.hasNext()) {
                    Group group = it2.next();
                    validateParametersForGroup(validationContext, parameterValues, group);
                    if (shouldFailFast(validationContext)) {
                        return;
                    }
                    localExecutionContext.setCurrentGroup(group.getDefiningClass());
                    validateCascadedConstraints(validationContext, localExecutionContext);
                    if (shouldFailFast(validationContext)) {
                        return;
                    }
                }
                if (validationContext.getFailingConstraints().size() > numberOfViolations) {
                    break;
                }
            }
        }
    }

    private <T> int validateParametersForGroup(ValidationContext<T> validationContext, Object[] parameterValues, Group group) throws ConstraintDeclarationException {
        int numberOfViolationsBefore = validationContext.getFailingConstraints().size();
        BeanMetaData<T> beanMetaData = this.beanMetaDataManager.getBeanMetaData(validationContext.getRootBeanClass());
        ExecutableMetaData executableMetaData = beanMetaData.getMetaDataFor(validationContext.getExecutable());
        if (parameterValues.length != executableMetaData.getParameterTypes().length) {
            throw log.getInvalidParameterCountForExecutableException(ExecutableElement.getExecutableAsString(executableMetaData.getType().toString() + "#" + executableMetaData.getName(), executableMetaData.getParameterTypes()), parameterValues.length, executableMetaData.getParameterTypes().length);
        }
        if (group.isDefaultGroup()) {
            Iterator<Sequence> defaultGroupSequence = beanMetaData.getDefaultValidationSequence(validationContext.getRootBean());
            while (defaultGroupSequence.hasNext()) {
                Sequence sequence = defaultGroupSequence.next();
                Iterator<GroupWithInheritance> it = sequence.iterator();
                while (it.hasNext()) {
                    GroupWithInheritance expandedGroup = it.next();
                    int numberOfViolationsOfCurrentGroup = 0;
                    Iterator<Group> it2 = expandedGroup.iterator();
                    while (it2.hasNext()) {
                        Group defaultGroupSequenceElement = it2.next();
                        numberOfViolationsOfCurrentGroup += validateParametersForSingleGroup(validationContext, parameterValues, executableMetaData, defaultGroupSequenceElement.getDefiningClass());
                        if (shouldFailFast(validationContext)) {
                            return validationContext.getFailingConstraints().size() - numberOfViolationsBefore;
                        }
                    }
                    if (numberOfViolationsOfCurrentGroup > 0) {
                        return validationContext.getFailingConstraints().size() - numberOfViolationsBefore;
                    }
                }
            }
        } else {
            validateParametersForSingleGroup(validationContext, parameterValues, executableMetaData, group.getDefiningClass());
        }
        return validationContext.getFailingConstraints().size() - numberOfViolationsBefore;
    }

    private <T> int validateParametersForSingleGroup(ValidationContext<T> validationContext, Object[] parameterValues, ExecutableMetaData executableMetaData, Class<?> currentValidatedGroup) {
        int numberOfViolationsBefore = validationContext.getFailingConstraints().size();
        ValueContext<T, Object> valueContext = getExecutableValueContext(validationContext.getRootBean(), executableMetaData, currentValidatedGroup);
        valueContext.appendCrossParameterNode();
        valueContext.setCurrentValidatedValue(parameterValues);
        validateConstraintsForGroup(validationContext, valueContext, executableMetaData.getCrossParameterConstraints());
        if (shouldFailFast(validationContext)) {
            return validationContext.getFailingConstraints().size() - numberOfViolationsBefore;
        }
        ValueContext<T, Object> valueContext2 = getExecutableValueContext(validationContext.getRootBean(), executableMetaData, currentValidatedGroup);
        valueContext2.setCurrentValidatedValue(parameterValues);
        for (int i = 0; i < parameterValues.length; i++) {
            PathImpl originalPath = valueContext2.getPropertyPath();
            ParameterMetaData parameterMetaData = executableMetaData.getParameterMetaData(i);
            Object value = parameterValues[i];
            if (value != null) {
                Class<?> valueType = value.getClass();
                if ((parameterMetaData.getType() instanceof Class) && ((Class) parameterMetaData.getType()).isPrimitive()) {
                    valueType = ReflectionHelper.unBoxedType(valueType);
                }
                if (!TypeHelper.isAssignable(TypeHelper.getErasedType(parameterMetaData.getType()), valueType)) {
                    throw log.getParameterTypesDoNotMatchException(valueType.getName(), parameterMetaData.getType().toString(), i, validationContext.getExecutable().getMember());
                }
            }
            valueContext2.appendNode(parameterMetaData);
            valueContext2.setUnwrapMode(parameterMetaData.unwrapMode());
            valueContext2.setCurrentValidatedValue(value);
            validateConstraintsForGroup(validationContext, valueContext2, parameterMetaData);
            if (shouldFailFast(validationContext)) {
                return validationContext.getFailingConstraints().size() - numberOfViolationsBefore;
            }
            if (!parameterMetaData.isCascading()) {
                validateConstraintsForGroup(validationContext, valueContext2, parameterMetaData.getTypeArgumentsConstraints());
                if (shouldFailFast(validationContext)) {
                    return validationContext.getFailingConstraints().size() - numberOfViolationsBefore;
                }
            }
            valueContext2.setPropertyPath(originalPath);
        }
        return validationContext.getFailingConstraints().size() - numberOfViolationsBefore;
    }

    private <T> ValueContext<T, Object> getExecutableValueContext(T object, ExecutableMetaData executableMetaData, Class<?> group) {
        ValueContext<T, Object> valueContext;
        if (object != null) {
            valueContext = ValueContext.getLocalExecutionContext(object, (Validatable) null, PathImpl.createPathForExecutable(executableMetaData));
        } else {
            valueContext = ValueContext.getLocalExecutionContext((Class) null, (Validatable) null, PathImpl.createPathForExecutable(executableMetaData));
        }
        valueContext.setCurrentGroup(group);
        return valueContext;
    }

    private <V, T> void validateReturnValueInContext(ValidationContext<T> context, T bean, V value, ValidationOrder validationOrder) throws ConstraintDeclarationException, GroupDefinitionException {
        BeanMetaData<T> beanMetaData = this.beanMetaDataManager.getBeanMetaData(context.getRootBeanClass());
        ExecutableMetaData executableMetaData = beanMetaData.getMetaDataFor(context.getExecutable());
        if (executableMetaData == null) {
            return;
        }
        if (beanMetaData.defaultGroupSequenceIsRedefined()) {
            validationOrder.assertDefaultGroupSequenceIsExpandable(beanMetaData.getDefaultGroupSequence(bean));
        }
        Iterator<Group> groupIterator = validationOrder.getGroupIterator();
        while (groupIterator.hasNext()) {
            validateReturnValueForGroup(context, bean, value, groupIterator.next());
            if (shouldFailFast(context)) {
                return;
            }
        }
        ValueContext<?, Object> localExecutionContext = null;
        if (value != null) {
            localExecutionContext = ValueContext.getLocalExecutionContext(value, executableMetaData.getReturnValueMetaData(), PathImpl.createPathForExecutable(executableMetaData));
            Iterator<Group> groupIterator2 = validationOrder.getGroupIterator();
            while (groupIterator2.hasNext()) {
                localExecutionContext.setCurrentGroup(groupIterator2.next().getDefiningClass());
                validateCascadedConstraints(context, localExecutionContext);
                if (shouldFailFast(context)) {
                    return;
                }
            }
        }
        Iterator<Sequence> sequenceIterator = validationOrder.getSequenceIterator();
        while (sequenceIterator.hasNext()) {
            Sequence sequence = sequenceIterator.next();
            Iterator<GroupWithInheritance> it = sequence.iterator();
            while (it.hasNext()) {
                GroupWithInheritance groupOfGroups = it.next();
                int numberOfFailingConstraintsBeforeGroup = context.getFailingConstraints().size();
                Iterator<Group> it2 = groupOfGroups.iterator();
                while (it2.hasNext()) {
                    Group group = it2.next();
                    validateReturnValueForGroup(context, bean, value, group);
                    if (shouldFailFast(context)) {
                        return;
                    }
                    if (value != null) {
                        localExecutionContext.setCurrentGroup(group.getDefiningClass());
                        validateCascadedConstraints(context, localExecutionContext);
                        if (shouldFailFast(context)) {
                            return;
                        }
                    }
                }
                if (context.getFailingConstraints().size() > numberOfFailingConstraintsBeforeGroup) {
                    break;
                }
            }
        }
    }

    private <T> int validateReturnValueForGroup(ValidationContext<T> validationContext, T bean, Object value, Group group) throws ConstraintDeclarationException {
        int numberOfViolationsBefore = validationContext.getFailingConstraints().size();
        BeanMetaData<T> beanMetaData = this.beanMetaDataManager.getBeanMetaData(validationContext.getRootBeanClass());
        ExecutableMetaData executableMetaData = beanMetaData.getMetaDataFor(validationContext.getExecutable());
        if (executableMetaData == null) {
            return 0;
        }
        if (group.isDefaultGroup()) {
            Iterator<Sequence> defaultGroupSequence = beanMetaData.getDefaultValidationSequence(bean);
            while (defaultGroupSequence.hasNext()) {
                Sequence sequence = defaultGroupSequence.next();
                Iterator<GroupWithInheritance> it = sequence.iterator();
                while (it.hasNext()) {
                    GroupWithInheritance expandedGroup = it.next();
                    int numberOfViolationsOfCurrentGroup = 0;
                    Iterator<Group> it2 = expandedGroup.iterator();
                    while (it2.hasNext()) {
                        Group defaultGroupSequenceElement = it2.next();
                        numberOfViolationsOfCurrentGroup += validateReturnValueForSingleGroup(validationContext, executableMetaData, bean, value, defaultGroupSequenceElement.getDefiningClass());
                        if (shouldFailFast(validationContext)) {
                            return validationContext.getFailingConstraints().size() - numberOfViolationsBefore;
                        }
                    }
                    if (numberOfViolationsOfCurrentGroup > 0) {
                        return validationContext.getFailingConstraints().size() - numberOfViolationsBefore;
                    }
                }
            }
        } else {
            validateReturnValueForSingleGroup(validationContext, executableMetaData, bean, value, group.getDefiningClass());
        }
        return validationContext.getFailingConstraints().size() - numberOfViolationsBefore;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <T> int validateReturnValueForSingleGroup(ValidationContext<T> validationContext, ExecutableMetaData executableMetaData, T bean, Object obj, Class<?> oneGroup) {
        int numberOfViolationsBefore = validationContext.getFailingConstraints().size();
        ValueContext<T, Object> executableValueContext = getExecutableValueContext(executableMetaData.getKind() == ElementKind.CONSTRUCTOR ? obj : bean, executableMetaData, oneGroup);
        executableValueContext.setCurrentValidatedValue(obj);
        ReturnValueMetaData returnValueMetaData = executableMetaData.getReturnValueMetaData();
        executableValueContext.appendNode(returnValueMetaData);
        executableValueContext.setUnwrapMode(returnValueMetaData.unwrapMode());
        int numberOfViolationsOfCurrentGroup = validateConstraintsForGroup(validationContext, executableValueContext, returnValueMetaData);
        if (shouldFailFast(validationContext)) {
            return validationContext.getFailingConstraints().size() - numberOfViolationsBefore;
        }
        if (!returnValueMetaData.isCascading()) {
            numberOfViolationsOfCurrentGroup += validateConstraintsForGroup(validationContext, executableValueContext, returnValueMetaData.getTypeArgumentsConstraints());
            if (shouldFailFast(validationContext)) {
                return validationContext.getFailingConstraints().size() - numberOfViolationsBefore;
            }
        }
        return numberOfViolationsOfCurrentGroup;
    }

    private int validateConstraintsForGroup(ValidationContext<?> validationContext, ValueContext<?, ?> valueContext, Iterable<MetaConstraint<?>> constraints) {
        int numberOfViolationsBefore = validationContext.getFailingConstraints().size();
        for (MetaConstraint<?> metaConstraint : constraints) {
            if (isValidationRequired(validationContext, valueContext, metaConstraint)) {
                metaConstraint.validateConstraint(validationContext, valueContext);
                if (shouldFailFast(validationContext)) {
                    break;
                }
            }
        }
        return validationContext.getFailingConstraints().size() - numberOfViolationsBefore;
    }

    private <V> ValueContext<?, V> collectMetaConstraintsForPathWithValue(ValidationContext<?> validationContext, PathImpl propertyPath, List<MetaConstraint<?>> metaConstraints, List<MetaConstraint<?>> typeArgumentConstraints) {
        Class<?> clazz = validationContext.getRootBeanClass();
        Object value = validationContext.getRootBean();
        PropertyMetaData propertyMetaData = null;
        Iterator<Path.Node> propertyPathIter = propertyPath.iterator();
        while (propertyPathIter.hasNext()) {
            NodeImpl propertyPathNode = (NodeImpl) propertyPathIter.next();
            propertyMetaData = getBeanPropertyMetaData(clazz, propertyPathNode);
            if (propertyPathIter.hasNext()) {
                if (!propertyMetaData.isCascading()) {
                    throw log.getInvalidPropertyPathException(validationContext.getRootBeanClass().getName(), propertyPath.asString());
                }
                value = getBeanPropertyValue(validationContext, value, propertyMetaData);
                if (value == null) {
                    throw log.getUnableToReachPropertyToValidateException(validationContext.getRootBean(), propertyPath);
                }
                clazz = value.getClass();
                if (propertyPathNode.isIterable()) {
                    NodeImpl propertyPathNode2 = (NodeImpl) propertyPathIter.next();
                    if (propertyPathNode2.getIndex() != null) {
                        value = ReflectionHelper.getIndexedValue(value, propertyPathNode2.getIndex());
                    } else if (propertyPathNode2.getKey() != null) {
                        value = ReflectionHelper.getMappedValue(value, propertyPathNode2.getKey());
                    } else {
                        throw log.getPropertyPathMustProvideIndexOrMapKeyException();
                    }
                    if (value == null) {
                        throw log.getUnableToReachPropertyToValidateException(validationContext.getRootBean(), propertyPath);
                    }
                    clazz = value.getClass();
                    propertyMetaData = getBeanPropertyMetaData(clazz, propertyPathNode2);
                } else {
                    continue;
                }
            }
        }
        if (propertyMetaData == null) {
            throw log.getInvalidPropertyPathException(clazz.getName(), propertyPath.asString());
        }
        metaConstraints.addAll(propertyMetaData.getConstraints());
        typeArgumentConstraints.addAll(propertyMetaData.getTypeArgumentsConstraints());
        return ValueContext.getLocalExecutionContext(value, (Validatable) null, propertyPath);
    }

    private <V> ValueContext<?, V> collectMetaConstraintsForPathWithoutValue(ValidationContext<?> validationContext, PathImpl propertyPath, List<MetaConstraint<?>> metaConstraints, List<MetaConstraint<?>> typeArgumentConstraints) {
        Class<?> clazz = validationContext.getRootBeanClass();
        PropertyMetaData propertyMetaData = null;
        Iterator<Path.Node> propertyPathIter = propertyPath.iterator();
        while (propertyPathIter.hasNext()) {
            NodeImpl propertyPathNode = (NodeImpl) propertyPathIter.next();
            propertyMetaData = getBeanPropertyMetaData(clazz, propertyPathNode);
            if (propertyPathIter.hasNext()) {
                if (propertyPathNode.isIterable()) {
                    NodeImpl propertyPathNode2 = (NodeImpl) propertyPathIter.next();
                    clazz = ReflectionHelper.getClassFromType(ReflectionHelper.getCollectionElementType(propertyMetaData.getType()));
                    propertyMetaData = getBeanPropertyMetaData(clazz, propertyPathNode2);
                } else {
                    clazz = ReflectionHelper.getClassFromType(propertyMetaData.getType());
                }
            }
        }
        if (propertyMetaData == null) {
            throw log.getInvalidPropertyPathException(clazz.getName(), propertyPath.asString());
        }
        metaConstraints.addAll(propertyMetaData.getConstraints());
        typeArgumentConstraints.addAll(propertyMetaData.getTypeArgumentsConstraints());
        return ValueContext.getLocalExecutionContext((Class) clazz, (Validatable) null, propertyPath);
    }

    private TraversableResolver getCachingTraversableResolver() {
        return new CachingTraversableResolverForSingleValidation(this.traversableResolver);
    }

    private boolean isValidationRequired(ValidationContext<?> validationContext, ValueContext<?, ?> valueContext, MetaConstraint<?> metaConstraint) {
        if (validationContext.hasMetaConstraintBeenProcessed(valueContext.getCurrentBean(), valueContext.getPropertyPath(), metaConstraint) || !metaConstraint.getGroupList().contains(valueContext.getCurrentGroup())) {
            return false;
        }
        return isReachable(validationContext, valueContext.getCurrentBean(), valueContext.getPropertyPath(), metaConstraint.getElementType());
    }

    private boolean isReachable(ValidationContext<?> validationContext, Object traversableObject, PathImpl path, ElementType type) {
        if (needToCallTraversableResolver(path, type)) {
            return true;
        }
        Path pathToObject = path.getPathWithoutLeafNode();
        try {
            return validationContext.getTraversableResolver().isReachable(traversableObject, path.getLeafNode(), validationContext.getRootBeanClass(), pathToObject, type);
        } catch (RuntimeException e) {
            throw log.getErrorDuringCallOfTraversableResolverIsReachableException(e);
        }
    }

    private boolean needToCallTraversableResolver(PathImpl path, ElementType type) {
        return isClassLevelConstraint(type) || isCrossParameterValidation(path) || isParameterValidation(path) || isReturnValueValidation(path);
    }

    private boolean isCascadeRequired(ValidationContext<?> validationContext, Object traversableObject, PathImpl path, ElementType type) {
        if (needToCallTraversableResolver(path, type)) {
            return true;
        }
        boolean isReachable = isReachable(validationContext, traversableObject, path, type);
        if (!isReachable) {
            return false;
        }
        Path pathToObject = path.getPathWithoutLeafNode();
        try {
            return validationContext.getTraversableResolver().isCascadable(traversableObject, path.getLeafNode(), validationContext.getRootBeanClass(), pathToObject, type);
        } catch (RuntimeException e) {
            throw log.getErrorDuringCallOfTraversableResolverIsCascadableException(e);
        }
    }

    private boolean isClassLevelConstraint(ElementType type) {
        return ElementType.TYPE.equals(type);
    }

    private boolean isCrossParameterValidation(PathImpl path) {
        return path.getLeafNode().getKind() == ElementKind.CROSS_PARAMETER;
    }

    private boolean isParameterValidation(PathImpl path) {
        return path.getLeafNode().getKind() == ElementKind.PARAMETER;
    }

    private boolean isReturnValueValidation(PathImpl path) {
        return path.getLeafNode().getKind() == ElementKind.RETURN_VALUE;
    }

    private boolean shouldFailFast(ValidationContext<?> context) {
        return context.isFailFastModeEnabled() && !context.getFailingConstraints().isEmpty();
    }

    private PropertyMetaData getBeanPropertyMetaData(Class<?> beanClass, Path.Node propertyNode) {
        if (!ElementKind.PROPERTY.equals(propertyNode.getKind())) {
            throw log.getInvalidPropertyPathException(beanClass.getName(), propertyNode.getName());
        }
        BeanMetaData<?> beanMetaData = this.beanMetaDataManager.getBeanMetaData(beanClass);
        PropertyMetaData propertyMetaData = beanMetaData.getMetaDataFor(propertyNode.getName());
        if (propertyMetaData == null) {
            throw log.getInvalidPropertyPathException(beanClass.getName(), propertyNode.getName());
        }
        return propertyMetaData;
    }

    private Object getBeanPropertyValue(ValidationContext<?> validationContext, Object object, Cascadable cascadable) {
        ValidatedValueUnwrapper valueHandler;
        Object value = cascadable.getValue(object);
        UnwrapMode unwrapMode = cascadable.unwrapMode();
        if ((UnwrapMode.UNWRAP.equals(unwrapMode) || UnwrapMode.AUTOMATIC.equals(unwrapMode)) && (valueHandler = validationContext.getValidatedValueUnwrapper(cascadable.getCascadableType())) != null) {
            value = valueHandler.handleValidatedValue(value);
        }
        return value;
    }

    private Object getBeanMemberValue(Object object, Member member) {
        if (member == null) {
            return object;
        }
        Member member2 = getAccessible(member);
        if (member2 instanceof Method) {
            return ReflectionHelper.getValue((Method) member2, object);
        }
        if (member2 instanceof Field) {
            return ReflectionHelper.getValue((Field) member2, object);
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private Member getAccessible(Member member) {
        Member member2;
        if (((AccessibleObject) member).isAccessible()) {
            return member;
        }
        Member member3 = this.accessibleMembers.get(member);
        if (member3 != null) {
            return member3;
        }
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(HibernateValidatorPermission.ACCESS_PRIVATE_MEMBERS);
        }
        Class<?> clazz = member.getDeclaringClass();
        if (member instanceof Field) {
            member2 = (Member) run(GetDeclaredField.action(clazz, member.getName()));
        } else {
            member2 = (Member) run(GetDeclaredMethod.action(clazz, member.getName(), new Class[0]));
        }
        run(SetAccessibility.action(member2));
        Member cached = this.accessibleMembers.putIfAbsent(member, member2);
        if (cached != null) {
            member2 = cached;
        }
        return member2;
    }

    private <T> T run(PrivilegedAction<T> privilegedAction) {
        return System.getSecurityManager() != null ? (T) AccessController.doPrivileged(privilegedAction) : privilegedAction.run();
    }
}
