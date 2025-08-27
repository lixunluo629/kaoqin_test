package com.sun.jna;

/* loaded from: jna-3.0.9.jar:com/sun/jna/CallbackProxy.class */
public interface CallbackProxy extends Callback {
    Object callback(Object[] objArr);

    Class[] getParameterTypes();

    Class getReturnType();
}
