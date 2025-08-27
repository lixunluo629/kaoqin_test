package org.springframework.data.keyvalue.core.mapping;

import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/mapping/ClassNameKeySpaceResolver.class */
enum ClassNameKeySpaceResolver implements KeySpaceResolver {
    INSTANCE;

    @Override // org.springframework.data.keyvalue.core.mapping.KeySpaceResolver
    public String resolveKeySpace(Class<?> type) {
        Assert.notNull(type, "Type must not be null!");
        return ClassUtils.getUserClass(type).getName();
    }
}
