package com.sun.jna;

import java.lang.reflect.Method;

/* loaded from: jna-3.0.9.jar:com/sun/jna/FunctionMapper.class */
public interface FunctionMapper {
    String getFunctionName(NativeLibrary nativeLibrary, Method method);
}
