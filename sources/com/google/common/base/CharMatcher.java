package com.google.common.base;

import com.drew.metadata.exif.makernotes.SonyType1MakernoteDirectory;
import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.util.Arrays;
import java.util.BitSet;
import javax.annotation.CheckReturnValue;

@Beta
@GwtCompatible(emulated = true)
/* loaded from: guava-18.0.jar:com/google/common/base/CharMatcher.class */
public abstract class CharMatcher implements Predicate<Character> {
    public static final CharMatcher BREAKING_WHITESPACE = new CharMatcher() { // from class: com.google.common.base.CharMatcher.1
        @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
        public /* bridge */ /* synthetic */ boolean apply(Character ch2) {
            return super.apply(ch2);
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char c) {
            switch (c) {
                case '\t':
                case '\n':
                case 11:
                case '\f':
                case '\r':
                case ' ':
                case 133:
                case 5760:
                case 8232:
                case 8233:
                case 8287:
                case 12288:
                    return true;
                case SonyType1MakernoteDirectory.TAG_BRIGHTNESS /* 8199 */:
                    return false;
                default:
                    return c >= 8192 && c <= 8202;
            }
        }

        @Override // com.google.common.base.CharMatcher
        public String toString() {
            return "CharMatcher.BREAKING_WHITESPACE";
        }
    };
    public static final CharMatcher ASCII = inRange(0, 127, "CharMatcher.ASCII");
    private static final String ZEROES = "0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０";
    private static final String NINES;
    public static final CharMatcher DIGIT;
    public static final CharMatcher JAVA_DIGIT;
    public static final CharMatcher JAVA_LETTER;
    public static final CharMatcher JAVA_LETTER_OR_DIGIT;
    public static final CharMatcher JAVA_UPPER_CASE;
    public static final CharMatcher JAVA_LOWER_CASE;
    public static final CharMatcher JAVA_ISO_CONTROL;
    public static final CharMatcher INVISIBLE;
    public static final CharMatcher SINGLE_WIDTH;
    public static final CharMatcher ANY;
    public static final CharMatcher NONE;
    final String description;
    private static final int DISTINCT_CHARS = 65536;
    static final String WHITESPACE_TABLE = "\u2002\u3000\r\u0085\u200a\u2005\u2000\u3000\u2029\u000b\u3000\u2008\u2003\u205f\u3000\u1680\t \u2006\u2001  \f\u2009\u3000\u2004\u3000\u3000\u2028\n \u3000";
    static final int WHITESPACE_MULTIPLIER = 1682554634;
    static final int WHITESPACE_SHIFT;
    public static final CharMatcher WHITESPACE;

    public abstract boolean matches(char c);

