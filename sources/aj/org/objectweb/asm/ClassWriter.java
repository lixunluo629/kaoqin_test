package aj.org.objectweb.asm;

import org.apache.ibatis.javassist.bytecode.AnnotationsAttribute;
import org.apache.ibatis.javassist.bytecode.BootstrapMethodsAttribute;
import org.apache.ibatis.javassist.bytecode.DeprecatedAttribute;
import org.apache.ibatis.javassist.bytecode.EnclosingMethodAttribute;
import org.apache.ibatis.javassist.bytecode.InnerClassesAttribute;
import org.apache.ibatis.javassist.bytecode.SignatureAttribute;
import org.apache.ibatis.javassist.bytecode.SourceFileAttribute;
import org.apache.ibatis.javassist.bytecode.SyntheticAttribute;
import org.apache.ibatis.javassist.bytecode.TypeAnnotationsAttribute;

/* loaded from: aspectjweaver-1.8.14.jar:aj/org/objectweb/asm/ClassWriter.class */
public class ClassWriter extends ClassVisitor {
    public static final int COMPUTE_MAXS = 1;
    public static final int COMPUTE_FRAMES = 2;
    static final byte[] a;
    ClassReader M;
    int b;
    int c;
    final ByteVector d;
    Item[] e;
    int f;
    final Item g;
    final Item h;
    final Item i;
    final Item j;
    Item[] H;
    private short G;
    private int k;
    private int l;
    String I;
    private int m;
    private int n;
    private int o;
    private int[] p;
    private int q;
    private ByteVector r;
    private int s;
    private int t;
    private AnnotationWriter u;
    private AnnotationWriter v;
    private AnnotationWriter N;
    private AnnotationWriter O;
    private Attribute w;
    private int x;
    private ByteVector y;
    int z;
    ByteVector A;
    FieldWriter B;
    FieldWriter C;
    MethodWriter D;
    MethodWriter E;
    private boolean K;
    private boolean J;
    boolean L;

    public ClassWriter(int i) {
        super(327680);
        this.c = 1;
        this.d = new ByteVector();
        this.e = new Item[256];
        this.f = (int) (0.75d * this.e.length);
        this.g = new Item();
        this.h = new Item();
        this.i = new Item();
        this.j = new Item();
        this.K = (i & 1) != 0;
        this.J = (i & 2) != 0;
    }

    public ClassWriter(ClassReader classReader, int i) {
        this(i);
        classReader.a(this);
        this.M = classReader;
    }

    @Override // aj.org.objectweb.asm.ClassVisitor
    public final void visit(int i, int i2, String str, String str2, String str3, String[] strArr) {
        this.b = i;
        this.k = i2;
        this.l = newClass(str);
        this.I = str;
        if (str2 != null) {
            this.m = newUTF8(str2);
        }
        this.n = str3 == null ? 0 : newClass(str3);
        if (strArr == null || strArr.length <= 0) {
            return;
        }
        this.o = strArr.length;
        this.p = new int[this.o];
        for (int i3 = 0; i3 < this.o; i3++) {
            this.p[i3] = newClass(strArr[i3]);
        }
    }

    @Override // aj.org.objectweb.asm.ClassVisitor
    public final void visitSource(String str, String str2) {
        if (str != null) {
            this.q = newUTF8(str);
        }
        if (str2 != null) {
            this.r = new ByteVector().c(str2, 0, Integer.MAX_VALUE);
        }
    }

    @Override // aj.org.objectweb.asm.ClassVisitor
    public final void visitOuterClass(String str, String str2, String str3) {
        this.s = newClass(str);
        if (str2 == null || str3 == null) {
            return;
        }
        this.t = newNameType(str2, str3);
    }

    @Override // aj.org.objectweb.asm.ClassVisitor
    public final AnnotationVisitor visitAnnotation(String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        byteVector.putShort(newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this, true, byteVector, byteVector, 2);
        if (z) {
            annotationWriter.g = this.u;
            this.u = annotationWriter;
        } else {
            annotationWriter.g = this.v;
            this.v = annotationWriter;
        }
        return annotationWriter;
    }

