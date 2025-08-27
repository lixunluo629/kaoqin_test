package org.apache.xmlbeans.impl.xb.xsdschema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlQName;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/GroupRef.class */
public interface GroupRef extends RealGroup {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(GroupRef.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("groupref303ftype");

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    QName getRef();

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    XmlQName xgetRef();

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    boolean isSetRef();

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    void setRef(QName qName);

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    void xsetRef(XmlQName xmlQName);

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Group
    void unsetRef();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/GroupRef$Factory.class */
    public static final class Factory {
        public static GroupRef newInstance() {
            return (GroupRef) XmlBeans.getContextTypeLoader().newInstance(GroupRef.type, null);
        }

        public static GroupRef newInstance(XmlOptions options) {
            return (GroupRef) XmlBeans.getContextTypeLoader().newInstance(GroupRef.type, options);
        }

        public static GroupRef parse(String xmlAsString) throws XmlException {
            return (GroupRef) XmlBeans.getContextTypeLoader().parse(xmlAsString, GroupRef.type, (XmlOptions) null);
        }

        public static GroupRef parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (GroupRef) XmlBeans.getContextTypeLoader().parse(xmlAsString, GroupRef.type, options);
        }

        public static GroupRef parse(File file) throws XmlException, IOException {
            return (GroupRef) XmlBeans.getContextTypeLoader().parse(file, GroupRef.type, (XmlOptions) null);
        }

        public static GroupRef parse(File file, XmlOptions options) throws XmlException, IOException {
            return (GroupRef) XmlBeans.getContextTypeLoader().parse(file, GroupRef.type, options);
        }

        public static GroupRef parse(URL u) throws XmlException, IOException {
            return (GroupRef) XmlBeans.getContextTypeLoader().parse(u, GroupRef.type, (XmlOptions) null);
        }

        public static GroupRef parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (GroupRef) XmlBeans.getContextTypeLoader().parse(u, GroupRef.type, options);
        }

        public static GroupRef parse(InputStream is) throws XmlException, IOException {
            return (GroupRef) XmlBeans.getContextTypeLoader().parse(is, GroupRef.type, (XmlOptions) null);
        }

        public static GroupRef parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (GroupRef) XmlBeans.getContextTypeLoader().parse(is, GroupRef.type, options);
        }

        public static GroupRef parse(Reader r) throws XmlException, IOException {
            return (GroupRef) XmlBeans.getContextTypeLoader().parse(r, GroupRef.type, (XmlOptions) null);
        }

        public static GroupRef parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (GroupRef) XmlBeans.getContextTypeLoader().parse(r, GroupRef.type, options);
        }

        public static GroupRef parse(XMLStreamReader sr) throws XmlException {
            return (GroupRef) XmlBeans.getContextTypeLoader().parse(sr, GroupRef.type, (XmlOptions) null);
        }

        public static GroupRef parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (GroupRef) XmlBeans.getContextTypeLoader().parse(sr, GroupRef.type, options);
        }

        public static GroupRef parse(Node node) throws XmlException {
            return (GroupRef) XmlBeans.getContextTypeLoader().parse(node, GroupRef.type, (XmlOptions) null);
        }

        public static GroupRef parse(Node node, XmlOptions options) throws XmlException {
            return (GroupRef) XmlBeans.getContextTypeLoader().parse(node, GroupRef.type, options);
        }

        public static GroupRef parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (GroupRef) XmlBeans.getContextTypeLoader().parse(xis, GroupRef.type, (XmlOptions) null);
        }

        public static GroupRef parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (GroupRef) XmlBeans.getContextTypeLoader().parse(xis, GroupRef.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, GroupRef.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, GroupRef.type, options);
        }

        private Factory() {
        }
    }
}
