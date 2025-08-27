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
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/SignaturePolicyIdentifierType.class */
public interface SignaturePolicyIdentifierType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(SignaturePolicyIdentifierType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("signaturepolicyidentifiertype80aftype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/SignaturePolicyIdentifierType$Factory.class */
    public static final class Factory {
        public static SignaturePolicyIdentifierType newInstance() {
            return (SignaturePolicyIdentifierType) POIXMLTypeLoader.newInstance(SignaturePolicyIdentifierType.type, null);
        }

        public static SignaturePolicyIdentifierType newInstance(XmlOptions xmlOptions) {
            return (SignaturePolicyIdentifierType) POIXMLTypeLoader.newInstance(SignaturePolicyIdentifierType.type, xmlOptions);
        }

        public static SignaturePolicyIdentifierType parse(String str) throws XmlException {
            return (SignaturePolicyIdentifierType) POIXMLTypeLoader.parse(str, SignaturePolicyIdentifierType.type, (XmlOptions) null);
        }

        public static SignaturePolicyIdentifierType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (SignaturePolicyIdentifierType) POIXMLTypeLoader.parse(str, SignaturePolicyIdentifierType.type, xmlOptions);
        }

        public static SignaturePolicyIdentifierType parse(File file) throws XmlException, IOException {
            return (SignaturePolicyIdentifierType) POIXMLTypeLoader.parse(file, SignaturePolicyIdentifierType.type, (XmlOptions) null);
        }

        public static SignaturePolicyIdentifierType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (SignaturePolicyIdentifierType) POIXMLTypeLoader.parse(file, SignaturePolicyIdentifierType.type, xmlOptions);
        }

        public static SignaturePolicyIdentifierType parse(URL url) throws XmlException, IOException {
            return (SignaturePolicyIdentifierType) POIXMLTypeLoader.parse(url, SignaturePolicyIdentifierType.type, (XmlOptions) null);
        }

        public static SignaturePolicyIdentifierType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (SignaturePolicyIdentifierType) POIXMLTypeLoader.parse(url, SignaturePolicyIdentifierType.type, xmlOptions);
        }

        public static SignaturePolicyIdentifierType parse(InputStream inputStream) throws XmlException, IOException {
            return (SignaturePolicyIdentifierType) POIXMLTypeLoader.parse(inputStream, SignaturePolicyIdentifierType.type, (XmlOptions) null);
        }

        public static SignaturePolicyIdentifierType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (SignaturePolicyIdentifierType) POIXMLTypeLoader.parse(inputStream, SignaturePolicyIdentifierType.type, xmlOptions);
        }

        public static SignaturePolicyIdentifierType parse(Reader reader) throws XmlException, IOException {
            return (SignaturePolicyIdentifierType) POIXMLTypeLoader.parse(reader, SignaturePolicyIdentifierType.type, (XmlOptions) null);
        }

        public static SignaturePolicyIdentifierType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (SignaturePolicyIdentifierType) POIXMLTypeLoader.parse(reader, SignaturePolicyIdentifierType.type, xmlOptions);
        }

        public static SignaturePolicyIdentifierType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (SignaturePolicyIdentifierType) POIXMLTypeLoader.parse(xMLStreamReader, SignaturePolicyIdentifierType.type, (XmlOptions) null);
        }

        public static SignaturePolicyIdentifierType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (SignaturePolicyIdentifierType) POIXMLTypeLoader.parse(xMLStreamReader, SignaturePolicyIdentifierType.type, xmlOptions);
        }

        public static SignaturePolicyIdentifierType parse(Node node) throws XmlException {
            return (SignaturePolicyIdentifierType) POIXMLTypeLoader.parse(node, SignaturePolicyIdentifierType.type, (XmlOptions) null);
        }

        public static SignaturePolicyIdentifierType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (SignaturePolicyIdentifierType) POIXMLTypeLoader.parse(node, SignaturePolicyIdentifierType.type, xmlOptions);
        }

        public static SignaturePolicyIdentifierType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (SignaturePolicyIdentifierType) POIXMLTypeLoader.parse(xMLInputStream, SignaturePolicyIdentifierType.type, (XmlOptions) null);
        }

        public static SignaturePolicyIdentifierType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (SignaturePolicyIdentifierType) POIXMLTypeLoader.parse(xMLInputStream, SignaturePolicyIdentifierType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, SignaturePolicyIdentifierType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, SignaturePolicyIdentifierType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    SignaturePolicyIdType getSignaturePolicyId();

    boolean isSetSignaturePolicyId();

    void setSignaturePolicyId(SignaturePolicyIdType signaturePolicyIdType);

    SignaturePolicyIdType addNewSignaturePolicyId();

    void unsetSignaturePolicyId();

    XmlObject getSignaturePolicyImplied();

    boolean isSetSignaturePolicyImplied();

    void setSignaturePolicyImplied(XmlObject xmlObject);

    XmlObject addNewSignaturePolicyImplied();

    void unsetSignaturePolicyImplied();
}
