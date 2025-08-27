package org.apache.poi.ss.util;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.SheetNameFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.util.StringUtil;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/util/CellReference.class */
public class CellReference {
    private static final char ABSOLUTE_REFERENCE_MARKER = '$';
    private static final char SHEET_NAME_DELIMITER = '!';
    private static final char SPECIAL_NAME_DELIMITER = '\'';
    private static final Pattern CELL_REF_PATTERN = Pattern.compile("(\\$?[A-Z]+)?(\\$?[0-9]+)?", 2);
    private static final Pattern STRICTLY_CELL_REF_PATTERN = Pattern.compile("\\$?([A-Z]+)\\$?([0-9]+)", 2);
    private static final Pattern COLUMN_REF_PATTERN = Pattern.compile("\\$?([A-Z]+)", 2);
    private static final Pattern ROW_REF_PATTERN = Pattern.compile("\\$?([0-9]+)");
    private static final Pattern NAMED_RANGE_NAME_PATTERN = Pattern.compile("[_A-Z][_.A-Z0-9]*", 2);
    private final String _sheetName;
    private final int _rowIndex;
    private final int _colIndex;
    private final boolean _isRowAbs;
    private final boolean _isColAbs;

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/util/CellReference$NameType.class */
    public enum NameType {
        CELL,
        NAMED_RANGE,
        COLUMN,
        ROW,
        BAD_CELL_OR_NAMED_RANGE
    }

    public CellReference(String cellRef) {
        if (StringUtil.endsWithIgnoreCase(cellRef, "#REF!")) {
            throw new IllegalArgumentException("Cell reference invalid: " + cellRef);
        }
        CellRefParts parts = separateRefParts(cellRef);
        this._sheetName = parts.sheetName;
        String colRef = parts.colRef;
        this._isColAbs = colRef.length() > 0 && colRef.charAt(0) == '$';
        colRef = this._isColAbs ? colRef.substring(1) : colRef;
        if (colRef.length() == 0) {
            this._colIndex = -1;
        } else {
            this._colIndex = convertColStringToIndex(colRef);
        }
        String rowRef = parts.rowRef;
        this._isRowAbs = rowRef.length() > 0 && rowRef.charAt(0) == '$';
        rowRef = this._isRowAbs ? rowRef.substring(1) : rowRef;
        if (rowRef.length() == 0) {
            this._rowIndex = -1;
        } else {
            this._rowIndex = Integer.parseInt(rowRef) - 1;
        }
    }

    public CellReference(int pRow, int pCol) {
        this(pRow, pCol, false, false);
    }

    public CellReference(int pRow, short pCol) {
        this(pRow, pCol & 65535, false, false);
    }

    public CellReference(Cell cell) {
        this(cell.getRowIndex(), cell.getColumnIndex(), false, false);
    }

    public CellReference(int pRow, int pCol, boolean pAbsRow, boolean pAbsCol) {
        this(null, pRow, pCol, pAbsRow, pAbsCol);
    }

    public CellReference(String pSheetName, int pRow, int pCol, boolean pAbsRow, boolean pAbsCol) {
        if (pRow < -1) {
            throw new IllegalArgumentException("row index may not be negative, but had " + pRow);
        }
        if (pCol < -1) {
            throw new IllegalArgumentException("column index may not be negative, but had " + pCol);
        }
        this._sheetName = pSheetName;
        this._rowIndex = pRow;
        this._colIndex = pCol;
        this._isRowAbs = pAbsRow;
        this._isColAbs = pAbsCol;
    }

    public int getRow() {
        return this._rowIndex;
    }

    public short getCol() {
        return (short) this._colIndex;
    }

    public boolean isRowAbsolute() {
        return this._isRowAbs;
    }

    public boolean isColAbsolute() {
        return this._isColAbs;
    }

    public String getSheetName() {
        return this._sheetName;
    }

    public static boolean isPartAbsolute(String part) {
        return part.charAt(0) == '$';
    }

