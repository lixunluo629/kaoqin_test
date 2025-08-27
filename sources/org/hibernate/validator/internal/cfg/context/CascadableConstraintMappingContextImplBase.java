package org.hibernate.validator.internal.cfg.context;

import java.util.Map;
import org.hibernate.validator.cfg.context.Cascadable;
import org.hibernate.validator.cfg.context.GroupConversionTargetContext;
import org.hibernate.validator.cfg.context.Unwrapable;
import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
import org.hibernate.validator.internal.util.CollectionHelper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/cfg/context/CascadableConstraintMappingContextImplBase.class */
abstract class CascadableConstraintMappingContextImplBase<C extends Cascadable<C> & Unwrapable<C>> extends ConstraintMappingContextImplBase implements Cascadable<C>, Unwrapable<C> {
    protected boolean isCascading;
    protected Map<Class<?>, Class<?>> groupConversions;
    private UnwrapMode unwrapMode;

    /* JADX WARN: Incorrect return type in method signature: ()TC; */
    protected abstract Cascadable getThis();

    @Override // org.hibernate.validator.cfg.context.Unwrapable
    /* renamed from: unwrapValidatedValue, reason: collision with other method in class */
    public /* bridge */ /* synthetic */ Unwrapable mo5675unwrapValidatedValue(boolean z) {
        return (Unwrapable) unwrapValidatedValue(z);
    }

    CascadableConstraintMappingContextImplBase(DefaultConstraintMapping mapping) {
        super(mapping);
        this.groupConversions = CollectionHelper.newHashMap();
        this.unwrapMode = UnwrapMode.AUTOMATIC;
    }

    public void addGroupConversion(Class<?> from, Class<?> to) {
        this.groupConversions.put(from, to);
    }

    /* JADX WARN: Incorrect return type in method signature: ()TC; */
    @Override // org.hibernate.validator.cfg.context.Cascadable
    public Cascadable valid() {
        this.isCascading = true;
        return getThis();
    }

    @Override // org.hibernate.validator.cfg.context.Cascadable
    public GroupConversionTargetContext<C> convertGroup(Class<?> from) {
        return new GroupConversionTargetContextImpl(from, getThis(), this);
    }

    public boolean isCascading() {
        return this.isCascading;
    }

    public Map<Class<?>, Class<?>> getGroupConversions() {
        return this.groupConversions;
    }

    /* JADX WARN: Incorrect return type in method signature: (Z)TC; */
    public Cascadable unwrapValidatedValue(boolean unwrap) {
        if (unwrap) {
            this.unwrapMode = UnwrapMode.UNWRAP;
        } else {
            this.unwrapMode = UnwrapMode.SKIP_UNWRAP;
        }
        return getThis();
    }

    UnwrapMode unwrapMode() {
        return this.unwrapMode;
    }
}
