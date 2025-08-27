package org.apache.xmlbeans.impl.jam.internal.elements;

import org.apache.xmlbeans.impl.jam.JAnnotationValue;
import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.annotation.AnnotationProxy;
import org.apache.xmlbeans.impl.jam.mutable.MAnnotation;
import org.apache.xmlbeans.impl.jam.visitor.JVisitor;
import org.apache.xmlbeans.impl.jam.visitor.MVisitor;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/elements/AnnotationImpl.class */
public final class AnnotationImpl extends ElementImpl implements MAnnotation {
    private AnnotationProxy mProxy;
    private Object mAnnotationInstance;
    private String mQualifiedName;

    AnnotationImpl(ElementContext ctx, AnnotationProxy proxy, String qualifiedName) {
        super(ctx);
        this.mAnnotationInstance = null;
        this.mQualifiedName = null;
        if (proxy == null) {
            throw new IllegalArgumentException("null proxy");
        }
        if (qualifiedName == null) {
            throw new IllegalArgumentException("null qn");
        }
        this.mProxy = proxy;
        setSimpleName(qualifiedName.substring(qualifiedName.lastIndexOf(46) + 1));
        this.mQualifiedName = qualifiedName;
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotation
    public Object getProxy() {
        return this.mProxy;
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotation
    public JAnnotationValue[] getValues() {
        return this.mProxy.getValues();
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotation
    public JAnnotationValue getValue(String name) {
        return this.mProxy.getValue(name);
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotation
    public Object getAnnotationInstance() {
        return this.mAnnotationInstance;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MAnnotation
    public void setAnnotationInstance(Object o) {
        this.mAnnotationInstance = o;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MAnnotation
    public void setSimpleValue(String name, Object value, JClass type) {
        if (name == null) {
            throw new IllegalArgumentException("null name");
        }
        if (type == null) {
            throw new IllegalArgumentException("null type");
        }
        if (value == null) {
            throw new IllegalArgumentException("null value");
        }
        this.mProxy.setValue(name, value, type);
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MAnnotation
    public MAnnotation createNestedValue(String name, String annTypeName) {
        if (name == null) {
            throw new IllegalArgumentException("null name");
        }
        if (annTypeName == null) {
            throw new IllegalArgumentException("null typename");
        }
        AnnotationProxy p = getContext().createAnnotationProxy(annTypeName);
        AnnotationImpl out = new AnnotationImpl(getContext(), p, annTypeName);
        JClass type = getContext().getClassLoader().loadClass(annTypeName);
        this.mProxy.setValue(name, out, type);
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MAnnotation
    public MAnnotation[] createNestedValueArray(String name, String annComponentTypeName, int dimensions) {
        if (name == null) {
            throw new IllegalArgumentException("null name");
        }
        if (annComponentTypeName == null) {
            throw new IllegalArgumentException("null typename");
        }
        if (dimensions < 0) {
            throw new IllegalArgumentException("dimensions = " + dimensions);
        }
        MAnnotation[] out = new MAnnotation[dimensions];
        for (int i = 0; i < out.length; i++) {
            AnnotationProxy p = getContext().createAnnotationProxy(annComponentTypeName);
            out[i] = new AnnotationImpl(getContext(), p, annComponentTypeName);
        }
        JClass type = getContext().getClassLoader().loadClass("[L" + annComponentTypeName + ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
        this.mProxy.setValue(name, out, type);
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.JElement
    public String getQualifiedName() {
        return this.mQualifiedName;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MElement
    public void accept(MVisitor visitor) {
        visitor.visit(this);
    }

    @Override // org.apache.xmlbeans.impl.jam.JElement
    public void accept(JVisitor visitor) {
        visitor.visit(this);
    }
}
