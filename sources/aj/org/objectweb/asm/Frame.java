package aj.org.objectweb.asm;

import net.dongliu.apk.parser.struct.resource.ResourceTableMap;
import org.bouncycastle.pqc.crypto.qteslarnd1.Parameter;

/* loaded from: aspectjweaver-1.8.14.jar:aj/org/objectweb/asm/Frame.class */
final class Frame {
    static final int[] a;
    Label b;
    int[] c;
    int[] d;
    private int[] e;
    private int[] f;
    private int g;
    private int h;
    private int[] i;

    Frame() {
    }

    private int a(int i) {
        if (this.e == null || i >= this.e.length) {
            return 33554432 | i;
        }
        int i2 = this.e[i];
        if (i2 == 0) {
            int i3 = 33554432 | i;
            this.e[i] = i3;
            i2 = i3;
        }
        return i2;
    }

    private void a(int i, int i2) {
        if (this.e == null) {
            this.e = new int[10];
        }
        int length = this.e.length;
        if (i >= length) {
            int[] iArr = new int[Math.max(i + 1, 2 * length)];
            System.arraycopy(this.e, 0, iArr, 0, length);
            this.e = iArr;
        }
        this.e[i] = i2;
    }

    private void b(int i) {
        if (this.f == null) {
            this.f = new int[10];
        }
        int length = this.f.length;
        if (this.g >= length) {
            int[] iArr = new int[Math.max(this.g + 1, 2 * length)];
            System.arraycopy(this.f, 0, iArr, 0, length);
            this.f = iArr;
        }
        int[] iArr2 = this.f;
        int i2 = this.g;
        this.g = i2 + 1;
        iArr2[i2] = i;
        int i3 = this.b.f + this.g;
        if (i3 > this.b.g) {
            this.b.g = i3;
        }
    }

    private void a(ClassWriter classWriter, String str) {
        int iB = b(classWriter, str);
        if (iB != 0) {
            b(iB);
            if (iB == 16777220 || iB == 16777219) {
                b(16777216);
            }
        }
    }

    private static int b(ClassWriter classWriter, String str) {
        int iM1c;
        int iIndexOf = str.charAt(0) == '(' ? str.indexOf(41) + 1 : 0;
        switch (str.charAt(iIndexOf)) {
            case 'B':
            case 'C':
            case 'I':
            case 'S':
            case 'Z':
                return ResourceTableMap.MapAttr.MIN;
            case 'D':
                return ResourceTableMap.MapAttr.L10N;
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
            case 'W':
            case 'X':
            case 'Y':
            default:
                int i = iIndexOf + 1;
                while (str.charAt(i) == '[') {
                    i++;
                }
                switch (str.charAt(i)) {
                    case 'B':
                        iM1c = 16777226;
                        break;
                    case 'C':
                        iM1c = 16777227;
                        break;
                    case 'D':
                        iM1c = 16777219;
                        break;
                    case 'E':
                    case 'G':
                    case 'H':
                    case 'K':
                    case 'L':
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
                        iM1c = 24117248 | classWriter.m1c(str.substring(i + 1, str.length() - 1));
                        break;
                    case 'F':
                        iM1c = 16777218;
                        break;
                    case 'I':
                        iM1c = 16777217;
                        break;
                    case 'J':
                        iM1c = 16777220;
                        break;
                    case 'S':
                        iM1c = 16777228;
                        break;
                    case 'Z':
                        iM1c = 16777225;
                        break;
                }
                return ((i - iIndexOf) << 28) | iM1c;
            case 'F':
                return ResourceTableMap.MapAttr.MAX;
            case 'J':
                return ResourceTableMap.MapAttr.OTHER;
            case 'L':
                return 24117248 | classWriter.m1c(str.substring(iIndexOf + 1, str.length() - 1));
            case 'V':
                return 0;
        }
    }

    private int a() {
        if (this.g > 0) {
            int[] iArr = this.f;
            int i = this.g - 1;
            this.g = i;
            return iArr[i];
        }
        Label label = this.b;
        int i2 = label.f - 1;
        label.f = i2;
        return 50331648 | (-i2);
    }

