package org.openxmlformats.schemas.xpackage.x2006.digitalSignature;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/xpackage/x2006/digitalSignature/CTSignatureTime.class */
public interface CTSignatureTime extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CTSignatureTime.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("ctsignaturetime461dtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/xpackage/x2006/digitalSignature/CTSignatureTime$Factory.class */
    public static final class Factory {
        public static CTSignatureTime newInstance() {
            return (CTSignatureTime) POIXMLTypeLoader.newInstance(CTSignatureTime.type, null);
        }

        public static CTSignatureTime newInstance(XmlOptions xmlOptions) {
            return (CTSignatureTime) POIXMLTypeLoader.newInstance(CTSignatureTime.type, xmlOptions);
        }

        public static CTSignatureTime parse(String str) throws XmlException {
            return (CTSignatureTime) POIXMLTypeLoader.parse(str, CTSignatureTime.type, (XmlOptions) null);
        }

        public static CTSignatureTime parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CTSignatureTime) POIXMLTypeLoader.parse(str, CTSignatureTime.type, xmlOptions);
        }

        public static CTSignatureTime parse(File file) throws XmlException, IOException {
            return (CTSignatureTime) POIXMLTypeLoader.parse(file, CTSignatureTime.type, (XmlOptions) null);
        }

        public static CTSignatureTime parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSignatureTime) POIXMLTypeLoader.parse(file, CTSignatureTime.type, xmlOptions);
        }

        public static CTSignatureTime parse(URL url) throws XmlException, IOException {
            return (CTSignatureTime) POIXMLTypeLoader.parse(url, CTSignatureTime.type, (XmlOptions) null);
        }

        public static CTSignatureTime parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSignatureTime) POIXMLTypeLoader.parse(url, CTSignatureTime.type, xmlOptions);
        }

        public static CTSignatureTime parse(InputStream inputStream) throws XmlException, IOException {
            return (CTSignatureTime) POIXMLTypeLoader.parse(inputStream, CTSignatureTime.type, (XmlOptions) null);
        }

        public static CTSignatureTime parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSignatureTime) POIXMLTypeLoader.parse(inputStream, CTSignatureTime.type, xmlOptions);
        }

        public static CTSignatureTime parse(Reader reader) throws XmlException, IOException {
            return (CTSignatureTime) POIXMLTypeLoader.parse(reader, CTSignatureTime.type, (XmlOptions) null);
        }

        public static CTSignatureTime parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CTSignatureTime) POIXMLTypeLoader.parse(reader, CTSignatureTime.type, xmlOptions);
        }

        public static CTSignatureTime parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CTSignatureTime) POIXMLTypeLoader.parse(xMLStreamReader, CTSignatureTime.type, (XmlOptions) null);
        }

        public static CTSignatureTime parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CTSignatureTime) POIXMLTypeLoader.parse(xMLStreamReader, CTSignatureTime.type, xmlOptions);
        }

        public static CTSignatureTime parse(Node node) throws XmlException {
            return (CTSignatureTime) POIXMLTypeLoader.parse(node, CTSignatureTime.type, (XmlOptions) null);
        }

        public static CTSignatureTime parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CTSignatureTime) POIXMLTypeLoader.parse(node, CTSignatureTime.type, xmlOptions);
        }

        public static CTSignatureTime parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CTSignatureTime) POIXMLTypeLoader.parse(xMLInputStream, CTSignatureTime.type, (XmlOptions) null);
        }

        public static CTSignatureTime parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CTSignatureTime) POIXMLTypeLoader.parse(xMLInputStream, CTSignatureTime.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSignatureTime.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CTSignatureTime.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getFormat();

    STFormat xgetFormat();

    void setFormat(String str);

    void xsetFormat(STFormat sTFormat);

    String getValue();

    STValue xgetValue();

    void setValue(String str);

    void xsetValue(STValue sTValue);
}
