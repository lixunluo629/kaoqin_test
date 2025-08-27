package com.alibaba.excel.analysis.v07;

import com.alibaba.excel.context.AnalysisContext;
import java.util.List;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/analysis/v07/XlsxRowHandler.class */
public class XlsxRowHandler extends DefaultHandler {
    private List<XlsxCellHandler> cellHandlers;
    private XlsxRowResultHolder rowResultHolder;

    public XlsxRowHandler(AnalysisContext analysisContext, StylesTable stylesTable) {
        this.cellHandlers = XlsxHandlerFactory.buildCellHandlers(analysisContext, stylesTable);
        for (XlsxCellHandler cellHandler : this.cellHandlers) {
            if (cellHandler instanceof XlsxRowResultHolder) {
                this.rowResultHolder = (XlsxRowResultHolder) cellHandler;
                return;
            }
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        for (XlsxCellHandler cellHandler : this.cellHandlers) {
            if (cellHandler.support(name)) {
                cellHandler.startHandle(name, attributes);
            }
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String uri, String localName, String name) throws SAXException {
        for (XlsxCellHandler cellHandler : this.cellHandlers) {
            if (cellHandler.support(name)) {
                cellHandler.endHandle(name);
            }
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] ch2, int start, int length) throws SAXException {
        if (this.rowResultHolder != null) {
            this.rowResultHolder.appendCurrentCellValue(ch2, start, length);
        }
    }
}
