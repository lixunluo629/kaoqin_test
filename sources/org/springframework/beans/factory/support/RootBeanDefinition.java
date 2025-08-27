package org.springframework.beans.factory.support;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/RootBeanDefinition.class */
public class RootBeanDefinition extends AbstractBeanDefinition {
    private BeanDefinitionHolder decoratedDefinition;
    private AnnotatedElement qualifiedElement;
    boolean allowCaching;
    boolean isFactoryMethodUnique;
    volatile ResolvableType targetType;
    volatile Class<?> resolvedTargetType;
    volatile ResolvableType factoryMethodReturnType;
    final Object constructorArgumentLock;
    Object resolvedConstructorOrFactoryMethod;
    boolean constructorArgumentsResolved;
    Object[] resolvedConstructorArguments;
    Object[] preparedConstructorArguments;
    final Object postProcessingLock;
    boolean postProcessed;
    volatile Boolean beforeInstantiationResolved;
    private Set<Member> externallyManagedConfigMembers;
    private Set<String> externallyManagedInitMethods;
    private Set<String> externallyManagedDestroyMethods;

    public RootBeanDefinition() {
        this.allowCaching = true;
        this.isFactoryMethodUnique = false;
        this.constructorArgumentLock = new Object();
        this.constructorArgumentsResolved = false;
        this.postProcessingLock = new Object();
        this.postProcessed = false;
    }

    public RootBeanDefinition(Class<?> beanClass) {
        this.allowCaching = true;
        this.isFactoryMethodUnique = false;
        this.constructorArgumentLock = new Object();
        this.constructorArgumentsResolved = false;
        this.postProcessingLock = new Object();
        this.postProcessed = false;
        setBeanClass(beanClass);
    }

    public RootBeanDefinition(Class<?> beanClass, int autowireMode, boolean dependencyCheck) {
        this.allowCaching = true;
        this.isFactoryMethodUnique = false;
        this.constructorArgumentLock = new Object();
        this.constructorArgumentsResolved = false;
        this.postProcessingLock = new Object();
        this.postProcessed = false;
        setBeanClass(beanClass);
        setAutowireMode(autowireMode);
        if (dependencyCheck && getResolvedAutowireMode() != 3) {
            setDependencyCheck(1);
        }
    }

    public RootBeanDefinition(Class<?> beanClass, ConstructorArgumentValues cargs, MutablePropertyValues pvs) {
        super(cargs, pvs);
        this.allowCaching = true;
        this.isFactoryMethodUnique = false;
        this.constructorArgumentLock = new Object();
        this.constructorArgumentsResolved = false;
        this.postProcessingLock = new Object();
        this.postProcessed = false;
        setBeanClass(beanClass);
    }

    public RootBeanDefinition(String beanClassName) {
        this.allowCaching = true;
        this.isFactoryMethodUnique = false;
        this.constructorArgumentLock = new Object();
        this.constructorArgumentsResolved = false;
        this.postProcessingLock = new Object();
        this.postProcessed = false;
        setBeanClassName(beanClassName);
    }

    public RootBeanDefinition(String beanClassName, ConstructorArgumentValues cargs, MutablePropertyValues pvs) {
        super(cargs, pvs);
        this.allowCaching = true;
        this.isFactoryMethodUnique = false;
        this.constructorArgumentLock = new Object();
        this.constructorArgumentsResolved = false;
        this.postProcessingLock = new Object();
        this.postProcessed = false;
        setBeanClassName(beanClassName);
    }

    public RootBeanDefinition(RootBeanDefinition original) {
        super(original);
        this.allowCaching = true;
        this.isFactoryMethodUnique = false;
        this.constructorArgumentLock = new Object();
        this.constructorArgumentsResolved = false;
        this.postProcessingLock = new Object();
        this.postProcessed = false;
        this.decoratedDefinition = original.decoratedDefinition;
        this.qualifiedElement = original.qualifiedElement;
        this.allowCaching = original.allowCaching;
        this.isFactoryMethodUnique = original.isFactoryMethodUnique;
        this.targetType = original.targetType;
    }

    RootBeanDefinition(BeanDefinition original) {
        super(original);
        this.allowCaching = true;
        this.isFactoryMethodUnique = false;
        this.constructorArgumentLock = new Object();
        this.constructorArgumentsResolved = false;
        this.postProcessingLock = new Object();
        this.postProcessed = false;
    }

