package org.apache.xmlbeans.impl.xb.xsdschema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlID;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/RedefineDocument.class */
public interface RedefineDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(RedefineDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("redefine3f55doctype");

    Redefine getRedefine();

    void setRedefine(Redefine redefine);

    Redefine addNewRedefine();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/RedefineDocument$Redefine.class */
    public interface Redefine extends OpenAttrs {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Redefine.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("redefine9e9felemtype");

        AnnotationDocument.Annotation[] getAnnotationArray();

        AnnotationDocument.Annotation getAnnotationArray(int i);

        int sizeOfAnnotationArray();

        void setAnnotationArray(AnnotationDocument.Annotation[] annotationArr);

        void setAnnotationArray(int i, AnnotationDocument.Annotation annotation);

        AnnotationDocument.Annotation insertNewAnnotation(int i);

        AnnotationDocument.Annotation addNewAnnotation();

        void removeAnnotation(int i);

        TopLevelSimpleType[] getSimpleTypeArray();

        TopLevelSimpleType getSimpleTypeArray(int i);

        int sizeOfSimpleTypeArray();

        void setSimpleTypeArray(TopLevelSimpleType[] topLevelSimpleTypeArr);

        void setSimpleTypeArray(int i, TopLevelSimpleType topLevelSimpleType);

        TopLevelSimpleType insertNewSimpleType(int i);

        TopLevelSimpleType addNewSimpleType();

        void removeSimpleType(int i);

        TopLevelComplexType[] getComplexTypeArray();

        TopLevelComplexType getComplexTypeArray(int i);

        int sizeOfComplexTypeArray();

        void setComplexTypeArray(TopLevelComplexType[] topLevelComplexTypeArr);

        void setComplexTypeArray(int i, TopLevelComplexType topLevelComplexType);

        TopLevelComplexType insertNewComplexType(int i);

        TopLevelComplexType addNewComplexType();

        void removeComplexType(int i);

        NamedGroup[] getGroupArray();

        NamedGroup getGroupArray(int i);

        int sizeOfGroupArray();

        void setGroupArray(NamedGroup[] namedGroupArr);

        void setGroupArray(int i, NamedGroup namedGroup);

        NamedGroup insertNewGroup(int i);

        NamedGroup addNewGroup();

        void removeGroup(int i);

        NamedAttributeGroup[] getAttributeGroupArray();

        NamedAttributeGroup getAttributeGroupArray(int i);

        int sizeOfAttributeGroupArray();

        void setAttributeGroupArray(NamedAttributeGroup[] namedAttributeGroupArr);

        void setAttributeGroupArray(int i, NamedAttributeGroup namedAttributeGroup);

        NamedAttributeGroup insertNewAttributeGroup(int i);

        NamedAttributeGroup addNewAttributeGroup();

        void removeAttributeGroup(int i);

        String getSchemaLocation();

        XmlAnyURI xgetSchemaLocation();

        void setSchemaLocation(String str);

        void xsetSchemaLocation(XmlAnyURI xmlAnyURI);

        String getId();

        XmlID xgetId();

        boolean isSetId();

        void setId(String str);

        void xsetId(XmlID xmlID);

        void unsetId();

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/RedefineDocument$Redefine$Factory.class */
        public static final class Factory {
            public static Redefine newInstance() {
                return (Redefine) XmlBeans.getContextTypeLoader().newInstance(Redefine.type, null);
            }

            public static Redefine newInstance(XmlOptions options) {
                return (Redefine) XmlBeans.getContextTypeLoader().newInstance(Redefine.type, options);
            }

            private Factory() {
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/RedefineDocument$Factory.class */
    public static final class Factory {
        public static RedefineDocument newInstance() {
            return (RedefineDocument) XmlBeans.getContextTypeLoader().newInstance(RedefineDocument.type, null);
        }

        public static RedefineDocument newInstance(XmlOptions options) {
            return (RedefineDocument) XmlBeans.getContextTypeLoader().newInstance(RedefineDocument.type, options);
        }

        public static RedefineDocument parse(String xmlAsString) throws XmlException {
            return (RedefineDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, RedefineDocument.type, (XmlOptions) null);
        }

        public static RedefineDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (RedefineDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, RedefineDocument.type, options);
        }

        public static RedefineDocument parse(File file) throws XmlException, IOException {
            return (RedefineDocument) XmlBeans.getContextTypeLoader().parse(file, RedefineDocument.type, (XmlOptions) null);
        }

        public static RedefineDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (RedefineDocument) XmlBeans.getContextTypeLoader().parse(file, RedefineDocument.type, options);
        }

        public static RedefineDocument parse(URL u) throws XmlException, IOException {
            return (RedefineDocument) XmlBeans.getContextTypeLoader().parse(u, RedefineDocument.type, (XmlOptions) null);
        }

        public static RedefineDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (RedefineDocument) XmlBeans.getContextTypeLoader().parse(u, RedefineDocument.type, options);
        }

        public static RedefineDocument parse(InputStream is) throws XmlException, IOException {
            return (RedefineDocument) XmlBeans.getContextTypeLoader().parse(is, RedefineDocument.type, (XmlOptions) null);
        }

        public static RedefineDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (RedefineDocument) XmlBeans.getContextTypeLoader().parse(is, RedefineDocument.type, options);
        }

        public static RedefineDocument parse(Reader r) throws XmlException, IOException {
            return (RedefineDocument) XmlBeans.getContextTypeLoader().parse(r, RedefineDocument.type, (XmlOptions) null);
        }

        public static RedefineDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (RedefineDocument) XmlBeans.getContextTypeLoader().parse(r, RedefineDocument.type, options);
        }

        public static RedefineDocument parse(XMLStreamReader sr) throws XmlException {
            return (RedefineDocument) XmlBeans.getContextTypeLoader().parse(sr, RedefineDocument.type, (XmlOptions) null);
        }

        public static RedefineDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (RedefineDocument) XmlBeans.getContextTypeLoader().parse(sr, RedefineDocument.type, options);
        }

        public static RedefineDocument parse(Node node) throws XmlException {
            return (RedefineDocument) XmlBeans.getContextTypeLoader().parse(node, RedefineDocument.type, (XmlOptions) null);
        }

        public static RedefineDocument parse(Node node, XmlOptions options) throws XmlException {
            return (RedefineDocument) XmlBeans.getContextTypeLoader().parse(node, RedefineDocument.type, options);
        }

        public static RedefineDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (RedefineDocument) XmlBeans.getContextTypeLoader().parse(xis, RedefineDocument.type, (XmlOptions) null);
        }

        public static RedefineDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (RedefineDocument) XmlBeans.getContextTypeLoader().parse(xis, RedefineDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, RedefineDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, RedefineDocument.type, options);
        }

        private Factory() {
        }
    }
}
