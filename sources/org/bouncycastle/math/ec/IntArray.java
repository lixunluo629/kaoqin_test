package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.util.Arrays;
import org.objectweb.asm.Opcodes;

/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/math/ec/IntArray.class */
class IntArray {
    private int[] m_ints;

    public IntArray(int i) {
        this.m_ints = new int[i];
    }

    public IntArray(int[] iArr) {
        this.m_ints = iArr;
    }

    public IntArray(BigInteger bigInteger) {
        this(bigInteger, 0);
    }

    public IntArray(BigInteger bigInteger, int i) {
        if (bigInteger.signum() == -1) {
            throw new IllegalArgumentException("Only positive Integers allowed");
        }
        if (bigInteger.equals(ECConstants.ZERO)) {
            this.m_ints = new int[]{0};
            return;
        }
        byte[] byteArray = bigInteger.toByteArray();
        int length = byteArray.length;
        int i2 = 0;
        if (byteArray[0] == 0) {
            length--;
            i2 = 1;
        }
        int i3 = (length + 3) / 4;
        if (i3 < i) {
            this.m_ints = new int[i];
        } else {
            this.m_ints = new int[i3];
        }
        int i4 = i3 - 1;
        int i5 = (length % 4) + i2;
        int i6 = 0;
        int i7 = i2;
        if (i2 < i5) {
            while (i7 < i5) {
                int i8 = i6 << 8;
                int i9 = byteArray[i7];
                if (i9 < 0) {
                    i9 += 256;
                }
                i6 = i8 | i9;
                i7++;
            }
            i4--;
            this.m_ints[i4] = i6;
        }
        while (i4 >= 0) {
            int i10 = 0;
            for (int i11 = 0; i11 < 4; i11++) {
                int i12 = i10 << 8;
                int i13 = i7;
                i7++;
                int i14 = byteArray[i13];
                if (i14 < 0) {
                    i14 += 256;
                }
                i10 = i12 | i14;
            }
            this.m_ints[i4] = i10;
            i4--;
        }
    }

    public boolean isZero() {
        return this.m_ints.length == 0 || (this.m_ints[0] == 0 && getUsedLength() == 0);
    }

    public int getUsedLength() {
        int length = this.m_ints.length;
        if (length < 1) {
            return 0;
        }
        if (this.m_ints[0] != 0) {
            do {
                length--;
            } while (this.m_ints[length] == 0);
            return length + 1;
        }
        do {
            length--;
            if (this.m_ints[length] != 0) {
                return length + 1;
            }
        } while (length > 0);
        return 0;
    }

    public int bitLength() {
        int usedLength = getUsedLength();
        if (usedLength == 0) {
            return 0;
        }
        int i = usedLength - 1;
        int i2 = this.m_ints[i];
        int i3 = (i << 5) + 1;
        if ((i2 & Opcodes.V_PREVIEW_EXPERIMENTAL) != 0) {
            if ((i2 & (-16777216)) != 0) {
                i3 += 24;
                i2 >>>= 24;
            } else {
                i3 += 16;
                i2 >>>= 16;
            }
        } else if (i2 > 255) {
            i3 += 8;
            i2 >>>= 8;
        }
        while (i2 != 1) {
            i3++;
            i2 >>>= 1;
        }
        return i3;
    }

    private int[] resizedInts(int i) {
        int[] iArr = new int[i];
        int length = this.m_ints.length;
        System.arraycopy(this.m_ints, 0, iArr, 0, length < i ? length : i);
        return iArr;
    }

    public BigInteger toBigInteger() {
        int usedLength = getUsedLength();
        if (usedLength == 0) {
            return ECConstants.ZERO;
        }
        int i = this.m_ints[usedLength - 1];
        byte[] bArr = new byte[4];
        int i2 = 0;
        boolean z = false;
        for (int i3 = 3; i3 >= 0; i3--) {
            byte b = (byte) (i >>> (8 * i3));
            if (z || b != 0) {
                z = true;
                int i4 = i2;
                i2++;
                bArr[i4] = b;
            }
        }
        byte[] bArr2 = new byte[(4 * (usedLength - 1)) + i2];
        for (int i5 = 0; i5 < i2; i5++) {
            bArr2[i5] = bArr[i5];
        }
        for (int i6 = usedLength - 2; i6 >= 0; i6--) {
            for (int i7 = 3; i7 >= 0; i7--) {
                int i8 = i2;
                i2++;
                bArr2[i8] = (byte) (this.m_ints[i6] >>> (8 * i7));
            }
        }
        return new BigInteger(1, bArr2);
    }

    public void shiftLeft() {
        int usedLength = getUsedLength();
        if (usedLength == 0) {
            return;
        }
        if (this.m_ints[usedLength - 1] < 0) {
            usedLength++;
            if (usedLength > this.m_ints.length) {
                this.m_ints = resizedInts(this.m_ints.length + 1);
            }
        }
        boolean z = false;
        for (int i = 0; i < usedLength; i++) {
            boolean z2 = this.m_ints[i] < 0;
            int[] iArr = this.m_ints;
            int i2 = i;
            iArr[i2] = iArr[i2] << 1;
            if (z) {
                int[] iArr2 = this.m_ints;
                int i3 = i;
                iArr2[i3] = iArr2[i3] | 1;
            }
            z = z2;
        }
    }

