package org.hibernate.validator.internal.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "annotationType", propOrder = {"element"})
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/AnnotationType.class */
public class AnnotationType {
    protected List<ElementType> element;

    public List<ElementType> getElement() {
        if (this.element == null) {
            this.element = new ArrayList();
        }
        return this.element;
    }
}
