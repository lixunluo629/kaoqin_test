package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import javax.annotation.Nullable;

@Beta
@GwtCompatible(emulated = true)
/* loaded from: guava-18.0.jar:com/google/common/base/Enums.class */
public final class Enums {

    @GwtIncompatible("java.lang.ref.WeakReference")
    private static final Map<Class<? extends Enum<?>>, Map<String, WeakReference<? extends Enum<?>>>> enumConstantCache = new WeakHashMap();

    private Enums() {
    }

    @GwtIncompatible("reflection")
    public static Field getField(Enum<?> enumValue) {
        try {
            return enumValue.getDeclaringClass().getDeclaredField(enumValue.name());
        } catch (NoSuchFieldException impossible) {
            throw new AssertionError(impossible);
        }
    }

    public static <T extends Enum<T>> Optional<T> getIfPresent(Class<T> enumClass, String value) {
        Preconditions.checkNotNull(enumClass);
        Preconditions.checkNotNull(value);
        return Platform.getEnumIfPresent(enumClass, value);
    }

    @GwtIncompatible("java.lang.ref.WeakReference")
    private static <T extends Enum<T>> Map<String, WeakReference<? extends Enum<?>>> populateCache(Class<T> enumClass) {
        Map<String, WeakReference<? extends Enum<?>>> result = new HashMap<>();
        Iterator i$ = EnumSet.allOf(enumClass).iterator();
        while (i$.hasNext()) {
            Enum r0 = (Enum) i$.next();
            result.put(r0.name(), new WeakReference<>(r0));
        }
        enumConstantCache.put(enumClass, result);
        return result;
    }

    @GwtIncompatible("java.lang.ref.WeakReference")
    static <T extends Enum<T>> Map<String, WeakReference<? extends Enum<?>>> getEnumConstants(Class<T> enumClass) {
        Map<String, WeakReference<? extends Enum<?>>> map;
        synchronized (enumConstantCache) {
            Map<String, WeakReference<? extends Enum<?>>> constants = enumConstantCache.get(enumClass);
            if (constants == null) {
                constants = populateCache(enumClass);
            }
            map = constants;
        }
        return map;
    }

    public static <T extends Enum<T>> Converter<String, T> stringConverter(Class<T> enumClass) {
        return new StringConverter(enumClass);
    }

    /* loaded from: guava-18.0.jar:com/google/common/base/Enums$StringConverter.class */
    private static final class StringConverter<T extends Enum<T>> extends Converter<String, T> implements Serializable {
        private final Class<T> enumClass;
        private static final long serialVersionUID = 0;

        StringConverter(Class<T> enumClass) {
            this.enumClass = (Class) Preconditions.checkNotNull(enumClass);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.base.Converter
        public T doForward(String str) {
            return (T) Enum.valueOf(this.enumClass, str);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.base.Converter
        public String doBackward(T enumValue) {
            return enumValue.name();
        }

        @Override // com.google.common.base.Converter, com.google.common.base.Function
        public boolean equals(@Nullable Object object) {
            if (object instanceof StringConverter) {
                StringConverter<?> that = (StringConverter) object;
                return this.enumClass.equals(that.enumClass);
            }
            return false;
        }

        public int hashCode() {
            return this.enumClass.hashCode();
        }

        public String toString() {
            String strValueOf = String.valueOf(String.valueOf(this.enumClass.getName()));
            return new StringBuilder(29 + strValueOf.length()).append("Enums.stringConverter(").append(strValueOf).append(".class)").toString();
        }
    }
}