    static {
        StringBuilder builder = new StringBuilder(ZEROES.length());
        for (int i = 0; i < ZEROES.length(); i++) {
            builder.append((char) (ZEROES.charAt(i) + '\t'));
        }
        NINES = builder.toString();
        DIGIT = new RangesMatcher("CharMatcher.DIGIT", ZEROES.toCharArray(), NINES.toCharArray());
        JAVA_DIGIT = new CharMatcher("CharMatcher.JAVA_DIGIT") { // from class: com.google.common.base.CharMatcher.2
            @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
            public /* bridge */ /* synthetic */ boolean apply(Character ch2) {
                return super.apply(ch2);
            }

            @Override // com.google.common.base.CharMatcher
            public boolean matches(char c) {
                return Character.isDigit(c);
            }
        };
        JAVA_LETTER = new CharMatcher("CharMatcher.JAVA_LETTER") { // from class: com.google.common.base.CharMatcher.3
            @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
            public /* bridge */ /* synthetic */ boolean apply(Character ch2) {
                return super.apply(ch2);
            }

            @Override // com.google.common.base.CharMatcher
            public boolean matches(char c) {
                return Character.isLetter(c);
            }
        };
        JAVA_LETTER_OR_DIGIT = new CharMatcher("CharMatcher.JAVA_LETTER_OR_DIGIT") { // from class: com.google.common.base.CharMatcher.4
            @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
            public /* bridge */ /* synthetic */ boolean apply(Character ch2) {
                return super.apply(ch2);
            }

            @Override // com.google.common.base.CharMatcher
            public boolean matches(char c) {
                return Character.isLetterOrDigit(c);
            }
        };
        JAVA_UPPER_CASE = new CharMatcher("CharMatcher.JAVA_UPPER_CASE") { // from class: com.google.common.base.CharMatcher.5
            @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
            public /* bridge */ /* synthetic */ boolean apply(Character ch2) {
                return super.apply(ch2);
            }

            @Override // com.google.common.base.CharMatcher
            public boolean matches(char c) {
                return Character.isUpperCase(c);
            }
        };
        JAVA_LOWER_CASE = new CharMatcher("CharMatcher.JAVA_LOWER_CASE") { // from class: com.google.common.base.CharMatcher.6
            @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
            public /* bridge */ /* synthetic */ boolean apply(Character ch2) {
                return super.apply(ch2);
            }

            @Override // com.google.common.base.CharMatcher
            public boolean matches(char c) {
                return Character.isLowerCase(c);
            }
        };
        JAVA_ISO_CONTROL = inRange((char) 0, (char) 31).or(inRange((char) 127, (char) 159)).withToString("CharMatcher.JAVA_ISO_CONTROL");
        INVISIBLE = new RangesMatcher("CharMatcher.INVISIBLE", "��\u007f\u00ad\u0600\u061c\u06dd\u070f\u1680\u180e\u2000\u2028\u205f\u2066\u2067\u2068\u2069\u206a\u3000�\ufeff\ufff9\ufffa".toCharArray(), "  \u00ad\u0604\u061c\u06dd\u070f\u1680\u180e\u200f \u2064\u2066\u2067\u2068\u2069\u206f\u3000\uf8ff\ufeff\ufff9\ufffb".toCharArray());
        SINGLE_WIDTH = new RangesMatcher("CharMatcher.SINGLE_WIDTH", "��־א׳\u0600ݐ\u0e00Ḁ℀ﭐﹰ｡".toCharArray(), "ӹ־ת״ۿݿ\u0e7f₯℺﷿\ufeffￜ".toCharArray());
        ANY = new FastMatcher("CharMatcher.ANY") { // from class: com.google.common.base.CharMatcher.7
            @Override // com.google.common.base.CharMatcher
            public boolean matches(char c) {
                return true;
            }

            @Override // com.google.common.base.CharMatcher
            public int indexIn(CharSequence sequence) {
                return sequence.length() == 0 ? -1 : 0;
            }

            @Override // com.google.common.base.CharMatcher
            public int indexIn(CharSequence sequence, int start) {
                int length = sequence.length();
                Preconditions.checkPositionIndex(start, length);
                if (start == length) {
                    return -1;
                }
                return start;
            }

            @Override // com.google.common.base.CharMatcher
            public int lastIndexIn(CharSequence sequence) {
                return sequence.length() - 1;
            }

            @Override // com.google.common.base.CharMatcher
            public boolean matchesAllOf(CharSequence sequence) {
                Preconditions.checkNotNull(sequence);
                return true;
            }

            @Override // com.google.common.base.CharMatcher
            public boolean matchesNoneOf(CharSequence sequence) {
                return sequence.length() == 0;
            }

            @Override // com.google.common.base.CharMatcher
            public String removeFrom(CharSequence sequence) {
                Preconditions.checkNotNull(sequence);
                return "";
            }

            @Override // com.google.common.base.CharMatcher
            public String replaceFrom(CharSequence sequence, char replacement) {
                char[] array = new char[sequence.length()];
                Arrays.fill(array, replacement);
                return new String(array);
            }

            @Override // com.google.common.base.CharMatcher
            public String replaceFrom(CharSequence sequence, CharSequence replacement) {
                StringBuilder retval = new StringBuilder(sequence.length() * replacement.length());
                for (int i2 = 0; i2 < sequence.length(); i2++) {
                    retval.append(replacement);
                }
                return retval.toString();
            }

            @Override // com.google.common.base.CharMatcher
            public String collapseFrom(CharSequence sequence, char replacement) {
                return sequence.length() == 0 ? "" : String.valueOf(replacement);
            }

            @Override // com.google.common.base.CharMatcher
            public String trimFrom(CharSequence sequence) {
                Preconditions.checkNotNull(sequence);
                return "";
            }

            @Override // com.google.common.base.CharMatcher
            public int countIn(CharSequence sequence) {
                return sequence.length();
            }

            @Override // com.google.common.base.CharMatcher
            public CharMatcher and(CharMatcher other) {
                return (CharMatcher) Preconditions.checkNotNull(other);
            }

            @Override // com.google.common.base.CharMatcher
            public CharMatcher or(CharMatcher other) {
                Preconditions.checkNotNull(other);
                return this;
            }

            @Override // com.google.common.base.CharMatcher.FastMatcher, com.google.common.base.CharMatcher
            public CharMatcher negate() {
                return NONE;
            }
        };
        NONE = new FastMatcher("CharMatcher.NONE") { // from class: com.google.common.base.CharMatcher.8
            @Override // com.google.common.base.CharMatcher
            public boolean matches(char c) {
                return false;
            }

            @Override // com.google.common.base.CharMatcher
            public int indexIn(CharSequence sequence) {
                Preconditions.checkNotNull(sequence);
                return -1;
            }

            @Override // com.google.common.base.CharMatcher
            public int indexIn(CharSequence sequence, int start) {
                int length = sequence.length();
                Preconditions.checkPositionIndex(start, length);
                return -1;
            }

            @Override // com.google.common.base.CharMatcher
            public int lastIndexIn(CharSequence sequence) {
                Preconditions.checkNotNull(sequence);
                return -1;
            }

            @Override // com.google.common.base.CharMatcher
            public boolean matchesAllOf(CharSequence sequence) {
                return sequence.length() == 0;
            }

            @Override // com.google.common.base.CharMatcher
            public boolean matchesNoneOf(CharSequence sequence) {
                Preconditions.checkNotNull(sequence);
                return true;
            }

            @Override // com.google.common.base.CharMatcher
            public String removeFrom(CharSequence sequence) {
                return sequence.toString();
            }

            @Override // com.google.common.base.CharMatcher
            public String replaceFrom(CharSequence sequence, char replacement) {
                return sequence.toString();
            }

            @Override // com.google.common.base.CharMatcher
            public String replaceFrom(CharSequence sequence, CharSequence replacement) {
                Preconditions.checkNotNull(replacement);
                return sequence.toString();
            }

            @Override // com.google.common.base.CharMatcher
            public String collapseFrom(CharSequence sequence, char replacement) {
                return sequence.toString();
            }

            @Override // com.google.common.base.CharMatcher
            public String trimFrom(CharSequence sequence) {
                return sequence.toString();
            }

            @Override // com.google.common.base.CharMatcher
            public String trimLeadingFrom(CharSequence sequence) {
                return sequence.toString();
            }

            @Override // com.google.common.base.CharMatcher
            public String trimTrailingFrom(CharSequence sequence) {
                return sequence.toString();
            }

            @Override // com.google.common.base.CharMatcher
            public int countIn(CharSequence sequence) {
                Preconditions.checkNotNull(sequence);
                return 0;
            }

            @Override // com.google.common.base.CharMatcher
            public CharMatcher and(CharMatcher other) {
                Preconditions.checkNotNull(other);
                return this;
            }

            @Override // com.google.common.base.CharMatcher
            public CharMatcher or(CharMatcher other) {
                return (CharMatcher) Preconditions.checkNotNull(other);
            }

            @Override // com.google.common.base.CharMatcher.FastMatcher, com.google.common.base.CharMatcher
            public CharMatcher negate() {
                return ANY;
            }
        };
        WHITESPACE_SHIFT = Integer.numberOfLeadingZeros(WHITESPACE_TABLE.length() - 1);
        WHITESPACE = new FastMatcher("WHITESPACE") { // from class: com.google.common.base.CharMatcher.15
            @Override // com.google.common.base.CharMatcher
            public boolean matches(char c) {
                return CharMatcher.WHITESPACE_TABLE.charAt((CharMatcher.WHITESPACE_MULTIPLIER * c) >>> WHITESPACE_SHIFT) == c;
            }

            @Override // com.google.common.base.CharMatcher
            @GwtIncompatible("java.util.BitSet")
            void setBits(BitSet table) {
                for (int i2 = 0; i2 < CharMatcher.WHITESPACE_TABLE.length(); i2++) {
                    table.set(CharMatcher.WHITESPACE_TABLE.charAt(i2));
                }
            }
        };
    }

