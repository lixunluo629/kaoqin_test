package org.apache.log4j;

import java.util.Hashtable;
import java.util.Map;

/* loaded from: log4j-over-slf4j-1.7.26.jar:org/apache/log4j/MDC.class */
public class MDC {
    public static void put(String key, String value) throws IllegalArgumentException {
        org.slf4j.MDC.put(key, value);
    }

    public static void put(String key, Object value) throws IllegalArgumentException {
        if (value != null) {
            put(key, value.toString());
        } else {
            put(key, (String) null);
        }
    }

    public static Object get(String key) {
        return org.slf4j.MDC.get(key);
    }

    public static void remove(String key) throws IllegalArgumentException {
        org.slf4j.MDC.remove(key);
    }

    public static void clear() {
        org.slf4j.MDC.clear();
    }

    @Deprecated
    public static Hashtable getContext() {
        Map map = org.slf4j.MDC.getCopyOfContextMap();
        if (map != null) {
            return new Hashtable(map);
        }
        return new Hashtable();
    }
}