    @Override // aj.org.objectweb.asm.ClassVisitor
    public final AnnotationVisitor visitTypeAnnotation(int i, TypePath typePath, String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        AnnotationWriter.a(i, typePath, byteVector);
        byteVector.putShort(newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this, true, byteVector, byteVector, byteVector.b - 2);
        if (z) {
            annotationWriter.g = this.N;
            this.N = annotationWriter;
        } else {
            annotationWriter.g = this.O;
            this.O = annotationWriter;
        }
        return annotationWriter;
    }

    @Override // aj.org.objectweb.asm.ClassVisitor
    public final void visitAttribute(Attribute attribute) {
        attribute.a = this.w;
        this.w = attribute;
    }

    @Override // aj.org.objectweb.asm.ClassVisitor
    public final void visitInnerClass(String str, String str2, String str3, int i) {
        if (this.y == null) {
            this.y = new ByteVector();
        }
        Item itemA = a(str);
        if (itemA.c == 0) {
            this.x++;
            this.y.putShort(itemA.a);
            this.y.putShort(str2 == null ? 0 : newClass(str2));
            this.y.putShort(str3 == null ? 0 : newUTF8(str3));
            this.y.putShort(i);
            itemA.c = this.x;
        }
    }

    @Override // aj.org.objectweb.asm.ClassVisitor
    public final FieldVisitor visitField(int i, String str, String str2, String str3, Object obj) {
        return new FieldWriter(this, i, str, str2, str3, obj);
    }

    @Override // aj.org.objectweb.asm.ClassVisitor
    public final MethodVisitor visitMethod(int i, String str, String str2, String str3, String[] strArr) {
        return new MethodWriter(this, i, str, str2, str3, strArr, this.K, this.J);
    }

    @Override // aj.org.objectweb.asm.ClassVisitor
    public final void visitEnd() {
    }

