package com.itextpdf.kernel.numbering;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/numbering/AlphabetNumbering.class */
public class AlphabetNumbering {
    public static String toAlphabetNumber(int number, char[] alphabet) {
        if (number < 1) {
            throw new IllegalArgumentException("The parameter must be a positive integer");
        }
        int cardinality = alphabet.length;
        int number2 = number - 1;
        int bytes = 1;
        int start = 0;
        int i = cardinality;
        while (true) {
            int symbols = i;
            if (number2 < symbols + start) {
                break;
            }
            bytes++;
            start += symbols;
            i = symbols * cardinality;
        }
        int c = number2 - start;
        char[] value = new char[bytes];
        while (bytes > 0) {
            bytes--;
            value[bytes] = alphabet[c % cardinality];
            c /= cardinality;
        }
        return new String(value);
    }
}
