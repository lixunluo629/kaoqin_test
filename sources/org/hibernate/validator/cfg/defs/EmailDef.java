package org.hibernate.validator.cfg.defs;

import javax.validation.constraints.Pattern;
import org.hibernate.validator.cfg.ConstraintDef;
import org.hibernate.validator.constraints.Email;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/cfg/defs/EmailDef.class */
public class EmailDef extends ConstraintDef<EmailDef, Email> {
    public EmailDef() {
        super(Email.class);
    }

    public EmailDef regexp(String regexp) {
        addParameter("regexp", regexp);
        return this;
    }

    public EmailDef flags(Pattern.Flag... flags) {
        addParameter("flags", flags);
        return this;
    }
}
