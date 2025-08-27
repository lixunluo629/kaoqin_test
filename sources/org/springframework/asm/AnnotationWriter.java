package org.springframework.asm;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/asm/AnnotationWriter.class */
final class AnnotationWriter extends AnnotationVisitor {
    private final ClassWriter cw;
    private int size;
    private final boolean named;
    private final ByteVector bv;
    private final ByteVector parent;
    private final int offset;
    AnnotationWriter next;
    AnnotationWriter prev;

    AnnotationWriter(ClassWriter cw, boolean named, ByteVector bv, ByteVector parent, int offset) {
        super(393216);
        this.cw = cw;
        this.named = named;
        this.bv = bv;
        this.parent = parent;
        this.offset = offset;
    }

    @Override // org.springframework.asm.AnnotationVisitor
    public void visit(String name, Object value) {
        this.size++;
        if (this.named) {
            this.bv.putShort(this.cw.newUTF8(name));
        }
        if (value instanceof String) {
            this.bv.put12(115, this.cw.newUTF8((String) value));
            return;
        }
        if (value instanceof Byte) {
            this.bv.put12(66, this.cw.newInteger(((Byte) value).byteValue()).index);
            return;
        }
        if (value instanceof Boolean) {
            this.bv.put12(90, this.cw.newInteger(((Boolean) value).booleanValue() ? 1 : 0).index);
            return;
        }
        if (value instanceof Character) {
            this.bv.put12(67, this.cw.newInteger(((Character) value).charValue()).index);
            return;
        }
        if (value instanceof Short) {
            this.bv.put12(83, this.cw.newInteger(((Short) value).shortValue()).index);
            return;
        }
        if (value instanceof Type) {
            this.bv.put12(99, this.cw.newUTF8(((Type) value).getDescriptor()));
            return;
        }
        if (value instanceof byte[]) {
            byte[] v = (byte[]) value;
            this.bv.put12(91, v.length);
            for (byte b : v) {
                this.bv.put12(66, this.cw.newInteger(b).index);
            }
            return;
        }
        if (value instanceof boolean[]) {
            boolean[] v2 = (boolean[]) value;
            this.bv.put12(91, v2.length);
            for (boolean z : v2) {
                this.bv.put12(90, this.cw.newInteger(z ? 1 : 0).index);
            }
            return;
        }
        if (value instanceof short[]) {
            short[] v3 = (short[]) value;
            this.bv.put12(91, v3.length);
            for (short s : v3) {
                this.bv.put12(83, this.cw.newInteger(s).index);
            }
            return;
        }
        if (value instanceof char[]) {
            char[] v4 = (char[]) value;
            this.bv.put12(91, v4.length);
            for (char c : v4) {
                this.bv.put12(67, this.cw.newInteger(c).index);
            }
            return;
        }
        if (value instanceof int[]) {
            int[] v5 = (int[]) value;
            this.bv.put12(91, v5.length);
            for (int i : v5) {
                this.bv.put12(73, this.cw.newInteger(i).index);
            }
            return;
        }
        if (value instanceof long[]) {
            long[] v6 = (long[]) value;
            this.bv.put12(91, v6.length);
            for (long j : v6) {
                this.bv.put12(74, this.cw.newLong(j).index);
            }
            return;
        }
        if (value instanceof float[]) {
            float[] v7 = (float[]) value;
            this.bv.put12(91, v7.length);
            for (float f : v7) {
                this.bv.put12(70, this.cw.newFloat(f).index);
            }
            return;
        }
        if (value instanceof double[]) {
            double[] v8 = (double[]) value;
            this.bv.put12(91, v8.length);
            for (double d : v8) {
                this.bv.put12(68, this.cw.newDouble(d).index);
            }
            return;
        }
        Item i2 = this.cw.newConstItem(value);
        this.bv.put12(".s.IFJDCS".charAt(i2.type), i2.index);
    }

