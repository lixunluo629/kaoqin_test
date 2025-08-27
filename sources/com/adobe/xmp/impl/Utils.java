package com.adobe.xmp.impl;

import com.adobe.xmp.XMPConst;
import com.moredian.onpremise.core.common.constants.SymbolConstants;

/* loaded from: xmpcore-5.1.3.jar:com/adobe/xmp/impl/Utils.class */
public class Utils implements XMPConst {
    public static final int UUID_SEGMENT_COUNT = 4;
    public static final int UUID_LENGTH = 36;
    private static boolean[] xmlNameStartChars;
    private static boolean[] xmlNameChars;

    private Utils() {
    }

    public static String normalizeLangValue(String str) {
        if ("x-default".equals(str)) {
            return str;
        }
        int i = 1;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < str.length(); i2++) {
            switch (str.charAt(i2)) {
                case ' ':
                    break;
                case '-':
                case '_':
                    stringBuffer.append('-');
                    i++;
                    break;
                default:
                    if (i != 2) {
                        stringBuffer.append(Character.toLowerCase(str.charAt(i2)));
                        break;
                    } else {
                        stringBuffer.append(Character.toUpperCase(str.charAt(i2)));
                        break;
                    }
            }
        }
        return stringBuffer.toString();
    }

    static String[] splitNameAndValue(String str) {
        int iIndexOf = str.indexOf(61);
        int i = 1;
        if (str.charAt(1) == '?') {
            i = 1 + 1;
        }
        String strSubstring = str.substring(i, iIndexOf);
        int i2 = iIndexOf + 1;
        char cCharAt = str.charAt(i2);
        int i3 = i2 + 1;
        int length = str.length() - 2;
        StringBuffer stringBuffer = new StringBuffer(length - iIndexOf);
        while (i3 < length) {
            stringBuffer.append(str.charAt(i3));
            i3++;
            if (str.charAt(i3) == cCharAt) {
                i3++;
            }
        }
        return new String[]{strSubstring, stringBuffer.toString()};
    }

    static boolean isInternalProperty(String str, String str2) {
        boolean z = false;
        if ("http://purl.org/dc/elements/1.1/".equals(str)) {
            if ("dc:format".equals(str2) || "dc:language".equals(str2)) {
                z = true;
            }
        } else if ("http://ns.adobe.com/xap/1.0/".equals(str)) {
            if ("xmp:BaseURL".equals(str2) || "xmp:CreatorTool".equals(str2) || "xmp:Format".equals(str2) || "xmp:Locale".equals(str2) || "xmp:MetadataDate".equals(str2) || "xmp:ModifyDate".equals(str2)) {
                z = true;
            }
        } else if ("http://ns.adobe.com/pdf/1.3/".equals(str)) {
            if ("pdf:BaseURL".equals(str2) || "pdf:Creator".equals(str2) || "pdf:ModDate".equals(str2) || "pdf:PDFVersion".equals(str2) || "pdf:Producer".equals(str2)) {
                z = true;
            }
        } else if ("http://ns.adobe.com/tiff/1.0/".equals(str)) {
            z = true;
            if ("tiff:ImageDescription".equals(str2) || "tiff:Artist".equals(str2) || "tiff:Copyright".equals(str2)) {
                z = false;
            }
        } else if ("http://ns.adobe.com/exif/1.0/".equals(str)) {
            z = true;
            if ("exif:UserComment".equals(str2)) {
                z = false;
            }
        } else if ("http://ns.adobe.com/exif/1.0/aux/".equals(str)) {
            z = true;
        } else if ("http://ns.adobe.com/photoshop/1.0/".equals(str)) {
            if ("photoshop:ICCProfile".equals(str2)) {
                z = true;
            }
        } else if ("http://ns.adobe.com/camera-raw-settings/1.0/".equals(str)) {
            if ("crs:Version".equals(str2) || "crs:RawFileName".equals(str2) || "crs:ToneCurveName".equals(str2)) {
                z = true;
            }
        } else if ("http://ns.adobe.com/StockPhoto/1.0/".equals(str) || "http://ns.adobe.com/xap/1.0/mm/".equals(str) || "http://ns.adobe.com/xap/1.0/t/".equals(str) || "http://ns.adobe.com/xap/1.0/t/pg/".equals(str) || "http://ns.adobe.com/xap/1.0/g/".equals(str) || "http://ns.adobe.com/xap/1.0/g/img/".equals(str) || "http://ns.adobe.com/xap/1.0/sType/Font#".equals(str)) {
            z = true;
        }
        return z;
    }

    static boolean checkUUIDFormat(String str) {
        boolean z = true;
        int i = 0;
        if (str == null) {
            return false;
        }
        int i2 = 0;
        while (i2 < str.length()) {
            if (str.charAt(i2) == '-') {
                i++;
                z = z && (i2 == 8 || i2 == 13 || i2 == 18 || i2 == 23);
            }
            i2++;
        }
        return z && 4 == i && 36 == i2;
    }

    public static boolean isXMLName(String str) {
        if (str.length() > 0 && !isNameStartChar(str.charAt(0))) {
            return false;
        }
        for (int i = 1; i < str.length(); i++) {
            if (!isNameChar(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isXMLNameNS(String str) {
        if (str.length() > 0 && (!isNameStartChar(str.charAt(0)) || str.charAt(0) == ':')) {
            return false;
        }
        for (int i = 1; i < str.length(); i++) {
            if (!isNameChar(str.charAt(i)) || str.charAt(i) == ':') {
                return false;
            }
        }
        return true;
    }

    static boolean isControlChar(char c) {
        return ((c > 31 && c != 127) || c == '\t' || c == '\n' || c == '\r') ? false : true;
    }

    public static String escapeXML(String str, boolean z, boolean z2) {
        boolean z3 = false;
        for (int i = 0; i < str.length(); i++) {
            char cCharAt = str.charAt(i);
            if (cCharAt == '<' || cCharAt == '>' || cCharAt == '&' || ((z2 && (cCharAt == '\t' || cCharAt == '\n' || cCharAt == '\r')) || (z && cCharAt == '\"'))) {
                z3 = true;
                break;
            }
        }
        if (!z3) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer((str.length() * 4) / 3);
        for (int i2 = 0; i2 < str.length(); i2++) {
            char cCharAt2 = str.charAt(i2);
            if (!z2 || (cCharAt2 != '\t' && cCharAt2 != '\n' && cCharAt2 != '\r')) {
                switch (cCharAt2) {
                    case '\"':
                        stringBuffer.append(z ? "&quot;" : SymbolConstants.QUOTES_SYMBOL);
                        break;
                    case '&':
                        stringBuffer.append("&amp;");
                        break;
                    case '<':
                        stringBuffer.append("&lt;");
                        break;
                    case '>':
                        stringBuffer.append("&gt;");
                        break;
                    default:
                        stringBuffer.append(cCharAt2);
                        break;
                }
            } else {
                stringBuffer.append("&#x");
                stringBuffer.append(Integer.toHexString(cCharAt2).toUpperCase());
                stringBuffer.append(';');
            }
        }
        return stringBuffer.toString();
    }

    static String removeControlChars(String str) {
        StringBuffer stringBuffer = new StringBuffer(str);
        for (int i = 0; i < stringBuffer.length(); i++) {
            if (isControlChar(stringBuffer.charAt(i))) {
                stringBuffer.setCharAt(i, ' ');
            }
        }
        return stringBuffer.toString();
    }

    private static boolean isNameStartChar(char c) {
        return (c <= 255 && xmlNameStartChars[c]) || (c >= 256 && c <= 767) || ((c >= 880 && c <= 893) || ((c >= 895 && c <= 8191) || ((c >= 8204 && c <= 8205) || ((c >= 8304 && c <= 8591) || ((c >= 11264 && c <= 12271) || ((c >= 12289 && c <= 55295) || ((c >= 63744 && c <= 64975) || ((c >= 65008 && c <= 65533) || (c >= 0 && c <= 65535)))))))));
    }

    private static boolean isNameChar(char c) {
        return (c <= 255 && xmlNameChars[c]) || isNameStartChar(c) || (c >= 768 && c <= 879) || (c >= 8255 && c <= 8256);
    }

    private static void initCharTables() {
        xmlNameChars = new boolean[256];
        xmlNameStartChars = new boolean[256];
        char c = 0;
        while (true) {
            char c2 = c;
            if (c2 >= xmlNameChars.length) {
                return;
            }
            xmlNameStartChars[c2] = c2 == ':' || ('A' <= c2 && c2 <= 'Z') || c2 == '_' || (('a' <= c2 && c2 <= 'z') || ((192 <= c2 && c2 <= 214) || ((216 <= c2 && c2 <= 246) || (248 <= c2 && c2 <= 255))));
            xmlNameChars[c2] = xmlNameStartChars[c2] || c2 == '-' || c2 == '.' || ('0' <= c2 && c2 <= '9') || c2 == 183;
            c = (char) (c2 + 1);
        }
    }

    static {
        initCharTables();
    }
}
