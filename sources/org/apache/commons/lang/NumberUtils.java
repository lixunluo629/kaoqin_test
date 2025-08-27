package org.apache.commons.lang;

import java.math.BigDecimal;
import java.math.BigInteger;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/NumberUtils.class */
public final class NumberUtils {
    public static int stringToInt(String str) {
        return stringToInt(str, 0);
    }

    public static int stringToInt(String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0202 A[Catch: NumberFormatException -> 0x0218, TryCatch #2 {NumberFormatException -> 0x0218, blocks: (B:85:0x01f3, B:87:0x0202), top: B:151:0x01f3 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.Number createNumber(java.lang.String r5) throws java.lang.NumberFormatException {
        /*
            Method dump skipped, instructions count: 739
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang.NumberUtils.createNumber(java.lang.String):java.lang.Number");
    }

    private static boolean isAllZeros(String s) {
        if (s == null) {
            return true;
        }
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) != '0') {
                return false;
            }
        }
        return s.length() > 0;
    }

    public static Float createFloat(String val) {
        return Float.valueOf(val);
    }

    public static Double createDouble(String val) {
        return Double.valueOf(val);
    }

    public static Integer createInteger(String val) {
        return Integer.decode(val);
    }

    public static Long createLong(String val) {
        return Long.valueOf(val);
    }

    public static BigInteger createBigInteger(String val) {
        BigInteger bi = new BigInteger(val);
        return bi;
    }

    public static BigDecimal createBigDecimal(String val) {
        BigDecimal bd = new BigDecimal(val);
        return bd;
    }

    public static long minimum(long a, long b, long c) {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            a = c;
        }
        return a;
    }

    public static int minimum(int a, int b, int c) {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            a = c;
        }
        return a;
    }

    public static long maximum(long a, long b, long c) {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            a = c;
        }
        return a;
    }

    public static int maximum(int a, int b, int c) {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            a = c;
        }
        return a;
    }

    public static int compare(double lhs, double rhs) {
        if (lhs < rhs) {
            return -1;
        }
        if (lhs > rhs) {
            return 1;
        }
        long lhsBits = Double.doubleToLongBits(lhs);
        long rhsBits = Double.doubleToLongBits(rhs);
        if (lhsBits == rhsBits) {
            return 0;
        }
        if (lhsBits < rhsBits) {
            return -1;
        }
        return 1;
    }

    public static int compare(float lhs, float rhs) {
        if (lhs < rhs) {
            return -1;
        }
        if (lhs > rhs) {
            return 1;
        }
        int lhsBits = Float.floatToIntBits(lhs);
        int rhsBits = Float.floatToIntBits(rhs);
        if (lhsBits == rhsBits) {
            return 0;
        }
        if (lhsBits < rhsBits) {
            return -1;
        }
        return 1;
    }

    public static boolean isDigits(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:102:0x0172, code lost:
    
        if (r9 != false) goto L113;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x017b, code lost:
    
        if (r0[r12] == 'd') goto L111;
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x0184, code lost:
    
        if (r0[r12] == 'D') goto L111;
     */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x018d, code lost:
    
        if (r0[r12] == 'f') goto L111;
     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x0196, code lost:
    
        if (r0[r12] != 'F') goto L113;
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x019b, code lost:
    
        return r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x01a2, code lost:
    
        if (r0[r12] == 'l') goto L117;
     */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x01ab, code lost:
    
        if (r0[r12] != 'L') goto L124;
     */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x01b0, code lost:
    
        if (r10 == false) goto L122;
     */
    /* JADX WARN: Code restructure failed: missing block: B:120:0x01b4, code lost:
    
        if (r7 != false) goto L122;
     */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x01b7, code lost:
    
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x01bb, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:124:0x01bd, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:127:0x01c1, code lost:
    
        if (r9 != false) goto L131;
     */
    /* JADX WARN: Code restructure failed: missing block: B:129:0x01c6, code lost:
    
        if (r10 == false) goto L131;
     */
    /* JADX WARN: Code restructure failed: missing block: B:130:0x01c9, code lost:
    
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:131:0x01cd, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:156:?, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x0145, code lost:
    
        if (r12 >= r0.length) goto L126;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x014e, code lost:
    
        if (r0[r12] < '0') goto L95;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x0157, code lost:
    
        if (r0[r12] > '9') goto L95;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x015a, code lost:
    
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x0162, code lost:
    
        if (r0[r12] == 'e') goto L99;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x016b, code lost:
    
        if (r0[r12] != 'E') goto L101;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x016e, code lost:
    
        return false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean isNumber(java.lang.String r4) {
        /*
            Method dump skipped, instructions count: 463
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang.NumberUtils.isNumber(java.lang.String):boolean");
    }
}
