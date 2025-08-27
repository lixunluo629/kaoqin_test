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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTBookmarkRange.class */
public interface CTBookmarkRange extends CTMarkupRange {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTBookmarkRange.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctbookmarkranged88btype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTBookmarkRange$Factory.class */
    public static final class Factory {
        public static CTBookmarkRange newInstance() {
            return (CTBookmarkRange) POIXMLTypeLoader.newInstance(CTBookmarkRange.type, null);
        }

        public static CTBookmarkRange newInstance(XmlOptions xmlOptions) {
            return (CTBookmarkRange) POIXMLTypeLoader.newInstance(CTBookmarkRange.type, xmlOptions);
        }

        public static CTBookmarkRange parse(String str) throws XmlException {
            return (CTBookmarkRange) POIXMLTypeLoader.parse(str, CTBookmarkRange.type, (XmlOptions) null);
        }

        public static CTBookmarkRange parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTBookmarkRange) POIXMLTypeLoader.parse(str, CTBookmarkRange.type, xmlOptions);
        }

        public static CTBookmarkRange parse(File file) throws XmlException, IOException {
            return (CTBookmarkRange) POIXMLTypeLoader.parse(file, CTBookmarkRange.type, (XmlOptions) null);
        }

        public static CTBookmarkRange parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBookmarkRange) POIXMLTypeLoader.parse(file, CTBookmarkRange.type, xmlOptions);
        }

        public static CTBookmarkRange parse(URL url) throws XmlException, IOException {
            return (CTBookmarkRange) POIXMLTypeLoader.parse(url, CTBookmarkRange.type, (XmlOptions) null);
        }

        public static CTBookmarkRange parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBookmarkRange) POIXMLTypeLoader.parse(url, CTBookmarkRange.type, xmlOptions);
        }

        public static CTBookmarkRange parse(InputStream inputStream) throws XmlException, IOException {
            return (CTBookmarkRange) POIXMLTypeLoader.parse(inputStream, CTBookmarkRange.type, (XmlOptions) null);
        }

        public static CTBookmarkRange parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBookmarkRange) POIXMLTypeLoader.parse(inputStream, CTBookmarkRange.type, xmlOptions);
        }

        public static CTBookmarkRange parse(Reader reader) throws XmlException, IOException {
            return (CTBookmarkRange) POIXMLTypeLoader.parse(reader, CTBookmarkRange.type, (XmlOptions) null);
        }

        public static CTBookmarkRange parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTBookmarkRange) POIXMLTypeLoader.parse(reader, CTBookmarkRange.type, xmlOptions);
        }

        public static CTBookmarkRange parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTBookmarkRange) POIXMLTypeLoader.parse(xMLStreamReader, CTBookmarkRange.type, (XmlOptions) null);
        }

        public static CTBookmarkRange parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTBookmarkRange) POIXMLTypeLoader.parse(xMLStreamReader, CTBookmarkRange.type, xmlOptions);
        }

        public static CTBookmarkRange parse(Node node) throws XmlException {
            return (CTBookmarkRange) POIXMLTypeLoader.parse(node, CTBookmarkRange.type, (XmlOptions) null);
        }

        public static CTBookmarkRange parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTBookmarkRange) POIXMLTypeLoader.parse(node, CTBookmarkRange.type, xmlOptions);
        }

        public static CTBookmarkRange parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTBookmarkRange) POIXMLTypeLoader.parse(xMLInputStream, CTBookmarkRange.type, (XmlOptions) null);
        }

        public static CTBookmarkRange parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTBookmarkRange) POIXMLTypeLoader.parse(xMLInputStream, CTBookmarkRange.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTBookmarkRange.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTBookmarkRange.type, xmlOptions);
        }

        private Factory() {
        }
    }

    BigInteger getColFirst();

    STDecimalNumber xgetColFirst();

    boolean isSetColFirst();

    void setColFirst(BigInteger bigInteger);

    void xsetColFirst(STDecimalNumber sTDecimalNumber);

    void unsetColFirst();

    BigInteger getColLast();

    STDecimalNumber xgetColLast();

    boolean isSetColLast();

    void setColLast(BigInteger bigInteger);

    void xsetColLast(STDecimalNumber sTDecimalNumber);

    void unsetColLast();
}
