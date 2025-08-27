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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTInd.class */
public interface CTInd extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTInd.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctind4b93type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTInd$Factory.class */
    public static final class Factory {
        public static CTInd newInstance() {
            return (CTInd) POIXMLTypeLoader.newInstance(CTInd.type, null);
        }

        public static CTInd newInstance(XmlOptions xmlOptions) {
            return (CTInd) POIXMLTypeLoader.newInstance(CTInd.type, xmlOptions);
        }

        public static CTInd parse(String str) throws XmlException {
            return (CTInd) POIXMLTypeLoader.parse(str, CTInd.type, (XmlOptions) null);
        }

        public static CTInd parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTInd) POIXMLTypeLoader.parse(str, CTInd.type, xmlOptions);
        }

        public static CTInd parse(File file) throws XmlException, IOException {
            return (CTInd) POIXMLTypeLoader.parse(file, CTInd.type, (XmlOptions) null);
        }

        public static CTInd parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTInd) POIXMLTypeLoader.parse(file, CTInd.type, xmlOptions);
        }

        public static CTInd parse(URL url) throws XmlException, IOException {
            return (CTInd) POIXMLTypeLoader.parse(url, CTInd.type, (XmlOptions) null);
        }

        public static CTInd parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTInd) POIXMLTypeLoader.parse(url, CTInd.type, xmlOptions);
        }

        public static CTInd parse(InputStream inputStream) throws XmlException, IOException {
            return (CTInd) POIXMLTypeLoader.parse(inputStream, CTInd.type, (XmlOptions) null);
        }

        public static CTInd parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTInd) POIXMLTypeLoader.parse(inputStream, CTInd.type, xmlOptions);
        }

        public static CTInd parse(Reader reader) throws XmlException, IOException {
            return (CTInd) POIXMLTypeLoader.parse(reader, CTInd.type, (XmlOptions) null);
        }

        public static CTInd parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTInd) POIXMLTypeLoader.parse(reader, CTInd.type, xmlOptions);
        }

        public static CTInd parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTInd) POIXMLTypeLoader.parse(xMLStreamReader, CTInd.type, (XmlOptions) null);
        }

        public static CTInd parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTInd) POIXMLTypeLoader.parse(xMLStreamReader, CTInd.type, xmlOptions);
        }

        public static CTInd parse(Node node) throws XmlException {
            return (CTInd) POIXMLTypeLoader.parse(node, CTInd.type, (XmlOptions) null);
        }

        public static CTInd parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTInd) POIXMLTypeLoader.parse(node, CTInd.type, xmlOptions);
        }

        public static CTInd parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTInd) POIXMLTypeLoader.parse(xMLInputStream, CTInd.type, (XmlOptions) null);
        }

        public static CTInd parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTInd) POIXMLTypeLoader.parse(xMLInputStream, CTInd.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTInd.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTInd.type, xmlOptions);
        }

        private Factory() {
        }
    }

    BigInteger getLeft();

    STSignedTwipsMeasure xgetLeft();

    boolean isSetLeft();

    void setLeft(BigInteger bigInteger);

    void xsetLeft(STSignedTwipsMeasure sTSignedTwipsMeasure);

    void unsetLeft();

    BigInteger getLeftChars();

    STDecimalNumber xgetLeftChars();

    boolean isSetLeftChars();

    void setLeftChars(BigInteger bigInteger);

    void xsetLeftChars(STDecimalNumber sTDecimalNumber);

    void unsetLeftChars();

    BigInteger getRight();

    STSignedTwipsMeasure xgetRight();

    boolean isSetRight();

    void setRight(BigInteger bigInteger);

    void xsetRight(STSignedTwipsMeasure sTSignedTwipsMeasure);

    void unsetRight();

    BigInteger getRightChars();

    STDecimalNumber xgetRightChars();

    boolean isSetRightChars();

    void setRightChars(BigInteger bigInteger);

    void xsetRightChars(STDecimalNumber sTDecimalNumber);

    void unsetRightChars();

    BigInteger getHanging();

    STTwipsMeasure xgetHanging();

    boolean isSetHanging();

    void setHanging(BigInteger bigInteger);

    void xsetHanging(STTwipsMeasure sTTwipsMeasure);

    void unsetHanging();

    BigInteger getHangingChars();

    STDecimalNumber xgetHangingChars();

    boolean isSetHangingChars();

    void setHangingChars(BigInteger bigInteger);

    void xsetHangingChars(STDecimalNumber sTDecimalNumber);

    void unsetHangingChars();

    BigInteger getFirstLine();

    STTwipsMeasure xgetFirstLine();

    boolean isSetFirstLine();

    void setFirstLine(BigInteger bigInteger);

    void xsetFirstLine(STTwipsMeasure sTTwipsMeasure);

    void unsetFirstLine();

    BigInteger getFirstLineChars();

    STDecimalNumber xgetFirstLineChars();

    boolean isSetFirstLineChars();

    void setFirstLineChars(BigInteger bigInteger);

    void xsetFirstLineChars(STDecimalNumber sTDecimalNumber);

    void unsetFirstLineChars();
}
