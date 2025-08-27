package org.apache.poi.ss.util;

import java.math.BigDecimal;
import java.math.BigInteger;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/util/NormalisedDecimal.class */
final class NormalisedDecimal {
    private static final int EXPONENT_OFFSET = 14;
    private static final BigDecimal BD_2_POW_24 = new BigDecimal(BigInteger.ONE.shiftLeft(24));
    private static final int LOG_BASE_10_OF_2_TIMES_2_POW_20 = 315653;
    private static final int C_2_POW_19 = 524288;
    private static final int FRAC_HALF = 8388608;
    private static final long MAX_REP_WHOLE_PART = 1000000000000000L;
    private final int _relativeDecimalExponent;
    private final long _wholePart;
    private final int _fractionalPart;

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0072  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x008a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static org.apache.poi.ss.util.NormalisedDecimal create(java.math.BigInteger r5, int r6) {
        /*
            r0 = r6
            r1 = 49
            if (r0 > r1) goto Lc
            r0 = r6
            r1 = 46
            if (r0 >= r1) goto L22
        Lc:
            r0 = 15204352(0xe80000, float:2.1305835E-38)
            r1 = r6
            r2 = 315653(0x4d105, float:4.42324E-40)
            int r1 = r1 * r2
            int r0 = r0 - r1
            r8 = r0
            r0 = r8
            r1 = 524288(0x80000, float:7.34684E-40)
            int r0 = r0 + r1
            r8 = r0
            r0 = r8
            r1 = 20
            int r0 = r0 >> r1
            int r0 = -r0
            r7 = r0
            goto L24
        L22:
            r0 = 0
            r7 = r0
        L24:
            org.apache.poi.ss.util.MutableFPNumber r0 = new org.apache.poi.ss.util.MutableFPNumber
            r1 = r0
            r2 = r5
            r3 = r6
            r1.<init>(r2, r3)
            r8 = r0
            r0 = r7
            if (r0 == 0) goto L38
            r0 = r8
            r1 = r7
            int r1 = -r1
            r0.multiplyByPowerOfTen(r1)
        L38:
            r0 = r8
            int r0 = r0.get64BitNormalisedExponent()
            switch(r0) {
                case 44: goto L72;
                case 45: goto L72;
                case 46: goto L68;
                case 47: goto L7d;
                case 48: goto L7d;
                case 49: goto L80;
                case 50: goto L8a;
                default: goto L95;
            }
        L68:
            r0 = r8
            boolean r0 = r0.isAboveMinRep()
            if (r0 == 0) goto L72
            goto Lb8
        L72:
            r0 = r8
            r1 = 1
            r0.multiplyByPowerOfTen(r1)
            int r7 = r7 + (-1)
            goto Lb8
        L7d:
            goto Lb8
        L80:
            r0 = r8
            boolean r0 = r0.isBelowMaxRep()
            if (r0 == 0) goto L8a
            goto Lb8
        L8a:
            r0 = r8
            r1 = -1
            r0.multiplyByPowerOfTen(r1)
            int r7 = r7 + 1
            goto Lb8
        L95:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            r1 = r0
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r3 = r2
            r3.<init>()
            java.lang.String r3 = "Bad binary exp "
            java.lang.StringBuilder r2 = r2.append(r3)
            r3 = r8
            int r3 = r3.get64BitNormalisedExponent()
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = "."
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r0
        Lb8:
            r0 = r8
            r0.normalise64bit()
            r0 = r8
            r1 = r7
            org.apache.poi.ss.util.NormalisedDecimal r0 = r0.createNormalisedDecimal(r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.ss.util.NormalisedDecimal.create(java.math.BigInteger, int):org.apache.poi.ss.util.NormalisedDecimal");
    }

    public NormalisedDecimal roundUnits() {
        long wholePart = this._wholePart;
        if (this._fractionalPart >= 8388608) {
            wholePart++;
        }
        int de = this._relativeDecimalExponent;
        if (wholePart < MAX_REP_WHOLE_PART) {
            return new NormalisedDecimal(wholePart, 0, de);
        }
        return new NormalisedDecimal(wholePart / 10, 0, de + 1);
    }

    NormalisedDecimal(long wholePart, int fracPart, int decimalExponent) {
        this._wholePart = wholePart;
        this._fractionalPart = fracPart;
        this._relativeDecimalExponent = decimalExponent;
    }

    public ExpandedDouble normaliseBaseTwo() {
        MutableFPNumber cc = new MutableFPNumber(composeFrac(), 39);
        cc.multiplyByPowerOfTen(this._relativeDecimalExponent);
        cc.normalise64bit();
        return cc.createExpandedDouble();
    }

    BigInteger composeFrac() {
        long wp = this._wholePart;
        int fp = this._fractionalPart;
        return new BigInteger(new byte[]{(byte) (wp >> 56), (byte) (wp >> 48), (byte) (wp >> 40), (byte) (wp >> 32), (byte) (wp >> 24), (byte) (wp >> 16), (byte) (wp >> 8), (byte) (wp >> 0), (byte) (fp >> 16), (byte) (fp >> 8), (byte) (fp >> 0)});
    }

    public String getSignificantDecimalDigits() {
        return Long.toString(this._wholePart);
    }

    public String getSignificantDecimalDigitsLastDigitRounded() {
        long wp = this._wholePart + 5;
        StringBuilder sb = new StringBuilder(24);
        sb.append(wp);
        sb.setCharAt(sb.length() - 1, '0');
        return sb.toString();
    }

    public int getDecimalExponent() {
        return this._relativeDecimalExponent + 14;
    }

    public int compareNormalised(NormalisedDecimal other) {
        int cmp = this._relativeDecimalExponent - other._relativeDecimalExponent;
        if (cmp != 0) {
            return cmp;
        }
        if (this._wholePart > other._wholePart) {
            return 1;
        }
        if (this._wholePart < other._wholePart) {
            return -1;
        }
        return this._fractionalPart - other._fractionalPart;
    }

    public BigDecimal getFractionalPart() {
        return new BigDecimal(this._fractionalPart).divide(BD_2_POW_24);
    }

    private String getFractionalDigits() {
        if (this._fractionalPart == 0) {
            return "0";
        }
        return getFractionalPart().toString().substring(2);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append(" [");
        String ws = String.valueOf(this._wholePart);
        sb.append(ws.charAt(0));
        sb.append('.');
        sb.append(ws.substring(1));
        sb.append(' ');
        sb.append(getFractionalDigits());
        sb.append("E");
        sb.append(getDecimalExponent());
        sb.append("]");
        return sb.toString();
    }
}
