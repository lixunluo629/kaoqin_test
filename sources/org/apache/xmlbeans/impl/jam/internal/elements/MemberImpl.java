package org.apache.xmlbeans.impl.jam.internal.elements;

import java.lang.reflect.Modifier;
import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.JElement;
import org.apache.xmlbeans.impl.jam.JMember;
import org.apache.xmlbeans.impl.jam.mutable.MMember;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/elements/MemberImpl.class */
public abstract class MemberImpl extends AnnotatedElementImpl implements MMember {
    private int mModifiers;

    protected MemberImpl(ElementImpl parent) {
        super(parent);
        this.mModifiers = 0;
    }

    protected MemberImpl(ElementContext ctx) {
        super(ctx);
        this.mModifiers = 0;
    }

    @Override // org.apache.xmlbeans.impl.jam.JMember
    public JClass getContainingClass() {
        JElement p = getParent();
        if (p instanceof JClass) {
            return (JClass) p;
        }
        if (p instanceof JMember) {
            return ((JMember) p).getContainingClass();
        }
        return null;
    }

    public int getModifiers() {
        return this.mModifiers;
    }

    @Override // org.apache.xmlbeans.impl.jam.JMember
    public boolean isPackagePrivate() {
        return (isPrivate() || isPublic() || isProtected()) ? false : true;
    }

    @Override // org.apache.xmlbeans.impl.jam.JMember
    public boolean isPrivate() {
        return Modifier.isPrivate(getModifiers());
    }

    @Override // org.apache.xmlbeans.impl.jam.JMember
    public boolean isProtected() {
        return Modifier.isProtected(getModifiers());
    }

    @Override // org.apache.xmlbeans.impl.jam.JMember
    public boolean isPublic() {
        return Modifier.isPublic(getModifiers());
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MMember
    public void setModifiers(int modifiers) {
        this.mModifiers = modifiers;
    }
}
