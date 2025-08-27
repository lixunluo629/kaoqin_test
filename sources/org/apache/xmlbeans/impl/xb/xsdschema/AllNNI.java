package org.apache.xmlbeans.impl.xb.xsdschema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.StringEnumAbstractBase;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlNMTOKEN;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/AllNNI.class */
public interface AllNNI extends XmlAnySimpleType {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(AllNNI.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("allnni78cbtype");

    Object getObjectValue();

    void setObjectValue(Object obj);

    Object objectValue();

    void objectSet(Object obj);

    SchemaType instanceType();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/AllNNI$Member.class */
    public interface Member extends XmlNMTOKEN {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Member.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("anon0330type");
        public static final Enum UNBOUNDED = Enum.forString("unbounded");
        public static final int INT_UNBOUNDED = 1;

        StringEnumAbstractBase enumValue();

        void set(StringEnumAbstractBase stringEnumAbstractBase);

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/AllNNI$Member$Enum.class */
        public static final class Enum extends StringEnumAbstractBase {
            static final int INT_UNBOUNDED = 1;
            public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("unbounded", 1)});
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

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/AllNNI$Member$Factory.class */
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

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/AllNNI$Factory.class */
    public static final class Factory {
        public static AllNNI newValue(Object obj) {
            return (AllNNI) AllNNI.type.newValue(obj);
        }

        public static AllNNI newInstance() {
            return (AllNNI) XmlBeans.getContextTypeLoader().newInstance(AllNNI.type, null);
        }

        public static AllNNI newInstance(XmlOptions options) {
            return (AllNNI) XmlBeans.getContextTypeLoader().newInstance(AllNNI.type, options);
        }

        public static AllNNI parse(String xmlAsString) throws XmlException {
            return (AllNNI) XmlBeans.getContextTypeLoader().parse(xmlAsString, AllNNI.type, (XmlOptions) null);
        }

        public static AllNNI parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (AllNNI) XmlBeans.getContextTypeLoader().parse(xmlAsString, AllNNI.type, options);
        }

        public static AllNNI parse(File file) throws XmlException, IOException {
            return (AllNNI) XmlBeans.getContextTypeLoader().parse(file, AllNNI.type, (XmlOptions) null);
        }

        public static AllNNI parse(File file, XmlOptions options) throws XmlException, IOException {
            return (AllNNI) XmlBeans.getContextTypeLoader().parse(file, AllNNI.type, options);
        }

        public static AllNNI parse(URL u) throws XmlException, IOException {
            return (AllNNI) XmlBeans.getContextTypeLoader().parse(u, AllNNI.type, (XmlOptions) null);
        }

        public static AllNNI parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (AllNNI) XmlBeans.getContextTypeLoader().parse(u, AllNNI.type, options);
        }

        public static AllNNI parse(InputStream is) throws XmlException, IOException {
            return (AllNNI) XmlBeans.getContextTypeLoader().parse(is, AllNNI.type, (XmlOptions) null);
        }

        public static AllNNI parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (AllNNI) XmlBeans.getContextTypeLoader().parse(is, AllNNI.type, options);
        }

        public static AllNNI parse(Reader r) throws XmlException, IOException {
            return (AllNNI) XmlBeans.getContextTypeLoader().parse(r, AllNNI.type, (XmlOptions) null);
        }

        public static AllNNI parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (AllNNI) XmlBeans.getContextTypeLoader().parse(r, AllNNI.type, options);
        }

        public static AllNNI parse(XMLStreamReader sr) throws XmlException {
            return (AllNNI) XmlBeans.getContextTypeLoader().parse(sr, AllNNI.type, (XmlOptions) null);
        }

        public static AllNNI parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (AllNNI) XmlBeans.getContextTypeLoader().parse(sr, AllNNI.type, options);
        }

        public static AllNNI parse(Node node) throws XmlException {
            return (AllNNI) XmlBeans.getContextTypeLoader().parse(node, AllNNI.type, (XmlOptions) null);
        }

        public static AllNNI parse(Node node, XmlOptions options) throws XmlException {
            return (AllNNI) XmlBeans.getContextTypeLoader().parse(node, AllNNI.type, options);
        }

        public static AllNNI parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (AllNNI) XmlBeans.getContextTypeLoader().parse(xis, AllNNI.type, (XmlOptions) null);
        }

        public static AllNNI parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (AllNNI) XmlBeans.getContextTypeLoader().parse(xis, AllNNI.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, AllNNI.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, AllNNI.type, options);
        }

        private Factory() {
        }
    }
}
