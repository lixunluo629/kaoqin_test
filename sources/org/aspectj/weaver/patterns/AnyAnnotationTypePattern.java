package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.Map;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.AnnotatedElement;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/AnyAnnotationTypePattern.class */
public class AnyAnnotationTypePattern extends AnnotationTypePattern {
    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public FuzzyBoolean fastMatches(AnnotatedElement annotated) {
        return FuzzyBoolean.YES;
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public FuzzyBoolean matches(AnnotatedElement annotated) {
        return FuzzyBoolean.YES;
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public FuzzyBoolean matches(AnnotatedElement annotated, ResolvedType[] parameterAnnotations) {
        return FuzzyBoolean.YES;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(7);
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public void resolve(World world) {
    }

    public String toString() {
        return "@ANY";
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public boolean isAny() {
        return true;
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public AnnotationTypePattern parameterizeWith(Map arg0, World w) {
        return this;
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public void setForParameterAnnotationMatch() {
    }
}
