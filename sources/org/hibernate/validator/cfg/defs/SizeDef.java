package org.hibernate.validator.cfg.defs;

import javax.validation.constraints.Size;
import org.hibernate.validator.cfg.ConstraintDef;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/cfg/defs/SizeDef.class */
public class SizeDef extends ConstraintDef<SizeDef, Size> {
    public SizeDef() {
        super(Size.class);
    }

    public SizeDef min(int min) {
        addParameter("min", Integer.valueOf(min));
        return this;
    }

    public SizeDef max(int max) {
        addParameter("max", Integer.valueOf(max));
        return this;
    }
}
