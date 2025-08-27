package org.hibernate.validator.cfg;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Payload;
import org.hibernate.validator.cfg.ConstraintDef;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/cfg/ConstraintDef.class */
public abstract class ConstraintDef<C extends ConstraintDef<C, A>, A extends Annotation> {
    protected final Class<A> constraintType;
    protected final Map<String, Object> parameters;

    protected ConstraintDef(Class<A> constraintType) {
        this.constraintType = constraintType;
        this.parameters = new HashMap();
    }

    protected ConstraintDef(ConstraintDef<?, A> original) {
        this.constraintType = original.constraintType;
        this.parameters = original.parameters;
    }

    private C getThis() {
        return this;
    }

    protected C addParameter(String str, Object obj) {
        this.parameters.put(str, obj);
        return (C) getThis();
    }

    public C message(String str) {
        addParameter(ConstraintHelper.MESSAGE, str);
        return (C) getThis();
    }

    public C groups(Class<?>... clsArr) {
        addParameter(ConstraintHelper.GROUPS, clsArr);
        return (C) getThis();
    }

    public C payload(Class<? extends Payload>... clsArr) {
        addParameter(ConstraintHelper.PAYLOAD, clsArr);
        return (C) getThis();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append(", constraintType=").append(this.constraintType);
        sb.append(", parameters=").append(this.parameters);
        sb.append('}');
        return sb.toString();
    }
}
