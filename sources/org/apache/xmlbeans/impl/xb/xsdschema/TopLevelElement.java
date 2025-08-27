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
import org.apache.xmlbeans.XmlNCName;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/TopLevelElement.class */
public interface TopLevelElement extends Element {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(TopLevelElement.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("toplevelelement98d8type");

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    String getName();

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    XmlNCName xgetName();

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    boolean isSetName();

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    void setName(String str);

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    void xsetName(XmlNCName xmlNCName);

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Element
    void unsetName();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/TopLevelElement$Factory.class */
    public static final class Factory {
        public static TopLevelElement newInstance() {
            return (TopLevelElement) XmlBeans.getContextTypeLoader().newInstance(TopLevelElement.type, null);
        }

        public static TopLevelElement newInstance(XmlOptions options) {
            return (TopLevelElement) XmlBeans.getContextTypeLoader().newInstance(TopLevelElement.type, options);
        }

        public static TopLevelElement parse(String xmlAsString) throws XmlException {
            return (TopLevelElement) XmlBeans.getContextTypeLoader().parse(xmlAsString, TopLevelElement.type, (XmlOptions) null);
        }

        public static TopLevelElement parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (TopLevelElement) XmlBeans.getContextTypeLoader().parse(xmlAsString, TopLevelElement.type, options);
        }

        public static TopLevelElement parse(File file) throws XmlException, IOException {
            return (TopLevelElement) XmlBeans.getContextTypeLoader().parse(file, TopLevelElement.type, (XmlOptions) null);
        }

        public static TopLevelElement parse(File file, XmlOptions options) throws XmlException, IOException {
            return (TopLevelElement) XmlBeans.getContextTypeLoader().parse(file, TopLevelElement.type, options);
        }

        public static TopLevelElement parse(URL u) throws XmlException, IOException {
            return (TopLevelElement) XmlBeans.getContextTypeLoader().parse(u, TopLevelElement.type, (XmlOptions) null);
        }

        public static TopLevelElement parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (TopLevelElement) XmlBeans.getContextTypeLoader().parse(u, TopLevelElement.type, options);
        }

        public static TopLevelElement parse(InputStream is) throws XmlException, IOException {
            return (TopLevelElement) XmlBeans.getContextTypeLoader().parse(is, TopLevelElement.type, (XmlOptions) null);
        }

        public static TopLevelElement parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (TopLevelElement) XmlBeans.getContextTypeLoader().parse(is, TopLevelElement.type, options);
        }

        public static TopLevelElement parse(Reader r) throws XmlException, IOException {
            return (TopLevelElement) XmlBeans.getContextTypeLoader().parse(r, TopLevelElement.type, (XmlOptions) null);
        }

        public static TopLevelElement parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (TopLevelElement) XmlBeans.getContextTypeLoader().parse(r, TopLevelElement.type, options);
        }

        public static TopLevelElement parse(XMLStreamReader sr) throws XmlException {
            return (TopLevelElement) XmlBeans.getContextTypeLoader().parse(sr, TopLevelElement.type, (XmlOptions) null);
        }

        public static TopLevelElement parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (TopLevelElement) XmlBeans.getContextTypeLoader().parse(sr, TopLevelElement.type, options);
        }

        public static TopLevelElement parse(Node node) throws XmlException {
            return (TopLevelElement) XmlBeans.getContextTypeLoader().parse(node, TopLevelElement.type, (XmlOptions) null);
        }

        public static TopLevelElement parse(Node node, XmlOptions options) throws XmlException {
            return (TopLevelElement) XmlBeans.getContextTypeLoader().parse(node, TopLevelElement.type, options);
        }

        public static TopLevelElement parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (TopLevelElement) XmlBeans.getContextTypeLoader().parse(xis, TopLevelElement.type, (XmlOptions) null);
        }

        public static TopLevelElement parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (TopLevelElement) XmlBeans.getContextTypeLoader().parse(xis, TopLevelElement.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, TopLevelElement.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, TopLevelElement.type, options);
        }

        private Factory() {
        }
    }
}
