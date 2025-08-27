package org.aspectj.weaver.patterns;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
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

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/AndAnnotationTypePattern.class */
public class AndAnnotationTypePattern extends AnnotationTypePattern {
    private AnnotationTypePattern left;
    private AnnotationTypePattern right;

    public AndAnnotationTypePattern(AnnotationTypePattern left, AnnotationTypePattern right) {
        this.left = left;
        this.right = right;
        setLocation(left.getSourceContext(), left.getStart(), right.getEnd());
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public FuzzyBoolean matches(AnnotatedElement annotated) {
        return this.left.matches(annotated).and(this.right.matches(annotated));
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public FuzzyBoolean matches(AnnotatedElement annotated, ResolvedType[] parameterAnnotations) {
        return this.left.matches(annotated, parameterAnnotations).and(this.right.matches(annotated, parameterAnnotations));
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public void resolve(World world) {
        this.left.resolve(world);
        this.right.resolve(world);
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public AnnotationTypePattern resolveBindings(IScope scope, Bindings bindings, boolean allowBinding) {
        this.left = this.left.resolveBindings(scope, bindings, allowBinding);
        this.right = this.right.resolveBindings(scope, bindings, allowBinding);
        return this;
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public AnnotationTypePattern parameterizeWith(Map<String, UnresolvedType> typeVariableMap, World w) {
        AnnotationTypePattern newLeft = this.left.parameterizeWith(typeVariableMap, w);
        AnnotationTypePattern newRight = this.right.parameterizeWith(typeVariableMap, w);
        AndAnnotationTypePattern ret = new AndAnnotationTypePattern(newLeft, newRight);
        ret.copyLocationFrom(this);
        if (isForParameterAnnotationMatch()) {
            ret.setForParameterAnnotationMatch();
        }
        return ret;
    }

    public static AnnotationTypePattern read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        AnnotationTypePattern p = new AndAnnotationTypePattern(AnnotationTypePattern.read(s, context), AnnotationTypePattern.read(s, context));
        p.readLocation(context, s);
        if (s.getMajorVersion() >= 4 && s.readBoolean()) {
            p.setForParameterAnnotationMatch();
        }
        return p;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(5);
        this.left.write(s);
        this.right.write(s);
        writeLocation(s);
        s.writeBoolean(isForParameterAnnotationMatch());
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof AndAnnotationTypePattern)) {
            return false;
        }
        AndAnnotationTypePattern other = (AndAnnotationTypePattern) obj;
        return this.left.equals(other.left) && this.right.equals(other.right) && this.left.isForParameterAnnotationMatch() == this.right.isForParameterAnnotationMatch();
    }

    public int hashCode() {
        int result = (17 * 37) + this.left.hashCode();
        return (((result * 37) + this.right.hashCode()) * 37) + (isForParameterAnnotationMatch() ? 0 : 1);
    }

    public String toString() {
        return this.left.toString() + SymbolConstants.SPACE_SYMBOL + this.right.toString();
    }

    public AnnotationTypePattern getLeft() {
        return this.left;
    }

    public AnnotationTypePattern getRight() {
        return this.right;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object traverse(PatternNodeVisitor visitor, Object data) {
        Object ret = accept(visitor, data);
        this.left.traverse(visitor, ret);
        this.right.traverse(visitor, ret);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.AnnotationTypePattern
    public void setForParameterAnnotationMatch() {
        this.left.setForParameterAnnotationMatch();
        this.right.setForParameterAnnotationMatch();
    }
}