    private void c(int i) {
        if (this.g >= i) {
            this.g -= i;
            return;
        }
        this.b.f -= i - this.g;
        this.g = 0;
    }

    private void a(String str) {
        char cCharAt = str.charAt(0);
        if (cCharAt == '(') {
            c((Type.getArgumentsAndReturnSizes(str) >> 2) - 1);
        } else if (cCharAt == 'J' || cCharAt == 'D') {
            c(2);
        } else {
            c(1);
        }
    }

    private void d(int i) {
        if (this.i == null) {
            this.i = new int[2];
        }
        int length = this.i.length;
        if (this.h >= length) {
            int[] iArr = new int[Math.max(this.h + 1, 2 * length)];
            System.arraycopy(this.i, 0, iArr, 0, length);
            this.i = iArr;
        }
        int[] iArr2 = this.i;
        int i2 = this.h;
        this.h = i2 + 1;
        iArr2[i2] = i;
    }

    private int a(ClassWriter classWriter, int i) {
        int iM1c;
        if (i == 16777222) {
            iM1c = 24117248 | classWriter.m1c(classWriter.I);
        } else {
            if ((i & (-1048576)) != 25165824) {
                return i;
            }
            iM1c = 24117248 | classWriter.m1c(classWriter.H[i & 1048575].g);
        }
        for (int i2 = 0; i2 < this.h; i2++) {
            int i3 = this.i[i2];
            int i4 = i3 & (-268435456);
            int i5 = i3 & 251658240;
            if (i5 == 33554432) {
                i3 = i4 + this.c[i3 & Parameter.B_III_P];
            } else if (i5 == 50331648) {
                i3 = i4 + this.d[this.d.length - (i3 & Parameter.B_III_P)];
            }
            if (i == i3) {
                return iM1c;
            }
        }
        return i;
    }

    void a(ClassWriter classWriter, int i, Type[] typeArr, int i2) {
        this.c = new int[i2];
        this.d = new int[0];
        int i3 = 0;
        if ((i & 8) == 0) {
            if ((i & 524288) == 0) {
                i3 = 0 + 1;
                this.c[0] = 24117248 | classWriter.m1c(classWriter.I);
            } else {
                i3 = 0 + 1;
                this.c[0] = 16777222;
            }
        }
        for (Type type : typeArr) {
            int iB = b(classWriter, type.getDescriptor());
            int i4 = i3;
            i3++;
            this.c[i4] = iB;
            if (iB == 16777220 || iB == 16777219) {
                i3++;
                this.c[i3] = 16777216;
            }
        }
        while (i3 < i2) {
            int i5 = i3;
            i3++;
            this.c[i5] = 16777216;
        }
    }

