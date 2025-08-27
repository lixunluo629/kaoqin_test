package com.itextpdf.kernel.xmp.impl;

import com.itextpdf.kernel.xmp.XMPConst;
import com.moredian.onpremise.core.common.constants.SymbolConstants;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/xmp/impl/Utils.class */
public final class Utils implements XMPConst {
    public static final int UUID_SEGMENT_COUNT = 4;
    public static final int UUID_LENGTH = 36;
    private static boolean[] xmlNameStartChars;
    private static boolean[] xmlNameChars;

    static {
        initCharTables();
    }

    private Utils() {
    }

    public static String normalizeLangValue(String value) {
        if ("x-default".equals(value)) {
            return value;
        }
        int subTag = 1;
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < value.length(); i++) {
            switch (value.charAt(i)) {
                case ' ':
                    break;
                case '-':
                case '_':
                    buffer.append('-');
                    subTag++;
                    break;
                default:
                    if (subTag != 2) {
                        buffer.append(Character.toLowerCase(value.charAt(i)));
                        break;
                    } else {
                        buffer.append(Character.toUpperCase(value.charAt(i)));
                        break;
                    }
            }
        }
        return buffer.toString();
    }

    static String[] splitNameAndValue(String selector) {
        int eq = selector.indexOf(61);
        int pos = 1;
        if (selector.charAt(1) == '?') {
            pos = 1 + 1;
        }
        String name = selector.substring(pos, eq);
        int pos2 = eq + 1;
        char quote = selector.charAt(pos2);
        int pos3 = pos2 + 1;
        int end = selector.length() - 2;
        StringBuffer value = new StringBuffer(end - eq);
        while (pos3 < end) {
            value.append(selector.charAt(pos3));
            pos3++;
            if (selector.charAt(pos3) == quote) {
                pos3++;
            }
        }
        return new String[]{name, value.toString()};
    }

    static boolean isInternalProperty(String schema, String prop) {
        boolean isInternal = false;
        if ("http://purl.org/dc/elements/1.1/".equals(schema)) {
            if ("dc:format".equals(prop) || "dc:language".equals(prop)) {
                isInternal = true;
            }
        } else if ("http://ns.adobe.com/xap/1.0/".equals(schema)) {
            if ("xmp:BaseURL".equals(prop) || "xmp:CreatorTool".equals(prop) || "xmp:Format".equals(prop) || "xmp:Locale".equals(prop) || "xmp:MetadataDate".equals(prop) || "xmp:ModifyDate".equals(prop)) {
                isInternal = true;
            }
        } else if ("http://ns.adobe.com/pdf/1.3/".equals(schema)) {
            if ("pdf:BaseURL".equals(prop) || "pdf:Creator".equals(prop) || "pdf:ModDate".equals(prop) || "pdf:PDFVersion".equals(prop) || "pdf:Producer".equals(prop)) {
                isInternal = true;
            }
        } else if ("http://ns.adobe.com/tiff/1.0/".equals(schema)) {
            isInternal = true;
            if ("tiff:ImageDescription".equals(prop) || "tiff:Artist".equals(prop) || "tiff:Copyright".equals(prop)) {
                isInternal = false;
            }
        } else if ("http://ns.adobe.com/exif/1.0/".equals(schema)) {
            isInternal = true;
            if ("exif:UserComment".equals(prop)) {
                isInternal = false;
            }
        } else if ("http://ns.adobe.com/exif/1.0/aux/".equals(schema)) {
            isInternal = true;
        } else if ("http://ns.adobe.com/photoshop/1.0/".equals(schema)) {
            if ("photoshop:ICCProfile".equals(prop)) {
                isInternal = true;
            }
        } else if ("http://ns.adobe.com/camera-raw-settings/1.0/".equals(schema)) {
            if ("crs:Version".equals(prop) || "crs:RawFileName".equals(prop) || "crs:ToneCurveName".equals(prop)) {
                isInternal = true;
            }
        } else if ("http://ns.adobe.com/StockPhoto/1.0/".equals(schema) || "http://ns.adobe.com/xap/1.0/mm/".equals(schema) || "http://ns.adobe.com/xap/1.0/t/".equals(schema) || "http://ns.adobe.com/xap/1.0/t/pg/".equals(schema) || "http://ns.adobe.com/xap/1.0/g/".equals(schema) || "http://ns.adobe.com/xap/1.0/g/img/".equals(schema) || "http://ns.adobe.com/xap/1.0/sType/Font#".equals(schema)) {
            isInternal = true;
        }
        return isInternal;
    }

    static boolean checkUUIDFormat(String uuid) {
        boolean result = true;
        int delimCnt = 0;
        if (uuid == null) {
            return false;
        }
        int delimPos = 0;
        while (delimPos < uuid.length()) {
            if (uuid.charAt(delimPos) == '-') {
                delimCnt++;
                result = result && (delimPos == 8 || delimPos == 13 || delimPos == 18 || delimPos == 23);
            }
            delimPos++;
        }
        return result && 4 == delimCnt && 36 == delimPos;
    }

    public static boolean isXMLName(String name) {
        if (name.length() > 0 && !isNameStartChar(name.charAt(0))) {
            return false;
        }
        for (int i = 1; i < name.length(); i++) {
            if (!isNameChar(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isXMLNameNS(String name) {
        if (name.length() > 0 && (!isNameStartChar(name.charAt(0)) || name.charAt(0) == ':')) {
            return false;
        }
        for (int i = 1; i < name.length(); i++) {
            if (!isNameChar(name.charAt(i)) || name.charAt(i) == ':') {
                return false;
            }
        }
        return true;
    }

    static boolean isControlChar(char c) {
        return ((c > 31 && c != 127) || c == '\t' || c == '\n' || c == '\r') ? false : true;
    }

    public static String escapeXML(String value, boolean forAttribute, boolean escapeWhitespaces) {
        boolean needsEscaping = false;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '<' || c == '>' || c == '&' || ((escapeWhitespaces && (c == '\t' || c == '\n' || c == '\r')) || (forAttribute && c == '\"'))) {
                needsEscaping = true;
                break;
            }
        }
        if (!needsEscaping) {
            return value;
        }
        StringBuffer buffer = new StringBuffer((value.length() * 4) / 3);
        for (int i2 = 0; i2 < value.length(); i2++) {
            char c2 = value.charAt(i2);
            if (!escapeWhitespaces || (c2 != '\t' && c2 != '\n' && c2 != '\r')) {
                switch (c2) {
                    case '\"':
                        buffer.append(forAttribute ? "&quot;" : SymbolConstants.QUOTES_SYMBOL);
                        break;
                    case '&':
                        buffer.append("&amp;");
                        break;
                    case '<':
                        buffer.append("&lt;");
                        break;
                    case '>':
                        buffer.append("&gt;");
                        break;
                    default:
                        buffer.append(c2);
                        break;
                }
            } else {
                buffer.append("&#x");
                buffer.append(Integer.toHexString(c2).toUpperCase());
                buffer.append(';');
            }
        }
        return buffer.toString();
    }

    static String removeControlChars(String value) {
        StringBuilder buffer = new StringBuilder(value);
        for (int i = 0; i < buffer.length(); i++) {
            if (isControlChar(buffer.charAt(i))) {
                buffer.setCharAt(i, ' ');
            }
        }
        return buffer.toString();
    }

    private static boolean isNameStartChar(char ch2) {
        return (ch2 <= 255 && xmlNameStartChars[ch2]) || (ch2 >= 256 && ch2 <= 767) || ((ch2 >= 880 && ch2 <= 893) || ((ch2 >= 895 && ch2 <= 8191) || ((ch2 >= 8204 && ch2 <= 8205) || ((ch2 >= 8304 && ch2 <= 8591) || ((ch2 >= 11264 && ch2 <= 12271) || ((ch2 >= 12289 && ch2 <= 55295) || ((ch2 >= 63744 && ch2 <= 64975) || ((ch2 >= 65008 && ch2 <= 65533) || (ch2 >= 0 && ch2 <= 65535)))))))));
    }

    private static boolean isNameChar(char ch2) {
        return (ch2 <= 255 && xmlNameChars[ch2]) || isNameStartChar(ch2) || (ch2 >= 768 && ch2 <= 879) || (ch2 >= 8255 && ch2 <= 8256);
    }

    private static void initCharTables() {
        xmlNameChars = new boolean[256];
        xmlNameStartChars = new boolean[256];
        for (int i = 0; i < xmlNameChars.length; i++) {
            char ch2 = (char) i;
            xmlNameStartChars[ch2] = ch2 == ':' || ('A' <= ch2 && ch2 <= 'Z') || ch2 == '_' || (('a' <= ch2 && ch2 <= 'z') || ((192 <= ch2 && ch2 <= 214) || ((216 <= ch2 && ch2 <= 246) || (248 <= ch2 && ch2 <= 255))));
            xmlNameChars[ch2] = xmlNameStartChars[ch2] || ch2 == '-' || ch2 == '.' || ('0' <= ch2 && ch2 <= '9') || ch2 == 183;
        }
    }
}
