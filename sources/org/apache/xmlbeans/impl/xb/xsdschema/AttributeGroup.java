package org.apache.xmlbeans.impl.xb.xsdschema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlNCName;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlQName;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/AttributeGroup.class */
public interface AttributeGroup extends Annotated {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(AttributeGroup.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("attributegroupe530type");

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

    QName getRef();

    XmlQName xgetRef();

    boolean isSetRef();

    void setRef(QName qName);

    void xsetRef(XmlQName xmlQName);

    void unsetRef();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/AttributeGroup$Factory.class */
    public static final class Factory {
        public static AttributeGroup newInstance() {
            return (AttributeGroup) XmlBeans.getContextTypeLoader().newInstance(AttributeGroup.type, null);
        }

        public static AttributeGroup newInstance(XmlOptions options) {
            return (AttributeGroup) XmlBeans.getContextTypeLoader().newInstance(AttributeGroup.type, options);
        }

        public static AttributeGroup parse(String xmlAsString) throws XmlException {
            return (AttributeGroup) XmlBeans.getContextTypeLoader().parse(xmlAsString, AttributeGroup.type, (XmlOptions) null);
        }

        public static AttributeGroup parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (AttributeGroup) XmlBeans.getContextTypeLoader().parse(xmlAsString, AttributeGroup.type, options);
        }

        public static AttributeGroup parse(File file) throws XmlException, IOException {
            return (AttributeGroup) XmlBeans.getContextTypeLoader().parse(file, AttributeGroup.type, (XmlOptions) null);
        }

        public static AttributeGroup parse(File file, XmlOptions options) throws XmlException, IOException {
            return (AttributeGroup) XmlBeans.getContextTypeLoader().parse(file, AttributeGroup.type, options);
        }

        public static AttributeGroup parse(URL u) throws XmlException, IOException {
            return (AttributeGroup) XmlBeans.getContextTypeLoader().parse(u, AttributeGroup.type, (XmlOptions) null);
        }

        public static AttributeGroup parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (AttributeGroup) XmlBeans.getContextTypeLoader().parse(u, AttributeGroup.type, options);
        }

        public static AttributeGroup parse(InputStream is) throws XmlException, IOException {
            return (AttributeGroup) XmlBeans.getContextTypeLoader().parse(is, AttributeGroup.type, (XmlOptions) null);
        }

        public static AttributeGroup parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (AttributeGroup) XmlBeans.getContextTypeLoader().parse(is, AttributeGroup.type, options);
        }

        public static AttributeGroup parse(Reader r) throws XmlException, IOException {
            return (AttributeGroup) XmlBeans.getContextTypeLoader().parse(r, AttributeGroup.type, (XmlOptions) null);
        }

        public static AttributeGroup parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (AttributeGroup) XmlBeans.getContextTypeLoader().parse(r, AttributeGroup.type, options);
        }

        public static AttributeGroup parse(XMLStreamReader sr) throws XmlException {
            return (AttributeGroup) XmlBeans.getContextTypeLoader().parse(sr, AttributeGroup.type, (XmlOptions) null);
        }

        public static AttributeGroup parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (AttributeGroup) XmlBeans.getContextTypeLoader().parse(sr, AttributeGroup.type, options);
        }

        public static AttributeGroup parse(Node node) throws XmlException {
            return (AttributeGroup) XmlBeans.getContextTypeLoader().parse(node, AttributeGroup.type, (XmlOptions) null);
        }

        public static AttributeGroup parse(Node node, XmlOptions options) throws XmlException {
            return (AttributeGroup) XmlBeans.getContextTypeLoader().parse(node, AttributeGroup.type, options);
        }

        public static AttributeGroup parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (AttributeGroup) XmlBeans.getContextTypeLoader().parse(xis, AttributeGroup.type, (XmlOptions) null);
        }

        public static AttributeGroup parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (AttributeGroup) XmlBeans.getContextTypeLoader().parse(xis, AttributeGroup.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, AttributeGroup.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, AttributeGroup.type, options);
        }

        private Factory() {
        }
    }
}