    public byte[] toByteArray() {
        if (this.c > 65535) {
            throw new RuntimeException("Class file too large!");
        }
        int iA = 24 + (2 * this.o);
        int i = 0;
        FieldWriter fieldWriter = this.B;
        while (true) {
            FieldWriter fieldWriter2 = fieldWriter;
            if (fieldWriter2 == null) {
                break;
            }
            i++;
            iA += fieldWriter2.a();
            fieldWriter = (FieldWriter) fieldWriter2.fv;
        }
        int i2 = 0;
        MethodWriter methodWriter = this.D;
        while (true) {
            MethodWriter methodWriter2 = methodWriter;
            if (methodWriter2 == null) {
                break;
            }
            i2++;
            iA += methodWriter2.a();
            methodWriter = (MethodWriter) methodWriter2.mv;
        }
        int iA2 = 0;
        if (this.A != null) {
            iA2 = 0 + 1;
            iA += 8 + this.A.b;
            newUTF8(BootstrapMethodsAttribute.tag);
        }
        if (this.m != 0) {
            iA2++;
            iA += 8;
            newUTF8(SignatureAttribute.tag);
        }
        if (this.q != 0) {
            iA2++;
            iA += 8;
            newUTF8(SourceFileAttribute.tag);
        }
        if (this.r != null) {
            iA2++;
            iA += this.r.b + 6;
            newUTF8("SourceDebugExtension");
        }
        if (this.s != 0) {
            iA2++;
            iA += 10;
            newUTF8(EnclosingMethodAttribute.tag);
        }
        if ((this.k & 131072) != 0) {
            iA2++;
            iA += 6;
            newUTF8(DeprecatedAttribute.tag);
        }
        if ((this.k & 4096) != 0 && ((this.b & 65535) < 49 || (this.k & 262144) != 0)) {
            iA2++;
            iA += 6;
            newUTF8(SyntheticAttribute.tag);
        }
        if (this.y != null) {
            iA2++;
            iA += 8 + this.y.b;
            newUTF8(InnerClassesAttribute.tag);
        }
        if (this.u != null) {
            iA2++;
            iA += 8 + this.u.a();
            newUTF8(AnnotationsAttribute.visibleTag);
        }
        if (this.v != null) {
            iA2++;
            iA += 8 + this.v.a();
            newUTF8(AnnotationsAttribute.invisibleTag);
        }
        if (this.N != null) {
            iA2++;
            iA += 8 + this.N.a();
            newUTF8(TypeAnnotationsAttribute.visibleTag);
        }
        if (this.O != null) {
            iA2++;
            iA += 8 + this.O.a();
            newUTF8(TypeAnnotationsAttribute.invisibleTag);
        }
        if (this.w != null) {
            iA2 += this.w.a();
            iA += this.w.a(this, null, 0, -1, -1);
        }
        ByteVector byteVector = new ByteVector(iA + this.d.b);
        byteVector.putInt(-889275714).putInt(this.b);
        byteVector.putShort(this.c).putByteArray(this.d.a, 0, this.d.b);
        byteVector.putShort(this.k & ((393216 | ((this.k & 262144) / 64)) ^ (-1))).putShort(this.l).putShort(this.n);
        byteVector.putShort(this.o);
        for (int i3 = 0; i3 < this.o; i3++) {
            byteVector.putShort(this.p[i3]);
        }
        byteVector.putShort(i);
        FieldWriter fieldWriter3 = this.B;
        while (true) {
            FieldWriter fieldWriter4 = fieldWriter3;
            if (fieldWriter4 == null) {
                break;
            }
            fieldWriter4.a(byteVector);
            fieldWriter3 = (FieldWriter) fieldWriter4.fv;
        }
        byteVector.putShort(i2);
        MethodWriter methodWriter3 = this.D;
        while (true) {
            MethodWriter methodWriter4 = methodWriter3;
            if (methodWriter4 == null) {
                break;
            }
            methodWriter4.a(byteVector);
            methodWriter3 = (MethodWriter) methodWriter4.mv;
        }
        byteVector.putShort(iA2);
        if (this.A != null) {
            byteVector.putShort(newUTF8(BootstrapMethodsAttribute.tag));
            byteVector.putInt(this.A.b + 2).putShort(this.z);
            byteVector.putByteArray(this.A.a, 0, this.A.b);
        }
        if (this.m != 0) {
            byteVector.putShort(newUTF8(SignatureAttribute.tag)).putInt(2).putShort(this.m);
        }
        if (this.q != 0) {
            byteVector.putShort(newUTF8(SourceFileAttribute.tag)).putInt(2).putShort(this.q);
        }
        if (this.r != null) {
            int i4 = this.r.b;
            byteVector.putShort(newUTF8("SourceDebugExtension")).putInt(i4);
            byteVector.putByteArray(this.r.a, 0, i4);
        }
        if (this.s != 0) {
            byteVector.putShort(newUTF8(EnclosingMethodAttribute.tag)).putInt(4);
            byteVector.putShort(this.s).putShort(this.t);
        }
        if ((this.k & 131072) != 0) {
            byteVector.putShort(newUTF8(DeprecatedAttribute.tag)).putInt(0);
        }
        if ((this.k & 4096) != 0 && ((this.b & 65535) < 49 || (this.k & 262144) != 0)) {
            byteVector.putShort(newUTF8(SyntheticAttribute.tag)).putInt(0);
        }
        if (this.y != null) {
            byteVector.putShort(newUTF8(InnerClassesAttribute.tag));
            byteVector.putInt(this.y.b + 2).putShort(this.x);
            byteVector.putByteArray(this.y.a, 0, this.y.b);
        }
        if (this.u != null) {
            byteVector.putShort(newUTF8(AnnotationsAttribute.visibleTag));
            this.u.a(byteVector);
        }
        if (this.v != null) {
            byteVector.putShort(newUTF8(AnnotationsAttribute.invisibleTag));
            this.v.a(byteVector);
        }
        if (this.N != null) {
            byteVector.putShort(newUTF8(TypeAnnotationsAttribute.visibleTag));
            this.N.a(byteVector);
        }
        if (this.O != null) {
            byteVector.putShort(newUTF8(TypeAnnotationsAttribute.invisibleTag));
            this.O.a(byteVector);
        }
        if (this.w != null) {
            this.w.a(this, null, 0, -1, -1, byteVector);
        }
        if (!this.L) {
            return byteVector.a;
        }
        this.u = null;
        this.v = null;
        this.w = null;
        this.x = 0;
        this.y = null;
        this.z = 0;
        this.A = null;
        this.B = null;
        this.C = null;
        this.D = null;
        this.E = null;
        this.K = false;
        this.J = true;
        this.L = false;
        new ClassReader(byteVector.a).accept(this, 4);
        return toByteArray();
    }

