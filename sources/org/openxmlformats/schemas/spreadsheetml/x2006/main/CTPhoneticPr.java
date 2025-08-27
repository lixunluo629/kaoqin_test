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
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTPhoneticPr.class */
public interface CTPhoneticPr extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPhoneticPr.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctphoneticpr898btype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/spreadsheetml/x2006/main/CTPhoneticPr$Factory.class */
    public static final class Factory {
        public static CTPhoneticPr newInstance() {
            return (CTPhoneticPr) POIXMLTypeLoader.newInstance(CTPhoneticPr.type, null);
        }

        public static CTPhoneticPr newInstance(XmlOptions xmlOptions) {
            return (CTPhoneticPr) POIXMLTypeLoader.newInstance(CTPhoneticPr.type, xmlOptions);
        }

        public static CTPhoneticPr parse(String str) throws XmlException {
            return (CTPhoneticPr) POIXMLTypeLoader.parse(str, CTPhoneticPr.type, (XmlOptions) null);
        }

        public static CTPhoneticPr parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPhoneticPr) POIXMLTypeLoader.parse(str, CTPhoneticPr.type, xmlOptions);
        }

        public static CTPhoneticPr parse(File file) throws XmlException, IOException {
            return (CTPhoneticPr) POIXMLTypeLoader.parse(file, CTPhoneticPr.type, (XmlOptions) null);
        }

        public static CTPhoneticPr parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPhoneticPr) POIXMLTypeLoader.parse(file, CTPhoneticPr.type, xmlOptions);
        }

        public static CTPhoneticPr parse(URL url) throws XmlException, IOException {
            return (CTPhoneticPr) POIXMLTypeLoader.parse(url, CTPhoneticPr.type, (XmlOptions) null);
        }

        public static CTPhoneticPr parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPhoneticPr) POIXMLTypeLoader.parse(url, CTPhoneticPr.type, xmlOptions);
        }

        public static CTPhoneticPr parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPhoneticPr) POIXMLTypeLoader.parse(inputStream, CTPhoneticPr.type, (XmlOptions) null);
        }

        public static CTPhoneticPr parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPhoneticPr) POIXMLTypeLoader.parse(inputStream, CTPhoneticPr.type, xmlOptions);
        }

        public static CTPhoneticPr parse(Reader reader) throws XmlException, IOException {
            return (CTPhoneticPr) POIXMLTypeLoader.parse(reader, CTPhoneticPr.type, (XmlOptions) null);
        }

        public static CTPhoneticPr parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPhoneticPr) POIXMLTypeLoader.parse(reader, CTPhoneticPr.type, xmlOptions);
        }

        public static CTPhoneticPr parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPhoneticPr) POIXMLTypeLoader.parse(xMLStreamReader, CTPhoneticPr.type, (XmlOptions) null);
        }

        public static CTPhoneticPr parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPhoneticPr) POIXMLTypeLoader.parse(xMLStreamReader, CTPhoneticPr.type, xmlOptions);
        }

        public static CTPhoneticPr parse(Node node) throws XmlException {
            return (CTPhoneticPr) POIXMLTypeLoader.parse(node, CTPhoneticPr.type, (XmlOptions) null);
        }

        public static CTPhoneticPr parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPhoneticPr) POIXMLTypeLoader.parse(node, CTPhoneticPr.type, xmlOptions);
        }

        public static CTPhoneticPr parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPhoneticPr) POIXMLTypeLoader.parse(xMLInputStream, CTPhoneticPr.type, (XmlOptions) null);
        }

        public static CTPhoneticPr parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPhoneticPr) POIXMLTypeLoader.parse(xMLInputStream, CTPhoneticPr.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPhoneticPr.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPhoneticPr.type, xmlOptions);
        }

        private Factory() {
        }
    }

    long getFontId();

    STFontId xgetFontId();

    void setFontId(long j);

    void xsetFontId(STFontId sTFontId);

    STPhoneticType$Enum getType();

    STPhoneticType xgetType();

    boolean isSetType();

    void setType(STPhoneticType$Enum sTPhoneticType$Enum);

    void xsetType(STPhoneticType sTPhoneticType);

    void unsetType();

    STPhoneticAlignment$Enum getAlignment();

    STPhoneticAlignment xgetAlignment();

    boolean isSetAlignment();

    void setAlignment(STPhoneticAlignment$Enum sTPhoneticAlignment$Enum);

    void xsetAlignment(STPhoneticAlignment sTPhoneticAlignment);

    void unsetAlignment();
}
