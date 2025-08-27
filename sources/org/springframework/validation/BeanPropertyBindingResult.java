package org.springframework.validation;

import java.io.Serializable;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/validation/BeanPropertyBindingResult.class */
public class BeanPropertyBindingResult extends AbstractPropertyBindingResult implements Serializable {
    private final Object target;
    private final boolean autoGrowNestedPaths;
    private final int autoGrowCollectionLimit;
    private transient BeanWrapper beanWrapper;

    public BeanPropertyBindingResult(Object target, String objectName) {
        this(target, objectName, true, Integer.MAX_VALUE);
    }

    public BeanPropertyBindingResult(Object target, String objectName, boolean autoGrowNestedPaths, int autoGrowCollectionLimit) {
        super(objectName);
        this.target = target;
        this.autoGrowNestedPaths = autoGrowNestedPaths;
        this.autoGrowCollectionLimit = autoGrowCollectionLimit;
    }

    @Override // org.springframework.validation.AbstractBindingResult, org.springframework.validation.BindingResult
    public final Object getTarget() {
        return this.target;
    }

    @Override // org.springframework.validation.AbstractPropertyBindingResult
    public final ConfigurablePropertyAccessor getPropertyAccessor() {
        if (this.beanWrapper == null) {
            this.beanWrapper = createBeanWrapper();
            this.beanWrapper.setExtractOldValueForEditor(true);
            this.beanWrapper.setAutoGrowNestedPaths(this.autoGrowNestedPaths);
            this.beanWrapper.setAutoGrowCollectionLimit(this.autoGrowCollectionLimit);
        }
        return this.beanWrapper;
    }

    protected BeanWrapper createBeanWrapper() {
        if (this.target == null) {
            throw new IllegalStateException("Cannot access properties on null bean instance '" + getObjectName() + "'");
        }
        return PropertyAccessorFactory.forBeanPropertyAccess(this.target);
    }
}
