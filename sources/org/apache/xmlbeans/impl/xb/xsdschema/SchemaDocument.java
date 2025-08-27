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
import org.apache.xmlbeans.XmlLanguage;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlToken;
import org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.FormChoice;
import org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.IncludeDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.NotationDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/SchemaDocument.class */
public interface SchemaDocument extends XmlObject {
    public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(SchemaDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("schema0782doctype");

    Schema getSchema();

    void setSchema(Schema schema);

    Schema addNewSchema();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/SchemaDocument$Schema.class */
    public interface Schema extends OpenAttrs {
        public static final SchemaType type = (SchemaType) XmlBeans.typeSystemForClassLoader(Schema.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sXMLSCHEMA").resolveHandle("schemad77felemtype");

        IncludeDocument.Include[] getIncludeArray();

        IncludeDocument.Include getIncludeArray(int i);

        int sizeOfIncludeArray();

        void setIncludeArray(IncludeDocument.Include[] includeArr);

        void setIncludeArray(int i, IncludeDocument.Include include);

        IncludeDocument.Include insertNewInclude(int i);

        IncludeDocument.Include addNewInclude();

        void removeInclude(int i);

        ImportDocument.Import[] getImportArray();

        ImportDocument.Import getImportArray(int i);

        int sizeOfImportArray();

        void setImportArray(ImportDocument.Import[] importArr);

        void setImportArray(int i, ImportDocument.Import r2);

        ImportDocument.Import insertNewImport(int i);

        ImportDocument.Import addNewImport();

        void removeImport(int i);

        RedefineDocument.Redefine[] getRedefineArray();

        RedefineDocument.Redefine getRedefineArray(int i);

        int sizeOfRedefineArray();

        void setRedefineArray(RedefineDocument.Redefine[] redefineArr);

        void setRedefineArray(int i, RedefineDocument.Redefine redefine);

        RedefineDocument.Redefine insertNewRedefine(int i);

        RedefineDocument.Redefine addNewRedefine();

        void removeRedefine(int i);

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

        TopLevelElement[] getElementArray();

        TopLevelElement getElementArray(int i);

        int sizeOfElementArray();

        void setElementArray(TopLevelElement[] topLevelElementArr);

        void setElementArray(int i, TopLevelElement topLevelElement);

        TopLevelElement insertNewElement(int i);

        TopLevelElement addNewElement();

        void removeElement(int i);

        TopLevelAttribute[] getAttributeArray();

        TopLevelAttribute getAttributeArray(int i);

        int sizeOfAttributeArray();

        void setAttributeArray(TopLevelAttribute[] topLevelAttributeArr);

        void setAttributeArray(int i, TopLevelAttribute topLevelAttribute);

        TopLevelAttribute insertNewAttribute(int i);

        TopLevelAttribute addNewAttribute();

        void removeAttribute(int i);

        NotationDocument.Notation[] getNotationArray();

        NotationDocument.Notation getNotationArray(int i);

        int sizeOfNotationArray();

        void setNotationArray(NotationDocument.Notation[] notationArr);

        void setNotationArray(int i, NotationDocument.Notation notation);

        NotationDocument.Notation insertNewNotation(int i);

        NotationDocument.Notation addNewNotation();

        void removeNotation(int i);

        String getTargetNamespace();

        XmlAnyURI xgetTargetNamespace();

        boolean isSetTargetNamespace();

        void setTargetNamespace(String str);

        void xsetTargetNamespace(XmlAnyURI xmlAnyURI);

        void unsetTargetNamespace();

        String getVersion();

        XmlToken xgetVersion();

        boolean isSetVersion();

        void setVersion(String str);

        void xsetVersion(XmlToken xmlToken);

        void unsetVersion();

        Object getFinalDefault();

        FullDerivationSet xgetFinalDefault();

        boolean isSetFinalDefault();

        void setFinalDefault(Object obj);

        void xsetFinalDefault(FullDerivationSet fullDerivationSet);

        void unsetFinalDefault();

        Object getBlockDefault();

        BlockSet xgetBlockDefault();

        boolean isSetBlockDefault();

        void setBlockDefault(Object obj);

        void xsetBlockDefault(BlockSet blockSet);

        void unsetBlockDefault();

        FormChoice.Enum getAttributeFormDefault();

        FormChoice xgetAttributeFormDefault();

        boolean isSetAttributeFormDefault();

        void setAttributeFormDefault(FormChoice.Enum r1);

        void xsetAttributeFormDefault(FormChoice formChoice);

        void unsetAttributeFormDefault();

        FormChoice.Enum getElementFormDefault();

        FormChoice xgetElementFormDefault();

        boolean isSetElementFormDefault();

        void setElementFormDefault(FormChoice.Enum r1);

        void xsetElementFormDefault(FormChoice formChoice);

        void unsetElementFormDefault();

        String getId();

        XmlID xgetId();

        boolean isSetId();

        void setId(String str);

        void xsetId(XmlID xmlID);

        void unsetId();

        String getLang();

        XmlLanguage xgetLang();

        boolean isSetLang();

        void setLang(String str);

        void xsetLang(XmlLanguage xmlLanguage);

        void unsetLang();

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/SchemaDocument$Schema$Factory.class */
        public static final class Factory {
            public static Schema newInstance() {
                return (Schema) XmlBeans.getContextTypeLoader().newInstance(Schema.type, null);
            }

            public static Schema newInstance(XmlOptions options) {
                return (Schema) XmlBeans.getContextTypeLoader().newInstance(Schema.type, options);
            }

            private Factory() {
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/SchemaDocument$Factory.class */
    public static final class Factory {
        public static SchemaDocument newInstance() {
            return (SchemaDocument) XmlBeans.getContextTypeLoader().newInstance(SchemaDocument.type, null);
        }

        public static SchemaDocument newInstance(XmlOptions options) {
            return (SchemaDocument) XmlBeans.getContextTypeLoader().newInstance(SchemaDocument.type, options);
        }

        public static SchemaDocument parse(String xmlAsString) throws XmlException {
            return (SchemaDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, SchemaDocument.type, (XmlOptions) null);
        }

        public static SchemaDocument parse(String xmlAsString, XmlOptions options) throws XmlException {
            return (SchemaDocument) XmlBeans.getContextTypeLoader().parse(xmlAsString, SchemaDocument.type, options);
        }

        public static SchemaDocument parse(File file) throws XmlException, IOException {
            return (SchemaDocument) XmlBeans.getContextTypeLoader().parse(file, SchemaDocument.type, (XmlOptions) null);
        }

        public static SchemaDocument parse(File file, XmlOptions options) throws XmlException, IOException {
            return (SchemaDocument) XmlBeans.getContextTypeLoader().parse(file, SchemaDocument.type, options);
        }

        public static SchemaDocument parse(URL u) throws XmlException, IOException {
            return (SchemaDocument) XmlBeans.getContextTypeLoader().parse(u, SchemaDocument.type, (XmlOptions) null);
        }

        public static SchemaDocument parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (SchemaDocument) XmlBeans.getContextTypeLoader().parse(u, SchemaDocument.type, options);
        }

        public static SchemaDocument parse(InputStream is) throws XmlException, IOException {
            return (SchemaDocument) XmlBeans.getContextTypeLoader().parse(is, SchemaDocument.type, (XmlOptions) null);
        }

        public static SchemaDocument parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (SchemaDocument) XmlBeans.getContextTypeLoader().parse(is, SchemaDocument.type, options);
        }

        public static SchemaDocument parse(Reader r) throws XmlException, IOException {
            return (SchemaDocument) XmlBeans.getContextTypeLoader().parse(r, SchemaDocument.type, (XmlOptions) null);
        }

        public static SchemaDocument parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (SchemaDocument) XmlBeans.getContextTypeLoader().parse(r, SchemaDocument.type, options);
        }

        public static SchemaDocument parse(XMLStreamReader sr) throws XmlException {
            return (SchemaDocument) XmlBeans.getContextTypeLoader().parse(sr, SchemaDocument.type, (XmlOptions) null);
        }

        public static SchemaDocument parse(XMLStreamReader sr, XmlOptions options) throws XmlException {
            return (SchemaDocument) XmlBeans.getContextTypeLoader().parse(sr, SchemaDocument.type, options);
        }

        public static SchemaDocument parse(Node node) throws XmlException {
            return (SchemaDocument) XmlBeans.getContextTypeLoader().parse(node, SchemaDocument.type, (XmlOptions) null);
        }

        public static SchemaDocument parse(Node node, XmlOptions options) throws XmlException {
            return (SchemaDocument) XmlBeans.getContextTypeLoader().parse(node, SchemaDocument.type, options);
        }

        public static SchemaDocument parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (SchemaDocument) XmlBeans.getContextTypeLoader().parse(xis, SchemaDocument.type, (XmlOptions) null);
        }

        public static SchemaDocument parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (SchemaDocument) XmlBeans.getContextTypeLoader().parse(xis, SchemaDocument.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, SchemaDocument.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, SchemaDocument.type, options);
        }

        private Factory() {
        }
    }
}
