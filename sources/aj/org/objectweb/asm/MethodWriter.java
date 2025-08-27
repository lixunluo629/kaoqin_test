package aj.org.objectweb.asm;

import org.apache.ibatis.javassist.bytecode.AnnotationDefaultAttribute;
import org.apache.ibatis.javassist.bytecode.AnnotationsAttribute;
import org.apache.ibatis.javassist.bytecode.DeprecatedAttribute;
import org.apache.ibatis.javassist.bytecode.ExceptionsAttribute;
import org.apache.ibatis.javassist.bytecode.LineNumberAttribute;
import org.apache.ibatis.javassist.bytecode.LocalVariableAttribute;
import org.apache.ibatis.javassist.bytecode.MethodParametersAttribute;
import org.apache.ibatis.javassist.bytecode.ParameterAnnotationsAttribute;
import org.apache.ibatis.javassist.bytecode.SignatureAttribute;
import org.apache.ibatis.javassist.bytecode.StackMap;
import org.apache.ibatis.javassist.bytecode.StackMapTable;
import org.apache.ibatis.javassist.bytecode.SyntheticAttribute;
import org.apache.ibatis.javassist.bytecode.TypeAnnotationsAttribute;
import org.apache.poi.ddf.EscherProperties;

/* loaded from: aspectjweaver-1.8.14.jar:aj/org/objectweb/asm/MethodWriter.class */
class MethodWriter extends MethodVisitor {
    final ClassWriter b;
    private int c;
    private final int d;
    private final int e;
    private final String f;
    String g;
    int h;
    int i;
    int j;
    int[] k;
    private ByteVector l;
    private AnnotationWriter m;
    private AnnotationWriter n;
    private AnnotationWriter U;
    private AnnotationWriter V;
    private AnnotationWriter[] o;
    private AnnotationWriter[] p;
    private int S;
    private Attribute q;
    private ByteVector r;
    private int s;
    private int t;
    private int T;
    private int u;
    private ByteVector v;
    private int w;
    private int[] x;
    private int[] z;
    private int A;
    private Handler B;
    private Handler C;
    private int Z;
    private ByteVector $;
    private int D;
    private ByteVector E;
    private int F;
    private ByteVector G;
    private int H;
    private ByteVector I;
    private int Y;
    private AnnotationWriter W;
    private AnnotationWriter X;
    private Attribute J;
    private boolean K;
    private int L;
    private final int M;
    private Label N;
    private Label O;
    private Label P;
    private int Q;
    private int R;

