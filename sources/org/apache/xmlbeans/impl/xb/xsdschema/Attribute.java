package org.apache.xmlbeans.impl.xb.xsdschema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.StringEnumAbstractBase;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlNCName;
import org.apache.xmlbeans.XmlNMTOKEN;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlQName;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.xb.xsdschema.FormChoice;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.springframework.validation.DefaultBindingErrorProcessor;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/Attribute.class */
public interface Attribute extends Annotated {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Attribute.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("attribute83a9type");

    LocalSimpleType getSimpleType();

    boolean isSetSimpleType();

    void setSimpleType(LocalSimpleType localSimpleType);

    LocalSimpleType addNewSimpleType();

    void unsetSimpleType();

    String getName();

    XmlNCName xgetName();

    boolean isSetName();

    void setName(String str);

    void xsetName(XmlNCName xmlNCName);

    void unsetName();

    QName getRef();

    XmlQName xgetRef();

    boolean isSetRef();

    void setRef(QName qName);

    void xsetRef(XmlQName xmlQName);

    void unsetRef();

    QName getType();

    XmlQName xgetType();

    boolean isSetType();

    void setType(QName qName);

    void xsetType(XmlQName xmlQName);

    void unsetType();

    Use.Enum getUse();

    Use xgetUse();

    boolean isSetUse();

    void setUse(Use.Enum r1);

    void xsetUse(Use use);

    void unsetUse();

    String getDefault();

    XmlString xgetDefault();

    boolean isSetDefault();

    void setDefault(String str);

    void xsetDefault(XmlString xmlString);

    void unsetDefault();

    String getFixed();

    XmlString xgetFixed();

    boolean isSetFixed();

    void setFixed(String str);

    void xsetFixed(XmlString xmlString);

    void unsetFixed();

    FormChoice.Enum getForm();

    FormChoice xgetForm();

    boolean isSetForm();

    void setForm(FormChoice.Enum r1);

    void xsetForm(FormChoice formChoice);

    void unsetForm();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/Attribute$Use.class */
    public interface Use extends XmlNMTOKEN {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Use.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("usea41aattrtype");
        public static final Enum PROHIBITED = Enum.forString("prohibited");
        public static final Enum OPTIONAL = Enum.forString("optional");
        public static final Enum REQUIRED = Enum.forString(DefaultBindingErrorProcessor.MISSING_FIELD_ERROR_CODE);
        public static final int INT_PROHIBITED = 1;
        public static final int INT_OPTIONAL = 2;
        public static final int INT_REQUIRED = 3;

        StringEnumAbstractBase enumValue();

        void set(StringEnumAbstractBase stringEnumAbstractBase);

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/Attribute$Use$Enum.class */
        public static final class Enum extends StringEnumAbstractBase {
            static final int INT_PROHIBITED = 1;
            static final int INT_OPTIONAL = 2;
            static final int INT_REQUIRED = 3;
            public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("prohibited", 1), new Enum("optional", 2), new Enum(DefaultBindingErrorProcessor.MISSING_FIELD_ERROR_CODE, 3)});
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

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/Attribute$Use$Factory.class */
        public static final class Factory {
            public static Use newValue(Object obj) {
                return (Use) Use.type.newValue(obj);
            }

            public static Use newInstance() {
                return (Use) XmlBeans.getContextTypeLoader().newInstance(Use.type, null);
            }

            public static Use newInstance(XmlOptions options) {
                return (Use) XmlBeans.getContextTypeLoader().newInstance(Use.type, options);
            }

            private Factory() {
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/Attribute$Factory.class */
    public static final class Factory {
        public static Attribute newInstance() {
            return (Attribute) XmlBeans.getContextTypeLoader().newInstance(Attribute.type, null);
        }

        public static Attribute newInstance(XmlOptions options) {
            return (Attribute) XmlBeans.getContextTypeLoader().newInstance(Attribute.type, options);
        }

        public static Attribute parse(String xmlAsString) throws XmlException {
            return (Attribute) XmlBeans.getContextTypeLoader().parse(xmlAsString, Attribute.type, (XmlOptions) null);
        }

        public static Attribute parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (Attribute) XmlBeans.getContextTypeLoader().parse(xmlAsString, Attribute.type, options);
        }

        public static Attribute parse(File file) throws XmlException, IOException {
            return (Attribute) XmlBeans.getContextTypeLoader().parse(file, Attribute.type, (XmlOptions) null);
        }

        public static Attribute parse(File file, XmlOptions options) throws XmlException, IOException {
            return (Attribute) XmlBeans.getContextTypeLoader().parse(file, Attribute.type, options);
        }

        public static Attribute parse(URL u) throws XmlException, IOException {
            return (Attribute) XmlBeans.getContextTypeLoader().parse(u, Attribute.type, (XmlOptions) null);
        }

        public static Attribute parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (Attribute) XmlBeans.getContextTypeLoader().parse(u, Attribute.type, options);
        }

        public static Attribute parse(InputStream is) throws XmlException, IOException {
            return (Attribute) XmlBeans.getContextTypeLoader().parse(is, Attribute.type, (XmlOptions) null);
        }

        public static Attribute parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (Attribute) XmlBeans.getContextTypeLoader().parse(is, Attribute.type, options);
        }

        public static Attribute parse(Reader r) throws XmlException, IOException {
            return (Attribute) XmlBeans.getContextTypeLoader().parse(r, Attribute.type, (XmlOptions) null);
        }

        public static Attribute parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (Attribute) XmlBeans.getContextTypeLoader().parse(r, Attribute.type, options);
        }

        public static Attribute parse(XMLStreamReader sr) throws XmlException {
            return (Attribute) XmlBeans.getContextTypeLoader().parse(sr, Attribute.type, (XmlOptions) null);
        }

        public static Attribute parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (Attribute) XmlBeans.getContextTypeLoader().parse(sr, Attribute.type, options);
        }

        public static Attribute parse(Node node) throws XmlException {
            return (Attribute) XmlBeans.getContextTypeLoader().parse(node, Attribute.type, (XmlOptions) null);
        }

        public static Attribute parse(Node node, XmlOptions options) throws XmlException {
            return (Attribute) XmlBeans.getContextTypeLoader().parse(node, Attribute.type, options);
        }

        public static Attribute parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (Attribute) XmlBeans.getContextTypeLoader().parse(xis, Attribute.type, (XmlOptions) null);
        }

        public static Attribute parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (Attribute) XmlBeans.getContextTypeLoader().parse(xis, Attribute.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Attribute.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Attribute.type, options);
        }

        private Factory() {
        }
    }
}
