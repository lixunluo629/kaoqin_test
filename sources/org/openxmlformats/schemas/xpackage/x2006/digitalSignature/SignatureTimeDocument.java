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

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/xpackage/x2006/digitalSignature/SignatureTimeDocument.class */
public interface SignatureTimeDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(SignatureTimeDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3F193EE11A2F798ACF65489B9E6078").resolveHandle("signaturetime9c91doctype");

    /* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/xpackage/x2006/digitalSignature/SignatureTimeDocument$Factory.class */
    public static final class Factory {
        public static SignatureTimeDocument newInstance() {
            return (SignatureTimeDocument) POIXMLTypeLoader.newInstance(SignatureTimeDocument.type, null);
        }

        public static SignatureTimeDocument newInstance(XmlOptions xmlOptions) {
            return (SignatureTimeDocument) POIXMLTypeLoader.newInstance(SignatureTimeDocument.type, xmlOptions);
        }

        public static SignatureTimeDocument parse(String str) throws XmlException {
            return (SignatureTimeDocument) POIXMLTypeLoader.parse(str, SignatureTimeDocument.type, (XmlOptions) null);
        }

        public static SignatureTimeDocument parse(String str, XmlOptions xmlOptions) throws XmlException {
            return (SignatureTimeDocument) POIXMLTypeLoader.parse(str, SignatureTimeDocument.type, xmlOptions);
        }

        public static SignatureTimeDocument parse(File file) throws XmlException, IOException {
            return (SignatureTimeDocument) POIXMLTypeLoader.parse(file, SignatureTimeDocument.type, (XmlOptions) null);
        }

        public static SignatureTimeDocument parse(File file, XmlOptions xmlOptions) throws XmlException, IOException {
            return (SignatureTimeDocument) POIXMLTypeLoader.parse(file, SignatureTimeDocument.type, xmlOptions);
        }

        public static SignatureTimeDocument parse(URL url) throws XmlException, IOException {
            return (SignatureTimeDocument) POIXMLTypeLoader.parse(url, SignatureTimeDocument.type, (XmlOptions) null);
        }

        public static SignatureTimeDocument parse(URL url, XmlOptions xmlOptions) throws XmlException, IOException {
            return (SignatureTimeDocument) POIXMLTypeLoader.parse(url, SignatureTimeDocument.type, xmlOptions);
        }

        public static SignatureTimeDocument parse(InputStream inputStream) throws XmlException, IOException {
            return (SignatureTimeDocument) POIXMLTypeLoader.parse(inputStream, SignatureTimeDocument.type, (XmlOptions) null);
        }

        public static SignatureTimeDocument parse(InputStream inputStream, XmlOptions xmlOptions) throws XmlException, IOException {
            return (SignatureTimeDocument) POIXMLTypeLoader.parse(inputStream, SignatureTimeDocument.type, xmlOptions);
        }

        public static SignatureTimeDocument parse(Reader reader) throws XmlException, IOException {
            return (SignatureTimeDocument) POIXMLTypeLoader.parse(reader, SignatureTimeDocument.type, (XmlOptions) null);
        }

        public static SignatureTimeDocument parse(Reader reader, XmlOptions xmlOptions) throws XmlException, IOException {
            return (SignatureTimeDocument) POIXMLTypeLoader.parse(reader, SignatureTimeDocument.type, xmlOptions);
        }

        public static SignatureTimeDocument parse(XMLStreamReader xMLStreamReader) throws XmlException {
            return (SignatureTimeDocument) POIXMLTypeLoader.parse(xMLStreamReader, SignatureTimeDocument.type, (XmlOptions) null);
        }

        public static SignatureTimeDocument parse(XMLStreamReader xMLStreamReader, XmlOptions xmlOptions) throws XmlException {
            return (SignatureTimeDocument) POIXMLTypeLoader.parse(xMLStreamReader, SignatureTimeDocument.type, xmlOptions);
        }

        public static SignatureTimeDocument parse(Node node) throws XmlException {
            return (SignatureTimeDocument) POIXMLTypeLoader.parse(node, SignatureTimeDocument.type, (XmlOptions) null);
        }

        public static SignatureTimeDocument parse(Node node, XmlOptions xmlOptions) throws XmlException {
            return (SignatureTimeDocument) POIXMLTypeLoader.parse(node, SignatureTimeDocument.type, xmlOptions);
        }

        public static SignatureTimeDocument parse(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return (SignatureTimeDocument) POIXMLTypeLoader.parse(xMLInputStream, SignatureTimeDocument.type, (XmlOptions) null);
        }

        public static SignatureTimeDocument parse(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return (SignatureTimeDocument) POIXMLTypeLoader.parse(xMLInputStream, SignatureTimeDocument.type, xmlOptions);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, SignatureTimeDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xMLInputStream, XmlOptions xmlOptions) throws XMLStreamException, XmlException {
            return POIXMLTypeLoader.newValidatingXMLInputStream(xMLInputStream, SignatureTimeDocument.type, xmlOptions);
        }

        private Factory() {
        }
    }

    CTSignatureTime getSignatureTime();

    void setSignatureTime(CTSignatureTime cTSignatureTime);

    CTSignatureTime addNewSignatureTime();
}
