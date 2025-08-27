package com.sun.jna;

/* loaded from: jna-3.0.9.jar:com/sun/jna/FromNativeConverter.class */
public interface FromNativeConverter {
    Object fromNative(Object obj, FromNativeContext fromNativeContext);

    Class nativeType();
}
