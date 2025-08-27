package org.apache.poi.util;

import ch.qos.logback.classic.joran.action.InsertFromJNDIAction;
import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.drew.metadata.exif.makernotes.CanonMakernoteDirectory;
import com.drew.metadata.exif.makernotes.CasioType2MakernoteDirectory;
import com.drew.metadata.exif.makernotes.FujifilmMakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusMakernoteDirectory;
import com.drew.metadata.exif.makernotes.SonyType1MakernoteDirectory;
import com.itextpdf.kernel.pdf.canvas.wmf.MetaDo;
import com.mysql.jdbc.MysqlErrorNumbers;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.poi.hssf.record.chart.ChartFRTInfoRecord;

/* loaded from: poi-3.17.jar:org/apache/poi/util/LocaleUtil.class */
public final class LocaleUtil {
    public static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone("UTC");
    public static final Charset CHARSET_1252 = Charset.forName("CP1252");
    private static final ThreadLocal<TimeZone> userTimeZone = new ThreadLocal<>();
    private static final ThreadLocal<Locale> userLocale = new ThreadLocal<>();

    private LocaleUtil() {
    }

    public static void setUserTimeZone(TimeZone timezone) {
        userTimeZone.set(timezone);
    }

    @SuppressForbidden("implementation around default locales in POI")
    public static TimeZone getUserTimeZone() {
        TimeZone timeZone = userTimeZone.get();
        return timeZone != null ? timeZone : TimeZone.getDefault();
    }

    public static void resetUserTimeZone() {
        userTimeZone.remove();
    }

    public static void setUserLocale(Locale locale) {
        userLocale.set(locale);
    }

    @SuppressForbidden("implementation around default locales in POI")
    public static Locale getUserLocale() {
        Locale locale = userLocale.get();
        return locale != null ? locale : Locale.getDefault();
    }

    public static void resetUserLocale() {
        userLocale.remove();
    }

    public static Calendar getLocaleCalendar() {
        return getLocaleCalendar(getUserTimeZone());
    }

    public static Calendar getLocaleCalendar(int year, int month, int day) {
        return getLocaleCalendar(year, month, day, 0, 0, 0);
    }

    public static Calendar getLocaleCalendar(int year, int month, int day, int hour, int minute, int second) {
        Calendar cal = getLocaleCalendar();
        cal.set(year, month, day, hour, minute, second);
        cal.clear(14);
        return cal;
    }

    public static Calendar getLocaleCalendar(TimeZone timeZone) {
        return Calendar.getInstance(timeZone, getUserLocale());
    }

