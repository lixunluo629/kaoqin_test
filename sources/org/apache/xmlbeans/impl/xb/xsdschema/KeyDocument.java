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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/KeyDocument.class */
public interface KeyDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(KeyDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("key5d16doctype");

    Keybase getKey();

    void setKey(Keybase keybase);

    Keybase addNewKey();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/KeyDocument$Factory.class */
    public static final class Factory {
        public static KeyDocument newInstance() {
            return (KeyDocument) XmlBeans.getContextTypeLoader().newInstance(KeyDocument.type, null);
        }

        public static KeyDocument newInstance(XmlOptions options) {
            return (KeyDocument) XmlBeans.getContextTypeLoader().newInstance(KeyDocument.type, options);
        }

        public static KeyDocument parse(String xmlAsString) throws XmlException {
            return (KeyDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, KeyDocument.type, (XmlOptions) null);
        }

        public static KeyDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (KeyDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, KeyDocument.type, options);
        }

        public static KeyDocument parse(File file) throws XmlException, IOException {
            return (KeyDocument) XmlBeans.getContextTypeLoader().parse(file, KeyDocument.type, (XmlOptions) null);
        }

        public static KeyDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (KeyDocument) XmlBeans.getContextTypeLoader().parse(file, KeyDocument.type, options);
        }

        public static KeyDocument parse(URL u) throws XmlException, IOException {
            return (KeyDocument) XmlBeans.getContextTypeLoader().parse(u, KeyDocument.type, (XmlOptions) null);
        }

        public static KeyDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (KeyDocument) XmlBeans.getContextTypeLoader().parse(u, KeyDocument.type, options);
        }

        public static KeyDocument parse(InputStream is) throws XmlException, IOException {
            return (KeyDocument) XmlBeans.getContextTypeLoader().parse(is, KeyDocument.type, (XmlOptions) null);
        }

        public static KeyDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (KeyDocument) XmlBeans.getContextTypeLoader().parse(is, KeyDocument.type, options);
        }

        public static KeyDocument parse(Reader r) throws XmlException, IOException {
            return (KeyDocument) XmlBeans.getContextTypeLoader().parse(r, KeyDocument.type, (XmlOptions) null);
        }

        public static KeyDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (KeyDocument) XmlBeans.getContextTypeLoader().parse(r, KeyDocument.type, options);
        }

        public static KeyDocument parse(XMLStreamReader sr) throws XmlException {
            return (KeyDocument) XmlBeans.getContextTypeLoader().parse(sr, KeyDocument.type, (XmlOptions) null);
        }

        public static KeyDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (KeyDocument) XmlBeans.getContextTypeLoader().parse(sr, KeyDocument.type, options);
        }

        public static KeyDocument parse(Node node) throws XmlException {
            return (KeyDocument) XmlBeans.getContextTypeLoader().parse(node, KeyDocument.type, (XmlOptions) null);
        }

        public static KeyDocument parse(Node node, XmlOptions options) throws XmlException {
            return (KeyDocument) XmlBeans.getContextTypeLoader().parse(node, KeyDocument.type, options);
        }

        public static KeyDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (KeyDocument) XmlBeans.getContextTypeLoader().parse(xis, KeyDocument.type, (XmlOptions) null);
        }

        public static KeyDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (KeyDocument) XmlBeans.getContextTypeLoader().parse(xis, KeyDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, KeyDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, KeyDocument.type, options);
        }

        private Factory() {
        }
    }
}
