package org.hibernate.validator.internal.cfg.context;

import org.hibernate.validator.cfg.context.GroupConversionTargetContext;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/cfg/context/GroupConversionTargetContextImpl.class */
class GroupConversionTargetContextImpl<C> implements GroupConversionTargetContext<C> {
    private final C cascadableContext;
    private final Class<?> from;
    private final CascadableConstraintMappingContextImplBase<?> target;

    GroupConversionTargetContextImpl(Class<?> from, C cascadableContext, CascadableConstraintMappingContextImplBase<?> target) {
        this.from = from;
        this.cascadableContext = cascadableContext;
        this.target = target;
    }

    @Override // org.hibernate.validator.cfg.context.GroupConversionTargetContext
    public C to(Class<?> to) {
        this.target.addGroupConversion(this.from, to);
        return this.cascadableContext;
    }
}
