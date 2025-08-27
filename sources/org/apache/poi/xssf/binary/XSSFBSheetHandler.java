package org.apache.poi.xssf.binary;

import java.io.InputStream;
import java.util.Queue;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.util.Internal;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

@Internal
/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/binary/XSSFBSheetHandler.class */
public class XSSFBSheetHandler extends XSSFBParser {
    private static final int CHECK_ALL_ROWS = -1;
    private final XSSFBSharedStringsTable stringsTable;
    private final XSSFSheetXMLHandler.SheetContentsHandler handler;
    private final XSSFBStylesTable styles;
    private final XSSFBCommentsTable comments;
    private final DataFormatter dataFormatter;
    private final boolean formulasNotResults;
    private int lastEndedRow;
    private int lastStartedRow;
    private int currentRow;
    private byte[] rkBuffer;
    private XSSFBCellRange hyperlinkCellRange;
    private StringBuilder xlWideStringBuffer;
    private final XSSFBCellHeader cellBuffer;

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/binary/XSSFBSheetHandler$SheetContentsHandler.class */
    public interface SheetContentsHandler extends XSSFSheetXMLHandler.SheetContentsHandler {
        void hyperlinkCell(String str, String str2, String str3, String str4, XSSFComment xSSFComment);
    }

    public XSSFBSheetHandler(InputStream is, XSSFBStylesTable styles, XSSFBCommentsTable comments, XSSFBSharedStringsTable strings, XSSFSheetXMLHandler.SheetContentsHandler sheetContentsHandler, DataFormatter dataFormatter, boolean formulasNotResults) {
        super(is);
        this.lastEndedRow = -1;
        this.lastStartedRow = -1;
        this.currentRow = 0;
        this.rkBuffer = new byte[8];
        this.hyperlinkCellRange = null;
        this.xlWideStringBuffer = new StringBuilder();
        this.cellBuffer = new XSSFBCellHeader();
        this.styles = styles;
        this.comments = comments;
        this.stringsTable = strings;
        this.handler = sheetContentsHandler;
        this.dataFormatter = dataFormatter;
        this.formulasNotResults = formulasNotResults;
    }

    @Override // org.apache.poi.xssf.binary.XSSFBParser
    public void handleRecord(int id, byte[] data) throws XSSFBParseException {
        XSSFBRecordType type = XSSFBRecordType.lookup(id);
        switch (type) {
            case BrtRowHdr:
                int rw = XSSFBUtils.castToInt(LittleEndian.getUInt(data, 0));
                if (rw > 1048576) {
                    throw new XSSFBParseException("Row number beyond allowable range: " + rw);
                }
                this.currentRow = rw;
                checkMissedComments(this.currentRow);
                startRow(this.currentRow);
                return;
            case BrtCellIsst:
                handleBrtCellIsst(data);
                return;
            case BrtCellSt:
                handleCellSt(data);
                return;
            case BrtCellRk:
                handleCellRk(data);
                return;
            case BrtCellReal:
                handleCellReal(data);
                return;
            case BrtCellBool:
                handleBoolean(data);
                return;
            case BrtCellError:
                handleCellError(data);
                return;
            case BrtCellBlank:
                beforeCellValue(data);
                return;
            case BrtFmlaString:
                handleFmlaString(data);
                return;
            case BrtFmlaNum:
                handleFmlaNum(data);
                return;
            case BrtFmlaError:
                handleFmlaError(data);
                return;
            case BrtEndSheetData:
                checkMissedComments(-1);
                endRow(this.lastStartedRow);
                return;
            case BrtBeginHeaderFooter:
                handleHeaderFooter(data);
                return;
            default:
                return;
        }
    }

    private void beforeCellValue(byte[] data) {
        XSSFBCellHeader.parse(data, 0, this.currentRow, this.cellBuffer);
        checkMissedComments(this.currentRow, this.cellBuffer.getColNum());
    }

    private void handleCellValue(String formattedValue) {
        CellAddress cellAddress = new CellAddress(this.currentRow, this.cellBuffer.getColNum());
        XSSFBComment comment = null;
        if (this.comments != null) {
            comment = this.comments.get(cellAddress);
        }
        this.handler.cell(cellAddress.formatAsString(), formattedValue, comment);
    }

    private void handleFmlaNum(byte[] data) {
        beforeCellValue(data);
        double val = LittleEndian.getDouble(data, XSSFBCellHeader.length);
        handleCellValue(formatVal(val, this.cellBuffer.getStyleIdx()));
    }

    private void handleCellSt(byte[] data) throws XSSFBParseException {
        beforeCellValue(data);
        this.xlWideStringBuffer.setLength(0);
        XSSFBUtils.readXLWideString(data, XSSFBCellHeader.length, this.xlWideStringBuffer);
        handleCellValue(this.xlWideStringBuffer.toString());
    }

    private void handleFmlaString(byte[] data) throws XSSFBParseException {
        beforeCellValue(data);
        this.xlWideStringBuffer.setLength(0);
        XSSFBUtils.readXLWideString(data, XSSFBCellHeader.length, this.xlWideStringBuffer);
        handleCellValue(this.xlWideStringBuffer.toString());
    }

    private void handleCellError(byte[] data) {
        beforeCellValue(data);
        handleCellValue("ERROR");
    }

