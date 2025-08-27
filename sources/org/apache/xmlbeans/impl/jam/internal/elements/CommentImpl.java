package org.apache.xmlbeans.impl.jam.internal.elements;

import org.apache.xmlbeans.impl.jam.mutable.MComment;
import org.apache.xmlbeans.impl.jam.visitor.JVisitor;
import org.apache.xmlbeans.impl.jam.visitor.MVisitor;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/elements/CommentImpl.class */
public final class CommentImpl extends ElementImpl implements MComment {
    private String mText;

    CommentImpl(ElementImpl parent) {
        super(parent);
        this.mText = null;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MComment
    public void setText(String text) {
        this.mText = text;
    }

    @Override // org.apache.xmlbeans.impl.jam.JComment
    public String getText() {
        return this.mText == null ? "" : this.mText;
    }

    @Override // org.apache.xmlbeans.impl.jam.mutable.MElement
    public void accept(MVisitor visitor) {
        visitor.visit(this);
    }

    @Override // org.apache.xmlbeans.impl.jam.JElement
    public void accept(JVisitor visitor) {
        visitor.visit(this);
    }

    @Override // org.apache.xmlbeans.impl.jam.JElement
    public String getQualifiedName() {
        return getParent().getQualifiedName() + ".{comment}";
    }
}
