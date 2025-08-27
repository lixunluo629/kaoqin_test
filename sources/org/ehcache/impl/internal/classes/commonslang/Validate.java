package org.ehcache.impl.internal.classes.commonslang;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/classes/commonslang/Validate.class */
public class Validate {
    public static <T> T notNull(T object, String message, Object... values) {
        if (object == null) {
            throw new NullPointerException(String.format(message, values));
        }
        return object;
    }
}
