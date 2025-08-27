package org.apache.xmlbeans.impl.common;

import com.drew.metadata.exif.makernotes.CasioType2MakernoteDirectory;
import com.drew.metadata.exif.makernotes.FujifilmMakernoteDirectory;
import com.drew.metadata.exif.makernotes.NikonType2MakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusCameraSettingsMakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusImageProcessingMakernoteDirectory;
import com.drew.metadata.exif.makernotes.SanyoMakernoteDirectory;
import com.drew.metadata.iptc.IptcDirectory;
import com.drew.metadata.photoshop.PhotoshopDirectory;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.itextpdf.kernel.pdf.canvas.wmf.MetaDo;
import com.mysql.jdbc.MysqlErrorNumbers;
import org.apache.ibatis.javassist.compiler.TokenId;
import org.apache.poi.ddf.EscherProperties;
import org.aspectj.apache.bcel.Constants;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/XMLChar.class */
public class XMLChar {
    private static final byte[] CHARS = new byte[65536];
    public static final int MASK_VALID = 1;
    public static final int MASK_SPACE = 2;
    public static final int MASK_NAME_START = 4;
    public static final int MASK_NAME = 8;
    public static final int MASK_PUBID = 16;
    public static final int MASK_CONTENT = 32;
    public static final int MASK_NCNAME_START = 64;
    public static final int MASK_NCNAME = 128;