    void a(int i, int i2, ClassWriter classWriter, Item item) {
        switch (i) {
            case 0:
            case 116:
            case 117:
            case 118:
            case 119:
            case 145:
            case 146:
            case 147:
            case 167:
            case 177:
                return;
            case 1:
                b(ResourceTableMap.MapAttr.ZERO);
                return;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 16:
            case 17:
            case 21:
                b(ResourceTableMap.MapAttr.MIN);
                return;
            case 9:
            case 10:
            case 22:
                b(ResourceTableMap.MapAttr.OTHER);
                b(16777216);
                return;
            case 11:
            case 12:
            case 13:
            case 23:
                b(ResourceTableMap.MapAttr.MAX);
                return;
            case 14:
            case 15:
            case 24:
                b(ResourceTableMap.MapAttr.L10N);
                b(16777216);
                return;
            case 18:
                switch (item.b) {
                    case 3:
                        b(ResourceTableMap.MapAttr.MIN);
                        return;
                    case 4:
                        b(ResourceTableMap.MapAttr.MAX);
                        return;
                    case 5:
                        b(ResourceTableMap.MapAttr.OTHER);
                        b(16777216);
                        return;
                    case 6:
                        b(ResourceTableMap.MapAttr.L10N);
                        b(16777216);
                        return;
                    case 7:
                        b(24117248 | classWriter.m1c("java/lang/Class"));
                        return;
                    case 8:
                        b(24117248 | classWriter.m1c("java/lang/String"));
                        return;
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    default:
                        b(24117248 | classWriter.m1c("java/lang/invoke/MethodHandle"));
                        return;
                    case 16:
                        b(24117248 | classWriter.m1c("java/lang/invoke/MethodType"));
                        return;
                }
            case 19:
            case 20:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 196:
            case 197:
            default:
                c(i2);
                a(classWriter, item.g);
                return;
            case 25:
                b(a(i2));
                return;
            case 46:
            case 51:
            case 52:
            case 53:
                c(2);
                b(ResourceTableMap.MapAttr.MIN);
                return;
            case 47:
            case 143:
                c(2);
                b(ResourceTableMap.MapAttr.OTHER);
                b(16777216);
                return;
            case 48:
                c(2);
                b(ResourceTableMap.MapAttr.MAX);
                return;
            case 49:
            case 138:
                c(2);
                b(ResourceTableMap.MapAttr.L10N);
                b(16777216);
                return;
            case 50:
                c(1);
                b((-268435456) + a());
                return;
            case 54:
            case 56:
            case 58:
                a(i2, a());
                if (i2 > 0) {
                    int iA = a(i2 - 1);
                    if (iA == 16777220 || iA == 16777219) {
                        a(i2 - 1, 16777216);
                        return;
                    } else {
                        if ((iA & 251658240) != 16777216) {
                            a(i2 - 1, iA | 8388608);
                            return;
                        }
                        return;
                    }
                }
                return;
            case 55:
            case 57:
                c(1);
                a(i2, a());
                a(i2 + 1, 16777216);
                if (i2 > 0) {
                    int iA2 = a(i2 - 1);
                    if (iA2 == 16777220 || iA2 == 16777219) {
                        a(i2 - 1, 16777216);
                        return;
                    } else {
                        if ((iA2 & 251658240) != 16777216) {
                            a(i2 - 1, iA2 | 8388608);
                            return;
                        }
                        return;
                    }
                }
                return;
            case 79:
            case 81:
            case 83:
            case 84:
            case 85:
            case 86:
                c(3);
                return;
            case 80:
            case 82:
                c(4);
                return;
            case 87:
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 170:
            case 171:
            case 172:
            case 174:
            case 176:
            case 191:
            case 194:
            case 195:
            case 198:
            case 199:
                c(1);
                return;
            case 88:
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 173:
            case 175:
                c(2);
                return;
            case 89:
                int iA3 = a();
                b(iA3);
                b(iA3);
                return;
            case 90:
                int iA4 = a();
                int iA5 = a();
                b(iA4);
                b(iA5);
                b(iA4);
                return;
            case 91:
                int iA6 = a();
                int iA7 = a();
                int iA8 = a();
                b(iA6);
                b(iA8);
                b(iA7);
                b(iA6);
                return;
            case 92:
                int iA9 = a();
                int iA10 = a();
                b(iA10);
                b(iA9);
                b(iA10);
                b(iA9);
                return;
            case 93:
                int iA11 = a();
                int iA12 = a();
                int iA13 = a();
                b(iA12);
                b(iA11);
                b(iA13);
                b(iA12);
                b(iA11);
                return;
            case 94:
                int iA14 = a();
                int iA15 = a();
                int iA16 = a();
                int iA17 = a();
                b(iA15);
                b(iA14);
                b(iA17);
                b(iA16);
                b(iA15);
                b(iA14);
                return;
            case 95:
                int iA18 = a();
                int iA19 = a();
                b(iA18);
                b(iA19);
                return;
            case 96:
            case 100:
            case 104:
            case 108:
            case 112:
            case 120:
            case 122:
            case 124:
            case 126:
            case 128:
            case 130:
            case 136:
            case 142:
            case 149:
            case 150:
                c(2);
                b(ResourceTableMap.MapAttr.MIN);
                return;
            case 97:
            case 101:
            case 105:
            case 109:
            case 113:
            case 127:
            case 129:
            case 131:
                c(4);
                b(ResourceTableMap.MapAttr.OTHER);
                b(16777216);
                return;
            case 98:
            case 102:
            case 106:
            case 110:
            case 114:
            case 137:
            case 144:
                c(2);
                b(ResourceTableMap.MapAttr.MAX);
                return;
            case 99:
            case 103:
            case 107:
            case 111:
            case 115:
                c(4);
                b(ResourceTableMap.MapAttr.L10N);
                b(16777216);
                return;
            case 121:
            case 123:
            case 125:
                c(3);
                b(ResourceTableMap.MapAttr.OTHER);
                b(16777216);
                return;
            case 132:
                a(i2, ResourceTableMap.MapAttr.MIN);
                return;
            case 133:
            case 140:
                c(1);
                b(ResourceTableMap.MapAttr.OTHER);
                b(16777216);
                return;
            case 134:
                c(1);
                b(ResourceTableMap.MapAttr.MAX);
                return;
            case 135:
            case 141:
                c(1);
                b(ResourceTableMap.MapAttr.L10N);
                b(16777216);
                return;
            case 139:
            case 190:
            case 193:
                c(1);
                b(ResourceTableMap.MapAttr.MIN);
                return;
            case 148:
            case 151:
            case 152:
                c(4);
                b(ResourceTableMap.MapAttr.MIN);
                return;
            case 168:
            case 169:
                throw new RuntimeException("JSR/RET are not supported with computeFrames option");
            case 178:
                a(classWriter, item.i);
                return;
            case 179:
                a(item.i);
                return;
            case 180:
                c(1);
                a(classWriter, item.i);
                return;
            case 181:
                a(item.i);
                a();
                return;
            case 182:
            case 183:
            case 184:
            case 185:
                a(item.i);
                if (i != 184) {
                    int iA20 = a();
                    if (i == 183 && item.h.charAt(0) == '<') {
                        d(iA20);
                    }
                }
                a(classWriter, item.i);
                return;
            case 186:
                a(item.h);
                a(classWriter, item.h);
                return;
            case 187:
                b(25165824 | classWriter.a(item.g, i2));
                return;
            case 188:
                a();
                switch (i2) {
                    case 4:
                        b(285212681);
                        return;
                    case 5:
                        b(285212683);
                        return;
                    case 6:
                        b(285212674);
                        return;
                    case 7:
                        b(285212675);
                        return;
                    case 8:
                        b(285212682);
                        return;
                    case 9:
                        b(285212684);
                        return;
                    case 10:
                        b(285212673);
                        return;
                    default:
                        b(285212676);
                        return;
                }
            case 189:
                String str = item.g;
                a();
                if (str.charAt(0) == '[') {
                    a(classWriter, new StringBuffer().append('[').append(str).toString());
                    return;
                } else {
                    b(292552704 | classWriter.m1c(str));
                    return;
                }
            case 192:
                String str2 = item.g;
                a();
                if (str2.charAt(0) == '[') {
                    a(classWriter, str2);
                    return;
                } else {
                    b(24117248 | classWriter.m1c(str2));
                    return;
                }
        }
    }

