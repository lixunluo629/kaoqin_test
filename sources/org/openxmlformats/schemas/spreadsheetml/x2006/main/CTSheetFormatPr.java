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
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlDouble;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlUnsignedByte;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTSheetFormatPr.class */
public interface CTSheetFormatPr extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTSheetFormatPr.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctsheetformatprdef7type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTSheetFormatPr$Factory.class */
    public static final class Factory {
        public static CTSheetFormatPr newInstance() {
            return (CTSheetFormatPr) POIXMLTypeLoader.newInstance(CTSheetFormatPr.type, null);
        }

        public static CTSheetFormatPr newInstance(XmlOptions xmlOptions) {
            return (CTSheetFormatPr) POIXMLTypeLoader.newInstance(CTSheetFormatPr.type, xmlOptions);
        }

        public static CTSheetFormatPr parse(String str) throws XmlException {
            return (CTSheetFormatPr) POIXMLTypeLoader.parse(str, CTSheetFormatPr.type, (XmlOptions) null);
        }

        public static CTSheetFormatPr parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTSheetFormatPr) POIXMLTypeLoader.parse(str, CTSheetFormatPr.type, xmlOptions);
        }

        public static CTSheetFormatPr parse(File file) throws XmlException, IOException {
            return (CTSheetFormatPr) POIXMLTypeLoader.parse(file, CTSheetFormatPr.type, (XmlOptions) null);
        }

        public static CTSheetFormatPr parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSheetFormatPr) POIXMLTypeLoader.parse(file, CTSheetFormatPr.type, xmlOptions);
        }

        public static CTSheetFormatPr parse(URL url) throws XmlException, IOException {
            return (CTSheetFormatPr) POIXMLTypeLoader.parse(url, CTSheetFormatPr.type, (XmlOptions) null);
        }

        public static CTSheetFormatPr parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSheetFormatPr) POIXMLTypeLoader.parse(url, CTSheetFormatPr.type, xmlOptions);
        }

        public static CTSheetFormatPr parse(InputStream inputStream) throws XmlException, IOException {
            return (CTSheetFormatPr) POIXMLTypeLoader.parse(inputStream, CTSheetFormatPr.type, (XmlOptions) null);
        }

        public static CTSheetFormatPr parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSheetFormatPr) POIXMLTypeLoader.parse(inputStream, CTSheetFormatPr.type, xmlOptions);
        }

        public static CTSheetFormatPr parse(Reader reader) throws XmlException, IOException {
            return (CTSheetFormatPr) POIXMLTypeLoader.parse(reader, CTSheetFormatPr.type, (XmlOptions) null);
        }

        public static CTSheetFormatPr parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSheetFormatPr) POIXMLTypeLoader.parse(reader, CTSheetFormatPr.type, xmlOptions);
        }

        public static CTSheetFormatPr parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTSheetFormatPr) POIXMLTypeLoader.parse(xMLStreamReader, CTSheetFormatPr.type, (XmlOptions) null);
        }

        public static CTSheetFormatPr parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTSheetFormatPr) POIXMLTypeLoader.parse(xMLStreamReader, CTSheetFormatPr.type, xmlOptions);
        }

        public static CTSheetFormatPr parse(Node node) throws XmlException {
            return (CTSheetFormatPr) POIXMLTypeLoader.parse(node, CTSheetFormatPr.type, (XmlOptions) null);
        }

        public static CTSheetFormatPr parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTSheetFormatPr) POIXMLTypeLoader.parse(node, CTSheetFormatPr.type, xmlOptions);
        }

        public static CTSheetFormatPr parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTSheetFormatPr) POIXMLTypeLoader.parse(xMLInputStream, CTSheetFormatPr.type, (XmlOptions) null);
        }

        public static CTSheetFormatPr parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTSheetFormatPr) POIXMLTypeLoader.parse(xMLInputStream, CTSheetFormatPr.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSheetFormatPr.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSheetFormatPr.type, xmlOptions);
        }

        private Factory() {
        }
    }

    long getBaseColWidth();

    XmlUnsignedInt xgetBaseColWidth();

    boolean isSetBaseColWidth();

    void setBaseColWidth(long j);

    void xsetBaseColWidth(XmlUnsignedInt xmlUnsignedInt);

    void unsetBaseColWidth();

    double getDefaultColWidth();

    XmlDouble xgetDefaultColWidth();

    boolean isSetDefaultColWidth();

    void setDefaultColWidth(double d);

    void xsetDefaultColWidth(XmlDouble xmlDouble);

    void unsetDefaultColWidth();

    double getDefaultRowHeight();

    XmlDouble xgetDefaultRowHeight();

    void setDefaultRowHeight(double d);

    void xsetDefaultRowHeight(XmlDouble xmlDouble);

    boolean getCustomHeight();

    XmlBoolean xgetCustomHeight();

    boolean isSetCustomHeight();

    void setCustomHeight(boolean z);

    void xsetCustomHeight(XmlBoolean xmlBoolean);

    void unsetCustomHeight();

    boolean getZeroHeight();

    XmlBoolean xgetZeroHeight();

    boolean isSetZeroHeight();

    void setZeroHeight(boolean z);

    void xsetZeroHeight(XmlBoolean xmlBoolean);

    void unsetZeroHeight();

    boolean getThickTop();

    XmlBoolean xgetThickTop();

    boolean isSetThickTop();

    void setThickTop(boolean z);

    void xsetThickTop(XmlBoolean xmlBoolean);

    void unsetThickTop();

    boolean getThickBottom();

    XmlBoolean xgetThickBottom();

    boolean isSetThickBottom();

    void setThickBottom(boolean z);

    void xsetThickBottom(XmlBoolean xmlBoolean);

    void unsetThickBottom();

    short getOutlineLevelRow();

    XmlUnsignedByte xgetOutlineLevelRow();

    boolean isSetOutlineLevelRow();

    void setOutlineLevelRow(short s);

    void xsetOutlineLevelRow(XmlUnsignedByte xmlUnsignedByte);

    void unsetOutlineLevelRow();

    short getOutlineLevelCol();

    XmlUnsignedByte xgetOutlineLevelCol();

    boolean isSetOutlineLevelCol();

    void setOutlineLevelCol(short s);

    void xsetOutlineLevelCol(XmlUnsignedByte xmlUnsignedByte);

    void unsetOutlineLevelCol();
}
