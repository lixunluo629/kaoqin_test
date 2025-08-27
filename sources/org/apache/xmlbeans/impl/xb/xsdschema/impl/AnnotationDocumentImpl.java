package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlID;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.AppinfoDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/AnnotationDocumentImpl.class */
public class AnnotationDocumentImpl extends XmlComplexContentImpl implements AnnotationDocument {
    private static final long serialVersionUID = 1;
    private static final QName ANNOTATION$0 = new QName("http://www.w3.org/2001/XMLSchema", JamXmlElements.ANNOTATION);

    public AnnotationDocumentImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument
    public AnnotationDocument.Annotation getAnnotation() {
        synchronized (monitor()) {
            check_orphaned();
            AnnotationDocument.Annotation target = (AnnotationDocument.Annotation) get_store().find_element_user(ANNOTATION$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument
    public void setAnnotation(AnnotationDocument.Annotation annotation) {
        generatedSetterHelperImpl(annotation, ANNOTATION$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument
    public AnnotationDocument.Annotation addNewAnnotation() {
        AnnotationDocument.Annotation target;
        synchronized (monitor()) {
            check_orphaned();
            target = (AnnotationDocument.Annotation) get_store().add_element_user(ANNOTATION$0);
        }
        return target;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/AnnotationDocumentImpl$AnnotationImpl.class */
    public static class AnnotationImpl extends OpenAttrsImpl implements AnnotationDocument.Annotation {
        private static final long serialVersionUID = 1;
        private static final QName APPINFO$0 = new QName("http://www.w3.org/2001/XMLSchema", "appinfo");
        private static final QName DOCUMENTATION$2 = new QName("http://www.w3.org/2001/XMLSchema", "documentation");
        private static final QName ID$4 = new QName("", "id");

        public AnnotationImpl(SchemaType sType) {
            super(sType);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument.Annotation
        public AppinfoDocument.Appinfo[] getAppinfoArray() {
            AppinfoDocument.Appinfo[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(APPINFO$0, targetList);
                result = new AppinfoDocument.Appinfo[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument.Annotation
        public AppinfoDocument.Appinfo getAppinfoArray(int i) {
            AppinfoDocument.Appinfo target;
            synchronized (monitor()) {
                check_orphaned();
                target = (AppinfoDocument.Appinfo) get_store().find_element_user(APPINFO$0, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument.Annotation
        public int sizeOfAppinfoArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(APPINFO$0);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument.Annotation
        public void setAppinfoArray(AppinfoDocument.Appinfo[] appinfoArray) {
            check_orphaned();
            arraySetterHelper(appinfoArray, APPINFO$0);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument.Annotation
        public void setAppinfoArray(int i, AppinfoDocument.Appinfo appinfo) {
            generatedSetterHelperImpl(appinfo, APPINFO$0, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument.Annotation
        public AppinfoDocument.Appinfo insertNewAppinfo(int i) {
            AppinfoDocument.Appinfo target;
            synchronized (monitor()) {
                check_orphaned();
                target = (AppinfoDocument.Appinfo) get_store().insert_element_user(APPINFO$0, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument.Annotation
        public AppinfoDocument.Appinfo addNewAppinfo() {
            AppinfoDocument.Appinfo target;
            synchronized (monitor()) {
                check_orphaned();
                target = (AppinfoDocument.Appinfo) get_store().add_element_user(APPINFO$0);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument.Annotation
        public void removeAppinfo(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(APPINFO$0, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument.Annotation
        public DocumentationDocument.Documentation[] getDocumentationArray() {
            DocumentationDocument.Documentation[] result;
            synchronized (monitor()) {
                check_orphaned();
                List targetList = new ArrayList();
                get_store().find_all_element_users(DOCUMENTATION$2, targetList);
                result = new DocumentationDocument.Documentation[targetList.size()];
                targetList.toArray(result);
            }
            return result;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument.Annotation
        public DocumentationDocument.Documentation getDocumentationArray(int i) {
            DocumentationDocument.Documentation target;
            synchronized (monitor()) {
                check_orphaned();
                target = (DocumentationDocument.Documentation) get_store().find_element_user(DOCUMENTATION$2, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument.Annotation
        public int sizeOfDocumentationArray() {
            int iCount_elements;
            synchronized (monitor()) {
                check_orphaned();
                iCount_elements = get_store().count_elements(DOCUMENTATION$2);
            }
            return iCount_elements;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument.Annotation
        public void setDocumentationArray(DocumentationDocument.Documentation[] documentationArray) {
            check_orphaned();
            arraySetterHelper(documentationArray, DOCUMENTATION$2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument.Annotation
        public void setDocumentationArray(int i, DocumentationDocument.Documentation documentation) {
            generatedSetterHelperImpl(documentation, DOCUMENTATION$2, i, (short) 2);
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument.Annotation
        public DocumentationDocument.Documentation insertNewDocumentation(int i) {
            DocumentationDocument.Documentation target;
            synchronized (monitor()) {
                check_orphaned();
                target = (DocumentationDocument.Documentation) get_store().insert_element_user(DOCUMENTATION$2, i);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument.Annotation
        public DocumentationDocument.Documentation addNewDocumentation() {
            DocumentationDocument.Documentation target;
            synchronized (monitor()) {
                check_orphaned();
                target = (DocumentationDocument.Documentation) get_store().add_element_user(DOCUMENTATION$2);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument.Annotation
        public void removeDocumentation(int i) {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_element(DOCUMENTATION$2, i);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument.Annotation
        public String getId() {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(ID$4);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument.Annotation
        public XmlID xgetId() {
            XmlID target;
            synchronized (monitor()) {
                check_orphaned();
                target = (XmlID) get_store().find_attribute_user(ID$4);
            }
            return target;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument.Annotation
        public boolean isSetId() {
            boolean z;
            synchronized (monitor()) {
                check_orphaned();
                z = get_store().find_attribute_user(ID$4) != null;
            }
            return z;
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument.Annotation
        public void setId(String id) {
            synchronized (monitor()) {
                check_orphaned();
                SimpleValue target = (SimpleValue) get_store().find_attribute_user(ID$4);
                if (target == null) {
                    target = (SimpleValue) get_store().add_attribute_user(ID$4);
                }
                target.setStringValue(id);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument.Annotation
        public void xsetId(XmlID id) {
            synchronized (monitor()) {
                check_orphaned();
                XmlID target = (XmlID) get_store().find_attribute_user(ID$4);
                if (target == null) {
                    target = (XmlID) get_store().add_attribute_user(ID$4);
                }
                target.set(id);
            }
        }

        @Override // org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument.Annotation
        public void unsetId() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(ID$4);
            }
        }
    }
}
