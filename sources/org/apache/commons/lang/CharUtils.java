package org.apache.commons.lang;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/CharUtils.class */
public class CharUtils {
    private static final String CHAR_STRING = "��\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t\n\u000b\f\r\u000e\u000f\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001a\u001b\u001c\u001d\u001e\u001f !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u007f";
    private static final String[] CHAR_STRING_ARRAY = new String[128];
    private static final Character[] CHAR_ARRAY = new Character[128];
    public static final char LF = '\n';
    public static final char CR = '\r';

    static {
        for (int i = 127; i >= 0; i--) {
            CHAR_STRING_ARRAY[i] = CHAR_STRING.substring(i, i + 1);
            CHAR_ARRAY[i] = new Character((char) i);
        }
    }

    public static Character toCharacterObject(char ch2) {
        if (ch2 < CHAR_ARRAY.length) {
            return CHAR_ARRAY[ch2];
        }
        return new Character(ch2);
    }

    public static Character toCharacterObject(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return toCharacterObject(str.charAt(0));
    }

    public static char toChar(Character ch2) {
        if (ch2 == null) {
            throw new IllegalArgumentException("The Character must not be null");
        }
        return ch2.charValue();
    }

    public static char toChar(Character ch2, char defaultValue) {
        if (ch2 == null) {
            return defaultValue;
        }
        return ch2.charValue();
    }

    public static char toChar(String str) {
        if (StringUtils.isEmpty(str)) {
            throw new IllegalArgumentException("The String must not be empty");
        }
        return str.charAt(0);
    }

    public static char toChar(String str, char defaultValue) {
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        return str.charAt(0);
    }

    public static int toIntValue(char ch2) {
        if (!isAsciiNumeric(ch2)) {
            throw new IllegalArgumentException(new StringBuffer().append("The character ").append(ch2).append(" is not in the range '0' - '9'").toString());
        }
        return ch2 - '0';
    }

    public static int toIntValue(char ch2, int defaultValue) {
        if (!isAsciiNumeric(ch2)) {
            return defaultValue;
        }
        return ch2 - '0';
    }

    public static int toIntValue(Character ch2) {
        if (ch2 == null) {
            throw new IllegalArgumentException("The character must not be null");
        }
        return toIntValue(ch2.charValue());
    }

    public static int toIntValue(Character ch2, int defaultValue) {
        if (ch2 == null) {
            return defaultValue;
        }
        return toIntValue(ch2.charValue(), defaultValue);
    }

    public static String toString(char ch2) {
        if (ch2 < 128) {
            return CHAR_STRING_ARRAY[ch2];
        }
        return new String(new char[]{ch2});
    }

    public static String toString(Character ch2) {
        if (ch2 == null) {
            return null;
        }
        return toString(ch2.charValue());
    }

    public static String unicodeEscaped(char ch2) {
        if (ch2 < 16) {
            return new StringBuffer().append("\\u000").append(Integer.toHexString(ch2)).toString();
        }
        if (ch2 < 256) {
            return new StringBuffer().append("\\u00").append(Integer.toHexString(ch2)).toString();
        }
        if (ch2 < 4096) {
            return new StringBuffer().append("\\u0").append(Integer.toHexString(ch2)).toString();
        }
        return new StringBuffer().append("\\u").append(Integer.toHexString(ch2)).toString();
    }

    public static String unicodeEscaped(Character ch2) {
        if (ch2 == null) {
            return null;
        }
        return unicodeEscaped(ch2.charValue());
    }

    public static boolean isAscii(char ch2) {
        return ch2 < 128;
    }

    public static boolean isAsciiPrintable(char ch2) {
        return ch2 >= ' ' && ch2 < 127;
    }

    public static boolean isAsciiControl(char ch2) {
        return ch2 < ' ' || ch2 == 127;
    }

    public static boolean isAsciiAlpha(char ch2) {
        return (ch2 >= 'A' && ch2 <= 'Z') || (ch2 >= 'a' && ch2 <= 'z');
    }

    public static boolean isAsciiAlphaUpper(char ch2) {
        return ch2 >= 'A' && ch2 <= 'Z';
    }

    public static boolean isAsciiAlphaLower(char ch2) {
        return ch2 >= 'a' && ch2 <= 'z';
    }

    public static boolean isAsciiNumeric(char ch2) {
        return ch2 >= '0' && ch2 <= '9';
    }

    public static boolean isAsciiAlphanumeric(char ch2) {
        return (ch2 >= 'A' && ch2 <= 'Z') || (ch2 >= 'a' && ch2 <= 'z') || (ch2 >= '0' && ch2 <= '9');
    }

    static boolean isHighSurrogate(char ch2) {
        return 55296 <= ch2 && 56319 >= ch2;
    }
}