    @Override // org.springframework.beans.factory.config.BeanDefinition
    public String getParentName() {
        return null;
    }

    @Override // org.springframework.beans.factory.config.BeanDefinition
    public void setParentName(String parentName) {
        if (parentName != null) {
            throw new IllegalArgumentException("Root bean cannot be changed into a child bean with parent reference");
        }
    }

    public void setDecoratedDefinition(BeanDefinitionHolder decoratedDefinition) {
        this.decoratedDefinition = decoratedDefinition;
    }

    public BeanDefinitionHolder getDecoratedDefinition() {
        return this.decoratedDefinition;
    }

    public void setQualifiedElement(AnnotatedElement qualifiedElement) {
        this.qualifiedElement = qualifiedElement;
    }

    public AnnotatedElement getQualifiedElement() {
        return this.qualifiedElement;
    }

    public void setTargetType(ResolvableType targetType) {
        this.targetType = targetType;
    }

    public void setTargetType(Class<?> targetType) {
        this.targetType = targetType != null ? ResolvableType.forClass(targetType) : null;
    }

    public Class<?> getTargetType() {
        if (this.resolvedTargetType != null) {
            return this.resolvedTargetType;
        }
        if (this.targetType != null) {
            return this.targetType.resolve();
        }
        return null;
    }

    public void setUniqueFactoryMethodName(String name) {
        Assert.hasText(name, "Factory method name must not be empty");
        setFactoryMethodName(name);
        this.isFactoryMethodUnique = true;
    }

    public boolean isFactoryMethod(Method candidate) {
        return candidate != null && candidate.getName().equals(getFactoryMethodName());
    }

    public Method getResolvedFactoryMethod() {
        Method method;
        synchronized (this.constructorArgumentLock) {
            Object candidate = this.resolvedConstructorOrFactoryMethod;
            method = candidate instanceof Method ? (Method) candidate : null;
        }
        return method;
    }

    public void registerExternallyManagedConfigMember(Member configMember) {
        synchronized (this.postProcessingLock) {
            if (this.externallyManagedConfigMembers == null) {
                this.externallyManagedConfigMembers = new HashSet(1);
            }
            this.externallyManagedConfigMembers.add(configMember);
        }
    }

    public boolean isExternallyManagedConfigMember(Member configMember) {
        boolean z;
        synchronized (this.postProcessingLock) {
            z = this.externallyManagedConfigMembers != null && this.externallyManagedConfigMembers.contains(configMember);
        }
        return z;
    }

    public void registerExternallyManagedInitMethod(String initMethod) {
        synchronized (this.postProcessingLock) {
            if (this.externallyManagedInitMethods == null) {
                this.externallyManagedInitMethods = new HashSet(1);
            }
            this.externallyManagedInitMethods.add(initMethod);
        }
    }

    public boolean isExternallyManagedInitMethod(String initMethod) {
        boolean z;
        synchronized (this.postProcessingLock) {
            z = this.externallyManagedInitMethods != null && this.externallyManagedInitMethods.contains(initMethod);
        }
        return z;
    }

    public void registerExternallyManagedDestroyMethod(String destroyMethod) {
        synchronized (this.postProcessingLock) {
            if (this.externallyManagedDestroyMethods == null) {
                this.externallyManagedDestroyMethods = new HashSet(1);
            }
            this.externallyManagedDestroyMethods.add(destroyMethod);
        }
    }

    public boolean isExternallyManagedDestroyMethod(String destroyMethod) {
        boolean z;
        synchronized (this.postProcessingLock) {
            z = this.externallyManagedDestroyMethods != null && this.externallyManagedDestroyMethods.contains(destroyMethod);
        }
        return z;
    }

    @Override // org.springframework.beans.factory.support.AbstractBeanDefinition
    public RootBeanDefinition cloneBeanDefinition() {
        return new RootBeanDefinition(this);
    }

    @Override // org.springframework.beans.factory.support.AbstractBeanDefinition, org.springframework.core.AttributeAccessorSupport
    public boolean equals(Object other) {
        return this == other || ((other instanceof RootBeanDefinition) && super.equals(other));
    }

    @Override // org.springframework.beans.factory.support.AbstractBeanDefinition
    public String toString() {
        return "Root bean: " + super.toString();
    }
}
