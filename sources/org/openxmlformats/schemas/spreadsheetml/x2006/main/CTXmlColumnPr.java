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
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STXmlDataType;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTXmlColumnPr.class */
public interface CTXmlColumnPr extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTXmlColumnPr.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctxmlcolumnprc14etype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTXmlColumnPr$Factory.class */
    public static final class Factory {
        public static CTXmlColumnPr newInstance() {
            return (CTXmlColumnPr) POIXMLTypeLoader.newInstance(CTXmlColumnPr.type, null);
        }

        public static CTXmlColumnPr newInstance(XmlOptions xmlOptions) {
            return (CTXmlColumnPr) POIXMLTypeLoader.newInstance(CTXmlColumnPr.type, xmlOptions);
        }

        public static CTXmlColumnPr parse(String str) throws XmlException {
            return (CTXmlColumnPr) POIXMLTypeLoader.parse(str, CTXmlColumnPr.type, (XmlOptions) null);
        }

        public static CTXmlColumnPr parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTXmlColumnPr) POIXMLTypeLoader.parse(str, CTXmlColumnPr.type, xmlOptions);
        }

        public static CTXmlColumnPr parse(File file) throws XmlException, IOException {
            return (CTXmlColumnPr) POIXMLTypeLoader.parse(file, CTXmlColumnPr.type, (XmlOptions) null);
        }

        public static CTXmlColumnPr parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTXmlColumnPr) POIXMLTypeLoader.parse(file, CTXmlColumnPr.type, xmlOptions);
        }

        public static CTXmlColumnPr parse(URL url) throws XmlException, IOException {
            return (CTXmlColumnPr) POIXMLTypeLoader.parse(url, CTXmlColumnPr.type, (XmlOptions) null);
        }

        public static CTXmlColumnPr parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTXmlColumnPr) POIXMLTypeLoader.parse(url, CTXmlColumnPr.type, xmlOptions);
        }

        public static CTXmlColumnPr parse(InputStream inputStream) throws XmlException, IOException {
            return (CTXmlColumnPr) POIXMLTypeLoader.parse(inputStream, CTXmlColumnPr.type, (XmlOptions) null);
        }

        public static CTXmlColumnPr parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTXmlColumnPr) POIXMLTypeLoader.parse(inputStream, CTXmlColumnPr.type, xmlOptions);
        }

        public static CTXmlColumnPr parse(Reader reader) throws XmlException, IOException {
            return (CTXmlColumnPr) POIXMLTypeLoader.parse(reader, CTXmlColumnPr.type, (XmlOptions) null);
        }

        public static CTXmlColumnPr parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTXmlColumnPr) POIXMLTypeLoader.parse(reader, CTXmlColumnPr.type, xmlOptions);
        }

        public static CTXmlColumnPr parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTXmlColumnPr) POIXMLTypeLoader.parse(xMLStreamReader, CTXmlColumnPr.type, (XmlOptions) null);
        }

        public static CTXmlColumnPr parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTXmlColumnPr) POIXMLTypeLoader.parse(xMLStreamReader, CTXmlColumnPr.type, xmlOptions);
        }

        public static CTXmlColumnPr parse(Node node) throws XmlException {
            return (CTXmlColumnPr) POIXMLTypeLoader.parse(node, CTXmlColumnPr.type, (XmlOptions) null);
        }

        public static CTXmlColumnPr parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTXmlColumnPr) POIXMLTypeLoader.parse(node, CTXmlColumnPr.type, xmlOptions);
        }

        public static CTXmlColumnPr parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTXmlColumnPr) POIXMLTypeLoader.parse(xMLInputStream, CTXmlColumnPr.type, (XmlOptions) null);
        }

        public static CTXmlColumnPr parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTXmlColumnPr) POIXMLTypeLoader.parse(xMLInputStream, CTXmlColumnPr.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTXmlColumnPr.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTXmlColumnPr.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTExtensionList getExtLst();

    boolean isSetExtLst();

    void setExtLst(CTExtensionList cTExtensionList);

    CTExtensionList addNewExtLst();

    void unsetExtLst();

    long getMapId();

    XmlUnsignedInt xgetMapId();

    void setMapId(long j);

    void xsetMapId(XmlUnsignedInt xmlUnsignedInt);

    String getXpath();

    STXstring xgetXpath();

    void setXpath(String str);

    void xsetXpath(STXstring sTXstring);

    boolean getDenormalized();

    XmlBoolean xgetDenormalized();

    boolean isSetDenormalized();

    void setDenormalized(boolean z);

    void xsetDenormalized(XmlBoolean xmlBoolean);

    void unsetDenormalized();

    STXmlDataType.Enum getXmlDataType();

    STXmlDataType xgetXmlDataType();

    void setXmlDataType(STXmlDataType.Enum r1);

    void xsetXmlDataType(STXmlDataType sTXmlDataType);
}
