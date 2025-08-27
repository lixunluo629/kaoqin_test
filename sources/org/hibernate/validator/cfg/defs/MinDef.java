package org.hibernate.validator.cfg.defs;

import javax.validation.constraints.Min;
import org.hibernate.validator.cfg.ConstraintDef;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/cfg/defs/MinDef.class */
public class MinDef extends ConstraintDef<MinDef, Min> {
    public MinDef() {
        super(Min.class);
    }

    public MinDef value(long min) {
        addParameter("value", Long.valueOf(min));
        return this;
    }
}