    public static int convertColStringToIndex(String ref) {
        int retval = 0;
        char[] refs = ref.toUpperCase(Locale.ROOT).toCharArray();
        for (int k = 0; k < refs.length; k++) {
            char thechar = refs[k];
            if (thechar == '$') {
                if (k != 0) {
                    throw new IllegalArgumentException("Bad col ref format '" + ref + "'");
                }
            } else {
                retval = (retval * 26) + (thechar - 'A') + 1;
            }
        }
        return retval - 1;
    }

    public static NameType classifyCellReference(String str, SpreadsheetVersion ssVersion) {
        int len = str.length();
        if (len < 1) {
            throw new IllegalArgumentException("Empty string not allowed");
        }
        char firstChar = str.charAt(0);
        switch (firstChar) {
            case '$':
            case '.':
            case '_':
                break;
            default:
                if (!Character.isLetter(firstChar) && !Character.isDigit(firstChar)) {
                    throw new IllegalArgumentException("Invalid first char (" + firstChar + ") of cell reference or named range.  Letter expected");
                }
                break;
        }
        if (!Character.isDigit(str.charAt(len - 1))) {
            return validateNamedRangeName(str, ssVersion);
        }
        Matcher cellRefPatternMatcher = STRICTLY_CELL_REF_PATTERN.matcher(str);
        if (!cellRefPatternMatcher.matches()) {
            return validateNamedRangeName(str, ssVersion);
        }
        String lettersGroup = cellRefPatternMatcher.group(1);
        String digitsGroup = cellRefPatternMatcher.group(2);
        if (cellReferenceIsWithinRange(lettersGroup, digitsGroup, ssVersion)) {
            return NameType.CELL;
        }
        if (str.indexOf(36) >= 0) {
            return NameType.BAD_CELL_OR_NAMED_RANGE;
        }
        return NameType.NAMED_RANGE;
    }

    private static NameType validateNamedRangeName(String str, SpreadsheetVersion ssVersion) {
        Matcher colMatcher = COLUMN_REF_PATTERN.matcher(str);
        if (colMatcher.matches()) {
            String colStr = colMatcher.group(1);
            if (isColumnWithinRange(colStr, ssVersion)) {
                return NameType.COLUMN;
            }
        }
        Matcher rowMatcher = ROW_REF_PATTERN.matcher(str);
        if (rowMatcher.matches()) {
            String rowStr = rowMatcher.group(1);
            if (isRowWithinRange(rowStr, ssVersion)) {
                return NameType.ROW;
            }
        }
        if (!NAMED_RANGE_NAME_PATTERN.matcher(str).matches()) {
            return NameType.BAD_CELL_OR_NAMED_RANGE;
        }
        return NameType.NAMED_RANGE;
    }

    public static boolean cellReferenceIsWithinRange(String colStr, String rowStr, SpreadsheetVersion ssVersion) {
        if (!isColumnWithinRange(colStr, ssVersion)) {
            return false;
        }
        return isRowWithinRange(rowStr, ssVersion);
    }

    public static boolean isColumnWithinRange(String colStr, SpreadsheetVersion ssVersion) {
        String lastCol = ssVersion.getLastColumnName();
        int lastColLength = lastCol.length();
        int numberOfLetters = colStr.length();
        if (numberOfLetters > lastColLength) {
            return false;
        }
        if (numberOfLetters == lastColLength && colStr.toUpperCase(Locale.ROOT).compareTo(lastCol) > 0) {
            return false;
        }
        return true;
    }

    public static boolean isRowWithinRange(String rowStr, SpreadsheetVersion ssVersion) {
        int rowNum = Integer.parseInt(rowStr) - 1;
        return isRowWithinRange(rowNum, ssVersion);
    }

    public static boolean isRowWithinRange(int rowNum, SpreadsheetVersion ssVersion) {
        return 0 <= rowNum && rowNum <= ssVersion.getLastRowIndex();
    }

    /* loaded from: poi-3.17.jar:org/apache/poi/ss/util/CellReference$CellRefParts.class */
    private static final class CellRefParts {
        final String sheetName;
        final String rowRef;
        final String colRef;

        private CellRefParts(String sheetName, String rowRef, String colRef) {
            this.sheetName = sheetName;
            this.rowRef = rowRef != null ? rowRef : "";
            this.colRef = colRef != null ? colRef : "";
        }
    }

