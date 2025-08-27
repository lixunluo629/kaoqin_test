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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/FullDerivationSet.class */
public interface FullDerivationSet extends XmlAnySimpleType {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(FullDerivationSet.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("fullderivationsetd369type");

    Object getObjectValue();

    void setObjectValue(Object obj);

    Object objectValue();

    void objectSet(Object obj);

    SchemaType instanceType();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/FullDerivationSet$Member.class */
    public interface Member extends XmlToken {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Member.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("anon47e4type");
        public static final Enum ALL = Enum.forString("#all");
        public static final int INT_ALL = 1;

        StringEnumAbstractBase enumValue();

        void set(StringEnumAbstractBase stringEnumAbstractBase);

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/FullDerivationSet$Member$Enum.class */
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

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/FullDerivationSet$Member$Factory.class */
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

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/FullDerivationSet$Member2.class */
    public interface Member2 extends XmlAnySimpleType {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Member2.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("anonc683type");

        List getListValue();

        List xgetListValue();

        void setListValue(List list);

        List listValue();

        List xlistValue();

        void set(List list);

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/FullDerivationSet$Member2$Factory.class */
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

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/FullDerivationSet$Factory.class */
    public static final class Factory {
        public static FullDerivationSet newValue(Object obj) {
            return (FullDerivationSet) FullDerivationSet.type.newValue(obj);
        }

        public static FullDerivationSet newInstance() {
            return (FullDerivationSet) XmlBeans.getContextTypeLoader().newInstance(FullDerivationSet.type, null);
        }

        public static FullDerivationSet newInstance(XmlOptions options) {
            return (FullDerivationSet) XmlBeans.getContextTypeLoader().newInstance(FullDerivationSet.type, options);
        }

        public static FullDerivationSet parse(String xmlAsString) throws XmlException {
            return (FullDerivationSet) XmlBeans.getContextTypeLoader().parse(xmlAsString, FullDerivationSet.type, (XmlOptions) null);
        }

        public static FullDerivationSet parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (FullDerivationSet) XmlBeans.getContextTypeLoader().parse(xmlAsString, FullDerivationSet.type, options);
        }

        public static FullDerivationSet parse(File file) throws XmlException, IOException {
            return (FullDerivationSet) XmlBeans.getContextTypeLoader().parse(file, FullDerivationSet.type, (XmlOptions) null);
        }

        public static FullDerivationSet parse(File file, XmlOptions options) throws XmlException, IOException {
            return (FullDerivationSet) XmlBeans.getContextTypeLoader().parse(file, FullDerivationSet.type, options);
        }

        public static FullDerivationSet parse(URL u) throws XmlException, IOException {
            return (FullDerivationSet) XmlBeans.getContextTypeLoader().parse(u, FullDerivationSet.type, (XmlOptions) null);
        }

        public static FullDerivationSet parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (FullDerivationSet) XmlBeans.getContextTypeLoader().parse(u, FullDerivationSet.type, options);
        }

        public static FullDerivationSet parse(InputStream is) throws XmlException, IOException {
            return (FullDerivationSet) XmlBeans.getContextTypeLoader().parse(is, FullDerivationSet.type, (XmlOptions) null);
        }

        public static FullDerivationSet parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (FullDerivationSet) XmlBeans.getContextTypeLoader().parse(is, FullDerivationSet.type, options);
        }

        public static FullDerivationSet parse(Reader r) throws XmlException, IOException {
            return (FullDerivationSet) XmlBeans.getContextTypeLoader().parse(r, FullDerivationSet.type, (XmlOptions) null);
        }

        public static FullDerivationSet parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (FullDerivationSet) XmlBeans.getContextTypeLoader().parse(r, FullDerivationSet.type, options);
        }

        public static FullDerivationSet parse(XMLStreamReader sr) throws XmlException {
            return (FullDerivationSet) XmlBeans.getContextTypeLoader().parse(sr, FullDerivationSet.type, (XmlOptions) null);
        }

        public static FullDerivationSet parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (FullDerivationSet) XmlBeans.getContextTypeLoader().parse(sr, FullDerivationSet.type, options);
        }

        public static FullDerivationSet parse(Node node) throws XmlException {
            return (FullDerivationSet) XmlBeans.getContextTypeLoader().parse(node, FullDerivationSet.type, (XmlOptions) null);
        }

        public static FullDerivationSet parse(Node node, XmlOptions options) throws XmlException {
            return (FullDerivationSet) XmlBeans.getContextTypeLoader().parse(node, FullDerivationSet.type, options);
        }

        public static FullDerivationSet parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (FullDerivationSet) XmlBeans.getContextTypeLoader().parse(xis, FullDerivationSet.type, (XmlOptions) null);
        }

        public static FullDerivationSet parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (FullDerivationSet) XmlBeans.getContextTypeLoader().parse(xis, FullDerivationSet.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, FullDerivationSet.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, FullDerivationSet.type, options);
        }

        private Factory() {
        }
    }
}
