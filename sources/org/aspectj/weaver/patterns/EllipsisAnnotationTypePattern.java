package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.Map;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.AnnotatedElement;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.World;

/* compiled from: AnnotationTypePattern.java */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/EllipsisAnnotationTypePattern.class */
class EllipsisAnnotationTypePattern extends AnnotationTypePattern {
    EllipsisAnnotationTypePattern() {
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public FuzzyBoolean matches(AnnotatedElement annotated) {
        return FuzzyBoolean.NO;
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public FuzzyBoolean matches(AnnotatedElement annotated, ResolvedType[] parameterAnnotations) {
        return FuzzyBoolean.NO;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(6);
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public void resolve(World world) {
    }

    public String toString() {
        return "..";
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public AnnotationTypePattern parameterizeWith(Map arg0, World w) {
        return this;
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public void setForParameterAnnotationMatch() {
    }
}
