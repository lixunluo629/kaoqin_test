package org.springframework.data.util;

import java.lang.reflect.TypeVariable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/util/TypeVariableTypeInformation.class */
class TypeVariableTypeInformation<T> extends ParentTypeAwareTypeInformation<T> {
    private final TypeVariable<?> variable;

    public TypeVariableTypeInformation(TypeVariable<?> variable, TypeDiscoverer<?> parent) {
        super(variable, parent);
        Assert.notNull(variable, "TypeVariable must not be null!");
        this.variable = variable;
    }

    @Override // org.springframework.data.util.ParentTypeAwareTypeInformation, org.springframework.data.util.TypeDiscoverer
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TypeVariableTypeInformation)) {
            return false;
        }
        TypeVariableTypeInformation<?> that = (TypeVariableTypeInformation) obj;
        return getType().equals(that.getType());
    }

    @Override // org.springframework.data.util.ParentTypeAwareTypeInformation, org.springframework.data.util.TypeDiscoverer
    public int hashCode() {
        int result = 17 + (31 * ObjectUtils.nullSafeHashCode(getType()));
        return result;
    }

    public String toString() {
        return this.variable.getName();
    }
}
