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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTNumLvl.class */
public interface CTNumLvl extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTNumLvl.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctnumlvl416ctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTNumLvl$Factory.class */
    public static final class Factory {
        public static CTNumLvl newInstance() {
            return (CTNumLvl) POIXMLTypeLoader.newInstance(CTNumLvl.type, null);
        }

        public static CTNumLvl newInstance(XmlOptions xmlOptions) {
            return (CTNumLvl) POIXMLTypeLoader.newInstance(CTNumLvl.type, xmlOptions);
        }

        public static CTNumLvl parse(String str) throws XmlException {
            return (CTNumLvl) POIXMLTypeLoader.parse(str, CTNumLvl.type, (XmlOptions) null);
        }

        public static CTNumLvl parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTNumLvl) POIXMLTypeLoader.parse(str, CTNumLvl.type, xmlOptions);
        }

        public static CTNumLvl parse(File file) throws XmlException, IOException {
            return (CTNumLvl) POIXMLTypeLoader.parse(file, CTNumLvl.type, (XmlOptions) null);
        }

        public static CTNumLvl parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNumLvl) POIXMLTypeLoader.parse(file, CTNumLvl.type, xmlOptions);
        }

        public static CTNumLvl parse(URL url) throws XmlException, IOException {
            return (CTNumLvl) POIXMLTypeLoader.parse(url, CTNumLvl.type, (XmlOptions) null);
        }

        public static CTNumLvl parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNumLvl) POIXMLTypeLoader.parse(url, CTNumLvl.type, xmlOptions);
        }

        public static CTNumLvl parse(InputStream inputStream) throws XmlException, IOException {
            return (CTNumLvl) POIXMLTypeLoader.parse(inputStream, CTNumLvl.type, (XmlOptions) null);
        }

        public static CTNumLvl parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNumLvl) POIXMLTypeLoader.parse(inputStream, CTNumLvl.type, xmlOptions);
        }

        public static CTNumLvl parse(Reader reader) throws XmlException, IOException {
            return (CTNumLvl) POIXMLTypeLoader.parse(reader, CTNumLvl.type, (XmlOptions) null);
        }

        public static CTNumLvl parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTNumLvl) POIXMLTypeLoader.parse(reader, CTNumLvl.type, xmlOptions);
        }

        public static CTNumLvl parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTNumLvl) POIXMLTypeLoader.parse(xMLStreamReader, CTNumLvl.type, (XmlOptions) null);
        }

        public static CTNumLvl parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTNumLvl) POIXMLTypeLoader.parse(xMLStreamReader, CTNumLvl.type, xmlOptions);
        }

        public static CTNumLvl parse(Node node) throws XmlException {
            return (CTNumLvl) POIXMLTypeLoader.parse(node, CTNumLvl.type, (XmlOptions) null);
        }

        public static CTNumLvl parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTNumLvl) POIXMLTypeLoader.parse(node, CTNumLvl.type, xmlOptions);
        }

        public static CTNumLvl parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTNumLvl) POIXMLTypeLoader.parse(xMLInputStream, CTNumLvl.type, (XmlOptions) null);
        }

        public static CTNumLvl parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTNumLvl) POIXMLTypeLoader.parse(xMLInputStream, CTNumLvl.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTNumLvl.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTNumLvl.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTDecimalNumber getStartOverride();

    boolean isSetStartOverride();

    void setStartOverride(CTDecimalNumber cTDecimalNumber);

    CTDecimalNumber addNewStartOverride();

    void unsetStartOverride();

    CTLvl getLvl();

    boolean isSetLvl();

    void setLvl(CTLvl cTLvl);

    CTLvl addNewLvl();

    void unsetLvl();

    BigInteger getIlvl();

    STDecimalNumber xgetIlvl();

    void setIlvl(BigInteger bigInteger);

    void xsetIlvl(STDecimalNumber sTDecimalNumber);
}
