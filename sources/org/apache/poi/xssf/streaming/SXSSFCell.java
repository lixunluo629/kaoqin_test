package org.apache.poi.xssf.streaming;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.usermodel.Cell;
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
import org.apache.poi.util.LocaleUtil;
import org.apache.poi.util.NotImplemented;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/streaming/SXSSFCell.class */
public class SXSSFCell implements Cell {
    private static final POILogger logger = POILogFactory.getLogger((Class<?>) SXSSFCell.class);
    private final SXSSFRow _row;
    private Value _value;
    private CellStyle _style;
    private Property _firstProperty;

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/streaming/SXSSFCell$Value.class */
    interface Value {
        CellType getType();
    }

    public SXSSFCell(SXSSFRow row, CellType cellType) {
        this._row = row;
        setType(cellType);
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public int getColumnIndex() {
        return this._row.getCellIndex(this);
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public int getRowIndex() {
        return this._row.getRowNum();
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public CellAddress getAddress() {
        return new CellAddress(this);
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public SXSSFSheet getSheet() {
        return this._row.getSheet();
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public Row getRow() {
        return this._row;
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void setCellType(int cellType) {
        ensureType(CellType.forInt(cellType));
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void setCellType(CellType cellType) {
        ensureType(cellType);
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public int getCellType() {
        return getCellTypeEnum().getCode();
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public CellType getCellTypeEnum() {
        return this._value.getType();
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public int getCachedFormulaResultType() {
        return getCachedFormulaResultTypeEnum().getCode();
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public CellType getCachedFormulaResultTypeEnum() {
        if (this._value.getType() != CellType.FORMULA) {
            throw new IllegalStateException("Only formula cells have cached results");
        }
        return ((FormulaValue) this._value).getFormulaType();
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void setCellValue(double value) {
        if (Double.isInfinite(value)) {
            setCellErrorValue(FormulaError.DIV0.getCode());
            return;
        }
        if (Double.isNaN(value)) {
            setCellErrorValue(FormulaError.NUM.getCode());
            return;
        }
        ensureTypeOrFormulaType(CellType.NUMERIC);
        if (this._value.getType() == CellType.FORMULA) {
            ((NumericFormulaValue) this._value).setPreEvaluatedValue(value);
        } else {
            ((NumericValue) this._value).setValue(value);
        }
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void setCellValue(Date value) {
        if (value == null) {
            setCellType(CellType.BLANK);
        } else {
            boolean date1904 = getSheet().getWorkbook().isDate1904();
            setCellValue(DateUtil.getExcelDate(value, date1904));
        }
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void setCellValue(Calendar value) {
        if (value == null) {
            setCellType(CellType.BLANK);
        } else {
            boolean date1904 = getSheet().getWorkbook().isDate1904();
            setCellValue(DateUtil.getExcelDate(value, date1904));
        }
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void setCellValue(RichTextString value) {
        XSSFRichTextString xvalue = (XSSFRichTextString) value;
        if (xvalue != null && xvalue.getString() != null) {
            ensureRichTextStringType();
            if (xvalue.length() > SpreadsheetVersion.EXCEL2007.getMaxTextLength()) {
                throw new IllegalArgumentException("The maximum length of cell contents (text) is 32,767 characters");
            }
            if (xvalue.hasFormatting()) {
                logger.log(5, "SXSSF doesn't support Shared Strings, rich text formatting information has be lost");
            }
            ((RichTextValue) this._value).setValue(xvalue);
            return;
        }
        setCellType(CellType.BLANK);
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void setCellValue(String value) {
        if (value != null) {
            ensureTypeOrFormulaType(CellType.STRING);
            if (value.length() > SpreadsheetVersion.EXCEL2007.getMaxTextLength()) {
                throw new IllegalArgumentException("The maximum length of cell contents (text) is 32,767 characters");
            }
            if (this._value.getType() == CellType.FORMULA) {
                if (this._value instanceof NumericFormulaValue) {
                    ((NumericFormulaValue) this._value).setPreEvaluatedValue(Double.parseDouble(value));
                    return;
                } else {
                    ((StringFormulaValue) this._value).setPreEvaluatedValue(value);
                    return;
                }
            }
            ((PlainStringValue) this._value).setValue(value);
            return;
        }
        setCellType(CellType.BLANK);
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void setCellFormula(String formula) throws FormulaParseException {
        if (formula == null) {
            setType(CellType.BLANK);
        } else {
            ensureFormulaType(computeTypeFromFormula(formula));
            ((FormulaValue) this._value).setValue(formula);
        }
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public String getCellFormula() {
        if (this._value.getType() != CellType.FORMULA) {
            throw typeMismatch(CellType.FORMULA, this._value.getType(), false);
        }
        return ((FormulaValue) this._value).getValue();
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public double getNumericCellValue() {
        CellType cellType = getCellTypeEnum();
        switch (cellType) {
            case BLANK:
                return 0.0d;
            case FORMULA:
                FormulaValue fv = (FormulaValue) this._value;
                if (fv.getFormulaType() != CellType.NUMERIC) {
                    throw typeMismatch(CellType.NUMERIC, CellType.FORMULA, false);
                }
                return ((NumericFormulaValue) this._value).getPreEvaluatedValue();
            case NUMERIC:
                return ((NumericValue) this._value).getValue();
            default:
                throw typeMismatch(CellType.NUMERIC, cellType, false);
        }
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public Date getDateCellValue() {
        CellType cellType = getCellTypeEnum();
        if (cellType == CellType.BLANK) {
            return null;
        }
        double value = getNumericCellValue();
        boolean date1904 = getSheet().getWorkbook().isDate1904();
        return DateUtil.getJavaDate(value, date1904);
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public RichTextString getRichStringCellValue() {
        CellType cellType = getCellTypeEnum();
        if (getCellTypeEnum() != CellType.STRING) {
            throw typeMismatch(CellType.STRING, cellType, false);
        }
        StringValue sval = (StringValue) this._value;
        if (sval.isRichText()) {
            return ((RichTextValue) this._value).getValue();
        }
        String plainText = getStringCellValue();
        return getSheet().getWorkbook().getCreationHelper().createRichTextString(plainText);
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public String getStringCellValue() {
        CellType cellType = getCellTypeEnum();
        switch (cellType) {
            case BLANK:
                return "";
            case FORMULA:
                FormulaValue fv = (FormulaValue) this._value;
                if (fv.getFormulaType() != CellType.STRING) {
                    throw typeMismatch(CellType.STRING, CellType.FORMULA, false);
                }
                return ((StringFormulaValue) this._value).getPreEvaluatedValue();
            case NUMERIC:
            default:
                throw typeMismatch(CellType.STRING, cellType, false);
            case STRING:
                if (((StringValue) this._value).isRichText()) {
                    return ((RichTextValue) this._value).getValue().getString();
                }
                return ((PlainStringValue) this._value).getValue();
        }
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void setCellValue(boolean value) {
        ensureTypeOrFormulaType(CellType.BOOLEAN);
        if (this._value.getType() == CellType.FORMULA) {
            ((BooleanFormulaValue) this._value).setPreEvaluatedValue(value);
        } else {
            ((BooleanValue) this._value).setValue(value);
        }
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void setCellErrorValue(byte value) {
        ensureType(CellType.ERROR);
        if (this._value.getType() == CellType.FORMULA) {
            ((ErrorFormulaValue) this._value).setPreEvaluatedValue(value);
        } else {
            ((ErrorValue) this._value).setValue(value);
        }
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public boolean getBooleanCellValue() {
        CellType cellType = getCellTypeEnum();
        switch (cellType) {
            case BLANK:
                return false;
            case FORMULA:
                FormulaValue fv = (FormulaValue) this._value;
                if (fv.getFormulaType() != CellType.BOOLEAN) {
                    throw typeMismatch(CellType.BOOLEAN, CellType.FORMULA, false);
                }
                return ((BooleanFormulaValue) this._value).getPreEvaluatedValue();
            case NUMERIC:
            case STRING:
            default:
                throw typeMismatch(CellType.BOOLEAN, cellType, false);
            case BOOLEAN:
                return ((BooleanValue) this._value).getValue();
        }
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public byte getErrorCellValue() {
        CellType cellType = getCellTypeEnum();
        switch (cellType) {
            case BLANK:
                return (byte) 0;
            case FORMULA:
                FormulaValue fv = (FormulaValue) this._value;
                if (fv.getFormulaType() != CellType.ERROR) {
                    throw typeMismatch(CellType.ERROR, CellType.FORMULA, false);
                }
                return ((ErrorFormulaValue) this._value).getPreEvaluatedValue();
            case ERROR:
                return ((ErrorValue) this._value).getValue();
            default:
                throw typeMismatch(CellType.ERROR, cellType, false);
        }
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void setCellStyle(CellStyle style) {
        this._style = style;
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public CellStyle getCellStyle() {
        if (this._style == null) {
            SXSSFWorkbook wb = (SXSSFWorkbook) getRow().getSheet().getWorkbook();
            return wb.getCellStyleAt(0);
        }
        return this._style;
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void setAsActiveCell() {
        getSheet().setActiveCell(getAddress());
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void setCellComment(Comment comment) {
        setProperty(1, comment);
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public Comment getCellComment() {
        return (Comment) getPropertyValue(1);
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void removeCellComment() {
        removeProperty(1);
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public Hyperlink getHyperlink() {
        return (Hyperlink) getPropertyValue(2);
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void setHyperlink(Hyperlink link) {
        if (link == null) {
            removeHyperlink();
            return;
        }
        setProperty(2, link);
        XSSFHyperlink xssfobj = (XSSFHyperlink) link;
        CellReference ref = new CellReference(getRowIndex(), getColumnIndex());
        xssfobj.setCellReference(ref);
        getSheet()._sh.addHyperlink(xssfobj);
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    public void removeHyperlink() {
        removeProperty(2);
        getSheet()._sh.removeHyperlink(getRowIndex(), getColumnIndex());
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    @NotImplemented
    public CellRangeAddress getArrayFormulaRange() {
        return null;
    }

    @Override // org.apache.poi.ss.usermodel.Cell
    @NotImplemented
    public boolean isPartOfArrayFormulaGroup() {
        return false;
    }

    public String toString() {
        switch (getCellTypeEnum()) {
            case BLANK:
                return "";
            case FORMULA:
                return getCellFormula();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(this)) {
                    DateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", LocaleUtil.getUserLocale());
                    sdf.setTimeZone(LocaleUtil.getUserTimeZone());
                    return sdf.format(getDateCellValue());
                }
                return getNumericCellValue() + "";
            case STRING:
                return getRichStringCellValue().toString();
            case BOOLEAN:
                return getBooleanCellValue() ? "TRUE" : "FALSE";
            case ERROR:
                return ErrorEval.getText(getErrorCellValue());
            default:
                return "Unknown Cell Type: " + getCellTypeEnum();
        }
    }

    void removeProperty(int type) {
        Property current = this._firstProperty;
        Property previous = null;
        while (current != null && current.getType() != type) {
            previous = current;
            current = current._next;
        }
        if (current != null) {
            if (previous != null) {
                previous._next = current._next;
            } else {
                this._firstProperty = current._next;
            }
        }
    }

    void setProperty(int type, Object value) {
        Property current;
        Property current2 = this._firstProperty;
        Property previous = null;
        while (current2 != null && current2.getType() != type) {
            previous = current2;
            current2 = current2._next;
        }
        if (current2 != null) {
            current2.setValue(value);
            return;
        }
        switch (type) {
            case 1:
                current = new CommentProperty(value);
                break;
            case 2:
                current = new HyperlinkProperty(value);
                break;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
        if (previous != null) {
            previous._next = current;
        } else {
            this._firstProperty = current;
        }
    }

    Object getPropertyValue(int type) {
        return getPropertyValue(type, null);
    }

    Object getPropertyValue(int type, String defaultValue) {
        Property current;
        Property property = this._firstProperty;
        while (true) {
            current = property;
            if (current == null || current.getType() == type) {
                break;
            }
            property = current._next;
        }
        return current == null ? defaultValue : current.getValue();
    }

    void ensurePlainStringType() {
        if (this._value.getType() != CellType.STRING || ((StringValue) this._value).isRichText()) {
            this._value = new PlainStringValue();
        }
    }

    void ensureRichTextStringType() {
        if (this._value.getType() != CellType.STRING || !((StringValue) this._value).isRichText()) {
            this._value = new RichTextValue();
        }
    }

    void ensureType(CellType type) {
        if (this._value.getType() != type) {
            setType(type);
        }
    }

    void ensureFormulaType(CellType type) {
        if (this._value.getType() != CellType.FORMULA || ((FormulaValue) this._value).getFormulaType() != type) {
            setFormulaType(type);
        }
    }

    void ensureTypeOrFormulaType(CellType type) {
        if (this._value.getType() == type) {
            if (type == CellType.STRING && ((StringValue) this._value).isRichText()) {
                setType(CellType.STRING);
                return;
            }
            return;
        }
        if (this._value.getType() == CellType.FORMULA) {
            if (((FormulaValue) this._value).getFormulaType() == type) {
                return;
            }
            setFormulaType(type);
            return;
        }
        setType(type);
    }

    void setType(CellType type) {
        switch (type) {
            case BLANK:
                this._value = new BlankValue();
                return;
            case FORMULA:
                this._value = new NumericFormulaValue();
                return;
            case NUMERIC:
                this._value = new NumericValue();
                return;
            case STRING:
                PlainStringValue sval = new PlainStringValue();
                if (this._value != null) {
                    String str = convertCellValueToString();
                    sval.setValue(str);
                }
                this._value = sval;
                return;
            case BOOLEAN:
                BooleanValue bval = new BooleanValue();
                if (this._value != null) {
                    boolean val = convertCellValueToBoolean();
                    bval.setValue(val);
                }
                this._value = bval;
                return;
            case ERROR:
                this._value = new ErrorValue();
                return;
            default:
                throw new IllegalArgumentException("Illegal type " + type);
        }
    }

    void setFormulaType(CellType type) {
        Value prevValue = this._value;
        switch (type) {
            case NUMERIC:
                this._value = new NumericFormulaValue();
                break;
            case STRING:
                this._value = new StringFormulaValue();
                break;
            case BOOLEAN:
                this._value = new BooleanFormulaValue();
                break;
            case ERROR:
                this._value = new ErrorFormulaValue();
                break;
            default:
                throw new IllegalArgumentException("Illegal type " + type);
        }
        if (prevValue instanceof FormulaValue) {
            ((FormulaValue) this._value)._value = ((FormulaValue) prevValue)._value;
        }
    }

    @NotImplemented
    CellType computeTypeFromFormula(String formula) {
        return CellType.NUMERIC;
    }

    private static RuntimeException typeMismatch(CellType expectedTypeCode, CellType actualTypeCode, boolean isFormulaCell) {
        String msg = "Cannot get a " + expectedTypeCode + " value from a " + actualTypeCode + SymbolConstants.SPACE_SYMBOL + (isFormulaCell ? "formula " : "") + "cell";
        return new IllegalStateException(msg);
    }

    private boolean convertCellValueToBoolean() {
        CellType cellType = getCellTypeEnum();
        if (cellType == CellType.FORMULA) {
            cellType = getCachedFormulaResultTypeEnum();
        }
        switch (cellType) {
            case BLANK:
            case ERROR:
                return false;
            case FORMULA:
            default:
                throw new RuntimeException("Unexpected cell type (" + cellType + ")");
            case NUMERIC:
                return getNumericCellValue() != 0.0d;
            case STRING:
                String text = getStringCellValue();
                return Boolean.parseBoolean(text);
            case BOOLEAN:
                return getBooleanCellValue();
        }
    }

    private String convertCellValueToString() {
        CellType cellType = getCellTypeEnum();
        return convertCellValueToString(cellType);
    }

    private String convertCellValueToString(CellType cellType) {
        switch (cellType) {
            case BLANK:
                return "";
            case FORMULA:
                if (this._value != null) {
                    FormulaValue fv = (FormulaValue) this._value;
                    if (fv.getFormulaType() != CellType.FORMULA) {
                        return convertCellValueToString(fv.getFormulaType());
                    }
                    return "";
                }
                return "";
            case NUMERIC:
                return Double.toString(getNumericCellValue());
            case STRING:
                return getStringCellValue();
            case BOOLEAN:
                return getBooleanCellValue() ? "TRUE" : "FALSE";
            case ERROR:
                byte errVal = getErrorCellValue();
                return FormulaError.forInt(errVal).getString();
            default:
                throw new IllegalStateException("Unexpected cell type (" + cellType + ")");
        }
    }

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/streaming/SXSSFCell$Property.class */
    static abstract class Property {
        static final int COMMENT = 1;
        static final int HYPERLINK = 2;
        Object _value;
        Property _next;

        abstract int getType();

        public Property(Object value) {
            this._value = value;
        }

        void setValue(Object value) {
            this._value = value;
        }

        Object getValue() {
            return this._value;
        }
    }

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/streaming/SXSSFCell$CommentProperty.class */
    static class CommentProperty extends Property {
        public CommentProperty(Object value) {
            super(value);
        }

        @Override // org.apache.poi.xssf.streaming.SXSSFCell.Property
        public int getType() {
            return 1;
        }
    }

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/streaming/SXSSFCell$HyperlinkProperty.class */
    static class HyperlinkProperty extends Property {
        public HyperlinkProperty(Object value) {
            super(value);
        }

        @Override // org.apache.poi.xssf.streaming.SXSSFCell.Property
        public int getType() {
            return 2;
        }
    }

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/streaming/SXSSFCell$NumericValue.class */
    static class NumericValue implements Value {
        double _value;

        NumericValue() {
        }

        @Override // org.apache.poi.xssf.streaming.SXSSFCell.Value
        public CellType getType() {
            return CellType.NUMERIC;
        }

        void setValue(double value) {
            this._value = value;
        }

        double getValue() {
            return this._value;
        }
    }

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/streaming/SXSSFCell$StringValue.class */
    static abstract class StringValue implements Value {
        abstract boolean isRichText();

        StringValue() {
        }

        @Override // org.apache.poi.xssf.streaming.SXSSFCell.Value
        public CellType getType() {
            return CellType.STRING;
        }
    }

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/streaming/SXSSFCell$PlainStringValue.class */
    static class PlainStringValue extends StringValue {
        String _value;

        PlainStringValue() {
        }

        void setValue(String value) {
            this._value = value;
        }

        String getValue() {
            return this._value;
        }

        @Override // org.apache.poi.xssf.streaming.SXSSFCell.StringValue
        boolean isRichText() {
            return false;
        }
    }

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/streaming/SXSSFCell$RichTextValue.class */
    static class RichTextValue extends StringValue {
        RichTextString _value;

        RichTextValue() {
        }

        @Override // org.apache.poi.xssf.streaming.SXSSFCell.StringValue, org.apache.poi.xssf.streaming.SXSSFCell.Value
        public CellType getType() {
            return CellType.STRING;
        }

        void setValue(RichTextString value) {
            this._value = value;
        }

        RichTextString getValue() {
            return this._value;
        }

        @Override // org.apache.poi.xssf.streaming.SXSSFCell.StringValue
        boolean isRichText() {
            return true;
        }
    }

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/streaming/SXSSFCell$FormulaValue.class */
    static abstract class FormulaValue implements Value {
        String _value;

        abstract CellType getFormulaType();

        FormulaValue() {
        }

        @Override // org.apache.poi.xssf.streaming.SXSSFCell.Value
        public CellType getType() {
            return CellType.FORMULA;
        }

        void setValue(String value) {
            this._value = value;
        }

        String getValue() {
            return this._value;
        }
    }

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/streaming/SXSSFCell$NumericFormulaValue.class */
    static class NumericFormulaValue extends FormulaValue {
        double _preEvaluatedValue;

        NumericFormulaValue() {
        }

        @Override // org.apache.poi.xssf.streaming.SXSSFCell.FormulaValue
        CellType getFormulaType() {
            return CellType.NUMERIC;
        }

        void setPreEvaluatedValue(double value) {
            this._preEvaluatedValue = value;
        }

        double getPreEvaluatedValue() {
            return this._preEvaluatedValue;
        }
    }

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/streaming/SXSSFCell$StringFormulaValue.class */
    static class StringFormulaValue extends FormulaValue {
        String _preEvaluatedValue;

        StringFormulaValue() {
        }

        @Override // org.apache.poi.xssf.streaming.SXSSFCell.FormulaValue
        CellType getFormulaType() {
            return CellType.STRING;
        }

        void setPreEvaluatedValue(String value) {
            this._preEvaluatedValue = value;
        }

        String getPreEvaluatedValue() {
            return this._preEvaluatedValue;
        }
    }

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/streaming/SXSSFCell$BooleanFormulaValue.class */
    static class BooleanFormulaValue extends FormulaValue {
        boolean _preEvaluatedValue;

        BooleanFormulaValue() {
        }

        @Override // org.apache.poi.xssf.streaming.SXSSFCell.FormulaValue
        CellType getFormulaType() {
            return CellType.BOOLEAN;
        }

        void setPreEvaluatedValue(boolean value) {
            this._preEvaluatedValue = value;
        }

        boolean getPreEvaluatedValue() {
            return this._preEvaluatedValue;
        }
    }

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/streaming/SXSSFCell$ErrorFormulaValue.class */
    static class ErrorFormulaValue extends FormulaValue {
        byte _preEvaluatedValue;

        ErrorFormulaValue() {
        }

        @Override // org.apache.poi.xssf.streaming.SXSSFCell.FormulaValue
        CellType getFormulaType() {
            return CellType.ERROR;
        }

        void setPreEvaluatedValue(byte value) {
            this._preEvaluatedValue = value;
        }

        byte getPreEvaluatedValue() {
            return this._preEvaluatedValue;
        }
    }

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/streaming/SXSSFCell$BlankValue.class */
    static class BlankValue implements Value {
        BlankValue() {
        }

        @Override // org.apache.poi.xssf.streaming.SXSSFCell.Value
        public CellType getType() {
            return CellType.BLANK;
        }
    }

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/streaming/SXSSFCell$BooleanValue.class */
    static class BooleanValue implements Value {
        boolean _value;

        BooleanValue() {
        }

        @Override // org.apache.poi.xssf.streaming.SXSSFCell.Value
        public CellType getType() {
            return CellType.BOOLEAN;
        }

        void setValue(boolean value) {
            this._value = value;
        }

        boolean getValue() {
            return this._value;
        }
    }

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/streaming/SXSSFCell$ErrorValue.class */
    static class ErrorValue implements Value {
        byte _value;

        ErrorValue() {
        }

        @Override // org.apache.poi.xssf.streaming.SXSSFCell.Value
        public CellType getType() {
            return CellType.ERROR;
        }

        void setValue(byte value) {
            this._value = value;
        }

        byte getValue() {
            return this._value;
        }
    }
}
