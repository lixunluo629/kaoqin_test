package org.apache.poi.xssf.eventusermodel;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.alibaba.excel.constant.ExcelXmlConstants;
import java.util.LinkedList;
import java.util.Queue;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.xssf.model.CommentsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.aspectj.weaver.tools.cache.SimpleCache;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComment;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/eventusermodel/XSSFSheetXMLHandler.class */
public class XSSFSheetXMLHandler extends DefaultHandler {
    private static final POILogger logger = POILogFactory.getLogger((Class<?>) XSSFSheetXMLHandler.class);
    private StylesTable stylesTable;
    private CommentsTable commentsTable;
    private ReadOnlySharedStringsTable sharedStringsTable;
    private final SheetContentsHandler output;
    private boolean vIsOpen;
    private boolean fIsOpen;
    private boolean isIsOpen;
    private boolean hfIsOpen;
    private xssfDataType nextDataType;
    private short formatIndex;
    private String formatString;
    private final DataFormatter formatter;
    private int rowNum;
    private int nextRowNum;
    private String cellRef;
    private boolean formulasNotResults;
    private StringBuffer value;
    private StringBuffer formula;
    private StringBuffer headerFooter;
    private Queue<CellAddress> commentCellRefs;

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/eventusermodel/XSSFSheetXMLHandler$EmptyCellCommentsCheckType.class */
    private enum EmptyCellCommentsCheckType {
        CELL,
        END_OF_ROW,
        END_OF_SHEET_DATA
    }

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/eventusermodel/XSSFSheetXMLHandler$SheetContentsHandler.class */
    public interface SheetContentsHandler {
        void startRow(int i);

        void endRow(int i);

        void cell(String str, String str2, XSSFComment xSSFComment);

        void headerFooter(String str, boolean z, String str2);
    }

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/eventusermodel/XSSFSheetXMLHandler$xssfDataType.class */
    enum xssfDataType {
        BOOLEAN,
        ERROR,
        FORMULA,
        INLINE_STRING,
        SST_STRING,
        NUMBER
    }

    public XSSFSheetXMLHandler(StylesTable styles, CommentsTable comments, ReadOnlySharedStringsTable strings, SheetContentsHandler sheetContentsHandler, DataFormatter dataFormatter, boolean formulasNotResults) {
        this.value = new StringBuffer();
        this.formula = new StringBuffer();
        this.headerFooter = new StringBuffer();
        this.stylesTable = styles;
        this.commentsTable = comments;
        this.sharedStringsTable = strings;
        this.output = sheetContentsHandler;
        this.formulasNotResults = formulasNotResults;
        this.nextDataType = xssfDataType.NUMBER;
        this.formatter = dataFormatter;
        init();
    }

    public XSSFSheetXMLHandler(StylesTable styles, ReadOnlySharedStringsTable strings, SheetContentsHandler sheetContentsHandler, DataFormatter dataFormatter, boolean formulasNotResults) {
        this(styles, null, strings, sheetContentsHandler, dataFormatter, formulasNotResults);
    }

    public XSSFSheetXMLHandler(StylesTable styles, ReadOnlySharedStringsTable strings, SheetContentsHandler sheetContentsHandler, boolean formulasNotResults) {
        this(styles, strings, sheetContentsHandler, new DataFormatter(), formulasNotResults);
    }

    private void init() {
        if (this.commentsTable != null) {
            this.commentCellRefs = new LinkedList();
            CTComment[] arr$ = this.commentsTable.getCTComments().getCommentList().getCommentArray();
            for (CTComment comment : arr$) {
                this.commentCellRefs.add(new CellAddress(comment.getRef()));
            }
        }
    }

