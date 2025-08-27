package org.hibernate.validator.internal.util;

import java.util.Arrays;
import java.util.Locale;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/util/StringHelper.class */
public class StringHelper {
    private StringHelper() {
    }

    public static String join(Object[] array, String separator) {
        if (array != null) {
            return join(Arrays.asList(array), separator);
        }
        return null;
    }

    public static String join(Iterable<?> iterable, String separator) {
        if (iterable == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (Object object : iterable) {
            if (!isFirst) {
                sb.append(separator);
            } else {
                isFirst = false;
            }
            sb.append(object);
        }
        return sb.toString();
    }

    public static String decapitalize(String string) {
        if (string == null || string.isEmpty() || startsWithSeveralUpperCaseLetters(string)) {
            return string;
        }
        return string.substring(0, 1).toLowerCase(Locale.ROOT) + string.substring(1);
    }

    public static boolean isNullOrEmptyString(String value) {
        return value == null || value.trim().isEmpty();
    }

    private static boolean startsWithSeveralUpperCaseLetters(String string) {
        return string.length() > 1 && Character.isUpperCase(string.charAt(0)) && Character.isUpperCase(string.charAt(1));
    }
}
