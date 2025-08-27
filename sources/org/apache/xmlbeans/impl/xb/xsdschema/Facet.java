package org.apache.xmlbeans.impl.xb.xsdschema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/Facet.class */
public interface Facet extends Annotated {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Facet.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("facet446etype");

    XmlAnySimpleType getValue();

    void setValue(XmlAnySimpleType xmlAnySimpleType);

    XmlAnySimpleType addNewValue();

    boolean getFixed();

    XmlBoolean xgetFixed();

    boolean isSetFixed();

    void setFixed(boolean z);

    void xsetFixed(XmlBoolean xmlBoolean);

    void unsetFixed();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/Facet$Factory.class */
    public static final class Factory {
        public static Facet newInstance() {
            return (Facet) XmlBeans.getContextTypeLoader().newInstance(Facet.type, null);
        }

        public static Facet newInstance(XmlOptions options) {
            return (Facet) XmlBeans.getContextTypeLoader().newInstance(Facet.type, options);
        }

        public static Facet parse(String xmlAsString) throws XmlException {
            return (Facet) XmlBeans.getContextTypeLoader().parse(xmlAsString, Facet.type, (XmlOptions) null);
        }

        public static Facet parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (Facet) XmlBeans.getContextTypeLoader().parse(xmlAsString, Facet.type, options);
        }

        public static Facet parse(File file) throws XmlException, IOException {
            return (Facet) XmlBeans.getContextTypeLoader().parse(file, Facet.type, (XmlOptions) null);
        }

        public static Facet parse(File file, XmlOptions options) throws XmlException, IOException {
            return (Facet) XmlBeans.getContextTypeLoader().parse(file, Facet.type, options);
        }

        public static Facet parse(URL u) throws XmlException, IOException {
            return (Facet) XmlBeans.getContextTypeLoader().parse(u, Facet.type, (XmlOptions) null);
        }

        public static Facet parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (Facet) XmlBeans.getContextTypeLoader().parse(u, Facet.type, options);
        }

        public static Facet parse(InputStream is) throws XmlException, IOException {
            return (Facet) XmlBeans.getContextTypeLoader().parse(is, Facet.type, (XmlOptions) null);
        }

        public static Facet parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (Facet) XmlBeans.getContextTypeLoader().parse(is, Facet.type, options);
        }

        public static Facet parse(Reader r) throws XmlException, IOException {
            return (Facet) XmlBeans.getContextTypeLoader().parse(r, Facet.type, (XmlOptions) null);
        }

        public static Facet parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (Facet) XmlBeans.getContextTypeLoader().parse(r, Facet.type, options);
        }

        public static Facet parse(XMLStreamReader sr) throws XmlException {
            return (Facet) XmlBeans.getContextTypeLoader().parse(sr, Facet.type, (XmlOptions) null);
        }

        public static Facet parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (Facet) XmlBeans.getContextTypeLoader().parse(sr, Facet.type, options);
        }

        public static Facet parse(Node node) throws XmlException {
            return (Facet) XmlBeans.getContextTypeLoader().parse(node, Facet.type, (XmlOptions) null);
        }

        public static Facet parse(Node node, XmlOptions options) throws XmlException {
            return (Facet) XmlBeans.getContextTypeLoader().parse(node, Facet.type, options);
        }

        public static Facet parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (Facet) XmlBeans.getContextTypeLoader().parse(xis, Facet.type, (XmlOptions) null);
        }

        public static Facet parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (Facet) XmlBeans.getContextTypeLoader().parse(xis, Facet.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Facet.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Facet.type, options);
        }

        private Factory() {
        }
    }
}
