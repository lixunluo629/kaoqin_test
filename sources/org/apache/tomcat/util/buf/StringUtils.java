package org.apache.tomcat.util.buf;

import java.util.Arrays;
import java.util.Collection;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/buf/StringUtils.class */
public final class StringUtils {
    private static final String EMPTY_STRING = "";

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/buf/StringUtils$Function.class */
    public interface Function<T> {
        String apply(T t);
    }

    private StringUtils() {
    }

    public static String join(String[] array) {
        if (array == null) {
            return "";
        }
        return join(Arrays.asList(array));
    }

    public static void join(String[] array, char separator, StringBuilder sb) {
        if (array == null) {
            return;
        }
        join(Arrays.asList(array), separator, sb);
    }

    public static String join(Collection<String> collection) {
        return join(collection, ',');
    }

    public static String join(Collection<String> collection, char separator) {
        if (collection == null || collection.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        join(collection, separator, result);
        return result.toString();
    }

    public static void join(Iterable<String> iterable, char separator, StringBuilder sb) {
        join(iterable, separator, new Function<String>() { // from class: org.apache.tomcat.util.buf.StringUtils.1
            @Override // org.apache.tomcat.util.buf.StringUtils.Function
            public String apply(String t) {
                return t;
            }
        }, sb);
    }

    public static <T> void join(T[] array, char separator, Function<T> function, StringBuilder sb) {
        if (array == null) {
            return;
        }
        join(Arrays.asList(array), separator, function, sb);
    }

    public static <T> void join(Iterable<T> iterable, char separator, Function<T> function, StringBuilder sb) {
        if (iterable == null) {
            return;
        }
        boolean first = true;
        for (T value : iterable) {
            if (first) {
                first = false;
            } else {
                sb.append(separator);
            }
            sb.append(function.apply(value));
        }
    }
}
