package org.apache.xmlbeans.impl.xb.xsdschema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/ComplexContentDocument.class */
public interface ComplexContentDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(ComplexContentDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("complexcontentc57adoctype");

    ComplexContent getComplexContent();

    void setComplexContent(ComplexContent complexContent);

    ComplexContent addNewComplexContent();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/ComplexContentDocument$ComplexContent.class */
    public interface ComplexContent extends Annotated {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(ComplexContent.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("complexcontentaa7felemtype");

        ComplexRestrictionType getRestriction();

        boolean isSetRestriction();

        void setRestriction(ComplexRestrictionType complexRestrictionType);

        ComplexRestrictionType addNewRestriction();

        void unsetRestriction();

        ExtensionType getExtension();

        boolean isSetExtension();

        void setExtension(ExtensionType extensionType);

        ExtensionType addNewExtension();

        void unsetExtension();

        boolean getMixed();

        XmlBoolean xgetMixed();

        boolean isSetMixed();

        void setMixed(boolean z);

        void xsetMixed(XmlBoolean xmlBoolean);

        void unsetMixed();

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/ComplexContentDocument$ComplexContent$Factory.class */
        public static final class Factory {
            public static ComplexContent newInstance() {
                return (ComplexContent) XmlBeans.getContextTypeLoader().newInstance(ComplexContent.type, null);
            }

            public static ComplexContent newInstance(XmlOptions options) {
                return (ComplexContent) XmlBeans.getContextTypeLoader().newInstance(ComplexContent.type, options);
            }

            private Factory() {
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/ComplexContentDocument$Factory.class */
    public static final class Factory {
        public static ComplexContentDocument newInstance() {
            return (ComplexContentDocument) XmlBeans.getContextTypeLoader().newInstance(ComplexContentDocument.type, null);
        }

        public static ComplexContentDocument newInstance(XmlOptions options) {
            return (ComplexContentDocument) XmlBeans.getContextTypeLoader().newInstance(ComplexContentDocument.type, options);
        }

        public static ComplexContentDocument parse(String xmlAsString) throws XmlException {
            return (ComplexContentDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, ComplexContentDocument.type, (XmlOptions) null);
        }

        public static ComplexContentDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (ComplexContentDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, ComplexContentDocument.type, options);
        }

        public static ComplexContentDocument parse(File file) throws XmlException, IOException {
            return (ComplexContentDocument) XmlBeans.getContextTypeLoader().parse(file, ComplexContentDocument.type, (XmlOptions) null);
        }

        public static ComplexContentDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (ComplexContentDocument) XmlBeans.getContextTypeLoader().parse(file, ComplexContentDocument.type, options);
        }

        public static ComplexContentDocument parse(URL u) throws XmlException, IOException {
            return (ComplexContentDocument) XmlBeans.getContextTypeLoader().parse(u, ComplexContentDocument.type, (XmlOptions) null);
        }

        public static ComplexContentDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (ComplexContentDocument) XmlBeans.getContextTypeLoader().parse(u, ComplexContentDocument.type, options);
        }

        public static ComplexContentDocument parse(InputStream is) throws XmlException, IOException {
            return (ComplexContentDocument) XmlBeans.getContextTypeLoader().parse(is, ComplexContentDocument.type, (XmlOptions) null);
        }

        public static ComplexContentDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (ComplexContentDocument) XmlBeans.getContextTypeLoader().parse(is, ComplexContentDocument.type, options);
        }

        public static ComplexContentDocument parse(Reader r) throws XmlException, IOException {
            return (ComplexContentDocument) XmlBeans.getContextTypeLoader().parse(r, ComplexContentDocument.type, (XmlOptions) null);
        }

        public static ComplexContentDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (ComplexContentDocument) XmlBeans.getContextTypeLoader().parse(r, ComplexContentDocument.type, options);
        }

        public static ComplexContentDocument parse(XMLStreamReader sr) throws XmlException {
            return (ComplexContentDocument) XmlBeans.getContextTypeLoader().parse(sr, ComplexContentDocument.type, (XmlOptions) null);
        }

        public static ComplexContentDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (ComplexContentDocument) XmlBeans.getContextTypeLoader().parse(sr, ComplexContentDocument.type, options);
        }

        public static ComplexContentDocument parse(Node node) throws XmlException {
            return (ComplexContentDocument) XmlBeans.getContextTypeLoader().parse(node, ComplexContentDocument.type, (XmlOptions) null);
        }

        public static ComplexContentDocument parse(Node node, XmlOptions options) throws XmlException {
            return (ComplexContentDocument) XmlBeans.getContextTypeLoader().parse(node, ComplexContentDocument.type, options);
        }

        public static ComplexContentDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (ComplexContentDocument) XmlBeans.getContextTypeLoader().parse(xis, ComplexContentDocument.type, (XmlOptions) null);
        }

        public static ComplexContentDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (ComplexContentDocument) XmlBeans.getContextTypeLoader().parse(xis, ComplexContentDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, ComplexContentDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, ComplexContentDocument.type, options);
        }

        private Factory() {
        }
    }
}
