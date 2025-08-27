package org.springframework.expression.spel.support;

import org.springframework.expression.TypedValue;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/support/BooleanTypedValue.class */
public class BooleanTypedValue extends TypedValue {
    public static final BooleanTypedValue TRUE = new BooleanTypedValue(true);
    public static final BooleanTypedValue FALSE = new BooleanTypedValue(false);

    private BooleanTypedValue(boolean b) {
        super(Boolean.valueOf(b));
    }

    public static BooleanTypedValue forValue(boolean b) {
        return b ? TRUE : FALSE;
    }
}
