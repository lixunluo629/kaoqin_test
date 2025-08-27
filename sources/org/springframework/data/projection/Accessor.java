package org.springframework.data.projection;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/projection/Accessor.class */
public final class Accessor {
    private final PropertyDescriptor descriptor;
    private final Method method;

    public Accessor(Method method) {
        Assert.notNull(method, "Method must not be null!");
        this.descriptor = BeanUtils.findPropertyForMethod(method);
        this.method = method;
        Assert.notNull(this.descriptor, String.format("Invoked method %s is no accessor method!", method));
    }

    public boolean isGetter() {
        return this.method.equals(this.descriptor.getReadMethod());
    }

    public boolean isSetter() {
        return this.method.equals(this.descriptor.getWriteMethod());
    }

    public String getPropertyName() {
        return this.descriptor.getName();
    }
}
