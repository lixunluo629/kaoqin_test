package org.hibernate.validator.cfg.defs;

import org.hibernate.validator.cfg.ConstraintDef;
import org.hibernate.validator.constraints.Length;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/cfg/defs/LengthDef.class */
public class LengthDef extends ConstraintDef<LengthDef, Length> {
    public LengthDef() {
        super(Length.class);
    }

    public LengthDef min(int min) {
        addParameter("min", Integer.valueOf(min));
        return this;
    }

    public LengthDef max(int max) {
        addParameter("max", Integer.valueOf(max));
        return this;
    }
}