    private boolean isTextTag(String name) {
        if (ExcelXmlConstants.CELL_VALUE_TAG.equals(name) || "inlineStr".equals(name)) {
            return true;
        }
        if ("t".equals(name) && this.isIsOpen) {
            return true;
        }
        return false;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException, NumberFormatException {
        if (uri != null && !uri.equals(XSSFRelation.NS_SPREADSHEETML)) {
            return;
        }
        if (isTextTag(localName)) {
            this.vIsOpen = true;
            this.value.setLength(0);
            return;
        }
        if (BeanUtil.PREFIX_GETTER_IS.equals(localName)) {
            this.isIsOpen = true;
            return;
        }
        if (ExcelXmlConstants.CELL_FORMULA_TAG.equals(localName)) {
            this.formula.setLength(0);
            if (this.nextDataType == xssfDataType.NUMBER) {
                this.nextDataType = xssfDataType.FORMULA;
            }
            String type = attributes.getValue("t");
            if (type != null && type.equals(SimpleCache.IMPL_NAME)) {
                String ref = attributes.getValue("ref");
                attributes.getValue("si");
                if (ref != null) {
                    this.fIsOpen = true;
                    return;
                } else {
                    if (this.formulasNotResults) {
                        logger.log(5, "shared formulas not yet supported!");
                        return;
                    }
                    return;
                }
            }
            this.fIsOpen = true;
            return;
        }
        if ("oddHeader".equals(localName) || "evenHeader".equals(localName) || "firstHeader".equals(localName) || "firstFooter".equals(localName) || "oddFooter".equals(localName) || "evenFooter".equals(localName)) {
            this.hfIsOpen = true;
            this.headerFooter.setLength(0);
            return;
        }
        if (ExcelXmlConstants.ROW_TAG.equals(localName)) {
            String rowNumStr = attributes.getValue(ExcelXmlConstants.POSITION);
            if (rowNumStr != null) {
                this.rowNum = Integer.parseInt(rowNumStr) - 1;
            } else {
                this.rowNum = this.nextRowNum;
            }
            this.output.startRow(this.rowNum);
            return;
        }
        if (ExcelXmlConstants.CELL_TAG.equals(localName)) {
            this.nextDataType = xssfDataType.NUMBER;
            this.formatIndex = (short) -1;
            this.formatString = null;
            this.cellRef = attributes.getValue(ExcelXmlConstants.POSITION);
            String cellType = attributes.getValue("t");
            String cellStyleStr = attributes.getValue(ExcelXmlConstants.CELL_DATA_FORMAT_TAG);
            if ("b".equals(cellType)) {
                this.nextDataType = xssfDataType.BOOLEAN;
                return;
            }
            if ("e".equals(cellType)) {
                this.nextDataType = xssfDataType.ERROR;
                return;
            }
            if ("inlineStr".equals(cellType)) {
                this.nextDataType = xssfDataType.INLINE_STRING;
                return;
            }
            if (ExcelXmlConstants.CELL_DATA_FORMAT_TAG.equals(cellType)) {
                this.nextDataType = xssfDataType.SST_STRING;
                return;
            }
            if ("str".equals(cellType)) {
                this.nextDataType = xssfDataType.FORMULA;
                return;
            }
            XSSFCellStyle style = null;
            if (this.stylesTable != null) {
                if (cellStyleStr != null) {
                    int styleIndex = Integer.parseInt(cellStyleStr);
                    style = this.stylesTable.getStyleAt(styleIndex);
                } else if (this.stylesTable.getNumCellStyles() > 0) {
                    style = this.stylesTable.getStyleAt(0);
                }
            }
            if (style != null) {
                this.formatIndex = style.getDataFormat();
                this.formatString = style.getDataFormatString();
                if (this.formatString == null) {
                    this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);
                }
            }
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String uri, String localName, String qName) throws SAXException, NumberFormatException {
        if (uri != null && !uri.equals(XSSFRelation.NS_SPREADSHEETML)) {
            return;
        }
        String thisStr = null;
        if (!isTextTag(localName)) {
            if (ExcelXmlConstants.CELL_FORMULA_TAG.equals(localName)) {
                this.fIsOpen = false;
                return;
            }
            if (BeanUtil.PREFIX_GETTER_IS.equals(localName)) {
                this.isIsOpen = false;
                return;
            }
            if (ExcelXmlConstants.ROW_TAG.equals(localName)) {
                checkForEmptyCellComments(EmptyCellCommentsCheckType.END_OF_ROW);
                this.output.endRow(this.rowNum);
                this.nextRowNum = this.rowNum + 1;
                return;
            }
            if ("sheetData".equals(localName)) {
                checkForEmptyCellComments(EmptyCellCommentsCheckType.END_OF_SHEET_DATA);
                return;
            }
            if ("oddHeader".equals(localName) || "evenHeader".equals(localName) || "firstHeader".equals(localName)) {
                this.hfIsOpen = false;
                this.output.headerFooter(this.headerFooter.toString(), true, localName);
                return;
            } else {
                if ("oddFooter".equals(localName) || "evenFooter".equals(localName) || "firstFooter".equals(localName)) {
                    this.hfIsOpen = false;
                    this.output.headerFooter(this.headerFooter.toString(), false, localName);
                    return;
                }
                return;
            }
        }
        this.vIsOpen = false;
        switch (this.nextDataType) {
            case BOOLEAN:
                char first = this.value.charAt(0);
                thisStr = first == '0' ? "FALSE" : "TRUE";
                break;
            case ERROR:
                thisStr = "ERROR:" + ((Object) this.value);
                break;
            case FORMULA:
                if (this.formulasNotResults) {
                    thisStr = this.formula.toString();
                    break;
                } else {
                    String fv = this.value.toString();
                    if (this.formatString != null) {
                        try {
                            double d = Double.parseDouble(fv);
                            thisStr = this.formatter.formatRawCellContents(d, this.formatIndex, this.formatString);
                            break;
                        } catch (NumberFormatException e) {
                            thisStr = fv;
                            break;
                        }
                    } else {
                        thisStr = fv;
                        break;
                    }
                }
            case INLINE_STRING:
                XSSFRichTextString rtsi = new XSSFRichTextString(this.value.toString());
                thisStr = rtsi.toString();
                break;
            case SST_STRING:
                String sstIndex = this.value.toString();
                try {
                    int idx = Integer.parseInt(sstIndex);
                    XSSFRichTextString rtss = new XSSFRichTextString(this.sharedStringsTable.getEntryAt(idx));
                    thisStr = rtss.toString();
                    break;
                } catch (NumberFormatException ex) {
                    logger.log(7, "Failed to parse SST index '" + sstIndex, ex);
                    break;
                }
            case NUMBER:
                String n = this.value.toString();
                if (this.formatString != null && n.length() > 0) {
                    thisStr = this.formatter.formatRawCellContents(Double.parseDouble(n), this.formatIndex, this.formatString);
                    break;
                } else {
                    thisStr = n;
                    break;
                }
                break;
            default:
                thisStr = "(TODO: Unexpected type: " + this.nextDataType + ")";
                break;
        }
        checkForEmptyCellComments(EmptyCellCommentsCheckType.CELL);
        XSSFComment comment = this.commentsTable != null ? this.commentsTable.findCellComment(new CellAddress(this.cellRef)) : null;
        this.output.cell(this.cellRef, thisStr, comment);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] ch2, int start, int length) throws SAXException {
        if (this.vIsOpen) {
            this.value.append(ch2, start, length);
        }
        if (this.fIsOpen) {
            this.formula.append(ch2, start, length);
        }
        if (this.hfIsOpen) {
            this.headerFooter.append(ch2, start, length);
        }
    }