    /* loaded from: guava-18.0.jar:com/google/common/base/CharMatcher$RangesMatcher.class */
    private static class RangesMatcher extends CharMatcher {
        private final char[] rangeStarts;
        private final char[] rangeEnds;

        @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
        public /* bridge */ /* synthetic */ boolean apply(Character ch2) {
            return super.apply(ch2);
        }

        RangesMatcher(String description, char[] rangeStarts, char[] rangeEnds) {
            super(description);
            this.rangeStarts = rangeStarts;
            this.rangeEnds = rangeEnds;
            Preconditions.checkArgument(rangeStarts.length == rangeEnds.length);
            for (int i = 0; i < rangeStarts.length; i++) {
                Preconditions.checkArgument(rangeStarts[i] <= rangeEnds[i]);
                if (i + 1 < rangeStarts.length) {
                    Preconditions.checkArgument(rangeEnds[i] < rangeStarts[i + 1]);
                }
            }
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char c) {
            int index = Arrays.binarySearch(this.rangeStarts, c);
            if (index >= 0) {
                return true;
            }
            int index2 = (index ^ (-1)) - 1;
            return index2 >= 0 && c <= this.rangeEnds[index2];
        }
    }

    private static String showCharacter(char c) {
        char[] tmp = new char[6];
        tmp[0] = '\\';
        tmp[1] = 'u';
        tmp[2] = 0;
        tmp[3] = 0;
        tmp[4] = 0;
        tmp[5] = 0;
        for (int i = 0; i < 4; i++) {
            tmp[5 - i] = "0123456789ABCDEF".charAt(c & 15);
            c = (char) (c >> 4);
        }
        return String.copyValueOf(tmp);
    }

