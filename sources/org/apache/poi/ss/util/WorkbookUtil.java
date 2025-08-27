package org.apache.poi.ss.util;

import org.apache.poi.util.Removal;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/util/WorkbookUtil.class */
public class WorkbookUtil {
    public static final String createSafeSheetName(String nameProposal) {
        return createSafeSheetName(nameProposal, ' ');
    }

    public static final String createSafeSheetName(String nameProposal, char replaceChar) {
        if (nameProposal == null) {
            return "null";
        }
        if (nameProposal.length() < 1) {
            return "empty";
        }
        int length = Math.min(31, nameProposal.length());
        String shortenname = nameProposal.substring(0, length);
        StringBuilder result = new StringBuilder(shortenname);
        for (int i = 0; i < length; i++) {
            char ch2 = result.charAt(i);
            switch (ch2) {
                case 0:
                case 3:
                case '*':
                case '/':
                case ':':
                case '?':
                case '[':
                case '\\':
                case ']':
                    result.setCharAt(i, replaceChar);
                    break;
                case '\'':
                    if (i != 0 && i != length - 1) {
                        break;
                    } else {
                        result.setCharAt(i, replaceChar);
                        break;
                    }
                    break;
            }
        }
        return result.toString();
    }

    public static void validateSheetName(String sheetName) {
        if (sheetName == null) {
            throw new IllegalArgumentException("sheetName must not be null");
        }
        int len = sheetName.length();
        if (len < 1 || len > 31) {
            throw new IllegalArgumentException("sheetName '" + sheetName + "' is invalid - character count MUST be greater than or equal to 1 and less than or equal to 31");
        }
        for (int i = 0; i < len; i++) {
            char ch2 = sheetName.charAt(i);
            switch (ch2) {
                case '*':
                case '/':
                case ':':
                case '?':
                case '[':
                case '\\':
                case ']':
                    throw new IllegalArgumentException("Invalid char (" + ch2 + ") found at index (" + i + ") in sheet name '" + sheetName + "'");
                default:
            }
        }
        if (sheetName.charAt(0) == '\'' || sheetName.charAt(len - 1) == '\'') {
            throw new IllegalArgumentException("Invalid sheet name '" + sheetName + "'. Sheet names must not begin or end with (').");
        }
    }

    @Removal(version = "3.18")
    @Deprecated
    public static void validateSheetState(int state) {
        switch (state) {
            case 0:
            case 1:
            case 2:
                return;
            default:
                throw new IllegalArgumentException("Invalid sheet state : " + state + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR + "Sheet state must be one of the Workbook.SHEET_STATE_* constants");
        }
    }
}
