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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/CompleteRevocationRefsType.class */
public interface CompleteRevocationRefsType extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(CompleteRevocationRefsType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("completerevocationrefstyped8a5type");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/etsi/uri/x01903/v13/CompleteRevocationRefsType$Factory.class */
    public static final class Factory {
        public static CompleteRevocationRefsType newInstance() {
            return (CompleteRevocationRefsType) POIXMLTypeLoader.newInstance(CompleteRevocationRefsType.type, null);
        }

        public static CompleteRevocationRefsType newInstance(XmlOptions xmlOptions) {
            return (CompleteRevocationRefsType) POIXMLTypeLoader.newInstance(CompleteRevocationRefsType.type, xmlOptions);
        }

        public static CompleteRevocationRefsType parse(String str) throws XmlException {
            return (CompleteRevocationRefsType) POIXMLTypeLoader.parse(str, CompleteRevocationRefsType.type, (XmlOptions) null);
        }

        public static CompleteRevocationRefsType parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (CompleteRevocationRefsType) POIXMLTypeLoader.parse(str, CompleteRevocationRefsType.type, xmlOptions);
        }

        public static CompleteRevocationRefsType parse(File file) throws XmlException, IOException {
            return (CompleteRevocationRefsType) POIXMLTypeLoader.parse(file, CompleteRevocationRefsType.type, (XmlOptions) null);
        }

        public static CompleteRevocationRefsType parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CompleteRevocationRefsType) POIXMLTypeLoader.parse(file, CompleteRevocationRefsType.type, xmlOptions);
        }

        public static CompleteRevocationRefsType parse(URL url) throws XmlException, IOException {
            return (CompleteRevocationRefsType) POIXMLTypeLoader.parse(url, CompleteRevocationRefsType.type, (XmlOptions) null);
        }

        public static CompleteRevocationRefsType parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CompleteRevocationRefsType) POIXMLTypeLoader.parse(url, CompleteRevocationRefsType.type, xmlOptions);
        }

        public static CompleteRevocationRefsType parse(InputStream inputStream) throws XmlException, IOException {
            return (CompleteRevocationRefsType) POIXMLTypeLoader.parse(inputStream, CompleteRevocationRefsType.type, (XmlOptions) null);
        }

        public static CompleteRevocationRefsType parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CompleteRevocationRefsType) POIXMLTypeLoader.parse(inputStream, CompleteRevocationRefsType.type, xmlOptions);
        }

        public static CompleteRevocationRefsType parse(Reader reader) throws XmlException, IOException {
            return (CompleteRevocationRefsType) POIXMLTypeLoader.parse(reader, CompleteRevocationRefsType.type, (XmlOptions) null);
        }

        public static CompleteRevocationRefsType parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (CompleteRevocationRefsType) POIXMLTypeLoader.parse(reader, CompleteRevocationRefsType.type, xmlOptions);
        }

        public static CompleteRevocationRefsType parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (CompleteRevocationRefsType) POIXMLTypeLoader.parse(xMLStreamReader, CompleteRevocationRefsType.type, (XmlOptions) null);
        }

        public static CompleteRevocationRefsType parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (CompleteRevocationRefsType) POIXMLTypeLoader.parse(xMLStreamReader, CompleteRevocationRefsType.type, xmlOptions);
        }

        public static CompleteRevocationRefsType parse(Node node) throws XmlException {
            return (CompleteRevocationRefsType) POIXMLTypeLoader.parse(node, CompleteRevocationRefsType.type, (XmlOptions) null);
        }

        public static CompleteRevocationRefsType parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (CompleteRevocationRefsType) POIXMLTypeLoader.parse(node, CompleteRevocationRefsType.type, xmlOptions);
        }

        public static CompleteRevocationRefsType parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (CompleteRevocationRefsType) POIXMLTypeLoader.parse(xMLInputStream, CompleteRevocationRefsType.type, (XmlOptions) null);
        }

        public static CompleteRevocationRefsType parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (CompleteRevocationRefsType) POIXMLTypeLoader.parse(xMLInputStream, CompleteRevocationRefsType.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CompleteRevocationRefsType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, CompleteRevocationRefsType.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CRLRefsType getCRLRefs();

    boolean isSetCRLRefs();

    void setCRLRefs(CRLRefsType cRLRefsType);

    CRLRefsType addNewCRLRefs();

    void unsetCRLRefs();

    OCSPRefsType getOCSPRefs();

    boolean isSetOCSPRefs();

    void setOCSPRefs(OCSPRefsType oCSPRefsType);

    OCSPRefsType addNewOCSPRefs();

    void unsetOCSPRefs();

    OtherCertStatusRefsType getOtherRefs();

    boolean isSetOtherRefs();

    void setOtherRefs(OtherCertStatusRefsType otherCertStatusRefsType);

    OtherCertStatusRefsType addNewOtherRefs();

    void unsetOtherRefs();

    String getId();

    XmlID xgetId();

    boolean isSetId();

    void setId(String str);

    void xsetId(XmlID xmlID);

    void unsetId();
}
