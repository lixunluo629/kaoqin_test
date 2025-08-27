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
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/GroupDocument.class */
public interface GroupDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(GroupDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("group6eb6doctype");

    NamedGroup getGroup();

    void setGroup(NamedGroup namedGroup);

    NamedGroup addNewGroup();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/GroupDocument$Factory.class */
    public static final class Factory {
        public static GroupDocument newInstance() {
            return (GroupDocument) XmlBeans.getContextTypeLoader().newInstance(GroupDocument.type, null);
        }

        public static GroupDocument newInstance(XmlOptions options) {
            return (GroupDocument) XmlBeans.getContextTypeLoader().newInstance(GroupDocument.type, options);
        }

        public static GroupDocument parse(String xmlAsString) throws XmlException {
            return (GroupDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, GroupDocument.type, (XmlOptions) null);
        }

        public static GroupDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (GroupDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, GroupDocument.type, options);
        }

        public static GroupDocument parse(File file) throws XmlException, IOException {
            return (GroupDocument) XmlBeans.getContextTypeLoader().parse(file, GroupDocument.type, (XmlOptions) null);
        }

        public static GroupDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (GroupDocument) XmlBeans.getContextTypeLoader().parse(file, GroupDocument.type, options);
        }

        public static GroupDocument parse(URL u) throws XmlException, IOException {
            return (GroupDocument) XmlBeans.getContextTypeLoader().parse(u, GroupDocument.type, (XmlOptions) null);
        }

        public static GroupDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (GroupDocument) XmlBeans.getContextTypeLoader().parse(u, GroupDocument.type, options);
        }

        public static GroupDocument parse(InputStream is) throws XmlException, IOException {
            return (GroupDocument) XmlBeans.getContextTypeLoader().parse(is, GroupDocument.type, (XmlOptions) null);
        }

        public static GroupDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (GroupDocument) XmlBeans.getContextTypeLoader().parse(is, GroupDocument.type, options);
        }

        public static GroupDocument parse(Reader r) throws XmlException, IOException {
            return (GroupDocument) XmlBeans.getContextTypeLoader().parse(r, GroupDocument.type, (XmlOptions) null);
        }

        public static GroupDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (GroupDocument) XmlBeans.getContextTypeLoader().parse(r, GroupDocument.type, options);
        }

        public static GroupDocument parse(XMLStreamReader sr) throws XmlException {
            return (GroupDocument) XmlBeans.getContextTypeLoader().parse(sr, GroupDocument.type, (XmlOptions) null);
        }

        public static GroupDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (GroupDocument) XmlBeans.getContextTypeLoader().parse(sr, GroupDocument.type, options);
        }

        public static GroupDocument parse(Node node) throws XmlException {
            return (GroupDocument) XmlBeans.getContextTypeLoader().parse(node, GroupDocument.type, (XmlOptions) null);
        }

        public static GroupDocument parse(Node node, XmlOptions options) throws XmlException {
            return (GroupDocument) XmlBeans.getContextTypeLoader().parse(node, GroupDocument.type, options);
        }

        public static GroupDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (GroupDocument) XmlBeans.getContextTypeLoader().parse(xis, GroupDocument.type, (XmlOptions) null);
        }

        public static GroupDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (GroupDocument) XmlBeans.getContextTypeLoader().parse(xis, GroupDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, GroupDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, GroupDocument.type, options);
        }

        private Factory() {
        }
    }
}
