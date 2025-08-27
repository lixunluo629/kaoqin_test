package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTTableColumn.class */
public interface CTTableColumn extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTTableColumn.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("cttablecolumn08a3type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTTableColumn$Factory.class */
    public static final class Factory {
        public static CTTableColumn newInstance() {
            return (CTTableColumn) POIXMLTypeLoader.newInstance(CTTableColumn.type, null);
        }

        public static CTTableColumn newInstance(XmlOptions xmlOptions) {
            return (CTTableColumn) POIXMLTypeLoader.newInstance(CTTableColumn.type, xmlOptions);
        }

        public static CTTableColumn parse(String str) throws XmlException {
            return (CTTableColumn) POIXMLTypeLoader.parse(str, CTTableColumn.type, (XmlOptions) null);
        }

        public static CTTableColumn parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTTableColumn) POIXMLTypeLoader.parse(str, CTTableColumn.type, xmlOptions);
        }

        public static CTTableColumn parse(File file) throws XmlException, IOException {
            return (CTTableColumn) POIXMLTypeLoader.parse(file, CTTableColumn.type, (XmlOptions) null);
        }

        public static CTTableColumn parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableColumn) POIXMLTypeLoader.parse(file, CTTableColumn.type, xmlOptions);
        }

        public static CTTableColumn parse(URL url) throws XmlException, IOException {
            return (CTTableColumn) POIXMLTypeLoader.parse(url, CTTableColumn.type, (XmlOptions) null);
        }

        public static CTTableColumn parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableColumn) POIXMLTypeLoader.parse(url, CTTableColumn.type, xmlOptions);
        }

        public static CTTableColumn parse(InputStream inputStream) throws XmlException, IOException {
            return (CTTableColumn) POIXMLTypeLoader.parse(inputStream, CTTableColumn.type, (XmlOptions) null);
        }

        public static CTTableColumn parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableColumn) POIXMLTypeLoader.parse(inputStream, CTTableColumn.type, xmlOptions);
        }

        public static CTTableColumn parse(Reader reader) throws XmlException, IOException {
            return (CTTableColumn) POIXMLTypeLoader.parse(reader, CTTableColumn.type, (XmlOptions) null);
        }

        public static CTTableColumn parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTTableColumn) POIXMLTypeLoader.parse(reader, CTTableColumn.type, xmlOptions);
        }

        public static CTTableColumn parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTTableColumn) POIXMLTypeLoader.parse(xMLStreamReader, CTTableColumn.type, (XmlOptions) null);
        }

        public static CTTableColumn parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTTableColumn) POIXMLTypeLoader.parse(xMLStreamReader, CTTableColumn.type, xmlOptions);
        }

        public static CTTableColumn parse(Node node) throws XmlException {
            return (CTTableColumn) POIXMLTypeLoader.parse(node, CTTableColumn.type, (XmlOptions) null);
        }

        public static CTTableColumn parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTTableColumn) POIXMLTypeLoader.parse(node, CTTableColumn.type, xmlOptions);
        }

        public static CTTableColumn parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTTableColumn) POIXMLTypeLoader.parse(xMLInputStream, CTTableColumn.type, (XmlOptions) null);
        }

        public static CTTableColumn parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTTableColumn) POIXMLTypeLoader.parse(xMLInputStream, CTTableColumn.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTableColumn.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTTableColumn.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTTableFormula getCalculatedColumnFormula();

    boolean isSetCalculatedColumnFormula();

    void setCalculatedColumnFormula(CTTableFormula cTTableFormula);

    CTTableFormula addNewCalculatedColumnFormula();

    void unsetCalculatedColumnFormula();

    CTTableFormula getTotalsRowFormula();

    boolean isSetTotalsRowFormula();

    void setTotalsRowFormula(CTTableFormula cTTableFormula);

    CTTableFormula addNewTotalsRowFormula();

    void unsetTotalsRowFormula();

    CTXmlColumnPr getXmlColumnPr();

    boolean isSetXmlColumnPr();

    void setXmlColumnPr(CTXmlColumnPr cTXmlColumnPr);

    CTXmlColumnPr addNewXmlColumnPr();

    void unsetXmlColumnPr();

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();

    long getId();

    XmlUnsignedInt xgetId();

    void setId(long j);

    void xsetId(XmlUnsignedInt xmlUnsignedInt);

    String getUniqueName();

    STXstring xgetUniqueName();

    boolean isSetUniqueName();

    void setUniqueName(String str);

    void xsetUniqueName(STXstring sTXstring);

    void unsetUniqueName();

    String getName();

    STXstring xgetName();

    void setName(String str);

    void xsetName(STXstring sTXstring);

    STTotalsRowFunction$Enum getTotalsRowFunction();

    STTotalsRowFunction xgetTotalsRowFunction();

    boolean isSetTotalsRowFunction();

    void setTotalsRowFunction(STTotalsRowFunction$Enum sTTotalsRowFunction$Enum);

    void xsetTotalsRowFunction(STTotalsRowFunction sTTotalsRowFunction);

    void unsetTotalsRowFunction();

    String getTotalsRowLabel();

    STXstring xgetTotalsRowLabel();

    boolean isSetTotalsRowLabel();

    void setTotalsRowLabel(String str);

    void xsetTotalsRowLabel(STXstring sTXstring);

    void unsetTotalsRowLabel();

    long getQueryTableFieldId();

    XmlUnsignedInt xgetQueryTableFieldId();

    boolean isSetQueryTableFieldId();

    void setQueryTableFieldId(long j);

    void xsetQueryTableFieldId(XmlUnsignedInt xmlUnsignedInt);

    void unsetQueryTableFieldId();

    long getHeaderRowDxfId();

    STDxfId xgetHeaderRowDxfId();

    boolean isSetHeaderRowDxfId();

    void setHeaderRowDxfId(long j);

    void xsetHeaderRowDxfId(STDxfId sTDxfId);

    void unsetHeaderRowDxfId();

    long getDataDxfId();

    STDxfId xgetDataDxfId();

    boolean isSetDataDxfId();

    void setDataDxfId(long j);

    void xsetDataDxfId(STDxfId sTDxfId);

    void unsetDataDxfId();

    long getTotalsRowDxfId();

    STDxfId xgetTotalsRowDxfId();

    boolean isSetTotalsRowDxfId();

    void setTotalsRowDxfId(long j);

    void xsetTotalsRowDxfId(STDxfId sTDxfId);

    void unsetTotalsRowDxfId();

    String getHeaderRowCellStyle();

    STXstring xgetHeaderRowCellStyle();

    boolean isSetHeaderRowCellStyle();

    void setHeaderRowCellStyle(String str);

    void xsetHeaderRowCellStyle(STXstring sTXstring);

    void unsetHeaderRowCellStyle();

    String getDataCellStyle();

    STXstring xgetDataCellStyle();

    boolean isSetDataCellStyle();

    void setDataCellStyle(String str);

    void xsetDataCellStyle(STXstring sTXstring);

    void unsetDataCellStyle();

    String getTotalsRowCellStyle();

    STXstring xgetTotalsRowCellStyle();

    boolean isSetTotalsRowCellStyle();

    void setTotalsRowCellStyle(String str);

    void xsetTotalsRowCellStyle(STXstring sTXstring);

    void unsetTotalsRowCellStyle();
}
