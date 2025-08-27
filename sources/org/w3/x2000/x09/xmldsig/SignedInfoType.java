package org.w3.x2000.x09.xmldsig;

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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/w3/x2000/x09/xmldsig/SignedInfoType.class */
public interface SignedInfoType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(SignedInfoType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("signedinfotype54dbtype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/w3/x2000/x09/xmldsig/SignedInfoType$Factory.class */
    public static final class Factory {
        public static SignedInfoType newInstance() {
            return (SignedInfoType) POIXMLTypeLoader.newInstance(SignedInfoType.type, null);
        }

        public static SignedInfoType newInstance(XmlOptions xmlOptions) {
            return (SignedInfoType) POIXMLTypeLoader.newInstance(SignedInfoType.type, xmlOptions);
        }

        public static SignedInfoType parse(String str) throws XmlException {
            return (SignedInfoType) POIXMLTypeLoader.parse(str, SignedInfoType.type, (XmlOptions) null);
        }

        public static SignedInfoType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (SignedInfoType) POIXMLTypeLoader.parse(str, SignedInfoType.type, xmlOptions);
        }

        public static SignedInfoType parse(File file) throws XmlException, IOException {
            return (SignedInfoType) POIXMLTypeLoader.parse(file, SignedInfoType.type, (XmlOptions) null);
        }

        public static SignedInfoType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (SignedInfoType) POIXMLTypeLoader.parse(file, SignedInfoType.type, xmlOptions);
        }

        public static SignedInfoType parse(URL url) throws XmlException, IOException {
            return (SignedInfoType) POIXMLTypeLoader.parse(url, SignedInfoType.type, (XmlOptions) null);
        }

        public static SignedInfoType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (SignedInfoType) POIXMLTypeLoader.parse(url, SignedInfoType.type, xmlOptions);
        }

        public static SignedInfoType parse(InputStream inputStream) throws XmlException, IOException {
            return (SignedInfoType) POIXMLTypeLoader.parse(inputStream, SignedInfoType.type, (XmlOptions) null);
        }

        public static SignedInfoType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (SignedInfoType) POIXMLTypeLoader.parse(inputStream, SignedInfoType.type, xmlOptions);
        }

        public static SignedInfoType parse(Reader reader) throws XmlException, IOException {
            return (SignedInfoType) POIXMLTypeLoader.parse(reader, SignedInfoType.type, (XmlOptions) null);
        }

        public static SignedInfoType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (SignedInfoType) POIXMLTypeLoader.parse(reader, SignedInfoType.type, xmlOptions);
        }

        public static SignedInfoType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (SignedInfoType) POIXMLTypeLoader.parse(xMLStreamReader, SignedInfoType.type, (XmlOptions) null);
        }

        public static SignedInfoType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (SignedInfoType) POIXMLTypeLoader.parse(xMLStreamReader, SignedInfoType.type, xmlOptions);
        }

        public static SignedInfoType parse(Node node) throws XmlException {
            return (SignedInfoType) POIXMLTypeLoader.parse(node, SignedInfoType.type, (XmlOptions) null);
        }

        public static SignedInfoType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (SignedInfoType) POIXMLTypeLoader.parse(node, SignedInfoType.type, xmlOptions);
        }

        public static SignedInfoType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (SignedInfoType) POIXMLTypeLoader.parse(xMLInputStream, SignedInfoType.type, (XmlOptions) null);
        }

        public static SignedInfoType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (SignedInfoType) POIXMLTypeLoader.parse(xMLInputStream, SignedInfoType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, SignedInfoType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, SignedInfoType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CanonicalizationMethodType getCanonicalizationMethod();

    void setCanonicalizationMethod(CanonicalizationMethodType canonicalizationMethodType);

    CanonicalizationMethodType addNewCanonicalizationMethod();

    SignatureMethodType getSignatureMethod();

    void setSignatureMethod(SignatureMethodType signatureMethodType);

    SignatureMethodType addNewSignatureMethod();

    List<ReferenceType> getReferenceList();

    ReferenceType[] getReferenceArray();

    ReferenceType getReferenceArray(int i);

    int sizeOfReferenceArray();

    void setReferenceArray(ReferenceType[] referenceTypeArr);

    void setReferenceArray(int i, ReferenceType referenceType);

    ReferenceType insertNewReference(int i);

    ReferenceType addNewReference();

    void removeReference(int i);

    String getId();

    XmlID xgetId();

    boolean isSetId();

    void setId(String str);

    void xsetId(XmlID xmlID);

    void unsetId();
}
