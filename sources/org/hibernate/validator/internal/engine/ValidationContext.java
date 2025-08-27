package org.hibernate.validator.internal.engine;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import java.lang.annotation.ElementType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.ConstraintViolation;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.Path;
import javax.validation.TraversableResolver;
import javax.validation.ValidationException;
import javax.validation.metadata.ConstraintDescriptor;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorManager;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintViolationCreationContext;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;
import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.IdentitySet;
import org.hibernate.validator.internal.util.TypeHelper;
import org.hibernate.validator.internal.util.TypeResolutionHelper;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.spi.time.TimeProvider;
import org.hibernate.validator.spi.valuehandling.ValidatedValueUnwrapper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/ValidationContext.class */
public class ValidationContext<T> {
    private static final Log log = LoggerFactory.make();
    private final ConstraintValidatorManager constraintValidatorManager;
    private final T rootBean;
    private final Class<T> rootBeanClass;
    private final ExecutableElement executable;
    private final Object[] executableParameters;
    private final Object executableReturnValue;
    private final Map<Class<?>, IdentitySet> processedBeansPerGroup;
    private final Map<Object, Set<PathImpl>> processedPathsPerBean;
    private final Map<BeanAndPath, IdentitySet> processedMetaConstraints;
    private final Set<ConstraintViolation<T>> failingConstraintViolations;
    private final MessageInterpolator messageInterpolator;
    private final ConstraintValidatorFactory constraintValidatorFactory;
    private final TraversableResolver traversableResolver;
    private final ParameterNameProvider parameterNameProvider;
    private final List<ValidatedValueUnwrapper<?>> validatedValueUnwrappers;
    private final TypeResolutionHelper typeResolutionHelper;
    private final boolean failFast;
    private final TimeProvider timeProvider;

    private ValidationContext(ConstraintValidatorManager constraintValidatorManager, MessageInterpolator messageInterpolator, ConstraintValidatorFactory constraintValidatorFactory, TraversableResolver traversableResolver, ParameterNameProvider parameterNameProvider, TimeProvider timeProvider, List<ValidatedValueUnwrapper<?>> validatedValueUnwrappers, TypeResolutionHelper typeResolutionHelper, boolean failFast, T rootBean, Class<T> rootBeanClass, ExecutableElement executable, Object[] executableParameters, Object executableReturnValue) {
        this.constraintValidatorManager = constraintValidatorManager;
        this.messageInterpolator = messageInterpolator;
        this.constraintValidatorFactory = constraintValidatorFactory;
        this.traversableResolver = traversableResolver;
        this.parameterNameProvider = parameterNameProvider;
        this.timeProvider = timeProvider;
        this.validatedValueUnwrappers = validatedValueUnwrappers;
        this.typeResolutionHelper = typeResolutionHelper;
        this.failFast = failFast;
        this.rootBean = rootBean;
        this.rootBeanClass = rootBeanClass;
        this.executable = executable;
        this.executableParameters = executableParameters;
        this.executableReturnValue = executableReturnValue;
        this.processedBeansPerGroup = CollectionHelper.newHashMap();
        this.processedPathsPerBean = new IdentityHashMap();
        this.processedMetaConstraints = CollectionHelper.newHashMap();
        this.failingConstraintViolations = CollectionHelper.newHashSet();
    }

    public static ValidationContextBuilder getValidationContext(ConstraintValidatorManager constraintValidatorManager, MessageInterpolator messageInterpolator, ConstraintValidatorFactory constraintValidatorFactory, TraversableResolver traversableResolver, TimeProvider timeProvider, List<ValidatedValueUnwrapper<?>> validatedValueUnwrappers, TypeResolutionHelper typeResolutionHelper, boolean failFast) {
        return new ValidationContextBuilder(constraintValidatorManager, messageInterpolator, constraintValidatorFactory, traversableResolver, timeProvider, validatedValueUnwrappers, typeResolutionHelper, failFast);
    }

    public T getRootBean() {
        return this.rootBean;
    }

    public Class<T> getRootBeanClass() {
        return this.rootBeanClass;
    }

    public ExecutableElement getExecutable() {
        return this.executable;
    }

