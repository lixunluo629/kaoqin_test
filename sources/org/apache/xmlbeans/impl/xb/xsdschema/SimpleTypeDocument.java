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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/SimpleTypeDocument.class */
public interface SimpleTypeDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(SimpleTypeDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("simpletypedef7doctype");

    TopLevelSimpleType getSimpleType();

    void setSimpleType(TopLevelSimpleType topLevelSimpleType);

    TopLevelSimpleType addNewSimpleType();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/SimpleTypeDocument$Factory.class */
    public static final class Factory {
        public static SimpleTypeDocument newInstance() {
            return (SimpleTypeDocument) XmlBeans.getContextTypeLoader().newInstance(SimpleTypeDocument.type, null);
        }

        public static SimpleTypeDocument newInstance(XmlOptions options) {
            return (SimpleTypeDocument) XmlBeans.getContextTypeLoader().newInstance(SimpleTypeDocument.type, options);
        }

        public static SimpleTypeDocument parse(String xmlAsString) throws XmlException {
            return (SimpleTypeDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, SimpleTypeDocument.type, (XmlOptions) null);
        }

        public static SimpleTypeDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (SimpleTypeDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, SimpleTypeDocument.type, options);
        }

        public static SimpleTypeDocument parse(File file) throws XmlException, IOException {
            return (SimpleTypeDocument) XmlBeans.getContextTypeLoader().parse(file, SimpleTypeDocument.type, (XmlOptions) null);
        }

        public static SimpleTypeDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (SimpleTypeDocument) XmlBeans.getContextTypeLoader().parse(file, SimpleTypeDocument.type, options);
        }

        public static SimpleTypeDocument parse(URL u) throws XmlException, IOException {
            return (SimpleTypeDocument) XmlBeans.getContextTypeLoader().parse(u, SimpleTypeDocument.type, (XmlOptions) null);
        }

        public static SimpleTypeDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (SimpleTypeDocument) XmlBeans.getContextTypeLoader().parse(u, SimpleTypeDocument.type, options);
        }

        public static SimpleTypeDocument parse(InputStream is) throws XmlException, IOException {
            return (SimpleTypeDocument) XmlBeans.getContextTypeLoader().parse(is, SimpleTypeDocument.type, (XmlOptions) null);
        }

        public static SimpleTypeDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (SimpleTypeDocument) XmlBeans.getContextTypeLoader().parse(is, SimpleTypeDocument.type, options);
        }

        public static SimpleTypeDocument parse(Reader r) throws XmlException, IOException {
            return (SimpleTypeDocument) XmlBeans.getContextTypeLoader().parse(r, SimpleTypeDocument.type, (XmlOptions) null);
        }

        public static SimpleTypeDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (SimpleTypeDocument) XmlBeans.getContextTypeLoader().parse(r, SimpleTypeDocument.type, options);
        }

        public static SimpleTypeDocument parse(XMLStreamReader sr) throws XmlException {
            return (SimpleTypeDocument) XmlBeans.getContextTypeLoader().parse(sr, SimpleTypeDocument.type, (XmlOptions) null);
        }

        public static SimpleTypeDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (SimpleTypeDocument) XmlBeans.getContextTypeLoader().parse(sr, SimpleTypeDocument.type, options);
        }

        public static SimpleTypeDocument parse(Node node) throws XmlException {
            return (SimpleTypeDocument) XmlBeans.getContextTypeLoader().parse(node, SimpleTypeDocument.type, (XmlOptions) null);
        }

        public static SimpleTypeDocument parse(Node node, XmlOptions options) throws XmlException {
            return (SimpleTypeDocument) XmlBeans.getContextTypeLoader().parse(node, SimpleTypeDocument.type, options);
        }

        public static SimpleTypeDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (SimpleTypeDocument) XmlBeans.getContextTypeLoader().parse(xis, SimpleTypeDocument.type, (XmlOptions) null);
        }

        public static SimpleTypeDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (SimpleTypeDocument) XmlBeans.getContextTypeLoader().parse(xis, SimpleTypeDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, SimpleTypeDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, SimpleTypeDocument.type, options);
        }

        private Factory() {
        }
    }
}