    public static CharMatcher is(final char match) {
        String strValueOf = String.valueOf(String.valueOf(showCharacter(match)));
        String description = new StringBuilder(18 + strValueOf.length()).append("CharMatcher.is('").append(strValueOf).append("')").toString();
        return new FastMatcher(description) { // from class: com.google.common.base.CharMatcher.9
            @Override // com.google.common.base.CharMatcher
            public boolean matches(char c) {
                return c == match;
            }

            @Override // com.google.common.base.CharMatcher
            public String replaceFrom(CharSequence sequence, char replacement) {
                return sequence.toString().replace(match, replacement);
            }

            @Override // com.google.common.base.CharMatcher
            public CharMatcher and(CharMatcher other) {
                return other.matches(match) ? this : NONE;
            }

            @Override // com.google.common.base.CharMatcher
            public CharMatcher or(CharMatcher other) {
                return other.matches(match) ? other : super.or(other);
            }

            @Override // com.google.common.base.CharMatcher.FastMatcher, com.google.common.base.CharMatcher
            public CharMatcher negate() {
                return isNot(match);
            }

            @Override // com.google.common.base.CharMatcher
            @GwtIncompatible("java.util.BitSet")
            void setBits(BitSet table) {
                table.set(match);
            }
        };
    }

    public static CharMatcher isNot(final char match) {
        String strValueOf = String.valueOf(String.valueOf(showCharacter(match)));
        String description = new StringBuilder(21 + strValueOf.length()).append("CharMatcher.isNot('").append(strValueOf).append("')").toString();
        return new FastMatcher(description) { // from class: com.google.common.base.CharMatcher.10
            @Override // com.google.common.base.CharMatcher
            public boolean matches(char c) {
                return c != match;
            }

            @Override // com.google.common.base.CharMatcher
            public CharMatcher and(CharMatcher other) {
                return other.matches(match) ? super.and(other) : other;
            }

            @Override // com.google.common.base.CharMatcher
            public CharMatcher or(CharMatcher other) {
                return other.matches(match) ? ANY : this;
            }

            @Override // com.google.common.base.CharMatcher
            @GwtIncompatible("java.util.BitSet")
            void setBits(BitSet table) {
                table.set(0, match);
                table.set(match + 1, 65536);
            }

            @Override // com.google.common.base.CharMatcher.FastMatcher, com.google.common.base.CharMatcher
            public CharMatcher negate() {
                return is(match);
            }
        };
    }

    public static CharMatcher anyOf(CharSequence sequence) {
        switch (sequence.length()) {
            case 0:
                return NONE;
            case 1:
                return is(sequence.charAt(0));
            case 2:
                return isEither(sequence.charAt(0), sequence.charAt(1));
            default:
                final char[] chars = sequence.toString().toCharArray();
                Arrays.sort(chars);
                StringBuilder description = new StringBuilder("CharMatcher.anyOf(\"");
                for (char c : chars) {
                    description.append(showCharacter(c));
                }
                description.append("\")");
                return new CharMatcher(description.toString()) { // from class: com.google.common.base.CharMatcher.11
                    @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
                    public /* bridge */ /* synthetic */ boolean apply(Character ch2) {
                        return super.apply(ch2);
                    }

                    @Override // com.google.common.base.CharMatcher
                    public boolean matches(char c2) {
                        return Arrays.binarySearch(chars, c2) >= 0;
                    }

                    @Override // com.google.common.base.CharMatcher
                    @GwtIncompatible("java.util.BitSet")
                    void setBits(BitSet table) {
                        char[] arr$ = chars;
                        for (char c2 : arr$) {
                            table.set(c2);
                        }
                    }
                };
        }
    }

