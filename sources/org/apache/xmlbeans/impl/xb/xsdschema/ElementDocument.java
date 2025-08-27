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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/ElementDocument.class */
public interface ElementDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(ElementDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("element7f99doctype");

    TopLevelElement getElement();

    void setElement(TopLevelElement topLevelElement);

    TopLevelElement addNewElement();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/ElementDocument$Factory.class */
    public static final class Factory {
        public static ElementDocument newInstance() {
            return (ElementDocument) XmlBeans.getContextTypeLoader().newInstance(ElementDocument.type, null);
        }

        public static ElementDocument newInstance(XmlOptions options) {
            return (ElementDocument) XmlBeans.getContextTypeLoader().newInstance(ElementDocument.type, options);
        }

        public static ElementDocument parse(String xmlAsString) throws XmlException {
            return (ElementDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, ElementDocument.type, (XmlOptions) null);
        }

        public static ElementDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (ElementDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, ElementDocument.type, options);
        }

        public static ElementDocument parse(File file) throws XmlException, IOException {
            return (ElementDocument) XmlBeans.getContextTypeLoader().parse(file, ElementDocument.type, (XmlOptions) null);
        }

        public static ElementDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (ElementDocument) XmlBeans.getContextTypeLoader().parse(file, ElementDocument.type, options);
        }

        public static ElementDocument parse(URL u) throws XmlException, IOException {
            return (ElementDocument) XmlBeans.getContextTypeLoader().parse(u, ElementDocument.type, (XmlOptions) null);
        }

        public static ElementDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (ElementDocument) XmlBeans.getContextTypeLoader().parse(u, ElementDocument.type, options);
        }

        public static ElementDocument parse(InputStream is) throws XmlException, IOException {
            return (ElementDocument) XmlBeans.getContextTypeLoader().parse(is, ElementDocument.type, (XmlOptions) null);
        }

        public static ElementDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (ElementDocument) XmlBeans.getContextTypeLoader().parse(is, ElementDocument.type, options);
        }

        public static ElementDocument parse(Reader r) throws XmlException, IOException {
            return (ElementDocument) XmlBeans.getContextTypeLoader().parse(r, ElementDocument.type, (XmlOptions) null);
        }

        public static ElementDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (ElementDocument) XmlBeans.getContextTypeLoader().parse(r, ElementDocument.type, options);
        }

        public static ElementDocument parse(XMLStreamReader sr) throws XmlException {
            return (ElementDocument) XmlBeans.getContextTypeLoader().parse(sr, ElementDocument.type, (XmlOptions) null);
        }

        public static ElementDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (ElementDocument) XmlBeans.getContextTypeLoader().parse(sr, ElementDocument.type, options);
        }

        public static ElementDocument parse(Node node) throws XmlException {
            return (ElementDocument) XmlBeans.getContextTypeLoader().parse(node, ElementDocument.type, (XmlOptions) null);
        }

        public static ElementDocument parse(Node node, XmlOptions options) throws XmlException {
            return (ElementDocument) XmlBeans.getContextTypeLoader().parse(node, ElementDocument.type, options);
        }

        public static ElementDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (ElementDocument) XmlBeans.getContextTypeLoader().parse(xis, ElementDocument.type, (XmlOptions) null);
        }

        public static ElementDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (ElementDocument) XmlBeans.getContextTypeLoader().parse(xis, ElementDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, ElementDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, ElementDocument.type, options);
        }

        private Factory() {
        }
    }
}
