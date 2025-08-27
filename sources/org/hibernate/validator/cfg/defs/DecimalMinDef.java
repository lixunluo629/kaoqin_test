package org.hibernate.validator.cfg.defs;

import javax.validation.constraints.DecimalMin;
import org.hibernate.validator.cfg.ConstraintDef;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/cfg/defs/DecimalMinDef.class */
public class DecimalMinDef extends ConstraintDef<DecimalMinDef, DecimalMin> {
    public DecimalMinDef() {
        super(DecimalMin.class);
    }

    public DecimalMinDef value(String min) {
        addParameter("value", min);
        return this;
    }

    public DecimalMinDef inclusive(boolean inclusive) {
        addParameter("inclusive", Boolean.valueOf(inclusive));
        return this;
    }
}
