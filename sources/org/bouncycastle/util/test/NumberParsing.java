package org.bouncycastle.util.test;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/util/test/NumberParsing.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/util/test/NumberParsing.class */
public final class NumberParsing {
    private NumberParsing() {
    }

    public static long decodeLongFromHex(String str) {
        return (str.charAt(1) == 'x' || str.charAt(1) == 'X') ? Long.parseLong(str.substring(2), 16) : Long.parseLong(str, 16);
    }

    public static int decodeIntFromHex(String str) {
        return (str.charAt(1) == 'x' || str.charAt(1) == 'X') ? Integer.parseInt(str.substring(2), 16) : Integer.parseInt(str, 16);
    }
}