    @Override // org.springframework.asm.AnnotationVisitor
    public void visitEnum(String name, String desc, String value) {
        this.size++;
        if (this.named) {
            this.bv.putShort(this.cw.newUTF8(name));
        }
        this.bv.put12(101, this.cw.newUTF8(desc)).putShort(this.cw.newUTF8(value));
    }

    @Override // org.springframework.asm.AnnotationVisitor
    public AnnotationVisitor visitAnnotation(String name, String desc) {
        this.size++;
        if (this.named) {
            this.bv.putShort(this.cw.newUTF8(name));
        }
        this.bv.put12(64, this.cw.newUTF8(desc)).putShort(0);
        return new AnnotationWriter(this.cw, true, this.bv, this.bv, this.bv.length - 2);
    }

    @Override // org.springframework.asm.AnnotationVisitor
    public AnnotationVisitor visitArray(String name) {
        this.size++;
        if (this.named) {
            this.bv.putShort(this.cw.newUTF8(name));
        }
        this.bv.put12(91, 0);
        return new AnnotationWriter(this.cw, false, this.bv, this.bv, this.bv.length - 2);
    }

    @Override // org.springframework.asm.AnnotationVisitor
    public void visitEnd() {
        if (this.parent != null) {
            byte[] data = this.parent.data;
            data[this.offset] = (byte) (this.size >>> 8);
            data[this.offset + 1] = (byte) this.size;
        }
    }

    int getSize() {
        int size = 0;
        AnnotationWriter annotationWriter = this;
        while (true) {
            AnnotationWriter aw = annotationWriter;
            if (aw != null) {
                size += aw.bv.length;
                annotationWriter = aw.next;
            } else {
                return size;
            }
        }
    }

    void put(ByteVector out) {
        int n = 0;
        int size = 2;
        AnnotationWriter last = null;
        for (AnnotationWriter aw = this; aw != null; aw = aw.next) {
            n++;
            size += aw.bv.length;
            aw.visitEnd();
            aw.prev = last;
            last = aw;
        }
        out.putInt(size);
        out.putShort(n);
        AnnotationWriter annotationWriter = last;
        while (true) {
            AnnotationWriter aw2 = annotationWriter;
            if (aw2 != null) {
                out.putByteArray(aw2.bv.data, 0, aw2.bv.length);
                annotationWriter = aw2.prev;
            } else {
                return;
            }
        }
    }

    static void put(AnnotationWriter[] panns, int off, ByteVector out) {
        int size = 1 + (2 * (panns.length - off));
        for (int i = off; i < panns.length; i++) {
            size += panns[i] == null ? 0 : panns[i].getSize();
        }
        out.putInt(size).putByte(panns.length - off);
        for (int i2 = off; i2 < panns.length; i2++) {
            AnnotationWriter last = null;
            int n = 0;
            for (AnnotationWriter aw = panns[i2]; aw != null; aw = aw.next) {
                n++;
                aw.visitEnd();
                aw.prev = last;
                last = aw;
            }
            out.putShort(n);
            AnnotationWriter annotationWriter = last;
            while (true) {
                AnnotationWriter aw2 = annotationWriter;
                if (aw2 != null) {
                    out.putByteArray(aw2.bv.data, 0, aw2.bv.length);
                    annotationWriter = aw2.prev;
                }
            }
        }
    }

    static void putTarget(int typeRef, TypePath typePath, ByteVector out) {
        switch (typeRef >>> 24) {
            case 0:
            case 1:
            case 22:
                out.putShort(typeRef >>> 16);
                break;
            case 19:
            case 20:
            case 21:
                out.putByte(typeRef >>> 24);
                break;
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
                out.putInt(typeRef);
                break;
            default:
                out.put12(typeRef >>> 24, (typeRef & 16776960) >> 8);
                break;
        }
        if (typePath == null) {
            out.putByte(0);
        } else {
            int length = (typePath.b[typePath.offset] * 2) + 1;
            out.putByteArray(typePath.b, typePath.offset, length);
        }
    }
}