    public TraversableResolver getTraversableResolver() {
        return this.traversableResolver;
    }

    public boolean isFailFastModeEnabled() {
        return this.failFast;
    }

    public ConstraintValidatorManager getConstraintValidatorManager() {
        return this.constraintValidatorManager;
    }

    public List<String> getParameterNames() {
        if (this.parameterNameProvider == null) {
            return null;
        }
        if (this.executable.getElementType() == ElementType.METHOD) {
            return this.parameterNameProvider.getParameterNames((Method) this.executable.getMember());
        }
        return this.parameterNameProvider.getParameterNames((Constructor<?>) this.executable.getMember());
    }

    public TimeProvider getTimeProvider() {
        return this.timeProvider;
    }

    public Set<ConstraintViolation<T>> createConstraintViolations(ValueContext<?, ?> localContext, ConstraintValidatorContextImpl constraintValidatorContext) {
        Set<ConstraintViolation<T>> constraintViolations = CollectionHelper.newHashSet();
        for (ConstraintViolationCreationContext constraintViolationCreationContext : constraintValidatorContext.getConstraintViolationCreationContexts()) {
            ConstraintViolation<T> violation = createConstraintViolation(localContext, constraintViolationCreationContext, constraintValidatorContext.getConstraintDescriptor());
            constraintViolations.add(violation);
        }
        return constraintViolations;
    }

    public ConstraintValidatorFactory getConstraintValidatorFactory() {
        return this.constraintValidatorFactory;
    }

    public boolean isBeanAlreadyValidated(Object value, Class<?> group, PathImpl path) {
        boolean alreadyValidated = isAlreadyValidatedForCurrentGroup(value, group);
        if (alreadyValidated) {
            alreadyValidated = isAlreadyValidatedForPath(value, path);
        }
        return alreadyValidated;
    }

    public void markCurrentBeanAsProcessed(ValueContext<?, ?> valueContext) {
        markCurrentBeanAsProcessedForCurrentGroup(valueContext.getCurrentBean(), valueContext.getCurrentGroup());
        markCurrentBeanAsProcessedForCurrentPath(valueContext.getCurrentBean(), valueContext.getPropertyPath());
    }

    public void addConstraintFailures(Set<ConstraintViolation<T>> failingConstraintViolations) {
        this.failingConstraintViolations.addAll(failingConstraintViolations);
    }

    public Set<ConstraintViolation<T>> getFailingConstraints() {
        return this.failingConstraintViolations;
    }

    public ConstraintViolation<T> createConstraintViolation(ValueContext<?, ?> localContext, ConstraintViolationCreationContext constraintViolationCreationContext, ConstraintDescriptor<?> descriptor) {
        String messageTemplate = constraintViolationCreationContext.getMessage();
        String interpolatedMessage = interpolate(messageTemplate, localContext.getCurrentValidatedValue(), descriptor, constraintViolationCreationContext.getExpressionVariables());
        Path path = PathImpl.createCopy(constraintViolationCreationContext.getPath());
        Map<String, Object> expressionVariables = Collections.unmodifiableMap(constraintViolationCreationContext.getExpressionVariables());
        Object dynamicPayload = constraintViolationCreationContext.getDynamicPayload();
        if (this.executableParameters != null) {
            return ConstraintViolationImpl.forParameterValidation(messageTemplate, expressionVariables, interpolatedMessage, getRootBeanClass(), getRootBean(), localContext.getCurrentBean(), localContext.getCurrentValidatedValue(), path, descriptor, localContext.getElementType(), this.executableParameters, dynamicPayload);
        }
        if (this.executableReturnValue != null) {
            return ConstraintViolationImpl.forReturnValueValidation(messageTemplate, expressionVariables, interpolatedMessage, getRootBeanClass(), getRootBean(), localContext.getCurrentBean(), localContext.getCurrentValidatedValue(), path, descriptor, localContext.getElementType(), this.executableReturnValue, dynamicPayload);
        }
        return ConstraintViolationImpl.forBeanValidation(messageTemplate, expressionVariables, interpolatedMessage, getRootBeanClass(), getRootBean(), localContext.getCurrentBean(), localContext.getCurrentValidatedValue(), path, descriptor, localContext.getElementType(), dynamicPayload);
    }

