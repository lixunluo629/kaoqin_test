package org.etsi.uri.x01903.v13;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Calendar;
import javax.xml.stream.XMLStreamReader;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlDateTime;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlID;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/SignedSignaturePropertiesType.class */
public interface SignedSignaturePropertiesType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(SignedSignaturePropertiesType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("signedsignaturepropertiestype06abtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/SignedSignaturePropertiesType$Factory.class */
    public static final class Factory {
        public static SignedSignaturePropertiesType newInstance() {
            return (SignedSignaturePropertiesType) POIXMLTypeLoader.newInstance(SignedSignaturePropertiesType.type, null);
        }

        public static SignedSignaturePropertiesType newInstance(XmlOptions xmlOptions) {
            return (SignedSignaturePropertiesType) POIXMLTypeLoader.newInstance(SignedSignaturePropertiesType.type, xmlOptions);
        }

        public static SignedSignaturePropertiesType parse(String str) throws XmlException {
            return (SignedSignaturePropertiesType) POIXMLTypeLoader.parse(str, SignedSignaturePropertiesType.type, (XmlOptions) null);
        }

        public static SignedSignaturePropertiesType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (SignedSignaturePropertiesType) POIXMLTypeLoader.parse(str, SignedSignaturePropertiesType.type, xmlOptions);
        }

        public static SignedSignaturePropertiesType parse(File file) throws XmlException, IOException {
            return (SignedSignaturePropertiesType) POIXMLTypeLoader.parse(file, SignedSignaturePropertiesType.type, (XmlOptions) null);
        }

        public static SignedSignaturePropertiesType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (SignedSignaturePropertiesType) POIXMLTypeLoader.parse(file, SignedSignaturePropertiesType.type, xmlOptions);
        }

        public static SignedSignaturePropertiesType parse(URL url) throws XmlException, IOException {
            return (SignedSignaturePropertiesType) POIXMLTypeLoader.parse(url, SignedSignaturePropertiesType.type, (XmlOptions) null);
        }

        public static SignedSignaturePropertiesType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (SignedSignaturePropertiesType) POIXMLTypeLoader.parse(url, SignedSignaturePropertiesType.type, xmlOptions);
        }

        public static SignedSignaturePropertiesType parse(InputStream inputStream) throws XmlException, IOException {
            return (SignedSignaturePropertiesType) POIXMLTypeLoader.parse(inputStream, SignedSignaturePropertiesType.type, (XmlOptions) null);
        }

        public static SignedSignaturePropertiesType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (SignedSignaturePropertiesType) POIXMLTypeLoader.parse(inputStream, SignedSignaturePropertiesType.type, xmlOptions);
        }

        public static SignedSignaturePropertiesType parse(Reader reader) throws XmlException, IOException {
            return (SignedSignaturePropertiesType) POIXMLTypeLoader.parse(reader, SignedSignaturePropertiesType.type, (XmlOptions) null);
        }

        public static SignedSignaturePropertiesType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (SignedSignaturePropertiesType) POIXMLTypeLoader.parse(reader, SignedSignaturePropertiesType.type, xmlOptions);
        }

        public static SignedSignaturePropertiesType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (SignedSignaturePropertiesType) POIXMLTypeLoader.parse(xMLStreamReader, SignedSignaturePropertiesType.type, (XmlOptions) null);
        }

        public static SignedSignaturePropertiesType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (SignedSignaturePropertiesType) POIXMLTypeLoader.parse(xMLStreamReader, SignedSignaturePropertiesType.type, xmlOptions);
        }

        public static SignedSignaturePropertiesType parse(Node node) throws XmlException {
            return (SignedSignaturePropertiesType) POIXMLTypeLoader.parse(node, SignedSignaturePropertiesType.type, (XmlOptions) null);
        }

        public static SignedSignaturePropertiesType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (SignedSignaturePropertiesType) POIXMLTypeLoader.parse(node, SignedSignaturePropertiesType.type, xmlOptions);
        }

        public static SignedSignaturePropertiesType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (SignedSignaturePropertiesType) POIXMLTypeLoader.parse(xMLInputStream, SignedSignaturePropertiesType.type, (XmlOptions) null);
        }

        public static SignedSignaturePropertiesType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (SignedSignaturePropertiesType) POIXMLTypeLoader.parse(xMLInputStream, SignedSignaturePropertiesType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, SignedSignaturePropertiesType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, SignedSignaturePropertiesType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    Calendar getSigningTime();

    XmlDateTime xgetSigningTime();

    boolean isSetSigningTime();

    void setSigningTime(Calendar calendar);

    void xsetSigningTime(XmlDateTime xmlDateTime);

    void unsetSigningTime();

    CertIDListType getSigningCertificate();

    boolean isSetSigningCertificate();

    void setSigningCertificate(CertIDListType certIDListType);

    CertIDListType addNewSigningCertificate();

    void unsetSigningCertificate();

    SignaturePolicyIdentifierType getSignaturePolicyIdentifier();

    boolean isSetSignaturePolicyIdentifier();

    void setSignaturePolicyIdentifier(SignaturePolicyIdentifierType signaturePolicyIdentifierType);

    SignaturePolicyIdentifierType addNewSignaturePolicyIdentifier();

    void unsetSignaturePolicyIdentifier();

    SignatureProductionPlaceType getSignatureProductionPlace();

    boolean isSetSignatureProductionPlace();

    void setSignatureProductionPlace(SignatureProductionPlaceType signatureProductionPlaceType);

    SignatureProductionPlaceType addNewSignatureProductionPlace();

    void unsetSignatureProductionPlace();

    SignerRoleType getSignerRole();

    boolean isSetSignerRole();

    void setSignerRole(SignerRoleType signerRoleType);

    SignerRoleType addNewSignerRole();

    void unsetSignerRole();

    String getId();

    XmlID xgetId();

    boolean isSetId();

    void setId(String str);

    void xsetId(XmlID xmlID);

    void unsetId();
}
