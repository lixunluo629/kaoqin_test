package com.itextpdf.kernel.numbering;

import org.apache.poi.ddf.EscherProperties;
import org.apache.poi.util.CodePageUtil;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/numbering/GreekAlphabetNumbering.class */
public class GreekAlphabetNumbering {
    protected static final char[] ALPHABET_LOWERCASE = new char[24];
    protected static final char[] ALPHABET_UPPERCASE = new char[24];
    protected static final int ALPHABET_LENGTH = 24;

    static {
        int i = 0;
        while (i < 24) {
            ALPHABET_LOWERCASE[i] = (char) (945 + i + (i > 16 ? 1 : 0));
            ALPHABET_UPPERCASE[i] = (char) (EscherProperties.GROUPSHAPE__POSV + i + (i > 16 ? 1 : 0));
            i++;
        }
    }

    public static String toGreekAlphabetNumberLowerCase(int number) {
        return AlphabetNumbering.toAlphabetNumber(number, ALPHABET_LOWERCASE);
    }

    public static String toGreekAlphabetNumberUpperCase(int number) {
        return AlphabetNumbering.toAlphabetNumber(number, ALPHABET_UPPERCASE);
    }

    public static String toGreekAlphabetNumber(int number, boolean upperCase) {
        return toGreekAlphabetNumber(number, upperCase, false);
    }

    public static String toGreekAlphabetNumber(int number, boolean upperCase, boolean symbolFont) {
        String result = upperCase ? toGreekAlphabetNumberUpperCase(number) : toGreekAlphabetNumberLowerCase(number);
        if (symbolFont) {
            StringBuilder symbolFontStr = new StringBuilder();
            for (int i = 0; i < result.length(); i++) {
                symbolFontStr.append(getSymbolFontChar(result.charAt(i)));
            }
            return symbolFontStr.toString();
        }
        return result;
    }

    private static char getSymbolFontChar(char unicodeChar) {
        switch (unicodeChar) {
            case EscherProperties.GROUPSHAPE__POSV /* 913 */:
                return 'A';
            case EscherProperties.GROUPSHAPE__POSRELV /* 914 */:
                return 'B';
            case EscherProperties.GROUPSHAPE__HR_PCT /* 915 */:
                return 'G';
            case EscherProperties.GROUPSHAPE__HR_ALIGN /* 916 */:
                return 'D';
            case EscherProperties.GROUPSHAPE__HR_HEIGHT /* 917 */:
                return 'E';
            case EscherProperties.GROUPSHAPE__HR_WIDTH /* 918 */:
                return 'Z';
            case EscherProperties.GROUPSHAPE__SCRIPTEXT /* 919 */:
                return 'H';
            case EscherProperties.GROUPSHAPE__SCRIPTLANG /* 920 */:
                return 'Q';
            case 921:
                return 'I';
            case 922:
                return 'K';
            case EscherProperties.GROUPSHAPE__BORDERTOPCOLOR /* 923 */:
                return 'L';
            case EscherProperties.GROUPSHAPE__BORDERLEFTCOLOR /* 924 */:
                return 'M';
            case EscherProperties.GROUPSHAPE__BORDERBOTTOMCOLOR /* 925 */:
                return 'N';
            case EscherProperties.GROUPSHAPE__BORDERRIGHTCOLOR /* 926 */:
                return 'X';
            case EscherProperties.GROUPSHAPE__TABLEPROPERTIES /* 927 */:
                return 'O';
            case EscherProperties.GROUPSHAPE__TABLEROWPROPERTIES /* 928 */:
                return 'P';
            case 929:
                return 'R';
            case 930:
            case EscherProperties.GROUPSHAPE__ZORDER /* 938 */:
            case 939:
            case 940:
            case 941:
            case 942:
            case 943:
            case 944:
            default:
                return ' ';
            case 931:
                return 'S';
            case CodePageUtil.CP_SJIS /* 932 */:
                return 'T';
            case EscherProperties.GROUPSHAPE__WEBBOT /* 933 */:
                return 'U';
            case 934:
                return 'F';
            case 935:
                return 'C';
            case CodePageUtil.CP_GBK /* 936 */:
                return 'Y';
            case EscherProperties.GROUPSHAPE__METROBLOB /* 937 */:
                return 'W';
            case 945:
                return 'a';
            case 946:
                return 'b';
            case 947:
                return 'g';
            case 948:
                return 'd';
            case CodePageUtil.CP_MS949 /* 949 */:
                return 'e';
            case 950:
                return 'z';
            case 951:
                return 'h';
            case 952:
                return 'q';
            case EscherProperties.GROUPSHAPE__EDITEDWRAP /* 953 */:
                return 'i';
            case EscherProperties.GROUPSHAPE__BEHINDDOCUMENT /* 954 */:
                return 'k';
            case EscherProperties.GROUPSHAPE__ONDBLCLICKNOTIFY /* 955 */:
                return 'l';
            case EscherProperties.GROUPSHAPE__ISBUTTON /* 956 */:
                return 'm';
            case EscherProperties.GROUPSHAPE__1DADJUSTMENT /* 957 */:
                return 'n';
            case EscherProperties.GROUPSHAPE__HIDDEN /* 958 */:
                return 'x';
            case 959:
                return 'o';
            case 960:
                return 'p';
            case 961:
                return 'r';
            case 962:
                return 'V';
            case 963:
                return 's';
            case 964:
                return 't';
            case 965:
                return 'u';
            case 966:
                return 'f';
            case 967:
                return 'c';
            case 968:
                return 'y';
            case 969:
                return 'w';
        }
    }
}