    private static CharMatcher isEither(final char match1, final char match2) {
        String strValueOf = String.valueOf(String.valueOf(showCharacter(match1)));
        String strValueOf2 = String.valueOf(String.valueOf(showCharacter(match2)));
        String description = new StringBuilder(21 + strValueOf.length() + strValueOf2.length()).append("CharMatcher.anyOf(\"").append(strValueOf).append(strValueOf2).append("\")").toString();
        return new FastMatcher(description) { // from class: com.google.common.base.CharMatcher.12
            @Override // com.google.common.base.CharMatcher
            public boolean matches(char c) {
                return c == match1 || c == match2;
            }

            @Override // com.google.common.base.CharMatcher
            @GwtIncompatible("java.util.BitSet")
            void setBits(BitSet table) {
                table.set(match1);
                table.set(match2);
            }
        };
    }

    public static CharMatcher noneOf(CharSequence sequence) {
        return anyOf(sequence).negate();
    }

    public static CharMatcher inRange(char startInclusive, char endInclusive) {
        Preconditions.checkArgument(endInclusive >= startInclusive);
        String strValueOf = String.valueOf(String.valueOf(showCharacter(startInclusive)));
        String strValueOf2 = String.valueOf(String.valueOf(showCharacter(endInclusive)));
        String description = new StringBuilder(27 + strValueOf.length() + strValueOf2.length()).append("CharMatcher.inRange('").append(strValueOf).append("', '").append(strValueOf2).append("')").toString();
        return inRange(startInclusive, endInclusive, description);
    }

    static CharMatcher inRange(final char startInclusive, final char endInclusive, String description) {
        return new FastMatcher(description) { // from class: com.google.common.base.CharMatcher.13
            @Override // com.google.common.base.CharMatcher
            public boolean matches(char c) {
                return startInclusive <= c && c <= endInclusive;
            }

            @Override // com.google.common.base.CharMatcher
            @GwtIncompatible("java.util.BitSet")
            void setBits(BitSet table) {
                table.set(startInclusive, endInclusive + 1);
            }
        };
    }

