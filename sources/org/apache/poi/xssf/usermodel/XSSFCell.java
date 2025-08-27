package org.apache.poi.xssf.usermodel;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.FormulaParser;
import org.apache.poi.ss.formula.FormulaRenderer;
import org.apache.poi.ss.formula.FormulaType;
import org.apache.poi.ss.formula.SharedFormula;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellCopyPolicy;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaError;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.Internal;
import org.apache.poi.util.LocaleUtil;
import org.apache.poi.util.Removal;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCell;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellFormula;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellFormulaType;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellType;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/usermodel/XSSFCell.class */
public final class XSSFCell implements Cell {
    private static final String FALSE_AS_STRING = "0";
    private static final String TRUE_AS_STRING = "1";
    private static final String FALSE = "FALSE";
    private static final String TRUE = "TRUE";
    private CTCell _cell;
    private final XSSFRow _row;
    private int _cellNum;
    private SharedStringsTable _sharedStringSource;
    private StylesTable _stylesSource;

    protected XSSFCell(XSSFRow row, CTCell cell) {
        this._cell = cell;
        this._row = row;
        if (cell.getR() != null) {
            this._cellNum = new CellReference(cell.getR()).getCol();
        } else {
            int prevNum = row.getLastCellNum();
            if (prevNum != -1) {
                this._cellNum = row.getCell(prevNum - 1, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK).getColumnIndex() + 1;
            }
        }
        this._sharedStringSource = row.getSheet().getWorkbook().getSharedStringSource();
        this._stylesSource = row.getSheet().getWorkbook().getStylesSource();
    }

