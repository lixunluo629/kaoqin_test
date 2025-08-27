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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/TopLevelSimpleType.class */
public interface TopLevelSimpleType extends SimpleType {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(TopLevelSimpleType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("toplevelsimpletypec958type");

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    String getName();

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    XmlNCName xgetName();

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    boolean isSetName();

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    void setName(String str);

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    void xsetName(XmlNCName xmlNCName);

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SimpleType
    void unsetName();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/TopLevelSimpleType$Factory.class */
    public static final class Factory {
        public static TopLevelSimpleType newInstance() {
            return (TopLevelSimpleType) XmlBeans.getContextTypeLoader().newInstance(TopLevelSimpleType.type, null);
        }

        public static TopLevelSimpleType newInstance(XmlOptions options) {
            return (TopLevelSimpleType) XmlBeans.getContextTypeLoader().newInstance(TopLevelSimpleType.type, options);
        }

        public static TopLevelSimpleType parse(String xmlAsString) throws XmlException {
            return (TopLevelSimpleType) XmlBeans.getContextTypeLoader().parse(xmlAsString, TopLevelSimpleType.type, (XmlOptions) null);
        }

        public static TopLevelSimpleType parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (TopLevelSimpleType) XmlBeans.getContextTypeLoader().parse(xmlAsString, TopLevelSimpleType.type, options);
        }

        public static TopLevelSimpleType parse(File file) throws XmlException, IOException {
            return (TopLevelSimpleType) XmlBeans.getContextTypeLoader().parse(file, TopLevelSimpleType.type, (XmlOptions) null);
        }

        public static TopLevelSimpleType parse(File file, XmlOptions options) throws XmlException, IOException {
            return (TopLevelSimpleType) XmlBeans.getContextTypeLoader().parse(file, TopLevelSimpleType.type, options);
        }

        public static TopLevelSimpleType parse(URL u) throws XmlException, IOException {
            return (TopLevelSimpleType) XmlBeans.getContextTypeLoader().parse(u, TopLevelSimpleType.type, (XmlOptions) null);
        }

        public static TopLevelSimpleType parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (TopLevelSimpleType) XmlBeans.getContextTypeLoader().parse(u, TopLevelSimpleType.type, options);
        }

        public static TopLevelSimpleType parse(InputStream is) throws XmlException, IOException {
            return (TopLevelSimpleType) XmlBeans.getContextTypeLoader().parse(is, TopLevelSimpleType.type, (XmlOptions) null);
        }

        public static TopLevelSimpleType parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (TopLevelSimpleType) XmlBeans.getContextTypeLoader().parse(is, TopLevelSimpleType.type, options);
        }

        public static TopLevelSimpleType parse(Reader r) throws XmlException, IOException {
            return (TopLevelSimpleType) XmlBeans.getContextTypeLoader().parse(r, TopLevelSimpleType.type, (XmlOptions) null);
        }

        public static TopLevelSimpleType parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (TopLevelSimpleType) XmlBeans.getContextTypeLoader().parse(r, TopLevelSimpleType.type, options);
        }

        public static TopLevelSimpleType parse(XMLStreamReader sr) throws XmlException {
            return (TopLevelSimpleType) XmlBeans.getContextTypeLoader().parse(sr, TopLevelSimpleType.type, (XmlOptions) null);
        }

        public static TopLevelSimpleType parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (TopLevelSimpleType) XmlBeans.getContextTypeLoader().parse(sr, TopLevelSimpleType.type, options);
        }

        public static TopLevelSimpleType parse(Node node) throws XmlException {
            return (TopLevelSimpleType) XmlBeans.getContextTypeLoader().parse(node, TopLevelSimpleType.type, (XmlOptions) null);
        }

        public static TopLevelSimpleType parse(Node node, XmlOptions options) throws XmlException {
            return (TopLevelSimpleType) XmlBeans.getContextTypeLoader().parse(node, TopLevelSimpleType.type, options);
        }

        public static TopLevelSimpleType parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (TopLevelSimpleType) XmlBeans.getContextTypeLoader().parse(xis, TopLevelSimpleType.type, (XmlOptions) null);
        }

        public static TopLevelSimpleType parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (TopLevelSimpleType) XmlBeans.getContextTypeLoader().parse(xis, TopLevelSimpleType.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, TopLevelSimpleType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, TopLevelSimpleType.type, options);
        }

        private Factory() {
        }
    }
}
