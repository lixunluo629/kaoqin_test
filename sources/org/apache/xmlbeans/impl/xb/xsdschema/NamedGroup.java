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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/NamedGroup.class */
public interface NamedGroup extends RealGroup {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(NamedGroup.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("namedgroup878dtype");

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    String getName();

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    XmlNCName xgetName();

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    boolean isSetName();

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    void setName(String str);

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    void xsetName(XmlNCName xmlNCName);

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    void unsetName();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/NamedGroup$All.class */
    public interface All extends org.apache.xmlbeans.impl.xb.xsdschema.All {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(All.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("all82daelemtype");

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/NamedGroup$All$Factory.class */
        public static final class Factory {
            public static All newInstance() {
                return (All) XmlBeans.getContextTypeLoader().newInstance(All.type, null);
            }

            public static All newInstance(XmlOptions options) {
                return (All) XmlBeans.getContextTypeLoader().newInstance(All.type, options);
            }

            private Factory() {
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/NamedGroup$Factory.class */
    public static final class Factory {
        public static NamedGroup newInstance() {
            return (NamedGroup) XmlBeans.getContextTypeLoader().newInstance(NamedGroup.type, null);
        }

        public static NamedGroup newInstance(XmlOptions options) {
            return (NamedGroup) XmlBeans.getContextTypeLoader().newInstance(NamedGroup.type, options);
        }

        public static NamedGroup parse(String xmlAsString) throws XmlException {
            return (NamedGroup) XmlBeans.getContextTypeLoader().parse(xmlAsString, NamedGroup.type, (XmlOptions) null);
        }

        public static NamedGroup parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (NamedGroup) XmlBeans.getContextTypeLoader().parse(xmlAsString, NamedGroup.type, options);
        }

        public static NamedGroup parse(File file) throws XmlException, IOException {
            return (NamedGroup) XmlBeans.getContextTypeLoader().parse(file, NamedGroup.type, (XmlOptions) null);
        }

        public static NamedGroup parse(File file, XmlOptions options) throws XmlException, IOException {
            return (NamedGroup) XmlBeans.getContextTypeLoader().parse(file, NamedGroup.type, options);
        }

        public static NamedGroup parse(URL u) throws XmlException, IOException {
            return (NamedGroup) XmlBeans.getContextTypeLoader().parse(u, NamedGroup.type, (XmlOptions) null);
        }

        public static NamedGroup parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (NamedGroup) XmlBeans.getContextTypeLoader().parse(u, NamedGroup.type, options);
        }

        public static NamedGroup parse(InputStream is) throws XmlException, IOException {
            return (NamedGroup) XmlBeans.getContextTypeLoader().parse(is, NamedGroup.type, (XmlOptions) null);
        }

        public static NamedGroup parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (NamedGroup) XmlBeans.getContextTypeLoader().parse(is, NamedGroup.type, options);
        }

        public static NamedGroup parse(Reader r) throws XmlException, IOException {
            return (NamedGroup) XmlBeans.getContextTypeLoader().parse(r, NamedGroup.type, (XmlOptions) null);
        }

        public static NamedGroup parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (NamedGroup) XmlBeans.getContextTypeLoader().parse(r, NamedGroup.type, options);
        }

        public static NamedGroup parse(XMLStreamReader sr) throws XmlException {
            return (NamedGroup) XmlBeans.getContextTypeLoader().parse(sr, NamedGroup.type, (XmlOptions) null);
        }

        public static NamedGroup parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (NamedGroup) XmlBeans.getContextTypeLoader().parse(sr, NamedGroup.type, options);
        }

        public static NamedGroup parse(Node node) throws XmlException {
            return (NamedGroup) XmlBeans.getContextTypeLoader().parse(node, NamedGroup.type, (XmlOptions) null);
        }

        public static NamedGroup parse(Node node, XmlOptions options) throws XmlException {
            return (NamedGroup) XmlBeans.getContextTypeLoader().parse(node, NamedGroup.type, options);
        }

        public static NamedGroup parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (NamedGroup) XmlBeans.getContextTypeLoader().parse(xis, NamedGroup.type, (XmlOptions) null);
        }

        public static NamedGroup parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (NamedGroup) XmlBeans.getContextTypeLoader().parse(xis, NamedGroup.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, NamedGroup.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, NamedGroup.type, options);
        }

        private Factory() {
        }
    }
}
