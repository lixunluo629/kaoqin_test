package org.apache.xmlbeans.impl.jam.internal.elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.xmlbeans.impl.jam.JAnnotation;
import org.apache.xmlbeans.impl.jam.JAnnotationValue;
import org.apache.xmlbeans.impl.jam.JComment;
import org.apache.xmlbeans.impl.jam.annotation.AnnotationProxy;
import org.apache.xmlbeans.impl.jam.mutable.MAnnotatedElement;
import org.apache.xmlbeans.impl.jam.mutable.MAnnotation;
import org.apache.xmlbeans.impl.jam.mutable.MComment;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/elements/AnnotatedElementImpl.class */
public abstract class AnnotatedElementImpl extends ElementImpl implements MAnnotatedElement {
    private Map mName2Annotation;
    private MComment mComment;
    private List mAllAnnotations;

    protected AnnotatedElementImpl(ElementContext ctx) {
        super(ctx);
        this.mName2Annotation = null;
        this.mComment = null;
        this.mAllAnnotations = null;
    }

    protected AnnotatedElementImpl(ElementImpl parent) {
        super(parent);
        this.mName2Annotation = null;
        this.mComment = null;
        this.mAllAnnotations = null;
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotatedElement
    public JAnnotation[] getAnnotations() {
        return getMutableAnnotations();
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotatedElement
    public JAnnotation getAnnotation(Class proxyClass) {
        return getMutableAnnotation(proxyClass.getName());
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotatedElement
    public JAnnotation getAnnotation(String named) {
        return getMutableAnnotation(named);
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotatedElement
    public JAnnotationValue getAnnotationValue(String valueId) {
        if (this.mName2Annotation == null) {
            return null;
        }
        String valueId2 = valueId.trim();
        int delim = valueId2.indexOf(64);
        if (delim == -1 || delim == valueId2.length() - 1) {
            JAnnotation ann = getAnnotation(valueId2);
            if (ann == null) {
                return null;
            }
            return ann.getValue("value");
        }
        JAnnotation ann2 = getAnnotation(valueId2.substring(0, delim));
        if (ann2 == null) {
            return null;
        }
        return ann2.getValue(valueId2.substring(delim + 1));
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotatedElement
    public Object getAnnotationProxy(Class proxyClass) {
        return getEditableProxy(proxyClass);
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotatedElement
    public JComment getComment() {
        return getMutableComment();
    }

    @Override // org.apache.xmlbeans.impl.jam.JAnnotatedElement
    public JAnnotation[] getAllJavadocTags() {
        if (this.mAllAnnotations == null) {
            return NO_ANNOTATION;
        }
        JAnnotation[] out = new JAnnotation[this.mAllAnnotations.size()];
        this.mAllAnnotations.toArray(out);
        return out;
    }

    public AnnotationProxy getEditableProxy(Class proxyClass) {
        MAnnotation out;
        if (this.mName2Annotation == null || (out = getMutableAnnotation(proxyClass.getName())) == null) {
            return null;
        }
        return (AnnotationProxy) out.getProxy();
    }

    public void removeAnnotation(MAnnotation ann) {
        if (this.mName2Annotation != null) {
            this.mName2Annotation.values().remove(ann);
        }
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MAnnotatedElement
    public MAnnotation[] getMutableAnnotations() {
        if (this.mName2Annotation == null) {
            return new MAnnotation[0];
        }
        MAnnotation[] out = new MAnnotation[this.mName2Annotation.values().size()];
        this.mName2Annotation.values().toArray(out);
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MAnnotatedElement
    public MAnnotation getMutableAnnotation(String named) {
        if (this.mName2Annotation == null) {
            return null;
        }
        return (MAnnotation) this.mName2Annotation.get(named.trim());
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MAnnotatedElement
    public MAnnotation findOrCreateAnnotation(String annotationName) {
        MAnnotation ann = getMutableAnnotation(annotationName);
        if (ann != null) {
            return ann;
        }
        AnnotationProxy proxy = getContext().createAnnotationProxy(annotationName);
        AnnotationImpl annotationImpl = new AnnotationImpl(getContext(), proxy, annotationName);
        if (this.mName2Annotation == null) {
            this.mName2Annotation = new HashMap();
        }
        this.mName2Annotation.put(annotationImpl.getQualifiedName(), annotationImpl);
        return annotationImpl;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MAnnotatedElement
    public MAnnotation addLiteralAnnotation(String annName) {
        if (annName == null) {
            throw new IllegalArgumentException("null tagname");
        }
        String annName2 = annName.trim();
        AnnotationProxy proxy = getContext().createAnnotationProxy(annName2);
        AnnotationImpl annotationImpl = new AnnotationImpl(getContext(), proxy, annName2);
        if (this.mAllAnnotations == null) {
            this.mAllAnnotations = new ArrayList();
        }
        this.mAllAnnotations.add(annotationImpl);
        if (getMutableAnnotation(annName2) == null) {
            if (this.mName2Annotation == null) {
                this.mName2Annotation = new HashMap();
            }
            this.mName2Annotation.put(annName2, annotationImpl);
        }
        return annotationImpl;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MAnnotatedElement
    public MComment getMutableComment() {
        return this.mComment;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MAnnotatedElement
    public MComment createComment() {
        CommentImpl commentImpl = new CommentImpl(this);
        this.mComment = commentImpl;
        return commentImpl;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MAnnotatedElement
    public void removeComment() {
        this.mComment = null;
    }

    protected void addAnnotation(JAnnotation ann) {
        if (this.mName2Annotation == null) {
            this.mName2Annotation = new HashMap();
            this.mName2Annotation.put(ann.getQualifiedName(), ann);
        } else if (this.mName2Annotation.get(ann.getQualifiedName()) == null) {
            this.mName2Annotation.put(ann.getQualifiedName(), ann);
        }
        if (this.mAllAnnotations == null) {
            this.mAllAnnotations = new ArrayList();
        }
        this.mAllAnnotations.add(ann);
    }

    public MAnnotation addAnnotationForProxy(Class proxyClass, AnnotationProxy proxy) {
        String annotationName = proxyClass.getName();
        MAnnotation ann = getMutableAnnotation(annotationName);
        if (ann != null) {
            return ann;
        }
        AnnotationImpl annotationImpl = new AnnotationImpl(getContext(), proxy, annotationName);
        if (this.mName2Annotation == null) {
            this.mName2Annotation = new HashMap();
        }
        this.mName2Annotation.put(annotationImpl.getQualifiedName(), annotationImpl);
        return annotationImpl;
    }
}
