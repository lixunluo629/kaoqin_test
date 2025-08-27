package org.hibernate.validator.internal.util;

import com.fasterxml.classmate.TypeResolver;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/util/TypeResolutionHelper.class */
public class TypeResolutionHelper {
    private final TypeResolver typeResolver = new TypeResolver();

    public TypeResolver getTypeResolver() {
        return this.typeResolver;
    }
}
