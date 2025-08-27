package aj.org.objectweb.asm;

import java.io.IOException;
import java.io.InputStream;
import org.apache.ibatis.javassist.bytecode.AnnotationDefaultAttribute;
import org.apache.ibatis.javassist.bytecode.AnnotationsAttribute;
import org.apache.ibatis.javassist.bytecode.BootstrapMethodsAttribute;
import org.apache.ibatis.javassist.bytecode.ConstantAttribute;
import org.apache.ibatis.javassist.bytecode.DeprecatedAttribute;
import org.apache.ibatis.javassist.bytecode.EnclosingMethodAttribute;
import org.apache.ibatis.javassist.bytecode.ExceptionsAttribute;
import org.apache.ibatis.javassist.bytecode.InnerClassesAttribute;
import org.apache.ibatis.javassist.bytecode.LineNumberAttribute;
import org.apache.ibatis.javassist.bytecode.LocalVariableAttribute;
import org.apache.ibatis.javassist.bytecode.MethodParametersAttribute;
import org.apache.ibatis.javassist.bytecode.ParameterAnnotationsAttribute;
import org.apache.ibatis.javassist.bytecode.SignatureAttribute;
import org.apache.ibatis.javassist.bytecode.SourceFileAttribute;
import org.apache.ibatis.javassist.bytecode.StackMap;
import org.apache.ibatis.javassist.bytecode.StackMapTable;
import org.apache.ibatis.javassist.bytecode.SyntheticAttribute;
import org.apache.ibatis.javassist.bytecode.TypeAnnotationsAttribute;
import org.springframework.util.ClassUtils;

/* loaded from: aspectjweaver-1.8.14.jar:aj/org/objectweb/asm/ClassReader.class */
public class ClassReader {
    public static final int SKIP_CODE = 1;
    public static final int SKIP_DEBUG = 2;
    public static final int SKIP_FRAMES = 4;
    public static final int EXPAND_FRAMES = 8;
    public final byte[] b;
    private final int[] a;
    private final String[] c;
    private final int d;
    public final int header;

    public ClassReader(byte[] bArr) {
        this(bArr, 0, bArr.length);
    }

    public ClassReader(byte[] bArr, int i, int i2) {
        int unsignedShort;
        this.b = bArr;
        if (readShort(i + 6) > 52) {
            throw new IllegalArgumentException();
        }
        this.a = new int[readUnsignedShort(i + 8)];
        int length = this.a.length;
        this.c = new String[length];
        int i3 = 0;
        int i4 = i + 10;
        int i5 = 1;
        while (i5 < length) {
            this.a[i5] = i4 + 1;
            switch (bArr[i4]) {
                case 1:
                    unsignedShort = 3 + readUnsignedShort(i4 + 1);
                    if (unsignedShort <= i3) {
                        break;
                    } else {
                        i3 = unsignedShort;
                        break;
                    }
                case 2:
                case 7:
                case 8:
                case 13:
                case 14:
                case 16:
                case 17:
                default:
                    unsignedShort = 3;
                    break;
                case 3:
                case 4:
                case 9:
                case 10:
                case 11:
                case 12:
                case 18:
                    unsignedShort = 5;
                    break;
                case 5:
                case 6:
                    unsignedShort = 9;
                    i5++;
                    break;
                case 15:
                    unsignedShort = 4;
                    break;
            }
            i4 += unsignedShort;
            i5++;
        }
        this.d = i3;
        this.header = i4;
    }

    public int getAccess() {
        return readUnsignedShort(this.header);
    }

    public String getClassName() {
        return readClass(this.header + 2, new char[this.d]);
    }

    public String getSuperName() {
        return readClass(this.header + 4, new char[this.d]);
    }

    public String[] getInterfaces() {
        int i = this.header + 6;
        int unsignedShort = readUnsignedShort(i);
        String[] strArr = new String[unsignedShort];
        if (unsignedShort > 0) {
            char[] cArr = new char[this.d];
            for (int i2 = 0; i2 < unsignedShort; i2++) {
                i += 2;
                strArr[i2] = readClass(i, cArr);
            }
        }
        return strArr;
    }

    void a(ClassWriter classWriter) {
        char[] cArr = new char[this.d];
        int length = this.a.length;
        Item[] itemArr = new Item[length];
        int i = 1;
        while (i < length) {
            int i2 = this.a[i];
            byte b = this.b[i2 - 1];
            Item item = new Item(i);
            switch (b) {
                case 1:
                    String str = this.c[i];
                    if (str == null) {
                        int i3 = this.a[i];
                        String strA = a(i3 + 2, readUnsignedShort(i3), cArr);
                        this.c[i] = strA;
                        str = strA;
                    }
                    item.a(b, str, null, null);
                    break;
                case 2:
                case 7:
                case 8:
                case 13:
                case 14:
                case 16:
                case 17:
                default:
                    item.a(b, readUTF8(i2, cArr), null, null);
                    break;
                case 3:
                    item.a(readInt(i2));
                    break;
                case 4:
                    item.a(Float.intBitsToFloat(readInt(i2)));
                    break;
                case 5:
                    item.a(readLong(i2));
                    i++;
                    break;
                case 6:
                    item.a(Double.longBitsToDouble(readLong(i2)));
                    i++;
                    break;
                case 9:
                case 10:
                case 11:
                    int i4 = this.a[readUnsignedShort(i2 + 2)];
                    item.a(b, readClass(i2, cArr), readUTF8(i4, cArr), readUTF8(i4 + 2, cArr));
                    break;
                case 12:
                    item.a(b, readUTF8(i2, cArr), readUTF8(i2 + 2, cArr), null);
                    break;
                case 15:
                    int i5 = this.a[readUnsignedShort(i2 + 1)];
                    int i6 = this.a[readUnsignedShort(i5 + 2)];
                    item.a(20 + readByte(i2), readClass(i5, cArr), readUTF8(i6, cArr), readUTF8(i6 + 2, cArr));
                    break;
                case 18:
                    if (classWriter.A == null) {
                        a(classWriter, itemArr, cArr);
                    }
                    int i7 = this.a[readUnsignedShort(i2 + 2)];
                    item.a(readUTF8(i7, cArr), readUTF8(i7 + 2, cArr), readUnsignedShort(i2));
                    break;
            }
            int length2 = item.j % itemArr.length;
            item.k = itemArr[length2];
            itemArr[length2] = item;
            i++;
        }
        int i8 = this.a[1] - 1;
        classWriter.d.putByteArray(this.b, i8, this.header - i8);
        classWriter.e = itemArr;
        classWriter.f = (int) (0.75d * length);
        classWriter.c = length;
    }

    private void a(ClassWriter classWriter, Item[] itemArr, char[] cArr) {
        int iA = a();
        boolean z = false;
        int unsignedShort = readUnsignedShort(iA);
        while (true) {
            if (unsignedShort <= 0) {
                break;
            }
            if (BootstrapMethodsAttribute.tag.equals(readUTF8(iA + 2, cArr))) {
                z = true;
                break;
            } else {
                iA += 6 + readInt(iA + 4);
                unsignedShort--;
            }
        }
        if (z) {
            int unsignedShort2 = readUnsignedShort(iA + 8);
            int i = iA + 10;
            for (int i2 = 0; i2 < unsignedShort2; i2++) {
                int i3 = (i - iA) - 10;
                int iHashCode = readConst(readUnsignedShort(i), cArr).hashCode();
                for (int unsignedShort3 = readUnsignedShort(i + 2); unsignedShort3 > 0; unsignedShort3--) {
                    iHashCode ^= readConst(readUnsignedShort(i + 4), cArr).hashCode();
                    i += 2;
                }
                i += 4;
                Item item = new Item(i2);
                item.a(i3, iHashCode & Integer.MAX_VALUE);
                int length = item.j % itemArr.length;
                item.k = itemArr[length];
                itemArr[length] = item;
            }
            int i4 = readInt(iA + 4);
            ByteVector byteVector = new ByteVector(i4 + 62);
            byteVector.putByteArray(this.b, iA + 10, i4 - 2);
            classWriter.z = unsignedShort2;
            classWriter.A = byteVector;
        }
    }

