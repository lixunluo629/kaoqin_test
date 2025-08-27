package org.apache.xmlbeans.impl.jam.internal.elements;

import java.util.ArrayList;
import java.util.List;
import org.apache.xmlbeans.impl.jam.JClass;
import org.apache.xmlbeans.impl.jam.mutable.MClass;
import org.apache.xmlbeans.impl.jam.mutable.MPackage;
import org.apache.xmlbeans.impl.jam.visitor.JVisitor;
import org.apache.xmlbeans.impl.jam.visitor.MVisitor;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/elements/PackageImpl.class */
public class PackageImpl extends AnnotatedElementImpl implements MPackage {
    private List mRootClasses;
    private String mName;

    public PackageImpl(ElementContext ctx, String name) {
        super(ctx);
        this.mRootClasses = new ArrayList();
        this.mName = name;
        int lastDot = this.mName.lastIndexOf(46);
        setSimpleName(lastDot == -1 ? this.mName : this.mName.substring(lastDot + 1));
    }

    @Override // org.apache.xmlbeans.impl.jam.JElement
    public String getQualifiedName() {
        return this.mName;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MElement
    public void accept(MVisitor visitor) {
        visitor.visit(this);
    }

    @Override // org.apache.xmlbeans.impl.jam.JElement
    public void accept(JVisitor visitor) {
        visitor.visit(this);
    }

    @Override // org.apache.xmlbeans.impl.jam.JPackage
    public JClass[] getClasses() {
        JClass[] out = new JClass[this.mRootClasses.size()];
        this.mRootClasses.toArray(out);
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MPackage
    public MClass[] getMutableClasses() {
        MClass[] out = new MClass[this.mRootClasses.size()];
        this.mRootClasses.toArray(out);
        return out;
    }
}
