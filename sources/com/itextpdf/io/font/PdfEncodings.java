package com.itextpdf.io.font;

import com.drew.metadata.exif.makernotes.OlympusMakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusRawInfoMakernoteDirectory;
import com.drew.metadata.exif.makernotes.SonyType1MakernoteDirectory;
import com.itextpdf.io.util.EncodingUtil;
import com.itextpdf.io.util.IntHashtable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ddf.EscherProperties;
import org.aspectj.apache.bcel.Constants;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/PdfEncodings.class */
public class PdfEncodings {
    public static final String IDENTITY_H = "Identity-H";
    public static final String IDENTITY_V = "Identity-V";
    public static final String CP1250 = "Cp1250";
    public static final String CP1252 = "Cp1252";
    public static final String CP1253 = "Cp1253";
    public static final String CP1257 = "Cp1257";
    public static final String WINANSI = "Cp1252";
    public static final String MACROMAN = "MacRoman";
    public static final String SYMBOL = "Symbol";
    public static final String ZAPFDINGBATS = "ZapfDingbats";
    public static final String UNICODE_BIG = "UnicodeBig";
    public static final String UNICODE_BIG_UNMARKED = "UnicodeBigUnmarked";
    public static final String PDF_DOC_ENCODING = "PDF";
    public static final String UTF8 = "UTF-8";
    private static final String EMPTY_STRING = "";
    private static final char[] winansiByteToChar = {0, 1, 2, 3, 4, 5, 6, 7, '\b', '\t', '\n', 11, '\f', '\r', 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, ' ', '!', '\"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~', 127, 8364, 65533, 8218, 402, 8222, 8230, 8224, 8225, 710, 8240, 352, 8249, 338, 65533, 381, 65533, 65533, 8216, 8217, 8220, 8221, 8226, 8211, 8212, 732, 8482, 353, 8250, 339, 65533, 382, 376, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255};
    private static final char[] pdfEncodingByteToChar = {0, 1, 2, 3, 4, 5, 6, 7, '\b', '\t', '\n', 11, '\f', '\r', 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, ' ', '!', '\"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~', 127, 8226, 8224, 8225, 8230, 8212, 8211, 402, 8260, 8249, 8250, 8722, 8240, 8222, 8220, 8221, 8216, 8217, 8218, 8482, 64257, 64258, 321, 338, 352, 376, 381, 305, 322, 339, 353, 382, 65533, 8364, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255};
    static final int[] standardEncoding = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 32, 33, 34, 35, 36, 37, 38, 8217, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 8216, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 161, 162, 163, 8260, 165, 402, 167, 164, 39, 8220, 171, 8249, 8250, 64257, 64258, 0, SonyType1MakernoteDirectory.TAG_DISTORTION_CORRECTION, 8224, OlympusRawInfoMakernoteDirectory.TagCmHue, 183, 0, 182, 8226, 8218, SonyType1MakernoteDirectory.TAG_AF_POINT_SELECTED, 8221, 187, 8230, OlympusMakernoteDirectory.TAG_RAW_DEVELOPMENT, 0, 191, 0, 96, 180, EscherProperties.THREEDSTYLE__ROTATIONCENTERX, 732, 175, EscherProperties.THREEDSTYLE__FILLY, EscherProperties.THREEDSTYLE__FILLZ, 168, 0, EscherProperties.THREEDSTYLE__FILLINTENSITY, 184, 0, 733, 731, EscherProperties.THREEDSTYLE__ROTATIONCENTERY, SonyType1MakernoteDirectory.TAG_WB_SHIFT_AMBER_MAGENTA, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 198, 0, 170, 0, 0, 0, 0, 321, Constants.INVOKESUPER_QUICK, 338, 186, 0, 0, 0, 0, 0, 230, 0, 0, 0, 305, 0, 0, 322, EscherProperties.GEOTEXT__STRETCHCHARHEIGHT, 339, Constants.MULTIANEWARRAY_QUICK, 0, 0, 0, 0};
    private static final IntHashtable winansi = new IntHashtable();
    private static final IntHashtable pdfEncoding = new IntHashtable();
    private static final Map<String, IExtraEncoding> extraEncodings = new HashMap();

    static {
        for (int k = 128; k < 161; k++) {
            char c = winansiByteToChar[k];
            if (c != 65533) {
                winansi.put(c, k);
            }
        }
        for (int k2 = 128; k2 < 161; k2++) {
            char c2 = pdfEncodingByteToChar[k2];
            if (c2 != 65533) {
                pdfEncoding.put(c2, k2);
            }
        }
        addExtraEncoding("Wingdings", new WingdingsConversion());
        addExtraEncoding("Symbol", new SymbolConversion(true));
        addExtraEncoding("ZapfDingbats", new SymbolConversion(false));
        addExtraEncoding("SymbolTT", new SymbolTTConversion());
        addExtraEncoding("Cp437", new Cp437Conversion());
    }

    public static byte[] convertToBytes(String text, String encoding) {
        int c;
        byte[] b;
        if (text == null) {
            return new byte[0];
        }
        if (encoding == null || encoding.length() == 0) {
            int len = text.length();
            byte[] b2 = new byte[len];
            for (int k = 0; k < len; k++) {
                b2[k] = (byte) text.charAt(k);
            }
            return b2;
        }
        IExtraEncoding extra = extraEncodings.get(encoding.toLowerCase());
        if (extra != null && (b = extra.charToByte(text, encoding)) != null) {
            return b;
        }
        IntHashtable hash = null;
        if (encoding.equals("Cp1252")) {
            hash = winansi;
        } else if (encoding.equals(PDF_DOC_ENCODING)) {
            hash = pdfEncoding;
        }
        if (hash != null) {
            char[] cc = text.toCharArray();
            int len2 = cc.length;
            int ptr = 0;
            byte[] b3 = new byte[len2];
            for (char ch2 : cc) {
                if (ch2 < 128 || (ch2 > 160 && ch2 <= 255)) {
                    c = ch2;
                } else {
                    c = hash.get(ch2);
                }
                if (c != 0) {
                    int i = ptr;
                    ptr++;
                    b3[i] = (byte) c;
                }
            }
            if (ptr == len2) {
                return b3;
            }
            byte[] b22 = new byte[ptr];
            System.arraycopy(b3, 0, b22, 0, ptr);
            return b22;
        }
        try {
            return EncodingUtil.convertToBytes(text.toCharArray(), encoding);
        } catch (IOException e) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.CharacterCodeException, (Throwable) e);
        }
    }

    public static byte[] convertToBytes(char ch2, String encoding) {
        int c;
        if (encoding == null || encoding.length() == 0) {
            return new byte[]{(byte) ch2};
        }
        IntHashtable hash = null;
        if (encoding.equals("Cp1252")) {
            hash = winansi;
        } else if (encoding.equals(PDF_DOC_ENCODING)) {
            hash = pdfEncoding;
        }
        if (hash != null) {
            if (ch2 < 128 || (ch2 > 160 && ch2 <= 255)) {
                c = ch2;
            } else {
                c = hash.get(ch2);
            }
            if (c != 0) {
                return new byte[]{(byte) c};
            }
            return new byte[0];
        }
        try {
            return EncodingUtil.convertToBytes(new char[]{ch2}, encoding);
        } catch (IOException e) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.CharacterCodeException, (Throwable) e);
        }
    }

    public static String convertToString(byte[] bytes, String encoding) {
        String text;
        if (bytes == null) {
            return "";
        }
        if (encoding == null || encoding.length() == 0) {
            char[] c = new char[bytes.length];
            for (int k = 0; k < bytes.length; k++) {
                c[k] = (char) (bytes[k] & 255);
            }
            return new String(c);
        }
        IExtraEncoding extra = extraEncodings.get(encoding.toLowerCase());
        if (extra != null && (text = extra.byteToChar(bytes, encoding)) != null) {
            return text;
        }
        char[] ch2 = null;
        if (encoding.equals("Cp1252")) {
            ch2 = winansiByteToChar;
        } else if (encoding.equals(PDF_DOC_ENCODING)) {
            ch2 = pdfEncodingByteToChar;
        }
        if (ch2 != null) {
            int len = bytes.length;
            char[] c2 = new char[len];
            for (int k2 = 0; k2 < len; k2++) {
                c2[k2] = ch2[bytes[k2] & 255];
            }
            return new String(c2);
        }
        try {
            return EncodingUtil.convertToString(bytes, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.UnsupportedEncodingException, (Throwable) e);
        }
    }

    public static boolean isPdfDocEncoding(String text) {
        if (text == null) {
            return true;
        }
        int len = text.length();
        for (int k = 0; k < len; k++) {
            char ch2 = text.charAt(k);
            if (ch2 >= 128 && ((ch2 <= 160 || ch2 > 255) && !pdfEncoding.containsKey(ch2))) {
                return false;
            }
        }
        return true;
    }

    public static void addExtraEncoding(String name, IExtraEncoding enc) {
        synchronized (extraEncodings) {
            extraEncodings.put(name.toLowerCase(), enc);
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/PdfEncodings$WingdingsConversion.class */
    private static class WingdingsConversion implements IExtraEncoding {
        private static final byte[] table = {0, 35, 34, 0, 0, 0, 41, 62, 81, 42, 0, 0, 65, 63, 0, 0, 0, 0, 0, -4, 0, 0, 0, -5, 0, 0, 0, 0, 0, 0, 86, 0, 88, 89, 0, 0, 0, 0, 0, 0, 0, 0, -75, 0, 0, 0, 0, 0, -74, 0, 0, 0, -83, -81, -84, 0, 0, 0, 0, 0, 0, 0, 0, 124, 123, 0, 0, 0, 84, 0, 0, 0, 0, 0, 0, 0, 0, -90, 0, 0, 0, 113, 114, 0, 0, 0, 117, 0, 0, 0, 0, 0, 0, 125, 126, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -116, -115, -114, -113, -112, -111, -110, -109, -108, -107, -127, -126, -125, -124, -123, -122, -121, -120, -119, -118, -116, -115, -114, -113, -112, -111, -110, -109, -108, -107, -24, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -24, -40, 0, 0, -60, -58, 0, 0, -16, 0, 0, 0, 0, 0, 0, 0, 0, 0, -36, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        private WingdingsConversion() {
        }

        @Override // com.itextpdf.io.font.IExtraEncoding
        public byte[] charToByte(char char1, String encoding) {
            byte v;
            if (char1 == ' ') {
                return new byte[]{(byte) char1};
            }
            if (char1 >= 9985 && char1 <= 10174 && (v = table[char1 - 9984]) != 0) {
                return new byte[]{v};
            }
            return new byte[0];
        }

        @Override // com.itextpdf.io.font.IExtraEncoding
        public byte[] charToByte(String text, String encoding) {
            byte v;
            char[] cc = text.toCharArray();
            byte[] b = new byte[cc.length];
            int ptr = 0;
            int len = cc.length;
            for (char c : cc) {
                if (c == ' ') {
                    int i = ptr;
                    ptr++;
                    b[i] = (byte) c;
                } else if (c >= 9985 && c <= 10174 && (v = table[c - 9984]) != 0) {
                    int i2 = ptr;
                    ptr++;
                    b[i2] = v;
                }
            }
            if (ptr == len) {
                return b;
            }
            byte[] b2 = new byte[ptr];
            System.arraycopy(b, 0, b2, 0, ptr);
            return b2;
        }

        @Override // com.itextpdf.io.font.IExtraEncoding
        public String byteToChar(byte[] b, String encoding) {
            return null;
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/PdfEncodings$Cp437Conversion.class */
    private static class Cp437Conversion implements IExtraEncoding {
        private static IntHashtable c2b = new IntHashtable();
        private static final char[] table = {199, 252, 233, 226, 228, 224, 229, 231, 234, 235, 232, 239, 238, 236, 196, 197, 201, 230, 198, 244, 246, 242, 251, 249, 255, 214, 220, 162, 163, 165, 8359, 402, 225, 237, 243, 250, 241, 209, 170, 186, 191, 8976, 172, 189, 188, 161, 171, 187, 9617, 9618, 9619, 9474, 9508, 9569, 9570, 9558, 9557, 9571, 9553, 9559, 9565, 9564, 9563, 9488, 9492, 9524, 9516, 9500, 9472, 9532, 9566, 9567, 9562, 9556, 9577, 9574, 9568, 9552, 9580, 9575, 9576, 9572, 9573, 9561, 9560, 9554, 9555, 9579, 9578, 9496, 9484, 9608, 9604, 9612, 9616, 9600, 945, 223, 915, 960, 931, 963, 181, 964, 934, 920, 937, 948, 8734, 966, 949, 8745, 8801, 177, 8805, 8804, 8992, 8993, 247, 8776, 176, 8729, 183, 8730, 8319, 178, 9632, 160};

        private Cp437Conversion() {
        }

        static {
            for (int k = 0; k < table.length; k++) {
                c2b.put(table[k], k + 128);
            }
        }

        @Override // com.itextpdf.io.font.IExtraEncoding
        public byte[] charToByte(String text, String encoding) {
            char[] cc = text.toCharArray();
            byte[] b = new byte[cc.length];
            int ptr = 0;
            int len = cc.length;
            for (char c : cc) {
                if (c < 128) {
                    int i = ptr;
                    ptr++;
                    b[i] = (byte) c;
                } else {
                    byte v = (byte) c2b.get(c);
                    if (v != 0) {
                        int i2 = ptr;
                        ptr++;
                        b[i2] = v;
                    }
                }
            }
            if (ptr == len) {
                return b;
            }
            byte[] b2 = new byte[ptr];
            System.arraycopy(b, 0, b2, 0, ptr);
            return b2;
        }

        @Override // com.itextpdf.io.font.IExtraEncoding
        public byte[] charToByte(char char1, String encoding) {
            if (char1 < 128) {
                return new byte[]{(byte) char1};
            }
            byte v = (byte) c2b.get(char1);
            if (v != 0) {
                return new byte[]{v};
            }
            return new byte[0];
        }

        @Override // com.itextpdf.io.font.IExtraEncoding
        public String byteToChar(byte[] b, String encoding) {
            int len = b.length;
            char[] cc = new char[len];
            int ptr = 0;
            for (byte b2 : b) {
                int c = b2 & 255;
                if (c >= 32) {
                    if (c < 128) {
                        int i = ptr;
                        ptr++;
                        cc[i] = (char) c;
                    } else {
                        char v = table[c - 128];
                        int i2 = ptr;
                        ptr++;
                        cc[i2] = v;
                    }
                }
            }
            return new String(cc, 0, ptr);
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/PdfEncodings$SymbolConversion.class */
    private static class SymbolConversion implements IExtraEncoding {
        private IntHashtable translation;
        private final char[] byteToChar;
        private static final IntHashtable t1 = new IntHashtable();
        private static final IntHashtable t2 = new IntHashtable();
        private static final char[] table1 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ' ', '!', 8704, '#', 8707, '%', '&', 8715, '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', 8773, 913, 914, 935, 916, 917, 934, 915, 919, 921, 977, 922, 923, 924, 925, 927, 928, 920, 929, 931, 932, 933, 962, 937, 926, 936, 918, '[', 8756, ']', 8869, '_', 773, 945, 946, 967, 948, 949, 981, 947, 951, 953, 966, 954, 955, 956, 957, 959, 960, 952, 961, 963, 964, 965, 982, 969, 958, 968, 950, '{', '|', '}', '~', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8364, 978, 8242, 8804, 8260, 8734, 402, 9827, 9830, 9829, 9824, 8596, 8592, 8593, 8594, 8595, 176, 177, 8243, 8805, 215, 8733, 8706, 8226, 247, 8800, 8801, 8776, 8230, 9474, 9472, 8629, 8501, 8465, 8476, 8472, 8855, 8853, 8709, 8745, 8746, 8835, 8839, 8836, 8834, 8838, 8712, 8713, 8736, 8711, 174, 169, 8482, 8719, 8730, 8901, 172, 8743, 8744, 8660, 8656, 8657, 8658, 8659, 9674, 9001, 0, 0, 0, 8721, 9115, 9116, 9117, 9121, 9122, 9123, 9127, 9128, 9129, 9130, 0, 9002, 8747, 8992, 9134, 8993, 9118, 9119, 9120, 9124, 9125, 9126, 9131, 9132, 9133, 0};
        private static final char[] table2 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ' ', 9985, 9986, 9987, 9988, 9742, 9990, 9991, 9992, 9993, 9755, 9758, 9996, 9997, 9998, 9999, 10000, 10001, 10002, 10003, 10004, 10005, 10006, 10007, 10008, 10009, 10010, 10011, 10012, 10013, 10014, 10015, 10016, 10017, 10018, 10019, 10020, 10021, 10022, 10023, 9733, 10025, 10026, 10027, 10028, 10029, 10030, 10031, 10032, 10033, 10034, 10035, 10036, 10037, 10038, 10039, 10040, 10041, 10042, 10043, 10044, 10045, 10046, 10047, 10048, 10049, 10050, 10051, 10052, 10053, 10054, 10055, 10056, 10057, 10058, 10059, 9679, 10061, 9632, 10063, 10064, 10065, 10066, 9650, 9660, 9670, 10070, 9687, 10072, 10073, 10074, 10075, 10076, 10077, 10078, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10081, 10082, 10083, 10084, 10085, 10086, 10087, 9827, 9830, 9829, 9824, 9312, 9313, 9314, 9315, 9316, 9317, 9318, 9319, 9320, 9321, 10102, 10103, 10104, 10105, 10106, 10107, 10108, 10109, 10110, 10111, 10112, 10113, 10114, 10115, 10116, 10117, 10118, 10119, 10120, 10121, 10122, 10123, 10124, 10125, 10126, 10127, 10128, 10129, 10130, 10131, 10132, 8594, 8596, 8597, 10136, 10137, 10138, 10139, 10140, 10141, 10142, 10143, 10144, 10145, 10146, 10147, 10148, 10149, 10150, 10151, 10152, 10153, 10154, 10155, 10156, 10157, 10158, 10159, 0, 10161, 10162, 10163, 10164, 10165, 10166, 10167, 10168, 10169, 10170, 10171, 10172, 10173, 10174, 0};

        static {
            for (int k = 0; k < 256; k++) {
                char c = table1[k];
                if (c != 0) {
                    t1.put(c, k);
                }
            }
            for (int k2 = 0; k2 < 256; k2++) {
                char c2 = table2[k2];
                if (c2 != 0) {
                    t2.put(c2, k2);
                }
            }
        }

        SymbolConversion(boolean symbol) {
            if (symbol) {
                this.translation = t1;
                this.byteToChar = table1;
            } else {
                this.translation = t2;
                this.byteToChar = table2;
            }
        }

        @Override // com.itextpdf.io.font.IExtraEncoding
        public byte[] charToByte(String text, String encoding) {
            char[] cc = text.toCharArray();
            byte[] b = new byte[cc.length];
            int ptr = 0;
            int len = cc.length;
            for (char c : cc) {
                byte v = (byte) this.translation.get(c);
                if (v != 0) {
                    int i = ptr;
                    ptr++;
                    b[i] = v;
                }
            }
            if (ptr == len) {
                return b;
            }
            byte[] b2 = new byte[ptr];
            System.arraycopy(b, 0, b2, 0, ptr);
            return b2;
        }

        @Override // com.itextpdf.io.font.IExtraEncoding
        public byte[] charToByte(char char1, String encoding) {
            byte v = (byte) this.translation.get(char1);
            if (v != 0) {
                return new byte[]{v};
            }
            return new byte[0];
        }

        @Override // com.itextpdf.io.font.IExtraEncoding
        public String byteToChar(byte[] b, String encoding) {
            int len = b.length;
            char[] cc = new char[len];
            int ptr = 0;
            for (byte b2 : b) {
                int c = b2 & 255;
                char v = this.byteToChar[c];
                int i = ptr;
                ptr++;
                cc[i] = v;
            }
            return new String(cc, 0, ptr);
        }
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/font/PdfEncodings$SymbolTTConversion.class */
    private static class SymbolTTConversion implements IExtraEncoding {
        private SymbolTTConversion() {
        }

        @Override // com.itextpdf.io.font.IExtraEncoding
        public byte[] charToByte(char char1, String encoding) {
            if ((char1 & 65280) == 0 || (char1 & 65280) == 61440) {
                return new byte[]{(byte) char1};
            }
            return new byte[0];
        }

        @Override // com.itextpdf.io.font.IExtraEncoding
        public byte[] charToByte(String text, String encoding) {
            char[] ch2 = text.toCharArray();
            byte[] b = new byte[ch2.length];
            int ptr = 0;
            int len = ch2.length;
            for (char c : ch2) {
                if ((c & 65280) == 0 || (c & 65280) == 61440) {
                    int i = ptr;
                    ptr++;
                    b[i] = (byte) c;
                }
            }
            if (ptr == len) {
                return b;
            }
            byte[] b2 = new byte[ptr];
            System.arraycopy(b, 0, b2, 0, ptr);
            return b2;
        }

        @Override // com.itextpdf.io.font.IExtraEncoding
        public String byteToChar(byte[] b, String encoding) {
            return null;
        }
    }
}
