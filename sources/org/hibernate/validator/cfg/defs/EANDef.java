package org.hibernate.validator.cfg.defs;

import org.hibernate.validator.cfg.ConstraintDef;
import org.hibernate.validator.constraints.EAN;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/cfg/defs/EANDef.class */
public class EANDef extends ConstraintDef<EANDef, EAN> {
    public EANDef() {
        super(EAN.class);
    }

    public EANDef type(EAN.Type type) {
        addParameter("type", type);
        return this;
    }
}
