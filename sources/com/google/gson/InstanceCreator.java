package com.google.gson;

import java.lang.reflect.Type;

/* loaded from: gson-2.8.5.jar:com/google/gson/InstanceCreator.class */
public interface InstanceCreator<T> {
    T createInstance(Type type);
}
