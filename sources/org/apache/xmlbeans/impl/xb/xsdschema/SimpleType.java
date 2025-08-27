package org.apache.xmlbeans.impl.xb.xsdschema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlNCName;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.xb.xsdschema.ListDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.UnionDocument;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/SimpleType.class */
public interface SimpleType extends Annotated {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(SimpleType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("simpletype0707type");

    RestrictionDocument.Restriction getRestriction();

    boolean isSetRestriction();

    void setRestriction(RestrictionDocument.Restriction restriction);

    RestrictionDocument.Restriction addNewRestriction();

    void unsetRestriction();

    ListDocument.List getList();

    boolean isSetList();

    void setList(ListDocument.List list);

    ListDocument.List addNewList();

    void unsetList();

    UnionDocument.Union getUnion();

    boolean isSetUnion();

    void setUnion(UnionDocument.Union union);

    UnionDocument.Union addNewUnion();

    void unsetUnion();

    Object getFinal();

    SimpleDerivationSet xgetFinal();

    boolean isSetFinal();

    void setFinal(Object obj);

    void xsetFinal(SimpleDerivationSet simpleDerivationSet);

    void unsetFinal();

    String getName();

    XmlNCName xgetName();

    boolean isSetName();

    void setName(String str);

    void xsetName(XmlNCName xmlNCName);

    void unsetName();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/SimpleType$Factory.class */
    public static final class Factory {
        public static SimpleType newInstance() {
            return (SimpleType) XmlBeans.getContextTypeLoader().newInstance(SimpleType.type, null);
        }

        public static SimpleType newInstance(XmlOptions options) {
            return (SimpleType) XmlBeans.getContextTypeLoader().newInstance(SimpleType.type, options);
        }

        public static SimpleType parse(String xmlAsString) throws XmlException {
            return (SimpleType) XmlBeans.getContextTypeLoader().parse(xmlAsString, SimpleType.type, (XmlOptions) null);
        }

        public static SimpleType parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (SimpleType) XmlBeans.getContextTypeLoader().parse(xmlAsString, SimpleType.type, options);
        }

        public static SimpleType parse(File file) throws XmlException, IOException {
            return (SimpleType) XmlBeans.getContextTypeLoader().parse(file, SimpleType.type, (XmlOptions) null);
        }

        public static SimpleType parse(File file, XmlOptions options) throws XmlException, IOException {
            return (SimpleType) XmlBeans.getContextTypeLoader().parse(file, SimpleType.type, options);
        }

        public static SimpleType parse(URL u) throws XmlException, IOException {
            return (SimpleType) XmlBeans.getContextTypeLoader().parse(u, SimpleType.type, (XmlOptions) null);
        }

        public static SimpleType parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (SimpleType) XmlBeans.getContextTypeLoader().parse(u, SimpleType.type, options);
        }

        public static SimpleType parse(InputStream is) throws XmlException, IOException {
            return (SimpleType) XmlBeans.getContextTypeLoader().parse(is, SimpleType.type, (XmlOptions) null);
        }

        public static SimpleType parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (SimpleType) XmlBeans.getContextTypeLoader().parse(is, SimpleType.type, options);
        }

        public static SimpleType parse(Reader r) throws XmlException, IOException {
            return (SimpleType) XmlBeans.getContextTypeLoader().parse(r, SimpleType.type, (XmlOptions) null);
        }

        public static SimpleType parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (SimpleType) XmlBeans.getContextTypeLoader().parse(r, SimpleType.type, options);
        }

        public static SimpleType parse(XMLStreamReader sr) throws XmlException {
            return (SimpleType) XmlBeans.getContextTypeLoader().parse(sr, SimpleType.type, (XmlOptions) null);
        }

        public static SimpleType parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (SimpleType) XmlBeans.getContextTypeLoader().parse(sr, SimpleType.type, options);
        }

        public static SimpleType parse(Node node) throws XmlException {
            return (SimpleType) XmlBeans.getContextTypeLoader().parse(node, SimpleType.type, (XmlOptions) null);
        }

        public static SimpleType parse(Node node, XmlOptions options) throws XmlException {
            return (SimpleType) XmlBeans.getContextTypeLoader().parse(node, SimpleType.type, options);
        }

        public static SimpleType parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (SimpleType) XmlBeans.getContextTypeLoader().parse(xis, SimpleType.type, (XmlOptions) null);
        }

        public static SimpleType parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (SimpleType) XmlBeans.getContextTypeLoader().parse(xis, SimpleType.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, SimpleType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, SimpleType.type, options);
        }

        private Factory() {
        }
    }
}
