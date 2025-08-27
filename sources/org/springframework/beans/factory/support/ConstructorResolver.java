package org.springframework.beans.factory.support;

import java.beans.ConstructorProperties;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.NamedThreadLocal;
import org.springframework.util.ClassUtils;
import org.springframework.util.MethodInvoker;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/ConstructorResolver.class */
class ConstructorResolver {
    private static final NamedThreadLocal<InjectionPoint> currentInjectionPoint = new NamedThreadLocal<>("Current injection point");
    private final AbstractAutowireCapableBeanFactory beanFactory;

    public ConstructorResolver(AbstractAutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /* JADX WARN: Removed duplicated region for block: B:81:0x022e  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0238  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0248  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0261  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public org.springframework.beans.BeanWrapper autowireConstructor(final java.lang.String r11, final org.springframework.beans.factory.support.RootBeanDefinition r12, java.lang.reflect.Constructor<?>[] r13, java.lang.Object[] r14) {
        /*
            Method dump skipped, instructions count: 903
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.springframework.beans.factory.support.ConstructorResolver.autowireConstructor(java.lang.String, org.springframework.beans.factory.support.RootBeanDefinition, java.lang.reflect.Constructor[], java.lang.Object[]):org.springframework.beans.BeanWrapper");
    }

    public void resolveFactoryMethodIfPossible(RootBeanDefinition mbd) {
        Class<?> factoryClass;
        boolean isStatic;
        if (mbd.getFactoryBeanName() != null) {
            factoryClass = this.beanFactory.getType(mbd.getFactoryBeanName());
            isStatic = false;
        } else {
            factoryClass = mbd.getBeanClass();
            isStatic = true;
        }
        Method[] candidates = getCandidateMethods(ClassUtils.getUserClass(factoryClass), mbd);
        Method uniqueCandidate = null;
        int length = candidates.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            Method candidate = candidates[i];
            if (Modifier.isStatic(candidate.getModifiers()) == isStatic && mbd.isFactoryMethod(candidate)) {
                if (uniqueCandidate == null) {
                    uniqueCandidate = candidate;
                } else if (!Arrays.equals(uniqueCandidate.getParameterTypes(), candidate.getParameterTypes())) {
                    uniqueCandidate = null;
                    break;
                }
            }
            i++;
        }
        synchronized (mbd.constructorArgumentLock) {
            mbd.resolvedConstructorOrFactoryMethod = uniqueCandidate;
        }
    }

    private Method[] getCandidateMethods(final Class<?> factoryClass, final RootBeanDefinition mbd) {
        if (System.getSecurityManager() != null) {
            return (Method[]) AccessController.doPrivileged(new PrivilegedAction<Method[]>() { // from class: org.springframework.beans.factory.support.ConstructorResolver.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public Method[] run() {
                    return mbd.isNonPublicAccessAllowed() ? ReflectionUtils.getAllDeclaredMethods(factoryClass) : factoryClass.getMethods();
                }
            });
        }
        return mbd.isNonPublicAccessAllowed() ? ReflectionUtils.getAllDeclaredMethods(factoryClass) : factoryClass.getMethods();
    }

    /* JADX WARN: Removed duplicated region for block: B:93:0x02b7  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x02c1  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x02d1  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x02ea  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public org.springframework.beans.BeanWrapper instantiateUsingFactoryMethod(final java.lang.String r11, final org.springframework.beans.factory.support.RootBeanDefinition r12, java.lang.Object[] r13) {
        /*
            Method dump skipped, instructions count: 1473
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(java.lang.String, org.springframework.beans.factory.support.RootBeanDefinition, java.lang.Object[]):org.springframework.beans.BeanWrapper");
    }

    private int resolveConstructorArguments(String beanName, RootBeanDefinition mbd, BeanWrapper bw, ConstructorArgumentValues cargs, ConstructorArgumentValues resolvedValues) {
        TypeConverter customConverter = this.beanFactory.getCustomTypeConverter();
        TypeConverter converter = customConverter != null ? customConverter : bw;
        BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this.beanFactory, beanName, mbd, converter);
        int minNrOfArgs = cargs.getArgumentCount();
        for (Map.Entry<Integer, ConstructorArgumentValues.ValueHolder> entry : cargs.getIndexedArgumentValues().entrySet()) {
            int index = entry.getKey().intValue();
            if (index < 0) {
                throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Invalid constructor argument index: " + index);
            }
            if (index > minNrOfArgs) {
                minNrOfArgs = index + 1;
            }
            ConstructorArgumentValues.ValueHolder valueHolder = entry.getValue();
            if (valueHolder.isConverted()) {
                resolvedValues.addIndexedArgumentValue(index, valueHolder);
            } else {
                Object resolvedValue = valueResolver.resolveValueIfNecessary("constructor argument", valueHolder.getValue());
                ConstructorArgumentValues.ValueHolder resolvedValueHolder = new ConstructorArgumentValues.ValueHolder(resolvedValue, valueHolder.getType(), valueHolder.getName());
                resolvedValueHolder.setSource(valueHolder);
                resolvedValues.addIndexedArgumentValue(index, resolvedValueHolder);
            }
        }
        for (ConstructorArgumentValues.ValueHolder valueHolder2 : cargs.getGenericArgumentValues()) {
            if (valueHolder2.isConverted()) {
                resolvedValues.addGenericArgumentValue(valueHolder2);
            } else {
                Object resolvedValue2 = valueResolver.resolveValueIfNecessary("constructor argument", valueHolder2.getValue());
                ConstructorArgumentValues.ValueHolder resolvedValueHolder2 = new ConstructorArgumentValues.ValueHolder(resolvedValue2, valueHolder2.getType(), valueHolder2.getName());
                resolvedValueHolder2.setSource(valueHolder2);
                resolvedValues.addGenericArgumentValue(resolvedValueHolder2);
            }
        }
        return minNrOfArgs;
    }

    private ArgumentsHolder createArgumentArray(String beanName, RootBeanDefinition mbd, ConstructorArgumentValues resolvedValues, BeanWrapper bw, Class<?>[] paramTypes, String[] paramNames, Object methodOrCtor, boolean autowiring) throws UnsatisfiedDependencyException {
        Object convertedValue;
        TypeConverter customConverter = this.beanFactory.getCustomTypeConverter();
        TypeConverter converter = customConverter != null ? customConverter : bw;
        ArgumentsHolder args = new ArgumentsHolder(paramTypes.length);
        Set<ConstructorArgumentValues.ValueHolder> usedValueHolders = new HashSet<>(paramTypes.length);
        Set<String> autowiredBeanNames = new LinkedHashSet<>(4);
        for (int paramIndex = 0; paramIndex < paramTypes.length; paramIndex++) {
            Class<?> paramType = paramTypes[paramIndex];
            String paramName = paramNames != null ? paramNames[paramIndex] : "";
            ConstructorArgumentValues.ValueHolder valueHolder = resolvedValues.getArgumentValue(paramIndex, paramType, paramName, usedValueHolders);
            if (valueHolder == null && (!autowiring || paramTypes.length == resolvedValues.getArgumentCount())) {
                valueHolder = resolvedValues.getGenericArgumentValue(null, null, usedValueHolders);
            }
            if (valueHolder != null) {
                usedValueHolders.add(valueHolder);
                Object originalValue = valueHolder.getValue();
                if (valueHolder.isConverted()) {
                    convertedValue = valueHolder.getConvertedValue();
                    args.preparedArguments[paramIndex] = convertedValue;
                } else {
                    ConstructorArgumentValues.ValueHolder sourceHolder = (ConstructorArgumentValues.ValueHolder) valueHolder.getSource();
                    Object sourceValue = sourceHolder.getValue();
                    MethodParameter methodParam = MethodParameter.forMethodOrConstructor(methodOrCtor, paramIndex);
                    try {
                        convertedValue = converter.convertIfNecessary(originalValue, paramType, methodParam);
                        args.resolveNecessary = true;
                        args.preparedArguments[paramIndex] = sourceValue;
                    } catch (TypeMismatchException ex) {
                        throw new UnsatisfiedDependencyException(mbd.getResourceDescription(), beanName, new InjectionPoint(methodParam), "Could not convert argument value of type [" + ObjectUtils.nullSafeClassName(valueHolder.getValue()) + "] to required type [" + paramType.getName() + "]: " + ex.getMessage());
                    }
                }
                args.arguments[paramIndex] = convertedValue;
                args.rawArguments[paramIndex] = originalValue;
            } else {
                MethodParameter methodParam2 = MethodParameter.forMethodOrConstructor(methodOrCtor, paramIndex);
                if (!autowiring) {
                    throw new UnsatisfiedDependencyException(mbd.getResourceDescription(), beanName, new InjectionPoint(methodParam2), "Ambiguous argument values for parameter of type [" + paramType.getName() + "] - did you specify the correct bean references as arguments?");
                }
                try {
                    Object autowiredArgument = resolveAutowiredArgument(methodParam2, beanName, autowiredBeanNames, converter);
                    args.rawArguments[paramIndex] = autowiredArgument;
                    args.arguments[paramIndex] = autowiredArgument;
                    args.preparedArguments[paramIndex] = new AutowiredArgumentMarker();
                    args.resolveNecessary = true;
                } catch (BeansException ex2) {
                    throw new UnsatisfiedDependencyException(mbd.getResourceDescription(), beanName, new InjectionPoint(methodParam2), ex2);
                }
            }
        }
        for (String autowiredBeanName : autowiredBeanNames) {
            this.beanFactory.registerDependentBean(autowiredBeanName, beanName);
            if (this.beanFactory.logger.isDebugEnabled()) {
                this.beanFactory.logger.debug("Autowiring by type from bean name '" + beanName + "' via " + (methodOrCtor instanceof Constructor ? "constructor" : "factory method") + " to bean named '" + autowiredBeanName + "'");
            }
        }
        return args;
    }

    private Object[] resolvePreparedArguments(String beanName, RootBeanDefinition mbd, BeanWrapper bw, Member methodOrCtor, Object[] argsToResolve) {
        TypeConverter customConverter = this.beanFactory.getCustomTypeConverter();
        TypeConverter converter = customConverter != null ? customConverter : bw;
        BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this.beanFactory, beanName, mbd, converter);
        Class<?>[] paramTypes = methodOrCtor instanceof Method ? ((Method) methodOrCtor).getParameterTypes() : ((Constructor) methodOrCtor).getParameterTypes();
        Object[] resolvedArgs = new Object[argsToResolve.length];
        for (int argIndex = 0; argIndex < argsToResolve.length; argIndex++) {
            Object argValue = argsToResolve[argIndex];
            MethodParameter methodParam = MethodParameter.forMethodOrConstructor(methodOrCtor, argIndex);
            GenericTypeResolver.resolveParameterType(methodParam, methodOrCtor.getDeclaringClass());
            if (argValue instanceof AutowiredArgumentMarker) {
                argValue = resolveAutowiredArgument(methodParam, beanName, null, converter);
            } else if (argValue instanceof BeanMetadataElement) {
                argValue = valueResolver.resolveValueIfNecessary("constructor argument", argValue);
            } else if (argValue instanceof String) {
                argValue = this.beanFactory.evaluateBeanDefinitionString((String) argValue, mbd);
            }
            Class<?> paramType = paramTypes[argIndex];
            try {
                resolvedArgs[argIndex] = converter.convertIfNecessary(argValue, paramType, methodParam);
            } catch (TypeMismatchException ex) {
                throw new UnsatisfiedDependencyException(mbd.getResourceDescription(), beanName, new InjectionPoint(methodParam), "Could not convert argument value of type [" + ObjectUtils.nullSafeClassName(argValue) + "] to required type [" + paramType.getName() + "]: " + ex.getMessage());
            }
        }
        return resolvedArgs;
    }

    protected Constructor<?> getUserDeclaredConstructor(Constructor<?> constructor) {
        Class<?> declaringClass = constructor.getDeclaringClass();
        Class<?> userClass = ClassUtils.getUserClass(declaringClass);
        if (userClass != declaringClass) {
            try {
                return userClass.getDeclaredConstructor(constructor.getParameterTypes());
            } catch (NoSuchMethodException e) {
            }
        }
        return constructor;
    }

    protected Object resolveAutowiredArgument(MethodParameter param, String beanName, Set<String> autowiredBeanNames, TypeConverter typeConverter) {
        if (InjectionPoint.class.isAssignableFrom(param.getParameterType())) {
            InjectionPoint injectionPoint = currentInjectionPoint.get();
            if (injectionPoint == null) {
                throw new IllegalStateException("No current InjectionPoint available for " + param);
            }
            return injectionPoint;
        }
        return this.beanFactory.resolveDependency(new DependencyDescriptor(param, true), beanName, autowiredBeanNames, typeConverter);
    }

    static InjectionPoint setCurrentInjectionPoint(InjectionPoint injectionPoint) {
        InjectionPoint old = currentInjectionPoint.get();
        if (injectionPoint != null) {
            currentInjectionPoint.set(injectionPoint);
        } else {
            currentInjectionPoint.remove();
        }
        return old;
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/ConstructorResolver$ArgumentsHolder.class */
    private static class ArgumentsHolder {
        public final Object[] rawArguments;
        public final Object[] arguments;
        public final Object[] preparedArguments;
        public boolean resolveNecessary = false;

        public ArgumentsHolder(int size) {
            this.rawArguments = new Object[size];
            this.arguments = new Object[size];
            this.preparedArguments = new Object[size];
        }

        public ArgumentsHolder(Object[] args) {
            this.rawArguments = args;
            this.arguments = args;
            this.preparedArguments = args;
        }

        public int getTypeDifferenceWeight(Class<?>[] paramTypes) {
            int typeDiffWeight = MethodInvoker.getTypeDifferenceWeight(paramTypes, this.arguments);
            int rawTypeDiffWeight = MethodInvoker.getTypeDifferenceWeight(paramTypes, this.rawArguments) - 1024;
            return rawTypeDiffWeight < typeDiffWeight ? rawTypeDiffWeight : typeDiffWeight;
        }

        public int getAssignabilityWeight(Class<?>[] paramTypes) {
            for (int i = 0; i < paramTypes.length; i++) {
                if (!ClassUtils.isAssignableValue(paramTypes[i], this.arguments[i])) {
                    return Integer.MAX_VALUE;
                }
            }
            for (int i2 = 0; i2 < paramTypes.length; i2++) {
                if (!ClassUtils.isAssignableValue(paramTypes[i2], this.rawArguments[i2])) {
                    return 2147483135;
                }
            }
            return 2147482623;
        }

        public void storeCache(RootBeanDefinition mbd, Object constructorOrFactoryMethod) {
            synchronized (mbd.constructorArgumentLock) {
                mbd.resolvedConstructorOrFactoryMethod = constructorOrFactoryMethod;
                mbd.constructorArgumentsResolved = true;
                if (this.resolveNecessary) {
                    mbd.preparedConstructorArguments = this.preparedArguments;
                } else {
                    mbd.resolvedConstructorArguments = this.arguments;
                }
            }
        }
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/ConstructorResolver$AutowiredArgumentMarker.class */
    private static class AutowiredArgumentMarker {
        private AutowiredArgumentMarker() {
        }
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/ConstructorResolver$ConstructorPropertiesChecker.class */
    private static class ConstructorPropertiesChecker {
        private ConstructorPropertiesChecker() {
        }

        public static String[] evaluate(Constructor<?> candidate, int paramCount) {
            ConstructorProperties cp = candidate.getAnnotation(ConstructorProperties.class);
            if (cp != null) {
                String[] names = cp.value();
                if (names.length != paramCount) {
                    throw new IllegalStateException("Constructor annotated with @ConstructorProperties but not corresponding to actual number of parameters (" + paramCount + "): " + candidate);
                }
                return names;
            }
            return null;
        }
    }
}
