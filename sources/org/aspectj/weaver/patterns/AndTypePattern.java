package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.Map;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/AndTypePattern.class */
public class AndTypePattern extends TypePattern {
    private TypePattern left;
    private TypePattern right;

    public AndTypePattern(TypePattern left, TypePattern right) {
        super(false, false);
        this.left = left;
        this.right = right;
        setLocation(left.getSourceContext(), left.getStart(), right.getEnd());
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean couldEverMatchSameTypesAs(TypePattern other) {
        return true;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public FuzzyBoolean matchesInstanceof(ResolvedType type) {
        return this.left.matchesInstanceof(type).and(this.right.matchesInstanceof(type));
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean matchesExactly(ResolvedType type) {
        return this.left.matchesExactly(type) && this.right.matchesExactly(type);
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean matchesExactly(ResolvedType type, ResolvedType annotatedType) {
        return this.left.matchesExactly(type, annotatedType) && this.right.matchesExactly(type, annotatedType);
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public boolean matchesStatically(ResolvedType type) {
        return this.left.matchesStatically(type) && this.right.matchesStatically(type);
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public void setIsVarArgs(boolean isVarArgs) {
        this.isVarArgs = isVarArgs;
        this.left.setIsVarArgs(isVarArgs);
        this.right.setIsVarArgs(isVarArgs);
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public void setAnnotationTypePattern(AnnotationTypePattern annPatt) {
        if (annPatt == AnnotationTypePattern.ANY) {
            return;
        }
        if (this.left.annotationPattern == AnnotationTypePattern.ANY) {
            this.left.setAnnotationTypePattern(annPatt);
        } else {
            this.left.setAnnotationTypePattern(new AndAnnotationTypePattern(this.left.annotationPattern, annPatt));
        }
        if (this.right.annotationPattern == AnnotationTypePattern.ANY) {
            this.right.setAnnotationTypePattern(annPatt);
        } else {
            this.right.setAnnotationTypePattern(new AndAnnotationTypePattern(this.right.annotationPattern, annPatt));
        }
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(8);
        this.left.write(s);
        this.right.write(s);
        writeLocation(s);
    }

    public static TypePattern read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        AndTypePattern ret = new AndTypePattern(TypePattern.read(s, context), TypePattern.read(s, context));
        ret.readLocation(context, s);
        if (ret.left.isVarArgs && ret.right.isVarArgs) {
            ret.isVarArgs = true;
        }
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public TypePattern resolveBindings(IScope scope, Bindings bindings, boolean allowBinding, boolean requireExactType) {
        if (requireExactType) {
            return notExactType(scope);
        }
        this.left = this.left.resolveBindings(scope, bindings, false, false);
        this.right = this.right.resolveBindings(scope, bindings, false, false);
        return this;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public TypePattern parameterizeWith(Map typeVariableMap, World w) {
        TypePattern newLeft = this.left.parameterizeWith(typeVariableMap, w);
        TypePattern newRight = this.right.parameterizeWith(typeVariableMap, w);
        AndTypePattern ret = new AndTypePattern(newLeft, newRight);
        ret.copyLocationFrom(this);
        return ret;
    }

    public String toString() {
        StringBuffer buff = new StringBuffer();
        if (this.annotationPattern != AnnotationTypePattern.ANY) {
            buff.append('(');
            buff.append(this.annotationPattern.toString());
            buff.append(' ');
        }
        buff.append('(');
        buff.append(this.left.toString());
        buff.append(" && ");
        buff.append(this.right.toString());
        buff.append(')');
        if (this.annotationPattern != AnnotationTypePattern.ANY) {
            buff.append(')');
        }
        return buff.toString();
    }

    public TypePattern getLeft() {
        return this.left;
    }

    public TypePattern getRight() {
        return this.right;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof AndTypePattern)) {
            return false;
        }
        AndTypePattern atp = (AndTypePattern) obj;
        return this.left.equals(atp.left) && this.right.equals(atp.right);
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public boolean isStarAnnotation() {
        return this.left.isStarAnnotation() && this.right.isStarAnnotation();
    }

    public int hashCode() {
        int ret = 17 + (37 * this.left.hashCode());
        return ret + (37 * this.right.hashCode());
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
}
