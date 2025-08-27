package org.etsi.uri.x01903.v13;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.List;
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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/OCSPValuesType.class */
public interface OCSPValuesType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(OCSPValuesType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("ocspvaluestypeb421type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/OCSPValuesType$Factory.class */
    public static final class Factory {
        public static OCSPValuesType newInstance() {
            return (OCSPValuesType) POIXMLTypeLoader.newInstance(OCSPValuesType.type, null);
        }

        public static OCSPValuesType newInstance(XmlOptions xmlOptions) {
            return (OCSPValuesType) POIXMLTypeLoader.newInstance(OCSPValuesType.type, xmlOptions);
        }

        public static OCSPValuesType parse(String str) throws XmlException {
            return (OCSPValuesType) POIXMLTypeLoader.parse(str, OCSPValuesType.type, (XmlOptions) null);
        }

        public static OCSPValuesType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (OCSPValuesType) POIXMLTypeLoader.parse(str, OCSPValuesType.type, xmlOptions);
        }

        public static OCSPValuesType parse(File file) throws XmlException, IOException {
            return (OCSPValuesType) POIXMLTypeLoader.parse(file, OCSPValuesType.type, (XmlOptions) null);
        }

        public static OCSPValuesType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (OCSPValuesType) POIXMLTypeLoader.parse(file, OCSPValuesType.type, xmlOptions);
        }

        public static OCSPValuesType parse(URL url) throws XmlException, IOException {
            return (OCSPValuesType) POIXMLTypeLoader.parse(url, OCSPValuesType.type, (XmlOptions) null);
        }

        public static OCSPValuesType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (OCSPValuesType) POIXMLTypeLoader.parse(url, OCSPValuesType.type, xmlOptions);
        }

        public static OCSPValuesType parse(InputStream inputStream) throws XmlException, IOException {
            return (OCSPValuesType) POIXMLTypeLoader.parse(inputStream, OCSPValuesType.type, (XmlOptions) null);
        }

        public static OCSPValuesType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (OCSPValuesType) POIXMLTypeLoader.parse(inputStream, OCSPValuesType.type, xmlOptions);
        }

        public static OCSPValuesType parse(Reader reader) throws XmlException, IOException {
            return (OCSPValuesType) POIXMLTypeLoader.parse(reader, OCSPValuesType.type, (XmlOptions) null);
        }

        public static OCSPValuesType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (OCSPValuesType) POIXMLTypeLoader.parse(reader, OCSPValuesType.type, xmlOptions);
        }

        public static OCSPValuesType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (OCSPValuesType) POIXMLTypeLoader.parse(xMLStreamReader, OCSPValuesType.type, (XmlOptions) null);
        }

        public static OCSPValuesType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (OCSPValuesType) POIXMLTypeLoader.parse(xMLStreamReader, OCSPValuesType.type, xmlOptions);
        }

        public static OCSPValuesType parse(Node node) throws XmlException {
            return (OCSPValuesType) POIXMLTypeLoader.parse(node, OCSPValuesType.type, (XmlOptions) null);
        }

        public static OCSPValuesType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (OCSPValuesType) POIXMLTypeLoader.parse(node, OCSPValuesType.type, xmlOptions);
        }

        public static OCSPValuesType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (OCSPValuesType) POIXMLTypeLoader.parse(xMLInputStream, OCSPValuesType.type, (XmlOptions) null);
        }

        public static OCSPValuesType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (OCSPValuesType) POIXMLTypeLoader.parse(xMLInputStream, OCSPValuesType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, OCSPValuesType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, OCSPValuesType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<EncapsulatedPKIDataType> getEncapsulatedOCSPValueList();

    EncapsulatedPKIDataType[] getEncapsulatedOCSPValueArray();

    EncapsulatedPKIDataType getEncapsulatedOCSPValueArray(int i);

    int sizeOfEncapsulatedOCSPValueArray();

    void setEncapsulatedOCSPValueArray(EncapsulatedPKIDataType[] encapsulatedPKIDataTypeArr);

    void setEncapsulatedOCSPValueArray(int i, EncapsulatedPKIDataType encapsulatedPKIDataType);

    EncapsulatedPKIDataType insertNewEncapsulatedOCSPValue(int i);

    EncapsulatedPKIDataType addNewEncapsulatedOCSPValue();

    void removeEncapsulatedOCSPValue(int i);
}
