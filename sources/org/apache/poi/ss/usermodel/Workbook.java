package org.apache.poi.ss.usermodel;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.Removal;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/Workbook.class */
public interface Workbook extends Closeable, Iterable<Sheet> {
    public static final int PICTURE_TYPE_EMF = 2;
    public static final int PICTURE_TYPE_WMF = 3;
    public static final int PICTURE_TYPE_PICT = 4;
    public static final int PICTURE_TYPE_JPEG = 5;
    public static final int PICTURE_TYPE_PNG = 6;
    public static final int PICTURE_TYPE_DIB = 7;

    @Removal(version = "3.18")
    @Deprecated
    public static final int SHEET_STATE_VISIBLE = 0;

    @Removal(version = "3.18")
    @Deprecated
    public static final int SHEET_STATE_HIDDEN = 1;

    @Removal(version = "3.18")
    @Deprecated
    public static final int SHEET_STATE_VERY_HIDDEN = 2;

    int getActiveSheetIndex();

    void setActiveSheet(int i);

    int getFirstVisibleTab();

    void setFirstVisibleTab(int i);

    void setSheetOrder(String str, int i);

    void setSelectedTab(int i);

    void setSheetName(int i, String str);

    String getSheetName(int i);

    int getSheetIndex(String str);

    int getSheetIndex(Sheet sheet);

    Sheet createSheet();

    Sheet createSheet(String str);

    Sheet cloneSheet(int i);

    Iterator<Sheet> sheetIterator();

    int getNumberOfSheets();

    Sheet getSheetAt(int i);

    Sheet getSheet(String str);

    void removeSheetAt(int i);

    Font createFont();

    Font findFont(boolean z, short s, short s2, String str, boolean z2, boolean z3, short s3, byte b);

    short getNumberOfFonts();

    Font getFontAt(short s);

    CellStyle createCellStyle();

    int getNumCellStyles();

    CellStyle getCellStyleAt(int i);

    void write(OutputStream outputStream) throws IOException;

    @Override // java.io.Closeable, java.lang.AutoCloseable
    void close() throws IOException;

    int getNumberOfNames();

    Name getName(String str);

    List<? extends Name> getNames(String str);

    List<? extends Name> getAllNames();

    Name getNameAt(int i);

    Name createName();

    int getNameIndex(String str);

    void removeName(int i);

    void removeName(String str);

    void removeName(Name name);

    int linkExternalWorkbook(String str, Workbook workbook);

    void setPrintArea(int i, String str);

    void setPrintArea(int i, int i2, int i3, int i4, int i5);

    String getPrintArea(int i);

    void removePrintArea(int i);

    Row.MissingCellPolicy getMissingCellPolicy();

    void setMissingCellPolicy(Row.MissingCellPolicy missingCellPolicy);

    DataFormat createDataFormat();

    int addPicture(byte[] bArr, int i);

    List<? extends PictureData> getAllPictures();

    CreationHelper getCreationHelper();

    boolean isHidden();

    void setHidden(boolean z);

    boolean isSheetHidden(int i);

    boolean isSheetVeryHidden(int i);

    void setSheetHidden(int i, boolean z);

    @Removal(version = "3.18")
    void setSheetHidden(int i, int i2);

    SheetVisibility getSheetVisibility(int i);

    void setSheetVisibility(int i, SheetVisibility sheetVisibility);

    void addToolPack(UDFFinder uDFFinder);

    void setForceFormulaRecalculation(boolean z);

    boolean getForceFormulaRecalculation();

    SpreadsheetVersion getSpreadsheetVersion();

    int addOlePackage(byte[] bArr, String str, String str2, String str3) throws IOException;
}