    public boolean hasMetaConstraintBeenProcessed(Object bean, Path path, MetaConstraint<?> metaConstraint) {
        IdentitySet processedConstraints = this.processedMetaConstraints.get(new BeanAndPath(bean, path));
        return processedConstraints != null && processedConstraints.contains(metaConstraint);
    }

    public void markConstraintProcessed(Object bean, Path path, MetaConstraint<?> metaConstraint) {
        BeanAndPath beanAndPath = new BeanAndPath(bean, path);
        if (this.processedMetaConstraints.containsKey(beanAndPath)) {
            this.processedMetaConstraints.get(beanAndPath).add(metaConstraint);
            return;
        }
        IdentitySet set = new IdentitySet();
        set.add(metaConstraint);
        this.processedMetaConstraints.put(beanAndPath, set);
    }

    public ValidatedValueUnwrapper<?> getValidatedValueUnwrapper(Type type) throws NegativeArraySizeException {
        TypeResolver typeResolver = this.typeResolutionHelper.getTypeResolver();
        for (ValidatedValueUnwrapper<?> handler : this.validatedValueUnwrappers) {
            ResolvedType handlerType = typeResolver.resolve(handler.getClass(), new Type[0]);
            List<ResolvedType> typeParameters = handlerType.typeParametersFor(ValidatedValueUnwrapper.class);
            if (TypeHelper.isAssignable(typeParameters.get(0).getErasedType(), type)) {
                return handler;
            }
        }
        return null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ValidationContext");
        sb.append("{rootBean=").append(this.rootBean);
        sb.append('}');
        return sb.toString();
    }

    private String interpolate(String messageTemplate, Object validatedValue, ConstraintDescriptor<?> descriptor, Map<String, Object> messageParameters) {
        MessageInterpolatorContext context = new MessageInterpolatorContext(descriptor, validatedValue, getRootBeanClass(), messageParameters);
        try {
            return this.messageInterpolator.interpolate(messageTemplate, context);
        } catch (ValidationException ve) {
            throw ve;
        } catch (Exception e) {
            throw log.getExceptionOccurredDuringMessageInterpolationException(e);
        }
    }

