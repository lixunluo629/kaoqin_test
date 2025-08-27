package aj.org.objectweb.asm;

/* loaded from: aspectjweaver-1.8.14.jar:aj/org/objectweb/asm/AnnotationWriter.class */
final class AnnotationWriter extends AnnotationVisitor {
    private final ClassWriter a;
    private int b;
    private final boolean c;
    private final ByteVector d;
    private final ByteVector e;
    private final int f;
    AnnotationWriter g;
    AnnotationWriter h;

    AnnotationWriter(ClassWriter classWriter, boolean z, ByteVector byteVector, ByteVector byteVector2, int i) {
        super(327680);
        this.a = classWriter;
        this.c = z;
        this.d = byteVector;
        this.e = byteVector2;
        this.f = i;
    }

    @Override // aj.org.objectweb.asm.AnnotationVisitor
    public void visit(String str, Object obj) {
        this.b++;
        if (this.c) {
            this.d.putShort(this.a.newUTF8(str));
        }
        if (obj instanceof String) {
            this.d.b(115, this.a.newUTF8((String) obj));
            return;
        }
        if (obj instanceof Byte) {
            this.d.b(66, this.a.a((int) ((Byte) obj).byteValue()).a);
            return;
        }
        if (obj instanceof Boolean) {
            this.d.b(90, this.a.a(((Boolean) obj).booleanValue() ? 1 : 0).a);
            return;
        }
        if (obj instanceof Character) {
            this.d.b(67, this.a.a((int) ((Character) obj).charValue()).a);
            return;
        }
        if (obj instanceof Short) {
            this.d.b(83, this.a.a((int) ((Short) obj).shortValue()).a);
            return;
        }
        if (obj instanceof Type) {
            this.d.b(99, this.a.newUTF8(((Type) obj).getDescriptor()));
            return;
        }
        if (obj instanceof byte[]) {
            byte[] bArr = (byte[]) obj;
            this.d.b(91, bArr.length);
            for (byte b : bArr) {
                this.d.b(66, this.a.a((int) b).a);
            }
            return;
        }
        if (obj instanceof boolean[]) {
            boolean[] zArr = (boolean[]) obj;
            this.d.b(91, zArr.length);
            for (boolean z : zArr) {
                this.d.b(90, this.a.a(z ? 1 : 0).a);
            }
            return;
        }
        if (obj instanceof short[]) {
            short[] sArr = (short[]) obj;
            this.d.b(91, sArr.length);
            for (short s : sArr) {
                this.d.b(83, this.a.a((int) s).a);
            }
            return;
        }
        if (obj instanceof char[]) {
            char[] cArr = (char[]) obj;
            this.d.b(91, cArr.length);
            for (char c : cArr) {
                this.d.b(67, this.a.a((int) c).a);
            }
            return;
        }
        if (obj instanceof int[]) {
            int[] iArr = (int[]) obj;
            this.d.b(91, iArr.length);
            for (int i : iArr) {
                this.d.b(73, this.a.a(i).a);
            }
            return;
        }
        if (obj instanceof long[]) {
            long[] jArr = (long[]) obj;
            this.d.b(91, jArr.length);
            for (long j : jArr) {
                this.d.b(74, this.a.a(j).a);
            }
            return;
        }
        if (obj instanceof float[]) {
            float[] fArr = (float[]) obj;
            this.d.b(91, fArr.length);
            for (float f : fArr) {
                this.d.b(70, this.a.a(f).a);
            }
            return;
        }
        if (!(obj instanceof double[])) {
            Item itemA = this.a.a(obj);
            this.d.b(".s.IFJDCS".charAt(itemA.b), itemA.a);
            return;
        }
        double[] dArr = (double[]) obj;
        this.d.b(91, dArr.length);
        for (double d : dArr) {
            this.d.b(68, this.a.a(d).a);
        }
    }

    @Override // aj.org.objectweb.asm.AnnotationVisitor
    public void visitEnum(String str, String str2, String str3) {
        this.b++;
        if (this.c) {
            this.d.putShort(this.a.newUTF8(str));
        }
        this.d.b(101, this.a.newUTF8(str2)).putShort(this.a.newUTF8(str3));
    }

