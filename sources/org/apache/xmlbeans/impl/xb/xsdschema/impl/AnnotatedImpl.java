package org.apache.xmlbeans.impl.xb.xsdschema.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlID;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;
import org.apache.xmlbeans.impl.xb.xsdschema.Annotated;
import org.apache.xmlbeans.impl.xb.xsdschema.AnnotationDocument;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xb/xsdschema/impl/AnnotatedImpl.class */
public class AnnotatedImpl extends OpenAttrsImpl implements Annotated {
    private static final long serialVersionUID = 1;
    private static final QName ANNOTATION$0 = new QName("http://www.w3.org/2001/XMLSchema", JamXmlElements.ANNOTATION);
    private static final QName ID$2 = new QName("", "id");

    public AnnotatedImpl(SchemaType sType) {
        super(sType);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Annotated
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

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Annotated
    public boolean isSetAnnotation() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(ANNOTATION$0) != 0;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Annotated
    public void setAnnotation(AnnotationDocument.Annotation annotation) {
        generatedSetterHelperImpl(annotation, ANNOTATION$0, 0, (short) 1);
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Annotated
    public AnnotationDocument.Annotation addNewAnnotation() {
        AnnotationDocument.Annotation target;
        synchronized (monitor()) {
            check_orphaned();
            target = (AnnotationDocument.Annotation) get_store().add_element_user(ANNOTATION$0);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Annotated
    public void unsetAnnotation() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ANNOTATION$0, 0);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Annotated
    public String getId() {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(ID$2);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Annotated
    public XmlID xgetId() {
        XmlID target;
        synchronized (monitor()) {
            check_orphaned();
            target = (XmlID) get_store().find_attribute_user(ID$2);
        }
        return target;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Annotated
    public boolean isSetId() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().find_attribute_user(ID$2) != null;
        }
        return z;
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Annotated
    public void setId(String id) {
        synchronized (monitor()) {
            check_orphaned();
            SimpleValue target = (SimpleValue) get_store().find_attribute_user(ID$2);
            if (target == null) {
                target = (SimpleValue) get_store().add_attribute_user(ID$2);
            }
            target.setStringValue(id);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Annotated
    public void xsetId(XmlID id) {
        synchronized (monitor()) {
            check_orphaned();
            XmlID target = (XmlID) get_store().find_attribute_user(ID$2);
            if (target == null) {
                target = (XmlID) get_store().add_attribute_user(ID$2);
            }
            target.set(id);
        }
    }

    @Override // org.apache.xmlbeans.impl.xb.xsdschema.Annotated
    public void unsetId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(ID$2);
        }
    }
}
