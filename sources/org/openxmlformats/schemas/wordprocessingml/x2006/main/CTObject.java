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
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTObject.class */
public interface CTObject extends CTPictureBase {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTObject.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctobject47c9type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTObject$Factory.class */
    public static final class Factory {
        public static CTObject newInstance() {
            return (CTObject) POIXMLTypeLoader.newInstance(CTObject.type, null);
        }

        public static CTObject newInstance(XmlOptions xmlOptions) {
            return (CTObject) POIXMLTypeLoader.newInstance(CTObject.type, xmlOptions);
        }

        public static CTObject parse(String str) throws XmlException {
            return (CTObject) POIXMLTypeLoader.parse(str, CTObject.type, (XmlOptions) null);
        }

        public static CTObject parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTObject) POIXMLTypeLoader.parse(str, CTObject.type, xmlOptions);
        }

        public static CTObject parse(File file) throws XmlException, IOException {
            return (CTObject) POIXMLTypeLoader.parse(file, CTObject.type, (XmlOptions) null);
        }

        public static CTObject parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTObject) POIXMLTypeLoader.parse(file, CTObject.type, xmlOptions);
        }

        public static CTObject parse(URL url) throws XmlException, IOException {
            return (CTObject) POIXMLTypeLoader.parse(url, CTObject.type, (XmlOptions) null);
        }

        public static CTObject parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTObject) POIXMLTypeLoader.parse(url, CTObject.type, xmlOptions);
        }

        public static CTObject parse(InputStream inputStream) throws XmlException, IOException {
            return (CTObject) POIXMLTypeLoader.parse(inputStream, CTObject.type, (XmlOptions) null);
        }

        public static CTObject parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTObject) POIXMLTypeLoader.parse(inputStream, CTObject.type, xmlOptions);
        }

        public static CTObject parse(Reader reader) throws XmlException, IOException {
            return (CTObject) POIXMLTypeLoader.parse(reader, CTObject.type, (XmlOptions) null);
        }

        public static CTObject parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTObject) POIXMLTypeLoader.parse(reader, CTObject.type, xmlOptions);
        }

        public static CTObject parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTObject) POIXMLTypeLoader.parse(xMLStreamReader, CTObject.type, (XmlOptions) null);
        }

        public static CTObject parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTObject) POIXMLTypeLoader.parse(xMLStreamReader, CTObject.type, xmlOptions);
        }

        public static CTObject parse(Node node) throws XmlException {
            return (CTObject) POIXMLTypeLoader.parse(node, CTObject.type, (XmlOptions) null);
        }

        public static CTObject parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTObject) POIXMLTypeLoader.parse(node, CTObject.type, xmlOptions);
        }

        public static CTObject parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTObject) POIXMLTypeLoader.parse(xMLInputStream, CTObject.type, (XmlOptions) null);
        }

        public static CTObject parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTObject) POIXMLTypeLoader.parse(xMLInputStream, CTObject.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTObject.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTObject.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTControl getControl();

    boolean isSetControl();

    void setControl(CTControl cTControl);

    CTControl addNewControl();

    void unsetControl();

    BigInteger getDxaOrig();

    STTwipsMeasure xgetDxaOrig();

    boolean isSetDxaOrig();

    void setDxaOrig(BigInteger bigInteger);

    void xsetDxaOrig(STTwipsMeasure sTTwipsMeasure);

    void unsetDxaOrig();

    BigInteger getDyaOrig();

    STTwipsMeasure xgetDyaOrig();

    boolean isSetDyaOrig();

    void setDyaOrig(BigInteger bigInteger);

    void xsetDyaOrig(STTwipsMeasure sTTwipsMeasure);

    void unsetDyaOrig();
}
