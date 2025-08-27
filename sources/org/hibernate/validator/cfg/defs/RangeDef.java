package org.hibernate.validator.cfg.defs;

import org.hibernate.validator.cfg.ConstraintDef;
import org.hibernate.validator.constraints.Range;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/cfg/defs/RangeDef.class */
public class RangeDef extends ConstraintDef<RangeDef, Range> {
    public RangeDef() {
        super(Range.class);
    }

    public RangeDef min(long min) {
        addParameter("min", Long.valueOf(min));
        return this;
    }

    public RangeDef max(long max) {
        addParameter("max", Long.valueOf(max));
        return this;
    }
}
