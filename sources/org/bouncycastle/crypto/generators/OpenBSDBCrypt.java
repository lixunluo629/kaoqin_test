package org.bouncycastle.crypto.generators;

import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Set;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Strings;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/generators/OpenBSDBCrypt.class */
public class OpenBSDBCrypt {
    private static final String defaultVersion = "2y";
    private static final byte[] encodingTable = {46, 47, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57};
    private static final byte[] decodingTable = new byte[128];
    private static final Set<String> allowedVersions = new HashSet();

    private static String createBcryptString(String str, byte[] bArr, byte[] bArr2, int i) {
        if (!allowedVersions.contains(str)) {
            throw new IllegalArgumentException("Version " + str + " is not accepted by this implementation.");
        }
        StringBuffer stringBuffer = new StringBuffer(60);
        stringBuffer.append('$');
        stringBuffer.append(str);
        stringBuffer.append('$');
        stringBuffer.append(i < 10 ? "0" + i : Integer.toString(i));
        stringBuffer.append('$');
        stringBuffer.append(encodeData(bArr2));
        stringBuffer.append(encodeData(BCrypt.generate(bArr, bArr2, i)));
        return stringBuffer.toString();
    }

    public static String generate(char[] cArr, byte[] bArr, int i) {
        return generate(defaultVersion, cArr, bArr, i);
    }

    public static String generate(String str, char[] cArr, byte[] bArr, int i) {
        if (!allowedVersions.contains(str)) {
            throw new IllegalArgumentException("Version " + str + " is not accepted by this implementation.");
        }
        if (cArr == null) {
            throw new IllegalArgumentException("Password required.");
        }
        if (bArr == null) {
            throw new IllegalArgumentException("Salt required.");
        }
        if (bArr.length != 16) {
            throw new DataLengthException("16 byte salt required: " + bArr.length);
        }
        if (i < 4 || i > 31) {
            throw new IllegalArgumentException("Invalid cost factor.");
        }
        byte[] uTF8ByteArray = Strings.toUTF8ByteArray(cArr);
        byte[] bArr2 = new byte[uTF8ByteArray.length >= 72 ? 72 : uTF8ByteArray.length + 1];
        if (bArr2.length > uTF8ByteArray.length) {
            System.arraycopy(uTF8ByteArray, 0, bArr2, 0, uTF8ByteArray.length);
        } else {
            System.arraycopy(uTF8ByteArray, 0, bArr2, 0, bArr2.length);
        }
        Arrays.fill(uTF8ByteArray, (byte) 0);
        String strCreateBcryptString = createBcryptString(str, bArr2, bArr, i);
        Arrays.fill(bArr2, (byte) 0);
        return strCreateBcryptString;
    }

    public static boolean checkPassword(String str, char[] cArr) throws NumberFormatException {
        if (str.length() != 60) {
            throw new DataLengthException("Bcrypt String length: " + str.length() + ", 60 required.");
        }
        if (str.charAt(0) != '$' || str.charAt(3) != '$' || str.charAt(6) != '$') {
            throw new IllegalArgumentException("Invalid Bcrypt String format.");
        }
        String strSubstring = str.substring(1, 3);
        if (!allowedVersions.contains(strSubstring)) {
            throw new IllegalArgumentException("Bcrypt version '" + strSubstring + "' is not supported by this implementation");
        }
        String strSubstring2 = str.substring(4, 6);
        try {
            int i = Integer.parseInt(strSubstring2);
            if (i < 4 || i > 31) {
                throw new IllegalArgumentException("Invalid cost factor: " + i + ", 4 < cost < 31 expected.");
            }
            if (cArr == null) {
                throw new IllegalArgumentException("Missing password.");
            }
            return str.equals(generate(strSubstring, cArr, decodeSaltString(str.substring(str.lastIndexOf(36) + 1, str.length() - 31)), i));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid cost factor: " + strSubstring2);
        }
    }

    private static String encodeData(byte[] bArr) {
        if (bArr.length != 24 && bArr.length != 16) {
            throw new DataLengthException("Invalid length: " + bArr.length + ", 24 for key or 16 for salt expected");
        }
        boolean z = false;
        if (bArr.length == 16) {
            z = true;
            byte[] bArr2 = new byte[18];
            System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
            bArr = bArr2;
        } else {
            bArr[bArr.length - 1] = 0;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int length = bArr.length;
        for (int i = 0; i < length; i += 3) {
            int i2 = bArr[i] & 255;
            int i3 = bArr[i + 1] & 255;
            int i4 = bArr[i + 2] & 255;
            byteArrayOutputStream.write(encodingTable[(i2 >>> 2) & 63]);
            byteArrayOutputStream.write(encodingTable[((i2 << 4) | (i3 >>> 4)) & 63]);
            byteArrayOutputStream.write(encodingTable[((i3 << 2) | (i4 >>> 6)) & 63]);
            byteArrayOutputStream.write(encodingTable[i4 & 63]);
        }
        String strFromByteArray = Strings.fromByteArray(byteArrayOutputStream.toByteArray());
        return z ? strFromByteArray.substring(0, 22) : strFromByteArray.substring(0, strFromByteArray.length() - 1);
    }

    private static byte[] decodeSaltString(String str) {
        char[] charArray = str.toCharArray();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(16);
        if (charArray.length != 22) {
            throw new DataLengthException("Invalid base64 salt length: " + charArray.length + " , 22 required.");
        }
        for (char c : charArray) {
            if (c > 'z' || c < '.' || (c > '9' && c < 'A')) {
                throw new IllegalArgumentException("Salt string contains invalid character: " + ((int) c));
            }
        }
        char[] cArr = new char[24];
        System.arraycopy(charArray, 0, cArr, 0, charArray.length);
        int length = cArr.length;
        for (int i = 0; i < length; i += 4) {
            byte b = decodingTable[cArr[i]];
            byte b2 = decodingTable[cArr[i + 1]];
            byte b3 = decodingTable[cArr[i + 2]];
            byte b4 = decodingTable[cArr[i + 3]];
            byteArrayOutputStream.write((b << 2) | (b2 >> 4));
            byteArrayOutputStream.write((b2 << 4) | (b3 >> 2));
            byteArrayOutputStream.write((b3 << 6) | b4);
        }
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        byte[] bArr = new byte[16];
        System.arraycopy(byteArray, 0, bArr, 0, bArr.length);
        return bArr;
    }

    static {
        allowedVersions.add("2a");
        allowedVersions.add(defaultVersion);
        allowedVersions.add("2b");
        for (int i = 0; i < decodingTable.length; i++) {
            decodingTable[i] = -1;
        }
        for (int i2 = 0; i2 < encodingTable.length; i2++) {
            decodingTable[encodingTable[i2]] = (byte) i2;
        }
    }
}
