package com.sun.jna;

/* loaded from: jna-3.0.9.jar:com/sun/jna/FromNativeContext.class */
public class FromNativeContext {
    private Class type;

    FromNativeContext(Class javaType) {
        this.type = javaType;
    }

    public Class getTargetType() {
        return this.type;
    }
}
