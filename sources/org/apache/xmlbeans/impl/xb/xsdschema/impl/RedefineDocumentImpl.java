package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.XmlID;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.NamedAttributeGroup;
import org.apache.xmlbeans.impl.xb.xsdschema.NamedGroup;
import org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelSimpleType;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/RedefineDocumentImpl.class */
public class RedefineDocumentImpl extends XmlComplexContentImpl implements RedefineDocument {
    private static final long serialVersionUID = 1;
    private static final QName REDEFINE$0 = new QName("http://www.w3.org/2001/XMLSchema", "redefine");

    public RedefineDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument
    public RedefineDocument.Redefine getRedefine() {
        synchronized (monitor()) {
            check_orphaned();
            RedefineDocument.Redefine target = (RedefineDocument.Redefine) get_store().find_element_user(REDEFINE$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument
    public void setRedefine(RedefineDocument.Redefine redefine) {
        generatedSetterHelperImpl(redefine, REDEFINE$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument
    public RedefineDocument.Redefine addNewRedefine() {
        RedefineDocument.Redefine target;
        synchronized (monitor()) {
            check_orphaned();
            target = (RedefineDocument.Redefine) get_store().add_element_user(REDEFINE$0);
        }
        return target;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/RedefineDocumentImpl$RedefineImpl.class */
    public static class RedefineImpl extends OpenAttrsImpl implements RedefineDocument.Redefine {
        private static final long serialVersionUID = 1;
        private static final QName ANNOTATION$0 = new QName("http://www.w3.org/2001/XMLSchema", JamXmlElements.ANNOTATION);
        private static final QName SIMPLETYPE$2 = new QName("http://www.w3.org/2001/XMLSchema", "simpleType");
        private static final QName COMPLEXTYPE$4 = new QName("http://www.w3.org/2001/XMLSchema", "complexType");
        private static final QName GROUP$6 = new QName("http://www.w3.org/2001/XMLSchema", "group");
        private static final QName ATTRIBUTEGROUP$8 = new QName("http://www.w3.org/2001/XMLSchema", "attributeGroup");
        private static final QName SCHEMALOCATION$10 = new QName("", "schemaLocation");
        private static final QName ID$12 = new QName("", "id");

        public RedefineImpl(SchemaType sType) {
            super(sType);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public AnnotationDocument.Annotation[] getAnnotationArray() {
            AnnotationDocument.Annotation[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(ANNOTATION$0, targetList);
                result = new AnnotationDocument.Annotation[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public AnnotationDocument.Annotation getAnnotationArray(int i) {
            AnnotationDocument.Annotation target;
            synchronized (monitor()) {
                check_orphaned();
                target = (AnnotationDocument.Annotation) get_store().find_element_user(ANNOTATION$0, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public int sizeOfAnnotationArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(ANNOTATION$0);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public void setAnnotationArray(AnnotationDocument.Annotation[] annotationArray) {
            check_orphaned();
            arraySetterHelper(annotationArray, ANNOTATION$0);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public void setAnnotationArray(int i, AnnotationDocument.Annotation annotation) {
            generatedSetterHelperImpl(annotation, ANNOTATION$0, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public AnnotationDocument.Annotation insertNewAnnotation(int i) {
            AnnotationDocument.Annotation target;
            synchronized (monitor()) {
                check_orphaned();
                target = (AnnotationDocument.Annotation) get_store().insert_element_user(ANNOTATION$0, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public AnnotationDocument.Annotation addNewAnnotation() {
            AnnotationDocument.Annotation target;
            synchronized (monitor()) {
                check_orphaned();
                target = (AnnotationDocument.Annotation) get_store().add_element_user(ANNOTATION$0);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public void removeAnnotation(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(ANNOTATION$0, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public TopLevelSimpleType[] getSimpleTypeArray() {
            TopLevelSimpleType[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(SIMPLETYPE$2, targetList);
                result = new TopLevelSimpleType[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public TopLevelSimpleType getSimpleTypeArray(int i) {
            TopLevelSimpleType target;
            synchronized (monitor()) {
                check_orphaned();
                target = (TopLevelSimpleType) get_store().find_element_user(SIMPLETYPE$2, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public int sizeOfSimpleTypeArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(SIMPLETYPE$2);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public void setSimpleTypeArray(TopLevelSimpleType[] simpleTypeArray) {
            check_orphaned();
            arraySetterHelper(simpleTypeArray, SIMPLETYPE$2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public void setSimpleTypeArray(int i, TopLevelSimpleType simpleType) {
            generatedSetterHelperImpl(simpleType, SIMPLETYPE$2, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public TopLevelSimpleType insertNewSimpleType(int i) {
            TopLevelSimpleType target;
            synchronized (monitor()) {
                check_orphaned();
                target = (TopLevelSimpleType) get_store().insert_element_user(SIMPLETYPE$2, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public TopLevelSimpleType addNewSimpleType() {
            TopLevelSimpleType target;
            synchronized (monitor()) {
                check_orphaned();
                target = (TopLevelSimpleType) get_store().add_element_user(SIMPLETYPE$2);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public void removeSimpleType(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(SIMPLETYPE$2, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public TopLevelComplexType[] getComplexTypeArray() {
            TopLevelComplexType[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(COMPLEXTYPE$4, targetList);
                result = new TopLevelComplexType[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public TopLevelComplexType getComplexTypeArray(int i) {
            TopLevelComplexType target;
            synchronized (monitor()) {
                check_orphaned();
                target = (TopLevelComplexType) get_store().find_element_user(COMPLEXTYPE$4, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public int sizeOfComplexTypeArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(COMPLEXTYPE$4);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public void setComplexTypeArray(TopLevelComplexType[] complexTypeArray) {
            check_orphaned();
            arraySetterHelper(complexTypeArray, COMPLEXTYPE$4);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public void setComplexTypeArray(int i, TopLevelComplexType complexType) {
            generatedSetterHelperImpl(complexType, COMPLEXTYPE$4, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public TopLevelComplexType insertNewComplexType(int i) {
            TopLevelComplexType target;
            synchronized (monitor()) {
                check_orphaned();
                target = (TopLevelComplexType) get_store().insert_element_user(COMPLEXTYPE$4, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public TopLevelComplexType addNewComplexType() {
            TopLevelComplexType target;
            synchronized (monitor()) {
                check_orphaned();
                target = (TopLevelComplexType) get_store().add_element_user(COMPLEXTYPE$4);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public void removeComplexType(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(COMPLEXTYPE$4, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public NamedGroup[] getGroupArray() {
            NamedGroup[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(GROUP$6, targetList);
                result = new NamedGroup[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public NamedGroup getGroupArray(int i) {
            NamedGroup target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NamedGroup) get_store().find_element_user(GROUP$6, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public int sizeOfGroupArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(GROUP$6);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public void setGroupArray(NamedGroup[] groupArray) {
            check_orphaned();
            arraySetterHelper(groupArray, GROUP$6);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public void setGroupArray(int i, NamedGroup group) {
            generatedSetterHelperImpl(group, GROUP$6, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public NamedGroup insertNewGroup(int i) {
            NamedGroup target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NamedGroup) get_store().insert_element_user(GROUP$6, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public NamedGroup addNewGroup() {
            NamedGroup target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NamedGroup) get_store().add_element_user(GROUP$6);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public void removeGroup(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(GROUP$6, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public NamedAttributeGroup[] getAttributeGroupArray() {
            NamedAttributeGroup[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(ATTRIBUTEGROUP$8, targetList);
                result = new NamedAttributeGroup[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public NamedAttributeGroup getAttributeGroupArray(int i) {
            NamedAttributeGroup target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NamedAttributeGroup) get_store().find_element_user(ATTRIBUTEGROUP$8, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public int sizeOfAttributeGroupArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(ATTRIBUTEGROUP$8);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public void setAttributeGroupArray(NamedAttributeGroup[] attributeGroupArray) {
            check_orphaned();
            arraySetterHelper(attributeGroupArray, ATTRIBUTEGROUP$8);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public void setAttributeGroupArray(int i, NamedAttributeGroup attributeGroup) {
            generatedSetterHelperImpl(attributeGroup, ATTRIBUTEGROUP$8, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public NamedAttributeGroup insertNewAttributeGroup(int i) {
            NamedAttributeGroup target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NamedAttributeGroup) get_store().insert_element_user(ATTRIBUTEGROUP$8, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public NamedAttributeGroup addNewAttributeGroup() {
            NamedAttributeGroup target;
            synchronized (monitor()) {
                check_orphaned();
                target = (NamedAttributeGroup) get_store().add_element_user(ATTRIBUTEGROUP$8);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public void removeAttributeGroup(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(ATTRIBUTEGROUP$8, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public String getSchemaLocation() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(SCHEMALOCATION$10);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public XmlAnyURI xgetSchemaLocation() {
            XmlAnyURI target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlAnyURI) get_store().find_attribute_user(SCHEMALOCATION$10);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public void setSchemaLocation(String schemaLocation) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(SCHEMALOCATION$10);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(SCHEMALOCATION$10);
                }
                target.setStringValue(schemaLocation);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public void xsetSchemaLocation(XmlAnyURI schemaLocation) {
            synchronized (monitor()) {
                check_orphaned();
                XmlAnyURI target = (XmlAnyURI) get_store().find_attribute_user(SCHEMALOCATION$10);
                if (target == null) {
                    target = (XmlAnyURI) get_store().add_attribute_user(SCHEMALOCATION$10);
                }
                target.set(schemaLocation);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public String getId() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(ID$12);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public XmlID xgetId() {
            XmlID target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlID) get_store().find_attribute_user(ID$12);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public boolean isSetId() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().find_attribute_user(ID$12) != null;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public void setId(String id) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(ID$12);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(ID$12);
                }
                target.setStringValue(id);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public void xsetId(XmlID id) {
            synchronized (monitor()) {
                check_orphaned();
                XmlID target = (XmlID) get_store().find_attribute_user(ID$12);
                if (target == null) {
                    target = (XmlID) get_store().add_attribute_user(ID$12);
                }
                target.set(id);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument.Redefine
        public void unsetId() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(ID$12);
            }
        }
    }
}
