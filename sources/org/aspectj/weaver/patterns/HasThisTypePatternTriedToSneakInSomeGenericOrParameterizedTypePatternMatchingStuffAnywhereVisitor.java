package org.aspectj.weaver.patterns;

import org.aspectj.weaver.UnresolvedType;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor.class */
public class HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor extends AbstractPatternNodeVisitor {
    boolean ohYesItHas = false;

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(ExactTypePattern node, Object data) {
        UnresolvedType theExactType = node.getExactType();
        if (theExactType.isParameterizedType()) {
            this.ohYesItHas = true;
        }
        return data;
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(WildTypePattern node, Object data) {
        if (node.getUpperBound() != null) {
            this.ohYesItHas = true;
        }
        if (node.getLowerBound() != null) {
            this.ohYesItHas = true;
        }
        if (node.getTypeParameters().size() != 0) {
            this.ohYesItHas = true;
        }
        return data;
    }

    public boolean wellHasItThen() {
        return this.ohYesItHas;
    }
}
