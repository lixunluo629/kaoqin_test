package org.etsi.uri.x01903.v13;

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
import org.apache.xmlbeans.XmlID;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/RevocationValuesType.class */
public interface RevocationValuesType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(RevocationValuesType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("revocationvaluestype9a6etype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/RevocationValuesType$Factory.class */
    public static final class Factory {
        public static RevocationValuesType newInstance() {
            return (RevocationValuesType) POIXMLTypeLoader.newInstance(RevocationValuesType.type, null);
        }

        public static RevocationValuesType newInstance(XmlOptions xmlOptions) {
            return (RevocationValuesType) POIXMLTypeLoader.newInstance(RevocationValuesType.type, xmlOptions);
        }

        public static RevocationValuesType parse(String str) throws XmlException {
            return (RevocationValuesType) POIXMLTypeLoader.parse(str, RevocationValuesType.type, (XmlOptions) null);
        }

        public static RevocationValuesType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (RevocationValuesType) POIXMLTypeLoader.parse(str, RevocationValuesType.type, xmlOptions);
        }

        public static RevocationValuesType parse(File file) throws XmlException, IOException {
            return (RevocationValuesType) POIXMLTypeLoader.parse(file, RevocationValuesType.type, (XmlOptions) null);
        }

        public static RevocationValuesType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (RevocationValuesType) POIXMLTypeLoader.parse(file, RevocationValuesType.type, xmlOptions);
        }

        public static RevocationValuesType parse(URL url) throws XmlException, IOException {
            return (RevocationValuesType) POIXMLTypeLoader.parse(url, RevocationValuesType.type, (XmlOptions) null);
        }

        public static RevocationValuesType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (RevocationValuesType) POIXMLTypeLoader.parse(url, RevocationValuesType.type, xmlOptions);
        }

        public static RevocationValuesType parse(InputStream inputStream) throws XmlException, IOException {
            return (RevocationValuesType) POIXMLTypeLoader.parse(inputStream, RevocationValuesType.type, (XmlOptions) null);
        }

        public static RevocationValuesType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (RevocationValuesType) POIXMLTypeLoader.parse(inputStream, RevocationValuesType.type, xmlOptions);
        }

        public static RevocationValuesType parse(Reader reader) throws XmlException, IOException {
            return (RevocationValuesType) POIXMLTypeLoader.parse(reader, RevocationValuesType.type, (XmlOptions) null);
        }

        public static RevocationValuesType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (RevocationValuesType) POIXMLTypeLoader.parse(reader, RevocationValuesType.type, xmlOptions);
        }

        public static RevocationValuesType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (RevocationValuesType) POIXMLTypeLoader.parse(xMLStreamReader, RevocationValuesType.type, (XmlOptions) null);
        }

        public static RevocationValuesType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (RevocationValuesType) POIXMLTypeLoader.parse(xMLStreamReader, RevocationValuesType.type, xmlOptions);
        }

        public static RevocationValuesType parse(Node node) throws XmlException {
            return (RevocationValuesType) POIXMLTypeLoader.parse(node, RevocationValuesType.type, (XmlOptions) null);
        }

        public static RevocationValuesType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (RevocationValuesType) POIXMLTypeLoader.parse(node, RevocationValuesType.type, xmlOptions);
        }

        public static RevocationValuesType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (RevocationValuesType) POIXMLTypeLoader.parse(xMLInputStream, RevocationValuesType.type, (XmlOptions) null);
        }

        public static RevocationValuesType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (RevocationValuesType) POIXMLTypeLoader.parse(xMLInputStream, RevocationValuesType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, RevocationValuesType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, RevocationValuesType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CRLValuesType getCRLValues();

    boolean isSetCRLValues();

    void setCRLValues(CRLValuesType cRLValuesType);

    CRLValuesType addNewCRLValues();

    void unsetCRLValues();

    OCSPValuesType getOCSPValues();

    boolean isSetOCSPValues();

    void setOCSPValues(OCSPValuesType oCSPValuesType);

    OCSPValuesType addNewOCSPValues();

    void unsetOCSPValues();

    OtherCertStatusValuesType getOtherValues();

    boolean isSetOtherValues();

    void setOtherValues(OtherCertStatusValuesType otherCertStatusValuesType);

    OtherCertStatusValuesType addNewOtherValues();

    void unsetOtherValues();

    String getId();

    XmlID xgetId();

    boolean isSetId();

    void setId(String str);

    void xsetId(XmlID xmlID);

    void unsetId();
}
