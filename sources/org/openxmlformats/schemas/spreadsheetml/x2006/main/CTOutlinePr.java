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
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTOutlinePr.class */
public interface CTOutlinePr extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTOutlinePr.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctoutlineprc483type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTOutlinePr$Factory.class */
    public static final class Factory {
        public static CTOutlinePr newInstance() {
            return (CTOutlinePr) POIXMLTypeLoader.newInstance(CTOutlinePr.type, null);
        }

        public static CTOutlinePr newInstance(XmlOptions xmlOptions) {
            return (CTOutlinePr) POIXMLTypeLoader.newInstance(CTOutlinePr.type, xmlOptions);
        }

        public static CTOutlinePr parse(String str) throws XmlException {
            return (CTOutlinePr) POIXMLTypeLoader.parse(str, CTOutlinePr.type, (XmlOptions) null);
        }

        public static CTOutlinePr parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTOutlinePr) POIXMLTypeLoader.parse(str, CTOutlinePr.type, xmlOptions);
        }

        public static CTOutlinePr parse(File file) throws XmlException, IOException {
            return (CTOutlinePr) POIXMLTypeLoader.parse(file, CTOutlinePr.type, (XmlOptions) null);
        }

        public static CTOutlinePr parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTOutlinePr) POIXMLTypeLoader.parse(file, CTOutlinePr.type, xmlOptions);
        }

        public static CTOutlinePr parse(URL url) throws XmlException, IOException {
            return (CTOutlinePr) POIXMLTypeLoader.parse(url, CTOutlinePr.type, (XmlOptions) null);
        }

        public static CTOutlinePr parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTOutlinePr) POIXMLTypeLoader.parse(url, CTOutlinePr.type, xmlOptions);
        }

        public static CTOutlinePr parse(InputStream inputStream) throws XmlException, IOException {
            return (CTOutlinePr) POIXMLTypeLoader.parse(inputStream, CTOutlinePr.type, (XmlOptions) null);
        }

        public static CTOutlinePr parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTOutlinePr) POIXMLTypeLoader.parse(inputStream, CTOutlinePr.type, xmlOptions);
        }

        public static CTOutlinePr parse(Reader reader) throws XmlException, IOException {
            return (CTOutlinePr) POIXMLTypeLoader.parse(reader, CTOutlinePr.type, (XmlOptions) null);
        }

        public static CTOutlinePr parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTOutlinePr) POIXMLTypeLoader.parse(reader, CTOutlinePr.type, xmlOptions);
        }

        public static CTOutlinePr parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTOutlinePr) POIXMLTypeLoader.parse(xMLStreamReader, CTOutlinePr.type, (XmlOptions) null);
        }

        public static CTOutlinePr parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTOutlinePr) POIXMLTypeLoader.parse(xMLStreamReader, CTOutlinePr.type, xmlOptions);
        }

        public static CTOutlinePr parse(Node node) throws XmlException {
            return (CTOutlinePr) POIXMLTypeLoader.parse(node, CTOutlinePr.type, (XmlOptions) null);
        }

        public static CTOutlinePr parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTOutlinePr) POIXMLTypeLoader.parse(node, CTOutlinePr.type, xmlOptions);
        }

        public static CTOutlinePr parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTOutlinePr) POIXMLTypeLoader.parse(xMLInputStream, CTOutlinePr.type, (XmlOptions) null);
        }

        public static CTOutlinePr parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTOutlinePr) POIXMLTypeLoader.parse(xMLInputStream, CTOutlinePr.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTOutlinePr.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTOutlinePr.type, xmlOptions);
        }

        private Factory() {
        }
    }

    boolean getApplyStyles();

    XmlBoolean xgetApplyStyles();

    boolean isSetApplyStyles();

    void setApplyStyles(boolean z);

    void xsetApplyStyles(XmlBoolean xmlBoolean);

    void unsetApplyStyles();

    boolean getSummaryBelow();

    XmlBoolean xgetSummaryBelow();

    boolean isSetSummaryBelow();

    void setSummaryBelow(boolean z);

    void xsetSummaryBelow(XmlBoolean xmlBoolean);

    void unsetSummaryBelow();

    boolean getSummaryRight();

    XmlBoolean xgetSummaryRight();

    boolean isSetSummaryRight();

    void setSummaryRight(boolean z);

    void xsetSummaryRight(XmlBoolean xmlBoolean);

    void unsetSummaryRight();

    boolean getShowOutlineSymbols();

    XmlBoolean xgetShowOutlineSymbols();

    boolean isSetShowOutlineSymbols();

    void setShowOutlineSymbols(boolean z);

    void xsetShowOutlineSymbols(XmlBoolean xmlBoolean);

    void unsetShowOutlineSymbols();
}
