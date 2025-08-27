package org.apache.xmlbeans.impl.jam.internal.elements;

import org.apache.xmlbeans.impl.jam.JElement;
import org.apache.xmlbeans.impl.jam.JPackage;
import org.apache.xmlbeans.impl.jam.JProperty;
import org.apache.xmlbeans.impl.jam.JSourcePosition;
import org.apache.xmlbeans.impl.jam.JamClassLoader;
import org.apache.xmlbeans.impl.jam.internal.JamServiceContextImpl;
import org.apache.xmlbeans.impl.jam.mutable.MElement;
import org.apache.xmlbeans.impl.jam.mutable.MSourcePosition;
import org.apache.xmlbeans.impl.jam.provider.JamLogger;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/elements/ElementImpl.class */
public abstract class ElementImpl implements Comparable, MElement {
    public static final ElementImpl[] NO_NODE = new ElementImpl[0];
    public static final ClassImpl[] NO_CLASS = new ClassImpl[0];
    public static final FieldImpl[] NO_FIELD = new FieldImpl[0];
    public static final ConstructorImpl[] NO_CONSTRUCTOR = new ConstructorImpl[0];
    public static final MethodImpl[] NO_METHOD = new MethodImpl[0];
    public static final ParameterImpl[] NO_PARAMETER = new ParameterImpl[0];
    public static final JPackage[] NO_PACKAGE = new JPackage[0];
    public static final AnnotationImpl[] NO_ANNOTATION = new AnnotationImpl[0];
    public static final CommentImpl[] NO_COMMENT = new CommentImpl[0];
    public static final JProperty[] NO_PROPERTY = new JProperty[0];
    private ElementContext mContext;
    protected String mSimpleName;
    private MSourcePosition mPosition = null;
    private Object mArtifact = null;
    private ElementImpl mParent;

    protected ElementImpl(ElementImpl parent) {
        if (parent == null) {
            throw new IllegalArgumentException("null ctx");
        }
        if (parent == this) {
            throw new IllegalArgumentException("An element cannot be its own parent");
        }
        JElement parent2 = parent.getParent();
        while (true) {
            JElement check = parent2;
            if (check != null) {
                if (check == this) {
                    throw new IllegalArgumentException("cycle detected");
                }
                parent2 = check.getParent();
            } else {
                this.mContext = parent.getContext();
                this.mParent = parent;
                return;
            }
        }
    }

    protected ElementImpl(ElementContext ctx) {
        if (ctx == null) {
            throw new IllegalArgumentException("null ctx");
        }
        this.mContext = ctx;
    }

    @Override // org.apache.xmlbeans.impl.jam.JElement
    public final JElement getParent() {
        return this.mParent;
    }

    public String getSimpleName() {
        return this.mSimpleName;
    }

    public JSourcePosition getSourcePosition() {
        return this.mPosition;
    }

    @Override // org.apache.xmlbeans.impl.jam.JElement
    public Object getArtifact() {
        return this.mArtifact;
    }

    public void setSimpleName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("null name");
        }
        this.mSimpleName = name.trim();
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MElement
    public MSourcePosition createSourcePosition() {
        SourcePositionImpl sourcePositionImpl = new SourcePositionImpl();
        this.mPosition = sourcePositionImpl;
        return sourcePositionImpl;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MElement
    public void removeSourcePosition() {
        this.mPosition = null;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MElement
    public MSourcePosition getMutableSourcePosition() {
        return this.mPosition;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MElement
    public void setArtifact(Object artifact) {
        if (artifact == null) {
        }
        if (this.mArtifact != null) {
            throw new IllegalStateException("artifact already set");
        }
        this.mArtifact = artifact;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MElement
    public JamClassLoader getClassLoader() {
        return this.mContext.getClassLoader();
    }

    public static String defaultName(int count) {
        return "unnamed_" + count;
    }

    public boolean equals(Object o) {
        String oqn;
        if (this == o) {
            return true;
        }
        if (!(o instanceof ElementImpl)) {
            return false;
        }
        ElementImpl eElement = (ElementImpl) o;
        String qn = getQualifiedName();
        if (qn == null || (oqn = eElement.getQualifiedName()) == null) {
            return false;
        }
        return qn.equals(oqn);
    }

    public int hashCode() {
        String qn = getQualifiedName();
        if (qn == null) {
            return 0;
        }
        return qn.hashCode();
    }

    public ElementContext getContext() {
        return this.mContext;
    }

    @Override // org.apache.xmlbeans.impl.jam.JElement
    public String toString() {
        return getQualifiedName();
    }

    protected JamLogger getLogger() {
        return ((JamServiceContextImpl) this.mContext).getLogger();
    }

    @Override // java.lang.Comparable
    public int compareTo(Object o) {
        if (!(o instanceof JElement)) {
            return -1;
        }
        return getQualifiedName().compareTo(((JElement) o).getQualifiedName());
    }
}
