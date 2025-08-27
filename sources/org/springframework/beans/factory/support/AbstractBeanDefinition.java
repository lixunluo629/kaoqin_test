package org.springframework.beans.factory.support;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.BeanMetadataAttributeAccessor;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.core.io.DescriptiveResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/AbstractBeanDefinition.class */
public abstract class AbstractBeanDefinition extends BeanMetadataAttributeAccessor implements BeanDefinition, Cloneable {
    public static final String SCOPE_DEFAULT = "";
    public static final int AUTOWIRE_NO = 0;
    public static final int AUTOWIRE_BY_NAME = 1;
    public static final int AUTOWIRE_BY_TYPE = 2;
    public static final int AUTOWIRE_CONSTRUCTOR = 3;

    @Deprecated
    public static final int AUTOWIRE_AUTODETECT = 4;
    public static final int DEPENDENCY_CHECK_NONE = 0;
    public static final int DEPENDENCY_CHECK_OBJECTS = 1;
    public static final int DEPENDENCY_CHECK_SIMPLE = 2;
    public static final int DEPENDENCY_CHECK_ALL = 3;
    public static final String INFER_METHOD = "(inferred)";
    private volatile Object beanClass;
    private String scope;
    private boolean abstractFlag;
    private boolean lazyInit;
    private int autowireMode;
    private int dependencyCheck;
    private String[] dependsOn;
    private boolean autowireCandidate;
    private boolean primary;
    private final Map<String, AutowireCandidateQualifier> qualifiers;
    private boolean nonPublicAccessAllowed;
    private boolean lenientConstructorResolution;
    private String factoryBeanName;
    private String factoryMethodName;
    private ConstructorArgumentValues constructorArgumentValues;
    private MutablePropertyValues propertyValues;
    private MethodOverrides methodOverrides;
    private String initMethodName;
    private String destroyMethodName;
    private boolean enforceInitMethod;
    private boolean enforceDestroyMethod;
    private boolean synthetic;
    private int role;
    private String description;
    private Resource resource;

    public abstract AbstractBeanDefinition cloneBeanDefinition();

    protected AbstractBeanDefinition() {
        this(null, null);
    }

    protected AbstractBeanDefinition(ConstructorArgumentValues cargs, MutablePropertyValues pvs) {
        this.scope = "";
        this.abstractFlag = false;
        this.lazyInit = false;
        this.autowireMode = 0;
        this.dependencyCheck = 0;
        this.autowireCandidate = true;
        this.primary = false;
        this.qualifiers = new LinkedHashMap(0);
        this.nonPublicAccessAllowed = true;
        this.lenientConstructorResolution = true;
        this.methodOverrides = new MethodOverrides();
        this.enforceInitMethod = true;
        this.enforceDestroyMethod = true;
        this.synthetic = false;
        this.role = 0;
        setConstructorArgumentValues(cargs);
        setPropertyValues(pvs);
    }

    protected AbstractBeanDefinition(BeanDefinition original) {
        this.scope = "";
        this.abstractFlag = false;
        this.lazyInit = false;
        this.autowireMode = 0;
        this.dependencyCheck = 0;
        this.autowireCandidate = true;
        this.primary = false;
        this.qualifiers = new LinkedHashMap(0);
        this.nonPublicAccessAllowed = true;
        this.lenientConstructorResolution = true;
        this.methodOverrides = new MethodOverrides();
        this.enforceInitMethod = true;
        this.enforceDestroyMethod = true;
        this.synthetic = false;
        this.role = 0;
        setParentName(original.getParentName());
        setBeanClassName(original.getBeanClassName());
        setScope(original.getScope());
        setAbstract(original.isAbstract());
        setLazyInit(original.isLazyInit());
        setFactoryBeanName(original.getFactoryBeanName());
        setFactoryMethodName(original.getFactoryMethodName());
        setConstructorArgumentValues(new ConstructorArgumentValues(original.getConstructorArgumentValues()));
        setPropertyValues(new MutablePropertyValues(original.getPropertyValues()));
        setRole(original.getRole());
        setSource(original.getSource());
        copyAttributesFrom(original);
        if (original instanceof AbstractBeanDefinition) {
            AbstractBeanDefinition originalAbd = (AbstractBeanDefinition) original;
            if (originalAbd.hasBeanClass()) {
                setBeanClass(originalAbd.getBeanClass());
            }
            setAutowireMode(originalAbd.getAutowireMode());
            setDependencyCheck(originalAbd.getDependencyCheck());
            setDependsOn(originalAbd.getDependsOn());
            setAutowireCandidate(originalAbd.isAutowireCandidate());
            setPrimary(originalAbd.isPrimary());
            copyQualifiersFrom(originalAbd);
            setNonPublicAccessAllowed(originalAbd.isNonPublicAccessAllowed());
            setLenientConstructorResolution(originalAbd.isLenientConstructorResolution());
            setMethodOverrides(new MethodOverrides(originalAbd.getMethodOverrides()));
            setInitMethodName(originalAbd.getInitMethodName());
            setEnforceInitMethod(originalAbd.isEnforceInitMethod());
            setDestroyMethodName(originalAbd.getDestroyMethodName());
            setEnforceDestroyMethod(originalAbd.isEnforceDestroyMethod());
            setSynthetic(originalAbd.isSynthetic());
            setResource(originalAbd.getResource());
            return;
        }
        setResourceDescription(original.getResourceDescription());
    }

