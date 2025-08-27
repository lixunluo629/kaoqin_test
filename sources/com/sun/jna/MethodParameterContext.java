package com.sun.jna;

import java.lang.reflect.Method;

/* loaded from: jna-3.0.9.jar:com/sun/jna/MethodParameterContext.class */
public class MethodParameterContext extends FunctionParameterContext {
    private Method method;

    MethodParameterContext(Function f, Object[] args, int index, Method m) {
        super(f, args, index);
        this.method = m;
    }

    public Method getMethod() {
        return this.method;
    }
}
