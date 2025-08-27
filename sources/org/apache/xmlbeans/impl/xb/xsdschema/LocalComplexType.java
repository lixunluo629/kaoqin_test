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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/LocalComplexType.class */
public interface LocalComplexType extends ComplexType {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(LocalComplexType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("localcomplextype6494type");

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/LocalComplexType$Factory.class */
    public static final class Factory {
        public static LocalComplexType newInstance() {
            return (LocalComplexType) XmlBeans.getContextTypeLoader().newInstance(LocalComplexType.type, null);
        }

        public static LocalComplexType newInstance(XmlOptions options) {
            return (LocalComplexType) XmlBeans.getContextTypeLoader().newInstance(LocalComplexType.type, options);
        }

        public static LocalComplexType parse(String xmlAsString) throws XmlException {
            return (LocalComplexType) XmlBeans.getContextTypeLoader().parse(xmlAsString, LocalComplexType.type, (XmlOptions) null);
        }

        public static LocalComplexType parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (LocalComplexType) XmlBeans.getContextTypeLoader().parse(xmlAsString, LocalComplexType.type, options);
        }

        public static LocalComplexType parse(File file) throws XmlException, IOException {
            return (LocalComplexType) XmlBeans.getContextTypeLoader().parse(file, LocalComplexType.type, (XmlOptions) null);
        }

        public static LocalComplexType parse(File file, XmlOptions options) throws XmlException, IOException {
            return (LocalComplexType) XmlBeans.getContextTypeLoader().parse(file, LocalComplexType.type, options);
        }

        public static LocalComplexType parse(URL u) throws XmlException, IOException {
            return (LocalComplexType) XmlBeans.getContextTypeLoader().parse(u, LocalComplexType.type, (XmlOptions) null);
        }

        public static LocalComplexType parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (LocalComplexType) XmlBeans.getContextTypeLoader().parse(u, LocalComplexType.type, options);
        }

        public static LocalComplexType parse(InputStream is) throws XmlException, IOException {
            return (LocalComplexType) XmlBeans.getContextTypeLoader().parse(is, LocalComplexType.type, (XmlOptions) null);
        }

        public static LocalComplexType parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (LocalComplexType) XmlBeans.getContextTypeLoader().parse(is, LocalComplexType.type, options);
        }

        public static LocalComplexType parse(Reader r) throws XmlException, IOException {
            return (LocalComplexType) XmlBeans.getContextTypeLoader().parse(r, LocalComplexType.type, (XmlOptions) null);
        }

        public static LocalComplexType parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (LocalComplexType) XmlBeans.getContextTypeLoader().parse(r, LocalComplexType.type, options);
        }

        public static LocalComplexType parse(XMLStreamReader sr) throws XmlException {
            return (LocalComplexType) XmlBeans.getContextTypeLoader().parse(sr, LocalComplexType.type, (XmlOptions) null);
        }

        public static LocalComplexType parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (LocalComplexType) XmlBeans.getContextTypeLoader().parse(sr, LocalComplexType.type, options);
        }

        public static LocalComplexType parse(Node node) throws XmlException {
            return (LocalComplexType) XmlBeans.getContextTypeLoader().parse(node, LocalComplexType.type, (XmlOptions) null);
        }

        public static LocalComplexType parse(Node node, XmlOptions options) throws XmlException {
            return (LocalComplexType) XmlBeans.getContextTypeLoader().parse(node, LocalComplexType.type, options);
        }

        public static LocalComplexType parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (LocalComplexType) XmlBeans.getContextTypeLoader().parse(xis, LocalComplexType.type, (XmlOptions) null);
        }

        public static LocalComplexType parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (LocalComplexType) XmlBeans.getContextTypeLoader().parse(xis, LocalComplexType.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, LocalComplexType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, LocalComplexType.type, options);
        }

        private Factory() {
        }
    }
}
