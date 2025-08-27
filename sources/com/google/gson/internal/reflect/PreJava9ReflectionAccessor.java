package com.google.gson.internal.reflect;

import java.lang.reflect.AccessibleObject;

/* loaded from: gson-2.8.5.jar:com/google/gson/internal/reflect/PreJava9ReflectionAccessor.class */
final class PreJava9ReflectionAccessor extends ReflectionAccessor {
    PreJava9ReflectionAccessor() {
    }

    @Override // com.google.gson.internal.reflect.ReflectionAccessor
    public void makeAccessible(AccessibleObject ao) throws SecurityException {
        ao.setAccessible(true);
    }
}