    MethodWriter(ClassWriter classWriter, int i, String str, String str2, String str3, String[] strArr, boolean z, boolean z2) {
        super(327680);
        this.r = new ByteVector();
        if (classWriter.D == null) {
            classWriter.D = this;
        } else {
            classWriter.E.mv = this;
        }
        classWriter.E = this;
        this.b = classWriter;
        this.c = i;
        if ("<init>".equals(str)) {
            this.c |= 524288;
        }
        this.d = classWriter.newUTF8(str);
        this.e = classWriter.newUTF8(str2);
        this.f = str2;
        this.g = str3;
        if (strArr != null && strArr.length > 0) {
            this.j = strArr.length;
            this.k = new int[this.j];
            for (int i2 = 0; i2 < this.j; i2++) {
                this.k[i2] = classWriter.newClass(strArr[i2]);
            }
        }
        this.M = z2 ? 0 : z ? 1 : 2;
        if (z || z2) {
            int argumentsAndReturnSizes = Type.getArgumentsAndReturnSizes(this.f) >> 2;
            argumentsAndReturnSizes = (i & 8) != 0 ? argumentsAndReturnSizes - 1 : argumentsAndReturnSizes;
            this.t = argumentsAndReturnSizes;
            this.T = argumentsAndReturnSizes;
            this.N = new Label();
            this.N.a |= 8;
            visitLabel(this.N);
        }
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public void visitParameter(String str, int i) {
        if (this.$ == null) {
            this.$ = new ByteVector();
        }
        this.Z++;
        this.$.putShort(str == null ? 0 : this.b.newUTF8(str)).putShort(i);
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitAnnotationDefault() {
        this.l = new ByteVector();
        return new AnnotationWriter(this.b, false, this.l, null, 0);
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitAnnotation(String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        byteVector.putShort(this.b.newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this.b, true, byteVector, byteVector, 2);
        if (z) {
            annotationWriter.g = this.m;
            this.m = annotationWriter;
        } else {
            annotationWriter.g = this.n;
            this.n = annotationWriter;
        }
        return annotationWriter;
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitTypeAnnotation(int i, TypePath typePath, String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        AnnotationWriter.a(i, typePath, byteVector);
        byteVector.putShort(this.b.newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this.b, true, byteVector, byteVector, byteVector.b - 2);
        if (z) {
            annotationWriter.g = this.U;
            this.U = annotationWriter;
        } else {
            annotationWriter.g = this.V;
            this.V = annotationWriter;
        }
        return annotationWriter;
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitParameterAnnotation(int i, String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        if ("Ljava/lang/Synthetic;".equals(str)) {
            this.S = Math.max(this.S, i + 1);
            return new AnnotationWriter(this.b, false, byteVector, null, 0);
        }
        byteVector.putShort(this.b.newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this.b, true, byteVector, byteVector, 2);
        if (z) {
            if (this.o == null) {
                this.o = new AnnotationWriter[Type.getArgumentTypes(this.f).length];
            }
            annotationWriter.g = this.o[i];
            this.o[i] = annotationWriter;
        } else {
            if (this.p == null) {
                this.p = new AnnotationWriter[Type.getArgumentTypes(this.f).length];
            }
            annotationWriter.g = this.p[i];
            this.p[i] = annotationWriter;
        }
        return annotationWriter;
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public void visitAttribute(Attribute attribute) {
        if (attribute.isCodeAttribute()) {
            attribute.a = this.J;
            this.J = attribute;
        } else {
            attribute.a = this.q;
            this.q = attribute;
        }
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public void visitCode() {
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public void visitFrame(int i, int i2, Object[] objArr, int i3, Object[] objArr2) {
        int i4;
        if (this.M == 0) {
            return;
        }
        if (i == -1) {
            if (this.x == null) {
                f();
            }
            this.T = i2;
            int iA = a(this.r.b, i2, i3);
            for (int i5 = 0; i5 < i2; i5++) {
                if (objArr[i5] instanceof String) {
                    int i6 = iA;
                    iA++;
                    this.z[i6] = 24117248 | this.b.m1c((String) objArr[i5]);
                } else if (objArr[i5] instanceof Integer) {
                    int i7 = iA;
                    iA++;
                    this.z[i7] = ((Integer) objArr[i5]).intValue();
                } else {
                    int i8 = iA;
                    iA++;
                    this.z[i8] = 25165824 | this.b.a("", ((Label) objArr[i5]).c);
                }
            }
            for (int i9 = 0; i9 < i3; i9++) {
                if (objArr2[i9] instanceof String) {
                    int i10 = iA;
                    iA++;
                    this.z[i10] = 24117248 | this.b.m1c((String) objArr2[i9]);
                } else if (objArr2[i9] instanceof Integer) {
                    int i11 = iA;
                    iA++;
                    this.z[i11] = ((Integer) objArr2[i9]).intValue();
                } else {
                    int i12 = iA;
                    iA++;
                    this.z[i12] = 25165824 | this.b.a("", ((Label) objArr2[i9]).c);
                }
            }
            b();
        } else {
            if (this.v == null) {
                this.v = new ByteVector();
                i4 = this.r.b;
            } else {
                i4 = (this.r.b - this.w) - 1;
                if (i4 < 0) {
                    if (i != 3) {
                        throw new IllegalStateException();
                    }
                    return;
                }
            }
            switch (i) {
                case 0:
                    this.T = i2;
                    this.v.putByte(255).putShort(i4).putShort(i2);
                    for (int i13 = 0; i13 < i2; i13++) {
                        a(objArr[i13]);
                    }
                    this.v.putShort(i3);
                    for (int i14 = 0; i14 < i3; i14++) {
                        a(objArr2[i14]);
                    }
                    break;
                case 1:
                    this.T += i2;
                    this.v.putByte(251 + i2).putShort(i4);
                    for (int i15 = 0; i15 < i2; i15++) {
                        a(objArr[i15]);
                    }
                    break;
                case 2:
                    this.T -= i2;
                    this.v.putByte(251 - i2).putShort(i4);
                    break;
                case 3:
                    if (i4 < 64) {
                        this.v.putByte(i4);
                        break;
                    } else {
                        this.v.putByte(251).putShort(i4);
                        break;
                    }
                case 4:
                    if (i4 < 64) {
                        this.v.putByte(64 + i4);
                    } else {
                        this.v.putByte(247).putShort(i4);
                    }
                    a(objArr2[0]);
                    break;
            }
            this.w = this.r.b;
            this.u++;
        }
        this.s = Math.max(this.s, i3);
        this.t = Math.max(this.t, this.T);
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public void visitInsn(int i) {
        this.Y = this.r.b;
        this.r.putByte(i);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(i, 0, (ClassWriter) null, (Item) null);
            } else {
                int i2 = this.Q + Frame.a[i];
                if (i2 > this.R) {
                    this.R = i2;
                }
                this.Q = i2;
            }
            if ((i < 172 || i > 177) && i != 191) {
                return;
            }
            e();
        }
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public void visitIntInsn(int i, int i2) {
        this.Y = this.r.b;
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(i, i2, (ClassWriter) null, (Item) null);
            } else if (i != 188) {
                int i3 = this.Q + 1;
                if (i3 > this.R) {
                    this.R = i3;
                }
                this.Q = i3;
            }
        }
        if (i == 17) {
            this.r.b(i, i2);
        } else {
            this.r.a(i, i2);
        }
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public void visitVarInsn(int i, int i2) {
        this.Y = this.r.b;
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(i, i2, (ClassWriter) null, (Item) null);
            } else if (i == 169) {
                this.P.a |= 256;
                this.P.f = this.Q;
                e();
            } else {
                int i3 = this.Q + Frame.a[i];
                if (i3 > this.R) {
                    this.R = i3;
                }
                this.Q = i3;
            }
        }
        if (this.M != 2) {
            int i4 = (i == 22 || i == 24 || i == 55 || i == 57) ? i2 + 2 : i2 + 1;
            if (i4 > this.t) {
                this.t = i4;
            }
        }
        if (i2 < 4 && i != 169) {
            this.r.putByte(i < 54 ? 26 + ((i - 21) << 2) + i2 : 59 + ((i - 54) << 2) + i2);
        } else if (i2 >= 256) {
            this.r.putByte(196).b(i, i2);
        } else {
            this.r.a(i, i2);
        }
        if (i < 54 || this.M != 0 || this.A <= 0) {
            return;
        }
        visitLabel(new Label());
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public void visitTypeInsn(int i, String str) {
        this.Y = this.r.b;
        Item itemA = this.b.a(str);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(i, this.r.b, this.b, itemA);
            } else if (i == 187) {
                int i2 = this.Q + 1;
                if (i2 > this.R) {
                    this.R = i2;
                }
                this.Q = i2;
            }
        }
        this.r.b(i, itemA.a);
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public void visitFieldInsn(int i, String str, String str2, String str3) {
        int i2;
        this.Y = this.r.b;
        Item itemA = this.b.a(str, str2, str3);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(i, 0, this.b, itemA);
            } else {
                char cCharAt = str3.charAt(0);
                switch (i) {
                    case 178:
                        i2 = this.Q + ((cCharAt == 'D' || cCharAt == 'J') ? 2 : 1);
                        break;
                    case 179:
                        i2 = this.Q + ((cCharAt == 'D' || cCharAt == 'J') ? -2 : -1);
                        break;
                    case 180:
                        i2 = this.Q + ((cCharAt == 'D' || cCharAt == 'J') ? 1 : 0);
                        break;
                    default:
                        i2 = this.Q + ((cCharAt == 'D' || cCharAt == 'J') ? -3 : -2);
                        break;
                }
                if (i2 > this.R) {
                    this.R = i2;
                }
                this.Q = i2;
            }
        }
        this.r.b(i, itemA.a);
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public void visitMethodInsn(int i, String str, String str2, String str3, boolean z) {
        this.Y = this.r.b;
        Item itemA = this.b.a(str, str2, str3, z);
        int argumentsAndReturnSizes = itemA.c;
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(i, 0, this.b, itemA);
            } else {
                if (argumentsAndReturnSizes == 0) {
                    argumentsAndReturnSizes = Type.getArgumentsAndReturnSizes(str3);
                    itemA.c = argumentsAndReturnSizes;
                }
                int i2 = i == 184 ? (this.Q - (argumentsAndReturnSizes >> 2)) + (argumentsAndReturnSizes & 3) + 1 : (this.Q - (argumentsAndReturnSizes >> 2)) + (argumentsAndReturnSizes & 3);
                if (i2 > this.R) {
                    this.R = i2;
                }
                this.Q = i2;
            }
        }
        if (i != 185) {
            this.r.b(i, itemA.a);
            return;
        }
        if (argumentsAndReturnSizes == 0) {
            argumentsAndReturnSizes = Type.getArgumentsAndReturnSizes(str3);
            itemA.c = argumentsAndReturnSizes;
        }
        this.r.b(185, itemA.a).a(argumentsAndReturnSizes >> 2, 0);
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public void visitInvokeDynamicInsn(String str, String str2, Handle handle, Object... objArr) {
        this.Y = this.r.b;
        Item itemA = this.b.a(str, str2, handle, objArr);
        int argumentsAndReturnSizes = itemA.c;
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(186, 0, this.b, itemA);
            } else {
                if (argumentsAndReturnSizes == 0) {
                    argumentsAndReturnSizes = Type.getArgumentsAndReturnSizes(str2);
                    itemA.c = argumentsAndReturnSizes;
                }
                int i = (this.Q - (argumentsAndReturnSizes >> 2)) + (argumentsAndReturnSizes & 3) + 1;
                if (i > this.R) {
                    this.R = i;
                }
                this.Q = i;
            }
        }
        this.r.b(186, itemA.a);
        this.r.putShort(0);
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public void visitJumpInsn(int i, Label label) {
        this.Y = this.r.b;
        Label label2 = null;
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(i, 0, (ClassWriter) null, (Item) null);
                label.a().a |= 16;
                a(0, label);
                if (i != 167) {
                    label2 = new Label();
                }
            } else if (i == 168) {
                if ((label.a & 512) == 0) {
                    label.a |= 512;
                    this.L++;
                }
                this.P.a |= 128;
                a(this.Q + 1, label);
                label2 = new Label();
            } else {
                this.Q += Frame.a[i];
                a(this.Q, label);
            }
        }
        if ((label.a & 2) == 0 || label.c - this.r.b >= -32768) {
            this.r.putByte(i);
            label.a(this, this.r, this.r.b - 1, false);
        } else {
            if (i == 167) {
                this.r.putByte(200);
            } else if (i == 168) {
                this.r.putByte(201);
            } else {
                if (label2 != null) {
                    label2.a |= 16;
                }
                this.r.putByte(i <= 166 ? ((i + 1) ^ 1) - 1 : i ^ 1);
                this.r.putShort(8);
                this.r.putByte(200);
            }
            label.a(this, this.r, this.r.b - 1, true);
        }
        if (this.P != null) {
            if (label2 != null) {
                visitLabel(label2);
            }
            if (i == 167) {
                e();
            }
        }
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public void visitLabel(Label label) {
        this.K |= label.a(this, this.r.b, this.r.a);
        if ((label.a & 1) != 0) {
            return;
        }
        if (this.M != 0) {
            if (this.M == 1) {
                if (this.P != null) {
                    this.P.g = this.R;
                    a(this.Q, label);
                }
                this.P = label;
                this.Q = 0;
                this.R = 0;
                if (this.O != null) {
                    this.O.i = label;
                }
                this.O = label;
                return;
            }
            return;
        }
        if (this.P != null) {
            if (label.c == this.P.c) {
                this.P.a |= label.a & 16;
                label.h = this.P.h;
                return;
            }
            a(0, label);
        }
        this.P = label;
        if (label.h == null) {
            label.h = new Frame();
            label.h.b = label;
        }
        if (this.O != null) {
            if (label.c == this.O.c) {
                this.O.a |= label.a & 16;
                label.h = this.O.h;
                this.P = this.O;
                return;
            }
            this.O.i = label;
        }
        this.O = label;
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public void visitLdcInsn(Object obj) {
        this.Y = this.r.b;
        Item itemA = this.b.a(obj);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(18, 0, this.b, itemA);
            } else {
                int i = (itemA.b == 5 || itemA.b == 6) ? this.Q + 2 : this.Q + 1;
                if (i > this.R) {
                    this.R = i;
                }
                this.Q = i;
            }
        }
        int i2 = itemA.a;
        if (itemA.b == 5 || itemA.b == 6) {
            this.r.b(20, i2);
        } else if (i2 >= 256) {
            this.r.b(19, i2);
        } else {
            this.r.a(18, i2);
        }
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public void visitIincInsn(int i, int i2) {
        int i3;
        this.Y = this.r.b;
        if (this.P != null && this.M == 0) {
            this.P.h.a(132, i, (ClassWriter) null, (Item) null);
        }
        if (this.M != 2 && (i3 = i + 1) > this.t) {
            this.t = i3;
        }
        if (i > 255 || i2 > 127 || i2 < -128) {
            this.r.putByte(196).b(132, i).putShort(i2);
        } else {
            this.r.putByte(132).a(i, i2);
        }
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public void visitTableSwitchInsn(int i, int i2, Label label, Label... labelArr) {
        this.Y = this.r.b;
        int i3 = this.r.b;
        this.r.putByte(170);
        this.r.putByteArray(null, 0, (4 - (this.r.b % 4)) % 4);
        label.a(this, this.r, i3, true);
        this.r.putInt(i).putInt(i2);
        for (Label label2 : labelArr) {
            label2.a(this, this.r, i3, true);
        }
        a(label, labelArr);
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public void visitLookupSwitchInsn(Label label, int[] iArr, Label[] labelArr) {
        this.Y = this.r.b;
        int i = this.r.b;
        this.r.putByte(171);
        this.r.putByteArray(null, 0, (4 - (this.r.b % 4)) % 4);
        label.a(this, this.r, i, true);
        this.r.putInt(labelArr.length);
        for (int i2 = 0; i2 < labelArr.length; i2++) {
            this.r.putInt(iArr[i2]);
            labelArr[i2].a(this, this.r, i, true);
        }
        a(label, labelArr);
    }

    private void a(Label label, Label[] labelArr) {
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(171, 0, (ClassWriter) null, (Item) null);
                a(0, label);
                label.a().a |= 16;
                for (int i = 0; i < labelArr.length; i++) {
                    a(0, labelArr[i]);
                    labelArr[i].a().a |= 16;
                }
            } else {
                this.Q--;
                a(this.Q, label);
                for (Label label2 : labelArr) {
                    a(this.Q, label2);
                }
            }
            e();
        }
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public void visitMultiANewArrayInsn(String str, int i) {
        this.Y = this.r.b;
        Item itemA = this.b.a(str);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(197, i, this.b, itemA);
            } else {
                this.Q += 1 - i;
            }
        }
        this.r.b(197, itemA.a).putByte(i);
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitInsnAnnotation(int i, TypePath typePath, String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        AnnotationWriter.a((i & (-16776961)) | (this.Y << 8), typePath, byteVector);
        byteVector.putShort(this.b.newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this.b, true, byteVector, byteVector, byteVector.b - 2);
        if (z) {
            annotationWriter.g = this.W;
            this.W = annotationWriter;
        } else {
            annotationWriter.g = this.X;
            this.X = annotationWriter;
        }
        return annotationWriter;
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public void visitTryCatchBlock(Label label, Label label2, Label label3, String str) {
        this.A++;
        Handler handler = new Handler();
        handler.a = label;
        handler.b = label2;
        handler.c = label3;
        handler.d = str;
        handler.e = str != null ? this.b.newClass(str) : 0;
        if (this.C == null) {
            this.B = handler;
        } else {
            this.C.f = handler;
        }
        this.C = handler;
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitTryCatchAnnotation(int i, TypePath typePath, String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        AnnotationWriter.a(i, typePath, byteVector);
        byteVector.putShort(this.b.newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this.b, true, byteVector, byteVector, byteVector.b - 2);
        if (z) {
            annotationWriter.g = this.W;
            this.W = annotationWriter;
        } else {
            annotationWriter.g = this.X;
            this.X = annotationWriter;
        }
        return annotationWriter;
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public void visitLocalVariable(String str, String str2, String str3, Label label, Label label2, int i) {
        if (str3 != null) {
            if (this.G == null) {
                this.G = new ByteVector();
            }
            this.F++;
            this.G.putShort(label.c).putShort(label2.c - label.c).putShort(this.b.newUTF8(str)).putShort(this.b.newUTF8(str3)).putShort(i);
        }
        if (this.E == null) {
            this.E = new ByteVector();
        }
        this.D++;
        this.E.putShort(label.c).putShort(label2.c - label.c).putShort(this.b.newUTF8(str)).putShort(this.b.newUTF8(str2)).putShort(i);
        if (this.M != 2) {
            char cCharAt = str2.charAt(0);
            int i2 = i + ((cCharAt == 'J' || cCharAt == 'D') ? 2 : 1);
            if (i2 > this.t) {
                this.t = i2;
            }
        }
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitLocalVariableAnnotation(int i, TypePath typePath, Label[] labelArr, Label[] labelArr2, int[] iArr, String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        byteVector.putByte(i >>> 24).putShort(labelArr.length);
        for (int i2 = 0; i2 < labelArr.length; i2++) {
            byteVector.putShort(labelArr[i2].c).putShort(labelArr2[i2].c - labelArr[i2].c).putShort(iArr[i2]);
        }
        if (typePath == null) {
            byteVector.putByte(0);
        } else {
            byteVector.putByteArray(typePath.a, typePath.b, (typePath.a[typePath.b] * 2) + 1);
        }
        byteVector.putShort(this.b.newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this.b, true, byteVector, byteVector, byteVector.b - 2);
        if (z) {
            annotationWriter.g = this.W;
            this.W = annotationWriter;
        } else {
            annotationWriter.g = this.X;
            this.X = annotationWriter;
        }
        return annotationWriter;
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public void visitLineNumber(int i, Label label) {
        if (this.I == null) {
            this.I = new ByteVector();
        }
        this.H++;
        this.I.putShort(label.c);
        this.I.putShort(i);
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public void visitMaxs(int i, int i2) {
        if (this.K) {
            d();
        }
        if (this.M != 0) {
            if (this.M != 1) {
                this.s = i;
                this.t = i2;
                return;
            }
            Handler handler = this.B;
            while (true) {
                Handler handler2 = handler;
                if (handler2 == null) {
                    break;
                }
                Label label = handler2.c;
                Label label2 = handler2.b;
                for (Label label3 = handler2.a; label3 != label2; label3 = label3.i) {
                    Edge edge = new Edge();
                    edge.a = Integer.MAX_VALUE;
                    edge.b = label;
                    if ((label3.a & 128) == 0) {
                        edge.c = label3.j;
                        label3.j = edge;
                    } else {
                        edge.c = label3.j.c.c;
                        label3.j.c.c = edge;
                    }
                }
                handler = handler2.f;
            }
            if (this.L > 0) {
                int i3 = 0;
                this.N.b(null, 1L, this.L);
                Label label4 = this.N;
                while (true) {
                    Label label5 = label4;
                    if (label5 == null) {
                        break;
                    }
                    if ((label5.a & 128) != 0) {
                        Label label6 = label5.j.c.b;
                        if ((label6.a & 1024) == 0) {
                            i3++;
                            label6.b(null, ((i3 / 32) << 32) | (1 << (i3 % 32)), this.L);
                        }
                    }
                    label4 = label5.i;
                }
                Label label7 = this.N;
                while (true) {
                    Label label8 = label7;
                    if (label8 == null) {
                        break;
                    }
                    if ((label8.a & 128) != 0) {
                        Label label9 = this.N;
                        while (true) {
                            Label label10 = label9;
                            if (label10 == null) {
                                break;
                            }
                            label10.a &= -2049;
                            label9 = label10.i;
                        }
                        label8.j.c.b.b(label8, 0L, this.L);
                    }
                    label7 = label8.i;
                }
            }
            int i4 = 0;
            Label label11 = this.N;
            while (label11 != null) {
                Label label12 = label11;
                label11 = label11.k;
                int i5 = label12.f;
                int i6 = i5 + label12.g;
                if (i6 > i4) {
                    i4 = i6;
                }
                Edge edge2 = label12.j;
                if ((label12.a & 128) != 0) {
                    edge2 = edge2.c;
                }
                while (edge2 != null) {
                    Label label13 = edge2.b;
                    if ((label13.a & 8) == 0) {
                        label13.f = edge2.a == Integer.MAX_VALUE ? 1 : i5 + edge2.a;
                        label13.a |= 8;
                        label13.k = label11;
                        label11 = label13;
                    }
                    edge2 = edge2.c;
                }
            }
            this.s = Math.max(i, i4);
            return;
        }
        Handler handler3 = this.B;
        while (true) {
            Handler handler4 = handler3;
            if (handler4 == null) {
                break;
            }
            Label labelA = handler4.c.a();
            Label labelA2 = handler4.b.a();
            int iM1c = 24117248 | this.b.m1c(handler4.d == null ? "java/lang/Throwable" : handler4.d);
            labelA.a |= 16;
            for (Label labelA3 = handler4.a.a(); labelA3 != labelA2; labelA3 = labelA3.i) {
                Edge edge3 = new Edge();
                edge3.a = iM1c;
                edge3.b = labelA;
                edge3.c = labelA3.j;
                labelA3.j = edge3;
            }
            handler3 = handler4.f;
        }
        Frame frame = this.N.h;
        frame.a(this.b, this.c, Type.getArgumentTypes(this.f), this.t);
        b(frame);
        int iMax = 0;
        Label label14 = this.N;
        while (label14 != null) {
            Label label15 = label14;
            label14 = label14.k;
            label15.k = null;
            Frame frame2 = label15.h;
            if ((label15.a & 16) != 0) {
                label15.a |= 32;
            }
            label15.a |= 64;
            int length = frame2.d.length + label15.g;
            if (length > iMax) {
                iMax = length;
            }
            Edge edge4 = label15.j;
            while (true) {
                Edge edge5 = edge4;
                if (edge5 != null) {
                    Label labelA4 = edge5.b.a();
                    if (frame2.a(this.b, labelA4.h, edge5.a) && labelA4.k == null) {
                        labelA4.k = label14;
                        label14 = labelA4;
                    }
                    edge4 = edge5.c;
                }
            }
        }
        Label label16 = this.N;
        while (true) {
            Label label17 = label16;
            if (label17 == null) {
                break;
            }
            Frame frame3 = label17.h;
            if ((label17.a & 32) != 0) {
                b(frame3);
            }
            if ((label17.a & 64) == 0) {
                Label label18 = label17.i;
                int i7 = label17.c;
                int i8 = (label18 == null ? this.r.b : label18.c) - 1;
                if (i8 >= i7) {
                    iMax = Math.max(iMax, 1);
                    for (int i9 = i7; i9 < i8; i9++) {
                        this.r.a[i9] = 0;
                    }
                    this.r.a[i8] = -65;
                    this.z[a(i7, 0, 1)] = 24117248 | this.b.m1c("java/lang/Throwable");
                    b();
                    this.B = Handler.a(this.B, label17, label18);
                }
            }
            label16 = label17.i;
        }
        this.A = 0;
        for (Handler handler5 = this.B; handler5 != null; handler5 = handler5.f) {
            this.A++;
        }
        this.s = iMax;
    }

    @Override // aj.org.objectweb.asm.MethodVisitor
    public void visitEnd() {
    }

    private void a(int i, Label label) {
        Edge edge = new Edge();
        edge.a = i;
        edge.b = label;
        edge.c = this.P.j;
        this.P.j = edge;
    }

    private void e() {
        if (this.M == 0) {
            Label label = new Label();
            label.h = new Frame();
            label.h.b = label;
            label.a(this, this.r.b, this.r.a);
            this.O.i = label;
            this.O = label;
        } else {
            this.P.g = this.R;
        }
        this.P = null;
    }

    private void b(Frame frame) {
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int[] iArr = frame.c;
        int[] iArr2 = frame.d;
        int i4 = 0;
        while (i4 < iArr.length) {
            int i5 = iArr[i4];
            if (i5 == 16777216) {
                i++;
            } else {
                i2 += i + 1;
                i = 0;
            }
            if (i5 == 16777220 || i5 == 16777219) {
                i4++;
            }
            i4++;
        }
        int i6 = 0;
        while (i6 < iArr2.length) {
            int i7 = iArr2[i6];
            i3++;
            if (i7 == 16777220 || i7 == 16777219) {
                i6++;
            }
            i6++;
        }
        int iA = a(frame.b.c, i2, i3);
        int i8 = 0;
        while (i2 > 0) {
            int i9 = iArr[i8];
            int i10 = iA;
            iA++;
            this.z[i10] = i9;
            if (i9 == 16777220 || i9 == 16777219) {
                i8++;
            }
            i8++;
            i2--;
        }
        int i11 = 0;
        while (i11 < iArr2.length) {
            int i12 = iArr2[i11];
            int i13 = iA;
            iA++;
            this.z[i13] = i12;
            if (i12 == 16777220 || i12 == 16777219) {
                i11++;
            }
            i11++;
        }
        b();
    }

    private void f() {
        int iA = a(0, this.f.length() + 1, 0);
        if ((this.c & 8) == 0) {
            if ((this.c & 524288) == 0) {
                iA++;
                this.z[iA] = 24117248 | this.b.m1c(this.b.I);
            } else {
                iA++;
                this.z[iA] = 6;
            }
        }
        int i = 1;
        while (true) {
            int i2 = i;
            int i3 = i;
            i++;
            switch (this.f.charAt(i3)) {
                case 'B':
                case 'C':
                case 'I':
                case 'S':
                case 'Z':
                    int i4 = iA;
                    iA++;
                    this.z[i4] = 1;
                    break;
                case 'D':
                    int i5 = iA;
                    iA++;
                    this.z[i5] = 3;
                    break;
                case 'E':
                case 'G':
                case 'H':
                case 'K':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                default:
                    this.z[1] = iA - 3;
                    b();
                    return;
                case 'F':
                    int i6 = iA;
                    iA++;
                    this.z[i6] = 2;
                    break;
                case 'J':
                    int i7 = iA;
                    iA++;
                    this.z[i7] = 4;
                    break;
                case 'L':
                    while (this.f.charAt(i) != ';') {
                        i++;
                    }
                    int i8 = iA;
                    iA++;
                    int i9 = i;
                    i++;
                    this.z[i8] = 24117248 | this.b.m1c(this.f.substring(i2 + 1, i9));
                    break;
                case '[':
                    while (this.f.charAt(i) == '[') {
                        i++;
                    }
                    if (this.f.charAt(i) == 'L') {
                        do {
                            i++;
                        } while (this.f.charAt(i) != ';');
                    }
                    int i10 = iA;
                    iA++;
                    i++;
                    this.z[i10] = 24117248 | this.b.m1c(this.f.substring(i2, i));
                    break;
            }
        }
    }

    private int a(int i, int i2, int i3) {
        int i4 = 3 + i2 + i3;
        if (this.z == null || this.z.length < i4) {
            this.z = new int[i4];
        }
        this.z[0] = i;
        this.z[1] = i2;
        this.z[2] = i3;
        return 3;
    }

    private void b() {
        if (this.x != null) {
            if (this.v == null) {
                this.v = new ByteVector();
            }
            c();
            this.u++;
        }
        this.x = this.z;
        this.z = null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v26 */
    /* JADX WARN: Type inference failed for: r0v27 */
    /* JADX WARN: Type inference failed for: r0v28 */
    /* JADX WARN: Type inference failed for: r0v29 */
    /* JADX WARN: Type inference failed for: r0v78 */
    /* JADX WARN: Type inference failed for: r0v79 */
    /* JADX WARN: Type inference failed for: r0v80 */
    private void c() {
        int i = this.z[1];
        int i2 = this.z[2];
        if ((this.b.b & 65535) < 50) {
            this.v.putShort(this.z[0]).putShort(i);
            a(3, 3 + i);
            this.v.putShort(i2);
            a(3 + i, 3 + i + i2);
        }
        int i3 = this.x[1];
        boolean z = 255;
        int i4 = 0;
        int i5 = this.u == 0 ? this.z[0] : (this.z[0] - this.x[0]) - 1;
        if (i2 == 0) {
            i4 = i - i3;
            switch (i4) {
                case -3:
                case -2:
                case -1:
                    z = 248;
                    i3 = i;
                    break;
                case 0:
                    z = i5 < 64 ? 0 : 251;
                    break;
                case 1:
                case 2:
                case 3:
                    z = 252;
                    break;
            }
        } else if (i == i3 && i2 == 1) {
            z = i5 < 63 ? 64 : 247;
        }
        if (z != 255) {
            int i6 = 3;
            int i7 = 0;
            while (true) {
                if (i7 < i3) {
                    if (this.z[i6] != this.x[i6]) {
                        z = 255;
                    } else {
                        i6++;
                        i7++;
                    }
                }
            }
        }
        switch (z) {
            case false:
                this.v.putByte(i5);
                break;
            case true:
                this.v.putByte(64 + i5);
                a(3 + i, 4 + i);
                break;
            case true:
                this.v.putByte(247).putShort(i5);
                a(3 + i, 4 + i);
                break;
            case EscherProperties.GEOTEXT__STRETCHCHARHEIGHT /* 248 */:
                this.v.putByte(251 + i4).putShort(i5);
                break;
            case true:
                this.v.putByte(251).putShort(i5);
                break;
            case true:
                this.v.putByte(251 + i4).putShort(i5);
                a(3 + i3, 3 + i);
                break;
            default:
                this.v.putByte(255).putShort(i5).putShort(i);
                a(3, 3 + i);
                this.v.putShort(i2);
                a(3 + i, 3 + i + i2);
                break;
        }
    }

    private void a(int i, int i2) {
        for (int i3 = i; i3 < i2; i3++) {
            int i4 = this.z[i3];
            int i5 = i4 & (-268435456);
            if (i5 == 0) {
                int i6 = i4 & 1048575;
                switch (i4 & 267386880) {
                    case 24117248:
                        this.v.putByte(7).putShort(this.b.newClass(this.b.H[i6].g));
                        break;
                    case 25165824:
                        this.v.putByte(8).putShort(this.b.H[i6].c);
                        break;
                    default:
                        this.v.putByte(i6);
                        break;
                }
            } else {
                StringBuffer stringBuffer = new StringBuffer();
                int i7 = i5 >> 28;
                while (true) {
                    int i8 = i7;
                    i7--;
                    if (i8 > 0) {
                        stringBuffer.append('[');
                    } else {
                        if ((i4 & 267386880) != 24117248) {
                            switch (i4 & 15) {
                                case 1:
                                    stringBuffer.append('I');
                                    break;
                                case 2:
                                    stringBuffer.append('F');
                                    break;
                                case 3:
                                    stringBuffer.append('D');
                                    break;
                                case 4:
                                case 5:
                                case 6:
                                case 7:
                                case 8:
                                default:
                                    stringBuffer.append('J');
                                    break;
                                case 9:
                                    stringBuffer.append('Z');
                                    break;
                                case 10:
                                    stringBuffer.append('B');
                                    break;
                                case 11:
                                    stringBuffer.append('C');
                                    break;
                                case 12:
                                    stringBuffer.append('S');
                                    break;
                            }
                        } else {
                            stringBuffer.append('L');
                            stringBuffer.append(this.b.H[i4 & 1048575].g);
                            stringBuffer.append(';');
                        }
                        this.v.putByte(7).putShort(this.b.newClass(stringBuffer.toString()));
                    }
                }
            }
        }
    }

    private void a(Object obj) {
        if (obj instanceof String) {
            this.v.putByte(7).putShort(this.b.newClass((String) obj));
        } else if (obj instanceof Integer) {
            this.v.putByte(((Integer) obj).intValue());
        } else {
            this.v.putByte(8).putShort(((Label) obj).c);
        }
    }

    final int a() {
        if (this.h != 0) {
            return 6 + this.i;
        }
        int iA = 8;
        if (this.r.b > 0) {
            if (this.r.b > 65536) {
                throw new RuntimeException("Method code too large!");
            }
            this.b.newUTF8("Code");
            iA = 8 + 18 + this.r.b + (8 * this.A);
            if (this.E != null) {
                this.b.newUTF8(LocalVariableAttribute.tag);
                iA += 8 + this.E.b;
            }
            if (this.G != null) {
                this.b.newUTF8("LocalVariableTypeTable");
                iA += 8 + this.G.b;
            }
            if (this.I != null) {
                this.b.newUTF8(LineNumberAttribute.tag);
                iA += 8 + this.I.b;
            }
            if (this.v != null) {
                this.b.newUTF8((this.b.b & 65535) >= 50 ? StackMapTable.tag : StackMap.tag);
                iA += 8 + this.v.b;
            }
            if (this.W != null) {
                this.b.newUTF8(TypeAnnotationsAttribute.visibleTag);
                iA += 8 + this.W.a();
            }
            if (this.X != null) {
                this.b.newUTF8(TypeAnnotationsAttribute.invisibleTag);
                iA += 8 + this.X.a();
            }
            if (this.J != null) {
                iA += this.J.a(this.b, this.r.a, this.r.b, this.s, this.t);
            }
        }
        if (this.j > 0) {
            this.b.newUTF8(ExceptionsAttribute.tag);
            iA += 8 + (2 * this.j);
        }
        if ((this.c & 4096) != 0 && ((this.b.b & 65535) < 49 || (this.c & 262144) != 0)) {
            this.b.newUTF8(SyntheticAttribute.tag);
            iA += 6;
        }
        if ((this.c & 131072) != 0) {
            this.b.newUTF8(DeprecatedAttribute.tag);
            iA += 6;
        }
        if (this.g != null) {
            this.b.newUTF8(SignatureAttribute.tag);
            this.b.newUTF8(this.g);
            iA += 8;
        }
        if (this.$ != null) {
            this.b.newUTF8(MethodParametersAttribute.tag);
            iA += 7 + this.$.b;
        }
        if (this.l != null) {
            this.b.newUTF8(AnnotationDefaultAttribute.tag);
            iA += 6 + this.l.b;
        }
        if (this.m != null) {
            this.b.newUTF8(AnnotationsAttribute.visibleTag);
            iA += 8 + this.m.a();
        }
        if (this.n != null) {
            this.b.newUTF8(AnnotationsAttribute.invisibleTag);
            iA += 8 + this.n.a();
        }
        if (this.U != null) {
            this.b.newUTF8(TypeAnnotationsAttribute.visibleTag);
            iA += 8 + this.U.a();
        }
        if (this.V != null) {
            this.b.newUTF8(TypeAnnotationsAttribute.invisibleTag);
            iA += 8 + this.V.a();
        }
        if (this.o != null) {
            this.b.newUTF8(ParameterAnnotationsAttribute.visibleTag);
            iA += 7 + (2 * (this.o.length - this.S));
            for (int length = this.o.length - 1; length >= this.S; length--) {
                iA += this.o[length] == null ? 0 : this.o[length].a();
            }
        }
        if (this.p != null) {
            this.b.newUTF8(ParameterAnnotationsAttribute.invisibleTag);
            iA += 7 + (2 * (this.p.length - this.S));
            for (int length2 = this.p.length - 1; length2 >= this.S; length2--) {
                iA += this.p[length2] == null ? 0 : this.p[length2].a();
            }
        }
        if (this.q != null) {
            iA += this.q.a(this.b, null, 0, -1, -1);
        }
        return iA;
    }

    final void a(ByteVector byteVector) {
        byteVector.putShort(this.c & ((917504 | ((this.c & 262144) / 64)) ^ (-1))).putShort(this.d).putShort(this.e);
        if (this.h != 0) {
            byteVector.putByteArray(this.b.M.b, this.h, this.i);
            return;
        }
        int iA = this.r.b > 0 ? 0 + 1 : 0;
        if (this.j > 0) {
            iA++;
        }
        if ((this.c & 4096) != 0 && ((this.b.b & 65535) < 49 || (this.c & 262144) != 0)) {
            iA++;
        }
        if ((this.c & 131072) != 0) {
            iA++;
        }
        if (this.g != null) {
            iA++;
        }
        if (this.$ != null) {
            iA++;
        }
        if (this.l != null) {
            iA++;
        }
        if (this.m != null) {
            iA++;
        }
        if (this.n != null) {
            iA++;
        }
        if (this.U != null) {
            iA++;
        }
        if (this.V != null) {
            iA++;
        }
        if (this.o != null) {
            iA++;
        }
        if (this.p != null) {
            iA++;
        }
        if (this.q != null) {
            iA += this.q.a();
        }
        byteVector.putShort(iA);
        if (this.r.b > 0) {
            int iA2 = 12 + this.r.b + (8 * this.A);
            if (this.E != null) {
                iA2 += 8 + this.E.b;
            }
            if (this.G != null) {
                iA2 += 8 + this.G.b;
            }
            if (this.I != null) {
                iA2 += 8 + this.I.b;
            }
            if (this.v != null) {
                iA2 += 8 + this.v.b;
            }
            if (this.W != null) {
                iA2 += 8 + this.W.a();
            }
            if (this.X != null) {
                iA2 += 8 + this.X.a();
            }
            if (this.J != null) {
                iA2 += this.J.a(this.b, this.r.a, this.r.b, this.s, this.t);
            }
            byteVector.putShort(this.b.newUTF8("Code")).putInt(iA2);
            byteVector.putShort(this.s).putShort(this.t);
            byteVector.putInt(this.r.b).putByteArray(this.r.a, 0, this.r.b);
            byteVector.putShort(this.A);
            if (this.A > 0) {
                Handler handler = this.B;
                while (true) {
                    Handler handler2 = handler;
                    if (handler2 == null) {
                        break;
                    }
                    byteVector.putShort(handler2.a.c).putShort(handler2.b.c).putShort(handler2.c.c).putShort(handler2.e);
                    handler = handler2.f;
                }
            }
            int iA3 = this.E != null ? 0 + 1 : 0;
            if (this.G != null) {
                iA3++;
            }
            if (this.I != null) {
                iA3++;
            }
            if (this.v != null) {
                iA3++;
            }
            if (this.W != null) {
                iA3++;
            }
            if (this.X != null) {
                iA3++;
            }
            if (this.J != null) {
                iA3 += this.J.a();
            }
            byteVector.putShort(iA3);
            if (this.E != null) {
                byteVector.putShort(this.b.newUTF8(LocalVariableAttribute.tag));
                byteVector.putInt(this.E.b + 2).putShort(this.D);
                byteVector.putByteArray(this.E.a, 0, this.E.b);
            }
            if (this.G != null) {
                byteVector.putShort(this.b.newUTF8("LocalVariableTypeTable"));
                byteVector.putInt(this.G.b + 2).putShort(this.F);
                byteVector.putByteArray(this.G.a, 0, this.G.b);
            }
            if (this.I != null) {
                byteVector.putShort(this.b.newUTF8(LineNumberAttribute.tag));
                byteVector.putInt(this.I.b + 2).putShort(this.H);
                byteVector.putByteArray(this.I.a, 0, this.I.b);
            }
            if (this.v != null) {
                byteVector.putShort(this.b.newUTF8((this.b.b & 65535) >= 50 ? StackMapTable.tag : StackMap.tag));
                byteVector.putInt(this.v.b + 2).putShort(this.u);
                byteVector.putByteArray(this.v.a, 0, this.v.b);
            }
            if (this.W != null) {
                byteVector.putShort(this.b.newUTF8(TypeAnnotationsAttribute.visibleTag));
                this.W.a(byteVector);
            }
            if (this.X != null) {
                byteVector.putShort(this.b.newUTF8(TypeAnnotationsAttribute.invisibleTag));
                this.X.a(byteVector);
            }
            if (this.J != null) {
                this.J.a(this.b, this.r.a, this.r.b, this.t, this.s, byteVector);
            }
        }
        if (this.j > 0) {
            byteVector.putShort(this.b.newUTF8(ExceptionsAttribute.tag)).putInt((2 * this.j) + 2);
            byteVector.putShort(this.j);
            for (int i = 0; i < this.j; i++) {
                byteVector.putShort(this.k[i]);
            }
        }
        if ((this.c & 4096) != 0 && ((this.b.b & 65535) < 49 || (this.c & 262144) != 0)) {
            byteVector.putShort(this.b.newUTF8(SyntheticAttribute.tag)).putInt(0);
        }
        if ((this.c & 131072) != 0) {
            byteVector.putShort(this.b.newUTF8(DeprecatedAttribute.tag)).putInt(0);
        }
        if (this.g != null) {
            byteVector.putShort(this.b.newUTF8(SignatureAttribute.tag)).putInt(2).putShort(this.b.newUTF8(this.g));
        }
        if (this.$ != null) {
            byteVector.putShort(this.b.newUTF8(MethodParametersAttribute.tag));
            byteVector.putInt(this.$.b + 1).putByte(this.Z);
            byteVector.putByteArray(this.$.a, 0, this.$.b);
        }
        if (this.l != null) {
            byteVector.putShort(this.b.newUTF8(AnnotationDefaultAttribute.tag));
            byteVector.putInt(this.l.b);
            byteVector.putByteArray(this.l.a, 0, this.l.b);
        }
        if (this.m != null) {
            byteVector.putShort(this.b.newUTF8(AnnotationsAttribute.visibleTag));
            this.m.a(byteVector);
        }
        if (this.n != null) {
            byteVector.putShort(this.b.newUTF8(AnnotationsAttribute.invisibleTag));
            this.n.a(byteVector);
        }
        if (this.U != null) {
            byteVector.putShort(this.b.newUTF8(TypeAnnotationsAttribute.visibleTag));
            this.U.a(byteVector);
        }
        if (this.V != null) {
            byteVector.putShort(this.b.newUTF8(TypeAnnotationsAttribute.invisibleTag));
            this.V.a(byteVector);
        }
        if (this.o != null) {
            byteVector.putShort(this.b.newUTF8(ParameterAnnotationsAttribute.visibleTag));
            AnnotationWriter.a(this.o, this.S, byteVector);
        }
        if (this.p != null) {
            byteVector.putShort(this.b.newUTF8(ParameterAnnotationsAttribute.invisibleTag));
            AnnotationWriter.a(this.p, this.S, byteVector);
        }
        if (this.q != null) {
            this.q.a(this.b, null, 0, -1, -1, byteVector);
        }
    }

    private void d() {
        int iB;
        int iB2;
        byte[] bArr = this.r.a;
        int[] iArr = new int[0];
        int[] iArr2 = new int[0];
        boolean[] zArr = new boolean[this.r.b];
        int i = 3;
        do {
            if (i == 3) {
                i = 2;
            }
            int iA = 0;
            while (iA < bArr.length) {
                int i2 = bArr[iA] & 255;
                int i3 = 0;
                switch (ClassWriter.a[i2]) {
                    case 0:
                    case 4:
                        iA++;
                        break;
                    case 1:
                    case 3:
                    case 11:
                        iA += 2;
                        break;
                    case 2:
                    case 5:
                    case 6:
                    case 12:
                    case 13:
                        iA += 3;
                        break;
                    case 7:
                    case 8:
                        iA += 5;
                        break;
                    case 9:
                        if (i2 > 201) {
                            i2 = i2 < 218 ? i2 - 49 : i2 - 20;
                            iB2 = iA + c(bArr, iA + 1);
                        } else {
                            iB2 = iA + b(bArr, iA + 1);
                        }
                        int iA2 = a(iArr, iArr2, iA, iB2);
                        if ((iA2 < -32768 || iA2 > 32767) && !zArr[iA]) {
                            i3 = (i2 == 167 || i2 == 168) ? 2 : 5;
                            zArr[iA] = true;
                        }
                        iA += 3;
                        break;
                    case 10:
                        iA += 5;
                        break;
                    case 14:
                        if (i == 1) {
                            i3 = -(a(iArr, iArr2, 0, iA) & 3);
                        } else if (!zArr[iA]) {
                            i3 = iA & 3;
                            zArr[iA] = true;
                        }
                        int i4 = (iA + 4) - (iA & 3);
                        iA = i4 + (4 * ((a(bArr, i4 + 8) - a(bArr, i4 + 4)) + 1)) + 12;
                        break;
                    case 15:
                        if (i == 1) {
                            i3 = -(a(iArr, iArr2, 0, iA) & 3);
                        } else if (!zArr[iA]) {
                            i3 = iA & 3;
                            zArr[iA] = true;
                        }
                        int i5 = (iA + 4) - (iA & 3);
                        iA = i5 + (8 * a(bArr, i5 + 4)) + 8;
                        break;
                    case 16:
                    default:
                        iA += 4;
                        break;
                    case 17:
                        if ((bArr[iA + 1] & 255) == 132) {
                            iA += 6;
                            break;
                        } else {
                            iA += 4;
                            break;
                        }
                }
                if (i3 != 0) {
                    int[] iArr3 = new int[iArr.length + 1];
                    int[] iArr4 = new int[iArr2.length + 1];
                    System.arraycopy(iArr, 0, iArr3, 0, iArr.length);
                    System.arraycopy(iArr2, 0, iArr4, 0, iArr2.length);
                    iArr3[iArr.length] = iA;
                    iArr4[iArr2.length] = i3;
                    iArr = iArr3;
                    iArr2 = iArr4;
                    if (i3 > 0) {
                        i = 3;
                    }
                }
            }
            if (i < 3) {
                i--;
            }
        } while (i != 0);
        ByteVector byteVector = new ByteVector(this.r.b);
        int i6 = 0;
        while (i6 < this.r.b) {
            int i7 = bArr[i6] & 255;
            switch (ClassWriter.a[i7]) {
                case 0:
                case 4:
                    byteVector.putByte(i7);
                    i6++;
                    break;
                case 1:
                case 3:
                case 11:
                    byteVector.putByteArray(bArr, i6, 2);
                    i6 += 2;
                    break;
                case 2:
                case 5:
                case 6:
                case 12:
                case 13:
                    byteVector.putByteArray(bArr, i6, 3);
                    i6 += 3;
                    break;
                case 7:
                case 8:
                    byteVector.putByteArray(bArr, i6, 5);
                    i6 += 5;
                    break;
                case 9:
                    if (i7 > 201) {
                        i7 = i7 < 218 ? i7 - 49 : i7 - 20;
                        iB = i6 + c(bArr, i6 + 1);
                    } else {
                        iB = i6 + b(bArr, i6 + 1);
                    }
                    int iA3 = a(iArr, iArr2, i6, iB);
                    if (zArr[i6]) {
                        if (i7 == 167) {
                            byteVector.putByte(200);
                        } else if (i7 == 168) {
                            byteVector.putByte(201);
                        } else {
                            byteVector.putByte(i7 <= 166 ? ((i7 + 1) ^ 1) - 1 : i7 ^ 1);
                            byteVector.putShort(8);
                            byteVector.putByte(200);
                            iA3 -= 3;
                        }
                        byteVector.putInt(iA3);
                    } else {
                        byteVector.putByte(i7);
                        byteVector.putShort(iA3);
                    }
                    i6 += 3;
                    break;
                case 10:
                    int iA4 = a(iArr, iArr2, i6, i6 + a(bArr, i6 + 1));
                    byteVector.putByte(i7);
                    byteVector.putInt(iA4);
                    i6 += 5;
                    break;
                case 14:
                    int i8 = i6;
                    int i9 = (i6 + 4) - (i8 & 3);
                    byteVector.putByte(170);
                    byteVector.putByteArray(null, 0, (4 - (byteVector.b % 4)) % 4);
                    int i10 = i9 + 4;
                    byteVector.putInt(a(iArr, iArr2, i8, i8 + a(bArr, i9)));
                    int iA5 = a(bArr, i10);
                    int i11 = i10 + 4;
                    byteVector.putInt(iA5);
                    i6 = i11 + 4;
                    byteVector.putInt(a(bArr, i6 - 4));
                    for (int iA6 = (a(bArr, i11) - iA5) + 1; iA6 > 0; iA6--) {
                        int iA7 = i8 + a(bArr, i6);
                        i6 += 4;
                        byteVector.putInt(a(iArr, iArr2, i8, iA7));
                    }
                    break;
                case 15:
                    int i12 = i6;
                    int i13 = (i6 + 4) - (i12 & 3);
                    byteVector.putByte(171);
                    byteVector.putByteArray(null, 0, (4 - (byteVector.b % 4)) % 4);
                    int i14 = i13 + 4;
                    byteVector.putInt(a(iArr, iArr2, i12, i12 + a(bArr, i13)));
                    int iA8 = a(bArr, i14);
                    i6 = i14 + 4;
                    byteVector.putInt(iA8);
                    while (iA8 > 0) {
                        byteVector.putInt(a(bArr, i6));
                        int i15 = i6 + 4;
                        int iA9 = i12 + a(bArr, i15);
                        i6 = i15 + 4;
                        byteVector.putInt(a(iArr, iArr2, i12, iA9));
                        iA8--;
                    }
                    break;
                case 16:
                default:
                    byteVector.putByteArray(bArr, i6, 4);
                    i6 += 4;
                    break;
                case 17:
                    if ((bArr[i6 + 1] & 255) == 132) {
                        byteVector.putByteArray(bArr, i6, 6);
                        i6 += 6;
                        break;
                    } else {
                        byteVector.putByteArray(bArr, i6, 4);
                        i6 += 4;
                        break;
                    }
            }
        }
        if (this.M == 0) {
            Label label = this.N;
            while (true) {
                Label label2 = label;
                if (label2 != null) {
                    int i16 = label2.c - 3;
                    if (i16 >= 0 && zArr[i16]) {
                        label2.a |= 16;
                    }
                    a(iArr, iArr2, label2);
                    label = label2.i;
                } else if (this.b.H != null) {
                    for (int i17 = 0; i17 < this.b.H.length; i17++) {
                        Item item = this.b.H[i17];
                        if (item != null && item.b == 31) {
                            item.c = a(iArr, iArr2, 0, item.c);
                        }
                    }
                }
            }
        } else if (this.u > 0) {
            this.b.L = true;
        }
        Handler handler = this.B;
        while (true) {
            Handler handler2 = handler;
            if (handler2 != null) {
                a(iArr, iArr2, handler2.a);
                a(iArr, iArr2, handler2.b);
                a(iArr, iArr2, handler2.c);
                handler = handler2.f;
            } else {
                int i18 = 0;
                while (i18 < 2) {
                    ByteVector byteVector2 = i18 == 0 ? this.E : this.G;
                    if (byteVector2 != null) {
                        byte[] bArr2 = byteVector2.a;
                        for (int i19 = 0; i19 < byteVector2.b; i19 += 10) {
                            int iC = c(bArr2, i19);
                            int iA10 = a(iArr, iArr2, 0, iC);
                            a(bArr2, i19, iA10);
                            a(bArr2, i19 + 2, a(iArr, iArr2, 0, iC + c(bArr2, i19 + 2)) - iA10);
                        }
                    }
                    i18++;
                }
                if (this.I != null) {
                    byte[] bArr3 = this.I.a;
                    for (int i20 = 0; i20 < this.I.b; i20 += 4) {
                        a(bArr3, i20, a(iArr, iArr2, 0, c(bArr3, i20)));
                    }
                }
                Attribute attribute = this.J;
                while (true) {
                    Attribute attribute2 = attribute;
                    if (attribute2 == null) {
                        this.r = byteVector;
                        return;
                    }
                    Label[] labels = attribute2.getLabels();
                    if (labels != null) {
                        for (int length = labels.length - 1; length >= 0; length--) {
                            a(iArr, iArr2, labels[length]);
                        }
                    }
                    attribute = attribute2.a;
                }
            }
        }
    }

    static int c(byte[] bArr, int i) {
        return ((bArr[i] & 255) << 8) | (bArr[i + 1] & 255);
    }

    static short b(byte[] bArr, int i) {
        return (short) (((bArr[i] & 255) << 8) | (bArr[i + 1] & 255));
    }

    static int a(byte[] bArr, int i) {
        return ((bArr[i] & 255) << 24) | ((bArr[i + 1] & 255) << 16) | ((bArr[i + 2] & 255) << 8) | (bArr[i + 3] & 255);
    }

    static void a(byte[] bArr, int i, int i2) {
        bArr[i] = (byte) (i2 >>> 8);
        bArr[i + 1] = (byte) i2;
    }

    static int a(int[] iArr, int[] iArr2, int i, int i2) {
        int i3 = i2 - i;
        for (int i4 = 0; i4 < iArr.length; i4++) {
            if (i < iArr[i4] && iArr[i4] <= i2) {
                i3 += iArr2[i4];
            } else if (i2 < iArr[i4] && iArr[i4] <= i) {
                i3 -= iArr2[i4];
            }
        }
        return i3;
    }

    static void a(int[] iArr, int[] iArr2, Label label) {
        if ((label.a & 4) == 0) {
            label.c = a(iArr, iArr2, 0, label.c);
            label.a |= 4;
        }
    }
}
