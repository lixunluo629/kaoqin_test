package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.Map;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.AnnotatedElement;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/NotAnnotationTypePattern.class */
public class NotAnnotationTypePattern extends AnnotationTypePattern {
    AnnotationTypePattern negatedPattern;

    public NotAnnotationTypePattern(AnnotationTypePattern pattern) {
        this.negatedPattern = pattern;
        setLocation(pattern.getSourceContext(), pattern.getStart(), pattern.getEnd());
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public FuzzyBoolean matches(AnnotatedElement annotated) {
        return this.negatedPattern.matches(annotated).not();
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public FuzzyBoolean matches(AnnotatedElement annotated, ResolvedType[] parameterAnnotations) {
        return this.negatedPattern.matches(annotated, parameterAnnotations).not();
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public void resolve(World world) {
        this.negatedPattern.resolve(world);
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public AnnotationTypePattern resolveBindings(IScope scope, Bindings bindings, boolean allowBinding) {
        this.negatedPattern = this.negatedPattern.resolveBindings(scope, bindings, allowBinding);
        return this;
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public AnnotationTypePattern parameterizeWith(Map<String, UnresolvedType> typeVariableMap, World w) {
        AnnotationTypePattern newNegatedPattern = this.negatedPattern.parameterizeWith(typeVariableMap, w);
        NotAnnotationTypePattern ret = new NotAnnotationTypePattern(newNegatedPattern);
        ret.copyLocationFrom(this);
        if (isForParameterAnnotationMatch()) {
            ret.setForParameterAnnotationMatch();
        }
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(3);
        this.negatedPattern.write(s);
        writeLocation(s);
        s.writeBoolean(isForParameterAnnotationMatch());
    }

    public static AnnotationTypePattern read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        AnnotationTypePattern ret = new NotAnnotationTypePattern(AnnotationTypePattern.read(s, context));
        ret.readLocation(context, s);
        if (s.getMajorVersion() >= 4 && s.readBoolean()) {
            ret.setForParameterAnnotationMatch();
        }
        return ret;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof NotAnnotationTypePattern)) {
            return false;
        }
        NotAnnotationTypePattern other = (NotAnnotationTypePattern) obj;
        return other.negatedPattern.equals(this.negatedPattern) && other.isForParameterAnnotationMatch() == isForParameterAnnotationMatch();
    }

    public int hashCode() {
        int result = 17 + (37 * this.negatedPattern.hashCode());
        return (37 * result) + (isForParameterAnnotationMatch() ? 0 : 1);
    }

    public String toString() {
        return "!" + this.negatedPattern.toString();
    }

    public AnnotationTypePattern getNegatedPattern() {
        return this.negatedPattern;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object traverse(PatternNodeVisitor visitor, Object data) {
        Object ret = accept(visitor, data);
        this.negatedPattern.traverse(visitor, ret);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public void setForParameterAnnotationMatch() {
        this.negatedPattern.setForParameterAnnotationMatch();
    }
}
