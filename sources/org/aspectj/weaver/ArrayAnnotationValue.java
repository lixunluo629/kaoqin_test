package org.aspectj.weaver;

import org.springframework.beans.PropertyAccessor;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ArrayAnnotationValue.class */
public class ArrayAnnotationValue extends AnnotationValue {
    private AnnotationValue[] values;

    public ArrayAnnotationValue() {
        super(91);
    }

    public void setValues(AnnotationValue[] values) {
        this.values = values;
    }

    public ArrayAnnotationValue(AnnotationValue[] values) {
        super(91);
        this.values = values;
    }

    public AnnotationValue[] getValues() {
        return this.values;
    }

    @Override // org.aspectj.weaver.AnnotationValue
    public String stringify() {
        StringBuffer sb = new StringBuffer();
        sb.append(PropertyAccessor.PROPERTY_KEY_PREFIX);
        for (int i = 0; i < this.values.length; i++) {
            sb.append(this.values[i].stringify());
            if (i + 1 < this.values.length) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        for (int i = 0; i < this.values.length; i++) {
            sb.append(this.values[i].toString());
            if (i + 1 < this.values.length) {
                sb.append(",");
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
