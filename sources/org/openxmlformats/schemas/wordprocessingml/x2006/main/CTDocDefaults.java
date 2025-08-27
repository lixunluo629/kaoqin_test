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
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTDocDefaults.class */
public interface CTDocDefaults extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTDocDefaults.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctdocdefaults2ea8type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTDocDefaults$Factory.class */
    public static final class Factory {
        public static CTDocDefaults newInstance() {
            return (CTDocDefaults) POIXMLTypeLoader.newInstance(CTDocDefaults.type, null);
        }

        public static CTDocDefaults newInstance(XmlOptions xmlOptions) {
            return (CTDocDefaults) POIXMLTypeLoader.newInstance(CTDocDefaults.type, xmlOptions);
        }

        public static CTDocDefaults parse(String str) throws XmlException {
            return (CTDocDefaults) POIXMLTypeLoader.parse(str, CTDocDefaults.type, (XmlOptions) null);
        }

        public static CTDocDefaults parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTDocDefaults) POIXMLTypeLoader.parse(str, CTDocDefaults.type, xmlOptions);
        }

        public static CTDocDefaults parse(File file) throws XmlException, IOException {
            return (CTDocDefaults) POIXMLTypeLoader.parse(file, CTDocDefaults.type, (XmlOptions) null);
        }

        public static CTDocDefaults parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDocDefaults) POIXMLTypeLoader.parse(file, CTDocDefaults.type, xmlOptions);
        }

        public static CTDocDefaults parse(URL url) throws XmlException, IOException {
            return (CTDocDefaults) POIXMLTypeLoader.parse(url, CTDocDefaults.type, (XmlOptions) null);
        }

        public static CTDocDefaults parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDocDefaults) POIXMLTypeLoader.parse(url, CTDocDefaults.type, xmlOptions);
        }

        public static CTDocDefaults parse(InputStream inputStream) throws XmlException, IOException {
            return (CTDocDefaults) POIXMLTypeLoader.parse(inputStream, CTDocDefaults.type, (XmlOptions) null);
        }

        public static CTDocDefaults parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDocDefaults) POIXMLTypeLoader.parse(inputStream, CTDocDefaults.type, xmlOptions);
        }

        public static CTDocDefaults parse(Reader reader) throws XmlException, IOException {
            return (CTDocDefaults) POIXMLTypeLoader.parse(reader, CTDocDefaults.type, (XmlOptions) null);
        }

        public static CTDocDefaults parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTDocDefaults) POIXMLTypeLoader.parse(reader, CTDocDefaults.type, xmlOptions);
        }

        public static CTDocDefaults parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTDocDefaults) POIXMLTypeLoader.parse(xMLStreamReader, CTDocDefaults.type, (XmlOptions) null);
        }

        public static CTDocDefaults parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTDocDefaults) POIXMLTypeLoader.parse(xMLStreamReader, CTDocDefaults.type, xmlOptions);
        }

        public static CTDocDefaults parse(Node node) throws XmlException {
            return (CTDocDefaults) POIXMLTypeLoader.parse(node, CTDocDefaults.type, (XmlOptions) null);
        }

        public static CTDocDefaults parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTDocDefaults) POIXMLTypeLoader.parse(node, CTDocDefaults.type, xmlOptions);
        }

        public static CTDocDefaults parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTDocDefaults) POIXMLTypeLoader.parse(xMLInputStream, CTDocDefaults.type, (XmlOptions) null);
        }

        public static CTDocDefaults parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTDocDefaults) POIXMLTypeLoader.parse(xMLInputStream, CTDocDefaults.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTDocDefaults.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTDocDefaults.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTRPrDefault getRPrDefault();

    boolean isSetRPrDefault();

    void setRPrDefault(CTRPrDefault cTRPrDefault);

    CTRPrDefault addNewRPrDefault();

    void unsetRPrDefault();

    CTPPrDefault getPPrDefault();

    boolean isSetPPrDefault();

    void setPPrDefault(CTPPrDefault cTPPrDefault);

    CTPPrDefault addNewPPrDefault();

    void unsetPPrDefault();
}
