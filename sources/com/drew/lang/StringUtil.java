package com.drew.lang;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/lang/StringUtil.class */
public final class StringUtil {
    @NotNull
    public static String join(@NotNull Iterable<? extends CharSequence> strings, @NotNull String delimiter) {
        int capacity = 0;
        int delimLength = delimiter.length();
        Iterator<? extends CharSequence> iter = strings.iterator();
        if (iter.hasNext()) {
            capacity = 0 + iter.next().length() + delimLength;
        }
        StringBuilder buffer = new StringBuilder(capacity);
        Iterator<? extends CharSequence> iter2 = strings.iterator();
        if (iter2.hasNext()) {
            buffer.append(iter2.next());
            while (iter2.hasNext()) {
                buffer.append(delimiter);
                buffer.append(iter2.next());
            }
        }
        return buffer.toString();
    }

    @NotNull
    public static <T extends CharSequence> String join(@NotNull T[] strings, @NotNull String delimiter) {
        int capacity = 0;
        int delimLength = delimiter.length();
        for (T value : strings) {
            capacity += value.length() + delimLength;
        }
        StringBuilder buffer = new StringBuilder(capacity);
        boolean first = true;
        for (T value2 : strings) {
            if (!first) {
                buffer.append(delimiter);
            } else {
                first = false;
            }
            buffer.append((CharSequence) value2);
        }
        return buffer.toString();
    }

    @NotNull
    public static String fromStream(@NotNull InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        while (true) {
            String line = reader.readLine();
            if (line != null) {
                sb.append(line);
            } else {
                return sb.toString();
            }
        }
    }

    public static int compare(@Nullable String s1, @Nullable String s2) {
        boolean null1 = s1 == null;
        boolean null2 = s2 == null;
        if (null1 && null2) {
            return 0;
        }
        if (null1) {
            return -1;
        }
        if (null2) {
            return 1;
        }
        return s1.compareTo(s2);
    }

    @NotNull
    public static String urlEncode(@NotNull String name) {
        return name.replace(SymbolConstants.SPACE_SYMBOL, "%20");
    }
}
