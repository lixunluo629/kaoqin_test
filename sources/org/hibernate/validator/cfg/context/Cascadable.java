package org.hibernate.validator.cfg.context;

import org.hibernate.validator.cfg.context.Cascadable;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/cfg/context/Cascadable.class */
public interface Cascadable<C extends Cascadable<C>> {
    C valid();

    GroupConversionTargetContext<C> convertGroup(Class<?> cls);
}
