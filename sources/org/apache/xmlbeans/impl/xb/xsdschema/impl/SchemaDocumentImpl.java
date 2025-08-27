package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.XmlID;
import org.apache.xmlbeans.XmlLanguage;
import org.apache.xmlbeans.XmlToken;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.BlockSet;
import org.apache.xmlbeans.impl.xb.xsdschema.FormChoice;
import org.apache.xmlbeans.impl.xb.xsdschema.FullDerivationSet;
import org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.IncludeDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.NamedAttributeGroup;
import org.apache.xmlbeans.impl.xb.xsdschema.NamedGroup;
import org.apache.xmlbeans.impl.xb.xsdschema.NotationDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelAttribute;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelElement;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelSimpleType;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/SchemaDocumentImpl.class */
public class SchemaDocumentImpl extends XmlComplexContentImpl implements SchemaDocument {
    private static final long serialVersionUID = 1;
    private static final QName SCHEMA$0 = new QName("http://www.w3.org/2001/XMLSchema", "schema");

    public SchemaDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument
    public SchemaDocument.Schema getSchema() {
        synchronized (monitor()) {
            check_orphaned();
            SchemaDocument.Schema target = (SchemaDocument.Schema) get_store().find_element_user(SCHEMA$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument
    public void setSchema(SchemaDocument.Schema schema) {
        generatedSetterHelperImpl(schema, SCHEMA$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument
    public SchemaDocument.Schema addNewSchema() {
        SchemaDocument.Schema target;
        synchronized (monitor()) {
            check_orphaned();
            target = (SchemaDocument.Schema) get_store().add_element_user(SCHEMA$0);
        }
        return target;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/SchemaDocumentImpl$SchemaImpl.class */
    public static class SchemaImpl extends OpenAttrsImpl implements SchemaDocument.Schema {
        private static final long serialVersionUID = 1;
        private static final QName INCLUDE$0 = new QName("http://www.w3.org/2001/XMLSchema", "include");
        private static final QName IMPORT$2 = new QName("http://www.w3.org/2001/XMLSchema", DefaultBeanDefinitionDocumentReader.IMPORT_ELEMENT);
        private static final QName REDEFINE$4 = new QName("http://www.w3.org/2001/XMLSchema", "redefine");
        private static final QName ANNOTATION$6 = new QName("http://www.w3.org/2001/XMLSchema", JamXmlElements.ANNOTATION);
        private static final QName SIMPLETYPE$8 = new QName("http://www.w3.org/2001/XMLSchema", "simpleType");
        private static final QName COMPLEXTYPE$10 = new QName("http://www.w3.org/2001/XMLSchema", "complexType");
        private static final QName GROUP$12 = new QName("http://www.w3.org/2001/XMLSchema", "group");
        private static final QName ATTRIBUTEGROUP$14 = new QName("http://www.w3.org/2001/XMLSchema", "attributeGroup");
        private static final QName ELEMENT$16 = new QName("http://www.w3.org/2001/XMLSchema", "element");
        private static final QName ATTRIBUTE$18 = new QName("http://www.w3.org/2001/XMLSchema", BeanDefinitionParserDelegate.QUALIFIER_ATTRIBUTE_ELEMENT);
        private static final QName NOTATION$20 = new QName("http://www.w3.org/2001/XMLSchema", "notation");
        private static final QName TARGETNAMESPACE$22 = new QName("", "targetNamespace");
        private static final QName VERSION$24 = new QName("", "version");
        private static final QName FINALDEFAULT$26 = new QName("", "finalDefault");
        private static final QName BLOCKDEFAULT$28 = new QName("", "blockDefault");
        private static final QName ATTRIBUTEFORMDEFAULT$30 = new QName("", "attributeFormDefault");
        private static final QName ELEMENTFORMDEFAULT$32 = new QName("", "elementFormDefault");
        private static final QName ID$34 = new QName("", "id");
        private static final QName LANG$36 = new QName("http://www.w3.org/XML/1998/namespace", AbstractHtmlElementTag.LANG_ATTRIBUTE);

        public SchemaImpl(SchemaType sType) {
            super(sType);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public IncludeDocument.Include[] getIncludeArray() {
            IncludeDocument.Include[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(INCLUDE$0, targetList);
                result = new IncludeDocument.Include[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public IncludeDocument.Include getIncludeArray(int i) {
            IncludeDocument.Include target;
            synchronized (monitor()) {
                check_orphaned();
                target = (IncludeDocument.Include) get_store().find_element_user(INCLUDE$0, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public int sizeOfIncludeArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(INCLUDE$0);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setIncludeArray(IncludeDocument.Include[] includeArray) {
            check_orphaned();
            arraySetterHelper(includeArray, INCLUDE$0);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setIncludeArray(int i, IncludeDocument.Include include) {
            generatedSetterHelperImpl(include, INCLUDE$0, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public IncludeDocument.Include insertNewInclude(int i) {
            IncludeDocument.Include target;
            synchronized (monitor()) {
                check_orphaned();
                target = (IncludeDocument.Include) get_store().insert_element_user(INCLUDE$0, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public IncludeDocument.Include addNewInclude() {
            IncludeDocument.Include target;
            synchronized (monitor()) {
                check_orphaned();
                target = (IncludeDocument.Include) get_store().add_element_user(INCLUDE$0);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void removeInclude(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(INCLUDE$0, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public ImportDocument.Import[] getImportArray() {
            ImportDocument.Import[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(IMPORT$2, targetList);
                result = new ImportDocument.Import[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public ImportDocument.Import getImportArray(int i) {
            ImportDocument.Import target;
            synchronized (monitor()) {
                check_orphaned();
                target = (ImportDocument.Import) get_store().find_element_user(IMPORT$2, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public int sizeOfImportArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(IMPORT$2);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setImportArray(ImportDocument.Import[] ximportArray) {
            check_orphaned();
            arraySetterHelper(ximportArray, IMPORT$2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setImportArray(int i, ImportDocument.Import ximport) {
            generatedSetterHelperImpl(ximport, IMPORT$2, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public ImportDocument.Import insertNewImport(int i) {
            ImportDocument.Import target;
            synchronized (monitor()) {
                check_orphaned();
                target = (ImportDocument.Import) get_store().insert_element_user(IMPORT$2, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public ImportDocument.Import addNewImport() {
            ImportDocument.Import target;
            synchronized (monitor()) {
                check_orphaned();
                target = (ImportDocument.Import) get_store().add_element_user(IMPORT$2);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void removeImport(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(IMPORT$2, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public RedefineDocument.Redefine[] getRedefineArray() {
            RedefineDocument.Redefine[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(REDEFINE$4, targetList);
                result = new RedefineDocument.Redefine[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public RedefineDocument.Redefine getRedefineArray(int i) {
            RedefineDocument.Redefine target;
            synchronized (monitor()) {
                check_orphaned();
                target = (RedefineDocument.Redefine) get_store().find_element_user(REDEFINE$4, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public int sizeOfRedefineArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(REDEFINE$4);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setRedefineArray(RedefineDocument.Redefine[] redefineArray) {
            check_orphaned();
            arraySetterHelper(redefineArray, REDEFINE$4);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setRedefineArray(int i, RedefineDocument.Redefine redefine) {
            generatedSetterHelperImpl(redefine, REDEFINE$4, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public RedefineDocument.Redefine insertNewRedefine(int i) {
            RedefineDocument.Redefine target;
            synchronized (monitor()) {
                check_orphaned();
                target = (RedefineDocument.Redefine) get_store().insert_element_user(REDEFINE$4, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public RedefineDocument.Redefine addNewRedefine() {
            RedefineDocument.Redefine target;
            synchronized (monitor()) {
                check_orphaned();
                target = (RedefineDocument.Redefine) get_store().add_element_user(REDEFINE$4);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void removeRedefine(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(REDEFINE$4, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public AnnotationDocument.Annotation[] getAnnotationArray() {
            AnnotationDocument.Annotation[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(ANNOTATION$6, targetList);
                result = new AnnotationDocument.Annotation[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public AnnotationDocument.Annotation getAnnotationArray(int i) {
            AnnotationDocument.Annotation target;
            synchronized (monitor()) {
                check_orphaned();
                target = (AnnotationDocument.Annotation) get_store().find_element_user(ANNOTATION$6, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public int sizeOfAnnotationArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(ANNOTATION$6);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setAnnotationArray(AnnotationDocument.Annotation[] annotationArray) {
            check_orphaned();
            arraySetterHelper(annotationArray, ANNOTATION$6);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setAnnotationArray(int i, AnnotationDocument.Annotation annotation) {
            generatedSetterHelperImpl(annotation, ANNOTATION$6, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public AnnotationDocument.Annotation insertNewAnnotation(int i) {
            AnnotationDocument.Annotation target;
            synchronized (monitor()) {
                check_orphaned();
                target = (AnnotationDocument.Annotation) get_store().insert_element_user(ANNOTATION$6, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public AnnotationDocument.Annotation addNewAnnotation() {
            AnnotationDocument.Annotation target;
            synchronized (monitor()) {
                check_orphaned();
                target = (AnnotationDocument.Annotation) get_store().add_element_user(ANNOTATION$6);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void removeAnnotation(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(ANNOTATION$6, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public TopLevelSimpleType[] getSimpleTypeArray() {
            TopLevelSimpleType[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(SIMPLETYPE$8, targetList);
                result = new TopLevelSimpleType[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public TopLevelSimpleType getSimpleTypeArray(int i) {
            TopLevelSimpleType target;
            synchronized (monitor()) {
                check_orphaned();
                target = (TopLevelSimpleType) get_store().find_element_user(SIMPLETYPE$8, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public int sizeOfSimpleTypeArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(SIMPLETYPE$8);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setSimpleTypeArray(TopLevelSimpleType[] simpleTypeArray) {
            check_orphaned();
            arraySetterHelper(simpleTypeArray, SIMPLETYPE$8);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setSimpleTypeArray(int i, TopLevelSimpleType simpleType) {
            generatedSetterHelperImpl(simpleType, SIMPLETYPE$8, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public TopLevelSimpleType insertNewSimpleType(int i) {
            TopLevelSimpleType target;
            synchronized (monitor()) {
                check_orphaned();
                target = (TopLevelSimpleType) get_store().insert_element_user(SIMPLETYPE$8, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public TopLevelSimpleType addNewSimpleType() {
            TopLevelSimpleType target;
            synchronized (monitor()) {
                check_orphaned();
                target = (TopLevelSimpleType) get_store().add_element_user(SIMPLETYPE$8);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void removeSimpleType(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(SIMPLETYPE$8, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public TopLevelComplexType[] getComplexTypeArray() {
            TopLevelComplexType[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(COMPLEXTYPE$10, targetList);
                result = new TopLevelComplexType[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public TopLevelComplexType getComplexTypeArray(int i) {
            TopLevelComplexType target;
            synchronized (monitor()) {
                check_orphaned();
                target = (TopLevelComplexType) get_store().find_element_user(COMPLEXTYPE$10, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public int sizeOfComplexTypeArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(COMPLEXTYPE$10);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setComplexTypeArray(TopLevelComplexType[] complexTypeArray) {
            check_orphaned();
            arraySetterHelper(complexTypeArray, COMPLEXTYPE$10);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setComplexTypeArray(int i, TopLevelComplexType complexType) {
            generatedSetterHelperImpl(complexType, COMPLEXTYPE$10, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public TopLevelComplexType insertNewComplexType(int i) {
            TopLevelComplexType target;
            synchronized (monitor()) {
                check_orphaned();
                target = (TopLevelComplexType) get_store().insert_element_user(COMPLEXTYPE$10, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public TopLevelComplexType addNewComplexType() {
            TopLevelComplexType target;
            synchronized (monitor()) {
                check_orphaned();
                target = (TopLevelComplexType) get_store().add_element_user(COMPLEXTYPE$10);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void removeComplexType(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(COMPLEXTYPE$10, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public NamedGroup[] getGroupArray() {
            NamedGroup[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(GROUP$12, targetList);
                result = new NamedGroup[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public NamedGroup getGroupArray(int i) {
            NamedGroup target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NamedGroup) get_store().find_element_user(GROUP$12, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public int sizeOfGroupArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(GROUP$12);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setGroupArray(NamedGroup[] groupArray) {
            check_orphaned();
            arraySetterHelper(groupArray, GROUP$12);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setGroupArray(int i, NamedGroup group) {
            generatedSetterHelperImpl(group, GROUP$12, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public NamedGroup insertNewGroup(int i) {
            NamedGroup target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NamedGroup) get_store().insert_element_user(GROUP$12, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public NamedGroup addNewGroup() {
            NamedGroup target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NamedGroup) get_store().add_element_user(GROUP$12);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void removeGroup(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(GROUP$12, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public NamedAttributeGroup[] getAttributeGroupArray() {
            NamedAttributeGroup[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(ATTRIBUTEGROUP$14, targetList);
                result = new NamedAttributeGroup[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public NamedAttributeGroup getAttributeGroupArray(int i) {
            NamedAttributeGroup target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NamedAttributeGroup) get_store().find_element_user(ATTRIBUTEGROUP$14, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public int sizeOfAttributeGroupArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(ATTRIBUTEGROUP$14);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setAttributeGroupArray(NamedAttributeGroup[] attributeGroupArray) {
            check_orphaned();
            arraySetterHelper(attributeGroupArray, ATTRIBUTEGROUP$14);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setAttributeGroupArray(int i, NamedAttributeGroup attributeGroup) {
            generatedSetterHelperImpl(attributeGroup, ATTRIBUTEGROUP$14, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public NamedAttributeGroup insertNewAttributeGroup(int i) {
            NamedAttributeGroup target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NamedAttributeGroup) get_store().insert_element_user(ATTRIBUTEGROUP$14, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public NamedAttributeGroup addNewAttributeGroup() {
            NamedAttributeGroup target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NamedAttributeGroup) get_store().add_element_user(ATTRIBUTEGROUP$14);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void removeAttributeGroup(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(ATTRIBUTEGROUP$14, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public TopLevelElement[] getElementArray() {
            TopLevelElement[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(ELEMENT$16, targetList);
                result = new TopLevelElement[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public TopLevelElement getElementArray(int i) {
            TopLevelElement target;
            synchronized (monitor()) {
                check_orphaned();
                target = (TopLevelElement) get_store().find_element_user(ELEMENT$16, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public int sizeOfElementArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(ELEMENT$16);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setElementArray(TopLevelElement[] elementArray) {
            check_orphaned();
            arraySetterHelper(elementArray, ELEMENT$16);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setElementArray(int i, TopLevelElement element) {
            generatedSetterHelperImpl(element, ELEMENT$16, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public TopLevelElement insertNewElement(int i) {
            TopLevelElement target;
            synchronized (monitor()) {
                check_orphaned();
                target = (TopLevelElement) get_store().insert_element_user(ELEMENT$16, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public TopLevelElement addNewElement() {
            TopLevelElement target;
            synchronized (monitor()) {
                check_orphaned();
                target = (TopLevelElement) get_store().add_element_user(ELEMENT$16);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void removeElement(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(ELEMENT$16, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public TopLevelAttribute[] getAttributeArray() {
            TopLevelAttribute[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(ATTRIBUTE$18, targetList);
                result = new TopLevelAttribute[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public TopLevelAttribute getAttributeArray(int i) {
            TopLevelAttribute target;
            synchronized (monitor()) {
                check_orphaned();
                target = (TopLevelAttribute) get_store().find_element_user(ATTRIBUTE$18, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public int sizeOfAttributeArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(ATTRIBUTE$18);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setAttributeArray(TopLevelAttribute[] attributeArray) {
            check_orphaned();
            arraySetterHelper(attributeArray, ATTRIBUTE$18);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setAttributeArray(int i, TopLevelAttribute attribute) {
            generatedSetterHelperImpl(attribute, ATTRIBUTE$18, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public TopLevelAttribute insertNewAttribute(int i) {
            TopLevelAttribute target;
            synchronized (monitor()) {
                check_orphaned();
                target = (TopLevelAttribute) get_store().insert_element_user(ATTRIBUTE$18, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public TopLevelAttribute addNewAttribute() {
            TopLevelAttribute target;
            synchronized (monitor()) {
                check_orphaned();
                target = (TopLevelAttribute) get_store().add_element_user(ATTRIBUTE$18);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void removeAttribute(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(ATTRIBUTE$18, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public NotationDocument.Notation[] getNotationArray() {
            NotationDocument.Notation[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(NOTATION$20, targetList);
                result = new NotationDocument.Notation[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public NotationDocument.Notation getNotationArray(int i) {
            NotationDocument.Notation target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NotationDocument.Notation) get_store().find_element_user(NOTATION$20, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public int sizeOfNotationArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(NOTATION$20);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setNotationArray(NotationDocument.Notation[] notationArray) {
            check_orphaned();
            arraySetterHelper(notationArray, NOTATION$20);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setNotationArray(int i, NotationDocument.Notation notation) {
            generatedSetterHelperImpl(notation, NOTATION$20, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public NotationDocument.Notation insertNewNotation(int i) {
            NotationDocument.Notation target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NotationDocument.Notation) get_store().insert_element_user(NOTATION$20, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public NotationDocument.Notation addNewNotation() {
            NotationDocument.Notation target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NotationDocument.Notation) get_store().add_element_user(NOTATION$20);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void removeNotation(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(NOTATION$20, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public String getTargetNamespace() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(TARGETNAMESPACE$22);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public XmlAnyURI xgetTargetNamespace() {
            XmlAnyURI target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlAnyURI) get_store().find_attribute_user(TARGETNAMESPACE$22);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public boolean isSetTargetNamespace() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().find_attribute_user(TARGETNAMESPACE$22) != null;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setTargetNamespace(String targetNamespace) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(TARGETNAMESPACE$22);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(TARGETNAMESPACE$22);
                }
                target.setStringValue(targetNamespace);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void xsetTargetNamespace(XmlAnyURI targetNamespace) {
            synchronized (monitor()) {
                check_orphaned();
                XmlAnyURI target = (XmlAnyURI) get_store().find_attribute_user(TARGETNAMESPACE$22);
                if (target == null) {
                    target = (XmlAnyURI) get_store().add_attribute_user(TARGETNAMESPACE$22);
                }
                target.set(targetNamespace);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void unsetTargetNamespace() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(TARGETNAMESPACE$22);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public String getVersion() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(VERSION$24);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public XmlToken xgetVersion() {
            XmlToken target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlToken) get_store().find_attribute_user(VERSION$24);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public boolean isSetVersion() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().find_attribute_user(VERSION$24) != null;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setVersion(String version) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(VERSION$24);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(VERSION$24);
                }
                target.setStringValue(version);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void xsetVersion(XmlToken version) {
            synchronized (monitor()) {
                check_orphaned();
                XmlToken target = (XmlToken) get_store().find_attribute_user(VERSION$24);
                if (target == null) {
                    target = (XmlToken) get_store().add_attribute_user(VERSION$24);
                }
                target.set(version);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void unsetVersion() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(VERSION$24);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public Object getFinalDefault() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(FINALDEFAULT$26);
                if (target == null) {
                    target = (SimpleValue) get_default_attribute_value(FINALDEFAULT$26);
                }
                if (target == null) {
                    return null;
                }
                return target.getObjectValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public FullDerivationSet xgetFinalDefault() {
            FullDerivationSet fullDerivationSet;
            synchronized (monitor()) {
                check_orphaned();
                FullDerivationSet target = (FullDerivationSet) get_store().find_attribute_user(FINALDEFAULT$26);
                if (target == null) {
                    target = (FullDerivationSet) get_default_attribute_value(FINALDEFAULT$26);
                }
                fullDerivationSet = target;
            }
            return fullDerivationSet;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public boolean isSetFinalDefault() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().find_attribute_user(FINALDEFAULT$26) != null;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setFinalDefault(Object finalDefault) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(FINALDEFAULT$26);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(FINALDEFAULT$26);
                }
                target.setObjectValue(finalDefault);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void xsetFinalDefault(FullDerivationSet finalDefault) {
            synchronized (monitor()) {
                check_orphaned();
                FullDerivationSet target = (FullDerivationSet) get_store().find_attribute_user(FINALDEFAULT$26);
                if (target == null) {
                    target = (FullDerivationSet) get_store().add_attribute_user(FINALDEFAULT$26);
                }
                target.set(finalDefault);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void unsetFinalDefault() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(FINALDEFAULT$26);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public Object getBlockDefault() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(BLOCKDEFAULT$28);
                if (target == null) {
                    target = (SimpleValue) get_default_attribute_value(BLOCKDEFAULT$28);
                }
                if (target == null) {
                    return null;
                }
                return target.getObjectValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public BlockSet xgetBlockDefault() {
            BlockSet blockSet;
            synchronized (monitor()) {
                check_orphaned();
                BlockSet target = (BlockSet) get_store().find_attribute_user(BLOCKDEFAULT$28);
                if (target == null) {
                    target = (BlockSet) get_default_attribute_value(BLOCKDEFAULT$28);
                }
                blockSet = target;
            }
            return blockSet;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public boolean isSetBlockDefault() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().find_attribute_user(BLOCKDEFAULT$28) != null;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setBlockDefault(Object blockDefault) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(BLOCKDEFAULT$28);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(BLOCKDEFAULT$28);
                }
                target.setObjectValue(blockDefault);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void xsetBlockDefault(BlockSet blockDefault) {
            synchronized (monitor()) {
                check_orphaned();
                BlockSet target = (BlockSet) get_store().find_attribute_user(BLOCKDEFAULT$28);
                if (target == null) {
                    target = (BlockSet) get_store().add_attribute_user(BLOCKDEFAULT$28);
                }
                target.set(blockDefault);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void unsetBlockDefault() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(BLOCKDEFAULT$28);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public FormChoice.Enum getAttributeFormDefault() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(ATTRIBUTEFORMDEFAULT$30);
                if (target == null) {
                    target = (SimpleValue) get_default_attribute_value(ATTRIBUTEFORMDEFAULT$30);
                }
                if (target == null) {
                    return null;
                }
                return (FormChoice.Enum) target.getEnumValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public FormChoice xgetAttributeFormDefault() {
            FormChoice formChoice;
            synchronized (monitor()) {
                check_orphaned();
                FormChoice target = (FormChoice) get_store().find_attribute_user(ATTRIBUTEFORMDEFAULT$30);
                if (target == null) {
                    target = (FormChoice) get_default_attribute_value(ATTRIBUTEFORMDEFAULT$30);
                }
                formChoice = target;
            }
            return formChoice;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public boolean isSetAttributeFormDefault() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().find_attribute_user(ATTRIBUTEFORMDEFAULT$30) != null;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setAttributeFormDefault(FormChoice.Enum attributeFormDefault) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(ATTRIBUTEFORMDEFAULT$30);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(ATTRIBUTEFORMDEFAULT$30);
                }
                target.setEnumValue(attributeFormDefault);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void xsetAttributeFormDefault(FormChoice attributeFormDefault) {
            synchronized (monitor()) {
                check_orphaned();
                FormChoice target = (FormChoice) get_store().find_attribute_user(ATTRIBUTEFORMDEFAULT$30);
                if (target == null) {
                    target = (FormChoice) get_store().add_attribute_user(ATTRIBUTEFORMDEFAULT$30);
                }
                target.set(attributeFormDefault);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void unsetAttributeFormDefault() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(ATTRIBUTEFORMDEFAULT$30);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public FormChoice.Enum getElementFormDefault() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(ELEMENTFORMDEFAULT$32);
                if (target == null) {
                    target = (SimpleValue) get_default_attribute_value(ELEMENTFORMDEFAULT$32);
                }
                if (target == null) {
                    return null;
                }
                return (FormChoice.Enum) target.getEnumValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public FormChoice xgetElementFormDefault() {
            FormChoice formChoice;
            synchronized (monitor()) {
                check_orphaned();
                FormChoice target = (FormChoice) get_store().find_attribute_user(ELEMENTFORMDEFAULT$32);
                if (target == null) {
                    target = (FormChoice) get_default_attribute_value(ELEMENTFORMDEFAULT$32);
                }
                formChoice = target;
            }
            return formChoice;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public boolean isSetElementFormDefault() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().find_attribute_user(ELEMENTFORMDEFAULT$32) != null;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setElementFormDefault(FormChoice.Enum elementFormDefault) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(ELEMENTFORMDEFAULT$32);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(ELEMENTFORMDEFAULT$32);
                }
                target.setEnumValue(elementFormDefault);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void xsetElementFormDefault(FormChoice elementFormDefault) {
            synchronized (monitor()) {
                check_orphaned();
                FormChoice target = (FormChoice) get_store().find_attribute_user(ELEMENTFORMDEFAULT$32);
                if (target == null) {
                    target = (FormChoice) get_store().add_attribute_user(ELEMENTFORMDEFAULT$32);
                }
                target.set(elementFormDefault);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void unsetElementFormDefault() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(ELEMENTFORMDEFAULT$32);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public String getId() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(ID$34);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public XmlID xgetId() {
            XmlID target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlID) get_store().find_attribute_user(ID$34);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public boolean isSetId() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().find_attribute_user(ID$34) != null;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setId(String id) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(ID$34);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(ID$34);
                }
                target.setStringValue(id);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void xsetId(XmlID id) {
            synchronized (monitor()) {
                check_orphaned();
                XmlID target = (XmlID) get_store().find_attribute_user(ID$34);
                if (target == null) {
                    target = (XmlID) get_store().add_attribute_user(ID$34);
                }
                target.set(id);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void unsetId() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(ID$34);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public String getLang() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(LANG$36);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public XmlLanguage xgetLang() {
            XmlLanguage target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlLanguage) get_store().find_attribute_user(LANG$36);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public boolean isSetLang() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().find_attribute_user(LANG$36) != null;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void setLang(String lang) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(LANG$36);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(LANG$36);
                }
                target.setStringValue(lang);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void xsetLang(XmlLanguage lang) {
            synchronized (monitor()) {
                check_orphaned();
                XmlLanguage target = (XmlLanguage) get_store().find_attribute_user(LANG$36);
                if (target == null) {
                    target = (XmlLanguage) get_store().add_attribute_user(LANG$36);
                }
                target.set(lang);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema
        public void unsetLang() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(LANG$36);
            }
        }
    }
}