    static {
        int[] charRange = {9, 10, 13, 13, 32, 55295, 57344, 65533};
        int[] spaceChar = {32, 9, 13, 10};
        int[] nameChar = {45, 46};
        int[] nameStartChar = {58, 95};
        int[] pubidChar = {10, 13, 32, 33, 35, 36, 37, 61, 95};
        int[] pubidRange = {39, 59, 63, 90, 97, 122};
        int[] letterRange = {65, 90, 97, 122, 192, Constants.INVOKEVIRTUAL_QUICK, Constants.INVOKESUPER_QUICK, EscherProperties.GEOTEXT__CHARBOUNDINGBOX, EscherProperties.GEOTEXT__STRETCHCHARHEIGHT, 305, TokenId.CONST, 318, 321, 328, 330, EscherProperties.GEOMETRY__FILLSHADESHAPEOK, 384, EscherProperties.LINESTYLE__CRMOD, EscherProperties.LINESTYLE__LINESTYLE, MetaDo.META_DELETEOBJECT, 500, 501, 506, SanyoMakernoteDirectory.TAG_RECORD_SHUTTER_RELEASE, IptcDirectory.TAG_BY_LINE, 680, IptcDirectory.TAG_UNIQUE_DOCUMENT_ID, EscherProperties.THREEDSTYLE__XROTATIONANGLE, EscherProperties.GROUPSHAPE__REGROUPID, EscherProperties.GROUPSHAPE__UNUSED906, 910, 929, 931, 974, 976, 982, 994, 1011, 1025, 1036, MysqlErrorNumbers.ER_OUT_OF_SORTMEMORY, MysqlErrorNumbers.ER_WRONG_TABLE_NAME, MysqlErrorNumbers.ER_UNKNOWN_ERROR, MysqlErrorNumbers.ER_TOO_MANY_TABLES, MysqlErrorNumbers.ER_TOO_BIG_ROWSIZE, MysqlErrorNumbers.ER_NET_PACKET_TOO_LARGE, MysqlErrorNumbers.ER_WRONG_MRG_TABLE, MysqlErrorNumbers.ER_ERROR_WHEN_EXECUTING_COMMAND, MysqlErrorNumbers.ER_CANT_UPDATE_WITH_READLOCK, MysqlErrorNumbers.ER_MIXING_NOT_ALLOWED, MysqlErrorNumbers.ER_SPECIFIC_ACCESS_DENIED_ERROR, MysqlErrorNumbers.ER_LOCAL_VARIABLE, MysqlErrorNumbers.ER_WRONG_TYPE_FOR_VAR, MysqlErrorNumbers.ER_ZLIB_Z_DATA_ERROR, MysqlErrorNumbers.ER_WARN_TOO_MANY_RECORDS, MysqlErrorNumbers.ER_REVOKE_GRANTS, MysqlErrorNumbers.ER_VARIABLE_IS_NOT_STRUCT, MysqlErrorNumbers.ER_UNKNOWN_COLLATION, MysqlErrorNumbers.ER_SP_FETCH_NO_DATA, MysqlErrorNumbers.ER_TRUNCATED_WRONG_VALUE_FOR_FIELD, MysqlErrorNumbers.ER_BINLOG_PURGE_FATAL_ERR, MysqlErrorNumbers.ER_SP_NOT_VAR_ARG, MysqlErrorNumbers.ER_FIELD_NOT_FOUND_PART_ERROR, MysqlErrorNumbers.ER_ADD_PARTITION_NO_NEW_PARTITION, MysqlErrorNumbers.ER_REORG_OUTSIDE_RANGE, MysqlErrorNumbers.ER_PART_STATE_ERROR, MysqlErrorNumbers.ER_DUP_ENTRY_AUTOINCREMENT_CASE, MysqlErrorNumbers.ER_SLAVE_RELAY_LOG_READ_FAILURE, MysqlErrorNumbers.ER_SR_INVALID_CREATION_CTX, MysqlErrorNumbers.ER_SLAVE_CORRUPT_EVENT, MysqlErrorNumbers.ER_UNKNOWN_LOCALE, MysqlErrorNumbers.ER_BINLOG_UNSAFE_UPDATE_IGNORE, MysqlErrorNumbers.ER_BINLOG_UNSAFE_WRITE_AUTOINC_SELECT, MysqlErrorNumbers.ER_UNSUPPORTED_ENGINE, MysqlErrorNumbers.ER_CANNOT_LOAD_FROM_TABLE_V2, MysqlErrorNumbers.ER_RPL_INFO_DATA_TOO_LONG, MysqlErrorNumbers.ER_BINLOG_READ_EVENT_CHECKSUM_FAILURE, MysqlErrorNumbers.ER_PARTITION_CLAUSE_ON_NONPARTITIONED, MysqlErrorNumbers.ER_VARIABLE_NOT_SETTABLE_IN_SF_OR_TRIGGER, MysqlErrorNumbers.ER_VARIABLE_NOT_SETTABLE_IN_TRANSACTION, 2309, 2361, 2392, 2401, 2437, 2444, 2447, 2448, 2451, 2472, 2474, 2480, 2486, 2489, 2524, 2525, 2527, 2529, 2544, 2545, 2565, 2570, 2575, 2576, 2579, 2600, 2602, 2608, MetaDo.META_EXTTEXTOUT, 2611, 2613, 2614, 2616, 2617, 2649, 2652, 2674, 2676, 2693, 2699, 2703, 2705, 2707, 2728, 2730, 2736, 2738, 2739, 2741, 2745, 2821, 2828, 2831, 2832, 2835, 2856, 2858, 2864, 2866, 2867, 2870, 2873, 2908, 2909, 2911, 2913, 2949, 2954, 2958, 2960, 2962, 2965, 2969, 2970, 2974, 2975, 2979, 2980, 2984, 2986, 2990, 2997, PhotoshopDirectory.TAG_CLIPPING_PATH_NAME, 3001, 3077, 3084, 3086, 3088, 3090, 3112, 3114, 3123, 3125, 3129, 3168, 3169, 3205, 3212, 3214, 3216, 3218, 3240, 3242, 3251, 3253, 3257, 3296, 3297, 3333, 3340, 3342, 3344, 3346, 3368, 3370, 3385, 3424, 3425, NikonType2MakernoteDirectory.TAG_NIKON_CAPTURE_DATA, 3630, 3634, 3635, 3648, 3653, 3713, 3714, 3719, 3720, 3732, 3735, 3737, 3743, 3745, 3747, 3754, 3755, 3757, 3758, 3762, 3763, 3776, 3780, 3904, 3911, 3913, 3945, 4256, 4293, 4304, 4342, 4354, OlympusImageProcessingMakernoteDirectory.TagUnknownBlock3, 4357, 4359, 4363, 4364, 4366, OlympusImageProcessingMakernoteDirectory.TagAspectRatio, 4436, 4437, 4447, 4449, 4461, 4462, 4466, 4467, 4526, 4527, 4535, 4536, 4540, 4546, 7680, 7835, 7840, 7929, 7936, 7957, 7960, 7965, 7968, 8005, 8008, 8013, 8016, 8023, 8031, 8061, 8064, 8116, 8118, 8124, 8130, 8132, 8134, 8140, 8144, 8147, 8150, 8155, 8160, 8172, 8178, 8180, 8182, 8188, 8490, 8491, 8576, 8578, 12353, 12436, 12449, 12538, 12549, 12588, 44032, 55203, 12321, 12329, 19968, 40869};
        int[] letterChar = {EscherProperties.GROUPSHAPE__WRAPDISTRIGHT, 908, 986, 988, 990, 992, MysqlErrorNumbers.ER_VIEW_CHECK_FAILED, MysqlErrorNumbers.ER_NO_SUCH_PARTITION__UNUSED, 2365, 2482, 2654, 2701, 2749, 2784, 2877, 2972, 3294, 3632, 3716, 3722, 3725, 3749, 3751, 3760, 3773, FujifilmMakernoteDirectory.TAG_AUTO_BRACKETING, 4361, 4412, 4414, 4416, 4428, 4430, 4432, 4441, 4451, 4453, 4455, 4457, 4469, 4510, 4520, 4523, 4538, 4587, 4592, 4601, 8025, 8027, 8029, 8126, 8486, 8494, CasioType2MakernoteDirectory.TAG_BESTSHOT_MODE};
        int[] combiningCharRange = {768, EscherProperties.CALLOUT__CALLOUTLENGTHSPECIFIED, 864, 865, MysqlErrorNumbers.ER_NET_FCNTL_ERROR, MysqlErrorNumbers.ER_NET_READ_ERROR, MysqlErrorNumbers.ER_TOO_BIG_SCALE, MysqlErrorNumbers.ER_DATETIME_FUNCTION_OVERFLOW, MysqlErrorNumbers.ER_VIEW_PREVENT_UPDATE, MysqlErrorNumbers.ER_NO_TRIGGERS_ON_SYSTEM_SCHEMA, MysqlErrorNumbers.ER_AUTOINC_READ_FAILED, MysqlErrorNumbers.ER_HOSTNAME, MysqlErrorNumbers.ER_TOO_HIGH_LEVEL_OF_NESTING_FOR_SELECT, MysqlErrorNumbers.ER_NAME_BECOMES_EMPTY, MysqlErrorNumbers.ER_LOAD_DATA_INVALID_COLUMN, MysqlErrorNumbers.WARN_OPTION_IGNORED, MysqlErrorNumbers.ER_CHANGE_RPL_INFO_REPOSITORY_FAILURE, MysqlErrorNumbers.ER_MTS_INCONSISTENT_DATA, 1757, MysqlErrorNumbers.ER_INSECURE_PLAIN_TEXT, MysqlErrorNumbers.ER_INSECURE_CHANGE_MASTER, MysqlErrorNumbers.ER_TABLE_HAS_NO_FT, MysqlErrorNumbers.ER_GTID_NEXT_IS_NOT_IN_GTID_NEXT_LIST, MysqlErrorNumbers.ER_CANT_CHANGE_GTID_NEXT_IN_TRANSACTION_WHEN_GTID_NEXT_LIST_IS_NULL, MysqlErrorNumbers.ER_GTID_NEXT_CANT_BE_AUTOMATIC_IF_GTID_NEXT_LIST_IS_NON_NULL, MysqlErrorNumbers.ER_MALFORMED_GTID_SET_ENCODING, OlympusCameraSettingsMakernoteDirectory.TagManometerReading, OlympusCameraSettingsMakernoteDirectory.TagRollAngle, 2366, 2380, 2385, 2388, 2402, 2403, 2433, 2435, 2496, 2500, 2503, 2504, 2507, 2509, 2530, 2531, 2624, 2626, 2631, 2632, 2635, 2637, 2672, 2673, 2689, 2691, 2750, 2757, 2759, 2761, 2763, 2765, 2817, 2819, 2878, 2883, 2887, 2888, 2891, 2893, 2902, 2903, 2946, 2947, 3006, 3010, 3014, 3016, 3018, 3021, 3073, 3075, 3134, 3140, 3142, 3144, 3146, 3149, 3157, 3158, 3202, 3203, 3262, 3268, 3270, 3272, 3274, 3277, 3285, 3286, 3330, 3331, 3390, 3395, 3398, 3400, 3402, 3405, 3636, 3642, 3655, 3662, 3764, 3769, 3771, 3772, 3784, 3789, 3864, 3865, 3953, 3972, 3974, 3979, 3984, 3989, 3993, 4013, 4017, 4023, 8400, 8412, 12330, 12335};
        int[] combiningCharChar = {1471, MysqlErrorNumbers.ER_FOREIGN_SERVER_EXISTS, MysqlErrorNumbers.ER_COND_ITEM_TOO_LONG, 2364, 2381, 2492, 2494, 2495, 2519, 2562, 2620, 2622, 2623, 2748, 2876, 3031, 3415, 3633, 3761, 3893, 3895, 3897, 3902, 3903, 3991, 4025, 8417, 12441, 12442};
        int[] digitRange = {48, 57, MysqlErrorNumbers.ER_TABLE_NAME, MysqlErrorNumbers.ER_DUP_SIGNAL_SET, MysqlErrorNumbers.ER_BAD_SLAVE_AUTO_POSITION, MysqlErrorNumbers.ER_GTID_UNSAFE_NON_TRANSACTIONAL_TABLE, 2406, 2415, 2534, 2543, 2662, 2671, 2790, 2799, 2918, 2927, 3047, 3055, 3174, 3183, 3302, 3311, 3430, 3439, 3664, 3673, 3792, 3801, 3872, 3881};
        int[] extenderRange = {12337, 12341, 12445, 12446, 12540, 12542};
        int[] extenderChar = {183, EscherProperties.THREEDSTYLE__SKEWANGLE, EscherProperties.THREEDSTYLE__SKEWAMOUNT, 903, MysqlErrorNumbers.ER_VIEW_INVALID_CREATION_CTX, 3654, 3782, 12293};
        int[] specialChar = {60, 38, 10, 13, 93};
        for (int i = 0; i < charRange.length; i += 2) {
            for (int j = charRange[i]; j <= charRange[i + 1]; j++) {
                byte[] bArr = CHARS;
                int i2 = j;
                bArr[i2] = (byte) (bArr[i2] | 33);
            }
        }
        for (int i3 = 0; i3 < specialChar.length; i3++) {
            CHARS[specialChar[i3]] = (byte) (CHARS[specialChar[i3]] & (-33));
        }
        for (int i4 : spaceChar) {
            byte[] bArr2 = CHARS;
            bArr2[i4] = (byte) (bArr2[i4] | 2);
        }
        for (int i5 : nameStartChar) {
            byte[] bArr3 = CHARS;
            bArr3[i5] = (byte) (bArr3[i5] | 204);
        }
        for (int i6 = 0; i6 < letterRange.length; i6 += 2) {
            for (int j2 = letterRange[i6]; j2 <= letterRange[i6 + 1]; j2++) {
                byte[] bArr4 = CHARS;
                int i7 = j2;
                bArr4[i7] = (byte) (bArr4[i7] | 204);
            }
        }
        for (int i8 : letterChar) {
            byte[] bArr5 = CHARS;
            bArr5[i8] = (byte) (bArr5[i8] | 204);
        }
        for (int i9 : nameChar) {
            byte[] bArr6 = CHARS;
            bArr6[i9] = (byte) (bArr6[i9] | 136);
        }
        for (int i10 = 0; i10 < digitRange.length; i10 += 2) {
            for (int j3 = digitRange[i10]; j3 <= digitRange[i10 + 1]; j3++) {
                byte[] bArr7 = CHARS;
                int i11 = j3;
                bArr7[i11] = (byte) (bArr7[i11] | 136);
            }
        }
        for (int i12 = 0; i12 < combiningCharRange.length; i12 += 2) {
            for (int j4 = combiningCharRange[i12]; j4 <= combiningCharRange[i12 + 1]; j4++) {
                byte[] bArr8 = CHARS;
                int i13 = j4;
                bArr8[i13] = (byte) (bArr8[i13] | 136);
            }
        }
        for (int i14 : combiningCharChar) {
            byte[] bArr9 = CHARS;
            bArr9[i14] = (byte) (bArr9[i14] | 136);
        }
        for (int i15 = 0; i15 < extenderRange.length; i15 += 2) {
            for (int j5 = extenderRange[i15]; j5 <= extenderRange[i15 + 1]; j5++) {
                byte[] bArr10 = CHARS;
                int i16 = j5;
                bArr10[i16] = (byte) (bArr10[i16] | 136);
            }
        }
        for (int i17 : extenderChar) {
            byte[] bArr11 = CHARS;
            bArr11[i17] = (byte) (bArr11[i17] | 136);
        }
        byte[] bArr12 = CHARS;
        bArr12[58] = (byte) (bArr12[58] & (-193));
        for (int i18 : pubidChar) {
            byte[] bArr13 = CHARS;
            bArr13[i18] = (byte) (bArr13[i18] | 16);
        }
        for (int i19 = 0; i19 < pubidRange.length; i19 += 2) {
            for (int j6 = pubidRange[i19]; j6 <= pubidRange[i19 + 1]; j6++) {
                byte[] bArr14 = CHARS;
                int i20 = j6;
                bArr14[i20] = (byte) (bArr14[i20] | 16);
            }
        }
    }

