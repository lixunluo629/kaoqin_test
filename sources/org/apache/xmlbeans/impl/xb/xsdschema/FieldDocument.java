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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/FieldDocument.class */
public interface FieldDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(FieldDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("field3f9bdoctype");

    Field getField();

    void setField(Field field);

    Field addNewField();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/FieldDocument$Field.class */
    public interface Field extends Annotated {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Field.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("field12f5elemtype");

        String getXpath();

        Xpath xgetXpath();

        void setXpath(String str);

        void xsetXpath(Xpath xpath);

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/FieldDocument$Field$Xpath.class */
        public interface Xpath extends XmlToken {
            public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Xpath.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("xpath7f90attrtype");

            /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/FieldDocument$Field$Xpath$Factory.class */
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

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/FieldDocument$Field$Factory.class */
        public static final class Factory {
            public static Field newInstance() {
                return (Field) XmlBeans.getContextTypeLoader().newInstance(Field.type, null);
            }

            public static Field newInstance(XmlOptions options) {
                return (Field) XmlBeans.getContextTypeLoader().newInstance(Field.type, options);
            }

            private Factory() {
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/FieldDocument$Factory.class */
    public static final class Factory {
        public static FieldDocument newInstance() {
            return (FieldDocument) XmlBeans.getContextTypeLoader().newInstance(FieldDocument.type, null);
        }

        public static FieldDocument newInstance(XmlOptions options) {
            return (FieldDocument) XmlBeans.getContextTypeLoader().newInstance(FieldDocument.type, options);
        }

        public static FieldDocument parse(String xmlAsString) throws XmlException {
            return (FieldDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, FieldDocument.type, (XmlOptions) null);
        }

        public static FieldDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (FieldDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, FieldDocument.type, options);
        }

        public static FieldDocument parse(File file) throws XmlException, IOException {
            return (FieldDocument) XmlBeans.getContextTypeLoader().parse(file, FieldDocument.type, (XmlOptions) null);
        }

        public static FieldDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (FieldDocument) XmlBeans.getContextTypeLoader().parse(file, FieldDocument.type, options);
        }

        public static FieldDocument parse(URL u) throws XmlException, IOException {
            return (FieldDocument) XmlBeans.getContextTypeLoader().parse(u, FieldDocument.type, (XmlOptions) null);
        }

        public static FieldDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (FieldDocument) XmlBeans.getContextTypeLoader().parse(u, FieldDocument.type, options);
        }

        public static FieldDocument parse(InputStream is) throws XmlException, IOException {
            return (FieldDocument) XmlBeans.getContextTypeLoader().parse(is, FieldDocument.type, (XmlOptions) null);
        }

        public static FieldDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (FieldDocument) XmlBeans.getContextTypeLoader().parse(is, FieldDocument.type, options);
        }

        public static FieldDocument parse(Reader r) throws XmlException, IOException {
            return (FieldDocument) XmlBeans.getContextTypeLoader().parse(r, FieldDocument.type, (XmlOptions) null);
        }

        public static FieldDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (FieldDocument) XmlBeans.getContextTypeLoader().parse(r, FieldDocument.type, options);
        }

        public static FieldDocument parse(XMLStreamReader sr) throws XmlException {
            return (FieldDocument) XmlBeans.getContextTypeLoader().parse(sr, FieldDocument.type, (XmlOptions) null);
        }

        public static FieldDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (FieldDocument) XmlBeans.getContextTypeLoader().parse(sr, FieldDocument.type, options);
        }

        public static FieldDocument parse(Node node) throws XmlException {
            return (FieldDocument) XmlBeans.getContextTypeLoader().parse(node, FieldDocument.type, (XmlOptions) null);
        }

        public static FieldDocument parse(Node node, XmlOptions options) throws XmlException {
            return (FieldDocument) XmlBeans.getContextTypeLoader().parse(node, FieldDocument.type, options);
        }

        public static FieldDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (FieldDocument) XmlBeans.getContextTypeLoader().parse(xis, FieldDocument.type, (XmlOptions) null);
        }

        public static FieldDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (FieldDocument) XmlBeans.getContextTypeLoader().parse(xis, FieldDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, FieldDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, FieldDocument.type, options);
        }

        private Factory() {
        }
    }
}