    boolean a(ClassWriter classWriter, Frame frame, int i) {
        int iA;
        int iA2;
        int i2;
        boolean zA = false;
        int length = this.c.length;
        int length2 = this.d.length;
        if (frame.c == null) {
            frame.c = new int[length];
            zA = true;
        }
        int i3 = 0;
        while (i3 < length) {
            if (this.e == null || i3 >= this.e.length || (i2 = this.e[i3]) == 0) {
                iA2 = this.c[i3];
            } else {
                int i4 = i2 & (-268435456);
                int i5 = i2 & 251658240;
                if (i5 == 16777216) {
                    iA2 = i2;
                } else {
                    iA2 = i5 == 33554432 ? i4 + this.c[i2 & Parameter.B_III_P] : i4 + this.d[length2 - (i2 & Parameter.B_III_P)];
                    if ((i2 & 8388608) != 0 && (iA2 == 16777220 || iA2 == 16777219)) {
                        iA2 = 16777216;
                    }
                }
            }
            if (this.i != null) {
                iA2 = a(classWriter, iA2);
            }
            zA |= a(classWriter, iA2, frame.c, i3);
            i3++;
        }
        if (i > 0) {
            for (int i6 = 0; i6 < length; i6++) {
                zA |= a(classWriter, this.c[i6], frame.c, i6);
            }
            if (frame.d == null) {
                frame.d = new int[1];
                zA = true;
            }
            return zA | a(classWriter, i, frame.d, 0);
        }
        int length3 = this.d.length + this.b.f;
        if (frame.d == null) {
            frame.d = new int[length3 + this.g];
            zA = true;
        }
        for (int i7 = 0; i7 < length3; i7++) {
            int iA3 = this.d[i7];
            if (this.i != null) {
                iA3 = a(classWriter, iA3);
            }
            zA |= a(classWriter, iA3, frame.d, i7);
        }
        for (int i8 = 0; i8 < this.g; i8++) {
            int i9 = this.f[i8];
            int i10 = i9 & (-268435456);
            int i11 = i9 & 251658240;
            if (i11 == 16777216) {
                iA = i9;
            } else {
                iA = i11 == 33554432 ? i10 + this.c[i9 & Parameter.B_III_P] : i10 + this.d[length2 - (i9 & Parameter.B_III_P)];
                if ((i9 & 8388608) != 0 && (iA == 16777220 || iA == 16777219)) {
                    iA = 16777216;
                }
            }
            if (this.i != null) {
                iA = a(classWriter, iA);
            }
            zA |= a(classWriter, iA, frame.d, length3 + i8);
        }
        return zA;
    }