    public ClassReader(InputStream inputStream) throws IOException {
        this(a(inputStream, false));
    }

    public ClassReader(String str) throws IOException {
        this(a(ClassLoader.getSystemResourceAsStream(new StringBuffer().append(str.replace('.', '/')).append(ClassUtils.CLASS_FILE_SUFFIX).toString()), true));
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x002c, code lost:
    
        if (r9 >= r8.length) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x002f, code lost:
    
        r0 = new byte[r9];
        java.lang.System.arraycopy(r8, 0, r0, 0, r9);
        r8 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x004d, code lost:
    
        return r8;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static byte[] a(java.io.InputStream r6, boolean r7) throws java.io.IOException {
        /*
            r0 = r6
            if (r0 != 0) goto Le
            java.io.IOException r0 = new java.io.IOException
            r1 = r0
            java.lang.String r2 = "Class not found"
            r1.<init>(r2)
            throw r0
        Le:
            r0 = r6
            int r0 = r0.available()     // Catch: java.lang.Throwable -> L95
            byte[] r0 = new byte[r0]     // Catch: java.lang.Throwable -> L95
            r8 = r0
            r0 = 0
            r9 = r0
        L17:
            r0 = r6
            r1 = r8
            r2 = r9
            r3 = r8
            int r3 = r3.length     // Catch: java.lang.Throwable -> L95
            r4 = r9
            int r3 = r3 - r4
            int r0 = r0.read(r1, r2, r3)     // Catch: java.lang.Throwable -> L95
            r10 = r0
            r0 = r10
            r1 = -1
            if (r0 != r1) goto L4e
            r0 = r9
            r1 = r8
            int r1 = r1.length     // Catch: java.lang.Throwable -> L95
            if (r0 >= r1) goto L40
            r0 = r9
            byte[] r0 = new byte[r0]     // Catch: java.lang.Throwable -> L95
            r11 = r0
            r0 = r8
            r1 = 0
            r2 = r11
            r3 = 0
            r4 = r9
            java.lang.System.arraycopy(r0, r1, r2, r3, r4)     // Catch: java.lang.Throwable -> L95
            r0 = r11
            r8 = r0
        L40:
            r0 = r8
            r11 = r0
            r0 = r7
            if (r0 == 0) goto L4b
            r0 = r6
            r0.close()
        L4b:
            r0 = r11
            return r0
        L4e:
            r0 = r9
            r1 = r10
            int r0 = r0 + r1
            r9 = r0
            r0 = r9
            r1 = r8
            int r1 = r1.length     // Catch: java.lang.Throwable -> L95
            if (r0 != r1) goto L92
            r0 = r6
            int r0 = r0.read()     // Catch: java.lang.Throwable -> L95
            r11 = r0
            r0 = r11
            if (r0 >= 0) goto L72
            r0 = r8
            r12 = r0
            r0 = r7
            if (r0 == 0) goto L6f
            r0 = r6
            r0.close()
        L6f:
            r0 = r12
            return r0
        L72:
            r0 = r8
            int r0 = r0.length     // Catch: java.lang.Throwable -> L95
            r1 = 1000(0x3e8, float:1.401E-42)
            int r0 = r0 + r1
            byte[] r0 = new byte[r0]     // Catch: java.lang.Throwable -> L95
            r12 = r0
            r0 = r8
            r1 = 0
            r2 = r12
            r3 = 0
            r4 = r9
            java.lang.System.arraycopy(r0, r1, r2, r3, r4)     // Catch: java.lang.Throwable -> L95
            r0 = r12
            r1 = r9
            int r9 = r9 + 1
            r2 = r11
            byte r2 = (byte) r2     // Catch: java.lang.Throwable -> L95
            r0[r1] = r2     // Catch: java.lang.Throwable -> L95
            r0 = r12
            r8 = r0
        L92:
            goto L17
        L95:
            r13 = move-exception
            r0 = r7
            if (r0 == 0) goto L9f
            r0 = r6
            r0.close()
        L9f:
            r0 = r13
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: aj.org.objectweb.asm.ClassReader.a(java.io.InputStream, boolean):byte[]");
    }

    public void accept(ClassVisitor classVisitor, int i) {
        accept(classVisitor, new Attribute[0], i);
    }

    public void accept(ClassVisitor classVisitor, Attribute[] attributeArr, int i) {
        int i2 = this.header;
        char[] cArr = new char[this.d];
        Context context = new Context();
        context.a = attributeArr;
        context.b = i;
        context.c = cArr;
        int unsignedShort = readUnsignedShort(i2);
        String str = readClass(i2 + 2, cArr);
        String str2 = readClass(i2 + 4, cArr);
        String[] strArr = new String[readUnsignedShort(i2 + 6)];
        int i3 = i2 + 8;
        for (int i4 = 0; i4 < strArr.length; i4++) {
            strArr[i4] = readClass(i3, cArr);
            i3 += 2;
        }
        String utf8 = null;
        String utf82 = null;
        String strA = null;
        String str3 = null;
        String utf83 = null;
        String utf84 = null;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        Attribute attribute = null;
        int iA = a();
        for (int unsignedShort2 = readUnsignedShort(iA); unsignedShort2 > 0; unsignedShort2--) {
            String utf85 = readUTF8(iA + 2, cArr);
            if (SourceFileAttribute.tag.equals(utf85)) {
                utf82 = readUTF8(iA + 8, cArr);
            } else if (InnerClassesAttribute.tag.equals(utf85)) {
                i9 = iA + 8;
            } else if (EnclosingMethodAttribute.tag.equals(utf85)) {
                str3 = readClass(iA + 8, cArr);
                int unsignedShort3 = readUnsignedShort(iA + 10);
                if (unsignedShort3 != 0) {
                    utf83 = readUTF8(this.a[unsignedShort3], cArr);
                    utf84 = readUTF8(this.a[unsignedShort3] + 2, cArr);
                }
            } else if (SignatureAttribute.tag.equals(utf85)) {
                utf8 = readUTF8(iA + 8, cArr);
            } else if (AnnotationsAttribute.visibleTag.equals(utf85)) {
                i5 = iA + 8;
            } else if (TypeAnnotationsAttribute.visibleTag.equals(utf85)) {
                i7 = iA + 8;
            } else if (DeprecatedAttribute.tag.equals(utf85)) {
                unsignedShort |= 131072;
            } else if (SyntheticAttribute.tag.equals(utf85)) {
                unsignedShort |= 266240;
            } else if ("SourceDebugExtension".equals(utf85)) {
                int i10 = readInt(iA + 4);
                strA = a(iA + 8, i10, new char[i10]);
            } else if (AnnotationsAttribute.invisibleTag.equals(utf85)) {
                i6 = iA + 8;
            } else if (TypeAnnotationsAttribute.invisibleTag.equals(utf85)) {
                i8 = iA + 8;
            } else if (BootstrapMethodsAttribute.tag.equals(utf85)) {
                int[] iArr = new int[readUnsignedShort(iA + 8)];
                int unsignedShort4 = iA + 10;
                for (int i11 = 0; i11 < iArr.length; i11++) {
                    iArr[i11] = unsignedShort4;
                    unsignedShort4 += (2 + readUnsignedShort(unsignedShort4 + 2)) << 1;
                }
                context.d = iArr;
            } else {
                Attribute attributeA = a(attributeArr, utf85, iA + 8, readInt(iA + 4), cArr, -1, null);
                if (attributeA != null) {
                    attributeA.a = attribute;
                    attribute = attributeA;
                }
            }
            iA += 6 + readInt(iA + 4);
        }
        classVisitor.visit(readInt(this.a[1] - 7), unsignedShort, str, utf8, str2, strArr);
        if ((i & 2) == 0 && (utf82 != null || strA != null)) {
            classVisitor.visitSource(utf82, strA);
        }
        if (str3 != null) {
            classVisitor.visitOuterClass(str3, utf83, utf84);
        }
        if (i5 != 0) {
            int iA2 = i5 + 2;
            for (int unsignedShort5 = readUnsignedShort(i5); unsignedShort5 > 0; unsignedShort5--) {
                iA2 = a(iA2 + 2, cArr, true, classVisitor.visitAnnotation(readUTF8(iA2, cArr), true));
            }
        }
        if (i6 != 0) {
            int iA3 = i6 + 2;
            for (int unsignedShort6 = readUnsignedShort(i6); unsignedShort6 > 0; unsignedShort6--) {
                iA3 = a(iA3 + 2, cArr, true, classVisitor.visitAnnotation(readUTF8(iA3, cArr), false));
            }
        }
        if (i7 != 0) {
            int iA4 = i7 + 2;
            for (int unsignedShort7 = readUnsignedShort(i7); unsignedShort7 > 0; unsignedShort7--) {
                int iA5 = a(context, iA4);
                iA4 = a(iA5 + 2, cArr, true, classVisitor.visitTypeAnnotation(context.i, context.j, readUTF8(iA5, cArr), true));
            }
        }
        if (i8 != 0) {
            int iA6 = i8 + 2;
            for (int unsignedShort8 = readUnsignedShort(i8); unsignedShort8 > 0; unsignedShort8--) {
                int iA7 = a(context, iA6);
                iA6 = a(iA7 + 2, cArr, true, classVisitor.visitTypeAnnotation(context.i, context.j, readUTF8(iA7, cArr), false));
            }
        }
        while (attribute != null) {
            Attribute attribute2 = attribute.a;
            attribute.a = null;
            classVisitor.visitAttribute(attribute);
            attribute = attribute2;
        }
        if (i9 != 0) {
            int i12 = i9 + 2;
            for (int unsignedShort9 = readUnsignedShort(i9); unsignedShort9 > 0; unsignedShort9--) {
                classVisitor.visitInnerClass(readClass(i12, cArr), readClass(i12 + 2, cArr), readUTF8(i12 + 4, cArr), readUnsignedShort(i12 + 6));
                i12 += 8;
            }
        }
        int length = this.header + 10 + (2 * strArr.length);
        for (int unsignedShort10 = readUnsignedShort(length - 2); unsignedShort10 > 0; unsignedShort10--) {
            length = a(classVisitor, context, length);
        }
        int iB = length + 2;
        for (int unsignedShort11 = readUnsignedShort(iB - 2); unsignedShort11 > 0; unsignedShort11--) {
            iB = b(classVisitor, context, iB);
        }
        classVisitor.visitEnd();
    }

    private int a(ClassVisitor classVisitor, Context context, int i) {
        char[] cArr = context.c;
        int unsignedShort = readUnsignedShort(i);
        String utf8 = readUTF8(i + 2, cArr);
        String utf82 = readUTF8(i + 4, cArr);
        int i2 = i + 6;
        String utf83 = null;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        Object obj = null;
        Attribute attribute = null;
        for (int unsignedShort2 = readUnsignedShort(i2); unsignedShort2 > 0; unsignedShort2--) {
            String utf84 = readUTF8(i2 + 2, cArr);
            if (ConstantAttribute.tag.equals(utf84)) {
                int unsignedShort3 = readUnsignedShort(i2 + 8);
                obj = unsignedShort3 == 0 ? null : readConst(unsignedShort3, cArr);
            } else if (SignatureAttribute.tag.equals(utf84)) {
                utf83 = readUTF8(i2 + 8, cArr);
            } else if (DeprecatedAttribute.tag.equals(utf84)) {
                unsignedShort |= 131072;
            } else if (SyntheticAttribute.tag.equals(utf84)) {
                unsignedShort |= 266240;
            } else if (AnnotationsAttribute.visibleTag.equals(utf84)) {
                i3 = i2 + 8;
            } else if (TypeAnnotationsAttribute.visibleTag.equals(utf84)) {
                i5 = i2 + 8;
            } else if (AnnotationsAttribute.invisibleTag.equals(utf84)) {
                i4 = i2 + 8;
            } else if (TypeAnnotationsAttribute.invisibleTag.equals(utf84)) {
                i6 = i2 + 8;
            } else {
                Attribute attributeA = a(context.a, utf84, i2 + 8, readInt(i2 + 4), cArr, -1, null);
                if (attributeA != null) {
                    attributeA.a = attribute;
                    attribute = attributeA;
                }
            }
            i2 += 6 + readInt(i2 + 4);
        }
        int i7 = i2 + 2;
        FieldVisitor fieldVisitorVisitField = classVisitor.visitField(unsignedShort, utf8, utf82, utf83, obj);
        if (fieldVisitorVisitField == null) {
            return i7;
        }
        if (i3 != 0) {
            int iA = i3 + 2;
            for (int unsignedShort4 = readUnsignedShort(i3); unsignedShort4 > 0; unsignedShort4--) {
                iA = a(iA + 2, cArr, true, fieldVisitorVisitField.visitAnnotation(readUTF8(iA, cArr), true));
            }
        }
        if (i4 != 0) {
            int iA2 = i4 + 2;
            for (int unsignedShort5 = readUnsignedShort(i4); unsignedShort5 > 0; unsignedShort5--) {
                iA2 = a(iA2 + 2, cArr, true, fieldVisitorVisitField.visitAnnotation(readUTF8(iA2, cArr), false));
            }
        }
        if (i5 != 0) {
            int iA3 = i5 + 2;
            for (int unsignedShort6 = readUnsignedShort(i5); unsignedShort6 > 0; unsignedShort6--) {
                int iA4 = a(context, iA3);
                iA3 = a(iA4 + 2, cArr, true, fieldVisitorVisitField.visitTypeAnnotation(context.i, context.j, readUTF8(iA4, cArr), true));
            }
        }
        if (i6 != 0) {
            int iA5 = i6 + 2;
            for (int unsignedShort7 = readUnsignedShort(i6); unsignedShort7 > 0; unsignedShort7--) {
                int iA6 = a(context, iA5);
                iA5 = a(iA6 + 2, cArr, true, fieldVisitorVisitField.visitTypeAnnotation(context.i, context.j, readUTF8(iA6, cArr), false));
            }
        }
        while (attribute != null) {
            Attribute attribute2 = attribute.a;
            attribute.a = null;
            fieldVisitorVisitField.visitAttribute(attribute);
            attribute = attribute2;
        }
        fieldVisitorVisitField.visitEnd();
        return i7;
    }

    private int b(ClassVisitor classVisitor, Context context, int i) {
        char[] cArr = context.c;
        context.e = readUnsignedShort(i);
        context.f = readUTF8(i + 2, cArr);
        context.g = readUTF8(i + 4, cArr);
        int i2 = i + 6;
        int i3 = 0;
        int i4 = 0;
        String[] strArr = null;
        String utf8 = null;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        int i10 = 0;
        int i11 = 0;
        int i12 = 0;
        Attribute attribute = null;
        for (int unsignedShort = readUnsignedShort(i2); unsignedShort > 0; unsignedShort--) {
            String utf82 = readUTF8(i2 + 2, cArr);
            if ("Code".equals(utf82)) {
                if ((context.b & 1) == 0) {
                    i3 = i2 + 8;
                }
            } else if (ExceptionsAttribute.tag.equals(utf82)) {
                strArr = new String[readUnsignedShort(i2 + 8)];
                i4 = i2 + 10;
                for (int i13 = 0; i13 < strArr.length; i13++) {
                    strArr[i13] = readClass(i4, cArr);
                    i4 += 2;
                }
            } else if (SignatureAttribute.tag.equals(utf82)) {
                utf8 = readUTF8(i2 + 8, cArr);
            } else if (DeprecatedAttribute.tag.equals(utf82)) {
                context.e |= 131072;
            } else if (AnnotationsAttribute.visibleTag.equals(utf82)) {
                i6 = i2 + 8;
            } else if (TypeAnnotationsAttribute.visibleTag.equals(utf82)) {
                i8 = i2 + 8;
            } else if (AnnotationDefaultAttribute.tag.equals(utf82)) {
                i10 = i2 + 8;
            } else if (SyntheticAttribute.tag.equals(utf82)) {
                context.e |= 266240;
            } else if (AnnotationsAttribute.invisibleTag.equals(utf82)) {
                i7 = i2 + 8;
            } else if (TypeAnnotationsAttribute.invisibleTag.equals(utf82)) {
                i9 = i2 + 8;
            } else if (ParameterAnnotationsAttribute.visibleTag.equals(utf82)) {
                i11 = i2 + 8;
            } else if (ParameterAnnotationsAttribute.invisibleTag.equals(utf82)) {
                i12 = i2 + 8;
            } else if (MethodParametersAttribute.tag.equals(utf82)) {
                i5 = i2 + 8;
            } else {
                Attribute attributeA = a(context.a, utf82, i2 + 8, readInt(i2 + 4), cArr, -1, null);
                if (attributeA != null) {
                    attributeA.a = attribute;
                    attribute = attributeA;
                }
            }
            i2 += 6 + readInt(i2 + 4);
        }
        int i14 = i2 + 2;
        MethodVisitor methodVisitorVisitMethod = classVisitor.visitMethod(context.e, context.f, context.g, utf8, strArr);
        if (methodVisitorVisitMethod == null) {
            return i14;
        }
        if (methodVisitorVisitMethod instanceof MethodWriter) {
            MethodWriter methodWriter = (MethodWriter) methodVisitorVisitMethod;
            if (methodWriter.b.M == this && utf8 == methodWriter.g) {
                boolean z = false;
                if (strArr == null) {
                    z = methodWriter.j == 0;
                } else if (strArr.length == methodWriter.j) {
                    z = true;
                    int length = strArr.length - 1;
                    while (true) {
                        if (length < 0) {
                            break;
                        }
                        i4 -= 2;
                        if (methodWriter.k[length] != readUnsignedShort(i4)) {
                            z = false;
                            break;
                        }
                        length--;
                    }
                }
                if (z) {
                    methodWriter.h = i2;
                    methodWriter.i = i14 - i2;
                    return i14;
                }
            }
        }
        if (i5 != 0) {
            int i15 = this.b[i5] & 255;
            int i16 = i5;
            int i17 = 1;
            while (true) {
                int i18 = i16 + i17;
                if (i15 <= 0) {
                    break;
                }
                methodVisitorVisitMethod.visitParameter(readUTF8(i18, cArr), readUnsignedShort(i18 + 2));
                i15--;
                i16 = i18;
                i17 = 4;
            }
        }
        if (i10 != 0) {
            AnnotationVisitor annotationVisitorVisitAnnotationDefault = methodVisitorVisitMethod.visitAnnotationDefault();
            a(i10, cArr, (String) null, annotationVisitorVisitAnnotationDefault);
            if (annotationVisitorVisitAnnotationDefault != null) {
                annotationVisitorVisitAnnotationDefault.visitEnd();
            }
        }
        if (i6 != 0) {
            int iA = i6 + 2;
            for (int unsignedShort2 = readUnsignedShort(i6); unsignedShort2 > 0; unsignedShort2--) {
                iA = a(iA + 2, cArr, true, methodVisitorVisitMethod.visitAnnotation(readUTF8(iA, cArr), true));
            }
        }
        if (i7 != 0) {
            int iA2 = i7 + 2;
            for (int unsignedShort3 = readUnsignedShort(i7); unsignedShort3 > 0; unsignedShort3--) {
                iA2 = a(iA2 + 2, cArr, true, methodVisitorVisitMethod.visitAnnotation(readUTF8(iA2, cArr), false));
            }
        }
        if (i8 != 0) {
            int iA3 = i8 + 2;
            for (int unsignedShort4 = readUnsignedShort(i8); unsignedShort4 > 0; unsignedShort4--) {
                int iA4 = a(context, iA3);
                iA3 = a(iA4 + 2, cArr, true, methodVisitorVisitMethod.visitTypeAnnotation(context.i, context.j, readUTF8(iA4, cArr), true));
            }
        }
        if (i9 != 0) {
            int iA5 = i9 + 2;
            for (int unsignedShort5 = readUnsignedShort(i9); unsignedShort5 > 0; unsignedShort5--) {
                int iA6 = a(context, iA5);
                iA5 = a(iA6 + 2, cArr, true, methodVisitorVisitMethod.visitTypeAnnotation(context.i, context.j, readUTF8(iA6, cArr), false));
            }
        }
        if (i11 != 0) {
            b(methodVisitorVisitMethod, context, i11, true);
        }
        if (i12 != 0) {
            b(methodVisitorVisitMethod, context, i12, false);
        }
        while (attribute != null) {
            Attribute attribute2 = attribute.a;
            attribute.a = null;
            methodVisitorVisitMethod.visitAttribute(attribute);
            attribute = attribute2;
        }
        if (i3 != 0) {
            methodVisitorVisitMethod.visitCode();
            a(methodVisitorVisitMethod, context, i3);
        }
        methodVisitorVisitMethod.visitEnd();
        return i14;
    }

    private void a(MethodVisitor methodVisitor, Context context, int i) {
        int unsignedShort;
        Attribute attribute;
        Label label;
        byte[] bArr = this.b;
        char[] cArr = context.c;
        int unsignedShort2 = readUnsignedShort(i);
        int unsignedShort3 = readUnsignedShort(i + 2);
        int i2 = readInt(i + 4);
        int i3 = i + 8;
        int i4 = i3 + i2;
        Label[] labelArr = new Label[i2 + 2];
        context.h = labelArr;
        readLabel(i2 + 1, labelArr);
        while (i3 < i4) {
            int i5 = i3 - i3;
            switch (ClassWriter.a[bArr[i3] & 255]) {
                case 0:
                case 4:
                    i3++;
                    break;
                case 1:
                case 3:
                case 11:
                    i3 += 2;
                    break;
                case 2:
                case 5:
                case 6:
                case 12:
                case 13:
                    i3 += 3;
                    break;
                case 7:
                case 8:
                    i3 += 5;
                    break;
                case 9:
                    readLabel(i5 + readShort(i3 + 1), labelArr);
                    i3 += 3;
                    break;
                case 10:
                    readLabel(i5 + readInt(i3 + 1), labelArr);
                    i3 += 5;
                    break;
                case 14:
                    int i6 = (i3 + 4) - (i5 & 3);
                    readLabel(i5 + readInt(i6), labelArr);
                    for (int i7 = (readInt(i6 + 8) - readInt(i6 + 4)) + 1; i7 > 0; i7--) {
                        readLabel(i5 + readInt(i6 + 12), labelArr);
                        i6 += 4;
                    }
                    i3 = i6 + 12;
                    break;
                case 15:
                    int i8 = (i3 + 4) - (i5 & 3);
                    readLabel(i5 + readInt(i8), labelArr);
                    for (int i9 = readInt(i8 + 4); i9 > 0; i9--) {
                        readLabel(i5 + readInt(i8 + 12), labelArr);
                        i8 += 8;
                    }
                    i3 = i8 + 8;
                    break;
                case 16:
                default:
                    i3 += 4;
                    break;
                case 17:
                    if ((bArr[i3 + 1] & 255) == 132) {
                        i3 += 6;
                        break;
                    } else {
                        i3 += 4;
                        break;
                    }
            }
        }
        for (int unsignedShort4 = readUnsignedShort(i3); unsignedShort4 > 0; unsignedShort4--) {
            methodVisitor.visitTryCatchBlock(readLabel(readUnsignedShort(i3 + 2), labelArr), readLabel(readUnsignedShort(i3 + 4), labelArr), readLabel(readUnsignedShort(i3 + 6), labelArr), readUTF8(this.a[readUnsignedShort(i3 + 8)], cArr));
            i3 += 8;
        }
        int i10 = i3 + 2;
        int[] iArrA = null;
        int[] iArrA2 = null;
        int i11 = 0;
        int i12 = 0;
        int unsignedShort5 = -1;
        int unsignedShort6 = -1;
        int i13 = 0;
        int i14 = 0;
        boolean z = true;
        boolean z2 = (context.b & 8) != 0;
        int iA = 0;
        int i15 = 0;
        int unsignedShort7 = 0;
        Context context2 = null;
        Attribute attribute2 = null;
        for (int unsignedShort8 = readUnsignedShort(i10); unsignedShort8 > 0; unsignedShort8--) {
            String utf8 = readUTF8(i10 + 2, cArr);
            if (LocalVariableAttribute.tag.equals(utf8)) {
                if ((context.b & 2) == 0) {
                    i13 = i10 + 8;
                    int i16 = i10;
                    for (int unsignedShort9 = readUnsignedShort(i10 + 8); unsignedShort9 > 0; unsignedShort9--) {
                        int unsignedShort10 = readUnsignedShort(i16 + 10);
                        if (labelArr[unsignedShort10] == null) {
                            readLabel(unsignedShort10, labelArr).a |= 1;
                        }
                        int unsignedShort11 = unsignedShort10 + readUnsignedShort(i16 + 12);
                        if (labelArr[unsignedShort11] == null) {
                            readLabel(unsignedShort11, labelArr).a |= 1;
                        }
                        i16 += 10;
                    }
                }
            } else if ("LocalVariableTypeTable".equals(utf8)) {
                i14 = i10 + 8;
            } else if (LineNumberAttribute.tag.equals(utf8)) {
                if ((context.b & 2) == 0) {
                    int i17 = i10;
                    for (int unsignedShort12 = readUnsignedShort(i10 + 8); unsignedShort12 > 0; unsignedShort12--) {
                        int unsignedShort13 = readUnsignedShort(i17 + 10);
                        if (labelArr[unsignedShort13] == null) {
                            readLabel(unsignedShort13, labelArr).a |= 1;
                        }
                        Label label2 = labelArr[unsignedShort13];
                        while (true) {
                            label = label2;
                            if (label.b > 0) {
                                if (label.k == null) {
                                    label.k = new Label();
                                }
                                label2 = label.k;
                            }
                        }
                        label.b = readUnsignedShort(i17 + 12);
                        i17 += 4;
                    }
                }
            } else if (TypeAnnotationsAttribute.visibleTag.equals(utf8)) {
                iArrA = a(methodVisitor, context, i10 + 8, true);
                unsignedShort5 = (iArrA.length == 0 || readByte(iArrA[0]) < 67) ? -1 : readUnsignedShort(iArrA[0] + 1);
            } else if (TypeAnnotationsAttribute.invisibleTag.equals(utf8)) {
                iArrA2 = a(methodVisitor, context, i10 + 8, false);
                unsignedShort6 = (iArrA2.length == 0 || readByte(iArrA2[0]) < 67) ? -1 : readUnsignedShort(iArrA2[0] + 1);
            } else if (StackMapTable.tag.equals(utf8)) {
                if ((context.b & 4) == 0) {
                    iA = i10 + 10;
                    i15 = readInt(i10 + 4);
                    unsignedShort7 = readUnsignedShort(i10 + 8);
                }
            } else if (!StackMap.tag.equals(utf8)) {
                for (int i18 = 0; i18 < context.a.length; i18++) {
                    if (context.a[i18].type.equals(utf8) && (attribute = context.a[i18].read(this, i10 + 8, readInt(i10 + 4), cArr, i3 - 8, labelArr)) != null) {
                        attribute.a = attribute2;
                        attribute2 = attribute;
                    }
                }
            } else if ((context.b & 4) == 0) {
                z = false;
                iA = i10 + 10;
                i15 = readInt(i10 + 4);
                unsignedShort7 = readUnsignedShort(i10 + 8);
            }
            i10 += 6 + readInt(i10 + 4);
        }
        int i19 = i10 + 2;
        if (iA != 0) {
            context2 = context;
            context2.o = -1;
            context2.p = 0;
            context2.q = 0;
            context2.r = 0;
            context2.t = 0;
            context2.s = new Object[unsignedShort3];
            context2.u = new Object[unsignedShort2];
            if (z2) {
                a(context);
            }
            for (int i20 = iA; i20 < (iA + i15) - 2; i20++) {
                if (bArr[i20] == 8 && (unsignedShort = readUnsignedShort(i20 + 1)) >= 0 && unsignedShort < i2 && (bArr[i3 + unsignedShort] & 255) == 187) {
                    readLabel(unsignedShort, labelArr);
                }
            }
        }
        int i21 = i3;
        while (i21 < i4) {
            int i22 = i21 - i3;
            Label label3 = labelArr[i22];
            if (label3 != null) {
                label3.k = null;
                methodVisitor.visitLabel(label3);
                if ((context.b & 2) == 0 && label3.b > 0) {
                    methodVisitor.visitLineNumber(label3.b, label3);
                    for (Label label4 = label3.k; label4 != null; label4 = label4.k) {
                        methodVisitor.visitLineNumber(label4.b, label3);
                    }
                }
            }
            while (context2 != null && (context2.o == i22 || context2.o == -1)) {
                if (context2.o != -1) {
                    if (!z || z2) {
                        methodVisitor.visitFrame(-1, context2.q, context2.s, context2.t, context2.u);
                    } else {
                        methodVisitor.visitFrame(context2.p, context2.r, context2.s, context2.t, context2.u);
                    }
                }
                if (unsignedShort7 > 0) {
                    iA = a(iA, z, z2, context2);
                    unsignedShort7--;
                } else {
                    context2 = null;
                }
            }
            int i23 = bArr[i21] & 255;
            switch (ClassWriter.a[i23]) {
                case 0:
                    methodVisitor.visitInsn(i23);
                    i21++;
                    break;
                case 1:
                    methodVisitor.visitIntInsn(i23, bArr[i21 + 1]);
                    i21 += 2;
                    break;
                case 2:
                    methodVisitor.visitIntInsn(i23, readShort(i21 + 1));
                    i21 += 3;
                    break;
                case 3:
                    methodVisitor.visitVarInsn(i23, bArr[i21 + 1] & 255);
                    i21 += 2;
                    break;
                case 4:
                    if (i23 > 54) {
                        int i24 = i23 - 59;
                        methodVisitor.visitVarInsn(54 + (i24 >> 2), i24 & 3);
                    } else {
                        int i25 = i23 - 26;
                        methodVisitor.visitVarInsn(21 + (i25 >> 2), i25 & 3);
                    }
                    i21++;
                    break;
                case 5:
                    methodVisitor.visitTypeInsn(i23, readClass(i21 + 1, cArr));
                    i21 += 3;
                    break;
                case 6:
                case 7:
                    int i26 = this.a[readUnsignedShort(i21 + 1)];
                    boolean z3 = bArr[i26 - 1] == 11;
                    String str = readClass(i26, cArr);
                    int i27 = this.a[readUnsignedShort(i26 + 2)];
                    String utf82 = readUTF8(i27, cArr);
                    String utf83 = readUTF8(i27 + 2, cArr);
                    if (i23 < 182) {
                        methodVisitor.visitFieldInsn(i23, str, utf82, utf83);
                    } else {
                        methodVisitor.visitMethodInsn(i23, str, utf82, utf83, z3);
                    }
                    if (i23 == 185) {
                        i21 += 5;
                        break;
                    } else {
                        i21 += 3;
                        break;
                    }
                case 8:
                    int i28 = this.a[readUnsignedShort(i21 + 1)];
                    int i29 = context.d[readUnsignedShort(i28)];
                    Handle handle = (Handle) readConst(readUnsignedShort(i29), cArr);
                    int unsignedShort14 = readUnsignedShort(i29 + 2);
                    Object[] objArr = new Object[unsignedShort14];
                    int i30 = i29 + 4;
                    for (int i31 = 0; i31 < unsignedShort14; i31++) {
                        objArr[i31] = readConst(readUnsignedShort(i30), cArr);
                        i30 += 2;
                    }
                    int i32 = this.a[readUnsignedShort(i28 + 2)];
                    methodVisitor.visitInvokeDynamicInsn(readUTF8(i32, cArr), readUTF8(i32 + 2, cArr), handle, objArr);
                    i21 += 5;
                    break;
                case 9:
                    methodVisitor.visitJumpInsn(i23, labelArr[i22 + readShort(i21 + 1)]);
                    i21 += 3;
                    break;
                case 10:
                    methodVisitor.visitJumpInsn(i23 - 33, labelArr[i22 + readInt(i21 + 1)]);
                    i21 += 5;
                    break;
                case 11:
                    methodVisitor.visitLdcInsn(readConst(bArr[i21 + 1] & 255, cArr));
                    i21 += 2;
                    break;
                case 12:
                    methodVisitor.visitLdcInsn(readConst(readUnsignedShort(i21 + 1), cArr));
                    i21 += 3;
                    break;
                case 13:
                    methodVisitor.visitIincInsn(bArr[i21 + 1] & 255, bArr[i21 + 2]);
                    i21 += 3;
                    break;
                case 14:
                    int i33 = (i21 + 4) - (i22 & 3);
                    int i34 = i22 + readInt(i33);
                    int i35 = readInt(i33 + 4);
                    int i36 = readInt(i33 + 8);
                    Label[] labelArr2 = new Label[(i36 - i35) + 1];
                    i21 = i33 + 12;
                    for (int i37 = 0; i37 < labelArr2.length; i37++) {
                        labelArr2[i37] = labelArr[i22 + readInt(i21)];
                        i21 += 4;
                    }
                    methodVisitor.visitTableSwitchInsn(i35, i36, labelArr[i34], labelArr2);
                    break;
                case 15:
                    int i38 = (i21 + 4) - (i22 & 3);
                    int i39 = i22 + readInt(i38);
                    int i40 = readInt(i38 + 4);
                    int[] iArr = new int[i40];
                    Label[] labelArr3 = new Label[i40];
                    i21 = i38 + 8;
                    for (int i41 = 0; i41 < i40; i41++) {
                        iArr[i41] = readInt(i21);
                        labelArr3[i41] = labelArr[i22 + readInt(i21 + 4)];
                        i21 += 8;
                    }
                    methodVisitor.visitLookupSwitchInsn(labelArr[i39], iArr, labelArr3);
                    break;
                case 16:
                default:
                    methodVisitor.visitMultiANewArrayInsn(readClass(i21 + 1, cArr), bArr[i21 + 3] & 255);
                    i21 += 4;
                    break;
                case 17:
                    int i42 = bArr[i21 + 1] & 255;
                    if (i42 == 132) {
                        methodVisitor.visitIincInsn(readUnsignedShort(i21 + 2), readShort(i21 + 4));
                        i21 += 6;
                        break;
                    } else {
                        methodVisitor.visitVarInsn(i42, readUnsignedShort(i21 + 2));
                        i21 += 4;
                        break;
                    }
            }
            while (iArrA != null && i11 < iArrA.length && unsignedShort5 <= i22) {
                if (unsignedShort5 == i22) {
                    int iA2 = a(context, iArrA[i11]);
                    a(iA2 + 2, cArr, true, methodVisitor.visitInsnAnnotation(context.i, context.j, readUTF8(iA2, cArr), true));
                }
                i11++;
                unsignedShort5 = (i11 >= iArrA.length || readByte(iArrA[i11]) < 67) ? -1 : readUnsignedShort(iArrA[i11] + 1);
            }
            while (iArrA2 != null && i12 < iArrA2.length && unsignedShort6 <= i22) {
                if (unsignedShort6 == i22) {
                    int iA3 = a(context, iArrA2[i12]);
                    a(iA3 + 2, cArr, true, methodVisitor.visitInsnAnnotation(context.i, context.j, readUTF8(iA3, cArr), false));
                }
                i12++;
                unsignedShort6 = (i12 >= iArrA2.length || readByte(iArrA2[i12]) < 67) ? -1 : readUnsignedShort(iArrA2[i12] + 1);
            }
        }
        if (labelArr[i2] != null) {
            methodVisitor.visitLabel(labelArr[i2]);
        }
        if ((context.b & 2) == 0 && i13 != 0) {
            int[] iArr2 = null;
            if (i14 != 0) {
                int i43 = i14 + 2;
                iArr2 = new int[readUnsignedShort(i14) * 3];
                int length = iArr2.length;
                while (length > 0) {
                    int i44 = length - 1;
                    iArr2[i44] = i43 + 6;
                    int i45 = i44 - 1;
                    iArr2[i45] = readUnsignedShort(i43 + 8);
                    length = i45 - 1;
                    iArr2[length] = readUnsignedShort(i43);
                    i43 += 10;
                }
            }
            int i46 = i13 + 2;
            for (int unsignedShort15 = readUnsignedShort(i13); unsignedShort15 > 0; unsignedShort15--) {
                int unsignedShort16 = readUnsignedShort(i46);
                int unsignedShort17 = readUnsignedShort(i46 + 2);
                int unsignedShort18 = readUnsignedShort(i46 + 8);
                String utf84 = null;
                if (iArr2 != null) {
                    int i47 = 0;
                    while (true) {
                        if (i47 >= iArr2.length) {
                            break;
                        }
                        if (iArr2[i47] == unsignedShort16 && iArr2[i47 + 1] == unsignedShort18) {
                            utf84 = readUTF8(iArr2[i47 + 2], cArr);
                        } else {
                            i47 += 3;
                        }
                    }
                }
                methodVisitor.visitLocalVariable(readUTF8(i46 + 4, cArr), readUTF8(i46 + 6, cArr), utf84, labelArr[unsignedShort16], labelArr[unsignedShort16 + unsignedShort17], unsignedShort18);
                i46 += 10;
            }
        }
        if (iArrA != null) {
            for (int i48 = 0; i48 < iArrA.length; i48++) {
                if ((readByte(iArrA[i48]) >> 1) == 32) {
                    int iA4 = a(context, iArrA[i48]);
                    a(iA4 + 2, cArr, true, methodVisitor.visitLocalVariableAnnotation(context.i, context.j, context.l, context.m, context.n, readUTF8(iA4, cArr), true));
                }
            }
        }
        if (iArrA2 != null) {
            for (int i49 = 0; i49 < iArrA2.length; i49++) {
                if ((readByte(iArrA2[i49]) >> 1) == 32) {
                    int iA5 = a(context, iArrA2[i49]);
                    a(iA5 + 2, cArr, true, methodVisitor.visitLocalVariableAnnotation(context.i, context.j, context.l, context.m, context.n, readUTF8(iA5, cArr), false));
                }
            }
        }
        while (attribute2 != null) {
            Attribute attribute3 = attribute2.a;
            attribute2.a = null;
            methodVisitor.visitAttribute(attribute2);
            attribute2 = attribute3;
        }
        methodVisitor.visitMaxs(unsignedShort2, unsignedShort3);
    }

    private int[] a(MethodVisitor methodVisitor, Context context, int i, boolean z) {
        int i2;
        int iA;
        char[] cArr = context.c;
        int[] iArr = new int[readUnsignedShort(i)];
        int i3 = i + 2;
        for (int i4 = 0; i4 < iArr.length; i4++) {
            iArr[i4] = i3;
            int i5 = readInt(i3);
            switch (i5 >>> 24) {
                case 0:
                case 1:
                case 22:
                    i2 = i3 + 2;
                    break;
                case 19:
                case 20:
                case 21:
                    i2 = i3 + 1;
                    break;
                case 64:
                case 65:
                    for (int unsignedShort = readUnsignedShort(i3 + 1); unsignedShort > 0; unsignedShort--) {
                        int unsignedShort2 = readUnsignedShort(i3 + 3);
                        int unsignedShort3 = readUnsignedShort(i3 + 5);
                        readLabel(unsignedShort2, context.h);
                        readLabel(unsignedShort2 + unsignedShort3, context.h);
                        i3 += 6;
                    }
                    i2 = i3 + 3;
                    break;
                case 71:
                case 72:
                case 73:
                case 74:
                case 75:
                    i2 = i3 + 4;
                    break;
                default:
                    i2 = i3 + 3;
                    break;
            }
            int i6 = readByte(i2);
            if ((i5 >>> 24) == 66) {
                TypePath typePath = i6 == 0 ? null : new TypePath(this.b, i2);
                int i7 = i2 + 1 + (2 * i6);
                iA = a(i7 + 2, cArr, true, methodVisitor.visitTryCatchAnnotation(i5, typePath, readUTF8(i7, cArr), z));
            } else {
                iA = a(i2 + 3 + (2 * i6), cArr, true, (AnnotationVisitor) null);
            }
            i3 = iA;
        }
        return iArr;
    }

    private int a(Context context, int i) {
        int i2;
        int i3;
        int i4 = readInt(i);
        switch (i4 >>> 24) {
            case 0:
            case 1:
            case 22:
                i2 = i4 & org.objectweb.asm.Opcodes.V_PREVIEW_EXPERIMENTAL;
                i3 = i + 2;
                break;
            case 19:
            case 20:
            case 21:
                i2 = i4 & (-16777216);
                i3 = i + 1;
                break;
            case 64:
            case 65:
                i2 = i4 & (-16777216);
                int unsignedShort = readUnsignedShort(i + 1);
                context.l = new Label[unsignedShort];
                context.m = new Label[unsignedShort];
                context.n = new int[unsignedShort];
                i3 = i + 3;
                for (int i5 = 0; i5 < unsignedShort; i5++) {
                    int unsignedShort2 = readUnsignedShort(i3);
                    int unsignedShort3 = readUnsignedShort(i3 + 2);
                    context.l[i5] = readLabel(unsignedShort2, context.h);
                    context.m[i5] = readLabel(unsignedShort2 + unsignedShort3, context.h);
                    context.n[i5] = readUnsignedShort(i3 + 4);
                    i3 += 6;
                }
                break;
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
                i2 = i4 & (-16776961);
                i3 = i + 4;
                break;
            default:
                i2 = i4 & ((i4 >>> 24) < 67 ? -256 : -16777216);
                i3 = i + 3;
                break;
        }
        int i6 = readByte(i3);
        context.i = i2;
        context.j = i6 == 0 ? null : new TypePath(this.b, i3);
        return i3 + 1 + (2 * i6);
    }

    private void b(MethodVisitor methodVisitor, Context context, int i, boolean z) {
        int iA = i + 1;
        int i2 = this.b[i] & 255;
        int length = Type.getArgumentTypes(context.g).length - i2;
        int i3 = 0;
        while (i3 < length) {
            AnnotationVisitor annotationVisitorVisitParameterAnnotation = methodVisitor.visitParameterAnnotation(i3, "Ljava/lang/Synthetic;", false);
            if (annotationVisitorVisitParameterAnnotation != null) {
                annotationVisitorVisitParameterAnnotation.visitEnd();
            }
            i3++;
        }
        char[] cArr = context.c;
        while (i3 < i2 + length) {
            iA += 2;
            for (int unsignedShort = readUnsignedShort(iA); unsignedShort > 0; unsignedShort--) {
                iA = a(iA + 2, cArr, true, methodVisitor.visitParameterAnnotation(i3, readUTF8(iA, cArr), z));
            }
            i3++;
        }
    }

    private int a(int i, char[] cArr, boolean z, AnnotationVisitor annotationVisitor) {
        int unsignedShort = readUnsignedShort(i);
        int iA = i + 2;
        if (z) {
            while (unsignedShort > 0) {
                iA = a(iA + 2, cArr, readUTF8(iA, cArr), annotationVisitor);
                unsignedShort--;
            }
        } else {
            while (unsignedShort > 0) {
                iA = a(iA, cArr, (String) null, annotationVisitor);
                unsignedShort--;
            }
        }
        if (annotationVisitor != null) {
            annotationVisitor.visitEnd();
        }
        return iA;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: ModVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't remove SSA var: r3v48 ??, still in use, count: 3, list:
          (r3v48 ?? I:java.lang.Object) from 0x0169: INVOKE (r13v0 java.lang.String), (r3v48 ?? I:java.lang.String), (r3v48 ?? I:java.lang.Object) VIRTUAL call: aj.org.objectweb.asm.AnnotationVisitor.visit(java.lang.String, java.lang.Object):void A[MD:(java.lang.String, java.lang.Object):void (m)]
          (r3v48 ?? I:java.lang.String) from 0x0169: INVOKE (r13v0 java.lang.String), (r3v48 ?? I:java.lang.String), (r3v48 ?? I:java.lang.Object) VIRTUAL call: aj.org.objectweb.asm.AnnotationVisitor.visit(java.lang.String, java.lang.Object):void A[MD:(java.lang.String, java.lang.Object):void (m)]
          (r3v48 ?? I:byte) from 0x0166: CONSTRUCTOR (r2v60 java.lang.Byte) = (r3v48 ?? I:byte) A[MD:(byte):void (c)] call: java.lang.Byte.<init>(byte):void type: CONSTRUCTOR
        	at jadx.core.utils.InsnRemover.removeSsaVar(InsnRemover.java:162)
        	at jadx.core.utils.InsnRemover.unbindResult(InsnRemover.java:127)
        	at jadx.core.utils.InsnRemover.unbindInsn(InsnRemover.java:91)
        	at jadx.core.utils.InsnRemover.addAndUnbind(InsnRemover.java:57)
        	at jadx.core.dex.visitors.ModVisitor.removeStep(ModVisitor.java:463)
        	at jadx.core.dex.visitors.ModVisitor.visit(ModVisitor.java:97)
        */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v35, types: [char, java.lang.Character, java.lang.Object, java.lang.String] */
    /* JADX WARN: Type inference failed for: r3v40, types: [java.lang.Object, java.lang.Short, java.lang.String, short] */
    /* JADX WARN: Type inference failed for: r3v48, types: [byte, java.lang.Byte, java.lang.Object, java.lang.String] */
    private int a(int r11, char[] r12, java.lang.String r13, aj.org.objectweb.asm.AnnotationVisitor r14) {
        /*
            Method dump skipped, instructions count: 1227
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: aj.org.objectweb.asm.ClassReader.a(int, char[], java.lang.String, aj.org.objectweb.asm.AnnotationVisitor):int");
    }

    private void a(Context context) {
        String str = context.g;
        Object[] objArr = context.s;
        int i = 0;
        if ((context.e & 8) == 0) {
            if ("<init>".equals(context.f)) {
                i = 0 + 1;
                objArr[0] = Opcodes.UNINITIALIZED_THIS;
            } else {
                i = 0 + 1;
                objArr[0] = readClass(this.header + 2, context.c);
            }
        }
        int i2 = 1;
        while (true) {
            int i3 = i2;
            int i4 = i2;
            i2++;
            switch (str.charAt(i4)) {
                case 'B':
                case 'C':
                case 'I':
                case 'S':
                case 'Z':
                    int i5 = i;
                    i++;
                    objArr[i5] = Opcodes.INTEGER;
                    break;
                case 'D':
                    int i6 = i;
                    i++;
                    objArr[i6] = Opcodes.DOUBLE;
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
                    context.q = i;
                    return;
                case 'F':
                    int i7 = i;
                    i++;
                    objArr[i7] = Opcodes.FLOAT;
                    break;
                case 'J':
                    int i8 = i;
                    i++;
                    objArr[i8] = Opcodes.LONG;
                    break;
                case 'L':
                    while (str.charAt(i2) != ';') {
                        i2++;
                    }
                    int i9 = i;
                    i++;
                    int i10 = i2;
                    i2++;
                    objArr[i9] = str.substring(i3 + 1, i10);
                    break;
                case '[':
                    while (str.charAt(i2) == '[') {
                        i2++;
                    }
                    if (str.charAt(i2) == 'L') {
                        do {
                            i2++;
                        } while (str.charAt(i2) != ';');
                    }
                    int i11 = i;
                    i++;
                    i2++;
                    objArr[i11] = str.substring(i3, i2);
                    break;
            }
        }
    }

    private int a(int i, boolean z, boolean z2, Context context) {
        int i2;
        int unsignedShort;
        char[] cArr = context.c;
        Label[] labelArr = context.h;
        if (z) {
            i++;
            i2 = this.b[i] & 255;
        } else {
            i2 = 255;
            context.o = -1;
        }
        context.r = 0;
        if (i2 < 64) {
            unsignedShort = i2;
            context.p = 3;
            context.t = 0;
        } else if (i2 < 128) {
            unsignedShort = i2 - 64;
            i = a(context.u, 0, i, cArr, labelArr);
            context.p = 4;
            context.t = 1;
        } else {
            unsignedShort = readUnsignedShort(i);
            i += 2;
            if (i2 == 247) {
                i = a(context.u, 0, i, cArr, labelArr);
                context.p = 4;
                context.t = 1;
            } else if (i2 >= 248 && i2 < 251) {
                context.p = 2;
                context.r = 251 - i2;
                context.q -= context.r;
                context.t = 0;
            } else if (i2 == 251) {
                context.p = 3;
                context.t = 0;
            } else if (i2 < 255) {
                int i3 = z2 ? context.q : 0;
                for (int i4 = i2 - 251; i4 > 0; i4--) {
                    int i5 = i3;
                    i3++;
                    i = a(context.s, i5, i, cArr, labelArr);
                }
                context.p = 1;
                context.r = i2 - 251;
                context.q += context.r;
                context.t = 0;
            } else {
                context.p = 0;
                int unsignedShort2 = readUnsignedShort(i);
                int iA = i + 2;
                context.r = unsignedShort2;
                context.q = unsignedShort2;
                int i6 = 0;
                while (unsignedShort2 > 0) {
                    int i7 = i6;
                    i6++;
                    iA = a(context.s, i7, iA, cArr, labelArr);
                    unsignedShort2--;
                }
                int unsignedShort3 = readUnsignedShort(iA);
                i = iA + 2;
                context.t = unsignedShort3;
                int i8 = 0;
                while (unsignedShort3 > 0) {
                    int i9 = i8;
                    i8++;
                    i = a(context.u, i9, i, cArr, labelArr);
                    unsignedShort3--;
                }
            }
        }
        context.o += unsignedShort + 1;
        readLabel(context.o, labelArr);
        return i;
    }

    private int a(Object[] objArr, int i, int i2, char[] cArr, Label[] labelArr) {
        int i3 = i2 + 1;
        switch (this.b[i2] & 255) {
            case 0:
                objArr[i] = Opcodes.TOP;
                break;
            case 1:
                objArr[i] = Opcodes.INTEGER;
                break;
            case 2:
                objArr[i] = Opcodes.FLOAT;
                break;
            case 3:
                objArr[i] = Opcodes.DOUBLE;
                break;
            case 4:
                objArr[i] = Opcodes.LONG;
                break;
            case 5:
                objArr[i] = Opcodes.NULL;
                break;
            case 6:
                objArr[i] = Opcodes.UNINITIALIZED_THIS;
                break;
            case 7:
                objArr[i] = readClass(i3, cArr);
                i3 += 2;
                break;
            default:
                objArr[i] = readLabel(readUnsignedShort(i3), labelArr);
                i3 += 2;
                break;
        }
        return i3;
    }

    protected Label readLabel(int i, Label[] labelArr) {
        if (labelArr[i] == null) {
            labelArr[i] = new Label();
        }
        return labelArr[i];
    }

    private int a() {
        int unsignedShort = this.header + 8 + (readUnsignedShort(this.header + 6) * 2);
        for (int unsignedShort2 = readUnsignedShort(unsignedShort); unsignedShort2 > 0; unsignedShort2--) {
            for (int unsignedShort3 = readUnsignedShort(unsignedShort + 8); unsignedShort3 > 0; unsignedShort3--) {
                unsignedShort += 6 + readInt(unsignedShort + 12);
            }
            unsignedShort += 8;
        }
        int i = unsignedShort + 2;
        for (int unsignedShort4 = readUnsignedShort(i); unsignedShort4 > 0; unsignedShort4--) {
            for (int unsignedShort5 = readUnsignedShort(i + 8); unsignedShort5 > 0; unsignedShort5--) {
                i += 6 + readInt(i + 12);
            }
            i += 8;
        }
        return i + 2;
    }

    private Attribute a(Attribute[] attributeArr, String str, int i, int i2, char[] cArr, int i3, Label[] labelArr) {
        for (int i4 = 0; i4 < attributeArr.length; i4++) {
            if (attributeArr[i4].type.equals(str)) {
                return attributeArr[i4].read(this, i, i2, cArr, i3, labelArr);
            }
        }
        return new Attribute(str).read(this, i, i2, null, -1, null);
    }

    public int getItemCount() {
        return this.a.length;
    }

    public int getItem(int i) {
        return this.a[i];
    }

    public int getMaxStringLength() {
        return this.d;
    }

    public int readByte(int i) {
        return this.b[i] & 255;
    }

    public int readUnsignedShort(int i) {
        byte[] bArr = this.b;
        return ((bArr[i] & 255) << 8) | (bArr[i + 1] & 255);
    }

    public short readShort(int i) {
        byte[] bArr = this.b;
        return (short) (((bArr[i] & 255) << 8) | (bArr[i + 1] & 255));
    }

    public int readInt(int i) {
        byte[] bArr = this.b;
        return ((bArr[i] & 255) << 24) | ((bArr[i + 1] & 255) << 16) | ((bArr[i + 2] & 255) << 8) | (bArr[i + 3] & 255);
    }

    public long readLong(int i) {
        return (readInt(i) << 32) | (readInt(i + 4) & 4294967295L);
    }

    public String readUTF8(int i, char[] cArr) {
        int unsignedShort = readUnsignedShort(i);
        if (i == 0 || unsignedShort == 0) {
            return null;
        }
        String str = this.c[unsignedShort];
        if (str != null) {
            return str;
        }
        int i2 = this.a[unsignedShort];
        String[] strArr = this.c;
        String strA = a(i2 + 2, readUnsignedShort(i2), cArr);
        strArr[unsignedShort] = strA;
        return strA;
    }

    private String a(int i, int i2, char[] cArr) {
        int i3 = i + i2;
        byte[] bArr = this.b;
        int i4 = 0;
        boolean z = false;
        char c = 0;
        while (i < i3) {
            int i5 = i;
            i++;
            byte b = bArr[i5];
            switch (z) {
                case false:
                    int i6 = b & 255;
                    if (i6 >= 128) {
                        if (i6 < 224 && i6 > 191) {
                            c = (char) (i6 & 31);
                            z = true;
                            break;
                        } else {
                            c = (char) (i6 & 15);
                            z = 2;
                            break;
                        }
                    } else {
                        int i7 = i4;
                        i4++;
                        cArr[i7] = (char) i6;
                        break;
                    }
                case true:
                    int i8 = i4;
                    i4++;
                    cArr[i8] = (char) ((c << 6) | (b & 63));
                    z = false;
                    break;
                case true:
                    c = (char) ((c << 6) | (b & 63));
                    z = true;
                    break;
            }
        }
        return new String(cArr, 0, i4);
    }

    public String readClass(int i, char[] cArr) {
        return readUTF8(this.a[readUnsignedShort(i)], cArr);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: ModVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't remove SSA var: r1v26 ??, still in use, count: 2, list:
          (r1v26 ?? I:java.lang.Object) from 0x0062: RETURN (r1v26 ?? I:java.lang.Object)
          (r1v26 ?? I:int) from 0x005f: CONSTRUCTOR (r0v41 ?? I:java.lang.Integer) = (r1v26 ?? I:int) A[MD:(int):void (c)] call: java.lang.Integer.<init>(int):void type: CONSTRUCTOR
        	at jadx.core.utils.InsnRemover.removeSsaVar(InsnRemover.java:162)
        	at jadx.core.utils.InsnRemover.unbindResult(InsnRemover.java:127)
        	at jadx.core.utils.InsnRemover.unbindInsn(InsnRemover.java:91)
        	at jadx.core.utils.InsnRemover.addAndUnbind(InsnRemover.java:57)
        	at jadx.core.dex.visitors.ModVisitor.removeStep(ModVisitor.java:463)
        	at jadx.core.dex.visitors.ModVisitor.visit(ModVisitor.java:97)
        */
    /* JADX WARN: Type inference failed for: r1v17, types: [double, java.lang.Double, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r1v20, types: [java.lang.Long, java.lang.Object, long] */
    /* JADX WARN: Type inference failed for: r1v23, types: [float, java.lang.Float, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r1v26, types: [int, java.lang.Integer, java.lang.Object] */
    public java.lang.Object readConst(int r8, char[] r9) {
        /*
            Method dump skipped, instructions count: 260
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: aj.org.objectweb.asm.ClassReader.readConst(int, char[]):java.lang.Object");
    }
}
