package org.apache.xmlbeans.impl.jam.annotation;

import java.util.ArrayList;
import java.util.List;
import org.apache.xmlbeans.impl.jam.JAnnotationValue;
import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.internal.elements.AnnotationValueImpl;
import org.apache.xmlbeans.impl.jam.internal.elements.ElementContext;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/annotation/DefaultAnnotationProxy.class */
public class DefaultAnnotationProxy extends AnnotationProxy {
    private List mValues = new ArrayList();

    @Override // org.apache.xmlbeans.impl.jam.annotation.AnnotationProxy
    public JAnnotationValue[] getValues() {
        JAnnotationValue[] out = new JAnnotationValue[this.mValues.size()];
        this.mValues.toArray(out);
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.annotation.AnnotationProxy
    public void setValue(String name, Object value, JClass type) {
        if (name == null) {
            throw new IllegalArgumentException("null name");
        }
        if (type == null) {
            throw new IllegalArgumentException("null type");
        }
        if (value == null) {
            throw new IllegalArgumentException("null value");
        }
        this.mValues.add(new AnnotationValueImpl((ElementContext) getLogger(), name.trim(), value, type));
    }
}