    Item a(Object obj) {
        if (obj instanceof Integer) {
            return a(((Integer) obj).intValue());
        }
        if (obj instanceof Byte) {
            return a(((Byte) obj).intValue());
        }
        if (obj instanceof Character) {
            return a((int) ((Character) obj).charValue());
        }
        if (obj instanceof Short) {
            return a(((Short) obj).intValue());
        }
        if (obj instanceof Boolean) {
            return a(((Boolean) obj).booleanValue() ? 1 : 0);
        }
        if (obj instanceof Float) {
            return a(((Float) obj).floatValue());
        }
        if (obj instanceof Long) {
            return a(((Long) obj).longValue());
        }
        if (obj instanceof Double) {
            return a(((Double) obj).doubleValue());
        }
        if (obj instanceof String) {
            return b((String) obj);
        }
        if (obj instanceof Type) {
            Type type = (Type) obj;
            int sort = type.getSort();
            return sort == 10 ? a(type.getInternalName()) : sort == 11 ? c(type.getDescriptor()) : a(type.getDescriptor());
        }
        if (!(obj instanceof Handle)) {
            throw new IllegalArgumentException(new StringBuffer().append("value ").append(obj).toString());
        }
        Handle handle = (Handle) obj;
        return a(handle.a, handle.b, handle.c, handle.d);
    }

    public int newConst(Object obj) {
        return a(obj).a;
    }

    public int newUTF8(String str) {
        this.g.a(1, str, null, null);
        Item itemA = a(this.g);
        if (itemA == null) {
            this.d.putByte(1).putUTF8(str);
            int i = this.c;
            this.c = i + 1;
            itemA = new Item(i, this.g);
            b(itemA);
        }
        return itemA.a;
    }

    Item a(String str) {
        this.h.a(7, str, null, null);
        Item itemA = a(this.h);
        if (itemA == null) {
            this.d.b(7, newUTF8(str));
            int i = this.c;
            this.c = i + 1;
            itemA = new Item(i, this.h);
            b(itemA);
        }
        return itemA;
    }

    public int newClass(String str) {
        return a(str).a;
    }

    Item c(String str) {
        this.h.a(16, str, null, null);
        Item itemA = a(this.h);
        if (itemA == null) {
            this.d.b(16, newUTF8(str));
            int i = this.c;
            this.c = i + 1;
            itemA = new Item(i, this.h);
            b(itemA);
        }
        return itemA;
    }

    public int newMethodType(String str) {
        return c(str).a;
    }

    Item a(int i, String str, String str2, String str3) {
        this.j.a(20 + i, str, str2, str3);
        Item itemA = a(this.j);
        if (itemA == null) {
            if (i <= 4) {
                b(15, i, newField(str, str2, str3));
            } else {
                b(15, i, newMethod(str, str2, str3, i == 9));
            }
            int i2 = this.c;
            this.c = i2 + 1;
            itemA = new Item(i2, this.j);
            b(itemA);
        }
        return itemA;
    }

    public int newHandle(int i, String str, String str2, String str3) {
        return a(i, str, str2, str3).a;
    }

