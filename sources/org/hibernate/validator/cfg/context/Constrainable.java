package org.hibernate.validator.cfg.context;

import org.hibernate.validator.cfg.ConstraintDef;
import org.hibernate.validator.cfg.context.Constrainable;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/cfg/context/Constrainable.class */
public interface Constrainable<C extends Constrainable<C>> {
    C constraint(ConstraintDef<?, ?> constraintDef);
}
