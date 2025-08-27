package org.hibernate.validator.internal.cfg.context;

import java.lang.reflect.Constructor;
import org.hibernate.validator.cfg.context.ConstructorConstraintMappingContext;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/cfg/context/ConstructorConstraintMappingContextImpl.class */
class ConstructorConstraintMappingContextImpl extends ExecutableConstraintMappingContextImpl implements ConstructorConstraintMappingContext {
    <T> ConstructorConstraintMappingContextImpl(TypeConstraintMappingContextImpl<T> typeContext, Constructor<T> constructor) {
        super(typeContext, constructor);
    }

    @Override // org.hibernate.validator.cfg.context.AnnotationIgnoreOptions
    public ConstructorConstraintMappingContext ignoreAnnotations(boolean ignoreAnnotations) {
        this.typeContext.mapping.getAnnotationProcessingOptions().ignoreConstraintAnnotationsOnMember(this.executable.getMember(), Boolean.valueOf(ignoreAnnotations));
        return this;
    }
}