    public static boolean isSupplemental(int c) {
        return c >= 65536 && c <= 1114111;
    }

    public static int supplemental(char h, char l) {
        return ((h - GeneratorBase.SURR1_FIRST) * 1024) + (l - GeneratorBase.SURR2_FIRST) + 65536;
    }

    public static char highSurrogate(int c) {
        return (char) (((c - 65536) >> 10) + GeneratorBase.SURR1_FIRST);
    }

    public static char lowSurrogate(int c) {
        return (char) (((c - 65536) & 1023) + GeneratorBase.SURR2_FIRST);
    }

    public static boolean isHighSurrogate(int c) {
        return 55296 <= c && c <= 56319;
    }

    public static boolean isLowSurrogate(int c) {
        return 56320 <= c && c <= 57343;
    }

    public static boolean isValid(int c) {
        return (c < 65536 && (CHARS[c] & 1) != 0) || (65536 <= c && c <= 1114111);
    }

    public static boolean isInvalid(int c) {
        return !isValid(c);
    }

    public static boolean isContent(int c) {
        return (c < 65536 && (CHARS[c] & 32) != 0) || (65536 <= c && c <= 1114111);
    }

    public static boolean isMarkup(int c) {
        return c == 60 || c == 38 || c == 37;
    }

    public static boolean isSpace(int c) {
        return c < 65536 && (CHARS[c] & 2) != 0;
    }

