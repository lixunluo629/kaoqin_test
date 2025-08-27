package net.dongliu.apk.parser.cert.asn1.ber;

import ch.qos.logback.core.joran.action.ActionConst;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import net.dongliu.apk.parser.cert.asn1.Asn1TagClass;
import net.dongliu.apk.parser.cert.asn1.Asn1Type;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/cert/asn1/ber/BerEncoding.class */
public abstract class BerEncoding {
    public static final int ID_FLAG_CONSTRUCTED_ENCODING = 32;
    public static final int TAG_CLASS_UNIVERSAL = 0;
    public static final int TAG_CLASS_APPLICATION = 1;
    public static final int TAG_CLASS_CONTEXT_SPECIFIC = 2;
    public static final int TAG_CLASS_PRIVATE = 3;
    public static final int TAG_NUMBER_INTEGER = 2;
    public static final int TAG_NUMBER_OCTET_STRING = 4;
    public static final int TAG_NUMBER_NULL = 5;
    public static final int TAG_NUMBER_OBJECT_IDENTIFIER = 6;
    public static final int TAG_NUMBER_SEQUENCE = 16;
    public static final int TAG_NUMBER_SET = 17;

    private BerEncoding() {
    }

    public static int getTagNumber(Asn1Type dataType) {
        switch (dataType) {
            case INTEGER:
                return 2;
            case OBJECT_IDENTIFIER:
                return 6;
            case OCTET_STRING:
                return 4;
            case SET_OF:
                return 17;
            case SEQUENCE:
            case SEQUENCE_OF:
                return 16;
            default:
                throw new IllegalArgumentException("Unsupported data type: " + dataType);
        }
    }

    public static int getTagClass(Asn1TagClass tagClass) {
        switch (tagClass) {
            case APPLICATION:
                return 1;
            case CONTEXT_SPECIFIC:
                return 2;
            case PRIVATE:
                return 3;
            case UNIVERSAL:
                return 0;
            default:
                throw new IllegalArgumentException("Unsupported tag class: " + tagClass);
        }
    }

    public static String tagClassToString(int typeClass) {
        switch (typeClass) {
            case 0:
                return "UNIVERSAL";
            case 1:
                return "APPLICATION";
            case 2:
                return "";
            case 3:
                return "PRIVATE";
            default:
                throw new IllegalArgumentException("Unsupported type class: " + typeClass);
        }
    }

    public static String tagClassAndNumberToString(int tagClass, int tagNumber) {
        String classString = tagClassToString(tagClass);
        String numberString = tagNumberToString(tagNumber);
        return classString.isEmpty() ? numberString : classString + SymbolConstants.SPACE_SYMBOL + numberString;
    }

    public static String tagNumberToString(int tagNumber) {
        switch (tagNumber) {
            case 2:
                return "INTEGER";
            case 3:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            default:
                return "0x" + Integer.toHexString(tagNumber);
            case 4:
                return "OCTET STRING";
            case 5:
                return ActionConst.NULL;
            case 6:
                return "OBJECT IDENTIFIER";
            case 16:
                return "SEQUENCE";
            case 17:
                return "SET";
        }
    }

    public static boolean isConstructed(byte firstIdentifierByte) {
        return (firstIdentifierByte & 32) != 0;
    }

    public static int getTagClass(byte firstIdentifierByte) {
        return (firstIdentifierByte & 255) >> 6;
    }

    public static byte setTagClass(byte firstIdentifierByte, int tagClass) {
        return (byte) ((firstIdentifierByte & 63) | (tagClass << 6));
    }

    public static int getTagNumber(byte firstIdentifierByte) {
        return firstIdentifierByte & 31;
    }

    public static byte setTagNumber(byte firstIdentifierByte, int tagNumber) {
        return (byte) ((firstIdentifierByte & (-32)) | tagNumber);
    }
}