    public static String getLocaleFromLCID(int lcid) {
        int languageId = lcid & 65535;
        switch (languageId) {
            case 1:
                return ArchiveStreamFactory.AR;
            case 2:
                return "bg";
            case 3:
                return "ca";
            case 4:
                return "zh-Hans";
            case 5:
                return "cs";
            case 6:
                return "da";
            case 7:
                return "de";
            case 8:
                return "el";
            case 9:
                return "en";
            case 10:
                return "es";
            case 11:
                return "fi";
            case 12:
                return "fr";
            case 13:
                return "he";
            case 14:
                return "hu";
            case 15:
                return BeanUtil.PREFIX_GETTER_IS;
            case 16:
                return "it";
            case 17:
                return "ja";
            case 18:
                return "ko";
            case 19:
                return "nl";
            case 20:
                return "no";
            case 21:
                return "pl";
            case 22:
                return "pt";
            case 23:
                return "rm";
            case 24:
                return "ro";
            case 25:
                return "ru";
            case 26:
                return "bs, hr, or sr";
            case 27:
                return "sk";
            case 28:
                return "sq";
            case 29:
                return "sv";
            case 30:
                return "th";
            case 31:
                return "tr";
            case 32:
                return "ur";
            case 33:
                return "id";
            case 34:
                return "uk";
            case 35:
                return "be";
            case 36:
                return "sl";
            case 37:
                return "et";
            case 38:
                return "lv";
            case 39:
                return "lt";
            case 40:
                return "tg";
            case 41:
                return "fa";
            case 42:
                return "vi";
            case 43:
                return "hy";
            case 44:
                return "az";
            case 45:
                return "eu";
            case 46:
                return "dsb or hsb";
            case 47:
                return "mk";
            case 48:
                return "st";
            case 49:
                return "ts";
            case 50:
                return "tn";
            case 51:
                return "ve";
            case 52:
                return "xh";
            case 53:
                return "zu";
            case 54:
                return "af";
            case 55:
                return "ka";
            case 56:
                return "fo";
            case 57:
                return "hi";
            case 58:
                return "mt";
            case 59:
                return "se";
            case 60:
                return "ga";
            case 61:
                return "yi";
            case 62:
                return "ms";
            case 63:
                return "kk";
            case 64:
                return "ky";
            case 65:
                return "sw";
            case 66:
                return "tk";
            case 67:
                return "uz";
            case 68:
                return "tt";
            case 69:
                return "bn";
            case 70:
                return "pa";
            case 71:
                return "gu";
            case 72:
                return "or";
            case 73:
                return "ta";
            case 74:
                return "te";
            case 75:
                return "kn";
            case 76:
                return "ml";
            case 77:
                return InsertFromJNDIAction.AS_ATTR;
            case 78:
                return "mr";
            case 79:
                return "sa";
            case 80:
                return "mn";
            case 81:
                return "bo";
            case 82:
                return "cy";
            case 83:
                return "km";
            case 84:
                return "lo";
            case 85:
                return "my";
            case 86:
                return "gl";
            case 87:
                return "kok";
            case 88:
                return "mni";
            case 89:
                return "sd";
            case 90:
                return "syr";
            case 91:
                return "si";
            case 92:
                return "chr";
            case 93:
                return "iu";
            case 94:
                return "am";
            case 95:
                return "tzm";
            case 96:
                return "ks";
            case 97:
                return "ne";
            case 98:
                return "fy";
            case 99:
                return "ps";
            case 100:
                return "fil";
            case 101:
                return "dv";
            case 102:
                return "bin";
            case 103:
                return "ff";
            case 104:
                return "ha";
            case 105:
                return "ibb";
            case 106:
                return "yo";
            case 107:
                return "quz";
            case 108:
                return "nso";
            case 109:
                return "ba";
            case 110:
                return "lb";
            case 111:
                return "kl";
            case 112:
                return "ig";
            case 113:
                return "kr";
            case 114:
                return "om";
            case 115:
                return "ti";
            case 116:
                return "gn";
            case 117:
                return "haw";
            case 118:
                return "la";
            case 119:
                return "so";
            case 120:
                return "ii";
            case 121:
                return "pap";
            case 122:
                return "arn";
            case 123:
                return "invalid";
            case 124:
                return "moh";
            case 125:
                return "invalid";
            case 126:
                return CompressorStreamFactory.BROTLI;
            case 127:
                return "invalid";
            case 128:
                return "ug";
            case 129:
                return "mi";
            case 130:
                return "oc";
            case 131:
                return "co";
            case 132:
                return "gsw";
            case 133:
                return "sah";
            case 134:
                return "qut";
            case 135:
                return "rw";
            case 136:
                return "wo";
            case 137:
                return "invalid";
            case 138:
                return "invalid";
            case 139:
                return "invalid";
            case 140:
                return "prs";
            case 141:
                return "invalid";
            case 142:
                return "invalid";
            case 143:
                return "invalid";
            case 144:
                return "invalid";
            case 145:
                return "gd";
            case 146:
                return "ku";
            case 147:
                return "quc";
            case 1025:
                return "ar-SA";
            case 1026:
                return "bg-BG";
            case 1027:
                return "ca-ES";
            case 1028:
                return "zh-TW";
            case 1029:
                return "cs-CZ";
            case 1030:
                return "da-DK";
            case 1031:
                return "de-DE";
            case 1032:
                return "el-GR";
            case 1033:
                return "en-US";
            case 1034:
                return "es-ES_tradnl";
            case 1035:
                return "fi-FI";
            case 1036:
                return "fr-FR";
            case 1037:
                return "he-IL";
            case MysqlErrorNumbers.ER_OUT_OF_SORTMEMORY /* 1038 */:
                return "hu-HU";
            case 1039:
                return "is-IS";
            case 1040:
                return "it-IT";
            case 1041:
                return "ja-JP";
            case 1042:
                return "ko-KR";
            case 1043:
                return "nl-NL";
            case 1044:
                return "nb-NO";
            case 1045:
                return "pl-PL";
            case 1046:
                return "pt-BR";
            case 1047:
                return "rm-CH";
            case 1048:
                return "ro-RO";
            case 1049:
                return "ru-RU";
            case 1050:
                return "hr-HR";
            case 1051:
                return "sk-SK";
            case 1052:
                return "sq-AL";
            case 1053:
                return "sv-SE";
            case 1054:
                return "th-TH";
            case 1055:
                return "tr-TR";
            case MysqlErrorNumbers.ER_WRONG_GROUP_FIELD /* 1056 */:
                return "ur-PK";
            case 1057:
                return "id-ID";
            case 1058:
                return "uk-UA";
            case 1059:
                return "be-BY";
            case 1060:
                return "sl-SI";
            case 1061:
                return "et-EE";
            case 1062:
                return "lv-LV";
            case MysqlErrorNumbers.ER_WRONG_FIELD_SPEC /* 1063 */:
                return "lt-LT";
            case 1064:
                return "tg-Cyrl-TJ";
            case 1065:
                return "fa-IR";
            case 1066:
                return "vi-VN";
            case 1067:
                return "hy-AM";
            case MysqlErrorNumbers.ER_MULTIPLE_PRI_KEY /* 1068 */:
                return "az-Latn-AZ";
            case 1069:
                return "eu-ES";
            case 1070:
                return "hsb-DE";
            case 1071:
                return "mk-MK";
            case 1072:
                return "st-ZA";
            case 1073:
                return "ts-ZA";
            case 1074:
                return "tn-ZA";
            case 1075:
                return "ve-ZA";
            case 1076:
                return "xh-ZA";
            case 1077:
                return "zu-ZA";
            case 1078:
                return "af-ZA";
            case MysqlErrorNumbers.ER_SHUTDOWN_COMPLETE /* 1079 */:
                return "ka-GE";
            case 1080:
                return "fo-FO";
            case MysqlErrorNumbers.ER_IPSOCK_ERROR /* 1081 */:
                return "hi-IN";
            case 1082:
                return "mt-MT";
            case 1083:
                return "se-NO";
            case 1085:
                return "yi-Hebr";
            case 1086:
                return "ms-MY";
            case 1087:
                return "kk-KZ";
            case 1088:
                return "ky-KG";
            case MysqlErrorNumbers.ER_WRONG_SUB_KEY /* 1089 */:
                return "sw-KE";
            case MysqlErrorNumbers.ER_CANT_REMOVE_ALL_FIELDS /* 1090 */:
                return "tk-TM";
            case MysqlErrorNumbers.ER_CANT_DROP_FIELD_OR_KEY /* 1091 */:
                return "uz-Latn-UZ";
            case MysqlErrorNumbers.ER_INSERT_INFO /* 1092 */:
                return "tt-RU";
            case MysqlErrorNumbers.ER_UPDATE_TABLE_USED /* 1093 */:
                return "bn-IN";
            case MysqlErrorNumbers.ER_NO_SUCH_THREAD /* 1094 */:
                return "pa-IN";
            case MysqlErrorNumbers.ER_KILL_DENIED_ERROR /* 1095 */:
                return "gu-IN";
            case MysqlErrorNumbers.ER_NO_TABLES_USED /* 1096 */:
                return "or-IN";
            case MysqlErrorNumbers.ER_TOO_BIG_SET /* 1097 */:
                return "ta-IN";
            case MysqlErrorNumbers.ER_NO_UNIQUE_LOGFILE /* 1098 */:
                return "te-IN";
            case MysqlErrorNumbers.ER_TABLE_NOT_LOCKED_FOR_WRITE /* 1099 */:
                return "kn-IN";
            case MysqlErrorNumbers.ER_TABLE_NOT_LOCKED /* 1100 */:
                return "ml-IN";
            case MysqlErrorNumbers.ER_BLOB_CANT_HAVE_DEFAULT /* 1101 */:
                return "as-IN";
            case MysqlErrorNumbers.ER_WRONG_DB_NAME /* 1102 */:
                return "mr-IN";
            case MysqlErrorNumbers.ER_WRONG_TABLE_NAME /* 1103 */:
                return "sa-IN";
            case MysqlErrorNumbers.ER_TOO_BIG_SELECT /* 1104 */:
                return "mn-MN";
            case MysqlErrorNumbers.ER_UNKNOWN_ERROR /* 1105 */:
                return "bo-CN";
            case MysqlErrorNumbers.ER_UNKNOWN_PROCEDURE /* 1106 */:
                return "cy-GB";
            case MysqlErrorNumbers.ER_WRONG_PARAMCOUNT_TO_PROCEDURE /* 1107 */:
                return "km-KH";
            case MysqlErrorNumbers.ER_WRONG_PARAMETERS_TO_PROCEDURE /* 1108 */:
                return "lo-LA";
            case MysqlErrorNumbers.ER_UNKNOWN_TABLE /* 1109 */:
                return "my-MM";
            case MysqlErrorNumbers.ER_FIELD_SPECIFIED_TWICE /* 1110 */:
                return "gl-ES";
            case MysqlErrorNumbers.ER_INVALID_GROUP_FUNC_USE /* 1111 */:
                return "kok-IN";
            case MysqlErrorNumbers.ER_UNSUPPORTED_EXTENSION /* 1112 */:
                return "mni-IN";
            case MysqlErrorNumbers.ER_TABLE_MUST_HAVE_COLUMNS /* 1113 */:
                return "sd-Deva-IN";
            case MysqlErrorNumbers.ER_RECORD_FILE_FULL /* 1114 */:
                return "syr-SY";
            case MysqlErrorNumbers.ER_UNKNOWN_CHARACTER_SET /* 1115 */:
                return "si-LK";
            case MysqlErrorNumbers.ER_TOO_MANY_TABLES /* 1116 */:
                return "chr-Cher-US";
            case MysqlErrorNumbers.ER_TOO_MANY_FIELDS /* 1117 */:
                return "iu-Cans-CA";
            case MysqlErrorNumbers.ER_TOO_BIG_ROWSIZE /* 1118 */:
                return "am-ET";
            case MysqlErrorNumbers.ER_STACK_OVERRUN /* 1119 */:
                return "tzm-Arab-MA";
            case MysqlErrorNumbers.ER_WRONG_OUTER_JOIN /* 1120 */:
                return "ks-Arab";
            case MysqlErrorNumbers.ER_NULL_COLUMN_IN_INDEX /* 1121 */:
                return "ne-NP";
            case MysqlErrorNumbers.ER_CANT_FIND_UDF /* 1122 */:
                return "fy-NL";
            case MysqlErrorNumbers.ER_CANT_INITIALIZE_UDF /* 1123 */:
                return "ps-AF";
            case MysqlErrorNumbers.ER_UDF_NO_PATHS /* 1124 */:
                return "fil-PH";
            case MysqlErrorNumbers.ER_UDF_EXISTS /* 1125 */:
                return "dv-MV";
            case MysqlErrorNumbers.ER_CANT_OPEN_LIBRARY /* 1126 */:
                return "bin-NG";
            case MysqlErrorNumbers.ER_CANT_FIND_DL_ENTRY /* 1127 */:
                return "fuv-NG";
            case MysqlErrorNumbers.ER_FUNCTION_NOT_DEFINED /* 1128 */:
                return "ha-Latn-NG";
            case MysqlErrorNumbers.ER_HOST_IS_BLOCKED /* 1129 */:
                return "ibb-NG";
            case MysqlErrorNumbers.ER_HOST_NOT_PRIVILEGED /* 1130 */:
                return "yo-NG";
            case MysqlErrorNumbers.ER_PASSWORD_ANONYMOUS_USER /* 1131 */:
                return "quz-BO";
            case MysqlErrorNumbers.ER_PASSWORD_NOT_ALLOWED /* 1132 */:
                return "nso-ZA";
            case MysqlErrorNumbers.ER_PASSWORD_NO_MATCH /* 1133 */:
                return "ba-RU";
            case MysqlErrorNumbers.ER_UPDATE_INFO /* 1134 */:
                return "lb-LU";
            case MysqlErrorNumbers.ER_CANT_CREATE_THREAD /* 1135 */:
                return "kl-GL";
            case MysqlErrorNumbers.ER_WRONG_VALUE_COUNT_ON_ROW /* 1136 */:
                return "ig-NG";
            case MysqlErrorNumbers.ER_CANT_REOPEN_TABLE /* 1137 */:
                return "kr-NG";
            case MysqlErrorNumbers.ER_INVALID_USE_OF_NULL /* 1138 */:
                return "om-Ethi-ET";
            case MysqlErrorNumbers.ER_REGEXP_ERROR /* 1139 */:
                return "ti-ET";
            case MysqlErrorNumbers.ER_MIX_OF_GROUP_FUNC_AND_FIELDS /* 1140 */:
                return "gn-PY";
            case MysqlErrorNumbers.ER_NONEXISTING_GRANT /* 1141 */:
                return "haw-US";
            case MysqlErrorNumbers.ER_TABLEACCESS_DENIED_ERROR /* 1142 */:
                return "la-Latn";
            case MysqlErrorNumbers.ER_COLUMNACCESS_DENIED_ERROR /* 1143 */:
                return "so-SO";
            case MysqlErrorNumbers.ER_ILLEGAL_GRANT_FOR_TABLE /* 1144 */:
                return "ii-CN";
            case MysqlErrorNumbers.ER_GRANT_WRONG_HOST_OR_USER /* 1145 */:
                return "pap-x029";
            case MysqlErrorNumbers.ER_NO_SUCH_TABLE /* 1146 */:
                return "arn-CL";
            case MysqlErrorNumbers.ER_NOT_ALLOWED_COMMAND /* 1148 */:
                return "moh-CA";
            case MysqlErrorNumbers.ER_DELAYED_CANT_CHANGE_LOCK /* 1150 */:
                return "br-FR";
            case MysqlErrorNumbers.ER_ABORTING_CONNECTION /* 1152 */:
                return "ug-CN";
            case MysqlErrorNumbers.ER_NET_PACKET_TOO_LARGE /* 1153 */:
                return "mi-NZ";
            case MysqlErrorNumbers.ER_NET_READ_ERROR_FROM_PIPE /* 1154 */:
                return "oc-FR";
            case MysqlErrorNumbers.ER_NET_FCNTL_ERROR /* 1155 */:
                return "co-FR";
            case MysqlErrorNumbers.ER_NET_PACKETS_OUT_OF_ORDER /* 1156 */:
                return "gsw-FR";
            case MysqlErrorNumbers.ER_NET_UNCOMPRESS_ERROR /* 1157 */:
                return "sah-RU";
            case MysqlErrorNumbers.ER_NET_READ_ERROR /* 1158 */:
                return "qut-GT";
            case MysqlErrorNumbers.ER_NET_READ_INTERRUPTED /* 1159 */:
                return "rw-RW";
            case MysqlErrorNumbers.ER_NET_ERROR_ON_WRITE /* 1160 */:
                return "wo-SN";
            case MysqlErrorNumbers.ER_TABLE_CANT_HANDLE_AUTO_INCREMENT /* 1164 */:
                return "prs-AF";
            case MysqlErrorNumbers.ER_DELAYED_INSERT_TABLE_LOCKED /* 1165 */:
                return "plt-MG";
            case MysqlErrorNumbers.ER_WRONG_COLUMN_NAME /* 1166 */:
                return "zh-yue-HK";
            case MysqlErrorNumbers.ER_WRONG_KEY_COLUMN /* 1167 */:
                return "tdd-Tale-CN";
            case MysqlErrorNumbers.ER_WRONG_MRG_TABLE /* 1168 */:
                return "khb-Talu-CN";
            case MysqlErrorNumbers.ER_DUP_UNIQUE /* 1169 */:
                return "gd-GB";
            case MysqlErrorNumbers.ER_BLOB_KEY_WITHOUT_LENGTH /* 1170 */:
                return "ku-Arab-IQ";
            case MysqlErrorNumbers.ER_PRIMARY_CANT_HAVE_NULL /* 1171 */:
                return "quc-CO";
            case 1281:
                return "qps-ploc";
            case MysqlErrorNumbers.ER_BINLOG_ROW_LOGGING_FAILED /* 1534 */:
                return "qps-ploca";
            case 2049:
                return "ar-IQ";
            case 2051:
                return "ca-ES-valencia";
            case 2052:
                return "zh-CN";
            case 2055:
                return "de-CH";
            case 2057:
                return "en-GB";
            case 2058:
                return "es-MX";
            case 2060:
                return "fr-BE";
            case 2064:
                return "it-CH";
            case 2065:
                return "ja-Ploc-JP";
            case 2067:
                return "nl-BE";
            case 2068:
                return "nn-NO";
            case 2070:
                return "pt-PT";
            case 2072:
                return "ro-MO";
            case 2073:
                return "ru-MO";
            case MetaDo.META_PIE /* 2074 */:
                return "sr-Latn-CS";
            case 2077:
                return "sv-FI";
            case 2080:
                return "ur-IN";
            case 2087:
                return "invalid";
            case 2092:
                return "az-Cyrl-AZ";
            case 2094:
                return "dsb-DE";
            case 2098:
                return "tn-BW";
            case 2107:
                return "se-SE";
            case 2108:
                return "ga-IE";
            case 2110:
                return "ms-BN";
            case 2115:
                return "uz-Cyrl-UZ";
            case 2117:
                return "bn-BD";
            case 2118:
                return "pa-Arab-PK";
            case 2121:
                return "ta-LK";
            case ChartFRTInfoRecord.sid /* 2128 */:
                return "mn-Mong-CN";
            case 2129:
                return "bo-BT";
            case 2137:
                return "sd-Arab-PK";
            case 2141:
                return "iu-Latn-CA";
            case 2143:
                return "tzm-Latn-DZ";
            case 2144:
                return "ks-Deva";
            case 2145:
                return "ne-IN";
            case 2151:
                return "ff-Latn-SN";
            case 2155:
                return "quz-EC";
            case 2163:
                return "ti-ER";
            case 2559:
                return "qps-plocm";
            case 3073:
                return "ar-EG";
            case 3076:
                return "zh-HK";
            case 3079:
                return "de-AT";
            case 3081:
                return "en-AU";
            case 3082:
                return "es-ES";
            case 3084:
                return "fr-CA";
            case 3098:
                return "sr-Cyrl-CS";
            case 3131:
                return "se-FI";
            case 3167:
                return "tmz-MA";
            case 3179:
                return "quz-PE";
            case 4097:
                return "ar-LY";
            case 4100:
                return "zh-SG";
            case 4103:
                return "de-LU";
            case OlympusMakernoteDirectory.TAG_LIGHT_CONDITION /* 4105 */:
                return "en-CA";
            case 4106:
                return "es-GT";
            case 4108:
                return "fr-CH";
            case 4122:
                return "hr-BA";
            case OlympusMakernoteDirectory.TAG_INFINITY_LENS_STEP /* 4155 */:
                return "smj-NO";
            case FujifilmMakernoteDirectory.TAG_FILM_MODE /* 5121 */:
                return "ar-DZ";
            case FujifilmMakernoteDirectory.TAG_MIN_FOCAL_LENGTH /* 5124 */:
                return "zh-MO";
            case FujifilmMakernoteDirectory.TAG_MAX_APERTURE_AT_MAX_FOCAL /* 5127 */:
                return "de-LI";
            case 5129:
                return "en-NZ";
            case 5130:
                return "es-CR";
            case 5132:
                return "fr-LU";
            case 5146:
                return "bs-Latn-BA";
            case 5179:
                return "smj-SE";
            case 6145:
                return "ar-MA";
            case 6153:
                return "en-IE";
            case 6154:
                return "es-PA";
            case 6156:
                return "fr-MC";
            case 6170:
                return "sr-Latn-BA";
            case 6203:
                return "sma-NO";
            case 7169:
                return "ar-TN";
            case 7177:
                return "en-ZA";
            case 7178:
                return "es-DO";
            case 7180:
                return "invalid";
            case 7194:
                return "sr-Cyrl-BA";
            case 7227:
                return "sma-SE";
            case 8193:
                return "ar-OM";
            case SonyType1MakernoteDirectory.TAG_LONG_EXPOSURE_NOISE_REDUCTION /* 8200 */:
                return "invalid";
            case SonyType1MakernoteDirectory.TAG_HIGH_ISO_NOISE_REDUCTION /* 8201 */:
                return "en-JM";
            case SonyType1MakernoteDirectory.TAG_HDR /* 8202 */:
                return "es-VE";
            case 8204:
                return "fr-RE";
            case 8218:
                return "bs-Cyrl-BA";
            case 8251:
                return "sms-FI";
            case 9217:
                return "ar-YE";
            case 9225:
                return "en-029";
            case 9226:
                return "es-CO";
            case 9228:
                return "fr-CG";
            case 9242:
                return "sr-Latn-RS";
            case 9275:
                return "smn-FI";
            case 10241:
                return "ar-SY";
            case 10249:
                return "en-BZ";
            case 10250:
                return "es-PE";
            case 10252:
                return "fr-SN";
            case 10266:
                return "sr-Cyrl-RS";
            case 11265:
                return "ar-JO";
            case 11273:
                return "en-TT";
            case 11274:
                return "es-AR";
            case 11276:
                return "fr-CM";
            case 11290:
                return "sr-Latn-ME";
            case CasioType2MakernoteDirectory.TAG_SELF_TIMER /* 12289 */:
                return "ar-LB";
            case 12297:
                return "en-ZW";
            case 12298:
                return "es-EC";
            case 12300:
                return "fr-CI";
            case 12314:
                return "sr-Cyrl-ME";
            case 13313:
                return "ar-KW";
            case 13321:
                return "en-PH";
            case 13322:
                return "es-CL";
            case 13324:
                return "fr-ML";
            case 14337:
                return "ar-AE";
            case 14345:
                return "en-ID";
            case 14346:
                return "es-UY";
            case 14348:
                return "fr-MA";
            case 15361:
                return "ar-BH";
            case 15369:
                return "en-HK";
            case 15370:
                return "es-PY";
            case 15372:
                return "fr-HT";
            case CanonMakernoteDirectory.TAG_COLOR_DATA_ARRAY_2 /* 16385 */:
                return "ar-QA";
            case 16393:
                return "en-IN";
            case 16394:
                return "es-BO";
            case 17409:
                return "ar-Ploc-SA";
            case 17417:
                return "en-MY";
            case 17418:
                return "es-SV";
            case 18433:
                return "ar-145";
            case 18441:
                return "en-SG";
            case 18442:
                return "es-HN";
            case 19465:
                return "en-AE";
            case 19466:
                return "es-NI";
            case 20489:
                return "en-BH";
            case 20490:
                return "es-PR";
            case 21513:
                return "en-EG";
            case 21514:
                return "es-US";
            case 22537:
                return "en-JO";
            case 23561:
                return "en-KW";
            case 24585:
                return "en-TR";
            case 25609:
                return "en-YE";
            case 25626:
                return "bs-Cyrl";
            case 26650:
                return "bs-Latn";
            case 27674:
                return "sr-Cyrl";
            case 28698:
                return "sr-Latn";
            case 28731:
                return "smn";
            case 29740:
                return "az-Cyrl";
            case 29755:
                return "sms";
            case 30724:
                return "zh";
            case 30740:
                return "nn";
            case 30746:
                return "bs";
            case 30764:
                return "az-Latn";
            case 30779:
                return "sma";
            case 30787:
                return "uz-Cyrl";
            case 30800:
                return "mn-Cyrl";
            case 30813:
                return "iu-Cans";
            case 31748:
                return "zh-Hant";
            case 31764:
                return "nb";
            case 31770:
                return "sr";
            case 31784:
                return "tg-Cyrl";
            case 31790:
                return "dsb";
            case 31803:
                return "smj";
            case 31811:
                return "uz-Latn";
            case 31814:
                return "pa-Arab";
            case 31824:
                return "mn-Mong";
            case 31833:
                return "sd-Arab";
            case 31836:
                return "chr-Cher";
            case 31837:
                return "iu-Latn";
            case 31839:
                return "tzm-Latn";
            case 31847:
                return "ff-Latn";
            case 31848:
                return "ha-Latn";
            case 31890:
                return "ku-Arab";
            default:
                return "invalid";
        }
    }
}
