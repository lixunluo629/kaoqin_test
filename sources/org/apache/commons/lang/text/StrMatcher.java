package org.apache.commons.lang.text;

import java.util.Arrays;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/text/StrMatcher.class */
public abstract class StrMatcher {
    private static final StrMatcher COMMA_MATCHER = new CharMatcher(',');
    private static final StrMatcher TAB_MATCHER = new CharMatcher('\t');
    private static final StrMatcher SPACE_MATCHER = new CharMatcher(' ');
    private static final StrMatcher SPLIT_MATCHER = new CharSetMatcher(" \t\n\r\f".toCharArray());
    private static final StrMatcher TRIM_MATCHER = new TrimMatcher();
    private static final StrMatcher SINGLE_QUOTE_MATCHER = new CharMatcher('\'');
    private static final StrMatcher DOUBLE_QUOTE_MATCHER = new CharMatcher('\"');
    private static final StrMatcher QUOTE_MATCHER = new CharSetMatcher("'\"".toCharArray());
    private static final StrMatcher NONE_MATCHER = new NoMatcher();

    public abstract int isMatch(char[] cArr, int i, int i2, int i3);

    public static StrMatcher commaMatcher() {
        return COMMA_MATCHER;
    }

    public static StrMatcher tabMatcher() {
        return TAB_MATCHER;
    }

    public static StrMatcher spaceMatcher() {
        return SPACE_MATCHER;
    }

    public static StrMatcher splitMatcher() {
        return SPLIT_MATCHER;
    }

    public static StrMatcher trimMatcher() {
        return TRIM_MATCHER;
    }

    public static StrMatcher singleQuoteMatcher() {
        return SINGLE_QUOTE_MATCHER;
    }

    public static StrMatcher doubleQuoteMatcher() {
        return DOUBLE_QUOTE_MATCHER;
    }

    public static StrMatcher quoteMatcher() {
        return QUOTE_MATCHER;
    }

    public static StrMatcher noneMatcher() {
        return NONE_MATCHER;
    }

    public static StrMatcher charMatcher(char ch2) {
        return new CharMatcher(ch2);
    }

    public static StrMatcher charSetMatcher(char[] chars) {
        if (chars == null || chars.length == 0) {
            return NONE_MATCHER;
        }
        if (chars.length == 1) {
            return new CharMatcher(chars[0]);
        }
        return new CharSetMatcher(chars);
    }

    public static StrMatcher charSetMatcher(String chars) {
        if (chars == null || chars.length() == 0) {
            return NONE_MATCHER;
        }
        if (chars.length() == 1) {
            return new CharMatcher(chars.charAt(0));
        }
        return new CharSetMatcher(chars.toCharArray());
    }

    public static StrMatcher stringMatcher(String str) {
        if (str == null || str.length() == 0) {
            return NONE_MATCHER;
        }
        return new StringMatcher(str);
    }

    protected StrMatcher() {
    }

    public int isMatch(char[] buffer, int pos) {
        return isMatch(buffer, pos, 0, buffer.length);
    }

    /* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/text/StrMatcher$CharSetMatcher.class */
    static final class CharSetMatcher extends StrMatcher {
        private final char[] chars;

        CharSetMatcher(char[] chars) {
            this.chars = (char[]) chars.clone();
            Arrays.sort(this.chars);
        }

        @Override // org.apache.commons.lang.text.StrMatcher
        public int isMatch(char[] buffer, int pos, int bufferStart, int bufferEnd) {
            return Arrays.binarySearch(this.chars, buffer[pos]) >= 0 ? 1 : 0;
        }
    }

    /* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/text/StrMatcher$CharMatcher.class */
    static final class CharMatcher extends StrMatcher {

        /* renamed from: ch, reason: collision with root package name */
        private final char f8ch;

        CharMatcher(char ch2) {
            this.f8ch = ch2;
        }

        @Override // org.apache.commons.lang.text.StrMatcher
        public int isMatch(char[] buffer, int pos, int bufferStart, int bufferEnd) {
            return this.f8ch == buffer[pos] ? 1 : 0;
        }
    }

    /* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/text/StrMatcher$StringMatcher.class */
    static final class StringMatcher extends StrMatcher {
        private final char[] chars;

        StringMatcher(String str) {
            this.chars = str.toCharArray();
        }

        @Override // org.apache.commons.lang.text.StrMatcher
        public int isMatch(char[] buffer, int pos, int bufferStart, int bufferEnd) {
            int len = this.chars.length;
            if (pos + len > bufferEnd) {
                return 0;
            }
            int i = 0;
            while (i < this.chars.length) {
                if (this.chars[i] == buffer[pos]) {
                    i++;
                    pos++;
                } else {
                    return 0;
                }
            }
            return len;
        }
    }

    /* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/text/StrMatcher$NoMatcher.class */
    static final class NoMatcher extends StrMatcher {
        NoMatcher() {
        }

        @Override // org.apache.commons.lang.text.StrMatcher
        public int isMatch(char[] buffer, int pos, int bufferStart, int bufferEnd) {
            return 0;
        }
    }

    /* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/text/StrMatcher$TrimMatcher.class */
    static final class TrimMatcher extends StrMatcher {
        TrimMatcher() {
        }

        @Override // org.apache.commons.lang.text.StrMatcher
        public int isMatch(char[] buffer, int pos, int bufferStart, int bufferEnd) {
            return buffer[pos] <= ' ' ? 1 : 0;
        }
    }
}
