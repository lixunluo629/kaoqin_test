package org.apache.xmlbeans.impl.xb.xsdschema;

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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/IncludeDocument.class */
public interface IncludeDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(IncludeDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("includeaf6ddoctype");

    Include getInclude();

    void setInclude(Include include);

    Include addNewInclude();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/IncludeDocument$Include.class */
    public interface Include extends Annotated {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Include.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("include59d9elemtype");

        String getSchemaLocation();

        XmlAnyURI xgetSchemaLocation();

        void setSchemaLocation(String str);

        void xsetSchemaLocation(XmlAnyURI xmlAnyURI);

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/IncludeDocument$Include$Factory.class */
        public static final class Factory {
            public static Include newInstance() {
                return (Include) XmlBeans.getContextTypeLoader().newInstance(Include.type, null);
            }

            public static Include newInstance(XmlOptions options) {
                return (Include) XmlBeans.getContextTypeLoader().newInstance(Include.type, options);
            }

            private Factory() {
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/IncludeDocument$Factory.class */
    public static final class Factory {
        public static IncludeDocument newInstance() {
            return (IncludeDocument) XmlBeans.getContextTypeLoader().newInstance(IncludeDocument.type, null);
        }

        public static IncludeDocument newInstance(XmlOptions options) {
            return (IncludeDocument) XmlBeans.getContextTypeLoader().newInstance(IncludeDocument.type, options);
        }

        public static IncludeDocument parse(String xmlAsString) throws XmlException {
            return (IncludeDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, IncludeDocument.type, (XmlOptions) null);
        }

        public static IncludeDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (IncludeDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, IncludeDocument.type, options);
        }

        public static IncludeDocument parse(File file) throws XmlException, IOException {
            return (IncludeDocument) XmlBeans.getContextTypeLoader().parse(file, IncludeDocument.type, (XmlOptions) null);
        }

        public static IncludeDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (IncludeDocument) XmlBeans.getContextTypeLoader().parse(file, IncludeDocument.type, options);
        }

        public static IncludeDocument parse(URL u) throws XmlException, IOException {
            return (IncludeDocument) XmlBeans.getContextTypeLoader().parse(u, IncludeDocument.type, (XmlOptions) null);
        }

        public static IncludeDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (IncludeDocument) XmlBeans.getContextTypeLoader().parse(u, IncludeDocument.type, options);
        }

        public static IncludeDocument parse(InputStream is) throws XmlException, IOException {
            return (IncludeDocument) XmlBeans.getContextTypeLoader().parse(is, IncludeDocument.type, (XmlOptions) null);
        }

        public static IncludeDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (IncludeDocument) XmlBeans.getContextTypeLoader().parse(is, IncludeDocument.type, options);
        }

        public static IncludeDocument parse(Reader r) throws XmlException, IOException {
            return (IncludeDocument) XmlBeans.getContextTypeLoader().parse(r, IncludeDocument.type, (XmlOptions) null);
        }

        public static IncludeDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (IncludeDocument) XmlBeans.getContextTypeLoader().parse(r, IncludeDocument.type, options);
        }

        public static IncludeDocument parse(XMLStreamReader sr) throws XmlException {
            return (IncludeDocument) XmlBeans.getContextTypeLoader().parse(sr, IncludeDocument.type, (XmlOptions) null);
        }

        public static IncludeDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (IncludeDocument) XmlBeans.getContextTypeLoader().parse(sr, IncludeDocument.type, options);
        }

        public static IncludeDocument parse(Node node) throws XmlException {
            return (IncludeDocument) XmlBeans.getContextTypeLoader().parse(node, IncludeDocument.type, (XmlOptions) null);
        }

        public static IncludeDocument parse(Node node, XmlOptions options) throws XmlException {
            return (IncludeDocument) XmlBeans.getContextTypeLoader().parse(node, IncludeDocument.type, options);
        }

        public static IncludeDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (IncludeDocument) XmlBeans.getContextTypeLoader().parse(xis, IncludeDocument.type, (XmlOptions) null);
        }

        public static IncludeDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (IncludeDocument) XmlBeans.getContextTypeLoader().parse(xis, IncludeDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, IncludeDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, IncludeDocument.type, options);
        }

        private Factory() {
        }
    }
}
