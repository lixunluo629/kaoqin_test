package org.apache.xmlbeans.impl.xb.substwsdl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/substwsdl/TImport.class */
public interface TImport extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(TImport.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLTOOLS").resolveHandle("timport22datype");

    String getNamespace();

    XmlAnyURI xgetNamespace();

    void setNamespace(String str);

    void xsetNamespace(XmlAnyURI xmlAnyURI);

    String getLocation();

    XmlAnyURI xgetLocation();

    void setLocation(String str);

    void xsetLocation(XmlAnyURI xmlAnyURI);

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/substwsdl/TImport$Factory.class */
    public static final class Factory {
        public static TImport newInstance() {
            return (TImport) XmlBeans.getContextTypeLoader().newInstance(TImport.type, null);
        }

        public static TImport newInstance(XmlOptions options) {
            return (TImport) XmlBeans.getContextTypeLoader().newInstance(TImport.type, options);
        }

        public static TImport parse(String xmlAsString) throws XmlException {
            return (TImport) XmlBeans.getContextTypeLoader().parse(xmlAsString, TImport.type, (XmlOptions) null);
        }

        public static TImport parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (TImport) XmlBeans.getContextTypeLoader().parse(xmlAsString, TImport.type, options);
        }

        public static TImport parse(File file) throws XmlException, IOException {
            return (TImport) XmlBeans.getContextTypeLoader().parse(file, TImport.type, (XmlOptions) null);
        }

        public static TImport parse(File file, XmlOptions options) throws XmlException, IOException {
            return (TImport) XmlBeans.getContextTypeLoader().parse(file, TImport.type, options);
        }

        public static TImport parse(URL u) throws XmlException, IOException {
            return (TImport) XmlBeans.getContextTypeLoader().parse(u, TImport.type, (XmlOptions) null);
        }

        public static TImport parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (TImport) XmlBeans.getContextTypeLoader().parse(u, TImport.type, options);
        }

        public static TImport parse(InputStream is) throws XmlException, IOException {
            return (TImport) XmlBeans.getContextTypeLoader().parse(is, TImport.type, (XmlOptions) null);
        }

        public static TImport parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (TImport) XmlBeans.getContextTypeLoader().parse(is, TImport.type, options);
        }

        public static TImport parse(Reader r) throws XmlException, IOException {
            return (TImport) XmlBeans.getContextTypeLoader().parse(r, TImport.type, (XmlOptions) null);
        }

        public static TImport parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (TImport) XmlBeans.getContextTypeLoader().parse(r, TImport.type, options);
        }

        public static TImport parse(XMLStreamReader sr) throws XmlException {
            return (TImport) XmlBeans.getContextTypeLoader().parse(sr, TImport.type, (XmlOptions) null);
        }

        public static TImport parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (TImport) XmlBeans.getContextTypeLoader().parse(sr, TImport.type, options);
        }

        public static TImport parse(Node node) throws XmlException {
            return (TImport) XmlBeans.getContextTypeLoader().parse(node, TImport.type, (XmlOptions) null);
        }

        public static TImport parse(Node node, XmlOptions options) throws XmlException {
            return (TImport) XmlBeans.getContextTypeLoader().parse(node, TImport.type, options);
        }

        public static TImport parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (TImport) XmlBeans.getContextTypeLoader().parse(xis, TImport.type, (XmlOptions) null);
        }

        public static TImport parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (TImport) XmlBeans.getContextTypeLoader().parse(xis, TImport.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, TImport.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, TImport.type, options);
        }

        private Factory() {
        }
    }
}
