package org.hibernate.validator.internal.cfg.context;

import java.lang.reflect.Method;
import org.hibernate.validator.cfg.context.MethodConstraintMappingContext;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/cfg/context/MethodConstraintMappingContextImpl.class */
class MethodConstraintMappingContextImpl extends ExecutableConstraintMappingContextImpl implements MethodConstraintMappingContext {
    MethodConstraintMappingContextImpl(TypeConstraintMappingContextImpl<?> typeContext, Method method) {
        super(typeContext, method);
    }

    @Override // org.hibernate.validator.cfg.context.AnnotationIgnoreOptions
    public MethodConstraintMappingContext ignoreAnnotations(boolean ignoreAnnotations) {
        this.typeContext.mapping.getAnnotationProcessingOptions().ignoreConstraintAnnotationsOnMember(this.executable.getMember(), Boolean.valueOf(ignoreAnnotations));
        return this;
    }
}
