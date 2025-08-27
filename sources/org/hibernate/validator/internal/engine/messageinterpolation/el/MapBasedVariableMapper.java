package org.hibernate.validator.internal.engine.messageinterpolation.el;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.el.ValueExpression;
import javax.el.VariableMapper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/messageinterpolation/el/MapBasedVariableMapper.class */
public class MapBasedVariableMapper extends VariableMapper {
    private Map<String, ValueExpression> map = Collections.emptyMap();

    @Override // javax.el.VariableMapper
    public ValueExpression resolveVariable(String variable) {
        return this.map.get(variable);
    }

    @Override // javax.el.VariableMapper
    public ValueExpression setVariable(String variable, ValueExpression expression) {
        if (this.map.isEmpty()) {
            this.map = new HashMap();
        }
        return this.map.put(variable, expression);
    }
}
