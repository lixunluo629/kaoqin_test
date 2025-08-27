package org.apache.poi.ss.usermodel;

import java.util.Calendar;
import java.util.Date;
import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.Removal;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/Cell.class */
public interface Cell {

    @Removal(version = "4.0")
    @Deprecated
    public static final int CELL_TYPE_NUMERIC = 0;

    @Removal(version = "4.0")
    @Deprecated
    public static final int CELL_TYPE_STRING = 1;

    @Removal(version = "4.0")
    @Deprecated
    public static final int CELL_TYPE_FORMULA = 2;

    @Removal(version = "4.0")
    @Deprecated
    public static final int CELL_TYPE_BLANK = 3;

    @Removal(version = "4.0")
    @Deprecated
    public static final int CELL_TYPE_BOOLEAN = 4;

    @Removal(version = "4.0")
    @Deprecated
    public static final int CELL_TYPE_ERROR = 5;

    int getColumnIndex();

    int getRowIndex();

    Sheet getSheet();

    Row getRow();

    @Removal(version = "4.0")
    @Deprecated
    void setCellType(int i);

    void setCellType(CellType cellType);

    @Deprecated
    int getCellType();

    @Removal(version = "4.2")
    CellType getCellTypeEnum();

    @Deprecated
    int getCachedFormulaResultType();

    CellType getCachedFormulaResultTypeEnum();

    void setCellValue(double d);

    void setCellValue(Date date);

    void setCellValue(Calendar calendar);

    void setCellValue(RichTextString richTextString);

    void setCellValue(String str);

    void setCellFormula(String str) throws FormulaParseException;

    String getCellFormula();

    double getNumericCellValue();

    Date getDateCellValue();

    RichTextString getRichStringCellValue();

    String getStringCellValue();

    void setCellValue(boolean z);

    void setCellErrorValue(byte b);

    boolean getBooleanCellValue();

    byte getErrorCellValue();

    void setCellStyle(CellStyle cellStyle);

    CellStyle getCellStyle();

    void setAsActiveCell();

    CellAddress getAddress();

    void setCellComment(Comment comment);

    Comment getCellComment();

    void removeCellComment();

    Hyperlink getHyperlink();

    void setHyperlink(Hyperlink hyperlink);

    void removeHyperlink();

    CellRangeAddress getArrayFormulaRange();

    boolean isPartOfArrayFormulaGroup();
}
