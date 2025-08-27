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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/NamedAttributeGroup.class */
public interface NamedAttributeGroup extends AttributeGroup {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(NamedAttributeGroup.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("namedattributegroup2e29type");

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.AttributeGroup
    String getName();

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.AttributeGroup
    XmlNCName xgetName();

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.AttributeGroup
    boolean isSetName();

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.AttributeGroup
    void setName(String str);

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.AttributeGroup
    void xsetName(XmlNCName xmlNCName);

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.AttributeGroup
    void unsetName();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/NamedAttributeGroup$Factory.class */
    public static final class Factory {
        public static NamedAttributeGroup newInstance() {
            return (NamedAttributeGroup) XmlBeans.getContextTypeLoader().newInstance(NamedAttributeGroup.type, null);
        }

        public static NamedAttributeGroup newInstance(XmlOptions options) {
            return (NamedAttributeGroup) XmlBeans.getContextTypeLoader().newInstance(NamedAttributeGroup.type, options);
        }

        public static NamedAttributeGroup parse(String xmlAsString) throws XmlException {
            return (NamedAttributeGroup) XmlBeans.getContextTypeLoader().parse(xmlAsString, NamedAttributeGroup.type, (XmlOptions) null);
        }

        public static NamedAttributeGroup parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (NamedAttributeGroup) XmlBeans.getContextTypeLoader().parse(xmlAsString, NamedAttributeGroup.type, options);
        }

        public static NamedAttributeGroup parse(File file) throws XmlException, IOException {
            return (NamedAttributeGroup) XmlBeans.getContextTypeLoader().parse(file, NamedAttributeGroup.type, (XmlOptions) null);
        }

        public static NamedAttributeGroup parse(File file, XmlOptions options) throws XmlException, IOException {
            return (NamedAttributeGroup) XmlBeans.getContextTypeLoader().parse(file, NamedAttributeGroup.type, options);
        }

        public static NamedAttributeGroup parse(URL u) throws XmlException, IOException {
            return (NamedAttributeGroup) XmlBeans.getContextTypeLoader().parse(u, NamedAttributeGroup.type, (XmlOptions) null);
        }

        public static NamedAttributeGroup parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (NamedAttributeGroup) XmlBeans.getContextTypeLoader().parse(u, NamedAttributeGroup.type, options);
        }

        public static NamedAttributeGroup parse(InputStream is) throws XmlException, IOException {
            return (NamedAttributeGroup) XmlBeans.getContextTypeLoader().parse(is, NamedAttributeGroup.type, (XmlOptions) null);
        }

        public static NamedAttributeGroup parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (NamedAttributeGroup) XmlBeans.getContextTypeLoader().parse(is, NamedAttributeGroup.type, options);
        }

        public static NamedAttributeGroup parse(Reader r) throws XmlException, IOException {
            return (NamedAttributeGroup) XmlBeans.getContextTypeLoader().parse(r, NamedAttributeGroup.type, (XmlOptions) null);
        }

        public static NamedAttributeGroup parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (NamedAttributeGroup) XmlBeans.getContextTypeLoader().parse(r, NamedAttributeGroup.type, options);
        }

        public static NamedAttributeGroup parse(XMLStreamReader sr) throws XmlException {
            return (NamedAttributeGroup) XmlBeans.getContextTypeLoader().parse(sr, NamedAttributeGroup.type, (XmlOptions) null);
        }

        public static NamedAttributeGroup parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (NamedAttributeGroup) XmlBeans.getContextTypeLoader().parse(sr, NamedAttributeGroup.type, options);
        }

        public static NamedAttributeGroup parse(Node node) throws XmlException {
            return (NamedAttributeGroup) XmlBeans.getContextTypeLoader().parse(node, NamedAttributeGroup.type, (XmlOptions) null);
        }

        public static NamedAttributeGroup parse(Node node, XmlOptions options) throws XmlException {
            return (NamedAttributeGroup) XmlBeans.getContextTypeLoader().parse(node, NamedAttributeGroup.type, options);
        }

        public static NamedAttributeGroup parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (NamedAttributeGroup) XmlBeans.getContextTypeLoader().parse(xis, NamedAttributeGroup.type, (XmlOptions) null);
        }

        public static NamedAttributeGroup parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (NamedAttributeGroup) XmlBeans.getContextTypeLoader().parse(xis, NamedAttributeGroup.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, NamedAttributeGroup.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, NamedAttributeGroup.type, options);
        }

        private Factory() {
        }
    }
}