    @Internal
    public void copyCellFrom(Cell srcCell, CellCopyPolicy policy) throws IllegalArgumentException {
        if (policy.isCopyCellValue()) {
            if (srcCell != null) {
                CellType copyCellType = srcCell.getCellTypeEnum();
                if (copyCellType == CellType.FORMULA && !policy.isCopyCellFormula()) {
                    copyCellType = srcCell.getCachedFormulaResultTypeEnum();
                }
                switch (copyCellType) {
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(srcCell)) {
                            setCellValue(srcCell.getDateCellValue());
                            break;
                        } else {
                            setCellValue(srcCell.getNumericCellValue());
                            break;
                        }
                    case STRING:
                        setCellValue(srcCell.getStringCellValue());
                        break;
                    case FORMULA:
                        setCellFormula(srcCell.getCellFormula());
                        break;
                    case BLANK:
                        setBlank();
                        break;
                    case BOOLEAN:
                        setCellValue(srcCell.getBooleanCellValue());
                        break;
                    case ERROR:
                        setCellErrorValue(srcCell.getErrorCellValue());
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid cell type " + srcCell.getCellTypeEnum());
                }
            } else {
                setBlank();
            }
        }
        if (policy.isCopyCellStyle()) {
            setCellStyle(srcCell == null ? null : srcCell.getCellStyle());
        }
        Hyperlink srcHyperlink = srcCell == null ? null : srcCell.getHyperlink();
        if (policy.isMergeHyperlink()) {
            if (srcHyperlink != null) {
                setHyperlink(new XSSFHyperlink(srcHyperlink));
            }
        } else if (policy.isCopyHyperlink()) {
            setHyperlink(srcHyperlink == null ? null : new XSSFHyperlink(srcHyperlink));
        }
    }

    protected SharedStringsTable getSharedStringSource() {
        return this._sharedStringSource;
    }

    protected StylesTable getStylesSource() {
        return this._stylesSource;
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public XSSFSheet getSheet() {
        return getRow().getSheet();
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public XSSFRow getRow() {
        return this._row;
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public boolean getBooleanCellValue() {
        CellType cellType = getCellTypeEnum();
        switch (cellType) {
            case FORMULA:
                return this._cell.isSetV() && "1".equals(this._cell.getV());
            case BLANK:
                return false;
            case BOOLEAN:
                return this._cell.isSetV() && "1".equals(this._cell.getV());
            default:
                throw typeMismatch(CellType.BOOLEAN, cellType, false);
        }
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void setCellValue(boolean value) {
        this._cell.setT(STCellType.B);
        this._cell.setV(value ? "1" : "0");
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public double getNumericCellValue() {
        CellType cellType = getCellTypeEnum();
        switch (cellType) {
            case NUMERIC:
            case FORMULA:
                if (this._cell.isSetV()) {
                    String v = this._cell.getV();
                    if (v.isEmpty()) {
                        return 0.0d;
                    }
                    try {
                        return Double.parseDouble(v);
                    } catch (NumberFormatException e) {
                        throw typeMismatch(CellType.NUMERIC, CellType.STRING, false);
                    }
                }
                return 0.0d;
            case STRING:
            default:
                throw typeMismatch(CellType.NUMERIC, cellType, false);
            case BLANK:
                return 0.0d;
        }
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void setCellValue(double value) {
        if (Double.isInfinite(value)) {
            this._cell.setT(STCellType.E);
            this._cell.setV(FormulaError.DIV0.getString());
        } else if (Double.isNaN(value)) {
            this._cell.setT(STCellType.E);
            this._cell.setV(FormulaError.NUM.getString());
        } else {
            this._cell.setT(STCellType.N);
            this._cell.setV(String.valueOf(value));
        }
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public String getStringCellValue() {
        return getRichStringCellValue().getString();
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public XSSFRichTextString getRichStringCellValue() throws NumberFormatException {
        XSSFRichTextString rt;
        CellType cellType = getCellTypeEnum();
        switch (cellType) {
            case STRING:
                if (this._cell.getT() == STCellType.INLINE_STR) {
                    if (this._cell.isSetIs()) {
                        rt = new XSSFRichTextString(this._cell.getIs());
                        break;
                    } else if (this._cell.isSetV()) {
                        rt = new XSSFRichTextString(this._cell.getV());
                        break;
                    } else {
                        rt = new XSSFRichTextString("");
                        break;
                    }
                } else if (this._cell.getT() == STCellType.STR) {
                    rt = new XSSFRichTextString(this._cell.isSetV() ? this._cell.getV() : "");
                    break;
                } else if (this._cell.isSetV()) {
                    int idx = Integer.parseInt(this._cell.getV());
                    rt = new XSSFRichTextString(this._sharedStringSource.getEntryAt(idx));
                    break;
                } else {
                    rt = new XSSFRichTextString("");
                    break;
                }
            case FORMULA:
                checkFormulaCachedValueType(CellType.STRING, getBaseCellType(false));
                rt = new XSSFRichTextString(this._cell.isSetV() ? this._cell.getV() : "");
                break;
            case BLANK:
                rt = new XSSFRichTextString("");
                break;
            default:
                throw typeMismatch(CellType.STRING, cellType, false);
        }
        rt.setStylesTableReference(this._stylesSource);
        return rt;
    }

    private static void checkFormulaCachedValueType(CellType expectedTypeCode, CellType cachedValueType) {
        if (cachedValueType != expectedTypeCode) {
            throw typeMismatch(expectedTypeCode, cachedValueType, true);
        }
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void setCellValue(String str) throws NumberFormatException {
        setCellValue(str == null ? null : new XSSFRichTextString(str));
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void setCellValue(RichTextString str) throws NumberFormatException {
        if (str == null || str.getString() == null) {
            setCellType(CellType.BLANK);
            return;
        }
        if (str.length() > SpreadsheetVersion.EXCEL2007.getMaxTextLength()) {
            throw new IllegalArgumentException("The maximum length of cell contents (text) is 32,767 characters");
        }
        CellType cellType = getCellTypeEnum();
        switch (cellType) {
            case FORMULA:
                this._cell.setV(str.getString());
                this._cell.setT(STCellType.STR);
                return;
            default:
                if (this._cell.getT() == STCellType.INLINE_STR) {
                    this._cell.setV(str.getString());
                    return;
                }
                this._cell.setT(STCellType.S);
                XSSFRichTextString rt = (XSSFRichTextString) str;
                rt.setStylesTableReference(this._stylesSource);
                int sRef = this._sharedStringSource.addEntry(rt.getCTRst());
                this._cell.setV(Integer.toString(sRef));
                return;
        }
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public String getCellFormula() {
        return getCellFormula(null);
    }

    protected String getCellFormula(XSSFEvaluationWorkbook fpb) {
        CellType cellType = getCellTypeEnum();
        if (cellType != CellType.FORMULA) {
            throw typeMismatch(CellType.FORMULA, cellType, false);
        }
        CTCellFormula f = this._cell.getF();
        if (isPartOfArrayFormulaGroup() && f == null) {
            XSSFCell cell = getSheet().getFirstCellInArrayFormula(this);
            return cell.getCellFormula(fpb);
        }
        if (f.getT() == STCellFormulaType.SHARED) {
            return convertSharedFormula((int) f.getSi(), fpb == null ? XSSFEvaluationWorkbook.create(getSheet().getWorkbook()) : fpb);
        }
        return f.getStringValue();
    }

    private String convertSharedFormula(int si, XSSFEvaluationWorkbook fpb) {
        XSSFSheet sheet = getSheet();
        CTCellFormula f = sheet.getSharedFormula(si);
        if (f == null) {
            throw new IllegalStateException("Master cell of a shared formula with sid=" + si + " was not found");
        }
        String sharedFormula = f.getStringValue();
        String sharedFormulaRange = f.getRef();
        CellRangeAddress ref = CellRangeAddress.valueOf(sharedFormulaRange);
        int sheetIndex = sheet.getWorkbook().getSheetIndex(sheet);
        SharedFormula sf = new SharedFormula(SpreadsheetVersion.EXCEL2007);
        Ptg[] ptgs = FormulaParser.parse(sharedFormula, fpb, FormulaType.CELL, sheetIndex, getRowIndex());
        Ptg[] fmla = sf.convertSharedFormulas(ptgs, getRowIndex() - ref.getFirstRow(), getColumnIndex() - ref.getFirstColumn());
        return FormulaRenderer.toFormulaString(fpb, fmla);
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void setCellFormula(String formula) {
        if (isPartOfArrayFormulaGroup()) {
            notifyArrayFormulaChanging();
        }
        setFormula(formula, FormulaType.CELL);
    }

    void setCellArrayFormula(String formula, CellRangeAddress range) {
        setFormula(formula, FormulaType.ARRAY);
        CTCellFormula cellFormula = this._cell.getF();
        cellFormula.setT(STCellFormulaType.ARRAY);
        cellFormula.setRef(range.formatAsString());
    }

    private void setFormula(String formula, FormulaType formulaType) {
        XSSFWorkbook wb = this._row.getSheet().getWorkbook();
        if (formula == null) {
            wb.onDeleteFormula(this);
            if (this._cell.isSetF()) {
                this._cell.unsetF();
                return;
            }
            return;
        }
        XSSFEvaluationWorkbook fpb = XSSFEvaluationWorkbook.create(wb);
        FormulaParser.parse(formula, fpb, formulaType, wb.getSheetIndex(getSheet()), getRowIndex());
        CTCellFormula f = CTCellFormula.Factory.newInstance();
        f.setStringValue(formula);
        this._cell.setF(f);
        if (this._cell.isSetV()) {
            this._cell.unsetV();
        }
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public int getColumnIndex() {
        return this._cellNum;
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public int getRowIndex() {
        return this._row.getRowNum();
    }

    public String getReference() {
        String ref = this._cell.getR();
        if (ref == null) {
            return getAddress().formatAsString();
        }
        return ref;
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public CellAddress getAddress() {
        return new CellAddress(this);
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public XSSFCellStyle getCellStyle() {
        XSSFCellStyle style = null;
        if (this._stylesSource.getNumCellStyles() > 0) {
            long idx = this._cell.isSetS() ? this._cell.getS() : 0L;
            style = this._stylesSource.getStyleAt((int) idx);
        }
        return style;
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void setCellStyle(CellStyle style) {
        if (style == null) {
            if (this._cell.isSetS()) {
                this._cell.unsetS();
            }
        } else {
            XSSFCellStyle xStyle = (XSSFCellStyle) style;
            xStyle.verifyBelongsToStylesSource(this._stylesSource);
            long idx = this._stylesSource.putStyle(xStyle);
            this._cell.setS(idx);
        }
    }

    private boolean isFormulaCell() {
        if ((this._cell.isSetF() && this._cell.getF().getT() != STCellFormulaType.DATA_TABLE) || getSheet().isCellInArrayFormulaContext(this)) {
            return true;
        }
        return false;
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    @Removal(version = "3.17")
    @Deprecated
    public int getCellType() {
        return getCellTypeEnum().getCode();
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public CellType getCellTypeEnum() {
        if (isFormulaCell()) {
            return CellType.FORMULA;
        }
        return getBaseCellType(true);
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    @Removal(version = "3.17")
    @Deprecated
    public int getCachedFormulaResultType() {
        return getCachedFormulaResultTypeEnum().getCode();
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public CellType getCachedFormulaResultTypeEnum() {
        if (!isFormulaCell()) {
            throw new IllegalStateException("Only formula cells have cached results");
        }
        return getBaseCellType(false);
    }

    private CellType getBaseCellType(boolean blankCells) {
        switch (this._cell.getT().intValue()) {
            case 1:
                return CellType.BOOLEAN;
            case 2:
                if (!this._cell.isSetV() && blankCells) {
                    return CellType.BLANK;
                }
                return CellType.NUMERIC;
            case 3:
                return CellType.ERROR;
            case 4:
            case 5:
            case 6:
                return CellType.STRING;
            default:
                throw new IllegalStateException("Illegal cell type: " + this._cell.getT());
        }
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public Date getDateCellValue() {
        if (getCellTypeEnum() == CellType.BLANK) {
            return null;
        }
        double value = getNumericCellValue();
        boolean date1904 = getSheet().getWorkbook().isDate1904();
        return DateUtil.getJavaDate(value, date1904);
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void setCellValue(Date value) throws NumberFormatException {
        if (value == null) {
            setCellType(CellType.BLANK);
        } else {
            boolean date1904 = getSheet().getWorkbook().isDate1904();
            setCellValue(DateUtil.getExcelDate(value, date1904));
        }
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void setCellValue(Calendar value) throws NumberFormatException {
        if (value == null) {
            setCellType(CellType.BLANK);
        } else {
            boolean date1904 = getSheet().getWorkbook().isDate1904();
            setCellValue(DateUtil.getExcelDate(value, date1904));
        }
    }

    public String getErrorCellString() throws IllegalStateException {
        CellType cellType = getBaseCellType(true);
        if (cellType != CellType.ERROR) {
            throw typeMismatch(CellType.ERROR, cellType, false);
        }
        return this._cell.getV();
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public byte getErrorCellValue() throws IllegalStateException {
        String code = getErrorCellString();
        if (code == null) {
            return (byte) 0;
        }
        try {
            return FormulaError.forString(code).getCode();
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Unexpected error code", e);
        }
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void setCellErrorValue(byte errorCode) throws IllegalArgumentException {
        FormulaError error = FormulaError.forInt(errorCode);
        setCellErrorValue(error);
    }

    public void setCellErrorValue(FormulaError error) {
        this._cell.setT(STCellType.E);
        this._cell.setV(error.getString());
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void setAsActiveCell() {
        getSheet().setActiveCell(getAddress());
    }

    private void setBlank() {
        CTCell blank = CTCell.Factory.newInstance();
        blank.setR(this._cell.getR());
        if (this._cell.isSetS()) {
            blank.setS(this._cell.getS());
        }
        this._cell.set(blank);
    }

    protected void setCellNum(int num) {
        checkBounds(num);
        this._cellNum = num;
        String ref = new CellReference(getRowIndex(), getColumnIndex()).formatAsString();
        this._cell.setR(ref);
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    @Removal(version = "3.17")
    @Deprecated
    public void setCellType(int cellType) throws NumberFormatException {
        setCellType(CellType.forInt(cellType));
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void setCellType(CellType cellType) throws NumberFormatException {
        CellType prevType = getCellTypeEnum();
        if (isPartOfArrayFormulaGroup()) {
            notifyArrayFormulaChanging();
        }
        if (prevType == CellType.FORMULA && cellType != CellType.FORMULA) {
            getSheet().getWorkbook().onDeleteFormula(this);
        }
        switch (cellType) {
            case NUMERIC:
                this._cell.setT(STCellType.N);
                break;
            case STRING:
                if (prevType != CellType.STRING) {
                    String str = convertCellValueToString();
                    XSSFRichTextString rt = new XSSFRichTextString(str);
                    rt.setStylesTableReference(this._stylesSource);
                    int sRef = this._sharedStringSource.addEntry(rt.getCTRst());
                    this._cell.setV(Integer.toString(sRef));
                }
                this._cell.setT(STCellType.S);
                break;
            case FORMULA:
                if (!this._cell.isSetF()) {
                    CTCellFormula f = CTCellFormula.Factory.newInstance();
                    f.setStringValue("0");
                    this._cell.setF(f);
                    if (this._cell.isSetT()) {
                        this._cell.unsetT();
                        break;
                    }
                }
                break;
            case BLANK:
                setBlank();
                break;
            case BOOLEAN:
                String newVal = convertCellValueToBoolean() ? "1" : "0";
                this._cell.setT(STCellType.B);
                this._cell.setV(newVal);
                break;
            case ERROR:
                this._cell.setT(STCellType.E);
                break;
            default:
                throw new IllegalArgumentException("Illegal cell type: " + cellType);
        }
        if (cellType != CellType.FORMULA && this._cell.isSetF()) {
            this._cell.unsetF();
        }
    }

    public String toString() {
        switch (getCellTypeEnum()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(this)) {
                    DateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", LocaleUtil.getUserLocale());
                    sdf.setTimeZone(LocaleUtil.getUserTimeZone());
                    return sdf.format(getDateCellValue());
                }
                return Double.toString(getNumericCellValue());
            case STRING:
                return getRichStringCellValue().toString();
            case FORMULA:
                return getCellFormula();
            case BLANK:
                return "";
            case BOOLEAN:
                return getBooleanCellValue() ? TRUE : FALSE;
            case ERROR:
                return ErrorEval.getText(getErrorCellValue());
            default:
                return "Unknown Cell Type: " + getCellTypeEnum();
        }
    }

    public String getRawValue() {
        return this._cell.getV();
    }

    private static RuntimeException typeMismatch(CellType expectedType, CellType actualType, boolean isFormulaCell) {
        String msg = "Cannot get a " + expectedType + " value from a " + actualType + SymbolConstants.SPACE_SYMBOL + (isFormulaCell ? "formula " : "") + "cell";
        return new IllegalStateException(msg);
    }

    private static void checkBounds(int cellIndex) {
        SpreadsheetVersion v = SpreadsheetVersion.EXCEL2007;
        int maxcol = SpreadsheetVersion.EXCEL2007.getLastColumnIndex();
        if (cellIndex < 0 || cellIndex > maxcol) {
            throw new IllegalArgumentException("Invalid column index (" + cellIndex + ").  Allowable column range for " + v.name() + " is (0.." + maxcol + ") or ('A'..'" + v.getLastColumnName() + "')");
        }
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public XSSFComment getCellComment() {
        return getSheet().getCellComment(new CellAddress(this));
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void setCellComment(Comment comment) {
        if (comment == null) {
            removeCellComment();
        } else {
            comment.setAddress(getRowIndex(), getColumnIndex());
        }
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void removeCellComment() {
        XSSFComment comment = getCellComment();
        if (comment != null) {
            CellAddress ref = new CellAddress(getReference());
            XSSFSheet sh = getSheet();
            sh.getCommentsTable(false).removeComment(ref);
            sh.getVMLDrawing(false).removeCommentShape(getRowIndex(), getColumnIndex());
        }
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public XSSFHyperlink getHyperlink() {
        return getSheet().getHyperlink(this._row.getRowNum(), this._cellNum);
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void setHyperlink(Hyperlink hyperlink) {
        if (hyperlink == null) {
            removeHyperlink();
            return;
        }
        XSSFHyperlink link = (XSSFHyperlink) hyperlink;
        link.setCellReference(new CellReference(this._row.getRowNum(), this._cellNum).formatAsString());
        getSheet().addHyperlink(link);
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void removeHyperlink() {
        getSheet().removeHyperlink(this._row.getRowNum(), this._cellNum);
    }

    @Internal
    public CTCell getCTCell() {
        return this._cell;
    }

    @Internal
    public void setCTCell(CTCell cell) {
        this._cell = cell;
    }

    private boolean convertCellValueToBoolean() throws NumberFormatException {
        CellType cellType = getCellTypeEnum();
        if (cellType == CellType.FORMULA) {
            cellType = getBaseCellType(false);
        }
        switch (cellType) {
            case NUMERIC:
                return Double.parseDouble(this._cell.getV()) != 0.0d;
            case STRING:
                int sstIndex = Integer.parseInt(this._cell.getV());
                XSSFRichTextString rt = new XSSFRichTextString(this._sharedStringSource.getEntryAt(sstIndex));
                String text = rt.getString();
                return Boolean.parseBoolean(text);
            case FORMULA:
            default:
                throw new RuntimeException("Unexpected cell type (" + cellType + ")");
            case BLANK:
            case ERROR:
                return false;
            case BOOLEAN:
                return "1".equals(this._cell.getV());
        }
    }

    private String convertCellValueToString() throws NumberFormatException {
        CellType cellType = getCellTypeEnum();
        switch (cellType) {
            case NUMERIC:
            case ERROR:
                return this._cell.getV();
            case STRING:
                int sstIndex = Integer.parseInt(this._cell.getV());
                XSSFRichTextString rt = new XSSFRichTextString(this._sharedStringSource.getEntryAt(sstIndex));
                return rt.getString();
            case FORMULA:
                CellType cellType2 = getBaseCellType(false);
                String textValue = this._cell.getV();
                switch (cellType2) {
                    case NUMERIC:
                    case STRING:
                    case ERROR:
                        return textValue;
                    case FORMULA:
                    case BLANK:
                    default:
                        throw new IllegalStateException("Unexpected formula result type (" + cellType2 + ")");
                    case BOOLEAN:
                        if ("1".equals(textValue)) {
                            return TRUE;
                        }
                        if ("0".equals(textValue)) {
                            return FALSE;
                        }
                        throw new IllegalStateException("Unexpected boolean cached formula value '" + textValue + "'.");
                }
            case BLANK:
                return "";
            case BOOLEAN:
                return "1".equals(this._cell.getV()) ? TRUE : FALSE;
            default:
                throw new IllegalStateException("Unexpected cell type (" + cellType + ")");
        }
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public CellRangeAddress getArrayFormulaRange() {
        XSSFCell cell = getSheet().getFirstCellInArrayFormula(this);
        if (cell == null) {
            throw new IllegalStateException("Cell " + getReference() + " is not part of an array formula.");
        }
        String formulaRef = cell._cell.getF().getRef();
        return CellRangeAddress.valueOf(formulaRef);
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public boolean isPartOfArrayFormulaGroup() {
        return getSheet().isCellInArrayFormulaContext(this);
    }

    void notifyArrayFormulaChanging(String msg) {
        if (isPartOfArrayFormulaGroup()) {
            CellRangeAddress cra = getArrayFormulaRange();
            if (cra.getNumberOfCells() > 1) {
                throw new IllegalStateException(msg);
            }
            getRow().getSheet().removeArrayFormula(this);
        }
    }

    void notifyArrayFormulaChanging() {
        CellReference ref = new CellReference(this);
        String msg = "Cell " + ref.formatAsString() + " is part of a multi-cell array formula. You cannot change part of an array.";
        notifyArrayFormulaChanging(msg);
    }
}
