package com.itextpdf.kernel.numbering;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/numbering/EnglishAlphabetNumbering.class */
public class EnglishAlphabetNumbering {
    protected static final char[] ALPHABET_LOWERCASE = new char[26];
    protected static final char[] ALPHABET_UPPERCASE = new char[26];
    protected static final int ALPHABET_LENGTH = 26;

    static {
        for (int i = 0; i < 26; i++) {
            ALPHABET_LOWERCASE[i] = (char) (97 + i);
            ALPHABET_UPPERCASE[i] = (char) (65 + i);
        }
    }

    public static String toLatinAlphabetNumberLowerCase(int number) {
        return AlphabetNumbering.toAlphabetNumber(number, ALPHABET_LOWERCASE);
    }

    public static String toLatinAlphabetNumberUpperCase(int number) {
        return AlphabetNumbering.toAlphabetNumber(number, ALPHABET_UPPERCASE);
    }

    public static String toLatinAlphabetNumber(int number, boolean upperCase) {
        return upperCase ? toLatinAlphabetNumberUpperCase(number) : toLatinAlphabetNumberLowerCase(number);
    }
}
