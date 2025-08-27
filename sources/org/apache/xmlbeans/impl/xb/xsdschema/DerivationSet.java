package org.apache.xmlbeans.impl.xb.xsdschema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.StringEnumAbstractBase;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlToken;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/DerivationSet.class */
public interface DerivationSet extends XmlAnySimpleType {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(DerivationSet.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("derivationset037atype");

    Object getObjectValue();

    void setObjectValue(Object obj);

    Object objectValue();

    void objectSet(Object obj);

    SchemaType instanceType();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/DerivationSet$Member.class */
    public interface Member extends XmlToken {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Member.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("anoned75type");
        public static final Enum ALL = Enum.forString("#all");
        public static final int INT_ALL = 1;

        StringEnumAbstractBase enumValue();

        void set(StringEnumAbstractBase stringEnumAbstractBase);

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/DerivationSet$Member$Enum.class */
        public static final class Enum extends StringEnumAbstractBase {
            static final int INT_ALL = 1;
            public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("#all", 1)});
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

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/DerivationSet$Member$Factory.class */
        public static final class Factory {
            public static Member newValue(Object obj) {
                return (Member) Member.type.newValue(obj);
            }

            public static Member newInstance() {
                return (Member) XmlBeans.getContextTypeLoader().newInstance(Member.type, null);
            }

            public static Member newInstance(XmlOptions options) {
                return (Member) XmlBeans.getContextTypeLoader().newInstance(Member.type, options);
            }

            private Factory() {
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/DerivationSet$Member2.class */
    public interface Member2 extends XmlAnySimpleType {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Member2.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("anon9394type");

        List getListValue();

        List xgetListValue();

        void setListValue(List list);

        List listValue();

        List xlistValue();

        void set(List list);

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/DerivationSet$Member2$Factory.class */
        public static final class Factory {
            public static Member2 newValue(Object obj) {
                return (Member2) Member2.type.newValue(obj);
            }

            public static Member2 newInstance() {
                return (Member2) XmlBeans.getContextTypeLoader().newInstance(Member2.type, null);
            }

            public static Member2 newInstance(XmlOptions options) {
                return (Member2) XmlBeans.getContextTypeLoader().newInstance(Member2.type, options);
            }

            private Factory() {
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/DerivationSet$Factory.class */
    public static final class Factory {
        public static DerivationSet newValue(Object obj) {
            return (DerivationSet) DerivationSet.type.newValue(obj);
        }

        public static DerivationSet newInstance() {
            return (DerivationSet) XmlBeans.getContextTypeLoader().newInstance(DerivationSet.type, null);
        }

        public static DerivationSet newInstance(XmlOptions options) {
            return (DerivationSet) XmlBeans.getContextTypeLoader().newInstance(DerivationSet.type, options);
        }

        public static DerivationSet parse(String xmlAsString) throws XmlException {
            return (DerivationSet) XmlBeans.getContextTypeLoader().parse(xmlAsString, DerivationSet.type, (XmlOptions) null);
        }

        public static DerivationSet parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (DerivationSet) XmlBeans.getContextTypeLoader().parse(xmlAsString, DerivationSet.type, options);
        }

        public static DerivationSet parse(File file) throws XmlException, IOException {
            return (DerivationSet) XmlBeans.getContextTypeLoader().parse(file, DerivationSet.type, (XmlOptions) null);
        }

        public static DerivationSet parse(File file, XmlOptions options) throws XmlException, IOException {
            return (DerivationSet) XmlBeans.getContextTypeLoader().parse(file, DerivationSet.type, options);
        }

        public static DerivationSet parse(URL u) throws XmlException, IOException {
            return (DerivationSet) XmlBeans.getContextTypeLoader().parse(u, DerivationSet.type, (XmlOptions) null);
        }

        public static DerivationSet parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (DerivationSet) XmlBeans.getContextTypeLoader().parse(u, DerivationSet.type, options);
        }

        public static DerivationSet parse(InputStream is) throws XmlException, IOException {
            return (DerivationSet) XmlBeans.getContextTypeLoader().parse(is, DerivationSet.type, (XmlOptions) null);
        }

        public static DerivationSet parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (DerivationSet) XmlBeans.getContextTypeLoader().parse(is, DerivationSet.type, options);
        }

        public static DerivationSet parse(Reader r) throws XmlException, IOException {
            return (DerivationSet) XmlBeans.getContextTypeLoader().parse(r, DerivationSet.type, (XmlOptions) null);
        }

        public static DerivationSet parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (DerivationSet) XmlBeans.getContextTypeLoader().parse(r, DerivationSet.type, options);
        }

        public static DerivationSet parse(XMLStreamReader sr) throws XmlException {
            return (DerivationSet) XmlBeans.getContextTypeLoader().parse(sr, DerivationSet.type, (XmlOptions) null);
        }

        public static DerivationSet parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (DerivationSet) XmlBeans.getContextTypeLoader().parse(sr, DerivationSet.type, options);
        }

        public static DerivationSet parse(Node node) throws XmlException {
            return (DerivationSet) XmlBeans.getContextTypeLoader().parse(node, DerivationSet.type, (XmlOptions) null);
        }

        public static DerivationSet parse(Node node, XmlOptions options) throws XmlException {
            return (DerivationSet) XmlBeans.getContextTypeLoader().parse(node, DerivationSet.type, options);
        }

        public static DerivationSet parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (DerivationSet) XmlBeans.getContextTypeLoader().parse(xis, DerivationSet.type, (XmlOptions) null);
        }

        public static DerivationSet parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (DerivationSet) XmlBeans.getContextTypeLoader().parse(xis, DerivationSet.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, DerivationSet.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, DerivationSet.type, options);
        }

        private Factory() {
        }
    }
}
