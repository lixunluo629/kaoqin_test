package org.etsi.uri.x01903.v13;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.XmlBase64Binary;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlID;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/EncapsulatedPKIDataType.class */
public interface EncapsulatedPKIDataType extends XmlBase64Binary {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(EncapsulatedPKIDataType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("encapsulatedpkidatatype4081type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/EncapsulatedPKIDataType$Factory.class */
    public static final class Factory {
        public static EncapsulatedPKIDataType newInstance() {
            return (EncapsulatedPKIDataType) POIXMLTypeLoader.newInstance(EncapsulatedPKIDataType.type, null);
        }

        public static EncapsulatedPKIDataType newInstance(XmlOptions xmlOptions) {
            return (EncapsulatedPKIDataType) POIXMLTypeLoader.newInstance(EncapsulatedPKIDataType.type, xmlOptions);
        }

        public static EncapsulatedPKIDataType parse(String str) throws XmlException {
            return (EncapsulatedPKIDataType) POIXMLTypeLoader.parse(str, EncapsulatedPKIDataType.type, (XmlOptions) null);
        }

        public static EncapsulatedPKIDataType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (EncapsulatedPKIDataType) POIXMLTypeLoader.parse(str, EncapsulatedPKIDataType.type, xmlOptions);
        }

        public static EncapsulatedPKIDataType parse(File file) throws XmlException, IOException {
            return (EncapsulatedPKIDataType) POIXMLTypeLoader.parse(file, EncapsulatedPKIDataType.type, (XmlOptions) null);
        }

        public static EncapsulatedPKIDataType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (EncapsulatedPKIDataType) POIXMLTypeLoader.parse(file, EncapsulatedPKIDataType.type, xmlOptions);
        }

        public static EncapsulatedPKIDataType parse(URL url) throws XmlException, IOException {
            return (EncapsulatedPKIDataType) POIXMLTypeLoader.parse(url, EncapsulatedPKIDataType.type, (XmlOptions) null);
        }

        public static EncapsulatedPKIDataType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (EncapsulatedPKIDataType) POIXMLTypeLoader.parse(url, EncapsulatedPKIDataType.type, xmlOptions);
        }

        public static EncapsulatedPKIDataType parse(InputStream inputStream) throws XmlException, IOException {
            return (EncapsulatedPKIDataType) POIXMLTypeLoader.parse(inputStream, EncapsulatedPKIDataType.type, (XmlOptions) null);
        }

        public static EncapsulatedPKIDataType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (EncapsulatedPKIDataType) POIXMLTypeLoader.parse(inputStream, EncapsulatedPKIDataType.type, xmlOptions);
        }

        public static EncapsulatedPKIDataType parse(Reader reader) throws XmlException, IOException {
            return (EncapsulatedPKIDataType) POIXMLTypeLoader.parse(reader, EncapsulatedPKIDataType.type, (XmlOptions) null);
        }

        public static EncapsulatedPKIDataType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (EncapsulatedPKIDataType) POIXMLTypeLoader.parse(reader, EncapsulatedPKIDataType.type, xmlOptions);
        }

        public static EncapsulatedPKIDataType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (EncapsulatedPKIDataType) POIXMLTypeLoader.parse(xMLStreamReader, EncapsulatedPKIDataType.type, (XmlOptions) null);
        }

        public static EncapsulatedPKIDataType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (EncapsulatedPKIDataType) POIXMLTypeLoader.parse(xMLStreamReader, EncapsulatedPKIDataType.type, xmlOptions);
        }

        public static EncapsulatedPKIDataType parse(Node node) throws XmlException {
            return (EncapsulatedPKIDataType) POIXMLTypeLoader.parse(node, EncapsulatedPKIDataType.type, (XmlOptions) null);
        }

        public static EncapsulatedPKIDataType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (EncapsulatedPKIDataType) POIXMLTypeLoader.parse(node, EncapsulatedPKIDataType.type, xmlOptions);
        }

        public static EncapsulatedPKIDataType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (EncapsulatedPKIDataType) POIXMLTypeLoader.parse(xMLInputStream, EncapsulatedPKIDataType.type, (XmlOptions) null);
        }

        public static EncapsulatedPKIDataType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (EncapsulatedPKIDataType) POIXMLTypeLoader.parse(xMLInputStream, EncapsulatedPKIDataType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, EncapsulatedPKIDataType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, EncapsulatedPKIDataType.type, xmlOptions);
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

    String getEncoding();

    XmlAnyURI xgetEncoding();

    boolean isSetEncoding();

    void setEncoding(String str);

    void xsetEncoding(XmlAnyURI xmlAnyURI);

    void unsetEncoding();
}
