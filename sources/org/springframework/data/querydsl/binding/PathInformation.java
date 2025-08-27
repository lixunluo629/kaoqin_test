package org.springframework.data.querydsl.binding;

import com.querydsl.core.types.Path;
import java.beans.PropertyDescriptor;
import org.springframework.data.querydsl.EntityPathResolver;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/querydsl/binding/PathInformation.class */
interface PathInformation {
    Class<?> getLeafType();

    Class<?> getLeafParentType();

    String getLeafProperty();

    PropertyDescriptor getLeafPropertyDescriptor();

    String toDotPath();

    Path<?> reifyPath(EntityPathResolver entityPathResolver);
}
