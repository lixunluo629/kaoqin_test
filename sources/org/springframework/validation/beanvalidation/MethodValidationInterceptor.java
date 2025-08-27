package org.springframework.validation.beanvalidation;

import java.lang.reflect.Method;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.method.MethodConstraintViolation;
import org.hibernate.validator.method.MethodConstraintViolationException;
import org.hibernate.validator.method.MethodValidator;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.annotation.Validated;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/validation/beanvalidation/MethodValidationInterceptor.class */
public class MethodValidationInterceptor implements MethodInterceptor {
    private static Method forExecutablesMethod;
    private static Method validateParametersMethod;
    private static Method validateReturnValueMethod;
    private volatile Validator validator;

    static {
        try {
            forExecutablesMethod = Validator.class.getMethod("forExecutables", new Class[0]);
            Class<?> executableValidatorClass = forExecutablesMethod.getReturnType();
            validateParametersMethod = executableValidatorClass.getMethod("validateParameters", Object.class, Method.class, Object[].class, Class[].class);
            validateReturnValueMethod = executableValidatorClass.getMethod("validateReturnValue", Object.class, Method.class, Object.class, Class[].class);
        } catch (Exception e) {
        }
    }

    public MethodValidationInterceptor() {
        this(forExecutablesMethod != null ? Validation.buildDefaultValidatorFactory() : HibernateValidatorDelegate.buildValidatorFactory());
    }

    public MethodValidationInterceptor(ValidatorFactory validatorFactory) {
        this(validatorFactory.getValidator());
    }

    public MethodValidationInterceptor(Validator validator) {
        this.validator = validator;
    }

    @Override // org.aopalliance.intercept.MethodInterceptor
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object execVal;
        Set<ConstraintViolation<?>> result;
        if (isFactoryBeanMetadataMethod(invocation.getMethod())) {
            return invocation.proceed();
        }
        Class<?>[] groups = determineValidationGroups(invocation);
        if (forExecutablesMethod != null) {
            try {
                execVal = ReflectionUtils.invokeMethod(forExecutablesMethod, this.validator);
            } catch (AbstractMethodError e) {
                Validator nativeValidator = (Validator) this.validator.unwrap(Validator.class);
                execVal = ReflectionUtils.invokeMethod(forExecutablesMethod, nativeValidator);
                this.validator = nativeValidator;
            }
            Method methodToValidate = invocation.getMethod();
            try {
                result = (Set) ReflectionUtils.invokeMethod(validateParametersMethod, execVal, invocation.getThis(), methodToValidate, invocation.getArguments(), groups);
            } catch (IllegalArgumentException e2) {
                methodToValidate = BridgeMethodResolver.findBridgedMethod(ClassUtils.getMostSpecificMethod(invocation.getMethod(), invocation.getThis().getClass()));
                result = (Set) ReflectionUtils.invokeMethod(validateParametersMethod, execVal, invocation.getThis(), methodToValidate, invocation.getArguments(), groups);
            }
            if (!result.isEmpty()) {
                throw new ConstraintViolationException(result);
            }
            Object returnValue = invocation.proceed();
            Set<ConstraintViolation<?>> result2 = (Set) ReflectionUtils.invokeMethod(validateReturnValueMethod, execVal, invocation.getThis(), methodToValidate, returnValue, groups);
            if (!result2.isEmpty()) {
                throw new ConstraintViolationException(result2);
            }
            return returnValue;
        }
        return HibernateValidatorDelegate.invokeWithinValidation(invocation, this.validator, groups);
    }

    private boolean isFactoryBeanMetadataMethod(Method method) {
        Class<?> clazz = method.getDeclaringClass();
        if (clazz.isInterface()) {
            return (clazz == FactoryBean.class || clazz == SmartFactoryBean.class) && !method.getName().equals("getObject");
        }
        Class<?> factoryBeanType = null;
        if (SmartFactoryBean.class.isAssignableFrom(clazz)) {
            factoryBeanType = SmartFactoryBean.class;
        } else if (FactoryBean.class.isAssignableFrom(clazz)) {
            factoryBeanType = FactoryBean.class;
        }
        return (factoryBeanType == null || method.getName().equals("getObject") || !ClassUtils.hasMethod(factoryBeanType, method.getName(), method.getParameterTypes())) ? false : true;
    }

    protected Class<?>[] determineValidationGroups(MethodInvocation invocation) {
        Validated validatedAnn = (Validated) AnnotationUtils.findAnnotation(invocation.getMethod(), Validated.class);
        if (validatedAnn == null) {
            validatedAnn = (Validated) AnnotationUtils.findAnnotation(invocation.getThis().getClass(), Validated.class);
        }
        return validatedAnn != null ? validatedAnn.value() : new Class[0];
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/validation/beanvalidation/MethodValidationInterceptor$HibernateValidatorDelegate.class */
    private static class HibernateValidatorDelegate {
        private HibernateValidatorDelegate() {
        }

        public static ValidatorFactory buildValidatorFactory() {
            return ((HibernateValidatorConfiguration) Validation.byProvider(HibernateValidator.class).configure()).buildValidatorFactory();
        }

        /* JADX INFO: Thrown type has an unknown type hierarchy: org.hibernate.validator.method.MethodConstraintViolationException */
        public static Object invokeWithinValidation(MethodInvocation invocation, Validator validator, Class<?>[] groups) throws Throwable {
            MethodValidator methodValidator = (MethodValidator) validator.unwrap(MethodValidator.class);
            Set<MethodConstraintViolation<Object>> result = methodValidator.validateAllParameters(invocation.getThis(), invocation.getMethod(), invocation.getArguments(), groups);
            if (!result.isEmpty()) {
                throw new MethodConstraintViolationException(result);
            }
            Object returnValue = invocation.proceed();
            Set<MethodConstraintViolation<Object>> result2 = methodValidator.validateReturnValue(invocation.getThis(), invocation.getMethod(), returnValue, groups);
            if (!result2.isEmpty()) {
                throw new MethodConstraintViolationException(result2);
            }
            return returnValue;
        }
    }
}
