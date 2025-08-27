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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTPPrDefault.class */
public interface CTPPrDefault extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTPPrDefault.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sF1327CCA741569E70F9CA8C9AF9B44B2").resolveHandle("ctpprdefaultf839type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/wordprocessingml/x2006/main/CTPPrDefault$Factory.class */
    public static final class Factory {
        public static CTPPrDefault newInstance() {
            return (CTPPrDefault) POIXMLTypeLoader.newInstance(CTPPrDefault.type, null);
        }

        public static CTPPrDefault newInstance(XmlOptions xmlOptions) {
            return (CTPPrDefault) POIXMLTypeLoader.newInstance(CTPPrDefault.type, xmlOptions);
        }

        public static CTPPrDefault parse(String str) throws XmlException {
            return (CTPPrDefault) POIXMLTypeLoader.parse(str, CTPPrDefault.type, (XmlOptions) null);
        }

        public static CTPPrDefault parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTPPrDefault) POIXMLTypeLoader.parse(str, CTPPrDefault.type, xmlOptions);
        }

        public static CTPPrDefault parse(File file) throws XmlException, IOException {
            return (CTPPrDefault) POIXMLTypeLoader.parse(file, CTPPrDefault.type, (XmlOptions) null);
        }

        public static CTPPrDefault parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPPrDefault) POIXMLTypeLoader.parse(file, CTPPrDefault.type, xmlOptions);
        }

        public static CTPPrDefault parse(URL url) throws XmlException, IOException {
            return (CTPPrDefault) POIXMLTypeLoader.parse(url, CTPPrDefault.type, (XmlOptions) null);
        }

        public static CTPPrDefault parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPPrDefault) POIXMLTypeLoader.parse(url, CTPPrDefault.type, xmlOptions);
        }

        public static CTPPrDefault parse(InputStream inputStream) throws XmlException, IOException {
            return (CTPPrDefault) POIXMLTypeLoader.parse(inputStream, CTPPrDefault.type, (XmlOptions) null);
        }

        public static CTPPrDefault parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPPrDefault) POIXMLTypeLoader.parse(inputStream, CTPPrDefault.type, xmlOptions);
        }

        public static CTPPrDefault parse(Reader reader) throws XmlException, IOException {
            return (CTPPrDefault) POIXMLTypeLoader.parse(reader, CTPPrDefault.type, (XmlOptions) null);
        }

        public static CTPPrDefault parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTPPrDefault) POIXMLTypeLoader.parse(reader, CTPPrDefault.type, xmlOptions);
        }

        public static CTPPrDefault parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTPPrDefault) POIXMLTypeLoader.parse(xMLStreamReader, CTPPrDefault.type, (XmlOptions) null);
        }

        public static CTPPrDefault parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTPPrDefault) POIXMLTypeLoader.parse(xMLStreamReader, CTPPrDefault.type, xmlOptions);
        }

        public static CTPPrDefault parse(Node node) throws XmlException {
            return (CTPPrDefault) POIXMLTypeLoader.parse(node, CTPPrDefault.type, (XmlOptions) null);
        }

        public static CTPPrDefault parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTPPrDefault) POIXMLTypeLoader.parse(node, CTPPrDefault.type, xmlOptions);
        }

        public static CTPPrDefault parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTPPrDefault) POIXMLTypeLoader.parse(xMLInputStream, CTPPrDefault.type, (XmlOptions) null);
        }

        public static CTPPrDefault parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTPPrDefault) POIXMLTypeLoader.parse(xMLInputStream, CTPPrDefault.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPPrDefault.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTPPrDefault.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTPPr getPPr();

    boolean isSetPPr();

    void setPPr(CTPPr cTPPr);

    CTPPr addNewPPr();

    void unsetPPr();
}
