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
import org.apache.xmlbeans.XmlID;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/CertificateValuesType.class */
public interface CertificateValuesType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CertificateValuesType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("certificatevaluestype5c75type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/CertificateValuesType$Factory.class */
    public static final class Factory {
        public static CertificateValuesType newInstance() {
            return (CertificateValuesType) POIXMLTypeLoader.newInstance(CertificateValuesType.type, null);
        }

        public static CertificateValuesType newInstance(XmlOptions xmlOptions) {
            return (CertificateValuesType) POIXMLTypeLoader.newInstance(CertificateValuesType.type, xmlOptions);
        }

        public static CertificateValuesType parse(String str) throws XmlException {
            return (CertificateValuesType) POIXMLTypeLoader.parse(str, CertificateValuesType.type, (XmlOptions) null);
        }

        public static CertificateValuesType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CertificateValuesType) POIXMLTypeLoader.parse(str, CertificateValuesType.type, xmlOptions);
        }

        public static CertificateValuesType parse(File file) throws XmlException, IOException {
            return (CertificateValuesType) POIXMLTypeLoader.parse(file, CertificateValuesType.type, (XmlOptions) null);
        }

        public static CertificateValuesType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CertificateValuesType) POIXMLTypeLoader.parse(file, CertificateValuesType.type, xmlOptions);
        }

        public static CertificateValuesType parse(URL url) throws XmlException, IOException {
            return (CertificateValuesType) POIXMLTypeLoader.parse(url, CertificateValuesType.type, (XmlOptions) null);
        }

        public static CertificateValuesType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CertificateValuesType) POIXMLTypeLoader.parse(url, CertificateValuesType.type, xmlOptions);
        }

        public static CertificateValuesType parse(InputStream inputStream) throws XmlException, IOException {
            return (CertificateValuesType) POIXMLTypeLoader.parse(inputStream, CertificateValuesType.type, (XmlOptions) null);
        }

        public static CertificateValuesType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CertificateValuesType) POIXMLTypeLoader.parse(inputStream, CertificateValuesType.type, xmlOptions);
        }

        public static CertificateValuesType parse(Reader reader) throws XmlException, IOException {
            return (CertificateValuesType) POIXMLTypeLoader.parse(reader, CertificateValuesType.type, (XmlOptions) null);
        }

        public static CertificateValuesType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CertificateValuesType) POIXMLTypeLoader.parse(reader, CertificateValuesType.type, xmlOptions);
        }

        public static CertificateValuesType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CertificateValuesType) POIXMLTypeLoader.parse(xMLStreamReader, CertificateValuesType.type, (XmlOptions) null);
        }

        public static CertificateValuesType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CertificateValuesType) POIXMLTypeLoader.parse(xMLStreamReader, CertificateValuesType.type, xmlOptions);
        }

        public static CertificateValuesType parse(Node node) throws XmlException {
            return (CertificateValuesType) POIXMLTypeLoader.parse(node, CertificateValuesType.type, (XmlOptions) null);
        }

        public static CertificateValuesType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CertificateValuesType) POIXMLTypeLoader.parse(node, CertificateValuesType.type, xmlOptions);
        }

        public static CertificateValuesType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CertificateValuesType) POIXMLTypeLoader.parse(xMLInputStream, CertificateValuesType.type, (XmlOptions) null);
        }

        public static CertificateValuesType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CertificateValuesType) POIXMLTypeLoader.parse(xMLInputStream, CertificateValuesType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CertificateValuesType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CertificateValuesType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    List<EncapsulatedPKIDataType> getEncapsulatedX509CertificateList();

    EncapsulatedPKIDataType[] getEncapsulatedX509CertificateArray();

    EncapsulatedPKIDataType getEncapsulatedX509CertificateArray(int i);

    int sizeOfEncapsulatedX509CertificateArray();

    void setEncapsulatedX509CertificateArray(EncapsulatedPKIDataType[] encapsulatedPKIDataTypeArr);

    void setEncapsulatedX509CertificateArray(int i, EncapsulatedPKIDataType encapsulatedPKIDataType);

    EncapsulatedPKIDataType insertNewEncapsulatedX509Certificate(int i);

    EncapsulatedPKIDataType addNewEncapsulatedX509Certificate();

    void removeEncapsulatedX509Certificate(int i);

    List<AnyType> getOtherCertificateList();

    AnyType[] getOtherCertificateArray();

    AnyType getOtherCertificateArray(int i);

    int sizeOfOtherCertificateArray();

    void setOtherCertificateArray(AnyType[] anyTypeArr);

    void setOtherCertificateArray(int i, AnyType anyType);

    AnyType insertNewOtherCertificate(int i);

    AnyType addNewOtherCertificate();

    void removeOtherCertificate(int i);

    String getId();

    XmlID xgetId();

    boolean isSetId();

    void setId(String str);

    void xsetId(XmlID xmlID);

    void unsetId();
}
