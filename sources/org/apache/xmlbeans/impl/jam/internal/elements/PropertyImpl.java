package org.apache.xmlbeans.impl.jam.internal.elements;

import org.apache.xmlbeans.impl.jam.JAnnotation;
import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.JComment;
import org.apache.xmlbeans.impl.jam.JMethod;
import org.apache.xmlbeans.impl.jam.JProperty;
import org.apache.xmlbeans.impl.jam.JSourcePosition;
import org.apache.xmlbeans.impl.jam.internal.classrefs.JClassRef;
import org.apache.xmlbeans.impl.jam.internal.classrefs.QualifiedJClassRef;
import org.apache.xmlbeans.impl.jam.mutable.MMethod;
import org.apache.xmlbeans.impl.jam.visitor.JVisitor;
import org.apache.xmlbeans.impl.jam.visitor.MVisitor;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/elements/PropertyImpl.class */
public class PropertyImpl extends AnnotatedElementImpl implements JProperty {
    private String mName;
    private JMethod mGetter;
    private JMethod mSetter;
    private JClassRef mTypeRef;

    public PropertyImpl(String name, JMethod getter, JMethod setter, String qualifiedTypeName) {
        super((ElementImpl) (getter != null ? getter.getParent() : setter.getParent()));
        this.mName = name;
        this.mGetter = getter;
        this.mSetter = setter;
        ClassImpl cont = (ClassImpl) (getter != null ? getter.getContainingClass() : setter.getContainingClass());
        this.mTypeRef = QualifiedJClassRef.create(qualifiedTypeName, cont);
        initAnnotations();
    }

    @Override // org.apache.xmlbeans.impl.jam.JProperty
    public JClass getType() {
        return this.mTypeRef.getRefClass();
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.ElementImpl, org.apache.xmlbeans.impl.jam.JElement
    public String getSimpleName() {
        return this.mName;
    }

    @Override // org.apache.xmlbeans.impl.jam.JElement
    public String getQualifiedName() {
        return getParent().getQualifiedName() + "." + getSimpleName();
    }

    @Override // org.apache.xmlbeans.impl.jam.JProperty
    public JMethod getSetter() {
        return this.mSetter;
    }

    @Override // org.apache.xmlbeans.impl.jam.JProperty
    public JMethod getGetter() {
        return this.mGetter;
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.AnnotatedElementImpl, org.apache.xmlbeans.impl.jam.JAnnotatedElement
    public JAnnotation[] getAnnotations() {
        return combine(this.mGetter == null ? ElementImpl.NO_ANNOTATION : this.mGetter.getAnnotations(), this.mSetter == null ? ElementImpl.NO_ANNOTATION : this.mSetter.getAnnotations());
    }

    public void setSetter(JMethod method) {
        this.mSetter = method;
    }

    public void setGetter(JMethod method) {
        this.mGetter = method;
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.AnnotatedElementImpl, org.apache.xmlbeans.impl.jam.JAnnotatedElement
    public JAnnotation getAnnotation(String named) {
        JAnnotation out = this.mGetter != null ? this.mGetter.getAnnotation(named) : null;
        if (out != null) {
            return out;
        }
        if (this.mSetter != null) {
            return this.mSetter.getAnnotation(named);
        }
        return null;
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.AnnotatedElementImpl, org.apache.xmlbeans.impl.jam.JAnnotatedElement
    public JComment getComment() {
        if (this.mGetter != null) {
            return this.mGetter.getComment();
        }
        if (this.mSetter != null) {
            return this.mSetter.getComment();
        }
        return null;
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.ElementImpl, org.apache.xmlbeans.impl.jam.JElement
    public JSourcePosition getSourcePosition() {
        return this.mGetter != null ? this.mGetter.getSourcePosition() : this.mSetter.getSourcePosition();
    }

    @Override // org.apache.xmlbeans.impl.jam.JElement
    public void accept(JVisitor visitor) {
        if (this.mGetter != null) {
            visitor.visit(this.mGetter);
        }
        if (this.mSetter != null) {
            visitor.visit(this.mSetter);
        }
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.ElementImpl, org.apache.xmlbeans.impl.jam.JElement
    public String toString() {
        return getQualifiedName();
    }

    private void initAnnotations() {
        if (this.mSetter != null) {
            for (JAnnotation jAnnotation : this.mSetter.getAnnotations()) {
                super.addAnnotation(jAnnotation);
            }
            for (JAnnotation jAnnotation2 : this.mSetter.getAllJavadocTags()) {
                super.addAnnotation(jAnnotation2);
            }
        }
        if (this.mGetter != null) {
            for (JAnnotation jAnnotation3 : this.mGetter.getAnnotations()) {
                super.addAnnotation(jAnnotation3);
            }
            JAnnotation[] anns = this.mGetter.getAllJavadocTags();
            for (JAnnotation jAnnotation4 : anns) {
                super.addAnnotation(jAnnotation4);
            }
        }
    }

    private JAnnotation[] combine(JAnnotation[] a, JAnnotation[] b) {
        if (a.length == 0) {
            return b;
        }
        if (b.length == 0) {
            return a;
        }
        JAnnotation[] out = new JAnnotation[a.length + b.length];
        System.arraycopy(a, 0, out, 0, a.length);
        System.arraycopy(b, 0, out, a.length, b.length);
        return out;
    }

    private JComment[] combine(JComment[] a, JComment[] b) {
        if (a.length == 0) {
            return b;
        }
        if (b.length == 0) {
            return a;
        }
        JComment[] out = new JComment[a.length + b.length];
        System.arraycopy(a, 0, out, 0, a.length);
        System.arraycopy(b, 0, out, a.length, b.length);
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MElement
    public void accept(MVisitor visitor) {
        if (this.mGetter != null) {
            visitor.visit((MMethod) this.mGetter);
        }
        if (this.mSetter != null) {
            visitor.visit((MMethod) this.mSetter);
        }
    }
}
