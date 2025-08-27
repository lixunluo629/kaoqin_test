package org.apache.xmlbeans.impl.xb.xmlschema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlschema/BaseAttribute.class */
public interface BaseAttribute extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(BaseAttribute.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLLANG").resolveHandle("basece23attrtypetype");

    String getBase();

    XmlAnyURI xgetBase();

    boolean isSetBase();

    void setBase(String str);

    void xsetBase(XmlAnyURI xmlAnyURI);

    void unsetBase();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlschema/BaseAttribute$Factory.class */
    public static final class Factory {
        public static BaseAttribute newInstance() {
            return (BaseAttribute) XmlBeans.getContextTypeLoader().newInstance(BaseAttribute.type, null);
        }

        public static BaseAttribute newInstance(XmlOptions options) {
            return (BaseAttribute) XmlBeans.getContextTypeLoader().newInstance(BaseAttribute.type, options);
        }

        public static BaseAttribute parse(String xmlAsString) throws XmlException {
            return (BaseAttribute) XmlBeans.getContextTypeLoader().parse(xmlAsString, BaseAttribute.type, (XmlOptions) null);
        }

        public static BaseAttribute parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (BaseAttribute) XmlBeans.getContextTypeLoader().parse(xmlAsString, BaseAttribute.type, options);
        }

        public static BaseAttribute parse(File file) throws XmlException, IOException {
            return (BaseAttribute) XmlBeans.getContextTypeLoader().parse(file, BaseAttribute.type, (XmlOptions) null);
        }

        public static BaseAttribute parse(File file, XmlOptions options) throws XmlException, IOException {
            return (BaseAttribute) XmlBeans.getContextTypeLoader().parse(file, BaseAttribute.type, options);
        }

        public static BaseAttribute parse(URL u) throws XmlException, IOException {
            return (BaseAttribute) XmlBeans.getContextTypeLoader().parse(u, BaseAttribute.type, (XmlOptions) null);
        }

        public static BaseAttribute parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (BaseAttribute) XmlBeans.getContextTypeLoader().parse(u, BaseAttribute.type, options);
        }

        public static BaseAttribute parse(InputStream is) throws XmlException, IOException {
            return (BaseAttribute) XmlBeans.getContextTypeLoader().parse(is, BaseAttribute.type, (XmlOptions) null);
        }

        public static BaseAttribute parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (BaseAttribute) XmlBeans.getContextTypeLoader().parse(is, BaseAttribute.type, options);
        }

        public static BaseAttribute parse(Reader r) throws XmlException, IOException {
            return (BaseAttribute) XmlBeans.getContextTypeLoader().parse(r, BaseAttribute.type, (XmlOptions) null);
        }

        public static BaseAttribute parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (BaseAttribute) XmlBeans.getContextTypeLoader().parse(r, BaseAttribute.type, options);
        }

        public static BaseAttribute parse(XMLStreamReader sr) throws XmlException {
            return (BaseAttribute) XmlBeans.getContextTypeLoader().parse(sr, BaseAttribute.type, (XmlOptions) null);
        }

        public static BaseAttribute parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (BaseAttribute) XmlBeans.getContextTypeLoader().parse(sr, BaseAttribute.type, options);
        }

        public static BaseAttribute parse(Node node) throws XmlException {
            return (BaseAttribute) XmlBeans.getContextTypeLoader().parse(node, BaseAttribute.type, (XmlOptions) null);
        }

        public static BaseAttribute parse(Node node, XmlOptions options) throws XmlException {
            return (BaseAttribute) XmlBeans.getContextTypeLoader().parse(node, BaseAttribute.type, options);
        }

        public static BaseAttribute parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (BaseAttribute) XmlBeans.getContextTypeLoader().parse(xis, BaseAttribute.type, (XmlOptions) null);
        }

        public static BaseAttribute parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (BaseAttribute) XmlBeans.getContextTypeLoader().parse(xis, BaseAttribute.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, BaseAttribute.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, BaseAttribute.type, options);
        }

        private Factory() {
        }
    }
}
