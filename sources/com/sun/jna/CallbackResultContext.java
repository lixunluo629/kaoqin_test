package com.sun.jna;

import java.lang.reflect.Method;

/* loaded from: jna-3.0.9.jar:com/sun/jna/CallbackResultContext.class */
public class CallbackResultContext extends ToNativeContext {
    private Method method;

    CallbackResultContext(Method callbackMethod) {
        this.method = callbackMethod;
    }

    public Method getMethod() {
        return this.method;
    }
}
