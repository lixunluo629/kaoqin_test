package org.apache.xmlbeans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlObject.class */
public interface XmlObject extends XmlTokenSource {
    public static final SchemaType type = XmlBeans.getBuiltinTypeSystem().typeForHandle("_BI_anyType");
    public static final int LESS_THAN = -1;
    public static final int EQUAL = 0;
    public static final int GREATER_THAN = 1;
    public static final int NOT_EQUAL = 2;

    SchemaType schemaType();

    boolean validate();

    boolean validate(XmlOptions xmlOptions);

    XmlObject[] selectPath(String str);

    XmlObject[] selectPath(String str, XmlOptions xmlOptions);

    XmlObject[] execQuery(String str);

    XmlObject[] execQuery(String str, XmlOptions xmlOptions);

    XmlObject changeType(SchemaType schemaType);

    XmlObject substitute(QName qName, SchemaType schemaType);

    boolean isNil();

    void setNil();

    String toString();

    boolean isImmutable();

    XmlObject set(XmlObject xmlObject);

    XmlObject copy();

    XmlObject copy(XmlOptions xmlOptions);

    boolean valueEquals(XmlObject xmlObject);

    int valueHashCode();

    int compareTo(Object obj);

    int compareValue(XmlObject xmlObject);

    XmlObject[] selectChildren(QName qName);

    XmlObject[] selectChildren(String str, String str2);

    XmlObject[] selectChildren(QNameSet qNameSet);

    XmlObject selectAttribute(QName qName);

    XmlObject selectAttribute(String str, String str2);

    XmlObject[] selectAttributes(QNameSet qNameSet);

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlObject$Factory.class */
    public static final class Factory {
        public static XmlObject newInstance() {
            return XmlBeans.getContextTypeLoader().newInstance(null, null);
        }

        public static XmlObject newInstance(XmlOptions options) {
            return XmlBeans.getContextTypeLoader().newInstance(null, options);
        }

        public static XmlObject newValue(Object obj) {
            return XmlObject.type.newValue(obj);
        }

        public static XmlObject parse(String xmlAsString) throws XmlException {
            return XmlBeans.getContextTypeLoader().parse(xmlAsString, (SchemaType) null, (XmlOptions) null);
        }

        public static XmlObject parse(String xmlAsString, XmlOptions options) throws XmlException {
            return XmlBeans.getContextTypeLoader().parse(xmlAsString, (SchemaType) null, options);
        }

        public static XmlObject parse(File file) throws XmlException, IOException {
            return XmlBeans.getContextTypeLoader().parse(file, (SchemaType) null, (XmlOptions) null);
        }

        public static XmlObject parse(File file, XmlOptions options) throws XmlException, IOException {
            return XmlBeans.getContextTypeLoader().parse(file, (SchemaType) null, options);
        }

        public static XmlObject parse(URL u) throws XmlException, IOException {
            return XmlBeans.getContextTypeLoader().parse(u, (SchemaType) null, (XmlOptions) null);
        }

        public static XmlObject parse(URL u, XmlOptions options) throws XmlException, IOException {
            return XmlBeans.getContextTypeLoader().parse(u, (SchemaType) null, options);
        }

        public static XmlObject parse(InputStream is) throws XmlException, IOException {
            return XmlBeans.getContextTypeLoader().parse(is, (SchemaType) null, (XmlOptions) null);
        }

        public static XmlObject parse(XMLStreamReader xsr) throws XmlException {
            return XmlBeans.getContextTypeLoader().parse(xsr, (SchemaType) null, (XmlOptions) null);
        }

        public static XmlObject parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return XmlBeans.getContextTypeLoader().parse(is, (SchemaType) null, options);
        }

        public static XmlObject parse(XMLStreamReader xsr, XmlOptions options) throws XmlException {
            return XmlBeans.getContextTypeLoader().parse(xsr, (SchemaType) null, options);
        }

        public static XmlObject parse(Reader r) throws XmlException, IOException {
            return XmlBeans.getContextTypeLoader().parse(r, (SchemaType) null, (XmlOptions) null);
        }

        public static XmlObject parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return XmlBeans.getContextTypeLoader().parse(r, (SchemaType) null, options);
        }

        public static XmlObject parse(Node node) throws XmlException {
            return XmlBeans.getContextTypeLoader().parse(node, (SchemaType) null, (XmlOptions) null);
        }

        public static XmlObject parse(Node node, XmlOptions options) throws XmlException {
            return XmlBeans.getContextTypeLoader().parse(node, (SchemaType) null, options);
        }

        public static XmlObject parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().parse(xis, (SchemaType) null, (XmlOptions) null);
        }

        public static XmlObject parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().parse(xis, (SchemaType) null, options);
        }

        public static XmlSaxHandler newXmlSaxHandler() {
            return XmlBeans.getContextTypeLoader().newXmlSaxHandler(null, null);
        }

        public static XmlSaxHandler newXmlSaxHandler(XmlOptions options) {
            return XmlBeans.getContextTypeLoader().newXmlSaxHandler(null, options);
        }

        public static DOMImplementation newDomImplementation() {
            return XmlBeans.getContextTypeLoader().newDomImplementation(null);
        }

        public static DOMImplementation newDomImplementation(XmlOptions options) {
            return XmlBeans.getContextTypeLoader().newDomImplementation(options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, null, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, null, options);
        }

        private Factory() {
        }
    }
}
