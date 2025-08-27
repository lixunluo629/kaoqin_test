package com.itextpdf.io.font.woff2;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/woff2/Round.class */
class Round {
    Round() {
    }

    public static int round4(int value) {
        if (Integer.MAX_VALUE - value < 3) {
            return value;
        }
        return (value + 3) & (-4);
    }

    public static long round4(long value) {
        if (Long.MAX_VALUE - value < 3) {
            return value;
        }
        return (value + 3) & (-4);
    }
}
