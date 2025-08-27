package org.bouncycastle.math.raw;

import java.util.Random;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/raw/Mod.class */
public abstract class Mod {
    public static int inverse32(int i) {
        int i2 = i * (2 - (i * i));
        int i3 = i2 * (2 - (i * i2));
        int i4 = i3 * (2 - (i * i3));
        return i4 * (2 - (i * i4));
    }

    public static void invert(int[] iArr, int[] iArr2, int[] iArr3) {
        int length = iArr.length;
        if (Nat.isZero(length, iArr2)) {
            throw new IllegalArgumentException("'x' cannot be 0");
        }
        if (Nat.isOne(length, iArr2)) {
            System.arraycopy(iArr2, 0, iArr3, 0, length);
            return;
        }
        int[] iArrCopy = Nat.copy(length, iArr2);
        int[] iArrCreate = Nat.create(length);
        iArrCreate[0] = 1;
        int iInversionStep = 0;
        if ((iArrCopy[0] & 1) == 0) {
            iInversionStep = inversionStep(iArr, iArrCopy, length, iArrCreate, 0);
        }
        if (Nat.isOne(length, iArrCopy)) {
            inversionResult(iArr, iInversionStep, iArrCreate, iArr3);
            return;
        }
        int[] iArrCopy2 = Nat.copy(length, iArr);
        int[] iArrCreate2 = Nat.create(length);
        int iInversionStep2 = 0;
        int i = length;
        while (true) {
            if (iArrCopy[i - 1] == 0 && iArrCopy2[i - 1] == 0) {
                i--;
            } else if (Nat.gte(i, iArrCopy, iArrCopy2)) {
                Nat.subFrom(i, iArrCopy2, iArrCopy);
                iInversionStep = inversionStep(iArr, iArrCopy, i, iArrCreate, iInversionStep + (Nat.subFrom(length, iArrCreate2, iArrCreate) - iInversionStep2));
                if (Nat.isOne(i, iArrCopy)) {
                    inversionResult(iArr, iInversionStep, iArrCreate, iArr3);
                    return;
                }
            } else {
                Nat.subFrom(i, iArrCopy, iArrCopy2);
                iInversionStep2 = inversionStep(iArr, iArrCopy2, i, iArrCreate2, iInversionStep2 + (Nat.subFrom(length, iArrCreate, iArrCreate2) - iInversionStep));
                if (Nat.isOne(i, iArrCopy2)) {
                    inversionResult(iArr, iInversionStep2, iArrCreate2, iArr3);
                    return;
                }
            }
        }
    }

    public static int[] random(int[] iArr) {
        int length = iArr.length;
        Random random = new Random();
        int[] iArrCreate = Nat.create(length);
        int i = iArr[length - 1];
        int i2 = i | (i >>> 1);
        int i3 = i2 | (i2 >>> 2);
        int i4 = i3 | (i3 >>> 4);
        int i5 = i4 | (i4 >>> 8);
        int i6 = i5 | (i5 >>> 16);
        do {
            for (int i7 = 0; i7 != length; i7++) {
                iArrCreate[i7] = random.nextInt();
            }
            int i8 = length - 1;
            iArrCreate[i8] = iArrCreate[i8] & i6;
        } while (Nat.gte(length, iArrCreate, iArr));
        return iArrCreate;
    }

    public static void add(int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4) {
        int length = iArr.length;
        if (Nat.add(length, iArr2, iArr3, iArr4) != 0) {
            Nat.subFrom(length, iArr, iArr4);
        }
    }

    public static void subtract(int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4) {
        int length = iArr.length;
        if (Nat.sub(length, iArr2, iArr3, iArr4) != 0) {
            Nat.addTo(length, iArr, iArr4);
        }
    }

    private static void inversionResult(int[] iArr, int i, int[] iArr2, int[] iArr3) {
        if (i < 0) {
            Nat.add(iArr.length, iArr2, iArr, iArr3);
        } else {
            System.arraycopy(iArr2, 0, iArr3, 0, iArr.length);
        }
    }

    private static int inversionStep(int[] iArr, int[] iArr2, int i, int[] iArr3, int i2) {
        int length = iArr.length;
        int i3 = 0;
        while (iArr2[0] == 0) {
            Nat.shiftDownWord(i, iArr2, 0);
            i3 += 32;
        }
        int trailingZeroes = getTrailingZeroes(iArr2[0]);
        if (trailingZeroes > 0) {
            Nat.shiftDownBits(i, iArr2, trailingZeroes, 0);
            i3 += trailingZeroes;
        }
        for (int i4 = 0; i4 < i3; i4++) {
            if ((iArr3[0] & 1) != 0) {
                i2 = i2 < 0 ? i2 + Nat.addTo(length, iArr, iArr3) : i2 + Nat.subFrom(length, iArr, iArr3);
            }
            Nat.shiftDownBit(length, iArr3, i2);
        }
        return i2;
    }

    private static int getTrailingZeroes(int i) {
        int i2 = 0;
        while ((i & 1) == 0) {
            i >>>= 1;
            i2++;
        }
        return i2;
    }
}