    Item a(String str, String str2, Handle handle, Object... objArr) {
        Item item;
        int i;
        ByteVector byteVector = this.A;
        if (byteVector == null) {
            ByteVector byteVector2 = new ByteVector();
            this.A = byteVector2;
            byteVector = byteVector2;
        }
        int i2 = byteVector.b;
        int iHashCode = handle.hashCode();
        byteVector.putShort(newHandle(handle.a, handle.b, handle.c, handle.d));
        int length = objArr.length;
        byteVector.putShort(length);
        for (Object obj : objArr) {
            iHashCode ^= obj.hashCode();
            byteVector.putShort(newConst(obj));
        }
        byte[] bArr = byteVector.a;
        int i3 = (2 + length) << 1;
        int i4 = iHashCode & Integer.MAX_VALUE;
        Item item2 = this.e[i4 % this.e.length];
        loop1: while (true) {
            item = item2;
            if (item == null) {
                break;
            }
            if (item.b == 33 && item.j == i4) {
                int i5 = item.c;
                for (int i6 = 0; i6 < i3; i6++) {
                    if (bArr[i2 + i6] != bArr[i5 + i6]) {
                        item2 = item.k;
                    }
                }
                break loop1;
            }
            item2 = item.k;
        }
        if (item != null) {
            i = item.a;
            byteVector.b = i2;
        } else {
            int i7 = this.z;
            this.z = i7 + 1;
            i = i7;
            Item item3 = new Item(i);
            item3.a(i2, i4);
            b(item3);
        }
        this.i.a(str, str2, i);
        Item itemA = a(this.i);
        if (itemA == null) {
            a(18, i, newNameType(str, str2));
            int i8 = this.c;
            this.c = i8 + 1;
            itemA = new Item(i8, this.i);
            b(itemA);
        }
        return itemA;
    }

    public int newInvokeDynamic(String str, String str2, Handle handle, Object... objArr) {
        return a(str, str2, handle, objArr).a;
    }

    Item a(String str, String str2, String str3) {
        this.i.a(9, str, str2, str3);
        Item itemA = a(this.i);
        if (itemA == null) {
            a(9, newClass(str), newNameType(str2, str3));
            int i = this.c;
            this.c = i + 1;
            itemA = new Item(i, this.i);
            b(itemA);
        }
        return itemA;
    }

    public int newField(String str, String str2, String str3) {
        return a(str, str2, str3).a;
    }

    Item a(String str, String str2, String str3, boolean z) {
        int i = z ? 11 : 10;
        this.i.a(i, str, str2, str3);
        Item itemA = a(this.i);
        if (itemA == null) {
            a(i, newClass(str), newNameType(str2, str3));
            int i2 = this.c;
            this.c = i2 + 1;
            itemA = new Item(i2, this.i);
            b(itemA);
        }
        return itemA;
    }

    public int newMethod(String str, String str2, String str3, boolean z) {
        return a(str, str2, str3, z).a;
    }

    Item a(int i) {
        this.g.a(i);
        Item itemA = a(this.g);
        if (itemA == null) {
            this.d.putByte(3).putInt(i);
            int i2 = this.c;
            this.c = i2 + 1;
            itemA = new Item(i2, this.g);
            b(itemA);
        }
        return itemA;
    }

    Item a(float f) {
        this.g.a(f);
        Item itemA = a(this.g);
        if (itemA == null) {
            this.d.putByte(4).putInt(this.g.c);
            int i = this.c;
            this.c = i + 1;
            itemA = new Item(i, this.g);
            b(itemA);
        }
        return itemA;
    }

    Item a(long j) {
        this.g.a(j);
        Item itemA = a(this.g);
        if (itemA == null) {
            this.d.putByte(5).putLong(j);
            itemA = new Item(this.c, this.g);
            this.c += 2;
            b(itemA);
        }
        return itemA;
    }

    Item a(double d) {
        this.g.a(d);
        Item itemA = a(this.g);
        if (itemA == null) {
            this.d.putByte(6).putLong(this.g.d);
            itemA = new Item(this.c, this.g);
            this.c += 2;
            b(itemA);
        }
        return itemA;
    }

    private Item b(String str) {
        this.h.a(8, str, null, null);
        Item itemA = a(this.h);
        if (itemA == null) {
            this.d.b(8, newUTF8(str));
            int i = this.c;
            this.c = i + 1;
            itemA = new Item(i, this.h);
            b(itemA);
        }
        return itemA;
    }

    public int newNameType(String str, String str2) {
        return a(str, str2).a;
    }

