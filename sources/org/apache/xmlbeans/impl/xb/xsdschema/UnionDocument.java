package org.apache.xmlbeans.impl.xb.xsdschema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/UnionDocument.class */
public interface UnionDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(UnionDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("union5866doctype");

    Union getUnion();

    void setUnion(Union union);

    Union addNewUnion();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/UnionDocument$Union.class */
    public interface Union extends Annotated {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Union.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("union498belemtype");

        LocalSimpleType[] getSimpleTypeArray();

        LocalSimpleType getSimpleTypeArray(int i);

        int sizeOfSimpleTypeArray();

        void setSimpleTypeArray(LocalSimpleType[] localSimpleTypeArr);

        void setSimpleTypeArray(int i, LocalSimpleType localSimpleType);

        LocalSimpleType insertNewSimpleType(int i);

        LocalSimpleType addNewSimpleType();

        void removeSimpleType(int i);

        List getMemberTypes();

        MemberTypes xgetMemberTypes();

        boolean isSetMemberTypes();

        void setMemberTypes(List list);

        void xsetMemberTypes(MemberTypes memberTypes);

        void unsetMemberTypes();

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/UnionDocument$Union$MemberTypes.class */
        public interface MemberTypes extends XmlAnySimpleType {
            public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(MemberTypes.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("membertypes2404attrtype");

            List getListValue();

            List xgetListValue();

            void setListValue(List list);

            List listValue();

            List xlistValue();

            void set(List list);

            /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/UnionDocument$Union$MemberTypes$Factory.class */
            public static final class Factory {
                public static MemberTypes newValue(Object obj) {
                    return (MemberTypes) MemberTypes.type.newValue(obj);
                }

                public static MemberTypes newInstance() {
                    return (MemberTypes) XmlBeans.getContextTypeLoader().newInstance(MemberTypes.type, null);
                }

                public static MemberTypes newInstance(XmlOptions options) {
                    return (MemberTypes) XmlBeans.getContextTypeLoader().newInstance(MemberTypes.type, options);
                }

                private Factory() {
                }
            }
        }

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/UnionDocument$Union$Factory.class */
        public static final class Factory {
            public static Union newInstance() {
                return (Union) XmlBeans.getContextTypeLoader().newInstance(Union.type, null);
            }

            public static Union newInstance(XmlOptions options) {
                return (Union) XmlBeans.getContextTypeLoader().newInstance(Union.type, options);
            }

            private Factory() {
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/UnionDocument$Factory.class */
    public static final class Factory {
        public static UnionDocument newInstance() {
            return (UnionDocument) XmlBeans.getContextTypeLoader().newInstance(UnionDocument.type, null);
        }

        public static UnionDocument newInstance(XmlOptions options) {
            return (UnionDocument) XmlBeans.getContextTypeLoader().newInstance(UnionDocument.type, options);
        }

        public static UnionDocument parse(String xmlAsString) throws XmlException {
            return (UnionDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, UnionDocument.type, (XmlOptions) null);
        }

        public static UnionDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (UnionDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, UnionDocument.type, options);
        }

        public static UnionDocument parse(File file) throws XmlException, IOException {
            return (UnionDocument) XmlBeans.getContextTypeLoader().parse(file, UnionDocument.type, (XmlOptions) null);
        }

        public static UnionDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (UnionDocument) XmlBeans.getContextTypeLoader().parse(file, UnionDocument.type, options);
        }

        public static UnionDocument parse(URL u) throws XmlException, IOException {
            return (UnionDocument) XmlBeans.getContextTypeLoader().parse(u, UnionDocument.type, (XmlOptions) null);
        }

        public static UnionDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (UnionDocument) XmlBeans.getContextTypeLoader().parse(u, UnionDocument.type, options);
        }

        public static UnionDocument parse(InputStream is) throws XmlException, IOException {
            return (UnionDocument) XmlBeans.getContextTypeLoader().parse(is, UnionDocument.type, (XmlOptions) null);
        }

        public static UnionDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (UnionDocument) XmlBeans.getContextTypeLoader().parse(is, UnionDocument.type, options);
        }

        public static UnionDocument parse(Reader r) throws XmlException, IOException {
            return (UnionDocument) XmlBeans.getContextTypeLoader().parse(r, UnionDocument.type, (XmlOptions) null);
        }

        public static UnionDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (UnionDocument) XmlBeans.getContextTypeLoader().parse(r, UnionDocument.type, options);
        }

        public static UnionDocument parse(XMLStreamReader sr) throws XmlException {
            return (UnionDocument) XmlBeans.getContextTypeLoader().parse(sr, UnionDocument.type, (XmlOptions) null);
        }

        public static UnionDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (UnionDocument) XmlBeans.getContextTypeLoader().parse(sr, UnionDocument.type, options);
        }

        public static UnionDocument parse(Node node) throws XmlException {
            return (UnionDocument) XmlBeans.getContextTypeLoader().parse(node, UnionDocument.type, (XmlOptions) null);
        }

        public static UnionDocument parse(Node node, XmlOptions options) throws XmlException {
            return (UnionDocument) XmlBeans.getContextTypeLoader().parse(node, UnionDocument.type, options);
        }

        public static UnionDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (UnionDocument) XmlBeans.getContextTypeLoader().parse(xis, UnionDocument.type, (XmlOptions) null);
        }

        public static UnionDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (UnionDocument) XmlBeans.getContextTypeLoader().parse(xis, UnionDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, UnionDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, UnionDocument.type, options);
        }

        private Factory() {
        }
    }
}
