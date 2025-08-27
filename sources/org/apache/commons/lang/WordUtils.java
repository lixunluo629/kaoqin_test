package org.apache.commons.lang;

import com.moredian.onpremise.core.common.constants.SymbolConstants;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/WordUtils.class */
public class WordUtils {
    public static String wrap(String str, int wrapLength) {
        return wrap(str, wrapLength, null, false);
    }

    public static String wrap(String str, int wrapLength, String newLineStr, boolean wrapLongWords) {
        if (str == null) {
            return null;
        }
        if (newLineStr == null) {
            newLineStr = SystemUtils.LINE_SEPARATOR;
        }
        if (wrapLength < 1) {
            wrapLength = 1;
        }
        int inputLineLength = str.length();
        int offset = 0;
        StringBuffer wrappedLine = new StringBuffer(inputLineLength + 32);
        while (inputLineLength - offset > wrapLength) {
            if (str.charAt(offset) == ' ') {
                offset++;
            } else {
                int spaceToWrapAt = str.lastIndexOf(32, wrapLength + offset);
                if (spaceToWrapAt >= offset) {
                    wrappedLine.append(str.substring(offset, spaceToWrapAt));
                    wrappedLine.append(newLineStr);
                    offset = spaceToWrapAt + 1;
                } else if (wrapLongWords) {
                    wrappedLine.append(str.substring(offset, wrapLength + offset));
                    wrappedLine.append(newLineStr);
                    offset += wrapLength;
                } else {
                    int spaceToWrapAt2 = str.indexOf(32, wrapLength + offset);
                    if (spaceToWrapAt2 >= 0) {
                        wrappedLine.append(str.substring(offset, spaceToWrapAt2));
                        wrappedLine.append(newLineStr);
                        offset = spaceToWrapAt2 + 1;
                    } else {
                        wrappedLine.append(str.substring(offset));
                        offset = inputLineLength;
                    }
                }
            }
        }
        wrappedLine.append(str.substring(offset));
        return wrappedLine.toString();
    }

    public static String capitalize(String str) {
        return capitalize(str, null);
    }

    public static String capitalize(String str, char[] delimiters) {
        int delimLen = delimiters == null ? -1 : delimiters.length;
        if (str == null || str.length() == 0 || delimLen == 0) {
            return str;
        }
        int strLen = str.length();
        StringBuffer buffer = new StringBuffer(strLen);
        boolean capitalizeNext = true;
        for (int i = 0; i < strLen; i++) {
            char ch2 = str.charAt(i);
            if (isDelimiter(ch2, delimiters)) {
                buffer.append(ch2);
                capitalizeNext = true;
            } else if (capitalizeNext) {
                buffer.append(Character.toTitleCase(ch2));
                capitalizeNext = false;
            } else {
                buffer.append(ch2);
            }
        }
        return buffer.toString();
    }

    public static String capitalizeFully(String str) {
        return capitalizeFully(str, null);
    }

    public static String capitalizeFully(String str, char[] delimiters) {
        int delimLen = delimiters == null ? -1 : delimiters.length;
        if (str == null || str.length() == 0 || delimLen == 0) {
            return str;
        }
        return capitalize(str.toLowerCase(), delimiters);
    }

    public static String uncapitalize(String str) {
        return uncapitalize(str, null);
    }

    public static String uncapitalize(String str, char[] delimiters) {
        int delimLen = delimiters == null ? -1 : delimiters.length;
        if (str == null || str.length() == 0 || delimLen == 0) {
            return str;
        }
        int strLen = str.length();
        StringBuffer buffer = new StringBuffer(strLen);
        boolean uncapitalizeNext = true;
        for (int i = 0; i < strLen; i++) {
            char ch2 = str.charAt(i);
            if (isDelimiter(ch2, delimiters)) {
                buffer.append(ch2);
                uncapitalizeNext = true;
            } else if (uncapitalizeNext) {
                buffer.append(Character.toLowerCase(ch2));
                uncapitalizeNext = false;
            } else {
                buffer.append(ch2);
            }
        }
        return buffer.toString();
    }

    public static String swapCase(String str) {
        int strLen;
        char upperCase;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        StringBuffer buffer = new StringBuffer(strLen);
        boolean whitespace = true;
        for (int i = 0; i < strLen; i++) {
            char ch2 = str.charAt(i);
            if (Character.isUpperCase(ch2)) {
                upperCase = Character.toLowerCase(ch2);
            } else if (Character.isTitleCase(ch2)) {
                upperCase = Character.toLowerCase(ch2);
            } else if (Character.isLowerCase(ch2)) {
                if (whitespace) {
                    upperCase = Character.toTitleCase(ch2);
                } else {
                    upperCase = Character.toUpperCase(ch2);
                }
            } else {
                upperCase = ch2;
            }
            char tmp = upperCase;
            buffer.append(tmp);
            whitespace = Character.isWhitespace(ch2);
        }
        return buffer.toString();
    }

    public static String initials(String str) {
        return initials(str, null);
    }

    public static String initials(String str, char[] delimiters) {
        if (str == null || str.length() == 0) {
            return str;
        }
        if (delimiters != null && delimiters.length == 0) {
            return "";
        }
        int strLen = str.length();
        char[] buf = new char[(strLen / 2) + 1];
        int count = 0;
        boolean lastWasGap = true;
        for (int i = 0; i < strLen; i++) {
            char ch2 = str.charAt(i);
            if (isDelimiter(ch2, delimiters)) {
                lastWasGap = true;
            } else if (lastWasGap) {
                int i2 = count;
                count++;
                buf[i2] = ch2;
                lastWasGap = false;
            }
        }
        return new String(buf, 0, count);
    }

    private static boolean isDelimiter(char ch2, char[] delimiters) {
        if (delimiters == null) {
            return Character.isWhitespace(ch2);
        }
        for (char c : delimiters) {
            if (ch2 == c) {
                return true;
            }
        }
        return false;
    }

    public static String abbreviate(String str, int lower, int upper, String appendToEnd) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return "";
        }
        if (lower > str.length()) {
            lower = str.length();
        }
        if (upper == -1 || upper > str.length()) {
            upper = str.length();
        }
        if (upper < lower) {
            upper = lower;
        }
        StringBuffer result = new StringBuffer();
        int index = StringUtils.indexOf(str, SymbolConstants.SPACE_SYMBOL, lower);
        if (index == -1) {
            result.append(str.substring(0, upper));
            if (upper != str.length()) {
                result.append(StringUtils.defaultString(appendToEnd));
            }
        } else if (index > upper) {
            result.append(str.substring(0, upper));
            result.append(StringUtils.defaultString(appendToEnd));
        } else {
            result.append(str.substring(0, index));
            result.append(StringUtils.defaultString(appendToEnd));
        }
        return result.toString();
    }
}
