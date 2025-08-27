package org.apache.xmlbeans.impl.xb.xsdschema;

import ch.qos.logback.core.pattern.parser.Parser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.StringEnumAbstractBase;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlNMTOKEN;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/WhiteSpaceDocument.class */
public interface WhiteSpaceDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(WhiteSpaceDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("whitespaced2c6doctype");

    WhiteSpace getWhiteSpace();

    void setWhiteSpace(WhiteSpace whiteSpace);

    WhiteSpace addNewWhiteSpace();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/WhiteSpaceDocument$WhiteSpace.class */
    public interface WhiteSpace extends Facet {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(WhiteSpace.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("whitespace97ffelemtype");

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/WhiteSpaceDocument$WhiteSpace$Value.class */
        public interface Value extends XmlNMTOKEN {
            public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Value.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("value8186attrtype");
            public static final Enum PRESERVE = Enum.forString("preserve");
            public static final Enum REPLACE = Enum.forString(Parser.REPLACE_CONVERTER_WORD);
            public static final Enum COLLAPSE = Enum.forString("collapse");
            public static final int INT_PRESERVE = 1;
            public static final int INT_REPLACE = 2;
            public static final int INT_COLLAPSE = 3;

            StringEnumAbstractBase enumValue();

            void set(StringEnumAbstractBase stringEnumAbstractBase);

            /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/WhiteSpaceDocument$WhiteSpace$Value$Enum.class */
            public static final class Enum extends StringEnumAbstractBase {
                static final int INT_PRESERVE = 1;
                static final int INT_REPLACE = 2;
                static final int INT_COLLAPSE = 3;
                public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("preserve", 1), new Enum(Parser.REPLACE_CONVERTER_WORD, 2), new Enum("collapse", 3)});
                private static final long serialVersionUID = 1;

                public static Enum forString(String s) {
                    return (Enum) table.forString(s);
                }

                public static Enum forInt(int i) {
                    return (Enum) table.forInt(i);
                }

                private Enum(String s, int i) {
                    super(s, i);
                }

                private Object readResolve() {
                    return forInt(intValue());
                }
            }

            /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/WhiteSpaceDocument$WhiteSpace$Value$Factory.class */
            public static final class Factory {
                public static Value newValue(Object obj) {
                    return (Value) Value.type.newValue(obj);
                }

                public static Value newInstance() {
                    return (Value) XmlBeans.getContextTypeLoader().newInstance(Value.type, null);
                }

                public static Value newInstance(XmlOptions options) {
                    return (Value) XmlBeans.getContextTypeLoader().newInstance(Value.type, options);
                }

                private Factory() {
                }
            }
        }

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/WhiteSpaceDocument$WhiteSpace$Factory.class */
        public static final class Factory {
            public static WhiteSpace newInstance() {
                return (WhiteSpace) XmlBeans.getContextTypeLoader().newInstance(WhiteSpace.type, null);
            }

            public static WhiteSpace newInstance(XmlOptions options) {
                return (WhiteSpace) XmlBeans.getContextTypeLoader().newInstance(WhiteSpace.type, options);
            }

            private Factory() {
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/WhiteSpaceDocument$Factory.class */
    public static final class Factory {
        public static WhiteSpaceDocument newInstance() {
            return (WhiteSpaceDocument) XmlBeans.getContextTypeLoader().newInstance(WhiteSpaceDocument.type, null);
        }

        public static WhiteSpaceDocument newInstance(XmlOptions options) {
            return (WhiteSpaceDocument) XmlBeans.getContextTypeLoader().newInstance(WhiteSpaceDocument.type, options);
        }

        public static WhiteSpaceDocument parse(String xmlAsString) throws XmlException {
            return (WhiteSpaceDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, WhiteSpaceDocument.type, (XmlOptions) null);
        }

        public static WhiteSpaceDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (WhiteSpaceDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, WhiteSpaceDocument.type, options);
        }

        public static WhiteSpaceDocument parse(File file) throws XmlException, IOException {
            return (WhiteSpaceDocument) XmlBeans.getContextTypeLoader().parse(file, WhiteSpaceDocument.type, (XmlOptions) null);
        }

        public static WhiteSpaceDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (WhiteSpaceDocument) XmlBeans.getContextTypeLoader().parse(file, WhiteSpaceDocument.type, options);
        }

        public static WhiteSpaceDocument parse(URL u) throws XmlException, IOException {
            return (WhiteSpaceDocument) XmlBeans.getContextTypeLoader().parse(u, WhiteSpaceDocument.type, (XmlOptions) null);
        }

        public static WhiteSpaceDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (WhiteSpaceDocument) XmlBeans.getContextTypeLoader().parse(u, WhiteSpaceDocument.type, options);
        }

        public static WhiteSpaceDocument parse(InputStream is) throws XmlException, IOException {
            return (WhiteSpaceDocument) XmlBeans.getContextTypeLoader().parse(is, WhiteSpaceDocument.type, (XmlOptions) null);
        }

        public static WhiteSpaceDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (WhiteSpaceDocument) XmlBeans.getContextTypeLoader().parse(is, WhiteSpaceDocument.type, options);
        }

        public static WhiteSpaceDocument parse(Reader r) throws XmlException, IOException {
            return (WhiteSpaceDocument) XmlBeans.getContextTypeLoader().parse(r, WhiteSpaceDocument.type, (XmlOptions) null);
        }

        public static WhiteSpaceDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (WhiteSpaceDocument) XmlBeans.getContextTypeLoader().parse(r, WhiteSpaceDocument.type, options);
        }

        public static WhiteSpaceDocument parse(XMLStreamReader sr) throws XmlException {
            return (WhiteSpaceDocument) XmlBeans.getContextTypeLoader().parse(sr, WhiteSpaceDocument.type, (XmlOptions) null);
        }

        public static WhiteSpaceDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (WhiteSpaceDocument) XmlBeans.getContextTypeLoader().parse(sr, WhiteSpaceDocument.type, options);
        }

        public static WhiteSpaceDocument parse(Node node) throws XmlException {
            return (WhiteSpaceDocument) XmlBeans.getContextTypeLoader().parse(node, WhiteSpaceDocument.type, (XmlOptions) null);
        }

        public static WhiteSpaceDocument parse(Node node, XmlOptions options) throws XmlException {
            return (WhiteSpaceDocument) XmlBeans.getContextTypeLoader().parse(node, WhiteSpaceDocument.type, options);
        }

        public static WhiteSpaceDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (WhiteSpaceDocument) XmlBeans.getContextTypeLoader().parse(xis, WhiteSpaceDocument.type, (XmlOptions) null);
        }

        public static WhiteSpaceDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (WhiteSpaceDocument) XmlBeans.getContextTypeLoader().parse(xis, WhiteSpaceDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, WhiteSpaceDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, WhiteSpaceDocument.type, options);
        }

        private Factory() {
        }
    }
}
