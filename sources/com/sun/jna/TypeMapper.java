package com.sun.jna;

/* loaded from: jna-3.0.9.jar:com/sun/jna/TypeMapper.class */
public interface TypeMapper {
    FromNativeConverter getFromNativeConverter(Class cls);

    ToNativeConverter getToNativeConverter(Class cls);
}
