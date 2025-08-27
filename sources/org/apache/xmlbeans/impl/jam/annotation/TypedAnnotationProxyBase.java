package org.apache.xmlbeans.impl.jam.annotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.apache.xmlbeans.impl.jam.JAnnotationValue;
import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.internal.elements.AnnotationValueImpl;
import org.apache.xmlbeans.impl.jam.internal.elements.ElementContext;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/annotation/TypedAnnotationProxyBase.class */
public abstract class TypedAnnotationProxyBase extends AnnotationProxy {
    private List mValues = null;

    protected TypedAnnotationProxyBase() {
    }

    @Override // org.apache.xmlbeans.impl.jam.annotation.AnnotationProxy
    public void setValue(String name, Object value, JClass type) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (name == null) {
            throw new IllegalArgumentException("null name");
        }
        if (value == null) {
            throw new IllegalArgumentException("null value");
        }
        if (this.mValues == null) {
            this.mValues = new ArrayList();
        }
        this.mValues.add(new AnnotationValueImpl((ElementContext) this.mContext, name, value, type));
        Method m = getSetterFor(name, value.getClass());
        if (m == null) {
            return;
        }
        try {
            m.invoke(this, value);
        } catch (IllegalAccessException e) {
            getLogger().warning(e);
        } catch (InvocationTargetException e2) {
            getLogger().warning(e2);
        }
    }

    @Override // org.apache.xmlbeans.impl.jam.annotation.AnnotationProxy
    public JAnnotationValue[] getValues() {
        if (this.mValues == null) {
            return new JAnnotationValue[0];
        }
        JAnnotationValue[] out = new JAnnotationValue[this.mValues.size()];
        this.mValues.toArray(out);
        return out;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected Method getSetterFor(String memberName, Class valueType) {
        try {
            return getClass().getMethod("set" + memberName, valueType);
        } catch (NoSuchMethodException nsme) {
            getLogger().warning(nsme);
            return null;
        }
    }
}
