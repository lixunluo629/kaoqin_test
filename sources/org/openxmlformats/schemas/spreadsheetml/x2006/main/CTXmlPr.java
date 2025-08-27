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
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STXmlDataType;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTXmlPr.class */
public interface CTXmlPr extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTXmlPr.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctxmlpr2c58type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTXmlPr$Factory.class */
    public static final class Factory {
        public static CTXmlPr newInstance() {
            return (CTXmlPr) POIXMLTypeLoader.newInstance(CTXmlPr.type, null);
        }

        public static CTXmlPr newInstance(XmlOptions xmlOptions) {
            return (CTXmlPr) POIXMLTypeLoader.newInstance(CTXmlPr.type, xmlOptions);
        }

        public static CTXmlPr parse(String str) throws XmlException {
            return (CTXmlPr) POIXMLTypeLoader.parse(str, CTXmlPr.type, (XmlOptions) null);
        }

        public static CTXmlPr parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTXmlPr) POIXMLTypeLoader.parse(str, CTXmlPr.type, xmlOptions);
        }

        public static CTXmlPr parse(File file) throws XmlException, IOException {
            return (CTXmlPr) POIXMLTypeLoader.parse(file, CTXmlPr.type, (XmlOptions) null);
        }

        public static CTXmlPr parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTXmlPr) POIXMLTypeLoader.parse(file, CTXmlPr.type, xmlOptions);
        }

        public static CTXmlPr parse(URL url) throws XmlException, IOException {
            return (CTXmlPr) POIXMLTypeLoader.parse(url, CTXmlPr.type, (XmlOptions) null);
        }

        public static CTXmlPr parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTXmlPr) POIXMLTypeLoader.parse(url, CTXmlPr.type, xmlOptions);
        }

        public static CTXmlPr parse(InputStream inputStream) throws XmlException, IOException {
            return (CTXmlPr) POIXMLTypeLoader.parse(inputStream, CTXmlPr.type, (XmlOptions) null);
        }

        public static CTXmlPr parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTXmlPr) POIXMLTypeLoader.parse(inputStream, CTXmlPr.type, xmlOptions);
        }

        public static CTXmlPr parse(Reader reader) throws XmlException, IOException {
            return (CTXmlPr) POIXMLTypeLoader.parse(reader, CTXmlPr.type, (XmlOptions) null);
        }

        public static CTXmlPr parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTXmlPr) POIXMLTypeLoader.parse(reader, CTXmlPr.type, xmlOptions);
        }

        public static CTXmlPr parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTXmlPr) POIXMLTypeLoader.parse(xMLStreamReader, CTXmlPr.type, (XmlOptions) null);
        }

        public static CTXmlPr parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTXmlPr) POIXMLTypeLoader.parse(xMLStreamReader, CTXmlPr.type, xmlOptions);
        }

        public static CTXmlPr parse(Node node) throws XmlException {
            return (CTXmlPr) POIXMLTypeLoader.parse(node, CTXmlPr.type, (XmlOptions) null);
        }

        public static CTXmlPr parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTXmlPr) POIXMLTypeLoader.parse(node, CTXmlPr.type, xmlOptions);
        }

        public static CTXmlPr parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTXmlPr) POIXMLTypeLoader.parse(xMLInputStream, CTXmlPr.type, (XmlOptions) null);
        }

        public static CTXmlPr parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTXmlPr) POIXMLTypeLoader.parse(xMLInputStream, CTXmlPr.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTXmlPr.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTXmlPr.type, xmlOptions);
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

    STXmlDataType.Enum getXmlDataType();

    STXmlDataType xgetXmlDataType();

    void setXmlDataType(STXmlDataType.Enum r1);

    void xsetXmlDataType(STXmlDataType sTXmlDataType);
}
