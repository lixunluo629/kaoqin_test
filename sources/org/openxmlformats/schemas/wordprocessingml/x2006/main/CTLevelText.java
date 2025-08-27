package org.openxmlformats.schemas.wordprocessingml.x2006.main;

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
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTLevelText.class */
public interface CTLevelText extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTLevelText.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctleveltext0621type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTLevelText$Factory.class */
    public static final class Factory {
        public static CTLevelText newInstance() {
            return (CTLevelText) POIXMLTypeLoader.newInstance(CTLevelText.type, null);
        }

        public static CTLevelText newInstance(XmlOptions xmlOptions) {
            return (CTLevelText) POIXMLTypeLoader.newInstance(CTLevelText.type, xmlOptions);
        }

        public static CTLevelText parse(String str) throws XmlException {
            return (CTLevelText) POIXMLTypeLoader.parse(str, CTLevelText.type, (XmlOptions) null);
        }

        public static CTLevelText parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTLevelText) POIXMLTypeLoader.parse(str, CTLevelText.type, xmlOptions);
        }

        public static CTLevelText parse(File file) throws XmlException, IOException {
            return (CTLevelText) POIXMLTypeLoader.parse(file, CTLevelText.type, (XmlOptions) null);
        }

        public static CTLevelText parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLevelText) POIXMLTypeLoader.parse(file, CTLevelText.type, xmlOptions);
        }

        public static CTLevelText parse(URL url) throws XmlException, IOException {
            return (CTLevelText) POIXMLTypeLoader.parse(url, CTLevelText.type, (XmlOptions) null);
        }

        public static CTLevelText parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLevelText) POIXMLTypeLoader.parse(url, CTLevelText.type, xmlOptions);
        }

        public static CTLevelText parse(InputStream inputStream) throws XmlException, IOException {
            return (CTLevelText) POIXMLTypeLoader.parse(inputStream, CTLevelText.type, (XmlOptions) null);
        }

        public static CTLevelText parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLevelText) POIXMLTypeLoader.parse(inputStream, CTLevelText.type, xmlOptions);
        }

        public static CTLevelText parse(Reader reader) throws XmlException, IOException {
            return (CTLevelText) POIXMLTypeLoader.parse(reader, CTLevelText.type, (XmlOptions) null);
        }

        public static CTLevelText parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTLevelText) POIXMLTypeLoader.parse(reader, CTLevelText.type, xmlOptions);
        }

        public static CTLevelText parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTLevelText) POIXMLTypeLoader.parse(xMLStreamReader, CTLevelText.type, (XmlOptions) null);
        }

        public static CTLevelText parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTLevelText) POIXMLTypeLoader.parse(xMLStreamReader, CTLevelText.type, xmlOptions);
        }

        public static CTLevelText parse(Node node) throws XmlException {
            return (CTLevelText) POIXMLTypeLoader.parse(node, CTLevelText.type, (XmlOptions) null);
        }

        public static CTLevelText parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTLevelText) POIXMLTypeLoader.parse(node, CTLevelText.type, xmlOptions);
        }

        public static CTLevelText parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTLevelText) POIXMLTypeLoader.parse(xMLInputStream, CTLevelText.type, (XmlOptions) null);
        }

        public static CTLevelText parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTLevelText) POIXMLTypeLoader.parse(xMLInputStream, CTLevelText.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTLevelText.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTLevelText.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getVal();

    STString xgetVal();

    boolean isSetVal();

    void setVal(String str);

    void xsetVal(STString sTString);

    void unsetVal();

    STOnOff.Enum getNull();

    STOnOff xgetNull();

    boolean isSetNull();

    void setNull(STOnOff.Enum r1);

    void xsetNull(STOnOff sTOnOff);

    void unsetNull();
}