    public IntArray shiftLeft(int i) {
        int usedLength = getUsedLength();
        if (usedLength != 0 && i != 0) {
            if (i > 31) {
                throw new IllegalArgumentException("shiftLeft() for max 31 bits , " + i + "bit shift is not possible");
            }
            int[] iArr = new int[usedLength + 1];
            int i2 = 32 - i;
            iArr[0] = this.m_ints[0] << i;
            for (int i3 = 1; i3 < usedLength; i3++) {
                iArr[i3] = (this.m_ints[i3] << i) | (this.m_ints[i3 - 1] >>> i2);
            }
            iArr[usedLength] = this.m_ints[usedLength - 1] >>> i2;
            return new IntArray(iArr);
        }
        return this;
    }

    public void addShifted(IntArray intArray, int i) {
        int usedLength = intArray.getUsedLength();
        int i2 = usedLength + i;
        if (i2 > this.m_ints.length) {
            this.m_ints = resizedInts(i2);
        }
        for (int i3 = 0; i3 < usedLength; i3++) {
            int[] iArr = this.m_ints;
            int i4 = i3 + i;
            iArr[i4] = iArr[i4] ^ intArray.m_ints[i3];
        }
    }

    public int getLength() {
        return this.m_ints.length;
    }

    public boolean testBit(int i) {
        return (this.m_ints[i >> 5] & (1 << (i & 31))) != 0;
    }

    public void flipBit(int i) {
        int i2 = i >> 5;
        int[] iArr = this.m_ints;
        iArr[i2] = iArr[i2] ^ (1 << (i & 31));
    }

    public void setBit(int i) {
        int i2 = i >> 5;
        int[] iArr = this.m_ints;
        iArr[i2] = iArr[i2] | (1 << (i & 31));
    }

    public IntArray multiply(IntArray intArray, int i) {
        int i2 = (i + 31) >> 5;
        if (this.m_ints.length < i2) {
            this.m_ints = resizedInts(i2);
        }
        IntArray intArray2 = new IntArray(intArray.resizedInts(intArray.getLength() + 1));
        IntArray intArray3 = new IntArray(((i + i) + 31) >> 5);
        int i3 = 1;
        for (int i4 = 0; i4 < 32; i4++) {
            for (int i5 = 0; i5 < i2; i5++) {
                if ((this.m_ints[i5] & i3) != 0) {
                    intArray3.addShifted(intArray2, i5);
                }
            }
            i3 <<= 1;
            intArray2.shiftLeft();
        }
        return intArray3;
    }

    public void reduce(int i, int[] iArr) {
        for (int i2 = (i + i) - 2; i2 >= i; i2--) {
            if (testBit(i2)) {
                int i3 = i2 - i;
                flipBit(i3);
                flipBit(i2);
                int length = iArr.length;
                while (true) {
                    length--;
                    if (length >= 0) {
                        flipBit(iArr[length] + i3);
                    }
                }
            }
        }
        this.m_ints = resizedInts((i + 31) >> 5);
    }

    public IntArray square(int i) {
        int[] iArr = {0, 1, 4, 5, 16, 17, 20, 21, 64, 65, 68, 69, 80, 81, 84, 85};
        int i2 = (i + 31) >> 5;
        if (this.m_ints.length < i2) {
            this.m_ints = resizedInts(i2);
        }
        IntArray intArray = new IntArray(i2 + i2);
        for (int i3 = 0; i3 < i2; i3++) {
            int i4 = 0;
            for (int i5 = 0; i5 < 4; i5++) {
                i4 = (i4 >>> 8) | (iArr[(this.m_ints[i3] >>> (i5 * 4)) & 15] << 24);
            }
            intArray.m_ints[i3 + i3] = i4;
            int i6 = 0;
            int i7 = this.m_ints[i3] >>> 16;
            for (int i8 = 0; i8 < 4; i8++) {
                i6 = (i6 >>> 8) | (iArr[(i7 >>> (i8 * 4)) & 15] << 24);
            }
            intArray.m_ints[i3 + i3 + 1] = i6;
        }
        return intArray;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof IntArray)) {
            return false;
        }
        IntArray intArray = (IntArray) obj;
        int usedLength = getUsedLength();
        if (intArray.getUsedLength() != usedLength) {
            return false;
        }
        for (int i = 0; i < usedLength; i++) {
            if (this.m_ints[i] != intArray.m_ints[i]) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int usedLength = getUsedLength();
        int i = 1;
        for (int i2 = 0; i2 < usedLength; i2++) {
            i = (i * 31) + this.m_ints[i2];
        }
        return i;
    }

    public Object clone() {
        return new IntArray(Arrays.clone(this.m_ints));
    }

    public String toString() {
        int usedLength = getUsedLength();
        if (usedLength == 0) {
            return "0";
        }
        StringBuffer stringBuffer = new StringBuffer(Integer.toBinaryString(this.m_ints[usedLength - 1]));
        for (int i = usedLength - 2; i >= 0; i--) {
            String binaryString = Integer.toBinaryString(this.m_ints[i]);
            for (int length = binaryString.length(); length < 8; length++) {
                binaryString = "0" + binaryString;
            }
            stringBuffer.append(binaryString);
        }
        return stringBuffer.toString();
    }
}
