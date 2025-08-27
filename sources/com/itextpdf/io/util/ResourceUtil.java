package com.itextpdf.io.util;

import java.io.InputStream;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/util/ResourceUtil.class */
public final class ResourceUtil {
    private ResourceUtil() {
    }

    public static InputStream getResourceStream(String key) {
        return getResourceStream(key, null);
    }

    public static InputStream getResourceStream(String key, ClassLoader loader) {
        if (key.startsWith("/")) {
            key = key.substring(1);
        }
        InputStream stream = null;
        if (loader != null) {
            stream = loader.getResourceAsStream(key);
            if (stream != null) {
                return stream;
            }
        }
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader != null) {
                stream = contextClassLoader.getResourceAsStream(key);
            }
        } catch (SecurityException e) {
        }
        if (stream == null) {
            stream = ResourceUtil.class.getResourceAsStream("/" + key);
        }
        if (stream == null) {
            stream = ClassLoader.getSystemResourceAsStream(key);
        }
        return stream;
    }
}
