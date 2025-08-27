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
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlQName;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/ListDocument.class */
public interface ListDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(ListDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("listcde5doctype");

    List getList();

    void setList(List list);

    List addNewList();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/ListDocument$List.class */
    public interface List extends Annotated {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(List.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("list391felemtype");

        LocalSimpleType getSimpleType();

        boolean isSetSimpleType();

        void setSimpleType(LocalSimpleType localSimpleType);

        LocalSimpleType addNewSimpleType();

        void unsetSimpleType();

        QName getItemType();

        XmlQName xgetItemType();

        boolean isSetItemType();

        void setItemType(QName qName);

        void xsetItemType(XmlQName xmlQName);

        void unsetItemType();

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/ListDocument$List$Factory.class */
        public static final class Factory {
            public static List newInstance() {
                return (List) XmlBeans.getContextTypeLoader().newInstance(List.type, null);
            }

            public static List newInstance(XmlOptions options) {
                return (List) XmlBeans.getContextTypeLoader().newInstance(List.type, options);
            }

            private Factory() {
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/ListDocument$Factory.class */
    public static final class Factory {
        public static ListDocument newInstance() {
            return (ListDocument) XmlBeans.getContextTypeLoader().newInstance(ListDocument.type, null);
        }

        public static ListDocument newInstance(XmlOptions options) {
            return (ListDocument) XmlBeans.getContextTypeLoader().newInstance(ListDocument.type, options);
        }

        public static ListDocument parse(String xmlAsString) throws XmlException {
            return (ListDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, ListDocument.type, (XmlOptions) null);
        }

        public static ListDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (ListDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, ListDocument.type, options);
        }

        public static ListDocument parse(File file) throws XmlException, IOException {
            return (ListDocument) XmlBeans.getContextTypeLoader().parse(file, ListDocument.type, (XmlOptions) null);
        }

        public static ListDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (ListDocument) XmlBeans.getContextTypeLoader().parse(file, ListDocument.type, options);
        }

        public static ListDocument parse(URL u) throws XmlException, IOException {
            return (ListDocument) XmlBeans.getContextTypeLoader().parse(u, ListDocument.type, (XmlOptions) null);
        }

        public static ListDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (ListDocument) XmlBeans.getContextTypeLoader().parse(u, ListDocument.type, options);
        }

        public static ListDocument parse(InputStream is) throws XmlException, IOException {
            return (ListDocument) XmlBeans.getContextTypeLoader().parse(is, ListDocument.type, (XmlOptions) null);
        }

        public static ListDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (ListDocument) XmlBeans.getContextTypeLoader().parse(is, ListDocument.type, options);
        }

        public static ListDocument parse(Reader r) throws XmlException, IOException {
            return (ListDocument) XmlBeans.getContextTypeLoader().parse(r, ListDocument.type, (XmlOptions) null);
        }

        public static ListDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (ListDocument) XmlBeans.getContextTypeLoader().parse(r, ListDocument.type, options);
        }

        public static ListDocument parse(XMLStreamReader sr) throws XmlException {
            return (ListDocument) XmlBeans.getContextTypeLoader().parse(sr, ListDocument.type, (XmlOptions) null);
        }

        public static ListDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (ListDocument) XmlBeans.getContextTypeLoader().parse(sr, ListDocument.type, options);
        }

        public static ListDocument parse(Node node) throws XmlException {
            return (ListDocument) XmlBeans.getContextTypeLoader().parse(node, ListDocument.type, (XmlOptions) null);
        }

        public static ListDocument parse(Node node, XmlOptions options) throws XmlException {
            return (ListDocument) XmlBeans.getContextTypeLoader().parse(node, ListDocument.type, options);
        }

        public static ListDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (ListDocument) XmlBeans.getContextTypeLoader().parse(xis, ListDocument.type, (XmlOptions) null);
        }

        public static ListDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (ListDocument) XmlBeans.getContextTypeLoader().parse(xis, ListDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, ListDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, ListDocument.type, options);
        }

        private Factory() {
        }
    }
}
