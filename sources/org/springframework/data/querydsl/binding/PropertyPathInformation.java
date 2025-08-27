package org.springframework.data.querydsl.binding;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.CollectionPathBase;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import lombok.Generated;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/querydsl/binding/PropertyPathInformation.class */
class PropertyPathInformation implements PathInformation {
    private final PropertyPath path;

    @Generated
    public String toString() {
        return "PropertyPathInformation(path=" + this.path + ")";
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PropertyPathInformation)) {
            return false;
        }
        PropertyPathInformation other = (PropertyPathInformation) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$path = this.path;
        Object other$path = other.path;
        return this$path == null ? other$path == null : this$path.equals(other$path);
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof PropertyPathInformation;
    }

    @Generated
    public int hashCode() {
        Object $path = this.path;
        int result = (1 * 59) + ($path == null ? 43 : $path.hashCode());
        return result;
    }

    @Generated
    private PropertyPathInformation(PropertyPath path) {
        this.path = path;
    }

    @Generated
    private static PropertyPathInformation of(PropertyPath path) {
        return new PropertyPathInformation(path);
    }

    public static PropertyPathInformation of(String path, Class<?> type) {
        return of(PropertyPath.from(path, type));
    }

    public static PropertyPathInformation of(String path, TypeInformation<?> type) {
        return of(PropertyPath.from(path, type));
    }

    @Override // org.springframework.data.querydsl.binding.PathInformation
    public Class<?> getLeafType() {
        return this.path.getLeafProperty().getType();
    }

    @Override // org.springframework.data.querydsl.binding.PathInformation
    public Class<?> getLeafParentType() {
        return this.path.getLeafProperty().getOwningType().getType();
    }

    @Override // org.springframework.data.querydsl.binding.PathInformation
    public String getLeafProperty() {
        return this.path.getLeafProperty().getSegment();
    }

    @Override // org.springframework.data.querydsl.binding.PathInformation
    public PropertyDescriptor getLeafPropertyDescriptor() {
        return BeanUtils.getPropertyDescriptor(getLeafParentType(), getLeafProperty());
    }

    @Override // org.springframework.data.querydsl.binding.PathInformation
    public String toDotPath() {
        return this.path.toDotPath();
    }

    @Override // org.springframework.data.querydsl.binding.PathInformation
    public Path<?> reifyPath(EntityPathResolver resolver) {
        return reifyPath(resolver, this.path, null);
    }

    private static Path<?> reifyPath(EntityPathResolver resolver, PropertyPath path, Path<?> base) {
        if (base instanceof CollectionPathBase) {
            return reifyPath(resolver, path, ((CollectionPathBase) base).any());
        }
        Path<?> entityPath = base != null ? base : resolver.createPath(path.getOwningType().getType());
        Field field = ReflectionUtils.findField(entityPath.getClass(), path.getSegment());
        Object value = ReflectionUtils.getField(field, entityPath);
        if (path.hasNext()) {
            return reifyPath(resolver, path.next(), (Path) value);
        }
        return (Path) value;
    }
}
