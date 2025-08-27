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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/JavaNameList.class */
public interface JavaNameList extends XmlAnySimpleType {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(JavaNameList.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLCONFIG").resolveHandle("javanamelistbcfetype");

    Object getObjectValue();

    void setObjectValue(Object obj);

    Object objectValue();

    void objectSet(Object obj);

    SchemaType instanceType();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/JavaNameList$Member.class */
    public interface Member extends XmlToken {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Member.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLCONFIG").resolveHandle("anon3e39type");
        public static final Enum X = Enum.forString("*");
        public static final int INT_X = 1;

        StringEnumAbstractBase enumValue();

        void set(StringEnumAbstractBase stringEnumAbstractBase);

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/JavaNameList$Member$Enum.class */
        public static final class Enum extends StringEnumAbstractBase {
            static final int INT_X = 1;
            public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("*", 1)});
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

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/JavaNameList$Member$Factory.class */
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

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/JavaNameList$Member2.class */
    public interface Member2 extends XmlAnySimpleType {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Member2.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLCONFIG").resolveHandle("anon3a98type");

        List getListValue();

        List xgetListValue();

        void setListValue(List list);

        List listValue();

        List xlistValue();

        void set(List list);

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/JavaNameList$Member2$Factory.class */
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

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xmlconfig/JavaNameList$Factory.class */
    public static final class Factory {
        public static JavaNameList newValue(Object obj) {
            return (JavaNameList) JavaNameList.type.newValue(obj);
        }

        public static JavaNameList newInstance() {
            return (JavaNameList) XmlBeans.getContextTypeLoader().newInstance(JavaNameList.type, null);
        }

        public static JavaNameList newInstance(XmlOptions options) {
            return (JavaNameList) XmlBeans.getContextTypeLoader().newInstance(JavaNameList.type, options);
        }

        public static JavaNameList parse(String xmlAsString) throws XmlException {
            return (JavaNameList) XmlBeans.getContextTypeLoader().parse(xmlAsString, JavaNameList.type, (XmlOptions) null);
        }

        public static JavaNameList parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (JavaNameList) XmlBeans.getContextTypeLoader().parse(xmlAsString, JavaNameList.type, options);
        }

        public static JavaNameList parse(File file) throws XmlException, IOException {
            return (JavaNameList) XmlBeans.getContextTypeLoader().parse(file, JavaNameList.type, (XmlOptions) null);
        }

        public static JavaNameList parse(File file, XmlOptions options) throws XmlException, IOException {
            return (JavaNameList) XmlBeans.getContextTypeLoader().parse(file, JavaNameList.type, options);
        }

        public static JavaNameList parse(URL u) throws XmlException, IOException {
            return (JavaNameList) XmlBeans.getContextTypeLoader().parse(u, JavaNameList.type, (XmlOptions) null);
        }

        public static JavaNameList parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (JavaNameList) XmlBeans.getContextTypeLoader().parse(u, JavaNameList.type, options);
        }

        public static JavaNameList parse(InputStream is) throws XmlException, IOException {
            return (JavaNameList) XmlBeans.getContextTypeLoader().parse(is, JavaNameList.type, (XmlOptions) null);
        }

        public static JavaNameList parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (JavaNameList) XmlBeans.getContextTypeLoader().parse(is, JavaNameList.type, options);
        }

        public static JavaNameList parse(Reader r) throws XmlException, IOException {
            return (JavaNameList) XmlBeans.getContextTypeLoader().parse(r, JavaNameList.type, (XmlOptions) null);
        }

        public static JavaNameList parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (JavaNameList) XmlBeans.getContextTypeLoader().parse(r, JavaNameList.type, options);
        }

        public static JavaNameList parse(XMLStreamReader sr) throws XmlException {
            return (JavaNameList) XmlBeans.getContextTypeLoader().parse(sr, JavaNameList.type, (XmlOptions) null);
        }

        public static JavaNameList parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (JavaNameList) XmlBeans.getContextTypeLoader().parse(sr, JavaNameList.type, options);
        }

        public static JavaNameList parse(Node node) throws XmlException {
            return (JavaNameList) XmlBeans.getContextTypeLoader().parse(node, JavaNameList.type, (XmlOptions) null);
        }

        public static JavaNameList parse(Node node, XmlOptions options) throws XmlException {
            return (JavaNameList) XmlBeans.getContextTypeLoader().parse(node, JavaNameList.type, options);
        }

        public static JavaNameList parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (JavaNameList) XmlBeans.getContextTypeLoader().parse(xis, JavaNameList.type, (XmlOptions) null);
        }

        public static JavaNameList parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (JavaNameList) XmlBeans.getContextTypeLoader().parse(xis, JavaNameList.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, JavaNameList.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, JavaNameList.type, options);
        }

        private Factory() {
        }
    }
}
