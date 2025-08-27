package org.aspectj.weaver.bcel;

import org.aspectj.weaver.patterns.AbstractPatternNodeVisitor;
import org.aspectj.weaver.patterns.AndPointcut;
import org.aspectj.weaver.patterns.IfPointcut;
import org.aspectj.weaver.patterns.NotPointcut;
import org.aspectj.weaver.patterns.OrPointcut;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/IfFinder.class */
class IfFinder extends AbstractPatternNodeVisitor {
    boolean hasIf = false;

    IfFinder() {
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(IfPointcut node, Object data) {
        if (!node.alwaysFalse() && !node.alwaysTrue()) {
            this.hasIf = true;
        }
        return node;
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(AndPointcut node, Object data) {
        if (!this.hasIf) {
            node.getLeft().accept(this, data);
        }
        if (!this.hasIf) {
            node.getRight().accept(this, data);
        }
        return node;
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(NotPointcut node, Object data) {
        if (!this.hasIf) {
            node.getNegatedPointcut().accept(this, data);
        }
        return node;
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(OrPointcut node, Object data) {
        if (!this.hasIf) {
            node.getLeft().accept(this, data);
        }
        if (!this.hasIf) {
            node.getRight().accept(this, data);
        }
        return node;
    }
}
