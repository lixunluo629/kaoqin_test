package org.apache.xmlbeans.impl.common;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/XmlWhitespace.class */
public class XmlWhitespace {
    public static final int WS_UNSPECIFIED = 0;
    public static final int WS_PRESERVE = 1;
    public static final int WS_REPLACE = 2;
    public static final int WS_COLLAPSE = 3;

    public static boolean isSpace(char ch2) {
        switch (ch2) {
            case '\t':
            case '\n':
            case '\r':
            case ' ':
                return true;
            default:
                return false;
        }
    }

    public static boolean isAllSpace(String v) {
        int len = v.length();
        for (int i = 0; i < len; i++) {
            if (!isSpace(v.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllSpace(CharSequence v) {
        int len = v.length();
        for (int i = 0; i < len; i++) {
            if (!isSpace(v.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String collapse(String v) {
        return collapse(v, 3);
    }

    public static String collapse(String v, int wsr) {
        int i;
        if (wsr == 1 || wsr == 0) {
            return v;
        }
        if (v.indexOf(10) >= 0) {
            v = v.replace('\n', ' ');
        }
        if (v.indexOf(9) >= 0) {
            v = v.replace('\t', ' ');
        }
        if (v.indexOf(13) >= 0) {
            v = v.replace('\r', ' ');
        }
        if (wsr == 2) {
            return v;
        }
        int j = 0;
        int len = v.length();
        if (len == 0) {
            return v;
        }
        if (v.charAt(0) != ' ') {
            j = 2;
            while (true) {
                if (j < len) {
                    if (v.charAt(j) == ' ') {
                        if (v.charAt(j - 1) == ' ' || j == len - 1) {
                            break;
                        }
                        j++;
                        if (v.charAt(j) == ' ') {
                            break;
                        }
                    }
                    j += 2;
                } else if (j != len || v.charAt(j - 1) != ' ') {
                    return v;
                }
            }
            i = j;
        } else {
            while (j + 1 < v.length() && v.charAt(j + 1) == ' ') {
                j++;
            }
            i = 0;
        }
        char[] ch2 = v.toCharArray();
        loop1: while (true) {
            j++;
            if (j >= len) {
                break;
            }
            if (v.charAt(j) != ' ') {
                while (true) {
                    int i2 = i;
                    i++;
                    int i3 = j;
                    j++;
                    ch2[i2] = ch2[i3];
                    if (j >= len) {
                        break loop1;
                    }
                    if (ch2[j] == ' ') {
                        i++;
                        j++;
                        ch2[i] = ch2[j];
                        if (j >= len) {
                            break loop1;
                        }
                        if (ch2[j] == ' ') {
                            break;
                        }
                    }
                }
            }
        }
        return new String(ch2, 0, (i == 0 || ch2[i - 1] != ' ') ? i : i - 1);
    }
}
