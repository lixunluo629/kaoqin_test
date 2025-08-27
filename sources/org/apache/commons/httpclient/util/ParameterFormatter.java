package org.apache.commons.httpclient.util;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.apache.commons.httpclient.NameValuePair;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/util/ParameterFormatter.class */
public class ParameterFormatter {
    private static final char[] SEPARATORS = {'(', ')', '<', '>', '@', ',', ';', ':', '\\', '\"', '/', '[', ']', '?', '=', '{', '}', ' ', '\t'};
    private static final char[] UNSAFE_CHARS = {'\"', '\\'};
    private boolean alwaysUseQuotes = true;

    private static boolean isOneOf(char[] chars, char ch2) {
        for (char c : chars) {
            if (ch2 == c) {
                return true;
            }
        }
        return false;
    }

    private static boolean isUnsafeChar(char ch2) {
        return isOneOf(UNSAFE_CHARS, ch2);
    }

    private static boolean isSeparator(char ch2) {
        return isOneOf(SEPARATORS, ch2);
    }

    public boolean isAlwaysUseQuotes() {
        return this.alwaysUseQuotes;
    }

    public void setAlwaysUseQuotes(boolean alwaysUseQuotes) {
        this.alwaysUseQuotes = alwaysUseQuotes;
    }

    public static void formatValue(StringBuffer buffer, String value, boolean alwaysUseQuotes) {
        if (buffer == null) {
            throw new IllegalArgumentException("String buffer may not be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("Value buffer may not be null");
        }
        if (alwaysUseQuotes) {
            buffer.append('\"');
            for (int i = 0; i < value.length(); i++) {
                char ch2 = value.charAt(i);
                if (isUnsafeChar(ch2)) {
                    buffer.append('\\');
                }
                buffer.append(ch2);
            }
            buffer.append('\"');
            return;
        }
        int offset = buffer.length();
        boolean unsafe = false;
        for (int i2 = 0; i2 < value.length(); i2++) {
            char ch3 = value.charAt(i2);
            if (isSeparator(ch3)) {
                unsafe = true;
            }
            if (isUnsafeChar(ch3)) {
                buffer.append('\\');
            }
            buffer.append(ch3);
        }
        if (unsafe) {
            buffer.insert(offset, '\"');
            buffer.append('\"');
        }
    }

    public void format(StringBuffer buffer, NameValuePair param) {
        if (buffer == null) {
            throw new IllegalArgumentException("String buffer may not be null");
        }
        if (param == null) {
            throw new IllegalArgumentException("Parameter may not be null");
        }
        buffer.append(param.getName());
        String value = param.getValue();
        if (value != null) {
            buffer.append(SymbolConstants.EQUAL_SYMBOL);
            formatValue(buffer, value, this.alwaysUseQuotes);
        }
    }

    public String format(NameValuePair param) {
        StringBuffer buffer = new StringBuffer();
        format(buffer, param);
        return buffer.toString();
    }
}
