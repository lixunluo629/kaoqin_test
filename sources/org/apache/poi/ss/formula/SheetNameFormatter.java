package org.apache.poi.ss.formula;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.util.CellReference;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/SheetNameFormatter.class */
public final class SheetNameFormatter {
    private static final char DELIMITER = '\'';
    private static final Pattern CELL_REF_PATTERN = Pattern.compile("([A-Za-z]+)([0-9]+)");

    private SheetNameFormatter() {
    }

    public static String format(String rawSheetName) {
        StringBuffer sb = new StringBuffer(rawSheetName.length() + 2);
        appendFormat(sb, rawSheetName);
        return sb.toString();
    }

    public static void appendFormat(StringBuffer out, String rawSheetName) {
        boolean needsQuotes = needsDelimiting(rawSheetName);
        if (needsQuotes) {
            out.append('\'');
            appendAndEscape(out, rawSheetName);
            out.append('\'');
            return;
        }
        out.append(rawSheetName);
    }

    public static void appendFormat(StringBuffer out, String workbookName, String rawSheetName) {
        boolean needsQuotes = needsDelimiting(workbookName) || needsDelimiting(rawSheetName);
        if (needsQuotes) {
            out.append('\'');
            out.append('[');
            appendAndEscape(out, workbookName.replace('[', '(').replace(']', ')'));
            out.append(']');
            appendAndEscape(out, rawSheetName);
            out.append('\'');
            return;
        }
        out.append('[');
        out.append(workbookName);
        out.append(']');
        out.append(rawSheetName);
    }

    private static void appendAndEscape(StringBuffer sb, String rawSheetName) {
        int len = rawSheetName.length();
        for (int i = 0; i < len; i++) {
            char ch2 = rawSheetName.charAt(i);
            if (ch2 == '\'') {
                sb.append('\'');
            }
            sb.append(ch2);
        }
    }

    private static boolean needsDelimiting(String rawSheetName) {
        int len = rawSheetName.length();
        if (len < 1) {
            throw new RuntimeException("Zero length string is an invalid sheet name");
        }
        if (Character.isDigit(rawSheetName.charAt(0))) {
            return true;
        }
        for (int i = 0; i < len; i++) {
            char ch2 = rawSheetName.charAt(i);
            if (isSpecialChar(ch2)) {
                return true;
            }
        }
        if ((Character.isLetter(rawSheetName.charAt(0)) && Character.isDigit(rawSheetName.charAt(len - 1)) && nameLooksLikePlainCellReference(rawSheetName)) || nameLooksLikeBooleanLiteral(rawSheetName)) {
            return true;
        }
        return false;
    }

    private static boolean nameLooksLikeBooleanLiteral(String rawSheetName) {
        switch (rawSheetName.charAt(0)) {
            case 'F':
            case 'f':
                return "FALSE".equalsIgnoreCase(rawSheetName);
            case 'T':
            case 't':
                return "TRUE".equalsIgnoreCase(rawSheetName);
            default:
                return false;
        }
    }

    static boolean isSpecialChar(char ch2) {
        if (Character.isLetterOrDigit(ch2)) {
            return false;
        }
        switch (ch2) {
            case '\t':
            case '\n':
            case '\r':
                throw new RuntimeException("Illegal character (0x" + Integer.toHexString(ch2) + ") found in sheet name");
            case '.':
            case '_':
                return false;
            default:
                return true;
        }
    }

    static boolean cellReferenceIsWithinRange(String lettersPrefix, String numbersSuffix) {
        return CellReference.cellReferenceIsWithinRange(lettersPrefix, numbersSuffix, SpreadsheetVersion.EXCEL97);
    }

    static boolean nameLooksLikePlainCellReference(String rawSheetName) {
        Matcher matcher = CELL_REF_PATTERN.matcher(rawSheetName);
        if (!matcher.matches()) {
            return false;
        }
        String lettersPrefix = matcher.group(1);
        String numbersSuffix = matcher.group(2);
        return cellReferenceIsWithinRange(lettersPrefix, numbersSuffix);
    }
}
