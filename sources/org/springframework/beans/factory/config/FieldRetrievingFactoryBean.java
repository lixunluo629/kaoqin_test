package org.springframework.beans.factory.config;

import java.lang.reflect.Field;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.FactoryBeanNotInitializedException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/config/FieldRetrievingFactoryBean.class */
public class FieldRetrievingFactoryBean implements FactoryBean<Object>, BeanNameAware, BeanClassLoaderAware, InitializingBean {
    private Class<?> targetClass;
    private Object targetObject;
    private String targetField;
    private String staticField;
    private String beanName;
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
    private Field fieldObject;

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public Class<?> getTargetClass() {
        return this.targetClass;
    }

    public void setTargetObject(Object targetObject) {
        this.targetObject = targetObject;
    }

    public Object getTargetObject() {
        return this.targetObject;
    }

    public void setTargetField(String targetField) {
        this.targetField = StringUtils.trimAllWhitespace(targetField);
    }

    public String getTargetField() {
        return this.targetField;
    }

    public void setStaticField(String staticField) {
        this.staticField = StringUtils.trimAllWhitespace(staticField);
    }

    @Override // org.springframework.beans.factory.BeanNameAware
    public void setBeanName(String beanName) {
        this.beanName = StringUtils.trimAllWhitespace(BeanFactoryUtils.originalBeanName(beanName));
    }

    @Override // org.springframework.beans.factory.BeanClassLoaderAware
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws NoSuchFieldException, ClassNotFoundException {
        if (this.targetClass != null && this.targetObject != null) {
            throw new IllegalArgumentException("Specify either targetClass or targetObject, not both");
        }
        if (this.targetClass == null && this.targetObject == null) {
            if (this.targetField != null) {
                throw new IllegalArgumentException("Specify targetClass or targetObject in combination with targetField");
            }
            if (this.staticField == null) {
                this.staticField = this.beanName;
            }
            int lastDotIndex = this.staticField.lastIndexOf(46);
            if (lastDotIndex == -1 || lastDotIndex == this.staticField.length()) {
                throw new IllegalArgumentException("staticField must be a fully qualified class plus static field name: e.g. 'example.MyExampleClass.MY_EXAMPLE_FIELD'");
            }
            String className = this.staticField.substring(0, lastDotIndex);
            String fieldName = this.staticField.substring(lastDotIndex + 1);
            this.targetClass = ClassUtils.forName(className, this.beanClassLoader);
            this.targetField = fieldName;
        } else if (this.targetField == null) {
            throw new IllegalArgumentException("targetField is required");
        }
        Class<?> targetClass = this.targetObject != null ? this.targetObject.getClass() : this.targetClass;
        this.fieldObject = targetClass.getField(this.targetField);
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Object getObject() throws IllegalAccessException {
        if (this.fieldObject == null) {
            throw new FactoryBeanNotInitializedException();
        }
        ReflectionUtils.makeAccessible(this.fieldObject);
        if (this.targetObject != null) {
            return this.fieldObject.get(this.targetObject);
        }
        return this.fieldObject.get(null);
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Class<?> getObjectType() {
        if (this.fieldObject != null) {
            return this.fieldObject.getType();
        }
        return null;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public boolean isSingleton() {
        return false;
    }
}