    @Override // aj.org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitAnnotation(String str, String str2) {
        this.b++;
        if (this.c) {
            this.d.putShort(this.a.newUTF8(str));
        }
        this.d.b(64, this.a.newUTF8(str2)).putShort(0);
        return new AnnotationWriter(this.a, true, this.d, this.d, this.d.b - 2);
    }

    @Override // aj.org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitArray(String str) {
        this.b++;
        if (this.c) {
            this.d.putShort(this.a.newUTF8(str));
        }
        this.d.b(91, 0);
        return new AnnotationWriter(this.a, false, this.d, this.d, this.d.b - 2);
    }

    @Override // aj.org.objectweb.asm.AnnotationVisitor
    public void visitEnd() {
        if (this.e != null) {
            byte[] bArr = this.e.a;
            bArr[this.f] = (byte) (this.b >>> 8);
            bArr[this.f + 1] = (byte) this.b;
        }
    }

    int a() {
        int i = 0;
        AnnotationWriter annotationWriter = this;
        while (true) {
            AnnotationWriter annotationWriter2 = annotationWriter;
            if (annotationWriter2 == null) {
                return i;
            }
            i += annotationWriter2.d.b;
            annotationWriter = annotationWriter2.g;
        }
    }

    void a(ByteVector byteVector) {
        int i = 0;
        int i2 = 2;
        AnnotationWriter annotationWriter = null;
        for (AnnotationWriter annotationWriter2 = this; annotationWriter2 != null; annotationWriter2 = annotationWriter2.g) {
            i++;
            i2 += annotationWriter2.d.b;
            annotationWriter2.visitEnd();
            annotationWriter2.h = annotationWriter;
            annotationWriter = annotationWriter2;
        }
        byteVector.putInt(i2);
        byteVector.putShort(i);
        AnnotationWriter annotationWriter3 = annotationWriter;
        while (true) {
            AnnotationWriter annotationWriter4 = annotationWriter3;
            if (annotationWriter4 == null) {
                return;
            }
            byteVector.putByteArray(annotationWriter4.d.a, 0, annotationWriter4.d.b);
            annotationWriter3 = annotationWriter4.h;
        }
    }

    static void a(AnnotationWriter[] annotationWriterArr, int i, ByteVector byteVector) {
        int length = 1 + (2 * (annotationWriterArr.length - i));
        for (int i2 = i; i2 < annotationWriterArr.length; i2++) {
            length += annotationWriterArr[i2] == null ? 0 : annotationWriterArr[i2].a();
        }
        byteVector.putInt(length).putByte(annotationWriterArr.length - i);
        for (int i3 = i; i3 < annotationWriterArr.length; i3++) {
            AnnotationWriter annotationWriter = null;
            int i4 = 0;
            for (AnnotationWriter annotationWriter2 = annotationWriterArr[i3]; annotationWriter2 != null; annotationWriter2 = annotationWriter2.g) {
                i4++;
                annotationWriter2.visitEnd();
                annotationWriter2.h = annotationWriter;
                annotationWriter = annotationWriter2;
            }
            byteVector.putShort(i4);
            AnnotationWriter annotationWriter3 = annotationWriter;
            while (true) {
                AnnotationWriter annotationWriter4 = annotationWriter3;
                if (annotationWriter4 != null) {
                    byteVector.putByteArray(annotationWriter4.d.a, 0, annotationWriter4.d.b);
                    annotationWriter3 = annotationWriter4.h;
                }
            }
        }
    }

    static void a(int i, TypePath typePath, ByteVector byteVector) {
        switch (i >>> 24) {
            case 0:
            case 1:
            case 22:
                byteVector.putShort(i >>> 16);
                break;
            case 19:
            case 20:
            case 21:
                byteVector.putByte(i >>> 24);
                break;
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
                byteVector.putInt(i);
                break;
            default:
                byteVector.b(i >>> 24, (i & 16776960) >> 8);
                break;
        }
        if (typePath == null) {
            byteVector.putByte(0);
        } else {
            byteVector.putByteArray(typePath.a, typePath.b, (typePath.a[typePath.b] * 2) + 1);
        }
    }
}