    private void checkForEmptyCellComments(EmptyCellCommentsCheckType type) {
        CellAddress nextCommentCellRef;
        if (this.commentCellRefs != null && !this.commentCellRefs.isEmpty()) {
            if (type == EmptyCellCommentsCheckType.END_OF_SHEET_DATA) {
                while (!this.commentCellRefs.isEmpty()) {
                    outputEmptyCellComment(this.commentCellRefs.remove());
                }
                return;
            }
            if (this.cellRef == null) {
                if (type == EmptyCellCommentsCheckType.END_OF_ROW) {
                    while (!this.commentCellRefs.isEmpty() && this.commentCellRefs.peek().getRow() == this.rowNum) {
                        outputEmptyCellComment(this.commentCellRefs.remove());
                    }
                    return;
                }
                throw new IllegalStateException("Cell ref should be null only if there are only empty cells in the row; rowNum: " + this.rowNum);
            }
            do {
                CellAddress cellRef = new CellAddress(this.cellRef);
                CellAddress peekCellRef = this.commentCellRefs.peek();
                if (type == EmptyCellCommentsCheckType.CELL && cellRef.equals(peekCellRef)) {
                    this.commentCellRefs.remove();
                    return;
                }
                int comparison = peekCellRef.compareTo(cellRef);
                if (comparison > 0 && type == EmptyCellCommentsCheckType.END_OF_ROW && peekCellRef.getRow() <= this.rowNum) {
                    nextCommentCellRef = this.commentCellRefs.remove();
                    outputEmptyCellComment(nextCommentCellRef);
                } else if (comparison < 0 && type == EmptyCellCommentsCheckType.CELL && peekCellRef.getRow() <= this.rowNum) {
                    nextCommentCellRef = this.commentCellRefs.remove();
                    outputEmptyCellComment(nextCommentCellRef);
                } else {
                    nextCommentCellRef = null;
                }
                if (nextCommentCellRef == null) {
                    return;
                }
            } while (!this.commentCellRefs.isEmpty());
        }
    }

    private void outputEmptyCellComment(CellAddress cellRef) {
        XSSFComment comment = this.commentsTable.findCellComment(cellRef);
        this.output.cell(cellRef.formatAsString(), null, comment);
    }
}
