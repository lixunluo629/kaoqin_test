package org.hibernate.validator.internal.engine.messageinterpolation.el;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.el.FunctionMapper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/messageinterpolation/el/MapBasedFunctionMapper.class */
public class MapBasedFunctionMapper extends FunctionMapper {
    private static final String FUNCTION_NAME_SEPARATOR = ":";
    private Map<String, Method> map = Collections.emptyMap();

    @Override // javax.el.FunctionMapper
    public Method resolveFunction(String prefix, String localName) {
        return this.map.get(prefix + ":" + localName);
    }

    public void setFunction(String prefix, String localName, Method method) {
        if (this.map.isEmpty()) {
            this.map = new HashMap();
        }
        this.map.put(prefix + ":" + localName, method);
    }
}
