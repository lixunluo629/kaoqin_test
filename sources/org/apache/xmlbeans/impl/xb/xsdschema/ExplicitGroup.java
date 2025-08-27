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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/ExplicitGroup.class */
public interface ExplicitGroup extends Group {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(ExplicitGroup.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("explicitgroup4efatype");

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/ExplicitGroup$Factory.class */
    public static final class Factory {
        public static ExplicitGroup newInstance() {
            return (ExplicitGroup) XmlBeans.getContextTypeLoader().newInstance(ExplicitGroup.type, null);
        }

        public static ExplicitGroup newInstance(XmlOptions options) {
            return (ExplicitGroup) XmlBeans.getContextTypeLoader().newInstance(ExplicitGroup.type, options);
        }

        public static ExplicitGroup parse(String xmlAsString) throws XmlException {
            return (ExplicitGroup) XmlBeans.getContextTypeLoader().parse(xmlAsString, ExplicitGroup.type, (XmlOptions) null);
        }

        public static ExplicitGroup parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (ExplicitGroup) XmlBeans.getContextTypeLoader().parse(xmlAsString, ExplicitGroup.type, options);
        }

        public static ExplicitGroup parse(File file) throws XmlException, IOException {
            return (ExplicitGroup) XmlBeans.getContextTypeLoader().parse(file, ExplicitGroup.type, (XmlOptions) null);
        }

        public static ExplicitGroup parse(File file, XmlOptions options) throws XmlException, IOException {
            return (ExplicitGroup) XmlBeans.getContextTypeLoader().parse(file, ExplicitGroup.type, options);
        }

        public static ExplicitGroup parse(URL u) throws XmlException, IOException {
            return (ExplicitGroup) XmlBeans.getContextTypeLoader().parse(u, ExplicitGroup.type, (XmlOptions) null);
        }

        public static ExplicitGroup parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (ExplicitGroup) XmlBeans.getContextTypeLoader().parse(u, ExplicitGroup.type, options);
        }

        public static ExplicitGroup parse(InputStream is) throws XmlException, IOException {
            return (ExplicitGroup) XmlBeans.getContextTypeLoader().parse(is, ExplicitGroup.type, (XmlOptions) null);
        }

        public static ExplicitGroup parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (ExplicitGroup) XmlBeans.getContextTypeLoader().parse(is, ExplicitGroup.type, options);
        }

        public static ExplicitGroup parse(Reader r) throws XmlException, IOException {
            return (ExplicitGroup) XmlBeans.getContextTypeLoader().parse(r, ExplicitGroup.type, (XmlOptions) null);
        }

        public static ExplicitGroup parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (ExplicitGroup) XmlBeans.getContextTypeLoader().parse(r, ExplicitGroup.type, options);
        }

        public static ExplicitGroup parse(XMLStreamReader sr) throws XmlException {
            return (ExplicitGroup) XmlBeans.getContextTypeLoader().parse(sr, ExplicitGroup.type, (XmlOptions) null);
        }

        public static ExplicitGroup parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (ExplicitGroup) XmlBeans.getContextTypeLoader().parse(sr, ExplicitGroup.type, options);
        }

        public static ExplicitGroup parse(Node node) throws XmlException {
            return (ExplicitGroup) XmlBeans.getContextTypeLoader().parse(node, ExplicitGroup.type, (XmlOptions) null);
        }

        public static ExplicitGroup parse(Node node, XmlOptions options) throws XmlException {
            return (ExplicitGroup) XmlBeans.getContextTypeLoader().parse(node, ExplicitGroup.type, options);
        }

        public static ExplicitGroup parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (ExplicitGroup) XmlBeans.getContextTypeLoader().parse(xis, ExplicitGroup.type, (XmlOptions) null);
        }

        public static ExplicitGroup parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (ExplicitGroup) XmlBeans.getContextTypeLoader().parse(xis, ExplicitGroup.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, ExplicitGroup.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, ExplicitGroup.type, options);
        }

        private Factory() {
        }
    }
}