    public static boolean isXML11Space(int c) {
        return (c < 65536 && (CHARS[c] & 2) != 0) || c == 133 || c == 8232;
    }

    public static boolean isNameStart(int c) {
        return c < 65536 && (CHARS[c] & 4) != 0;
    }

    public static boolean isName(int c) {
        return c < 65536 && (CHARS[c] & 8) != 0;
    }

    public static boolean isNCNameStart(int c) {
        return c < 65536 && (CHARS[c] & 64) != 0;
    }

    public static boolean isNCName(int c) {
        return c < 65536 && (CHARS[c] & 128) != 0;
    }

    public static boolean isPubid(int c) {
        return c < 65536 && (CHARS[c] & 16) != 0;
    }

    public static boolean isValidName(String name) {
        if (name.length() == 0) {
            return false;
        }
        char ch2 = name.charAt(0);
        if (!isNameStart(ch2)) {
            return false;
        }
        for (int i = 1; i < name.length(); i++) {
            char ch3 = name.charAt(i);
            if (!isName(ch3)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidNCName(String ncName) {
        if (ncName.length() == 0) {
            return false;
        }
        char ch2 = ncName.charAt(0);
        if (!isNCNameStart(ch2)) {
            return false;
        }
        for (int i = 1; i < ncName.length(); i++) {
            char ch3 = ncName.charAt(i);
            if (!isNCName(ch3)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidNmtoken(String nmtoken) {
        if (nmtoken.length() == 0) {
            return false;
        }
        for (int i = 0; i < nmtoken.length(); i++) {
            char ch2 = nmtoken.charAt(i);
            if (!isName(ch2)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidIANAEncoding(String ianaEncoding) {
        int length;
        if (ianaEncoding != null && (length = ianaEncoding.length()) > 0) {
            char c = ianaEncoding.charAt(0);
            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
                for (int i = 1; i < length; i++) {
                    char c2 = ianaEncoding.charAt(i);
                    if ((c2 < 'A' || c2 > 'Z') && ((c2 < 'a' || c2 > 'z') && ((c2 < '0' || c2 > '9') && c2 != '.' && c2 != '_' && c2 != '-'))) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }

    public static boolean isValidJavaEncoding(String javaEncoding) {
        int length;
        if (javaEncoding != null && (length = javaEncoding.length()) > 0) {
            for (int i = 1; i < length; i++) {
                char c = javaEncoding.charAt(i);
                if ((c < 'A' || c > 'Z') && ((c < 'a' || c > 'z') && ((c < '0' || c > '9') && c != '.' && c != '_' && c != '-'))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
