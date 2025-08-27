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
import org.apache.xmlbeans.XmlToken;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/SelectorDocument.class */
public interface SelectorDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(SelectorDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("selectorcb44doctype");

    Selector getSelector();

    void setSelector(Selector selector);

    Selector addNewSelector();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/SelectorDocument$Selector.class */
    public interface Selector extends Annotated {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Selector.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("selector233felemtype");

        String getXpath();

        Xpath xgetXpath();

        void setXpath(String str);

        void xsetXpath(Xpath xpath);

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/SelectorDocument$Selector$Xpath.class */
        public interface Xpath extends XmlToken {
            public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Xpath.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("xpath6f9aattrtype");

            /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/SelectorDocument$Selector$Xpath$Factory.class */
            public static final class Factory {
                public static Xpath newValue(Object obj) {
                    return (Xpath) Xpath.type.newValue(obj);
                }

                public static Xpath newInstance() {
                    return (Xpath) XmlBeans.getContextTypeLoader().newInstance(Xpath.type, null);
                }

                public static Xpath newInstance(XmlOptions options) {
                    return (Xpath) XmlBeans.getContextTypeLoader().newInstance(Xpath.type, options);
                }

                private Factory() {
                }
            }
        }

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/SelectorDocument$Selector$Factory.class */
        public static final class Factory {
            public static Selector newInstance() {
                return (Selector) XmlBeans.getContextTypeLoader().newInstance(Selector.type, null);
            }

            public static Selector newInstance(XmlOptions options) {
                return (Selector) XmlBeans.getContextTypeLoader().newInstance(Selector.type, options);
            }

            private Factory() {
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/SelectorDocument$Factory.class */
    public static final class Factory {
        public static SelectorDocument newInstance() {
            return (SelectorDocument) XmlBeans.getContextTypeLoader().newInstance(SelectorDocument.type, null);
        }

        public static SelectorDocument newInstance(XmlOptions options) {
            return (SelectorDocument) XmlBeans.getContextTypeLoader().newInstance(SelectorDocument.type, options);
        }

        public static SelectorDocument parse(String xmlAsString) throws XmlException {
            return (SelectorDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, SelectorDocument.type, (XmlOptions) null);
        }

        public static SelectorDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (SelectorDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, SelectorDocument.type, options);
        }

        public static SelectorDocument parse(File file) throws XmlException, IOException {
            return (SelectorDocument) XmlBeans.getContextTypeLoader().parse(file, SelectorDocument.type, (XmlOptions) null);
        }

        public static SelectorDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (SelectorDocument) XmlBeans.getContextTypeLoader().parse(file, SelectorDocument.type, options);
        }

        public static SelectorDocument parse(URL u) throws XmlException, IOException {
            return (SelectorDocument) XmlBeans.getContextTypeLoader().parse(u, SelectorDocument.type, (XmlOptions) null);
        }

        public static SelectorDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (SelectorDocument) XmlBeans.getContextTypeLoader().parse(u, SelectorDocument.type, options);
        }

        public static SelectorDocument parse(InputStream is) throws XmlException, IOException {
            return (SelectorDocument) XmlBeans.getContextTypeLoader().parse(is, SelectorDocument.type, (XmlOptions) null);
        }

        public static SelectorDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (SelectorDocument) XmlBeans.getContextTypeLoader().parse(is, SelectorDocument.type, options);
        }

        public static SelectorDocument parse(Reader r) throws XmlException, IOException {
            return (SelectorDocument) XmlBeans.getContextTypeLoader().parse(r, SelectorDocument.type, (XmlOptions) null);
        }

        public static SelectorDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (SelectorDocument) XmlBeans.getContextTypeLoader().parse(r, SelectorDocument.type, options);
        }

        public static SelectorDocument parse(XMLStreamReader sr) throws XmlException {
            return (SelectorDocument) XmlBeans.getContextTypeLoader().parse(sr, SelectorDocument.type, (XmlOptions) null);
        }

        public static SelectorDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (SelectorDocument) XmlBeans.getContextTypeLoader().parse(sr, SelectorDocument.type, options);
        }

        public static SelectorDocument parse(Node node) throws XmlException {
            return (SelectorDocument) XmlBeans.getContextTypeLoader().parse(node, SelectorDocument.type, (XmlOptions) null);
        }

        public static SelectorDocument parse(Node node, XmlOptions options) throws XmlException {
            return (SelectorDocument) XmlBeans.getContextTypeLoader().parse(node, SelectorDocument.type, options);
        }

        public static SelectorDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (SelectorDocument) XmlBeans.getContextTypeLoader().parse(xis, SelectorDocument.type, (XmlOptions) null);
        }

        public static SelectorDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (SelectorDocument) XmlBeans.getContextTypeLoader().parse(xis, SelectorDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, SelectorDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, SelectorDocument.type, options);
        }

        private Factory() {
        }
    }
}
