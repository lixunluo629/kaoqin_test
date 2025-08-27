package org.aspectj.weaver.patterns;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/HasMemberTypePatternFinder.class */
public class HasMemberTypePatternFinder extends AbstractPatternNodeVisitor {
    private boolean hasMemberTypePattern = false;

    public HasMemberTypePatternFinder(TypePattern aPattern) {
        aPattern.traverse(this, null);
    }

    @Override // org.aspectj.weaver.patterns.AbstractPatternNodeVisitor, org.aspectj.weaver.patterns.PatternNodeVisitor
    public Object visit(HasMemberTypePattern node, Object data) {
        this.hasMemberTypePattern = true;
        return null;
    }

    public boolean hasMemberTypePattern() {
        return this.hasMemberTypePattern;
    }
}
