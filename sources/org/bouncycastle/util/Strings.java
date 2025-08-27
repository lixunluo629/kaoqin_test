package org.bouncycastle.util;

import java.io.ByteArrayOutputStream;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Vector;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/util/Strings.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/util/Strings.class */
public final class Strings {

    /* renamed from: org.bouncycastle.util.Strings$1, reason: invalid class name */
    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/util/Strings$1.class */
    static class AnonymousClass1 implements PrivilegedAction<String> {
        AnonymousClass1() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        public String run() {
            return System.getProperty("line.separator");
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/util/Strings$StringListImpl.class */
    private static class StringListImpl extends ArrayList<String> implements StringList {
        private StringListImpl() {
        }

        @Override // java.util.ArrayList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean add(String str) {
            return super.add((StringListImpl) str);
        }

        @Override // java.util.ArrayList, java.util.AbstractList, java.util.List
        public String set(int i, String str) {
            return (String) super.set(i, (int) str);
        }

        @Override // java.util.ArrayList, java.util.AbstractList, java.util.List
        public void add(int i, String str) {
            super.add(i, (int) str);
        }

        @Override // org.bouncycastle.util.StringList
        public String[] toStringArray() {
            String[] strArr = new String[size()];
            for (int i = 0; i != strArr.length; i++) {
                strArr[i] = (String) get(i);
            }
            return strArr;
        }

        @Override // org.bouncycastle.util.StringList
        public String[] toStringArray(int i, int i2) {
            String[] strArr = new String[i2 - i];
            for (int i3 = i; i3 != size() && i3 != i2; i3++) {
                strArr[i3 - i] = (String) get(i3);
            }
            return strArr;
        }

        @Override // java.util.ArrayList, java.util.AbstractList, java.util.List, org.bouncycastle.util.StringList
        public /* bridge */ /* synthetic */ String get(int i) {
            return (String) super.get(i);
        }

        /* synthetic */ StringListImpl(AnonymousClass1 anonymousClass1) {
            this();
        }
    }

    public static String fromUTF8ByteArray(byte[] bArr) {
        char c;
        int i = 0;
        int i2 = 0;
        while (i < bArr.length) {
            i2++;
            if ((bArr[i] & 240) == 240) {
                i2++;
                i += 4;
            } else {
                i = (bArr[i] & 224) == 224 ? i + 3 : (bArr[i] & 192) == 192 ? i + 2 : i + 1;
            }
        }
        char[] cArr = new char[i2];
        int i3 = 0;
        int i4 = 0;
        while (i3 < bArr.length) {
            if ((bArr[i3] & 240) == 240) {
                int i5 = (((((bArr[i3] & 3) << 18) | ((bArr[i3 + 1] & 63) << 12)) | ((bArr[i3 + 2] & 63) << 6)) | (bArr[i3 + 3] & 63)) - 65536;
                int i6 = i4;
                i4++;
                cArr[i6] = (char) (55296 | (i5 >> 10));
                c = (char) (56320 | (i5 & 1023));
                i3 += 4;
            } else if ((bArr[i3] & 224) == 224) {
                c = (char) (((bArr[i3] & 15) << 12) | ((bArr[i3 + 1] & 63) << 6) | (bArr[i3 + 2] & 63));
                i3 += 3;
            } else if ((bArr[i3] & 208) == 208) {
                c = (char) (((bArr[i3] & 31) << 6) | (bArr[i3 + 1] & 63));
                i3 += 2;
            } else if ((bArr[i3] & 192) == 192) {
                c = (char) (((bArr[i3] & 31) << 6) | (bArr[i3 + 1] & 63));
                i3 += 2;
            } else {
                c = (char) (bArr[i3] & 255);
                i3++;
            }
            int i7 = i4;
            i4++;
            cArr[i7] = c;
        }
        return new String(cArr);
    }

    public static byte[] toUTF8ByteArray(String str) {
        return toUTF8ByteArray(str.toCharArray());
    }

    public static byte[] toUTF8ByteArray(char[] cArr) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 0;
        while (i < cArr.length) {
            char c = cArr[i];
            if (c < 128) {
                byteArrayOutputStream.write(c);
            } else if (c < 2048) {
                byteArrayOutputStream.write(192 | (c >> 6));
                byteArrayOutputStream.write(128 | (c & '?'));
            } else if (c < 55296 || c > 57343) {
                byteArrayOutputStream.write(224 | (c >> '\f'));
                byteArrayOutputStream.write(128 | ((c >> 6) & 63));
                byteArrayOutputStream.write(128 | (c & '?'));
            } else {
                if (i + 1 >= cArr.length) {
                    throw new IllegalStateException("invalid UTF-16 codepoint");
                }
                i++;
                char c2 = cArr[i];
                if (c > 56319) {
                    throw new IllegalStateException("invalid UTF-16 codepoint");
                }
                int i2 = (((c & 1023) << 10) | (c2 & 1023)) + 65536;
                byteArrayOutputStream.write(240 | (i2 >> 18));
                byteArrayOutputStream.write(128 | ((i2 >> 12) & 63));
                byteArrayOutputStream.write(128 | ((i2 >> 6) & 63));
                byteArrayOutputStream.write(128 | (i2 & 63));
            }
            i++;
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static String toUpperCase(String str) {
        boolean z = false;
        char[] charArray = str.toCharArray();
        for (int i = 0; i != charArray.length; i++) {
            char c = charArray[i];
            if ('a' <= c && 'z' >= c) {
                z = true;
                charArray[i] = (char) ((c - 'a') + 65);
            }
        }
        return z ? new String(charArray) : str;
    }

    public static String toLowerCase(String str) {
        boolean z = false;
        char[] charArray = str.toCharArray();
        for (int i = 0; i != charArray.length; i++) {
            char c = charArray[i];
            if ('A' <= c && 'Z' >= c) {
                z = true;
                charArray[i] = (char) ((c - 'A') + 97);
            }
        }
        return z ? new String(charArray) : str;
    }

    public static byte[] toByteArray(char[] cArr) {
        byte[] bArr = new byte[cArr.length];
        for (int i = 0; i != bArr.length; i++) {
            bArr[i] = (byte) cArr[i];
        }
        return bArr;
    }

    public static byte[] toByteArray(String str) {
        byte[] bArr = new byte[str.length()];
        for (int i = 0; i != bArr.length; i++) {
            bArr[i] = (byte) str.charAt(i);
        }
        return bArr;
    }

    public static String[] split(String str, char c) {
        Vector vector = new Vector();
        boolean z = true;
        while (z) {
            int iIndexOf = str.indexOf(c);
            if (iIndexOf > 0) {
                vector.addElement(str.substring(0, iIndexOf));
                str = str.substring(iIndexOf + 1);
            } else {
                z = false;
                vector.addElement(str);
            }
        }
        String[] strArr = new String[vector.size()];
        for (int i = 0; i != strArr.length; i++) {
            strArr[i] = (String) vector.elementAt(i);
        }
        return strArr;
    }
}