    public static CharMatcher forPredicate(final Predicate<? super Character> predicate) {
        Preconditions.checkNotNull(predicate);
        if (predicate instanceof CharMatcher) {
            return (CharMatcher) predicate;
        }
        String strValueOf = String.valueOf(String.valueOf(predicate));
        String description = new StringBuilder(26 + strValueOf.length()).append("CharMatcher.forPredicate(").append(strValueOf).append(")").toString();
        return new CharMatcher(description) { // from class: com.google.common.base.CharMatcher.14
            @Override // com.google.common.base.CharMatcher
            public boolean matches(char c) {
                return predicate.apply(Character.valueOf(c));
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
            public boolean apply(Character character) {
                return predicate.apply(Preconditions.checkNotNull(character));
            }
        };
    }

    CharMatcher(String description) {
        this.description = description;
    }

    protected CharMatcher() {
        this.description = super.toString();
    }

    public CharMatcher negate() {
        return new NegatedMatcher(this);
    }

    /* loaded from: guava-18.0.jar:com/google/common/base/CharMatcher$NegatedMatcher.class */
    private static class NegatedMatcher extends CharMatcher {
        final CharMatcher original;

        @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
        public /* bridge */ /* synthetic */ boolean apply(Character ch2) {
            return super.apply(ch2);
        }

        NegatedMatcher(String toString, CharMatcher original) {
            super(toString);
            this.original = original;
        }

        /* JADX WARN: Illegal instructions before constructor call */
        NegatedMatcher(CharMatcher original) {
            String strValueOf = String.valueOf(String.valueOf(original));
            this(new StringBuilder(9 + strValueOf.length()).append(strValueOf).append(".negate()").toString(), original);
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char c) {
            return !this.original.matches(c);
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matchesAllOf(CharSequence sequence) {
            return this.original.matchesNoneOf(sequence);
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matchesNoneOf(CharSequence sequence) {
            return this.original.matchesAllOf(sequence);
        }

        @Override // com.google.common.base.CharMatcher
        public int countIn(CharSequence sequence) {
            return sequence.length() - this.original.countIn(sequence);
        }

        @Override // com.google.common.base.CharMatcher
        @GwtIncompatible("java.util.BitSet")
        void setBits(BitSet table) {
            BitSet tmp = new BitSet();
            this.original.setBits(tmp);
            tmp.flip(0, 65536);
            table.or(tmp);
        }

        @Override // com.google.common.base.CharMatcher
        public CharMatcher negate() {
            return this.original;
        }

        @Override // com.google.common.base.CharMatcher
        CharMatcher withToString(String description) {
            return new NegatedMatcher(description, this.original);
        }
    }

    public CharMatcher and(CharMatcher other) {
        return new And(this, (CharMatcher) Preconditions.checkNotNull(other));
    }

    /* loaded from: guava-18.0.jar:com/google/common/base/CharMatcher$And.class */
    private static class And extends CharMatcher {
        final CharMatcher first;
        final CharMatcher second;

        @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
        public /* bridge */ /* synthetic */ boolean apply(Character ch2) {
            return super.apply(ch2);
        }

        /* JADX WARN: Illegal instructions before constructor call */
        And(CharMatcher a, CharMatcher b) {
            String strValueOf = String.valueOf(String.valueOf(a));
            String strValueOf2 = String.valueOf(String.valueOf(b));
            this(a, b, new StringBuilder(19 + strValueOf.length() + strValueOf2.length()).append("CharMatcher.and(").append(strValueOf).append(", ").append(strValueOf2).append(")").toString());
        }

        And(CharMatcher a, CharMatcher b, String description) {
            super(description);
            this.first = (CharMatcher) Preconditions.checkNotNull(a);
            this.second = (CharMatcher) Preconditions.checkNotNull(b);
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char c) {
            return this.first.matches(c) && this.second.matches(c);
        }

        @Override // com.google.common.base.CharMatcher
        @GwtIncompatible("java.util.BitSet")
        void setBits(BitSet table) {
            BitSet tmp1 = new BitSet();
            this.first.setBits(tmp1);
            BitSet tmp2 = new BitSet();
            this.second.setBits(tmp2);
            tmp1.and(tmp2);
            table.or(tmp1);
        }

        @Override // com.google.common.base.CharMatcher
        CharMatcher withToString(String description) {
            return new And(this.first, this.second, description);
        }
    }

    public CharMatcher or(CharMatcher other) {
        return new Or(this, (CharMatcher) Preconditions.checkNotNull(other));
    }

    /* loaded from: guava-18.0.jar:com/google/common/base/CharMatcher$Or.class */
    private static class Or extends CharMatcher {
        final CharMatcher first;
        final CharMatcher second;

        @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
        public /* bridge */ /* synthetic */ boolean apply(Character ch2) {
            return super.apply(ch2);
        }

        Or(CharMatcher a, CharMatcher b, String description) {
            super(description);
            this.first = (CharMatcher) Preconditions.checkNotNull(a);
            this.second = (CharMatcher) Preconditions.checkNotNull(b);
        }

        /* JADX WARN: Illegal instructions before constructor call */
        Or(CharMatcher a, CharMatcher b) {
            String strValueOf = String.valueOf(String.valueOf(a));
            String strValueOf2 = String.valueOf(String.valueOf(b));
            this(a, b, new StringBuilder(18 + strValueOf.length() + strValueOf2.length()).append("CharMatcher.or(").append(strValueOf).append(", ").append(strValueOf2).append(")").toString());
        }

        @Override // com.google.common.base.CharMatcher
        @GwtIncompatible("java.util.BitSet")
        void setBits(BitSet table) {
            this.first.setBits(table);
            this.second.setBits(table);
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char c) {
            return this.first.matches(c) || this.second.matches(c);
        }

        @Override // com.google.common.base.CharMatcher
        CharMatcher withToString(String description) {
            return new Or(this.first, this.second, description);
        }
    }

    public CharMatcher precomputed() {
        return Platform.precomputeCharMatcher(this);
    }

    CharMatcher withToString(String description) {
        throw new UnsupportedOperationException();
    }

    @GwtIncompatible("java.util.BitSet")
    CharMatcher precomputedInternal() {
        String strConcat;
        BitSet table = new BitSet();
        setBits(table);
        int totalCharacters = table.cardinality();
        if (totalCharacters * 2 <= 65536) {
            return precomputedPositive(totalCharacters, table, this.description);
        }
        table.flip(0, 65536);
        int negatedCharacters = 65536 - totalCharacters;
        if (this.description.endsWith(".negate()")) {
            strConcat = this.description.substring(0, this.description.length() - ".negate()".length());
        } else {
            String strValueOf = String.valueOf(this.description);
            String strValueOf2 = String.valueOf(".negate()");
            if (strValueOf2.length() != 0) {
                strConcat = strValueOf.concat(strValueOf2);
            } else {
                strConcat = str;
                String str = new String(strValueOf);
            }
        }
        String negatedDescription = strConcat;
        return new NegatedFastMatcher(toString(), precomputedPositive(negatedCharacters, table, negatedDescription));
    }

    /* loaded from: guava-18.0.jar:com/google/common/base/CharMatcher$FastMatcher.class */
    static abstract class FastMatcher extends CharMatcher {
        @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
        public /* bridge */ /* synthetic */ boolean apply(Character ch2) {
            return super.apply(ch2);
        }

        FastMatcher() {
        }

        FastMatcher(String description) {
            super(description);
        }

        @Override // com.google.common.base.CharMatcher
        public final CharMatcher precomputed() {
            return this;
        }

        @Override // com.google.common.base.CharMatcher
        public CharMatcher negate() {
            return new NegatedFastMatcher(this);
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/base/CharMatcher$NegatedFastMatcher.class */
    static final class NegatedFastMatcher extends NegatedMatcher {
        NegatedFastMatcher(CharMatcher original) {
            super(original);
        }

        NegatedFastMatcher(String toString, CharMatcher original) {
            super(toString, original);
        }

        @Override // com.google.common.base.CharMatcher
        public final CharMatcher precomputed() {
            return this;
        }

        @Override // com.google.common.base.CharMatcher.NegatedMatcher, com.google.common.base.CharMatcher
        CharMatcher withToString(String description) {
            return new NegatedFastMatcher(description, this.original);
        }
    }

    @GwtIncompatible("java.util.BitSet")
    private static CharMatcher precomputedPositive(int totalCharacters, BitSet table, String description) {
        switch (totalCharacters) {
            case 0:
                return NONE;
            case 1:
                return is((char) table.nextSetBit(0));
            case 2:
                char c1 = (char) table.nextSetBit(0);
                char c2 = (char) table.nextSetBit(c1 + 1);
                return isEither(c1, c2);
            default:
                return isSmall(totalCharacters, table.length()) ? SmallCharMatcher.from(table, description) : new BitSetMatcher(table, description);
        }
    }

    @GwtIncompatible("SmallCharMatcher")
    private static boolean isSmall(int totalCharacters, int tableLength) {
        return totalCharacters <= 1023 && tableLength > (totalCharacters * 4) * 16;
    }

    @GwtIncompatible("java.util.BitSet")
    /* loaded from: guava-18.0.jar:com/google/common/base/CharMatcher$BitSetMatcher.class */
    private static class BitSetMatcher extends FastMatcher {
        private final BitSet table;

        private BitSetMatcher(BitSet table, String description) {
            super(description);
            this.table = table.length() + 64 < table.size() ? (BitSet) table.clone() : table;
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char c) {
            return this.table.get(c);
        }

        @Override // com.google.common.base.CharMatcher
        void setBits(BitSet bitSet) {
            bitSet.or(this.table);
        }
    }

    @GwtIncompatible("java.util.BitSet")
    void setBits(BitSet table) {
        for (int c = 65535; c >= 0; c--) {
            if (matches((char) c)) {
                table.set(c);
            }
        }
    }

    public boolean matchesAnyOf(CharSequence sequence) {
        return !matchesNoneOf(sequence);
    }

    public boolean matchesAllOf(CharSequence sequence) {
        for (int i = sequence.length() - 1; i >= 0; i--) {
            if (!matches(sequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean matchesNoneOf(CharSequence sequence) {
        return indexIn(sequence) == -1;
    }

    public int indexIn(CharSequence sequence) {
        int length = sequence.length();
        for (int i = 0; i < length; i++) {
            if (matches(sequence.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    public int indexIn(CharSequence sequence, int start) {
        int length = sequence.length();
        Preconditions.checkPositionIndex(start, length);
        for (int i = start; i < length; i++) {
            if (matches(sequence.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexIn(CharSequence sequence) {
        for (int i = sequence.length() - 1; i >= 0; i--) {
            if (matches(sequence.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    public int countIn(CharSequence sequence) {
        int count = 0;
        for (int i = 0; i < sequence.length(); i++) {
            if (matches(sequence.charAt(i))) {
                count++;
            }
        }
        return count;
    }

    @CheckReturnValue
    public String removeFrom(CharSequence sequence) {
        String string = sequence.toString();
        int pos = indexIn(string);
        if (pos == -1) {
            return string;
        }
        char[] chars = string.toCharArray();
        int spread = 1;
        while (true) {
            while (true) {
                pos++;
                if (pos != chars.length) {
                    if (matches(chars[pos])) {
                        break;
                    }
                    chars[pos - spread] = chars[pos];
                } else {
                    return new String(chars, 0, pos - spread);
                }
            }
            spread++;
        }
    }

    @CheckReturnValue
    public String retainFrom(CharSequence sequence) {
        return negate().removeFrom(sequence);
    }

    @CheckReturnValue
    public String replaceFrom(CharSequence sequence, char replacement) {
        String string = sequence.toString();
        int pos = indexIn(string);
        if (pos == -1) {
            return string;
        }
        char[] chars = string.toCharArray();
        chars[pos] = replacement;
        for (int i = pos + 1; i < chars.length; i++) {
            if (matches(chars[i])) {
                chars[i] = replacement;
            }
        }
        return new String(chars);
    }

    @CheckReturnValue
    public String replaceFrom(CharSequence sequence, CharSequence replacement) {
        int replacementLen = replacement.length();
        if (replacementLen == 0) {
            return removeFrom(sequence);
        }
        if (replacementLen == 1) {
            return replaceFrom(sequence, replacement.charAt(0));
        }
        String string = sequence.toString();
        int pos = indexIn(string);
        if (pos == -1) {
            return string;
        }
        int len = string.length();
        StringBuilder buf = new StringBuilder(((len * 3) / 2) + 16);
        int oldpos = 0;
        do {
            buf.append((CharSequence) string, oldpos, pos);
            buf.append(replacement);
            oldpos = pos + 1;
            pos = indexIn(string, oldpos);
        } while (pos != -1);
        buf.append((CharSequence) string, oldpos, len);
        return buf.toString();
    }

    @CheckReturnValue
    public String trimFrom(CharSequence sequence) {
        int len = sequence.length();
        int first = 0;
        while (first < len && matches(sequence.charAt(first))) {
            first++;
        }
        int last = len - 1;
        while (last > first && matches(sequence.charAt(last))) {
            last--;
        }
        return sequence.subSequence(first, last + 1).toString();
    }

    @CheckReturnValue
    public String trimLeadingFrom(CharSequence sequence) {
        int len = sequence.length();
        for (int first = 0; first < len; first++) {
            if (!matches(sequence.charAt(first))) {
                return sequence.subSequence(first, len).toString();
            }
        }
        return "";
    }

    @CheckReturnValue
    public String trimTrailingFrom(CharSequence sequence) {
        int len = sequence.length();
        for (int last = len - 1; last >= 0; last--) {
            if (!matches(sequence.charAt(last))) {
                return sequence.subSequence(0, last + 1).toString();
            }
        }
        return "";
    }

    @CheckReturnValue
    public String collapseFrom(CharSequence sequence, char replacement) {
        int len = sequence.length();
        int i = 0;
        while (i < len) {
            char c = sequence.charAt(i);
            if (matches(c)) {
                if (c == replacement && (i == len - 1 || !matches(sequence.charAt(i + 1)))) {
                    i++;
                } else {
                    StringBuilder builder = new StringBuilder(len).append(sequence.subSequence(0, i)).append(replacement);
                    return finishCollapseFrom(sequence, i + 1, len, replacement, builder, true);
                }
            }
            i++;
        }
        return sequence.toString();
    }

    @CheckReturnValue
    public String trimAndCollapseFrom(CharSequence sequence, char replacement) {
        int len = sequence.length();
        int first = 0;
        while (first < len && matches(sequence.charAt(first))) {
            first++;
        }
        int last = len - 1;
        while (last > first && matches(sequence.charAt(last))) {
            last--;
        }
        return (first == 0 && last == len - 1) ? collapseFrom(sequence, replacement) : finishCollapseFrom(sequence, first, last + 1, replacement, new StringBuilder((last + 1) - first), false);
    }

    private String finishCollapseFrom(CharSequence sequence, int start, int end, char replacement, StringBuilder builder, boolean inMatchingGroup) {
        for (int i = start; i < end; i++) {
            char c = sequence.charAt(i);
            if (matches(c)) {
                if (!inMatchingGroup) {
                    builder.append(replacement);
                    inMatchingGroup = true;
                }
            } else {
                builder.append(c);
                inMatchingGroup = false;
            }
        }
        return builder.toString();
    }

    @Override // com.google.common.base.Predicate
    @Deprecated
    public boolean apply(Character character) {
        return matches(character.charValue());
    }

    public String toString() {
        return this.description;
    }
}
