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
import org.apache.xmlbeans.impl.xb.xsdschema.DerivationControl;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/SimpleDerivationSet.class */
public interface SimpleDerivationSet extends XmlAnySimpleType {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(SimpleDerivationSet.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("simplederivationsetf70ctype");

    Object getObjectValue();

    void setObjectValue(Object obj);

    Object objectValue();

    void objectSet(Object obj);

    SchemaType instanceType();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/SimpleDerivationSet$Member.class */
    public interface Member extends XmlToken {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Member.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("anon38c7type");
        public static final Enum ALL = Enum.forString("#all");
        public static final int INT_ALL = 1;

        StringEnumAbstractBase enumValue();

        void set(StringEnumAbstractBase stringEnumAbstractBase);

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/SimpleDerivationSet$Member$Enum.class */
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

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/SimpleDerivationSet$Member$Factory.class */
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

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/SimpleDerivationSet$Member2.class */
    public interface Member2 extends XmlAnySimpleType {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Member2.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("anon8ba6type");

        List getListValue();

        List xgetListValue();

        void setListValue(List list);

        List listValue();

        List xlistValue();

        void set(List list);

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/SimpleDerivationSet$Member2$Item.class */
        public interface Item extends DerivationControl {
            public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Item.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("anonf38etype");
            public static final DerivationControl.Enum LIST = DerivationControl.LIST;
            public static final DerivationControl.Enum UNION = DerivationControl.UNION;
            public static final DerivationControl.Enum RESTRICTION = DerivationControl.RESTRICTION;
            public static final int INT_LIST = 4;
            public static final int INT_UNION = 5;
            public static final int INT_RESTRICTION = 3;

            /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/SimpleDerivationSet$Member2$Item$Factory.class */
            public static final class Factory {
                public static Item newValue(Object obj) {
                    return (Item) Item.type.newValue(obj);
                }

                public static Item newInstance() {
                    return (Item) XmlBeans.getContextTypeLoader().newInstance(Item.type, null);
                }

                public static Item newInstance(XmlOptions options) {
                    return (Item) XmlBeans.getContextTypeLoader().newInstance(Item.type, options);
                }

                private Factory() {
                }
            }
        }

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/SimpleDerivationSet$Member2$Factory.class */
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

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/SimpleDerivationSet$Factory.class */
    public static final class Factory {
        public static SimpleDerivationSet newValue(Object obj) {
            return (SimpleDerivationSet) SimpleDerivationSet.type.newValue(obj);
        }

        public static SimpleDerivationSet newInstance() {
            return (SimpleDerivationSet) XmlBeans.getContextTypeLoader().newInstance(SimpleDerivationSet.type, null);
        }

        public static SimpleDerivationSet newInstance(XmlOptions options) {
            return (SimpleDerivationSet) XmlBeans.getContextTypeLoader().newInstance(SimpleDerivationSet.type, options);
        }

        public static SimpleDerivationSet parse(String xmlAsString) throws XmlException {
            return (SimpleDerivationSet) XmlBeans.getContextTypeLoader().parse(xmlAsString, SimpleDerivationSet.type, (XmlOptions) null);
        }

        public static SimpleDerivationSet parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (SimpleDerivationSet) XmlBeans.getContextTypeLoader().parse(xmlAsString, SimpleDerivationSet.type, options);
        }

        public static SimpleDerivationSet parse(File file) throws XmlException, IOException {
            return (SimpleDerivationSet) XmlBeans.getContextTypeLoader().parse(file, SimpleDerivationSet.type, (XmlOptions) null);
        }

        public static SimpleDerivationSet parse(File file, XmlOptions options) throws XmlException, IOException {
            return (SimpleDerivationSet) XmlBeans.getContextTypeLoader().parse(file, SimpleDerivationSet.type, options);
        }

        public static SimpleDerivationSet parse(URL u) throws XmlException, IOException {
            return (SimpleDerivationSet) XmlBeans.getContextTypeLoader().parse(u, SimpleDerivationSet.type, (XmlOptions) null);
        }

        public static SimpleDerivationSet parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (SimpleDerivationSet) XmlBeans.getContextTypeLoader().parse(u, SimpleDerivationSet.type, options);
        }

        public static SimpleDerivationSet parse(InputStream is) throws XmlException, IOException {
            return (SimpleDerivationSet) XmlBeans.getContextTypeLoader().parse(is, SimpleDerivationSet.type, (XmlOptions) null);
        }

        public static SimpleDerivationSet parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (SimpleDerivationSet) XmlBeans.getContextTypeLoader().parse(is, SimpleDerivationSet.type, options);
        }

        public static SimpleDerivationSet parse(Reader r) throws XmlException, IOException {
            return (SimpleDerivationSet) XmlBeans.getContextTypeLoader().parse(r, SimpleDerivationSet.type, (XmlOptions) null);
        }

        public static SimpleDerivationSet parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (SimpleDerivationSet) XmlBeans.getContextTypeLoader().parse(r, SimpleDerivationSet.type, options);
        }

        public static SimpleDerivationSet parse(XMLStreamReader sr) throws XmlException {
            return (SimpleDerivationSet) XmlBeans.getContextTypeLoader().parse(sr, SimpleDerivationSet.type, (XmlOptions) null);
        }

        public static SimpleDerivationSet parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (SimpleDerivationSet) XmlBeans.getContextTypeLoader().parse(sr, SimpleDerivationSet.type, options);
        }

        public static SimpleDerivationSet parse(Node node) throws XmlException {
            return (SimpleDerivationSet) XmlBeans.getContextTypeLoader().parse(node, SimpleDerivationSet.type, (XmlOptions) null);
        }

        public static SimpleDerivationSet parse(Node node, XmlOptions options) throws XmlException {
            return (SimpleDerivationSet) XmlBeans.getContextTypeLoader().parse(node, SimpleDerivationSet.type, options);
        }

        public static SimpleDerivationSet parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (SimpleDerivationSet) XmlBeans.getContextTypeLoader().parse(xis, SimpleDerivationSet.type, (XmlOptions) null);
        }

        public static SimpleDerivationSet parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (SimpleDerivationSet) XmlBeans.getContextTypeLoader().parse(xis, SimpleDerivationSet.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, SimpleDerivationSet.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, SimpleDerivationSet.type, options);
        }

        private Factory() {
        }
    }
}