    private static boolean a(ClassWriter classWriter, int i, int[] iArr, int i2) {
        int iMin;
        int i3 = iArr[i2];
        if (i3 == i) {
            return false;
        }
        if ((i & 268435455) == 16777221) {
            if (i3 == 16777221) {
                return false;
            }
            i = 16777221;
        }
        if (i3 == 0) {
            iArr[i2] = i;
            return true;
        }
        if ((i3 & 267386880) == 24117248 || (i3 & (-268435456)) != 0) {
            if (i == 16777221) {
                return false;
            }
            if ((i & (-1048576)) == (i3 & (-1048576))) {
                iMin = (i3 & 267386880) == 24117248 ? (i & (-268435456)) | 24117248 | classWriter.a(i & 1048575, i3 & 1048575) : ((-268435456) + (i3 & (-268435456))) | 24117248 | classWriter.m1c("java/lang/Object");
            } else if ((i & 267386880) == 24117248 || (i & (-268435456)) != 0) {
                iMin = Math.min((((i & (-268435456)) == 0 || (i & 267386880) == 24117248) ? 0 : -268435456) + (i & (-268435456)), (((i3 & (-268435456)) == 0 || (i3 & 267386880) == 24117248) ? 0 : -268435456) + (i3 & (-268435456))) | 24117248 | classWriter.m1c("java/lang/Object");
            } else {
                iMin = 16777216;
            }
        } else if (i3 == 16777221) {
            iMin = ((i & 267386880) == 24117248 || (i & (-268435456)) != 0) ? i : 16777216;
        } else {
            iMin = 16777216;
        }
        if (i3 == iMin) {
            return false;
        }
        iArr[i2] = iMin;
        return true;
    }

    static {
        _clinit_();
        int[] iArr = new int[202];
        for (int i = 0; i < iArr.length; i++) {
            iArr[i] = "EFFFFFFFFGGFFFGGFFFEEFGFGFEEEEEEEEEEEEEEEEEEEEDEDEDDDDDCDCDEEEEEEEEEEEEEEEEEEEEBABABBBBDCFFFGGGEDCDCDCDCDCDCDCDCDCDCEEEEDDDDDDDCDCDCEFEFDDEEFFDEDEEEBDDBBDDDDDDCCCCCCCCEFEDDDCDCDEEEEEEEEEEFEEEEEEDDEEDDEE".charAt(i) - 'E';
        }
        a = iArr;
    }

    static /* synthetic */ void _clinit_() {
    }
}