    public void overrideFrom(BeanDefinition other) {
        if (StringUtils.hasLength(other.getBeanClassName())) {
            setBeanClassName(other.getBeanClassName());
        }
        if (StringUtils.hasLength(other.getScope())) {
            setScope(other.getScope());
        }
        setAbstract(other.isAbstract());
        setLazyInit(other.isLazyInit());
        if (StringUtils.hasLength(other.getFactoryBeanName())) {
            setFactoryBeanName(other.getFactoryBeanName());
        }
        if (StringUtils.hasLength(other.getFactoryMethodName())) {
            setFactoryMethodName(other.getFactoryMethodName());
        }
        getConstructorArgumentValues().addArgumentValues(other.getConstructorArgumentValues());
        getPropertyValues().addPropertyValues(other.getPropertyValues());
        setRole(other.getRole());
        setSource(other.getSource());
        copyAttributesFrom(other);
        if (other instanceof AbstractBeanDefinition) {
            AbstractBeanDefinition otherAbd = (AbstractBeanDefinition) other;
            if (otherAbd.hasBeanClass()) {
                setBeanClass(otherAbd.getBeanClass());
            }
            setAutowireMode(otherAbd.getAutowireMode());
            setDependencyCheck(otherAbd.getDependencyCheck());
            setDependsOn(otherAbd.getDependsOn());
            setAutowireCandidate(otherAbd.isAutowireCandidate());
            setPrimary(otherAbd.isPrimary());
            copyQualifiersFrom(otherAbd);
            setNonPublicAccessAllowed(otherAbd.isNonPublicAccessAllowed());
            setLenientConstructorResolution(otherAbd.isLenientConstructorResolution());
            getMethodOverrides().addOverrides(otherAbd.getMethodOverrides());
            if (StringUtils.hasLength(otherAbd.getInitMethodName())) {
                setInitMethodName(otherAbd.getInitMethodName());
                setEnforceInitMethod(otherAbd.isEnforceInitMethod());
            }
            if (otherAbd.getDestroyMethodName() != null) {
                setDestroyMethodName(otherAbd.getDestroyMethodName());
                setEnforceDestroyMethod(otherAbd.isEnforceDestroyMethod());
            }
            setSynthetic(otherAbd.isSynthetic());
            setResource(otherAbd.getResource());
            return;
        }
        setResourceDescription(other.getResourceDescription());
    }

    public void applyDefaults(BeanDefinitionDefaults defaults) {
        setLazyInit(defaults.isLazyInit());
        setAutowireMode(defaults.getAutowireMode());
        setDependencyCheck(defaults.getDependencyCheck());
        setInitMethodName(defaults.getInitMethodName());
        setEnforceInitMethod(false);
        setDestroyMethodName(defaults.getDestroyMethodName());
        setEnforceDestroyMethod(false);
    }

    @Override // org.springframework.beans.factory.config.BeanDefinition
    public void setBeanClassName(String beanClassName) {
        this.beanClass = beanClassName;
    }

