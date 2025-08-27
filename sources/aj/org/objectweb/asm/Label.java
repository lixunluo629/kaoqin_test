package aj.org.objectweb.asm;

import com.itextpdf.kernel.pdf.tagging.StandardRoles;

/* loaded from: aspectjweaver-1.8.14.jar:aj/org/objectweb/asm/Label.class */
public class Label {
    public Object info;
    int a;
    int b;
    int c;
    private int d;
    private int[] e;
    int f;
    int g;
    Frame h;
    Label i;
    Edge j;
    Label k;

    public int getOffset() {
        if ((this.a & 2) == 0) {
            throw new IllegalStateException("Label offset position has not been resolved yet");
        }
        return this.c;
    }

    void a(MethodWriter methodWriter, ByteVector byteVector, int i, boolean z) {
        if ((this.a & 2) != 0) {
            if (z) {
                byteVector.putInt(this.c - i);
                return;
            } else {
                byteVector.putShort(this.c - i);
                return;
            }
        }
        if (z) {
            a((-1) - i, byteVector.b);
            byteVector.putInt(-1);
        } else {
            a(i, byteVector.b);
            byteVector.putShort(-1);
        }
    }

    private void a(int i, int i2) {
        if (this.e == null) {
            this.e = new int[6];
        }
        if (this.d >= this.e.length) {
            int[] iArr = new int[this.e.length + 6];
            System.arraycopy(this.e, 0, iArr, 0, this.e.length);
            this.e = iArr;
        }
        int[] iArr2 = this.e;
        int i3 = this.d;
        this.d = i3 + 1;
        iArr2[i3] = i;
        int[] iArr3 = this.e;
        int i4 = this.d;
        this.d = i4 + 1;
        iArr3[i4] = i2;
    }

    boolean a(MethodWriter methodWriter, int i, byte[] bArr) {
        boolean z = false;
        this.a |= 2;
        this.c = i;
        int i2 = 0;
        while (i2 < this.d) {
            int i3 = i2;
            int i4 = i2 + 1;
            int i5 = this.e[i3];
            i2 = i4 + 1;
            int i6 = this.e[i4];
            if (i5 >= 0) {
                int i7 = i - i5;
                if (i7 < -32768 || i7 > 32767) {
                    int i8 = bArr[i6 - 1] & 255;
                    if (i8 <= 168) {
                        bArr[i6 - 1] = (byte) (i8 + 49);
                    } else {
                        bArr[i6 - 1] = (byte) (i8 + 20);
                    }
                    z = true;
                }
                bArr[i6] = (byte) (i7 >>> 8);
                bArr[i6 + 1] = (byte) i7;
            } else {
                int i9 = i + i5 + 1;
                int i10 = i6 + 1;
                bArr[i6] = (byte) (i9 >>> 24);
                int i11 = i10 + 1;
                bArr[i10] = (byte) (i9 >>> 16);
                bArr[i11] = (byte) (i9 >>> 8);
                bArr[i11 + 1] = (byte) i9;
            }
        }
        return z;
    }

    Label a() {
        return this.h == null ? this : this.h.b;
    }

    boolean a(long j) {
        return ((this.a & 1024) == 0 || (this.e[(int) (j >>> 32)] & ((int) j)) == 0) ? false : true;
    }

    boolean a(Label label) {
        if ((this.a & 1024) == 0 || (label.a & 1024) == 0) {
            return false;
        }
        for (int i = 0; i < this.e.length; i++) {
            if ((this.e[i] & label.e[i]) != 0) {
                return true;
            }
        }
        return false;
    }

    void a(long j, int i) {
        if ((this.a & 1024) == 0) {
            this.a |= 1024;
            this.e = new int[(i / 32) + 1];
        }
        int[] iArr = this.e;
        int i2 = (int) (j >>> 32);
        iArr[i2] = iArr[i2] | ((int) j);
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x00a1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void b(aj.org.objectweb.asm.Label r6, long r7, int r9) {
        /*
            r5 = this;
            r0 = r5
            r10 = r0
        L3:
            r0 = r10
            if (r0 == 0) goto Le3
            r0 = r10
            r11 = r0
            r0 = r11
            aj.org.objectweb.asm.Label r0 = r0.k
            r10 = r0
            r0 = r11
            r1 = 0
            r0.k = r1
            r0 = r6
            if (r0 == 0) goto L81
            r0 = r11
            int r0 = r0.a
            r1 = 2048(0x800, float:2.87E-42)
            r0 = r0 & r1
            if (r0 == 0) goto L2c
            goto L3
        L2c:
            r0 = r11
            r1 = r0
            int r1 = r1.a
            r2 = 2048(0x800, float:2.87E-42)
            r1 = r1 | r2
            r0.a = r1
            r0 = r11
            int r0 = r0.a
            r1 = 256(0x100, float:3.59E-43)
            r0 = r0 & r1
            if (r0 == 0) goto L95
            r0 = r11
            r1 = r6
            boolean r0 = r0.a(r1)
            if (r0 != 0) goto L95
            aj.org.objectweb.asm.Edge r0 = new aj.org.objectweb.asm.Edge
            r1 = r0
            r1.<init>()
            r12 = r0
            r0 = r12
            r1 = r11
            int r1 = r1.f
            r0.a = r1
            r0 = r12
            r1 = r6
            aj.org.objectweb.asm.Edge r1 = r1.j
            aj.org.objectweb.asm.Label r1 = r1.b
            r0.b = r1
            r0 = r12
            r1 = r11
            aj.org.objectweb.asm.Edge r1 = r1.j
            r0.c = r1
            r0 = r11
            r1 = r12
            r0.j = r1
            goto L95
        L81:
            r0 = r11
            r1 = r7
            boolean r0 = r0.a(r1)
            if (r0 == 0) goto L8d
            goto L3
        L8d:
            r0 = r11
            r1 = r7
            r2 = r9
            r0.a(r1, r2)
        L95:
            r0 = r11
            aj.org.objectweb.asm.Edge r0 = r0.j
            r12 = r0
        L9c:
            r0 = r12
            if (r0 == 0) goto Le0
            r0 = r11
            int r0 = r0.a
            r1 = 128(0x80, float:1.8E-43)
            r0 = r0 & r1
            if (r0 == 0) goto Lba
            r0 = r12
            r1 = r11
            aj.org.objectweb.asm.Edge r1 = r1.j
            aj.org.objectweb.asm.Edge r1 = r1.c
            if (r0 == r1) goto Ld6
        Lba:
            r0 = r12
            aj.org.objectweb.asm.Label r0 = r0.b
            aj.org.objectweb.asm.Label r0 = r0.k
            if (r0 != 0) goto Ld6
            r0 = r12
            aj.org.objectweb.asm.Label r0 = r0.b
            r1 = r10
            r0.k = r1
            r0 = r12
            aj.org.objectweb.asm.Label r0 = r0.b
            r10 = r0
        Ld6:
            r0 = r12
            aj.org.objectweb.asm.Edge r0 = r0.c
            r12 = r0
            goto L9c
        Le0:
            goto L3
        Le3:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: aj.org.objectweb.asm.Label.b(aj.org.objectweb.asm.Label, long, int):void");
    }

    public String toString() {
        return new StringBuffer().append(StandardRoles.L).append(System.identityHashCode(this)).toString();
    }
}