    private void handleFmlaError(byte[] data) {
        beforeCellValue(data);
        handleCellValue("ERROR");
    }

    private void handleBoolean(byte[] data) {
        beforeCellValue(data);
        String formattedVal = data[XSSFBCellHeader.length] == 1 ? "TRUE" : "FALSE";
        handleCellValue(formattedVal);
    }

    private void handleCellReal(byte[] data) {
        beforeCellValue(data);
        double val = LittleEndian.getDouble(data, XSSFBCellHeader.length);
        handleCellValue(formatVal(val, this.cellBuffer.getStyleIdx()));
    }

    private void handleCellRk(byte[] data) {
        beforeCellValue(data);
        double val = rkNumber(data, XSSFBCellHeader.length);
        handleCellValue(formatVal(val, this.cellBuffer.getStyleIdx()));
    }

    private String formatVal(double val, int styleIdx) {
        String formatString = this.styles.getNumberFormatString(styleIdx);
        short styleIndex = this.styles.getNumberFormatIndex(styleIdx);
        if (formatString == null) {
            formatString = BuiltinFormats.getBuiltinFormat(0);
            styleIndex = 0;
        }
        return this.dataFormatter.formatRawCellContents(val, styleIndex, formatString);
    }

    private void handleBrtCellIsst(byte[] data) {
        beforeCellValue(data);
        int idx = XSSFBUtils.castToInt(LittleEndian.getUInt(data, XSSFBCellHeader.length));
        XSSFRichTextString rtss = new XSSFRichTextString(this.stringsTable.getEntryAt(idx));
        handleCellValue(rtss.getString());
    }

    private void handleHeaderFooter(byte[] data) throws XSSFBParseException {
        XSSFBHeaderFooters headerFooter = XSSFBHeaderFooters.parse(data);
        outputHeaderFooter(headerFooter.getHeader());
        outputHeaderFooter(headerFooter.getFooter());
        outputHeaderFooter(headerFooter.getHeaderEven());
        outputHeaderFooter(headerFooter.getFooterEven());
        outputHeaderFooter(headerFooter.getHeaderFirst());
        outputHeaderFooter(headerFooter.getFooterFirst());
    }

    private void outputHeaderFooter(XSSFBHeaderFooter headerFooter) {
        String text = headerFooter.getString();
        if (text != null && text.trim().length() > 0) {
            this.handler.headerFooter(text, headerFooter.isHeader(), headerFooter.getHeaderFooterTypeLabel());
        }
    }

    private void checkMissedComments(int currentRow, int colNum) {
        if (this.comments == null) {
            return;
        }
        Queue<CellAddress> queue = this.comments.getAddresses();
        while (queue.size() > 0) {
            CellAddress cellAddress = queue.peek();
            if (cellAddress.getRow() == currentRow && cellAddress.getColumn() < colNum) {
                CellAddress cellAddress2 = queue.remove();
                dumpEmptyCellComment(cellAddress2, this.comments.get(cellAddress2));
            } else if (cellAddress.getRow() == currentRow && cellAddress.getColumn() == colNum) {
                queue.remove();
                return;
            } else if ((cellAddress.getRow() == currentRow && cellAddress.getColumn() > colNum) || cellAddress.getRow() > currentRow) {
                return;
            }
        }
    }

    private void checkMissedComments(int currentRow) {
        if (this.comments == null) {
            return;
        }
        Queue<CellAddress> queue = this.comments.getAddresses();
        int row = -1;
        while (true) {
            int lastInterpolatedRow = row;
            if (queue.size() > 0) {
                CellAddress cellAddress = queue.peek();
                if (currentRow == -1 || cellAddress.getRow() < currentRow) {
                    CellAddress cellAddress2 = queue.remove();
                    if (cellAddress2.getRow() != lastInterpolatedRow) {
                        startRow(cellAddress2.getRow());
                    }
                    dumpEmptyCellComment(cellAddress2, this.comments.get(cellAddress2));
                    row = cellAddress2.getRow();
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private void startRow(int row) {
        if (row == this.lastStartedRow) {
            return;
        }
        if (this.lastStartedRow != this.lastEndedRow) {
            endRow(this.lastStartedRow);
        }
        this.handler.startRow(row);
        this.lastStartedRow = row;
    }

    private void endRow(int row) {
        if (this.lastEndedRow == row) {
            return;
        }
        this.handler.endRow(row);
        this.lastEndedRow = row;
    }

    private void dumpEmptyCellComment(CellAddress cellAddress, XSSFBComment comment) {
        this.handler.cell(cellAddress.formatAsString(), null, comment);
    }

    private double rkNumber(byte[] data, int offset) {
        double d;
        byte b0 = data[offset];
        Integer.toString(b0, 2);
        boolean numDivBy100 = (b0 & 1) == 1;
        boolean floatingPoint = ((b0 >> 1) & 1) == 0;
        this.rkBuffer[4] = (byte) (((byte) (b0 & (-2))) & (-3));
        for (int i = 1; i < 4; i++) {
            this.rkBuffer[i + 4] = data[offset + i];
        }
        if (floatingPoint) {
            d = LittleEndian.getDouble(this.rkBuffer);
        } else {
            d = LittleEndian.getInt(this.rkBuffer);
        }
        return numDivBy100 ? d / 100.0d : d;
    }
}
