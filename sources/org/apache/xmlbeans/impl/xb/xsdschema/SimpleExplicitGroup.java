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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/SimpleExplicitGroup.class */
public interface SimpleExplicitGroup extends ExplicitGroup {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(SimpleExplicitGroup.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("simpleexplicitgroup428ctype");

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/SimpleExplicitGroup$Factory.class */
    public static final class Factory {
        public static SimpleExplicitGroup newInstance() {
            return (SimpleExplicitGroup) XmlBeans.getContextTypeLoader().newInstance(SimpleExplicitGroup.type, null);
        }

        public static SimpleExplicitGroup newInstance(XmlOptions options) {
            return (SimpleExplicitGroup) XmlBeans.getContextTypeLoader().newInstance(SimpleExplicitGroup.type, options);
        }

        public static SimpleExplicitGroup parse(String xmlAsString) throws XmlException {
            return (SimpleExplicitGroup) XmlBeans.getContextTypeLoader().parse(xmlAsString, SimpleExplicitGroup.type, (XmlOptions) null);
        }

        public static SimpleExplicitGroup parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (SimpleExplicitGroup) XmlBeans.getContextTypeLoader().parse(xmlAsString, SimpleExplicitGroup.type, options);
        }

        public static SimpleExplicitGroup parse(File file) throws XmlException, IOException {
            return (SimpleExplicitGroup) XmlBeans.getContextTypeLoader().parse(file, SimpleExplicitGroup.type, (XmlOptions) null);
        }

        public static SimpleExplicitGroup parse(File file, XmlOptions options) throws XmlException, IOException {
            return (SimpleExplicitGroup) XmlBeans.getContextTypeLoader().parse(file, SimpleExplicitGroup.type, options);
        }

        public static SimpleExplicitGroup parse(URL u) throws XmlException, IOException {
            return (SimpleExplicitGroup) XmlBeans.getContextTypeLoader().parse(u, SimpleExplicitGroup.type, (XmlOptions) null);
        }

        public static SimpleExplicitGroup parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (SimpleExplicitGroup) XmlBeans.getContextTypeLoader().parse(u, SimpleExplicitGroup.type, options);
        }

        public static SimpleExplicitGroup parse(InputStream is) throws XmlException, IOException {
            return (SimpleExplicitGroup) XmlBeans.getContextTypeLoader().parse(is, SimpleExplicitGroup.type, (XmlOptions) null);
        }

        public static SimpleExplicitGroup parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (SimpleExplicitGroup) XmlBeans.getContextTypeLoader().parse(is, SimpleExplicitGroup.type, options);
        }

        public static SimpleExplicitGroup parse(Reader r) throws XmlException, IOException {
            return (SimpleExplicitGroup) XmlBeans.getContextTypeLoader().parse(r, SimpleExplicitGroup.type, (XmlOptions) null);
        }

        public static SimpleExplicitGroup parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (SimpleExplicitGroup) XmlBeans.getContextTypeLoader().parse(r, SimpleExplicitGroup.type, options);
        }

        public static SimpleExplicitGroup parse(XMLStreamReader sr) throws XmlException {
            return (SimpleExplicitGroup) XmlBeans.getContextTypeLoader().parse(sr, SimpleExplicitGroup.type, (XmlOptions) null);
        }

        public static SimpleExplicitGroup parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (SimpleExplicitGroup) XmlBeans.getContextTypeLoader().parse(sr, SimpleExplicitGroup.type, options);
        }

        public static SimpleExplicitGroup parse(Node node) throws XmlException {
            return (SimpleExplicitGroup) XmlBeans.getContextTypeLoader().parse(node, SimpleExplicitGroup.type, (XmlOptions) null);
        }

        public static SimpleExplicitGroup parse(Node node, XmlOptions options) throws XmlException {
            return (SimpleExplicitGroup) XmlBeans.getContextTypeLoader().parse(node, SimpleExplicitGroup.type, options);
        }

        public static SimpleExplicitGroup parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (SimpleExplicitGroup) XmlBeans.getContextTypeLoader().parse(xis, SimpleExplicitGroup.type, (XmlOptions) null);
        }

        public static SimpleExplicitGroup parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (SimpleExplicitGroup) XmlBeans.getContextTypeLoader().parse(xis, SimpleExplicitGroup.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, SimpleExplicitGroup.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, SimpleExplicitGroup.type, options);
        }

        private Factory() {
        }
    }
}
