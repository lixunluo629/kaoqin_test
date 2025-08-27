package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigInteger;
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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTHeight.class */
public interface CTHeight extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTHeight.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctheighta2e1type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTHeight$Factory.class */
    public static final class Factory {
        public static CTHeight newInstance() {
            return (CTHeight) POIXMLTypeLoader.newInstance(CTHeight.type, null);
        }

        public static CTHeight newInstance(XmlOptions xmlOptions) {
            return (CTHeight) POIXMLTypeLoader.newInstance(CTHeight.type, xmlOptions);
        }

        public static CTHeight parse(String str) throws XmlException {
            return (CTHeight) POIXMLTypeLoader.parse(str, CTHeight.type, (XmlOptions) null);
        }

        public static CTHeight parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTHeight) POIXMLTypeLoader.parse(str, CTHeight.type, xmlOptions);
        }

        public static CTHeight parse(File file) throws XmlException, IOException {
            return (CTHeight) POIXMLTypeLoader.parse(file, CTHeight.type, (XmlOptions) null);
        }

        public static CTHeight parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTHeight) POIXMLTypeLoader.parse(file, CTHeight.type, xmlOptions);
        }

        public static CTHeight parse(URL url) throws XmlException, IOException {
            return (CTHeight) POIXMLTypeLoader.parse(url, CTHeight.type, (XmlOptions) null);
        }

        public static CTHeight parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTHeight) POIXMLTypeLoader.parse(url, CTHeight.type, xmlOptions);
        }

        public static CTHeight parse(InputStream inputStream) throws XmlException, IOException {
            return (CTHeight) POIXMLTypeLoader.parse(inputStream, CTHeight.type, (XmlOptions) null);
        }

        public static CTHeight parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTHeight) POIXMLTypeLoader.parse(inputStream, CTHeight.type, xmlOptions);
        }

        public static CTHeight parse(Reader reader) throws XmlException, IOException {
            return (CTHeight) POIXMLTypeLoader.parse(reader, CTHeight.type, (XmlOptions) null);
        }

        public static CTHeight parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTHeight) POIXMLTypeLoader.parse(reader, CTHeight.type, xmlOptions);
        }

        public static CTHeight parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTHeight) POIXMLTypeLoader.parse(xMLStreamReader, CTHeight.type, (XmlOptions) null);
        }

        public static CTHeight parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTHeight) POIXMLTypeLoader.parse(xMLStreamReader, CTHeight.type, xmlOptions);
        }

        public static CTHeight parse(Node node) throws XmlException {
            return (CTHeight) POIXMLTypeLoader.parse(node, CTHeight.type, (XmlOptions) null);
        }

        public static CTHeight parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTHeight) POIXMLTypeLoader.parse(node, CTHeight.type, xmlOptions);
        }

        public static CTHeight parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTHeight) POIXMLTypeLoader.parse(xMLInputStream, CTHeight.type, (XmlOptions) null);
        }

        public static CTHeight parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTHeight) POIXMLTypeLoader.parse(xMLInputStream, CTHeight.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTHeight.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTHeight.type, xmlOptions);
        }

        private Factory() {
        }
    }

    BigInteger getVal();

    STTwipsMeasure xgetVal();

    boolean isSetVal();

    void setVal(BigInteger bigInteger);

    void xsetVal(STTwipsMeasure sTTwipsMeasure);

    void unsetVal();

    STHeightRule$Enum getHRule();

    STHeightRule xgetHRule();

    boolean isSetHRule();

    void setHRule(STHeightRule$Enum sTHeightRule$Enum);

    void xsetHRule(STHeightRule sTHeightRule);

    void unsetHRule();
}
