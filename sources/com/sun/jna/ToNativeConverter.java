package com.sun.jna;

/* loaded from: jna-3.0.9.jar:com/sun/jna/ToNativeConverter.class */
public interface ToNativeConverter {
    Object toNative(Object obj, ToNativeContext toNativeContext);

    Class nativeType();
}
