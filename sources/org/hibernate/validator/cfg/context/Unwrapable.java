package org.hibernate.validator.cfg.context;

import org.hibernate.validator.cfg.context.Unwrapable;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/cfg/context/Unwrapable.class */
public interface Unwrapable<U extends Unwrapable<U>> {
    /* renamed from: unwrapValidatedValue */
    U mo5675unwrapValidatedValue(boolean z);
}
