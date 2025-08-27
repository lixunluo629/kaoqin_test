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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/ComplexTypeDocument.class */
public interface ComplexTypeDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(ComplexTypeDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("complextype83cbdoctype");

    TopLevelComplexType getComplexType();

    void setComplexType(TopLevelComplexType topLevelComplexType);

    TopLevelComplexType addNewComplexType();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/ComplexTypeDocument$Factory.class */
    public static final class Factory {
        public static ComplexTypeDocument newInstance() {
            return (ComplexTypeDocument) XmlBeans.getContextTypeLoader().newInstance(ComplexTypeDocument.type, null);
        }

        public static ComplexTypeDocument newInstance(XmlOptions options) {
            return (ComplexTypeDocument) XmlBeans.getContextTypeLoader().newInstance(ComplexTypeDocument.type, options);
        }

        public static ComplexTypeDocument parse(String xmlAsString) throws XmlException {
            return (ComplexTypeDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, ComplexTypeDocument.type, (XmlOptions) null);
        }

        public static ComplexTypeDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (ComplexTypeDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, ComplexTypeDocument.type, options);
        }

        public static ComplexTypeDocument parse(File file) throws XmlException, IOException {
            return (ComplexTypeDocument) XmlBeans.getContextTypeLoader().parse(file, ComplexTypeDocument.type, (XmlOptions) null);
        }

        public static ComplexTypeDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (ComplexTypeDocument) XmlBeans.getContextTypeLoader().parse(file, ComplexTypeDocument.type, options);
        }

        public static ComplexTypeDocument parse(URL u) throws XmlException, IOException {
            return (ComplexTypeDocument) XmlBeans.getContextTypeLoader().parse(u, ComplexTypeDocument.type, (XmlOptions) null);
        }

        public static ComplexTypeDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (ComplexTypeDocument) XmlBeans.getContextTypeLoader().parse(u, ComplexTypeDocument.type, options);
        }

        public static ComplexTypeDocument parse(InputStream is) throws XmlException, IOException {
            return (ComplexTypeDocument) XmlBeans.getContextTypeLoader().parse(is, ComplexTypeDocument.type, (XmlOptions) null);
        }

        public static ComplexTypeDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (ComplexTypeDocument) XmlBeans.getContextTypeLoader().parse(is, ComplexTypeDocument.type, options);
        }

        public static ComplexTypeDocument parse(Reader r) throws XmlException, IOException {
            return (ComplexTypeDocument) XmlBeans.getContextTypeLoader().parse(r, ComplexTypeDocument.type, (XmlOptions) null);
        }

        public static ComplexTypeDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (ComplexTypeDocument) XmlBeans.getContextTypeLoader().parse(r, ComplexTypeDocument.type, options);
        }

        public static ComplexTypeDocument parse(XMLStreamReader sr) throws XmlException {
            return (ComplexTypeDocument) XmlBeans.getContextTypeLoader().parse(sr, ComplexTypeDocument.type, (XmlOptions) null);
        }

        public static ComplexTypeDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (ComplexTypeDocument) XmlBeans.getContextTypeLoader().parse(sr, ComplexTypeDocument.type, options);
        }

        public static ComplexTypeDocument parse(Node node) throws XmlException {
            return (ComplexTypeDocument) XmlBeans.getContextTypeLoader().parse(node, ComplexTypeDocument.type, (XmlOptions) null);
        }

        public static ComplexTypeDocument parse(Node node, XmlOptions options) throws XmlException {
            return (ComplexTypeDocument) XmlBeans.getContextTypeLoader().parse(node, ComplexTypeDocument.type, options);
        }

        public static ComplexTypeDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (ComplexTypeDocument) XmlBeans.getContextTypeLoader().parse(xis, ComplexTypeDocument.type, (XmlOptions) null);
        }

        public static ComplexTypeDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (ComplexTypeDocument) XmlBeans.getContextTypeLoader().parse(xis, ComplexTypeDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, ComplexTypeDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, ComplexTypeDocument.type, options);
        }

        private Factory() {
        }
    }
}