    @Override // org.springframework.beans.factory.config.BeanDefinition
    public String getBeanClassName() {
        Object beanClassObject = this.beanClass;
        if (beanClassObject instanceof Class) {
            return ((Class) beanClassObject).getName();
        }
        return (String) beanClassObject;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public Class<?> getBeanClass() throws IllegalStateException {
        Object beanClassObject = this.beanClass;
        if (beanClassObject == null) {
            throw new IllegalStateException("No bean class specified on bean definition");
        }
        if (!(beanClassObject instanceof Class)) {
            throw new IllegalStateException("Bean class name [" + beanClassObject + "] has not been resolved into an actual Class");
        }
        return (Class) beanClassObject;
    }

    public boolean hasBeanClass() {
        return this.beanClass instanceof Class;
    }

    public Class<?> resolveBeanClass(ClassLoader classLoader) throws LinkageError, ClassNotFoundException {
        String className = getBeanClassName();
        if (className == null) {
            return null;
        }
        Class<?> resolvedClass = ClassUtils.forName(className, classLoader);
        this.beanClass = resolvedClass;
        return resolvedClass;
    }

    @Override // org.springframework.beans.factory.config.BeanDefinition
    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override // org.springframework.beans.factory.config.BeanDefinition
    public String getScope() {
        return this.scope;
    }

    @Override // org.springframework.beans.factory.config.BeanDefinition
    public boolean isSingleton() {
        return "singleton".equals(this.scope) || "".equals(this.scope);
    }

    @Override // org.springframework.beans.factory.config.BeanDefinition
    public boolean isPrototype() {
        return "prototype".equals(this.scope);
    }

    public void setAbstract(boolean abstractFlag) {
        this.abstractFlag = abstractFlag;
    }

    @Override // org.springframework.beans.factory.config.BeanDefinition
    public boolean isAbstract() {
        return this.abstractFlag;
    }

    @Override // org.springframework.beans.factory.config.BeanDefinition
    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    @Override // org.springframework.beans.factory.config.BeanDefinition
    public boolean isLazyInit() {
        return this.lazyInit;
    }

    public void setAutowireMode(int autowireMode) {
        this.autowireMode = autowireMode;
    }

    public int getAutowireMode() {
        return this.autowireMode;
    }

    public int getResolvedAutowireMode() throws SecurityException {
        if (this.autowireMode == 4) {
            Constructor<?>[] constructors = getBeanClass().getConstructors();
            for (Constructor<?> constructor : constructors) {
                if (constructor.getParameterTypes().length == 0) {
                    return 2;
                }
            }
            return 3;
        }
        return this.autowireMode;
    }

    public void setDependencyCheck(int dependencyCheck) {
        this.dependencyCheck = dependencyCheck;
    }

    public int getDependencyCheck() {
        return this.dependencyCheck;
    }

    @Override // org.springframework.beans.factory.config.BeanDefinition
    public void setDependsOn(String... dependsOn) {
        this.dependsOn = dependsOn;
    }

    @Override // org.springframework.beans.factory.config.BeanDefinition
    public String[] getDependsOn() {
        return this.dependsOn;
    }

    @Override // org.springframework.beans.factory.config.BeanDefinition
    public void setAutowireCandidate(boolean autowireCandidate) {
        this.autowireCandidate = autowireCandidate;
    }

    @Override // org.springframework.beans.factory.config.BeanDefinition
    public boolean isAutowireCandidate() {
        return this.autowireCandidate;
    }

    @Override // org.springframework.beans.factory.config.BeanDefinition
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    @Override // org.springframework.beans.factory.config.BeanDefinition
    public boolean isPrimary() {
        return this.primary;
    }

    public void addQualifier(AutowireCandidateQualifier qualifier) {
        this.qualifiers.put(qualifier.getTypeName(), qualifier);
    }

    public boolean hasQualifier(String typeName) {
        return this.qualifiers.containsKey(typeName);
    }

    public AutowireCandidateQualifier getQualifier(String typeName) {
        return this.qualifiers.get(typeName);
    }

    public Set<AutowireCandidateQualifier> getQualifiers() {
        return new LinkedHashSet(this.qualifiers.values());
    }

    public void copyQualifiersFrom(AbstractBeanDefinition source) {
        Assert.notNull(source, "Source must not be null");
        this.qualifiers.putAll(source.qualifiers);
    }

    public void setNonPublicAccessAllowed(boolean nonPublicAccessAllowed) {
        this.nonPublicAccessAllowed = nonPublicAccessAllowed;
    }

    public boolean isNonPublicAccessAllowed() {
        return this.nonPublicAccessAllowed;
    }

    public void setLenientConstructorResolution(boolean lenientConstructorResolution) {
        this.lenientConstructorResolution = lenientConstructorResolution;
    }

    public boolean isLenientConstructorResolution() {
        return this.lenientConstructorResolution;
    }

    @Override // org.springframework.beans.factory.config.BeanDefinition
    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    @Override // org.springframework.beans.factory.config.BeanDefinition
    public String getFactoryBeanName() {
        return this.factoryBeanName;
    }

    @Override // org.springframework.beans.factory.config.BeanDefinition
    public void setFactoryMethodName(String factoryMethodName) {
        this.factoryMethodName = factoryMethodName;
    }

    @Override // org.springframework.beans.factory.config.BeanDefinition
    public String getFactoryMethodName() {
        return this.factoryMethodName;
    }

    public void setConstructorArgumentValues(ConstructorArgumentValues constructorArgumentValues) {
        this.constructorArgumentValues = constructorArgumentValues != null ? constructorArgumentValues : new ConstructorArgumentValues();
    }

    @Override // org.springframework.beans.factory.config.BeanDefinition
    public ConstructorArgumentValues getConstructorArgumentValues() {
        return this.constructorArgumentValues;
    }

    public boolean hasConstructorArgumentValues() {
        return !this.constructorArgumentValues.isEmpty();
    }

    public void setPropertyValues(MutablePropertyValues propertyValues) {
        this.propertyValues = propertyValues != null ? propertyValues : new MutablePropertyValues();
    }

    @Override // org.springframework.beans.factory.config.BeanDefinition
    public MutablePropertyValues getPropertyValues() {
        return this.propertyValues;
    }

    public void setMethodOverrides(MethodOverrides methodOverrides) {
        this.methodOverrides = methodOverrides != null ? methodOverrides : new MethodOverrides();
    }

    public MethodOverrides getMethodOverrides() {
        return this.methodOverrides;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    public String getInitMethodName() {
        return this.initMethodName;
    }

    public void setEnforceInitMethod(boolean enforceInitMethod) {
        this.enforceInitMethod = enforceInitMethod;
    }

    public boolean isEnforceInitMethod() {
        return this.enforceInitMethod;
    }

    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

    public String getDestroyMethodName() {
        return this.destroyMethodName;
    }

    public void setEnforceDestroyMethod(boolean enforceDestroyMethod) {
        this.enforceDestroyMethod = enforceDestroyMethod;
    }

    public boolean isEnforceDestroyMethod() {
        return this.enforceDestroyMethod;
    }

    public void setSynthetic(boolean synthetic) {
        this.synthetic = synthetic;
    }

    public boolean isSynthetic() {
        return this.synthetic;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @Override // org.springframework.beans.factory.config.BeanDefinition
    public int getRole() {
        return this.role;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override // org.springframework.beans.factory.config.BeanDefinition
    public String getDescription() {
        return this.description;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Resource getResource() {
        return this.resource;
    }

    public void setResourceDescription(String resourceDescription) {
        this.resource = new DescriptiveResource(resourceDescription);
    }

    @Override // org.springframework.beans.factory.config.BeanDefinition
    public String getResourceDescription() {
        if (this.resource != null) {
            return this.resource.getDescription();
        }
        return null;
    }

    public void setOriginatingBeanDefinition(BeanDefinition originatingBd) {
        this.resource = new BeanDefinitionResource(originatingBd);
    }

    @Override // org.springframework.beans.factory.config.BeanDefinition
    public BeanDefinition getOriginatingBeanDefinition() {
        if (this.resource instanceof BeanDefinitionResource) {
            return ((BeanDefinitionResource) this.resource).getBeanDefinition();
        }
        return null;
    }

    public void validate() throws BeanDefinitionValidationException {
        if (!getMethodOverrides().isEmpty() && getFactoryMethodName() != null) {
            throw new BeanDefinitionValidationException("Cannot combine static factory method with method overrides: the static factory method must create the instance");
        }
        if (hasBeanClass()) {
            prepareMethodOverrides();
        }
    }

    public void prepareMethodOverrides() throws BeanDefinitionValidationException {
        MethodOverrides methodOverrides = getMethodOverrides();
        if (!methodOverrides.isEmpty()) {
            Set<MethodOverride> overrides = methodOverrides.getOverrides();
            synchronized (overrides) {
                for (MethodOverride mo : overrides) {
                    prepareMethodOverride(mo);
                }
            }
        }
    }

    protected void prepareMethodOverride(MethodOverride mo) throws BeanDefinitionValidationException, SecurityException {
        int count = ClassUtils.getMethodCountForName(getBeanClass(), mo.getMethodName());
        if (count == 0) {
            throw new BeanDefinitionValidationException("Invalid method override: no method with name '" + mo.getMethodName() + "' on class [" + getBeanClassName() + "]");
        }
        if (count == 1) {
            mo.setOverloaded(false);
        }
    }

    public Object clone() {
        return cloneBeanDefinition();
    }

    @Override // org.springframework.core.AttributeAccessorSupport
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AbstractBeanDefinition)) {
            return false;
        }
        AbstractBeanDefinition that = (AbstractBeanDefinition) other;
        if (ObjectUtils.nullSafeEquals(getBeanClassName(), that.getBeanClassName()) && ObjectUtils.nullSafeEquals(this.scope, that.scope) && this.abstractFlag == that.abstractFlag && this.lazyInit == that.lazyInit && this.autowireMode == that.autowireMode && this.dependencyCheck == that.dependencyCheck && Arrays.equals(this.dependsOn, that.dependsOn) && this.autowireCandidate == that.autowireCandidate && ObjectUtils.nullSafeEquals(this.qualifiers, that.qualifiers) && this.primary == that.primary && this.nonPublicAccessAllowed == that.nonPublicAccessAllowed && this.lenientConstructorResolution == that.lenientConstructorResolution && ObjectUtils.nullSafeEquals(this.constructorArgumentValues, that.constructorArgumentValues) && ObjectUtils.nullSafeEquals(this.propertyValues, that.propertyValues) && ObjectUtils.nullSafeEquals(this.methodOverrides, that.methodOverrides) && ObjectUtils.nullSafeEquals(this.factoryBeanName, that.factoryBeanName) && ObjectUtils.nullSafeEquals(this.factoryMethodName, that.factoryMethodName) && ObjectUtils.nullSafeEquals(this.initMethodName, that.initMethodName) && this.enforceInitMethod == that.enforceInitMethod && ObjectUtils.nullSafeEquals(this.destroyMethodName, that.destroyMethodName) && this.enforceDestroyMethod == that.enforceDestroyMethod && this.synthetic == that.synthetic && this.role == that.role) {
            return super.equals(other);
        }
        return false;
    }

    @Override // org.springframework.core.AttributeAccessorSupport
    public int hashCode() {
        int hashCode = ObjectUtils.nullSafeHashCode(getBeanClassName());
        return (29 * ((29 * ((29 * ((29 * ((29 * ((29 * hashCode) + ObjectUtils.nullSafeHashCode(this.scope))) + ObjectUtils.nullSafeHashCode(this.constructorArgumentValues))) + ObjectUtils.nullSafeHashCode(this.propertyValues))) + ObjectUtils.nullSafeHashCode(this.factoryBeanName))) + ObjectUtils.nullSafeHashCode(this.factoryMethodName))) + super.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("class [");
        sb.append(getBeanClassName()).append("]");
        sb.append("; scope=").append(this.scope);
        sb.append("; abstract=").append(this.abstractFlag);
        sb.append("; lazyInit=").append(this.lazyInit);
        sb.append("; autowireMode=").append(this.autowireMode);
        sb.append("; dependencyCheck=").append(this.dependencyCheck);
        sb.append("; autowireCandidate=").append(this.autowireCandidate);
        sb.append("; primary=").append(this.primary);
        sb.append("; factoryBeanName=").append(this.factoryBeanName);
        sb.append("; factoryMethodName=").append(this.factoryMethodName);
        sb.append("; initMethodName=").append(this.initMethodName);
        sb.append("; destroyMethodName=").append(this.destroyMethodName);
        if (this.resource != null) {
            sb.append("; defined in ").append(this.resource.getDescription());
        }
        return sb.toString();
    }
}