    private boolean isAlreadyValidatedForPath(Object value, PathImpl path) {
        Set<PathImpl> pathSet = this.processedPathsPerBean.get(value);
        if (pathSet == null) {
            return false;
        }
        for (PathImpl p : pathSet) {
            if (path.isRootPath() || p.isRootPath() || isSubPathOf(path, p) || isSubPathOf(p, path)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSubPathOf(Path p1, Path p2) {
        Iterator<Path.Node> p2Iter = p2.iterator();
        for (Path.Node p1Node : p1) {
            if (!p2Iter.hasNext()) {
                return false;
            }
            Path.Node p2Node = p2Iter.next();
            if (!p1Node.equals(p2Node)) {
                return false;
            }
        }
        return true;
    }

    private boolean isAlreadyValidatedForCurrentGroup(Object value, Class<?> group) {
        IdentitySet objectsProcessedInCurrentGroups = this.processedBeansPerGroup.get(group);
        return objectsProcessedInCurrentGroups != null && objectsProcessedInCurrentGroups.contains(value);
    }

    private void markCurrentBeanAsProcessedForCurrentPath(Object value, PathImpl path) {
        PathImpl path2 = PathImpl.createCopy(path);
        if (this.processedPathsPerBean.containsKey(value)) {
            this.processedPathsPerBean.get(value).add(path2);
            return;
        }
        Set<PathImpl> set = new HashSet<>();
        set.add(path2);
        this.processedPathsPerBean.put(value, set);
    }

    private void markCurrentBeanAsProcessedForCurrentGroup(Object value, Class<?> group) {
        if (this.processedBeansPerGroup.containsKey(group)) {
            this.processedBeansPerGroup.get(group).add(value);
            return;
        }
        IdentitySet set = new IdentitySet();
        set.add(value);
        this.processedBeansPerGroup.put(group, set);
    }

    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/ValidationContext$ValidationContextBuilder.class */
    public static class ValidationContextBuilder {
        private final ConstraintValidatorManager constraintValidatorManager;
        private final MessageInterpolator messageInterpolator;
        private final ConstraintValidatorFactory constraintValidatorFactory;
        private final TraversableResolver traversableResolver;
        private final TimeProvider timeProvider;
        private final List<ValidatedValueUnwrapper<?>> validatedValueUnwrappers;
        private final TypeResolutionHelper typeResolutionHelper;
        private final boolean failFast;

        private ValidationContextBuilder(ConstraintValidatorManager constraintValidatorManager, MessageInterpolator messageInterpolator, ConstraintValidatorFactory constraintValidatorFactory, TraversableResolver traversableResolver, TimeProvider timeProvider, List<ValidatedValueUnwrapper<?>> validatedValueUnwrappers, TypeResolutionHelper typeResolutionHelper, boolean failFast) {
            this.constraintValidatorManager = constraintValidatorManager;
            this.messageInterpolator = messageInterpolator;
            this.constraintValidatorFactory = constraintValidatorFactory;
            this.traversableResolver = traversableResolver;
            this.timeProvider = timeProvider;
            this.validatedValueUnwrappers = validatedValueUnwrappers;
            this.typeResolutionHelper = typeResolutionHelper;
            this.failFast = failFast;
        }

        public <T> ValidationContext<T> forValidate(T rootBean) {
            return new ValidationContext<>(this.constraintValidatorManager, this.messageInterpolator, this.constraintValidatorFactory, this.traversableResolver, null, this.timeProvider, this.validatedValueUnwrappers, this.typeResolutionHelper, this.failFast, rootBean, rootBean.getClass(), null, null, null);
        }

        public <T> ValidationContext<T> forValidateProperty(T rootBean) {
            return new ValidationContext<>(this.constraintValidatorManager, this.messageInterpolator, this.constraintValidatorFactory, this.traversableResolver, null, this.timeProvider, this.validatedValueUnwrappers, this.typeResolutionHelper, this.failFast, rootBean, rootBean.getClass(), null, null, null);
        }

        public <T> ValidationContext<T> forValidateValue(Class<T> rootBeanClass) {
            return new ValidationContext<>(this.constraintValidatorManager, this.messageInterpolator, this.constraintValidatorFactory, this.traversableResolver, null, this.timeProvider, this.validatedValueUnwrappers, this.typeResolutionHelper, this.failFast, null, rootBeanClass, null, null, null);
        }

        public <T> ValidationContext<T> forValidateParameters(ParameterNameProvider parameterNameProvider, T rootBean, ExecutableElement executable, Object[] executableParameters) {
            return new ValidationContext<>(this.constraintValidatorManager, this.messageInterpolator, this.constraintValidatorFactory, this.traversableResolver, parameterNameProvider, this.timeProvider, this.validatedValueUnwrappers, this.typeResolutionHelper, this.failFast, rootBean, rootBean != null ? rootBean.getClass() : executable.getMember().getDeclaringClass(), executable, executableParameters, null);
        }

        public <T> ValidationContext<T> forValidateReturnValue(T rootBean, ExecutableElement executable, Object executableReturnValue) {
            return new ValidationContext<>(this.constraintValidatorManager, this.messageInterpolator, this.constraintValidatorFactory, this.traversableResolver, null, this.timeProvider, this.validatedValueUnwrappers, this.typeResolutionHelper, this.failFast, rootBean, rootBean != null ? rootBean.getClass() : executable.getMember().getDeclaringClass(), executable, null, executableReturnValue);
        }
    }

    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/ValidationContext$BeanAndPath.class */
    private static final class BeanAndPath {
        private final Object bean;
        private final Path path;
        private final int hashCode;

        private BeanAndPath(Object bean, Path path) {
            this.bean = bean;
            this.path = path;
            this.hashCode = createHashCode();
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            BeanAndPath that = (BeanAndPath) o;
            if (this.bean != that.bean || !this.path.equals(that.path)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return this.hashCode;
        }

        private int createHashCode() {
            int result = System.identityHashCode(this.bean);
            return (31 * result) + this.path.hashCode();
        }
    }
}
