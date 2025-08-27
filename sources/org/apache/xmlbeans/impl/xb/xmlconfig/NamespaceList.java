package org.apache.xmlbeans.impl.xb.xmlconfig;

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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/NamespaceList.class */
public interface NamespaceList extends XmlAnySimpleType {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(NamespaceList.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLCONFIG").resolveHandle("namespacelist20datype");

    Object getObjectValue();

    void setObjectValue(Object obj);

    Object objectValue();

    void objectSet(Object obj);

    SchemaType instanceType();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/NamespaceList$Member.class */
    public interface Member extends XmlToken {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Member.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLCONFIG").resolveHandle("anonc6fftype");
        public static final Enum ANY = Enum.forString("##any");
        public static final int INT_ANY = 1;

        StringEnumAbstractBase enumValue();

        void set(StringEnumAbstractBase stringEnumAbstractBase);

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/NamespaceList$Member$Enum.class */
        public static final class Enum extends StringEnumAbstractBase {
            static final int INT_ANY = 1;
            public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("##any", 1)});
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

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/NamespaceList$Member$Factory.class */
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

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/NamespaceList$Member2.class */
    public interface Member2 extends XmlAnySimpleType {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Member2.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLCONFIG").resolveHandle("anon5680type");

        List getListValue();

        List xgetListValue();

        void setListValue(List list);

        List listValue();

        List xlistValue();

        void set(List list);

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/NamespaceList$Member2$Item.class */
        public interface Item extends XmlAnySimpleType {
            public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Item.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLCONFIG").resolveHandle("anon0798type");

            Object getObjectValue();

            void setObjectValue(Object obj);

            Object objectValue();

            void objectSet(Object obj);

            SchemaType instanceType();

            /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/NamespaceList$Member2$Item$Member.class */
            public interface Member extends XmlToken {
                public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Member.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLCONFIG").resolveHandle("anon1dd3type");
                public static final Enum LOCAL = Enum.forString("##local");
                public static final int INT_LOCAL = 1;

                StringEnumAbstractBase enumValue();

                void set(StringEnumAbstractBase stringEnumAbstractBase);

                /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/NamespaceList$Member2$Item$Member$Enum.class */
                public static final class Enum extends StringEnumAbstractBase {
                    static final int INT_LOCAL = 1;
                    public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("##local", 1)});
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

                /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/NamespaceList$Member2$Item$Member$Factory.class */
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

            /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/NamespaceList$Member2$Item$Factory.class */
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

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/NamespaceList$Member2$Factory.class */
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

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/NamespaceList$Factory.class */
    public static final class Factory {
        public static NamespaceList newValue(Object obj) {
            return (NamespaceList) NamespaceList.type.newValue(obj);
        }

        public static NamespaceList newInstance() {
            return (NamespaceList) XmlBeans.getContextTypeLoader().newInstance(NamespaceList.type, null);
        }

        public static NamespaceList newInstance(XmlOptions options) {
            return (NamespaceList) XmlBeans.getContextTypeLoader().newInstance(NamespaceList.type, options);
        }

        public static NamespaceList parse(String xmlAsString) throws XmlException {
            return (NamespaceList) XmlBeans.getContextTypeLoader().parse(xmlAsString, NamespaceList.type, (XmlOptions) null);
        }

        public static NamespaceList parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (NamespaceList) XmlBeans.getContextTypeLoader().parse(xmlAsString, NamespaceList.type, options);
        }

        public static NamespaceList parse(File file) throws XmlException, IOException {
            return (NamespaceList) XmlBeans.getContextTypeLoader().parse(file, NamespaceList.type, (XmlOptions) null);
        }

        public static NamespaceList parse(File file, XmlOptions options) throws XmlException, IOException {
            return (NamespaceList) XmlBeans.getContextTypeLoader().parse(file, NamespaceList.type, options);
        }

        public static NamespaceList parse(URL u) throws XmlException, IOException {
            return (NamespaceList) XmlBeans.getContextTypeLoader().parse(u, NamespaceList.type, (XmlOptions) null);
        }

        public static NamespaceList parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (NamespaceList) XmlBeans.getContextTypeLoader().parse(u, NamespaceList.type, options);
        }

        public static NamespaceList parse(InputStream is) throws XmlException, IOException {
            return (NamespaceList) XmlBeans.getContextTypeLoader().parse(is, NamespaceList.type, (XmlOptions) null);
        }

        public static NamespaceList parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (NamespaceList) XmlBeans.getContextTypeLoader().parse(is, NamespaceList.type, options);
        }

        public static NamespaceList parse(Reader r) throws XmlException, IOException {
            return (NamespaceList) XmlBeans.getContextTypeLoader().parse(r, NamespaceList.type, (XmlOptions) null);
        }

        public static NamespaceList parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (NamespaceList) XmlBeans.getContextTypeLoader().parse(r, NamespaceList.type, options);
        }

        public static NamespaceList parse(XMLStreamReader sr) throws XmlException {
            return (NamespaceList) XmlBeans.getContextTypeLoader().parse(sr, NamespaceList.type, (XmlOptions) null);
        }

        public static NamespaceList parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (NamespaceList) XmlBeans.getContextTypeLoader().parse(sr, NamespaceList.type, options);
        }

        public static NamespaceList parse(Node node) throws XmlException {
            return (NamespaceList) XmlBeans.getContextTypeLoader().parse(node, NamespaceList.type, (XmlOptions) null);
        }

        public static NamespaceList parse(Node node, XmlOptions options) throws XmlException {
            return (NamespaceList) XmlBeans.getContextTypeLoader().parse(node, NamespaceList.type, options);
        }

        public static NamespaceList parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (NamespaceList) XmlBeans.getContextTypeLoader().parse(xis, NamespaceList.type, (XmlOptions) null);
        }

        public static NamespaceList parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (NamespaceList) XmlBeans.getContextTypeLoader().parse(xis, NamespaceList.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, NamespaceList.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, NamespaceList.type, options);
        }

        private Factory() {
        }
    }
}
