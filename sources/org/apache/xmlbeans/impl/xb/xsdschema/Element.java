package org.apache.xmlbeans.impl.xb.xsdschema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigInteger;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlNCName;
import org.apache.xmlbeans.XmlNonNegativeInteger;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlQName;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.xb.xsdschema.FormChoice;
import org.apache.xmlbeans.impl.xb.xsdschema.KeyrefDocument;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/Element.class */
public interface Element extends Annotated {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Element.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("elementd189type");

    LocalSimpleType getSimpleType();

    boolean isSetSimpleType();

    void setSimpleType(LocalSimpleType localSimpleType);

    LocalSimpleType addNewSimpleType();

    void unsetSimpleType();

    LocalComplexType getComplexType();

    boolean isSetComplexType();

    void setComplexType(LocalComplexType localComplexType);

    LocalComplexType addNewComplexType();

    void unsetComplexType();

    Keybase[] getUniqueArray();

    Keybase getUniqueArray(int i);

    int sizeOfUniqueArray();

    void setUniqueArray(Keybase[] keybaseArr);

    void setUniqueArray(int i, Keybase keybase);

    Keybase insertNewUnique(int i);

    Keybase addNewUnique();

    void removeUnique(int i);

    Keybase[] getKeyArray();

    Keybase getKeyArray(int i);

    int sizeOfKeyArray();

    void setKeyArray(Keybase[] keybaseArr);

    void setKeyArray(int i, Keybase keybase);

    Keybase insertNewKey(int i);

    Keybase addNewKey();

    void removeKey(int i);

    KeyrefDocument.Keyref[] getKeyrefArray();

    KeyrefDocument.Keyref getKeyrefArray(int i);

    int sizeOfKeyrefArray();

    void setKeyrefArray(KeyrefDocument.Keyref[] keyrefArr);

    void setKeyrefArray(int i, KeyrefDocument.Keyref keyref);

    KeyrefDocument.Keyref insertNewKeyref(int i);

    KeyrefDocument.Keyref addNewKeyref();

    void removeKeyref(int i);

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

    QName getSubstitutionGroup();

    XmlQName xgetSubstitutionGroup();

    boolean isSetSubstitutionGroup();

    void setSubstitutionGroup(QName qName);

    void xsetSubstitutionGroup(XmlQName xmlQName);

    void unsetSubstitutionGroup();

    BigInteger getMinOccurs();

    XmlNonNegativeInteger xgetMinOccurs();

    boolean isSetMinOccurs();

    void setMinOccurs(BigInteger bigInteger);

    void xsetMinOccurs(XmlNonNegativeInteger xmlNonNegativeInteger);

    void unsetMinOccurs();

    Object getMaxOccurs();

    AllNNI xgetMaxOccurs();

    boolean isSetMaxOccurs();

    void setMaxOccurs(Object obj);

    void xsetMaxOccurs(AllNNI allNNI);

    void unsetMaxOccurs();

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

    boolean getNillable();

    XmlBoolean xgetNillable();

    boolean isSetNillable();

    void setNillable(boolean z);

    void xsetNillable(XmlBoolean xmlBoolean);

    void unsetNillable();

    boolean getAbstract();

    XmlBoolean xgetAbstract();

    boolean isSetAbstract();

    void setAbstract(boolean z);

    void xsetAbstract(XmlBoolean xmlBoolean);

    void unsetAbstract();

    Object getFinal();

    DerivationSet xgetFinal();

    boolean isSetFinal();

    void setFinal(Object obj);

    void xsetFinal(DerivationSet derivationSet);

    void unsetFinal();

    Object getBlock();

    BlockSet xgetBlock();

    boolean isSetBlock();

    void setBlock(Object obj);

    void xsetBlock(BlockSet blockSet);

    void unsetBlock();

    FormChoice.Enum getForm();

    FormChoice xgetForm();

    boolean isSetForm();

    void setForm(FormChoice.Enum r1);

    void xsetForm(FormChoice formChoice);

    void unsetForm();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/Element$Factory.class */
    public static final class Factory {
        public static Element newInstance() {
            return (Element) XmlBeans.getContextTypeLoader().newInstance(Element.type, null);
        }

        public static Element newInstance(XmlOptions options) {
            return (Element) XmlBeans.getContextTypeLoader().newInstance(Element.type, options);
        }

        public static Element parse(String xmlAsString) throws XmlException {
            return (Element) XmlBeans.getContextTypeLoader().parse(xmlAsString, Element.type, (XmlOptions) null);
        }

        public static Element parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (Element) XmlBeans.getContextTypeLoader().parse(xmlAsString, Element.type, options);
        }

        public static Element parse(File file) throws XmlException, IOException {
            return (Element) XmlBeans.getContextTypeLoader().parse(file, Element.type, (XmlOptions) null);
        }

        public static Element parse(File file, XmlOptions options) throws XmlException, IOException {
            return (Element) XmlBeans.getContextTypeLoader().parse(file, Element.type, options);
        }

        public static Element parse(URL u) throws XmlException, IOException {
            return (Element) XmlBeans.getContextTypeLoader().parse(u, Element.type, (XmlOptions) null);
        }

        public static Element parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (Element) XmlBeans.getContextTypeLoader().parse(u, Element.type, options);
        }

        public static Element parse(InputStream is) throws XmlException, IOException {
            return (Element) XmlBeans.getContextTypeLoader().parse(is, Element.type, (XmlOptions) null);
        }

        public static Element parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (Element) XmlBeans.getContextTypeLoader().parse(is, Element.type, options);
        }

        public static Element parse(Reader r) throws XmlException, IOException {
            return (Element) XmlBeans.getContextTypeLoader().parse(r, Element.type, (XmlOptions) null);
        }

        public static Element parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (Element) XmlBeans.getContextTypeLoader().parse(r, Element.type, options);
        }

        public static Element parse(XMLStreamReader sr) throws XmlException {
            return (Element) XmlBeans.getContextTypeLoader().parse(sr, Element.type, (XmlOptions) null);
        }

        public static Element parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (Element) XmlBeans.getContextTypeLoader().parse(sr, Element.type, options);
        }

        public static Element parse(Node node) throws XmlException {
            return (Element) XmlBeans.getContextTypeLoader().parse(node, Element.type, (XmlOptions) null);
        }

        public static Element parse(Node node, XmlOptions options) throws XmlException {
            return (Element) XmlBeans.getContextTypeLoader().parse(node, Element.type, options);
        }

        public static Element parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (Element) XmlBeans.getContextTypeLoader().parse(xis, Element.type, (XmlOptions) null);
        }

        public static Element parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (Element) XmlBeans.getContextTypeLoader().parse(xis, Element.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Element.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Element.type, options);
        }

        private Factory() {
        }
    }
}
