package org.hibernate.validator.cfg.defs;

import org.hibernate.validator.cfg.ConstraintDef;
import org.hibernate.validator.constraints.ScriptAssert;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/cfg/defs/ScriptAssertDef.class */
public class ScriptAssertDef extends ConstraintDef<ScriptAssertDef, ScriptAssert> {
    public ScriptAssertDef() {
        super(ScriptAssert.class);
    }

    public ScriptAssertDef lang(String lang) {
        addParameter(AbstractHtmlElementTag.LANG_ATTRIBUTE, lang);
        return this;
    }

    public ScriptAssertDef script(String script) {
        addParameter("script", script);
        return this;
    }

    public ScriptAssertDef alias(String alias) {
        addParameter("alias", alias);
        return this;
    }
}
