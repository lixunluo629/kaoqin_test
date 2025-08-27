package org.terracotta.offheapstore.util;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/util/Validation.class */
public final class Validation {
    private static final boolean VALIDATE_ALL;

    static {
        boolean validate;
        try {
            Class.forName("org.terracotta.offheapstore.util.ValidationTest");
            validate = true;
        } catch (Throwable th) {
            validate = false;
        }
        VALIDATE_ALL = validate;
    }

    public static boolean shouldValidate(Class<?> klazz) {
        return VALIDATE_ALL || Boolean.getBoolean(new StringBuilder().append(klazz.getName()).append(".VALIDATE").toString());
    }

    public static void validate(boolean assertion) {
        if (!assertion) {
            throw new AssertionError();
        }
    }
}
