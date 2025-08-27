package org.springframework.validation;

import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/validation/DirectFieldBindingResult.class */
public class DirectFieldBindingResult extends AbstractPropertyBindingResult {
    private final Object target;
    private final boolean autoGrowNestedPaths;
    private transient ConfigurablePropertyAccessor directFieldAccessor;

    public DirectFieldBindingResult(Object target, String objectName) {
        this(target, objectName, true);
    }

    public DirectFieldBindingResult(Object target, String objectName, boolean autoGrowNestedPaths) {
        super(objectName);
        this.target = target;
        this.autoGrowNestedPaths = autoGrowNestedPaths;
    }

    @Override // org.springframework.validation.AbstractBindingResult, org.springframework.validation.BindingResult
    public final Object getTarget() {
        return this.target;
    }

    @Override // org.springframework.validation.AbstractPropertyBindingResult
    public final ConfigurablePropertyAccessor getPropertyAccessor() {
        if (this.directFieldAccessor == null) {
            this.directFieldAccessor = createDirectFieldAccessor();
            this.directFieldAccessor.setExtractOldValueForEditor(true);
            this.directFieldAccessor.setAutoGrowNestedPaths(this.autoGrowNestedPaths);
        }
        return this.directFieldAccessor;
    }

    protected ConfigurablePropertyAccessor createDirectFieldAccessor() {
        if (this.target == null) {
            throw new IllegalStateException("Cannot access fields on null target instance '" + getObjectName() + "'");
        }
        return PropertyAccessorFactory.forDirectFieldAccess(this.target);
    }
}