    Item a(String str, String str2) {
        this.h.a(12, str, str2, null);
        Item itemA = a(this.h);
        if (itemA == null) {
            a(12, newUTF8(str), newUTF8(str2));
            int i = this.c;
            this.c = i + 1;
            itemA = new Item(i, this.h);
            b(itemA);
        }
        return itemA;
    }

    /* renamed from: c, reason: collision with other method in class */
    int m1c(String str) {
        this.g.a(30, str, null, null);
        Item itemA = a(this.g);
        if (itemA == null) {
            itemA = c(this.g);
        }
        return itemA.a;
    }

    int a(String str, int i) {
        this.g.b = 31;
        this.g.c = i;
        this.g.g = str;
        this.g.j = Integer.MAX_VALUE & (31 + str.hashCode() + i);
        Item itemA = a(this.g);
        if (itemA == null) {
            itemA = c(this.g);
        }
        return itemA.a;
    }

    private Item c(Item item) {
        this.G = (short) (this.G + 1);
        Item item2 = new Item(this.G, this.g);
        b(item2);
        if (this.H == null) {
            this.H = new Item[16];
        }
        if (this.G == this.H.length) {
            Item[] itemArr = new Item[2 * this.H.length];
            System.arraycopy(this.H, 0, itemArr, 0, this.H.length);
            this.H = itemArr;
        }
        this.H[this.G] = item2;
        return item2;
    }

    int a(int i, int i2) {
        this.h.b = 32;
        this.h.d = i | (i2 << 32);
        this.h.j = Integer.MAX_VALUE & (32 + i + i2);
        Item itemA = a(this.h);
        if (itemA == null) {
            this.h.c = m1c(getCommonSuperClass(this.H[i].g, this.H[i2].g));
            itemA = new Item(0, this.h);
            b(itemA);
        }
        return itemA.c;
    }

    protected String getCommonSuperClass(String str, String str2) throws ClassNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            Class<?> cls = Class.forName(str.replace('/', '.'), false, classLoader);
            Class<?> cls2 = Class.forName(str2.replace('/', '.'), false, classLoader);
            if (cls.isAssignableFrom(cls2)) {
                return str;
            }
            if (cls2.isAssignableFrom(cls)) {
                return str2;
            }
            if (cls.isInterface() || cls2.isInterface()) {
                return "java/lang/Object";
            }
            do {
                cls = cls.getSuperclass();
            } while (!cls.isAssignableFrom(cls2));
            return cls.getName().replace('.', '/');
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    private Item a(Item item) {
        Item item2;
        Item item3 = this.e[item.j % this.e.length];
        while (true) {
            item2 = item3;
            if (item2 == null || (item2.b == item.b && item.a(item2))) {
                break;
            }
            item3 = item2.k;
        }
        return item2;
    }

    private void b(Item item) {
        if (this.c + this.G > this.f) {
            int length = this.e.length;
            int i = (length * 2) + 1;
            Item[] itemArr = new Item[i];
            for (int i2 = length - 1; i2 >= 0; i2--) {
                Item item2 = this.e[i2];
                while (true) {
                    Item item3 = item2;
                    if (item3 != null) {
                        int length2 = item3.j % itemArr.length;
                        Item item4 = item3.k;
                        item3.k = itemArr[length2];
                        itemArr[length2] = item3;
                        item2 = item4;
                    }
                }
            }
            this.e = itemArr;
            this.f = (int) (i * 0.75d);
        }
        int length3 = item.j % this.e.length;
        item.k = this.e[length3];
        this.e[length3] = item;
    }

    private void a(int i, int i2, int i3) {
        this.d.b(i, i2).putShort(i3);
    }

    private void b(int i, int i2, int i3) {
        this.d.a(i, i2).putShort(i3);
    }

    static {
        _clinit_();
        byte[] bArr = new byte[220];
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = (byte) ("AAAAAAAAAAAAAAAABCLMMDDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAADDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAANAAAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJDOPAAAAAAGGGGGGGHIFBFAAFFAARQJJKKJJJJJJJJJJJJJJJJJJ".charAt(i) - 'A');
        }
        a = bArr;
    }

    static /* synthetic */ void _clinit_() {
    }
}
