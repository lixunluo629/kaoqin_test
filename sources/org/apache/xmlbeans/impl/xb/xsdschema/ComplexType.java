package org.apache.xmlbeans.impl.xb.xsdschema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlNCName;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.xb.xsdschema.ComplexContentDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.SimpleContentDocument;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/ComplexType.class */
public interface ComplexType extends Annotated {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(ComplexType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("complextype5dbbtype");

    SimpleContentDocument.SimpleContent getSimpleContent();

    boolean isSetSimpleContent();

    void setSimpleContent(SimpleContentDocument.SimpleContent simpleContent);

    SimpleContentDocument.SimpleContent addNewSimpleContent();

    void unsetSimpleContent();

    ComplexContentDocument.ComplexContent getComplexContent();

    boolean isSetComplexContent();

    void setComplexContent(ComplexContentDocument.ComplexContent complexContent);

    ComplexContentDocument.ComplexContent addNewComplexContent();

    void unsetComplexContent();

    GroupRef getGroup();

    boolean isSetGroup();

    void setGroup(GroupRef groupRef);

    GroupRef addNewGroup();

    void unsetGroup();

    All getAll();

    boolean isSetAll();

    void setAll(All all);

    All addNewAll();

    void unsetAll();

    ExplicitGroup getChoice();

    boolean isSetChoice();

    void setChoice(ExplicitGroup explicitGroup);

    ExplicitGroup addNewChoice();

    void unsetChoice();

    ExplicitGroup getSequence();

    boolean isSetSequence();

    void setSequence(ExplicitGroup explicitGroup);

    ExplicitGroup addNewSequence();

    void unsetSequence();

    Attribute[] getAttributeArray();

    Attribute getAttributeArray(int i);

    int sizeOfAttributeArray();

    void setAttributeArray(Attribute[] attributeArr);

    void setAttributeArray(int i, Attribute attribute);

    Attribute insertNewAttribute(int i);

    Attribute addNewAttribute();

    void removeAttribute(int i);

    AttributeGroupRef[] getAttributeGroupArray();

    AttributeGroupRef getAttributeGroupArray(int i);

    int sizeOfAttributeGroupArray();

    void setAttributeGroupArray(AttributeGroupRef[] attributeGroupRefArr);

    void setAttributeGroupArray(int i, AttributeGroupRef attributeGroupRef);

    AttributeGroupRef insertNewAttributeGroup(int i);

    AttributeGroupRef addNewAttributeGroup();

    void removeAttributeGroup(int i);

    Wildcard getAnyAttribute();

    boolean isSetAnyAttribute();

    void setAnyAttribute(Wildcard wildcard);

    Wildcard addNewAnyAttribute();

    void unsetAnyAttribute();

    String getName();

    XmlNCName xgetName();

    boolean isSetName();

    void setName(String str);

    void xsetName(XmlNCName xmlNCName);

    void unsetName();

    boolean getMixed();

    XmlBoolean xgetMixed();

    boolean isSetMixed();

    void setMixed(boolean z);

    void xsetMixed(XmlBoolean xmlBoolean);

    void unsetMixed();

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

    DerivationSet xgetBlock();

    boolean isSetBlock();

    void setBlock(Object obj);

    void xsetBlock(DerivationSet derivationSet);

    void unsetBlock();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/ComplexType$Factory.class */
    public static final class Factory {
        public static ComplexType newInstance() {
            return (ComplexType) XmlBeans.getContextTypeLoader().newInstance(ComplexType.type, null);
        }

        public static ComplexType newInstance(XmlOptions options) {
            return (ComplexType) XmlBeans.getContextTypeLoader().newInstance(ComplexType.type, options);
        }

        public static ComplexType parse(String xmlAsString) throws XmlException {
            return (ComplexType) XmlBeans.getContextTypeLoader().parse(xmlAsString, ComplexType.type, (XmlOptions) null);
        }

        public static ComplexType parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (ComplexType) XmlBeans.getContextTypeLoader().parse(xmlAsString, ComplexType.type, options);
        }

        public static ComplexType parse(File file) throws XmlException, IOException {
            return (ComplexType) XmlBeans.getContextTypeLoader().parse(file, ComplexType.type, (XmlOptions) null);
        }

        public static ComplexType parse(File file, XmlOptions options) throws XmlException, IOException {
            return (ComplexType) XmlBeans.getContextTypeLoader().parse(file, ComplexType.type, options);
        }

        public static ComplexType parse(URL u) throws XmlException, IOException {
            return (ComplexType) XmlBeans.getContextTypeLoader().parse(u, ComplexType.type, (XmlOptions) null);
        }

        public static ComplexType parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (ComplexType) XmlBeans.getContextTypeLoader().parse(u, ComplexType.type, options);
        }

        public static ComplexType parse(InputStream is) throws XmlException, IOException {
            return (ComplexType) XmlBeans.getContextTypeLoader().parse(is, ComplexType.type, (XmlOptions) null);
        }

        public static ComplexType parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (ComplexType) XmlBeans.getContextTypeLoader().parse(is, ComplexType.type, options);
        }

        public static ComplexType parse(Reader r) throws XmlException, IOException {
            return (ComplexType) XmlBeans.getContextTypeLoader().parse(r, ComplexType.type, (XmlOptions) null);
        }

        public static ComplexType parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (ComplexType) XmlBeans.getContextTypeLoader().parse(r, ComplexType.type, options);
        }

        public static ComplexType parse(XMLStreamReader sr) throws XmlException {
            return (ComplexType) XmlBeans.getContextTypeLoader().parse(sr, ComplexType.type, (XmlOptions) null);
        }

        public static ComplexType parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (ComplexType) XmlBeans.getContextTypeLoader().parse(sr, ComplexType.type, options);
        }

        public static ComplexType parse(Node node) throws XmlException {
            return (ComplexType) XmlBeans.getContextTypeLoader().parse(node, ComplexType.type, (XmlOptions) null);
        }

        public static ComplexType parse(Node node, XmlOptions options) throws XmlException {
            return (ComplexType) XmlBeans.getContextTypeLoader().parse(node, ComplexType.type, options);
        }

        public static ComplexType parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (ComplexType) XmlBeans.getContextTypeLoader().parse(xis, ComplexType.type, (XmlOptions) null);
        }

        public static ComplexType parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (ComplexType) XmlBeans.getContextTypeLoader().parse(xis, ComplexType.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, ComplexType.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, ComplexType.type, options);
        }

        private Factory() {
        }
    }
}
