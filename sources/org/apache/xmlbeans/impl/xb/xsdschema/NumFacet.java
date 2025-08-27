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
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/NumFacet.class */
public interface NumFacet extends Facet {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(NumFacet.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("numfacet93a2type");

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/NumFacet$Factory.class */
    public static final class Factory {
        public static NumFacet newInstance() {
            return (NumFacet) XmlBeans.getContextTypeLoader().newInstance(NumFacet.type, null);
        }

        public static NumFacet newInstance(XmlOptions options) {
            return (NumFacet) XmlBeans.getContextTypeLoader().newInstance(NumFacet.type, options);
        }

        public static NumFacet parse(String xmlAsString) throws XmlException {
            return (NumFacet) XmlBeans.getContextTypeLoader().parse(xmlAsString, NumFacet.type, (XmlOptions) null);
        }

        public static NumFacet parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (NumFacet) XmlBeans.getContextTypeLoader().parse(xmlAsString, NumFacet.type, options);
        }

        public static NumFacet parse(File file) throws XmlException, IOException {
            return (NumFacet) XmlBeans.getContextTypeLoader().parse(file, NumFacet.type, (XmlOptions) null);
        }

        public static NumFacet parse(File file, XmlOptions options) throws XmlException, IOException {
            return (NumFacet) XmlBeans.getContextTypeLoader().parse(file, NumFacet.type, options);
        }

        public static NumFacet parse(URL u) throws XmlException, IOException {
            return (NumFacet) XmlBeans.getContextTypeLoader().parse(u, NumFacet.type, (XmlOptions) null);
        }

        public static NumFacet parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (NumFacet) XmlBeans.getContextTypeLoader().parse(u, NumFacet.type, options);
        }

        public static NumFacet parse(InputStream is) throws XmlException, IOException {
            return (NumFacet) XmlBeans.getContextTypeLoader().parse(is, NumFacet.type, (XmlOptions) null);
        }

        public static NumFacet parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (NumFacet) XmlBeans.getContextTypeLoader().parse(is, NumFacet.type, options);
        }

        public static NumFacet parse(Reader r) throws XmlException, IOException {
            return (NumFacet) XmlBeans.getContextTypeLoader().parse(r, NumFacet.type, (XmlOptions) null);
        }

        public static NumFacet parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (NumFacet) XmlBeans.getContextTypeLoader().parse(r, NumFacet.type, options);
        }

        public static NumFacet parse(XMLStreamReader sr) throws XmlException {
            return (NumFacet) XmlBeans.getContextTypeLoader().parse(sr, NumFacet.type, (XmlOptions) null);
        }

        public static NumFacet parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (NumFacet) XmlBeans.getContextTypeLoader().parse(sr, NumFacet.type, options);
        }

        public static NumFacet parse(Node node) throws XmlException {
            return (NumFacet) XmlBeans.getContextTypeLoader().parse(node, NumFacet.type, (XmlOptions) null);
        }

        public static NumFacet parse(Node node, XmlOptions options) throws XmlException {
            return (NumFacet) XmlBeans.getContextTypeLoader().parse(node, NumFacet.type, options);
        }

        public static NumFacet parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (NumFacet) XmlBeans.getContextTypeLoader().parse(xis, NumFacet.type, (XmlOptions) null);
        }

        public static NumFacet parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (NumFacet) XmlBeans.getContextTypeLoader().parse(xis, NumFacet.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, NumFacet.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, NumFacet.type, options);
        }

        private Factory() {
        }
    }
}
