package org.springframework.data.querydsl.binding;

import com.querydsl.core.types.Path;
import java.beans.PropertyDescriptor;
import lombok.Generated;
import org.springframework.beans.BeanUtils;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.QueryDslUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/querydsl/binding/QuerydslPathInformation.class */
class QuerydslPathInformation implements PathInformation {
    private final Path<?> path;

    @Generated
    public String toString() {
        return "QuerydslPathInformation(path=" + this.path + ")";
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof QuerydslPathInformation)) {
            return false;
        }
        QuerydslPathInformation other = (QuerydslPathInformation) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$path = this.path;
        Object other$path = other.path;
        return this$path == null ? other$path == null : this$path.equals(other$path);
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof QuerydslPathInformation;
    }

    @Generated
    public int hashCode() {
        Object $path = this.path;
        int result = (1 * 59) + ($path == null ? 43 : $path.hashCode());
        return result;
    }

    @Generated
    private QuerydslPathInformation(Path<?> path) {
        this.path = path;
    }

    @Generated
    public static QuerydslPathInformation of(Path<?> path) {
        return new QuerydslPathInformation(path);
    }

    @Override // org.springframework.data.querydsl.binding.PathInformation
    public Class<?> getLeafType() {
        return this.path.getType();
    }

    @Override // org.springframework.data.querydsl.binding.PathInformation
    public Class<?> getLeafParentType() {
        return this.path.getMetadata().getParent().getType();
    }

    @Override // org.springframework.data.querydsl.binding.PathInformation
    public String getLeafProperty() {
        return this.path.getMetadata().getElement().toString();
    }

    @Override // org.springframework.data.querydsl.binding.PathInformation
    public PropertyDescriptor getLeafPropertyDescriptor() {
        return BeanUtils.getPropertyDescriptor(getLeafParentType(), getLeafProperty());
    }

    @Override // org.springframework.data.querydsl.binding.PathInformation
    public String toDotPath() {
        return QueryDslUtils.toDotPath(this.path);
    }

    @Override // org.springframework.data.querydsl.binding.PathInformation
    public Path<?> reifyPath(EntityPathResolver resolver) {
        return this.path;
    }
}
