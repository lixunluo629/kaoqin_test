package org.hibernate.validator.cfg.defs;

import javax.validation.constraints.DecimalMax;
import org.hibernate.validator.cfg.ConstraintDef;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/cfg/defs/DecimalMaxDef.class */
public class DecimalMaxDef extends ConstraintDef<DecimalMaxDef, DecimalMax> {
    public DecimalMaxDef() {
        super(DecimalMax.class);
    }

    public DecimalMaxDef value(String max) {
        addParameter("value", max);
        return this;
    }

    public DecimalMaxDef inclusive(boolean inclusive) {
        addParameter("inclusive", Boolean.valueOf(inclusive));
        return this;
    }
}
