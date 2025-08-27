package org.hibernate.validator.cfg.defs;

import javax.validation.constraints.Pattern;
import org.hibernate.validator.cfg.ConstraintDef;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/cfg/defs/PatternDef.class */
public class PatternDef extends ConstraintDef<PatternDef, Pattern> {
    public PatternDef() {
        super(Pattern.class);
    }

    public PatternDef flags(Pattern.Flag[] flags) {
        addParameter("flags", flags);
        return this;
    }

    public PatternDef regexp(String regexp) {
        addParameter("regexp", regexp);
        return this;
    }
}
