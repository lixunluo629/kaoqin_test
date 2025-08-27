package org.w3.x2000.x09.xmldsig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBase64Binary;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlID;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/w3/x2000/x09/xmldsig/SignatureValueType.class */
public interface SignatureValueType extends XmlBase64Binary {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(SignatureValueType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("signaturevaluetype58cctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/w3/x2000/x09/xmldsig/SignatureValueType$Factory.class */
    public static final class Factory {
        public static SignatureValueType newInstance() {
            return (SignatureValueType) POIXMLTypeLoader.newInstance(SignatureValueType.type, null);
        }

        public static SignatureValueType newInstance(XmlOptions xmlOptions) {
            return (SignatureValueType) POIXMLTypeLoader.newInstance(SignatureValueType.type, xmlOptions);
        }

        public static SignatureValueType parse(String str) throws XmlException {
            return (SignatureValueType) POIXMLTypeLoader.parse(str, SignatureValueType.type, (XmlOptions) null);
        }

        public static SignatureValueType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (SignatureValueType) POIXMLTypeLoader.parse(str, SignatureValueType.type, xmlOptions);
        }

        public static SignatureValueType parse(File file) throws XmlException, IOException {
            return (SignatureValueType) POIXMLTypeLoader.parse(file, SignatureValueType.type, (XmlOptions) null);
        }

        public static SignatureValueType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (SignatureValueType) POIXMLTypeLoader.parse(file, SignatureValueType.type, xmlOptions);
        }

        public static SignatureValueType parse(URL url) throws XmlException, IOException {
            return (SignatureValueType) POIXMLTypeLoader.parse(url, SignatureValueType.type, (XmlOptions) null);
        }

        public static SignatureValueType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (SignatureValueType) POIXMLTypeLoader.parse(url, SignatureValueType.type, xmlOptions);
        }

        public static SignatureValueType parse(InputStream inputStream) throws XmlException, IOException {
            return (SignatureValueType) POIXMLTypeLoader.parse(inputStream, SignatureValueType.type, (XmlOptions) null);
        }

        public static SignatureValueType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (SignatureValueType) POIXMLTypeLoader.parse(inputStream, SignatureValueType.type, xmlOptions);
        }

        public static SignatureValueType parse(Reader reader) throws XmlException, IOException {
            return (SignatureValueType) POIXMLTypeLoader.parse(reader, SignatureValueType.type, (XmlOptions) null);
        }

        public static SignatureValueType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (SignatureValueType) POIXMLTypeLoader.parse(reader, SignatureValueType.type, xmlOptions);
        }

        public static SignatureValueType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (SignatureValueType) POIXMLTypeLoader.parse(xMLStreamReader, SignatureValueType.type, (XmlOptions) null);
        }

        public static SignatureValueType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (SignatureValueType) POIXMLTypeLoader.parse(xMLStreamReader, SignatureValueType.type, xmlOptions);
        }

        public static SignatureValueType parse(Node node) throws XmlException {
            return (SignatureValueType) POIXMLTypeLoader.parse(node, SignatureValueType.type, (XmlOptions) null);
        }

        public static SignatureValueType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (SignatureValueType) POIXMLTypeLoader.parse(node, SignatureValueType.type, xmlOptions);
        }

        public static SignatureValueType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (SignatureValueType) POIXMLTypeLoader.parse(xMLInputStream, SignatureValueType.type, (XmlOptions) null);
        }

        public static SignatureValueType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (SignatureValueType) POIXMLTypeLoader.parse(xMLInputStream, SignatureValueType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, SignatureValueType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, SignatureValueType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    String getId();

    XmlID xgetId();

    boolean isSetId();

    void setId(String str);

    void xsetId(XmlID xmlID);

    void unsetId();
}
