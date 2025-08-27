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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/TopLevelComplexType.class */
public interface TopLevelComplexType extends ComplexType {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(TopLevelComplexType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("toplevelcomplextypee58atype");

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    String getName();

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    XmlNCName xgetName();

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    boolean isSetName();

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    void setName(String str);

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    void xsetName(XmlNCName xmlNCName);

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.ComplexType
    void unsetName();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/TopLevelComplexType$Factory.class */
    public static final class Factory {
        public static TopLevelComplexType newInstance() {
            return (TopLevelComplexType) XmlBeans.getContextTypeLoader().newInstance(TopLevelComplexType.type, null);
        }

        public static TopLevelComplexType newInstance(XmlOptions options) {
            return (TopLevelComplexType) XmlBeans.getContextTypeLoader().newInstance(TopLevelComplexType.type, options);
        }

        public static TopLevelComplexType parse(String xmlAsString) throws XmlException {
            return (TopLevelComplexType) XmlBeans.getContextTypeLoader().parse(xmlAsString, TopLevelComplexType.type, (XmlOptions) null);
        }

        public static TopLevelComplexType parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (TopLevelComplexType) XmlBeans.getContextTypeLoader().parse(xmlAsString, TopLevelComplexType.type, options);
        }

        public static TopLevelComplexType parse(File file) throws XmlException, IOException {
            return (TopLevelComplexType) XmlBeans.getContextTypeLoader().parse(file, TopLevelComplexType.type, (XmlOptions) null);
        }

        public static TopLevelComplexType parse(File file, XmlOptions options) throws XmlException, IOException {
            return (TopLevelComplexType) XmlBeans.getContextTypeLoader().parse(file, TopLevelComplexType.type, options);
        }

        public static TopLevelComplexType parse(URL u) throws XmlException, IOException {
            return (TopLevelComplexType) XmlBeans.getContextTypeLoader().parse(u, TopLevelComplexType.type, (XmlOptions) null);
        }

        public static TopLevelComplexType parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (TopLevelComplexType) XmlBeans.getContextTypeLoader().parse(u, TopLevelComplexType.type, options);
        }

        public static TopLevelComplexType parse(InputStream is) throws XmlException, IOException {
            return (TopLevelComplexType) XmlBeans.getContextTypeLoader().parse(is, TopLevelComplexType.type, (XmlOptions) null);
        }

        public static TopLevelComplexType parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (TopLevelComplexType) XmlBeans.getContextTypeLoader().parse(is, TopLevelComplexType.type, options);
        }

        public static TopLevelComplexType parse(Reader r) throws XmlException, IOException {
            return (TopLevelComplexType) XmlBeans.getContextTypeLoader().parse(r, TopLevelComplexType.type, (XmlOptions) null);
        }

        public static TopLevelComplexType parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (TopLevelComplexType) XmlBeans.getContextTypeLoader().parse(r, TopLevelComplexType.type, options);
        }

        public static TopLevelComplexType parse(XMLStreamReader sr) throws XmlException {
            return (TopLevelComplexType) XmlBeans.getContextTypeLoader().parse(sr, TopLevelComplexType.type, (XmlOptions) null);
        }

        public static TopLevelComplexType parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (TopLevelComplexType) XmlBeans.getContextTypeLoader().parse(sr, TopLevelComplexType.type, options);
        }

        public static TopLevelComplexType parse(Node node) throws XmlException {
            return (TopLevelComplexType) XmlBeans.getContextTypeLoader().parse(node, TopLevelComplexType.type, (XmlOptions) null);
        }

        public static TopLevelComplexType parse(Node node, XmlOptions options) throws XmlException {
            return (TopLevelComplexType) XmlBeans.getContextTypeLoader().parse(node, TopLevelComplexType.type, options);
        }

        public static TopLevelComplexType parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (TopLevelComplexType) XmlBeans.getContextTypeLoader().parse(xis, TopLevelComplexType.type, (XmlOptions) null);
        }

        public static TopLevelComplexType parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (TopLevelComplexType) XmlBeans.getContextTypeLoader().parse(xis, TopLevelComplexType.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, TopLevelComplexType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, TopLevelComplexType.type, options);
        }

        private Factory() {
        }
    }
}
