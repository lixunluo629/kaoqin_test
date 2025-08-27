package com.sun.jna;

/* loaded from: jna-3.0.9.jar:com/sun/jna/NativeMapped.class */
public interface NativeMapped {
    Object fromNative(Object obj, FromNativeContext fromNativeContext);

    Object toNative();

    Class nativeType();
}
