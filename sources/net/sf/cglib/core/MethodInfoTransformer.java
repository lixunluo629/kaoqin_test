package net.sf.cglib.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/* loaded from: cglib-3.1.jar:net/sf/cglib/core/MethodInfoTransformer.class */
public class MethodInfoTransformer implements Transformer {
    private static final MethodInfoTransformer INSTANCE = new MethodInfoTransformer();

    public static MethodInfoTransformer getInstance() {
        return INSTANCE;
    }

    @Override // net.sf.cglib.core.Transformer
    public Object transform(Object value) {
        if (value instanceof Method) {
            return ReflectUtils.getMethodInfo((Method) value);
        }
        if (value instanceof Constructor) {
            return ReflectUtils.getMethodInfo((Constructor) value);
        }
        throw new IllegalArgumentException(new StringBuffer().append("cannot get method info for ").append(value).toString());
    }
}
