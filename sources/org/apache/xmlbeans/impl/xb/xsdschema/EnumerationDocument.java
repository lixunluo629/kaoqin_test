package org.apache.xmlbeans.impl.xb.xsdschema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/EnumerationDocument.class */
public interface EnumerationDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(EnumerationDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("enumeration052edoctype");

    NoFixedFacet getEnumeration();

    void setEnumeration(NoFixedFacet noFixedFacet);

    NoFixedFacet addNewEnumeration();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/EnumerationDocument$Factory.class */
    public static final class Factory {
        public static EnumerationDocument newInstance() {
            return (EnumerationDocument) XmlBeans.getContextTypeLoader().newInstance(EnumerationDocument.type, null);
        }

        public static EnumerationDocument newInstance(XmlOptions options) {
            return (EnumerationDocument) XmlBeans.getContextTypeLoader().newInstance(EnumerationDocument.type, options);
        }

        public static EnumerationDocument parse(String xmlAsString) throws XmlException {
            return (EnumerationDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, EnumerationDocument.type, (XmlOptions) null);
        }

        public static EnumerationDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (EnumerationDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, EnumerationDocument.type, options);
        }

        public static EnumerationDocument parse(File file) throws XmlException, IOException {
            return (EnumerationDocument) XmlBeans.getContextTypeLoader().parse(file, EnumerationDocument.type, (XmlOptions) null);
        }

        public static EnumerationDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (EnumerationDocument) XmlBeans.getContextTypeLoader().parse(file, EnumerationDocument.type, options);
        }

        public static EnumerationDocument parse(URL u) throws XmlException, IOException {
            return (EnumerationDocument) XmlBeans.getContextTypeLoader().parse(u, EnumerationDocument.type, (XmlOptions) null);
        }

        public static EnumerationDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (EnumerationDocument) XmlBeans.getContextTypeLoader().parse(u, EnumerationDocument.type, options);
        }

        public static EnumerationDocument parse(InputStream is) throws XmlException, IOException {
            return (EnumerationDocument) XmlBeans.getContextTypeLoader().parse(is, EnumerationDocument.type, (XmlOptions) null);
        }

        public static EnumerationDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (EnumerationDocument) XmlBeans.getContextTypeLoader().parse(is, EnumerationDocument.type, options);
        }

        public static EnumerationDocument parse(Reader r) throws XmlException, IOException {
            return (EnumerationDocument) XmlBeans.getContextTypeLoader().parse(r, EnumerationDocument.type, (XmlOptions) null);
        }

        public static EnumerationDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (EnumerationDocument) XmlBeans.getContextTypeLoader().parse(r, EnumerationDocument.type, options);
        }

        public static EnumerationDocument parse(XMLStreamReader sr) throws XmlException {
            return (EnumerationDocument) XmlBeans.getContextTypeLoader().parse(sr, EnumerationDocument.type, (XmlOptions) null);
        }

        public static EnumerationDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (EnumerationDocument) XmlBeans.getContextTypeLoader().parse(sr, EnumerationDocument.type, options);
        }

        public static EnumerationDocument parse(Node node) throws XmlException {
            return (EnumerationDocument) XmlBeans.getContextTypeLoader().parse(node, EnumerationDocument.type, (XmlOptions) null);
        }

        public static EnumerationDocument parse(Node node, XmlOptions options) throws XmlException {
            return (EnumerationDocument) XmlBeans.getContextTypeLoader().parse(node, EnumerationDocument.type, options);
        }

        public static EnumerationDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (EnumerationDocument) XmlBeans.getContextTypeLoader().parse(xis, EnumerationDocument.type, (XmlOptions) null);
        }

        public static EnumerationDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (EnumerationDocument) XmlBeans.getContextTypeLoader().parse(xis, EnumerationDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, EnumerationDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, EnumerationDocument.type, options);
        }

        private Factory() {
        }
    }
}
