package com.sun.jna;

import java.util.Arrays;
import java.util.Collection;
import org.springframework.jmx.export.naming.IdentityNamingStrategy;

/* loaded from: jna-3.0.9.jar:com/sun/jna/Callback.class */
public interface Callback {
    public static final String METHOD_NAME = "callback";
    public static final Collection FORBIDDEN_NAMES = Arrays.asList(IdentityNamingStrategy.HASH_CODE_KEY, "equals", "toString");
}