    private static CellRefParts separateRefParts(String reference) {
        int plingPos = reference.lastIndexOf(33);
        String sheetName = parseSheetName(reference, plingPos);
        String cell = reference.substring(plingPos + 1).toUpperCase(Locale.ROOT);
        Matcher matcher = CELL_REF_PATTERN.matcher(cell);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid CellReference: " + reference);
        }
        String col = matcher.group(1);
        String row = matcher.group(2);
        CellRefParts cellRefParts = new CellRefParts(sheetName, row, col);
        return cellRefParts;
    }

    private static String parseSheetName(String reference, int indexOfSheetNameDelimiter) {
        if (indexOfSheetNameDelimiter < 0) {
            return null;
        }
        boolean isQuoted = reference.charAt(0) == '\'';
        if (!isQuoted) {
            if (!reference.contains(SymbolConstants.SPACE_SYMBOL)) {
                return reference.substring(0, indexOfSheetNameDelimiter);
            }
            throw new IllegalArgumentException("Sheet names containing spaces must be quoted: (" + reference + ")");
        }
        int lastQuotePos = indexOfSheetNameDelimiter - 1;
        if (reference.charAt(lastQuotePos) != '\'') {
            throw new IllegalArgumentException("Mismatched quotes: (" + reference + ")");
        }
        StringBuffer sb = new StringBuffer(indexOfSheetNameDelimiter);
        int i = 1;
        while (i < lastQuotePos) {
            char ch2 = reference.charAt(i);
            if (ch2 != '\'') {
                sb.append(ch2);
            } else if (i + 1 < lastQuotePos && reference.charAt(i + 1) == '\'') {
                i++;
                sb.append(ch2);
            } else {
                throw new IllegalArgumentException("Bad sheet name quote escaping: (" + reference + ")");
            }
            i++;
        }
        return sb.toString();
    }

    public static String convertNumToColString(int col) {
        int excelColNum = col + 1;
        StringBuilder colRef = new StringBuilder(2);
        int colRemain = excelColNum;
        while (colRemain > 0) {
            int thisPart = colRemain % 26;
            if (thisPart == 0) {
                thisPart = 26;
            }
            colRemain = (colRemain - thisPart) / 26;
            char colChar = (char) (thisPart + 64);
            colRef.insert(0, colChar);
        }
        return colRef.toString();
    }

    public String formatAsString() {
        StringBuffer sb = new StringBuffer(32);
        if (this._sheetName != null) {
            SheetNameFormatter.appendFormat(sb, this._sheetName);
            sb.append('!');
        }
        appendCellReference(sb);
        return sb.toString();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(64);
        sb.append(getClass().getName()).append(" [");
        sb.append(formatAsString());
        sb.append("]");
        return sb.toString();
    }

    public String[] getCellRefParts() {
        return new String[]{this._sheetName, Integer.toString(this._rowIndex + 1), convertNumToColString(this._colIndex)};
    }

    void appendCellReference(StringBuffer sb) {
        if (this._colIndex != -1) {
            if (this._isColAbs) {
                sb.append('$');
            }
            sb.append(convertNumToColString(this._colIndex));
        }
        if (this._rowIndex != -1) {
            if (this._isRowAbs) {
                sb.append('$');
            }
            sb.append(this._rowIndex + 1);
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CellReference)) {
            return false;
        }
        CellReference cr = (CellReference) o;
        return this._rowIndex == cr._rowIndex && this._colIndex == cr._colIndex && this._isRowAbs == cr._isRowAbs && this._isColAbs == cr._isColAbs && (this._sheetName != null ? this._sheetName.equals(cr._sheetName) : cr._sheetName == null);
    }

    public int hashCode() {
        int result = (31 * 17) + this._rowIndex;
        return (31 * ((31 * ((31 * ((31 * result) + this._colIndex)) + (this._isRowAbs ? 1 : 0))) + (this._isColAbs ? 1 : 0))) + (this._sheetName == null ? 0 : this._sheetName.hashCode());
    }
}
